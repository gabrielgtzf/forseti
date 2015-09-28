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
	String adm_periodos_dlg = (String)request.getAttribute("adm_periodos_dlg");
	if(adm_periodos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo = JUtil.getSesion(request).getSesion("ADM_PERIODOS").generarTitulo(JUtil.Msj("CEF","ADM_PERIODOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	Calendar fecha = GregorianCalendar.getInstance();
	int Ano = JUtil.obtAno(fecha);
    int Mes = JUtil.obtMes(fecha);
	String Meses = JUtil.Msj("GLB","GLB","GLB","MESES-ANO",2); 
			
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
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","FECHA",2)%>", formAct.ano.value, 2000, 2100) )
		return false;
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
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmPeriodosDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_periodos_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmPeriodosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
		   	<td colspan="2" align="center" class="titChicoAzc"><%= JUtil.Msj("CEF","ADM_PERIODOS","DLG","ETQ",2) %></td>
		  </tr> 
		  <tr>
		  	<td width="50%">
					<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
					<input name="subproceso" type="hidden" value="ENVIAR"/>
					<%= JUtil.Msj("GLB","GLB","GLB","FECHA",3)%></td>
			<td><%= JUtil.Msj("GLB","GLB","GLB","FECHA",2)%></td>
          </tr> 
		  <tr>
		  	<td>
					<select style="width:90%" name="mes" size="1" class="cpoBco">
					<option value="1"<% if(Mes == 1) out.print(" selected"); %>><%= JUtil.Elm(Meses,1) %></option>
					<option value="2"<% if(Mes == 2) out.print(" selected"); %>><%= JUtil.Elm(Meses,2) %></option>
					<option value="3"<% if(Mes == 3) out.print(" selected"); %>><%= JUtil.Elm(Meses,3) %></option>
					<option value="4"<% if(Mes == 4) out.print(" selected"); %>><%= JUtil.Elm(Meses,4) %></option>
					<option value="5"<% if(Mes == 5) out.print(" selected"); %>><%= JUtil.Elm(Meses,5) %></option>
					<option value="6"<% if(Mes == 6) out.print(" selected"); %>><%= JUtil.Elm(Meses,6) %></option>
					<option value="7"<% if(Mes == 7) out.print(" selected"); %>><%= JUtil.Elm(Meses,7) %></option>
					<option value="8"<% if(Mes == 8) out.print(" selected"); %>><%= JUtil.Elm(Meses,8) %></option>
					<option value="9"<% if(Mes == 9) out.print(" selected"); %>><%= JUtil.Elm(Meses,9) %></option>
					<option value="10"<% if(Mes == 10) out.print(" selected"); %>><%= JUtil.Elm(Meses,10) %></option>
					<option value="11"<% if(Mes == 11) out.print(" selected"); %>><%= JUtil.Elm(Meses,11) %></option>
					<option value="12"<% if(Mes == 12) out.print(" selected"); %>><%= JUtil.Elm(Meses,12) %></option>
				  </select></td>
    		<td><input name="ano" type="text" id="ano" style="width:50%" maxlength="4" value="<%= Ano %>"/></td>
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
