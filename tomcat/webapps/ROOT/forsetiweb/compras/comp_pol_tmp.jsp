<!--
    Forseti, El ERP Gratuito para PyMEs
    Copyright (C) 2015 Gabriel Guti�rrez Fuentes.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->
<%@ page import="forseti.*, forseti.sets.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link href="../../compfsi/ceftmp-coolmenus.css" rel="stylesheet" type="text/css">
<script language="JavaScript1.2" src="../../compfsi/coolmenus4.js"></script>
<script language="JavaScript1.2" src="../../compfsi/coolmenus4mccTmp.js">
</script>
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<script>
oCMenu.level[0].width=20
oCMenu.makeMenu('top0','','&nbsp;*','/servlet/CEFCompPoliticasCtrl?tiempo=TODO','cuerpo')
<%
		int i = 1;
		for (char c = 'A'; c <= 'Z'; c++) 
		{
			String str = Character.toString(c);
%>
oCMenu.makeMenu('top<%=i++%>','','&nbsp;<%=str%>','/servlet/CEFCompPoliticasCtrl?tiempo=<%= str %>','cuerpo')
<%
		}
%>
oCMenu.construct()
</script>
</body>
</html>
