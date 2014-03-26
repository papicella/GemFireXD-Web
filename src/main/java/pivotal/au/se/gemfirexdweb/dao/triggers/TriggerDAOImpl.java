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
package pivotal.au.se.gemfirexdweb.dao.triggers;

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

public class TriggerDAOImpl implements TriggerDAO
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<Trigger> retrieveTriggerList(String schema, String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<Trigger>     triggers = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_TRIGGERS);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, schema);
	      stmt.setString(2, srch);  
	      rset = stmt.executeQuery();

	      triggers = makeTriggerListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all triggers with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all triggers with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return triggers;
	}

	public Result simpletriggerCommand(String schemaName, String triggerName, String type, String userKey) throws SqlFireException 
	{

	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_TRIGGER, schemaName, triggerName);
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}

	private List<Trigger> makeTriggerListFromResultSet (ResultSet rset) throws SQLException
	{
		List<Trigger> triggers = new ArrayList<Trigger>();
		
		while (rset.next())
		{
			Trigger trigger = new Trigger(rset.getString(1),
					              		  rset.getString(2),
					                      rset.getString(3),
					                      rset.getString(4),
					                      rset.getString(5),
					                      rset.getString(6),
					                      rset.getString(7),
					                      rset.getString(8),
					                      rset.getString(9));
			triggers.add(trigger);
		}
		
		return triggers;
		
	}

	public javax.servlet.jsp.jstl.sql.Result getAllTriggerInfo
		(String schema, String triggerId, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_ALL_USER_TRIGGER_COLUMNS, triggerId), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving info for trigger with id " + triggerId);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

    public javax.servlet.jsp.jstl.sql.Result getTriggerTable (String triggerId, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.VIEW_TRIGGER_TABLE);
            stmt.setString(1, triggerId);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving table for trigger with id = " + triggerId);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving table for trigger with id = " + triggerId);
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
