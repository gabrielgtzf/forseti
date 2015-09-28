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
	String alm_traspasos = (String)request.getAttribute("alm_traspasos");
	if(alm_traspasos == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ALM_TRASPASOS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ALM_TRASPASOS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ALM_TRASPASOS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "ALM_TRASPASOS", "VISTA", "STATUS", 2);

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
<form action="/servlet/CEFAlmTraspasosDlg" method="post" enctype="application/x-www-form-urlencoded" name="alm_traspasos">
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
			  <a href="../forsetiweb/almacen/alm_traspasos_ent_pm.jsp"><img src="../imgfsi/p_izq_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <a href="../forsetiweb/almacen/alm_traspasos_tmp_pm.jsp"><img src="../imgfsi/p_inf_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",2) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <a href="../forsetiweb/almacen/alm_traspasos_sts_pm.jsp"><img src="../imgfsi/p_der_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",3) %>" width="24" height="24" border="0"/></a></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFAlmTraspasosCtrl"><img src="../imgfsi/actualizar24.png" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/> 
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
			  <input name="proceso" type="hidden" value="ACTUALIZAR"/>
			  <input name="tipomov" type="hidden" value="TRASPASOS"/>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_TRASPASO',<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","AGREGAR_TRASPASO",4) %>,<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","AGREGAR_TRASPASO",5) %>)" src="../imgfsi/pm_agregar.png" title="<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","AGREGAR_TRASPASO",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_TRASPASO',<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","CONSULTAR_TRASPASO",4) %>,<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","CONSULTAR_TRASPASO",5) %>)" src="../imgfsi/pm_cambiar.png" title="<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","CONSULTAR_TRASPASO",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",3) %>')) { establecerProcesoSVE(this.form.proceso, 'CANCELAR_TRASPASO',<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","CANCELAR_TRASPASO",4) %>,<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","CANCELAR_TRASPASO",5) %>); } else { return false; } " src="../imgfsi/pm_cancelar.png" title="<%= JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","CANCELAR_TRASPASO",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_TRASPASO',400,250)" src="../imgfsi/pm_rastrear_registro.png" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",4) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
 	</td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="5" cellspacing="0">
        <tr>			
			<td width="10%" align="center">&nbsp;</td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFAlmTraspasosCtrl?orden=Salida&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="25%" align="center"><a class="titChico" href="/servlet/CEFAlmTraspasosCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="35%"><a class="titChico" href="/servlet/CEFAlmTraspasosCtrl?orden=BodegaDEST&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="center"><a class="titChico" href="/servlet/CEFAlmTraspasosCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		</tr>
	  </table>
	  <table width="100%" border="0" cellpadding="5" cellspacing="0">
      	<tr>
			<td><a class="titChico" href="/servlet/CEFAlmTraspasosCtrl?orden=Concepto&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="35%"><a class="titChico" href="/servlet/CEFAlmTraspasosCtrl?orden=Referencia&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	 	</tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="260">&nbsp;</td>
 	</tr>
  	<tr>
      <td> 
<%
	JAlmacenesMovBodSetV2 set = new JAlmacenesMovBodSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase;
		
		if(set.getAbsRow(i).getStatus().equals("G"))
		{
			status = JUtil.Elm(sts,2); 
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("E"))
		{
			status = JUtil.Elm(sts,3); 
			clase = "";
		}
		else if(set.getAbsRow(i).getStatus().equals("R"))
		{
			status = JUtil.Elm(sts,4);
			clase = " class=\"txtChicoAz\"";
		}
		else if(set.getAbsRow(i).getStatus().equals("P"))
		{
			status = JUtil.Elm(sts,5);
			clase = " class=\"txtChicoAz\"";
		}
		else if(set.getAbsRow(i).getStatus().equals("C"))
		{
			status = JUtil.Elm(sts,6);
			clase = " class=\"txtChicoRj\"";
		}
		else
		{
			status = "";
			clase = "";
		} 
%>
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
		  <tr<%= clase  %>>
	      	<td width="10%" align="center"><input type="radio" name="ID" value="<%= set.getAbsRow(i).getID_Movimiento() %>"></td>
			<td width="10%" align="center"><%= set.getAbsRow(i).getSalida() %></td>
			<td width="25%" align="center"><%= set.getAbsRow(i).getFecha() %></td>
			<td width="35%"><%= set.getAbsRow(i).getBodegaDEST() %></td>
			<td width="20%" align="center"><%= status %></td>
		  </tr>
		 </table>
		 <table width="100%" border="0" cellpadding="5" cellspacing="0">
		  <tr<%= clase %>>
		    <td><%= set.getAbsRow(i).getConcepto() %></td>
			<td width="35%"><%= set.getAbsRow(i).getReferencia() %></td>
         </tr>
		</table>		
<%
	}
%>		
	 </td>
  </tr>
</table>
</form>
</body>
</html>
