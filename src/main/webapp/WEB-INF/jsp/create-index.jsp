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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
<link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
<script src="../js/functions.js" type="text/javascript"></script>
<SCRIPT language=Javascript>
   <!--
   function isNumberKey(evt)
   {
      var charCode = (evt.which) ? evt.which : event.keyCode
      if (charCode > 31 && (charCode < 48 || charCode > 57))
         return false;

      return true;
   }

   function validate()
   {
       if (document.getElementById("indexName").value == "")
       {
           alert("Index Name Cannot Be Empty");
           return false;
       }
       else
       {
           return true;
       }
   }
   //-->
</SCRIPT>
<title><fmt:message key="sqlfireweb.appname" /> Create Index</title>
</head>
<body>
<h2><fmt:message key="sqlfireweb.appname" /> Create Index</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    Creates an index on one or more columns of a table.
</p>

<c:if test="${!empty sql}">
    <div class="success">
        Successfully generated SQL
    </div>
    <h3>Create Index SQL </h3>
    <a href="javascript:history.back()">Return to Ensure Index Colmun Selection Not Lost</a>
    <p />
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
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

 <c:if test="${not empty columns}">
	<div class="notice">
		Creating index for table ${tabName}
	</div> 
	<br />

    <form:form action="createindex" method="POST" modelAttribute="indexAttribute" onsubmit="return validate()">
    <table class="data">
        <thead>
        <tr>
            <th colspan="2">Index Properties</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Index Name</td>
            <td><form:input type="TEXT" path="indexName" size="20" maxlength="30" /></td>
        </tr>
        <tr>
            <td>Schema Name</td>
            <td><form:input type="TEXT" path="schemaName" value="${tableSchemaName}" readonly="readonly" /></td>
        </tr>
        <tr>
            <td>Table Name</td>
            <td><form:input type="TEXT" path="tableName" value="${tabName}" readonly="readonly" /></td>
        </tr>
        <tr>
            <td>Unique?</td>
            <td>
                <form:select path="unique">
                    <c:choose>
                        <c:when test="${indexAttribute.unique == 'N'}">
                            <form:option value="N" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="N" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${indexAttribute.unique == 'Y'}">
                            <form:option value="Y" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="Y" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
        </tr>
        <tr>
            <td>Case Sensitive?</td>
            <td>
                <form:select path="caseSensitive">
                    <form:option value=""></form:option>
                    <c:choose>
                        <c:when test="${indexAttribute.caseSensitive == '-- SQLFIRE-PROPERTIES caseSensitive=false'}">
                            <form:option value="-- SQLFIRE-PROPERTIES caseSensitive=false" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="-- SQLFIRE-PROPERTIES caseSensitive=false" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${indexAttribute.caseSensitive == '-- SQLFIRE-PROPERTIES caseSensitive=true'}">
                            <form:option value="-- SQLFIRE-PROPERTIES caseSensitive=true" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="-- SQLFIRE-PROPERTIES caseSensitive=true" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
        </tr>
        </tbody>
    </table>

    <p />
	
	<table id="table_columns" class="data">
	<thead>
	  <tr>
	   <th></th>
	   <th>Column</th>
	   <th>Order</th>
	   <th>Position</th>
	  </tr>
	</thead>
	<tbody>
	<c:forEach var="entry" varStatus="loop" items="${columns}">
	 <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		 <td align="center">
		     <input type="checkbox" 
		            name="selected_column[]"
		            value="${entry['columnName']}_${loop.index + 1}"
		            id="checkbox_tbl_${loop.index + 1}" />
		  </td>
		  <td align="center">${entry.columnName}</td>
		  <td>
		  	<select name="idxOrder[]">
	  			<option value="ASC" selected="selected">ASC</option>
	  			<option value="DESC">DESC</option>
			</select>
		  </td>
		  <td align="center">
		  	<input type="TEXT" name="position[]" size="4" maxlength="100" onkeypress="return isNumberKey(event);" />
		  </td>
	  </tr>
	</c:forEach>
	</tbody>
	</table>
	<br />
	<input type="submit" value="Create" name="pSubmit" />
    <input type="submit" value="Show SQL" name="pSubmit" />
    <input type="submit" value="Save to File" name="pSubmit" />
	</form:form>
 </c:if>


<jsp:include page="footer.jsp" flush="true" />

</body>
</html>