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
    <c:choose>
        <c:when test="${viewType == 'HDFS'}">
            <fmt:message key="sqlfireweb.appname" /> HDFS Tables
        </c:when>
        <c:when test="${viewType == 'OFFHEAP'}">
            <fmt:message key="sqlfireweb.appname" /> Offheap Tables
        </c:when>
        <c:otherwise>
            <fmt:message key="sqlfireweb.appname" /> Tables
        </c:otherwise>
    </c:choose>
</title>
</head>
<body>

<c:set var="tabord" value="tab" />
<c:set var="tabhdfs" value="tab" />
<c:set var="tabheap" value="tab" />

<c:choose>
    <c:when test="${viewType == 'HDFS'}">
        <c:set var="tabhdfs" value="tabactive" />
    </c:when>
    <c:when test="${viewType == 'OFFHEAP'}">
        <c:set var="tabheap" value="tabactive" />
    </c:when>
    <c:otherwise>
        <c:set var="tabord" value="tabactive" />
    </c:otherwise>
</c:choose>

<h2>
    <c:choose>
     <c:when test="${viewType == 'HDFS'}">
        <fmt:message key="sqlfireweb.appname" /> HDFS Tables
     </c:when>
     <c:when test="${viewType == 'OFFHEAP'}">
        <fmt:message key="sqlfireweb.appname" /> Offheap Tables
     </c:when>
     <c:otherwise>
        <fmt:message key="sqlfireweb.appname" /> Tables
     </c:otherwise>
    </c:choose>
</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div id="topmenucontainer" >
    <ul id="topmenu">
        <li>
            <a class="${tabord}" href="tables">
                <img class="icon"
                     src="../themes/original/img/s_tbl.png"
                     width="16"
                     height="16"
                     alt="Tables" />Tables</a></li>
        <li>
            <a class="${tabhdfs}" href="tables?viewType=HDFS">
                <img class="icon"
                     src="../themes/original/img/s_tbl.png"
                     width="16"
                     height="16"
                     alt="Views" />HDFS Tables</a></li>
        <li>
            <a class="${tabheap}" href="tables?viewType=OFFHEAP">
                <img class="icon"
                     src="../themes/original/img/s_tbl.png"
                     width="16"
                     height="16"
                     alt="Views" />Offheap Tables</a></li>
    </ul>
    <div class="clearfloat"></div>
</div>
<br />

<div class="notice">
Found ${records} table(s). &nbsp; &nbsp;
<a href="createtable">
 <img class="icon" width="16" height="16" src="../themes/original/img/b_newtbl.png" alt="Add Table" title="Add Table" />
 Create Table
</a>
</div>

<c:if test="${!empty dataLocationResults}">
 <h3>Data locations for ${tablename} table </h3>
 <table id="table_results" class="data">
  <thead>
    <tr>
     <c:forEach var="columnName" items="${dataLocationResults.columnNames}">
      <th><c:out value="${columnName}"/></th>
    </c:forEach>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${dataLocationResults.rows}">
      <tr class="odd">
        <c:forEach var="columnName" items="${dataLocationResults.columnNames}">
          <td align="center"><c:out value="${row[columnName]}"/></td>
        </c:forEach>           
       </tr>
    </c:forEach>  
  </tbody>
 </table>
 <br />
</c:if>

<c:if test="${!empty expirationEvictionResult}">
 <h3>Expiration / Eviction Attributes for ${tablename} table </h3>
 <table id="table_results" class="data">
  <thead>
    <tr>
     <c:forEach var="columnName" items="${expirationEvictionResult.columnNames}">
      <th><c:out value="${columnName}"/></th>
    </c:forEach>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${expirationEvictionResult.rows}">
      <tr class="odd">
        <c:forEach var="columnName" items="${expirationEvictionResult.columnNames}">
          <td align="center"><c:out value="${row[columnName]}"/></td>
        </c:forEach>           
       </tr>
    </c:forEach>  
  </tbody>
 </table>
 <br />
</c:if>

<c:if test="${!empty tableStructure}">
 <h3>${tablename} Structure </h3>
 <table id="table_results" class="data">
  <thead>
    <tr>
     <c:forEach var="columnName" items="${tableStructure.columnNames}">
      <th><c:out value="${columnName}"/></th>
    </c:forEach>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${tableStructure.rows}">
      <tr class="odd">
        <c:forEach var="columnName" items="${tableStructure.columnNames}">
          <td align="center"><c:out value="${row[columnName]}"/></td>
        </c:forEach>           
       </tr>
    </c:forEach>  
  </tbody>
 </table>
 <br />
</c:if>

<c:if test="${!empty tableTriggersResult}">
    <h3>${tablename} Triggers </h3>
    <table id="table_results" class="data">
        <thead>
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
    <br />
    <c:if test="${empty tableTriggersResult.rows}">
        <i>No rows exist</i>
        <p />
    </c:if>
