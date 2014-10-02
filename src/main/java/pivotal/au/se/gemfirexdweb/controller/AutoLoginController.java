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
import pivotal.au.se.gemfirexdweb.beans.Login;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.ConnectionManager;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;
import pivotal.au.se.gemfirexdweb.utils.SQLFireJDBCConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;

@Controller
public class AutoLoginController
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/autologin", method = RequestMethod.GET)
    public String autologin
    (Model model,
     HttpSession session,
     HttpServletRequest request) throws Exception
    {
    	logger.debug("Received request to auto login");
    	ConnectionManager cm = ConnectionManager.getInstance();
    	Connection conn;
        String username = null;
        String passwd = null;
        String url = null;

    	try
    	{
            username = fixRequestParam(request.getParameter("username"));
            passwd = fixRequestParam(request.getParameter("passwd"));
            url = fixRequestParam(request.getParameter("url"));

            logger.debug ("username = " + username);
            logger.debug ("passwd = " + passwd);
            logger.debug ("url = " + url);

	    	if (username.trim().equals(""))
	    	{
	    		conn = AdminUtil.getNewConnection(url);
	    	}
	    	else
	    	{
	    		conn = AdminUtil.getNewConnection
	    		  (url,
	    		   username,
	    		   passwd);
	    	}
	    	
	    	SQLFireJDBCConnection newConn = 
	    			new SQLFireJDBCConnection
	    			  (conn,
	    			   url,
	    			   new java.util.Date().toString(),
	    			   username.trim().equals("") ? "APP" : username.toUpperCase());
	    	
	    	cm.addConnection(newConn, session.getId());
	    	
	    	session.setAttribute("user_key", session.getId());
	    	session.setAttribute("schema", username.trim().equals("") ? "APP" : username.toUpperCase());
	    	session.setAttribute("url", url);
	    	session.setAttribute("prefs", new UserPref());
	    	session.setAttribute("history", new LinkedList());
	    	
	    	Map<String, String> schemaMap = AdminUtil.getSchemaMap();
	    	
	    	// get schema count now
	    	schemaMap = QueryUtil.populateSchemaMap
	    			(conn, schemaMap, username.trim().equals("") ? "APP" : username.toUpperCase());
	    	
	    	session.setAttribute("schemaMap", schemaMap);
	    	
	    	// This will resolve to /WEB-INF/jsp/main.jsp
	    	return "main";
    	}
    	catch (Exception ex)
    	{
    		model.addAttribute("error", ex.getMessage());
            Login login = new Login();
            login.setUsername(username);
            login.setUrl(url);

            model.addAttribute("loginAttribute", login);
    		// This will resolve to /WEB-INF/jsp/loginpage.jsp
    		return "loginpage";
    	}

    }

    private String fixRequestParam (String s)
    {
        if (s == null)
        {
            return "";
        }
        else
        {
            return s;
        }
    }
}
