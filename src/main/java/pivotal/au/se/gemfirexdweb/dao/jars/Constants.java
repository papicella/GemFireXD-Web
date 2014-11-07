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
package pivotal.au.se.gemfirexdweb.dao.jars;

public interface Constants
{
    public static final String ALL_JARS =
            "select id, \"SCHEMA\", alias " +
            "from sys.jars " +
            "where alias like ? " +
            "order by 2, 3";

    public static final String REMOVE_JAR = "CALL SQLJ.REMOVE_JAR('%s.%s', 0)";
}
