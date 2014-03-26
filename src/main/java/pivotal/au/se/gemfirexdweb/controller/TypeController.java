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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
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
import pivotal.au.se.gemfirexdweb.dao.types.Type;
import pivotal.au.se.gemfirexdweb.dao.types.TypeDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class TypeController 
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/types", method = RequestMethod.GET)
    public String showTypes
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		int startAtIndex = 0, endAtIndex = 0;
		javax.servlet.jsp.jstl.sql.Result dataLocationResult = null;
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
    	
    	logger.debug("Received request to show types");
    	
    	TypeDAO typeDAO = GemFireXDWebDAOFactory.getTypeDAO();
    	Result result = new Result();
    	
    	String typeAction = request.getParameter("typeAction");
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
    	
    	if (typeAction != null)
    	{
    		logger.debug("typeAction = " + typeAction);
    		
            result = null;
            result =
              typeDAO.simpletypeCommand
                (schema,
                 (String)request.getParameter("typeName"),
                 typeAction,
                 (String)session.getAttribute("user_key"));
            model.addAttribute("result", result);
            
    	}
    	
    	List<Type> types = typeDAO.retrieveTypeList
    			(schema, 
    			 null, 
    			 (String)session.getAttribute("user_key"));
    	
    	model.addAttribute("records", types.size());
    	model.addAttribute("estimatedrecords", types.size());
    	        
    	UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (types.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("types", types);  
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
            if (endAtIndex > types.size())
            {
              endAtIndex = types.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = types.subList(startAtIndex, endAtIndex);
          model.addAttribute("types", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("schemas", 
        		           GemFireXDWebDAOUtil.getAllSchemas
                                   ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);
        
    	// This will resolve to /WEB-INF/jsp/types.jsp
    	return "types";
    }
	
	@RequestMapping(value = "/types", method = RequestMethod.POST)
    public String performTypeAction
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
    	String schema = null;
    	Result result = new Result();
    	List<Type> types = null;
        String ddlString = null;

    	logger.debug("Received request to perform an action on the types");

    	TypeDAO typeDAO = GemFireXDWebDAOFactory.getTypeDAO();

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
    	
    	if (request.getParameter("search") != null)
    	{
	    	types = typeDAO.retrieveTypeList
	    			(schema, 
	    			 (String)request.getParameter("search"), 
	    			 (String)session.getAttribute("user_key"));
	    	
	    	model.addAttribute("search", (String)request.getParameter("search"));
    	}
    	else
    	{
	        String[] tableList  = request.getParameterValues("selected_type[]");
	        String   commandStr = request.getParameter("submit_mult");
	        
	        logger.debug("tableList = " + Arrays.toString(tableList));
	        logger.debug("command = " + commandStr);
	        
	        // start actions now if tableList is not null
	        
	        if (tableList != null)
	        {
	        	List al = new ArrayList<Result>();
                List<String> al2 = new ArrayList<String>();

	        	for (String type: tableList)
	        	{
                    if (commandStr.equalsIgnoreCase("DDL") || commandStr.equalsIgnoreCase("DDL_FILE"))
                    {
                        ddlString = typeDAO.generateDDL(schema, type, (String)session.getAttribute("user_key"));
                        al2.add(ddlString);
                    }
                    else
                    {
                        result = null;
                        result =
                          typeDAO.simpletypeCommand
                            (schema,
                             type,
                             commandStr,
                             (String)session.getAttribute("user_key"));
                        al.add(result);
                    }
	        	}

                if (commandStr.equalsIgnoreCase("DDL"))
                {
                    request.setAttribute("arrayresultddl", al2);
                }
                else if (commandStr.equalsIgnoreCase("DDL_FILE"))
                {
                    response.setContentType(SAVE_CONTENT_TYPE);
                    response.setHeader("Content-Disposition", "attachment; filename=" +
                            String.format(FILENAME, "TypeDDL"));

                    ServletOutputStream out = response.getOutputStream();
                    for (String ddl: al2)
                    {
                        out.println(ddl);
                    }

                    out.close();
                    return null;
                }
                else
                {
                    model.addAttribute("arrayresult", al);
                }
	        }
	        
	    	types = typeDAO.retrieveTypeList
	    			(schema, 
	    			 null, 
	    			 (String)session.getAttribute("user_key"));
    	}
    	
    	model.addAttribute("records", types.size());
    	model.addAttribute("estimatedrecords", types.size());
    	        
    	UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (types.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("types", types);  
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
            if (endAtIndex > types.size())
            {
              endAtIndex = types.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = types.subList(startAtIndex, endAtIndex);
          model.addAttribute("types", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        
        model.addAttribute("schemas", 
		           GemFireXDWebDAOUtil.getAllSchemas
                           ((String) session.getAttribute("user_key")));
        
        model.addAttribute("chosenSchema", schema);
        
    	// This will resolve to /WEB-INF/jsp/types.jsp
    	return "types";
    }
}
