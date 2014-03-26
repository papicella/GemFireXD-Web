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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class QueryBeanReader 
{
	  protected static Logger logger = Logger.getLogger("controller");
	  private static QueryBeanReader instance = null;
	  private ApplicationContext appCtx = null;
	  
	  static
	  {
	    instance = new QueryBeanReader();  
	  }
	  


	  private QueryBeanReader()
	  {
	      appCtx = new ClassPathXmlApplicationContext("query-beans.xml");
	      logger.info("query-beans.xml has been read");
	  }
	  
	  public static QueryBeanReader getInstance()
	  {
	    return instance;
	  }
 
	  public QueryList getQueryListBean (String name)
	  {
	    QueryList ql = null;
	    
	    ql = (QueryList) appCtx.getBean(name);
	    
	    return ql;
	  }
	  
	  public Query getQueryBean (String id)
	  {
	    Query q = null;
	    
	    q = (Query) appCtx.getBean(id);
	    return q;
	  }
	  
	  public String[] getQueryListBeans ()
	  {
	    String[] queryListBeans = appCtx.getBeanNamesForType(QueryList.class);

	    return queryListBeans;
	  }
}
