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
import pivotal.au.se.gemfirexdweb.beans.NewFunction;
import pivotal.au.se.gemfirexdweb.beans.NewProcedure;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.Arrays;

@Controller
public class CreateFunctionController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createfunction", method = RequestMethod.GET)
    public String createFunction
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
    	
    	logger.debug("Received request to create a new Function");

        session.setAttribute("numParams", "0");
        model.addAttribute("numParams", "0");
        model.addAttribute("functionAttribute", new NewFunction());

    	// This will resolve to /WEB-INF/jsp/create-function.jsp
    	return "create-function";
    }
	
	@RequestMapping(value = "/createfunction", method = RequestMethod.POST)
    public String createFunctionAction
    (@ModelAttribute("functionAttribute") NewFunction functionAttribute,
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

        String schema = functionAttribute.getSchemaName().trim();

        if (schema.length() == 0)
        {
            schema = (String) session.getAttribute("schema");
        }

    	logger.debug("New Function schema name = " + schema);
        logger.debug("New Function name = " + functionAttribute.getFunctionName());

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
                StringBuffer createFunction = new StringBuffer();

                createFunction.append("CREATE FUNCTION " + schema + "." + functionAttribute.getFunctionName() + " \n");

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
                            createFunction.append("(");
                        }

                        createFunction.append(parameterName + " " + dataTypes[i]);
                        if (columnPrecision[i].length() != 0)
                        {
                            createFunction.append("(" + columnPrecision[i] + ")");
                        }

                        i++;
                        if (i < size)
                        {
                            createFunction.append(", \n");
                        }
                    }

                    if (i >= 1)
                    {
                       createFunction.append(") \n");
                    }
                }

                createFunction.append("RETURNS " + functionAttribute.getReturnType());
                if (functionAttribute.getReturnPrecision().trim().length() != 0)
                {
                    createFunction.append("(" + functionAttribute.getReturnPrecision() + ") \n");
                }
                else
                {
                    createFunction.append(" \n");
                }

                if (!checkIfParameterEmpty(request, "language"))
                {
                    createFunction.append("LANGUAGE " + functionAttribute.getLanguage() + " \n");
                }

                createFunction.append("EXTERNAL NAME '" + functionAttribute.getExternalName() + "' \n");

                if (!checkIfParameterEmpty(request, "parameterStyle"))
                {
                    createFunction.append("PARAMETER STYLE " + functionAttribute.getParameterStyle() + " \n");
                }

                if (!checkIfParameterEmpty(request, "sqlAccess"))
                {
                    createFunction.append(functionAttribute.getSqlAccess() + " \n");
                }

                if (!checkIfParameterEmpty(request, "ifNull"))
                {
                    createFunction.append(functionAttribute.getIfNull() + " \n");
                }

                if (submit.equalsIgnoreCase("create"))
                {
                    Result result = new Result();

                    logger.debug("Creating Function as -> " + createFunction.toString());

                    result = GemFireXDWebDAOUtil.runCommand
                            (createFunction.toString(),
                             (String) session.getAttribute("user_key"));

                    model.addAttribute("result", result);
                    model.addAttribute("returnType", functionAttribute.getReturnType());
                }
                else if (submit.equalsIgnoreCase("Show SQL"))
                {
                    logger.debug("Create Function SQL as follows as -> " + createFunction.toString());
                    model.addAttribute("sql", createFunction.toString());
                    model.addAttribute("returnType", functionAttribute.getReturnType());
                }
                else if (submit.equalsIgnoreCase("Save to File"))
                {
                    response.setContentType(SAVE_CONTENT_TYPE);
                    response.setHeader("Content-Disposition", "attachment; filename=" +
                            String.format(FILENAME, functionAttribute.getFunctionName()));

                    ServletOutputStream out = response.getOutputStream();
                    out.println(createFunction.toString());
                    out.close();
                    return null;
                }
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-function.jsp
    	return "create-function";
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
