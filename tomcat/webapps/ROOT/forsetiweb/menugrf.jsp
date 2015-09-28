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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Menu Principal</title>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
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
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_catcuentas.png"/></a></td>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_rubros.png"/></a></td>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_tipopoliza.png"/></a></td>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_polizas.png"/></a></td>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_enlaces.png"/></a></td>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_polcierre.png"/></a></td>
		</tr>
     	<tr>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","RUBROS") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","TIPOPOLIZA") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLIZAS") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","ENLACES") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLCIERRE") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
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
			<td align="center"><a href="/servlet/CEFCatLineasCtrl"><img src="../imgfsi/invserv_lineas.png"/></a></td>
			<td align="center"><a href="/servlet/CEFCatProdCtrl"><img src="../imgfsi/invserv_productos.png"/></a></td>
			<td align="center"><a href="/servlet/CEFCatServCtrl"><img src="../imgfsi/invserv_servicios.png"/></a></td>
			<td align="center"><a href="/servlet/CEFCatGastosCtrl"><img src="../imgfsi/invserv_gastos.png"/></a></td>
		</tr>
     	<tr>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","LINEAS") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","PROD") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","SERV") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","INVSERV","GASTOS") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
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
			<td align="center"><a href="/servlet/CEFMovBancariosCtrl"><img src="../imgfsi/bancaj_bancos.png"/></a></td>
			<td align="center"><a href="/servlet/CEFMovCajasCtrl"><img src="../imgfsi/bancaj_cajas.png"/></a></td>
			<td align="center"><a href="/servlet/CEFValesCajaCtrl"><img src="../imgfsi/bancaj_vales.png"/></a></td>
			<td align="center"><a href="/servlet/CEFCierresCajaCtrl"><img src="../imgfsi/bancaj_cierres.png"/></a></td>
		</tr>
     	<tr>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","BANCOS") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","CAJAS") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","VALES") %></td>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","BANCAJ","CIERRES") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_catcuentas.png"/></a></td>
		</tr>
     	<tr>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_catcuentas.png"/></a></td>
		</tr>
     	<tr>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_catcuentas.png"/></a></td>
		</tr>
     	<tr>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
  <tr>
    <td>
	  <table cellpadding="5" cellspacing="0">
	  	<tr>
		  <td class="titGiganteAzc"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
		</tr>
	  </table>
	</td> 
  </tr>
  <tr>
  	<td>
	  <table cellpadding="5" cellspacing="0">
	    <tr>
			<td align="center"><a href="/servlet/CEFContaCatCuentasCtrl"><img src="../imgfsi/cont_catcuentas.png"/></a></td>
		</tr>
     	<tr>
			<td align="center" valign="top" width="100" class="txtCuerpoBco"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></td>
		</tr>
	  </table> 
	</td>
  </tr>
  
  <tr>
  	<td>
	  <table>      	
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