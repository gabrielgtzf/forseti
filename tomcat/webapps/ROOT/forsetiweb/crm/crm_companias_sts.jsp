<!--
    Forseti, El ERP Gratuito para PyMEs
    Copyright (C) 2015 Gabriel Gutiérrez Fuentes.

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
<link href="../../compfsi/cefsts-coolmenus.css" rel="stylesheet" type="text/css">
<script language="JavaScript1.2" src="../../compfsi/coolmenus4.js"></script>
<script language="JavaScript1.2" src="../../compfsi/coolmenus4mccSts.js">
</script>
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<script>
<%
    JCRMLookUpSet set = new JCRMLookUpSet(request, "companies");
    set.m_Where = "ID_Section = 'tp_company' ";
	set.m_OrderBy = "PG_Lookup ASC";
    set.Open();
	for(int i = 0; i < set.getNumRows(); i++)
	{
		String str = set.getAbsRow(i).getVL_lookup(); 
%>
oCMenu.makeMenu('top<%=i%>','','&nbsp;<%=str%>','/servlet/CEFCRMCompaniasCtrl?status=<%=set.getAbsRow(i).getPG_lookup()%>','cuerpo')
<%
	} 
%>
oCMenu.construct()		
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="75" height="20"><img src="../../imgfsi/ztatuz.gif" width="75" height="20"></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td>&nbsp;</td>
		 <td width="5"><img src="../../imgfsi/lineas-ent.gif"></td>
        </tr>
      </table></td>
  </tr>
</table>
</body>
</html>
