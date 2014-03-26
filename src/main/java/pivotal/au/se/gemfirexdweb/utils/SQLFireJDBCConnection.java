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
package pivotal.au.se.gemfirexdweb.utils;

import java.sql.Connection;

public class SQLFireJDBCConnection 
{
	private Connection conn;
	private String url;
	private String connectedAt;
	private String schema;

	public SQLFireJDBCConnection(Connection conn, String url,
			String connectedAt, String schema) {
		super();
		this.conn = conn;
		this.url = url;
		this.connectedAt = connectedAt;
		this.schema = schema;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConnectedAt() {
		return connectedAt;
	}

	public void setConnectedAt(String connectedAt) {
		this.connectedAt = connectedAt;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
}
