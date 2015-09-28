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
	String adm_saldos = (String)request.getAttribute("adm_saldos");
	if(adm_saldos == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ADM_SALDOS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ADM_SALDOS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ADM_SALDOS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","ADM_SALDOS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","ADM_SALDOS","VISTA","COLUMNAS",1);
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
<form action="/servlet/CEFAdmSaldosDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_saldos" target="_self">
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
 			  <input  name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'INICIAR_SALDO',<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","INICIAR_SALDO",4) %>,<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","INICIAR_SALDO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","INICIAR_SALDO") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","INICIAR_SALDO",2) %>" border="0">
			  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("CEF","ADM_SALDOS","DLG","MSJ-PROCOK") %>')) { establecerProcesoSVE(this.form.proceso, 'ACTUALIZAR_SALDO',<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_SALDO",4) %>,<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_SALDO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_SALDO") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_SALDO",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("CEF","ADM_SALDOS","DLG","MSJ-PROCOK",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ACTUALIZAR_TODO',<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_TODO",4) %>,<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_TODO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_TODO") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_SALDOS","VISTA","ACTUALIZAR_TODO",2) %>" border="0">
			  <a href="javascript:try { gestionarArchivos('ADM_SALDOS', document.adm_saldos.id.value, ''); } catch(err) { gestionarArchivos('ADM_SALDOS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
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
			<td width="5%" align="center">&nbsp;</td>
			<td width="45%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="5%" align="center">&nbsp;</td>
			<td width="45%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
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
	for(int i = 1; i <= 5; i++)
	{
%>
          <tr> 
		  	<td width="5%" align="center"><input type="radio" name="id" value="I<%= i %>"></td>
			<td width="45%"><%= JUtil.Msj("CEF","ADM_SALDOS","DLG","ETQ3",i) %></td>
		  	<td width="5%" align="center"><input type="radio" name="id" value="S<%= i %>"></td>
			<td width="45%"><%= JUtil.Msj("CEF","ADM_SALDOS","DLG","ETQ",i) %></td>
		  </tr>
<%
	}
	for(int i = 1; i <= 3; i++)
	{
%>
		  <tr>
	      	<td colspan="2">&nbsp;</td>
	      	<td width="5%" align="center"><input type="radio" name="id" value="S<%= 5+i %>"></td>
			<td width="45%"><%= JUtil.Msj("CEF","ADM_SALDOS","DLG","ETQ2",i) %></td>
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
