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
import pivotal.au.se.gemfirexdweb.beans.NewProcedure;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.types.Type;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.Arrays;

@Controller
public class CreateProcedureController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createprocedure", method = RequestMethod.GET)
    public String createProcedure
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
    	
    	logger.debug("Received request to create a new Procedure");

        session.setAttribute("numParams", "0");
        model.addAttribute("numParams", "0");
        model.addAttribute("procedureAttribute", new NewProcedure());

    	// This will resolve to /WEB-INF/jsp/create-procedure.jsp
    	return "create-procedure";
    }
	
	@RequestMapping(value = "/createprocedure", method = RequestMethod.POST)
    public String createProcedureAction
    (@ModelAttribute("procedureAttribute") NewProcedure procedureAttribute,
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
    	
    	logger.debug("Received request to action an event for create Procedure");

        String schema = procedureAttribute.getSchemaName().trim();

        if (schema.length() == 0)
        {
            schema = (String) session.getAttribute("schema");
        }

    	logger.debug("New Procedure schema name = " + schema);
        logger.debug("New Procedure name = " + procedureAttribute.getProcedureName());

    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");

    	if (submit != null)
    	{
            if (submit.equalsIgnoreCase("Parameter(s)"))
            {
                int cols = Integer.parseInt(request.getParameter("numParams"));
                int numParams = Integer.parseInt((String)session.getAttribute("numParams"));

                numParams = numParams + cols;

                session.setAttribute("numParams", "" + numParams);
                model.addAttribute("numParams", numParams);
            }
            else
            {
                // build create HDFS Store SQL
                StringBuffer createProcedure = new StringBuffer();

                createProcedure.append("CREATE PROCEDURE " + schema + "." + procedureAttribute.getProcedureName() + " \n");

                String[] parameterTypes  = request.getParameterValues("parameter_type[]");
                String[] parameterNames  = request.getParameterValues("parameter_name[]");
                String[] dataTypes  = request.getParameterValues("data_type[]");
                String[] columnPrecision  = request.getParameterValues("column_precision[]");

                logger.debug("parameterTypes = " + Arrays.toString(parameterTypes));
                logger.debug("parameterNames = " + Arrays.toString(parameterNames));
                logger.debug("dataTypes = " + Arrays.toString(dataTypes));
                logger.debug("columnPrecision = " + Arrays.toString(columnPrecision));

                int i = 0;

                int size = 0;

                if (parameterNames != null)
                {
                    size = parameterNames.length;

                    for (String parameterName: parameterNames)
                    {
                        if (i == 0)
                        {
                            createProcedure.append("(");
                        }

                        createProcedure.append(parameterTypes[i] + " " + parameterName + " " + dataTypes[i]);
                        if (columnPrecision[i].length() != 0)
                        {
                            createProcedure.append("(" + columnPrecision[i] + ")");
                        }

                        i++;
                        if (i < size)
                        {
                            createProcedure.append(", \n");
                        }
                    }

                    if (i >= 1)
                    {
                       createProcedure.append(") \n");
                    }
                }

                if (!checkIfParameterEmpty(request, "language"))
                {
                    createProcedure.append("LANGUAGE " + procedureAttribute.getLanguage() + " \n");
                }

                if (!checkIfParameterEmpty(request, "parameterStyle"))
                {
                    createProcedure.append("PARAMETER STYLE " + procedureAttribute.getParameterStyle() + " \n");
                }

                if (!checkIfParameterEmpty(request, "sqlAccess"))
                {
                    createProcedure.append(procedureAttribute.getSqlAccess() + " \n");
                }

                if (!checkIfParameterEmpty(request, "dynamicResultsets"))
                {
                    createProcedure.append("DYNAMIC RESULT SETS " + procedureAttribute.getDynamicResultsets() + " \n");
                }

                createProcedure.append("EXTERNAL NAME '" + procedureAttribute.getExternalName() + "'\n");

                if (submit.equalsIgnoreCase("create"))
                {
                    Result result = new Result();

                    logger.debug("Creating Procedure as -> " + createProcedure.toString());

                    result = GemFireXDWebDAOUtil.runCommand
                            (createProcedure.toString(),
                             (String) session.getAttribute("user_key"));

                    model.addAttribute("result", result);

                }
                else if (submit.equalsIgnoreCase("Show SQL"))
                {
                    logger.debug("Create Procedure SQL as follows as -> " + createProcedure.toString());
                    model.addAttribute("sql", createProcedure.toString());
                }
                else if (submit.equalsIgnoreCase("Save to File"))
                {
                    response.setContentType(SAVE_CONTENT_TYPE);
                    response.setHeader("Content-Disposition", "attachment; filename=" +
                            String.format(FILENAME, procedureAttribute.getProcedureName()));

                    ServletOutputStream out = response.getOutputStream();
                    out.println(createProcedure.toString());
                    out.close();
                    return null;
                }
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-procedure.jsp
    	return "create-procedure";
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
