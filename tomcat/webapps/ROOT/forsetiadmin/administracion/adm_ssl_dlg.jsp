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
	String adm_ssl_dlg = (String)request.getAttribute("adm_ssl_dlg");
	if(adm_ssl_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_SSL").generarTitulo(JUtil.Msj("SAF","ADMIN_SSL","VISTA",request.getParameter("proceso"),3));
	
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
	if(!esCadena("Nombre", formAct.nombre.value, 3, 20) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %>", formAct.password.value, 3, 30) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %>", formAct.confpwd.value, 3, 30) )
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmSSLDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_ssl_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clock"> 
              <%  if(JUtil.getSesionAdmin(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAdmSSLCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
	%>
    <tr> 
      <td> 
	  	<table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td width="30%" align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
              Nombre del Archivo: </td>
            <td> forsetikeystore<input name="nombre" type="text" class="cpoCol" size="15" maxlength="20"></td>
          </tr>
          <tr> 
            <td align="right">Alias</td>
            <td><input name="alias" type="text" id="alias" size="20" maxlength="20"></td>
          </tr>
          <tr> 
            <td align="right">Nombre Común:</td>
            <td><input name="cn" type="text" id="cn" size="80" maxlength="255">
              Puede ser el dominio o la IP del servidor Ej: forseti.org.mx o 54.209.221.45</td>
          </tr>
		  <tr> 
            <td align="right">Unidad Organizativa:</td>
            <td><input name="ou" type="text" id="ou" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right">Organización</td>
            <td><input name="o" type="text" id="o" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right">Localidad</td>
            <td><input name="l" type="text" id="l" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right">Estado:</td>
            <td><input name="st" type="text" id="web" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right">Codigo de Pais:</td>
            <td><input name="c" type="text" id="c" size="15" maxlength="2"></td>
          </tr>
		  <tr> 
            <td align="right">Nombres alternativos:</td>
            <td><input name="altnames" type="text" id="altnames" size="80" maxlength="255"> Ej: dns:forseti.org.mx,ip:127.0.0.1,ip:54.201.9.221</td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %></td>
            <td><input name="password" type="password" class="cpoCol" id="password" size="15" maxlength="30"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %></td>
            <td><input name="confpwd" type="password" class="cpoCol" id="confpwd" size="15" maxlength="30"></td>
          </tr>
        </table>
	  </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
document.adm_ssl_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.alias.value = '<% if(request.getParameter("alias") != null) { out.print( request.getParameter("alias") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.cn.value = '<% if(request.getParameter("cn") != null) { out.print( request.getParameter("cn") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.ou.value = '<% if(request.getParameter("ou") != null) { out.print( request.getParameter("ou") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.o.value = '<% if(request.getParameter("o") != null) { out.print( request.getParameter("o") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.l.value = '<% if(request.getParameter("l") != null) { out.print( request.getParameter("l") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.st.value = '<% if(request.getParameter("st") != null) { out.print( request.getParameter("st") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.c.value = '<% if(request.getParameter("c") != null) { out.print( request.getParameter("c") ); } else { out.print(""); } %>'
document.adm_ssl_dlg.altnames.value = '<% if(request.getParameter("altnames") != null) { out.print( request.getParameter("altnames") ); } else { out.print(""); } %>'
</script>
</body>
</html>
