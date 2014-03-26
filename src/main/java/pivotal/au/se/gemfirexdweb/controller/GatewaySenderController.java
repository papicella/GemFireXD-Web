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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOFactory;
import pivotal.au.se.gemfirexdweb.dao.gatewaysenders.GatewaySender;
import pivotal.au.se.gemfirexdweb.dao.gatewaysenders.GatewaySenderDAO;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.UserPref;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;

@Controller
public class GatewaySenderController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/gatewaysenders", method = RequestMethod.GET)
    public String showGatewaySenders
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		int startAtIndex = 0, endAtIndex = 0;
		javax.servlet.jsp.jstl.sql.Result allGatewaySenderInfoResult, runningGatewaySenders = null;
		
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
    	
    	logger.debug("Received request to show gateway senders");
    	
    	GatewaySenderDAO gsDAO = GemFireXDWebDAOFactory.getGatewaySenderDAO();
    	Result result = new Result();
    	
    	String gsAction = request.getParameter("gsAction");
    	
    	if (gsAction != null)
    	{
    		logger.debug("gsAction = " + gsAction);
    		
    		if (gsAction.equals("ALLGATEWAYSENDERINFO"))
    		{
    			allGatewaySenderInfoResult =
    					gsDAO.getGatewaySenderInfo
    					  ((String)request.getParameter("senderId"), 
    					   (String)session.getAttribute("user_key"));
    			
    			model.addAttribute("allGatewaySenderInfoResult", allGatewaySenderInfoResult);
    			model.addAttribute("gatewaysender", (String)request.getParameter("senderId"));
    		}
            else if (gsAction.equals("RUNNINGGATEWAYSENDERS"))
            {
                runningGatewaySenders =
                        gsDAO.getRunningGatewaySenders
                                ((String)request.getParameter("senderId"),
                                 (String)session.getAttribute("user_key"));

                model.addAttribute("runningGatewaySenders", runningGatewaySenders);
                model.addAttribute("gatewaysender", (String)request.getParameter("senderId"));
            }
    		else
    		{
	            result = null;
	            result =
	            	gsDAO.simplegatewaySenderCommand
	                ((String)request.getParameter("senderId"),
	                 gsAction,
	                 (String)session.getAttribute("user_key"));
	            
	            model.addAttribute("result", result);
    		}
    	}
    	
    	List<GatewaySender> gatewaysenders = gsDAO.retrieveGatewaySenderList
    			((String)session.getAttribute("schema"), 
    			 null, 
    			 (String)session.getAttribute("user_key"));
    	
    	model.addAttribute("records", gatewaysenders.size());
    	model.addAttribute("estimatedrecords", gatewaysenders.size());
    	
        UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (gatewaysenders.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("gatewaysenders", gatewaysenders);  
        }
        else
        {
          if (request.getParameter("startAtIndex") != null)
          {
            startAtIndex = Integer.parseInt(request.getParameter("startAtIndex"));
          }
          
          if (request.getParameter("endAtIndex") != null)
          {
            endAtIndex = Integer.parseInt(request.getParameter("endAtIndex"));
            if (endAtIndex > gatewaysenders.size())
            {
              endAtIndex = gatewaysenders.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = gatewaysenders.subList(startAtIndex, endAtIndex);
          model.addAttribute("gatewaysenders", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        
    	// This will resolve to /WEB-INF/jsp/gatewaysenders.jsp
    	return "gatewaysenders";
    }
	
	@RequestMapping(value = "/gatewaysenders", method = RequestMethod.POST)
    public String performGatewaySendersAction
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
    	
    	int startAtIndex = 0, endAtIndex = 0;
    	Result result = new Result();
    	List<GatewaySender> gatewaysenders = null;
    	
    	logger.debug("Received request to perform an action on the gateway senders");

    	GatewaySenderDAO gsDAO = GemFireXDWebDAOFactory.getGatewaySenderDAO();
    	
    	if (request.getParameter("search") != null)
    	{
    		gatewaysenders = gsDAO.retrieveGatewaySenderList
				((String)session.getAttribute("schema"), 
				 (String)request.getParameter("search"), 
				 (String)session.getAttribute("user_key"));
	    	
	    	model.addAttribute("search", (String)request.getParameter("search"));
    	}
    	else
    	{
	        String[] tableList  = request.getParameterValues("selected_gatewaysenders[]");
	        String   commandStr = request.getParameter("submit_mult");
	        
	        logger.debug("tableList = " + Arrays.toString(tableList));
	        logger.debug("command = " + commandStr);
	        
	        // start actions now if tableList is not null
	        
	        if (tableList != null)
	        {
	        	List al = new ArrayList<Result>();
	        	for (String senderId: tableList)
	        	{
	                result = null;
	                result =
                    	gsDAO.simplegatewaySenderCommand
                        (senderId,
                         commandStr,
                         (String)session.getAttribute("user_key"));
	                al.add(result);
	        	}
	        	
	        	model.addAttribute("arrayresult", al);
	        }
	        
	    	gatewaysenders = gsDAO.retrieveGatewaySenderList
	    			((String)session.getAttribute("schema"), 
	    			 null, 
	    			 (String)session.getAttribute("user_key"));
    	}
    	
    	model.addAttribute("records", gatewaysenders.size());
    	model.addAttribute("estimatedrecords", gatewaysenders.size());
    	
        UserPref userPref = (UserPref) session.getAttribute("prefs");
        
        if (gatewaysenders.size() <= userPref.getRecordsToDisplay())
        {
        	model.addAttribute("gatewaysenders", gatewaysenders);  
        }
        else
        {
          if (request.getParameter("startAtIndex") != null)
          {
            startAtIndex = Integer.parseInt(request.getParameter("startAtIndex"));
          }
          
          if (request.getParameter("endAtIndex") != null)
          {
            endAtIndex = Integer.parseInt(request.getParameter("endAtIndex"));
            if (endAtIndex > gatewaysenders.size())
            {
              endAtIndex = gatewaysenders.size();
            }
          }
          else
          {
            endAtIndex = userPref.getRecordsToDisplay();
          }
          
          List subList = gatewaysenders.subList(startAtIndex, endAtIndex);
          model.addAttribute("gatewaysenders", subList);
        }  
        
        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        
    	// This will resolve to /WEB-INF/jsp/gatewaysenders.jsp
    	return "gatewaysenders";
    
    }
}
