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
package pivotal.au.se.gemfirexdweb.dao.stored;

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

public class StoredProcDAOImpl implements StoredProcDAO
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<StoredProc> retrieveProcList(String schema, String search, String procType, String userKey) throws SqlFireException 
	{
	    Connection        	conn = null;
	    PreparedStatement 	stmt = null;
	    ResultSet         	rset = null;
	    List<StoredProc>    procs = null;
	    String            	srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_STORED_CODE);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, procType);
	      stmt.setString(2, schema);
	      stmt.setString(3, srch);
	      rset = stmt.executeQuery();

	      procs = makeProcListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all procs with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all procs with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return procs;
	}

	public Result simpleprocCommand(String schemaName, String procName, String type, String procType, String userKey) throws SqlFireException 
	{
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = 
	        		String.format(Constants.DROP_STORED_CODE, procType.toUpperCase(), 
	        												  schemaName,
	        				                                  procName);
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}

	private List<StoredProc> makeProcListFromResultSet (ResultSet rset) throws SQLException
	{
		List<StoredProc> procs = new ArrayList<StoredProc>();
		
		while (rset.next())
		{
			StoredProc storedProc = 
					new StoredProc(rset.getString(1), rset.getString(2), rset.getString(3));
			procs.add(storedProc);
		}
		
		return procs;
		
	}

	public List<ProcedureParameter> describeProcedure(String schemaName, String procName, String procType, String userKey) throws SqlFireException
	{
		List<ProcedureParameter> procParams = new ArrayList<ProcedureParameter>();

	    Connection        	conn = null;
	    ResultSet         	rset = null;
	   
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);

          if (procType.equals("P"))
          {
              rset = conn.getMetaData().getProcedureColumns
                      (null, schemaName.toUpperCase(), procName.toUpperCase(), "%");
          }
          else
          {
              rset = conn.getMetaData().getFunctionColumns
                      (null, schemaName.toUpperCase(), procName.toUpperCase(), "%");
          }

		  while (rset.next())
		  {
			  ProcedureParameter param = 
					  new ProcedureParameter(rset.getInt("PARAMETER_ID"),
							  				 rset.getString("COLUMN_NAME"),
							  				 rset.getString("TYPE_NAME"));
			
			  procParams.add(param);
		  }	     
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all parameters for procedure with name " + procName);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all parameters for procedure with name " + procName);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	    }
	    
		// TODO Auto-generated method stub
		return procParams;
	}

    public javax.servlet.jsp.jstl.sql.Result getProcPrivs (String schema, String procName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_STORED_CODE_PRIVS);
            stmt.setString(1, schema);
            stmt.setString(2, procName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving priviledges for stored proc = " + procName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving priviledges for stored proc = " + procName);
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

    public Result performPrivilege (String schemaName, String procName, String procType, String privType, String privTo, String userKey) throws SqlFireException
    {
        String            command = null;
        Result            res     = null;

        if (privType.equalsIgnoreCase("GRANT"))
        {
            command = String.format(Constants.GRANT_EXECUTE_PRIV, procType.toUpperCase(), schemaName, procName, privTo);
        }
        else
        {
            command = String.format(Constants.REVOKE_EXECUTE_PRIV, procType.toUpperCase(), schemaName, procName, privTo);
        }

        res = GemFireXDWebDAOUtil.runCommand(command, userKey);

        return res;
    }
	
}
