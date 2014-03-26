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
package pivotal.au.se.gemfirexdweb.dao.triggers;

public class Trigger 
{
	private String triggerId;
	private String schemaName;
	private String triggerName;
	private String created;
	private String event;
	private String firingTime;
	private String type;
	private String state;
	private String triggerDefinition;
	
	public Trigger ()
	{
	}

	public Trigger(String triggerId, String schemaName, String triggerName, String created, String event,
			String firingTime, String type, String state,
			String triggerDefinition) 
	{
		super();
		this.triggerId = triggerId;
		this.schemaName = schemaName;
		this.triggerName = triggerName;
		this.created = created;
		this.event = event;
		this.firingTime = firingTime;
		this.type = type;
		this.state = state;
		this.triggerDefinition = triggerDefinition;
	}

	
	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getFiringTime() {
		return firingTime;
	}

	public void setFiringTime(String firingTime) {
		this.firingTime = firingTime;
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

	public String getTriggerDefinition() {
		return triggerDefinition;
	}

	public void setTriggerDefinition(String triggerDefinition) {
		this.triggerDefinition = triggerDefinition;
	}

	
	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	@Override
	public String toString() {
		return "Trigger [triggerId=" + triggerId + ", schemaName=" + schemaName
				+ ", triggerName=" + triggerName + ", created=" + created
				+ ", event=" + event + ", firingTime=" + firingTime + ", type="
				+ type + ", state=" + state + ", triggerDefinition="
				+ triggerDefinition + "]";
	}
	
	
}
