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

public class AddWriter
{
    private String schemaName;
    private String tableName;
    private String functionString;
    private String initInfoString;
    private String serverGroups;


    public AddWriter()
    {
    }

    public AddWriter(String schemaName, String tableName, String functionString, String initInfoString, String serverGroups) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.functionString = functionString;
        this.initInfoString = initInfoString;
        this.serverGroups = serverGroups;
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

    public String getFunctionString() {
        return functionString;
    }

    public void setFunctionString(String functionString) {
        this.functionString = functionString;
    }

    public String getInitInfoString() {
        return initInfoString;
    }

    public void setInitInfoString(String initInfoString) {
        this.initInfoString = initInfoString;
    }

    public String getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(String serverGroups) {
        this.serverGroups = serverGroups;
    }

    @Override
    public String toString() {
        return "AddWriter{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", functionString='" + functionString + '\'' +
                ", initInfoString='" + initInfoString + '\'' +
                ", serverGroups='" + serverGroups + '\'' +
                '}';
    }
}
