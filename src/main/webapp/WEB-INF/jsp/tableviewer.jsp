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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
    <link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
    <script type="text/javascript">
        // <![CDATA[

        // js form validation stuff
        var confirmMsg  = 'Do you really want to ';
        // ]]>
    </script>
    <script src="../js/functions.js" type="text/javascript"></script>
    <title>
        <fmt:message key="sqlfireweb.appname" /> Table Viewer
    </title>
</head>
<body>

<h2> <fmt:message key="sqlfireweb.appname" /> Table Viewer </h2>

<jsp:include page="toolbar.jsp" flush="true" />

<h3>${chosenSchema}.${tablename} Table </h3>

<a href="#1">All Info</a> &nbsp;|&nbsp;
<a href="#2">Members</a> &nbsp;|&nbsp;
<a href="#3">Structure</a> &nbsp;|&nbsp;
<a href="#4">Async</a> &nbsp;|&nbsp;
<a href="#4.1">Gateways</a> &nbsp;|&nbsp;
<a href="#4.2">Constraints</a> &nbsp;|&nbsp;
<a href="#4.3">Triggers</a> &nbsp;|&nbsp;
<a href="#4.4">Privileges</a> &nbsp;|&nbsp;
<a href="#5">Sample Data</a> &nbsp;|&nbsp;
<a href="#6">Java</a>

<p />
<a href="createindex?tabName=${tablename}&schemaName=${chosenSchema}&viewType=${viewType}">
    <img class="icon" width="16" height="16" src="../themes/original/img/b_index.png" alt="Create Index" title="Create Index" />
    Create Index
</a>&nbsp;|&nbsp;
<a href="addlistener?tableName=${tablename}&schemaName=${chosenSchema}">
    <img class="icon" width="16" height="16" src="../themes/original/img/s_vars.png" alt="Add Listener" title="Add Listener" />
    Add Listener
</a>&nbsp;|&nbsp;
<a href="createtrigger?settablename=${chosenSchema}.${tablename}">
    <img class="icon" src="../themes/original/img/b_trigger_add.png" width="16" height="16" alt="Add Table Trigger" />
    Create Trigger
</a>&nbsp;|&nbsp;
<a href="createsynonym?objectName=${chosenSchema}.${tablename}">
    <img class="icon" width="16" height="16" src="../themes/original/img/synonym.png" alt="Add Synonym" title="Add Synonym" />
    Create Synonym
</a>
&nbsp;|&nbsp;
<a href="addwriter?tableName=${tablename}&schemaName=${chosenSchema}">
    <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Attach Writer" title="Attach Writer" />
    Attach Writer
</a>
&nbsp;|&nbsp;
<a href="addloader?tableName=${tablename}&schemaName=${chosenSchema}">
    <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Attach Loader" title="Attach Loader" />
    Attach Loader
</a>
<p />

<c:if test="${!empty result}">
    <fieldset>
        <legend>Result</legend>
        <table class="formlayout">
            <tr>
                <td align="right">Command:</td>
                <td>${result.command} </td>
            </tr>
            <tr>
                <td align="right">Message:</td>
                <td>
                    <font color="${result.message == 'SUCCESS' ? 'green' : 'red'}">
                            ${result.message}
                    </font>
                </td>
            </tr>
        </table>
    </fieldset>
    <br />
</c:if>

<a name="1"></a>
<c:if test="${!empty allTableInfoResult}">
    <h4><font color="#00008b">All Table Information</font></h4>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th colspan="2">TABLE INFORMATION</th>
        </tr>
        <tr>
            <th>Column Name</th>
            <th>Value</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${allTableInfoResult.rows}">
            <c:forEach var="columnName" items="${allTableInfoResult.columnNames}">
                <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                    <td align="right">${columnName}</td>
                    <td align="left"><c:out value="${row[columnName]}"/></td>
                    <c:choose>
                        <c:when test="${columnName == 'ASYNCLISTENERS'}">
                            <c:set var="curasynclisteners" value="${row[columnName]}" />
                        </c:when>
                        <c:when test="${columnName == 'GATEWAYSENDERS'}">
                            <c:set var="curgatewaysenders" value="${row[columnName]}" />
                        </c:when>
                    </c:choose>
                </tr>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>
    <br />
</c:if>

