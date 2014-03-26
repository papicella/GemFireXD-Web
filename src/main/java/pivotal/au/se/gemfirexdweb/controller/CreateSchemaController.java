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
import pivotal.au.se.gemfirexdweb.beans.NewSchema;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.types.Type;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

@Controller
public class CreateSchemaController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createschema", method = RequestMethod.GET)
    public String createSchema
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
    	
    	logger.debug("Received request to create a new Schema");

        model.addAttribute("schemaAttribute", new NewSchema());
        model.addAttribute("schemas",
                GemFireXDWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

    	// This will resolve to /WEB-INF/jsp/create-schema.jsp
    	return "create-schema";
    }
	
	@RequestMapping(value = "/createschema", method = RequestMethod.POST)
    public String createSchemaAction
    (@ModelAttribute("schemaAttribute") NewSchema schemaAttribute,
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
    	
    	logger.debug("Received request to action an event for create Schema");

        model.addAttribute("schemas",
                GemFireXDWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        String schema = schemaAttribute.getSchemaName();

    	logger.debug("New Schema name = " + schema);

    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");

    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createSchema = new StringBuffer();

            createSchema.append("CREATE SCHEMA ");

            if (!checkIfParameterEmpty(request, "authorizationSchema"))
            {
                createSchema.append("AUTHORIZATION " + schemaAttribute.getAuthorizationSchema() + " \n");
            }
            else
            {
                createSchema.append(schema + " \n");
            }

            if (!checkIfParameterEmpty(request, "serverGroups"))
            {
                createSchema.append("DEFAULT SERVER GROUPS (" + schemaAttribute.getServerGroups() + ") \n");
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating Schema as -> " + createSchema.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createSchema.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);
                model.addAttribute("chosenSchema", schemaAttribute.getAuthorizationSchema());

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create Schema SQL as follows as -> " + createSchema.toString());
                model.addAttribute("sql", createSchema.toString());
                model.addAttribute("chosenSchema", schemaAttribute.getAuthorizationSchema());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, schemaAttribute.getSchemaName()));

                ServletOutputStream out = response.getOutputStream();
                out.println(createSchema.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-schema.jsp
    	return "create-schema";
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
