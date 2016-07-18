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
	String nom_permisos = (String)request.getAttribute("nom_permisos");
	if(nom_permisos == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("NOM_PERMISOS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("NOM_PERMISOS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("NOM_PERMISOS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","NOM_PERMISOS","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","NOM_PERMISOS","VISTA","COLUMNAS",2);
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
if(parent.tiempo.document.URL.indexOf('nom_permisos_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/nomina/nom_permisos_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('nom_permisos_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/nomina/nom_permisos_ent.jsp"
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
<form action="/servlet/CEFNomPermisosDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_permisos" target="_self">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_PERMISO',<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","AGREGAR_PERMISO",4) %>,<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","AGREGAR_PERMISO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","AGREGAR_PERMISO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","AGREGAR_PERMISO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_PERMISO',<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","CAMBIAR_PERMISO",4) %>,<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","CAMBIAR_PERMISO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","CAMBIAR_PERMISO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","CAMBIAR_PERMISO",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_PERMISO',<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","ELIMINAR_PERMISO",4) %>,<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","ELIMINAR_PERMISO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","ELIMINAR_PERMISO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_PERMISOS","VISTA","ELIMINAR_PERMISO",2) %>" border="0">
			  <a href="/servlet/CEFReportesCtrl?tipo=NOM_PERMISOS" target="_self"><img src="../imgfsi/rep_nomina.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
              <a href="javascript:try { gestionarArchivos('NOM_PERMISOS', document.nom_permisos.id.value, ''); } catch(err) { gestionarArchivos('NOM_PERMISOS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFNomPermisosCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td width="2%" align="center">&nbsp;</td>
		  	<td width="6%" align="left"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=ID_Empleado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="17%" align="left"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="17%" align="left"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
    		<td width="7%" align="center"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=ID_FechaMovimiento&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
     		<td width="6%" align="center"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=Desde&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
     		<td width="6%" align="center"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=Hasta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="center"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=Total&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="33%" align="left"><a class="titChico" href="/servlet/CEFNomPermisosCtrl?orden=Obs&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JPermisosSet set = new JPermisosSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
%>
        <tr>
		  <td width="2%" align="center"><input type="radio" name="id" value="_FE_<%= set.getAbsRow(i).getID_Empleado() %>|_FM_<%= set.getAbsRow(i).getID_Movimiento() %>|_FF_<%=  JUtil.obtFechaTxt(set.getAbsRow(i).getID_FechaMovimiento(),"dd/MM/yyyy")  %>|"></td>
		  <td width="6%" align="left"><%= set.getAbsRow(i).getID_Empleado() %></td>
		  <td width="17%" align="left"><%= set.getAbsRow(i).getNombre()  %></td>
  		  <td width="17%" align="left"><%= set.getAbsRow(i).getDescripcion()  %></td>
		  <td width="7%" align="center"><%= (set.getAbsRow(i).getDiasCompletos() ? "&nbsp;" : JUtil.obtFechaTxt(set.getAbsRow(i).getID_FechaMovimiento(),"dd/MM/yyyy") ) %></td>
		  <td width="6%" align="center"><%= (set.getAbsRow(i).getDiasCompletos() ? JUtil.obtFechaTxt(set.getAbsRow(i).getDesde(),"dd/MM/yyyy") : JUtil.obtHoraTxt(set.getAbsRow(i).getHoraDesde(),"HH:mm") ) %></td>
 		  <td width="6%" align="center"><%= (set.getAbsRow(i).getDiasCompletos() ? JUtil.obtFechaTxt(set.getAbsRow(i).getHasta(),"dd/MM/yyyy") : JUtil.obtHoraTxt(set.getAbsRow(i).getHoraHasta(),"HH:mm") ) %></td>
 		  <td width="6%" align="left"><% if(set.getAbsRow(i).getDiasCompletos()) { out.print(set.getAbsRow(i).getNum_de_Dias() + " Dias"); } else { out.print(set.getAbsRow(i).getNum_de_Horas() + " Hrs"); } %></td>
 		  <td width="33%" align="left"><%= set.getAbsRow(i).getObs()  %></td>
   		</tr>		
<%
	}
	
	JPermisosGrupoSet setgrp = new JPermisosGrupoSet(request);
	setgrp.m_Where = donde;
	setgrp.m_OrderBy = orden; 
	setgrp.Open();
	for(int i=0; i < setgrp.getNumRows(); i++)
	{
%>
        <tr>
		  <td width="2%" align="center"><input type="radio" name="id" value="_FE_FSINOMINA-<%= setgrp.getAbsRow(i).getID_Sucursal() %>|_FM_<%= setgrp.getAbsRow(i).getID_Movimiento() %>|_FF_<%=  JUtil.obtFechaTxt(setgrp.getAbsRow(i).getID_FechaMovimiento(),"dd/MM/yyyy")  %>|"></td>
		  <td width="6%" align="left">&nbsp;</td>
		  <td width="17%" align="left">&nbsp;</td>
  		  <td width="17%" align="left"><%= setgrp.getAbsRow(i).getDescripcion()  %></td>
		  <td width="7%" align="center">&nbsp;</td>
		  <td width="6%" align="center"><%= JUtil.obtFechaTxt(setgrp.getAbsRow(i).getDesde(),"dd/MM/yyyy") %></td>
 		  <td width="6%" align="center"><%= JUtil.obtFechaTxt(setgrp.getAbsRow(i).getHasta(),"dd/MM/yyyy") %></td>
 		  <td width="6%" align="left"><%= (setgrp.getAbsRow(i).getNum_de_Dias() + " Dias") %></td>
 		  <td width="33%" align="left">&nbsp;</td>
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