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
	String adm_vendedores = (String)request.getAttribute("adm_vendedores");
	if(adm_vendedores == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ADM_VENDEDORES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ADM_VENDEDORES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ADM_VENDEDORES").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "ADM_VENDEDORES", "VISTA", "STATUS", 2);
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
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('adm_vendedores_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/administracion/adm_vendedores_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAdmVendedoresDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_vendedores" target="_self">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo %></td>
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_VENDEDOR',<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","AGREGAR_VENDEDOR",4) %>,<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","AGREGAR_VENDEDOR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","AGREGAR_VENDEDOR") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","AGREGAR_VENDEDOR",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_VENDEDOR',<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","CAMBIAR_VENDEDOR",4) %>,<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","CAMBIAR_VENDEDOR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","CAMBIAR_VENDEDOR") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","CAMBIAR_VENDEDOR",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_VENDEDOR',<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","ELIMINAR_VENDEDOR",4) %>,<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","ELIMINAR_VENDEDOR",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","ELIMINAR_VENDEDOR") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","ELIMINAR_VENDEDOR",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos('ADM_VENDEDORES', document.adm_vendedores.id.value, ''); } catch(err) { gestionarArchivos('ADM_VENDEDORES', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFAdmVendedoresCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
            </div>
		  </td>
        </tr> 
      </table>
	</td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="7%" align="left"><a class="titChico" href="/servlet/CEFAdmVendedoresCtrl?orden=ID_Vendedor&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmVendedoresCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	 		<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmVendedoresCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAdmVendedoresCtrl?orden=Comision&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JVendedoresSet set = new JVendedoresSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase;
		if(set.getAbsRow(i).getStatus().equals("A"))
		{
			status = JUtil.Elm(sts,2); 
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("B"))
		{
			status = JUtil.Elm(sts,3);
			clase = " class=\"txtChicoRj\"";
		}
		else
		{
			status = "";
			clase = "";
		}
%>
          <tr<%= clase %>>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Vendedor() %>"></td>
			<td width="7%" align="left"><%= set.getAbsRow(i).getID_Vendedor() %></td>
			<td><%= set.getAbsRow(i).getNombre() %></td>
        	<td width="7%" align="center"><%= status %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getComision() + " %" %></td>
		  </tr>		
<%
	}
%>		
     </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
