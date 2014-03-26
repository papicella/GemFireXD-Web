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
import pivotal.au.se.gemfirexdweb.beans.NewDiskStore;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

@Controller
public class CreateDiskStoreController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/creatediskstore", method = RequestMethod.GET)
    public String createDiskStore
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
    	
    	logger.debug("Received request to create a new Disk Store");

        model.addAttribute("diskStoreAttribute", new NewDiskStore());

    	// This will resolve to /WEB-INF/jsp/create-hdfsstore.jsp
    	return "create-diskstore";
    }
	
	@RequestMapping(value = "/creatediskstore", method = RequestMethod.POST)
    public String createHDFSStoreAction
    (@ModelAttribute("diskStoreAttribute") NewDiskStore diskStoreAttribute,
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
    	
    	logger.debug("Received request to action an event for create Disk Store");
    	
    	String diskStoreName = diskStoreAttribute.getDiskStoreName();

    	logger.debug("New Disk Store Name = " + diskStoreName);
    	
    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");
    	
    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createDiskStore = new StringBuffer();
    			
            createDiskStore.append("create DISKSTORE " + diskStoreName + " \n");

            if (!checkIfParameterEmpty(request, "maxLogSize"))
            {
                createDiskStore.append("MAXLOGSIZE " + diskStoreAttribute.getMaxLogSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "autoCompact"))
            {
                createDiskStore.append("AUTOCOMPACT " + diskStoreAttribute.getAutoCompact() + " \n");
            }

            if (!checkIfParameterEmpty(request, "allowForceCompaction"))
            {
                createDiskStore.append("ALLOWFORCECOMPACTION " + diskStoreAttribute.getAllowForceCompaction() + " \n");
            }

            if (!checkIfParameterEmpty(request, "compactionThreshold"))
            {
                createDiskStore.append("COMPACTIONTHRESHOLD " + diskStoreAttribute.getCompactionThreshold() + " \n");
            }

            if (!checkIfParameterEmpty(request, "timeInterval"))
            {
                createDiskStore.append("TIMEINTERVAL " + diskStoreAttribute.getTimeInterval() + " \n");
            }

            if (!checkIfParameterEmpty(request, "writeBufferSize"))
            {
                createDiskStore.append("WRITEBUFFERSIZE " + diskStoreAttribute.getWriteBufferSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "additionalParams"))
            {
                createDiskStore.append(diskStoreAttribute.getAdditionalParams());
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating disk store as -> " + createDiskStore.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createDiskStore.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create Disk Store SQL as follows as -> " + createDiskStore.toString());
                model.addAttribute("sql", createDiskStore.toString());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, diskStoreName));

                ServletOutputStream out = response.getOutputStream();
                out.println(createDiskStore.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-diskstore.jsp
    	return "create-diskstore";
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
