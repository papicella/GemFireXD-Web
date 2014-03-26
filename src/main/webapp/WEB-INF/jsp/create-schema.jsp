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
            if (document.getElementById("schemaName").value == "")
            {
                alert("Schema Name Cannot Be Empty");
                return false;
            }
            else
            {
                return true;
            }
        }
        //-->
    </SCRIPT>
    <title><fmt:message key="sqlfireweb.appname" /> Create Schema</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Create Schema</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    This creates a schema with the given name which provides a mechanism to logically group objects by providing a namespace for objects. This can then be used by other CREATE statements as the namespace prefix. For example, CREATE TABLE SCHEMA1.TABLE1 ( ... ) will create a table TABLE1 in the schema SCHEMA1. The DEFAULT SERVER GROUPS for a schema specifies the server groups used by the CREATE TABLE statement by default when no explicit server groups have been mentioned.
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
    <h3>Create Schema SQL </h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
        </tr>
        </tbody>
    </table>
    <br />
</c:if>

<h3>Schema Properties</h3>

<form:form action="createschema" method="POST" modelAttribute="schemaAttribute">
    <table>
        <thead>
        <tr>
            <th colspan="3">Schema Properties</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
        <tr class="even">
            <td align="right">Schema Name</td>
            <td>
                <form:input type="text" path="schemaName" size="20" maxlength="30"  />
            </td>
            <td>Optional Schema Name</td>
        </tr>
        <tr class="odd">
            <td align="right">Authorization Schema</td>
            <td>
                <form:select path="authorizationSchema">
                    <form:option value="" />
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
                </form:select>
            </td>
            <td>AUTHORIZATION user-name with a user-name other than the current user name</td>
        </tr>
        <tr class="even">
            <td align="right">Server Groups</td>
            <td>
                <form:input type="text" path="serverGroups" size="20" maxlength="200"  />
            </td>
            <td>Optional : Default server groups, Eg: servergroup1,servergroup2</td>
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