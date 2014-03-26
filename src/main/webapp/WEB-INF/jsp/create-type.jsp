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
            if (document.getElementById("typeName").value == "")
            {
                alert("Type Name Cannot Be Empty");
                return false;
            }
            else if (document.getElementById("javaClassName").value == "")
            {
                alert("Java Class Name Cannot Be Empty");
                return false;
            }
            else
            {
                return true;
            }
        }
        //-->
    </SCRIPT>
    <title><fmt:message key="sqlfireweb.appname" /> Create Type</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Create Type</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    The CREATE TYPE statement creates a user-defined type (UDT). A UDT is a serializable Java class whose instances are stored in columns. The class must implement the java.io.Serializable interface.
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
    <h3>Create Type SQL </h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
        </tr>
        </tbody>
    </table>
    <br />
</c:if>

<h3>UDT (Type) Properties</h3>

<form:form action="createtype" method="POST" modelAttribute="typeAttribute" onsubmit="return validate()">
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
            <td align="right">UDT (Type) Name</td>
            <td>
                <form:input type="text" path="typeName" size="20" maxlength="100" id="typeName" />
            </td>
            <td>(Required) UDT (Type) Name</td>
        </tr>
        <tr class="even">
            <td align="right">External Name</td>
            <td>
                <form:input type="text" path="javaClassName" size="20" maxlength="100" id ="javaClassName" />
            </td>
            <td>(Required) Java Class name which must implement java.io.Serializable</td>
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