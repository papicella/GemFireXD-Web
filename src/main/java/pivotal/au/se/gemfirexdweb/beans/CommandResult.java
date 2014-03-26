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

public class CommandResult 
{
    private String command;
    private String message;
    private int rows;
    private String elapsedTime;
    
    public void setCommand(String command)
    {
    	this.command = command;
    }

    public String getCommand()
    {
    	return command;
    }

    public void setMessage(String message)
    {
    	this.message = message;
    }

    public String getMessage()
    {
    	return message;
    }

    
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	
	public String getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	@Override
	public String toString() {
		return "CommandResult [command=" + command + ", message=" + message
				+ ", rows=" + rows + ", elapsedTime=" + elapsedTime + "]";
	}
	  
}
