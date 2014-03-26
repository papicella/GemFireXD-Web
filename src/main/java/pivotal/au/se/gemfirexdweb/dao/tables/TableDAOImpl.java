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
package pivotal.au.se.gemfirexdweb.dao.tables;

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

import javax.servlet.jsp.jstl.sql.ResultSupport;

public class TableDAOImpl implements TableDAO
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<Table> retrieveTableList(String schema, String search, String userKey, String viewType) throws SqlFireException
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<Table>       tbls = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
          if (viewType.equals("HDFS"))
          {
              stmt = conn.prepareStatement(Constants.USER_TABLES_HDFS);
          }
          else if (viewType.equals("OFFHEAP"))
          {
              stmt = conn.prepareStatement(Constants.USER_TABLES_OFFHEAP);
          }
          else
          {
              stmt = conn.prepareStatement(Constants.USER_TABLES);
          }

	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, schema);
	      stmt.setString(2, srch);  
	      rset = stmt.executeQuery();

	      tbls = makeTableListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all tables with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all tables with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return tbls;
	}
	
	public Result simpletableCommand(String schemaName, String tableName, String type, String userKey) throws SqlFireException 
	{
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_TABLE, schemaName, tableName);
	      }
	      else if (type.equalsIgnoreCase("EMPTY"))
	      {
	        command = String.format(Constants.TRUNCATE_TABLE, schemaName, tableName);
	      }
          else if (type.equalsIgnoreCase("REMOVEALLASYNC"))
          {
              command = String.format(Constants.REMOVE_ASYNC_EVENT_LISTENERS, schemaName, tableName);
          }
          else if (type.equalsIgnoreCase("REMOVEALLGATEWAYSENDERS"))
          {
              command = String.format(Constants.REMOVE_GATEWAYSENDERS, schemaName, tableName);
          }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}

    public Result addAsyncEventListener (String schemaName, String tableName, String asyncIdName, String curValue, String userKey) throws SqlFireException
    {
        String            command = null;
        Result            res     = null;

        String s = curValue.trim();

        if (s.length() != 0)
        {
            asyncIdName = s + "," + asyncIdName;
        }

        command = String.format(Constants.ADD_ASYNC_EVENT_LISTENER, schemaName, tableName, asyncIdName);
        res = GemFireXDWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    public Result addGatewaySender (String schemaName, String tableName, String gatewaySender, String curValue, String userKey) throws SqlFireException
    {
        String            command = null;
        Result            res     = null;

        String s = curValue.trim();

        if (s.length() != 0)
        {
            gatewaySender = s + "," + gatewaySender;
        }

        command = String.format(Constants.ADD_GATEWAYSENDER, schemaName, tableName, gatewaySender);

        res = GemFireXDWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    public Result performPrivilege (String schemaName, String tableName, String privType, String privOption, String privTo, String userKey) throws SqlFireException
    {
        String            command = null;
        Result            res     = null;

        if (privType.equalsIgnoreCase("GRANT"))
        {
            command = String.format(Constants.GRANT_TABLE_PRIV, privOption, schemaName, tableName, privTo);
        }
        else
        {
            command = String.format(Constants.REVOKE_TABLE_PRIV, privOption, schemaName, tableName, privTo);
        }

        res = GemFireXDWebDAOUtil.runCommand(command, userKey);

        return res;
    }

	private List<Table> makeTableListFromResultSet (ResultSet rset) throws SQLException
	{
		List<Table> tbls = new ArrayList<Table>();
		
		while (rset.next())
		{
			Table table = new Table(rset.getString(1),
					              rset.getString(2),
					              rset.getString(4),
					              rset.getString(3),
					              rset.getString(5),
					              rset.getString(6));
			tbls.add(table);
		}
		
		return tbls;
		
	}

	public String generateLoadScript(String schema, String tableName) throws SqlFireException 
	{
		// TODO Auto-generated method stub
		String loadSQL = String.format(Constants.LOAD_TABLE_SCRIPT, schema, tableName, tableName);
				
		return loadSQL;
	}

	public javax.servlet.jsp.jstl.sql.Result getDataLocations
		(String schema, String tableName, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.TABLE_DATA_LOCATION, schema, tableName), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving table data locations");
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

	public String viewPartitionAttrs
		(String schema, String tableName, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		String res = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    
		try 
		{
			conn = AdminUtil.getConnection(userKey);
		    stmt = conn.prepareStatement(Constants.VIEW_PARTITION_ATTRS);
		   
		    stmt.setString(1, schema);
		    stmt.setString(2, tableName);  
		    rset = stmt.executeQuery();
		    if (rset != null)
		    {
		    	rset.next();
		    	res = rset.getString(1);
		    }
		}
		catch (Exception e) 
		{
		      logger.debug("Error retrieving table partition attributes");
		      throw new SqlFireException(e); 
		}
		finally
		{
			JDBCUtil.close(stmt);
			JDBCUtil.close(rset);
		}
		
		return res;
	}

	public javax.servlet.jsp.jstl.sql.Result getExpirationEvictionAttrs
		(String schema, String tableName, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_EVICTION_EXPIRATION_ATTRS, schema, tableName), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving eviction / expiration attributes for table " + tableName);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

	public javax.servlet.jsp.jstl.sql.Result getAllTableInfo
		(String schema, String tableName, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_ALL_TABLE_COLUMNS, schema, tableName), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving info for table " + tableName);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

	public javax.servlet.jsp.jstl.sql.Result getTableStructure
		(String schema, String tableName, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_TABLE_STRUCTURE, schema, tableName), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving structure for table " + tableName);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

	public javax.servlet.jsp.jstl.sql.Result getMemoryUsage
		(String schema, String tableName, String userKey) throws SqlFireException 
	{
		
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.TABLE_MEMORY_USAGE, schema, tableName), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving memory usage for table " + tableName);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

    public javax.servlet.jsp.jstl.sql.Result getMemoryUsageSum (String schema, String tableName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.TABLE_MEMORY_USAGE_SUM);
            stmt.setString(1, schema + "." + tableName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving memory usage for table = " + tableName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving memory usage for table = " + tableName);
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

    public javax.servlet.jsp.jstl.sql.Result getTableAsyncListeners (String tableName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.VIEW_ASYNC_EVENT_LISTENERS);
            stmt.setString(1, tableName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving async event listeners for table = " + tableName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving async event listeners for table = " + tableName);
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

    public javax.servlet.jsp.jstl.sql.Result getTableTriggers (String schema, String tableName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.VIEW_TABLE_TRIGGERS);
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving triggers for table = " + tableName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving triggers for table = " + tableName);
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

    public javax.servlet.jsp.jstl.sql.Result getTablePrivs (String schema, String tableName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.VIEW_TABLE_PRIVS);
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving priviledges for table = " + tableName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving priviledges for table = " + tableName);
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
