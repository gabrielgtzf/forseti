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
<%@ page import="forseti.*, java.util.* " %>
<%
	String RGST = (String)request.getAttribute("RGST");
	if(RGST == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String BLOQ = (String)request.getAttribute("BLOQ");
	boolean registrado = JUtil.yaRegistradoEnFsiAdmin(request, response);

	String mensaje = JUtil.getMensajeAdmin(request, response);
	String datos = JUtil.Msj("SAF","REGISTRO","SESION","DATOS");
	String usuario = JUtil.Msj("SAF","REGISTRO","SESION","USUARIO");
	String password = JUtil.Msj("SAF","REGISTRO","SESION","PASSWORD");
	String hola = JUtil.Msj("SAF","REGISTRO","OK","BIENVENIDO");
	String bienvenido = JUtil.Msj("SAF","REGISTRO","OK","BIENVENIDO", 2, 3);
	String actividades1 = JUtil.Msj("SAF","REGISTRO","OK","ACTIVIDADES");
	String actividades2 = JUtil.Msj("SAF","REGISTRO","OK","ACTIVIDADES", 2);
	String actividades3 = JUtil.Msj("SAF","REGISTRO","OK","ACTIVIDADES", 3);
	String actividades4 = JUtil.Msj("SAF","REGISTRO","OK","ACTIVIDADES", 4);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript">
<!-- 
if(top.location == self.location) {
	top.location.href = "../forsetiadmin/forseti.html"
}
<%
	if(registrado) 
	{
%>  
if(parent.folder.document.URL.indexOf('folder.jsp') == -1) {
	parent.folder.document.location.href = "../forsetiadmin/folder.jsp"
}
if(parent.menu.document.URL.indexOf('menu.jsp') == -1) {
	parent.menu.document.location.href = "../forsetiadmin/menu.jsp"
}
if(parent.barra.document.URL.indexOf('barra.jsp') == -1) {
	parent.barra.document.location.href = "../forsetiadmin/barra.jsp"
}
<%
	}
	else
	{
%>  
if(parent.folder.document.URL.indexOf('folder.html') == -1) {
	parent.folder.document.location.href = "../forsetiadmin/folder.html"
}
if(parent.menu.document.URL.indexOf('menu.html') == -1) {
	parent.menu.document.location.href = "../forsetiadmin/menu.html"
}
if(parent.barra.document.URL.indexOf('barra.html') == -1) {
	parent.barra.document.location.href = "../forsetiadmin/barra.html"
}
<%
	}
%>
if(parent.tiempo.document.URL.indexOf('tiempo.html') == -1) {
	parent.tiempo.document.location.href = "../forsetiadmin/tiempo.html"
}
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiadmin/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiadmin/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#333333" text="#000000" link="#FF6600" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#FF6600"><%= JUtil.Msj("GLB","GLB","GLB","SAF") %></td>
  </tr>
  
<%

	out.println(mensaje);

	// Inicia con registrar el objeto de sesion si no esta registrado
	if(BLOQ.equals("false"))
	{
		if(!registrado) 
		{
%>  
  <tr>
    <td>
      <p>&nbsp;</p><form action="/servlet/SAFRegistro" method="post" name="registro" target="_self">
	  	<table width="300" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">
          <tr> 
            <td class="titChico" colspan="2" align="center" bgcolor="#FF6600"> 
              <%= datos %></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><%= usuario %></td>
            <td align="center"> <input name="usuario" type="text" id="usuario" size="20" maxlength="30"<% if(request.getParameter("usuario") != null) {	out.println(" value=\"" + request.getParameter("usuario") + "\""); } %>> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><%= password %></td>
            <td align="center"> <input name="password" type="password" id="password" size="20" maxlength="30"> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
          <tr> 
            <td colspan="2" align="center"><input type="submit" name="submit" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
        </table> 
	   </form> 
	</td>
  </tr>
<%
		}
		else // si ya esta registrado
		{
%>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr> 
    <td align="center" class="titGiganteNar"><%= hola %></td>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>  
  <tr> 
    <td><p class="txtCuerpoBco"><%= JUtil.getSesionAdmin(request).getNombreUsuario() + " " + bienvenido %> </p>
      <ul class="txtChicoNar">
        <li><%= actividades1 %></li>
        <li><%= actividades2 %></li>
        <li><%= actividades3 %></li>
        <li><%= actividades4 %></li>
      </ul>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>
<%	
		}
	}
	else
	{
%>
    <tr> 
          <td><p class="textoGris"></p>
      </td>
  </tr>
 <%	
	}
%>
</table>

</body>
</html>
