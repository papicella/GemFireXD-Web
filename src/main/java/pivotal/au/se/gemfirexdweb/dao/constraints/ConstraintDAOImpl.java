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
package pivotal.au.se.gemfirexdweb.dao.constraints;

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

import javax.servlet.jsp.jstl.sql.ResultSupport;

public class ConstraintDAOImpl implements ConstraintDAO 
{

	protected static Logger logger = Logger.getLogger("controller");
	
	public List<Constraint> retrieveConstraintList(String schema, String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<Constraint>  cons = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_CONSTRAINTS);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, schema);
	      stmt.setString(2, srch);  
	      rset = stmt.executeQuery();

	      cons = makeConstraintListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all constraints with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all constraints with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return cons;
	}

	public Result simpleconstraintCommand(String schemaName, String tableName, String constraintName, String type, String userKey) throws SqlFireException 
	{
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_CONSTRAINT, schemaName, tableName, constraintName);
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}

    public List<Constraint> retrieveTableConstraintList(String schema, String tableName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        List<Constraint>  cons = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_TABLE_CONSTRAINTS);

            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            rset = stmt.executeQuery();

            cons = makeConstraintListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all constraints for table = " + tableName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all constraints for table = " + tableName);
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return cons;
    }

	private List<Constraint> makeConstraintListFromResultSet (ResultSet rset) throws SQLException
	{
		List<Constraint> cons = new ArrayList<Constraint>();
		
		while (rset.next())
		{
			Constraint con = new Constraint(rset.getString(1),
											rset.getString(2),
					              			rset.getString(3),
					              			rset.getString(4),
					              			rset.getString(5),
                                            rset.getString(6));
			cons.add(con);
		}
		
		return cons;
		
	}

    public javax.servlet.jsp.jstl.sql.Result getFKInfo (String schema, String constraintId, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.FK_INFO);
            stmt.setString(1, constraintId);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving foreign key info for contraintid = " + constraintId);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving foreign key info for contraintid = " + constraintId);
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
