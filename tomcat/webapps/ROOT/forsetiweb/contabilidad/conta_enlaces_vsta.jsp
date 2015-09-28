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
	String conta_enlaces = (String)request.getAttribute("conta_enlaces");
	if(conta_enlaces == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CONT_ENLACES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CONT_ENLACES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CONT_ENLACES").generarOrderBy();
			
	String ent = JUtil.getSesion(request).getSesion("CONT_ENLACES").getEspecial();
	
	String colvsta;
	String coletq;
	String Tipos;
	if(ent.equals("ALMACEN"))
	{
		colvsta = JUtil.Msj("CEF","CONT_ENLACES","VISTA","COLUMNAS",1);
		coletq = JUtil.Msj("CEF","CONT_ENLACES","VISTA","COLUMNAS",2);
		Tipos = JUtil.Msj("CEF","CONT_ENLACES","VISTA","TIPOS",1);
	}
	else 
	{
		colvsta = JUtil.Msj("CEF","CONT_ENLACES","VISTA","COLUMNAS",3);
		coletq = JUtil.Msj("CEF","CONT_ENLACES","VISTA","COLUMNAS",4);
		Tipos = JUtil.Msj("CEF","CONT_ENLACES","VISTA","TIPOS",2);
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
if(parent.entidad.document.URL.indexOf('CONT_ENLACES_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/contabilidad/conta_enlaces_ent.jsp"
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
<form action="/servlet/CEFContaEnlacesDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_enlaces">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_ENLACE')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_ENLACES","VISTA","AGREGAR_ENLACE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_ENLACES","VISTA","AGREGAR_ENLACE",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_ENLACE')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_ENLACES","VISTA","CAMBIAR_ENLACE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_ENLACES","VISTA","CAMBIAR_ENLACE",2) %>" border="0">
              <input  type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_ENLACE'); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_ENLACES","VISTA","ELIMINAR_ENLACE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_ENLACES","VISTA","ELIMINAR_ENLACE",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos2('CONT_ENLACES', '<%= ent %>', document.conta_enlaces.id.value, ''); } catch(err) { gestionarArchivos2('CONT_ENLACES', '<%= ent %>', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFContaEnlacesCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
	
	if(ent.equals("ALMACEN"))
	{
%>	  
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=ID_Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="60%" align="left"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="left"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=Tipo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=RecalcularCosto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="left"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=CC&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
          </tr>
<%
	}
	else
	{
%>
		  <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=ID_Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="60%" align="left"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="left"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=Tipo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="25%" align="left"><a class="titChico" href="/servlet/CEFContaEnlacesCtrl?orden=CC&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
<%
	}
%>
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
	if(ent.equals("ALMACEN"))
	{
		JAdmInvservCostosConceptosSet set = new JAdmInvservCostosConceptosSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			String tipo, clase;
		
	   		if(set.getAbsRow(i).getTipo().equals("ENT")) 
				tipo = JUtil.Elm(Tipos,1);
			else if(set.getAbsRow(i).getTipo().equals("SAL")) 
				tipo = JUtil.Elm(Tipos,2);
			else
				tipo = "";
			
			if(set.getAbsRow(i).getDeSistema())
				clase = " class=\"txtChicoAz\"";
			else
				clase = "";
%>
          <tr<%= clase %>>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Concepto() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_Concepto() %></td>
			<td width="60%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
  			<td width="7%" align="left"><%= tipo %></td>
    		<td width="5%" align="center"><%= (set.getAbsRow(i).getRecalcularCosto()) ? "X" : "---" %></td>
			<td width="20%" align="left"><%= JUtil.obtCuentaFormato(set.getAbsRow(i).getCC(), request) %></td>
          </tr>		
<%
		}
	}
	else if(ent.equals("CXP"))
	{
		JAdmProveeCXPConceptos set = new JAdmProveeCXPConceptos(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			String tipo, clase;
		
	   		if(set.getAbsRow(i).getTipo().equals("ALT")) 
				tipo = JUtil.Elm(Tipos,1);
			else if(set.getAbsRow(i).getTipo().equals("SAL")) 
				tipo = JUtil.Elm(Tipos,2);
			else
				tipo = "";
			
			if(set.getAbsRow(i).getDeSistema())
				clase = " class=\"txtChicoAz\"";
			else
				clase = "";
%>
          <tr<%= clase %>>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Concepto() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_Concepto() %></td>
			<td width="60%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
  			<td width="7%" align="left"><%= tipo %></td>
    		<td width="25%" align="left"><%= JUtil.obtCuentaFormato(set.getAbsRow(i).getCC(), request) %></td>
		  </tr>		
<%
		}
	}
	else
	{
		JAdmClientCXCConceptos set = new JAdmClientCXCConceptos(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			String tipo, clase;
		
	   		if(set.getAbsRow(i).getTipo().equals("ALT")) 
				tipo = JUtil.Elm(Tipos,1);
			else if(set.getAbsRow(i).getTipo().equals("SAL")) 
				tipo = JUtil.Elm(Tipos,2);
			else
				tipo = "";
			
			if(set.getAbsRow(i).getDeSistema())
				clase = " class=\"txtChicoAz\"";
			else
				clase = "";
%>
          <tr<%= clase %>>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Concepto() %>"></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getID_Concepto() %></td>
			<td width="60%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
  			<td width="7%" align="left"><%= tipo %></td>
    		<td width="25%" align="left"><%= JUtil.obtCuentaFormato(set.getAbsRow(i).getCC(), request) %></td>
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
