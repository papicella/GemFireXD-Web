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
import pivotal.au.se.gemfirexdweb.beans.NewAsync;
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
public class CreateAsyncController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createasync", method = RequestMethod.GET)
    public String createAsync
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
    	
    	logger.debug("Received request to create a new Async Event Listener");

        DiskStoreDAO dsDAO = GemFireXDWebDAOFactory.getDiskStoreDAO();

        List<DiskStore> dsks = dsDAO.retrieveDiskStoreForCreateList
                ((String)session.getAttribute("user_key"));

        model.addAttribute("diskstores", dsks);
        model.addAttribute("asyncAttribute", new NewAsync());

    	// This will resolve to /WEB-INF/jsp/create-async.jsp
    	return "create-async";
    }
	
	@RequestMapping(value = "/createasync", method = RequestMethod.POST)
    public String createAsyncAction
    (@ModelAttribute("asyncAttribute") NewAsync asyncAttribute,
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
    	
    	logger.debug("Received request to action an event for create Async Event Listener");

        DiskStoreDAO dsDAO = GemFireXDWebDAOFactory.getDiskStoreDAO();

        List<DiskStore> dsks = dsDAO.retrieveDiskStoreForCreateList
                ((String)session.getAttribute("user_key"));

        model.addAttribute("diskstores", dsks);

    	String asyncName = asyncAttribute.getAsyncName();
        String listenerClass = asyncAttribute.getListenerClass();

    	logger.debug("New Async Event Listener Name = " + asyncName);
        logger.debug("New Async Event Listener Class = " + listenerClass);

    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");
    	boolean needCloseBracket = false;

    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createAsync = new StringBuffer();
    			
            createAsync.append("CREATE ASYNCEVENTLISTENER " + asyncName + " (\n");
            createAsync.append("LISTENERCLASS '" + listenerClass + "' \n");

            if (!checkIfParameterEmpty(request, "initParams"))
            {
                createAsync.append("INITPARAMS " + asyncAttribute.getInitParams() + " \n");
            }
            else
            {
                createAsync.append("INITPARAMS '' \n");
            }

            if (!checkIfParameterEmpty(request, "manualStart"))
            {
                createAsync.append("MANUALSTART " + asyncAttribute.getManualStart() + " \n");
            }

            if (!checkIfParameterEmpty(request, "enableBatchConflation"))
            {
                createAsync.append("ENABLEBATCHCONFLATION " + asyncAttribute.getEnableBatchConflation() + " \n");
            }

            if (!checkIfParameterEmpty(request, "batchSize"))
            {
                createAsync.append("BATCHSIZE " + asyncAttribute.getBatchSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "batchTimeInterval"))
            {
                createAsync.append("BATCHTIMEINTERVAL " + asyncAttribute.getBatchTimeInterval() + " \n");
            }

            if (!checkIfParameterEmpty(request, "enablePersistence"))
            {
                createAsync.append("ENABLEPERSISTENCE " + asyncAttribute.getEnablePersistence() + " \n");
                if (asyncAttribute.getEnablePersistence().equals("TRUE"))
                {
                    if (!checkIfParameterEmpty(request, "diskStore"))
                    {
                        createAsync.append("DISKSTORENAME " + asyncAttribute.getDiskStore() + " \n");
                    }
                }
            }

            if (!checkIfParameterEmpty(request, "maxQueueMemory"))
            {
                createAsync.append("MAXQUEUEMEMORY " + asyncAttribute.getMaxQueueMemory() + " \n");
            }

            if (!checkIfParameterEmpty(request, "alertThreshold"))
            {
                createAsync.append("ALERTTHRESHOLD " + asyncAttribute.getAlertThreshold() + " \n");
            }

            createAsync.append(") \n");

            if (!checkIfParameterEmpty(request, "serverGroups"))
            {
                createAsync.append("SERVER GROUPS (" + asyncAttribute.getServerGroups() + ") \n");
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating async event listener as -> " + createAsync.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createAsync.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create Async SQL as follows as -> " + createAsync.toString());
                model.addAttribute("sql", createAsync.toString());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, asyncName));

                ServletOutputStream out = response.getOutputStream();
                out.println(createAsync.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-async.jsp
    	return "create-async";
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
