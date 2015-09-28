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
	String vales_caja = (String)request.getAttribute("vales_caja");
	if(vales_caja == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","BANCAJ_VALES","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","BANCAJ_VALES","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "BANCAJ_VALES", "VISTA", "STATUS", 2);

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
if(parent.tiempo.document.URL.indexOf('vales_caja_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/caja_y_bancos/vales_caja_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('vales_caja_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/caja_y_bancos/vales_caja_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('vales_caja_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/caja_y_bancos/vales_caja_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFValesCajaDlg" method="post" enctype="application/x-www-form-urlencoded" name="vales_caja" target="_self">
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
	<td bgcolor="333333">
		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right"> 
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","AGREGAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","AGREGAR_VALE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","AGREGAR_VALE") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","AGREGAR_VALE",2) %>" border="0">
             <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","CAMBIAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","CAMBIAR_VALE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","CAMBIAR_VALE") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","CAMBIAR_VALE",2) %>" border="0">
               <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'TRASPASAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","TRASPASAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","TRASPASAR_VALE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","TRASPASAR_VALE") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","TRASPASAR_VALE",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_VALE',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","ELIMINAR_VALE",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","ELIMINAR_VALE",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","ELIMINAR_VALE") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","ELIMINAR_VALE",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PROTEGER_CAJA',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","PROTEGER_CAJA",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","PROTEGER_CAJA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","PROTEGER_CAJA") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","PROTEGER_CAJA",2) %>" border="0">
          	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'GENERAR_GASTO',<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","GENERAR_GASTO",4) %>,<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","GENERAR_GASTO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","GENERAR_GASTO") %>" alt="" title="<%= JUtil.Msj("CEF","BANCAJ_VALES","VISTA","GENERAR_GASTO",2) %>" border="0">
          	  <a href="javascript:try { gestionarArchivos('BANCAJ_VALES', document.vales_caja.ID.value, ''); } catch(err) { gestionarArchivos('BANCAJ_VALES', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFValesCajaCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'IMPRIMIR',400,250)" src="../imgfsi/imprimir.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",2) %>" border="0">
             </div></td>
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
	  		<td width="3%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=ID_Gasto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="left"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Provisional&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Final&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Factura&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Pago&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Compra&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFValesCajaCtrl?orden=Traspaso&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JCajasValesSetV2 set = new JCajasValesSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	
	float Provisional = 0.0f, Final = 0.0f, Factura = 0.0f, Compra = 0.0f, Pago = 0.0f, Traspaso = 0.0f;

	for(int i=0; i < set.getNumRows(); i++)
	{
		Provisional += set.getAbsRow(i).getProvisional();
		Final += set.getAbsRow(i).getFinal();
		Factura += set.getAbsRow(i).getFactura();
		Compra += set.getAbsRow(i).getCompra();
		Pago += set.getAbsRow(i).getPago();
		Traspaso += set.getAbsRow(i).getTraspaso();
			 
%>
       	<tr>
	        <td width="3%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Vale() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getFecha() %></td>
			<td width="5%" align="left"><%= set.getAbsRow(i).getID_Gasto() %></td>
			<td width="20%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td align="left"><%= set.getAbsRow(i).getConcepto() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getProvisional() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getFinal() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getFactura() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getPago() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getCompra() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getTraspaso() %></td>
		</tr>		
<%
	}
%>
		<tr>
	        <td width="3%" align="center">&nbsp;</td>
			<td width="10%" align="center">&nbsp;</td>
			<td width="5%" align="left">&nbsp;</td>
			<td width="20%" align="left">&nbsp;</td>
			<td align="left">&nbsp;</td>
			<td width="6%" align="right" class="txtChicoAzc"><%= Provisional %></td>
			<td width="6%" align="right" class="txtChicoAzc"><%= Final %></td>
			<td width="6%" align="right" class="txtChicoAzc"><%= Factura %></td>
			<td width="6%" align="right" class="txtChicoAzc"><%= Compra %></td>
			<td width="6%" align="right" class="txtChicoAzc"><%= Pago %></td>
			<td width="6%" align="right" class="txtChicoAzc"><%= Traspaso %></td>
		</tr>		
     </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
