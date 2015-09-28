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
	String adm_bd_dlg = (String)request.getAttribute("adm_bd_dlg");
	if(adm_bd_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarTitulo(JUtil.Msj("SAF","ADMIN_BD","VISTA",request.getParameter("proceso"),3));
	JBDRegistradasSet set = new JBDRegistradasSet(null);
	if(request.getParameter("proceso").equals("CAMBIAR_PROPIEDADES"))
	{
		set.ConCat(true);
		set.m_Where = "ID_BD = '" + JUtil.p(request.getParameter("id")) + "'";
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
	if(!esCadena("<%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %>", formAct.nombre.value, 3, 20) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %>", formAct.compania.value, 3, 254) ||
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
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmBDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_bd_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAdmBDCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
                <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %></td>
            <td> <input name="nombre" type="text" class="cpoCol" size="15" maxlength="20"<%= (request.getParameter("proceso").equals("CAMBIAR_PROPIEDADES")) ? " readOnly=\"true\"" : "" %>> 
              <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE",2) %></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %></td>
            <td><input name="compania" type="text" class="cpoCol" id="compania" size="80" maxlength="255"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","DIRECCION") %></td>
            <td><input name="direccion" type="text" id="direccion" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","POBLACION") %></td>
            <td><input name="poblacion" type="text" id="poblacion" size="25" maxlength="50"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
            <td><input name="cp" type="text" id="cp" size="7" maxlength="9"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
            <td><input name="mail" type="text" id="mail" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","WEB") %></td>
            <td><input name="web" type="text" id="web" size="80" maxlength="255"></td>
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
document.adm_bd_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPRESA")) { out.print( set.getAbsRow(0).getNombre().substring(6) ); } else { out.print(""); } %>'
document.adm_bd_dlg.compania.value = '<% if(request.getParameter("compania") != null) { out.print( request.getParameter("compania") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPRESA")) { out.print( set.getAbsRow(0).getCompania() ); } else { out.print(""); } %>'
document.adm_bd_dlg.direccion.value = '<% if(request.getParameter("direccion") != null) { out.print( request.getParameter("direccion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPRESA")) { out.print( set.getAbsRow(0).getDireccion() ); } else { out.print(""); } %>'
document.adm_bd_dlg.poblacion.value = '<% if(request.getParameter("poblacion") != null) { out.print( request.getParameter("poblacion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPRESA")) { out.print( set.getAbsRow(0).getPoblacion() ); } else { out.print(""); } %>'
document.adm_bd_dlg.cp.value = '<% if(request.getParameter("cp") != null) { out.print( request.getParameter("cp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPRESA")) { out.print( set.getAbsRow(0).getCP() ); } else { out.print(""); } %>'
document.adm_bd_dlg.mail.value = '<% if(request.getParameter("mail") != null) { out.print( request.getParameter("mail") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPRESA")) { out.print( set.getAbsRow(0).getMail() ); } else { out.print(""); } %>'
document.adm_bd_dlg.web.value = '<% if(request.getParameter("web") != null) { out.print( request.getParameter("web") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPRESA")) { out.print( set.getAbsRow(0).getWeb() ); } else { out.print(""); } %>'
</script>
</body>
</html>
