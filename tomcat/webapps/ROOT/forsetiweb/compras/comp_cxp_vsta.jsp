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
	String comp_cxp = (String)request.getAttribute("comp_cxp");
	if(comp_cxp == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("COMP_CXP").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("COMP_CXP").generarWhere();
	String todo = (String)request.getAttribute("TODO");
	String orden = (todo != null) ? "ID_Aplicacion ASC, Clave ASC" : JUtil.getSesion(request).getSesion("COMP_CXP").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","COMP_CXP","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","COMP_CXP","VISTA","COLUMNAS",2);
	int etq = 1, col = 1;
	String Tipos = JUtil.Msj("CEF", "COMP_CXP", "VISTA", "TIPO", 2);
	String sts = JUtil.Msj("CEF", "COMP_CXP", "VISTA", "STATUS");
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
if(parent.tiempo.document.URL.indexOf('comp_cxp_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/compras/comp_cxp_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('comp_cxp_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/compras/comp_cxp_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('comp_cxp_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/compras/comp_cxp_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFCompCXPDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_cxp" target="_self">
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
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PAGAR_CXP',<%= JUtil.Msj("CEF","COMP_CXP","VISTA","PAGAR_CXP",4) %>,<%= JUtil.Msj("CEF","COMP_CXP","VISTA","PAGAR_CXP",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_CXP","VISTA","PAGAR_CXP") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_CXP","VISTA","PAGAR_CXP",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'SALDAR_CXP',<%= JUtil.Msj("CEF","COMP_CXP","VISTA","SALDAR_CXP",4) %>,<%= JUtil.Msj("CEF","COMP_CXP","VISTA","SALDAR_CXP",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_CXP","VISTA","SALDAR_CXP") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_CXP","VISTA","SALDAR_CXP",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'APLICAR_ANTICIPO',<%= JUtil.Msj("CEF","COMP_CXP","VISTA","APLICAR_ANTICIPO",4) %>,<%= JUtil.Msj("CEF","COMP_CXP","VISTA","APLICAR_ANTICIPO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_CXP","VISTA","APLICAR_ANTICIPO") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_CXP","VISTA","APLICAR_ANTICIPO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_CUENTA',<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CONSULTAR_CUENTA",4) %>,<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CONSULTAR_CUENTA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CONSULTAR_CUENTA") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CONSULTAR_CUENTA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'DEVOLVER_ANTICIPO',<%= JUtil.Msj("CEF","COMP_CXP","VISTA","DEVOLVER_ANTICIPO",4) %>,<%= JUtil.Msj("CEF","COMP_CXP","VISTA","DEVOLVER_ANTICIPO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","COMP_CXP","VISTA","DEVOLVER_ANTICIPO") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_CXP","VISTA","DEVOLVER_ANTICIPO",2) %>" border="0">
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR',<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CANCELAR",4) %>,<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CANCELAR",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CANCELAR") %>" alt="" title="<%= JUtil.Msj("CEF","COMP_CXP","VISTA","CANCELAR",2) %>" border="0">
			 <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_MOVIMIENTO',400,250)" src="../imgfsi/es_rastrear_registro.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" border="0">
              <a href="/servlet/CEFReportesCtrl?tipo=COMP_CXP" target="_self"><img src="../imgfsi/rep_compras.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
             <a href="javascript:try { gestionarArchivos('COMP_CXP', document.comp_cxp.id.value, ''); } catch(err) { gestionarArchivos('COMP_CXP', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFCompCXPCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
<%
	if(todo == null)
	{
%>
			<td width="2%" align="center">&nbsp;</td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=ID_TipoCP&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="3%" align="center"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=ID_Aplicacion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="22%" align="left"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="22%" align="left"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="3%" align="left"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Moneda&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="right"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Total&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="right"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Saldo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Vencimiento&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Pol&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="9%" align="left"><a class="titChico" href="/servlet/CEFCompCXPCtrl?orden=Ref&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
<%
	}
	else
	{
%>
			<td width="2%" align="center">&nbsp;</td>
			<td width="5%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="3%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="22%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="22%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="3%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="right" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="right" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="9%" align="left" class="titChico"><%= JUtil.Elm(colvsta,col++) %></a></td>
<%
	}
%>
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
	JProveeCXPSetV2 set = new JProveeCXPSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String tipocxc, status, clase;
		
	   	if(set.getAbsRow(i).getID_TipoCP().equals("ALT")) 
			tipocxc = JUtil.Elm(Tipos,2);
		else if(set.getAbsRow(i).getID_TipoCP().equals("ANT")) 
			tipocxc = JUtil.Elm(Tipos,3);
		else if(set.getAbsRow(i).getID_TipoCP().equals("PAG")) 
			tipocxc = JUtil.Elm(Tipos,4);
		else if(set.getAbsRow(i).getID_TipoCP().equals("SAL")) 
			tipocxc = JUtil.Elm(Tipos,5);
		else if(set.getAbsRow(i).getID_TipoCP().equals("APL")) 
			tipocxc = JUtil.Elm(Tipos,6);
		else if(set.getAbsRow(i).getID_TipoCP().equals("DPA")) 
			tipocxc = JUtil.Elm(Tipos,7);
		else if(set.getAbsRow(i).getID_TipoCP().equals("DEV")) 
			tipocxc = JUtil.Elm(Tipos,8);
		else
			tipocxc = "";
			
		if(set.getAbsRow(i).getStatus().equals("G"))
		{
			status = JUtil.Elm(sts,1); 
			if(set.getAbsRow(i).getID_TipoCP().equals("ALT"))
				clase = " class=\"txtChicoAzc\"";
			else if(set.getAbsRow(i).getID_TipoCP().equals("ANT"))
				clase = " class=\"txtChicoAz\"";
			else 
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
          <tr<%= clase %>>
		    <td width="2%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getClave() %>"></td>
			<td width="5%" align="left"><% if(todo == null || set.getAbsRow(i).getID_TipoCP().equals("ALT") || set.getAbsRow(i).getID_TipoCP().equals("ANT")) { out.print(tipocxc); } else { out.print("&nbsp;"); } %></td>
			<td width="3%" align="center"><% if(todo == null || set.getAbsRow(i).getID_TipoCP().equals("ALT") || set.getAbsRow(i).getID_TipoCP().equals("ANT")) { out.print(set.getAbsRow(i).getClave()); } else { out.print("&nbsp;"); } %></td>
			<td width="7%" align="center"><% if(todo == null || set.getAbsRow(i).getID_TipoCP().equals("ALT") || set.getAbsRow(i).getID_TipoCP().equals("ANT")) { out.print(JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy")); } else { out.print("&nbsp;"); } %></td>
			<td width="22%" align="left"><% if(todo == null || set.getAbsRow(i).getID_TipoCP().equals("ALT") || set.getAbsRow(i).getID_TipoCP().equals("ANT")) { out.print(set.getAbsRow(i).getNombre()); } else { out.print(JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy") + " - " +  tipocxc + " - " + set.getAbsRow(i).getClave()); } %></td>
			<td width="22%" align="left"><%= set.getAbsRow(i).getConcepto() %></td>
			<td width="5%" align="center"><%= status %></td>
			<td width="3%" align="left"><%= set.getAbsRow(i).getMonedaSim() %></td>
			<td width="5%" align="right"><%= set.getAbsRow(i).getTotal() %></td>
			<td width="5%" align="right"><% if(set.getAbsRow(i).getID_TipoCP().equals("ALT") || set.getAbsRow(i).getID_TipoCP().equals("ANT")) { out.print (set.getAbsRow(i).getSaldo()); } else { out.print("&nbsp;"); } %></td>
			<td width="7%" align="center"><% if(set.getAbsRow(i).getID_TipoCP().equals("ALT")) { out.print (set.getAbsRow(i).getVencimiento()); } else { out.print("&nbsp;"); } %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getPol() %></td>
			<td width="9%" align="left"><%= set.getAbsRow(i).getRef() %></td>
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
