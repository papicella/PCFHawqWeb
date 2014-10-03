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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<base target="frame_content" />
<link rel="stylesheet" type="text/css" href="css/theme_left.css" />
<link rel="stylesheet" type="text/css" href="css/print.css" media="print" />
</head>
<body id="body_leftFrame">

<b><i>User - ${sessionScope.user} </i></b>
<br />
<hr />

<div id="left_tableList">
 <ul id="subel0">
  <li>
     <a href="PCFHawq-Web/sessions" title="Sessions">
         <img class="icon" src="./themes/original/img/s_lang.png" width="16" height="16" alt="Sessions" />
         Sessions
     </a>
  </li>
  <li>
    <a href="PCFHawq-Web/query" title="SQL Worksheet">
      <img class="icon" src="./themes/original/img/b_sql.png" width="16" height="16" alt="SQL Worksheet" />
      Worksheet
    </a>
  </li>
  <li>
     <a href="PCFHawq-Web/tables?type=ordinary" title="View Tables">
         <img class="icon" src="./themes/original/img/s_tbl.png" width="16" height="16" alt="View Tables" />
         Tables (${sessionScope.schemaMap['Table']})
     </a>
  </li>
  <li>
     <a href="PCFHawq-Web/tables?type=external" title="View External Tables">
         <img class="icon" src="./themes/original/img/s_tbl.png" width="16" height="16" alt="View External Tables" />
         External Tables (${sessionScope.schemaMap['ExternalTable']})
     </a>
  </li>
  <li>
     <a href="PCFHawq-Web/views" title="View Views">
         <img class="icon" src="./themes/original/img/b_views.png" width="16" height="16" alt="View Views" />
         Views (${sessionScope.schemaMap['View']})
     </a>
  </li>
  <li>
     <a href="PCFHawq-Web/displayqueryreports" title="View Reports">
         <img class="icon" src="./themes/original/img/b_props.png" width="16" height="16" alt="View Reports" />
         Reports
     </a>
  </li>
  <li>
     <a href="PCFHawq-Web/logout" target="_top" title="Logoff">
         <img class="icon" src="./themes/original/img/s_loggoff.png" width="16" height="16" alt="Logoff" />
         Disconnect
     </a>
  </li>
 </ul>

<p />
<a href="PCFHawq-Web/refresh" title="Refresh List" target="_top">
    <img class="icon" src="./themes/original/img/Refresh List.png"  alt="Refresh List" />
</a>

<br />

<div id="selflink" class="print_ignore">
  <font size="-2">
  Ver 1.0 - &copy; 2014. All Rights Reserved <br />
  <a href="mailto:papicella@gopivotal.com">Pas Apicella</a>
  </font>
</div>

<div id="pmalogo">
    <img src="./themes/original/img/PoweredByPivotal1.png" />
</div>

</body>
</html>