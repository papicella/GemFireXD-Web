
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
            if (document.getElementById("triggerName").value == "")
            {
                alert("Trigger Name Cannot Be Empty");
                return false;
            }
            else
            {
                return true;
            }
        }
        //-->
    </SCRIPT>
    <title><fmt:message key="sqlfireweb.appname" /> Create Trigger</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Create Trigger</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    A trigger defines a set of actions that are executed when a delete, insert, or update operation is performed on a table. For example, if you define a trigger for a delete on a particular table, the trigger's action occurs whenever someone deletes a row or rows from the table.
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
    <h3>Create Trigger SQL </h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
        </tr>
        </tbody>
    </table>
</c:if>

<h3>Trigger Properties</h3>

<form:form action="createtrigger" method="POST" modelAttribute="triggerAttribute" onsubmit="return validate()">
<table>
    <thead>
    <tr>
        <th colspan="3">Trigger Properties</th>
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
            <td align="right">Trigger Name</td>
            <td>
                <form:input type="text" path="triggerName" size="20" maxlength="100" id="triggerName" />
            </td>
            <td>(Required) Trigger Name</td>
        </tr>
        <tr class="even">
            <td align="right">Before or After</td>
            <td>
                <form:select path="beforeOrAfter">
                    <c:choose>
                        <c:when test="${triggerAttribute.beforeOrAfter == 'AFTER'}">
                            <form:option selected="true" value="AFTER" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="AFTER" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${triggerAttribute.beforeOrAfter == 'NO CASCADE BEFORE'}">
                            <form:option selected="true" value="NO CASCADE BEFORE" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="NO CASCADE BEFORE" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
            <td>Triggers can be defined as either Before or After triggers</td>
        </tr>
        <tr class="odd">
            <td align="right">Trigger Event</td>
            <td>
                <form:select path="type" >
                    <c:choose>
                        <c:when test="${triggerAttribute.type == 'INSERT'}">
                            <form:option selected="true" value="INSERT" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="INSERT" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${triggerAttribute.type == 'UPDATE'}">
                            <form:option selected="true" value="UPDATE" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="UPDATE" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${triggerAttribute.type == 'DELETE'}">
                            <form:option selected="true" value="DELETE" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="DELETE" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
            <td>What triggering DML event, for UPDATE events you can optionally supply column names</td>
        </tr>
        <tr class="even">
            <td align="right">Update Column List</td>
            <td>
                <form:input type="text" path="columnList" size="20" maxlength="200" />
            </td>
            <td>IF UPDATE event optionally provide list of column names</td>
        </tr>
        <tr class="odd">
            <td align="right">On Table</td>
            <td>
                <form:select path="tableName">
                    <c:forEach var="entry" varStatus="loop" items="${tables}">
                        <c:set var="curvalue">
                            ${entry['schemaName']}.${entry['tableName']}
                        </c:set>
                        <c:choose>
                            <c:when test="${curvalue == tableName}">
                                <option value="${entry['schemaName']}.${entry['tableName']}" selected="selected">${entry['schemaName']}.${entry['tableName']}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${entry['schemaName']}.${entry['tableName']}">${entry['schemaName']}.${entry['tableName']}</option>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>
                </form:select>
            </td>
            <td>Table Name</td>
        </tr>
        <tr class="even">
            <td align="right">Referencing (OLD)</td>
            <td>
                <form:input type="text" path="referencingOldClause" size="20" maxlength="100" />
            </td>
            <td>Reference key for OLD values</td>
        </tr>
        <tr class="odd">
            <td align="right">Referencing (NEW)</td>
            <td>
                <form:input type="text" path="referencingNewClause" size="20" maxlength="100" />
            </td>
            <td>Reference key for NEW values</td>
        </tr>
        <tr class="even">
            <td align="right">For Each Row</td>
            <td>
                <form:select path="forEachRow">
                    <c:choose>
                        <c:when test="${triggerAttribute.forEachRow == 'FOR EACH ROW'}">
                            <form:option selected="true" value="FOR EACH ROW" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="FOR EACH ROW" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${triggerAttribute.forEachRow == 'FOR EACH ROW MODE DB2SQL'}">
                            <form:option selected="true" value="FOR EACH ROW MODE DB2SQL" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="FOR EACH ROW MODE DB2SQL" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
            <td>What triggering DML event, for UPDATE events you can optionally supply column names</td>
        </tr>
        <tr class="even">
            <td align="center" colspan="3">Triggering SQL statement</td>      
        </tr>
        <tr class="odd">
            <td align="center" colspan="3">
                <form:textarea path="triggerDefinition" cols="115" rows="10"></form:textarea>
            </td>
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