<a name="2"></a>
<c:if test="${!empty dataLocationResults}">
    <h4><font color="#00008b">Table Data Locations Per Member</font></h4>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th colspan="2">DATA LOCATIONS</th>
        </tr>
        <tr>
            <c:forEach var="columnName" items="${dataLocationResults.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${dataLocationResults.rows}">
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                <c:forEach var="columnName" items="${dataLocationResults.columnNames}">
                    <c:choose>
                        <c:when test="${columnName == 'MEMBER'}">
                          <td align="center">
                              <a href="members?memberId=${row[columnName]}&memberAction=ALLMEMBEREVENTINFO">
                                  <c:out value="${row[columnName]}"/>
                              </a>
                          </td>
                        </c:when>
                        <c:otherwise>
                            <td align="center"><c:out value="${row[columnName]}"/></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty dataLocationResults.rows}">
    <i>No rows exist</i>
    <br />
</c:if>

<a name="3"></a>
<c:if test="${!empty tableStructure}">
    <h4><font color="#00008b">Table Structure</font></h4>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th colspan="2">STRUCTURE</th>
        </tr>
        <tr>
            <c:forEach var="columnName" items="${tableStructure.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${tableStructure.rows}">
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                <c:forEach var="columnName" items="${tableStructure.columnNames}">
                    <td align="center"><c:out value="${row[columnName]}"/></td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br />
</c:if>

<a name="4"></a>
<h4><font color="#00008b">Async Event Listeners</font></h4>
<c:if test="${!empty asyncEventListeners}">
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th colspan="${fn:length(asyncEventListeners.columnNames)}">ASYNC EVENT LISTENERS</th>
        </tr>
        <tr>
            <c:forEach var="columnName" items="${asyncEventListeners.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${asyncEventListeners.rows}">
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                <c:forEach var="columnName" items="${asyncEventListeners.columnNames}">
                    <c:choose>
                        <c:when test="${columnName == 'DSID'}">
                            <td align="center">
                                <a href="members?memberId=${row[columnName]}&memberAction=ALLMEMBEREVENTINFO">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </td>
                        </c:when>
                        <c:when test="${columnName == 'TABLENAME'}">
                            <td align="center">
                                <a href="tableviewer?tabName=${row[columnName]}&selectedSchema=${row['SCHEMANAME']}">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </td>
                        </c:when>
                        <c:when test="${columnName == 'STATUS'}">
                            <td align="center">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'RUNNING'}">
                                        <font color="green">
                                            <c:out value="${row[columnName]}"/>
                                        </font>
                                    </c:when>
                                    <c:otherwise>
                                        <font color="red">
                                            <c:out value="${row[columnName]}"/>
                                        </font>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="center"><c:out value="${row[columnName]}"/></td>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty asyncEventListeners.rows}">
    <i>No rows exist</i>
    <br />
</c:if>

<c:if test="${!empty asyncEventListenersForAdd}">
  <br />
  <form action="tableviewer" method="GET">
    <input type="hidden" name="selectedSchema" value="${chosenSchema}" />
    <input type="hidden" name="tabName" value="${tablename}" />
    <input type="hidden" name="curasynclisteners" value="${curasynclisteners}" />
    <input type="hidden" name="addasync" value="Y" />
    <select name="asynceventid">
        <c:forEach var="entry" varStatus="loop" items="${asyncEventListenersForAdd}">
            <option value="${entry['name']}">${entry['name']} (${entry['listenerClass']}) </option>
        </c:forEach>
    </select>
    &nbsp;
    <input type="image" src="../themes/original/img/add16.gif" alt="Add Async Event Listener" />
    &nbsp;
    <a href="tableviewer?selectedSchema=${chosenSchema}&tabName=${tablename}&clearasync=Y">
      <img class="icon" src="../themes/original/img/b_drop.png" width="16" height="16" alt="Clear Async Event Listeners from Table" />
      <i>Clear All</i>
    </a>
  </form>
  <br />
</c:if>

<br />

<a name="4.1"></a>
<h4><font color="#00008b">Table Gateway Senders</font></h4>

<c:if test="${!empty tableGatewaySenders}">
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th colspan="${fn:length(tableGatewaySenders.columnNames)}">TABLE GATEWAY SENDERS</th>
        </tr>
        <tr>
            <c:forEach var="columnName" items="${tableGatewaySenders.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${tableGatewaySenders.rows}">
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                <c:forEach var="columnName" items="${tableGatewaySenders.columnNames}">
                    <c:choose>
                        <c:when test="${columnName == 'ID'}">
                            <td align="center">
                                <a href="members?memberId=${row[columnName]}&memberAction=ALLMEMBEREVENTINFO">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </td>
                        </c:when>
                        <c:when test="${columnName == 'TABLENAME'}">
                            <td align="center">
                                <a href="tableviewer?tabName=${row[columnName]}&selectedSchema=${row['SCHEMANAME']}">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </td>
                        </c:when>
                        <c:when test="${columnName == 'SENDER_ID'}">
                            <td align="center">
                                <a href="gatewaysenders?senderId=${row[columnName]}&gsAction=ALLGATEWAYSENDERINFO">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </td>
                        </c:when>
                        <c:when test="${columnName == 'STATUS'}">
                            <td align="center">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'RUNNING'}">
                                        <font color="green">
                                            <c:out value="${row[columnName]}"/>
                                        </font>
                                    </c:when>
                                    <c:otherwise>
                                        <font color="red">
                                            <c:out value="${row[columnName]}"/>
                                        </font>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="center"><c:out value="${row[columnName]}"/></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty tableGatewaySenders.rows}">
    <i>No rows exist</i>
    <br />
