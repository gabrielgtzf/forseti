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
<%@ page import="forseti.JUtil" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="right" valign="middle"> 
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040101.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a>
			</td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= JUtil.Msj("GLB","GLB","GLB","MENU") %></td>
  </tr>
  <tr> 
    <td>
	  <table width="90%" border="1" bordercolor="#333333" bordercolordark="#333333" bordercolorlight="#333333" align="center" cellpadding="5" cellspacing="0" bgcolor="#CCCCCC">
	    <tr><td colspan="2" class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %></td></tr>
		<tr><td width="24"><img src="../imgfsi/cont_catcuentas.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFContaCatCuentasCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/cont_rubros.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFContaRubrosCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","RUBROS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/cont_tipopoliza.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFContaTipoPolizasCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","TIPOPOLIZA") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/cont_polizas.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFContaPolizasCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLIZAS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/cont_enlaces.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFContaEnlacesCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","ENLACES") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/cont_polcierre.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFContaPolCierreCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLCIERRE") %></a></td></tr>
     	
		<tr><td colspan="2" class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td></tr>
		<tr><td width="24"><img src="../imgfsi/invserv_lineas.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFCatLineasCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","INVSERV","LINEAS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/invserv_productos.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFCatProdCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","INVSERV","PROD") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/invserv_servicios.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFCatServCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","INVSERV","SERV") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/invserv_gastos.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFCatGastosCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","INVSERV","GASTOS") %></a></td></tr>
 
 		<tr><td colspan="2" class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.Msj("CEF","MENU","GLB","BANCAJ") %></td></tr>
		<tr><td width="24"><img src="../imgfsi/bancaj_bancos.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFMovBancariosCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","BANCAJ","BANCOS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/bancaj_cajas.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFMovCajaCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","BANCAJ","CAJAS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/bancaj_vales.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFValesCajaCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","BANCAJ","VALES") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/bancaj_cierres.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFCierresCajaCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","BANCAJ","CIERRES") %></a></td></tr>
 		
		<tr><td colspan="2" class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.Msj("CEF","MENU","GLB","ALM") %></td></tr>
		<tr><td width="24"><img src="../imgfsi/alm_movim.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAlmMovimientosCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","ALM","MOVIM") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/alm_movplant.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAlmMovimPlantCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","ALM","MOVPLANT") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/alm_traspasos.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAlmTraspasosCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","ALM","TRASPASOS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/alm_requerimientos.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAlmRequerimientosCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","ALM","REQUERIMIENTOS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/alm_chfis.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAlmChFisCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","ALM","CHFIS") %></a></td></tr>

		<tr><td colspan="2" class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.Msj("CEF","MENU","GLB","COMP") %></td></tr>
		<tr><td width="24"><img src="../imgfsi/comp_provee.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFCompProveeCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","COMP","PROVEE") %></a></td></tr>
		
		<tr><td colspan="2" class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.Msj("CEF","MENU","GLB","VEN") %></td></tr>
		<tr><td width="24"><img src="../imgfsi/ven_client.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFVenClientCtrl" class="titCuerpoBco" ><%= JUtil.Msj("CEF","MENU","VEN","CLIENT") %></a></td></tr>
	
		
		<tr><td colspan="2" class="titCuerpoBco" align="center" bgcolor="#0099FF"><%= JUtil.Msj("CEF","MENU","GLB","ADM") %></td></tr>
		<tr><td width="24"><img src="../imgfsi/adm_usuarios.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAdmUsuariosCtrl" class="titCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","USUARIOS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/adm_entidades.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAdmEntidadesCtrl" class="titCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","ENTIDADES") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/adm_cfdi.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAdmCFDCtrl" class="titCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","CFDI") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/adm_periodos.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAdmPeriodosCtrl" class="titCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","PERIODOS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/adm_monedas.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAdmMonedasCtrl" class="titCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","MONEDAS") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/adm_variables.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAdmVariablesCtrl" class="titCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","VARIABLES") %></a></td></tr>
		<tr><td width="24"><img src="../imgfsi/adm_formatos.png" width="24" height="24"/></td><td align="center"><a href="/servlet/CEFAdmFormatosCtrl" class="titCuerpoBco"><%= JUtil.Msj("CEF","MENU","ADM","FORMATOS") %></a></td></tr>

	 </table>
	</td>
  </tr>
</table>
</body>
</html>