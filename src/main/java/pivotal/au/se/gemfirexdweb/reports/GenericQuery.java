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

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;

public class GenericQuery extends QueryBase 
{
	  protected static Logger logger = Logger.getLogger("controller");
	
	  @Override
	  public Result invoke(Connection conn) throws SqlFireException, SQLException
	  {
	    logger.info("GenericQuery - invoke called");
	    return super.invoke(conn);
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
}