</c:if>

<c:if test="${!empty gatewaySendersForAdd}">
    <br />
    <form action="tableviewer" method="GET">
        <input type="hidden" name="selectedSchema" value="${chosenSchema}" />
        <input type="hidden" name="tabName" value="${tablename}" />
        <input type="hidden" name="curgatewaysenders" value="${curgatewaysenders}" />
        <input type="hidden" name="addgateway" value="Y" />
        <select name="gatewaysender">
            <c:forEach var="entry" varStatus="loop" items="${gatewaySendersForAdd}">
                <option value="${entry['senderId']}">${entry['senderId']} </option>
            </c:forEach>
        </select>
        &nbsp;
        <input type="image" src="../themes/original/img/add16.gif" alt="Add Gateway Sender" />
        &nbsp;
        <a href="tableviewer?selectedSchema=${chosenSchema}&tabName=${tablename}&cleargatewaysender=Y">
            <img class="icon" src="../themes/original/img/b_drop.png" width="16" height="16" alt="Clear Gateway Senders from Table" />
            <i>Clear All</i>
        </a>
    </form>
    <br />
</c:if>

<a name="4.2"></a>
<h4><font color="#00008b">Table Constraints</font></h4>
<table id="table_results" class="data">
    <thead>
    <tr>
        <th colspan="5">CONSTRAINTS</th>
    </tr>
    <tr>
        <th>Schema</th>
        <th>Table Name</th>
        <th>Constraint Name</th>
        <th>Type</th>
        <th>State</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="entry" varStatus="loop" items="${cons}">
        <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
            <td align="center">${entry.schemaName}</td>
            <td align="center">${entry.tableName}</td>
            <td align="center">${entry.constraintName}</td>
            <td align="center">
                <c:choose>
                    <c:when test="${entry.type == 'P'}">
                        Primary
                    </c:when>
                    <c:when test="${entry.type == 'U'}">
                        Unique
                    </c:when>
                    <c:when test="${entry.type == 'C'}">
                        Check
                    </c:when>
                    <c:when test="${entry.type == 'F'}">
                        Foreign Key
                    </c:when>
                    <c:otherwise>
                        ${entry.type}
                    </c:otherwise>
                </c:choose>
            </td>
            <td align="center">
                <c:choose>
                    <c:when test="${entry.state == 'E'}">
                        <font color="green">
                            Enabled
                        </font>
                    </c:when>
                    <c:when test="${entry.state == 'D'}">
                        <font color="red">
                            Disabled
                        </font>
                    </c:when>
                    <c:otherwise>
                        ${entry.state}
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:if test="${empty cons}">
    <i>No rows exist</i>
    <br />
</c:if>

<a name="4.3"></a>
<h4><font color="#00008b">Table Triggers</font></h4>
<c:if test="${!empty tableTriggersResult}">
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th colspan="${fn:length(tableTriggersResult.columnNames)}">TABLE TRIGGERS</th>
        </tr>
        <tr>

            <c:forEach var="columnName" items="${tableTriggersResult.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${tableTriggersResult.rows}">
            <tr class="odd">
                <c:forEach var="columnName" items="${tableTriggersResult.columnNames}">
                    <td align="center">
                        <c:choose>
                            <c:when test="${columnName == 'TRIGGERNAME'}">
                                <a href="triggers?triggerId=${row['TRIGGERID']}&triggerName=${row[columnName]}&selectedSchema=${chosenSchema}&triggerAction=ALLTRIGGERINFO">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </c:when>
                            <c:when test="${columnName == 'EVENT'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'U'}">
                                        Update
                                    </c:when>
                                    <c:when test="${row[columnName] == 'D'}">
                                        Delete
                                    </c:when>
                                    <c:when test="${row[columnName] == 'I'}">
                                        Insert
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.event}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${columnName == 'FIRINGTIME'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'B'}">
                                        Before
                                    </c:when>
                                    <c:when test="${row[columnName] == 'A'}">
                                        After
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.type}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${columnName == 'TYPE'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'R'}">
                                        Row
                                    </c:when>
                                    <c:when test="${row[columnName] == 'S'}">
                                        Statement
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.type}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${columnName == 'STATE'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'E'}">
                                        <font color="green">
                                            Enabled
                                        </font>
                                    </c:when>
                                    <c:when test="${row[columnName] == 'D'}">
                                        <font color="red">
                                            Disabled
                                        </font>
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.state}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${row[columnName]}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty tableTriggersResult.rows}">
    <i>No rows exist</i>
    <br />
