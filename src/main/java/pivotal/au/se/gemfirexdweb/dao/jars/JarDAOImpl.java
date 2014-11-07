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
package pivotal.au.se.gemfirexdweb.dao.jars;

import org.apache.log4j.Logger;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JarDAOImpl implements JarDAO
{

    protected static Logger logger = Logger.getLogger("controller");

    public List<Jar> retrieveJarList(String schema, String search, String userKey) throws SqlFireException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        List<Jar>   jars = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.ALL_JARS);
            if (search == null)
                srch = "%";
            else
                srch = "%" + search.toUpperCase() + "%";

            stmt.setString(1, srch);
            rset = stmt.executeQuery();

            jars = makeJarListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all JARS with search string = " + search);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all JARS with search string = " + search);
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return jars;
    }

    @Override
    public Result simpleJarCommand(String schemaName, String alias, String type, String userKey) throws SqlFireException
    {
        String            command = null;
        Result            res     = null;

        if (type != null)
        {
            if (type.equalsIgnoreCase("REMOVE"))
            {
                command = String.format(Constants.REMOVE_JAR, schemaName, alias);
            }
        }

        res = GemFireXDWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    private List<Jar> makeJarListFromResultSet (ResultSet rset) throws SQLException
    {
        List<Jar> jars = new ArrayList<Jar>();

        while (rset.next())
        {
            Jar jar = new Jar(rset.getString(1), rset.getString(2), rset.getString(3));
            jars.add(jar);
        }

        return jars;

    }
}
