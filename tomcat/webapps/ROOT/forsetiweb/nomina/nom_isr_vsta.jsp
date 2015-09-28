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
	String nom_isr = (String)request.getAttribute("nom_isr");
	if(nom_isr == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("NOM_ISR").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("NOM_ISR").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("NOM_ISR").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","NOM_ISR","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","NOM_ISR","VISTA","COLUMNAS",2);
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
<form action="/servlet/CEFNomIsrDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_isr" target="_self">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_ISR',<%= JUtil.Msj("CEF","NOM_ISR","VISTA","AGREGAR_ISR",4) %>,<%= JUtil.Msj("CEF","NOM_ISR","VISTA","AGREGAR_ISR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_ISR","VISTA","AGREGAR_ISR") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_ISR","VISTA","AGREGAR_ISR",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_ISR',<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CAMBIAR_ISR",4) %>,<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CAMBIAR_ISR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CAMBIAR_ISR") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CAMBIAR_ISR",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_ISR',<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CONSULTAR_ISR",4) %>,<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CONSULTAR_ISR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CONSULTAR_ISR") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_ISR","VISTA","CONSULTAR_ISR",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos('NOM_ISR', document.nom_isr.id.value, ''); } catch(err) { gestionarArchivos('NOM_ISR', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFNomIsrCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
		  	<td width="11%" align="left"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=ID_Isr&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Limite_Inferior&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Limite_Superior&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Cuota_Fija&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Porcentaje_Exd&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Subsidio&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Subsidio_SIM&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Limite_Inferior_Anual&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Limite_Superior_Anual&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Cuota_Fija_Anual&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Porcentaje_Exd_Anual&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Subsidio_Anual&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="7%" align="right"><a class="titChico" href="/servlet/CEFNomIsrCtrl?orden=Subsidio_SIM_Anual&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JIsrSet set = new JIsrSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
%>
        <tr>
		  <td width="5%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Isr() %>"></td>
		  <td width="11%" align="left"><%= set.getAbsRow(i).getID_Isr() %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getLimite_Inferior()  %></td>
 		  <td width="7%" align="right"><%= set.getAbsRow(i).getLimite_Superior()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getCuota_Fija()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getPorcentaje_Exd()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getSubsidio()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getSubsidio_SIM()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getLimite_Inferior_Anual()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getLimite_Superior_Anual()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getCuota_Fija_Anual()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getPorcentaje_Exd_Anual()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getSubsidio_Anual()  %></td>
		  <td width="7%" align="right"><%= set.getAbsRow(i).getSubsidio_SIM_Anual()  %></td>
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