</c:if>

<c:if test="${!empty allTableInfoResult}">
 <h3>${tablename} table information</h3>
 <table id="table_results" class="data">
  <thead>
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
         </tr>
        </c:forEach>           
       </tr>
    </c:forEach>  
  </tbody>
 </table>
 <br />
</c:if>

<c:if test="${!empty tableMemoryUsage}">
 <h3>Memory Usage for ${tablename} table </h3>
 <c:if test="${memUsageSum == 'N'}">
     <a href="tables?tabName=${tablename}&tabAction=MEMORYUSAGESUM&selectedSchema=${chosenSchema}&viewType=${viewType}">
          Group Memory Usage by Server
     </a>
     <p />
 </c:if>
 <table id="table_results" class="data">
  <thead>
    <tr>
     <c:forEach var="columnName" items="${tableMemoryUsage.columnNames}">
      <th><c:out value="${columnName}"/></th>
    </c:forEach>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${tableMemoryUsage.rows}">
      <tr class="odd">
        <c:forEach var="columnName" items="${tableMemoryUsage.columnNames}">
            <c:choose>
                <c:when test="${columnName == 'ID'}">
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
 <br />
</c:if>

<c:if test="${!empty parAttrResult}">
 <h3>Partition Attributes for ${tablename} table </h3>
 <table id="table_results" class="data">
   <tbody>
     <tr class="odd">
      <td>${parAttrResult}</td>
     </tr>
   </tbody>
 </table>
 <br />
</c:if>

<c:if test="${!empty loadscriptsql}">
 <h3>Load script for ${tablename} table </h3>
 <table id="table_results" class="data">
   <tbody>
     <tr class="odd">
      <td><pre>${loadscriptsql}</pre></td>
     </tr>
   </tbody>
 </table>
 <br />
</c:if>

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

<c:if test="${!empty arrayresult}">
<fieldset>
 <legend>Multi Submit Results</legend>
 <table class="formlayout">
  <c:forEach var="result" items="${arrayresult}">
    <tr>
     <td align="right">Command:</td>
     <td> ${result.command} </td>
    </tr>
    <tr>
     <td align="right">Message:</td>
     <td> 
      <font color="${result.message == 'SUCCESS' ? 'green' : 'red'}">
        ${result.message}
      </font>
     </td>
    </tr>
  </c:forEach>
 </table>
</fieldset>
<br />
</c:if>

<form action="tables" method="POST">
   <input type="hidden" name="viewType" value="${viewType}" />
   <b>Filter Table Name: </b>
   <input type="TEXT" name="search" value="${search}" />
   <b>Schema : </b>
   <select name="selectedSchema">
	   <c:forEach var="row" items="${schemas}">
	   	<c:choose>
	   		<c:when test="${row == chosenSchema}">
	   		  <option value="${row}" selected="selected">${row}</option>
	   		</c:when>
	   		<c:otherwise>
	   		  <option value="${row}">${row}</option>
	   		</c:otherwise>
	   	</c:choose>
	   </c:forEach>
   </select>
   <input type="image" src="../themes/original/img/Search.png" />
</form>

<!-- Display previous/next set links -->
<c:if test="${estimatedrecords > sessionScope.prefs.recordsToDisplay}"> &nbsp; | &nbsp;
  <c:if test="${startAtIndex != 0}">
    <a href="tables?search=${param.search}&selectedSchema=${chosenSchema}&viewType=${viewType}&startAtIndex=${(startAtIndex - sessionScope.prefs.recordsToDisplay)}&endAtIndex=${startAtIndex}">
      <img src="../themes/original/img/Previous.png" border="0" />
    </a>
    &nbsp;
  </c:if>
  <c:if test="${estimatedrecords != endAtIndex}">
    <a href="tables?search=${param.search}&selectedSchema=${chosenSchema}&viewType=${viewType}&startAtIndex=${endAtIndex}&endAtIndex=${endAtIndex + sessionScope.prefs.recordsToDisplay}">
      <img src="../themes/original/img/Next.png" border="0" />
    </a>
  </c:if>
  &nbsp; <font color="Purple">Current Set [${startAtIndex + 1} - ${endAtIndex}] </font>
</c:if>

<p />

