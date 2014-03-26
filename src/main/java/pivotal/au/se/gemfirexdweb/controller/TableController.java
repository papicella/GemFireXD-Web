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
import pivotal.au.se.gemfirexdweb.dao.tables.Table;
import pivotal.au.se.gemfirexdweb.dao.tables.TableDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class TableController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String showTables
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		int startAtIndex = 0, endAtIndex = 0;
		javax.servlet.jsp.jstl.sql.Result dataLocationResult, memoryUsageResult, expirationEvictionResult,
                                          allTableInfoResult, tableStructure, tableTriggersResult = null;
		String schema = null; 
		
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
    	
    	logger.debug("Received request to show tables");
    	
    	TableDAO tableDAO = GemFireXDWebDAOFactory.getTableDAO();
    	Result result = new Result();
    	
    	String tabAction = request.getParameter("tabAction");
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
    	String viewType = request.getParameter("viewType");
        if (viewType == null)
        {
            viewType = "ORDINARY";
        }

        logger.debug("viewType = " + viewType);

    	if (tabAction != null)
    	{
    		logger.debug("tabAction = " + tabAction);
    		
    		if (tabAction.equalsIgnoreCase("LOADSCRIPT"))
    		{
    			model.addAttribute("loadscriptsql", 
    					tableDAO.generateLoadScript
    						(schema,
    						 (String)request.getParameter("tabName")));
    			model.addAttribute("tablename", (String)request.getParameter("tabName"));
    		}
    		else if (tabAction.equalsIgnoreCase("DATALOCATIONS"))
    		{
    			dataLocationResult = 
    					tableDAO.getDataLocations
    					   (schema,
    						(String)request.getParameter("tabName"),
    					    (String)session.getAttribute("user_key"));
    			
    			model.addAttribute("dataLocationResults", dataLocationResult);
    			model.addAttribute("tablename", (String)request.getParameter("tabName"));
    		}
            else if (tabAction.equalsIgnoreCase("TABLETRIGGERS"))
            {
                tableTriggersResult =
                        tableDAO. getTableTriggers
                                (schema,
                                (String)request.getParameter("tabName"),
                                (String)session.getAttribute("user_key"));

                model.addAttribute("tableTriggersResult", tableTriggersResult);
                model.addAttribute("tablename", (String)request.getParameter("tabName"));
            }
    		else if (tabAction.equalsIgnoreCase("MEMORYUSAGE"))
    		{
    			memoryUsageResult = 
    					tableDAO.getMemoryUsage
    					   (schema,
    						(String)request.getParameter("tabName"),
    					    (String)session.getAttribute("user_key"));
    			
    			model.addAttribute("tableMemoryUsage", memoryUsageResult);
    			model.addAttribute("tablename", (String)request.getParameter("tabName"));
                model.addAttribute("memUsageSum", "N");
    		}
            else if (tabAction.equalsIgnoreCase("MEMORYUSAGESUM"))
            {
                memoryUsageResult =
                        tableDAO.getMemoryUsageSum
                                (schema,
                                (String)request.getParameter("tabName"),
                                (String)session.getAttribute("user_key"));

                model.addAttribute("tableMemoryUsage", memoryUsageResult);
                model.addAttribute("tablename", (String)request.getParameter("tabName"));
                model.addAttribute("memUsageSum", "Y");
            }
    		else if (tabAction.equalsIgnoreCase("EXPIRATION-EVICTION"))
    		{
    			expirationEvictionResult = 
    					tableDAO.getExpirationEvictionAttrs
    					   (schema,
    						(String)request.getParameter("tabName"),
    					    (String)session.getAttribute("user_key"));
    			
    			model.addAttribute("expirationEvictionResult", expirationEvictionResult);
    			model.addAttribute("tablename", (String)request.getParameter("tabName"));
    		}
    		else if (tabAction.equalsIgnoreCase("ALLTABLEINFO"))
    		{
    			allTableInfoResult = 
    					tableDAO.getAllTableInfo
    					   (schema,
    						(String)request.getParameter("tabName"),
    					    (String)session.getAttribute("user_key"));
    			
    			model.addAttribute("allTableInfoResult", allTableInfoResult);
    			model.addAttribute("tablename", (String)request.getParameter("tabName"));
    		}
    		else if (tabAction.equalsIgnoreCase("STRUCTURE"))
    		{
    			tableStructure = 
    					tableDAO.getTableStructure
    					   (schema,
    						(String)request.getParameter("tabName"),
    					    (String)session.getAttribute("user_key"));
    			
    			model.addAttribute("tableStructure", tableStructure);
    			model.addAttribute("tablename", (String)request.getParameter("tabName"));
    		}
    		else if (tabAction.equalsIgnoreCase("PARTITIONATTRS"))
    		{
    			String parAttrResult = 
    					tableDAO.viewPartitionAttrs
    					   (schema,
    						(String)request.getParameter("tabName"),
    					    (String)session.getAttribute("user_key"));
    			
    			model.addAttribute("parAttrResult", parAttrResult);
    			model.addAttribute("tablename", (String)request.getParameter("tabName"));
    		}
    		else
    		{
	            result = null;
	            result =
	              tableDAO.simpletableCommand
	                (schema,
	                 (String)request.getParameter("tabName"),
	                 tabAction,
	                 (String)session.getAttribute("user_key"));
	            model.addAttribute("result", result);
    		}   
            
    	}
    	
    	List<Table> tbls = tableDAO.retrieveTableList
    			(schema, 
    			 null, 
    			 (String)session.getAttribute("user_key"),
                 viewType);
    	
    	model.addAttribute("records", tbls.size());
    	model.addAttribute("estimatedrecords", tbls.size());
    	        
    	UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (tbls.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("tables", tbls);  
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
            if (endAtIndex > tbls.size())
            {
              endAtIndex = tbls.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = tbls.subList(startAtIndex, endAtIndex);
          model.addAttribute("tables", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("schemas", 
        		           GemFireXDWebDAOUtil.getAllSchemas
                                   ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);
        model.addAttribute("viewType", viewType);

    	// This will resolve to /WEB-INF/jsp/tables.jsp
    	return "tables";
    }
	
	@RequestMapping(value = "/tables", method = RequestMethod.POST)
    public String performTableAction
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
    	List<Table> tbls = null;
    	
    	logger.debug("Received request to perform an action on the tables");

    	TableDAO tableDAO = GemFireXDWebDAOFactory.getTableDAO();

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
        String viewType = request.getParameter("viewType");
        if (viewType == null)
        {
            viewType = "ORDINARY";
        }

        logger.debug("viewType = " + viewType);

    	if (request.getParameter("search") != null)
    	{
	    	tbls = tableDAO.retrieveTableList
	    			(schema, 
	    			 (String)request.getParameter("search"), 
	    			 (String)session.getAttribute("user_key"),
                     viewType);
	    	
	    	model.addAttribute("search", (String)request.getParameter("search"));
    	}
    	else
    	{
	        String[] tableList  = request.getParameterValues("selected_tbl[]");
	        String   commandStr = request.getParameter("submit_mult");
	        
	        logger.debug("tableList = " + Arrays.toString(tableList));
	        logger.debug("command = " + commandStr);
	        
	        // start actions now if tableList is not null
	        
	        if (tableList != null)
	        {
	        	List al = new ArrayList<Result>();
	        	for (String table: tableList)
	        	{
	                result = null;
	                result =
	                  tableDAO.simpletableCommand
	                    (schema,
	                     table,
	                     commandStr,
	                     (String)session.getAttribute("user_key"));
	                al.add(result);
	        	}
	        	
	        	model.addAttribute("arrayresult", al);
	        }
	        
	    	tbls = tableDAO.retrieveTableList
	    			(schema, 
	    			 null, 
	    			 (String)session.getAttribute("user_key"),
                     viewType);
    	}
    	
    	model.addAttribute("records", tbls.size());
    	model.addAttribute("estimatedrecords", tbls.size());
    	
    	UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (tbls.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("tables", tbls);  
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
            if (endAtIndex > tbls.size())
            {
              endAtIndex = tbls.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
            
          }
          
          List subList = tbls.subList(startAtIndex, endAtIndex);
          model.addAttribute("tables", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("schemas", 
		           GemFireXDWebDAOUtil.getAllSchemas
                           ((String) session.getAttribute("user_key")));
        
        model.addAttribute("chosenSchema", schema);
        model.addAttribute("viewType", viewType);

    	// This will resolve to /WEB-INF/jsp/tables.jsp
    	return "tables";
    }
}
