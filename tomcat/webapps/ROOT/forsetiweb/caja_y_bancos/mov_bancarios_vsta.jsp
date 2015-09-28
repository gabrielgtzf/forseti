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
	String mov_bancarios = (String)request.getAttribute("mov_bancarios");
	if(mov_bancarios == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	  
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "BANCAJ_BANCOS", "VISTA", "STATUS", 2);
	JBancosSetIdsV2 SetIds = new JBancosSetIdsV2(request,JUtil.getSesion(request).getID_Usuario(),"0",JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial());
	SetIds.Open();

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
if(parent.tiempo.document.URL.indexOf('mov_bancarios_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/caja_y_bancos/mov_bancarios_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('mov_bancarios_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/caja_y_bancos/mov_bancarios_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('mov_bancarios_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/caja_y_bancos/mov_bancarios_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFMovBancariosDlg" method="post" enctype="application/x-www-form-urlencoded" name="mov_bancarios" target="_self">
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
	<td bgcolor="333333">
		<table width="100%" border="0" cellpadding="2" cellspacing="1">
          <tr align="center" bgcolor="#0099FF" class="titChico"> 
            <td>Cheque</td>
            <td>Saldo Bruto</td>
            <td>Saldo Real</td>
            <td>Dep&oacute;sitos salvo buen cobro</td>
            <td>Retiros por cobrar</td>
		 </tr>
		 <tr align="center" bgcolor="#FFFFFF"> 
<%
	if(SetIds.getNumRows() == 0)
	{
%>
			<td>&nbsp;</td>
	        <td>&nbsp;</td>
	        <td>&nbsp;</td>
	        <td>&nbsp;</td>
	        <td>&nbsp;</td>
<%
	}
	else
	{
%>
        	<td><%= SetIds.getAbsRow(0).getRef() %></td>
	        <td><%= SetIds.getAbsRow(0).getSaldoTotal() %></td>
	        <td><%= SetIds.getAbsRow(0).getSaldoAplicado() %></td>
	        <td><%= SetIds.getAbsRow(0).getDSBC() %></td>
	        <td><%= SetIds.getAbsRow(0).getRPC() %></td>
<%
	}
%>
		 </tr>
		</table>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right"> 
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_DEPOSITO',<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_DEPOSITO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_DEPOSITO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_DEPOSITO") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_DEPOSITO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_RETIRO',<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_RETIRO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_RETIRO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_RETIRO") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","AGREGAR_RETIRO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'TRASPASAR_FONDO',<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","TRASPASAR_FONDO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","TRASPASAR_FONDO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","TRASPASAR_FONDO") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","TRASPASAR_FONDO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_MOVIMIENTO',<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CONSULTAR_MOVIMIENTO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CONSULTAR_MOVIMIENTO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CONSULTAR_MOVIMIENTO") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CONSULTAR_MOVIMIENTO",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_MOVIMIENTO',<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CANCELAR_MOVIMIENTO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CANCELAR_MOVIMIENTO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CANCELAR_MOVIMIENTO") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CANCELAR_MOVIMIENTO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'APLICAR_MOVIMIENTO',<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","APLICAR_MOVIMIENTO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","APLICAR_MOVIMIENTO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","APLICAR_MOVIMIENTO") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","APLICAR_MOVIMIENTO",2) %>" border="0">
          	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PROTEGER_MES',<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","PROTEGER_MES",4) %>,<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","PROTEGER_MES",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","PROTEGER_MES") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","PROTEGER_MES",2) %>" border="0">
          	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_MOVIMIENTO',400,250)" src="../imgfsi/es_rastrear_registro.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" border="0">
              <a href="javascript:try { gestionarArchivos('BANCAJ_BANCOS', document.mov_bancarios.ID.value, ''); } catch(err) { gestionarArchivos('BANCAJ_BANCOS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFMovBancariosCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'IMPRIMIR',400,250)" src="../imgfsi/imprimir.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",2) %>" border="0">
             </div></td>
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
		  	<td width="5%" align="right"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Num&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%" align="center"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td align="left"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=DescMetodosPago&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="center"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Doc&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="17%" align="left"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Beneficiario&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
      		<td width="6%" align="right"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Deposito&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
      		<td width="6%" align="right"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Retiro&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
      		<td width="6%" align="right"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Saldo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
      		<td width="6%" align="center"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Estatus&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
      		<td width="5%" align="left"><a class="titChico" href="/servlet/CEFMovBancariosCtrl?orden=Ref&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
      	  </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="160" bgcolor="#333333">&nbsp;</td>
 	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
	JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase;
		
		if(set.getAbsRow(i).getEstatus().equals("G"))
		{
			status = JUtil.Elm(sts,2); 
			clase = "";
		}
		else if(set.getAbsRow(i).getEstatus().equals("T"))
		{
			status = JUtil.Elm(sts,3);
			clase = " class=\"txtChicoAz\"";
		}
		else if(set.getAbsRow(i).getEstatus().equals("C"))
		{
			status = JUtil.Elm(sts,4);
			clase = " class=\"txtChicoRj\"";
		}
		else
		{
			status = "";
			clase = "";
		} 
%> 
       <tr<%= clase  %>>
	      <td width="3%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID() %>"></td>
		  <td width="5%" align="right"><%= set.getAbsRow(i).getNum() %></td>
		  <td width="10%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy") %></td>
          <td align="left"><%= set.getAbsRow(i).getConcepto() %></td>
		  <td width="10%"><%= set.getAbsRow(i).getDescMetodosPago() %></td>
          <td width="7%" align="center"><% if(set.getAbsRow(i).getID_SatMetodosPago().equals("02")) { out.print(set.getAbsRow(i).getDoc()); } else { out.print(set.getAbsRow(i).getReferencia()); } %></td>
          <td width="17%" align="left"><%= set.getAbsRow(i).getBeneficiario() %></td>
	      <td width="6%" align="right"><%= set.getAbsRow(i).getDeposito() %></td>
		  <td width="6%" align="right"><%= set.getAbsRow(i).getRetiro() %></td>
		  <td width="6%" align="right"><%= set.getAbsRow(i).getSaldo() %></td>
          <td width="6%" align="center"><%= status %></td>
          <td width="5%" align="left"><%= set.getAbsRow(i).getRef() %></td>
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
