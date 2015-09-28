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
<link href="../../compfsi/saftmp-coolmenus.css" rel="stylesheet" type="text/css">
<script language="JavaScript1.2" src="../../compfsi/coolmenus4.js"></script>
<script language="JavaScript1.2" src="../../compfsi/coolmenus4mccTmp.js">
</script>
<script language="JavaScript1.2">
function ventEm()
{
	parametrs = "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=1,width=250,height=150";
	ventana = window.open('../../forsetiadmin/filtro_esp_por_mes.jsp?fsi_vista=SAFRegistIniSesCtrl', '', parametrs);
	ventana.focus();
}
</script>
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<script>
oCMenu.makeMenu('top1','','&nbsp;Hoy','/servlet/SAFRegistIniSesCtrl?tiempo=HOY','cuerpo')
oCMenu.makeMenu('top2','','&nbsp;Semana','/servlet/SAFRegistIniSesCtrl?tiempo=SEM','cuerpo')
oCMenu.makeMenu('top0','','&nbsp;Mes','javascript:ventEm()')

oCMenu.construct()		
</script>
</body>
</html>
