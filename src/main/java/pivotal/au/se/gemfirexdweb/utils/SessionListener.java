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
package pivotal.au.se.gemfirexdweb.utils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class SessionListener implements HttpSessionListener
{
	  protected static Logger logger = Logger.getLogger("controller");
	  private HttpSession session = null;
	  
	  public void sessionCreated(HttpSessionEvent event)
	  {
	    // no need to do anything here as connection may not have been established yet
	    session  = event.getSession();
	    logger.debug("Session created for id " + session.getId());
	  }

	  public void sessionDestroyed(HttpSessionEvent event)
	  {
	    session  = event.getSession();
	    /*
	     * Need to ensure Connection is closed from ConnectionManager
	     */
	    ConnectionManager cm = null;

	    try
	    {
	      cm = ConnectionManager.getInstance();
	      cm.removeConnection(session.getId());
	      logger.debug("Session destroyed for id " + session.getId());
	    }
	    catch (Exception e)
	    {
	      logger.debug("SesssionListener.sessionDestroyed Unable to obtain Connection", e);
	    }
	  }
}
