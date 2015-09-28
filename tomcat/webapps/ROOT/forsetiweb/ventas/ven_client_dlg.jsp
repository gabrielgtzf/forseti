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
	String ven_client_dlg = (String)request.getAttribute("ven_client_dlg");
	if(ven_client_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	String id_numero = (String)request.getAttribute("id_numero");
	String titulo =  JUtil.getSesion(request).getSesion("VEN_CLIENT").generarTitulo(JUtil.Msj("CEF","VEN_CLIENT","VISTA",request.getParameter("proceso"),3));
	String etq = JUtil.Msj("CEF","VEN_CLIENT","DLG","ETQ",1);
	String etq2 = JUtil.Msj("CEF","VEN_CLIENT","DLG","ETQ",2);
	String coletq = JUtil.Msj("CEF","VEN_CLIENT","DLG","COLUMNAS");
	String sts = JUtil.Msj("CEF", "VEN_CLIENT", "VISTA", "STATUS", 2);

	JClientClientSetV2 smod = new JClientClientSetV2(request);
	JClientClientMasSetV2 set = new JClientClientMasSetV2(request);
	if( request.getParameter("proceso").equals("CAMBIAR_CLIENTE") || request.getParameter("proceso").equals("CONSULTAR_CLIENTE") )
	{
		smod.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		set.m_Where = "ID_Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		smod.Open();
		set.Open();
	}
	
	JSatBancosSet setBan = new JSatBancosSet(request);
    setBan.m_OrderBy = "Clave ASC";
    setBan.Open();

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
	if(formAct.proceso.value == "AGREGAR_CLIENTE" || formAct.proceso.value == "CAMBIAR_CLIENTE")
	{
		if(	!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>', formAct.numero.value, 1, 9999999999) ||
			!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","CREDITO",3) %>', formAct.limite.value, 0, 9999999999, 2) ||
			!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","CREDITO",2) %>', formAct.dias.value, 0, 999) ||
			!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","VENDEDOR") %>', formAct.idvendedor.value, 0, 30000) ||
			!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","DESCUENTO") %>', formAct.descuento.value, 0, 100.00, 2) || 			
		   	!verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>", formAct.cuenta.value))
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
	else
	{	
		return false;
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFVenClientDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_client_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_CLIENTE")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFVenClientCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td><input name="numero" type="text" id="numero" size="15" maxlength="10"<% if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE")) { out.print(" readonly=\"true\""); } %>></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
            <td><input name="nombre" type="text" id="nombre" size="80" maxlength="80"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA",2) %></td>
            <td><input name="cuenta" type="text" id="cuenta" size="11" maxlength="25"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_client_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS&destino=cuenta_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="cuenta_nombre" type="text" id="cuenta_nombre" size="50" maxlength="250" readonly="true"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","RFC") %></td>
            <td><input name="rfc" type="text" id="rfc" size="15" maxlength="15"></td>
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
										if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE") || request.getParameter("proceso").equals("CONSULTAR_CLIENTE")) { 
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
										if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE") || request.getParameter("proceso").equals("CONSULTAR_CLIENTE")) { 
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
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
            <td><input name="colonia" type="text" id="colonia" size="30" maxlength="40"></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
            <td colspan="3"><input name="poblacion" type="text" id="poblacion" size="50" maxlength="80"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
            <td><input name="municipio" type="text" id="municipio" size="30" maxlength="40"></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
            <td colspan="3"><input name="cp" type="text" id="cp" size="7" maxlength="7"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
            <td><input name="estado" type="text" id="estado" size="30" maxlength="40"> 
            </td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
            <td colspan="3"><input name="pais" type="text" id="pais" size="15" maxlength="20"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","TEL") %></td>
            <td><input name="tel" type="text" id="tel" size="30" maxlength="25"></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","FAX") %></td>
            <td colspan="3"><input name="fax" type="text" id="fax2" size="20" maxlength="10"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
            <td><input name="correo" type="text" id="correo" size="40" maxlength="40"></td>
          	<td>Envio de CFDI por correo</td>
			<td colspan="3">
				<select name="smtp">
                	<option value="0"<% 
					   				 if(request.getParameter("smtp") != null) {
										if(request.getParameter("smtp").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE") || request.getParameter("proceso").equals("CONSULTAR_CLIENTE")) { 
											if(smod.getAbsRow(0).getSMTP() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>No Enviar</option>
                      <option value="1"<% 
					   				 if(request.getParameter("smtp") != null) {
										if(request.getParameter("smtp").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE") || request.getParameter("proceso").equals("CONSULTAR_CLIENTE")) { 
											if(smod.getAbsRow(0).getSMTP() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Enviar manualmente</option>
						<option value="2"<% 
					   				 if(request.getParameter("smtp") != null) {
										if(request.getParameter("smtp").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE") || request.getParameter("proceso").equals("CONSULTAR_CLIENTE")) { 
											if(smod.getAbsRow(0).getSMTP() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Envio automatico al sellar</option>
                    </select>
			</td>
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
            <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","VENDEDOR") %></td>
            <td width="80%"><input name="idvendedor" type="text" id="idvendedor" size="7" maxlength="3"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_client_dlg&lista=idvendedor&idcatalogo=23&nombre=VENDEDORES&destino=vendedor_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="vendedor_nombre" type="text" id="vendedor_nombre" size="50" maxlength="250" readonly="true"></td>
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
              %&nbsp;&nbsp; 
              <input name="prespmostr" type="checkbox" id="prespmostr">
              <%= JUtil.Elm(etq,4) %></td>
          </tr>
		  <tr> 
            <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","METODO_PAGO") %></td>
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
										if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { 
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
document.ven_client_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( smod.getAbsRow(0).getNumero() ); } else { if(id_numero == null) { out.print(""); } else { out.print(id_numero); } } %>'
document.ven_client_dlg.dias.value = '<% if(request.getParameter("dias") != null) { out.print( request.getParameter("dias") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( smod.getAbsRow(0).getDias() ); } else { out.print("0"); } %>' 
document.ven_client_dlg.limite.value = '<% if(request.getParameter("limite") != null) { out.print( request.getParameter("limite") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getLimiteCredito() ); } else { out.print("0.00"); } %>' 
document.ven_client_dlg.descuento.value = '<% if(request.getParameter("descuento") != null) { out.print( request.getParameter("descuento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getDescuento() ); } else { out.print("0"); } %>' 

document.ven_client_dlg.cuenta.value = '<% if(request.getParameter("cuenta") != null) { out.print( request.getParameter("cuenta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( JUtil.obtCuentaFormato(new StringBuffer(smod.getAbsRow(0).getCC()), request) ); } else { out.print(""); } %>'  
document.ven_client_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( smod.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.ven_client_dlg.cuenta_nombre.value = '<% if(request.getParameter("cuenta_nombre") != null) { out.print( request.getParameter("cuenta_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getCuentaNombre() ); } else { out.print(""); } %>' 
document.ven_client_dlg.rfc.value = '<% if(request.getParameter("rfc") != null) { out.print( request.getParameter("rfc") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getRFC() ); } else { out.print(""); } %>'  
document.ven_client_dlg.direccion.value = '<% if(request.getParameter("direccion") != null) { out.print( request.getParameter("direccion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getDireccion() ); } else { out.print(""); } %>'  
document.ven_client_dlg.colonia.value = '<% if(request.getParameter("colonia") != null) { out.print( request.getParameter("colonia") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getColonia() ); } else { out.print(""); } %>'  
document.ven_client_dlg.poblacion.value = '<% if(request.getParameter("poblacion") != null) { out.print( request.getParameter("poblacion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getPoblacion() ); } else { out.print(""); } %>'  
document.ven_client_dlg.noext.value = '<% if(request.getParameter("noext") != null) { out.print( request.getParameter("noext") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getNoExt() ); } else { out.print(""); } %>'  
document.ven_client_dlg.noint.value = '<% if(request.getParameter("noint") != null) { out.print( request.getParameter("noint") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getNoInt() ); } else { out.print(""); } %>'  
document.ven_client_dlg.municipio.value = '<% if(request.getParameter("municipio") != null) { out.print( request.getParameter("municipio") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getMunicipio() ); } else { out.print(""); } %>'  
document.ven_client_dlg.estado.value = '<% if(request.getParameter("estado") != null) { out.print( request.getParameter("estado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getEstado() ); } else { out.print(""); } %>'  
document.ven_client_dlg.pais.value = '<% if(request.getParameter("pais") != null) { out.print( request.getParameter("pais") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getPais() ); } else { out.print(""); } %>'  
document.ven_client_dlg.cp.value = '<% if(request.getParameter("cp") != null) { out.print( request.getParameter("cp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getCP() ); } else { out.print(""); } %>'  
document.ven_client_dlg.fax.value = '<% if(request.getParameter("fax") != null) { out.print( request.getParameter("fax") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getFax() ); } else { out.print(""); } %>'  
document.ven_client_dlg.tel.value = '<% if(request.getParameter("tel") != null) { out.print( request.getParameter("tel") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( smod.getAbsRow(0).getTel() ); } else { out.print(""); } %>'  
document.ven_client_dlg.correo.value = '<% if(request.getParameter("correo") != null) { out.print( request.getParameter("correo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( smod.getAbsRow(0).getEMail() ); } else { out.print(""); } %>'  
document.ven_client_dlg.atnpagos.value = '<% if(request.getParameter("atnpagos") != null) { out.print( request.getParameter("atnpagos") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getAtnPagos() ); } else { out.print(""); } %>'  
document.ven_client_dlg.atncompras.value = '<% if(request.getParameter("atncompras") != null) { out.print( request.getParameter("atncompras") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( smod.getAbsRow(0).getContacto() ); } else { out.print(""); } %>'  
document.ven_client_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getUltimaCompra(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
document.ven_client_dlg.metododepago.value = '<% if(request.getParameter("metododepago") != null) { out.print( request.getParameter("metododepago") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getMetodoDePago() ); } else { out.print(JUtil.Msj("GLB","GLB","GLB","METODO_PAGO",2)); } %>'
document.ven_client_dlg.obs.value = '<% if(request.getParameter("obs") != null) { out.print( request.getParameter("obs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getObs() ); } else { out.print(""); } %>'
document.ven_client_dlg.prespmostr.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_CLIENTE") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_CLIENTE") ) { out.print( (set.getAbsRow(0).getPrecioEspMostr() ? "true" : "false" ) ); } else if(request.getParameter("prespmostr") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.ven_client_dlg.idvendedor.value = '<% if(request.getParameter("idvendedor") != null) { out.print( request.getParameter("idvendedor") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getID_Vendedor() ); } else { out.print("0"); } %>'  
document.ven_client_dlg.vendedor_nombre.value = '<% if(request.getParameter("vendedor_nombre") != null) { out.print( request.getParameter("vendedor_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE")) { out.print( set.getAbsRow(0).getVendedorNombre() ); } else { out.print(""); } %>' 

</script>
</body>
</html>
