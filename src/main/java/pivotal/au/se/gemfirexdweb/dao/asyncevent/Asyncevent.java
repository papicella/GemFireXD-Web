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
package pivotal.au.se.gemfirexdweb.dao.asyncevent;

public class Asyncevent 
{
	public String name;
	public String listenerClass;
	public String serverGroup;
	private String isStarted;
	
	public Asyncevent()
	{	  
	}

	public Asyncevent(String name, String listenerClass, String serverGroup, String isStarted) 
	{
		super();
		this.name = name;
		this.listenerClass = listenerClass;
		this.serverGroup = serverGroup;
		this.isStarted = isStarted;
	}


	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getListenerClass() {
		return listenerClass;
	}
	
	public void setListenerClass(String listenerClass) {
		this.listenerClass = listenerClass;
	}
	
	public String getServerGroup() {
		return serverGroup;
	}
	
	public void setServerGroup(String serverGroup) {
		this.serverGroup = serverGroup;
	}

	
	public String getIsStarted() {
		return isStarted;
	}


	public void setIsStarted(String isStarted) {
		this.isStarted = isStarted;
	}


	@Override
	public String toString() {
		return "Asyncevent [name=" + name + ", listenerClass=" + listenerClass
				+ ", serverGroup=" + serverGroup + ", isStarted=" + isStarted
				+ "]";
	}
  
	
  
}
