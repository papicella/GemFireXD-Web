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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import pivotal.au.se.gemfirexdweb.beans.CommandResult;

public class QueryUtil 
{
	  static public String runExplainPlan (Connection conn, String query) throws SQLException
	  {
		Statement stmt = null;
		ResultSet rset = null;
		String result = "";

		try 
		{
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);

            // returns multiple row (one per member)
            while(rset.next())
            {
            	result += rset.getString(1);
            }

		} 
		finally
		{
			if (stmt != null)
			{
				try 
				{
					stmt.close();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (rset != null)
			{
				try 
				{
					stmt.close();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}		
		
		return result;
		  
	  }
	  
	  static public Result runQuery (Connection conn, String query, int maxrows) throws SQLException
	  {
	    Statement stmt  = null;
	    ResultSet rset  = null;
	    Result    res   = null;
	    
	    try
	    {
	      stmt = conn.createStatement();
	      rset = stmt.executeQuery(query);
	      
	      /* 
	       * Convert the ResultSet to a 
	       * Result object that can be used with JSTL tags 
	       */
	      if (maxrows == -1)
	      {
	        res = ResultSupport.toResult(rset);
	      }
	      else
	      {
	        res = ResultSupport.toResult(rset, maxrows);
	      }
	    }
	    finally
	    {
          JDBCUtil.close(stmt);
          JDBCUtil.close(rset);
	    }
	    
	    return res;
	  }

	  static public List<Result> runStoredprocWithResultSet 
	    (Connection conn, String query, int maxrows, int resultsets) throws SQLException
	  {
		PreparedStatement pstmt = null;
	    ResultSet rset  = null;
	    Result    res   = null;
	    List<Result> results = new ArrayList<Result>();
	    
	    try
	    {
	      
    	  for (int i = 1; i <= resultsets; i++)
    	  {
    		  if (i == 1)
    		  {
    			  pstmt = conn.prepareCall(query);
    			  pstmt.execute();
    		  }
    		  else
    		  {
    			  pstmt.getMoreResults();
    		  }
    		  
    	      rset = pstmt.getResultSet(); 
    	      res = null;
    	      
    	      /* 
    	       * Convert the ResultSet to a 
    	       * Result object that can be used with JSTL tags 
    	       */
    	      if (maxrows == -1)
    	      {
    	        res = ResultSupport.toResult(rset);
    	      }
    	      else
    	      {
    	        res = ResultSupport.toResult(rset, maxrows);
    	      }
    	      
    	      results.add(res);
    	      
    	      rset.close();
    	      rset = null;
    	  }
	      

	    }
	    finally
	    {
          JDBCUtil.close(pstmt);
          JDBCUtil.close(rset);
	    }
	    
	    return results;
	  }
	  
	  static public int runQueryCount (Connection conn, String query) throws SQLException
	  {
	    Statement stmt  = null;
	    ResultSet rset  = null;
	    int count = 0;
	    
	    try
	    {
	      stmt = conn.createStatement();
	      rset = stmt.executeQuery("select count(*) from (" + query + ") as \"Count\"");
	      rset.next();
	      count = rset.getInt(1);
	    }
	    catch (SQLException se)
	    {
	      // do nothing if we can't get count.
	    }
	    finally
	    {
	          JDBCUtil.close(stmt);
	          JDBCUtil.close(rset);
	    }
	    
	    return count;
	  }
	  
	  static public CommandResult runCommitOrRollback (Connection conn, boolean commit, String elapsedTime) throws SQLException
	  {
		  CommandResult res = new CommandResult();
		  
		  try
		  {
			  long start = System.currentTimeMillis();
			  
			  if (commit)
			  {
				  conn.commit(); 
				  res.setCommand("commit");
			  }
			  else
			  {
				  conn.rollback();
				  res.setCommand("rollback");
			  }
			  
		      long end = System.currentTimeMillis();
		      double timeTaken = new Double(end - start).doubleValue();
		      DecimalFormat df = new DecimalFormat("#.##");
		      
		      if (elapsedTime.equals("Y"))
		      {
		    	  res.setElapsedTime("" + df.format(timeTaken));
		      }
		      
		      res.setRows(0);
		      res.setMessage("SUCCESS");
		      
		  }
		  catch (SQLException se)
		  {
		      // we don't want to stop it running we just need the error
		      res.setMessage(se.getMessage());
		      res.setRows(-1);
		  }
		  
		  return res;
		  
	  }
	  
	  static public CommandResult runCommand (Connection conn, String command, String elapsedTime) throws SQLException
	  {
	    CommandResult res = new CommandResult();

	    Statement         stmt    = null;
	    
	    res.setCommand(command);

	    try
	    {
	      stmt = conn.createStatement();   

	      long start = System.currentTimeMillis();
	      int rowsAffected = stmt.executeUpdate(command); 
	      long end = System.currentTimeMillis();
	      
	      double timeTaken = new Double(end - start).doubleValue();
	      DecimalFormat df = new DecimalFormat("#.##");
	      
	      res.setRows(rowsAffected);
	      
	      // no need to commit it's auto commit already as it's DDL statement.
	      res.setCommand(command);
	      res.setMessage("SUCCESS");
	      
	      if (elapsedTime.equals("Y"))
	      {
	    	  res.setElapsedTime("" + df.format(timeTaken));
	      }
	    }
	    catch (SQLException se)
	    {
	      // we don't want to stop it running we just need the error
	      res.setMessage(se.getMessage());
	      res.setRows(-1);
	    }
	    finally 
	    {
	      JDBCUtil.close(stmt);
	    }
	    
	    return res;
	  }
	  
	  static public int checkForDynamicResultSetProc (Connection conn, String schema, String procName) throws SQLException
	  {
		   PreparedStatement stmt = null;
		   ResultSet rset = null;
		   String sql = "select cast (a.aliasinfo as varchar(1000)) " +
				        "from sys.sysaliases a, sys.sysschemas s " +
				        "where aliastype = 'P' " + 
				        "and a.schemaid = s.schemaid " +
				        "and s.schemaname = ? " +
				        "and a.alias = ?";
		   
		   try
		   {
			   stmt = conn.prepareStatement(sql);
			   stmt.setString(1, schema);
			   stmt.setString(2, procName);
			   rset = stmt.executeQuery();
			   
			   rset.next();
			   
			   if (rset != null)
			   {
				   String res = rset.getString(1);
				   
				   int index = res.indexOf("DYNAMIC RESULT SETS ");
				   
				   if (index == -1)
				   {
					   // does not exist
					   return 0;
				   }
				   
				   return Integer.parseInt(res.substring(index + 20));
			   }
	
		   }
		   catch (SQLException se)
		   {
			   return 0;
		   }
		   finally
		   {
			   JDBCUtil.close(stmt);
			   JDBCUtil.close(rset);
		   }
		 
		   return 0;
	   
	  }
		
	  static public Map<String, String> populateSchemaMap(Connection conn, Map<String, String> schemaMap, String schema) throws SQLException
	  {
		  String sql = 
				  "select object_type, count(*) " +
				  "from (select tablename, 'Table' as OBJECT_TYPE FROM SYS.SYSTABLES t, sys.sysschemas s WHERE s.schemaid = t.schemaid and t.TABLESCHEMANAME = ? and t.tabletype not in ('V', 'A') and t.datapolicy not like 'HDFS%' and t.offheapenabled = 0) x " +
				  "group by object_type " +
				  "union " + 
				  "select object_type, count(*) " +
				  "from (select v.tableid, 'View' as OBJECT_TYPE FROM SYS.SYSVIEWS v, SYS.SYSSCHEMAS s, sys.systables t WHERE s.schemaid = v.compilationschemaid and t.tableid = v.tableid AND s.schemaname = ?) y " + 
				  "group by object_type " +
				  "union " +
				  "select object_type, count(*) " + 
				  "from (select constraintname, 'Constraint' as OBJECT_TYPE FROM sys.systables t, sys.sysconstraints c, sys.sysschemas s where s.schemaid = c.schemaid and t.tableid = c.tableid and s.schemaname = ?) z " +
				  "group by object_type " +
				  "union " + 
				  "select object_type, count(*) " + 
				  //"from (select index, 'Index' as OBJECT_TYPE FROM sys.indexes where \"SCHEMA\" = ?) z " +
				  // add when using latest version
				  "from (select indexname, 'Index' as OBJECT_TYPE FROM sys.indexes where schemaname = ?) z " +
				  "group by object_type " + 
				  "union " +
				  "select object_type, count(*) " +  
				  "from (select triggerid, 'Trigger' as OBJECT_TYPE from SYS.SYSTRIGGERS t, sys.SYSSCHEMAS s where s.schemaid = t.schemaid and s.schemaname = ?) bb " +
				  "group by object_type " +
				  "union " +
				  "select object_type, count(*) " +
				  "from (select alias, 'Procedure' as OBJECT_TYPE from SYS.SYSALIASES a, sys.SYSSCHEMAS s where s.schemaid = a.schemaid and a.aliastype='P' and s.schemaname = ?) cc " +
				  "group by object_type " + 
				  "union " +
				  "select object_type, count(*) " +
				  "from (select alias, 'Function' as OBJECT_TYPE from SYS.SYSALIASES a, sys.SYSSCHEMAS s where s.schemaid = a.schemaid and a.aliastype='F' and s.schemaname = ?) dd " +
				  "group by object_type " +
				  "union " +
				  "select object_type, count(*) from (select 1, 'Diskstore' as OBJECT_TYPE from SYS.SYSDISKSTORES) ee " +
				  "group by object_type " + 
				  "union " +
				  "select object_type, count(*) " +
				  "from (select 1, 'AsyncEventList' as OBJECT_TYPE from SYS.ASYNCEVENTLISTENERS) ff " +
				  "group by object_type " + 
				  "union " +
				  "select object_type, count(*) " +
				  "from (select 1, 'Sender' as OBJECT_TYPE from SYS.GATEWAYSENDERS) gg " +
				  "group by object_type " +
				  "union " +
				  "select object_type, count(*) " +
				  "from (select 1, 'Receiver' as OBJECT_TYPE from SYS.GATEWAYRECEIVERS) hh " +
				  "group by object_type " + 
				  "union " +
				  "select object_type, count(*) " +
				  "from (select 1, 'Type' as OBJECT_TYPE from sys.sysaliases WHERE  aliasschemaname = ? and aliastype = 'A') ii " +
				  "group by object_type " +
                  "union " +
                  "select object_type, count(*) " +
                  "from (select tablename, 'TableHDFS' as OBJECT_TYPE FROM SYS.SYSTABLES t, sys.sysschemas s WHERE s.schemaid = t.schemaid and t.TABLESCHEMANAME = ? and t.tabletype not in ('V', 'A') and t.datapolicy like 'HDFS%') x " +
                  "group by object_type " +
                  "union " +
                  "select object_type, count(*) " +
                  "from (select tablename, 'TableOFFHEAP' as OBJECT_TYPE FROM SYS.SYSTABLES t, sys.sysschemas s WHERE s.schemaid = t.schemaid and t.TABLESCHEMANAME = ? and t.tabletype not in ('V', 'A') and t.offheapenabled = 1) x " +
                  "group by object_type " +
                  "union " +
                  "select object_type, count(*) from (select 1, 'Hdfsstore' as OBJECT_TYPE from SYS.SYSHDFSSTORES) ll " +
                  "group by object_type";

          PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  Map<String, String> schemaMapLocal = schemaMap;
		  
		  try
		  {
			  pstmt = conn.prepareStatement(sql);
			  pstmt.setString(1, schema);
			  pstmt.setString(2, schema);
			  pstmt.setString(3, schema);
			  pstmt.setString(4, schema);
			  pstmt.setString(5, schema);
			  pstmt.setString(6, schema);
			  // add back in when index fixed
			  pstmt.setString(7, schema); 
			  pstmt.setString(8, schema);
              pstmt.setString(9, schema);
              pstmt.setString(10, schema);

			  rset = pstmt.executeQuery();
			  while (rset.next())
			  {
				  schemaMapLocal.put(rset.getString(1).trim(), rset.getString(2));
			  }
			  
			  //System.out.println(schemaMapLocal);
		  }
		  finally
		  {
			  JDBCUtil.close(pstmt);
		  }
		  
		  return schemaMapLocal;
	  }
}
