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
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.se.gemfirexdweb.beans.Login;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.ConnectionManager;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;
import pivotal.au.se.gemfirexdweb.utils.SQLFireJDBCConnection;

@Controller
public class LoginController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) 
    {
    	logger.debug("Received request to show login page");
    	// Create new QueryWindow and add to model
    	// This is the formBackingObject
    	model.addAttribute("loginAttribute", new Login());
    	// This will resolve to /WEB-INF/jsp/loginpage.jsp
    	return "loginpage";
    }
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login
    (@ModelAttribute("loginAttribute") Login loginAttribute, 
     Model model, 
     HttpSession session) throws Exception
    {
    	logger.debug("Received request to login");
    	ConnectionManager cm = ConnectionManager.getInstance();
    	Connection conn;
    	
    	try
    	{
	    	if (loginAttribute.getUsername().trim().equals(""))
	    	{
	    		conn = AdminUtil.getNewConnection(loginAttribute.getUrl());
	    	}
	    	else
	    	{
	    		conn = AdminUtil.getNewConnection
	    		  (loginAttribute.getUrl(), 
	    		   loginAttribute.getUsername(),
	    		   loginAttribute.getPassword());
	    	}
	    	
	    	SQLFireJDBCConnection newConn = 
	    			new SQLFireJDBCConnection
	    			  (conn,
	    			   loginAttribute.getUrl(),
	    			   new java.util.Date().toString(),
	    			   loginAttribute.getUsername().trim().equals("") ? "APP" : loginAttribute.getUsername().toUpperCase());
	    	
	    	cm.addConnection(newConn, session.getId());
	    	
	    	session.setAttribute("user_key", session.getId());
	    	session.setAttribute("schema", loginAttribute.getUsername().trim().equals("") ? "APP" : loginAttribute.getUsername().toUpperCase());
	    	session.setAttribute("url", loginAttribute.getUrl());
	    	session.setAttribute("prefs", new UserPref());
	    	session.setAttribute("history", new LinkedList());
	    	session.setAttribute("connectedAt", new java.util.Date().toString());

	    	Map<String, String> schemaMap = AdminUtil.getSchemaMap();
	    	
	    	// get schema count now
	    	schemaMap = QueryUtil.populateSchemaMap
	    			(conn, schemaMap, loginAttribute.getUsername().trim().equals("") ? "APP" : loginAttribute.getUsername().toUpperCase());
	    	
	    	session.setAttribute("schemaMap", schemaMap);
	    	
	    	logger.info(loginAttribute);
	    	
	    	// This will resolve to /WEB-INF/jsp/main.jsp
	    	return "main";
    	}
    	catch (Exception ex)
    	{
    		model.addAttribute("error", ex.getMessage());
    		// This will resolve to /WEB-INF/jsp/loginpage.jsp
    		return "loginpage";
    	}
    }
}
