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
package pivotal.au.se.gemfirexdweb.dao.gatewayrecievers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

public class GatewayReceiverDAOImpl implements GatewayReceiverDAO
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<GatewayReceiver> retrieveGatewayReceiverList(String schema,
			String search, String userKey) throws SqlFireException 
	{
		// TODO Auto-generated method stub
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<GatewayReceiver>  gatewayreceivers = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_GATEWAY_RECEIVERS);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, srch);  
	      rset = stmt.executeQuery();

	      gatewayreceivers = makeGatewayReceiverListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all gateway receivers with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all gateway receievers with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return gatewayreceivers;
	}

	public Result simplegatewayReceiverCommand(String id, String type, String userKey) throws SqlFireException 
	{
		// TODO Auto-generated method stub
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_GATEWAY_RECEIVER, id);
	        res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	      }
	    }
	    
	    return res;
	}
	
	private List<GatewayReceiver> makeGatewayReceiverListFromResultSet (ResultSet rset) throws SQLException
	{
		List<GatewayReceiver> gatewayrecievers = new ArrayList<GatewayReceiver>();
		
		while (rset.next())
		{
			GatewayReceiver gr = new GatewayReceiver(rset.getString(1),
					                		  rset.getString(2),
					                		  rset.getString(3),
					                		  rset.getString(4));
			gatewayrecievers.add(gr);
		}
		
		return gatewayrecievers;
		
	}

	public javax.servlet.jsp.jstl.sql.Result getGatewayRecieverInfo
		(String id, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_ALL_GATEWAY_RECEIVERS_COLUMNS, id), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving all gateway reciever info for " + id);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

    public String generateDDL (String id, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        StringBuffer      ddl  = new StringBuffer();

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.GENERATE_DDL);

            stmt.setString(1, id);

            rset = stmt.executeQuery();
            if (rset != null)
            {
                rset.next();

                // create ddl here
                ddl.append("CREATE GATEWAYRECEIVER " + rset.getString("ID") + " \n");
                ddl.append("(BINDADDRESS '" + rset.getString("BIND_ADDRESS") + "' \n");
                ddl.append("STARTPORT " + rset.getString("START_PORT") + " \n");
                ddl.append("ENDPORT " + rset.getString("END_PORT") + " \n");
                ddl.append("SOCKETBUFFERSIZE " + rset.getString("SOCKET_BUFFER_SIZE") + " \n");
                ddl.append("MAXTIMEBETWEENPINGS " + rset.getString("MAX_TIME_BETWEEN_PINGS") + ") \n");
                ddl.append("SERVER GROUPS (" + rset.getString("SERVER_GROUPS") + "); \n");
            }
        }
        catch (SQLException se)
        {
            logger.debug("Error generating DDL for Afor Gateway Receiever with ID = " + id);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error generating DDL for Gateway Receiever with ID = " + id);
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
