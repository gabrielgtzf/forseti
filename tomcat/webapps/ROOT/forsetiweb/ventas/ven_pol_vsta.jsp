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
	String ven_pol = (String)request.getAttribute("ven_pol");
	if(ven_pol == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("VEN_POL").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("VEN_POL").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("VEN_POL").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","VEN_POL","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","VEN_POL","VISTA","COLUMNAS",2);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "VEN_POL", "VISTA", "STATUS", 1);
	String ent = JUtil.getSesion(request).getSesion("VEN_POL").getEspecial();
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
parent.tiempo.document.location.href = "../forsetiweb/ventas/ven_pol_tmp.jsp"
parent.entidad.document.location.href = "../forsetiweb/ventas/ven_pol_ent.jsp"
if(parent.ztatuz.document.URL.indexOf('ven_pol_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/ventas/ven_pol_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFVenPoliticasDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_pol" target="_self">
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
			  <input name="tipomov" type="hidden" value="<%= ent %>">
<%
	if(ent.equals("CLIENTES"))
	{
%>
				<input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PRECIOS_CLIENTE',<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_CLIENTE",4) %>,<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_CLIENTE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_CLIENTE") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_CLIENTE",2) %>" border="0">
             	<input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'DESCUENTOS_CLIENTE',<%= JUtil.Msj("CEF","VEN_POL","VISTA","DESCUENTOS_CLIENTE",4) %>,<%= JUtil.Msj("CEF","VEN_POL","VISTA","DESCUENTOS_CLIENTE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_POL","VISTA","DESCUENTOS_CLIENTE") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_POL","VISTA","DESCUENTOS_CLIENTE",2) %>" border="0">
<%
	}
	else if(ent.equals("PRODUCTOS"))
	{
%>
				<input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PRECIOS_PROD',<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_PROD",4) %>,<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_PROD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_PROD") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_POL","VISTA","PRECIOS_PROD",2) %>" border="0">
             	<input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AUMENTOS_PROD',<%= JUtil.Msj("CEF","VEN_POL","VISTA","AUMENTOS_PROD",4) %>,<%= JUtil.Msj("CEF","VEN_POL","VISTA","AUMENTOS_PROD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_POL","VISTA","AUMENTOS_PROD") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_POL","VISTA","AUMENTOS_PROD",2) %>" border="0">
             	<input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CANTIDADES_PROD',<%= JUtil.Msj("CEF","VEN_POL","VISTA","CANTIDADES_PROD",4) %>,<%= JUtil.Msj("CEF","VEN_POL","VISTA","CANTIDADES_PROD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_POL","VISTA","CANTIDADES_PROD") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_POL","VISTA","CANTIDADES_PROD",2) %>" border="0">
<%
	}
	else if(ent.equals("ENTIDADES"))
	{
%>
				<input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'POLITICAS_ENTIDAD',<%= JUtil.Msj("CEF","VEN_POL","VISTA","POLITICAS_ENTIDAD",4) %>,<%= JUtil.Msj("CEF","VEN_POL","VISTA","POLITICAS_ENTIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","VEN_POL","VISTA","POLITICAS_ENTIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","VEN_POL","VISTA","POLITICAS_ENTIDAD",2) %>" border="0">
<%
	}
%>
              <a href="/servlet/CEFReportesCtrl?tipo=VEN_POL" target="_self"><img src="../imgfsi/rep_ventas.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
              <a href="javascript:try { gestionarArchivos2('VEN_POL', '<%= ent %>', document.ven_pol.ID.value, ''); } catch(err) { gestionarArchivos2('VEN_POL', '<%= ent %>', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFVenPoliticasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
            </div></td>
        </tr>
      </table></td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
	
	if(ent.equals("CLIENTES"))
	{
%>
          <tr>
			<td width="5%" align="center">&nbsp;</td>
			<td width="12%" align="left"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=Numero&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="83%" align="left"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
<%
	}
	else if(ent.equals("PRODUCTOS"))
	{
%>
          <tr>
			<td width="2%" align="left">&nbsp;</td>
			<td width="12%" align="left"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=Unidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right" class="titChico"><!--<%= JUtil.Elm(coletq,etq++) %>--><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=P1&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=P2&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=P3&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=P4&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=P5&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=PMin&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="6%" align="right"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=PMax&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
<%
	}
	if(ent.equals("ENTIDADES"))
	{
%>
          <tr>
			<td width="2%" align="left">&nbsp;</td>
			<td width="12%" align="left"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFVenPoliticasCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>	
<%
	}
%>
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
	if(ent.equals("CLIENTES"))
	{
		JClientClientSetV2 set = new JClientClientSetV2(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			int mod = i % 2;
%>
          <tr>
	      	<td width="5%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="12%" align="left"><%= set.getAbsRow(i).getNumero() %></td>
			<td width="83%" align="left"><%= set.getAbsRow(i).getNombre() %></td>
		  </tr>		
<%
		}
	}
	else if(ent.equals("PRODUCTOS"))
	{
		JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
				
		for(int i=0; i < set.getNumRows(); i++)
		{
			byte dec = 2;
			float UC;
			if(set.getAbsRow(i).getID_Moneda() != 1) 
			{
				JContaMonedasSetV2 setMon = new JContaMonedasSetV2(request);
				setMon.m_Where = "Clave = '" + set.getAbsRow(i).getID_Moneda() + "'";
				setMon.Open();
				
				UC = JUtil.redondear(set.getAbsRow(i).getPComp() * setMon.getAbsRow(0).getTC(), (byte)4);
			} 
			else
				UC = set.getAbsRow(i).getPComp() ;
			
			float P1 =  ((UC != 0 && set.getAbsRow(i).getP1() != 0) ? JUtil.redondear((((set.getAbsRow(i).getP1() / UC) - 1) * 100), dec) : 0); 
			float P2 =  ((UC != 0 && set.getAbsRow(i).getP2() != 0) ? JUtil.redondear((((set.getAbsRow(i).getP2() / UC) - 1) * 100), dec) : 0); 	
			float P3 =  ((UC != 0 && set.getAbsRow(i).getP3() != 0) ? JUtil.redondear((((set.getAbsRow(i).getP3() / UC) - 1) * 100), dec) : 0); 	
			float P4 =  ((UC != 0 && set.getAbsRow(i).getP4() != 0) ? JUtil.redondear((((set.getAbsRow(i).getP4() / UC) - 1) * 100), dec) : 0); 	
			float P5 =  ((UC != 0 && set.getAbsRow(i).getP5() != 0) ? JUtil.redondear((((set.getAbsRow(i).getP5() / UC) - 1) * 100), dec) : 0); 	
			float PMin =  ((UC != 0 && set.getAbsRow(i).getPMin() != 0) ? JUtil.redondear((((set.getAbsRow(i).getPMin() / UC) - 1) * 100), dec) : 0); 	
			float PMax =  ((UC != 0 && set.getAbsRow(i).getPMax() != 0) ? JUtil.redondear((((set.getAbsRow(i).getPMax() / UC) - 1) * 100), dec) : 0); 	
	
%>
          <tr>
	      	<td width="2%" align="left"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="12%" align="left"><%= set.getAbsRow(i).getClave() %></td>
			<td align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getUnidad() %></td>
			<td width="6%" align="right"><%= UC %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getP1() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getP2() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getP3() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getP4() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getP5() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getPMin() %></td>
			<td width="6%" align="right"><%= set.getAbsRow(i).getPMax() %></td>
          </tr>
		  <tr>
	      	<td colspan="5" align="right"><b>Porcentajes:</b></td>
			<td width="6%" align="right"><b><%= ((P1 <= 0) ? "<font color='red'>" + Float.toString(P1) + "</font>" : Float.toString(P1) ) %></b></td>
			<td width="6%" align="right"><b><%= ((P2 <= 0) ? "<font color='red'>" + Float.toString(P2) + "</font>" : Float.toString(P2) ) %></b></td>
			<td width="6%" align="right"><b><%= ((P3 <= 0) ? "<font color='red'>" + Float.toString(P3) + "</font>" : Float.toString(P3) ) %></b></td>
			<td width="6%" align="right"><b><%= ((P4 <= 0) ? "<font color='red'>" + Float.toString(P4) + "</font>" : Float.toString(P4) ) %></b></td>
			<td width="6%" align="right"><b><%= ((P5 <= 0) ? "<font color='red'>" + Float.toString(P5) + "</font>" : Float.toString(P5) ) %></b></td>
			<td width="6%" align="right"><b><%= ((PMin <= 0) ? "<font color='red'>" + Float.toString(PMin) + "</font>" : Float.toString(PMin) ) %></b></td>
			<td width="6%" align="right"><b><%= ((PMax <= 0) ? "<font color='red'>" + Float.toString(PMax) + "</font>" : Float.toString(PMax) ) %></b></td>
          </tr>			
<%
		}
	}
	if(ent.equals("ENTIDADES"))
	{
		JVentasEntidadesSetIdsV2 set = new JVentasEntidadesSetIdsV2(request,JUtil.getSesion(request).getID_Usuario(),"CEF-X");
    	set.Open();
		for(int i = 0; i < set.getNumRows(); i++)
		{
			String str = set.getAbsRow(i).getDescripcion(); 
%>
          <tr bgcolor="#999999">
	      	<td width="2%" align="center" class="txtChicoBco"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Entidad() %>"></td>
			<td width="12%" align="left" class="txtChicoBco"><%= set.getAbsRow(i).getID_Entidad() %></td>
			<td width="83%" align="left" class="txtChicoBco"><%= set.getAbsRow(i).getDescripcion() %></td>
		  </tr>		
<%
		}
		JInvServInvPreciosSet setp = new JInvServInvPreciosSet(request);
		setp.m_Where = donde;
		setp.m_OrderBy = orden; 
		setp.Open();
		for(int j = 0; j < setp.getNumRows(); j++)
		{
%>
          <tr>
	      	<td width="2%" align="center">&nbsp;</td>
			<td width="12%" align="left"><%= setp.getAbsRow(j).getClave() %></td>
			<td width="83%" align="left"><%= setp.getAbsRow(j).getDescripcion() %></td>
		  </tr>		
<%
		}

	}	
%>		
     </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
