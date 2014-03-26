
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
            if (document.getElementById("storename").value == "")
            {
                alert("Disk Store Name Cannot Be Empty");
                return false;
            }
            else
            {
                return true;
            }
        }
        //-->
    </SCRIPT>
    <title><fmt:message key="sqlfireweb.appname" /> Create Disk Store</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Create Disk Store</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    Disk stores provide disk storage for tables and queues that need to overflow or persist (for instance when using an asynchronous write-behind listener).
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
    <h3>Create Disk Store SQL </h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
        </tr>
        </tbody>
    </table>
    <br />
</c:if>

<h3>Disk Store Properties</h3>

<form:form action="creatediskstore" method="POST" modelAttribute="diskStoreAttribute" onsubmit="return validate()">
<table>
    <thead>
    <tr>
        <th colspan="3">Disk Store Properties</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
        <tr class="even">
            <td align="right">Disk Store Name</td>
            <td>
                <form:input type="text" path="diskStoreName" size="20" maxlength="30" id="storename" />
            </td>
            <td>(Required.) A unique identifier for the HDFS store configuration. </td>
        </tr>
        <tr class="odd">
            <td align="right">Max Log Size</td>
            <td>
                <form:input type="text" path="maxLogSize" size="20" maxlength="10" value="1" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum size in megabytes that the oplog can become before SQLFire automatically rolls to a new file</td>
        </tr>
        <tr class="even">
            <td align="right">Auto Compact</td>
            <td>
                <form:select path="autoCompact">
                    <c:choose>
                        <c:when test="${diskStoreAttribute.autoCompact  == 'FALSE'}">
                            <form:option value="FALSE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="FALSE" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${diskStoreAttribute.autoCompact == 'TRUE'}">
                            <form:option value="TRUE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="TRUE" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
            <td>Set this option to "true" (the default) to automatically compact disk files</td>
        </tr>
        <tr class="odd">
            <td align="right">Allow Force Compaction</td>
            <td>
                <form:select path="allowForceCompaction">
                    <c:choose>
                        <c:when test="${diskStoreAttribute.allowForceCompaction  == 'FALSE'}">
                            <form:option value="FALSE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="FALSE" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${diskStoreAttribute.allowForceCompaction == 'TRUE'}">
                            <form:option value="TRUE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="TRUE" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
            <td>Set this option to "true" to enable online compaction of oplog files using the sqlf utility</td>
        </tr>
        <tr class="even">
            <td align="right">Compaction Threshold</td>
            <td>
                <form:input type="text" path="compactionThreshold" size="20" maxlength="3" value="50" onkeypress="return isNumberKey(event);" />
            </td>
            <td>Sets the threshold for the amount of "garbage" data that can exist in the oplog before SQLFire initiates automatic compaction</td>
        </tr>
        <tr class="odd">
            <td align="right">Time Interval</td>
            <td>
                <form:input type="text" path="timeInterval" size="20" maxlength="400" value="1000" onkeypress="return isNumberKey(event);" />
            </td>
            <td>Sets the number of milliseconds that can elapse before SQLFire asynchronously flushes data to disk</td>
        </tr>
        <tr class="even">
            <td align="right">Write Buffer Size</td>
            <td>
                <form:input type="text" path="writeBufferSize" size="20" maxlength="400" value="32768" onkeypress="return isNumberKey(event);" />
            </td>
            <td>Sets the buffer size in bytes to use when persisting data to disk</td>
        </tr>
        <tr class="odd">
            <td align="right">Additional Parameters</td>
            <td>
                <form:input type="text" path="additionalParams" size="20" maxlength="800" />
            </td>
            <td>Eg: QUEUESIZE 17374 ('dir1' 456) </td>
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