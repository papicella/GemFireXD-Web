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
<title><fmt:message key="sqlfireweb.appname" /> Gateway Senders</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Gateway Senders</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Found ${records} gateway sender(s).
    <a href="creategatewaysender">
        <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Add Gateway Sender" title="Add Gateway Sender" />
        Create Gateway Sender
    </a>
</div>

<c:if test="${!empty allGatewaySenderInfoResult}">
 <h3>${gatewaysender} gateway sender information</h3>
 <table id="table_results" class="data">
  <thead>
    <tr>
      <th>Column Name</th>
      <th>Value</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${allGatewaySenderInfoResult.rows}">
        <c:forEach var="columnName" items="${allGatewaySenderInfoResult.columnNames}">
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

<c:if test="${!empty runningGatewaySenders}">
    <h3>Running ${gatewaysender} Information</h3>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <c:forEach var="columnName" items="${runningGatewaySenders.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${runningGatewaySenders.rows}">
            <tr class="odd">
                <c:forEach var="columnName" items="${runningGatewaySenders.columnNames}">
                    <c:choose>
                        <c:when test="${columnName == 'ID'}">
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
                        <c:otherwise>
                            <td align="center"><c:out value="${row[columnName]}"/></td>
                        </c:otherwise>
                    </c:choose>

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

<form action="gatewaysenders" method="POST">
   <b>Filter Gateway Sender Name </b>
   <input type="TEXT" name="search" value="${search}" />
   <input type="image" src="../themes/original/img/Search.png" />
</form>

<!-- Display previous/next set links -->
<c:if test="${estimatedrecords > sessionScope.prefs.recordsToDisplay}"> &nbsp; | &nbsp;
  <c:if test="${startAtIndex != 0}">
    <a href="gatewaysenders?search=${param.search}&startAtIndex=${(startAtIndex - sessionScope.prefs.recordsToDisplay)}&endAtIndex=${startAtIndex}">
      <img src="../themes/original/img/Previous.png" border="0" />
    </a>
    &nbsp;
  </c:if>
  <c:if test="${estimatedrecords != endAtIndex}">
    <a href="gatewaysenders?search=${param.search}&startAtIndex=${endAtIndex}&endAtIndex=${endAtIndex + sessionScope.prefs.recordsToDisplay}">
      <img src="../themes/original/img/Next.png" border="0" />
    </a>
  </c:if>
  &nbsp; <font color="Purple">Current Set [${startAtIndex + 1} - ${endAtIndex}] </font>
</c:if>

<p />

<form method="post" action="gatewaysenders" name="tablesForm" id="tablesForm">
<table id="table_results" class="data">
 <thead>
   <tr>
    <th></th>
    <th>Gateway Sender</th>
    <th>Remote DS Id</th>
    <th>Server Group</th>
    <th>Is Started?</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="entry" varStatus="loop" items="${gatewaysenders}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		  <td align="center">
		      <input type="checkbox" 
		             name="selected_gatewaysenders[]"
		             value="${entry['senderId']}"
		             id="checkbox_gatewaysenders_${loop.index + 1}" />
		   </td>
	  	   <td align="center">${entry.senderId}</td>
	  	   <td align="center">${entry.remoteDSId}</td>
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
    		<a href="gatewaysenders?senderId=${entry['senderId']}&gsAction=DROP" onclick="return confirmLink(this, 'DROP GATEWAY SENDER ${entry['senderId']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Drop Gateway Sender" title="Drop Gateway Sender" />
            </a>&nbsp;
    		<a href="gatewaysenders?senderId=${entry['senderId']}&gsAction=START" onclick="return confirmLink(this, 'START GATEWAY SENDER ${entry['senderId']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/s_success.png" alt="Start Gateway Sender" title="Start Gateway Sender" />
            </a>&nbsp;
    		<a href="gatewaysenders?senderId=${entry['senderId']}&gsAction=STOP" onclick="return confirmLink(this, 'STOP GATEWAY SENDER ${entry['senderId']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/s_error.png" alt="Stop Gateway Sender" title="Stop Gateway Sender" />
            </a>&nbsp; 
    		<a href="gatewaysenders?senderId=${entry['senderId']}&gsAction=ALLGATEWAYSENDERINFO">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_tblexport.png" alt="View Gateway Sender Info" title="View Gateway Sender Info" />
            </a>&nbsp;
            <a href="gatewaysenders?senderId=${entry['senderId']}&gsAction=RUNNINGGATEWAYSENDERS">
               <img class="icon" width="16" height="16" src="../themes/original/img/b_relations.png" alt="View Running Gateway Senders" title="View Running Gateway Senders" />
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
<a href="gatewaysenders"
    onclick="if (unMarkAllRows('tablesForm')) return false;">
    Uncheck All</a>

<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
    <option value="With selected:" selected="selected">With selected:</option>
    <option value="Drop" >Drop</option>
    <option value="Start" >Start</option>
    <option value="Stop" >Stop</option>
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