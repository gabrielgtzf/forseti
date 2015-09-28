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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	String adm_usuarios_dlg = (String)request.getAttribute("adm_usuarios_dlg");
	if(adm_usuarios_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("ADM_USUARIOS").generarTitulo(JUtil.Msj("CEF","ADM_USUARIOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JUsuariosSet set = new JUsuariosSet(request);
	set.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	set.Open();
	      	                  
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
	if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
	{
		formAct.aceptar.disabled = true;
		return true;
	}
	else
	{
		return false;
	}
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmUsuariosDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_usuarios_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmUsuariosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
            <td width="30%">
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>"/>
				<input name="subproceso" type="hidden" value="ENVIAR"/>
                <%= JUtil.Msj("GLB","GLB","GLB","USUARIO") %></td>
            <td class="titChicoAzc"><%= set.getAbsRow(0).getID_Usuario() %></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
            <td class="titChicoAzc"><%= set.getAbsRow(0).getNombre() %></td>
          </tr>
		</table>
		<table width="100%" border="0" cellpadding="5" cellspacing="5">
			<tr>
				<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","BANCAJ","BANCOS") %></td>
			</tr>
            <tr>
				<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				<td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
				<td width="10%" align="center">&nbsp;</td>
			</tr>
<%
	JUsuariosSubmoduloBancosPerm pb = new JUsuariosSubmoduloBancosPerm(request);
	pb.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "' and Tipo = 0";
	pb.Open();
	for(int i=0; i < pb.getNumRows(); i++)
	{
%>
			<tr>
				<td align="center"><%= pb.getAbsRow(i).getClave() %></td>
				<td><%= pb.getAbsRow(i).getCuenta() %></td>
				<td align="center"><input type="checkbox" name="PER_BAN_<%= pb.getAbsRow(i).getClave() %>"<% if(pb.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>/></td>
			</tr>		
<%
	}
%>		
		</table>			
		<table width="100%" border="0" cellpadding="5" cellspacing="5">
        	<tr>
				<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","BANCAJ","CAJAS") %></td>
			</tr>
            <tr>
				<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				<td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
				<td width="10%" align="center">&nbsp;</td>
			</tr>
<%
	JUsuariosSubmoduloBancosPerm pc = new JUsuariosSubmoduloBancosPerm(request);
	pc.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "' and Tipo = 1";
	pc.Open();
	for(int i=0; i < pc.getNumRows(); i++)
	{
		int mod = i % 2;
%>
			<tr>
				<td align="center"><%= pc.getAbsRow(i).getClave() %></td>
				<td><%= pc.getAbsRow(i).getCuenta() %></td>
				<td align="center"><input type="checkbox" name="PER_CAJ_<%= pc.getAbsRow(i).getClave() %>"<% if(pc.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>></td>
			</tr>		
<%
	}
%>		
		</table>		
		<table width="100%" border="0" cellpadding="5" cellspacing="5">
        	<tr>
				<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","GLB","ALM") %></td>
			</tr>
            <tr>
				<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				<td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
				<td width="10%" align="center">&nbsp;</td>
			</tr>
<%
	JUsuariosSubmoduloBodegasPerm pbd = new JUsuariosSubmoduloBodegasPerm(request);
	pbd.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	pbd.Open();
	for(int i=0; i < pbd.getNumRows(); i++)
	{
		int mod = i % 2;
%>
			<tr>
				<td align="center"><%= pbd.getAbsRow(i).getID_Bodega() %></td>
				<td><%= pbd.getAbsRow(i).getNombre() %></td>
				<td align="center"><input type="checkbox" name="PER_BOD_<%= pbd.getAbsRow(i).getID_Bodega() %>"<% if(pbd.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>></td>
			</tr>		
<%
	}
%>		
		   </table>
		   <table width="100%" border="0" cellpadding="5" cellspacing="5">
                <tr>
					<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","GLB","COMP") %></td>
				</tr>
                <tr>
					<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				    <td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
					<td width="10%" align="center">&nbsp;</td>
				</tr>
<%
	JUsuariosSubmoduloComprasPerm pcom = new JUsuariosSubmoduloComprasPerm(request);
	pcom.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	pcom.Open();
	for(int i=0; i < pcom.getNumRows(); i++)
	{
%>
				  <tr>
					<td align="center"><%= pcom.getAbsRow(i).getID_EntidadCompra() %></td>
					<td><%= pcom.getAbsRow(i).getDescripcion() %></td>
					<td align="center"><input type="checkbox" name="PER_COM_<%= pcom.getAbsRow(i).getID_EntidadCompra() %>"<% if(pcom.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>></td>
				  </tr>		
<%
	}
%>		
		     </table>
			 <table width="100%" border="0" cellpadding="5" cellspacing="5">
                <tr>
					<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","GLB","VEN") %></td>
				</tr>
                <tr>
					<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				    <td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
					<td width="10%" align="center">&nbsp;</td>
				</tr>
<%
	JUsuariosSubmoduloVentasPerm pven = new JUsuariosSubmoduloVentasPerm(request);
	pven.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	pven.Open();
	for(int i=0; i < pven.getNumRows(); i++)
	{
%>
				  <tr>
					<td align="center"><%= pven.getAbsRow(i).getID_EntidadVenta() %></td>
					<td><%= pven.getAbsRow(i).getDescripcion() %></td>
					<td align="center"><input type="checkbox" name="PER_VEN_<%= pven.getAbsRow(i).getID_EntidadVenta() %>"<% if(pven.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>></td>
				  </tr>		
<%
	}
%>		
		     </table>
			 <table width="100%" border="0" cellpadding="5" cellspacing="5">
                <tr>
					<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","GLB","PROD") %></td>
				</tr>
                <tr>
					<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				    <td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
					<td width="10%" align="center">&nbsp;</td>
				</tr>
<%
	JUsuariosSubmoduloProduccionPerm pprd = new JUsuariosSubmoduloProduccionPerm(request);
	pprd.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	pprd.Open();
	for(int i=0; i < pprd.getNumRows(); i++)
	{
%>
				  <tr>
					<td align="center"><%= pprd.getAbsRow(i).getID_EntidadProd() %></td>
					<td><%= pprd.getAbsRow(i).getDescripcion() %></td>
					<td align="center"><input type="checkbox" name="PER_PRD_<%= pprd.getAbsRow(i).getID_EntidadProd() %>"<% if(pprd.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>></td>
				  </tr>		
<%
	}
%>		
		     </table>
			 <table width="100%" border="0" cellpadding="5" cellspacing="5">
                <tr>
					<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","GLB","NOM") %></td>
				</tr>
                <tr>
					<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				    <td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
					<td width="10%" align="center">&nbsp;</td>
				</tr>
<%
	JUsuariosSubmoduloNominaPerm pnom = new JUsuariosSubmoduloNominaPerm(request);
	pnom.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	pnom.Open();
	for(int i=0; i < pnom.getNumRows(); i++)
	{
%>
				  <tr>
					<td align="center"><%= pnom.getAbsRow(i).getID_Sucursal() %></td>
					<td><%= pnom.getAbsRow(i).getDescripcion() %></td>
					<td align="center"><input type="checkbox" name="PER_NOM_<%= pnom.getAbsRow(i).getID_Sucursal() %>"<% if(pnom.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>></td>
				  </tr>		
<%
	}
%>		
		     </table>
			 <table width="100%" border="0" cellpadding="5" cellspacing="5">
                <tr>
					<td colspan="3" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("CEF","MENU","GLB","BLQ") %></td>
				</tr>
                <tr>
					<td width="30%" align="center" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				    <td width="60%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
					<td width="10%" align="center">&nbsp;</td>
				</tr>
<%
	JUsuariosSubmoduloBlocksPerm pnot = new JUsuariosSubmoduloBlocksPerm(request);
	pnot.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	pnot.Open();
	for(int i=0; i < pnot.getNumRows(); i++)
	{
%>
				  <tr>
					<td align="center"><%= pnot.getAbsRow(i).getID_Block() %></td>
					<td><%= pnot.getAbsRow(i).getEtiqueta() %></td>
					<td align="center"><input type="checkbox" name="PER_NOT_<%= pnot.getAbsRow(i).getID_Block() %>"<% if(pnot.getAbsRow(i).getPermitido()) { out.print(" checked"); } %>></td>
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