<form method="post" action="tables" name="tablesForm" id="tablesForm">
<input type="hidden" name="selectedSchema" value="${chosenSchema}" />
<input type="hidden" name="viewType" value="${viewType}" />
<table id="table_results" class="data">
 <thead>
   <tr>
    <th></th>
    <th>Schema</th>
    <th>Name</th>
    <th>Data Policy</th>
    <th>Server Groups</th>
    <th>Async</th>
    <th>Senders</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="entry" varStatus="loop" items="${tables}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		  <td align="center">
		      <input type="checkbox" 
		             name="selected_tbl[]"
		             value="${entry['tableName']}"
		             id="checkbox_tbl_${loop.index + 1}" />
		   </td>
		   <td align="center">${entry.schemaName}</td>
	  	   <td align="center">
	  	    <a href="tableviewer?tabName=${entry.tableName}&selectedSchema=${chosenSchema}">
              ${entry.tableName}
            </a>
           </td>
	  	   <td align="center">${entry.dataPolicy}</td>
	  	   <td align="center">${entry.serverGroups}</td>
	  	   <td align="center">${entry.asyncListeners}</td>
	  	   <td align="center">${entry.gatewaySenders}</td>
	  	   <td align="left">
            <c:choose>
                <c:when test="${viewType == 'HDFS'}">
                    <a href="executequery?query=select * from ${chosenSchema}.${entry['tableName']}">
                        <img class="icon" width="16" height="16" src="../themes/original/img/b_views.png" alt="View Data" title="View Data" />
                    </a>&nbsp;
                </c:when>
                <c:otherwise>
                    <a href="executequery?query=select * from ${chosenSchema}.${entry['tableName']}">
                        <img class="icon" width="16" height="16" src="../themes/original/img/b_views.png" alt="View Data" title="View Data" />
                    </a>&nbsp;
                </c:otherwise>
            </c:choose>
    		<a href="tables?tabName=${entry['tableName']}&tabAction=DROP&selectedSchema=${chosenSchema}&viewType=${viewType}" onclick="return confirmLink(this, 'DROP TABLE ${entry['tableName']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Drop Table" title="Drop Table" />
            </a>&nbsp;  
    		<a href="tables?tabName=${entry['tableName']}&tabAction=EMPTY&selectedSchema=${chosenSchema}&viewType=${viewType}" onclick="return confirmLink(this, 'TRUNCATE TABLE ${entry['tableName']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_empty.png" alt="Truncate Table" title="Truncate Table" />
            </a>&nbsp;  
    		<a href="tables?tabName=${entry['tableName']}&tabAction=STRUCTURE&selectedSchema=${chosenSchema}&viewType=${viewType}">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_tbloptimize.png" alt="View Table Structure" title="View Table Structure" />
            </a>&nbsp;
    		<a href="tables?tabName=${entry['tableName']}&tabAction=MEMORYUSAGE&selectedSchema=${chosenSchema}&viewType=${viewType}">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_tblanalyse.png" alt="View Memory Usage of table" title="View Memory Usage of table" />
            </a>&nbsp;  
    		<a href="tables?tabName=${entry['tableName']}&tabAction=DATALOCATIONS&selectedSchema=${chosenSchema}&viewType=${viewType}">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_relations.png" alt="View Data Location Members" title="View Data Location Members" />
            </a>&nbsp;
    		<a href="tables?tabName=${entry['tableName']}&tabAction=ALLTABLEINFO&selectedSchema=${chosenSchema}&viewType=${viewType}">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_tblexport.png" alt="View All Table Information" title="View All Table Information" />
            </a>&nbsp;
            <a href="tables?tabName=${entry['tableName']}&tabAction=TABLETRIGGERS&selectedSchema=${chosenSchema}&viewType=${viewType}">
               <img class="icon" width="16" height="16" src="../themes/original/img/b_trigger.png" alt="View Table Triggers" title="View Table Triggers" />
            </a>&nbsp;
    		<a href="createindex?tabName=${entry['tableName']}&schemaName=${chosenSchema}&viewType=${viewType}">
             <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Create Index" title="Create Index" />
            </a>&nbsp;
            <a href="addlistener?tableName=${entry['tableName']}&schemaName=${chosenSchema}">
               <img class="icon" width="16" height="16" src="../themes/original/img/s_vars.png" alt="Add Listener" title="Add Listener" />
            </a>&nbsp;
            <a href="createtrigger?settablename=${chosenSchema}.${entry['tableName']}">
               <img class="icon" src="../themes/original/img/b_trigger_add.png" width="16" height="16" alt="Add Table Trigger" />
            </a>
	  	   </td>
  	   </tr>
	</c:forEach>
 </tbody>
</table>

<div class="clearfloat">
<img class="selectallarrow" src="../themes/original/img/arrow_ltr.png"
    width="38" height="22" alt="With selected:" />
<a href="tables?selectedSchema=${chosenSchema}&viewType=${viewType}"
    onclick="if (setCheckboxes('table_results', 'true')) return false;">
    Check All</a>

/
<a href="tables?selectedSchema=${chosenSchema}&viewType=${viewType}"
    onclick="if (unMarkAllRows('tablesForm')) return false;">
    Uncheck All</a>

<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
    <option value="With selected:" selected="selected">With selected:</option>
    <option value="Drop" >Drop</option>
    <option value="Empty" >Truncate</option>
</select>


<script type="text/javascript">
<!--
// Fake js to allow the use of the <noscript> tag
//-->
</script>
<noscript>
    <input type="submit" value="Go" />
</noscript>
</div>

</form>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>