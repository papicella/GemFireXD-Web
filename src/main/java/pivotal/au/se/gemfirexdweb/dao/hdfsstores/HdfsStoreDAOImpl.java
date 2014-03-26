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
package pivotal.au.se.gemfirexdweb.dao.hdfsstores;

import org.apache.log4j.Logger;
import pivotal.au.se.gemfirexdweb.dao.GemFireXDWebDAOUtil;
import pivotal.au.se.gemfirexdweb.main.Result;
import pivotal.au.se.gemfirexdweb.main.SqlFireException;
import pivotal.au.se.gemfirexdweb.utils.AdminUtil;
import pivotal.au.se.gemfirexdweb.utils.JDBCUtil;
import pivotal.au.se.gemfirexdweb.utils.QueryUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HdfsStoreDAOImpl  implements HdfsStoreDAO
{

    protected static Logger logger = Logger.getLogger("controller");

    public List<HdfsStore> retrieveHdfsStoreList(String schema, String search, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        List<HdfsStore>   hdfsStores = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_HDFS_STORES);
            if (search == null)
                srch = "%";
            else
                srch = "%" + search.toUpperCase() + "%";

            stmt.setString(1, srch);
            rset = stmt.executeQuery();

            hdfsStores = makeHdfsStoreListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all HDFS stores with search string = " + search);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all HDFS stores with search string = " + search);
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return hdfsStores;
    }

    public List<HdfsStore> retrieveHdfsStoreForCreateList (String userKey) throws SqlFireException
    {
        Connection        conn = null;
        Statement stmt = null;
        ResultSet         rset = null;
        List<HdfsStore>   hdfsStores = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.createStatement();

            rset = stmt.executeQuery(Constants.USER_HDFS_STORES_FOR_CREATE);

            hdfsStores = makeHdfsStoreListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all HDFS stores for create");
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all HDFS stores for create");
            throw new SqlFireException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return hdfsStores;
    }

    private List<HdfsStore> makeHdfsStoreListFromResultSet (ResultSet rset) throws SQLException
    {
        List<HdfsStore> hdfsStores = new ArrayList<HdfsStore>();

        while (rset.next())
        {
            HdfsStore ds = new HdfsStore(rset.getString(1), rset.getString(2), rset.getString(3));
            hdfsStores.add(ds);
        }

        return hdfsStores;

    }
    public Result simplehdfsStoreCommand(String hdfsStoreName, String type, String userKey) throws SqlFireException
    {
        String            command = null;
        Result            res     = null;

        if (type != null)
        {
            if (type.equalsIgnoreCase("DROP"))
            {
                command = String.format(Constants.DROP_HDFS_STORE, hdfsStoreName);
            }
        }

        res = GemFireXDWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    public javax.servlet.jsp.jstl.sql.Result getHdfsStoreInfo(String hdfsStoreName, String userKey) throws SqlFireException
    {
        Connection conn = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            res = QueryUtil.runQuery
                    (conn, String.format(Constants.VIEW_ALL_HDFSSTORE_INFO, hdfsStoreName), -1);
        }
        catch (Exception e)
        {
            logger.debug("Error retrieving all HDFS store info with name : " + hdfsStoreName);
            throw new SqlFireException(e);
        }

        return res;
    }

    public String generateDDL (String hdfsStoreName, String userKey) throws SqlFireException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        StringBuffer      ddl  = new StringBuffer();

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.GENERATE_DDL);

            stmt.setString(1, hdfsStoreName);

            rset = stmt.executeQuery();
            if (rset != null)
            {
                rset.next();

                // create ddl here
                ddl.append("CREATE HDFSSTORE " + rset.getString("NAME") + " \n");
                ddl.append("NAMENODE '" + rset.getString("NAMENODE") + "' \n");
                ddl.append("HOMEDIR '" + rset.getString("HOMEDIR") + "' \n");
                ddl.append("MAXQUEUEMEMORY " + rset.getString("MAXQUEUEMEMORY") + " \n");
                ddl.append("BATCHSIZE " + rset.getString("BATCHSIZE") + " \n");
                ddl.append("BATCHTIMEINTERVAL " + rset.getString("BATCHTIMEINTERVAL") + " \n");
                ddl.append("QUEUEPERSISTENT " + rset.getString("QUEUEPERSISTENT") + " \n");
                ddl.append("DISKSYNCHRONOUS " + rset.getString("DISKSYNCHRONOUS") + " \n");

                if (rset.getString("DISKSTORENAME") != null)
                {
                    ddl.append("DISKSTORENAME " + rset.getString("DISKSTORENAME") + " \n");
                }

                ddl.append("MINORCOMPACT " + rset.getString("MINORCOMPACT") + " \n");
                ddl.append("MAJORCOMPACT " + rset.getString("MAJORCOMPACT") + " \n");
                ddl.append("MAXINPUTFILESIZE " + rset.getString("MAXINPUTFILESIZE") + " \n");
                ddl.append("MININPUTFILECOUNT " + rset.getString("MININPUTFILECOUNT") + " \n");
                ddl.append("MAXINPUTFILECOUNT " + rset.getString("MAXINPUTFILECOUNT") + " \n");
                ddl.append("MINORCOMPACTIONTHREADS " + rset.getString("MINORCOMPACTIONTHREADS") + " \n");
                ddl.append("MAJORCOMPACTIONINTERVAL " + rset.getString("MAJORCOMPACTIONINTERVAL") + " \n");
                ddl.append("MAJORCOMPACTIONTHREADS " + rset.getString("MAJORCOMPACTIONTHREADS") + " \n");

                if (rset.getString("CLIENTCONFIGFILE") != null)
                {
                    ddl.append("CLIENTCONFIGFILE '" + rset.getString("CLIENTCONFIGFILE") + "' \n");
                }

                ddl.append("BLOCKCACHESIZE " + rset.getString("BLOCKCACHESIZE") + " \n");
                ddl.append("MAXWRITEONLYFILESIZE " + rset.getString("MAXWRITEONLYFILESIZE") + " \n");
                ddl.append("WRITEONLYFILEROLLOVERINTERVAL " + rset.getString("WRITEONLYFILEROLLOVERINTERVAL") + " \n");
                ddl.append("PURGEINERVAL " + rset.getString("PURGEINERVAL") + "; \n");

            }
        }
        catch (SQLException se)
        {
            logger.debug("Error generating DDl for HDFS store " + hdfsStoreName);
            throw new SqlFireException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error generating DDl for HDFS store " + hdfsStoreName);
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

