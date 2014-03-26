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
<title><fmt:message key="sqlfireweb.appname" /> - Triggers</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Triggers</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Found ${records} trigger(s).
    <a href="createtrigger">
        <img class="icon" width="16" height="16" src="../themes/original/img/add16.gif" alt="Add Trigger" title="Add Trigger " />
        Create Trigger
    </a>
</div>

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

<c:if test="${!empty allTriggerInfoResult}">
 <h3>${triggername} trigger information</h3>
 <table id="table_results" class="data">
  <thead>
    <tr>
      <th>Column Name</th>
      <th>Value</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="row" varStatus="loop" items="${allTriggerInfoResult.rows}">
        <c:forEach var="columnName" items="${allTriggerInfoResult.columnNames}">
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

<c:if test="${!empty tableTriggersResult}">
    <h3>${triggerName} Firing TABLE </h3>
    <table id="table_results" class="data">
        <thead>
        <tr>
            <c:forEach var="columnName" items="${tableTriggersResult.columnNames}">
                <th><c:out value="${columnName}"/></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" varStatus="loop" items="${tableTriggersResult.rows}">
            <tr class="odd">
                <c:forEach var="columnName" items="${tableTriggersResult.columnNames}">
                    <td align="center">
                        <c:choose>
                            <c:when test="${columnName == 'TRIGGERNAME'}">
                                <a href="triggers?triggerId=${row['TRIGGERID']}&triggerName=${row[columnName]}&selectedSchema=${chosenSchema}&triggerAction=ALLTRIGGERINFO">
                                    <c:out value="${row[columnName]}"/>
                                </a>
                            </c:when>
                            <c:when test="${columnName == 'EVENT'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'U'}">
                                        Update
                                    </c:when>
                                    <c:when test="${row[columnName] == 'D'}">
                                        Delete
                                    </c:when>
                                    <c:when test="${row[columnName] == 'I'}">
                                        Insert
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.event}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${columnName == 'FIRINGTIME'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'B'}">
                                        Before
                                    </c:when>
                                    <c:when test="${row[columnName] == 'A'}">
                                        After
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.type}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${columnName == 'TYPE'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'R'}">
                                        Row
                                    </c:when>
                                    <c:when test="${row[columnName] == 'S'}">
                                        Statement
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.type}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${columnName == 'STATE'}">
                                <c:choose>
                                    <c:when test="${row[columnName] == 'E'}">
                                        <font color="green">
                                            Enabled
                                        </font>
                                    </c:when>
                                    <c:when test="${row[columnName] == 'D'}">
                                        <font color="red">
                                            Disabled
                                        </font>
                                    </c:when>
                                    <c:otherwise>
                                        ${entry.state}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${row[columnName]}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br />
</c:if>

<form action="triggers" method="POST">
   <b>Filter Trigger Name </b>
   <input type="TEXT" name="search" value="${search}" />
   <b>Schema : </b>
   <select name="selectedSchema">
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
   </select>
   <input type="image" src="../themes/original/img/Search.png" />
</form>

<!-- Display previous/next set links -->
<c:if test="${estimatedrecords > sessionScope.prefs.recordsToDisplay}"> &nbsp; | &nbsp;
  <c:if test="${startAtIndex != 0}">
    <a href="triggers?search=${param.search}&selectedSchema=${chosenSchema}&startAtIndex=${(startAtIndex - sessionScope.prefs.recordsToDisplay)}&endAtIndex=${startAtIndex}">
      <img src="../themes/original/img/Previous.png" border="0" />
    </a>
    &nbsp;
  </c:if>
  <c:if test="${estimatedrecords != endAtIndex}">
    <a href="triggers?search=${param.search}&selectedSchema=${chosenSchema}&startAtIndex=${endAtIndex}&endAtIndex=${endAtIndex + sessionScope.prefs.recordsToDisplay}">
      <img src="../themes/original/img/Next.png" border="0" />
    </a>
  </c:if>
  &nbsp; <font color="Purple">Current Set [${startAtIndex + 1} - ${endAtIndex}] </font>
</c:if>

<p />

