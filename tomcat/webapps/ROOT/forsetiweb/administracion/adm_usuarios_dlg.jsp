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
	if( request.getParameter("proceso").equals("CAMBIAR_USUARIO") )
	{
		set.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
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
	if(!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","USUARIO") %>", formAct.elusuario.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %>", formAct.nombre.value, 1, 80) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %>", formAct.contrasena.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %>", formAct.passconf.value, 1, 10) )
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
<form onSubmit="return enviarlo(this);" action="/servlet/CEFAdmUsuariosDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_usuarios_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmUsuariosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
                <%= JUtil.Msj("GLB","GLB","GLB","USUARIO") %></div></td>
            <td><input class="cpoColAzc" name="elusuario" type="text" size="12" maxlength="10"<% if(request.getParameter("proceso").equals("CAMBIAR_USUARIO")) { out.print(" readonly=\"true\""); } %>>
			           </td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></div></td>
            <td><input name="nombre" type="text" id="nombre" size="40" maxlength="80"></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %></div></td>
            <td><input name="contrasena" type="password" id="contrasena" size="12" maxlength="10"></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %></div></td>
            <td><input name="passconf" type="password" id="passconf" size="12" maxlength="10"></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp; 
              </td>
          </tr>
        </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_usuarios_dlg.elusuario.value = '<% if(request.getParameter("elusuario") != null) { out.print( request.getParameter("elusuario") ); } else if(!request.getParameter("proceso").equals("AGREGAR_USUARIO")) { out.print( set.getAbsRow(0).getID_Usuario() ); } else { out.print(""); } %>'
document.adm_usuarios_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_USUARIO")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
</script>
</body>
</html>
