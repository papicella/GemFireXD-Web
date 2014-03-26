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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class ConnectionManager 
{

	protected static Logger logger = Logger.getLogger("controller");
	private Map<String,SQLFireJDBCConnection> conList = new ConcurrentHashMap<String, SQLFireJDBCConnection>();

	private static ConnectionManager instance = null;
	
	static
	{
		instance = new ConnectionManager();
	}
  
	private ConnectionManager()
	{
		// Exists only to defeat instantiation.
	}

	public static ConnectionManager getInstance() throws Exception
	{     
		return instance;
	}
	  
	public void addConnection (SQLFireJDBCConnection conn, String key)  
	{
	    conList.put(key, conn);
	    logger.debug("Connection added with key " + key);
	}
	
	public Connection getConnection (String key)
	{
        if (conList.containsKey(key))
        {
            return conList.get(key).getConn();
        }
        else
        {
            return null;
        }
	}
	  
	public void updateConnection(Connection conn, String key)
	{
		SQLFireJDBCConnection sqlfireConn = conList.get(key);
		sqlfireConn.setConn(conn);
		conList.put(key, sqlfireConn);
		logger.debug("Connection updated with key " + key);
	}
	
	public void removeConnection(String key) throws SQLException
	{
		if (conList.containsKey(key))
		{
		    Connection conn = getConnection(key); 
		    if (conn != null)
		    {
		      conn.close();
		      conn = null;
		    }
		    
		    conList.remove(key);
		    logger.debug("Connection removed with key " + key);
		}
		else
		{
			logger.debug("No connection with key " + key + " exists");
		}
	}
	  
	public Map <String,SQLFireJDBCConnection> getConnectionMap() 
	{
		return conList; 
	}
	  
	public int getConnectionListSize ()
	{
		return conList.size();
	}
	  
	public String displayMap ()
	{
	    StringBuffer sb = new StringBuffer();
	    
	    sb.append("-- Current Connection List --\n\n");
	    sb.append("Size = " + getConnectionListSize() + "\n\n");
		for (String key : conList.keySet())
		{
		  sb.append(String.format("Key %s, Connection %s\n", key, getConnection(key)));
		}
	  
	    return sb.toString(); 
	}
}
