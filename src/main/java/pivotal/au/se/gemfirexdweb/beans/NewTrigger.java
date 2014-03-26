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

public class NewTrigger
{
    private String schemaName;
    private String triggerName;
    private String beforeOrAfter;
    private String type;
    private String columnList;
    private String tableName;
    private String referencingOldClause;
    private String referencingNewClause;
    private String forEachRow;
    private String triggerDefinition;

    public NewTrigger()
    {
    }

    public NewTrigger(String schemaName, String triggerName, String beforeOrAfter, String type, String columnList, String tableName, String referencingOldClause, String referencingNewClause, String forEachRow, String triggerDefinition) {
        this.schemaName = schemaName;
        this.triggerName = triggerName;
        this.beforeOrAfter = beforeOrAfter;
        this.type = type;
        this.columnList = columnList;
        this.tableName = tableName;
        this.referencingOldClause = referencingOldClause;
        this.referencingNewClause = referencingNewClause;
        this.forEachRow = forEachRow;
        this.triggerDefinition = triggerDefinition;
    }

    public String getReferencingOldClause() {
        return referencingOldClause;
    }

    public void setReferencingOldClause(String referencingOldClause) {
        this.referencingOldClause = referencingOldClause;
    }

    public String getReferencingNewClause() {
        return referencingNewClause;
    }

    public void setReferencingNewClause(String referencingNewClause) {
        this.referencingNewClause = referencingNewClause;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getBeforeOrAfter() {
        return beforeOrAfter;
    }

    public void setBeforeOrAfter(String beforeOrAfter) {
        this.beforeOrAfter = beforeOrAfter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumnList() {
        return columnList;
    }

    public void setColumnList(String columnList) {
        this.columnList = columnList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getForEachRow() {
        return forEachRow;
    }

    public void setForEachRow(String forEachRow) {
        this.forEachRow = forEachRow;
    }

    public String getTriggerDefinition() {
        return triggerDefinition;
    }

    public void setTriggerDefinition(String triggerDefinition) {
        this.triggerDefinition = triggerDefinition;
    }

    @Override
    public String toString() {
        return "NewTrigger{" +
                "schemaName='" + schemaName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", beforeOrAfter='" + beforeOrAfter + '\'' +
                ", type='" + type + '\'' +
                ", columnList='" + columnList + '\'' +
                ", tableName='" + tableName + '\'' +
                ", referencingOldClause='" + referencingOldClause + '\'' +
                ", referencingNewClause='" + referencingNewClause + '\'' +
                ", forEahRow='" + forEachRow + '\'' +
                ", triggerDefinition='" + triggerDefinition + '\'' +
                '}';
    }
}
