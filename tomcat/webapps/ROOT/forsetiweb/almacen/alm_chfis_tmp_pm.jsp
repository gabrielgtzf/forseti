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
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td align="right" valign="middle"> 
				<a href="/servlet/CEFRegistro"><img src="../../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040205.html"><img src="../../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a>
			</td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= JUtil.Msj("CEF","MENU","ALM","CHFIS") %></td>
  </tr>
  <tr> 
    <td>
	  <table width="100%" border="1" bordercolor="#333333" align="center" cellpadding="5" cellspacing=A"0" bgcolor="#CCCCCC">
	    <tr><td class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.getSesion(request).getSesion("ALM_CHFIS").getPanelTiempo() %></td></tr>
		<tr><td align="center"><a href="../../forsetiweb/filtro_esp_por_mes_pm.jsp?fsi_vista=CEFAlmChFisCtrl" class="titCuerpoBco" ><%= JUtil.Msj("GLB","GLB","GLB","ESP",2) %></a></td></tr>
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
		<tr><td align="center"><a href="/servlet/CEFAlmChFisCtrl?tiempo=MAS&mes=<%=set.getAbsRow(i).getMes()%>&ano=<%=set.getAbsRow(i).getAno()%>" class="titCuerpoBco" ><%=str%></a></td></tr>
<%
	}
%>
      </table>
	</td>
  </tr>
</table>
</body>
</html>
