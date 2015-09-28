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
<html>
<head>
<style>
/* CoolMenus 4 - default styles - do not edit */
.clCMAbs{position:absolute; visibility:hidden; left:0; top:0}
/* CoolMenus 4 - default styles - end */

/*Styles for level 0*/
.clLevel0,.clLevel0over{position:absolute; padding:1px; font-family:tahoma,arial,helvetica; font-size:9px; font-weight:normal}
.clLevel0{background-color:#FF6600; layer-background-color:#000099; color:white;}
.clLevel0over{background-color:red; layer-background-color:#336699; color:Yellow; cursor:pointer; cursor:hand; }
.clLevel0border{position:absolute; visibility:hidden; background-color:#red; layer-background-color:red}

</style>
<script language="JavaScript1.2">
function ventEm()
{
	parametrs = "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=1,width=250,height=150";
	ventana = window.open('../../forsetiweb/filtro_esp_por_mes.jsp?fsi_vista=produccion.JProdProduccionCtrl', '', parametrs);
	ventana.focus();
}
</script>
<script language="JavaScript1.2" src="../coolmenus4.js">
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body bgcolor="#000000">
<script>

/*** 
This is the menu creation code - place it right after you body tag
Feel free to add this to a stand-alone js file and link it to your page.
**/

//Menu object creation
oCMenu=new makeCM("oCMenu") //Making the menu object. Argument: menuname

oCMenu.frames = 0

//Menu properties   
oCMenu.pxBetween=2
oCMenu.fromLeft=5
oCMenu.fromTop=0   
oCMenu.rows=1 
oCMenu.menuPlacement="left"
                                                             
oCMenu.offlineRoot="file:///C|/Inetpub/wwwroot/dhtmlcentral/projects/coolmenus/" 
oCMenu.onlineRoot="" 
oCMenu.resizeCheck=1 
oCMenu.wait=1000 
oCMenu.fillImg="cm_fill.gif"
oCMenu.zIndex=0

//Background bar properties
oCMenu.useBar=0

//Level properties - ALL properties have to be spesified in level 0
oCMenu.level[0]=new cm_makeLevel() //Add this for each new level
oCMenu.level[0].width=60
oCMenu.level[0].height=20
oCMenu.level[0].regClass="clLevel0"
oCMenu.level[0].overClass="clLevel0over"
oCMenu.level[0].borderX=1
oCMenu.level[0].borderY=1
oCMenu.level[0].borderClass="clLevel0border"
oCMenu.level[0].offsetX=0
oCMenu.level[0].offsetY=0
oCMenu.level[0].rows=0
oCMenu.level[0].arrow=0
oCMenu.level[0].arrowWidth=0
oCMenu.level[0].arrowHeight=0
oCMenu.level[0].align="bottom"

/******************************************
Menu item creation:
myCoolMenu.makeMenu(name, parent_name, text, link, target, width, height, regImage, overImage, regClass, overClass , align, rows, nolink, onclick, onmouseover, onmouseout) 
*************************************/
oCMenu.makeMenu('top0','','&nbsp;Especial','javascript:ventEm()')
oCMenu.makeMenu('top1','','&nbsp;Hoy','/servlet/forseti.produccion.JProdProduccionCtrl?tiempo=HOY','cuerpo')
oCMenu.makeMenu('top2','','&nbsp;Semana','/servlet/forseti.produccion.JProdProduccionCtrl?tiempo=SEM','cuerpo')
<%
	JContaCatalogSetPerV2 set = new JContaCatalogSetPerV2(request);
	set.Open();
	for(int i = 0; i < set.getNumRows(); i++)
	{
		String mes = (set.getAbsRow(i).getCerrado()) ? "* " + JUtil.convertirMesCorto(set.getAbsRow(i).getMes()) :
			JUtil.convertirMesCorto(set.getAbsRow(i).getMes());
		String str = mes + set.getAbsRow(i).getAno();
%>
		oCMenu.makeMenu('top<%=i+3%>','','&nbsp;<%=str%>','/servlet/forseti.produccion.JProdProduccionCtrl?tiempo=MAS&mes=<%=set.getAbsRow(i).getMes()%>&ano=<%=set.getAbsRow(i).getAno()%>','cuerpo')
<%
	}
%>

//Leave this line - it constructs the menu
oCMenu.construct()		
</script>

</body>
</html>
