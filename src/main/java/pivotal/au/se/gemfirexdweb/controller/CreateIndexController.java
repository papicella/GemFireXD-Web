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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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

import pivotal.au.se.gemfirexdweb.beans.IndexDefinition;
import pivotal.au.se.gemfirexdweb.beans.NewIndex;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.indexes.IndexColumn;
import pivotal.au.se.gemfirexdweb.dao.indexes.IndexDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class CreateIndexController 
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createindex", method = RequestMethod.GET)
    public String createIndex
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
    	
    	logger.debug("Received request to create a new index");
    	
    	String tabName = (String)request.getParameter("tabName");
    	String schema = (String)request.getParameter("schemaName");
    	
    	logger.debug("Table = " + schema + "." + tabName);
    	
    	IndexDAO indexDAO = GemFireXDWebDAOFactory.getIndexDAO();
    	
    	List<IndexColumn> columns = indexDAO.retrieveIndexColumns
    			(schema, tabName, (String)session.getAttribute("user_key"));
    	
    	model.addAttribute("tabName", tabName);
    	model.addAttribute("tableSchemaName", schema);
    	model.addAttribute("columns", columns);
    	model.addAttribute("schema", schema);

        model.addAttribute("indexAttribute", new NewIndex());

    	// This will resolve to /WEB-INF/jsp/create-index.jsp
    	return "create-index";
    }
	
	@RequestMapping(value = "/createindex", method = RequestMethod.POST)
    public String createIndexAction
    (
        @ModelAttribute("indexAttribute") NewIndex indexAttribute,
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
    	
    	logger.debug("Received request to action an event for create index");
    	
    	String tabName = indexAttribute.getTableName();
    	String schema = indexAttribute.getSchemaName();
    	String unique = indexAttribute.getUnique();
    	String idxName = indexAttribute.getIndexName();
        String submit = request.getParameter("pSubmit");

    	String[] selectedColumns  = request.getParameterValues("selected_column[]");
    	String[] indexOrder  = request.getParameterValues("idxOrder[]");
    	String[] position  = request.getParameterValues("position[]");
    	
    	logger.debug("selectedColumns = " + Arrays.toString(selectedColumns));
    	logger.debug("indexOrder = " + Arrays.toString(indexOrder));
    	logger.debug("position = " + Arrays.toString(position));

        IndexDAO indexDAO = GemFireXDWebDAOFactory.getIndexDAO();

        List<IndexColumn> columns = indexDAO.retrieveIndexColumns
                (schema, tabName, (String)session.getAttribute("user_key"));

    	StringBuffer createIndex = new StringBuffer();
    	
    	if (unique.equalsIgnoreCase("Y"))
    	{
    		createIndex.append("create unique index " + idxName + " on " + schema + "." + tabName + " " );
    	}
    	else
    	{
    		createIndex.append("create index " + idxName + " on " + schema + "." + tabName + " ");
    	}
    	
    	createIndex.append("(");

        if (selectedColumns != null)
        {
            int i = 0;
            Map<String, IndexDefinition> cols = new HashMap<String, IndexDefinition>();

            for (String column: selectedColumns)
            {
                String columnName = column.substring(0, column.indexOf("_"));
                String index = column.substring(column.indexOf("_") + 1);
                String pos = position[Integer.parseInt(index) - 1];
                if (pos.trim().length() == 0)
                {
                    pos = "" + i;
                }


                logger.debug("Column = " + columnName + ", indexOrder = " +
                             indexOrder[Integer.parseInt(index) - 1] + ", position = " +
                             pos);

                IndexDefinition idxDef = new IndexDefinition(columnName, indexOrder[Integer.parseInt(index) - 1]);

                cols.put("" + pos, idxDef);
                i++;
            }

            // sort map and create index now
            SortedSet<String> keys = new TreeSet<String>(cols.keySet());
            int length = keys.size();
            i = 0;
            for (String key : keys)
            {
                IndexDefinition idxDefTemp = cols.get(key);
                if (i == (length - 1))
                {
                    createIndex.append(idxDefTemp.getColumnName() + " " + idxDefTemp.getOrderType() + ")");
                }
                else
                {
                    createIndex.append(idxDefTemp.getColumnName() + " " + idxDefTemp.getOrderType() + ", ");
                }

                i++;

            }
        }

        if (!checkIfParameterEmpty(request, "caseSensitive"))
        {
            createIndex.append(" " + request.getParameter("caseSensitive") + "\n");
        }

        if (submit.equalsIgnoreCase("create"))
        {
            Result result = new Result();

            logger.debug("Creating index as -> " + createIndex.toString());

            result = GemFireXDWebDAOUtil.runCommand
                    (createIndex.toString(),
                    (String) session.getAttribute("user_key"));

            model.addAttribute("result", result);
            model.addAttribute("tabName", tabName);
            model.addAttribute("tableSchemaName", schema);
            model.addAttribute("columns", columns);
            model.addAttribute("schema", schema);

            session.removeAttribute("numColumns");
        }
        else if (submit.equalsIgnoreCase("Show SQL"))
        {
            logger.debug("Index SQL as follows as -> " + createIndex.toString());
            model.addAttribute("sql", createIndex.toString());
            model.addAttribute("tabName", tabName);
            model.addAttribute("tableSchemaName", schema);
            model.addAttribute("columns", columns);
            model.addAttribute("schema", schema);
        }
        else if (submit.equalsIgnoreCase("Save to File"))
        {
            response.setContentType(SAVE_CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    String.format(FILENAME, idxName));

            ServletOutputStream out = response.getOutputStream();
            out.println(createIndex.toString());
            out.close();
            return null;
        }
    	// This will resolve to /WEB-INF/jsp/create-index.jsp
    	return "create-index";
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
