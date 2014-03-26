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
<link rel="stylesheet" type="text/css" href="../css/isqlfire.css" />
<link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
<script type="text/javascript">
  // <![CDATA[
  
  // js form validation stuff
  var confirmMsg  = 'Do you really want to ';
  // ]]>
</script>
<script src="../js/functions.js" type="text/javascript"></script>
<title><fmt:message key="sqlfireweb.appname" /> Async Event Listeners</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Async Event Listeners</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Found ${records} async event listener(s).
    <a href="createasync">
        <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Add Async Event Listener" title="Add Async Event Listener" />
        Create Async Event Listener
    </a>
</div>

<c:if test="${!empty allAsyncEventInfoResult}">
 <h3>${asyncevent} Async Event information</h3>
 <table id="table_results" class="data">
  <thead>
    <tr>
      <th>Column Name</th>
      <th>Value</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${allAsyncEventInfoResult.rows}">
        <c:forEach var="columnName" items="${allAsyncEventInfoResult.columnNames}">
         <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
          <td align="right">${columnName}</td>
          <td align="left"><c:out value="${row[columnName]}"/></td>
         </tr>
        </c:forEach>           
       </tr>
    </c:forEach>  
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

<c:if test="${!empty arrayresultddl}">
    <fieldset>
        <legend>Async Eevent Listener DDL</legend>
        <table id="table_results_ddl" class="data">
            <c:forEach var="result" items="${arrayresultddl}">
                <tr>
                    <td class="odd"><pre>${result}</pre></td>
                </tr>
            </c:forEach>
        </table>
    </fieldset>
    <br />
</c:if>

<c:if test="${!empty arrayresult}">
<fieldset>
 <legend>Multi Submit Results</legend>
 <table class="formlayout">
  <c:forEach var="result" items="${arrayresult}">
    <tr>
     <td align="right">Command:</td>
     <td> ${result.command} </td>
    </tr>
    <tr>
     <td align="right">Message:</td>
     <td> 
      <font color="${result.message == 'SUCCESS' ? 'green' : 'red'}">
        ${result.message}
      </font>
     </td>
    </tr>
  </c:forEach>
 </table>
</fieldset>
<br />
</c:if>

<c:if test="${!empty asyncTables}">
    <h3>Async Event Listener Tables for id ${asyncevent} </h3>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <c:forEach var="columnName" items="${asyncTables.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${asyncTables.rows}">
            <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
                <c:forEach var="columnName" items="${asyncTables.columnNames}">
                    <c:choose>
                        <c:when test="${columnName == 'DSID'}">
                            <td align="center">
                                <a href="members?memberId=${row[columnName]}&memberAction=ALLMEMBEREVENTINFO">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </td>
                        </c:when>
                        <c:when test="${columnName == 'TABLENAME'}">
                            <td align="center">
                                <a href="tableviewer?tabName=${row[columnName]}&selectedSchema=${row['SCHEMANAME']}">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </td>
                        </c:when>
                        <c:when test="${columnName == 'STATUS'}">
                            <td align="center">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'RUNNING'}">
                                        <font color="green">
                                            <c:out value="${row[columnName]}"/>
                                        </font>
                                    </c:when>
                                    <c:otherwise>
                                        <font color="red">
                                            <c:out value="${row[columnName]}"/>
                                        </font>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="center"><c:out value="${row[columnName]}"/></td>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty asyncTables.rows}">
        <i>No rows exist</i>
        <br />
    </c:if>
    <br />
</c:if>

<form action="asyncevent" method="POST">
   <b>Filter Async Event Name </b>
   <input type="TEXT" name="search" value="${search}" />
   <input type="image" src="../themes/original/img/Search.png" />
</form>

