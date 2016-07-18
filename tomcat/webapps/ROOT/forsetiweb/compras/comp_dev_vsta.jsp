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
	String comp_dev = (String)request.getAttribute("comp_dev");
	if(comp_dev == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("COMP_DEV").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("COMP_DEV").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("COMP_DEV").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","COMP_DEV","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","COMP_DEV","VISTA","COLUMNAS",2);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "COMP_DEV", "VISTA", "STATUS", 2);
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
if(parent.tiempo.document.URL.indexOf('comp_dev_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/compras/comp_dev_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('comp_dev_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/compras/comp_dev_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('comp_dev_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/compras/comp_dev_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFCompFactDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_dev" target="_self">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CARGAR_COMPRA',<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CARGAR_COMPRA",4) %>,<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CARGAR_COMPRA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CARGAR_COMPRA") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CARGAR_COMPRA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ENLAZAR_COMPRA',<%= JUtil.Msj("CEF","COMP_DEV","VISTA","ENLAZAR_COMPRA",4) %>,<%= JUtil.Msj("CEF","COMP_DEV","VISTA","ENLAZAR_COMPRA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_DEV","VISTA","ENLAZAR_COMPRA") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_DEV","VISTA","ENLAZAR_COMPRA",2) %>" border="0">
   			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_COMPRA',<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CONSULTAR_COMPRA",4) %>,<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CONSULTAR_COMPRA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CONSULTAR_COMPRA") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CONSULTAR_COMPRA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'XML_COMPRA',<%= JUtil.Msj("CEF","COMP_DEV","VISTA","XML_COMPRA",4) %>,<%= JUtil.Msj("CEF","COMP_DEV","VISTA","XML_COMPRA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_DEV","VISTA","XML_COMPRA") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_DEV","VISTA","XML_COMPRA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PDF_COMPRA',<%= JUtil.Msj("CEF","COMP_DEV","VISTA","PDF_COMPRA",4) %>,<%= JUtil.Msj("CEF","COMP_DEV","VISTA","PDF_COMPRA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_DEV","VISTA","PDF_COMPRA") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_DEV","VISTA","PDF_COMPRA",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_COMPRA',<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CANCELAR_COMPRA",4) %>,<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CANCELAR_COMPRA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CANCELAR_COMPRA") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_DEV","VISTA","CANCELAR_COMPRA",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_MOVIMIENTO',400,250)" src="../imgfsi/es_rastrear_registro.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" border="0">
              <a href="/servlet/CEFReportesCtrl?tipo=COMP_DEV" target="_self"><img src="../imgfsi/rep_compras.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
             <a href="javascript:try { gestionarArchivos('COMP_DEV', document.comp_dev.ID.value, ''); } catch(err) { gestionarArchivos('COMP_DEV', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFCompDevolucionesCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=DevReb&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=Numero&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="left"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=Referencia&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="12%" align="right"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=Total&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=Proveedor&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=Factura&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="3%" align="center"><a class="titChico" href="/servlet/CEFCompDevolucionesCtrl?orden=TFD&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JComprasDevolucionesSet set = new JComprasDevolucionesSet(request);
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
			<td align="left"><%= set.getAbsRow(i).getProveedor() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getFactura() %></td>
			<td width="3%" align="center">
			<% 
			switch(set.getAbsRow(i).getTFD()) 
			{ 
				case 3: out.print("PDF");
				break;
				case 2: out.print("TFD"); 
				break;
				case 4: out.print("CBB"); 
				break;
				case 5: out.print("EXT");
				break;
				case 6: out.print("MIX");
				break;
				default: out.print("---"); 
				break;
			} %>
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
