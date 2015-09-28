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
	String cat_lineas_dlg = (String)request.getAttribute("cat_lineas_dlg");
	if(cat_lineas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("INVSERV_LINEAS").generarTitulo(JUtil.Msj("CEF","INVSERV_LINEAS","VISTA",request.getParameter("proceso"),3));
	
	String Tipos = JUtil.Msj("CEF","INVSERV_LINEAS","VISTA","TIPOS");
	
	JAdmInvservLineasSet lin = new JAdmInvservLineasSet(request);
	JInvServUnidadesSet uni = new JInvServUnidadesSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ELEMENTO") )
	{
		if(JUtil.getSesion(request).getSesion("INVSERV_LINEAS").getEspecial().equals("LINEAS"))
		{
			lin.m_Where = "ID_Linea = '" + JUtil.p(request.getParameter("id")) + "'";
			lin.Open();
		}
		else
		{
			uni.m_Where = "ID_Unidad = '" + JUtil.p(request.getParameter("id")) + "'";
			uni.Open();
		}
	}

%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.clave.value, 1, 8) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.descripcion.value, 1, 50)  )
		return false;
	else
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCatLineasDlg" method="post" enctype="application/x-www-form-urlencoded" name="cat_lineas_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCatLineasCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
  		  	<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040205.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
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
 	  <td height="150" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td  bgcolor="#FFFFFF"> 
	    <table width="100%" border="0" cellspacing="5" cellpadding="5">
		   <tr> 
            <td><input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>">
				<input name="ENTIDAD" type="hidden" value="<%= JUtil.getSesion(request).getSesion("INVSERV_LINEAS").getEspecial() %>">
                <input name="subproceso" type="hidden" value="ENVIAR">
				<%= JUtil.getSesion(request).getSesion("INVSERV_LINEAS").getPanelEntidad() %></td>
          </tr>
          <tr> 
          	<td class="titChicoAzc"><%= JUtil.getSesion(request).getSesion("INVSERV_LINEAS").getEntidadTit() %></td>
		  </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
          </tr>
          <tr> 
            <td><input class="cpoColAzc" name="clave" type="text" style="width:50%" maxlength="8"<% if(request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) { out.print(" readonly=\"true\""); } %>/></td>
          </tr>
		  <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></td>
          </tr>
          <tr> 
            <td>
			<select name="tipo" style="width:90%" class="cpoColAzc"<%= (request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) ? " readonly=\"true\"" : "" %>>
<%
		if(JUtil.getSesion(request).getSesion("INVSERV_LINEAS").getEspecial().equals("LINEAS"))
		{
%>
	                <option value="P"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("P")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) { 
											if(lin.getAbsRow(0).getID_InvServ().equals("P")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,1) %></option>
			     <option value="S"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("S")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) { 
											if(lin.getAbsRow(0).getID_InvServ().equals("S")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,2) %></option>
				  <option value="G"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("G")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) { 
											if(lin.getAbsRow(0).getID_InvServ().equals("G")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,3) %></option>				 
<%
		}
		else
		{
%>
                <option value="P"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("P")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) { 
											if(uni.getAbsRow(0).getID_InvServ().equals("P")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,1) %></option>
			     <option value="S"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("S")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) { 
											if(uni.getAbsRow(0).getID_InvServ().equals("S")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,2) %></option>
				  <option value="G"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("G")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ELEMENTO")) { 
											if(uni.getAbsRow(0).getID_InvServ().equals("G")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,3) %></option>	
<%
		}
%>
					</select>
				</td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
          </tr>
          <tr> 
            <td><input name="descripcion" type="text" id="descripcion" style="width:100%" maxlength="50"/></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
          </tr>
         </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
<%
	if(JUtil.getSesion(request).getSesion("INVSERV_LINEAS").getEspecial().equals("LINEAS"))
	{
%>
document.cat_lineas_dlg.clave.value = '<% if(request.getParameter("clave") != null) { out.print( request.getParameter("clave") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ELEMENTO")) { out.print( lin.getAbsRow(0).getID_Linea() ); } else { out.print(""); } %>'
document.cat_lineas_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ELEMENTO")) { out.print( lin.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
<%
	}
	else
	{
%>
document.cat_lineas_dlg.clave.value = '<% if(request.getParameter("clave") != null) { out.print( request.getParameter("clave") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ELEMENTO")) { out.print( uni.getAbsRow(0).getID_Unidad() ); } else { out.print(""); } %>'
document.cat_lineas_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ELEMENTO")) { out.print( uni.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
<%
	}
%>
</script>
</body>
</html>
