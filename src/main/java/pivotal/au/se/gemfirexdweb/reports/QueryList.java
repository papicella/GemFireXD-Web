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
package pivotal.au.se.gemfirexdweb.reports;

import java.util.Map;
import java.util.Set;

public class QueryList
{
  private Map queryList;
  private String description;
  
  public QueryList()
  {  
  }
  
  public void setQueryList(Map queryList)
  {
    this.queryList = queryList;
  }

  public Map getQueryList()
  {
    return queryList;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }

  @Override
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    
    sb.append("QueryList : paramMap keys " + queryList.keySet() + "\n");;
    sb.append("Data \n");
    Set<String> keys = queryList.keySet();
    
    for (String key: keys)
    {
      sb.append("key: " + key + " , value: " + queryList.get(key) + "\n");
    }
    
    return sb.toString();
  }
}
