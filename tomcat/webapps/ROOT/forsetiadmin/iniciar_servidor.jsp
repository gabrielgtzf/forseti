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
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("../genfsi/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	String mensaje = JUtil.getMensajeAdmin(request, response);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript">
<!-- 
if(top.location == self.location) {
	top.location.href = "../forseti.html"
}

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
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#FF6600">Sistema 
      Administrativo Forseti</td>
  </tr>
  
<%
	out.println(mensaje);
%>  
  <tr>
    <td>
<%
	String REST = (String)request.getAttribute("REST");
	if(REST == null)
	{
%>
      <p>&nbsp;</p><form action="/servlet/SAFRegistro" method="get" name="iniciar_servidor" target="_self">
	  	<table width="400" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">
          <tr> 
            <td class="titChico" colspan="2" align="center" bgcolor="#FF6600"> 
              DATOS DEL SERVIDOR A INICIAR</td>
          </tr>
          <tr> 
            <td width="50%">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
		  <tr> 
            <td align="right">Direcci&oacute;n IP del servidor PostgreSQL:</td>
            <td align="center"> <input name="addr" type="text" id="addr" size="20" maxlength="30"<% if(request.getParameter("addr") != null) {	out.println(" value=\"" + request.getParameter("addr") + "\""); } else { out.println(" value=\"127.0.0.1\""); }  %>> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td align="right">Puerto del cluster del servicio forseti:</td>
            <td align="center"> <input name="port" type="text" id="port" size="20" maxlength="30"<% if(request.getParameter("port") != null) {	out.println(" value=\"" + request.getParameter("port") + "\""); } else { out.println(" value=\"5432\""); }  %>> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td align="right">Contrase&ntilde;a del usuario forseti</td>
            <td align="center"> <input name="pass" type="password" id="pass" size="20" maxlength="30"> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
		  <tr> 
            <td class="titChico" colspan="2" align="center" bgcolor="#FF6600"> 
              Para iniciar a partir de un respaldo completo de servidor, ingresa 
              la ruta completa del tanto del archivo zip, como de la instalacion 
              de tomcat</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
		  <tr> 
		  	<td align="right">Archivo Zip</td>
            <td align="center"> <input name="urlresp" type="text" id="urlresp" style="width:90%"<% if(request.getParameter("urlresp") != null) { out.print(" value=\"" + request.getParameter("urlresp") + "\""); } %>></td>
          </tr>
		  <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
		  <tr> 
		  	<td align="right">Ruta Tomcat</td>
            <td align="center"> <input name="urltomcat" type="text" id="urltomcat" style="width:90%"<% if(request.getParameter("urltomcat") != null) { out.print(" value=\"" + request.getParameter("urltomcat") + "\""); } %>></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
		  <tr> 
            <td colspan="2" align="center"> 
              <input onClick="javascript: this.disabled=true;" type="submit" name="submit" value="OK"> </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
        </table> 
	   </form>
<%
	}
	else
		out.print("&nbsp;");
%> 
	</td>
  </tr>
</table>

</body>
</html>
