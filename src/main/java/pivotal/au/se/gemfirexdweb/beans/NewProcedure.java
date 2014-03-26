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

public class NewProcedure
{
    private String procedureName;
    private String schemaName;
    private String sqlAccess;
    private String dynamicResultsets;
    private String externalName;
    private String language;
    private String parameterStyle;

    public NewProcedure()
    {
    }

    public NewProcedure(String procedureName, String schemaName, String sqlAccess, String dynamicResultsets, String externalName, String language, String parameterStyle) {
        this.procedureName = procedureName;
        this.schemaName = schemaName;
        this.sqlAccess = sqlAccess;
        this.dynamicResultsets = dynamicResultsets;
        this.externalName = externalName;
        this.language = language;
        this.parameterStyle = parameterStyle;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getSqlAccess() {
        return sqlAccess;
    }

    public void setSqlAccess(String sqlAccess) {
        this.sqlAccess = sqlAccess;
    }

    public String getDynamicResultsets() {
        return dynamicResultsets;
    }

    public void setDynamicResultsets(String dynamicResultsets) {
        this.dynamicResultsets = dynamicResultsets;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getParameterStyle() {
        return parameterStyle;
    }

    public void setParameterStyle(String parameterStyle) {
        this.parameterStyle = parameterStyle;
    }

    @Override
    public String toString() {
        return "NewProcedure{" +
                "procedureName='" + procedureName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", sqlAccess='" + sqlAccess + '\'' +
                ", dynamicResultsets='" + dynamicResultsets + '\'' +
                ", externalName='" + externalName + '\'' +
                ", language='" + language + '\'' +
                ", parameterStyle='" + parameterStyle + '\'' +
                '}';
    }
}
