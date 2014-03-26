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
import pivotal.au.se.gemfirexdweb.dao.indexes.Index;
import pivotal.au.se.gemfirexdweb.dao.indexes.IndexDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class IndexController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/indexes", method = RequestMethod.GET)
    public String showInexes
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		int startAtIndex = 0, endAtIndex = 0;
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
    	
    	logger.debug("Received request to show indexes");
    	
    	IndexDAO indexDAO = GemFireXDWebDAOFactory.getIndexDAO();
    	Result result = new Result();
    	
    	String idxAction = request.getParameter("idxAction");
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
    	
    	if (idxAction != null)
    	{
    		logger.debug("idxAction = " + idxAction);
            result = null;
            result =
              indexDAO.simpleindexCommand
                (schema,
                 (String)request.getParameter("indexName"),
                 idxAction,
                 (String)session.getAttribute("user_key"));
            
            model.addAttribute("result", result);
    	}
    	
    	List<Index> indexes = indexDAO.retrieveIndexList
    			(schema, 
    			 null, 
    			 (String)session.getAttribute("user_key"));
    	
    	model.addAttribute("records", indexes.size());
    	model.addAttribute("estimatedrecords", indexes.size());
    	
        UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (indexes.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("indexes", indexes);  
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
            if (endAtIndex > indexes.size())
            {
              endAtIndex = indexes.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = indexes.subList(startAtIndex, endAtIndex);
          model.addAttribute("indexes", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("schemas", 
		           GemFireXDWebDAOUtil.getAllSchemas
                           ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

    	// This will resolve to /WEB-INF/jsp/indexes.jsp
    	return "indexes";
    }
	
	@RequestMapping(value = "/indexes", method = RequestMethod.POST)
    public String performIndexAction
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		String schema = null;
		int startAtIndex = 0, endAtIndex = 0;
		
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
    	
    	Result result = new Result();
    	List<Index> indexes = null;
    	
    	logger.debug("Received request to perform an action on the indexes");

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
    	
    	IndexDAO indexDAO = GemFireXDWebDAOFactory.getIndexDAO();
    	
    	if (request.getParameter("search") != null)
    	{
	    	indexes = indexDAO.retrieveIndexList
	    			(schema, 
	    			 (String)request.getParameter("search"), 
	    			 (String)session.getAttribute("user_key"));
	    	
	    	model.addAttribute("search", (String)request.getParameter("search"));
    	}
    	else
    	{
	        String[] tableList  = request.getParameterValues("selected_idx[]");
	        String   commandStr = request.getParameter("submit_mult");
	        
	        logger.debug("tableList = " + Arrays.toString(tableList));
	        logger.debug("command = " + commandStr);
	        
	        // start actions now if tableList is not null
	        
	        if (tableList != null)
	        {
	        	List al = new ArrayList<Result>();
	        	for (String index: tableList)
	        	{
	                result = null;
	                result =
	                 indexDAO.simpleindexCommand
	                    (schema,
	                     index,
	                     commandStr,
	                     (String)session.getAttribute("user_key"));
	                al.add(result);
	        	}
	        	
	        	model.addAttribute("arrayresult", al);
	        }
	        
	    	indexes = indexDAO.retrieveIndexList
	    			(schema, 
	    			 null, 
	    			 (String)session.getAttribute("user_key"));
    	}

    	model.addAttribute("records", indexes.size());
    	model.addAttribute("estimatedrecords", indexes.size());
    	
        UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (indexes.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("indexes", indexes);  
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
            if (endAtIndex > indexes.size())
            {
              endAtIndex = indexes.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = indexes.subList(startAtIndex, endAtIndex);
          model.addAttribute("indexes", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        
        model.addAttribute("schemas", 
		           GemFireXDWebDAOUtil.getAllSchemas
                           ((String) session.getAttribute("user_key")));
     
        model.addAttribute("chosenSchema", schema);
     
    	// This will resolve to /WEB-INF/jsp/indexes.jsp
    	return "indexes";
    }
}