<!-- Display previous/next set links -->
<c:if test="${estimatedrecords > sessionScope.prefs.recordsToDisplay}"> &nbsp; | &nbsp;
  <c:if test="${startAtIndex != 0}">
    <a href="asyncevent?search=${param.search}&selectedSchema=${chosenSchema}&startAtIndex=${(startAtIndex - sessionScope.prefs.recordsToDisplay)}&endAtIndex=${startAtIndex}">
      <img src="../themes/original/img/Previous.png" border="0" />
    </a>
    &nbsp;
  </c:if>
  <c:if test="${estimatedrecords != endAtIndex}">
    <a href="asyncevent?search=${param.search}&selectedSchema=${chosenSchema}&startAtIndex=${endAtIndex}&endAtIndex=${endAtIndex + sessionScope.prefs.recordsToDisplay}">
      <img src="../themes/original/img/Next.png" border="0" />
    </a>
  </c:if>
  &nbsp; <font color="Purple">Current Set [${startAtIndex + 1} - ${endAtIndex}] </font>
</c:if>

<p />

<form method="post" action="asyncevent" name="tablesForm" id="tablesForm">
<table id="table_results" class="data">
 <thead>
   <tr>
    <th></th>
    <th>Name</th>
    <th>Listener Class</th>
    <th>Server Group</th>
    <th>Is Started?</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="entry" varStatus="loop" items="${asyncevents}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		  <td align="center">
		      <input type="checkbox" 
		             name="selected_async[]"
		             value="${entry['name']}"
		             id="checkbox_async_${loop.index + 1}" />
		   </td>
	  	   <td align="center">${entry.name}</td>
	  	   <td align="center">${entry.listenerClass}</td>
	  	   <td align="center">${entry.serverGroup}</td>
	  	   <td align="center">
	  	     <c:choose>
	  	       <c:when test="${entry.isStarted == '1'}">
	  	         <font color="green">YES</font>
	  	       </c:when>
	  	       <c:otherwise>
	  	         <font color="red">NO</font>
	  	       </c:otherwise>
	  	     </c:choose>
	  	   </td>
	  	   <td align="center">
    		<a href="asyncevent?asyncName=${entry['name']}&asyncAction=DROP" onclick="return confirmLink(this, 'DROP ASYNC EVENT LISTENER ${entry['name']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Drop Async Event Listener" title="Drop Async Event Listener" />
            </a>&nbsp;  
    		<a href="asyncevent?asyncName=${entry['name']}&asyncAction=START" onclick="return confirmLink(this, 'START ASYNC EVENT LISTENER ${entry['name']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/s_success.png" alt="Start Async Event Listener" title="Start Async Event Listener" />
            </a>&nbsp;   
    		<a href="asyncevent?asyncName=${entry['name']}&asyncAction=STOP" onclick="return confirmLink(this, 'STOP ASYNC EVENT LISTENER ${entry['name']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/s_error.png" alt="Stop Async Event Listener" title="Stop Async Event Listener" />
            </a>&nbsp; 
    		<a href="asyncevent?asyncName=${entry['name']}&asyncAction=ALLASYNCEVENTINFO">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_tblexport.png" alt="View Async Event Listener Info" title="View Async Event Listener Info" />
            </a>&nbsp;
               <a href="asyncevent?asyncName=${entry['name']}&asyncAction=ASYNCTABLES">
                   <img class="icon" width="16" height="16" src="../themes/original/img/b_views.png" alt="View Async Tables" title="View Async Tables" />
               </a>&nbsp;
           </td>
  	   </tr>
	</c:forEach>
 </tbody>
</table>

<div class="clearfloat">
<img class="selectallarrow" src="../themes/original/img/arrow_ltr.png"
    width="38" height="22" alt="With selected:" />
<a href="asyncevent"
    onclick="if (setCheckboxes('table_results', 'true')) return false;">
    Check All</a>

/
<a href="asyncevent"
    onclick="if (unMarkAllRows('tablesForm')) return false;">
    Uncheck All</a>

<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
    <option value="With selected:" selected="selected">With selected:</option>
    <option value="Drop" >Drop</option>
    <option value="DDL" >Generate DDL</option>
    <option value="DDL_FILE" >Generate DDL File</option>
</select>


<script type="text/javascript">
<!--
// Fake js to allow the use of the <noscript> tag
//-->
</script>
<noscript>
    <input type="submit" value="Go" />
</noscript>
</div>

</form>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>