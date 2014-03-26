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

public interface Constants 
{
	public static final String ALL_MEMBERS =
			"select id, status, hostdata, iselder, host, pid, port, locator, servergroups " +
			"from sys.members";	
	
	public static final String VIEW_ALL_MEMBER_INFO = 
			"select * " +
			"from   sys.MEMBERS " +
			"where  id = '%s'";

    public static final String VIEW_GEMFIREPROPS_FOR_MEMBER =
            "select CAST(gemfireprops AS VARCHAR(32000)) " +
            "from   sys.MEMBERS " +
            "where  id = ?";

    public static final String VIEW_BOOTPROPS_FOR_MEMBER =
            "select CAST(bootprops AS VARCHAR(32000)) " +
            "from   sys.MEMBERS " +
            "where  id = ?";

    public static final String VIEW_SYSTEMPROPS_FOR_MEMBER =
            "select CAST(systemprops AS VARCHAR(32000)) " +
            "from   sys.MEMBERS " +
            "where  id = ?";
}
