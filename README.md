<h1> Pivotal GemFireXD*Web </h1>

![alt tag](https://dl.dropboxusercontent.com/u/15829935/fe-demos/GemFireXDWeb/images/GemFireXD1.png) 

GemFireXD*Web is New Browser-based Database Schema Management Tool Available for Pivotal GemFireXD which allows users to view/alter schema objects as well as GemFireXD schema objects.

GemFireXD*Web is a translation of phpmyadmin to GemFireXD and the GemFireXD*Web UI is base on the look and feel of phpmyadmin.

GemFireXD*Web Supports the following features

<ul>
    <li>Browse / Administer HDFS Stores</li>
    <li>Browse / Administer HDFS Tables</li>
    <li>Browse / Administer Schema Objects</li>
    <li>Auto Login without going through the Login Page</li>
    <li>Disconnect GemFireXD*Web JDBC Connections from GemFireXD</li>
    <li>Schema Creation Dialogs</li>
    <li>New Table Viewer to view everything about a table including sample data</li>
    <li>Grant table / program unit privileges when AUTHENTICATION is enabled</li>
    <li>JMX Mbean Monitoring</li>
    <li>Save Query Results in CSV or JSON format</li>
    <li>View member start properties</li>
    <li>SQL Worksheet to load/execute SQL DML/DDL statements</li>
    <li>View memory Usage for Tables/Indexes</li>
    <li>View data distribution for Tables across members and SQL queries</li>
    <li>Stop/Start Gateway senders / Async Event Listeners</li>
</ul>

<h2>Download</h2>

Download GemFireXD*Web using the link below. 

Current GA release 1.0, supports GemFireXD 1.3 - <a href="https://dl.dropboxusercontent.com/u/15829935/fe-demos/GemFireXDWeb/download/gfxdw.war">gfxdw.war</a>

<h2>Deployment via WAR file</h2>

The following is based on deployment to Pivotal tcServer 

1. Download WAR via link above
2. Create / Start instance

```
> ./tcruntime-instance.sh create gfxdw
> ./tcruntime-ctl.sh gfxdw start
```

3. Copy gfxdw.war to $TCSERVER_HOME/gfxdw/webapps

4. Access as follows

```
http://{server-ip-address}:{server-port}/gfxdw/
```

<h2>Deployment via Template with Pivotal tcServer</h2>

1. Download the template zip from the location below.

<a href="https://dl.dropboxusercontent.com/u/15829935/fe-demos/GemFireXDWeb/download/gemfirexdweb.zip">gemfirexdweb.zip</a>

2. Extract it in your $TCSERVER_HOME/templates directory

3. Create new instance based on this template

```
./tcruntime-instance.sh create --template gemfirexdweb {instance-name}
```

Example:

```
./tcruntime-instance.sh create --template gemfirexdweb gfxdwtemplate
```

4. Start tcServer instance

```
./tcruntime-ctl.sh gfxdwtemplate start
```

5. Browse app it should be started automatically

```
http://{server-ip-address}:{server-port}/gfxdwtemplate/
```

![alt tag](https://dl.dropboxusercontent.com/u/15829935/fe-demos/GemFireXDWeb/images/welcome.png)

<h2>JVM heap Settings</h2>

GemFireXD*Web will run with as low as 512M oif heap memory. It is recommended you run it with at least 1G of memory.
For example to increase the memory settings in Pivotal tcServer perform the steps below.

1. Edit $TCSERVER_HOME/{instance-name}/bin/setenv.sh

2. Increase memory to 2G of heap as shown below and re-start the instance

```
JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home"
AGENT_PATHS=""
JAVA_AGENTS=""
JAVA_LIBRARY_PATH=""
JVM_OPTS="-Xms2g -Xmx2g -Xss256K"
JAVA_OPTS="$JVM_OPTS $AGENT_PATHS $JAVA_AGENTS $JAVA_LIBRARY_PATH"
```

<h2>Enabling JMX monitoring</h2>

1. Start a locator using the following syntax to add jolokia-jvm-1.2.2-agent.jar

```
gfxd locator start -peer-discovery-address=$IP -peer-discovery-port=41111 \
-jmx-manager-port=1103 -jmx-manager-start=true -jmx-manager-http-port=7075 \
-conserve-sockets=false -client-bind-address=$IP \ 
-client-port=1527 -dir=locator -sync=false \
-J-javaagent:$CURRENT_DIR/lib/jolokia-jvm-1.2.2-agent.jar=host=0.0.0.0
```

2. Download jolokia-jvm-1.2.2-agent.jar from the link below.

```
http://www.jolokia.org/download.html
``` 
 
![alt tag](https://dl.dropboxusercontent.com/u/15829935/fe-demos/GemFireXDWeb/images/jmx-url.png)

![alt tag](https://dl.dropboxusercontent.com/u/15829935/fe-demos/GemFireXDWeb/images/jmx.png)

<h2>Saving Query Results</h2>

GemFireXD*Web allows you to save query results in CSV or JSON format. The output for each is as follows. This could be a whole table
or simply a restricted view of the table using a where clause for example.

<u>CSV</u>

```
"EMPNO","ENAME","JOB","MGR","HIREDATE","SAL","COMM","DEPTNO"
"7369","SMITH","CLERK","7902","17-Dec-1980","800","","20"
"7566","JONES","MANAGER","7839","02-Apr-1981","2975","","20"
"7788","SCOTT","ANALYST","7566","09-Dec-1982","3000","","20"
"7876","ADAMS","CLERK","7788","12-Jan-1983","1100","","20"
"7902","FORD","ANALYST","7566","03-Dec-1981","3000","","20"
```

<u>JSON</u>

```
{"fields":[{"name":"DEPTNO","type":"INTEGER"},{"name":"DNAME","type":"VARCHAR"},
{"name":"LOC","type":"VARCHAR"}],"records":[[10,"ACCOUNTING","NEW YORK"],
[20,"RESEARCH","DALLAS"],[30,"SALES","CHICAGO"],[40,"OPERATIONS","BRISBANE"],
[50,"MARKETING","ADELAIDE"],[60,"DEV","PERTH"],[70,"SUPPORT","SYDNEY"]]}
```

![alt tag](https://dl.dropboxusercontent.com/u/15829935/fe-demos/GemFireXDWeb/images/savequery.png)


Created by Pas Apicella - <a href="mailto:papicella@pivotal.io">papicella@pivotal.io</a> for Pivotal GemFireXD Schema Management

