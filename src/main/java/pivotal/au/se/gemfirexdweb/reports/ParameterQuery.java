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

import org.apache.log4j.Logger;

import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;


public class ParameterQuery extends QueryBase 
{
	protected static Logger logger = Logger.getLogger("controller");

	  private Map paramMap;
	  private Map paramValues;
	  
	  @Override
	  public Result invoke(Connection conn) throws SqlFireException, SQLException
	  {
		logger.info("ParamaterQuery - invoke called");
	    Result res = null;
	    PreparedStatement pstmt = null;
	    ResultSet rset  = null;

	    try
	    {
	      pstmt = conn.prepareStatement(getQuery());

	      // TODO: read params, create PreparedStatement, set variables, runquery
	      @SuppressWarnings("unchecked")
		  Set<String> keys = getParamMap().keySet();
	      int i = 0;
	      for (String param: keys)
	      {
	    	i++;
	        pstmt.setString(Integer.parseInt(param), (String) paramValues.get(param));
	      }

	      rset = pstmt.executeQuery();
	      res = ResultSupport.toResult(rset);
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(pstmt);
	    }
	    
	    return res;
	  }

	  public void setParamMap(Map paramMap)
	  {
	    this.paramMap = paramMap;
	  }

	  public Map getParamMap()
	  {
	    return paramMap;
	  }

	  @Override
	  public String getQuery()
	  {
	    return super.getQuery();
	  }

	  @Override
	  public String getQueryDescription()
	  {
	    return super.getQueryDescription();
	  }

	  public void setParamValues(Map paramValues)
	  {
	    this.paramValues = paramValues;
	  }

	  public Map getParamValues()
	  {
	    return paramValues;
	  }
}
