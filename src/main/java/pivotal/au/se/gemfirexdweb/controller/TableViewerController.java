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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.asyncevent.Asyncevent;
import pivotal.au.se.gemfirexdweb.dao.asyncevent.AsynceventDAO;
import pivotal.au.se.gemfirexdweb.dao.constraints.Constraint;
import pivotal.au.se.gemfirexdweb.dao.constraints.ConstraintDAO;
import pivotal.au.se.gemfirexdweb.dao.gatewaysenders.GatewaySender;
import pivotal.au.se.gemfirexdweb.dao.gatewaysenders.GatewaySenderDAO;
import pivotal.au.se.gemfirexdweb.dao.tables.Table;
import pivotal.au.se.gemfirexdweb.dao.tables.TableDAO;
import pivotal.au.se.gemfirexdweb.main.JavaCodeReader;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TableViewerController
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/tableviewer", method = RequestMethod.GET)
    public String browseTable
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		int startAtIndex = 0, endAtIndex = 0;
		javax.servlet.jsp.jstl.sql.Result asyncEventListeners, tableStructure, datalocations,
                                          sampledata, allTableInfoResult, tableGatewaySenders,
                                          tableTriggersResult, tablePrivsResult = null;
		String schema = null;
        Connection conn = null;

    	if (session.getAttribute("user_key") == null)
    	{
    		logger.debug("user_key is null new Login required");
    		response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
    		return null;
    	}
        else
        {
            conn = AdminUtil.getConnection((String)session.getAttribute("user_key"));
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
    	
    	logger.debug("Received request to show table viewer");
    	
    	TableDAO tableDAO = GemFireXDWebDAOFactory.getTableDAO();
        AsynceventDAO asyncDAO = GemFireXDWebDAOFactory.getAsynceventDAO();
        ConstraintDAO conDAO = GemFireXDWebDAOFactory.getConstraintDAO();
        GatewaySenderDAO gsDAO = GemFireXDWebDAOFactory.getGatewaySenderDAO();

        UserPref userPrefs = (UserPref) session.getAttribute("prefs");

        JavaCodeReader jcReader = JavaCodeReader.getInstance();

    	String tabName = request.getParameter("tabName");
        logger.debug("tableName = " + tabName);
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

        String addasync = request.getParameter("addasync");
        if (addasync == null)
        {
            addasync = "N";
        }

        logger.debug("addasync = " + addasync);

        String addgateway = request.getParameter("addgateway");
        if (addgateway == null)
        {
            addgateway = "N";
        }

        logger.debug("addgateway = " + addgateway);

        String clearasync = request.getParameter("clearasync");
        if (clearasync == null)
        {
          clearasync = "N";
        }

        logger.debug("clearasync = " + clearasync);

        String cleargatewaysender = request.getParameter("cleargatewaysender");
        if (cleargatewaysender == null)
        {
            cleargatewaysender = "N";
        }

        logger.debug("cleargatewaysender = " + cleargatewaysender);

        String addpriv = request.getParameter("addpriv");
        if (addpriv == null)
        {
            addpriv = "N";
        }

        logger.debug("addpriv = " + addpriv);

        Result result;

        if (addasync.equals("Y"))
        {
            result = tableDAO.addAsyncEventListener
                    (schema,
                     tabName,
                     (String)request.getParameter("asynceventid"),
                     (String)request.getParameter("curasynclisteners"),
                     (String)session.getAttribute("user_key"));

            model.addAttribute("result", result);
        }
        else if (clearasync.equals("Y"))
        {
            result = tableDAO.simpletableCommand
                    (schema,
                     tabName,
                     "REMOVEALLASYNC",
                     (String)session.getAttribute("user_key"));

            model.addAttribute("result", result);
        }
        else if (addgateway.equals("Y"))
        {
            result = tableDAO.addGatewaySender
                    (schema,
                     tabName,
                     (String)request.getParameter("gatewaysender"),
                     (String)request.getParameter("curgatewaysenders"),
                     (String)session.getAttribute("user_key"));

            model.addAttribute("result", result);
        }
        else if (cleargatewaysender.equals("Y"))
        {
            result = tableDAO.simpletableCommand
                    (schema,
                     tabName,
                     "REMOVEALLGATEWAYSENDERS",
                     (String)session.getAttribute("user_key"));

            model.addAttribute("result", result);
        }
        else if (addpriv.equals("Y"))
        {
            result = tableDAO.performPrivilege
                    (schema,
                     tabName,
                     (String)request.getParameter("privType"),
                     (String) request.getParameter("privOption"),
                     (String) request.getParameter("privTo"),
                     (String)session.getAttribute("user_key"));

            model.addAttribute("result", result);
        }

        datalocations = tableDAO.getDataLocations(schema, tabName, (String)session.getAttribute("user_key"));

        tableStructure =
                tableDAO.getTableStructure
                        (schema,
                         (String)request.getParameter("tabName"),
                         (String)session.getAttribute("user_key"));

        asyncEventListeners =
                tableDAO.getTableAsyncListeners(tabName, (String)session.getAttribute("user_key"));

        allTableInfoResult =
                tableDAO.getAllTableInfo
                        (schema,
                         (String)request.getParameter("tabName"),
                         (String)session.getAttribute("user_key"));

        String querysql = String.format("select * from %s.%s FETCH FIRST 10 ROWS ONLY", schema, tabName);

        sampledata = QueryUtil.runQuery(conn,
                                        querysql,
                                        userPrefs.getMaxRecordsinSQLQueryWindow());

        List<Asyncevent> asyncevents =
                asyncDAO.retrieveAsynceventListForAdd((String)session.getAttribute("user_key"));

        List<Constraint> cons =
                conDAO.retrieveTableConstraintList
                        (schema,
                         tabName,
                         (String)session.getAttribute("user_key"));

        tableGatewaySenders =
                gsDAO.getGatewaySendersForTable
                        ((String)request.getParameter("tabName"),
                         (String)session.getAttribute("user_key"));

        List<GatewaySender> gatewaysenders =
            gsDAO.retrieveGatewaySenderForAdd((String)session.getAttribute("user_key"));

        tableTriggersResult =
                tableDAO. getTableTriggers
                        (schema,
                        (String)request.getParameter("tabName"),
                        (String)session.getAttribute("user_key"));

        tablePrivsResult =
                tableDAO.getTablePrivs
                        (schema,
                        (String)request.getParameter("tabName"),
                        (String)session.getAttribute("user_key"));

        model.addAttribute("schemas",
                GemFireXDWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        model.addAttribute("tablePrivsResult", tablePrivsResult);
        model.addAttribute("tableTriggersResult", tableTriggersResult);
        model.addAttribute("tableGatewaySenders", tableGatewaySenders);
        model.addAttribute("gatewaySendersForAdd", gatewaysenders);

        model.addAttribute("cons", cons);
        model.addAttribute("asyncEventListenersForAdd", asyncevents);

        model.addAttribute("tableStructure", tableStructure);
        model.addAttribute("asyncEventListeners", asyncEventListeners);
        model.addAttribute("allTableInfoResult", allTableInfoResult);
        model.addAttribute("sampledata", sampledata);
        model.addAttribute("querysql", querysql);
        model.addAttribute("tablename", (String)request.getParameter("tabName"));

        model.addAttribute("dataLocationResults", datalocations);
        model.addAttribute("chosenSchema", schema);

        model.addAttribute("eventListener",
                           String.format(jcReader.getJavaCodeBean("callbackListener").getCode(), initCap((String)request.getParameter("tabName"))));

        model.addAttribute("asyncListener",
                String.format(jcReader.getJavaCodeBean("asyncListener").getCode(), initCap((String)request.getParameter("tabName"))));

    	// This will resolve to /WEB-INF/jsp/tableviewer.jsp
    	return "tableviewer";
    }

    private String initCap (String input)
    {
        StringBuilder ff = new StringBuilder();

        for(String f: input.split(" "))
        {
            if(ff.length()>0) {
                ff.append(" ");
            }
            ff.append(f.substring(0,1).toUpperCase()).append(f.substring(1,f.length()).toLowerCase());
        }

        return ff.toString().replace("_", "");
    }
}
