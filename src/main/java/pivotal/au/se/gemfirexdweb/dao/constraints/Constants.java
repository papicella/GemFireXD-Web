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
package pivotal.au.se.gemfirexdweb.dao.constraints;

public interface Constants 
{
	public static final String USER_CONSTRAINTS = 
			"select s.schemaname, t.tablename, c.CONSTRAINTNAME, c.TYPE, c.state, c.CONSTRAINTID " +
			"from sys.systables t, sys.sysconstraints c, sys.sysschemas s " +
			"where  s.schemaname = ? " +
			"and    s.schemaid = t.schemaid " +
			"and    t.tableid = c.tableid " +
			"and    c.constraintname like ? " +
            "order by 1, 2, 3, 4";
	
	public static String DROP_CONSTRAINT = "alter table %s.\"%s\" drop constraint \"%s\"";

    public static final String USER_TABLE_CONSTRAINTS =
            "select s.schemaname, t.tablename, c.CONSTRAINTNAME, c.TYPE, c.state, c.CONSTRAINTID " +
            "from sys.systables t, sys.sysconstraints c, sys.sysschemas s " +
            "where  s.schemaname = ? " +
            "and    t.tablename = ? " +
            "and    s.schemaid = t.schemaid " +
            "and    t.tableid = c.tableid";

    public static final String FK_INFO =
            "select * from sys.SYSFOREIGNKEYS " +
            "where CONSTRAINTID = ?";
}
