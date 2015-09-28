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
<%@ page import="forseti.*,forseti.sets.*"%>
<%
	String ayuda_tipo = (String)request.getAttribute("ayuda_tipo");
	if(ayuda_tipo == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_AYUDATIP").generarTitulo();
	String donde = JUtil.getSesionAdmin(request).getSesion("ADMIN_AYUDATIP").generarWhere();
	String orden = JUtil.getSesionAdmin(request).getSesion("ADMIN_AYUDATIP").generarOrderBy();
	String colvsta = JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","COLUMNAS",1);
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
	top.location.href = "../forsetiadmin/forseti.html"
}
if(parent.tiempo.document.URL.indexOf('tiempo.html') == -1) {
	parent.tiempo.document.location.href = "../forsetiadmin/tiempo.html"
}
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiadmin/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiadmin/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/SAFAyudaTipoDlg" method="post" enctype="application/x-www-form-urlencoded" name="ayuda_tipo" target="ventEm">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo  %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje); 
%>  
  <tr>
    <td bgcolor="#333333">
		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right">
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
                  <input name="submit" type="image" onClick="javascript:establecerProceso(this.form.proceso, 'AGREGAR_AYUDA',<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","AGREGAR_AYUDA",4) %>,<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","AGREGAR_AYUDA",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","AGREGAR_AYUDA") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","AGREGAR_AYUDA",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProceso(this.form.proceso, 'CAMBIAR_AYUDA',<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","CAMBIAR_AYUDA",4) %>,<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","CAMBIAR_AYUDA",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","CAMBIAR_AYUDA") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","CAMBIAR_AYUDA",2) %>" border="0">
                  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProceso(this.form.proceso, 'ELIMINAR_AYUDA',<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","ELIMINAR_AYUDA",4) %>,<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","ELIMINAR_AYUDA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","ELIMINAR_AYUDA") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_AYUDATIP","VISTA","ELIMINAR_AYUDA",2) %>" border="0">
                  <a href="/servlet/SAFAyudaTipoCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
                </div></td>
        </tr>
      </table></td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FF6600">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
          <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="15%" align="left"><a class="titChico" href="/servlet/SAFAyudaTipoCtrl?orden=ID_Tipo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="82%" align="left"><a class="titChico" href="/servlet/SAFAyudaTipoCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
		JAyudaTipoSet set = new JAyudaTipoSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.ConCat(true);
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			
%>
          <tr>
	      	<td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Tipo() %>"></td>
	      	<td width="15%" align="left"><%= set.getAbsRow(i).getID_Tipo() %></td>
		    <td width="82%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
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
