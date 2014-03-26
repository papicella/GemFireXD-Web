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
import pivotal.au.se.gemfirexdweb.dao.members.Member;
import pivotal.au.se.gemfirexdweb.dao.members.MemberDAO;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;


@Controller
public class MemberController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/members", method = RequestMethod.GET)
    public String showDiskstores
    (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception 
    {
		javax.servlet.jsp.jstl.sql.Result allMemberInfoResult = null;
		
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
    	
    	logger.debug("Received request to show members");
    	
    	MemberDAO mbrDAO = GemFireXDWebDAOFactory.getMemberDAO();
    	
    	String memberAction = request.getParameter("memberAction");
        String propertyAction = request.getParameter("propertyAction");

    	if (memberAction != null)
    	{
    		logger.debug("memberAction = " + memberAction);
            logger.debug("propertyAction = " + propertyAction);

    		if (memberAction.equals("ALLMEMBEREVENTINFO"))
    		{
    			allMemberInfoResult = 
    					mbrDAO.getMemberInfo((String)request.getParameter("memberId"), 
    										 (String)session.getAttribute("user_key"));
    			model.addAttribute("allMemberInfoResult", allMemberInfoResult);
    			model.addAttribute("memberid", (String)request.getParameter("memberId"));
    		}
            else if (memberAction.equals("PROPERTIES"))
            {
                String props =
                        mbrDAO.getMemberProperties((String)request.getParameter("memberId"),
                                                   propertyAction,
                                                   (String)session.getAttribute("user_key"));

                model.addAttribute("props", props);
                model.addAttribute("propertyAction", propertyAction);
                model.addAttribute("memberid", (String)request.getParameter("memberId"));
            }
    	}
    	
    	List<Member> members = mbrDAO.retrieveMembers
    			((String)session.getAttribute("user_key"));
    	
    	model.addAttribute("members", members);
    	model.addAttribute("records", members.size());
    	model.addAttribute("estimatedrecords", members.size());
    	        
    	// This will resolve to /WEB-INF/jsp/members.jsp
    	return "members";
    }

}
