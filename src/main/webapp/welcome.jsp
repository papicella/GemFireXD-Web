<%--
Copyright (C) MARCH-2014 Pivotal Software, Inc.

All rights reserved. This program and the accompanying materials
are made available under the terms of the under the Apache License,
Version 2.0 (the "License?); you may not use this file except in compliance
with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><fmt:message key="sqlfireweb.appname" /> - Welcome Page</title>
<link rel="stylesheet" type="text/css" href="css/isqlfire.css" />
<link rel="stylesheet" type="text/css" href="css/print.css" media="print" />
</head>
<body>

<h2>GemFireXD*Web - Welcome Page</h2>

<a href="GemFireXD-Web/home" target="_top" title="Home">
  <img class="icon" src="./themes/original/img/b_home.png" width="16" height="16" alt="Home" />
  Home Page
</a>&nbsp; | &nbsp;
<a href="GemFireXD-Web/preferences" title="Preferences">
  <img class="icon" src="./themes/original/img/b_props.png" width="16" height="16" alt="GemFireXD*Web Preferences" />
  Preferences
</a>&nbsp; | &nbsp;
<a href="http://gemfirexd-05.run.pivotal.io/index.jsp" title="GemFireXD Documentation" target="_top">
    <img class="icon" src="../themes/original/img/b_docs.png" width="16" height="16" alt="GemFireXD documentation" />
    GemFireXD Documentation
</a>&nbsp; | &nbsp;
<a href="GemFireXD-Web/viewconmap" title="View Connection Map">
  <img class="icon" src="./themes/original/img/Connection.gif" width="16" height="16" alt="View Connections Map" />
  Connection Map
</a>&nbsp; | &nbsp;
<a href="GemFireXD-Web/jmxmbeans" title="View JMX MBeans">
    <img class="icon" src="./themes/original/img/b_tblops.png" width="16" height="16" alt="View JMX Mbeans" />
    JMX MBeans
</a>

<p />

<div class="success">
Connected to GemFireXD using JDBC URL <b>${sessionScope.url}</b>
</div>

<br />
<table>
    <tr>
        <td align="center"><img class="icon" src="./themes/original/img/pulse-monitoring-gemfirexd.png" /></td>
        <td align="justify">
            Pivotal GemFire XD is a memory-optimized, distributed data store that is designed for applications that have demanding scalability and availability requirements.
            With GemFire XD you can manage data entirely using in-memory tables, or you can persist very large tables to local disk store files or to a Hadoop Distributed File System (HDFS) for big data deployments.
        </td>
    </tr>
</table>

<br />

<fieldset>
    <legend>Getting Started</legend>
    <table class="formlayout">
        <tr>
            <td>
                <a href="GemFireXD-Web/createtable">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_newtbl.png" alt="Add Table" title="Add Table" />
                    Create Table
                </a> &nbsp; | &nbsp;
                <a href="GemFireXD-Web/createsynonym">
                    <img class="icon" width="16" height="16" src="./themes/original/img/synonym.png" alt="Add Synonym" title="Add Synonym" />
                    Create Synonym
                </a>
                <p />
                <a href="GemFireXD-Web/creatediskstore">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_disks.png" alt="Add Disk Store" title="Add Disk Store" />
                    Create Disk Store
                </a> &nbsp; | &nbsp;
                <a href="GemFireXD-Web/createhdfsstore">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_disks.png" alt="Add HDFS Store" title="Add HDFS Store" />
                    Create HDFS Store
                </a>
                <p />
                <a href="GemFireXD-Web/createasync">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_async.png" alt="Add Async Event Listener" title="Add Async Event Listener" />
                    Create Async Event Listener
                </a> &nbsp; | &nbsp;
                <a href="GemFireXD-Web/creategatewaysender">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_sdb.png" alt="Add Gateway Sender" title="Add Gateway Sender" />
                    Create Gateway Sender
                </a> &nbsp; | &nbsp;
                <a href="GemFireXD-Web/creategatewayreceiver">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_sdb.png" alt="Add Gateway Receiver" title="Add Gateway Receiver" />
                    Create Gateway Receiver
                </a>
                <p />
                <a href="GemFireXD-Web/createtype">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_types.png" alt="Add UDT (Type)" title="Add UDT (Type) " />
                    Create Type
                </a> &nbsp; | &nbsp;
                <a href="GemFireXD-Web/createtrigger">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_trigger_add.png" alt="Add Trigger" title="Add Trigger " />
                    Create Trigger
                </a>
                <p />
                <a href="GemFireXD-Web/createprocedure">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_routine_add.png" alt="Add Procedure" title="Add Procedure " />
                    Create Procedure
                </a> &nbsp; | &nbsp;
                <a href="GemFireXD-Web/createfunction">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_routine_add.png" alt="Add Function" title="Add Function " />
                    Create Function
                </a>
                <p />
                <a href="GemFireXD-Web/createschema">
                    <img class="icon" width="16" height="16" src="./themes/original/img/b_usrlist.png" alt="Add Schema" title="Add Schema" />
                    Create Schema
                </a>
            </td>
        </tr>
    </table>
</fieldset>
<p />

<jsp:include page="footer.html" flush="true" />

</body>
</html>