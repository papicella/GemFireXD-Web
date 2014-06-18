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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
input:focus, textarea:focus {
background-color: #C4D4E2;
} 
</style>
    <style>

        .node circle {
            fill: #fff;
            stroke: steelblue;
            stroke-width: 1.5px;
        }

        .node {
            font: 10px sans-serif;
        }

        .link {
            fill: none;
            stroke: #ccc;
            stroke-width: 1.5px;
        }

    </style>

    <style type="text/css">
        ul{list-style-type: none;}
        table.hide { display: none; }
        span.expand, span.collapse
        {
            color:#3B5998;
            font-size:20px;
            position:relative;
            left:10px;
        }
        span.plus, span.minus
        {
            color:#000000;
            font-size:20px;
        }
        span.expand, span.collapse { cursor: pointer; }
        span.expand span.minus { display: none; }
        span.collapse span.plus { display: none }
    </style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><fmt:message key="sqlfireweb.appname" /> - Query Window</title>
<link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
<link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />

  <script language="JavaScript" type="text/javascript">
      window.onload = function()
      {
          var ul = document.getElementById('main-ul');
          var childUls = ul.getElementsByTagName('table');
          for (var i = 0, l = childUls.length; i &lt; l; i)
          {
              childUls[i].className = 'hide';
          }
      }

      function toggle(el)
      {
          do
          {
              var ul = getNextElementSibling(el);
          }
          while (ul &amp;&amp; ul.tagName.toLowerCase() !== 'table');
          ul.className = ul.className === '' ? 'hide' : '';
          el.className = el.className === 'collapse' ? 'expand' : 'collapse';
      }

      function getNextElementSibling(node) {
          while (node &amp;&amp; (node = node.nextSibling)) {
              if (node.nodeType == 1) {
                  return node;
              }
          }
      }

      function hide(el)
      {
          do
          {
              var ul = getNextElementSibling(el);
          }
          while (ul &amp;&amp; ul.tagName.toLowerCase() !== 'table');
          ul.className = ul.className === '' ? 'hide' : '';
          el.className = el.className === 'expand' ? 'collapse' : '';
      }

      function Collapser(item)
      {
          // Make sure the tags are setup correctly, if not just return
          if (!item.parentNode.getElementsByTagName("ul")[0]) return;
          var x = item.parentNode.getElementsByTagName("ul")[0];
          // if already visible, make disappear, otherwise reappear
          x.style.display = (x.style.display == "") ? 'none' : "";
          //item.parentNode.style.listStyle = (item.parentNode.style.listStyle == "") ? "disc" : "";
          item.parentNode.style.listStyleImage = (item.parentNode.style.listStyleImage == "")
                  ? "url('./themes/original/img/plus.gif')" : "";
      }

      function showSelected()
      {
         var selObj = document.getElementById('queryTypeId');
         var queryWindowObj = document.getElementById('sqlquery');
         var selIndex = selObj.selectedIndex;
         var queryType = selObj.options[selIndex].value;
         var query = '';

         if (queryType == 'Select')
         {
           query = 'select ';
         }
         else if ( queryType == 'Insert' )
         {
           query = 'insert into ';
         }
         else if ( queryType == 'Delete' )
         {
           query = 'delete from ';
         }
         else if ( queryType == 'DDL' )
         {
           query = 'alter ';
         }
         else if ( queryType == 'Update' )
         {
           query = 'update ';
         }
         else if (queryType == 'TODAYSDATE')
         {
           query = 'select current_date as "System Date" from sysibm.sysdummy1';
         }
         else if (queryType == 'VIEWMEMBERS')
         {
           query = 'select substr(id, 1, 48) as "Id", substr(netservers, 1, 30) as "NetServers", pid from sys.members';
         }
         else if (queryType == 'VIEWSCHEMAS')
         {
           query = 'select schemaname from sys.sysschemas';
         }
         else if (queryType == 'VIEWALIASES')
         {
           query = 'select aliasschemaname, alias, javaclassname from sys.sysaliases order by 1';
         }
         else if (queryType == 'VIEWSTATEMENTS')
         {
             query = 'select stmtname, text from sys.SYSSTATEMENTS';
         }
         else if (queryType == 'VIEWSYNONYMS')
         {
             query = 'select alias, aliasschemaname, CAST(aliasinfo AS VARCHAR(100)) as "TableOrView" from sys.sysaliases where aliastype = \'S\'';
         }

         queryWindowObj.value = query;

      }

      function checkFile() {
          var fileElement = document.getElementById("sqlfilename");
          var fileExtension = "";
          if (fileElement.value.lastIndexOf(".") > 0) {
              fileExtension = fileElement.value.substring(fileElement.value.lastIndexOf(".") + 1, fileElement.value.length);
          }
          if (fileExtension == "sql" || fileExtension == "SQL") {
              return true;
          }
          else {
              alert("You must select a SQL file for upload with .sql extension");
              return false;
          }
      }

  </script> 
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> SQL Worksheet</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="info">
 Displaying 
 ${sessionScope.prefs.maxRecordsinSQLQueryWindow == -1 ? "all " : sessionScope.prefs.maxRecordsinSQLQueryWindow} 
 records for SELECT queries
 &nbsp; 	
    <a href="preferences" title="Preferences">
	  <img class="icon" src="../themes/original/img/b_props.png" width="16" height="16" alt="SQLFire Preferences" />
	</a>
    <i>Alter records to display
    &nbsp; - &nbsp;
    Auto Commit = ${sessionScope.prefs.autoCommit}
    </i>
