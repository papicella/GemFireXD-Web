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
package pivotal.au.se.gemfirexdweb.dao.constraints;

public class Constraint 
{
	private String schemaName;
	private String tableName;
	private String constraintName;
	private String type;
	private String state;
    private String constraintId;
	
	public Constraint()
	{
	}

    public Constraint(String schemaName, String tableName, String constraintName, String type, String state, String constraintId) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.constraintName = constraintName;
        this.type = type;
        this.state = state;
        this.constraintId = constraintId;
    }

    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

    public String getConstraintId() {
        return constraintId;
    }

    public void setConstraintId(String constraintId) {
        this.constraintId = constraintId;
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", constraintName='" + constraintName + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", constraintId='" + constraintId + '\'' +
                '}';
    }
}
