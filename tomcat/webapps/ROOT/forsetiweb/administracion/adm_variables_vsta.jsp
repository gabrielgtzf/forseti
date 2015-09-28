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
	String adm_variables = (String)request.getAttribute("adm_variables");
	if(adm_variables == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ADM_VARIABLES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ADM_VARIABLES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ADM_VARIABLES").generarOrderBy();
			
	String ent = JUtil.getSesion(request).getSesion("ADM_VARIABLES").getEspecial();
	
	String colvsta = JUtil.Msj("CEF","ADM_VARIABLES","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","ADM_VARIABLES","VISTA","COLUMNAS",2);
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
if(parent.entidad.document.URL.indexOf('adm_variables_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/administracion/adm_variables_ent.jsp"
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
<form action="/servlet/CEFAdmVariablesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_variables" target="_self">
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
			  <input name="tipomov" type="hidden" value="<%= ent %>">
			  <input name="submit" type="image" onClick="javascript:establecerProceso(this.form.proceso, 'AGREGAR_VARIABLE',<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","AGREGAR_VARIABLE",4) %>,<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","AGREGAR_VARIABLE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","AGREGAR_VARIABLE") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","AGREGAR_VARIABLE",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProceso(this.form.proceso, 'CAMBIAR_VARIABLE',<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","CAMBIAR_VARIABLE",4) %>,<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","CAMBIAR_VARIABLE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","CAMBIAR_VARIABLE") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_VARIABLES","VISTA","CAMBIAR_VARIABLE",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos('ADM_VARIABLES', document.adm_variables.id.value, ''); } catch(err) { gestionarArchivos('ADM_VARIABLES', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFAdmVariablesCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td width="12%" align="left"><a class="titChico" href="/servlet/CEFAdmVariablesCtrl?orden=ID_Variable&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFAdmVariablesCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="40%" align="center" class="titChico"><!--<%= JUtil.Elm(coletq,etq++) %>--><%= JUtil.Elm(colvsta,col++) %></td>
 		</tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="125" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAdmVariablesSet set = new JAdmVariablesSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			String tipo = JUtil.Elm(set.getAbsRow(i).getTipo(),1);

%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Variable() %>"></td>
	      	<td width="12%" align="left"><%= set.getAbsRow(i).getID_Variable() %></td>
		    <td align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
	      	<td width="40%" align="center" class="txtChicoAzc">
<% 
		if(ent.equals("ESP"))
		{
			out.print(set.getAbsRow(i).getVEntero() + " / " + set.getAbsRow(i).getVDecimal() + " / " + 
				JUtil.obtFechaTxt(set.getAbsRow(i).getVFecha() , "dd/MMM/yyyy") + " " + JUtil.obtFechaTxt(set.getAbsRow(i).getVFecha() , "hh:mm:ss") + " / " +
				set.getAbsRow(i).getVAlfanumerico());
		}
		else
		{
			if(tipo.equals("BOOL"))
			{
				if(set.getAbsRow(i).getVEntero() == 0)
					out.print(JUtil.Msj("GLB","GLB","GLB","NO"));
				else
					out.print(JUtil.Msj("GLB","GLB","GLB","SI"));
			}
			else if(tipo.equals("INT"))
				out.print(set.getAbsRow(i).getVEntero());
			else if(tipo.equals("DECIMAL"))
				out.print(set.getAbsRow(i).getVDecimal());
			else if(tipo.equals("DATE"))
				out.print(JUtil.obtFechaTxt(set.getAbsRow(i).getVFecha() , "dd/MMM/yyyy"));
			else if(tipo.equals("TIME"))
				out.print(JUtil.obtFechaTxt(set.getAbsRow(i).getVFecha() , "hh:mm:ss"));
			else if(tipo.equals("CC"))
				out.print(JUtil.obtCuentaFormato(new StringBuffer(set.getAbsRow(i).getVAlfanumerico()), request));
			else
				out.print(set.getAbsRow(i).getVAlfanumerico());
		}
%>
			</td>
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
