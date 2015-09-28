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
<%@ page import="forseti.*, forseti.sets.*, forseti.catalogos.*, java.util.*, java.io.*"%>
<%
	String cat_serv_dlg = (String)request.getAttribute("cat_serv_dlg");
	if(cat_serv_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("INVSERV_SERV").generarTitulo(JUtil.Msj("CEF","INVSERV_SERV","VISTA",request.getParameter("proceso"),3));
	String etq = JUtil.Msj("CEF","INVSERV_INVSERV","DLG","ETQ");
	String coletq = JUtil.Msj("CEF","INVSERV_INVSERV","DLG","COLUMNAS");
	String sts = JUtil.Msj("CEF", "INVSERV_SERV", "VISTA", "STATUS", 2);

	JInvServInvSetV2 smod = new JInvServInvSetV2(request);
	JInvServInvSetMasV2 set = new JInvServInvSetMasV2(request);
	if( request.getParameter("proceso").equals("CAMBIAR_SERVICIO") || request.getParameter("proceso").equals("CONSULTAR_SERVICIO") )
	{
		smod.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		set.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		smod.Open();
		set.Open();
	}

	JInvServUnidadesSet uni = new JInvServUnidadesSet(request);
	uni.m_Where = "ID_InvServ = 'S'";
	uni.Open();
	
	session = request.getSession(true);
    JCatProdSes ses = (JCatProdSes)session.getAttribute("cat_serv_dlg");
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
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(formAct.proceso.value == "AGREGAR_SERVICIO" || formAct.proceso.value == "CAMBIAR_SERVICIO")
	{
		if(	!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","PRECIO") %> 1:', formAct.precio.value, 0, 9999999999, 2) ||
			!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","MIN") %>:', formAct.preciomin.value, 0, 9999999999, 2) ||
			!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","MAX") %>:', formAct.preciomax.value, 0, 9999999999, 2)  )
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
	else
	{	
		return true;
	}
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCatServDlg" method="post" enctype="application/x-www-form-urlencoded" name="cat_serv_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_SERVICIO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCatServCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"></td>
            <td width="50%" align="right" valign="middle">
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
            <td>
				<input name="subproceso" type="hidden" value="ENVIAR"/>
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/> 
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>"/> 
				<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
    		</tr>
			<tr>
                <td><input name="clave" type="text" style="width:50%"  maxlength="20"<% if(request.getParameter("proceso").equals("CAMBIAR_SERVICIO")) { out.print(" readonly=\"true\""); } %>/></td>
			</tr>
            <tr>
        		<td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
        	</tr>
			<tr>
				<td><input name="descripcion" type="text" id="descripcion" style="width:100%" maxlength="80"/></td>
            </tr>
          </table>
	   	 <table width="100%" border="0" cellspacing="5" cellpadding="5">
          <tr>
		    <td width="33%"><%= JUtil.Msj("GLB","GLB","GLB","PRECIO") %></td>
			<td width="33%"><%= JUtil.Msj("GLB","GLB","GLB","MIN") %></td>
        	<td><%= JUtil.Msj("GLB","GLB","GLB","MAX") %></td>
	      </tr>
		  <tr>
		    <td><input name="precio" type="text" id="precio" style="width:100%" maxlength="15"/></td>
       		<td><input name="preciomin" type="text" id="preciomin" style="width:100%" maxlength="15"/></td>
            <td><input name="preciomax" type="text" id="preciomax" style="width:100%" maxlength="15"/></td>
           </tr>
         </table>
		 <table width="100%" border="0" cellspacing="5" cellpadding="5">
          <tr> 
            <td width="70%"><%= JUtil.Msj("GLB","GLB","GLB","LINEA") %></td>
			<td><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></td>
           </tr>
			<tr>
              <td><table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td width="40%"> <input name="linea" type="text" id="linea" style="width:100%" maxlength="8"/></td>
              		<td width="24"><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg_pm.jsp?formul=cat_serv_dlg&lista=linea&idcatalogo=5&nombre=LINEAS&destino=linea_descripcion',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="24" height="24" border="0"/></a></td>
                    <td><input name="linea_descripcion" type="text" id="linea_descripcion" style="width:100%" maxlength="250" readonly="true"/></td></tr></table></td>
              <td> 
                <select style="width:100%" name="status" class="cpoColAzc">
                      <option value="V"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("V")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_SERVICIO") || request.getParameter("proceso").equals("CONSULTAR_SERVICIO")) { 
											if(set.getAbsRow(0).getStatus().equals("V")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,2) %></option>
                      <option value="D"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("D")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_SERVICIO") || request.getParameter("proceso").equals("CONSULTAR_SERVICIO")) { 
											if(set.getAbsRow(0).getStatus().equals("D")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,3) %></option>
                    </select>
              </td>
             </tr>
           </table>
		   <table width="100%" border="0" cellspacing="5" cellpadding="5">
			<tr>
			 <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","OBS") %></td>
			 <td><%= JUtil.Msj("GLB","GLB","GLB","IVA") %>&nbsp;<input type="checkbox" name="iva"/>&nbsp;<%= JUtil.Elm(etq,4) %></td>
            </tr>
            <tr> 
             <td width="100%" colspan="2"><textarea name="obs" style="width:100%" rows="3"></textarea></td>
            </tr>
          </table>
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.cat_serv_dlg.precio.value = <% if(request.getParameter("precio") != null) { out.print( request.getParameter("precio") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( set.getAbsRow(0).getPrecio() ); } else { out.print("0"); } %> 
document.cat_serv_dlg.iva.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_SERVICIO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_SERVICIO") ) { out.print( (set.getAbsRow(0).getIVA() ? "true" : "false" ) ); } else if(request.getParameter("iva") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.cat_serv_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( smod.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.cat_serv_dlg.clave.value = '<% if(request.getParameter("clave") != null) { out.print( request.getParameter("clave") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( smod.getAbsRow(0).getClave() ); } else { out.print(""); } %>' 
document.cat_serv_dlg.linea_descripcion.value = '<% if(request.getParameter("linea_descripcion") != null) { out.print( request.getParameter("linea_descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( set.getAbsRow(0).getLineaDescripcion() ); } else { out.print(""); } %>' 
document.cat_serv_dlg.linea.value = '<% if(request.getParameter("linea") != null) { out.print( request.getParameter("linea") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( smod.getAbsRow(0).getLinea() ); } else { out.print(""); } %>'  
document.cat_serv_dlg.obs.value = '<% if(request.getParameter("obs") != null) { out.print( request.getParameter("obs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( set.getAbsRow(0).getObs() ); } else { out.print(""); } %>'
document.cat_serv_dlg.preciomin.value = <% if(request.getParameter("preciomin") != null) { out.print( request.getParameter("preciomin") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( set.getAbsRow(0).getPrecioMin() ); } else { out.print("0"); } %> 
document.cat_serv_dlg.preciomax.value = <% if(request.getParameter("preciomax") != null) { out.print( request.getParameter("preciomax") ); } else if(!request.getParameter("proceso").equals("AGREGAR_SERVICIO")) { out.print( set.getAbsRow(0).getPrecioMax() ); } else { out.print("0"); } %> 
</script>
</body>
</html>