</div>

<c:if test="${!empty sqlfile}">
	<div class="success">
  		Successfully loaded ${sqlfile}
  	</div>
</c:if>

<br />
<form action="query" method="post" enctype="multipart/form-data" onsubmit="return checkFile();">
SQL File : <input id="sqlfilename" type="file" name="sqlfilename" size="20" maxlength="300" />
<input type="submit" value="Load SQL Script"/>
</form>
<p />

<form:form method="post" action="query" modelAttribute="queryAttribute" id="sqlqueryform">
<a name="querybox"></a>
<select id="queryTypeId" name="queryType" onclick="showSelected();" style="margin: 0 3em 0 3em;">
    <option value="Query Type:" selected="selected">Select option</option>
    <option value="Select" >SQL Select</option>
    <option value="Delete" >SQL Delete</option>
    <option value="Insert" >SQL Insert</option>
    <option value="Update" >SQL Update</option>
    <option value="DDL" >SQL DDL</option>
    <option value="TODAYSDATE" >Today's Date</option>
    <option value="VIEWMEMBERS" >View Distributed System Members</option>
    <option value="VIEWSCHEMAS" >View Schema Names</option>
    <option value="VIEWALIASES" >View System Aliases</option>
    <option value="VIEWSTATEMENTS" >View System Statements</option>
    <option value="VIEWSYNONYMS" >View Synonyms</option>
</select>
&nbsp; &nbsp;
<a href="query?action=commit" title="Commit">
  <img class="icon" src="../themes/original/img/commit.png" alt="Commit" border="0" />
  Commit
</a>
&nbsp; &nbsp;
<a href="query?action=rollback" title="Rollback">
  <img class="icon" src="../themes/original/img/rollback.png" alt="Commit" border="0" />
  Rollback
</a>
&nbsp; &nbsp;
<a href="history" title="History">
  <img class="icon" src="../themes/original/img/b_props.png" alt="History" border="0" />
  History
</a>

<div id="queryboxcontainer">
<fieldset id="querybox">
<legend>Run SQL query, DML or DDL into the query window below</legend>
<div id="queryfieldscontainer">
<div id="sqlquerycontainerfull">
<form:textarea path="query" id="sqlquery" cols="40"  rows="15"  dir="ltr"></form:textarea>
</div>
<div class="clearfloat"></div>
</div>
<div class="clearfloat"></div>
</fieldset>
</div>
<fieldset id="queryboxfooter" class="tblFooters">
<div class="formelement">
</div>
Save?
<form:select path="saveWorksheet" style="margin: 0 2em 0 2em;">
    <form:option value="N">No</form:option>
    <form:option value="Y">Yes</form:option>
</form:select>
Member?
<form:select path="showMember" style="margin: 0 2em 0 2em;">
   <form:option value="N">No</form:option> 
   <form:option value="Y">Yes</form:option>         
</form:select>
Count?
<form:select path="queryCount" style="margin: 0 2em 0 2em;">
   <form:option value="Y">Yes</form:option> 
   <form:option value="N">No</form:option>         
</form:select>
Time?
<form:select path="elapsedTime" style="margin: 0 2em 0 2em;">
   <form:option value="N">No</form:option>
   <form:option value="Y">Yes</form:option>          
</form:select>
Explain?
<form:select path="explainPlan" style="margin: 0 2em 0 2em;">
   <form:option value="N">No</form:option>
   <form:option value="Y">Yes</form:option>
   <form:option value="T">Plain</form:option>
</form:select>
<input type="image" src="../themes/original/img/Execute.png" name="SQL" />
<div class="clearfloat"></div>
</fieldset>
</form:form>

<br />

<c:if test="${!empty explainresult}">
    <c:import url="../advancedViewXSL2.xsl" var="xslt"/>
    <x:transform xml="${explainresult}" xslt="${xslt}"/>
    <td></td>
</c:if>

<c:if test="${!empty explaintxtresult}">
    <h3>Explain Result</h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${explaintxtresult}</pre></td>
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
   <td> ${result.command} </td>
  </tr>
  <tr>
   <td align="right">Message:</td>
   <td> 
    <font color="${result.message == 'SUCCESS' ? 'green' : 'red'}">
      <c:choose>
       <c:when test="${result.rows != -1}">
         ${result.message} : ${result.rows} rows inserted/updated/deleted
       </c:when>
       <c:otherwise>
         ${result.message}
       </c:otherwise>
      </c:choose>
    </font>
   </td>
  </tr>
  <c:if test="${not empty result.elapsedTime}">
  	<tr>
     <td align="right">Elapsed Time:</td>
     <td>${result.elapsedTime} seconds</td>
  	</tr> 
  </c:if>
 </table>
