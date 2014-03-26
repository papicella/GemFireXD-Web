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
package pivotal.au.se.gemfirexdweb.dao.views;

public class View 
{
	public String schemaName;
	public String viewName;
	public String dataPolicy;
	public String definition;
	
	public View()
	{	
	}
	
	public View(String schemaName, String viewName, String dataPolicy, String definition) 
	{
		super();
		this.schemaName = schemaName;
		this.viewName = viewName;
		this.dataPolicy = dataPolicy;
		this.definition = definition;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getDataPolicy() {
		return dataPolicy;
	}

	public void setDataPolicy(String dataPolicy) {
		this.dataPolicy = dataPolicy;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	
	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	@Override
	public String toString() {
		return "View [schemaName=" + schemaName + ", viewName=" + viewName
				+ ", dataPolicy=" + dataPolicy + ", definition=" + definition
				+ "]";
	}
	
	
	
}
