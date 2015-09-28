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
	String prod_formulas = (String)request.getAttribute("prod_formulas");
	if(prod_formulas == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("PROD_FORMULAS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("PROD_FORMULAS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("PROD_FORMULAS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","PROD_FORMULAS","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","PROD_FORMULAS","VISTA","COLUMNAS",2);
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
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFProdFormulasDlg" method="post" enctype="application/x-www-form-urlencoded" name="prod_formulas" target="_self">
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_FORMULA',<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","AGREGAR_FORMULA",4) %>,<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","AGREGAR_FORMULA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","AGREGAR_FORMULA") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","AGREGAR_FORMULA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_FORMULA',<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CAMBIAR_FORMULA",4) %>,<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CAMBIAR_FORMULA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CAMBIAR_FORMULA") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CAMBIAR_FORMULA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_FORMULA',<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CONSULTAR_FORMULA",4) %>,<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CONSULTAR_FORMULA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CONSULTAR_FORMULA") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","CONSULTAR_FORMULA",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'APLICAR_FORMULA',<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","APLICAR_FORMULA",4) %>,<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","APLICAR_FORMULA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","APLICAR_FORMULA") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_FORMULAS","VISTA","APLICAR_FORMULA",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos('PROD_FORMULAS', document.prod_formulas.ID.value, ''); } catch(err) { gestionarArchivos('PROD_FORMULAS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFProdFormulasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
           </div></td>
        </tr>
      </table></td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
        <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFProdFormulasCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="28%" align="left"><a class="titChico" href="/servlet/CEFProdFormulasCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="4%" align="center"><a class="titChico" href="/servlet/CEFProdFormulasCtrl?orden=Principal&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="28%" align="left"><a class="titChico" href="/servlet/CEFProdFormulasCtrl?orden=Formula&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="right"><a class="titChico" href="/servlet/CEFProdFormulasCtrl?orden=Cantidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFProdFormulasCtrl?orden=Unidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="right"><a class="titChico" href="/servlet/CEFProdFormulasCtrl?orden=MasMenos&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JProdFormulasSetV2 set = new JProdFormulasSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Formula() %>"></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getClave() %></td>
			<td width="28%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="4%" align="center"><% if(set.getAbsRow(i).getPrincipal()) { %>X<% } else { %>&nbsp;<% } %></td>
			<td width="28%" align="left"><%= set.getAbsRow(i).getFormula() %></td>
			<td width="10%" align="right"><%= set.getAbsRow(i).getCantidad() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getUnidad() %></td>
			<td width="10%" align="right"><%= set.getAbsRow(i).getMasMenos() %></td>
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