</fieldset>
<br />
</c:if>
  
<c:if test="${!empty procresults}">
 <div class="info">
    Stored Procedure ${callstatement} has ${dynamicresults} dynamic result sets returned.
 </div>
 <br />
 <c:forEach var="resultset" items="${procresults}">
     <div class="success">
       Stored Procedure ResultSet returned successfully
    </div> 
    <br />
    <table id="table_results" class="data">
    <thead>
      <tr>
       <c:forEach var="columnName" items="${resultset.columnNames}">
        <th><c:out value="${columnName}"/></th>
      </c:forEach>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="row" varStatus="loop" items="${resultset.rows}">
        <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
          <c:forEach var="columnName" items="${resultset.columnNames}">
            <td><c:out value="${row[columnName]}"/></td>
          </c:forEach>           
         </tr>
      </c:forEach>  
    </tbody>
   </table>
   <br />
 </c:forEach>
</c:if>

<c:if test="${!empty queryResults}">
  <c:choose>
    <c:when test="${!empty queryResultCount}">
     <div class="success">
       Query completed successfully - Total of ${queryResultCount} row(s)
    </div>
  </c:when>
  <c:otherwise>
     <div class="success">
       Query completed successfully
    </div>  
  </c:otherwise>
  </c:choose>

   <br />
   <table id="table_results" class="data">
     <tbody>
       <tr class="odd">
         <td><b>GemFireXD*Web&gt;</b> ${querysql}</td>
       </tr>
     </tbody>
   </table>
 
   <p />
   <table id="table_results" class="data">
    <thead>
      <tr>
       <c:forEach var="columnName" items="${queryResults.columnNames}">
        <th><c:out value="${columnName}"/></th>
      </c:forEach>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="row" varStatus="loop" items="${queryResults.rows}">
        <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
          <c:forEach var="columnName" items="${queryResults.columnNames}">
            <td><c:out value="${row[columnName]}"/></td>
          </c:forEach>           
         </tr>
      </c:forEach>  
    </tbody>
   </table>
   <c:if test="${!empty elapsedTime}">
       <br />
       <div class="info">
       Elapsed Time ${elapsedTime} seconds
       </div>
   </c:if>
</c:if>

<c:if test="${!empty sqlResultMap}">
  <h2>SQL Worksheet Results</h2>
  <div class="alert">
    Ran ${statementsExecuted} statements...
  </div>
  <c:forEach var="item" items="${sqlResultMap}">
   <c:choose>
    <c:when test="${fn:endsWith(item.key,'SELECT')}">
       <fieldset>
       <legend>Query Result</legend>
	   <table id="table_results" class="data">
	     <tbody>
	       <tr class="odd">
	         <c:set var="sql"></c:set>
	         <td><b>GemFireXD*Web&gt;</b> ${item.value[0]} </td>
	       </tr>
	     </tbody>
	   </table>
       <c:set var="queryResults" value="${item.value[1]}" />
       <table id="table_results" class="data">
        <thead>
          <tr>
           <c:forEach var="columnName" items="${queryResults.columnNames}">
            <th><c:out value="${columnName}"/></th>
          </c:forEach>
          </tr>
        </thead>
        <tbody>
          <c:set var="i" value="0" />
          <c:forEach var="row" varStatus="loop" items="${queryResults.rows}">
            <c:set var="i" value="${i + 1}" />
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
              <c:forEach var="columnName" items="${queryResults.columnNames}">
                <td><c:out value="${row[columnName]}"/></td>
              </c:forEach>           
             </tr>
          </c:forEach>  
        </tbody>
       </table>
       <div class="info">
        ${i} row(s) selected
       </div>
       <c:if test="${!empty item.value[2]}">
	       <div class="info">
	         Elapsed Time ${item.value[2]} seconds
	       </div>      
       </c:if>
       </fieldset>
       <br/>
    </c:when>
    <c:otherwise>
      <c:set var="result" value="${item.value}" />
      <fieldset>
       <legend>Result</legend>
       <table class="formlayout">
        <tr>
         <td align="right">Command:</td>
         <td> ${result.command} </td>
        </tr>
        <tr>
         <td align="right">Message:</td>
         <td> 
          <font color="${result.message == 'SUCCESS' ? 'green' : 'red'}">
		      <c:choose>
		       <c:when test="${result.rows != -1}">
		         ${result.message} : ${result.rows} rows inserted/updated/deleted
		       </c:when>
		       <c:otherwise>
		         ${result.message}
		       </c:otherwise>
		      </c:choose>
          </font>
         </td>
        </tr>
		  <c:if test="${not empty result.elapsedTime}">
		  	<tr>
		     <td align="right">Elapsed Time:</td>
		     <td>${result.elapsedTime} seconds</td>
		  	</tr> 
		  </c:if>
       </table>
      </fieldset>     
    </c:otherwise>
   </c:choose>
  </c:forEach>
</c:if>
 
<br />

<jsp:include page="footer.jsp" flush="true" />
</body>
</html>