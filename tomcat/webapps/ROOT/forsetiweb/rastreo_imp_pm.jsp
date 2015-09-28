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
<%@ page import="forseti.*, java.util.*, java.io.*"%>
<%
	String rastreo_imp = (String)request.getAttribute("rastreo_imp");
	if(rastreo_imp == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	JRastreo rastreo = (JRastreo)request.getAttribute("rastreo");
	int Niv = rastreo.getNiveles();
	String titulo =  rastreo.getTitulo();
	
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../compfsi/staticbar.js">
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#333333">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="left" valign="middle">
			   	<input type="button" name="cancelar" onClick="javascript:history.back();" value="<%= JUtil.Msj("GLB","GLB","GLB","CERRAR") %>"/></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040105.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="140" bgcolor="#333333">&nbsp;</td>
	</tr>
	<tr> 
    	<td> 
<%
	for(int N = 1; N <= Niv; N++)
	{
%>
			<table width="100%" border="0">
        		<tr>
				  <td height="30" class="clockCef" align="center"><%= JUtil.Msj("GLB","GLB","GLB","RASTREO-IMP",N) %></td>
				</tr>
			</table>
<%
		for(int ne = 1; ne <= rastreo.getNumElmsNiv(N); ne++)
		{
%>
			<table width="100%" border="0">
        		<tr>
				  <td class="txtChicoBco" bgcolor="#999999" align="center"><%= rastreo.getTitulo(N,ne) %></td>
			  	</tr>
			</table>
			<table width="100%" border="0">
<%
			for(int i = 0; i < rastreo.NumCabs(N,ne); i++)
			{
%>
			  <tr>
				<td class="titChicoAzc"><%= rastreo.CabsEtq(N,ne)[i] %></td>
				<td><%= rastreo.CabsVal(N,ne)[i] %></td>
			  </tr>
<%
			}
%>
			</table>
			<table width="100%" border="0">
			  <tr>
<%
			for(int i = 0; i < rastreo.NumConceptos(N,ne); i++)
			{
%>
				<td class="titChicoNeg"><%= rastreo.Conceptos(N,ne)[i] %></td>
<%
			}
%>
			  </tr>
<%
			for(int i = 0; i < rastreo.NumDets(N,ne); i++)
			{
%>			  
  			  <tr>
<%
				for(int j = 0; j < rastreo.NumConceptos(N,ne); j++)
				{
%>
				<td><%= rastreo.Dets(N,ne,i)[j] %></td>
<%
				}
%>
			  </tr>
<%
			}
%>
 			</table>
			
      <%
		}
	}
%>
    	</td>
	</tr>
</table>
</body>
</html>
