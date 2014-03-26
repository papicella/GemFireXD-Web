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
package pivotal.au.se.gemfirexdweb.dao.members;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;

import org.apache.log4j.Logger;

import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

public class MemberDAOImpl implements MemberDAO 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	public List<Member> retrieveMembers(String userKey) throws SqlFireException 
	{
	    Connection        conn = null;
	    Statement 		  stmt = null;
	    ResultSet         rset = null;
	    List<Member>  	  members = null;
	    
	    try
	    {
	      conn = AdminUtil.getConnection(userKey);
	      stmt = conn.createStatement();
	      
	      rset = stmt.executeQuery(Constants.ALL_MEMBERS);

	      members = makeMemberListFromResultSet(rset);
	    }
	    catch (SQLException se)
	    {
	      logger.debug("Error retrieving all members");
	      throw new SqlFireException(se);
	    }
	    catch (Exception ex)
	    {
	      logger.debug("Error retrieving all members");
	      throw new SqlFireException(ex);      
	    }
	    finally
	    {
	      // close all resources
	      JDBCUtil.close(rset);
	      JDBCUtil.close(stmt);
	    }
	    
	    return members;
	}

    public String getMemberProperties (String memberId, String type, String userKey) throws SqlFireException
    {
        String              result = null;
        Connection          conn = null;
        PreparedStatement   stmt = null;
        ResultSet           rset = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);

            if (type.equalsIgnoreCase("GEMFIRE"))
            {
                stmt = conn.prepareStatement(Constants.VIEW_GEMFIREPROPS_FOR_MEMBER);
            }
            else if (type.equalsIgnoreCase("BOOT"))
            {
                stmt = conn.prepareStatement(Constants.VIEW_BOOTPROPS_FOR_MEMBER);
            }
            else if (type.equalsIgnoreCase("SYSTEM"))
            {
                stmt = conn.prepareStatement(Constants.VIEW_SYSTEMPROPS_FOR_MEMBER);
            }

            stmt.setString(1, memberId);

            rset = stmt.executeQuery();

            if (rset != null)
            {
                rset.next();
                result = rset.getString(1);
            }
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving member properties for : " + memberId);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving member properties for : " + memberId);
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return result;
    }

	private List<Member> makeMemberListFromResultSet (ResultSet rset) throws SQLException
	{
		List<Member> members = new ArrayList<Member>();
		
		while (rset.next())
		{
			Member member = new Member(rset.getString(1),
				                	   rset.getString(2),
				                	   rset.getString(3),
				                	   rset.getString(4),
				                	   rset.getString(5),
				                	   rset.getString(6),
				                	   rset.getString(7),
				                	   rset.getString(8),
				                	   rset.getString(9));
			members.add(member);
		}
		
		return members;
		
	}

	public Result getMemberInfo(String memberId, String userKey) throws SqlFireException 
	{
		Connection        conn = null;
		javax.servlet.jsp.jstl.sql.Result res = null;
		
		try 
		{
			conn = AdminUtil.getConnection(userKey);
			res = QueryUtil.runQuery
					(conn, String.format(Constants.VIEW_ALL_MEMBER_INFO, memberId), -1);
		} 
		catch (Exception e) 
		{
		      logger.debug("Error retrieving all member info for meber with id : " + memberId);
		      throw new SqlFireException(e); 
		}
		
		return res;
	}

}
