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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JavaCodeReader
{
    protected static Logger logger = Logger.getLogger("controller");
    private static JavaCodeReader instance = null;
    private ApplicationContext appCtx = null;

    static
    {
      instance = new JavaCodeReader();
    }

    private JavaCodeReader()
    {
        appCtx = new ClassPathXmlApplicationContext("javacode-beans.xml");
        logger.info("query-beans.xml has been read");
    }

    public static JavaCodeReader getInstance()
    {
        return instance;
    }

    public JavaCode getJavaCodeBean (String id)
    {
        JavaCode jcBean = null;

        jcBean = (JavaCode) appCtx.getBean(id);
        return jcBean;
    }
}
