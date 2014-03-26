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

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.constraints.Constraint;
import pivotal.au.se.gemfirexdweb.dao.constraints.ConstraintDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class ConstraintController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/constraints", method = RequestMethod.GET)
    public String showConstraints
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		int startAtIndex = 0, endAtIndex = 0;
        javax.servlet.jsp.jstl.sql.Result fkInfoResult;
		String schema = null;
		
    	if (session.getAttribute("user_key") == null)
    	{
    		logger.debug("user_key is null new Login required");
    		response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
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

    	logger.debug("Received request to show constraints");
    	
    	ConstraintDAO conDAO = GemFireXDWebDAOFactory.getConstraintDAO();
    	Result result = new Result();
    	
    	String conAction = request.getParameter("conAction");
    	String selectedSchema = request.getParameter("selectedSchema");
    	logger.debug("selectedSchema = " + selectedSchema);
    	
    	if (selectedSchema != null)
    	{
    		schema = selectedSchema;
    	}
    	else
    	{
    		schema = (String)session.getAttribute("schema");
    	}
    	
    	logger.debug("schema = " + schema);
    	
    	if (conAction != null)
    	{
    		logger.debug("conAction = " + conAction);

            if (conAction.equalsIgnoreCase("FKINFO"))
            {
                fkInfoResult = conDAO.getFKInfo
                        (schema,
                        (String)request.getParameter("constraintId"),
                        (String)session.getAttribute("user_key"));

                model.addAttribute("fkInfoResult", fkInfoResult);
                model.addAttribute("constraintname", (String)request.getParameter("constraintName"));
            }
            else
            {
                result = null;
                result =
                  conDAO.simpleconstraintCommand
                    (schema,
                     (String)request.getParameter("tabName"),
                     (String)request.getParameter("constraintName"),
                     conAction,
                     (String)session.getAttribute("user_key"));

                model.addAttribute("result", result);
            }
    	}
    	
    	List<Constraint> cons = conDAO.retrieveConstraintList
    			(schema, 
    			 null, 
    			 (String)session.getAttribute("user_key"));
    	
    	model.addAttribute("records", cons.size());
    	model.addAttribute("estimatedrecords", cons.size());
    	
        UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (cons.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("cons", cons);  
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
            if (endAtIndex > cons.size())
            {
              endAtIndex = cons.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = cons.subList(startAtIndex, endAtIndex);
          model.addAttribute("cons", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("schemas", 
		           GemFireXDWebDAOUtil.getAllSchemas
                           ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

    	// This will resolve to /WEB-INF/jsp/constraints.jsp
    	return "constraints";
    }

	@RequestMapping(value = "/constraints", method = RequestMethod.POST)
    public String performConstraintAction
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		String schema = null;
		Result result = new Result();
		
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
    	
    	logger.debug("Received request to perform an action on the constraints");
    	
    	String selectedSchema = request.getParameter("selectedSchema");
    	logger.debug("selectedSchema = " + selectedSchema);
    	
    	if (selectedSchema != null)
    	{
    		schema = selectedSchema;
    	}
    	else
    	{
    		schema = (String)session.getAttribute("schema");
    	}
    	
    	logger.debug("schema = " + schema);
    	
    	ConstraintDAO conDAO = GemFireXDWebDAOFactory.getConstraintDAO();

    	List<Constraint> cons = null;
    	
    	cons = conDAO.retrieveConstraintList
    			(schema, 
    			 (String)request.getParameter("search"), 
    			 (String)session.getAttribute("user_key"));
	    	
	
    	model.addAttribute("search", (String)request.getParameter("search"));
    	model.addAttribute("cons", cons);
    	model.addAttribute("records", cons.size());
        model.addAttribute("schemas", 
		           GemFireXDWebDAOUtil.getAllSchemas
                           ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

    	// This will resolve to /WEB-INF/jsp/constraints.jsp
    	return "constraints";
    
    }

}
