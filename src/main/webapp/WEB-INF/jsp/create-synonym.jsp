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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
            if (document.getElementById("synonymName").value == "")
            {
                alert("Synonym Name Cannot Be Empty");
                return false;
            }
            else
            {
                return true;
            }
        }
        //-->
    </SCRIPT>
    <title><fmt:message key="sqlfireweb.appname" /> Create Synonym</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Create Synonym</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    Provide an alternate name for a table or view.
</p>

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
</c:if>

<c:if test="${!empty sql}">
    <div class="success">
        Successfully generated SQL
    </div>
    <h3>Create Synonym SQL </h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
        </tr>
        </tbody>
    </table>
    <br />
</c:if>

<h3>Synonym Properties</h3>

<form:form action="createsynonym" method="POST" modelAttribute="synonymAttribute" onsubmit="return validate()">
    <table>
        <thead>
        <tr>
            <th colspan="3">UDT (Type) Properties</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
        <tr class="even">
            <td align="right">Schema Name</td>
            <td>
                <form:input type="text" path="schemaName" size="20" maxlength="30" value="${sessionScope.schema}"  />
            </td>
            <td>Optional Schema Name, if omitted default to connected Schema</td>
        </tr>
        <tr class="odd">
            <td align="right">Synonym Name</td>
            <td>
                <form:input type="text" path="synonymName" size="20" maxlength="30"  />
            </td>
            <td>Required : Synonym Name</td>
        </tr>
        <tr class="even">
            <td align="right">For Table/View</td>
            <td>
                <form:select path="objectName">
                    <c:forEach var="entry" varStatus="loop" items="${tables}">
                        <c:set var="curvalue">
                            ${entry['schemaName']}.${entry['tableName']}
                        </c:set>
                        <c:choose>
                            <c:when test="${curvalue == objectName}">
                                <option value="${entry['schemaName']}.${entry['tableName']}" selected="selected">${entry['schemaName']}.${entry['tableName']} - TABLE</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${entry['schemaName']}.${entry['tableName']}">${entry['schemaName']}.${entry['tableName']} - TABLE</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:forEach var="entry" varStatus="loop" items="${views}">
                        <c:set var="curvalue">
                            ${entry['schemaName']}.${entry['viewName']}
                        </c:set>
                        <c:choose>
                            <c:when test="${curvalue == objectName}">
                                <option value="${entry['schemaName']}.${entry['viewName']}" selected="selected">${entry['schemaName']}.${entry['viewName']} - VIEW</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${entry['schemaName']}.${entry['viewName']}">${entry['schemaName']}.${entry['viewName']} - VIEW</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
            </td>
            <td>Table Name or View name</td>
        </tr>
        </tbody>
    </table>
    <br />
    <input type="submit" value="Create" name="pSubmit" />
    <input type="submit" value="Show SQL" name="pSubmit" />
    <input type="submit" value="Save to File" name="pSubmit" />
</form:form>

<br />

<jsp:include page="footer.jsp" flush="true" />


</body>
</html>