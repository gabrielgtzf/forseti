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
<%
 	String sts = JUtil.Msj("CEF", "VEN_CXC", "VISTA", "TIPO");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link href="../../compfsi/cefsts-coolmenus.css" rel="stylesheet" type="text/css">
<script language="JavaScript1.2" src="../../compfsi/coolmenus4.js"></script>
<script language="JavaScript1.2" src="../../compfsi/coolmenus4mccSts.js">
/*****************************************************************************
Copyright (c) 2001 Thomas Brattli (webmaster@dhtmlcentral.com)

DHTML coolMenus - Get it at coolmenus.dhtmlcentral.com
Version 4.0_beta
This script can be used freely as long as all copyright messages are
intact.

Extra info - Coolmenus reference/help - Extra links to help files **** 
CSS help: http://coolmenus.dhtmlcentral.com/projects/coolmenus/reference.asp?m=37
General: http://coolmenus.dhtmlcentral.com/reference.asp?m=35
Menu properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=47
Level properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=48
Background bar properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=49
Item properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=50
******************************************************************************/
</script>
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<script>
/******************************************
Menu item creation:
myCoolMenu.makeMenu(name, parent_name, text, link, target, width, height, regImage, overImage, regClass, overClass , align, rows, nolink, onclick, onmouseover, onmouseout) 
*************************************/
oCMenu.makeMenu('top0','','&nbsp;<%= JUtil.Elm(sts,1) %>','/servlet/CEFVenCXCCtrl?status=TODOS','cuerpo')
oCMenu.makeMenu('top1','','&nbsp;<%= JUtil.Elm(sts,2) %>','/servlet/CEFVenCXCCtrl?status=CUENTAS','cuerpo')
oCMenu.makeMenu('top2','','&nbsp;<%= JUtil.Elm(sts,3) %>','/servlet/CEFVenCXCCtrl?status=ANTICIPOS','cuerpo')
oCMenu.makeMenu('top3','','&nbsp;<%= JUtil.Elm(sts,4) %>','/servlet/CEFVenCXCCtrl?status=PAGOS','cuerpo')
oCMenu.makeMenu('top4','','&nbsp;<%= JUtil.Elm(sts,5) %>','/servlet/CEFVenCXCCtrl?status=SALDOS','cuerpo')
oCMenu.makeMenu('top5','','&nbsp;<%= JUtil.Elm(sts,6) %>','/servlet/CEFVenCXCCtrl?status=APLICACIONES','cuerpo')
oCMenu.makeMenu('top6','','&nbsp;<%= JUtil.Elm(sts,7) %>','/servlet/CEFVenCXCCtrl?status=DESCUENTOS','cuerpo')
oCMenu.makeMenu('top7','','&nbsp;<%= JUtil.Elm(sts,8) %>','/servlet/CEFVenCXCCtrl?status=DEVOLUCIONES','cuerpo')

//Leave this line - it constructs the menu
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
