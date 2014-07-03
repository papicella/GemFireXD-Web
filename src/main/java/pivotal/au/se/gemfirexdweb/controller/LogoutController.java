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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.se.gemfirexdweb.beans.Login;
import pivotal.au.se.gemfirexdweb.utils.ConnectionManager;

@Controller
public class LogoutController 
{
	protected static Logger logger = Logger.getLogger("controller");

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout
      (Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request) throws Exception 
    {
    	logger.debug("Received request to logout of GemFireXD*Web");

    	// remove connection from list
    	ConnectionManager cm = ConnectionManager.getInstance();
    	cm.removeConnection(session.getId());
    	
    	session.invalidate();

        model.addAttribute("loginAttribute", new Login());
        // This will resolve to /WEB-INF/jsp/loginpage.jsp
        return "loginpage";

    	//response.sendRedirect(request.getContextPath() + "/GemFireXD-Web/login");
    	//return null;
    }
}
