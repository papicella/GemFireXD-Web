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
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.se.gemfirexdweb.beans.NewTable;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStore;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStoreDAO;
import pivotal.au.se.gemfirexdweb.dao.hdfsstores.HdfsStore;
import pivotal.au.se.gemfirexdweb.dao.hdfsstores.HdfsStoreDAO;
import pivotal.au.se.gemfirexdweb.dao.types.Type;
import pivotal.au.se.gemfirexdweb.dao.types.TypeDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class CreateTableController 
{
	protected static Logger logger = Logger.getLogger("controller");
	private static final String FILENAME = "%s.sql";
	private static final String SAVE_CONTENT_TYPE = "application/x-download";
	
	@RequestMapping(value = "/createtable", method = RequestMethod.GET)
    public String createTable
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
    	
    	logger.debug("Received request to create a new table");
    	
    	session.setAttribute("numColumns", "0");
    	session.setAttribute("tabName", "newtable");
    			
    	model.addAttribute("numColumns", "0");

        DiskStoreDAO dsDAO = GemFireXDWebDAOFactory.getDiskStoreDAO();
        HdfsStoreDAO hdfsDAO = GemFireXDWebDAOFactory.getHdfsStoreDAO();

        List<DiskStore> dsks = dsDAO.retrieveDiskStoreForCreateList
                ((String)session.getAttribute("user_key"));

        List<HdfsStore> hdfs = hdfsDAO.retrieveHdfsStoreForCreateList
                ((String)session.getAttribute("user_key"));

        model.addAttribute("diskstores", dsks);
        model.addAttribute("hdfsstores", hdfs);

        model.addAttribute("tableAttribute", new NewTable());

    	// This will resolve to /WEB-INF/jsp/create-table.jsp
    	return "create-table";
    }
	
	@RequestMapping(value = "/createtable", method = RequestMethod.POST)
    public String createTableAction
    (@ModelAttribute("tableAttribute") NewTable tableAttribute,
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
    	
    	logger.debug("Received request to action an event for create table");
    	
    	String tabName = tableAttribute.getTableName();
    	
    	String[] columnNames  = request.getParameterValues("column_name[]");
    	String[] columnTypes  = request.getParameterValues("column_type[]");
    	String[] columnPrecision  = request.getParameterValues("column_precision[]");
    	String[] columnDefaultValues  = request.getParameterValues("column_default_value[]");
    	String[] columnSelectedNulls  = request.getParameterValues("column_selected_null[]");
    	String[] columnSelectedPrimaryKeys  = request.getParameterValues("column_selected_primary_key[]");
    	String[] columnSelectedAutoIncrements  = request.getParameterValues("column_selected_auto_increment[]");
    	
    	logger.debug("New Table Name = " + tabName);
    	logger.debug("columnNames = " + Arrays.toString(columnNames));
    	logger.debug("columnTypes = " + Arrays.toString(columnTypes));
    	logger.debug("columnPrecision = " + Arrays.toString(columnPrecision));
    	logger.debug("columnDefaultValues = " + Arrays.toString(columnDefaultValues));
    	logger.debug("columnSelectedNulls = " + Arrays.toString(columnSelectedNulls));
    	logger.debug("columnSelectedPrimaryKeys = " + Arrays.toString(columnSelectedPrimaryKeys));
    	logger.debug("columnSelectedAutoIncrements = " + Arrays.toString(columnSelectedAutoIncrements));
    	
    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");

        DiskStoreDAO dsDAO = GemFireXDWebDAOFactory.getDiskStoreDAO();
        HdfsStoreDAO hdfsDAO = GemFireXDWebDAOFactory.getHdfsStoreDAO();

        List<DiskStore> dsks = dsDAO.retrieveDiskStoreForCreateList
                ((String)session.getAttribute("user_key"));

        List<HdfsStore> hdfs = hdfsDAO.retrieveHdfsStoreForCreateList
                ((String)session.getAttribute("user_key"));

        model.addAttribute("diskstores", dsks);
        model.addAttribute("hdfsstores", hdfs);

    	if (submit != null)
    	{

    		if (submit.equalsIgnoreCase("Column(s)"))
    		{
    	    	TypeDAO typeDAO = GemFireXDWebDAOFactory.getTypeDAO();
    	    	List<Type> types = typeDAO.retrieveTypeList
    	    			((String)session.getAttribute("schema"), 
    	    			 null, 
    	    			 (String)session.getAttribute("user_key"));
    	    	
    	    	model.addAttribute("types", types); 
    	    	
    	    	int cols = Integer.parseInt(request.getParameter("numColumns"));
    	    	int numColumns = Integer.parseInt((String)session.getAttribute("numColumns"));
    	    	
    	    	numColumns = numColumns + cols;
    	    	
    	    	session.setAttribute("numColumns", "" + numColumns);
    	    	session.setAttribute("tabName", tableAttribute.getTableName());
    	    	
    	    	model.addAttribute("numColumns", numColumns);
    	    	model.addAttribute("tabName", tableAttribute.getTableName());
                model.addAttribute("hdfsStore", tableAttribute.getHdfsStore());
                model.addAttribute("diskStore", tableAttribute.getDiskStore());
    		}
    		else
    		{
    			// build create table SQL 
    			StringBuffer createTable = new StringBuffer();
    			String schema = tableAttribute.getSchemaName();
                if (schema.length() == 0)
                {
                   schema = (String)session.getAttribute("schema");
                }

    			createTable.append("create table " + schema + "." + tabName + " \n");
    			createTable.append("(");
    			
    			int i = 0;
    			String val = null;

                if (columnNames != null)
                {
                    int size = columnNames.length;
                    for (String columnName: columnNames)
                    {
                      createTable.append(columnName + " ");
                      createTable.append(columnTypes[i]);

                      // doing precision / size
                      val = checkIfEntryExists(columnPrecision, i);
                      if (val != null)
                      {
                          createTable.append("(" + columnPrecision[i] + ")");
                      }
                      val = null;

                      // doing auto increment check here
                      val = checkIfEntryExists(columnSelectedAutoIncrements, i);
                      if (val != null)
                      {
                          // should check for column type here
                          if (val.equalsIgnoreCase("Y"))
                          {
                              createTable.append(" generated always as identity");
                          }
                      }
                      val = null;

                      // doing default value
                      val = checkIfEntryExists(columnDefaultValues, i);
                      if (val != null)
                      {
                          // should check for column type here
                          createTable.append(" default " + columnDefaultValues[i]);
                      }
                      val = null;

                      // doing not null check here
                      val = checkIfEntryExists(columnSelectedNulls, i);
                      if (val != null)
                      {
                          if (val.equalsIgnoreCase("Y"))
                          {
                              createTable.append(" NOT NULL");
                          }
                      }
                      val = null;

                      // doing primary key check here
                      val = checkIfEntryExists(columnSelectedPrimaryKeys, i);
                      if (val != null)
                      {
                          if (val.equalsIgnoreCase("Y"))
                          {
                              createTable.append(" CONSTRAINT " + tabName + "_PK Primary Key");
                          }
                      }
                      val = null;

                      int j = size - 1;
                      if (i < j)
                      {
                          createTable.append(",\n");
                      }

                      i++;
                    }

                }

    			createTable.append(")\n");
    			
    			if (request.getParameter("dataPolicy").equalsIgnoreCase("REPLICATE"))
    			{
    			    createTable.append("REPLICATE\n");	
    			}
    			
    			if (!checkIfParameterEmpty(request, "serverGroups"))
    			{
    				createTable.append("SERVER GROUPS (" + tableAttribute.getServerGroups() + ")\n");
    			}

    			if (!checkIfParameterEmpty(request, "persistant"))
    			{
    				if (tableAttribute.getPersistant().equalsIgnoreCase("Y"))
    				{
    					createTable.append("PERSISTENT ");
        				if (!checkIfParameterEmpty(request, "diskStore"))
        				{
        					createTable.append("'" + tableAttribute.getDiskStore() + "' ");
        					if (!checkIfParameterEmpty(request, "persistenceType"))
        					{
        						createTable.append(tableAttribute.getPersistenceType() + "\n");
        					}
        					else
        					{
        						createTable.append("\n");
        					}
        					
        				}
    				}
    			}

    			if (request.getParameter("dataPolicy").equalsIgnoreCase("PARTITION"))
    			{
        			if (!checkIfParameterEmpty(request, "partitionBy"))
        			{
        				createTable.append("PARTITION BY " + tableAttribute.getPartitionBy() + "\n");
        			}  		
        			
    				if (!checkIfParameterEmpty(request, "colocateWith"))
    				{
    					createTable.append("COLOCATE WITH (" + tableAttribute.getColocateWith() + ")\n");
    				}

    				if (!checkIfParameterEmpty(request, "redundancy"))
    				{
    					createTable.append("REDUNDANCY " + tableAttribute.getRedundancy() + "\n");
    				}
    			}

                if (!checkIfParameterEmpty(request, "hdfsStore"))
                {
                    createTable.append("HDFSSTORE (" + tableAttribute.getHdfsStore() + ") ");
                    if (request.getParameter("writeonly").equalsIgnoreCase("Y"))
                    {
                        createTable.append("WRITEONLY\n");
                    }
                    else
                    {
                        // need to ad eviction properties here
                        if (!checkIfParameterEmpty(request, "evictionbycriteria"))
                        {
                            createTable.append("EVICTION BY CRITERIA (" + tableAttribute.getEvictionbycriteria() + ")\n");
                        }

                        if (!checkIfParameterEmpty(request, "evictionfrequency"))
                        {
                            createTable.append("EVICTION FREQUENCY " + tableAttribute.getEvictionfrequency() + "\n");
                        }
                        else
                        {
                            if (request.getParameter("evictincoming").equalsIgnoreCase("Y"))
                            {
                                createTable.append("EVICT INCOMING\n");
                            }
                        }
                    }

                }

                if (!checkIfParameterEmpty(request, "gatewaysender"))
                {
                    createTable.append("GATEWAYSENDER (" + tableAttribute.getGatewaysender() + ")\n");
                }

                if (!checkIfParameterEmpty(request, "asynceventlistener"))
                {
                    createTable.append("ASYNCEVENTLISTENER (" + tableAttribute.getAsynceventlistener() + ")\n");
                }

                if (!checkIfParameterEmpty(request, "offheap"))
                {
                    if (request.getParameter("offheap").equalsIgnoreCase("Y"))
                    {
                        createTable.append("OFFHEAP\n");
                    }
                }

                if (!checkIfParameterEmpty(request, "other"))
    			{
    				createTable.append(tableAttribute.getOther() + "\n");
    			}
    			
        		if (submit.equalsIgnoreCase("create"))
        		{
        			Result result = new Result();
        			
        			logger.debug("Creating table as -> " + createTable.toString());
        			
        			result = GemFireXDWebDAOUtil.runCommand
                            (createTable.toString(),
                                    (String) session.getAttribute("user_key"));
        			
        			model.addAttribute("result", result);
                    model.addAttribute("hdfsStore", tableAttribute.getHdfsStore());
                    model.addAttribute("diskStore", tableAttribute.getDiskStore());
        			
        		}   
        		else if (submit.equalsIgnoreCase("Show SQL"))
        		{
        			logger.debug("Table SQL as follows as -> " + createTable.toString());
        			model.addAttribute("sql", createTable.toString());
                    model.addAttribute("hdfsStore", tableAttribute.getHdfsStore());
                    model.addAttribute("diskStore", tableAttribute.getDiskStore());
        		}
        		else if (submit.equalsIgnoreCase("Save to File"))
        		{
                    response.setContentType(SAVE_CONTENT_TYPE);
                    response.setHeader("Content-Disposition", "attachment; filename=" + 
                                       String.format(FILENAME, tabName));
                    
                    ServletOutputStream out = response.getOutputStream();
                    out.println(createTable.toString());
                    out.close();
                    return null;
        		}
    		}
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-table.jsp
    	return "create-table";
    }
	
	private String checkIfEntryExists (String[] attribute, int index)
	{
		if (attribute != null)
		{
			try
			{
				String val = attribute[index];
				if (val.trim().length() == 0)
				{
					return null;
				}
				else
				{
					return val;
				}
			}
			catch (Exception ex)
			{
				return null;
			}
		}
		else
		{
			return null;
		}
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
