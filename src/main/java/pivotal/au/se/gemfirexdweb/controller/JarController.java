/*
Copyright (C) MARCH-2014 Pivotal Software, Inc.

All rights reserved. This program and the accompanying materials
are made available under the terms of the under the Apache License,
Version 2.0 (the "License”); you may not use this file except in compliance
with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package pivotal.au.se.gemfirexdweb.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStore;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStoreDAO;
import pivotal.au.se.gemfirexdweb.dao.jars.Jar;
import pivotal.au.se.gemfirexdweb.dao.jars.JarDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class JarController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/jars", method = RequestMethod.GET)
    public String showJars
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
    	if (session.getAttribute("user_key") == null)
    	{
    		logger.debug("user_key is null new Login required");
    		response.sendRedirect(request.getContextPath() + "/gemfirexdweb/login");
    		return null;
    	}
        else
        {
            Connection conn = AdminUtil.getConnection((String) session.getAttribute("user_key"));
            if (conn == null )
            {
                response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
                return null;
            }
            else
            {
                if (conn.isClosed())
                {
                    response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
                    return null;
                }
            }

        }
    	
    	logger.debug("Received request to show jars");

		int startAtIndex = 0, endAtIndex = 0;
		String schema = null; 
    	
    	JarDAO jarDAO = GemFireXDWebDAOFactory.getJarStoreDAO();
    	Result result = new Result();
    	
    	String jarAction = request.getParameter("jarAction");
   	
    	if (jarAction != null)
    	{
    		logger.debug("jarAction = " + jarAction);
            result = null;
            result =
                    jarDAO.simpleJarCommand
                            ((String)request.getParameter("schemaName"),
                             (String)request.getParameter("alias"),
                              jarAction,
                              (String)session.getAttribute("user_key"));

            model.addAttribute("result", result);

    	}
    	
    	List<Jar> jars = jarDAO.retrieveJarList
                ("",
                 null,
                 (String) session.getAttribute("user_key"));
    	
    	model.addAttribute("records", jars.size());
    	model.addAttribute("estimatedrecords", jars.size());
    	        
    	UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (jars.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("jars", jars);
        }
        else
        {
          if (request.getParameter("startAtIndex") != null)
          {
            startAtIndex = Integer.parseInt(request.getParameter("startAtIndex"));
          }
          
          if (request.getParameter("endAtIndex") != null)
          {
            endAtIndex = Integer.parseInt(request.getParameter("endAtIndex"));
            if (endAtIndex > jars.size())
            {
              endAtIndex = jars.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = jars.subList(startAtIndex, endAtIndex);
          model.addAttribute("jars", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
    	
    	// This will resolve to /WEB-INF/jsp/jars.jsp
    	return "jars";
    }

    @RequestMapping(value = "/jars", method = RequestMethod.POST)
    public String performJarAction
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        if (session.getAttribute("user_key") == null)
        {
            logger.debug("user_key is null new Login required");
            response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
            return null;
        }
        else
        {
            Connection conn = AdminUtil.getConnection((String)session.getAttribute("user_key"));
            if (conn == null )
            {
                response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
                return null;
            }
            else
            {
                if (conn.isClosed())
                {
                    response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
                    return null;
                }
            }

        }

        int startAtIndex = 0, endAtIndex = 0;
        Result result = new Result();
        List<Jar> jars = null;

        logger.debug("Received request to perform an action on the jar list");

        JarDAO jarDAO = GemFireXDWebDAOFactory.getJarStoreDAO();

        if (request.getParameter("search") != null)
        {
            jars = jarDAO.retrieveJarList
                    ("",
                    (String)request.getParameter("search"),
                    (String)session.getAttribute("user_key"));

            model.addAttribute("search", (String)request.getParameter("search"));
        }


        model.addAttribute("records", jars.size());
        model.addAttribute("estimatedrecords", jars.size());

        UserPref userPref = (UserPref) session.getAttribute("prefs");

        if (jars.size() <= userPref.getRecordsToDisplay())
        {
            model.addAttribute("jars", jars);
        }
        else
        {
            if (request.getParameter("startAtIndex") != null)
            {
                startAtIndex = Integer.parseInt(request.getParameter("startAtIndex"));
            }

            if (request.getParameter("endAtIndex") != null)
            {
                endAtIndex = Integer.parseInt(request.getParameter("endAtIndex"));
                if (endAtIndex > jars.size())
                {
                    endAtIndex = jars.size();
                }
            }
            else
            {
                endAtIndex = userPref.getRecordsToDisplay();
            }

            List subList = jars.subList(startAtIndex, endAtIndex);
            model.addAttribute("jars", subList);
        }

        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);

        // This will resolve to /WEB-INF/jsp/jars.jsp
        return "jars";
    }

}
