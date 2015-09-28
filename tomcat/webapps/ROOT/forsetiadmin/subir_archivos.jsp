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
	boolean registrado = JUtil.yaRegistradoEnFsiAdmin(request, response);
	Integer subir_archivos = (Integer)request.getAttribute("subir_archivos");
	Enumeration Param = request.getParameterNames();
	int numArchivos = 0;
    while(Param.hasMoreElements())
    {
        String nombreParam = (String)Param.nextElement();
		String[] valoresParam = request.getParameterValues(nombreParam);
        String valorParam = valoresParam[0];
		
		if(nombreParam.indexOf("archivo_") != -1)
			numArchivos++;
    } 
	
	if(subir_archivos == null || subir_archivos.intValue() != numArchivos || !registrado) 
	{ 
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
 	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body background="../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="<%= request.getParameter("verif") %>" method="post" enctype="multipart/form-data" name="subir_archivos" target="_self">
 <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#FF6600"><%= JUtil.Msj("GLB","GLB","GLB","SAAS") %></td>
  </tr>
 <tr> 
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td> 
      <table width="100%" border="0" cellpadding="2" cellspacing="0">
<%
	Enumeration nombresParam = request.getParameterNames();
    while(nombresParam.hasMoreElements())
    {
        String nombreParam = (String)nombresParam.nextElement();
		String[] valoresParam = request.getParameterValues(nombreParam);
        String valorParam = valoresParam[0];
		
		if(nombreParam.indexOf("archivo_") == -1)
			continue;
%>
          <tr>
			<td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","ARCHIVO") + " " + valorParam %></td>
			<td width="80%"><input name="<%= nombreParam %>" type="file" size="50"></td>
	      </tr>
<%
    } 
%>
		  <tr>
			<td colspan="2">
<%
	//Ahora pone los parametros que no son archivos
	Enumeration nombresParam2 = request.getParameterNames();
    while(nombresParam2.hasMoreElements())
    {
        String nombreParam = (String)nombresParam2.nextElement();
		String[] valoresParam = request.getParameterValues(nombreParam);
        String valorParam = valoresParam[0];
		
		if(nombreParam.indexOf("archivo_") != -1 || nombreParam.indexOf("verif") != -1)
			continue;
%>
       			<input name="<%= nombreParam %>" type="hidden" value="<%= valorParam %>">
<%			 
    } 
%>
			</td>
		  </tr>
		</table>
	    <br>
        <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td align="right"> 
             	<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
    			<input type="button" name="cancelar" onClick="javascript:history.back()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table>
      
	 </td>
  </tr>
</table>
</form>
</body>
</html>
