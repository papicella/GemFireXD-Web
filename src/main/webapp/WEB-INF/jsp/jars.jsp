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
    <title><fmt:message key="sqlfireweb.appname" /> - Jars</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Jars</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
    Found ${records} Jar(s).
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

<form action="jars" method="POST">
    <b>Filter Alias/Jar Name: </b>
    <input type="TEXT" name="search" value="${search}" />
    <input type="image" src="../themes/original/img/Search.png" />
</form>

<!-- Display previous/next set links -->
<c:if test="${estimatedrecords > sessionScope.prefs.recordsToDisplay}"> &nbsp; | &nbsp;
    <c:if test="${startAtIndex != 0}">
        <a href="jars?search=${param.search}&startAtIndex=${(startAtIndex - sessionScope.prefs.recordsToDisplay)}&endAtIndex=${startAtIndex}">
            <img src="../themes/original/img/Previous.png" border="0" />
        </a>
        &nbsp;
    </c:if>
    <c:if test="${estimatedrecords != endAtIndex}">
        <a href="jars?search=${param.search}&startAtIndex=${endAtIndex}&endAtIndex=${endAtIndex + sessionScope.prefs.recordsToDisplay}">
            <img src="../themes/original/img/Next.png" border="0" />
        </a>
    </c:if>
    &nbsp; <font color="Purple">Current Set [${startAtIndex + 1} - ${endAtIndex}] </font>
</c:if>

<p />

<form method="post" action="jars" name="tablesForm" id="tablesForm">
    <table id="table_results" class="data">
        <thead>
        <tr>
            <th>Id</th>
            <th>Schema Name</th>
            <th>Alias</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" varStatus="loop" items="${jars}">
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                <td align="center">${entry.id}</td>
                <td align="center">${entry.schemaName}</td>
                <td align="center">${entry.alias}</td>
                <td align="center">
                    <a href="jars?schemaName=${entry['schemaName']}&alias=${entry['alias']}&jarAction=REMOVE" onclick="return confirmLink(this, 'REMOVE JAR ${entry['alias']}?')">
                        <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Remove JAR" title="Remove JAR" />
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</form>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>

</body>
</html>
