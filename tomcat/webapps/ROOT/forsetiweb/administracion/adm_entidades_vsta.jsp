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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!-- 
if(top.location == self.location) {
	top.location.href = "../forsetiweb/forseti.html"
}
if(parent.tiempo.document.URL.indexOf('tiempo.html') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/tiempo.html"
}
if(parent.entidad.document.URL.indexOf('adm_entidades_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/administracion/adm_entidades_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAdmEntidadesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_entidades" target="_self">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo  %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
%>  
  <tr>
    <td bgcolor="#333333">
		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right">
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
			  <input name="tipomov" type="hidden" value="<%= ent %>">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_ENTIDAD',<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","AGREGAR_ENTIDAD",4) %>,<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","AGREGAR_ENTIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","AGREGAR_ENTIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","AGREGAR_ENTIDAD",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_ENTIDAD',<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CAMBIAR_ENTIDAD",4) %>,<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CAMBIAR_ENTIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CAMBIAR_ENTIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CAMBIAR_ENTIDAD",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_ENTIDAD',<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CONSULTAR_ENTIDAD",4) %>,<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CONSULTAR_ENTIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CONSULTAR_ENTIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","CONSULTAR_ENTIDAD",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos2('ADM_ENTIDADES', '<%= ent %>', document.adm_entidades.id.value, ''); } catch(err) { gestionarArchivos2('ADM_ENTIDADES', '<%= ent %>', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFAdmEntidadesCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
           </div></td>
        </tr>
      </table></td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
	
	if(ent.equals("BANCOS"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Cuenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=SigCheque&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=SigCheque&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
           </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmBancosCuentasSet set = new JAdmBancosCuentasSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getClave() %></td>
			<td width="10%"><%= set.getAbsRow(i).getCuenta() %></td>
  			<td width="7%" align="center"><%= set.getAbsRow(i).getSigCheque() %></td>
    		<td width="20%" align="center"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td><%= ( set.getAbsRow(i).getID_SatBanco().equals("000") ? set.getAbsRow(i).getBancoExt() : set.getAbsRow(i).getBanco() ) %></td>
          </tr>		
<%
		}
	}
	else 	if(ent.equals("CAJAS"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Cuenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
           </tr>
		  </table>
	</td>
  </tr>
</table>	
</div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmBancosCuentasSet set = new JAdmBancosCuentasSet (request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getClave() %></td>
			<td width="20%"><%= set.getAbsRow(i).getCuenta() %></td>
	   		<td><%= set.getAbsRow(i).getDescripcion() %></td>
          </tr>		
<%
		}
	}
	else 	if(ent.equals("BODEGAS"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_Bodega&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="60%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	      </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmInvservBodegasSet set = new JAdmInvservBodegasSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Bodega() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_Bodega() %></td>
			<td width="60%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
         </tr>		
<%
		}
	}
	else 	if(ent.equals("COMPRAS"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_EntidadCompra&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="40%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_TipoEntidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Serie&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Doc&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Orden&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Devolucion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>

	      </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmComprasEntidades set = new JAdmComprasEntidades(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_EntidadCompra() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_EntidadCompra() %></td>
			<td width="40%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="20%" align="left"><%= (set.getAbsRow(i).getID_TipoEntidad() == 0) ? "COMPRA" : "GASTO" %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getSerie() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getDoc() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getOrden() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getDevolucion() %></td>

         </tr>		
<%
		}
	}
	else 	if(ent.equals("VENTAS"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_EntidadVenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="60%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Serie&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Doc&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Pedido&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Devolucion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	      </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
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
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_EntidadVenta() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_EntidadVenta() %></td>
			<td width="60%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getSerie() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getDoc() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getPedido() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getDevolucion() %></td>
         </tr>		
<%
		}
	}
	else 	if(ent.equals("PRODUCCION"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_EntidadProd&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="60%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Serie&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Doc&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmProduccionEntidades set = new JAdmProduccionEntidades(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_EntidadProd() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_EntidadProd() %></td>
			<td width="60%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getSerie() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getDoc() %></td>
         </tr>		
<%
		}
	}
	else 	if(ent.equals("NOMINA"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_Sucursal&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="65%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmCompaniasSet set = new JAdmCompaniasSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Sucursal() %>"></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getID_Sucursal() %></td>
			<td width="12%" align="center"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="65%" align="left"><%= set.getAbsRow(i).getNombre() %></td>
		  </tr>		
<%
		}
	}
	else // MENSAJES
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=ID_Block&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="60%" align="left"><a class="titChico" href="/servlet/CEFAdmEntidadesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	      </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmNotasBlocks set = new JAdmNotasBlocks(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Block() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_Block() %></td>
			<td width="60%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
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
