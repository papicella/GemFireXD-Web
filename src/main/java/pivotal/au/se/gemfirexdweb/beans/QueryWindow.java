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

public class QueryWindow 
{
	private String query;
	private String queryCount;
	private String elapsedTime;
	private String explainPlan;
	private String showMember;
    private String saveWorksheet;
	 
	public QueryWindow()
	{	
	
	}
	
	public QueryWindow(String query, String queryCount, String elapsedTime, String showMember, String saveWorksheet)
	{
		super();
		this.query = query;
		this.queryCount = queryCount;
		this.elapsedTime = elapsedTime;
		this.showMember = showMember;
        this.saveWorksheet = saveWorksheet;
	}

	
	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public String getQueryCount() {
		return queryCount;
	}


	public void setQueryCount(String queryCount) {
		this.queryCount = queryCount;
	}


	public String getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime; 
	}

	
	public String getExplainPlan() {
		return explainPlan;
	}

	public void setExplainPlan(String explainPlan) {
		this.explainPlan = explainPlan;
	}

	
	public String getShowMember() {
		return showMember;
	}

	public void setShowMember(String showMember) {
		this.showMember = showMember;
	}

    public String getSaveWorksheet() {
        return saveWorksheet;
    }

    public void setSaveWorksheet(String saveWorksheet) {
        this.saveWorksheet = saveWorksheet;
    }

    @Override
    public String toString() {
        return "QueryWindow{" +
                "query='" + query + '\'' +
                ", queryCount='" + queryCount + '\'' +
                ", elapsedTime='" + elapsedTime + '\'' +
                ", explainPlan='" + explainPlan + '\'' +
                ", showMember='" + showMember + '\'' +
                ", saveWorksheet='" + saveWorksheet + '\'' +
                '}';
    }
}
