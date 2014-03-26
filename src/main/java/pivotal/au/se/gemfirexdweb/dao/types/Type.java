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
package pivotal.au.se.gemfirexdweb.dao.types;

public class Type 
{
	private String schemaName;
	private String typeName;
	private String javaClassName;
	
	public Type()
	{	
	}
	
	public Type(String schemaName, String typeName, String javaClassName) {
		super();
		this.schemaName = schemaName;
		this.typeName = typeName;
		this.javaClassName = javaClassName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getJavaClassName() {
		return javaClassName;
	}

	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

	@Override
	public String toString() {
		return "Type [schemaName=" + schemaName + ", typeName=" + typeName
				+ ", javaClassName=" + javaClassName + "]";
	}
	
}
