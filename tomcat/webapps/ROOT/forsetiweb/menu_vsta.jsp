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
<%@ page import="forseti.*, forseti.sets.*"%>
<%
	String glb_menu = (String)request.getAttribute("glb_menu");
	if(glb_menu == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String modulo =  JUtil.getSesion(request).getSesion("GLB_MENU").getEspecial();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!-- 
if(top.location == self.location) {
	top.location.href = "../forsetiweb/forseti.html"
}
if(parent.tiempo.document.URL.indexOf('tiempo.html') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/tiempo.html"
}
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFMenuCtrl" method="post" enctype="application/x-www-form-urlencoded" name="glb_menu" target="_self">
<div id="topbar">
<input name="modulo" type="hidden" value="ACTUALIZAR">
<table border="0" cellspacing="10" cellpadding="0" align="right">
 <tr>
 	<td>&nbsp;</td>
<%
if(JUtil.getSesion(request).getPermiso("CONT"))
{
%>
 	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'CONTABILIDAD')" src="../imgfsi/contabilidad.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("INVSERV"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'CATALOGOS')" src="../imgfsi/catalogos.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("BANCAJ"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'CAJA_Y_BANCOS')" src="../imgfsi/caja y bancos.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("ALM"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'ALMACEN')" src="../imgfsi/almacen.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("COMP"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'COMPRAS')" src="../imgfsi/compras.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("VEN"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'VENTAS')" src="../imgfsi/ventas.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("PROD"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'PRODUCCION')" src="../imgfsi/produccion.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("NOM"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'NOMINA')" src="../imgfsi/nomina.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("ADM"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'CENTRO_DE_CONTROL')" src="../imgfsi/centro de control.png" border="0"></td>
<%
}
if(JUtil.getSesion(request).getPermiso("REP"))
{
%>
	<td align="center"><input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.modulo, 'REPORTES')" src="../imgfsi/reportes.png" border="0"></td>
<%
}
%>
  </tr> 
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="105">&nbsp;</td>
 	</tr>
