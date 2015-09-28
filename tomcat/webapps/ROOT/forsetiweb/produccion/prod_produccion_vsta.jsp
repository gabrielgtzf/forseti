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
	String prod_produccion = (String)request.getAttribute("prod_produccion");
	if(prod_produccion == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
		// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("PROD_PRODUCCION").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("PROD_PRODUCCION").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("PROD_PRODUCCION").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","COLUMNAS",2);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "PROD_PRODUCCION", "VISTA", "STATUS", 2);
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
if(parent.tiempo.document.URL.indexOf('prod_produccion_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/produccion/prod_produccion_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('prod_produccion_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/produccion/prod_produccion_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('prod_produccion_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/produccion/prod_produccion_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form accept-charset=utf-8 action="/servlet/CEFProdProduccionDlg" method="post" enctype="application/x-www-form-urlencoded" name="prod_produccion" target="_self">
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
          	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_PRODUCCION',<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","AGREGAR_PRODUCCION",4) %>,<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","AGREGAR_PRODUCCION",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","AGREGAR_PRODUCCION") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","AGREGAR_PRODUCCION",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'APLICAR_PRODUCCION',<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","APLICAR_PRODUCCION",4) %>,<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","APLICAR_PRODUCCION",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","APLICAR_PRODUCCION") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","APLICAR_PRODUCCION",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_PRODUCCION',<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CONSULTAR_PRODUCCION",4) %>,<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CONSULTAR_PRODUCCION",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CONSULTAR_PRODUCCION") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CONSULTAR_PRODUCCION",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_PRODUCCION',<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CANCELAR_PRODUCCION",4) %>,<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CANCELAR_PRODUCCION",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CANCELAR_PRODUCCION") %>" alt="" title="<%= JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CANCELAR_PRODUCCION",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_MOVIMIENTO',400,250)" src="../imgfsi/es_rastrear_registro.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" border="0">
              <a href="javascript:try { gestionarArchivos('PROD_PRODUCCION', document.prod_produccion.ID.value, ''); } catch(err) { gestionarArchivos('PROD_PRODUCCION', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFProdProduccionCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=Numero&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="center"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=CDA&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=NumProc&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=Actual&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFProdProduccionCtrl?orden=Directa&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JProdProdSetV2 set = new JProdProdSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase, clase2, clase3;
		
		if(set.getAbsRow(i).getStatus().equals("G"))
		{
			status = JUtil.Elm(sts,1); 
			clase = " class=\"titChicoNeg\"";
			clase2 = " class=\"txtCuerpoNg\"";
			clase3 = " class=\"txtChicoNeg\"";
		}
		else if(set.getAbsRow(i).getStatus().equals("E"))
		{
			status = JUtil.Elm(sts,2);
			clase = " class=\"titChicoNeg\"";
			clase2 = " class=\"txtCuerpoNg\"";
			clase3 = " class=\"txtChicoNeg\"";
		}
		else if(set.getAbsRow(i).getStatus().equals("C"))
		{
			status = JUtil.Elm(sts,3);
			clase = " class=\"titChicoRj\"";
			clase2 = " class=\"txtCuerpoRj\"";
			clase3 = " class=\"txtChicoRj\"";
		}
		else if(set.getAbsRow(i).getStatus().equals("R"))
		{
			status = JUtil.Elm(sts,4);
			clase = " class=\"titChicoAz\"";
			clase2 = " class=\"txtCuerpoAz\"";
			clase3 = " class=\"txtChicoAz\"";
		}
		else if(set.getAbsRow(i).getStatus().equals("P"))
		{
			status = JUtil.Elm(sts,5);
			clase = " class=\"titChicoAz\"";
			clase2 = " class=\"txtCuerpoAz\"";
			clase3 = " class=\"txtChicoAz\"";
		}
		else
		{
			status = "";
			clase = "";
			clase2 = "";
			clase3 = "";
		}
		
		if(!set.getAbsRow(i).getCDA() && !set.getAbsRow(i).getStatus().equals("C"))
		{
			clase = " class=\"titChicoAzc\"";
			clase2 = " class=\"txtCuerpoAzc\"";
			clase3 = " class=\"txtChicoAzc\"";
		}
%>
          <tr>
	      	<td width="3%" align="center"<%= clase %>><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Reporte() %>"></td>
			<td width="5%" align="center"<%= clase %>><%= set.getAbsRow(i).getNumero() %></td>
			<td width="15%" align="center"<%= clase %>><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy") %></td>
			<td align="left"<%= clase %>><%= set.getAbsRow(i).getConcepto() %></td>
			<td width="10%" align="center"<%= clase %>><%= status %></td>
			<td width="5%" align="center"<%= clase %>><%= set.getAbsRow(i).getCDA() %></td>
			<td width="5%" align="center"<%= clase %>><%= set.getAbsRow(i).getNumProc() %></td>
			<td width="5%" align="center"<%= clase %>><%= set.getAbsRow(i).getActual() %></td>
			<td width="5%" align="center"<%= clase %>><%= (set.getAbsRow(i).getDirecta() ? "X" : "&nbsp;" ) %></td>
          </tr>
		  <tr>
		  	<td colspan="9">
				
				
				
				
     		<table width="100%" border="0" cellspacing="0" cellpadding="2">
<%
		JProdProdSetDetV2 SetDet = new JProdProdSetDetV2(request);
		SetDet.m_Where = "ID_Reporte = '" + set.getAbsRow(i).getID_Reporte() + "'";
        SetDet.m_OrderBy = "Partida ASC";
		SetDet.Open();
		              				
		for(int d = 0; d < SetDet.getNumRows(); d++)
		{
%>				
				<tr> 
				  <td width="15%" align="center"<%= clase2 %>><%= JUtil.obtFechaTxt(SetDet.getAbsRow(d).getFecha(),"dd/MMM/yyyy") %></td>
                  <td width="15%"<%= clase2 %>><%= SetDet.getAbsRow(d).getClave() %></td>
                  <td<%= clase2 %>><%= SetDet.getAbsRow(d).getDescripcion() %></td>
                  <td width="20%"<%= clase2 %>><%= SetDet.getAbsRow(d).getFormula() %></td>
                  <td width="10%" align="right"<%= clase2 %>><%= SetDet.getAbsRow(d).getCantidad() %></td>
                  <td width="5%" align="center"<%= clase2 %>><%= SetDet.getAbsRow(d).getUnidad() %></td>
				  <td width="15%"<%= clase2 %>><%= SetDet.getAbsRow(d).getLote() %></td>
                </tr>
				<tr> 
					<td colspan="8">
<% 
			JProdProdSetProcV2 SetProc = new JProdProdSetProcV2(request);
            SetProc.m_Where = "ID_Reporte = '" + set.getAbsRow(i).getID_Reporte() + "' AND Partida = '" + (d+1) + "'";
			SetProc.m_OrderBy = "ID_Proceso ASC";
       		SetProc.Open();

			for(int p = 0; p < SetProc.getNumRows(); p++)
			{
%>					
				  		<table width="100%" border="0" cellspacing="0" cellpadding="2">
						  <tr<%= clase3 %>>
						    <td width="20%"><%= SetProc.getAbsRow(p).getNombre() %></td>
							<td width="16%" align="right"><%= JUtil.obtFechaTxt(SetProc.getAbsRow(p).getFecha(), "dd/MMM/yyyy") + " <b>" + JUtil.obtFechaTxt(SetProc.getAbsRow(p).getFechaSP(), "dd/MMM/yyyy") + "</b>" %></td>
            				<td width="10%" align="right"><%= SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getCantidad() : "" %></td>
							<td width="8%" align="right"><%= SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getMasMenos() : "" %></td>
            				<td width="6%" align="center"><%= SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getUnidad() : "" %></td>
							<td width="12%"><%= SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getClave() : "" %></td>
            				<td><%= SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getDescripcion() : "" %></td>
							<td width="5%" align="right"><%= SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getPorcentaje() + "%" : "" %></td>
            			  </tr>
						  <tr>
						    <td colspan="7">
<%
				JProdProdSetDetprodV2 SetDetprod = new JProdProdSetDetprodV2(request);
              	SetDetprod.m_Where = "ID_Reporte = '" + set.getAbsRow(i).getID_Reporte() + "' AND Partida = '" + (d+1) + "' AND ID_Proceso = '" + (p+1) + "'";
				SetDetprod.m_OrderBy = "Secuencia ASC";
            	SetDetprod.Open();
				
				for(int dp = 0; dp < SetDetprod.getNumRows(); dp++)
				{
%>
							  <table width="100%" border="0" cellspacing="0" cellpadding="2">
								<tr<%= clase3 %>> 
								  <td width="10%" align="right"><%= SetDetprod.getAbsRow(dp).getCantidad() %></td>
								  <td width="10%" align="right"><%= SetDetprod.getAbsRow(dp).getMasMenos() %></td>
								  <td width="5%" align="center"><%= SetDetprod.getAbsRow(dp).getUnidad() %></td>
								  <td width="15%"><%= SetDetprod.getAbsRow(dp).getID_Prod() %></td>
								  <td><%= SetDetprod.getAbsRow(dp).getDescripcion() %></td>
								 </tr>
							  </table>
<%
				}
%>
							</td>
						  </tr>
						</table>
<%
			 }
 		}
%>
			</table>
						
				
				
				
				
				
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
