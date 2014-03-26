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
<title><fmt:message key="sqlfireweb.appname" />Web - Current JDBC Connections</title>
<link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
<link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
<script type="text/javascript">
    // <![CDATA[

    // js form validation stuff
    var confirmMsg  = 'Do you really want to ';
    // ]]>
</script>
</head>
<body>
<h2>Current <fmt:message key="sqlfireweb.appname" /> JDBC Connections</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Total of ${conmapsize} GemFireXD*Web JDBC Connections
</div>
<br />
<c:if test="${saved != null}">
    <div class="success">
            ${saved}
    </div>
    <br />
</c:if>

<table id="table_results" class="data">
 <thead>
   <tr>
    <th>No</th>
    <th>Key</th>
    <th>JDBC URL</th>
    <th>Schema</th>
    <th>Connected At</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
    <c:set var="i" value="0" />
	<c:forEach var="entry" varStatus="loop" items="${conmap}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
  	   <c:set var="i" value="${i + 1}" />
  	   <td>${i}</td>
  	   <td>
  	     <img src="../themes/original/img/key.png" width="16" height="16" alt="${entry.key}" title="${entry.key}"/>
  	   </td>
  	   <td>${entry.value.url}</td>
  	   <td>${entry.value.schema}</td>
  	   <td>${entry.value.connectedAt}</td>
       <td>
           <c:if test="${entry.key != sessionScope.user_key}">
               <a href="viewconmap?conMapAction=DELETE&key=${entry.key}" onclick="return confirmLink(this, 'Close Connection?')">
                   <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Close Connection" title="Close Connection" />
               </a>&nbsp;
           </c:if>
       </td>
	</c:forEach>
 </tbody>
</table>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>