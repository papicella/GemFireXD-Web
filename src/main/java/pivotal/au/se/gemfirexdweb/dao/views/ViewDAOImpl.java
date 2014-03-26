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
package pivotal.au.se.gemfirexdweb.dao.views;

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

public class ViewDAOImpl implements ViewDAO
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<View> retrieveViewList(String schema, String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<View>        views = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_VIEWS);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, schema);
	      stmt.setString(2, srch);  
	      rset = stmt.executeQuery();

	      views = makeViewListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all views with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all views with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return views;
	}

	public Result simpleviewCommand(String schemaName, String viewName, String type, String userKey) throws SqlFireException 
	{
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_VIEW, schemaName, viewName);
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}
	
	private List<View> makeViewListFromResultSet (ResultSet rset) throws SQLException
	{
		List<View> views = new ArrayList<View>();
		
		while (rset.next())
		{
			View view = new View(rset.getString(1),
					             rset.getString(2),
					             rset.getString(4),
					             rset.getString(3));
			views.add(view);
		}
		
		return views;
		
	}

}
