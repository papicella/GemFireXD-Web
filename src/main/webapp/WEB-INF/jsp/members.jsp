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
<title><fmt:message key="sqlfireweb.appname" /> - Members</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Members</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Found ${records} member(s).
</div>
<br />

<c:if test="${!empty allMemberInfoResult}">
 <h3>${memberid} Member information</h3>
 <table id="table_results" class="data">
  <thead>
    <tr>
      <th>Column Name</th>
      <th>Value</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${allMemberInfoResult.rows}">
        <c:forEach var="columnName" items="${allMemberInfoResult.columnNames}">
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

<c:if test="${!empty props}">
    <h3>${memberid} ${propertyAction} Properties </h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${props}</pre></td>
        </tr>
        </tbody>
    </table>
    <br />
</c:if>

<table id="table_results" class="data">
 <thead>
   <tr>
    <th>Id</th>
    <th>Status</th>
    <th>Hostdata</th>
    <th>ISelder</th>
    <th>Host</th>
    <th>Pid</th>
    <th>Port</th>
    <th>Locator</th>
    <th>Server Groups</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="entry" varStatus="loop" items="${members}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		   <td align="center">${entry.id}</td>
	  	   <td align="center">
	  	     <c:choose>
	  	     	<c:when test="${entry.status == 'RUNNING'}">
	  	     		<font color="green">
	  	     		  ${entry.status}
	  	     		</font>
	  	     	</c:when>
	  	     	<c:otherwise>
	  	     		<font color="red">
	  	     		  ${entry.status}
	  	     		</font>
	  	     	</c:otherwise>
	  	     </c:choose>	  	   
	  	   </td>
	  	   <td align="center">
	  	     <c:choose>
	  	     	<c:when test="${entry.hostdata == '1'}">
	  	     		YES
	  	     	</c:when>
	  	     	<c:otherwise>
	  	     		NO
	  	     	</c:otherwise>
	  	     </c:choose>
	  	   </td>
	  	   <td align="center">${entry.iselder}</td>
	  	   <td align="center">${entry.host}</td>
	  	   <td align="center">${entry.pid}</td>
	  	   <td align="center">${entry.port}</td>
	  	   <td align="center">${entry.locator}</td>
	  	   <td align="center">${entry.serverGroups}</td>
	  	   <td align="center">
    		<a href="members?memberId=${entry['id']}&memberAction=ALLMEMBEREVENTINFO">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_tblexport.png" alt="View Member Info" title="View Member Info" />
            </a>&nbsp;
            <a href="members?memberId=${entry['id']}&memberAction=PROPERTIES&propertyAction=GEMFIRE">
               <img class="icon" width="16" height="16" src="../themes/original/img/b_props.png" alt="View Member GemFire Properties" title="View Member GemFire Properties" />
            </a>&nbsp;
            <a href="members?memberId=${entry['id']}&memberAction=PROPERTIES&propertyAction=BOOT">
               <img class="icon" width="16" height="16" src="../themes/original/img/b_props.png" alt="View Member Boot Properties" title="View Member Boot Properties" />
            </a>&nbsp;
            <a href="members?memberId=${entry['id']}&memberAction=PROPERTIES&propertyAction=SYSTEM">
               <img class="icon" width="16" height="16" src="../themes/original/img/b_props.png" alt="View Member System Properties" title="View Member System Properties" />
            </a>&nbsp;
           </td>
  	   </tr>
	</c:forEach>
 </tbody>
</table>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>