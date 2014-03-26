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
import pivotal.au.se.gemfirexdweb.beans.NewTrigger;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.tables.Table;
import pivotal.au.se.gemfirexdweb.dao.tables.TableDAO;
import pivotal.au.se.gemfirexdweb.dao.types.Type;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;

@Controller
public class CreateTriggerController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createtrigger", method = RequestMethod.GET)
    public String createTrigger
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
    	
    	logger.debug("Received request to create a new Trigger");

        NewTrigger newTrigger = new NewTrigger();

        if (request.getParameter("settablename") != null)
        {
            model.addAttribute("tableName", request.getParameter("settablename"));
        }

        model.addAttribute("triggerAttribute", newTrigger);

        TableDAO tableDAO = GemFireXDWebDAOFactory.getTableDAO();

        List<Table> tables =
                tableDAO.retrieveTableList
                    ((String)session.getAttribute("schema"),
                     null,
                     (String)session.getAttribute("user_key"),
                     "tables");

        model.addAttribute("tables", tables);

    	// This will resolve to /WEB-INF/jsp/create-trigger.jsp
    	return "create-trigger";
    }
	
	@RequestMapping(value = "/createtrigger", method = RequestMethod.POST)
    public String createTriggerAction
    (@ModelAttribute("triggerAttribute") NewTrigger triggerAttribute,
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
    	
    	logger.debug("Received request to action an event for create Trigger");

        TableDAO tableDAO = GemFireXDWebDAOFactory.getTableDAO();

        List<Table> tables =
                tableDAO.retrieveTableList
                        ((String)session.getAttribute("schema"),
                        null,
                        (String)session.getAttribute("user_key"),
                        "tables");

        model.addAttribute("tables", tables);

        String schema = triggerAttribute.getSchemaName().trim();

        if (schema.length() == 0)
        {
            schema = (String) session.getAttribute("schema");
        }

    	logger.debug("New Trigger schema name = " + schema);
        logger.debug("New Trigger name = " + triggerAttribute.getTriggerName());

    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");

    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createTrigger = new StringBuffer();
    			
            createTrigger.append("CREATE TRIGGER " + schema + "." + triggerAttribute.getTriggerName() + " \n");
            createTrigger.append(triggerAttribute.getBeforeOrAfter() + " \n");
            createTrigger.append(triggerAttribute.getType() + " \n");

            if (!checkIfParameterEmpty(request, "columnList"))
            {
               createTrigger.append("OF " + triggerAttribute.getColumnList() + " \n");
            }

            createTrigger.append("ON " + triggerAttribute.getTableName() + " \n");

            if (!checkIfParameterEmpty(request, "referencingOldClause"))
            {
                createTrigger.append("REFERENCING OLD AS " + triggerAttribute.getReferencingOldClause() + " \n");
            }

            if (!checkIfParameterEmpty(request, "referencingNewClause"))
            {
                createTrigger.append("REFERENCING NEW AS " + triggerAttribute.getReferencingNewClause() + " \n");
            }

            createTrigger.append(triggerAttribute.getForEachRow() + " \n");

            if (!checkIfParameterEmpty(request, "triggerDefinition"))
            {
                createTrigger.append(triggerAttribute.getTriggerDefinition() + " \n");
            }

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating Trigger as -> " + createTrigger.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createTrigger.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);
                model.addAttribute("tableName", triggerAttribute.getTableName());

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create Trigger SQL as follows as -> " + createTrigger.toString());
                model.addAttribute("sql", createTrigger.toString());
                model.addAttribute("tableName", triggerAttribute.getTableName());
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, triggerAttribute.getTriggerName()));

                ServletOutputStream out = response.getOutputStream();
                out.println(createTrigger.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-trigger.jsp
    	return "create-trigger";
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
