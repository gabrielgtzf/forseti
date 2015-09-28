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
	String conta_tipopolizas = (String)request.getAttribute("conta_tipopolizas");
	if(conta_tipopolizas == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CONT_TIPOPOLIZA").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CONT_TIPOPOLIZA").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CONT_TIPOPOLIZA").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","COLUMNAS",1);
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
<form action="/servlet/CEFContaTipoPolizasDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_tipopolizas">
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_TIPOPOLIZA',<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","AGREGAR_TIPOPOLIZA",4) %>,<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","AGREGAR_TIPOPOLIZA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","AGREGAR_TIPOPOLIZA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","AGREGAR_TIPOPOLIZA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_TIPOPOLIZA',<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","CAMBIAR_TIPOPOLIZA",4) %>,<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","CAMBIAR_TIPOPOLIZA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","CAMBIAR_TIPOPOLIZA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","CAMBIAR_TIPOPOLIZA",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_TIPOPOLIZA',<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","ELIMINAR_TIPOPOLIZA",4) %>,<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","ELIMINAR_TIPOPOLIZA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","ELIMINAR_TIPOPOLIZA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","ELIMINAR_TIPOPOLIZA",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos('CONT_TIPOPOLIZA', document.conta_tipopolizas.id_clasificacion.value, ''); } catch(err) { gestionarArchivos('CONT_TIPOPOLIZA', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFContaTipoPolizasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
		    <td width="10%" align="left"><a class="titChico" href="/servlet/CEFContaTipoPolizasCtrl?orden=Ficha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
     		<td width="85%" align="left"><a class="titChico" href="/servlet/CEFContaTipoPolizasCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
        <table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
<%
	JContaPolizasClasificacionesSet set = new JContaPolizasClasificacionesSet(request);
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
%>
		<tr>
		  <td width="5%" align="center"><input type="radio" name="id_clasificacion" value="<%= set.getAbsRow(i).getID_Clasificacion() %>"></td>
		  <td width="10%" align="left"><%= set.getAbsRow(i).getFicha() %></td>
          <td width="85%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
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
