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
package pivotal.au.se.gemfirexdweb.dao.indexes;

public interface Constants 
{

	public static final String USER_INDEXES = 
			"select distinct indexname, \"UNIQUE\", tablename, indextype, COLUMNS_AND_ORDER, schemaname, CASESENSITIVE " +
			"from   sys.indexes " +
			"where  schemaname = ? " +
			"and indexname like ? " +
            "order by 1, 2";

	
	public static String DROP_INDEX = "drop index %s.\"%s\"";
	
	public static String VIEW_TABLE_COLUMNS =
			"select c.columnname " + 
			"FROM SYS.SYSCOLUMNS c, sys.systables t " + 
			"where c.referenceid = t.tableid " +
			"and t.tableschemaname = ? " +
			"and t.tablename = ? " + 
			"order by c.columnnumber";
}
