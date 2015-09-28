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
	String adm_entidades = (String)request.getAttribute("adm_entidades");
	if(adm_entidades == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ADM_ENTIDADES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ADM_ENTIDADES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ADM_ENTIDADES").generarOrderBy();
			
	String ent = JUtil.getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
	
	String colvsta;
	String coletq;
	if(ent.equals("BANCOS"))
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS",1);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS2",1);
	}
	else if(ent.equals("CAJAS"))
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS",2);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS2",2);
	}
	else if(ent.equals("BODEGAS"))
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS",3);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS2",3);
	}
	else if(ent.equals("COMPRAS"))
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS",4);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS2",4);
	}
	else if(ent.equals("VENTAS"))
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS",5);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS2",5);
	}
	else if(ent.equals("PRODUCCION"))
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS3",1);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS4",1);
	}
	else if(ent.equals("NOMINA"))
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS3",2);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS4",2);
	}
	else // mensajes
	{
		colvsta = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS",3);
		coletq = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","COLUMNAS2",3);
	}
	int etq = 1, col = 1;
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
<form action="/servlet/CEFAdmEntidadesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_entidades">
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
			  <a href="../forsetiweb/administracion/adm_entidades_ent_pm.jsp"><img src="../imgfsi/p_izq_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <img src="../imgfsi/p_inf_off.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <img src="../imgfsi/p_der_off.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",3) %>" width="24" height="24" border="0"/></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFAdmEntidadesCtrl"><img src="../imgfsi/actualizar24.png" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/> 
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
			  <input name="tipomov" type="hidden" value="<%= ent %>">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_ENTIDAD',<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","AGREGAR_ENTIDAD",4) %>,<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","AGREGAR_ENTIDAD",5) %>)" src="../imgfsi/pm_agregar_bd.png" title="<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","AGREGAR_ENTIDAD",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_ENTIDAD',<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CAMBIAR_ENTIDAD",4) %>,<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CAMBIAR_ENTIDAD",5) %>)" src="../imgfsi/pm_cambiar_bd.png" title="<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CAMBIAR_ENTIDAD",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_ENTIDAD',<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CONSULTAR_ENTIDAD",4) %>,<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CONSULTAR_ENTIDAD",5) %>)" src="../imgfsi/pm_consultar.png" title="<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CONSULTAR_ENTIDAD",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
	</td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
	
	if(ent.equals("BANCOS"))
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Cuenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="25%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=SigCheque&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=CC&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
           </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmBancosCuentasSet set = new JAdmBancosCuentasSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getClave() %>"/></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getClave() %></td>
			<td width="20%"><%= set.getAbsRow(i).getCuenta() %></td>
  			<td width="25%"><%= set.getAbsRow(i).getSigCheque() %></td>
    		<td><%= JUtil.obtCuentaFormato(set.getAbsRow(i).getCC(), request) %></td>
          </tr>		
<%
		}
	}
	else 	if(ent.equals("CAJAS"))
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="25%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Cuenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=CC&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
           </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmBancosCuentasSet set = new JAdmBancosCuentasSet (request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getClave() %></td>
			<td width="25%"><%= set.getAbsRow(i).getCuenta() %></td>
	   		<td><%= JUtil.obtCuentaFormato(set.getAbsRow(i).getCC(), request) %></td>
          </tr>		
<%
		}
	}
	else 	if(ent.equals("BODEGAS"))
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_Bodega&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	      </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmInvservBodegasSet set = new JAdmInvservBodegasSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Bodega() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getID_Bodega() %></td>
			<td><%= set.getAbsRow(i).getDescripcion() %></td>
         </tr>		
<%
		}
	}
	else 	if(ent.equals("COMPRAS"))
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_EntidadCompra&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_TipoEntidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Serie&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmComprasEntidades set = new JAdmComprasEntidades(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_EntidadCompra() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getID_EntidadCompra() %></td>
			<td><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="20%"><%= (set.getAbsRow(i).getID_TipoEntidad() == 0) ? "COMPRA" : "GASTO" %></td>
			<td width="20%"><%= set.getAbsRow(i).getSerie() %></td>
	      </tr>		
<%
		}
	}
	else 	if(ent.equals("VENTAS"))
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_EntidadVenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Serie&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		</tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmVentasEntidades set = new JAdmVentasEntidades(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			int mod = i % 2;
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_EntidadVenta() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getID_EntidadVenta() %></td>
			<td><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="20%"><%= set.getAbsRow(i).getSerie() %></td>
		  </tr>		
<%
		}
	}
	else 	if(ent.equals("PRODUCCION"))
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_EntidadProd&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Serie&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmProduccionEntidades set = new JAdmProduccionEntidades(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_EntidadProd() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getID_EntidadProd() %></td>
			<td><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="20%"><%= set.getAbsRow(i).getSerie() %></td>
         </tr>		
<%
		}
	}
	else 	if(ent.equals("NOMINA"))
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_Sucursal&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmCompaniasSet set = new JAdmCompaniasSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Sucursal() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getID_Sucursal() %></td>
			<td width="20%" align="center"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td><%= set.getAbsRow(i).getNombre() %></td>
		  </tr>		
<%
		}
	}
	else // MENSAJES
	{
%>	  
          <tr>
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_Block&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	      </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="250">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
		JAdmNotasBlocks set = new JAdmNotasBlocks(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Block() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getID_Block() %></td>
			<td><%= set.getAbsRow(i).getDescripcion() %></td>
         </tr>		
<%
		}
	}
%>				
     </table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
