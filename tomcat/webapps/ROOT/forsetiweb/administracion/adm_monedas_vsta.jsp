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
	String adm_monedas = (String)request.getAttribute("adm_monedas");
	if(adm_monedas == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ADM_MONEDAS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ADM_MONEDAS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ADM_MONEDAS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","ADM_MONEDAS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","ADM_MONEDAS","VISTA","COLUMNAS",1);
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAdmMonedasDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_monedas">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_MONEDA',<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","AGREGAR_MONEDA",4) %>,<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","AGREGAR_MONEDA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","AGREGAR_MONEDA") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","AGREGAR_MONEDA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_MONEDA',<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","CAMBIAR_MONEDA",4) %>,<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","CAMBIAR_MONEDA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","CAMBIAR_MONEDA") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_MONEDAS","VISTA","CAMBIAR_MONEDA",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos('ADM_MONEDAS', document.adm_monedas.id.value, ''); } catch(err) { gestionarArchivos('ADM_MONEDAS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFAdmMonedasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td width="10%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="10%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="10%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="10%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
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
	JContaMonedasSetV2 set = new JContaMonedasSetV2(request);
	set.m_OrderBy = orden; 
	set.Open();

	for(int i=0; i < set.getNumRows(); i++)
	{
%>
          <tr>
	     	  <td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getClave() %>"></td>
			  <td width="10%" align="center"><%= set.getAbsRow(i).getClave() %></td>
			  <td align="center"><%= set.getAbsRow(i).getMoneda() %></td>
			  <td width="10%" align="center"><%= set.getAbsRow(i).getSimbolo() %></td>
			  <td width="10%" align="right"><%= set.getAbsRow(i).getTC()  %></td>
			  <td width="10%" align="center"><%= set.getAbsRow(i).getID_SatMoneda()  %></td>
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
