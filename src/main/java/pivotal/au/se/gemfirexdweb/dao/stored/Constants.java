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
package pivotal.au.se.gemfirexdweb.dao.stored;

public interface Constants 
{
	public static final String USER_STORED_CODE = 
			"select s.schemaname, alias, javaclassname " + 
		    "from sys.sysaliases a, sys.sysschemas s " +
		    "where aliastype = ? " + 
		    "and a.schemaid = s.schemaid " + 
		    "and s.schemaname = ? " +
		    "and alias like ? " +
            "order by 1, 2";
	
	public static String DROP_STORED_CODE = "drop %s %s.\"%s\"";

    public static String GRANT_EXECUTE_PRIV = "GRANT EXECUTE ON %s %s.\"%s\" TO %s";

    public static String REVOKE_EXECUTE_PRIV = "REVOKE EXECUTE ON %s %s.\"%s\" FROM %s RESTRICT";

    public static String USER_STORED_CODE_PRIVS =
            "select s.schemaname, a.alias, r.grantee, r.grantor, r.grantoption " +
            "from sys.sysaliases a, sys.sysschemas s, sys.SYSROUTINEPERMS r " +
            "where a.schemaid = s.schemaid " +
            "and   a.aliasid = r.aliasid " +
            "and s.schemaname = ? " +
            "and a.alias = ?";
}
