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

public class NewSchema
{
    private String schemaName;
    private String authorizationSchema;
    private String serverGroups;

    public NewSchema()
    {
    }

    public NewSchema(String schemaName, String authorizationSchema, String serverGroups) {
        this.schemaName = schemaName;
        this.authorizationSchema = authorizationSchema;
        this.serverGroups = serverGroups;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getAuthorizationSchema() {
        return authorizationSchema;
    }

    public void setAuthorizationSchema(String authorizationSchema) {
        this.authorizationSchema = authorizationSchema;
    }

    public String getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(String serverGroups) {
        this.serverGroups = serverGroups;
    }

    @Override
    public String toString() {
        return "NewSchema{" +
                "schemaName='" + schemaName + '\'' +
                ", authorizationSchema='" + authorizationSchema + '\'' +
                ", serverGroups='" + serverGroups + '\'' +
                '}';
    }
}
