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
<%@ page import="forseti.*,java.io.*,fsi_admin.*,forseti.sets.*" %>
<%
	String adm_bd = (String)request.getAttribute("adm_bd");
	if(adm_bd == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp"); 
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarTitulo();
	String donde = JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarWhere();
	String orden = JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarOrderBy();
	String colvsta = JUtil.Msj("SAF","ADMIN_BD","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("SAF","ADMIN_BD","VISTA","COLUMNAS",1);
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
<form action="/servlet/SAFAdmBDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_bd" target="_self">
  <div id="topbar"> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr> 
        <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo  %></td>
      </tr>
      <%	
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje); 
	%>
      <tr> 
        <td bgcolor="#333333"> <table width="100%" border="0" cellpadding="0" cellspacing="5">
            <tr> 
              <td> <div align="right"> 
                  <input name="proceso" type="hidden" value="ACTUALIZAR">
				  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONFIGURAR_PROPIEDADES',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CONFIGURAR_PROPIEDADES",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CONFIGURAR_PROPIEDADES",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CONFIGURAR_PROPIEDADES") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CONFIGURAR_PROPIEDADES",2) %>" border="0">
                  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'ACTUALIZAR_SERVIDOR',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ACTUALIZAR_SERVIDOR",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ACTUALIZAR_SERVIDOR",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ACTUALIZAR_SERVIDOR") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ACTUALIZAR_SERVIDOR",2) %>" border="0">
				  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_EMPRESA',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","AGREGAR_EMPRESA",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","AGREGAR_EMPRESA",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","AGREGAR_EMPRESA") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","AGREGAR_EMPRESA",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_PROPIEDADES',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CAMBIAR_PROPIEDADES",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CAMBIAR_PROPIEDADES",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CAMBIAR_PROPIEDADES") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","CAMBIAR_PROPIEDADES",2) %>" border="0">
                  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_EMPRESA',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ELIMINAR_EMPRESA",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ELIMINAR_EMPRESA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ELIMINAR_EMPRESA") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","ELIMINAR_EMPRESA",2) %>" border="0">
				  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'RESPALDAR_EMPRESA',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESPALDAR_EMPRESA",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESPALDAR_EMPRESA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESPALDAR_EMPRESA") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESPALDAR_EMPRESA",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RESTAURAR_EMPRESA',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESTAURAR_EMPRESA",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESTAURAR_EMPRESA",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESTAURAR_EMPRESA") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","RESTAURAR_EMPRESA",2) %>" border="0">
   				  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'GENERAR_REPORTE',<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","GENERAR_REPORTE",4) %>,<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","GENERAR_REPORTE",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","GENERAR_REPORTE") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","GENERAR_REPORTE",2) %>" border="0">
				  <a href="/servlet/SAFAdmBDCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
              	<td width="5%" align="center">&nbsp;</td>
              	<td width="10%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Fechaalta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Usuario&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="20%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Compania&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Mail&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Web&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Direccion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=Poblacion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="5%" align="left"><a class="titChico" href="/servlet/SAFAdmBDCtrl?orden=CP&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            </tr>
          </table></td>
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
	JBDRegistradasSet set = new JBDRegistradasSet(null);
	set.ConCat(true);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	
	for(int i = 0; i < set.getNumRows(); i++)
	{
%>
          <tr<%= (!set.getAbsRow(i).getSU().equals("3")) ? " class=\"txtChicoRj\"" : "" %>> 
            <td width="5%" align="center"> 
              <input type="radio" name="id" value="<%= set.getAbsRow(i).getID_BD() %>"></td>
            <td width="10%" align="left"><%= set.getAbsRow(i).getNombre().substring(6) %></td>
			<td width="10%" align="left"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFechaalta(), "dd/MMM/yyyy") %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getUsuario() %></td>
			<td width="20%" align="left"><%= set.getAbsRow(i).getCompania() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getMail() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getWeb() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getDireccion() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getPoblacion() %></td>
			<td width="5%" align="left"><%= set.getAbsRow(i).getCP() %></td>
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
