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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pivotal.gemfirexd.jdbc.ClientDriver;

public class AdminUtil 
{
	protected static Logger logger = Logger.getLogger("controller");

	static public Connection getNewConnection 
	(String url,
	 String username,
	 String password) throws SQLException
	{
	   DriverManager.registerDriver(new ClientDriver());
	   Connection conn = DriverManager.getConnection(url,username,password);
	   return conn;
	}

	static public Connection getNewConnection (String url) throws SQLException
	{
	   DriverManager.registerDriver(new ClientDriver());
	   Connection conn = DriverManager.getConnection(url);
	   return conn;
	}
	
	/*
	 * Get connection from ConnectionManager conList Map
	 */
	static public Connection getConnection(String userKey) throws Exception
	{
		Connection conn = null;
		ConnectionManager cm = null;
    
		cm = ConnectionManager.getInstance();
		conn = cm.getConnection(userKey);
    
		return conn;
	}
	
	static public Map<String, String> getSchemaMap ()
	{
		Map<String, String> schemaMap = new HashMap<String, String>();
		
		schemaMap.put("Table", "0");
		schemaMap.put("Index", "0");
		schemaMap.put("View", "0");
		schemaMap.put("Constraint", "0");
		schemaMap.put("Trigger", "0");
		schemaMap.put("Procedure", "0");
		schemaMap.put("Function", "0");
		schemaMap.put("Diskstore", "0");
		schemaMap.put("AsyncEventList", "0");
		schemaMap.put("Sender", "0");
		schemaMap.put("Receiver", "0");
		schemaMap.put("Type", "0");
        schemaMap.put("TableHDFS", "0");
        schemaMap.put("Hdfsstore", "0");
        schemaMap.put("TableOFFHEAP", "0");
        schemaMap.put("Jar", "0");
		return schemaMap;
	}
}
