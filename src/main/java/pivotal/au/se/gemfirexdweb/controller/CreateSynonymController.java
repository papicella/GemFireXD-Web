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
import pivotal.au.se.gemfirexdweb.beans.NewSynonym;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.tables.Table;
import pivotal.au.se.gemfirexdweb.dao.tables.TableDAO;
import pivotal.au.se.gemfirexdweb.dao.types.Type;
import pivotal.au.se.gemfirexdweb.dao.views.View;
import pivotal.au.se.gemfirexdweb.dao.views.ViewDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;

@Controller
public class CreateSynonymController
{
	protected static Logger logger = Logger.getLogger("controller");
    private static final String FILENAME = "%s.sql";
    private static final String SAVE_CONTENT_TYPE = "application/x-download";

	@RequestMapping(value = "/createsynonym", method = RequestMethod.GET)
    public String createType
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
    	
    	logger.debug("Received request to create a new synonym");

        TableDAO tableDAO = GemFireXDWebDAOFactory.getTableDAO();
        ViewDAO viewDAO = GemFireXDWebDAOFactory.getViewDAO();

        List<View> views = viewDAO.retrieveViewList
                ((String)session.getAttribute("schema"),
                 null,
                 (String)session.getAttribute("user_key"));

        model.addAttribute("views", views);

        List<Table> tbls = tableDAO.retrieveTableList
                ((String)session.getAttribute("schema"),
                 null,
                 (String)session.getAttribute("user_key"),
                 "ORDINARY");

        List<Table> tblsHDFS = tableDAO.retrieveTableList
                ((String)session.getAttribute("schema"),
                 null,
                 (String)session.getAttribute("user_key"),
                 "HDFS");

        tbls.addAll(tblsHDFS);

        model.addAttribute("tables", tbls);

        model.addAttribute("synonymAttribute", new NewSynonym());
        model.addAttribute("objectName", request.getParameter("objectName"));

    	// This will resolve to /WEB-INF/jsp/create-synonym.jsp
    	return "create-synonym";
    }
	
	@RequestMapping(value = "/createsynonym", method = RequestMethod.POST)
    public String createSynonymAction
    (@ModelAttribute("synonymAttribute") NewSynonym synonymAttribute,
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
    	
    	logger.debug("Received request to action an event for create synonym");

        String schema = synonymAttribute.getSchemaName().trim();

        if (schema.length() == 0)
        {
            schema = (String) session.getAttribute("schema");
        }

        logger.debug("New synonym name = " + synonymAttribute.getSynonymName());

        TableDAO tableDAO = GemFireXDWebDAOFactory.getTableDAO();
        ViewDAO viewDAO = GemFireXDWebDAOFactory.getViewDAO();

        List<View> views = viewDAO.retrieveViewList
                ((String)session.getAttribute("schema"),
                        null,
                        (String)session.getAttribute("user_key"));

        model.addAttribute("views", views);

        List<Table> tbls = tableDAO.retrieveTableList
                ((String)session.getAttribute("schema"),
                        null,
                        (String)session.getAttribute("user_key"),
                        "ORDINARY");

        model.addAttribute("tables", tbls);

    	// perform some action here with what we have
    	String submit = request.getParameter("pSubmit");

    	if (submit != null)
    	{
            // build create HDFS Store SQL
            StringBuffer createSynonym = new StringBuffer();
    			
            createSynonym.append("CREATE SYNONYM " + schema + "." + synonymAttribute.getSynonymName() + " \n");
            createSynonym.append("FOR " + synonymAttribute.getObjectName() + " \n");

            if (submit.equalsIgnoreCase("create"))
            {
                Result result = new Result();

                logger.debug("Creating synonym as -> " + createSynonym.toString());

                result = GemFireXDWebDAOUtil.runCommand
                        (createSynonym.toString(),
                                (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);

            }
            else if (submit.equalsIgnoreCase("Show SQL"))
            {
                logger.debug("Create UDT (Type) SQL as follows as -> " + createSynonym.toString());
                model.addAttribute("sql", createSynonym.toString());
                model.addAttribute("objectName", request.getParameter("objectName"));
            }
            else if (submit.equalsIgnoreCase("Save to File"))
            {
                response.setContentType(SAVE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        String.format(FILENAME, synonymAttribute.getSynonymName()));

                ServletOutputStream out = response.getOutputStream();
                out.println(createSynonym.toString());
                out.close();
                return null;
            }
    			
    	}
    	
    	// This will resolve to /WEB-INF/jsp/create-synonym.jsp
    	return "create-synonym";
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
