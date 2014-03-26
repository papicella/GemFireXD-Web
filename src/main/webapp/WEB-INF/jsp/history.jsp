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
<title><fmt:message key="sqlfireweb.appname" /> History</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> History</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Found ${historysize} command(s).
</div>
<br />

<c:if test="${historyremoved != null}">
  <div class="success">
    ${historyremoved}
  </div>
</c:if>

<br />
<a href="history?histAction=CLEAR">
    <img class="icon" src="../themes/original/img/b_drop.png" width="16" height="16" alt="Clear All History" />
    &nbsp; Clear All History
</a>
<p />

<table id="table_results" class="data">
 <thead>
   <tr>
    <th>#</th>
    <th>SQL</th>
    <th>Run</th>
   </tr>
 </thead>
 <tbody>
    <c:set var="i" value="0" />
	<c:forEach var="entry" varStatus="loop" items="${historyList}">
	  <c:set var="i" value="${i + 1}" />
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		<td>${i}</td>
		<td align="left">${entry}</td>
		<td>
		   <a href="executequery?query=${entry}">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_sql.png" alt="Run" title="Run" />
           </a>
		</td>
	  </tr>
	</c:forEach>
 </tbody>
</table>
<br />

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>