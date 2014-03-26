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
<title><fmt:message key="sqlfireweb.appname" /> Query Result</title>
</head>
<body>
<h2>${queryDescription}</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<c:if test="${!empty paramMap}">
 <c:if test="${empty queryResults}">
  <div class="warning">
    Parameters must be set prior to executing this query
  </div>
 </c:if>
<form method="get" action="executequeryreport">
<input type="hidden" name="beanId" value="${param.beanId}" />
<fieldset>
 <legend>Query Parameter Form</legend>
 <table class="formlayout">
  <c:forEach var="item" items="${paramMap}">
    <tr>
     <td align="right">${item.value}</td>
     <td>
       <input type="text" name="${item.key}" />
     </td>
    </tr>
  </c:forEach>
 </table>
 </fieldset>
  <fieldset class="tblFooters">
    <input type="submit" value="Execute" name="pSubmit" />
    <input type="reset" value="Reset" />
  </fieldset>
 </form>
</c:if>

<c:choose>
  <c:when test="${!empty queryResults}">
   <p />
    <div class="success">
      Query completed successfully
    </div> 
   <br />
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
  </c:when>
</c:choose>

<br />
<jsp:include page="footer.jsp" flush="true" />

</body>
</html>