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
package pivotal.au.se.gemfirexdweb.dao.triggers;

public class Constants 
{
	public static final String USER_TRIGGERS = 
		"select t.triggerid, s.schemaname, t.triggername, t.creationtimestamp, t.event, " + 
		       "t.firingtime, t.type, t.state, t.triggerdefinition " + 
	    "from sys.systriggers t, sys.sysschemas s " +
	    "where s.schemaname = ? " +
	    "and t.schemaid = s.schemaid " + 
	    "and   t.triggername like ? " +
        "order by 2, 3";
	
	public static String DROP_TRIGGER = "drop trigger %s.\"%s\"";
	
	public static final String VIEW_ALL_USER_TRIGGER_COLUMNS = 
		"select * " +
		"from   sys.systriggers " +
		"where  triggerid = '%s'";

    public static String VIEW_TRIGGER_TABLE =
            "select a.triggerid, a.triggername, t.tablename, s.schemaname, a.event, a.type, a.firingtime, a.state " +
            "from   sys.systriggers a, sys.systables t, sys.sysschemas s " +
            "where  a.tableid = t.tableid " +
            "and    t.schemaid = s.schemaid " +
            "and    a.triggerid = ? ";
	
}
