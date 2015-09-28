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
	String crm_oportunidades = (String)request.getAttribute("crm_oportunidades");
	if(crm_oportunidades == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CRM_OPORTUNIDADES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CRM_OPORTUNIDADES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CRM_OPORTUNIDADES").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","COLUMNAS",2);
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
if(parent.tiempo.document.URL.indexOf('crm_oportunidades_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/crm/crm_oportunidades_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('crm_oportunidades_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/crm/crm_oportunidades_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('crm_oportunidades_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/crm/crm_oportunidades_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFCRMOportunidadesDlg" method="post" enctype="application/x-www-form-urlencoded" name="crm_oportunidades" target="_self">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_OPORTUNIDAD',<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","AGREGAR_OPORTUNIDAD",4) %>,<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","AGREGAR_OPORTUNIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","AGREGAR_OPORTUNIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","AGREGAR_OPORTUNIDAD",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_OPORTUNIDAD',<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CAMBIAR_OPORTUNIDAD",4) %>,<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CAMBIAR_OPORTUNIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CAMBIAR_OPORTUNIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CAMBIAR_OPORTUNIDAD",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_OPORTUNIDAD',<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CONSULTAR_OPORTUNIDAD",4) %>,<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CONSULTAR_OPORTUNIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CONSULTAR_OPORTUNIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA","CONSULTAR_OPORTUNIDAD",2) %>" border="0">
              <a href="/servlet/CEFCRMOportunidadesCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
            </div></td>
        </tr>
      </table></td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
        <tr bgcolor="#0099FF">
			<td width="3%" align="center">&nbsp;</td>
			<td align="left"><a class="titChico" href="/servlet/CEFCRMOportunidadesCtrl?orden=tl_oportunity&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFCRMOportunidadesCtrl?orden=tp_origin&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="30%" align="left"><a class="titChico" href="/servlet/CEFCRMOportunidadesCtrl?orden=tx_contact&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="right"><a class="titChico" href="/servlet/CEFCRMOportunidadesCtrl?orden=im_revenue&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFCRMOportunidadesCtrl?orden=dt_next_action&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="13%" align="center"><a class="titChico" href="/servlet/CEFCRMOportunidadesCtrl?orden=dt_last_call&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JCRMOportunitiesViewSet set = new JCRMOportunitiesViewSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	
	for(int i=0; i < set.getNumRows(); i++)
	{
		String clase = (set.getAbsRow(i).getBO_private() == 1 ? " class=\"txtChicoAzc\"" : "");
%>
          <tr<%=clase%>>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getGU_oportunity() %>"></td>
			<td align="left"><%= set.getAbsRow(i).getTL_oportunity() %></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getTP_origin() %></td>
			<td width="30%" align="left"><%= set.getAbsRow(i).getTX_contact() %></td>
			<td width="7%" align="right"><%= set.getAbsRow(i).getIM_revenue() %></td>
			<td width="7%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getDT_next_action(), "dd/MMM/yyyy") %></td>
			<td width="13%" align="center"><%= (set.getAbsRow(i).getDT_last_call() == null ? "Nunca" : JUtil.obtFechaTxt(set.getAbsRow(i).getDT_last_call(), "dd/MMM/yyyy")) %></td>
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
