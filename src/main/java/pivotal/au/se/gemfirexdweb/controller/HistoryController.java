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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class HistoryController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/history", method = RequestMethod.GET)
    public String showHistory
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
    	
    	logger.debug("Received request to show command history");
		UserPref userPref = (UserPref) session.getAttribute("prefs");
		
		String histAction = request.getParameter("histAction");
		
		if (histAction != null)
		{
			logger.debug("histAction = " + histAction);	
			// clear history
			session.setAttribute("history", new LinkedList());
			
			model.addAttribute("historyremoved", "Succesfully cleared history list");
		}
		
		LinkedList historyList = (LinkedList) session.getAttribute("history");
		
		int maxsize = userPref.getHistorySize();
		
		model.addAttribute("historyList", historyList.toArray());
		model.addAttribute("historysize", historyList.size());
		
    	// This will resolve to /WEB-INF/jsp/history.jsp
    	return "history";
    }
}

