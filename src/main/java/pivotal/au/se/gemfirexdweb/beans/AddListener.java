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

public class AddListener
{
    private String id;
    private String schemaName;
    private String tableName;
    private String functionName;
    private String initString;
    private String serverGroups;

    public AddListener()
    {
        // empty constructor
    }

    public AddListener(String id, String schemaName, String tableName, String functionName, String initString, String serverGroups) {
        this.id = id;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.functionName = functionName;
        this.initString = initString;
        this.serverGroups = serverGroups;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getInitString() {
        return initString;
    }

    public void setInitString(String initString) {
        this.initString = initString;
    }

    public String getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(String serverGroups) {
        this.serverGroups = serverGroups;
    }

    @Override
    public String toString() {
        return "AddListener{" +
                "id='" + id + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", functionName='" + functionName + '\'' +
                ", initString='" + initString + '\'' +
                ", serverGroups='" + serverGroups + '\'' +
                '}';
    }
}
