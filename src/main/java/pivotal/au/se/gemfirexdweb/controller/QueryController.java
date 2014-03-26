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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.se.gemfirexdweb.beans.CommandResult;
import pivotal.au.se.gemfirexdweb.beans.QueryWindow;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.ConnectionManager;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

@Controller
public class QueryController 
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "worksheet.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	// add comments here
	private final String QUERY_TYPES[] = {
		        "SELECT", "INSERT", "DELETE", "DDL", "UPDATE", "CALL", "COMMIT", "ROLLBACK"
		    };
	   
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String worksheet
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
    	
    	logger.debug("Received request to show query worksheet");
    	UserPref userPrefs = (UserPref) session.getAttribute("prefs");
    	
    	String action = request.getParameter("action");
    	if (action != null)
    	{
    		
    		CommandResult result = new CommandResult();
    		ConnectionManager cm = ConnectionManager.getInstance();
    		Connection conn = cm.getConnection(session.getId());
    		
    		if (action.trim().equals("commit"))
    		{
    			logger.debug("commit action requested");
    			result = QueryUtil.runCommitOrRollback(conn, true, "N");
    			addCommandToHistory(session, userPrefs, "commit");
    		}
    		else if (action.trim().equals("rollback"))
    		{
    			logger.debug("rollback action requested");
    			result = QueryUtil.runCommitOrRollback(conn, false, "N");
    			addCommandToHistory(session, userPrefs, "rollback");
    		}
    		
    		model.addAttribute("result", result);
    		
    	}
    	
    	// Create new QueryWindow and add to model
    	// This is the formBackingObject
    	model.addAttribute("queryAttribute", new QueryWindow());

    	// This will resolve to /WEB-INF/jsp/query.jsp
    	return "query";
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String worksheetAction
    (@ModelAttribute("queryAttribute") QueryWindow queryAttribute, 
     Model model, 
     HttpServletResponse response, HttpServletRequest request,
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
    	
    	logger.debug("Received request to action SQL from query worksheet");
    	logger.info(queryAttribute);

    	UserPref userPrefs = (UserPref) session.getAttribute("prefs");
    	
        ConnectionManager cm = ConnectionManager.getInstance();
        
        if (queryAttribute.getQuery() != null)
        {

            if (queryAttribute.getSaveWorksheet().equals("Y"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" + FILENAME);

                ServletOutputStream out = response.getOutputStream();
                out.println(queryAttribute.getQuery());
                out.close();
                return null;
            }

	        // retrieve connection
	        Connection conn = cm.getConnection(session.getId());
	        String query = queryAttribute.getQuery().trim();
	        logger.debug("Query = " + query);
	        
	        String [] splitQueryStr = spiltQuery(query);
	        
	        CommandResult result = new CommandResult();
	        
	        if (query.length() > 0)
	        {
	        	if (splitQueryStr.length == 1)
	        	{
	        		String s = checkForComments(query);
	        		s = s.trim();
	        		
			    	if (determineQueryType(s).equals("SELECT"))
			    	{
			    		try
			    		{
			    			if (queryAttribute.getExplainPlan().equals("Y"))
			    			{
			    				logger.debug("Need to run explain plan");
			    				model.addAttribute("explainresult", QueryUtil.runExplainPlan(conn, query));
			    			}
			    			else
			    			{
				    				
			    				if (queryAttribute.getShowMember().equals("Y"))
			    				{
			    					String replace = "select dsid() as \"Member\",";
			    					
			    					s = query.toLowerCase().replaceFirst("select", replace);	
			    				}
			    				
				    			long start = System.currentTimeMillis();
					    		Result res = QueryUtil.runQuery(conn, s, userPrefs.getMaxRecordsinSQLQueryWindow());
					    		long end = System.currentTimeMillis();
					    		
					  	        double timeTaken = new Double(end - start).doubleValue();
						        DecimalFormat df = new DecimalFormat("#.##");
					    		
					    		model.addAttribute("queryResults", res);
					    		model.addAttribute("query", s);
					    		model.addAttribute("querysql", s);
					    		if (queryAttribute.getQueryCount().equals("Y"))
					    		{
					    			model.addAttribute("queryResultCount", res.getRowCount());
					    		}
					    		
					    		if (queryAttribute.getElapsedTime().equals("Y"))
					    		{
					    			model.addAttribute("elapsedTime", df.format(timeTaken/1000));
					    		}
				    		
					    		addCommandToHistory(session, userPrefs, s);
					    		
			    			}
			    		}
			    		catch (Exception ex)
			    		{
			    			result.setCommand(s);
			    			result.setMessage(ex.getMessage() == null ? "Unable to run query" : ex.getMessage());
			    			result.setRows(-1);
			    			model.addAttribute("result", result);
			    			model.addAttribute("query", s);
			    		}
			    	}
			    	else
			    	{
			    		if (s.length() > 0)
			    		{
			    			if (determineQueryType(s).equals("COMMIT"))
			    			{
			    				result = QueryUtil.runCommitOrRollback(conn, true, queryAttribute.getElapsedTime());
			    				model.addAttribute("result", result);
			    				if (result.getMessage().startsWith("SUCCESS"))
			    				{
			    					addCommandToHistory(session, userPrefs, s);
			    				}
			    			}
			    			else if (determineQueryType(s).equals("ROLLBACK"))
			    			{
			    				result = QueryUtil.runCommitOrRollback(conn, false, queryAttribute.getElapsedTime());
			    				model.addAttribute("result", result);
			    				if (result.getMessage().startsWith("SUCCESS"))
			    				{
			    					addCommandToHistory(session, userPrefs, s);
			    				}
			    			}
			    			else if (determineQueryType(s).equals("CALL"))
			    			{
			    				
			    				String procName = getProcName(s);
			    				
			    				if (procName != null)
			    				{
				    				String schema = null;
				    				
				    				int x = procName.indexOf(".");
				    				if (x != -1)
				    				{
				    				  	String newProcName = procName.substring((procName.indexOf(".") + 1));
				    				  	schema = procName.substring(0, (procName.indexOf(".")));
				    				  	procName = newProcName;
				    				}
				    				else
				    				{
				    					schema = (String) session.getAttribute("schema");
				    				}
				    				
				    				logger.debug("schema for stored procedure = " + schema);
				    				logger.debug("call statement called for proc with name " + procName);
				    				
				    				// need to get schema name to check proc details
				    				int numberOfDynamicResultSets = 
				    						QueryUtil.checkForDynamicResultSetProc (conn, schema, procName);
				    				
				    				if (numberOfDynamicResultSets > 0)
				    				{
				    					logger.debug("call statement with " + numberOfDynamicResultSets + " dynamic resultset(s)");
				    					try
				    					{
					    					List<Result> procResults = 
					    							QueryUtil.runStoredprocWithResultSet(conn, 
					    																 s, 
					    																 userPrefs.getMaxRecordsinSQLQueryWindow(), 
					    																 numberOfDynamicResultSets);
					    					model.addAttribute("procresults", procResults);
					    					model.addAttribute("callstatement", procName);
					    					model.addAttribute("dynamicresults", numberOfDynamicResultSets);
					    					addCommandToHistory(session, userPrefs, s);
				    					}
				    					catch (Exception ex)
				    					{
							    			result.setCommand(s);
							    			result.setMessage(ex.getMessage() == null ? "Unable to run query" : ex.getMessage());
							    			result.setRows(-1);
							    			model.addAttribute("result", result);
							    			model.addAttribute("query", s);				    						
				    					}
				    				}
				    				else
				    				{
					    				result = QueryUtil.runCommand(conn, s, queryAttribute.getElapsedTime());
					    				model.addAttribute("result", result);	
					    				if (result.getMessage().startsWith("SUCCESS"))
					    				{
					    					addCommandToHistory(session, userPrefs, s);
					    				}
				    				}
			    				}
			    				else
			    				{
				    				result = QueryUtil.runCommand(conn, s, queryAttribute.getElapsedTime());
				    				model.addAttribute("result", result);	
				    				if (result.getMessage().startsWith("SUCCESS"))
				    				{
				    					addCommandToHistory(session, userPrefs, s);
				    				}
			    				}
			    			}
			    			else
			    			{
			    				result = QueryUtil.runCommand(conn, s, queryAttribute.getElapsedTime());
			    				model.addAttribute("result", result);
			    				if (result.getMessage().startsWith("SUCCESS"))
			    				{
			    					addCommandToHistory(session, userPrefs, s);
			    				}
			    			}
			    			
				    		
			    		}
			    	}
			    	
	        	}
		    	else
		    	{
		    		logger.debug("multiple SQL statements need to be executed");
		    		SortedMap<String, Object> queryResults = 
		    				handleMultipleStatements(splitQueryStr, conn, userPrefs, queryAttribute, session);
		    		logger.debug("keys : " + queryResults.keySet());
		    		model.addAttribute("sqlResultMap", queryResults);
		    		model.addAttribute("statementsExecuted", queryResults.size()); 
		    		
		    	}
	        }
        }
        else
        {
        	if (ServletFileUpload.isMultipartContent(request))
        	{
        		logger.debug("is multipartcontent request");
        		FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
        		List<?> fileItemsList = upload.parseRequest(request);
        		
        		logger.debug("fileItemList size = " + fileItemsList.size());
        		Iterator<?> it = fileItemsList.iterator();
        		while (it.hasNext())
        		{
        			FileItem fileItemTemp = (FileItem)it.next();
        			if (fileItemTemp.getFieldName().equals("sqlfilename"))
        			{
        				QueryWindow qw = new QueryWindow();
        				qw.setQuery(fileItemTemp.getString());
        				model.addAttribute("queryAttribute", qw);
        				model.addAttribute("sqlfile", fileItemTemp.getName());
        			}
        		}
        	}
        }
        
    	return "query";
    }

    @RequestMapping(value = "/executequery", method = RequestMethod.GET)
    public String executeQuery 
    	(@ModelAttribute("queryAttribute") QueryWindow queryAttribute,
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
    	
    	logger.debug("Received request to action a query directly");
    	
    	UserPref userPrefs = (UserPref) session.getAttribute("prefs");
    	
        ConnectionManager cm = ConnectionManager.getInstance();
        // retrieve connection
        Connection conn = cm.getConnection(session.getId());

        String query = request.getParameter("query");
        logger.debug("Query = " + query);

        QueryWindow qw = new QueryWindow();
        qw.setQuery(query);
        qw.setElapsedTime("N");
        qw.setExplainPlan("N");
        qw.setQueryCount("N");
        qw.setShowMember("N");
        
    	CommandResult result = new CommandResult();
    	String s = query.trim();
    	
    	if (determineQueryType(s).equals("SELECT"))
    	{
    		try
    		{
    			Result res = QueryUtil.runQuery(conn, query, userPrefs.getMaxRecordsinSQLQueryWindow());
    			logger.debug("Query run");
    			model.addAttribute("queryResults", res);
    			model.addAttribute("queryAttribute", qw);
    			model.addAttribute("querysql", query);  
    			
    		}
            catch (Exception ex)
            {
            	logger.debug("in here");
    			result.setCommand(query);
    			result.setMessage(ex.getMessage() == null ? "Unable to run query" : ex.getMessage());
    			result.setRows(-1);
    			model.addAttribute("result", result);
    			model.addAttribute("query", query);
            }
    	}
    	else if (determineQueryType(s).equals("COMMIT"))
		{
			result = QueryUtil.runCommitOrRollback(conn, true, qw.getElapsedTime());
			model.addAttribute("result", result);
		}
		else if (determineQueryType(s).equals("ROLLBACK"))
		{
			result = QueryUtil.runCommitOrRollback(conn, false, qw.getElapsedTime());
			model.addAttribute("result", result);
		}
		else if (determineQueryType(s).equals("CALL"))
		{
			
			String procName = getProcName(s);
			
			if (procName != null)
			{
				String schema = null;
				
				int x = procName.indexOf(".");
				if (x != -1)
				{
				  	String newProcName = procName.substring((procName.indexOf(".") + 1));
				  	schema = procName.substring(0, (procName.indexOf(".")));
				  	procName = newProcName;
				}
				else
				{
					schema = (String) session.getAttribute("schema");
				}
				
				logger.debug("schema for stored procedure = " + schema);
				logger.debug("call statement called for proc with name " + procName);
				
				// need to get schema name to check proc details
				int numberOfDynamicResultSets = 
						QueryUtil.checkForDynamicResultSetProc (conn, schema, procName);
				
				if (numberOfDynamicResultSets > 0)
				{
					logger.debug("call statement with " + numberOfDynamicResultSets + " dynamic resultset(s)");
					try
					{
    					List<Result> procResults = 
    							QueryUtil.runStoredprocWithResultSet(conn, 
    																 s, 
    																 userPrefs.getMaxRecordsinSQLQueryWindow(), 
    																 numberOfDynamicResultSets);
    					model.addAttribute("procresults", procResults);
    					model.addAttribute("callstatement", procName);
    					model.addAttribute("dynamicresults", numberOfDynamicResultSets);
					}
					catch (Exception ex)
					{
		    			result.setCommand(s);
		    			result.setMessage(ex.getMessage() == null ? "Unable to run query" : ex.getMessage());
		    			result.setRows(-1);
		    			model.addAttribute("result", result);
		    			model.addAttribute("query", s);				    						
					}
				}
				else
				{
    				result = QueryUtil.runCommand(conn, s, qw.getElapsedTime());
    				model.addAttribute("result", result);	
				}
			}
			else
			{
				result = QueryUtil.runCommand(conn, s, qw.getElapsedTime());
				model.addAttribute("result", result);	
			}
		}
		else
		{
			result = QueryUtil.runCommand(conn, s, qw.getElapsedTime());
			model.addAttribute("result", result);
		}
		
    	return "query";    	
    }
    
    private String determineQueryType (String query)
    {
      String sQuery = query.toLowerCase().trim();
      
      if (sQuery.startsWith("select"))
      {
        return decodeType(0);
      }
      else if (sQuery.startsWith("insert"))
      {
        return decodeType(1);
      }
      else if (sQuery.startsWith("delete"))
      {
        return decodeType(2);
      }
      else if (sQuery.startsWith("alter"))
      {
        return decodeType(3);
      }
      else if (sQuery.startsWith("update"))
      {
        return decodeType(4);
      }
      else if (sQuery.startsWith("call"))
      {
        return decodeType(5);
      }
      else if (sQuery.equals("commit;") || sQuery.equals("commit"))
      {
    	return decodeType(6);
      }
      else if (sQuery.equals("rollback;") || sQuery.equals("rollback"))
      {
    	return decodeType(7);
      }      
      else
      {
        return decodeType(3);
      }
    }

    private String decodeType(int type) 
    {
       return QUERY_TYPES[type];
    }
    
    private String[] spiltQuery (String query)
    {
        Pattern pattern = Pattern.compile(";\\s", Pattern.MULTILINE );
        String [] splitQueryStr = pattern.split(query);
		
        logger.debug("split query = {" + Arrays.toString(splitQueryStr) + "}");
        return splitQueryStr;   	
    }
    
    private SortedMap<String, Object> handleMultipleStatements 
    (String[] splitQueryStr, Connection conn, UserPref userPrefs, QueryWindow queryAttribute, HttpSession session) throws SQLException
    {
    	int counter = 9000;
    	
    	SortedMap<String, Object> queryResults = new TreeMap<String, Object>();
    	
    	for (String nextQuery: splitQueryStr)
    	{
    		CommandResult result = new CommandResult();
    		List queryResult = new ArrayList();
    		
    		String s = checkForComments(nextQuery.trim());
    		s = s.trim();

    		if (determineQueryType(s).equals("SELECT"))
    		{
    			Result res = null;
    			try
    			{
    				long start = System.currentTimeMillis();
    				res = QueryUtil.runQuery(conn, s, userPrefs.getMaxRecordsinSQLQueryWindow());
    				long end = System.currentTimeMillis();
    				
		  	        double timeTaken = new Double(end - start).doubleValue();
			        DecimalFormat df = new DecimalFormat("#.##");
    				
    				queryResult.add(s);
    				queryResult.add(res);
		    		
    				if (queryAttribute.getElapsedTime().equals("Y"))
		    		{
		    			queryResult.add(df.format(timeTaken/1000));
		    		}
		    		
    				queryResults.put(counter + "SELECT", queryResult); 
    				addCommandToHistory(session, userPrefs, s);
    			}
	    		catch (Exception ex)
	    		{
	    			result.setCommand(s);
	    			result.setMessage(ex.getMessage() == null ? "Unable to run query" : ex.getMessage());
	    			result.setRows(-1);
	    			queryResults.put(counter + "SELECTERROR", result);
	    		}
    			counter++;
    		}
    		else
    		{
	    		if (s.length() > 0)
	    		{
	    			if (determineQueryType(s).equals("COMMIT"))
	    			{
	    				result = QueryUtil.runCommitOrRollback(conn, true, queryAttribute.getElapsedTime());
	    			}
	    			else if (determineQueryType(s).equals("ROLLBACK"))
	    			{
	    				result = QueryUtil.runCommitOrRollback(conn, false, queryAttribute.getElapsedTime());
	    			}
	    			else
	    			{
	    				result = QueryUtil.runCommand(conn, s, queryAttribute.getElapsedTime());
	    			}
	    			
    				if (result.getMessage().startsWith("SUCCESS"))
    				{
    					addCommandToHistory(session, userPrefs, s);
    				}
	    			
	    			if (determineQueryType(s).equals("INSERT"))
	    			{
	    				queryResults.put(counter + "INSERT", result);
	    			}
	    			else if (determineQueryType(s).equals("UPDATE"))
	    			{
	    				queryResults.put(counter + "UPDATE", result);
	    			}
	    			else if (determineQueryType(s).equals("DELETE"))
	    			{
	    				queryResults.put(counter + "DELETE", result);
	    			} 
	    			else if (determineQueryType(s).equals("COMMIT"))
	    			{
	    				queryResults.put(counter + "COMMI", result);
	    			} 
	    			else if (determineQueryType(s).equals("ROLLBACK"))
	    			{
	    				queryResults.put(counter + "ROLLBACK", result);
	    			} 
	    			else
	    			{
	    				queryResults.put(counter + "DDL", result);
	    			}
	    			
	    			counter++;
	    		}
    		}
    		
    	}
    	
    	return queryResults;
    }
    
	private String checkForComments(String s)
	{
		if (s.startsWith("--"))
		{
			int index = s.indexOf("\n");		
			if (index != -1)
			{
				String newQuery = s.substring(s.indexOf("\n"));
				if (newQuery.trim().startsWith("--"))
				{
					return checkForComments(newQuery.trim());
				}
				else
				{
					return newQuery.trim();
				}
			}
			else
			{
				return "";
			}
		}
		else
		{
			return s;
		}
			
	}  
	
	private String getProcName (String sql)
	{
		String query = sql.toLowerCase().trim();
		String proc = null;
		
		try
		{
			int startIndex = query.indexOf(" ");
			int endIndex = query.indexOf("(");
			
			proc = query.substring(startIndex, (endIndex));
			
			return proc.trim().toUpperCase(); 
		}
		catch (Exception ex)
		{
		  return null;	
		}
		
	}
	
	private void addCommandToHistory (HttpSession session, UserPref prefs, String sql)
	{
		@SuppressWarnings("unchecked")
		LinkedList<String> historyList = (LinkedList<String>) session.getAttribute("history");
		
		int maxsize = prefs.getHistorySize();
		
		if (historyList.size() == maxsize)
		{
			historyList.remove((maxsize - 1));
			historyList.addFirst(sql);
		}
		else
		{
			historyList.addFirst(sql);
		}
		
	}
}
    
