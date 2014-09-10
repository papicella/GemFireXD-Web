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
        //-->
    </SCRIPT>
<title><fmt:message key="sqlfireweb.appname" /> Preferences</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Preferences</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
Specify at runtime the preferences you wish to use across display pages within GemFireXD*Web
</div>

<c:if test="${saved != null}">
  <br />
  <div class="success">
    ${saved}
  </div>
</c:if>

<form method="post" action="preferences">
<fieldset>
 <legend>GemFireXD*Web User Preferences Form</legend>
 <table class="formlayout">
  <tr>
   <td align="right">Records To Display for Schema Objects:</td>
   <td><input type="text" name="recordsToDisplay" size="10" value="${userPref.recordsToDisplay}" onkeypress="return isNumberKey(event)"/></td>
  </tr>
  <tr>
   <td align="right">Records To Display in SQL Worksheet:</td>
   <td><input type="text" name="maxRecordsinSQLQueryWindow" size="10" value="${userPref.maxRecordsinSQLQueryWindow}" onkeypress="return isNumberKey(event)" /></td>
  </tr>
  <tr>
   <td align="right">Connection Auto Commit:</td>
   <td>
     <select name="autoCommit">
      <c:choose>
       <c:when test="${userPref.autoCommit == 'Y'}">
         <option value="Y" selected="selected">Y</option>
         <option value="N">N</option>
       </c:when>
       <c:otherwise>
         <option value="N" selected="selected">N</option>
         <option value="Y">Y</option>       
       </c:otherwise>
      </c:choose>
     </select>    
   </td>
   <tr>
     <td align="right">Jolokia HTTP Rest URL:</td>
     <td><input type="text" name="jolokiaURL" size="50" value="${userPref.jolokiaURL}" maxlength="250" /></td>
   </tr>
  </tr>
 </table>
</fieldset>
<fieldset class="tblFooters">
  <input type="submit" value="Save Preferences" name="pSubmit" />
  <input type="reset" value="Reset" />
</fieldset>
</form>
<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>