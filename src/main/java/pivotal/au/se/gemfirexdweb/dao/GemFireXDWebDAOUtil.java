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
package pivotal.au.se.gemfirexdweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;

public class GemFireXDWebDAOUtil
{
	  static public Result runCommand (String command, String userKey) throws SqlFireException
	  {
	    Result res = new Result();
	    Connection        conn    = null;
	    Statement         stmt    = null;
	    
	    res.setCommand(command);

	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.createStatement();   

	      stmt.execute(command);
	      // no need to commit it's auto commit already as it's DDL statement.
	      res.setCommand(command);
	      res.setMessage("SUCCESS");
	    }
	    catch (SQLException se)
	    {
	      // we don't want to stop it running we just need the error
	      res.setMessage(se.getMessage());
	    }
	    catch (Exception ex)
	    {
	      throw new SqlFireException(ex);   
	    }
	    finally 
	    {
	      JDBCUtil.close(stmt);
	    }
	    
	    return res;
	  }

	  static public Result runStoredCommand (String command, String userKey) throws SqlFireException
	  {
	    Result res = new Result();
	    Connection        			conn    = null;
	    PreparedStatement         	stmt    = null;
	    
	    res.setCommand(command);

	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareCall(command);
	      stmt.execute();
	      
	      // no need to commit it's auto commit already as it's DDL statement.
	      res.setCommand(command);
	      res.setMessage("SUCCESS");
	    }
	    catch (SQLException se)
	    {
	      // we don't want to stop it running we just need the error
	      res.setMessage(se.getMessage());
	    }
	    catch (Exception ex)
	    {
	      throw new SqlFireException(ex);   
	    }
	    finally 
	    {
	      JDBCUtil.close(stmt);
	    }
	    
	    return res;
	  }
	  
	  static public List<String> getAllSchemas (String userKey) throws SqlFireException
	  {
		  List<String> schemas = new ArrayList<String>();
		  Connection        conn    = null;
		  Statement         stmt    = null;
		  ResultSet 		rset = null;
		  String sql = "select schemaname from sys.sysschemas order by 1";
	      try
	      {
	    	  conn = AdminUtil.getConnection(userKey);
	    	  stmt = conn.createStatement();  
	    	  rset = stmt.executeQuery(sql);
	    	  
	    	  while (rset.next())
	    	  {
	    		  schemas.add(rset.getString(1));
	    	  }
	      }
	      catch (Exception ex)
	      {
	    	  throw new SqlFireException(ex);   
	      }
	      finally 
	      {
	    	  JDBCUtil.close(stmt);
	      }
	      
		  return schemas;
		  
	  }
}
