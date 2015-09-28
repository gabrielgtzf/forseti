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
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker_pm.js" >
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
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCompProveeDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_provee_dlg" target="_self">
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
			    <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar"  value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCompProveeCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
		  <td colspan="2" class="titChicoAzc" align="center">
		  	<input name="subproceso" type="hidden" value="ENVIAR"/>
			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/> 
			<input name="id" type="hidden" value="<%= request.getParameter("id")%>"/> 
		      <%= JUtil.Elm(etq2,1) %></td>
          </tr>
		  <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
          </tr>
		  <tr> 
            <td colspan="2"><input name="numero" type="text" id="numero" style="width:25%" maxlength="10"<% if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE")) { out.print(" readonly=\"true\""); } %>/></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
          </tr>
		  <tr> 
            <td colspan="2"><input name="nombre" type="text" id="nombre" style="width:100%" maxlength="80"/></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA",2) %></td>
          </tr>
		  <tr> 
            <td colspan="2"><table width="100%"><tr><td width="30%"><input name="cuenta" type="text" id="cuenta" style="width:100%" maxlength="25"/></td>
			<td width="24"><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg_pm.jsp?formul=comp_provee_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS&destino=cuenta_nombre')"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="24" height="24" border="0"/></a></td> 
            <td><input name="cuenta_nombre" type="text" id="cuenta_nombre" style="width:100%" maxlength="250" readonly="true"/></td></tr></table></td>
          </tr>
          <tr> 
            <td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","RFC") %></td>
			<td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></td>
          </tr>
          <tr> 
            <td><input name="rfc" type="text" id="rfc" style="width:90%" maxlength="15"/></td>
          	<td><select name="status" class="cpoBco" style="width:100%">
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
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
          </tr>
          <tr> 
            <td colspan="2"><input name="direccion" type="text" id="direccion" style="width:90%" maxlength="80"/></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
          	<td><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
		  </tr>
          <tr> 
            <td><input name="noext" type="text" id="noext" style="width:50%" maxlength="10"/></td>
            <td><input name="noint" type="text" id="noint" style="width:50%" maxlength="10"/></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
          </tr>
          <tr> 
            <td colspan="2"><input name="colonia" type="text" id="colonia" style="width:90%" maxlength="40"/></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
          </tr>
          <tr> 
          	<td><input name="poblacion" type="text" id="poblacion" style="width:100%" maxlength="80"/></td>
          	<td><input name="cp" type="text" id="cp" style="width:50%" maxlength="7"/></td>
		  </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
          </tr>
          <tr> 
            <td><input name="municipio" type="text" id="municipio" style="width:100%" maxlength="40"/></td>
            <td><input name="estado" type="text" id="estado" style="width:100%" maxlength="40"/></td> 
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
          </tr>
          <tr> 
            <td colspan="2"><input name="pais" type="text" id="pais" style="width:50%" maxlength="20"/></td>
          </tr>
          <tr> 
		    <td colspan="2">
			<table width="100%">
			  <tr>
			    <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","TEL") %></td>
            	<td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","FAX") %></td>
				<td><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
			  </tr>	
			  <tr>
			  	<td><input name="tel" type="text" id="tel" style="width:100%" maxlength="25"/></td>
            	<td><input name="fax" type="text" id="fax" style="width:100%" maxlength="10"/></td>
			  	<td><input name="correo" type="text" id="correo" style="width:100%"maxlength="40"/></td>
			  </tr>
			</table>
			</td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Elm(etq,1) %></td>
          </tr>
          <tr> 
            <td colspan="2"><input name="atnpagos" type="text" id="atnpagos" style="width:100%" maxlength="50"/></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Elm(etq,2) %></td>
          </tr>
          <tr> 
            <td colspan="2"><input name="atncompras" type="text" id="atncompras" style="width:100%" maxlength="50"/></td>
          </tr>
		  <tr> 
            <td colspan="2" class="titChicoAzc" align="center"><%= JUtil.Elm(etq2,3) %></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","CREDITO",2) %></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","CREDITO",3) %></td>
          </tr>
          <tr> 
            <td><input name="dias" type="text" id="dias" style="width:50%" maxlength="3"/></td>
          	<td><input name="limite" type="text" id="limite" style="width:100%" maxlength="20"/></td>
		  </tr>
          <tr> 
            <td><%= JUtil.Elm(etq,3) %></td>
			<td align="right">% <%= JUtil.Msj("GLB","GLB","GLB","DESCUENTO") %></td>
          </tr>
          <tr> 
            <td><table width="100%"><tr><td><input name="fecha" type="text" id="fecha" style="width:100%" maxlength="15" readonly="true"></td> 
              <td width="24"><a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" width="24" height="24" border="0" align="absmiddle"/></a></td></tr></table></td>
            <td align="right"><input name="descuento" type="text" id="descuento" style="width:80%" maxlength="10"/></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","METODO_PAGO") %></td>
          </tr>
		  <tr> 
            <td colspan="2"><input name="metododepago" type="text" id="metododepago" style="width:90%" maxlength="254"/></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","OBS") %></td>
          </tr>
		  <tr> 
            <td colspan="2"><textarea name="obs" style="width:100%" rows="3" id="obs"></textarea></td>
          </tr>
        </table>
	
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.comp_provee_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( smod.getAbsRow(0).getNumero() ); } else { out.print(""); } %>'
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
document.comp_provee_dlg.estado.value = '<% if(request.getParameter("estado") != null) { out.print( request.getParameter("estado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getEstado() ); } else { out.print(""); } %>'  
document.comp_provee_dlg.pais.value = '<% if(request.getParameter("pais") != null) { out.print( request.getParameter("pais") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR")) { out.print( set.getAbsRow(0).getPais() ); } else { out.print(""); } %>'  
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
