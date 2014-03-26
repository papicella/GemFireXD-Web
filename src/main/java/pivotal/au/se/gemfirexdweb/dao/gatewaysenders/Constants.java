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
package pivotal.au.se.gemfirexdweb.dao.gatewaysenders;

public interface Constants 
{
	// sqlfire 1.1.1 = server_groups
	public static final String USER_GATEWAY_SENDERS = 
			"select sender_id, remote_ds_id, server_groups, is_started " +
			"from sys.GATEWAYSENDERS " +
			"where sender_id like ? " +
			"order by 1";

    public static final String USER_GATEWAY_SENDERS_FOR_ADD =
            "select sender_id, remote_ds_id, server_groups, is_started " +
            "from sys.GATEWAYSENDERS " +
            "order by 1";

	public static String START_GATEWAY_SENDER = "CALL SYS.START_GATEWAYSENDER ('%s')";
	
	public static String STOP_GATEWAY_SENDER = "CALL SYS.STOP_GATEWAYSENDER ('%s')";
	
	public static String DROP_GATEWAY_SENDER = "DROP GATEWAYSENDER \"%s\"";

	public static final String VIEW_ALL_USER_GATEWAY_SENDER_INFO = 
			"select * " +
			"from sys.GATEWAYSENDERS " +
			"where sender_id = '%s'";

    public static String TABLE_GATEWAY_SENDERS =
        "select m.id, m.netservers, g.sender_id, g.server_groups, s.schemaname, t.tablename " +
        "from sys.members m, sys.gatewaysenders g, SYS.SYSTABLES t, sys.sysschemas s " +
        "where t.tablename = ? " +
        "and groupsintersect(m.servergroups, g.server_groups) " +
        "and groupsintersect(t.gatewaysenders, g.sender_id) " +
        "and groupsintersect(t.schemaid, s.schemaid)";

    public static String RUNNING_GATEWAY_SENDERS =
            "select m.id, m.netservers, g.sender_id, g.server_groups, s.schemaname, t.tablename " +
            "from sys.members m, sys.gatewaysenders g, SYS.SYSTABLES t, sys.sysschemas s " +
            "where g.sender_id = ? " +
            "and groupsintersect(m.servergroups, g.server_groups) " +
            "and groupsintersect(t.gatewaysenders, g.sender_id) " +
            "and groupsintersect(t.schemaid, s.schemaid)";
}
