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

import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.ConnectionManager;

import java.sql.Connection;

@Controller
public class ConmapController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
    @RequestMapping(value = "/viewconmap", method = RequestMethod.GET)
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

    	logger.debug("Received request to show connection map");

    	ConnectionManager cm = ConnectionManager.getInstance();

        String conMapAction = request.getParameter("conMapAction");
        String key = request.getParameter("key");

        logger.debug("conMapAction = " + conMapAction);
        logger.debug("key = " + key);

        if (conMapAction != null)
        {
          if (conMapAction.equalsIgnoreCase("DELETE"))
          {
              // remove this connection from Map and close it.
              cm.removeConnection(key);
              logger.debug("Connection closed for key " + key);
              model.addAttribute("saved", "Successfully closed connection with key " + key);
          }
        }

    	model.addAttribute("conmap", cm.getConnectionMap());
    	model.addAttribute("conmapsize", cm.getConnectionListSize());
    	
    	// This will resolve to /WEB-INF/jsp/conmap.jsp
    	return "conmap";
    }
}
