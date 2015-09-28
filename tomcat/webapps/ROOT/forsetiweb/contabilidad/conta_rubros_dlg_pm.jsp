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
	String conta_rubros_dlg = (String)request.getAttribute("conta_rubros_dlg");
	if(conta_rubros_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("CONT_RUBROS").generarTitulo(JUtil.Msj("CEF","CONT_RUBROS","VISTA",request.getParameter("proceso"),3));

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
	if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
	{
		formAct.aceptar.disabled = true;
		return true;
	}
	else
		return false;
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFContaRubrosDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_rubros_dlg">
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
        			<input type="submit" name="aceptar"  value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaRubrosCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040203.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td width="30" align="center" class="titCuerpoAzc"><%= titulo %></td>
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
<%
	if(request.getParameter("proceso").equals("AGREGAR_RUBRO") || request.getParameter("proceso").equals("CAMBIAR_RUBRO"))
	{
		JContaRubrosSetV2 set = new JContaRubrosSetV2(request);
		if( request.getParameter("proceso").equals("CAMBIAR_RUBRO") )
		{
			set.m_Where = "ID = '" + JUtil.p(request.getParameter("Clave")) + "'";
			set.Open();
		}

		String Activo = JUtil.Msj("CEF","CONT_RUBROS","DLG","TIPOS",1);
		String Pasivo = JUtil.Msj("CEF","CONT_RUBROS","DLG","TIPOS",2);
		String Resultados = JUtil.Msj("CEF","CONT_RUBROS","DLG","TIPOS",3);

%>
  <tr> 
    <td> 
	    <table width="100%" border="0" cellspacing="5" cellpadding="0" bgcolor="#FFFFFF">
          <tr> 
            <td>
				<% 	if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) {
						out.println(" <input name=\"Clave\" type=\"hidden\" value=\"" + request.getParameter("Clave") + "\"/>");
					} %>
					<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
					<input name="subproceso" type="hidden" value="ENVIAR"/>
                <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %>
			 </td>
			</tr>
			<tr>
             <td> 
              <select name="tipo" style="width:95%">
                <option value="AC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("AC")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("AC")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Activo,1) %></option>
                <option value="AF" selected="selected"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("AF")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("AF")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Activo,2) %></option>
                <option value="AD"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("AD")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("AD")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Activo,3) %></option>
                <option value="PC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("PC")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("PC")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Pasivo,1) %></option>
                <option value="PL"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("PL")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("PL")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Pasivo,2) %></option>
                <option value="PD"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("PD")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("PD")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Pasivo,3) %></option>
                <option value="CC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("CC")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("CC")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,1) %></option>
                <option value="RI"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RI")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RI")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,2) %></option>
                <option value="RC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RC")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RC")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,3) %></option>
                <option value="RG"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RG")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RG")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,4) %></option>
                <option value="RO"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RO")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RO")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,5) %></option>
                <option value="IP"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("IP")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("IP")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,6) %></option>
              </select></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
		  </tr>
		  <tr>
            <td><input name="nombre" type="text" id="nombre" style="width:100%" maxlength="50"<% if(request.getParameter("nombre") != null) { out.println(" value=\"" + request.getParameter("nombre") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { out.println(" value=\"" + set.getAbsRow(0).getNombre() + "\""); } %>/></td>
          </tr>
		  <tr> 
            <td><%= JUtil.Msj("CEF","CONT_RUBROS","DLG","TIT-ESP") %></td>
		  </tr>
		  <tr>
            <td><input name="desde" type="text" id="desde" style="width:50%" maxlength="4"<% if(request.getParameter("desde") != null) { out.println(" value=\"" + request.getParameter("desde") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { out.println(" value=\"" + set.getAbsRow(0).getDesde() + "\""); } %>/>
              ( 0000 - 9999 )</td>
          </tr>
		  <tr> 
            <td><%= JUtil.Msj("CEF","CONT_RUBROS","DLG","TIT-ESP",2) %></td>
		  </tr>
		  <tr>
            <td><input name="hasta" type="text" id="hasta" style="width:50%" maxlength="4"<% if(request.getParameter("hasta") != null) { out.println(" value=\"" + request.getParameter("hasta") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { out.println(" value=\"" + set.getAbsRow(0).getHasta() + "\""); } %>/>
              ( 0000 - 9999 )</td>
          </tr>
        </table>
      </td>
  </tr>
  <%
	} // fin proceso AGREGAR / CAMBIAR
%>
</table>
</form>
</body>
</html>
