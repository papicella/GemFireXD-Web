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
import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.jolokia.client.request.J4pSearchRequest;
import org.jolokia.client.request.J4pSearchResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.dao.tables.Table;
import pivotal.au.se.gemfirexdweb.dao.tables.TableDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class JMXController
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/jmxmbeans", method = RequestMethod.GET)
    public String showJMXMbeans
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

        UserPref userPref = (UserPref) session.getAttribute("prefs");
        String jolokiaURL = userPref.getJolokiaURL();

        String mbeanName = request.getParameter("mbeanName");

        if (mbeanName != null)
        {
            logger.debug("Received request to show Mbean Attributes");
            logger.debug("mbeanName = " + mbeanName);

            String refreshTime = request.getParameter("refreshTime");
            if (refreshTime == null)
            {
                refreshTime = "none";
            }

            logger.debug("refresh time : " + refreshTime);

            Map<String, Object> attributes = getMbeanAttributeValues(mbeanName, jolokiaURL);

            model.addAttribute("refreshTime", refreshTime);
            model.addAttribute("mbeanName", mbeanName);
            model.addAttribute("mbeanMap", attributes);
            model.addAttribute("records", attributes.size());

            // This will resolve to /WEB-INF/jsp/viewmbean.jsp
            return "viewmbean";
        }
        else
        {
            logger.debug("Received request to show JMX Mbeans for cluster");

            logger.debug("jolokiaURL = " + jolokiaURL);

            List<String> mbeanNames = searchMbeans("", jolokiaURL);

            model.addAttribute("mbeanNames", mbeanNames);
            model.addAttribute("records", mbeanNames.size());

            // This will resolve to /WEB-INF/jsp/showmbeans.jsp
            return "showmbeans";
        }


    }
	
	@RequestMapping(value = "/jmxmbeans", method = RequestMethod.POST)
    public String performJMXAction
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
    	
    	logger.debug("Received request to search for Mbeans");
        UserPref userPref = (UserPref) session.getAttribute("prefs");
        String jolokiaURL = userPref.getJolokiaURL();

        logger.debug("jolokiaURL = " + jolokiaURL);
        String searchStr = request.getParameter("search");
        logger.debug("searchStr = " + searchStr);

        List<String> mbeanNames = searchMbeans("", jolokiaURL);

        List<String> returnMbeanList = new ArrayList<String>();

        for (String mbean: mbeanNames)
        {
            if (mbean.toUpperCase().contains(searchStr.toUpperCase()))
            {
                returnMbeanList.add(mbean);
            }
        }

        model.addAttribute("mbeanNames", returnMbeanList);
        model.addAttribute("records", mbeanNames.size());
        model.addAttribute("search", searchStr);

        // This will resolve to /WEB-INF/jsp/showmbeans.jsp
        return "showmbeans";

    }

    private List<String> searchMbeans (String searchStr, String jolokiaURL) throws Exception
    {
        J4pClient client = new J4pClient(jolokiaURL);
        J4pSearchRequest searchRequest = new J4pSearchRequest(searchStr);
        J4pSearchResponse searchResponse = client.execute(searchRequest);

        List<String> mbeanNames = searchResponse.getMBeanNames();

        return mbeanNames;
    }

    private Map<String,Object> getMbeanAttributeValues (String mbeanName, String jolokiaURL) throws Exception
    {
        J4pClient client = new J4pClient(jolokiaURL);
        J4pReadRequest request =
                new J4pReadRequest(mbeanName);
        J4pReadResponse response = client.execute(request);
        Map<String,Object> attributes = response.getValue();

        return attributes;

    }
}
