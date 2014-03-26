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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pivotal.au.se.gemfirexdweb.beans.AddListener;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

@Controller
public class AddListenerController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/addlistener", method = RequestMethod.GET)
    public String createListener
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
    	
    	logger.debug("Received request to add new Table Listener");

        AddListener listenerAttribute = new AddListener();

        if (request.getParameter("tableName") != null)
        {
            listenerAttribute.setTableName(request.getParameter("tableName"));
        }

        if (request.getParameter("schemaName") != null)
        {
            listenerAttribute.setSchemaName(request.getParameter("schemaName"));
        }

        model.addAttribute("listenerAttribute", listenerAttribute);


    	// This will resolve to /WEB-INF/jsp/addlistener.jsp
    	return "addlistener";
    }
	
	@RequestMapping(value = "/addlistener", method = RequestMethod.POST)
    public String createListenerAction
    (@ModelAttribute("listenerAttribute") AddListener listenerAttribute,
     Model model,
     HttpServletResponse response,
     HttpServletRequest request,
     HttpSession session) throws Exception
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
    	
    	logger.debug("Received request to action an event for add listener");

        logger.debug("Listener ID = " + listenerAttribute.getId());

    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");

    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer addListener = new StringBuffer();

            addListener.append("CALL SYS.ADD_LISTENER ('" + listenerAttribute.getId() + "', \n");
            addListener.append("'" + listenerAttribute.getSchemaName() + "', \n");
            addListener.append("'" + listenerAttribute.getTableName() + "', \n");
            addListener.append("'" + listenerAttribute.getFunctionName() + "', \n");

            if (!checkIfParameterEmpty(request, "initString"))
            {
                addListener.append("'" + listenerAttribute.getInitString() + "', \n");
            }
            else
            {
                addListener.append("'', \n");
            }

            if (!checkIfParameterEmpty(request, "serverGroups"))
            {
                addListener.append("'" + listenerAttribute.getServerGroups() + "')");
            }
            else
            {
                addListener.append("''), \n");
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating Listener as -> " + addListener.toString());

                result = GemFireXDWebDAOUtil.runStoredCommand
                        (addListener.toString(),
                        (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);
                model.addAttribute("tableName", listenerAttribute.getTableName());

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create Listener SQL as follows as -> " + addListener.toString());
                model.addAttribute("sql", addListener.toString());
                model.addAttribute("tableName", listenerAttribute.getTableName());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, listenerAttribute.getId()));

                ServletOutputStream out = response.getOutputStream();
                out.println(addListener.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/addlistener.jsp
    	return "addlistener";
    }
	
	private boolean checkIfParameterEmpty (HttpServletRequest request, String param)
	{
		String val = request.getParameter(param);
		
		if (val != null)
		{
			if (val.trim().length() == 0)
			{
				return true;
			}
			else
			{
				// we have some data
				return false;
			}
		}
		else
		{
			// no data here
			return true;
		}
	}
}
