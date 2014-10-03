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
<title><fmt:message key="sqlfireweb.appname" /> - Welcome Page</title>
<link rel="stylesheet" type="text/css" href="css/greenplum.css" />
<link rel="stylesheet" type="text/css" href="css/print.css" media="print" />
</head>
<body>

<h2>PCFHawq*Web - Welcome Page</h2>

<a href="PCFHawq-Web/home" target="_top" title="Home">
  <img class="icon" src="./themes/original/img/b_home.png" width="16" height="16" alt="Home" />
  Home Page
</a>&nbsp; | &nbsp;
<a href="PCFHawq-Web/preferences" title="Preferences">
  <img class="icon" src="./themes/original/img/b_props.png" width="16" height="16" alt="Greenplum*Web Preferences" />
  Preferences
</a>&nbsp; | &nbsp;
<a href="PCFHawq-Web/viewconmap" title="View Connection Map">
  <img class="icon" src="./themes/original/img/Connection.gif" width="16" height="16" alt="View Connections Map" />
  Connection Map
</a>
<p />

<div class="success">
Connected to HAWQ using JDBC URL <b>${sessionScope.url}</b>
</div>

<br />
<center>

</center>

<p />

<table>
    <tr>
        <td>
            <img src="./themes/original/img/hawq.png" alt="hawq"/>
        </td>
        <td align="justify">
            HAWQ adds SQL's expressive power to Hadoop to accelerate data analytics projects, simplify development while
            increasing productivity, expand Hadoop's capabilities and cut costs. HAWQ can help your organization render
            Hadoop queries faster than any Hadoop-based query interface on the market by adding rich, proven, parallel
            SQL processing facilities. HAWQ leverages your existing business intelligence products, analytic products,
            and your workforce's SQL skills to bring more than 100X performance improvement (in some cases up to 600X)
            to a wide range of query types and workloads. The world?s fastest SQL query engine on Hadoop, HAWQ is 100
            percent SQL compliant
        </td>
    </tr>
</table>

<br />

<fieldset>
    <legend>Getting Started</legend>
    <table class="formlayout">
        <tr>
            <td>
                <a href="PCFHawq-Web/query">
                    <img class="icon" src="./themes/original/img/b_sql.png" width="16" height="16" alt="SQL Worksheet" />
                    SQL Worksheet
                </a>
            </td>
        </tr>
    </table>
</fieldset>
<p />

<jsp:include page="footer.html" flush="true" />

</body>
</html>