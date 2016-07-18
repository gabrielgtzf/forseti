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
<%@ page import="forseti.*, forseti.sets.*, forseti.compras.*, forseti.ventas.*, java.util.*, java.io.*"%>
<%
	String ven_fact_dlg = (String)request.getAttribute("ven_fact_dlg");
	if(ven_fact_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String ID = (String)request.getAttribute("ID_Factura");
	if(ID == null)
		ID = JUtil.p(request.getParameter("ID"));
	
	String idmod = (String)request.getAttribute("idmod");
	String moddes = (String)request.getAttribute("moddes");
	
	String titulo =  JUtil.getSesion(request).getSesion(idmod).generarTitulo(JUtil.Msj("CEF",idmod,"VISTA",request.getParameter("proceso"),3));

	JComercioExteriorCabSet set = new JComercioExteriorCabSet(request,"VENTA");
	set.m_Where = "ID_VC = '" + ID + "'";
	set.Open();
	
	JComercioExteriorDetSet det = new JComercioExteriorDetSet(request,"VENTA");
	det.m_Where = "ID_VC = '" + ID + "'";
	det.m_OrderBy = "Partida ASC";
	det.Open();
	
	String entven = JUtil.getSesion(request).getSesion(idmod).getEspecial();
	
	JSatIncotermsSet inc = new JSatIncotermsSet(request);
	inc.Open();
	
	JSatUnidadesSet uni = new JSatUnidadesSet(request);
	uni.m_OrderBy = "Clave ASC";
	uni.Open();
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
// Funciones de forseti
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFVenFactDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_fact_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 && request.getAttribute("ID_Factura") == null) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFVenFactCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
		  <tr bgcolor="#CCCCCC"> 
            <td colspan="6" class="titChico" align="center"><input name="subproceso" type="hidden" value="ENVIAR"> 
              <input name="proceso" type="hidden" value="DATOS_EXPORTACION"> 
              <input name="ID" type="hidden" value="<%= ID %>"> 
              <input name="tipomov" type="hidden" value="<%= request.getParameter("tipomov") %>">Datos Generales de Exportacion</td>
          </tr>
          <tr> 
            <td width="15%">Tipo de operacion</td>
            <td>
			<select name="tipooperacion">
                <option value="-"<% if(request.getParameter("tipooperacion") != null) {
										if(request.getParameter("tipooperacion").equals("-")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getTipoOperacion().equals("-")) {
										out.println(" selected"); 
									} %>>----- Tipo de operacion -----</option>
                <option value="2"<% if(request.getParameter("tipooperacion") != null) {
										if(request.getParameter("tipooperacion").equals("2")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getTipoOperacion().equals("2")) {
										out.println(" selected"); 
									} %>>Exportacion de bienes o productos</option>
                <option value="A"<% if(request.getParameter("tipooperacion") != null) {
										if(request.getParameter("tipooperacion").equals("A")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getTipoOperacion().equals("A")) {
										out.println(" selected"); 
									} %>>Exportacion de servicios</option>
              </select>
			</td>
			<td width="15%">Certificado de origen</td>
            <td><select name="certificadoorigen">
                <option value="-1"<% if(request.getParameter("certificadoorigen") != null) {
										if(request.getParameter("certificadoorigen").equals("-1")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getCertificadoOrigen() == -1) {
										out.println(" selected"); 
									} %>>--- No Aplica ---</option>
                <option value="0"<% if(request.getParameter("certificadoorigen") != null) {
										if(request.getParameter("certificadoorigen").equals("0")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getCertificadoOrigen() == 0) {
										out.println(" selected"); 
									} %>>No funje como certificado de origen</option>
                <option value="1"<% if(request.getParameter("certificadoorigen") != null) {
										if(request.getParameter("certificadoorigen").equals("1")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getCertificadoOrigen() == 1) {
										out.println(" selected"); 
									} %>>Funje como certificado de origen</option>
              </select></td>
			<td width="15%">No. del certificado de origen</td>
            <td><input name="numcertificadoorigen" type="text" id="numcertificadoorigen" size="20" maxlength="40" value="<%= ( (request.getParameter("numcertificadoorigen") == null) ? set.getAbsRow(0).getNumCertificadoOrigen() : request.getParameter("numcertificadoorigen") ) %>"></td>
          </tr>
		  <tr> 
		  	<td width="15%">No. de exportador confiable</td>
            <td><input name="numeroexportadorconfiable" type="text" id="numeroexportadorconfiable" size="25" maxlength="50" value="<%= ( (request.getParameter("numeroexportadorconfiable") == null) ? set.getAbsRow(0).getNumeroExportadorConfiable() : request.getParameter("numeroexportadorconfiable") ) %>"></td>
          	<td width="15%">Termino comercial</td>
            <td colspan="3">
			  <select name="incoterm">
                <option value=""<% if(request.getParameter("incoterm") != null) {
										if(request.getParameter("incoterm").equals("")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getIncoterm().equals("---")) {
										out.println(" selected"); 
									} %>>--- Selecciona el Incoterm ---</option>
<%
					for(int i = 0; i < inc.getNumRows(); i++)
					{
%>
                <option value="<%= inc.getAbsRow(i).getClave() %>"<% 
									if(request.getParameter("incoterm") != null) { 
										if(request.getParameter("incoterm").equals(inc.getAbsRow(i).getClave())) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getIncoterm().equals(inc.getAbsRow(i).getClave())) {
										out.println(" selected"); 
									} %>><%= inc.getAbsRow(i).getClave() + " - " + inc.getAbsRow(i).getDescripcion() %></option>
<%
					}
%>
              </select>
			</td>
		  </tr>
		  <tr>
			<td width="15%">Subdivision en factura</td>
            <td><select name="subdivision">
				<option value="-1"<% if(request.getParameter("subdivision") != null) {
										if(request.getParameter("subdivision").equals("-1")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getSubdivision() == -1) {
										out.println(" selected"); 
									} %>>--- No Aplica ---</option>
                <option value="0"<% if(request.getParameter("subdivision") != null) {
										if(request.getParameter("subdivision").equals("0")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getSubdivision() == 0) {
										out.println(" selected"); 
									} %>>Sin subdivision</option>
                <option value="1"<% if(request.getParameter("subdivision") != null) {
										if(request.getParameter("subdivision").equals("1")) {
											out.print(" selected");
										}
									}
									else if(set.getAbsRow(0).getSubdivision() == 1) {
										out.println(" selected"); 
									} %>>Factura subdividida</option>
              </select></td>
			 <td width="15%">Observaciones</td>
			 <td colspan="3"><input name="observaciones" type="text" id="observaciones" size="80" maxlength="300" value="<%= ( (request.getParameter("observaciones") == null) ? set.getAbsRow(0).getObservaciones() : request.getParameter("observaciones") ) %>"></td>
			</tr>
			<tr>
			 <td width="15%">Total</td>
			 <td><input name="totalusd" type="text" id="totalusd" size="15" maxlength="20" value="<%= ( (request.getParameter("totalusd") != null) ? request.getParameter("totalusd") : ((set.getAbsRow(0).getTotalUsd() != 0.00) ? set.getAbsRow(0).getTotalUsd() : "") ) %>"></td>
			 <td width="15%">Tipo de cambio</td>
			 <td colspan="3"><input name="tipocambiousd" type="text" id="tipocambiousd" size="10" maxlength="20" value="<%= ( (request.getParameter("tipocambiousd") != null) ? request.getParameter("tipocambiousd") : ((set.getAbsRow(0).getTipoCambioUsd() != 0.00) ? set.getAbsRow(0).getTipoCambioUsd() : "") ) %>"></td>
			</tr>
			<tr bgcolor="#CCCCCC"> 
             <td colspan="6" class="titChico" align="center">Emisor</td>
            </tr>
			<tr>
			 <td width="15%">CURP</td>
			 <td colspan="5"><input name="emisor_curp" type="text" id="emisor_curp" size="40" maxlength="40" value="<%= ( (request.getParameter("emisor_curp") == null) ? set.getAbsRow(0).getEmisor_Curp() : request.getParameter("emisor_curp") ) %>"></td>
			</tr>
			<tr bgcolor="#CCCCCC"> 
             <td colspan="6" class="titChico" align="center">Receptor</td>
            </tr>
			<tr>
			 <td width="15%">CURP</td>
			 <td><input name="receptor_curp" type="text" id="receptor_curp" size="40" maxlength="40" value="<%= ( (request.getParameter("receptor_curp") == null) ? set.getAbsRow(0).getReceptor_Curp() : request.getParameter("receptor_curp") ) %>"></td>
			 <td width="15%">Registro Tributario</td>
			 <td colspan="3"><input name="receptor_numregidtrib" type="text" id="receptor_numregidtrib" size="40" maxlength="40" value="<%= ( (request.getParameter("receptor_numregidtrib") == null) ? set.getAbsRow(0).getReceptor_NumRegIdTrib() : request.getParameter("receptor_numregidtrib") ) %>"></td>
			</tr>
			<tr bgcolor="#CCCCCC"> 
             <td colspan="6" class="titChico" align="center">Destinatario</td>
            </tr>
			<tr>
			 <td width="15%">CURP</td>
			 <td><input name="destinatario_curp" type="text" id="destinatario_curp" size="40" maxlength="40" value="<%= ( (request.getParameter("destinatario_curp") == null) ? set.getAbsRow(0).getDestinatario_Curp() : request.getParameter("destinatario_curp") ) %>"></td>
			 <td width="15%">Registro Tributario</td>
			 <td><input name="destinatario_numregidtrib" type="text" id="destinatario_numregidtrib" size="40" maxlength="40" value="<%= ( (request.getParameter("destinatario_numregidtrib") == null) ? set.getAbsRow(0).getDestinatario_NumRegIdTrib() : request.getParameter("destinatario_numregidtrib") ) %>"></td>
			 <td width="15%">RFC</td>
			 <td><input name="destinatario_rfc" type="text" id="destinatario_rfc" size="15" maxlength="15" value="<%= ( (request.getParameter("destinatario_rfc") == null) ? set.getAbsRow(0).getDestinatario_RFC() : request.getParameter("destinatario_rfc") ) %>"></td>
			</tr>
			<tr>
			 <td width="15%">Nombre</td>
			 <td colspan="3"><input name="destinatario_nombre" type="text" id="destinatario_nombre" size="80" maxlength="300" value="<%= ( (request.getParameter("destinatario_nombre") == null) ? set.getAbsRow(0).getDestinatario_Nombre() : request.getParameter("destinatario_nombre") ) %>"></td>
			 <td width="15%">Referencia del domicilio</td>
			 <td><input name="destinatario_domicilio_referencia" type="text" id="destinatario_domicilio_referencia" size="40" maxlength="250" value="<%= ( (request.getParameter("destinatario_domicilio_referencia") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_Referencia() : request.getParameter("destinatario_domicilio_referencia") ) %>"></td>
			</tr>
			<tr> 
             <td colspan="6" class="titChicoNeg" align="center">Datos del domicilio</td>
            </tr>
			<tr>
			 <td width="15%">Calle</td>
			 <td><input name="destinatario_domicilio_calle" type="text" id="destinatario_domicilio_calle" size="30" maxlength="100" value="<%= ( (request.getParameter("destinatario_domicilio_calle") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_Calle() : request.getParameter("destinatario_domicilio_calle") ) %>"></td>
			 <td width="15%">No. Exterior</td>
			 <td><input name="destinatario_domicilio_numeroexterior" type="text" id="destinatario_domicilio_numeroexterior" size="10" maxlength="55" value="<%= ( (request.getParameter("destinatario_domicilio_numeroexterior") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_NumeroExterior() : request.getParameter("destinatario_domicilio_numeroexterior") ) %>"></td>
			 <td width="15%">No. Interior</td>
			 <td><input name="destinatario_domicilio_numerointerior" type="text" id="destinatario_domicilio_numerointerior" size="10" maxlength="55" value="<%= ( (request.getParameter("destinatario_domicilio_numerointerior") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_NumeroInterior() : request.getParameter("destinatario_domicilio_numerointerior") ) %>"></td>
			</tr>
			<tr>
			 <td width="15%">Colonia</td>
			 <td><input name="destinatario_domicilio_colonia" type="text" id="destinatario_domicilio_colonia" size="30" maxlength="120" value="<%= ( (request.getParameter("destinatario_domicilio_colonia") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_Colonia() : request.getParameter("destinatario_domicilio_colonia") ) %>"></td>
			 <td width="15%">Localidad</td>
			 <td><input name="destinatario_domicilio_localidad" type="text" id="destinatario_domicilio_localidad" size="30" maxlength="120" value="<%= ( (request.getParameter("destinatario_domicilio_localidad") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_Localidad() : request.getParameter("destinatario_domicilio_localidad") ) %>"></td>
			 <td width="15%">Municipio</td>
			 <td><input name="destinatario_domicilio_municipio" type="text" id="destinatario_domicilio_municipio" size="30" maxlength="120" value="<%= ( (request.getParameter("destinatario_domicilio_municipio") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_Municipio() : request.getParameter("destinatario_domicilio_municipio") ) %>"></td>
			</tr>
			<tr>
			 <td width="15%">Codigo Postal</td>
			 <td><input name="destinatario_domicilio_codigopostal" type="text" id="destinatario_domicilio_codigopostal" size="12" maxlength="12" value="<%= ( (request.getParameter("destinatario_domicilio_codigopostal") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_CodigoPostal() : request.getParameter("destinatario_domicilio_codigopostal") ) %>"></td>
			 <td width="15%">Estado</td>
			 <td><input name="destinatario_domicilio_estado" type="text" id="destinatario_domicilio_estado" size="20" maxlength="30" value="<%= ( (request.getParameter("destinatario_domicilio_estado") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_Estado() : request.getParameter("destinatario_domicilio_estado") ) %>"></td>
			 <td width="15%">Pais</td>
			 <td><input name="destinatario_domicilio_pais" type="text" id="destinatario_domicilio_pais" size="3" maxlength="3" value="<%= ( (request.getParameter("destinatario_domicilio_pais") == null) ? set.getAbsRow(0).getDestinatario_Domicilio_Pais() : request.getParameter("destinatario_domicilio_pais") ) %>"></td>
			</tr>
			<tr bgcolor="#0099FF"> 
             <td colspan="6" class="titChico" align="center">Mercancias</td>
            </tr>
			<tr> 
             <td colspan="6">
			 	<table width="100%" border="0" cellspacing="2" cellpadding="0">
				  <tr bgcolor="#CCCCCC">
				  	<td width="20%" class="titChico">Clave</td>
					<td width="35%" class="titChico">Arancel</td>
					<td width="10%" class="titChico" align="right">Cantidad</td>
					<td width="10%" class="titChico" align="center">Unidad</td>
					<td width="10%" class="titChico" align="right">Costo</td>
					<td width="15%" class="titChico" align="right">Valor</td>
				  </tr>
<%
			for(int i = 0; i < det.getNumRows(); i++)
			{
%>
				  <tr>
				  	<td><input name="noidentificacion_<%= det.getAbsRow(i).getPartida() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter("noidentificacion_" + det.getAbsRow(i).getPartida()) == null) ? det.getAbsRow(i).getNoIdentificacion() : request.getParameter("noidentificacion_" + det.getAbsRow(i).getPartida()) ) %>" readonly="true"></td>
					<td><input name="fraccionarancelaria_<%= det.getAbsRow(i).getPartida() %>" type="text" size="20" maxlength="12" value="<%= ( (request.getParameter("fraccionarancelaria_" + det.getAbsRow(i).getPartida()) == null) ? det.getAbsRow(i).getFraccionArancelaria() : request.getParameter("fraccionarancelaria_" + det.getAbsRow(i).getPartida()) ) %>"></td>
					<td align="right"><input name="cantidadaduana_<%= det.getAbsRow(i).getPartida() %>" type="text" size="12" maxlength="20" value="<%= ( (request.getParameter("cantidadaduana_" + det.getAbsRow(i).getPartida()) == null) ? det.getAbsRow(i).getCantidadAduana() : request.getParameter("cantidadaduana_" + det.getAbsRow(i).getPartida()) ) %>"></td>
					<td align="right">
					  <select name="unidadaduana_<%= det.getAbsRow(i).getPartida() %>" class="cpoBco">
<%
				for(int j = 0; j < uni.getNumRows(); j++)
				{	
%>
							<option value="<%=uni.getAbsRow(j).getClave()%>"<% 
									if(request.getParameter("unidadaduana_" + det.getAbsRow(i).getPartida()) != null) {
										if(request.getParameter("unidadaduana_" + det.getAbsRow(i).getPartida()).equals(Integer.toString(uni.getAbsRow(j).getClave()))) {
											out.print(" selected");
										}
									 } else {
										if(det.getAbsRow(i).getUnidadAduana() == uni.getAbsRow(j).getClave()) {
											out.print(" selected"); 
										}
									 }
									 %>><%= uni.getAbsRow(j).getDescripcion() %></option>
<%	
				}
%>
						</select></td>						
					<td align="right"><input name="valorunitarioaduana_<%= det.getAbsRow(i).getPartida() %>" type="text" size="12" maxlength="20" value="<%= ( (request.getParameter("valorunitarioaduana_" + det.getAbsRow(i).getPartida()) == null) ? det.getAbsRow(i).getValorUnitarioAduana() : request.getParameter("valorunitarioaduana_" + det.getAbsRow(i).getPartida()) ) %>"></td>
					<td align="right"><input name="valordolares_<%= det.getAbsRow(i).getPartida() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter("valordolares_" + det.getAbsRow(i).getPartida()) == null) ? det.getAbsRow(i).getValorDolares() : request.getParameter("valordolares_" + det.getAbsRow(i).getPartida()) ) %>"></td>
				  </tr>
  				  <tr>
				    <td colspan="6">
					  <table width="100%" border="0" cellspacing="2" cellpadding="0">
				      	<tr>
						  <td width="25%">Marca</td>
						  <td width="25%">Modelo</td>
					      <td width="25%">Sub-Modelo</td>
					      <td width="25%">Serie</td>
						</tr>
<%
				JComercioExteriorDetDescEspSet esp = new JComercioExteriorDetDescEspSet(request,"VENTA");
				esp.m_Where = "ID_VC = '" + ID + "' and Partida = '" + det.getAbsRow(i).getPartida() + "'";
				esp.m_OrderBy = "Descripcion ASC";
				esp.Open();
			
				for(int j = 0; j < esp.getNumRows(); j++)
				{
%>
						<tr>
						  <td><input name="marca_<%= esp.getAbsRow(j).getPartida() %>_<%= esp.getAbsRow(j).getDescripcion() %>" type="text" size="25" maxlength="35" value="<%= ( (request.getParameter("marca_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) == null) ? esp.getAbsRow(j).getMarca() : request.getParameter("marca_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) ) %>"></td>
						  <td><input name="modelo_<%= esp.getAbsRow(j).getPartida() %>_<%= esp.getAbsRow(j).getDescripcion() %>" type="text" size="35" maxlength="80" value="<%= ( (request.getParameter("modelo_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) == null) ? esp.getAbsRow(j).getModelo() : request.getParameter("modelo_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) ) %>"></td>
					      <td><input name="submodelo_<%= esp.getAbsRow(j).getPartida() %>_<%= esp.getAbsRow(j).getDescripcion() %>" type="text" size="30" maxlength="50" value="<%= ( (request.getParameter("submodelo_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) == null) ? esp.getAbsRow(j).getSubModelo() : request.getParameter("submodelo_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) ) %>"></td>
					      <td><input name="numeroserie_<%= esp.getAbsRow(j).getPartida() %>_<%= esp.getAbsRow(j).getDescripcion() %>" type="text" size="25" maxlength="40" value="<%= ( (request.getParameter("numeroserie_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) == null) ? esp.getAbsRow(j).getNumeroSerie() : request.getParameter("numeroserie_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()) ) %>"></td>
						</tr>
<%
				}
%>
					  </table>
					</td>
				  </tr>
<%
			}
%>
				</table>
			 </td>
            </tr>
         </table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