</c:if>

<br />

<a href="createtrigger?settablename=${chosenSchema}.${tablename}">
    <img class="icon" src="../themes/original/img/b_trigger_add.png" width="16" height="16" alt="Add Table Trigger" />
    <i>Add Table Trigger</i>
</a>

<p />
<a name="4.4"></a>
<h4><font color="#00008b">Table Privileges</font></h4>

<table id="table_results" class="data">
    <thead>
    <tr>
        <th colspan="2">Table Privileges</th>
    </tr>
    <tr>
        <c:forEach var="columnName" items="${tablePrivsResult.columnNames}">
            <th><c:out value="${columnName}"/></th>
        </c:forEach>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="row" varStatus="loop" items="${tablePrivsResult.rows}">
        <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
            <c:forEach var="columnName" items="${tablePrivsResult.columnNames}">
                <td align="center"><c:out value="${row[columnName]}"/></td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:if test="${empty tablePrivsResult.rows}">
    <i>No rows exist</i>
    <p />
</c:if>

<form action="tableviewer" method="GET">
    <input type="hidden" name="selectedSchema" value="${chosenSchema}" />
    <input type="hidden" name="tabName" value="${tablename}" />
    <input type="hidden" name="addpriv" value="Y" />
    <select name="privType">
        <option value="GRANT" selected="selected">GRANT</option>
        <option value="REVOKE">REVOKE</option>
    </select>
    &nbsp; Option &nbsp;
    <select name="privOption">
        <option value="SELECT" selected="selected">SELECT</option>
        <option value="UPDATE">UPDATE</option>
        <option value="DELETE">DELETE</option>
        <option value="TRIGGER">TRIGGER</option>
    </select>
    &nbsp; To &nbsp;
    <select name="privTo">
        <option value="PUBLIC">PUBLIC</option>
        <c:forEach var="row" items="${schemas}">
           <option value="${row}">${row}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Perform Privilege Action" />
</form>

<p />
<a name="5"></a>
<h4><font color="#00008b">Sample Table Data</font></h4>
<table id="table_results" class="data">
    <tbody>
    <tr class="odd">
        <td>
            <b>GemFireXD*Web&gt;</b> ${querysql} &nbsp;
            <a href="executequery?query=${querysql}">
                <img class="icon" width="16" height="16" src="../themes/original/img/b_views.png" alt="View Sample Data" title="View Sample Data" />
            </a>
        </td>
    </tr>
    </tbody>
</table>
<br />
<table id="table_results" class="data">
    <thead>
    <tr>
        <c:forEach var="columnName" items="${sampledata.columnNames}">
            <th><c:out value="${columnName}"/></th>
        </c:forEach>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="row" varStatus="loop" items="${sampledata.rows}">
        <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
            <c:forEach var="columnName" items="${sampledata.columnNames}">
                <td><c:out value="${row[columnName]}"/></td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:if test="${empty sampledata.rows}">
    <i>No rows exist</i>
    <br />
</c:if>

<a name="6"></a>
<h4><font color="#00008b">Java Code - Synchronous Listener </font> </h4>

<p>
    A listener enables you to receive after-event notifications of changes to a table (insert, update and delete). Any number of listeners can be defined for the same table. Listener callbacks are called synchronously, so they will cause the DML operation to block if the callback blocks
</p>

<table id="table_results" class="data">
    <tbody>
    <tr class="odd">
        <td><pre>${eventListener}</pre></td>
    </tr>
    </tbody>
</table>
<br />

<h4><font color="#00008b">Java Code - Async Event Listener </font></h4>

<p>
    An AsyncEventListener receives callbacks on the execution of data manipulation language (DML) statements (create, update, and delete operations) in GemFire XD. In a caching deployment, you can use an AsyncEventListener to analyze data in DML statements and take desired actions such as persisting the data in a specific format.
</p>

<table id="table_results" class="data">
    <tbody>
    <tr class="even">
        <td><pre>${asyncListener}</pre></td>
    </tr>
    </tbody>
</table>
<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>
