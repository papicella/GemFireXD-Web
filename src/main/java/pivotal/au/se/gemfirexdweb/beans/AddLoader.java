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
package pivotal.au.se.gemfirexdweb.beans;

public class AddLoader
{

    private String schemaName;
    private String tableName;
    private String functionName;
    private String initInfoString;

    public AddLoader()
    {
    }

    public AddLoader(String schemaName, String tableName, String functionName, String initInfoString) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.functionName = functionName;
        this.initInfoString = initInfoString;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getInitInfoString() {
        return initInfoString;
    }

    public void setInitInfoString(String initInfoString) {
        this.initInfoString = initInfoString;
    }

    @Override
    public String toString() {
        return "AddLoader{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", functionName='" + functionName + '\'' +
                ", initInfoString='" + initInfoString + '\'' +
                '}';
    }
}
