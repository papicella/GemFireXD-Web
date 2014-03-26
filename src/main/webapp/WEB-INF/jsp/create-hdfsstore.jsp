
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
                alert("Gateway Sender Name Cannot Be Empty");
                return false;
            }
            else
            {
                return true;
            }
        }
        //-->
    </SCRIPT>
    <title><fmt:message key="sqlfireweb.appname" /> Create HDFS Store</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Create HDFS Store</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    Creates a connection to a Hadoop name node in order to persist one or more tables to HDFS. Each connection defines the HDFS namenode and directory to use for persisting data, as well as GemFire XD-specific options to configure the queue used to persist table events, enable persistence for the connection, compact the HDFS operational logs, and so forth.
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
    <h3>Create HDFS Store SQL </h3>
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
        </tr>
        </tbody>
    </table>
    <br />
</c:if>

<h3>HDFS Store Properties</h3>

<form:form action="createhdfsstore" method="POST" modelAttribute="hdfsStoreAttribute" onsubmit="return validate()">
<table>
    <thead>
    <tr>
        <th colspan="3">HDFS Store Properties</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
        <tr class="even">
            <td align="right">Store Name</td>
            <td>
                <form:input type="text" path="storeName" size="20" maxlength="30" id="storename" />
            </td>
            <td>(Required.) A unique identifier for the HDFS store configuration. </td>
        </tr>
        <tr class="odd">
            <td align="right">Named Node</td>
            <td>
                <form:input type="text" path="nameNode" size="20" maxlength="400" value="hdfs://pivhdsne:8020" />
            </td>
            <td>The URL of the Hadoop NameNode for your Pivotal HD cluster (for example, hdfs://server-name:8020)</td>
        </tr>
        <tr class="even">
            <td align="right">Home Directory</td>
            <td>
                <form:input type="text" path="homeDir" size="20" maxlength="400" />
            </td>
            <td>The Hadoop directory in which you will persist GemFire XD tables</td>
        </tr>
        <tr class="odd">
            <td align="right">Batch Size</td>
            <td>
                <form:input type="text" path="batchSize" size="20" maxlength="10" value="5" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum size (in megabytes) of each batch that is written to the Hadoop directory</td>
        </tr>
        <tr class="odd">
            <td align="right">Batch Time Interval</td>
            <td>
                <form:input type="text" path="batchTimeInterval" size="20" maxlength="400" value="5000" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum number of milliseconds that can elapse between writing batches to HDFS</td>
        </tr>
        <tr class="even">
            <td align="right">Max Queue Memory</td>
            <td>
                <form:input type="text" path="maxQueueMemory" size="20" maxlength="10" value="100" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum amount of memory in megabytes that the queue can consume before overflowing to disk</td>
        </tr>
        <tr class="odd">
            <td align="right">Minor Compact</td>
            <td>
                <form:select path="minorCompact">
                    <c:choose>
                        <c:when test="${hdfsStoreAttribute.minorCompact  == 'FALSE'}">
                            <form:option value="FALSE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="FALSE" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${hdfsStoreAttribute.minorCompact == 'TRUE'}">
                            <form:option value="TRUE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="TRUE" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
            <td>Specify TRUE to enable automatic minor compaction for the HDFS read/write log files</td>
        </tr>
        <tr class="even">
            <td align="right">Max Input File Size</td>
            <td>
                <form:input type="text" path="maxInputFileSize" size="20" maxlength="100" value="512" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum amount of memory in megabytes that the queue can consume before overflowing to disk</td>
        </tr>
        <tr class="odd">
            <td align="right">Min Input File Count</td>
            <td>
                <form:input type="text" path="minInputFileCount" size="20" maxlength="20" value="3" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The minimum number of input files that can be created before GemFire XD begins automatically compacting files</td>
        </tr>
        <tr class="even">
            <td align="right">Max Input File Count</td>
            <td>
                <form:input type="text" path="maxInputFileCount" size="20" maxlength="20" value="10" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum number of input files to include in a minor compaction cycle</td>
        </tr>
        <tr class="odd">
            <td align="right">Minor Compaction Threads</td>
            <td>
                <form:input type="text" path="minorCompactionThreads" size="20" maxlength="20" value="10" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum number of threads that GemFire XD uses to perform minor compaction in this HDFS store</td>
        </tr>
        <tr class="even">
            <td align="right">Major Compact</td>
            <td>
                <form:select path="majorCompact">
                    <c:choose>
                        <c:when test="${hdfsStoreAttribute.majorCompact  == 'FALSE'}">
                            <form:option value="FALSE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="FALSE" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${hdfsStoreAttribute.majorCompact == 'TRUE'}">
                            <form:option value="TRUE" selected="true" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="TRUE" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
            <td>Specify TRUE to enable automatic major compaction for the HDFS read/write log files</td>
        </tr>
        <tr class="odd">
            <td align="right">Major Compaction Interval</td>
            <td>
                <form:input type="text" path="majorCompactionInterval" size="20" maxlength="20" value="720" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The amount of time (in minutes) after which GemFire XD performs the next major compaction cycle</td>
        </tr>
        <tr class="even">
            <td align="right">Major Compaction Threads</td>
            <td>
                <form:input type="text" path="majorCompactionThreads" size="20" maxlength="20" value="2" onkeypress="return isNumberKey(event);" />
            </td>
            <td>The maximum number of threads that GemFire XD uses to perform major compaction in this HDFS store</td>
        </tr>
        <tr class="even">
            <td align="right">Max Write Only File Size</td>
            <td>
                <form:input type="text" path="maxWriteOnlyFileSize" size="20" maxlength="40" value="256" onkeypress="return isNumberKey(event);" />
            </td>
            <td>For HDFS write-only tables, this defines the maximum size (in megabytes) that an HDFS log file can reach before GemFire XD closes the file and begins writing to a new file</td>
        </tr>
        <tr class="even">
            <td align="right">Write Only Rollover Interval</td>
            <td>
                <form:input type="text" path="writeOnlyRolloverInterval" size="20" maxlength="100" value="3600" onkeypress="return isNumberKey(event);" />
            </td>
            <td>For HDFS write-only tables, this defines the maximum time (in minutes) that can elapse before GemFire XD closes an HDFS log file and begins writing to a new file</td>
        </tr>
        <tr class="odd">
            <td align="right">Additional Parameters</td>
            <td>
                <form:input type="text" path="additionalParams" size="20" maxlength="800" />
            </td>
            <td>Eg: QUEUEPERSISTENT true etc...</td>
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