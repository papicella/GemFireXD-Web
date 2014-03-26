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
package pivotal.au.se.gemfirexdweb.dao.asyncevent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

import javax.servlet.jsp.jstl.sql.ResultSupport;

public class AsynceventDAOImpl implements AsynceventDAO
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<Asyncevent> retrieveAsynceventList(String schema, String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<Asyncevent>  asyncevents = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_ASYNCEVENTS);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, srch);  
	      rset = stmt.executeQuery();

	      asyncevents = makeAsynceventListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all async events with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all async events with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return asyncevents;
	}

    public List<Asyncevent> retrieveAsynceventListForAdd(String userKey) throws SqlFireException
    {
        Connection        conn = null;
        Statement stmt = null;
        ResultSet         rset = null;
        List<Asyncevent>  asyncevents = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.createStatement();
            rset = stmt.executeQuery(Constants.USER_ASYNCEVENTS_FOR_ADD);

            asyncevents = makeAsynceventListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all async events for add");
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all async events for add");
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return asyncevents;
    }

	public Result simpleasynceventCommand(String asyncEvent, String type, String userKey) throws SqlFireException 
	{
		
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_ASYNC, asyncEvent);
	      }
	      else if (type.equalsIgnoreCase("START"))
	      {
	    	command = String.format(Constants.START_ASYNC, asyncEvent);
	      }
	      else if ((type.equalsIgnoreCase("STOP")))
	      {
	    	command = String.format(Constants.STOP_ASYNC, asyncEvent);  
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}
	
	private List<Asyncevent> makeAsynceventListFromResultSet (ResultSet rset) throws SQLException
	{
		List<Asyncevent> asyncevents = new ArrayList<Asyncevent>();
		
		while (rset.next())
		{
			Asyncevent index = new Asyncevent(rset.getString(1),
					                		  rset.getString(2),
					                		  rset.getString(3),
					                		  rset.getString(4));
			asyncevents.add(index);
		}
		
		return asyncevents;
		
	}

	public javax.servlet.jsp.jstl.sql.Result getAsynEventInfo(
			String asyncEvent, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_ALL_USER_ASYNCEVENT_INFO, asyncEvent), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving all async event info for : " + asyncEvent);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

    public javax.servlet.jsp.jstl.sql.Result getAsyncTables (String asyncId, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.ASYNC_TABLES);
            stmt.setString(1, asyncId);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving async tables for async with id = " + asyncId);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving async tables for async with id = " + asyncId);
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return res;
    }

    public String generateDDL (String asyncId, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        StringBuffer      ddl  = new StringBuffer();

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.GENERATE_DDL);

            stmt.setString(1, asyncId);

            rset = stmt.executeQuery();
            if (rset != null)
            {
                rset.next();

                // create ddl here
                ddl.append("CREATE ASYNCEVENTLISTENER " + rset.getString("ID") + " \n");
                ddl.append("(LISTENERCLASS '" + rset.getString("LISTENER_CLASS") + "' \n");
                ddl.append("INITPARAMS '" + rset.getString("INIT_PARAMS") + "' \n");
                ddl.append("MANUALSTART " + rset.getString("MANUAL_START") + " \n");
                ddl.append("ENABLEBATCHCONFLATION " + rset.getString("BATCH_CONFLATION") + " \n");
                ddl.append("BATCHSIZE " + rset.getString("BATCH_SIZE") + " \n");
                ddl.append("BATCHTIMEINTERVAL " + rset.getString("BATCH_TIME_INTERVAL") + " \n");
                ddl.append("ENABLEPERSISTENCE " + rset.getString("IS_PERSISTENCE") + " \n");

                if (rset.getString("DISK_STORE_NAME") != null)
                {
                    ddl.append("DISKSTORENAME " + rset.getString("DISK_STORE_NAME") + " \n");
                }

                ddl.append("MAXQUEUEMEMORY " + rset.getString("MAX_QUEUE_MEMORY") + " \n");
                ddl.append("ALERTTHRESHOLD " + rset.getString("ALERT_THRESHOLD") + ") \n");
                ddl.append("SERVER GROUPS (" + rset.getString("SERVER_GROUPS") + "); \n");
            }
        }
        catch (SQLException se)
        {
            logger.debug("Error generating DDL for Async with ID = " + asyncId);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error generating DDL for Async with ID = " + asyncId);
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return ddl.toString();
    }
}
