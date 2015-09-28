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
	String adm_vendedores_dlg = (String)request.getAttribute("adm_vendedores_dlg");
	if(adm_vendedores_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("ADM_VENDEDORES").generarTitulo(JUtil.Msj("CEF","ADM_VENDEDORES","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	String sts = JUtil.Msj("CEF","ADM_VENDEDORES","VISTA","STATUS",2);

	JVendedoresSet set = new JVendedoresSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_VENDEDOR") || request.getParameter("proceso").equals("CONSULTAR_VENDEDOR") )
	{
		set.m_Where = "ID_Vendedor = " + JUtil.p(request.getParameter("id"));
		set.Open();
	}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.idvendedor.value, 1, 32000) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %>", formAct.nombre.value, 1, 80) ||
		!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","COMISION") %>", formAct.comision.value, 0, 100, 2)  )
	{
		return false;
	}
	else
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
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this);" action="/servlet/CEFAdmVendedoresDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_vendedores_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clockCef"> 
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmVendedoresCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr> 
        </table> 
      </td>
    </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="109" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%> 
	<tr> 
      <td> 
	     <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td><div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></div></td>
            <td><input class="cpoColAzc" name="idvendedor" type="text" size="7" maxlength="5"<% if(request.getParameter("proceso").equals("CAMBIAR_VENDEDOR")) { out.print(" readonly=\"true\""); } %>>
			           </td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></div></td>
            <td><input name="nombre" type="text" id="nombre" size="40" maxlength="80"></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","COMISION") %></div></td>
            <td><input name="comision" type="text" id="comision" size="5" maxlength="5"> %</td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></div></td>
            <td><select name="status" class="cpoBco">
                <option value="A"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("A")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VENDEDOR") || request.getParameter("proceso").equals("CONSULTAR_VENDEDOR")) { 
											if(set.getAbsRow(0).getStatus().equals("A")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,2) %></option>
                <option value="B"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("B")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VENDEDOR") || request.getParameter("proceso").equals("CONSULTAR_VENDEDOR")) { 
											if(set.getAbsRow(0).getStatus().equals("B")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,3) %></option>
              </select></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
        </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_vendedores_dlg.idvendedor.value = '<% if(request.getParameter("idvendedor") != null) { out.print( request.getParameter("idvendedor") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VENDEDOR")) { out.print( set.getAbsRow(0).getID_Vendedor() ); } else { out.print(""); } %>'
document.adm_vendedores_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VENDEDOR")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.adm_vendedores_dlg.comision.value = '<% if(request.getParameter("comision") != null) { out.print( request.getParameter("comision") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VENDEDOR")) { out.print( set.getAbsRow(0).getComision() ); } else { out.print("1"); } %>'
</script>
</body>
</html>
