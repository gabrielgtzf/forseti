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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="forseti.*, forseti.sets.*" %>
<%
	String rep_reportes = (String)request.getAttribute("rep_reportes");
	if(rep_reportes == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("REP_REPORTES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("REP_REPORTES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("REP_REPORTES").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","REP_REPORTES","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","REP_REPORTES","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<%
	if(request.getParameter("tipo").indexOf('_') == -1)
	{
%>
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
<%
	}
%>
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form  action="/servlet/CEFReportesDlg" method="post" enctype="application/x-www-form-urlencoded" name="rep_reportes" target="_self">
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CARGAR_REPORTE',<%= JUtil.Msj("CEF","REP_REPORTES","VISTA","CARGAR_REPORTE",4) %>,<%= JUtil.Msj("CEF","REP_REPORTES","VISTA","CARGAR_REPORTE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","REP_REPORTES","VISTA","CARGAR_REPORTE") %>" alt="" title="<%= JUtil.Msj("CEF","REP_REPORTES","VISTA","CARGAR_REPORTE",2) %>" border="0">
			  <a href="javascript:try { gestionarArchivos('REP_REPORTES', document.rep_reportes.REPID.value, ''); } catch(err) { gestionarArchivos('REP_REPORTES', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
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
          <td width="3%" align="center" class="titChico">&nbsp;</td>
          <td width="12%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
          <td align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
<%
	if(request.getParameter("tipo").indexOf('_') == -1)
	{
%>
		  <td width="25%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
<%
	}
	else
	{
%>
		  <!--td width="25%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td-->
<%
	}
%>
		  <td width="10%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
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
	JReportesBind1Set set = new JReportesBind1Set(request, JUtil.getSesion(request).getID_Usuario(), request.getParameter("tipo"), "CEF-X");
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String clase;
		if(set.getAbsRow(i).getID_Report() < 10000)
			clase = " class=\"titChicoNeg\"";
		else
			clase = " class=\"titChicoAzc\"";
%>
		 <tr <%= clase %>>
		  <td width="3%" align="center"><input type="radio" name="REPID" value="<%= set.getAbsRow(i).getID_Report() %>"></td>
		  <td width="12%" align="left"><%= set.getAbsRow(i).getID_Report() %></td>
 		  <td align="left"><%= set.getAbsRow(i).getDescription() %></td>
<%
	if(request.getParameter("tipo").indexOf('_') == -1)
	{
%>
		  <td width="25%" align="left"><%= set.getAbsRow(i).getTipo() %></td>
<%
	}
	else
	{
%>
		  <!--td width="25%" align="left"><%= set.getAbsRow(i).getTipo() %></td-->
<%
	}
%>
  		  <td width="10%" align="center"><%= (set.getAbsRow(i).getGraficar()) ? "X" : "&nbsp;" %></td>
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
