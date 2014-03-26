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
import pivotal.au.se.gemfirexdweb.beans.NewHDFSStore;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStore;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStoreDAO;
import pivotal.au.se.gemfirexdweb.dao.types.Type;
import pivotal.au.se.gemfirexdweb.dao.types.TypeDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

@Controller
public class CreateHDFSStoreController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createhdfsstore", method = RequestMethod.GET)
    public String createHDFSStore
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
    	
    	logger.debug("Received request to create a new HDFS Store");

        model.addAttribute("hdfsStoreAttribute", new NewHDFSStore());

    	// This will resolve to /WEB-INF/jsp/create-hdfsstore.jsp
    	return "create-hdfsstore";
    }
	
	@RequestMapping(value = "/createhdfsstore", method = RequestMethod.POST)
    public String createHDFSStoreAction
    (@ModelAttribute("hdfsStoreAttribute") NewHDFSStore hdfsStoreAttribute,
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
    	
    	logger.debug("Received request to action an event for create HDFS Store");
    	
    	String storeName = hdfsStoreAttribute.getStoreName();
        String nameNode = hdfsStoreAttribute.getNameNode();
        String homeDir = hdfsStoreAttribute.getHomeDir();

    	logger.debug("New HDFS Store Name = " + storeName);
    	
    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");
    	
    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createHDFSStore = new StringBuffer();
    			
            createHDFSStore.append("create HDFSSTORE " + storeName + " \n");
            createHDFSStore.append("NAMENODE '" + nameNode + "' \n");
            createHDFSStore.append("HOMEDIR '" + homeDir + "' \n");

            if (!checkIfParameterEmpty(request, "batchSize"))
            {
                createHDFSStore.append("BatchSize " + hdfsStoreAttribute.getBatchSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "batchTimeInterval"))
            {
                createHDFSStore.append("BatchTimeInterval " + hdfsStoreAttribute.getBatchTimeInterval() + " \n");
            }

            if (!checkIfParameterEmpty(request, "maxQueueMemory"))
            {
                createHDFSStore.append("MaxQueueMemory " + hdfsStoreAttribute.getMaxQueueMemory() + " \n");
            }

            if (!checkIfParameterEmpty(request, "minorCompact"))
            {
                createHDFSStore.append("MinorCompact " + hdfsStoreAttribute.getMinorCompact() + " \n");
            }

            if (!checkIfParameterEmpty(request, "maxInputFileSize"))
            {
                createHDFSStore.append("MaxInputFileSize " + hdfsStoreAttribute.getMaxInputFileSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "minInputFileCount"))
            {
                createHDFSStore.append("MinInputFileCount " + hdfsStoreAttribute.getMinInputFileCount() + " \n");
            }

            if (!checkIfParameterEmpty(request, "maxInputFileCount"))
            {
                createHDFSStore.append("MaxInputFileCount " + hdfsStoreAttribute.getMaxInputFileCount() + " \n");
            }

            if (!checkIfParameterEmpty(request, "minorCompactionThreads"))
            {
                createHDFSStore.append("MinorCompactionThreads " + hdfsStoreAttribute.getMinorCompactionThreads() + " \n");
            }

            if (!checkIfParameterEmpty(request, "majorCompact"))
            {
                createHDFSStore.append("MajorCompact " + hdfsStoreAttribute.getMajorCompact() + " \n");
            }

            if (!checkIfParameterEmpty(request, "majorCompactionInterval"))
            {
                createHDFSStore.append("MajorCompactionInterval " + hdfsStoreAttribute.getMajorCompactionInterval() + " \n");
            }

            if (!checkIfParameterEmpty(request, "majorCompactionThreads"))
            {
                createHDFSStore.append("MajorCompactionThreads " + hdfsStoreAttribute.getMajorCompactionThreads() + " \n");
            }

            if (!checkIfParameterEmpty(request, "maxWriteOnlyFileSize"))
            {
                createHDFSStore.append("MaxWriteOnlyFileSize " + hdfsStoreAttribute.getMaxWriteOnlyFileSize() + " \n");
            }

            if (!checkIfParameterEmpty(request, "writeOnlyRolloverInterval"))
            {
                createHDFSStore.append("WriteOnlyFileRolloverInterval " + hdfsStoreAttribute.getWriteOnlyRolloverInterval() + " \n");
            }

            if (!checkIfParameterEmpty(request, "additionalParams"))
            {
                createHDFSStore.append(hdfsStoreAttribute.getAdditionalParams());
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating hdfs store as -> " + createHDFSStore.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createHDFSStore.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create HDFS Store SQL as follows as -> " + createHDFSStore.toString());
                model.addAttribute("sql", createHDFSStore.toString());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, storeName));

                ServletOutputStream out = response.getOutputStream();
                out.println(createHDFSStore.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-table.jsp
    	return "create-hdfsstore";
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
