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
	String conta_catcuentas_dlg = (String)request.getAttribute("conta_catcuentas_dlg");
	if(conta_catcuentas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("CONT_CATCUENTAS").generarTitulo(JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JContaCatalogSetV2 set = new JContaCatalogSetV2(request);
	if( request.getParameter("proceso").equals("CAMBIAR_CUENTA") )
	{
		set.m_Where = "Cuenta = '" + JUtil.p(JUtil.obtCuentas(request.getParameter("cuenta"), (byte)19)) + "'";
		set.Open();
	}
	String Status = JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","STATUS",2);
	String Naturaleza = JUtil.Msj("CEF","CONT_CATCUENTAS","DLG","NATURALEZA");
		
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
	if(formAct.proceso.value == "AGREGAR_CUENTA")
	{
		if(verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>", formAct.cuenta.value) == true)
		{
			if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
			{
				formAct.aceptar.disabled = true;
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFContaCatcuentasDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_catcuentas_dlg" target="_self">
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
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaCatCuentasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	<tr> 
      <td> 
        <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td><div align="right">
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
				<!--input name="nivel" type="hidden" value=" JUtil.getSesion(request).getNivelCC() "-->
                <%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %></div></td>
            <td><input class="cpoColAzc" name="cuenta" type="text" id="cuenta" size="15" maxlength="25"<% if(request.getParameter("cuenta") != null) { out.println(" value=\"" + request.getParameter("cuenta") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { out.println(" value=\"" + JUtil.obtCuentaFormato(new StringBuffer(set.getAbsRow(0).getCuenta()), request) + "\""); } %><%= (request.getParameter("proceso").equals("CAMBIAR_CUENTA")) ? " readonly=\"true\"" : "" %>>
            </td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></div></td>
            <td><input name="nombre" type="text" id="nombre" size="35" maxlength="50"<% if(request.getParameter("nombre") != null) { out.println(" value=\"" + request.getParameter("nombre") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { out.print(" value=\"" + set.getAbsRow(0).getNombre() + "\""); } %>></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></div></td>
            <td>
				<select name="estatus">
                <option value="A"<% if(request.getParameter("estatus") != null) {
										if(request.getParameter("estatus").equals("A")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { 
											if(set.getAbsRow(0).getEstatus().equals("A")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Status,2) %></option>
                <option value="D"<% if(request.getParameter("estatus") != null) {
										if(request.getParameter("estatus").equals("D")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { 
											if(set.getAbsRow(0).getEstatus().equals("D")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Status,3) %></option>
                </select>
				</td>
          </tr>
 		  <tr> 
            <td> <div align="right">&nbsp;</div></td>
            <td><input type="checkbox" name="ac" value="ac"<% 
					if( request.getParameter("proceso").equals("CAMBIAR_CUENTA") && request.getParameter("subproceso") == null ) { 
						out.print( (set.getAbsRow(0).getAC() ? " checked" : "" ) ); } 
					else if(request.getParameter("ac") != null ) { 
						out.print(" checked"); } else { out.print(""); } %>>
              <%= JUtil.Msj("CEF","CONT_CATCUENTAS","DLG","TIT-ESP") %></td>
          </tr>
		  <tr> 
            <td> <div align="right"><%= JUtil.Msj("CEF","CONT_CATCUENTAS","DLG","TIT-ESP",2) %></div></td>
            <td><input name="codagrup" type="text" id="codagrup" size="5" maxlength="12"<% if(request.getParameter("codagrup") != null) { out.println(" value=\"" + request.getParameter("codagrup") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { out.print(" value=\"" + set.getAbsRow(0).getCE_CodAgrup() + "\""); } %>></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("CEF","CONT_CATCUENTAS","DLG","TIT-ESP",3) %></div></td>
            <td>
				<select name="natur">
                <option value="R"<% if(request.getParameter("natur") != null) {
										if(request.getParameter("natur").equals("R")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { 
											if(set.getAbsRow(0).getCE_Natur().equals("R")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Naturaleza,1) %></option>
                <option value="D"<% if(request.getParameter("natur") != null) {
										if(request.getParameter("natur").equals("D")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { 
											if(set.getAbsRow(0).getCE_Natur().equals("D")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Naturaleza,2) %></option>
				<option value="A"<% if(request.getParameter("natur") != null) {
										if(request.getParameter("natur").equals("A")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CUENTA")) { 
											if(set.getAbsRow(0).getCE_Natur().equals("A")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Naturaleza,3) %></option>
                </select>
				</td>
          </tr>
        </table>
      </td>
  </tr>
</table>
</form>
</body>
</html>
