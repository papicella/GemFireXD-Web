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
package pivotal.au.se.gemfirexdweb.dao.asyncevent;

public interface Constants 
{
	// sqlfire 1.1.1 = server_groups
	public static final String USER_ASYNCEVENTS = 
			"select id, listener_class, server_groups, is_started " +
			"from   sys.ASYNCEVENTLISTENERS " +
			"where  id like ? " +
            "order by 1";

    public static final String USER_ASYNCEVENTS_FOR_ADD =
            "select id, listener_class, server_groups, is_started " +
            "from   sys.ASYNCEVENTLISTENERS order by 1";

	public static String START_ASYNC = "call SYS.START_ASYNC_EVENT_LISTENER('%s')";
	
	public static String STOP_ASYNC = "call SYS.STOP_ASYNC_EVENT_LISTENER('%s')";
	
	public static String DROP_ASYNC = "DROP ASYNCEVENTLISTENER \"%s\"";

	public static final String VIEW_ALL_USER_ASYNCEVENT_INFO = 
			"select * " +
			"from   sys.ASYNCEVENTLISTENERS " +
			"where  id = '%s'";

    public static final String ASYNC_TABLES =
            "select m.ID DSID, m.status, s.schemaname, t.tablename, t.datapolicy, t.GATEWAYENABLED, t.servergroups " +
            "from SYS.SYSTABLES t, SYS.MEMBERS m, SYS.ASYNCEVENTLISTENERS a, sys.sysschemas s " +
            "where a.id = ? " +
            "and groupsintersect(a.SERVER_GROUPS, m.SERVERGROUPS) " +
            "and groupsintersect(t.ASYNCLISTENERS, a.ID) " +
            "and groupsintersect(t.schemaid, s.schemaid)";

    public static final String GENERATE_DDL =
            "select * " +
            "from   sys.ASYNCEVENTLISTENERS " +
            "where  id = ?";
}
