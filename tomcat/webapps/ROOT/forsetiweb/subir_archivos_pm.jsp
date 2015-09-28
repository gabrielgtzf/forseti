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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	boolean registrado = JUtil.yaRegistradoEnFsi(request, response);
	// Inicia con registrar el objeto de sesion si no esta registrado
	if(!registrado) 
	{ 
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
 	}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="<%= request.getParameter("verif") %>" method="post" enctype="multipart/form-data" name="subir_archivos">
<table bgcolor="#FFFFFF" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","SAAS") %></td>
  </tr>
 <tr> 
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td> 
      <table width="100%" border="0" cellpadding="5" cellspacing="5">
<%
    Enumeration nombresParam = request.getParameterNames();
    while(nombresParam.hasMoreElements())
    {
        String nombreParam = (String)nombresParam.nextElement();
		if(nombreParam.indexOf("archivo_") == -1)
			continue;
		String[] valoresParam = request.getParameterValues(nombreParam);
        String valorParam = valoresParam[0];
%>
        <tr>
			<td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","ARCHIVO") %> <!--= valorParam --></td>
			<td> 
              <input class="cpoColAzc" name="<%= nombreParam %>" type="file" style="width:100%"/></td>
	   </tr>
<%
    }      
%>
          <tr>
    		<td colspan="2">&nbsp;</td>
  		  </tr>
  		  <tr> 
            <td colspan="2" align="right"> 
             	<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
    			<input type="button" name="cancelar" onClick="javascript:window.close()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
