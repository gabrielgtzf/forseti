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
<%@ page import="forseti.*, forseti.sets.*, forseti.almacen.*, java.util.*, java.io.*"%>
<%
	String vales_caja_dlg = (String)request.getAttribute("vales_caja_dlg");
	if(vales_caja_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarTitulo(JUtil.Msj("CEF","BANCAJ_VALES","VISTA",request.getParameter("proceso"),3));

	JCajasValesSetV2 smod = new JCajasValesSetV2(request);
	if(request.getParameter("proceso").equals("CAMBIAR_VALE") )
	{
		smod.m_Where = "ID_Vale = '" + JUtil.p(request.getParameter("ID")) + "'";
		smod.Open();
	}
	
	Calendar fecha = GregorianCalendar.getInstance();
	
	String Tipos = JUtil.Msj("CEF","BANCAJ_VALES","DLG","TIPOS");

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
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker_pm.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(	!esNumeroDecimal("Total:", formAct.total.value, 0.01, 9999999999, 2) ||
		!esCadena("Concepto:", formAct.concepto.value, 1, 80) )
		return false;
	else
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
	
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFValesCajaDlg" method="post" enctype="application/x-www-form-urlencoded" name="vales_caja_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFValesCajaCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
        	<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040205.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 	  <td height="150" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td  bgcolor="#FFFFFF"> 
		<table width="100%" border="0" cellspacing="5" cellpadding="5">
          <tr> 
            <td width="50%">
				  			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
							<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"/>
							<input name="ID" type="hidden" id="ID" value="<%= request.getParameter("ID")%>"/>
                   <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %> </td>
			<td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
           </tr> 
		   <tr>            
			<td>
			  <select style="width:100%" name="idtipo">
                <option value="P"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("P")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("P")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,1) %></option>
                <option value="F"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("F")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("F")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,2) %></option>
                <option value="A"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("A")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("A")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,3) %></option>
                <option value="G"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("G")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("G")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,4) %></option>
                <option value="C"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("C")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("C")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,5) %></option>
               <option value="T"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("T")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("T")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,6) %></option>
                	</select>
			</td>
    		<td><table width="100%"><tr><td><input name="fecha" type="text" id="fecha" style="width:100%" maxlength="15" readonly="true"/></td><td width="24"><a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" width="24" height="24" align="absmiddle"></a></td></tr></table></td>
		  </tr>
        </table>
		<table width="100%" border="0" cellspacing="5" cellpadding="5">
          <tr> 
        	<td width="70%"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
			<td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %></td>
          <tr>
		    <td><table width="100%"><tr><td width="35%"><input name="idgasto" type="text" id="idgasto" style="width:100%" maxlength="20"/></td><td width="24"><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg_pm.jsp?formul=vales_caja_dlg&lista=idgasto&idcatalogo=15&nombre=GASTOS&destino=idgasto_nombre',250,350);"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0" width="24" height="24"/></a></td>
										<td><input name="idgasto_nombre" type="text" id="idgasto_nombre" style="width:100%" maxlength="250" readonly="true"/></td></tr></table></td>
		  	<td><input name="total" type="text" id="total" style="width:100%" maxlength="15"/></td>
		  </tr>
		 </table>       		
		 <table width="100%" border="0" cellspacing="5" cellpadding="5">
          <tr> 
        	<td><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
          </tr>
		    <td><input name="concepto" type="text" id="iconcepto" style="width:100%" maxlength="80"/></td>
          </tr>
         </table> 
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.vales_caja_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( JUtil.obtFechaTxt( smod.getAbsRow(0).getFecha(), "dd/MMM/yyyy")); } else { out.print( JUtil.obtFechaTxt(fecha,"dd/MMM/yyyy") ); } %>' 
document.vales_caja_dlg.idgasto.value = '<% if(request.getParameter("idgasto") != null) { out.print( request.getParameter("idgasto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getID_Gasto() ); } else { out.print(""); } %>'  
document.vales_caja_dlg.idgasto_nombre.value = '<% if(request.getParameter("idgasto_nombre") != null) { out.print( request.getParameter("idgasto_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.vales_caja_dlg.concepto.value = '<% if(request.getParameter("concepto") != null) { out.print( request.getParameter("concepto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getConcepto() ); } else { out.print(""); } %>' 
document.vales_caja_dlg.total.value = '<% if(request.getParameter("total") != null) { out.print( request.getParameter("total") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getTotal() ); } else { out.print("0.00"); } %>' 
</script>
</body>
</html>
