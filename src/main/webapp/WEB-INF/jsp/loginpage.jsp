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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <style type="text/css">
        input:focus, textarea:focus {
            background-color: #D0DCE0;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><fmt:message key="sqlfireweb.appname" /> - Login Page</title>
    <link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
    <link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Login Page</h2>

<div class="notice">
    Supports Pivotal GemFireXD
</div>

<c:if test="${!empty error}">
    <br />
    <div class="error">
        Unable to login into GemFireXD cluster due to the following error <p /> ${error}
    </div>
</c:if>

<font color="darkblue">
    <p>
        Please ensure you provide a login schema in order to set the correct schema. Leaving this
        out will default to the "APP" schema.
    </p>
    <p>
        <img src="../themes/original/img/b_tipp.png" border="0" />
        To avoid using the login page auto login as follows -> GemFireXD-Web/autologin?url=jdbc:sqlfire://192.168.1.7:1527/&username=app&passwd=app
    </p>
</font>

<form:form method="post" action="login" modelAttribute="loginAttribute" target="_top">
    <fieldset>
        <legend>Simple Login Form</legend>
        <table class="formlayout">
            <tr>
                <td align="right">Username:</td>
                <td><form:input type="text" path="username" maxlength="30" size="60" value=""/></td>
            </tr>
            <tr>
                <td align="right">Password:</td>
                <td><form:input type="password" path="password" maxlength="30" size="60" value=""/></td>
            </tr>
            <tr>
                <td align="right">Url:</td>
                <td><form:input type="text" path="url" maxlength="450" size="60" value="jdbc:gemfirexd://localhost:1527/" /></td>
            </tr>
        </table>
    </fieldset>
    <fieldset class="tblFooters">
        <input type="submit" value="Login" />
    </fieldset>
</form:form>

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>