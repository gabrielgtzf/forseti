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
	String conta_enlaces_dlg = (String)request.getAttribute("conta_enlaces_dlg");
	if(conta_enlaces_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String ent = JUtil.getSesion(request).getSesion("CONT_ENLACES").getEspecial();
	
	String titulo =  JUtil.getSesion(request).getSesion("CONT_ENLACES").generarTitulo(JUtil.Msj("CEF","CONT_ENLACES","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	String etq = JUtil.Msj("CEF","CONT_ENLACES","DLG","ETQ");

	JAdmInvservCostosConceptosSet invSet = new JAdmInvservCostosConceptosSet(request);
	JAdmProveeCXPConceptos cxpSet = new JAdmProveeCXPConceptos(request);
	JAdmClientCXCConceptos cxcSet = new JAdmClientCXCConceptos(request);

	if( request.getParameter("proceso").equals("CAMBIAR_ENLACE") )
	{
		if(ent.equals("ALMACEN"))
		{
			invSet.m_Where = "ID_Concepto = '" + JUtil.p(request.getParameter("id")) + "'";
			invSet.Open();
		}
		else if(ent.equals("CXP"))
		{
			cxpSet.m_Where = "ID_Concepto = '" + JUtil.p(request.getParameter("id")) + "'";
			cxpSet.Open();
		}
		else if(ent.equals("CXC"))
		{
			cxcSet.m_Where = "ID_Concepto = '" + JUtil.p(request.getParameter("id")) + "'";
			cxcSet.Open();
		}
	}

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
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.idconcepto.value, 0, 199) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.descripcion.value, 1, 80) ||
		!verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>", formAct.cuenta.value) )
		return false;
	else
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
		{
			return false;
		}	
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFContaEnlacesDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_enlaces_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaEnlacesCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
<%
	if(ent.equals("ALMACEN"))
	{
%>
          <tr> 
            <td><div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
			    <input name="ENTIDAD" type="hidden" value="<%= ent %>">
    			<input name="subproceso" type="hidden" value="ENVIAR">
                <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></div></td>
            <td><input class="cpoColAzc" name="idconcepto" type="text" size="12" maxlength="10"<% if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { out.print(" readonly=\"true\""); } %>> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></div></td>
            <td><input name="descripcion" type="text" id="nombre" size="40" maxlength="80"></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></div></td>
            <td>
				<select class="cpoColAzc" name="tipo">
                 <option value="ENT"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("ENT")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { 
											if(invSet.getAbsRow(0).getTipo().equals("ENT")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,1) %></option>
					<option value="SAL"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("SAL")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { 
											if(invSet.getAbsRow(0).getTipo().equals("SAL")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,2) %></option>
              	</select></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %></div></td>
            <td><input name="cuenta" type="text" id="cuenta" size="11" maxlength="25"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=conta_enlaces_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS&destino=cuenta_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="cuenta_nombre" type="text" id="cuenta_nombre" size="50" maxlength="250" readonly="true"></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="checkbox" name="recosto" value="recosto"<% if( (request.getParameter("proceso").equals("CAMBIAR_ENLACE") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR") ) { out.print( (invSet.getAbsRow(0).getRecalcularCosto() ? " checked" : "" ) ); } else if(request.getParameter("recosto") != null ) { out.print(" checked"); } else { out.print(""); } %>>
             <%= JUtil.Msj("CEF","CONT_ENLACES","DLG","ETQ",2) %></td>
          </tr>
<%
	} 
	else
	{
%>
          <tr> 
            <td><div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
			    <input name="ENTIDAD" type="hidden" value="<%= ent %>">
    			<input name="subproceso" type="hidden" value="ENVIAR">
                <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></div></td>
            <td><input class="cpoColAzc" name="idconcepto" type="text" size="12" maxlength="10"<% if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { out.print(" readonly=\"true\""); } %>> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></div></td>
            <td><input name="descripcion" type="text" id="nombre" size="40" maxlength="80"></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></div></td>
            <td>
<%
		if(ent.equals("CXP"))
		{
%>
              <select name="tipo" class="cpoColAzc">
                <option value="ALT"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("ALT")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { 
											if(cxpSet.getAbsRow(0).getTipo().equals("ALT")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,3) %></option>
                <option value="SAL"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("SAL")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { 
											if(cxpSet.getAbsRow(0).getTipo().equals("SAL")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,4) %></option>
              </select> 
              <%
		}
		else if(ent.equals("CXC"))
		{
%>
              <select name="tipo" class="cpoColAzc">
                <option value="ALT"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("ALT")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { 
											if(cxcSet.getAbsRow(0).getTipo().equals("ALT")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,3) %></option>
                <option value="SAL"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("SAL")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENLACE")) { 
											if(cxcSet.getAbsRow(0).getTipo().equals("SAL")) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,4) %></option>
              </select> 
              <%
		}
%>				
				</td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %></div></td>
            <td><input name="cuenta" type="text" id="cuenta" size="11" maxlength="25"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=conta_enlaces_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS&destino=cuenta_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="cuenta_nombre" type="text" id="cuenta_nombre" size="50" maxlength="250" readonly="true"></td>
          </tr>
<%
	}
%>
          <tr> 
            <td colspan="2">&nbsp; </td>
          </tr>
        </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
<%
	if(ent.equals("ALMACEN"))
	{
%>
document.conta_enlaces_dlg.idconcepto.value = '<% if(request.getParameter("idconcepto") != null) { out.print( request.getParameter("idconcepto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( invSet.getAbsRow(0).getID_Concepto() ); } else { out.print(""); } %>'
document.conta_enlaces_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( invSet.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.conta_enlaces_dlg.cuenta.value = '<% if(request.getParameter("cuenta") != null) { out.print( request.getParameter("cuenta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( JUtil.obtCuentaFormato(invSet.getAbsRow(0).getCC(), request) ); } else { out.print(""); } %>'  
document.conta_enlaces_dlg.cuenta_nombre.value = '<% if(request.getParameter("cuenta_nombre") != null) { out.print( request.getParameter("cuenta_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( invSet.getAbsRow(0).getCC() ); } else { out.print(""); } %>' 
<%
	} // Fin almacen
	else if(ent.equals("CXP"))
	{
%>
document.conta_enlaces_dlg.idconcepto.value = '<% if(request.getParameter("idconcepto") != null) { out.print( request.getParameter("idconcepto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( cxpSet.getAbsRow(0).getID_Concepto() ); } else { out.print(""); } %>'
document.conta_enlaces_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( cxpSet.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.conta_enlaces_dlg.cuenta.value = '<% if(request.getParameter("cuenta") != null) { out.print( request.getParameter("cuenta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( JUtil.obtCuentaFormato(cxpSet.getAbsRow(0).getCC(), request) ); } else { out.print(""); } %>'  
document.conta_enlaces_dlg.cuenta_nombre.value = '<% if(request.getParameter("cuenta_nombre") != null) { out.print( request.getParameter("cuenta_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( cxpSet.getAbsRow(0).getCC() ); } else { out.print(""); } %>' 
<%
	}
	else	if(ent.equals("CXC"))
	{
%>
document.conta_enlaces_dlg.idconcepto.value = '<% if(request.getParameter("idconcepto") != null) { out.print( request.getParameter("idconcepto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( cxcSet.getAbsRow(0).getID_Concepto() ); } else { out.print(""); } %>'
document.conta_enlaces_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( cxcSet.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.conta_enlaces_dlg.cuenta.value = '<% if(request.getParameter("cuenta") != null) { out.print( request.getParameter("cuenta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( JUtil.obtCuentaFormato(cxcSet.getAbsRow(0).getCC(), request) ); } else { out.print(""); } %>'  
document.conta_enlaces_dlg.cuenta_nombre.value = '<% if(request.getParameter("cuenta_nombre") != null) { out.print( request.getParameter("cuenta_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENLACE")) { out.print( cxcSet.getAbsRow(0).getCC() ); } else { out.print(""); } %>' 
<%
	} // Fin almacen
%>
</script>
</body>
</html>
