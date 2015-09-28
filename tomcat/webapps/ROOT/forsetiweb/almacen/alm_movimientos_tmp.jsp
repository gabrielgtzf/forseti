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
<link href="../../compfsi/ceftmp-coolmenus.css" rel="stylesheet" type="text/css">
<script language="JavaScript1.2" src="../../compfsi/coolmenus4.js"></script>
<script language="JavaScript1.2" src="../../compfsi/coolmenus4mccTmp.js">
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
<script language="JavaScript1.2">
function ventEm()
{
	parametrs = "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=1,width=250,height=150";
	ventana = window.open('../../forsetiweb/filtro_esp_por_mes.jsp?fsi_vista=CEFAlmMovimientosCtrl', '', parametrs);
	ventana.focus();
}
</script>
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<script>
/******************************************
Menu item creation:
myCoolMenu.makeMenu(name, parent_name, text, link, target, width, height, regImage, overImage, regClass, overClass , align, rows, nolink, onclick, onmouseover, onmouseout) 
*************************************/
oCMenu.makeMenu('top0','','&nbsp;<%= JUtil.Msj("GLB","GLB","GLB","ESP",2)%>','javascript:ventEm()')
oCMenu.makeMenu('top1','','&nbsp;<%= JUtil.Msj("GLB","GLB","GLB","HOY",1)%>','/servlet/CEFAlmMovimientosCtrl?tiempo=HOY','cuerpo')
oCMenu.makeMenu('top2','','&nbsp;<%= JUtil.Msj("GLB","GLB","GLB","SEMANA",1)%>','/servlet/CEFAlmMovimientosCtrl?tiempo=SEM','cuerpo')
<%
	String meses = JUtil.Msj("GLB","GLB","GLB","MESES-ANO",1);
	JContaCatalogSetPerV2 set = new JContaCatalogSetPerV2(request);
	set.Open();
	for(int i = 0; i < set.getNumRows(); i++)
	{
		String mes = (set.getAbsRow(i).getCerrado()) ? "*" + JUtil.Elm(meses,set.getAbsRow(i).getMes()) :
			JUtil.Elm(meses,set.getAbsRow(i).getMes());
		String str = mes + " " + set.getAbsRow(i).getAno();
%>
		oCMenu.makeMenu('top<%=i+3%>','','&nbsp;<%=str%>','/servlet/CEFAlmMovimientosCtrl?tiempo=MAS&mes=<%=set.getAbsRow(i).getMes()%>&ano=<%=set.getAbsRow(i).getAno()%>','cuerpo')
<%
	}
%>

//Leave this line - it constructs the menu
oCMenu.construct()		
</script>
</body>
</html>
