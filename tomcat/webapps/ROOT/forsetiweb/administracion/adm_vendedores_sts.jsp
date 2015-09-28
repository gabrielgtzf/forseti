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
<%
 	String sts = JUtil.Msj("CEF", "ADM_VENDEDORES", "VISTA", "STATUS");
%>
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
oCMenu.makeMenu('top0','','&nbsp;<%= JUtil.Elm(sts,1) %>','/servlet/CEFAdmVendedoresCtrl?status=TODOS','cuerpo')
oCMenu.makeMenu('top1','','&nbsp;<%= JUtil.Elm(sts,2) %>','/servlet/CEFAdmVendedoresCtrl?status=ALTAS','cuerpo')
oCMenu.makeMenu('top2','','&nbsp;<%= JUtil.Elm(sts,3) %>','/servlet/CEFAdmVendedoresCtrl?status=BAJAS','cuerpo')
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
