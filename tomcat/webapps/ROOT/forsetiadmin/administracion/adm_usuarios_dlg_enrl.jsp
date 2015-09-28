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
	String admin_usuarios_dlg = (String)request.getAttribute("admin_usuarios_dlg");
	if(admin_usuarios_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_USUARIOS").generarTitulo(JUtil.Msj("SAF","ADMIN_USUARIOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JUsuariosSet set = new JUsuariosSet(request);
	set.ConCat(true);
	set.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "'";
	set.Open();
	      	                  
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmUsuariosDlg" method="post" enctype="application/x-www-form-urlencoded" name="admin_usuarios_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAdmUsuariosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td><div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>">
				<input name="subproceso" type="hidden" value="ENVIAR">
                <%= JUtil.Msj("GLB","GLB","GLB","USUARIO") %></div></td>
            <td class="titChicoNar"><%= set.getAbsRow(0).getID_Usuario() %></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></div></td>
            <td class="titChicoNar"><%= set.getAbsRow(0).getNombre() %></td>
          </tr>
		  <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
		  <tr> 
            <td colspan="2" bgcolor="#FFFFFF">
			<table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
					<td colspan="3" bgcolor="#FF6600" class="titChico"><%= JUtil.Msj("SAF","ADMIN_USUARIOS","DLG","COLUMNAS") %></td>
				</tr>
                <tr>
					<td width="10%" class="titChicoNar"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				    <td width="80%" class="titChicoNar"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
					<td width="10%" align="center">&nbsp;</td>
				</tr>
<%
	JUsuariosSubmoduloRolesEnrl prm = new JUsuariosSubmoduloRolesEnrl(request);
	prm.ConCat(true);
	prm.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "' and ID_Rol like 'saf-%'";
	prm.Open();
	
	for(int i=0; i < prm.getNumRows(); i++)
	{
%>
				  <tr>
					<td width="10%"><%= prm.getAbsRow(i).getID_Rol() %></td>
					<td width="80%"><%= prm.getAbsRow(i).getNombre() %></td>
					<td width="10%" align="center"><input type="checkbox" name="PER_ROL_<%= prm.getAbsRow(i).getID_Rol() %>"<% if(prm.getAbsRow(i).getEnrolado()) { out.print(" checked"); } %>></td>
				  </tr>		
<%
	}
%>		
		     </table>			
			 <table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
                <tr>
					<td colspan="3" bgcolor="#FF6600" class="titChico"><%= JUtil.Msj("SAF","ADMIN_USUARIOS","DLG","COLUMNAS",2) %></td>
				</tr>
                <tr>
					<td width="10%" class="titChicoNar"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
				    <td width="80%" class="titChicoNar"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
					<td width="10%" align="center">&nbsp;</td>
				</tr>
<%
	JUsuariosSubmoduloRolesEnrl sete = new JUsuariosSubmoduloRolesEnrl(request);
	sete.ConCat(true);
	sete.m_Where = "ID_Usuario = '" + JUtil.p(request.getParameter("id")) + "' and ID_Rol not like 'saf-%'";
	sete.Open();

	for(int i=0; i < sete.getNumRows(); i++)
	{
%>
				  <tr>
					<td width="10%"><%= sete.getAbsRow(i).getID_Rol() %></td>
					<td width="80%"><%= sete.getAbsRow(i).getNombre() %></td>
					<td width="10%" align="center"><input type="checkbox" name="PER_ROL_<%= sete.getAbsRow(i).getID_Rol() %>"<% if(sete.getAbsRow(i).getEnrolado()) { out.print(" checked"); } %>></td>
				  </tr>		
<%
	}
%>		
		     </table>		
			</td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
         </table>
      </td>
  </tr>
</table>
</form>
</body>
</html>
