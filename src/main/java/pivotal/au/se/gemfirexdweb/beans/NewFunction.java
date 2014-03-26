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

public class NewFunction
{
    private String functionName;
    private String schemaName;
    private String sqlAccess;
    private String externalName;
    private String language;
    private String parameterStyle;
    private String ifNull;
    private String returnType;
    private String returnPrecision;

    public NewFunction()
    {
    }

    public NewFunction(String functionName, String schemaName, String sqlAccess, String externalName, String language, String parameterStyle, String ifNull, String returnType, String returnPrecision) {
        this.functionName = functionName;
        this.schemaName = schemaName;
        this.sqlAccess = sqlAccess;
        this.externalName = externalName;
        this.language = language;
        this.parameterStyle = parameterStyle;
        this.ifNull = ifNull;
        this.returnType = returnType;
        this.returnPrecision = returnPrecision;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSqlAccess() {
        return sqlAccess;
    }

    public void setSqlAccess(String sqlAccess) {
        this.sqlAccess = sqlAccess;
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

    public String getIfNull() {
        return ifNull;
    }

    public void setIfNull(String ifNull) {
        this.ifNull = ifNull;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnPrecision() {
        return returnPrecision;
    }

    public void setReturnPrecision(String returnPrecision) {
        this.returnPrecision = returnPrecision;
    }

    @Override
    public String toString() {
        return "NewFunction{" +
                "functionName='" + functionName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", sqlAccess='" + sqlAccess + '\'' +
                ", externalName='" + externalName + '\'' +
                ", language='" + language + '\'' +
                ", parameterStyle='" + parameterStyle + '\'' +
                ", ifNull='" + ifNull + '\'' +
                ", returnType='" + returnType + '\'' +
                ", returnPrecision='" + returnPrecision + '\'' +
                '}';
    }
}