<%
	if(modulo.equals("CONTABILIDAD"))
	{
%>
 	<tr>
    <td>
	  <table width="100%" cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc" valign="bottom"><%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("CONT_CATCUENTAS")) {%><td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_catcuentas.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_RUBROS")) {%><td align="center"><a href="/servlet/CEFContaRubrosCtrl"><img src="../imgfsi/cont_rubros.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_TIPOPOLIZA")) {%><td align="center"><a href="/servlet/CEFContaTipoPolizasCtrl"><img src="../imgfsi/cont_tipopoliza.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_POLIZAS")) {%><td align="center"><a href="/servlet/CEFContaPolizasCtrl"><img src="../imgfsi/cont_polizas.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_ENLACES")) {%><td align="center"><a href="/servlet/CEFContaEnlacesCtrl"><img src="../imgfsi/cont_enlaces.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_POLCIERRE")) {%><td align="center"><a href="/servlet/CEFContaPolizasCierreCtrl"><img src="../imgfsi/cont_polcierre.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("CONT_CATCUENTAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_RUBROS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","RUBROS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_TIPOPOLIZA")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","TIPOPOLIZA") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_POLIZAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLIZAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_ENLACES")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","ENLACES") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_POLCIERRE")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLCIERRE") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("CATALOGOS"))
	{
%>
  <tr>
    <td>
	  <table width="100%" cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc" valign="bottom"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_LINEAS")) {%><td align="center"><a href="/servlet/CEFCatLineasCtrl"><img src="../imgfsi/invserv_lineas.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_PROD")) {%><td align="center"><a href="/servlet/CEFCatProdCtrl"><img src="../imgfsi/invserv_productos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_SERV")) {%><td align="center"><a href="/servlet/CEFCatServCtrl"><img src="../imgfsi/invserv_servicios.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_GASTOS")) {%><td align="center"><a href="/servlet/CEFCatGastosCtrl"><img src="../imgfsi/invserv_gastos.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_LINEAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","LINEAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_PROD")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","PROD") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_SERV")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","SERV") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_GASTOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","GASTOS") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("CAJA_Y_BANCOS"))
	{
%>
  <tr>
    <td>
 	  <table width="100%" cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc" valign="bottom"><%= JUtil.Msj("CEF","MENU","GLB","BANCAJ") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_BANCOS")) {%><td align="center"><a href="/servlet/CEFMovBancariosCtrl"><img src="../imgfsi/bancaj_bancos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_CAJAS")) {%><td align="center"><a href="/servlet/CEFMovCajaCtrl"><img src="../imgfsi/bancaj_cajas.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_VALES")) {%><td align="center"><a href="/servlet/CEFValesCajaCtrl"><img src="../imgfsi/bancaj_vales.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_CIERRES")) {%><td align="center"><a href="/servlet/CEFCierresCajaCtrl"><img src="../imgfsi/bancaj_cierres.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_BANCOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","BANCOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_CAJAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","CAJAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_VALES")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","VALES") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_CIERRES")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","CIERRES") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("ALMACEN"))
	{
%>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","ALM") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("ALM_MOVIM")) {%><td align="center"><a href="/servlet/CEFAlmMovimientosCtrl"><img src="../imgfsi/alm_movim.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_MOVPLANT")) {%><td align="center"><a href="/servlet/CEFAlmMovimPlantCtrl"><img src="../imgfsi/alm_movplant.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_TRASPASOS")) {%><td align="center"><a href="/servlet/CEFAlmTraspasosCtrl"><img src="../imgfsi/alm_traspasos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_REQUERIMIENTOS")) {%><td align="center"><a href="/servlet/CEFAlmRequerimientosCtrl"><img src="../imgfsi/alm_requerimientos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_CHFIS")) {%><td align="center"><a href="/servlet/CEFAlmChFisCtrl"><img src="../imgfsi/alm_chfis.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_UTENSILIOS")) {%><td align="center"><a href="/servlet/CEFAlmUtensiliosCtrl"><img src="../imgfsi/alm_utensilios.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("ALM_MOVIM")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ALM","MOVIM") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_MOVPLANT")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ALM","MOVPLANT") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_TRASPASOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ALM","TRASPASOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_REQUERIMIENTOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ALM","REQUERIMIENTOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_CHFIS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ALM","CHFIS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_UTENSILIOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ALM","UTENSILIOS") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("COMPRAS"))
	{
%>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","COMP") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("COMP_PROVEE")) {%><td align="center"><a href="/servlet/CEFCompProveeCtrl"><img src="../imgfsi/comp_provee.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_CXP")) {%><td align="center"><a href="/servlet/CEFCompCXPCtrl"><img src="../imgfsi/comp_cxp.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_ORD")) {%><td align="center"><a href="/servlet/CEFCompOrdenesCtrl"><img src="../imgfsi/comp_ord.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_REC")) {%><td align="center"><a href="/servlet/CEFCompRecepcionesCtrl"><img src="../imgfsi/comp_rec.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_FAC")) {%><td align="center"><a href="/servlet/CEFCompFactCtrl"><img src="../imgfsi/comp_fac.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_DEV")) {%><td align="center"><a href="/servlet/CEFCompDevolucionesCtrl"><img src="../imgfsi/comp_dev.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_POL")) {%><td align="center"><a href="/servlet/CEFCompPoliticasCtrl"><img src="../imgfsi/comp_pol.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_GAS")) {%><td align="center"><a href="/servlet/CEFCompGastosCtrl"><img src="../imgfsi/comp_gas.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("COMP_PROVEE")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","PROVEE") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_CXP")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","CXP") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_ORD")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","ORD") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_REC")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","REC") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_FAC")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","FAC") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_DEV")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","DEV") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_POL")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","POL") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_GAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","COMP","GAS") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("VENTAS"))
	{
%>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","VEN") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("VEN_CLIENT")) {%><td align="center"><a href="/servlet/CEFVenClientCtrl"><img src="../imgfsi/ven_client.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_CXC")) {%><td align="center"><a href="/servlet/CEFVenCXCCtrl"><img src="../imgfsi/ven_cxc.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_COT")) {%><td align="center"><a href="/servlet/CEFVenCotizacionesCtrl"><img src="../imgfsi/ven_cot.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_PED")) {%><td align="center"><a href="/servlet/CEFVenPedidosCtrl"><img src="../imgfsi/ven_ped.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_REM")) {%><td align="center"><a href="/servlet/CEFVenRemisionesCtrl"><img src="../imgfsi/ven_rem.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_FAC")) {%><td align="center"><a href="/servlet/CEFVenFactCtrl"><img src="../imgfsi/ven_fac.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_DEV")) {%><td align="center"><a href="/servlet/CEFVenDevolucionesCtrl"><img src="../imgfsi/ven_dev.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_POL")) {%><td align="center"><a href="/servlet/CEFVenPoliticasCtrl"><img src="../imgfsi/ven_pol.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("VEN_CLIENT")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","CLIENT") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_CXC")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","CXC") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_COT")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","COT") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_PED")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","PED") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_REM")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","REM") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_FAC")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","FAC") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_DEV")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","DEV") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_POL")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","VEN","POL") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("PRODUCCION"))
	{
%>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","PROD") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("PROD_FORMULAS")) {%><td align="center"><a href="/servlet/CEFProdFormulasCtrl"><img src="../imgfsi/prod_formulas.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("PROD_PRODUCCION")) {%><td align="center"><a href="/servlet/CEFProdProduccionCtrl"><img src="../imgfsi/prod_produccion.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("PROD_FORMULAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","PROD","FORMULAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("PROD_PRODUCCION")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","PROD","PRODUCCION") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("NOMINA"))
	{
%>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","NOM") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
<%
		if(JUtil.getSesion(request).getPermiso("NOM_MOVIMIENTOS") || JUtil.getSesion(request).getPermiso("NOM_DEPARTAMENTOS") || 
			JUtil.getSesion(request).getPermiso("NOM_TURNOS") || JUtil.getSesion(request).getPermiso("NOM_CATEGORIAS") || JUtil.getSesion(request).getPermiso("NOM_EMPLEADOS"))
		{	 
%>  
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titChicoAzc"><%= JUtil.Msj("CEF","MENU","NOM","CAT") %></td>
		</tr>
	  </table>
	 </td>
	</tr>
	<tr>
	 <td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_MOVIMIENTOS")) {%><td align="center"><a href="/servlet/CEFNomMovimNomCtrl"><img src="../imgfsi/nom_movimientos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_DEPARTAMENTOS")) {%><td align="center"><a href="/servlet/CEFNomDepartamentosCtrl"><img src="../imgfsi/nom_departamentos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_TURNOS")) {%><td align="center"><a href="/servlet/CEFNomTurnosCtrl"><img src="../imgfsi/nom_turnos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_CATEGORIAS")) {%><td align="center"><a href="/servlet/CEFNomCategoriasCtrl"><img src="../imgfsi/nom_categorias.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_EMPLEADOS")) {%><td align="center"><a href="/servlet/CEFNomEmpleadosCtrl"><img src="../imgfsi/nom_empleados.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_MOVIMIENTOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","MOVIMIENTOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_DEPARTAMENTOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","DEPARTAMENTOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_TURNOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","TURNOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_CATEGORIAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","CATEGORIAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_EMPLEADOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","EMPLEADOS") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
		}
		if(JUtil.getSesion(request).getPermiso("NOM_ISR") || JUtil.getSesion(request).getPermiso("NOM_IMSS") || JUtil.getSesion(request).getPermiso("NOM_CREDSAL") ||
			JUtil.getSesion(request).getPermiso("NOM_AGUINALDO") || JUtil.getSesion(request).getPermiso("NOM_VACACIONES")) 
		{
%>  
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titChicoAzc"><%= JUtil.Msj("CEF","MENU","NOM","TBL") %></td>
		</tr>
	  </table>
	 </td>
	</tr>
	<tr>
	 <td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_ISR")) {%><td align="center"><a href="/servlet/CEFNomIsrCtrl"><img src="../imgfsi/nom_isr.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_IMSS")) {%><td align="center"><a href="/servlet/CEFNomImssCtrl"><img src="../imgfsi/nom_imss.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_CREDSAL")) {%><td align="center"><a href="/servlet/CEFNomCredSalCtrl"><img src="../imgfsi/nom_credsal.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_AGUINALDO")) {%><td align="center"><a href="/servlet/CEFNomAguinaldoCtrl"><img src="../imgfsi/nom_aguinaldo.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_VACACIONES")) {%><td align="center"><a href="/servlet/CEFNomVacacionesCtrl"><img src="../imgfsi/nom_vacaciones.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_ISR")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","ISR") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_IMSS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","IMSS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_CREDSAL")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","CREDSAL") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_AGUINALDO")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","AGUINALDO") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_VACACIONES")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","VACACIONES") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
		}
		if(JUtil.getSesion(request).getPermiso("NOM_ASISTENCIAS") || JUtil.getSesion(request).getPermiso("NOM_PERMISOS") ||
			JUtil.getSesion(request).getPermiso("NOM_PLANTILLAS") || JUtil.getSesion(request).getPermiso("NOM_FONACOT")) 
		{ 
%>  
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titChicoAzc"><%= JUtil.Msj("CEF","MENU","NOM","DAT") %></td>
		</tr>
	  </table>
	 </td>
	</tr>
	<tr>
	 <td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_ASISTENCIAS")) {%><td align="center"><a href="/servlet/CEFNomAsistenciasCtrl"><img src="../imgfsi/nom_asistencias.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_PERMISOS")) {%><td align="center"><a href="/servlet/CEFNomPermisosCtrl"><img src="../imgfsi/nom_permisos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_PLANTILLAS")) {%><td align="center"><a href="/servlet/CEFNomPlantillasCtrl"><img src="../imgfsi/nom_plantillas.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_FONACOT")) {%><td align="center"><a href="/servlet/CEFNomFonacotCtrl"><img src="../imgfsi/nom_fonacot.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_ASISTENCIAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","ASISTENCIAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_PERMISOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","PERMISOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_PLANTILLAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","PLANTILLAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_FONACOT")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","FONACOT") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
		}
		if(JUtil.getSesion(request).getPermiso("NOM_CIERRE") || JUtil.getSesion(request).getPermiso("NOM_NOMINA")) 
		{
%>  
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titChicoAzc"><%= JUtil.Msj("CEF","MENU","NOM","PROC") %></td>
		</tr>
	  </table>
	 </td>
	</tr>
	<tr>
	 <td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_CIERRE")) {%><td align="center"><a href="/servlet/CEFNomCierreDiarioCtrl"><img src="../imgfsi/nom_cierre.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_NOMINA")) {%><td align="center"><a href="/servlet/CEFNomMovDirCtrl"><img src="../imgfsi/nom_nomina.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("NOM_CIERRE")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","CIERRE") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_NOMINA")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","NOM","NOMINA") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
		}
	}
	else if(modulo.equals("CENTRO_DE_CONTROL"))
	{
%>   
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","ADM") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<%if(JUtil.getSesion(request).getPermiso("ADM_SALDOS")) {%><td align="center"><a href="/servlet/CEFAdmSaldosCtrl"><img src="../imgfsi/adm_saldos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_USUARIOS")) {%><td align="center"><a href="/servlet/CEFAdmUsuariosCtrl"><img src="../imgfsi/adm_usuarios.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_ENTIDADES")) {%><td align="center"><a href="/servlet/CEFAdmEntidadesCtrl"><img src="../imgfsi/adm_entidades.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_VENDEDORES")) {%><td align="center"><a href="/servlet/CEFAdmVendedoresCtrl"><img src="../imgfsi/adm_vendedores.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_CFDI")) {%><td align="center"><a href="/servlet/CEFAdmCFDCtrl"><img src="../imgfsi/adm_cfdi.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_PERIODOS")) {%><td align="center"><a href="/servlet/CEFAdmPeriodosCtrl"><img src="../imgfsi/adm_periodos.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_MONEDAS")) {%><td align="center"><a href="/servlet/CEFAdmMonedasCtrl"><img src="../imgfsi/adm_monedas.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_VARIABLES")) {%><td align="center"><a href="/servlet/CEFAdmVariablesCtrl"><img src="../imgfsi/adm_variables.png"/></a></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_FORMATOS")) {%><td align="center"><a href="/servlet/CEFAdmFormatosCtrl"><img src="../imgfsi/adm_formatos.png"/></a></td><%}%>
		</tr>
     	<tr>
