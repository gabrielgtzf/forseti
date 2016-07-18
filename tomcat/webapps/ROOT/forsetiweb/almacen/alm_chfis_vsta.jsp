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
	String alm_chfis = (String)request.getAttribute("alm_chfis");
	if(alm_chfis == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ALM_CHFIS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ALM_CHFIS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ALM_CHFIS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","ALM_CHFIS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","ALM_CHFIS","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "ALM_CHFIS", "VISTA", "STATUS", 2);

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
if(parent.tiempo.document.URL.indexOf('alm_chfis_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/almacen/alm_chfis_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('alm_chfis_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/almacen/alm_chfis_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('alm_chfis_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/almacen/alm_chfis_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAlmChFisDlg" method="post" enctype="application/x-www-form-urlencoded" name="alm_chfis" target="_self">
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
			  <input name="tipomov" type="hidden" value="CHFIS">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","AGREGAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","AGREGAR_CHFIS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","AGREGAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","AGREGAR_CHFIS",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CAMBIAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CAMBIAR_CHFIS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CAMBIAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CAMBIAR_CHFIS",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CONSULTAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CONSULTAR_CHFIS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CONSULTAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CONSULTAR_CHFIS",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'CALCULAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CALCULAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CALCULAR_CHFIS",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CALCULAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CALCULAR_CHFIS",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'CERRAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CERRAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CERRAR_CHFIS",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CERRAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CERRAR_CHFIS",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'GENERAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","GENERAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","GENERAR_CHFIS",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","GENERAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","GENERAR_CHFIS",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CANCELAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CANCELAR_CHFIS",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CANCELAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","CANCELAR_CHFIS",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_CHFIS',<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","ELIMINAR_CHFIS",4) %>,<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","ELIMINAR_CHFIS",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","ELIMINAR_CHFIS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_CHFIS","VISTA","ELIMINAR_CHFIS",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_CHFIS',400,250)" src="../imgfsi/es_rastrear_registro.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" border="0">
              <a href="/servlet/CEFReportesCtrl?tipo=ALM_CHFIS" target="_self"><img src="../imgfsi/rep_almacen.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
              <a href="javascript:try { gestionarArchivos('ALM_CHFIS', document.alm_chfis.ID.value, ''); } catch(err) { gestionarArchivos('ALM_CHFIS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFAlmChFisCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'IMPRIMIR',400,250)" src="../imgfsi/imprimir.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",2) %>" border="0">
           </div>
		  </td>
        </tr> 
      </table>
	</td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
          <tr>
			<td width="2%" align="center">&nbsp;</td>
			<td width="3%" align="center"><a class="titChico" href="/servlet/CEFAlmChFisCtrl?orden=Chequeo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAlmChFisCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAlmChFisCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="40%" align="center"><a class="titChico" href="/servlet/CEFAlmChFisCtrl?orden=Cerrado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="40%" align="center"><a class="titChico" href="/servlet/CEFAlmChFisCtrl?orden=Generado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JAlmacenesCHFISSet set = new JAlmacenesCHFISSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase;
		
		if(set.getAbsRow(i).getStatus().equals("G"))
		{
			status = JUtil.Elm(sts,2); 
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("E"))
		{
			status = JUtil.Elm(sts,3); 
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("C"))
		{
			status = JUtil.Elm(sts,4);
			clase = " class=\"txtChicoRj\"";
		}
		else
		{
			status = "";
			clase = "";
		} 
%>
          <tr<%= clase  %>>

	      	<td width="2%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_CHFIS() %>"></td>
			<td width="3%" align="center"><%= set.getAbsRow(i).getChequeo() %></td>
			<td width="10%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy") %></td>
			<td width="5%" align="center"><%= status %></td>
			<td width="40%" align="center"><%= (set.getAbsRow(i).getCerrado() ? "X" : "--") %></td>
			<td width="40%" align="center"><%= (set.getAbsRow(i).getGenerado() ? "X" : "--") %></td>

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
