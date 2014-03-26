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
package pivotal.au.se.gemfirexdweb.dao.members;

public class Member 
{
	private String id;
	private String status;
	private String hostdata;
	private String iselder;
	private String host;
	private String pid;
	private String port;
	private String locator;
	private String serverGroups;
	
	public Member(String id, String status, String hostdata, String iselder,
			String host, String pid, String port, String locator,
			String serverGroups) {
		super();
		this.id = id;
		this.status = status;
		this.hostdata = hostdata;
		this.iselder = iselder;
		this.host = host;
		this.pid = pid;
		this.port = port;
		this.locator = locator;
		this.serverGroups = serverGroups;
	}
	
	public Member()
	{
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHostdata() {
		return hostdata;
	}

	public void setHostdata(String hostdata) {
		this.hostdata = hostdata;
	}

	public String getIselder() {
		return iselder;
	}

	public void setIselder(String iselder) {
		this.iselder = iselder;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public String getServerGroups() {
		return serverGroups;
	}

	public void setServerGroups(String serverGroups) {
		this.serverGroups = serverGroups;
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", status=" + status + ", hostdata="
				+ hostdata + ", iselder=" + iselder + ", host=" + host
				+ ", pid=" + pid + ", port=" + port + ", locator=" + locator
				+ ", serverGroups=" + serverGroups + "]";
	}
	
}
