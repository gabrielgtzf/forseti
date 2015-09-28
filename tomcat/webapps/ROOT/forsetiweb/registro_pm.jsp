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
<%@ page import="forseti.*, forseti.sets.*, java.util.* " %>
<%
	String RGST = (String)request.getAttribute("RGST");
	if(RGST == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String BLOQ = (String)request.getAttribute("BLOQ");
	boolean registrado = JUtil.yaRegistradoEnFsi(request, response);

	
	String mensaje = JUtil.getMensaje(request, response);
	String datos = JUtil.Msj("CEF","REGISTRO","SESION","DATOS");
	String empresa = JUtil.Msj("CEF","REGISTRO","SESION","EMPRESA");
	String usuario = JUtil.Msj("CEF","REGISTRO","SESION","USUARIO");
	String password = JUtil.Msj("CEF","REGISTRO","SESION","PASSWORD");
	String hola = JUtil.Msj("CEF","REGISTRO","OK","BIENVENIDO");
	String bienvenido = JUtil.Msj("CEF","REGISTRO","OK","BIENVENIDO", 2, 3);
	String actividades1 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES");
	String actividades2 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 2);
	String actividades3 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 3);
	String actividades4 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 4);
	String actividades5 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 5);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
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
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="100%" align="right" valign="middle"><a href="../../forsetidoc/040101.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr> 
  <tr>
    <td>
      <form onsubmit="document.getElementById('aceptar').disabled=true;" action="/servlet/CEFRegistro" method="post" name="registro">
	  	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="5" bgcolor="#CCCCCC">
          <tr> 
            <td colspan="2" class="titChico" align="center" bgcolor="#0099FF">
              <%= datos %></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
          <tr> 
            <td width="20%">&nbsp;</td><td align="left"><%= empresa %></td>
          <tr>
		  	<td width="20%">&nbsp;</td><td align="left"> <input name="basededatos" type="text" id="basededatos" size="20" maxlength="30"<% if(request.getParameter("basededatos") != null) { out.println(" value=\"" + request.getParameter("basededatos") + "\""); } %>></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
          <tr> 
            <td width="20%">&nbsp;</td><td align="left"><%= usuario %></td>
           </tr>
		   <tr>
		     <td width="20%">&nbsp;</td><td align="left"> <input name="usuario" type="text" id="usuario" size="20" maxlength="30"<% if(request.getParameter("usuario") != null) {	out.println(" value=\"" + request.getParameter("usuario") + "\""); } %>></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
          <tr> 
            <td width="20%">&nbsp;</td><td align="left"><%= password %></td>
          </tr>
		  <tr>
		  	<td width="20%">&nbsp;</td><td align="left"> <input name="password" type="password" id="password" size="20" maxlength="30"></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
          <tr> 
            <td width="20%">&nbsp;</td><td align="left"><input type="submit" name="submit" id="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
        </table> 
	   </form> 
	</td>
  </tr>
<%
		}
		else // si ya esta registrado
		{
			Calendar ini = GregorianCalendar.getInstance();
          	ini.add(Calendar.MONTH, -1);
         	String donde;
			if(JUtil.getSesion(request).getID_Usuario().equals("cef-su"))
				donde = "FechaNota >= '" + JUtil.obtFechaSQL(ini) + "'";
			else
				donde = "FechaNota >= '" + JUtil.obtFechaSQL(ini) + "' AND ID_Block IN (select ID_Block from TBL_USUARIOS_SUBMODULO_BLOCKS where ID_Usuario ='" + JUtil.getSesion(request).getID_Usuario() + "')";
          
		  	JNotasSet not = new JNotasSet(request);
			not.m_Where = donde;
			not.m_OrderBy = "FechaNota DESC, ID_Nota ASC"; 
			not.Open();
			
%>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="left" valign="middle"><a href="../forsetiweb/menu_pm.jsp">
				<img src="../imgfsi/menu_principal.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",4) %>" width="24" height="24" border="0"/></a></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040101.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  			</tr>
		</table>
	</td>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr> 
    <td width="30" align="center" class="titGiganteAzc"><%= hola %></td>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>  
  <tr> 
      <td><p class="txtCuerpoBco"><%= JUtil.getSesion(request).getNombreUsuario() + " " + bienvenido %></p>
	  <ul class="txtChicoAzc">
        <li><%= actividades1 %></li>
        <li><%= actividades2 %></li>
        <li><%= actividades3 %></li>
        <li><%= actividades4 %></li>
		<li><%= actividades5 %></li>
      </ul>
      </td>
  </tr>
<%
			if(not.getNumRows() > 0)
			{
%>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="2">
        <tr> 
          <td colspan="3" align="center" bgcolor="#0099FF" class="titCuerpoBco">Notas 
            del &uacute;ltimo mes</td>
        </tr>
        <tr> 
		  <td bgcolor="#999999" class="txtChicoBco" align="left" width="20%">&nbsp;Fecha</td>
          <td bgcolor="#999999" class="txtChicoBco" align="left">Nota/Entidad</td>
          <td bgcolor="#999999" class="txtChicoBco" align="left" width="25%">Usuario&nbsp;</td>
        </tr>
<%
				for(int i = 0; i < not.getNumRows(); i++)
				{
%>
		<tr> 
			<td class="cpoBco" width="20%" align="left" valign="top">
				<% for(short j= 0; j < not.getAbsRow(i).getID_Nivel(); j++) { out.print("&nbsp;&nbsp;&nbsp;"); } %>
		  		<img src="../../imgfsi/<%= (not.getAbsRow(i).getID_Nivel() == 0) ? "flecha.gif" : "flechare.gif" %>"> - 
            <%= JUtil.obtFechaTxt(not.getAbsRow(i).getFecha(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(not.getAbsRow(i).getHora(), "hh:mm") %></td>
          	<td class="cpoBco" align="left" valign="top"><%= not.getAbsRow(i).getMensaje() %> / <%= not.getAbsRow(i).getEtiqueta() %></td>
           	<td class="cpoBco" width="25%" align="left" valign="top"><%= not.getAbsRow(i).getNombre() %> - <a href="/servlet/forseti.mensajes.JNotasCtrl" target="_self">Citar</a></td>
	    </tr>
<%
				}
%>		
     </table>
	</td>
  </tr>  
<%
			}
			
		}
	}
	else
	{
%>
  <tr> 
     <td><p class="textoGris">&nbsp;</p></td>
  </tr>
 <%	
	}
%>
</table>
</body>
</html>
