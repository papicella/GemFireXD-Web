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
<link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
<link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
<script type="text/javascript">
  // <![CDATA[
  
  // js form validation stuff
  var confirmMsg  = 'Do you really want to ';
  // ]]>
</script>
<script src="../js/functions.js" type="text/javascript"></script>

<title><fmt:message key="sqlfireweb.appname" /> - Stored ${procType}(s)</title>
</head>
<body>
<c:choose>
  <c:when test="${procType == 'P'}">
    <c:set var="type" value="Procedure" />
  </c:when>
  <c:otherwise>
    <c:set var="type" value="Function" />
  </c:otherwise>
</c:choose>

<h2><fmt:message key="sqlfireweb.appname" /> - Stored ${type}(s)</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Found ${records} ${type}(s).
    <c:choose>
        <c:when test="${procType == 'P'}">
            <a href="createprocedure">
                <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Add Procedure" title="Add Procedure" />
                Create Procedure
            </a>
        </c:when>
        <c:otherwise>
            <a href="createfunction">
                <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Add Function" title="Add Function" />
                Create Function
            </a>
        </c:otherwise>
    </c:choose>
</div>

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

<c:if test="${!empty procPermissionsResult}">
    <h3>Stored ${type} ${procName} Permissions </h3>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <c:forEach var="columnName" items="${procPermissionsResult.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${procPermissionsResult.rows}">
            <tr class="odd">
                <c:forEach var="columnName" items="${procPermissionsResult.columnNames}">
                    <td align="center"><c:out value="${row[columnName]}"/></td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty procPermissionsResult.rows}">
        <i>No rows exist</i>
        <p />
    </c:if>
</c:if>

<c:if test="${not empty procPermissionsResult.rows}">
    <br />
    <form action="procs" method="GET">
        <input type="hidden" name="selectedSchema" value="${chosenSchema}" />
        <input type="hidden" name="procName" value="${procName}" />
        <input type="hidden" name="procType" value="${type}" />
        <input type="hidden" name="procAction" value="ADDORREVOKE" />
        <select name="privType">
            <option value="GRANT" selected="selected">GRANT</option>
            <option value="REVOKE">REVOKE</option>
        </select>
        &nbsp; Option &nbsp;
        <select name="privOption">
            <option value="EXECUTE" selected="selected">EXECUTE</option>
        </select>
        &nbsp; To/From &nbsp;
        <select name="privTo">
            <option value="PUBLIC">PUBLIC</option>
            <c:forEach var="row" items="${schemas}">
                <option value="${row}">${row}</option>
            </c:forEach>
        </select>
        <input type="submit" value="Perform Privilege Action" />
    </form>
    <p />
</c:if>

<c:if test="${!empty procParams}">
<h3>Stored ${type} ${procName} Parameters</h3>
<table id="table_results" class="data">
 <thead>
   <tr>
    <th>Param Id#</th>
    <th>Column Name</th>
    <th>Type Name</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="procrow" items="${procParams}">
		<tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		  <td>
		     ${procrow.parameterId}
          </td>
		  <td>
             ${procrow.columnName}
          </td>
		  <td>
              <c:choose>
                  <c:when test="${procrow.columnName == ''}">
                      ${procrow.typeName}
                      <font color="GREEN">
                          (FUNCTION RETURN TYPE)
                      </font>
                  </c:when>
                  <c:otherwise>
                      ${procrow.typeName}
                  </c:otherwise>
              </c:choose>
          </td>
		</tr>
	</c:forEach>
 </tbody>
</table>
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

<form action="procs" method="POST">
   <b>Filter ${type} Name </b>
   <input type="hidden" name="procType" value="${procType}" />
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
    <a href="procs?search=${param.search}&selectedSchema=${chosenSchema}&procType=${procType}&startAtIndex=${(startAtIndex - sessionScope.prefs.recordsToDisplay)}&endAtIndex=${startAtIndex}">
      <img src="../themes/original/img/Previous.png" border="0" />
    </a>
    &nbsp;
  </c:if>
  <c:if test="${estimatedrecords != endAtIndex}">
    <a href="procs?search=${param.search}&selectedSchema=${chosenSchema}&procType=${procType}&startAtIndex=${endAtIndex}&endAtIndex=${endAtIndex + sessionScope.prefs.recordsToDisplay}">
      <img src="../themes/original/img/Next.png" border="0" />
    </a>
  </c:if>
  &nbsp; <font color="Purple">Current Set [${startAtIndex + 1} - ${endAtIndex}] </font>
</c:if>

<p />

<form method="post" action="procs" name="tablesForm" id="tablesForm">
<input type="hidden" name="procType" value="${procType}" />
<input type="hidden" name="selectedSchema" value="${chosenSchema}" />
<table id="table_results" class="data">
 <thead>
   <tr>
    <th></th>
    <th>Schema</th>
    <th>Name</th>
    <th>Java Class Name</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="entry" varStatus="loop" items="${procs}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		  <td align="center">
		      <input type="checkbox" 
		             name="selected_proc[]"
		             value="${entry['name']}"
		             id="checkbox_proc_${loop.index + 1}" />
		   </td>
		   <td align="center">${entry.schemaName}</td>
	  	   <td align="center">${entry.name}</td>
	  	   <td align="center">${entry.javaClassName}</td>
	  	   <td align="center">
    		<a href="procs?procName=${entry['name']}&procAction=DROP&procType=${procType}&selectedSchema=${chosenSchema}" onclick="return confirmLink(this, 'DROP ${type} ${entry['name']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Drop ${type}" title="Drop ${type}" />
            </a>&nbsp; 
            <c:if test="${procType == 'P' || procType == 'F'}">
	    		<a href="procs?procName=${entry['name']}&procAction=DESC&procType=${procType}&selectedSchema=${chosenSchema}">
	             <img class="icon" width="16" height="16" src="../themes/original/img/b_dbstatistics.png" alt="Describe ${type}" title="Describe ${type}" />
	            </a>&nbsp;
                <a href="procs?procName=${entry['name']}&procAction=PRIVS&procType=${procType}&selectedSchema=${chosenSchema}">
                    <img class="icon" width="16" height="16" src="../themes/original/img/s_rights.png" alt="Permissions" title="Permissions" />
                </a>&nbsp;
            </c:if>
	  	   </td>
  	   </tr>
	</c:forEach>
 </tbody>
</table>

<div class="clearfloat">
<img class="selectallarrow" src="../themes/original/img/arrow_ltr.png"
    width="38" height="22" alt="With selected:" />
<a href="procs?selectedSchema=${chosenSchema}&procType=${procType}"
    onclick="if (setCheckboxes('table_results', 'true')) return false;">
    Check All</a>

/
<a href="procs?selectedSchema=${chosenSchema}&procType=${procType}"
    onclick="if (unMarkAllRows('tablesForm')) return false;">
    Uncheck All</a>

<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
    <option value="With selected:" selected="selected">With selected:</option>
    <option value="Drop" >Drop</option>
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

<p />

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>