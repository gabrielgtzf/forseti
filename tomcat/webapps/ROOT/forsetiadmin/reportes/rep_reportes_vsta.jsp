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
<%@ page import="forseti.*, forseti.sets.*" %>
<%
	String reps_reportes = (String)request.getAttribute("reps_reportes");
	if(reps_reportes == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesionAdmin(request).getSesion("REPS_REPORTES").generarTitulo();
	String donde = JUtil.getSesionAdmin(request).getSesion("REPS_REPORTES").generarWhere();
	String orden = JUtil.getSesionAdmin(request).getSesion("REPS_REPORTES").generarOrderBy();
	String colvsta = JUtil.Msj("SAF","REPS_REPORTES","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("SAF","REPS_REPORTES","VISTA","COLUMNAS",1);
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/SAFReportesDlg" method="post" enctype="application/x-www-form-urlencoded" name="reps_reportes" target="_self">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo %></td>
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CARGAR_REPORTE',<%= JUtil.Msj("SAF","REPS_REPORTES","VISTA","CARGAR_REPORTE",4) %>,<%= JUtil.Msj("SAF","REPS_REPORTES","VISTA","CARGAR_REPORTE",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","REPS_REPORTES","VISTA","CARGAR_REPORTE") %>" alt="" title="<%= JUtil.Msj("SAF","REPS_REPORTES","VISTA","CARGAR_REPORTE",2) %>" border="0">
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
          <td width="3%" align="center" class="titChico">&nbsp;</td>
          <td width="12%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
          <td width="50%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
		  <td width="25%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
		  <td width="10%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
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
	JReportesBind1Set set = new JReportesBind1Set(request, JUtil.getSesionAdmin(request).getID_Usuario(), request.getParameter("tipo"), "SAF-X");
	set.ConCat(true);
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String clase;
		if(set.getAbsRow(i).getID_Report() < 10000)
			clase = " class=\"titChicoNeg\"";
		else
			clase = " class=\"titChicoAzc\"";
%>
		 <tr <%= clase %>>
		  <td width="3%" align="center"><input type="radio" name="REPID" value="<%= set.getAbsRow(i).getID_Report() %>"></td>
		  <td width="12%" align="left"><%= set.getAbsRow(i).getID_Report() %></td>
 		  <td width="50%" align="left"><%= set.getAbsRow(i).getDescription() %></td>
  		  <td width="25%" align="left"><%= set.getAbsRow(i).getTipo() %></td>
  		  <td width="10%" align="center"><%= (set.getAbsRow(i).getGraficar()) ? "X" : "&nbsp;" %></td>
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
