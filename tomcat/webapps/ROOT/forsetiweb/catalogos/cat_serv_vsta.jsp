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
	String cat_serv = (String)request.getAttribute("cat_serv");
	if(cat_serv == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("INVSERV_SERV").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("INVSERV_SERV").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("INVSERV_SERV").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","INVSERV_SERV","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","INVSERV_SERV","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "INVSERV_SERV", "VISTA", "STATUS", 2);
	
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
if(parent.tiempo.document.URL.indexOf('cat_serv_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/catalogos/cat_serv_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('cat_serv_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/catalogos/cat_serv_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFCatServDlg" method="post" enctype="application/x-www-form-urlencoded" name="cat_serv">
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_SERVICIO',<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","AGREGAR_SERVICIO",4) %>,<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","AGREGAR_SERVICIO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","AGREGAR_SERVICIO") %>" alt="" title="<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","AGREGAR_SERVICIO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_SERVICIO',<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CAMBIAR_SERVICIO",4) %>,<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CAMBIAR_SERVICIO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CAMBIAR_SERVICIO") %>" alt="" title="<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CAMBIAR_SERVICIO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_SERVICIO',<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CONSULTAR_SERVICIO",4) %>,<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CONSULTAR_SERVICIO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CONSULTAR_SERVICIO") %>" alt="" title="<%= JUtil.Msj("CEF","INVSERV_SERV","VISTA","CONSULTAR_SERVICIO",2) %>" border="0">
              <a href="javascript:try { gestionarArchivos('INVSERV_SERV', document.cat_serv.id.value, ''); } catch(err) { gestionarArchivos('INVSERV_SERV', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFCatServCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
		  <td width="3%" align="left">&nbsp;</td>
			<td width="15%" align="left"><a class="titChico" href="/servlet/CEFCatServCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFCatServCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="25%"><a class="titChico" href="/servlet/CEFCatServCtrl?orden=Linea&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="14%" align="center"><a class="titChico" href="/servlet/CEFCatServCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JInvServInvSetV2 set = new JInvServInvSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase;
		
	   	if(set.getAbsRow(i).getStatus().equals("V"))
		{
			status = JUtil.Elm(sts,2); 
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("D"))
		{
			status = JUtil.Elm(sts,3);
			clase = " class=\"txtChicoRj\"";
		}
		else
		{
			status = "";
			clase = "";
		} 	
%>
       <tr<%= clase %>>
	   		<td width="3%" align="left"><input type="radio" name="id" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="15%" align="left"><%= set.getAbsRow(i).getClave() %></td>
			<td align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="25%"><%= set.getAbsRow(i).getDescripcion_Linea() %></td>
			<td width="14%" align="center"><%= status %></td>
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