<form method="post" action="triggers" name="tablesForm" id="tablesForm">
<input type="hidden" name="selectedSchema" value="${chosenSchema}" />
<table id="table_results" class="data">
 <thead>
   <tr>
    <th></th>
    <th>Schema</th>
    <th>Name</th>
    <th>Created</th>
    <th>Event</th>
    <th>Firing</th>
    <th>Type</th>
    <th>State</th>
    <th>Definition</th>
    <th>Action</th>
   </tr>
 </thead>
 <tbody>
	<c:forEach var="entry" varStatus="loop" items="${triggers}">
  	  <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
		  <td align="center">
		      <input type="checkbox" 
		             name="selected_trigger[]"
		             value="${entry['triggerName']}"
		             id="checkbox_trigger_${loop.index + 1}" />
		   </td>
		   <td align="center">${entry.schemaName}</td>
	  	   <td align="center">${entry.triggerName}</td>
	  	   <td align="center">${entry.created}</td>
	  	   <td align="center">
	  	   	<c:choose>
	  	   	  <c:when test="${entry.event == 'U'}">
	  	   	    Update
	  	   	  </c:when>
	  	   	  <c:when test="${entry.event == 'D'}">
	  	   	    Delete
	  	   	  </c:when>
	  	   	  <c:when test="${entry.event == 'I'}">
	  	   	    Insert
	  	   	  </c:when>
	  	   	  <c:otherwise>
	  	   	    ${entry.event}
	  	   	  </c:otherwise>
	  	   	</c:choose>
	  	   </td>
	  	   <td align="center">
	  	   	<c:choose>
	  	   	  <c:when test="${entry.firingTime == 'B'}">
	  	   	    Before
	  	   	  </c:when>
	  	   	  <c:when test="${entry.firingTime == 'A'}">
	  	   	    After
	  	   	  </c:when>
	  	   	  <c:otherwise>
	  	   	    ${entry.type}
	  	   	  </c:otherwise>
	  	   	</c:choose>		  	   
	  	   </td>
	  	   <td align="center">
	  	   	<c:choose>
	  	   	  <c:when test="${entry.type == 'R'}">
	  	   	    Row
	  	   	  </c:when>
	  	   	  <c:when test="${entry.type == 'S'}">
	  	   	    Statement
	  	   	  </c:when>
	  	   	  <c:otherwise>
	  	   	    ${entry.type}
	  	   	  </c:otherwise>
	  	   	</c:choose>	  	   
	  	   </td>
	  	   <td align="center">
	  	   	<c:choose>
	  	   	  <c:when test="${entry.state == 'E'}">
	  	   	  	<font color="green">
	  	   	  	   Enabled
	  	   	  	</font>
	  	   	  </c:when>
	  	   	  <c:when test="${entry.state == 'D'}">
	  	   	    <font color="red">
	  	   	       Disabled
	  	   	    </font>
	  	   	  </c:when>
	  	   	  <c:otherwise>
	  	   	    ${entry.state}
	  	   	  </c:otherwise>
	  	   	</c:choose>	
	  	   </td>
	  	   <td align="center">${entry.triggerDefinition}</td>
	  	   <td align="center">
    		<a href="triggers?triggerName=${entry['triggerName']}&selectedSchema=${chosenSchema}&triggerAction=DROP" onclick="return confirmLink(this, 'DROP TRIGGER ${entry['triggerName']}?')">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Drop Trigger" title="Drop Trigger" />
            </a>&nbsp;  
    		<a href="triggers?triggerId=${entry['triggerId']}&triggerName=${entry['triggerName']}&selectedSchema=${chosenSchema}&triggerAction=ALLTRIGGERINFO">
             <img class="icon" width="16" height="16" src="../themes/original/img/b_tblexport.png" alt="View All Trigger Information" title="View All Trigger Information" />
            </a>&nbsp;
            <a href="triggers?triggerId=${entry['triggerId']}&triggerName=${entry['triggerName']}&selectedSchema=${chosenSchema}&triggerAction=TRIGGERTABLE">
               <img class="icon" width="16" height="16" src="../themes/original/img/s_tbl.png" alt="View Trigger Table" title="View Triger Table" />
            </a>&nbsp;
           </td>
  	   </tr>
	</c:forEach>
 </tbody>
</table>

<div class="clearfloat">
<img class="selectallarrow" src="../themes/original/img/arrow_ltr.png"
    width="38" height="22" alt="With selected:" />
<a href="triggers?&selectedSchema=${chosenSchema}"
    onclick="if (markAllRows('tablesForm')) return false;">
    Check All</a>

/
<a href="triggers?selectedSchema=${chosenSchema}"
    onclick="if (unMarkAllRows('tablesForm')) return false;">
    Uncheck All</a>

<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
    <option value="With selected:" selected="selected">With selected:</option>
    <option value="Drop" >Drop</option>
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