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
	String vales_caja = (String)request.getAttribute("vales_caja");
	if(vales_caja == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","BANCAJ_VALES","VISTA","COLUMNAS",3);
	String coletq = JUtil.Msj("CEF","BANCAJ_VALES","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "BANCAJ_VALES", "VISTA", "STATUS", 2);

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
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFValesCajaDlg" method="post" enctype="application/x-www-form-urlencoded" name="vales_caja">
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
			  <a href="../forsetiweb/menu_pm.jsp"><img src="../imgfsi/menu_principal.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",4) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <a href="../forsetiweb/caja_y_bancos/vales_caja_ent_pm.jsp"><img src="../imgfsi/p_izq_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <a href="../forsetiweb/caja_y_bancos/vales_caja_tmp_pm.jsp"><img src="../imgfsi/p_inf_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",2) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <a href="../forsetiweb/caja_y_bancos/vales_caja_sts_pm.jsp"><img src="../imgfsi/p_der_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",3) %>" width="24" height="24" border="0"/></a></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFValesCajaCtrl"><img src="../imgfsi/actualizar24.png" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/> 
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040105.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
%>  
  <tr> 
    <td align="right"> 
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","AGREGAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","AGREGAR_VALE",5) %>)" src="../imgfsi/pm_agregar.png" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","AGREGAR_VALE",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
             <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","CAMBIAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","CAMBIAR_VALE",5) %>)" src="../imgfsi/pm_cambiar.png" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","CAMBIAR_VALE",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
               <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'TRASPASAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","TRASPASAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","TRASPASAR_VALE",5) %>)" src="../imgfsi/pm_traspasar.png" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","TRASPASAR_VALE",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","ELIMINAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","ELIMINAR_VALE",5) %>); } else { return false; } " src="../imgfsi/pm_eliminar.png" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","ELIMINAR_VALE",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'GENERAR_PROCESO',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","GENERAR_PROCESO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","GENERAR_PROCESO",5) %>)" src="../imgfsi/pm_generar.png" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","GENERAR_PROCESO",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
 	</td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	 	<table width="100%" border="0" cellpadding="5" cellspacing="0">
		  <tr>
	  		<td width="10%" align="center">&nbsp;</td>
			<td width="20%" align="center"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=ID_Gasto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="27%"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  	<td><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
		</table>
		<table width="100%" border="0" cellpadding="5" cellspacing="0">
		  <tr>
			<td align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Provisional&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Final&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Factura&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Compra&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Pago&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Traspaso&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
        </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="265">&nbsp;</td>
 	</tr>
  	<tr>
      <td> 
<%
	JCajasValesSetV2 set = new JCajasValesSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	
	float Provisional = 0.0f, Final = 0.0f, Factura = 0.0f, Compra = 0.0f, Pago = 0.0f, Traspaso = 0.0f;

	for(int i=0; i < set.getNumRows(); i++)
	{
		Provisional += set.getAbsRow(i).getProvisional();
		Final += set.getAbsRow(i).getFinal();
		Factura += set.getAbsRow(i).getFactura();
		Compra += set.getAbsRow(i).getCompra();
		Pago += set.getAbsRow(i).getPago();
		Traspaso += set.getAbsRow(i).getTraspaso();
			 
%>
       <table width="100%" border="0" cellpadding="5" cellspacing="0">
       	<tr>
	        <td width="10%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Vale() %>"></td>
			<td width="20%" align="center"><%= set.getAbsRow(i).getFecha() %></td>
			<td width="15%"><%= set.getAbsRow(i).getID_Gasto() %></td>
			<td width="27%"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td><%= set.getAbsRow(i).getConcepto() %></td>
		</tr>
	   </table>
	   <table width="100%" border="0" cellpadding="5" cellspacing="0">
        <tr>
			<td align="right"><%= set.getAbsRow(i).getProvisional() %></td>
			<td width="15%" align="right"><%= set.getAbsRow(i).getFinal() %></td>
			<td width="15%" align="right"><%= set.getAbsRow(i).getFactura() %></td>
			<td width="15%" align="right"><%= set.getAbsRow(i).getCompra() %></td>
			<td width="15%" align="right"><%= set.getAbsRow(i).getPago() %></td>
			<td width="15%" align="right"><%= set.getAbsRow(i).getTraspaso() %></td>
		</tr>		
	   </table>
<%
	}
%>
	   <table width="100%" border="0" cellpadding="5" cellspacing="0">
      	<tr>
	    	<td align="right" class="txtChicoAzc"><%= Provisional %></td>
			<td width="15%" align="right" class="txtChicoAzc"><%= Final %></td>
			<td width="15%" align="right" class="txtChicoAzc"><%= Factura %></td>
			<td width="15%" align="right" class="txtChicoAzc"><%= Compra %></td>
			<td width="15%" align="right" class="txtChicoAzc"><%= Pago %></td>
			<td width="15%" align="right" class="txtChicoAzc"><%= Traspaso %></td>
		</tr>		
       </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
