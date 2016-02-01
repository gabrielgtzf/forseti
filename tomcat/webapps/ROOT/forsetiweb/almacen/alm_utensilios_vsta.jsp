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
	String alm_utensilios = (String)request.getAttribute("alm_utensilios");
	if(alm_utensilios == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ALM_UTENSILIOS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ALM_UTENSILIOS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ALM_UTENSILIOS").generarOrderBy();
	String colvsta;
	String coletq;
	int etq = 1, col = 1;
	String vista = JUtil.getSesion(request).getSesion("ALM_UTENSILIOS").getVista();
	String sts = JUtil.Msj("CEF", "ALM_UTENSILIOS", "VISTA", "STATUS", 1);
	if(vista.equals("MOVIMIENTOS"))
	{
		colvsta = JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","COLUMNAS",2);
		coletq = JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","COLUMNAS",1);
	}
	else
	{
		colvsta = JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","COLUMNAS",4);
		coletq = JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","COLUMNAS",3);
	}
	
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
parent.tiempo.document.location.href = "../forsetiweb/almacen/alm_utensilios_tmp.jsp"
parent.entidad.document.location.href = "../forsetiweb/almacen/alm_utensilios_ent.jsp"
if(parent.ztatuz.document.URL.indexOf('alm_utensilios_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/almacen/alm_utensilios_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAlmUtensiliosDlg" method="post" enctype="application/x-www-form-urlencoded" name="alm_utensilios" target="_self">
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
			  <input name="tipomov" type="hidden" value="<%= vista %>">
<%
	if(vista.equals("MOVIMIENTOS"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ENTRADA_UTENSILIOS',<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","ENTRADA_UTENSILIOS",4) %>,<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","ENTRADA_UTENSILIOS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","ENTRADA_UTENSILIOS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","ENTRADA_UTENSILIOS",2) %>" border="0">
<%
	}
%>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'SALIDA_UTENSILIOS',<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","SALIDA_UTENSILIOS",4) %>,<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","SALIDA_UTENSILIOS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","SALIDA_UTENSILIOS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","SALIDA_UTENSILIOS",2) %>" border="0">
<%
	if(vista.equals("MOVIMIENTOS"))
	{
%>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_MOVIMIENTO',<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CONSULTAR_UTENSILIOS",4) %>,<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CONSULTAR_UTENSILIOS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CONSULTAR_UTENSILIOS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CONSULTAR_UTENSILIOS",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_MOVIMIENTO',<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CANCELAR_UTENSILIOS",4) %>,<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CANCELAR_UTENSILIOS",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CANCELAR_UTENSILIOS") %>" alt="" title="<%= JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA","CANCELAR_UTENSILIOS",2) %>" border="0">
<%
	}
%>
              <a href="javascript:try { gestionarArchivos2('ALM_UTENSILIOS', '<%= vista %>', document.alm_utensilios.ID.value, ''); } catch(err) { gestionarArchivos2('ALM_UTENSILIOS', '<%= vista %>', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFAlmUtensiliosCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
<%
	if(vista.equals("MOVIMIENTOS"))
	{
%>
		  <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="3%" align="center"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Num&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Tipo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="right" class="titChico"><% etq++; %><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=ID_Prod&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="22%" align="left"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%" align="left"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Ref&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
<%
	}
	else
	{
%>
		  <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="15%" align="right"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Cantidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="2%">&nbsp;</td>
			<td width="15%"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=ID_Prod&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=ID_Linea&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="25%" align="left"><a class="titChico" href="/servlet/CEFAlmUtensiliosCtrl?orden=Linea&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	if(vista.equals("MOVIMIENTOS"))
	{
		JInvservAlmacenMovimDetallesUtensiliosSet set = new JInvservAlmacenMovimDetallesUtensiliosSet(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			String status, clase;
			
			if(set.getAbsRow(i).getStatus().equals("G"))
			{
				status = JUtil.Elm(sts,1); 
				clase = "";
			}
			else if(set.getAbsRow(i).getStatus().equals("C"))
			{
				status = JUtil.Elm(sts,2);
				clase = " class=\"txtChicoRj\"";
			}
			else
			{
				status = "";
				clase = "";
			} 
%>
          <tr<%= clase  %>>
	      	<td width="3%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Movimiento() %>"></td>
			<td width="3%" align="center"><%= set.getAbsRow(i).getNum() %></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getFecha() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getTipo() %></td>
			<td width="10%" align="right"><b><%= (set.getAbsRow(i).getID_Concepto() == -1 ? set.getAbsRow(i).getEntrada() : -set.getAbsRow(i).getSalida())  %></b>&nbsp;&nbsp;</td>
			<td width="10%">&nbsp;&nbsp;<%= set.getAbsRow(i).getID_Prod() %></td>
			<td align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="7%" align="center"><%= status %></td>
			<td width="22%" align="left"><%= set.getAbsRow(i).getConcepto() %></td>
			<td width="15%" align="left"><%= set.getAbsRow(i).getRef() %></td>
		  </tr>		
<%
		}
	}
	else
	{
		JInvservAlmacenMovimDetallesUtensiliosSetExist set = new JInvservAlmacenMovimDetallesUtensiliosSetExist(request);
		set.m_Where = donde;
		set.m_OrderBy = orden; 
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			String clase;
			
			if(set.getAbsRow(i).getStatus().equals("V"))
			{
				clase = "";
			}
			else if(set.getAbsRow(i).getStatus().equals("D"))
			{
				clase = " class=\"txtChicoRj\"";
			}
			else
			{
				clase = "";
			} 
%>
          <tr<%= clase  %>>
	      	<td width="3%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Prod() %>"></td>
			<td width="15%" align="right"><b><%= set.getAbsRow(i).getCantidad() %></b></td>
			<td width="2%">&nbsp;</td>
			<td width="15%"><%= set.getAbsRow(i).getID_Prod() %></td>
			<td align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
			<td width="7%" align="center"><%= set.getAbsRow(i).getID_Linea() %></td>
			<td width="25%" align="left"><%= set.getAbsRow(i).getLinea() %></td>
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
