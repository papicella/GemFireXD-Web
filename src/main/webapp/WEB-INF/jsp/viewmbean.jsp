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
    <c:if test="${!empty refreshTime}">
        <c:if test="${refreshTime != 'none'}">
            <meta http-equiv="refresh" content="${refreshTime}">
        </c:if>
    </c:if>
    <link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
    <link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
    <script type="text/javascript">
        // <![CDATA[

        // js form validation stuff
        var confirmMsg  = 'Do you really want to ';
        // ]]>
    </script>
    <script src="../js/functions.js" type="text/javascript"></script>
    <title><fmt:message key="sqlfireweb.appname" /> GemFireXD MBean Viewer</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> GemFireXD MBean Viewer</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<h3>${mbeanName}</h3>

<div class="notice">
    Found ${records} Attribute(s).
</div>
<br />

<!-- Add Refresh here -->
<form method="get" action="jmxmbeans">
    <input type="hidden" name="mbeanName" value="${mbeanName}" />
    Refresh :
    <select name="refreshTime">
        <c:choose>
            <c:when test="${refreshTime == 'none'}">
                <option value="none" selected="selected">none</option>
            </c:when>
            <c:otherwise>
                <option value="none">none</option>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${refreshTime == '10'}">
                <option value="10" selected="selected">10 Seconds</option>
            </c:when>
            <c:otherwise>
                <option value="10">10 Seconds</option>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${refreshTime == '20'}">
                <option value="20" selected="selected">20 Seconds</option>
            </c:when>
            <c:otherwise>
                <option value="20">20 Seconds</option>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${refreshTime == '30'}">
                <option value="30" selected="selected">30 Seconds</option>
            </c:when>
            <c:otherwise>
                <option value="30">30 Seconds</option>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${refreshTime == '60'}">
                <option value="60" selected="selected">60 Seconds</option>
            </c:when>
            <c:otherwise>
                <option value="60">60 Seconds</option>
            </c:otherwise>
        </c:choose>
    </select>
    &nbsp;
    <input type="submit" value="Auto Refresh" name="pSubmit" />
</form>
<br />
<br />

<table id="table_results" class="data">
    <thead>
    <tr>
        <th>Attribute Name</th>
        <th>Value</th>
    </tr>
    </thead>
    <tbody>
        <c:forEach var="entry" varStatus="loop" items="${mbeanMap}">
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                <td align="right">${entry.key}</td>
                <td align="left">${entry.value}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>

</body>
</html>

