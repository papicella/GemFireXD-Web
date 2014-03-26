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
package pivotal.au.se.gemfirexdweb.dao.indexes;

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

public class IndexDAOImpl implements IndexDAO
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<Index> retrieveIndexList(String schema, String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<Index>       indexes = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_INDEXES);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, schema);
	      stmt.setString(2, srch);  
	      rset = stmt.executeQuery();

	      indexes = makeIndexListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all indexes with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all indexes with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return indexes;
	}

	public Result simpleindexCommand(String schemaName, String indexName, String type, String userKey) throws SqlFireException 
	{
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_INDEX, schemaName, indexName);
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}



	public List<IndexColumn> retrieveIndexColumns
		(String schemaName, String tableName, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<IndexColumn> columns = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.VIEW_TABLE_COLUMNS);
	      
	      stmt.setString(1, schemaName);
	      stmt.setString(2, tableName);  
	      rset = stmt.executeQuery();

	      columns =  makeIndexColumnListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all indexe columns for table = " + tableName);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all indexe columns for table = " + tableName);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return columns;
	}

	private List<Index> makeIndexListFromResultSet (ResultSet rset) throws SQLException
	{
		List<Index> indexes = new ArrayList<Index>();
		
		while (rset.next())
		{
			Index index = new Index(rset.getString(1),
					                rset.getString(2),
					                rset.getString(3),
					                rset.getString(4),
					                rset.getString(5),
					                rset.getString(6),
					                rset.getString(7));
			indexes.add(index);
		}
		
		return indexes;
		
	}

	private List<IndexColumn> makeIndexColumnListFromResultSet (ResultSet rset) throws SQLException
	{
		List<IndexColumn> columns = new ArrayList<IndexColumn>();
		
		while (rset.next())
		{
			IndexColumn idxCol = new IndexColumn(rset.getString(1));
			columns.add(idxCol);
		}
		
		return columns;
		
	}
}
