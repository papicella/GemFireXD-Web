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
package pivotal.au.se.gemfirexdweb.dao.stored;

public class ProcedureParameter 
{
	private int parameterId;
	private String columnName;
	private String typeName;
	
	public ProcedureParameter(int parameterId, String columnName, String typeName) 
	{
		super();
		this.parameterId = parameterId;
		this.columnName = columnName;
		this.typeName = typeName;
	}
	
	public ProcedureParameter()
	{
	}

	public int getParameterId() {
		return parameterId;
	}

	public void setParameterId(int parameterId) {
		this.parameterId = parameterId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "ProcedureParameter [parameterId=" + parameterId
				+ ", columnName=" + columnName + ", typeName=" + typeName + "]";
	}
	
}
