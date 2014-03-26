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
import pivotal.au.se.gemfirexdweb.beans.NewGatewaySender;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStore;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStoreDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;

@Controller
public class CreateGatewaySenderController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/creategatewaysender", method = RequestMethod.GET)
    public String createGatewaySender
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
    	
    	logger.debug("Received request to create a new Gateway Sender");

        DiskStoreDAO dsDAO = GemFireXDWebDAOFactory.getDiskStoreDAO();

        List<DiskStore> dsks = dsDAO.retrieveDiskStoreForCreateList
                ((String)session.getAttribute("user_key"));

        model.addAttribute("diskstores", dsks);
        model.addAttribute("gatewaySenderAttribute", new NewGatewaySender());

    	// This will resolve to /WEB-INF/jsp/create-gatewaysender.jsp
    	return "create-gatewaysender";
    }
	
	@RequestMapping(value = "/creategatewaysender", method = RequestMethod.POST)
    public String createGatewaySenderAction
    (@ModelAttribute("gatewaySenderAttribute") NewGatewaySender gatewaySenderAttribute,
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
    	
    	logger.debug("Received request to action an event for create Gateway Sender");

        DiskStoreDAO dsDAO = GemFireXDWebDAOFactory.getDiskStoreDAO();

        List<DiskStore> dsks = dsDAO.retrieveDiskStoreForCreateList
                ((String)session.getAttribute("user_key"));

        model.addAttribute("diskstores", dsks);

    	String gatewaySenderName = gatewaySenderAttribute.getGatewaySenderName();

    	logger.debug("New Gateway Sender Name = " + gatewaySenderName);
    	
    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");
    	boolean needCloseBracket = false;

    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createGatewaySender = new StringBuffer();
    			
            createGatewaySender.append("CREATE GATEWAYSENDER " + gatewaySenderName + " (\n");
            createGatewaySender.append("REMOTEDSID " + gatewaySenderAttribute.getRemoteSID() + " \n");

            if (!checkIfParameterEmpty(request, "socketBufferSize"))
            {
                createGatewaySender.append("SOCKETBUFFERSIZE " + gatewaySenderAttribute.getSocketBufferSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "socketReadTimeout"))
            {
                createGatewaySender.append("SOCKETREADTIMEOUT " + gatewaySenderAttribute.getSocketReadTimeout() + " \n");
            }

            if (!checkIfParameterEmpty(request, "manualStart"))
            {
                createGatewaySender.append("MANUALSTART " + gatewaySenderAttribute.getManualStart() + " \n");
            }

            if (!checkIfParameterEmpty(request, "enableBatchConflation"))
            {
                createGatewaySender.append("ENABLEBATCHCONFLATION " + gatewaySenderAttribute.getEnableBatchConflation() + " \n");
            }

            if (!checkIfParameterEmpty(request, "batchSize"))
            {
                createGatewaySender.append("BATCHSIZE " + gatewaySenderAttribute.getBatchSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "batchTimeInterval"))
            {
                createGatewaySender.append("BATCHTIMEINTERVAL " + gatewaySenderAttribute.getBatchTimeInterval() + " \n");
            }

            if (!checkIfParameterEmpty(request, "enablePersistence"))
            {
                createGatewaySender.append("ENABLEPERSISTENCE " + gatewaySenderAttribute.getEnablePersistence() + " \n");
                if (gatewaySenderAttribute.getEnablePersistence().equals("TRUE"))
                {
                    if (!checkIfParameterEmpty(request, "diskStore"))
                    {
                        createGatewaySender.append("DISKSTORENAME " + gatewaySenderAttribute.getDiskStore() + " \n");
                    }
                }
            }

            if (!checkIfParameterEmpty(request, "maxQueueMemory"))
            {
                createGatewaySender.append("MAXQUEUEMEMORY " + gatewaySenderAttribute.getMaxQueueMemory() + " \n");
            }

            if (!checkIfParameterEmpty(request, "alertThreshold"))
            {
                createGatewaySender.append("ALERTTHRESHOLD " + gatewaySenderAttribute.getAlertThreshold() + " \n");
            }

            createGatewaySender.append(") \n");

            if (!checkIfParameterEmpty(request, "serverGroups"))
            {
                createGatewaySender.append("SERVER GROUPS (" + gatewaySenderAttribute.getServerGroups() + ") \n");
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating gateway sender as -> " + createGatewaySender.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createGatewaySender.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create Gateway Sender SQL as follows as -> " + createGatewaySender.toString());
                model.addAttribute("sql", createGatewaySender.toString());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, gatewaySenderName));

                ServletOutputStream out = response.getOutputStream();
                out.println(createGatewaySender.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-gatewaysender.jsp
    	return "create-gatewaysender";
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
