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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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

      function validate(numcolumns)
      {

          for (var i = 1; i <= numcolumns; i++)
          {
              if (document.getElementById("column_name_" + i).value == "")
              {
                  alert("Table Column Name Cannot Be Empty");
                  return false;
              }
          }

          if (document.getElementById("tableName").value == "")
          {
              alert("Table Name Cannot Be Empty");
              return false;
          }
          else if (document.getElementById("schemaName").value == "")
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
<title><fmt:message key="sqlfireweb.appname" /> Create Table</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Create Table</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<p>
    Creates a new table using GemFire XD features.
</p>

<c:if test="${!empty sql}">
    <div class="success">
        Successfully generated SQL
    </div>
    <h3>Create table SQL </h3>
    <a href="javascript:history.back()">Return to Ensure Table Column Selection Not Lost</a>
    <p />
    <table id="table_results" class="data">
        <tbody>
        <tr class="odd">
            <td><pre>${sql}</pre></td>
        </tr>
        </tbody>
    </table>
    <br />
</c:if>

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

<form:form action="createtable" method="POST" modelAttribute="tableAttribute" onsubmit="return validate(${numColumns})">
Schema:
<form:input type="TEXT" path="schemaName" id="schemaName" />
Table Name:
<form:input type="TEXT" path="tableName" id="tableName" />
&nbsp; - &nbsp; Add 
<input type="TEXT" name="numColumns" size="4" value="1" onkeypress="return isNumberKey(event);" />
<input type="submit" name="pSubmit" value="Column(s)" />

<p />
<h3>Table Columns</h3>

<c:if test="${numColumns = 0}">
    <i> No table columns exists , at least one is required </i>
</c:if>

<c:if test="${numColumns > 0}">
	 <table id="table_columns" class="data">
	 <thead>
	   <tr>
	    <th>Name</th>
	    <th>Type</th>
	    <th>Precision</th>
	    <th>Default</th>
	    <th>Not Null?</th>
	    <th>PK?</th>
	    <th>Auto Inc?</th>
	   </tr>
	 </thead>
	  <c:forEach var="row" varStatus="loop" begin="1" end="${numColumns}" step="1">
	   <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">	  
		 <td><input type="text" id="column_name_${row}" name="column_name[]" size="15" maxlength="30" /></td>
		 <td>
		   <select name="column_type[]">
		     <option value="INT">INT</option>
		     <option value="BIGINT">BIGINT</option>
		     <option value="BLOB">BLOB</option>
		     <option value="CHAR">CHAR</option>
		     <option value="CHAR FOR BIT DATA">CHAR FOR BIT DATA</option>
		     <option value="CLOB">CLOB</option>
		     <option value="DATE">DATE</option>
		     <option value="DECIMAL">DECIMAL</option>
		     <option value="DOUBLE">DOUBLE</option>
		     <option value="DOUBLE PRECISION">DOUBLE PRECISION</option>
		     <option value="FLOAT">FLOAT</option>
		     <option value="INTEGER">INTEGER</option>
		     <option value="LONG VARCHAR">LONG VARCHAR</option>
		     <option value="LONG VARCHAR FOR BIT DATA">LONG VARCHAR FOR BIT DATA</option>
		     <option value="NUMERIC">NUMERIC</option>
		     <option value="REAL">REAL</option>
		     <option value="SMALLINT">SMALLINT</option>
		     <option value="TIME">TIME</option>
		     <option value="TIMESTAMP">TIMESTAMP</option>
		     <option value="VARCHAR">VARCHAR</option>
		     <option value="VARCHAR FOR BIT DATA">VARCHAR FOR BIT DATA</option>
		     <option value="XML">XML</option>
		     <!-- user defined types go here -->
		     <c:forEach var="entry" varStatus="loop" items="${types}">
		       <option value="${entry.typeName}">${entry.typeName}</option>
		     </c:forEach>
		   </select>
		 </td>
		 <td><input type="text" name="column_precision[]" size="5" maxlength="30" onkeypress="return isNumberKey(event);"/></td>
		 <td><input type="text" name="column_default_value[]" size="10" maxlength="30" /></td>
		 <td>
		   <select name="column_selected_null[]">
		     <option value="N" selected="selected">N</option>
		     <option value="Y">Y</option>
		   </select>
		 </td>
		 <td>
		   <select name="column_selected_primary_key[]">
		     <option value="N" selected="selected">N</option>
		     <option value="Y">Y</option>
		   </select>
		 </td> 
		 <td>
		   <select name="column_selected_auto_increment[]">
		     <option value="N" selected="selected">N</option>
		     <option value="Y">Y</option>
		   </select>
		 </td> 
	   </tr>
	  </c:forEach>
 </c:if>

</table>
<br />
<input type="submit" value="Create" name="pSubmit" />
<input type="submit" value="Show SQL" name="pSubmit" />
<input type="submit" value="Save to File" name="pSubmit" />

<h3>Additional Table Properties</h3>
<table>
<thead>
<tr>
  <th colspan="3">Additional Table Properties</th>
</tr>
</thead>
<tbody>
<tr>
   <td colspan="3" ALIGN="center">
       <b>Local Disk Store Table Persistence Properties</b>
   </td>
</tr>
<tr class="even">
 <td align="right">Disk Store</td>
 <td>
   <form:select path="diskStore">
     <option value=""></option>
     <c:forEach var="entry" varStatus="loop" items="${diskstores}">
         <c:set var="curvalue">${entry['name']}</c:set>
         <c:choose>
             <c:when test="${curvalue == diskStore}">
                 <option value="${entry['name']}" selected="selected">${entry['name']}</option>
             </c:when>
             <c:otherwise>
                 <option value="${entry['name']}">${entry['name']}</option>
             </c:otherwise>
         </c:choose>
     </c:forEach>
   </form:select>
 </td>
 <td>Leave blank to use default DiskStore</td>
</tr>
<tr class="odd">
 <td align="right">Persistent?</td>
 <td>
   <form:select path="persistant">
       <c:choose>
           <c:when test="${tableAttribute.persistant == 'N'}">
               <form:option selected="true" value="N" />
           </c:when>
           <c:otherwise>
               <form:option value="N" />
           </c:otherwise>
       </c:choose>
       <c:choose>
           <c:when test="${tableAttribute.persistant == 'Y'}">
               <form:option selected="true" value="Y" />
           </c:when>
           <c:otherwise>
               <form:option value="Y" />
           </c:otherwise>
       </c:choose>
   </form:select>
 </td>
 <td>Persist data to disk</td>
</tr>
<tr class="even">
 <td align="right">Persistence Type</td>
 <td>
   <form:select path="persistenceType">
       <form:option value=""></form:option>
       <c:choose>
           <c:when test="${tableAttribute.persistenceType == 'ASYNCHRONOUS'}">
               <form:option selected="true" value="ASYNCHRONOUS" />
           </c:when>
           <c:otherwise>
               <form:option value="ASYNCHRONOUS" />
           </c:otherwise>
       </c:choose>
       <c:choose>
           <c:when test="${tableAttribute.persistenceType == 'SYNCHRONOUS'}">
               <form:option selected="true" value="SYNCHRONOUS" />
           </c:when>
           <c:otherwise>
               <form:option value="SYNCHRONOUS" />
           </c:otherwise>
       </c:choose>
   </form:select>
 </td>
 <td>How data should be written to the DiskStore</td>
</tr>
<tr>
   <td colspan="3" ALIGN="center">
       <b>HDFS Table Persistence Properties</b>
   </td>
</tr>
<tr class="odd">
   <td align="right">HDFS Store</td>
   <td>
       <form:select path="hdfsStore">
           <option value=""></option>
           <c:forEach var="entry" varStatus="loop" items="${hdfsstores}">
               <c:set var="curvalue">${entry['name']}</c:set>
               <c:choose>
                   <c:when test="${curvalue == hdfsStore}">
                       <option value="${entry['name']}" selected="selected">${entry['name']}</option>
                   </c:when>
                   <c:otherwise>
                       <option value="${entry['name']}">${entry['name']}</option>
                   </c:otherwise>
               </c:choose>
           </c:forEach>
       </form:select>
   </td>
   <td>Specify a HDFS Store to use HDFS persistence</td>
</tr>
<tr class="even">
   <td align="right">Write Only (HDFS) ?</td>
   <td>
       <form:select path="writeonly">
           <c:choose>
               <c:when test="${tableAttribute.writeonly == 'N'}">
                   <form:option selected="true" value="N" />
               </c:when>
               <c:otherwise>
                   <form:option value="N" />
               </c:otherwise>
           </c:choose>
           <c:choose>
               <c:when test="${tableAttribute.writeonly == 'Y'}">
                   <form:option selected="true" value="Y" />
               </c:when>
               <c:otherwise>
                   <form:option value="Y" />
               </c:otherwise>
           </c:choose>
       </form:select>
   </td>
   <td>Include the WRITEONLY option to use the HDFS write-only model</td>
</tr>
<tr class="odd">
   <td align="right">Eviction By Criteria</td>
   <td>
       <form:input type="text" path="evictionbycriteria" size="20" maxlength="400" />
   </td>
   <td>Eg: mycolumn = 'archive'</td>
</tr>
<tr class="even">
   <td align="right">Eviction Frequency</td>
   <td>
       <form:input type="text" path="evictionfrequency" size="20" maxlength="400" />
   </td>
   <td>360 { SECONDS | HOURS | DAYS }</td>
</tr>
<tr class="odd">
   <td align="right">Evict Incoming?</td>
   <td>
       <form:select path="evictincoming">
           <c:choose>
               <c:when test="${tableAttribute.evictincoming == 'N'}">
                   <form:option selected="true" value="N" />
               </c:when>
               <c:otherwise>
                   <form:option value="N" />
               </c:otherwise>
           </c:choose>
           <c:choose>
               <c:when test="${tableAttribute.evictincoming == 'Y'}">
                   <form:option selected="true" value="Y" />
               </c:when>
               <c:otherwise>
                   <form:option value="Y" />
               </c:otherwise>
           </c:choose>
       </form:select>
   </td>
   <td>Evict Data Immediately on criteria eviction clause</td>
</tr>
<tr>
   <td colspan="3" ALIGN="center">
       <b>Other Table Properties</b>
   </td>
</tr>
<tr class="even">
   <td align="right">Policy</td>
   <td>
       <form:select path="dataPolicy">
           <c:choose>
               <c:when test="${tableAttribute.dataPolicy == 'REPLICATE'}">
                   <form:option selected="true" value="REPLICATE" />
               </c:when>
               <c:otherwise>
                   <form:option value="REPLICATE" />
               </c:otherwise>
           </c:choose>
           <c:choose>
               <c:when test="${tableAttribute.dataPolicy == 'PARTITION'}">
                   <form:option selected="true" value="PARTITION" />
               </c:when>
               <c:otherwise>
                   <form:option value="PARTITION" />
               </c:otherwise>
           </c:choose>
       </form:select>
   </td>
   <td>&nbsp;</td>
</tr>
<tr class="odd">
 <td align="right">Server Groups</td>
 <td>
   <form:input type="text" path="serverGroups" size="20" maxlength="100" />
 </td>
 <td>Eg: MYGROUP or OrdersDB, OrdersReplicationGrp</td>
</tr>
<tr class="even">
 <td align="right">Partition By</td>
 <td>
   <form:input type="text" path="partitionBy" size="20" maxlength="200" />
 </td>
 <td>Eg: (employee_id) or PRIMARY KEY etc</td>
</tr>
<tr class="odd">
 <td align="right">Colocate With</td>
 <td>
   <form:input type="text" path="colocateWith" size="20" maxlength="50" />
 </td>
 <td>Eg: EMP</td>
</tr>
<tr class="even">
 <td align="right">Redundancy</td>
 <td>
   <form:input type="text" path="redundancy" size="20" maxlength="1" onkeypress="return isNumberKey(event);"/>
 </td>
 <td>Eg: 2</td>
</tr>
<tr class="odd">
   <td align="right">Gateway Sender</td>
   <td>
       <form:input type="text" path="gatewaysender" size="20" maxlength="200" />
   </td>
   <td>Eg: TEST_SENDER1, TEST_SENDER2 or TEST_SENDER</td>
</tr>
<tr class="even">
   <td align="right">Async Event Listener</td>
   <td>
       <form:input type="text" path="asynceventlistener" size="20" maxlength="200" />
   </td>
   <td>Eg: TEST_LISTENER1, TEST_LISTENER2 or TEST_LISTENER</td>
</tr>
<tr class="odd">
   <td align="right">Offheap?</td>
   <td>
       <form:select path="offheap">
           <c:choose>
               <c:when test="${tableAttribute.offheap == 'N'}">
                   <form:option selected="true" value="N" />
               </c:when>
               <c:otherwise>
                   <form:option value="N" />
               </c:otherwise>
           </c:choose>
           <c:choose>
               <c:when test="${tableAttribute.offheap == 'Y'}">
                   <form:option selected="true" value="Y" />
               </c:when>
               <c:otherwise>
                   <form:option value="Y" />
               </c:otherwise>
           </c:choose>
       </form:select>
   </td>
   <td>Store the data outside of the JVM heap</td>
</tr>
<tr class="even">
 <td align="right">Additional Parameters</td>
 <td>
   <form:input type="text" path="other" size="20" maxlength="400" />
 </td>
 <td>Eg: EVICTION BY LRUCOUNT 5 EVICTACTION OVERFLOW</td>
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