<%if(JUtil.getSesion(request).getPermiso("ADM_SALDOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","SALDOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_USUARIOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","USUARIOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_ENTIDADES")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","ENTIDADES") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_VENDEDORES")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","VENDEDORES") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_CFDI")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","CFDI") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_PERIODOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","PERIODOS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_MONEDAS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","MONEDAS") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_VARIABLES")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","VARIABLES") %></td><%}%>
<%if(JUtil.getSesion(request).getPermiso("ADM_FORMATOS")) {%><td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","FORMATOS") %></td><%}%>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
	else if(modulo.equals("REPORTES"))
	{
%>   
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=CONT"><img src="../imgfsi/rep_contabilidad.png"/></a></td>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=INVSERV"><img src="../imgfsi/rep_catalogos.png"/></a></td>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=BANCAJ"><img src="../imgfsi/rep_caja y bancos.png"/></a></td>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=ALM"><img src="../imgfsi/rep_almacen.png"/></a></td>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=COMP"><img src="../imgfsi/rep_compras.png"/></a></td>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=VEN"><img src="../imgfsi/rep_ventas.png"/></a></td>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=PROD"><img src="../imgfsi/rep_produccion.png"/></a></td>
<td align="center"><a href="/servlet/CEFReportesCtrl?tipo=NOM"><img src="../imgfsi/rep_nomina.png"/></a></td>
		</tr>
     	<tr>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %></td>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","BANCAJ") %></td>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","ALM") %></td>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","COMP") %></td>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","VEN") %></td>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","PROD") %></td>
<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","GLB","NOM") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
<%
	}
%>
</table>
</form>
</body>
</html>
