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
<%@ page import="forseti.*, forseti.sets.*, forseti.ventas.*, java.util.*, java.io.*"%>
<%
	String comp_provee_dlg = (String)request.getAttribute("comp_provee_dlg");
	if(comp_provee_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	String id_numero = (String)request.getAttribute("id_numero");
	String titulo =  JUtil.getSesion(request).getSesion("COMP_PROVEE").generarTitulo(JUtil.Msj("CEF","COMP_PROVEE","VISTA",request.getParameter("proceso"),3));
	String etq = JUtil.Msj("CEF","COMP_PROVEE","DLG","ETQ",1);
	String etq2 = JUtil.Msj("CEF","COMP_PROVEE","DLG","ETQ",2);
	String coletq = JUtil.Msj("CEF","COMP_PROVEE","DLG","COLUMNAS");
	String sts = JUtil.Msj("CEF", "COMP_PROVEE", "VISTA", "STATUS", 2);

	JProveeProveeSetV2 smod = new JProveeProveeSetV2(request);
	JProveeProveeMasSetV2 set = new JProveeProveeMasSetV2(request);
	if( request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR") || request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR") )
	{
		smod.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		set.m_Where = "ID_Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		smod.Open();
		set.Open();
	}
	
	JSatBancosSet setBan = new JSatBancosSet(request);
    setBan.m_OrderBy = "Clave ASC";
    setBan.Open();
	
	JSatPaisesSet paisSet = new JSatPaisesSet(request);
	paisSet.ConCat(true);
	paisSet.m_OrderBy = "Nombre ASC";
	paisSet.Open();
	
	JSatEstadosSet estSet = new JSatEstadosSet(request);
	estSet.ConCat(true);
	if(request.getParameter("pais") != null && (request.getParameter("pais").equals("MEX") || request.getParameter("pais").equals("USA") || request.getParameter("pais").equals("CAN")))
		estSet.m_Where = "CodPais3 = '" + JUtil.p(request.getParameter("pais")) + "'";
	else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR") && (set.getAbsRow(0).getPais().equals("MEX") || set.getAbsRow(0).getPais().equals("USA") || set.getAbsRow(0).getPais().equals("CAN")))
		estSet.m_Where = "CodPais3 = '" + set.getAbsRow(0).getPais() + "'";
	else
		estSet.m_Where = "CodPais3 = 'MEX'";
	estSet.m_OrderBy = "Nombre ASC";
	estSet.Open();
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(formAct.proceso.value == "AGREGAR_PROVEEDOR" || formAct.proceso.value == "CAMBIAR_PROVEEDOR")
	{
		if(	!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","SALDO") %>', formAct.saldo.value, -9999999999, 9999999999, 2) ||
			!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>', formAct.numero.value, 1, 9999999999) ||
			!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","CREDITO",3) %>', formAct.limite.value, 0, 9999999999, 2) ||
			!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","CREDITO",2) %>', formAct.dias.value, 0, 999) ||
			!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","DESCUENTO") %>', formAct.descuento.value, 0, 100.00, 2) || 			
		   	!verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>", formAct.cuenta.value))
			return false;
		else
			return true;
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCompProveeDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_provee_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCompProveeCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<input name="subproceso" type="hidden" value="ENVIAR">
		<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
		<input name="id" type="hidden" value="<%= request.getParameter("id")%>"> 
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td colspan="2" class="titChicoAzc" align="center"><%= JUtil.Elm(etq2,1) %></td>
          </tr>
		  <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
            <td><input name="numero" type="text" id="numero" size="15" maxlength="10"<% if(request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR")) { out.print(" readonly=\"true\""); } %>></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
            <td><input name="nombre" type="text" id="nombre" size="80" maxlength="80"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA",2) %></td>
            <td><input name="cuenta" type="text" id="cuenta" size="11" maxlength="25"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=comp_provee_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS&destino=cuenta_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="cuenta_nombre" type="text" id="cuenta_nombre" size="50" maxlength="250" readonly="true"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","RFC") %> / <%= JUtil.Msj("GLB","GLB","GLB","RFC",2) %></td>
            <td><input name="rfc" type="text" id="rfc" size="15" maxlength="40"></td>
          </tr>
		  <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></td>
            <td><select name="status" class="cpoBco">
                <option value="A"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("A")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR") || request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getStatus().equals("A")) {
												out.println(" selected"); 
											}
										}
									 } %>>
                <%= JUtil.Elm(sts,2) %>
                </option>
                <option value="B"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("B")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR") || request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getStatus().equals("B")) {
												out.println(" selected"); 
											}
										}
									 } %>>
                <%= JUtil.Elm(sts,3) %>
                </option>
              </select></td>
          </tr>
          <tr> 
            <td colspan="2" class="titChicoAzc" align="center"><%= JUtil.Elm(etq2,2) %></td>
          </tr>
        </table>
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
		    <td><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
            <td><select name="pais" onChange="javascript:establecerProcesoSVE(this.form.subproceso, 'ACTUALIZAR'); this.form.submit();">
                		<option value=""<% if(request.getParameter("pais") != null) {
										if(request.getParameter("pais").equals("")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getPais().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","PAIS") %> ---</option>
                <%
								  for(int i = 0; i< paisSet.getNumRows(); i++)
								  {
		%>
                <option value="<%= paisSet.getAbsRow(i).getAlfa3() %>"<% 
									if(request.getParameter("pais") != null) {
										if(request.getParameter("pais").equals(paisSet.getAbsRow(i).getAlfa3())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getPais().equals(paisSet.getAbsRow(i).getAlfa3())) {
												out.println(" selected"); 
											}
										}
									 }	  %>> 
                <%=  paisSet.getAbsRow(i).getNombre()  %>
                </option>
                <%
								  }
				%>
              </select></td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
            <td>
<%
	if(request.getParameter("pais") != null)
	{
		if(request.getParameter("pais").equals("MEX") || request.getParameter("pais").equals("USA") || request.getParameter("pais").equals("CAN"))
		{
%>
			  <select name="estado">
                		<option value=""<% if(request.getParameter("estado") != null) {
										if(request.getParameter("estado").equals("")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getEstado().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %> ---</option>
                <%
								  for(int i = 0; i< estSet.getNumRows(); i++)
								  {
		%>
                <option value="<%= estSet.getAbsRow(i).getCodEstado() %>"<% 
									if(request.getParameter("estado") != null) {
										if(request.getParameter("estado").equals(estSet.getAbsRow(i).getCodEstado())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getEstado().equals(estSet.getAbsRow(i).getCodEstado())) {
												out.println(" selected"); 
											}
										}
									 }	  %>> 
                <%=  estSet.getAbsRow(i).getNombre()  %>
                </option>
                <%
								  }
				%>
              </select>
<%
		}
		else
		{
%>  
			  <input name="estado" type="text" id="estado" size="30" maxlength="40">
<%
		}
	}
	else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR"))
	{
		if(set.getAbsRow(0).getPais().equals("MEX") || set.getAbsRow(0).getPais().equals("USA") || set.getAbsRow(0).getPais().equals("CAN"))
		{
%>
			  <select name="estado">
                		<option value=""<% if(request.getParameter("estado") != null) {
										if(request.getParameter("estado").equals("")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getEstado().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %> ---</option>
                <%
								  for(int i = 0; i< estSet.getNumRows(); i++)
								  {
		%>
                <option value="<%= estSet.getAbsRow(i).getCodEstado() %>"<% 
									if(request.getParameter("estado") != null) {
										if(request.getParameter("estado").equals(estSet.getAbsRow(i).getCodEstado())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getEstado().equals(estSet.getAbsRow(i).getCodEstado())) {
												out.println(" selected"); 
											}
										}
									 }	  %>> 
                <%=  estSet.getAbsRow(i).getNombre()  %>
                </option>
                <%
								  }
				%>
              </select>
<%
		}
		else
		{
%>  
			  <input name="estado" type="text" id="estado" size="30" maxlength="40">
<%
		}
	}
	else
	{
%>  
			  <input name="estado" type="text" id="estado" size="30" maxlength="40">
<%
	}
%>
			</td>
						<td>Pedimento</td>
			<td>
			  <select name="pedimento">
                <option value="--"<% 
					   				 if(request.getParameter("pedimento") != null) {
										if(request.getParameter("pedimento").equals("--")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR") || request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getPedimento().equals("--")) {
												out.println(" selected"); 
											}
										}
									 } %>>No requiere pedimento</option>
                <option value="A1"<% 
					   				 if(request.getParameter("pedimento") != null) {
										if(request.getParameter("pedimento").equals("A1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR") || request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getPedimento().equals("A1")) {
												out.println(" selected"); 
											}
										}
									 } %>>Exportacion o Importacion Definitiva</option>
              </select>
			</td>
          </tr>
		  <tr> 
		    <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
            <td><input name="municipio" type="text" id="municipio" size="30" maxlength="40"></td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
            <td colspan="3"><input name="poblacion" type="text" id="poblacion" size="50" maxlength="80"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
            <td><input name="cp" type="text" id="cp" size="7" maxlength="7"></td>
			<td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
            <td colspan="3"><input name="colonia" type="text" id="colonia" size="30" maxlength="40"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
            <td><input name="direccion" type="text" id="direccion" size="50" maxlength="80"></td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
            <td width="15%"> 
              <input name="noext" type="text" id="noext" size="10" maxlength="10">
            </td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
            <td width="15%"> 
              <input name="noint" type="text" id="noint" size="10" maxlength="10">
            </td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","TEL") %></td>
            <td><input name="tel" type="text" id="tel" size="30" maxlength="25"></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","FAX") %></td>
            <td colspan="3"><input name="fax" type="text" id="fax2" size="20" maxlength="10"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
            <td colspan="5"><input name="correo" type="text" id="correo" size="40" maxlength="40"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Elm(etq,1) %></td>
            <td colspan="5"><input name="atnpagos" type="text" id="atnpagos" size="50" maxlength="50"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Elm(etq,2) %></td>
            <td colspan="5"><input name="atncompras" type="text" id="atncompras" size="50" maxlength="50"></td>
          </tr>
		  <tr> 
            <td colspan="6" class="titChicoAzc" align="center"><%= JUtil.Elm(etq2,3) %></td>
          </tr>
        </table>

		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CREDITO",2) %></td>
            <td width="80%"><input name="dias" type="text" id="dias" size="15" maxlength="3"></td>
          </tr>
          <tr> 
            <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CREDITO",3) %></td>
            <td width="80%"><input name="limite" type="text" id="limite" size="20" maxlength="20"></td>
          </tr>
		  <tr> 
            <td width="20%"><%= JUtil.Elm(etq,3) %></td>
            <td width="80%"><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
          <tr> 
            <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","DESCUENTO") %></td>
            <td width="80%" valign="top">
<input name="descuento" type="text" id="descuento" size="15" maxlength="10">
              %</td>
          </tr>
		  <tr> 
            <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA",2) %></td>
            <td width="80%" valign="top"><input name="metododepago" type="text" id="metododepago" size="35" maxlength="254"></td>
          </tr>
		  <tr> 
            <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","BANCO") %></td>
            <td width="80%" valign="top">
			<select name="id_satbanco" class="cpoBco">
<%
		for(int i = 0; i < setBan.getNumRows(); i++)
		{	
%>
							<option value="<%=setBan.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("id_satbanco") != null) {
										if(request.getParameter("id_satbanco").equals(setBan.getAbsRow(i).getClave())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { 
											if(set.getAbsRow(0).getID_SatBanco().equals(setBan.getAbsRow(i).getClave())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= setBan.getAbsRow(i).getNombre() %></option>
<%	
		}
%>				
							</select>
			</td>
          </tr>
          <tr> 
            <td width="20%" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","OBS") %></td>
            <td width="80%"><textarea name="obs" cols="60" rows="3" id="obs"></textarea></td>
          </tr>
        </table>
	
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.comp_provee_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( smod.getAbsRow(0).getNumero() ); } else { if(id_numero == null) { out.print(""); } else { out.print(id_numero); } } %>'
document.comp_provee_dlg.dias.value = '<% if(request.getParameter("dias") != null) { out.print( request.getParameter("dias") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( smod.getAbsRow(0).getDias() ); } else { out.print("0"); } %>' 
document.comp_provee_dlg.limite.value = '<% if(request.getParameter("limite") != null) { out.print( request.getParameter("limite") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getLimiteCredito() ); } else { out.print("0.00"); } %>' 
document.comp_provee_dlg.descuento.value = '<% if(request.getParameter("descuento") != null) { out.print( request.getParameter("descuento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getDescuento() ); } else { out.print("0"); } %>' 

document.comp_provee_dlg.cuenta.value = '<% if(request.getParameter("cuenta") != null) { out.print( request.getParameter("cuenta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( JUtil.obtCuentaFormato(new StringBuffer(smod.getAbsRow(0).getCC()), request) ); } else { out.print(""); } %>'  
document.comp_provee_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( smod.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.comp_provee_dlg.cuenta_nombre.value = '<% if(request.getParameter("cuenta_nombre") != null) { out.print( request.getParameter("cuenta_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getCuentaNombre() ); } else { out.print(""); } %>' 
document.comp_provee_dlg.rfc.value = '<% if(request.getParameter("rfc") != null) { out.print( request.getParameter("rfc") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getRFC() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.direccion.value = '<% if(request.getParameter("direccion") != null) { out.print( request.getParameter("direccion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getDireccion() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.colonia.value = '<% if(request.getParameter("colonia") != null) { out.print( request.getParameter("colonia") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getColonia() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.poblacion.value = '<% if(request.getParameter("poblacion") != null) { out.print( request.getParameter("poblacion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getPoblacion() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.noext.value = '<% if(request.getParameter("noext") != null) { out.print( request.getParameter("noext") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getNoExt() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.noint.value = '<% if(request.getParameter("noint") != null) { out.print( request.getParameter("noint") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getNoInt() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.municipio.value = '<% if(request.getParameter("municipio") != null) { out.print( request.getParameter("municipio") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getMunicipio() ); } else { out.print(""); } %>'  
<%
	if(request.getParameter("pais") != null)
	{
	 	if(request.getParameter("pais").equals("MEX") || request.getParameter("pais").equals("USA") || request.getParameter("pais").equals("CAN"))
		{
		}
		else
		{
%>
document.comp_provee_dlg.estado.value = '<% if(request.getParameter("estado") != null) { out.print( request.getParameter("estado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getEstado() ); } else { out.print(""); } %>'  
<%
		}
	}
	else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR"))
	{
		if(set.getAbsRow(0).getPais().equals("MEX") || set.getAbsRow(0).getPais().equals("USA") || set.getAbsRow(0).getPais().equals("CAN"))
		{
		}
		else
		{
%>
document.comp_provee_dlg.estado.value = '<% if(request.getParameter("estado") != null) { out.print( request.getParameter("estado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getEstado() ); } else { out.print(""); } %>'  
<%
		}
	}
	else
	{
%>
document.comp_provee_dlg.estado.value = '<% if(request.getParameter("estado") != null) { out.print( request.getParameter("estado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getEstado() ); } else { out.print(""); } %>'  
<%
	}
%>
document.comp_provee_dlg.cp.value = '<% if(request.getParameter("cp") != null) { out.print( request.getParameter("cp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getCP() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.fax.value = '<% if(request.getParameter("fax") != null) { out.print( request.getParameter("fax") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getFax() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.tel.value = '<% if(request.getParameter("tel") != null) { out.print( request.getParameter("tel") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( smod.getAbsRow(0).getTel() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.correo.value = '<% if(request.getParameter("correo") != null) { out.print( request.getParameter("correo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( smod.getAbsRow(0).getEMail() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.atnpagos.value = '<% if(request.getParameter("atnpagos") != null) { out.print( request.getParameter("atnpagos") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getAtnPagos() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.atncompras.value = '<% if(request.getParameter("atncompras") != null) { out.print( request.getParameter("atncompras") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( smod.getAbsRow(0).getContacto() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getUltimaCompra(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
document.comp_provee_dlg.metododepago.value = '<% if(request.getParameter("metododepago") != null) { out.print( request.getParameter("metododepago") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getMetodoDePago() ); } else { out.print(JUtil.Msj("GLB","GLB","GLB","METODO_PAGO",2)); } %>'
document.comp_provee_dlg.obs.value = '<% if(request.getParameter("obs") != null) { out.print( request.getParameter("obs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getObs() ); } else { out.print(""); } %>'
</script>
</body>
</html>
