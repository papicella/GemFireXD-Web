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
package pivotal.au.se.gemfirexdweb.dao.gatewaysenders;

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

public class GatewaySenderDAOImpl implements GatewaySenderDAO
{
	protected static Logger logger = Logger.getLogger("controller");

	public List<GatewaySender> retrieveGatewaySenderList(String schema,
			String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<GatewaySender>  gatewayssenders = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_GATEWAY_SENDERS);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, srch);  
	      rset = stmt.executeQuery();

	      gatewayssenders = makeGatewaySenderListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all gateway senders with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all gateway senders with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return gatewayssenders;
	}

    public List<GatewaySender> retrieveGatewaySenderForAdd(String userKey) throws SqlFireException
    {
        Connection        conn = null;
        Statement          stmt = null;
        ResultSet         rset = null;
        List<GatewaySender>  gatewayssenders = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.createStatement();

            rset = stmt.executeQuery(Constants.USER_GATEWAY_SENDERS_FOR_ADD);

            gatewayssenders = makeGatewaySenderListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all gateway senders for add");
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all gateway senders for add");
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return gatewayssenders;
    }

	public Result simplegatewaySenderCommand(String senderId, String type, String userKey) throws SqlFireException 
	{
		// TODO Auto-generated method stub
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_GATEWAY_SENDER, senderId);
	        res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	      }
	      else if (type.equalsIgnoreCase("STOP"))
	      {
	    	command = String.format(Constants.STOP_GATEWAY_SENDER, senderId);  
	    	res = GemFireXDWebDAOUtil.runStoredCommand(command, userKey);
	      }
	      else if (type.equalsIgnoreCase("START"))
	      {
	    	command = String.format(Constants.START_GATEWAY_SENDER, senderId);  
	    	res = GemFireXDWebDAOUtil.runStoredCommand(command, userKey);
	      }
	    }
	    
	    
	    
	    return res;
	}

	private List<GatewaySender> makeGatewaySenderListFromResultSet (ResultSet rset) throws SQLException
	{
		List<GatewaySender> gatewaysenders = new ArrayList<GatewaySender>();
		
		while (rset.next())
		{
			GatewaySender gs = new GatewaySender(rset.getString(1),
					                		  rset.getString(2),
					                		  rset.getString(3),
					                		  rset.getString(4));
			gatewaysenders.add(gs);
		}
		
		return gatewaysenders;
		
	}

	public javax.servlet.jsp.jstl.sql.Result getGatewaySenderInfo
		(String senderId, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_ALL_USER_GATEWAY_SENDER_INFO, senderId), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving all gateway sender info info for " + senderId);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

    public javax.servlet.jsp.jstl.sql.Result getRunningGatewaySenders (String senderId, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;

        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.RUNNING_GATEWAY_SENDERS);
            stmt.setString(1, senderId);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset, -1);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving running gateways with senderid = " + senderId);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving running gateways with senderid = = " + senderId);
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

    public javax.servlet.jsp.jstl.sql.Result getGatewaySendersForTable (String tableName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.TABLE_GATEWAY_SENDERS);
            stmt.setString(1, tableName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset, -1);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving gateway senders for table = " + tableName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving gateway senders for table = " + tableName);
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
}
