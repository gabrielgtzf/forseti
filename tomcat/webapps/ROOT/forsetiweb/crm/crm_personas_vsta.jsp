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
	String crm_personas = (String)request.getAttribute("crm_personas");
	if(crm_personas == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CRM_PERSONAS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CRM_PERSONAS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CRM_PERSONAS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","CRM_PERSONAS","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","CRM_PERSONAS","VISTA","COLUMNAS",2);
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
if(parent.tiempo.document.URL.indexOf('crm_personas_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/crm/crm_personas_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('crm_personas_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/crm/crm_personas_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('crm_personas_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/crm/crm_personas_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFCRMPersonasDlg" method="post" enctype="application/x-www-form-urlencoded" name="crm_personas" target="_self">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_PERSONA',<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","AGREGAR_PERSONA",4) %>,<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","AGREGAR_PERSONA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","AGREGAR_PERSONA") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","AGREGAR_PERSONA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_PERSONA',<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CAMBIAR_PERSONA",4) %>,<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CAMBIAR_PERSONA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CAMBIAR_PERSONA") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CAMBIAR_PERSONA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_PERSONA',<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CONSULTAR_PERSONA",4) %>,<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CONSULTAR_PERSONA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CONSULTAR_PERSONA") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_PERSONAS","VISTA","CONSULTAR_PERSONA",2) %>" border="0">
              <a href="/servlet/CEFCRMPersonasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td align="left"><a class="titChico" href="/servlet/CEFCRMPersonasCtrl?orden=full_name&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFCRMPersonasCtrl?orden=tr_es&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="40%" align="left"><a class="titChico" href="/servlet/CEFCRMPersonasCtrl?orden=nm_legal&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFCRMPersonasCtrl?orden=dt_modified&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JCRMContactListSet set = new JCRMContactListSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	
	for(int i=0; i < set.getNumRows(); i++)
	{
	String clase = (set.getAbsRow(i).getBO_private() == 1 ? " class=\"txtChicoAzc\"" : "");
%>
          <tr<%=clase%>>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getGU_contact() %>"></td>
			<td align="left"><%= set.getAbsRow(i).getFull_name() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getTR_es() %></td>
			<td width="40%" align="left"><%= set.getAbsRow(i).getNM_legal() %></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getDT_modified() %></td>
		  </tr>		
<%
	}
	//out.println(set.getSQL());
%>		

     </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
