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
import pivotal.au.se.gemfirexdweb.beans.NewGatewayReceiver;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

@Controller
public class CreateGatewayReceiverController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/creategatewayreceiver", method = RequestMethod.GET)
    public String createGatewayReceiver
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
    	
    	logger.debug("Received request to create a new Gateway Receiver");

        model.addAttribute("gatewayReceiverAttribute", new NewGatewayReceiver());

    	// This will resolve to /WEB-INF/jsp/create-gatewayreceiver.jsp
    	return "create-gatewayreceiver";
    }
	
	@RequestMapping(value = "/creategatewayreceiver", method = RequestMethod.POST)
    public String createGatewayReceiverAction
    (@ModelAttribute("gatewayReceiverAttribute") NewGatewayReceiver gatewayReceiverAttribute,
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
    	
    	logger.debug("Received request to action an event for create Gateway Receiver");
    	
    	String gatewayReceiverName = gatewayReceiverAttribute.getGatewayReceiverName();

    	logger.debug("New Gateway Receiver Name = " + gatewayReceiverName);
    	
    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");
    	boolean needCloseBracket = false;

    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createGatewayReceiver = new StringBuffer();
    			
            createGatewayReceiver.append("CREATE GATEWAYRECEIVER " + gatewayReceiverName + " (\n");

            if (!checkIfParameterEmpty(request, "bindAddress"))
            {
                createGatewayReceiver.append("BINDADDRESS '" + gatewayReceiverAttribute.getBindAddress() + "' \n");
                needCloseBracket = true;
            }

            if (!checkIfParameterEmpty(request, "startPort"))
            {
                createGatewayReceiver.append("STARTPORT " + gatewayReceiverAttribute.getStartPort() + " \n");
                needCloseBracket = true;
            }

            if (!checkIfParameterEmpty(request, "endPort"))
            {
                createGatewayReceiver.append("ENDPORT " + gatewayReceiverAttribute.getEndPort() + " \n");
                needCloseBracket = true;
            }

            if (!checkIfParameterEmpty(request, "socketBufferSize"))
            {
                createGatewayReceiver.append("SOCKETBUFFERSIZE " + gatewayReceiverAttribute.getSocketBufferSize() + " \n");
                needCloseBracket = true;
            }

            if (!checkIfParameterEmpty(request, "maxTimeBetweenPings"))
            {
                createGatewayReceiver.append("MAXTIMEBETWEENPINGS " + gatewayReceiverAttribute.getMaxTimeBetweenPings() + " \n");
                needCloseBracket = true;
            }

            if (needCloseBracket)
            {
                createGatewayReceiver.append(") ");
            }

            if (!checkIfParameterEmpty(request, "serverGroups"))
            {
                createGatewayReceiver.append("SERVER GROUPS (" + gatewayReceiverAttribute.getServerGroups() + ") \n");
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating gateway receiver as -> " + createGatewayReceiver.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createGatewayReceiver.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create Async SQL as follows as -> " + createGatewayReceiver.toString());
                model.addAttribute("sql", createGatewayReceiver.toString());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, gatewayReceiverName));

                ServletOutputStream out = response.getOutputStream();
                out.println(createGatewayReceiver.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-gatewayreceiver.jsp
    	return "create-gatewayreceiver";
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
