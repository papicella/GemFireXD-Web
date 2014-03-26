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
package pivotal.au.se.gemfirexdweb.dao.tables;

public interface Constants 
{
	public static final String USER_TABLES =
			"select tableschemaname, tablename, servergroups, datapolicy, ASYNCLISTENERS, GATEWAYSENDERS " +
			"FROM SYS.SYSTABLES t, sys.sysschemas s " +
			"WHERE s.schemaid = t.schemaid " +
			"and   t.TABLESCHEMANAME = ? " +
			"and   t.tablename like ? " +
			"and   t.tabletype not in ('V', 'A') " +
            "and   t.datapolicy not like 'HDFS%' " +
            "and   t.offheapenabled = 0 " +
			"order by 1, 2";

    public static final String USER_TABLES_HDFS =
            "select tableschemaname, tablename, servergroups, datapolicy, ASYNCLISTENERS, GATEWAYSENDERS " +
                    "FROM SYS.SYSTABLES t, sys.sysschemas s " +
                    "WHERE s.schemaid = t.schemaid " +
                    "and   t.TABLESCHEMANAME = ? " +
                    "and   t.tablename like ? " +
                    "and   t.tabletype not in ('V', 'A') " +
                    "and   t.datapolicy like 'HDFS%' " +
                    "order by 1, 2";

    public static final String USER_TABLES_OFFHEAP =
            "select tableschemaname, tablename, servergroups, datapolicy, ASYNCLISTENERS, GATEWAYSENDERS " +
                    "FROM SYS.SYSTABLES t, sys.sysschemas s " +
                    "WHERE s.schemaid = t.schemaid " +
                    "and   t.TABLESCHEMANAME = ? " +
                    "and   t.tablename like ? " +
                    "and   t.tabletype not in ('V', 'A') " +
                    "and   t.offheapenabled = 1 " +
                    "order by 1, 2";

	public static String DROP_TABLE = "DROP TABLE %s.\"%s\"";
	
	public static String TRUNCATE_TABLE = "TRUNCATE TABLE %s.\"%s\"";

    public static String ADD_ASYNC_EVENT_LISTENER = "ALTER TABLE %s.\"%s\" SET ASYNCEVENTLISTENER (%s)";

    public static String REMOVE_ASYNC_EVENT_LISTENERS = "ALTER TABLE %s.\"%s\" SET ASYNCEVENTLISTENER ()";

    public static String ADD_GATEWAYSENDER = "ALTER TABLE %s.\"%s\"SET GATEWAYSENDER (%s)";

    public static String REMOVE_GATEWAYSENDERS = "ALTER TABLE %s.\"%s\" SET GATEWAYSENDER ()";

    public static String GRANT_TABLE_PRIV = "GRANT %s ON TABLE %s.\"%s\" TO %s";

    public static String REVOKE_TABLE_PRIV = "REVOKE %s ON TABLE %s.\"%s\" FROM %s";

	public static String TABLE_DATA_LOCATION = 
			"select dsid() Member, count(*) as \"Rows\" from %s.\"%s\" group by dsid()";

	public static String TABLE_MEMORY_USAGE = 
			"SELECT * FROM sys.memoryAnalytics where table_name = '%s.%s'";

    public static String TABLE_MEMORY_USAGE_SUM =
            "select id, table_name, sum(total_size) as \"Size\" " +
            "from sys.memoryAnalytics " +
            "where table_name = ? " +
            "group by id, table_name";

	public static String LOAD_TABLE_SCRIPT = 
			"connect client 'localhost:1527;load-balance=false;read-timeout=0'; \n" +
            "ELAPSEDTIME on; \n" +
            "call syscs_util.import_table_ex('%s' /* schema */, \n"  +
            "'%s' /* table */, \n" + 
            "'/fullpathtoloadfile/%s.csv' /* file path as seen by server */, \n" +
            "',' /* field separator */, \n" + 
            "NULL, \n" + 
            "NULL, \n" + 
            "0, \n" + 
            "0 /* don't lock the table */, \n" +
            "6 /* number of threads */, \n" +
            "0, \n" + 
            "NULL /* custom class for data transformation or NULL to use the default inbuilt Import class */, \n" + 
            "NULL); \n";
	
	public static String VIEW_PARTITION_ATTRS =
			"select partitionattrs " +
			"FROM   SYS.SYSTABLES t, sys.sysschemas s " + 
			"WHERE  s.schemaid = t.schemaid " + 
			"and    t.TABLESCHEMANAME = ? " + 
			"and    t.tablename = ?";

	public static String VIEW_EVICTION_EXPIRATION_ATTRS =
			"select expirationattrs, evictionattrs " +
			"FROM   SYS.SYSTABLES t, sys.sysschemas s " + 
			"WHERE  s.schemaid = t.schemaid " + 
			"and    t.TABLESCHEMANAME = '%s' " + 
			"and    t.tablename = '%s'";

	public static String VIEW_ALL_TABLE_COLUMNS =
			"select * " +
			"FROM   SYS.SYSTABLES t " + 
			"WHERE  t.TABLESCHEMANAME = '%s' " + 
			"and    t.tablename = '%s'";
	
	public static String VIEW_TABLE_STRUCTURE =
			"select c.columnname as \"ColumnName\", CAST(c.COLUMNDATATYPE AS VARCHAR(100)) as \"Type\" " +
			"FROM SYS.SYSCOLUMNS c, sys.systables t " + 
			"where c.referenceid = t.tableid " +
			"and t.tableschemaname = '%s' " +
			"and t.tablename = '%s' " + 
			"order by c.columnnumber";

    public static String VIEW_ASYNC_EVENT_LISTENERS =
            "select m.ID DSID, m.status, s.schemaname, t.tablename, t.datapolicy, t.GATEWAYENABLED, t.servergroups " +
            "from SYS.SYSTABLES t, SYS.MEMBERS m, SYS.ASYNCEVENTLISTENERS a, sys.sysschemas s " +
            "where t.tablename = ? " +
            "and groupsintersect(a.SERVER_GROUPS, m.SERVERGROUPS) " +
            "and groupsintersect(t.ASYNCLISTENERS, a.ID) " +
            "and groupsintersect(t.schemaid, s.schemaid)";

    public static String VIEW_TABLE_TRIGGERS =
            "select a.triggerid, a.triggername, t.tablename, s.schemaname, a.event, a.type, a.firingtime, a.state " +
            "from   sys.systriggers a, sys.systables t, sys.sysschemas s " +
            "where  a.tableid = t.tableid " +
            "and    t.schemaid = s.schemaid " +
            "and    s.schemaname = ? " +
            "and    t.tablename = ?";

    public static String VIEW_TABLE_PRIVS =
            "select p.grantee, p.grantor, s.schemaname, t.tablename, p.selectpriv, p.deletepriv, p.triggerpriv " +
            "from   sys.systableperms p, sys.systables t, sys.sysschemas s " +
            "where  p.tableid = t.tableid " +
            "and    t.schemaid = s.schemaid " +
            "and    s.schemaname = ? " +
            "and    t.tablename = ?";

}
