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
package pivotal.au.se.gemfirexdweb.reports;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;

import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

public abstract class QueryBase implements Query 
{

	  private String queryDescription;
	  private String query;
	  
	  public QueryBase()
	  {
	    super();
	  }

	  public void setQueryDescription(String queryDescription)
	  {
	    this.queryDescription = queryDescription;
	  }

	  public String getQueryDescription()
	  {
	    return queryDescription;
	  }

	  public void setQuery(String query)
	  {
	    this.query = query;
	  }

	  public String getQuery()
	  {
	    return query;
	  }
	  
	  public Result invoke(Connection conn) throws SqlFireException, SQLException
	  {
	    Result res = null;
	    res= QueryUtil.runQuery(conn, query, -1);
	    return res;
	  }

}
