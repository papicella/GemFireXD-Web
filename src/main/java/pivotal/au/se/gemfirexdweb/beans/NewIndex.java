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

public class NewIndex
{
    private String indexName;
    private String schemaName;
    private String tableName;
    private String unique;
    private String caseSensitive;

    public NewIndex()
    {
    }

    public NewIndex(String indexName, String schemaName, String tableName, String unique, String caseSensitive) {
        this.indexName = indexName;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.unique = unique;
        this.caseSensitive = caseSensitive;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
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

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public String getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(String caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public String toString() {
        return "NewIndex{" +
                "indexName='" + indexName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", unique='" + unique + '\'' +
                ", caseSensitive='" + caseSensitive + '\'' +
                '}';
    }
}
