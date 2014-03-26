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
package pivotal.au.se.gemfirexdweb.dao.diskstores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

public class DiskStoreDAOImpl implements DiskStoreDAO 
{

	protected static Logger logger = Logger.getLogger("controller");
	
	public List<DiskStore> retrieveDiskStoreList
		(String schema, String search, String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    PreparedStatement stmt = null;
	    ResultSet         rset = null;
	    List<DiskStore>   dsks = null;
	    String            srch = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.prepareStatement(Constants.USER_DISKSTORES);
	      if (search == null)
	        srch = "%";
	      else
	        srch = "%" + search.toUpperCase() + "%";
	      
	      stmt.setString(1, srch);  
	      rset = stmt.executeQuery();

	      dsks = makeDiskStoreListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all diskstores with search string = " + search);
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all diskstores with search string = " + search);
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return dsks;
	}

	public Result simplediskStoreCommand
		(String diskStoreName, String type, String userKey) throws SqlFireException 
	{
	    String            command = null;
	    Result            res     = null;

	    if (type != null)
	    {
	      if (type.equalsIgnoreCase("DROP"))
	      {
	        command = String.format(Constants.DROP_DISKSTORE, diskStoreName);
	      }
	    }
	    
	    res = GemFireXDWebDAOUtil.runCommand(command, userKey);
	    
	    return res;
	}
	
	private List<DiskStore> makeDiskStoreListFromResultSet (ResultSet rset) throws SQLException
	{
		List<DiskStore> dsks = new ArrayList<DiskStore>();
		
		while (rset.next())
		{
			DiskStore ds = new DiskStore(rset.getString(1), rset.getString(2));
			dsks.add(ds);
		}
		
		return dsks;
		
	}

	public List<DiskStore> retrieveDiskStoreForCreateList(String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    Statement 		  stmt = null;
	    ResultSet         rset = null;
	    List<DiskStore>   dsks = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.createStatement();
	      rset = stmt.executeQuery(Constants.USER_DISKSTORES_FOR_CREATE);

	      dsks = makeDiskStoreListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all diskstores for create");
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all diskstores for create");
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return dsks;
	}

	public javax.servlet.jsp.jstl.sql.Result getDiskstoreInfo(
			String diskStoreName, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_ALL_DISKSTORE_INFO, diskStoreName), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving all disk store info for disk store with name : " + diskStoreName);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

    public String generateDDL (String diskStoreName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        StringBuffer      ddl  = new StringBuffer();
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.GENERATE_DDL);

            stmt.setString(1, diskStoreName);

            rset = stmt.executeQuery();
            if (rset != null)
            {
                rset.next();
                ddl.append("CREATE DISKSTORE " + rset.getString("NAME") + " \n");
                ddl.append("MAXLOGSIZE " + rset.getString("MAXLOGSIZE") + " \n");
                ddl.append("AUTOCOMPACT " + rset.getString("AUTOCOMPACT") + " \n");
                ddl.append("ALLOWFORCECOMPACTION " + rset.getString("ALLOWFORCECOMPACTION") + " \n");
                ddl.append("COMPACTIONTHRESHOLD " + rset.getString("COMPACTIONTHRESHOLD") + " \n");
                ddl.append("TIMEINTERVAL " + rset.getString("TIMEINTERVAL") + " \n");
                ddl.append("WRITEBUFFERSIZE " + rset.getString("WRITEBUFFERSIZE") + " \n");
                ddl.append("QUEUESIZE " + rset.getString("QUEUESIZE") + " \n");

                String dirpath = rset.getString("DIR_PATH_SIZE");

                if (dirpath.contains(","))
                {
                    // need to split string
                    String[] parts = dirpath.split(",");
                    int i = 0;
                    int length = parts.length;

                    for (String s: parts)
                    {
                        i++;
                        if (i == 1)
                        {
                            ddl.append("(");
                        }

                        if (s.contains("("))
                        {
                          int start = s.indexOf("(");
                          int end = s.indexOf(")");
                          String size = s.substring(start + 1, end);
                          ddl.append("'" + s.substring(1, start) + "'");
                          ddl.append(" " + size);
                        }
                        else
                        {
                            ddl.append("'" + s + "'");
                        }

                        if (i == length)
                        {
                            ddl.append("); \n");
                        }
                        else
                        {
                            ddl.append(", \n");
                        }
                    }
                }
                else
                {
                    ddl.append("('" + rset.getString("DIR_PATH_SIZE") + "'); \n");
                }

            }

        }
        catch (SQLException se)
        {
            logger.debug("Error generating DDl for disk store " + diskStoreName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error generating DDl for disk store " + diskStoreName);
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
