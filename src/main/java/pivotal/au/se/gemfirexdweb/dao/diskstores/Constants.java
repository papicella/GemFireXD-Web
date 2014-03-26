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
package pivotal.au.se.gemfirexdweb.dao.diskstores;

public interface Constants {

	public static final String USER_DISKSTORES = 
			"select name, dir_path_size \"Directory\" " +
			"from sys.sysdiskstores " +
			"where name like ? " +
			"order by 1";

	public static final String USER_DISKSTORES_FOR_CREATE = 
			"select name, dir_path_size \"Directory\" " +
			"from sys.sysdiskstores " +
			"where name not in ('SQLF-DD-DISKSTORE', 'SQLF-DEFAULT-DISKSTORE') " +
			"order by 1";
	
	public static final String DROP_DISKSTORE =
			"drop diskstore %s";
	
	public static final String VIEW_ALL_DISKSTORE_INFO = 
			"select * " +
			"from   sys.sysdiskstores " +
			"where  name = '%s'";

    public static final String GENERATE_DDL =
            "select * " +
            "from   sys.sysdiskstores " +
            "where  name = ?";

}
