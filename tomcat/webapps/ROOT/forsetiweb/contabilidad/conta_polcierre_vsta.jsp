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
	String conta_polcierre = (String)request.getAttribute("conta_polcierre");
	if(conta_polcierre == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CONT_POLCIERRE").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CONT_POLCIERRE").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CONT_POLCIERRE").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","COLUMNAS",1);
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
if(parent.tiempo.document.URL.indexOf('conta_polcierre_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/contabilidad/conta_polcierre_tmp.jsp"
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
<form action="/servlet/CEFContaPolizasCierreDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_polcierre" target="_self">
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
			   <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'GENERAR_POLCIERRE'); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","GENERAR_POLCIERRE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","GENERAR_POLCIERRE",2) %>" border="0">
               <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_POLCIERRE')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","AGREGAR_POLCIERRE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","AGREGAR_POLCIERRE",2) %>" border="0">
               <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_POLCIERRE')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","CAMBIAR_POLCIERRE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","CAMBIAR_POLCIERRE",2) %>" border="0">
               <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_POLCIERRE'); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","ELIMINAR_POLCIERRE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","ELIMINAR_POLCIERRE",2) %>" border="0">
               <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_POLCIERRE'); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","CANCELAR_POLCIERRE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLCIERRE","VISTA","CANCELAR_POLCIERRE",2) %>" border="0">
               <a href="/servlet/CEFReportesCtrl?tipo=CONT_POLCIERRE" target="_self"><img src="../imgfsi/rep_contabilidad.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
               <a href="javascript:try { gestionarArchivosCta('CONT_POLCIERRE', document.conta_polcierre.id.value, ''); } catch(err) { gestionarArchivosCta('CONT_POLCIERRE', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			   <a href="/servlet/CEFContaPolizasCierreCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td width="5%" align="center">&nbsp;</td>
		  	<td width="10%" align="left"><a class="titChico" href="/servlet/CEFContaPolizasCierreCtrl?orden=Cuenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="40%" align="left"><a class="titChico" href="/servlet/CEFContaPolizasCierreCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%" align="right"><a class="titChico" href="/servlet/CEFContaPolizasCierreCtrl?orden=Saldo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
    		<td width="10%" align="right"><a class="titChico" href="/servlet/CEFContaPolizasCierreCtrl?orden=Debe&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%" align="right"><a class="titChico" href="/servlet/CEFContaPolizasCierreCtrl?orden=Haber&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%" align="right" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
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
	JContaPolizasDetalleCASet set = new JContaPolizasDetalleCASet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	double totDebe = 0, totHaber = 0;
	double totDiff = 0;
	
	for(int i=0; i < set.getNumRows(); i++)
	{
		totDebe += JUtil.redondear(set.getAbsRow(i).getDebe(),2);
		totHaber += JUtil.redondear(set.getAbsRow(i).getHaber(),2);
%>
        <tr>
	      <td width="5%" align="center"><input type="radio" name="id" value="<%= JUtil.obtCuentaFormato(set.getAbsRow(i).getCuenta(), request) %>"></td>
		  <td width="10%" align="left"><%=  JUtil.obtCuentaFormato(set.getAbsRow(i).getCuenta(), request)  %></td>
		  <td width="40%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
          <td width="10%" align="right"><%= set.getAbsRow(i).getSaldo() %></td>
          <td width="10%" align="right"><%= set.getAbsRow(i).getDebe() %></td>
          <td width="10%" align="right"><%= set.getAbsRow(i).getHaber() %></td>
          <td width="10%" align="right"><%= JUtil.redondear(set.getAbsRow(i).getSaldo() + set.getAbsRow(i).getDebe() - set.getAbsRow(i).getHaber(),2) %></td>
       </tr>		       
<%
	}
	
	totDiff = totDebe - totHaber;
	
%>	
		<tr bgcolor="#999999">
	      <td width="5%" align="center">&nbsp;</td>
		  <td width="10%" align="left">&nbsp;</td>
		  <td width="40%" align="left" class="titChicoNeg">&nbsp;</td>
          <td width="10%" align="right">&nbsp;</td>
          <td class="titChicoNeg" width="10%" align="right"><%= JUtil.redondear(totDebe,2) %></td>
          <td class="titChicoNeg" width="10%" align="right"><%= JUtil.redondear(totHaber,2) %></td>
          <td class="titChicoNeg" width="10%" align="right"><%= JUtil.redondear(totDiff,2) %></td>
       </tr>		
      </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
