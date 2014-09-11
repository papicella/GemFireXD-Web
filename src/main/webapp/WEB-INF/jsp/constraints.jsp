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
<title><fmt:message key="sqlfireweb.appname" /> - Constraints</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Constraints</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Found ${records} constraint(s).
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

<c:if test="${!empty fkInfoResult}">
    <h3>Foreign Key ${constraintname} Information</h3>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th>Column Name</th>
            <th>Value</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${fkInfoResult.rows}">
            <c:forEach var="columnName" items="${fkInfoResult.columnNames}">
                <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                    <td align="right">${columnName}</td>
                    <td align="left">
                        <c:choose>
                            <c:when test="${columnName == 'DELETERULE'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'R'}">
                                        NO ACTION
                                    </c:when>
                                    <c:when test="${row[columnName] == 'S'}">
                                        RESTRICT
                                    </c:when>
                                    <c:when test="${row[columnName] == 'C'}">
                                        CASCADE
                                    </c:when>
                                    <c:when test="${row[columnName] == 'U'}">
                                        SET NULL
                                    </c:when>
                                </c:choose>
                            </c:when>
                            <c:when test="${columnName == 'UPDATERULE'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'R'}">
                                        NO ACTION
                                    </c:when>
                                    <c:when test="${row[columnName] == 'S'}">
                                        RESTRICT
                                    </c:when>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${row[columnName]}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br />
</c:if>

<form action="constraints" method="POST">
   <b>Filter Constraint Name </b>
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
    <a href="constraints?search=${param.search}&selectedSchema=${chosenSchema}&startAtIndex=${(startAtIndex - sessionScope.prefs.recordsToDisplay)}&endAtIndex=${startAtIndex}">
      <img src="../themes/original/img/Previous.png" border="0" />
    </a>
    &nbsp;
  </c:if>
  <c:if test="${estimatedrecords != endAtIndex}">
    <a href="constraints?search=${param.search}&selectedSchema=${chosenSchema}&startAtIndex=${endAtIndex}&endAtIndex=${endAtIndex + sessionScope.prefs.recordsToDisplay}">
      <img src="../themes/original/img/Next.png" border="0" />
    </a>
  </c:if>
  &nbsp; <font color="Purple">Current Set [${startAtIndex + 1} - ${endAtIndex}] </font>
</c:if>

<p />

<table id="table_results" class="data">
 <thead>
   <tr>
    <th>Schema</th>
    <th>Table Name</th>
    <th>Constraint Name</th>
    <th>Type</th>
    <th>State</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="entry" varStatus="loop" items="${cons}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
	  	   <td align="center">${entry.schemaName}</td>
	  	   <td align="center">
               <a href="tableviewer?tabName=${entry.tableName}&selectedSchema=${chosenSchema}">
                   <c:out value="${entry.tableName}"/>
               </a>
           </td>
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
	  	   <td>
    		<a href="constraints?tabName=${entry['tableName']}&selectedSchema=${chosenSchema}&constraintName=${entry['constraintName']}&conAction=DROP" onclick="return confirmLink(this, 'DROP TABLE CONSTRAINT ${entry['constraintName']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Drop Table Constraint" title="Drop Table Constraint" />
            </a>&nbsp;
            <c:if test="${entry.type == 'F'}">
                <a href="constraints?selectedSchema=${chosenSchema}&constraintName=${entry['constraintName']}&constraintId=${entry['constraintId']}&conAction=FKINFO">
                    <img class="icon" width="16" height="16" src="../themes/original/img/b_tblexport.png" alt="View Foreign Key Info" title="View Foreign Key Info" />
                </a>&nbsp;
            </c:if>
	  	   </td>
  	   </tr>
	</c:forEach>
 </tbody>
</table>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>