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
	String adm_periodos = (String)request.getAttribute("adm_periodos");
	if(adm_periodos == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ADM_PERIODOS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ADM_PERIODOS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ADM_PERIODOS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","ADM_PERIODOS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","ADM_PERIODOS","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "ADM_PERIODOS", "VISTA", "STATUS");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/>
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAdmPeriodosDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_periodos">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#333333">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="left" valign="middle"> 
			  <a href="../forsetiweb/menu_pm.jsp"><img src="../imgfsi/menu_principal.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",4) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <img src="../imgfsi/p_izq_off.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",1) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <img src="../imgfsi/p_inf_off.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <img src="../imgfsi/p_der_off.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",3) %>" width="24" height="24" border="0"/></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFAdmPeriodosCtrl"><img src="../imgfsi/actualizar24.png" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/> 
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040105.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
%>  
  <tr> 
    <td align="right"> 
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
			  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("CEF","ADM_PERIODOS","DLG","MSJ-PROCOK") %>')) { establecerProcesoSVE(this.form.proceso, 'AGREGAR_PERIODO',<%= JUtil.Msj("CEF","ADM_PERIODOS","VISTA","AGREGAR_PERIODO",4) %>,<%= JUtil.Msj("CEF","ADM_PERIODOS","VISTA","AGREGAR_PERIODO",5) %>); } else { return false; } " src="../imgfsi/pm_agregar_bd.png" title="<%= JUtil.Msj("CEF","ADM_PERIODOS","VISTA","AGREGAR_PERIODO",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("CEF","ADM_PERIODOS","DLG","MSJ-PROCOK",2) %>')) { establecerProcesoSVE(this.form.proceso, 'CERRAR_PERIODO',<%= JUtil.Msj("CEF","ADM_PERIODOS","VISTA","CERRAR_PERIODO",4) %>,<%= JUtil.Msj("CEF","ADM_PERIODOS","VISTA","CERRAR_PERIODO",5) %>); } else { return false; } " src="../imgfsi/pm_cerrar.png" title="<%= JUtil.Msj("CEF","ADM_PERIODOS","VISTA","CERRAR_PERIODO",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
	</td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="5" cellspacing="0">
          <tr>
		  	<td width="10%" align="center">&nbsp;</td>
			<td width="40%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="25%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
	 	</tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="240">&nbsp;</td>
 	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
<%
	JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
	perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
	perIni.Open();
	
	JAdmPeriodosSet set = new JAdmPeriodosSet(request);
	set.m_Where = donde;
	set.m_OrderBy = "Ano DESC, Mes DESC"; 
	set.Open();

	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, meslargo;
		if(set.getAbsRow(i).getMes() == perIni.getAbsRow(0).getMes() && set.getAbsRow(i).getAno() == perIni.getAbsRow(0).getAno())
			meslargo = JUtil.Msj("CEF","ADM_PERIODOS","DLG","ETQ");
		else
			meslargo = JUtil.convertirMesLargo(set.getAbsRow(i).getMes());
		
		if(!set.getAbsRow(i).getCerrado())
			status = JUtil.Elm(sts,1); 
		else
			status = JUtil.Elm(sts,2); 
%>
          <tr>
	      	<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getAno() + "|" + set.getAbsRow(i).getMes() %>"/></td>
			<td width="40%" align="center"><%= meslargo %></td>
			<td width="25%" align="center"><%= set.getAbsRow(i).getAno() %></td>
 			<td align="center"><%= status %></td>
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
