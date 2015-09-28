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
	String comp_pol = (String)request.getAttribute("comp_pol");
	if(comp_pol == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("COMP_POL").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("COMP_POL").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("COMP_POL").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","COMP_POL","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","COMP_POL","VISTA","COLUMNAS",2);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "COMP_POL", "VISTA", "STATUS", 1);
	String ent = JUtil.getSesion(request).getSesion("COMP_POL").getEspecial();
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
parent.tiempo.document.location.href = "../forsetiweb/compras/comp_pol_tmp.jsp"
parent.entidad.document.location.href = "../forsetiweb/compras/comp_pol_ent.jsp"
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFCompPoliticasDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_pol" target="_self">
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
	if(ent.equals("PRODUCTOS"))
	{
%>
				<input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PRECIOS_PROD',<%= JUtil.Msj("CEF","COMP_POL","VISTA","PRECIOS_PROD",4) %>,<%= JUtil.Msj("CEF","COMP_POL","VISTA","PRECIOS_PROD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_POL","VISTA","PRECIOS_PROD") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_POL","VISTA","PRECIOS_PROD",2) %>" border="0">
<%
	}
%>
              <a href="/servlet/CEFCompPoliticasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
              <a href="javascript:try { gestionarArchivos2('COMP_POL', '<%= ent %>', document.comp_pol.ID.value, ''); } catch(err) { gestionarArchivos2('COMP_POL', '<%= ent %>', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
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
	
	if(ent.equals("PRODUCTOS"))
	{
%>
          <tr>
			<td width="2%" align="left">&nbsp;</td>
			<td width="12%" align="left"><a class="titChico" href="/servlet/CEFCompPoliticasCtrl?orden=Clave&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFCompPoliticasCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFCompPoliticasCtrl?orden=Unidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="right"><a class="titChico" href="/servlet/CEFCompPoliticasCtrl?orden=PComp&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	if(ent.equals("PRODUCTOS"))
	{
		JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
				
		for(int i=0; i < set.getNumRows(); i++)
		{
			JContaMonedasSetV2 setMon = new JContaMonedasSetV2(request);
			setMon.m_Where = "Clave = '" + set.getAbsRow(i).getID_Moneda() + "'";
			setMon.Open();
%>
          <tr>
	      	<td width="2%" align="left"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="12%" align="left"><%= set.getAbsRow(i).getClave() %></td>
			<td align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getUnidad() %></td>
			<td width="10%" align="right"><%= setMon.getAbsRow(0).getSimbolo() + " " + set.getAbsRow(i).getPComp() %></td>
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
