<%--
  	Copyright (c) 2013 GoPivotal, Inc. All Rights Reserved.

	This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; only version 2 of the License, and no
    later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

	The full text of the GPL is provided in the COPYING file.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="../css/greenplum.css" />
    <link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
    <script type="text/javascript">
        // <![CDATA[

        // js form validation stuff
        var confirmMsg  = 'Do you really want to ';
        // ]]>
    </script>
    <script src="../js/functions.js" type="text/javascript"></script>
    <title><fmt:message key="sqlfireweb.appname" /> Sessions</title>
</head>
<body>

<h2><fmt:message key="sqlfireweb.appname" /> Views</h2>

<jsp:include page="toolbar.jsp" flush="true" />

<div class="notice">
    Found ${records} user session(s).
</div>
<br />

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

<table id="table_results" class="data">
    <thead>
    <tr>
        <th>Pid#</th>
        <th>User</th>
        <th>Session Id#</th>
        <th>Current Query</th>
        <th>Waiting</th>
        <th>Query Start</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="entry" varStatus="loop" items="${sessions}">
        <tr class="${((loop.index % 2) == 0) ? 'even' : 'odd'}">
            <td align="center">${entry.procpid}</td>
            <td align="center">${entry.usename}</td>
            <td align="center">${entry.sessId}</td>
            <td align="center">${entry.currentQuery}</td>
            <td align="center">${entry.waiting}</td>
            <td align="center">${entry.queryStart}</td>
            <td align="center">
                <a href="sessions?pid=${entry['procpid']}&sessionAction=DESTROY" onclick="return confirmLink(this, 'KILL SESSION with PID ${entry['procpid']}?')">
                    <img class="icon" width="16" height="16" src="../themes/original/img/b_drop.png" alt="Kill Session" title="Kill Session" />
                </a>&nbsp;
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<br />

<jsp:include page="footer.jsp" flush="true" />

</body>
</html>
