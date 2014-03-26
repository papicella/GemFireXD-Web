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
package pivotal.au.se.gemfirexdweb.main;

public class UserPref 
{
	private int recordsToDisplay;
	private int maxRecordsinSQLQueryWindow;
	private String autoCommit;
	private int historySize;
	
	public UserPref()
	{
	   recordsToDisplay = 20;
	   maxRecordsinSQLQueryWindow = 5000;
	   autoCommit = "N";
	   historySize = 50;
	}

    public UserPref(int recordsToDisplay, int maxRecordsinSQLQueryWindow, String autoCommit, int historySize) {
        this.recordsToDisplay = recordsToDisplay;
        this.maxRecordsinSQLQueryWindow = maxRecordsinSQLQueryWindow;
        this.autoCommit = autoCommit;
        this.historySize = historySize;
    }

    public int getRecordsToDisplay() {
		return recordsToDisplay;
	}

	public void setRecordsToDisplay(int recordsToDisplay) {
		this.recordsToDisplay = recordsToDisplay;
	}

	public int getMaxRecordsinSQLQueryWindow() {
		return maxRecordsinSQLQueryWindow;
	}

	public void setMaxRecordsinSQLQueryWindow(int maxRecordsinSQLQueryWindow) {
		this.maxRecordsinSQLQueryWindow = maxRecordsinSQLQueryWindow;
	}

	public String getAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(String autoCommit) {
		this.autoCommit = autoCommit;
	}

	
	public int getHistorySize() {
		return historySize;
	}

	public void setHistorySize(int historySize) {
		this.historySize = historySize;
	}

	@Override
	public String toString() {
		return "UserPref [recordsToDisplay=" + recordsToDisplay
				+ ", maxRecordsinSQLQueryWindow=" + maxRecordsinSQLQueryWindow
				+ ", autoCommit=" + autoCommit + ", historySize=" + historySize
				+ "]";
	}
	
	
	
}
