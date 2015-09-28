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
	String ven_dev = (String)request.getAttribute("ven_dev");
	if(ven_dev == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("VEN_DEV").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("VEN_DEV").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("VEN_DEV").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","VEN_DEV","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","VEN_DEV","VISTA","COLUMNAS",2);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "VEN_DEV", "VISTA", "STATUS", 2);
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
if(parent.tiempo.document.URL.indexOf('ven_dev_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/ventas/ven_dev_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('ven_dev_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/ventas/ven_dev_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('ven_dev_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/ventas/ven_dev_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFVenFactDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_dev" target="_self">
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
			  <input name="tipomov" type="hidden" value="DEVOLUCIONES">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CARGAR_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CARGAR_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CARGAR_VENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CARGAR_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CARGAR_VENTA",2) %>" border="0">
   			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ENLAZAR_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENLAZAR_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENLAZAR_VENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENLAZAR_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENLAZAR_VENTA",2) %>" border="0">
   			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CONSULTAR_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CONSULTAR_VENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CONSULTAR_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CONSULTAR_VENTA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'SELLAR_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","SELLAR_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","SELLAR_VENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","SELLAR_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","SELLAR_VENTA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'XML_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","XML_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","XML_VENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","XML_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","XML_VENTA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PDF_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","PDF_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","PDF_VENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","PDF_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","PDF_VENTA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ENVIAR_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENVIAR_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENVIAR_VENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENVIAR_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","ENVIAR_VENTA",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_VENTA',<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CANCELAR_VENTA",4) %>,<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CANCELAR_VENTA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CANCELAR_VENTA") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_DEV","VISTA","CANCELAR_VENTA",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_MOVIMIENTO',400,250)" src="../imgfsi/es_rastrear_registro.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" border="0">
              <a href="javascript:try { gestionarArchivos('VEN_DEV', document.ven_dev.ID.value, ''); } catch(err) { gestionarArchivos('VEN_DEV', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFVenDevolucionesCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'IMPRIMIR',400,250)" src="../imgfsi/imprimir.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",2) %>" border="0">
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
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=DevReb&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=Numero&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="left"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=Referencia&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="12%" align="right"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=Total&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=Cliente&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=Factura&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="3%" align="center"><a class="titChico" href="/servlet/CEFVenDevolucionesCtrl?orden=TFD&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JVentasDevolucionesSet set = new JVentasDevolucionesSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase, devreb;
		
		if(set.getAbsRow(i).getDevReb().equals("REB"))
			devreb = JUtil.Msj("GLB","GLB","GLB","REBAJA");
		else
			devreb = JUtil.Msj("GLB","GLB","GLB","DEVOLUCION");
		
	   	if(set.getAbsRow(i).getStatus().equals("G"))
		{
			status = JUtil.Elm(sts,2); 
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("C"))
		{
			status = JUtil.Elm(sts,3);
			clase = " class=\"txtChicoRj\"";
		}
		else if(set.getAbsRow(i).getStatus().equals("E"))
		{
			status = JUtil.Elm(sts,4);
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("R"))
		{
			status = JUtil.Elm(sts,5);
			clase = " class=\"txtChicoAz\"";
		}
		else
		{
			status = "";
			clase = "";
		} 	
%>
          <tr<%= clase %>>
		    <td width="3%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Devolucion() %>"></td>
			<td width="10%" align="center"><%= devreb %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getNumero() %></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getFecha() %></td>
			<td width="6%" align="left"><%= set.getAbsRow(i).getReferencia() %></td>
			<td width="5%" align="center"><%= status %></td>
			<td width="12%" align="right"><%= set.getAbsRow(i).getSimbolo() + " " + set.getAbsRow(i).getTotal() %></td>
			<td align="left"><%= set.getAbsRow(i).getCliente() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getFactura() %></td>
			<td width="3%" align="center"><% if(set.getAbsRow(i).getTFD() == 3) { out.print("PDF"); } else if(set.getAbsRow(i).getTFD() == 2) { out.print("TFD"); } else { out.print("---"); } %></td>
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
