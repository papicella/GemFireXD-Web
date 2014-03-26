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
package pivotal.au.se.gemfirexdweb.dao.types;

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

public class TypeDAOImpl implements TypeDAO 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<Type> retrieveTypeList(String schema, String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<Type>        types = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_TYPES);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, schema);
	      stmt.setString(2, srch);  
	      rset = stmt.executeQuery();

	      types = makeTypeListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all types with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all types with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return types;
	}

	public Result simpletypeCommand(String schemaName, String typeName, String type, String userKey) throws SqlFireException 
	{
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_TYPE, schemaName, typeName);
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}

	private List<Type> makeTypeListFromResultSet (ResultSet rset) throws SQLException
	{
		List<Type> types = new ArrayList<Type>();
		
		while (rset.next())
		{
			Type type = new Type(rset.getString(1),
					             rset.getString(2),
					             rset.getString(3));
			types.add(type);
		}
		
		return types;
		
	}

    public String generateDDL (String schemaName, String typeName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        StringBuffer      ddl  = new StringBuffer();

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.GENERATE_DDL);

            stmt.setString(1, schemaName);
            stmt.setString(2, typeName);

            rset = stmt.executeQuery();
            if (rset != null)
            {
                rset.next();

                // create ddl here
                ddl.append("CREATE TYPE " + schemaName + "." + typeName + " \n");
                ddl.append("EXTERNAL NAME '" + rset.getString("JAVACLASSNAME") + "' \n");
                ddl.append("LANGUAGE JAVA; \n");
            }
        }
        catch (SQLException se)
        {
            logger.debug("Error generating DDL for Type " + typeName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error generating DDL for Type " + typeName);
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return ddl.toString();

    }

}
