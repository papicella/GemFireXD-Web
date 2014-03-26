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
package pivotal.au.se.gemfirexdweb.dao.tables;

public class Table 
{
	public String schemaName;
	public String tableName;
	public String dataPolicy;
	public String serverGroups;
	public String asyncListeners;
	public String gatewaySenders;
	
	public Table() {	
	}

    public Table(String schemaName, String tableName, String dataPolicy, String serverGroups, String asyncListeners, String gatewaySenders) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.dataPolicy = dataPolicy;
        this.serverGroups = serverGroups;
        this.asyncListeners = asyncListeners;
        this.gatewaySenders = gatewaySenders;
    }

    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDataPolicy() {
		return dataPolicy;
	}

	public void setDataPolicy(String dataPolicy) {
		this.dataPolicy = dataPolicy;
	}

	public String getServerGroups() {
		return serverGroups;
	}

	public void setServerGroups(String serverGroups) {
		this.serverGroups = serverGroups;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

    public String getAsyncListeners() {
        return asyncListeners;
    }

    public void setAsyncListeners(String asyncListeners) {
        this.asyncListeners = asyncListeners;
    }

    public String getGatewaySenders() {
        return gatewaySenders;
    }

    public void setGatewaySenders(String gatewaySenders) {
        this.gatewaySenders = gatewaySenders;
    }

    @Override
    public String toString() {
        return "Table{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", dataPolicy='" + dataPolicy + '\'' +
                ", serverGroups='" + serverGroups + '\'' +
                ", asyncListeners='" + asyncListeners + '\'' +
                ", gatewaySenders='" + gatewaySenders + '\'' +
                '}';
    }
}
