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
	
	String titulo =  JUtil.getSesion(request).getSesion("CONT_RUBROS").generarTitulo(JUtil.Msj("CEF","CONT_RUBROS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040203.html");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
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
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFContaRubrosDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_rubros_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clockCef"> 
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar"  value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaRubrosCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table> 
      </td>
    </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 	  <td height="109" bgcolor="#333333">&nbsp;</td>
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
	    <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td><div align="right">
				<% 	if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) {
						out.println(" <input name=\"Clave\" type=\"hidden\" value=\"" + request.getParameter("Clave") + "\">");
					} %>
					<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"><input name="subproceso" type="hidden" value="ENVIAR">
                <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></div></td>
            <td> 
              <select name="tipo">
			    <option value="AC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("AC")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("AC")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Activo,1) %></option>
                <option value="AF"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("AF")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("AF")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Activo,2) %></option>
                <option value="AD"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("AD")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("AD")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Activo,3) %></option>
                <option value="PC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("PC")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("PC")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Pasivo,1) %></option>
                <option value="PL"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("PL")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("PL")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Pasivo,2) %></option>
                <option value="PD"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("PD")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("PD")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Pasivo,3) %></option>
                <option value="CC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("CC")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("CC")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,1) %></option>
                <option value="RI"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RI")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RI")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,2) %></option>
                <option value="RC"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RC")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RC")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,3) %></option>
                <option value="RG"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RG")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RG")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,4) %></option>
                <option value="RO"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("RO")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("RO")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,5) %></option>
                <option value="IP"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("IP")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { 
											if(set.getAbsRow(0).getClave().equals("IP")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Resultados,6) %></option>
              </select> </td>
          </tr>
          <tr> 
            <td> 
              <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></div></td>
            <td><input name="nombre" type="text" id="nombre" size="30" maxlength="50"<% if(request.getParameter("nombre") != null) { out.println(" value=\"" + request.getParameter("nombre") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { out.println(" value=\"" + set.getAbsRow(0).getNombre() + "\""); } %>></td>
          </tr>
		  <tr> 
            <td> 
              <div align="right"><%= JUtil.Msj("CEF","CONT_RUBROS","DLG","TIT-ESP") %></div></td>
            <td><input name="desde" type="text" id="desde" size="10" maxlength="4"<% if(request.getParameter("desde") != null) { out.println(" value=\"" + request.getParameter("desde") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { out.println(" value=\"" + set.getAbsRow(0).getDesde() + "\""); } %>>
              ( 0000 - 9999 )</td>
          </tr>
		  <tr> 
            <td> 
              <div align="right"><%= JUtil.Msj("CEF","CONT_RUBROS","DLG","TIT-ESP",2) %></div></td>
            <td><input name="hasta" type="text" id="hasta" size="10" maxlength="4"<% if(request.getParameter("hasta") != null) { out.println(" value=\"" + request.getParameter("hasta") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_RUBRO")) { out.println(" value=\"" + set.getAbsRow(0).getHasta() + "\""); } %>>
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
