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
	String conta_polizas = (String)request.getAttribute("conta_polizas");
	if(conta_polizas == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CONT_POLIZAS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CONT_POLIZAS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CONT_POLIZAS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","CONT_POLIZAS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","CONT_POLIZAS","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "CONT_POLIZAS", "VISTA", "STATUS",2);
	String Tipos = JUtil.Msj("CEF","CONT_POLIZAS","DLG","TIPOS");
	
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
if(parent.tiempo.document.URL.indexOf('conta_polizas_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/contabilidad/conta_polizas_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('conta_polizas_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/contabilidad/conta_polizas_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('conta_polizas_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/contabilidad/conta_polizas_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFContaPolizasDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_polizas">
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_POLIZA')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","AGREGAR_POLIZA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","AGREGAR_POLIZA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_POLIZA')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CAMBIAR_POLIZA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CAMBIAR_POLIZA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_POLIZA')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CONSULTAR_POLIZA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CONSULTAR_POLIZA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONTABILIDAD_ELECTRONICA')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CONTABILIDAD_ELECTRONICA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CONTABILIDAD_ELECTRONICA",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_POLIZA'); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CANCELAR_POLIZA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CANCELAR_POLIZA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_POLIZA')" src="../imgfsi/es_rastrear_registro.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" border="0">
              <a href="/servlet/CEFReportesCtrl?tipo=CONT_POLIZAS" target="_self"><img src="../imgfsi/rep_contabilidad.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
              <a href="javascript:try { gestionarArchivos('CONT_POLIZAS', document.conta_polizas.ID.value, ''); } catch(err) { gestionarArchivos('CONT_POLIZAS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFContaPolizasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'IMPRIMIR')" src="../imgfsi/imprimir.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",2) %>" border="0">
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
		  	<td width="5%" align="center"><a class="titChico" href="/servlet/CEFContaPolizasCtrl?orden=Num&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%" align="center"><a class="titChico" href="/servlet/CEFContaPolizasCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td align="left"><a class="titChico" href="/servlet/CEFContaPolizasCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%" align="right" class="titChico"><!-- <%= JUtil.Elm(coletq,etq++) %> --><%= JUtil.Elm(colvsta,col++) %></td>
            <td width="10%" align="center"><a class="titChico" href="/servlet/CEFContaPolizasCtrl?orden=Tipo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="5%" align="center"><a class="titChico" href="/servlet/CEFContaPolizasCtrl?orden=Estatus&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="12%" align="left"><a class="titChico" href="/servlet/CEFContaPolizasCtrl?orden=Ref&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
 		  	<td width="8%" align="center"><a class="titChico" href="/servlet/CEFContaPolizasCtrl?orden=CE&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JContaPolizasSetV2 set = new JContaPolizasSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	
	//String Activa = JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ZTATUZ",2);
	//String Cancelada = JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ZTATUZ",3);
	
	
	for(int i=0; i < set.getNumRows(); i++)
	{
		String tipopol, status, clase;
		
	   	if(set.getAbsRow(i).getTipo().equals("DR")) 
			tipopol = JUtil.Elm(Tipos,1);
		else if(set.getAbsRow(i).getTipo().equals("IG")) 
			tipopol = JUtil.Elm(Tipos,2);
		else if(set.getAbsRow(i).getTipo().equals("EG")) 
			tipopol = JUtil.Elm(Tipos,3);
		else if(set.getAbsRow(i).getTipo().equals("AJ")) 
			tipopol = JUtil.Elm(Tipos,4);
		else if(set.getAbsRow(i).getTipo().equals("PE")) 
			tipopol = JUtil.Elm(Tipos,5);
		else
			tipopol = "";
			
		if(set.getAbsRow(i).getEstatus().equals("G"))
		{
			status = JUtil.Elm(sts,2); 
			clase = "";
		}
		else if(set.getAbsRow(i).getEstatus().equals("C"))
		{
			status = JUtil.Elm(sts,3);
			clase = " class=\"txtChicoRj\"";
		}
		else if(set.getAbsRow(i).getEstatus().equals("T"))
		{
			status = JUtil.Elm(sts,4);
			clase = " class=\"txtChicoAz\"";
		}
		else
		{
			status = "";
			clase = "";
		} 
%>
        <tr<%= clase  %>>
	      <td width="3%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID() %>"></td>
		  <td width="5%" align="center"><%= set.getAbsRow(i).getNum() %></td>
		  <td width="10%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy") %></td>
          <td align="left"><%= set.getAbsRow(i).getConcepto() %></td>
          <td width="10%" align="right"><%= set.getAbsRow(i).getDebe() %> / <%= set.getAbsRow(i).getHaber() %></td>
          <td width="10%" align="center"><%= tipopol %></td>
          <td width="5%" align="center"><%= status %></td>
          <td width="12%" align="left"><%= set.getAbsRow(i).getRef() %></td>
       	  <td width="8%" align="center"><%= set.getAbsRow(i).getCE() %></td>
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
