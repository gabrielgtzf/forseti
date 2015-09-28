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
	String conta_tipopolizas = (String)request.getAttribute("conta_tipopolizas");
	if(conta_tipopolizas == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CONT_TIPOPOLIZA").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CONT_TIPOPOLIZA").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CONT_TIPOPOLIZA").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
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
<form action="/servlet/CEFContaTipoPolizasDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_tipopolizas">
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
				<a href="/servlet/CEFContaTipoPolizasCtrl"><img src="../imgfsi/actualizar24.png" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/> 
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_TIPOPOLIZA',<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","AGREGAR_TIPOPOLIZA",4) %>,<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","AGREGAR_TIPOPOLIZA",5) %>)" src="../imgfsi/pm_agregar.png" title="<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","AGREGAR_TIPOPOLIZA",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_TIPOPOLIZA',<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","CAMBIAR_TIPOPOLIZA",4) %>,<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","CAMBIAR_TIPOPOLIZA",5) %>)" src="../imgfsi/pm_cambiar.png" title="<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","CAMBIAR_TIPOPOLIZA",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
              <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_TIPOPOLIZA',<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","ELIMINAR_TIPOPOLIZA",4) %>,<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","ELIMINAR_TIPOPOLIZA",5) %>); } else { return false; } " src="../imgfsi/pm_eliminar.png" title="<%= JUtil.Msj("CEF","CONT_TIPOPOLIZA","VISTA","ELIMINAR_TIPOPOLIZA",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
     </td>
   </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
        <tr>
			<td width="10%" align="center">&nbsp;</td>
		    <td width="30%" align="left"><a class="titChico" href="/servlet/CEFContaTipoPolizasCtrl?orden=Ficha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
     		<td align="left"><a class="titChico" href="/servlet/CEFContaTipoPolizasCtrl?orden=Descripcion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JContaPolizasClasificacionesSet set = new JContaPolizasClasificacionesSet(request);
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
%>
		<table width="100%" border="0" cellpadding="5" cellspacing="0">
        <tr>
		  <td width="10%" align="center"><input type="radio" name="id_clasificacion" value="<%= set.getAbsRow(i).getID_Clasificacion() %>"/></td>
		  <td width="30%" align="left"><%= set.getAbsRow(i).getFicha() %></td>
          <td align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
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
