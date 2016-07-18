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
	String adm_variables_dlg = (String)request.getAttribute("adm_variables_dlg");
	if(adm_variables_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo = JUtil.getSesion(request).getSesion("ADM_VARIABLES").generarTitulo(JUtil.Msj("CEF","ADM_VARIABLES","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JAdmVariablesSet varSet = new JAdmVariablesSet(request);
	varSet.m_Where = "ID_Variable = '" + JUtil.p(request.getParameter("id")) + "'";
	varSet.Open();
	String tipo = JUtil.Elm(varSet.getAbsRow(0).getTipo(),1);
	String nombre_cuenta = "";
	JPublicContCatalogSetV2 num = new JPublicContCatalogSetV2(request);
  	if(tipo.equals("CC") && !varSet.getAbsRow(0).getVAlfanumerico().equals(""))
	{
		num.m_Where = "Numero = '" + JUtil.p(varSet.getAbsRow(0).getVAlfanumerico())  + "'";
    	num.Open();
		nombre_cuenta = num.getAbsRow(0).getNombre();
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
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
<%
	if(tipo.equals("CC"))
	{
%>
	if(!verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>", formAct.valor.value) )
	{
		return false;
	}
<%
	}
%>
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmVariablesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_variables_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmVariablesCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td class="titChicoAzc"><%= varSet.getAbsRow(0).getID_Variable() %></td>
          </tr>
          <tr> 
            <td valign="top"> 
              <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></div></td>
            <td class="titChicoAzc"><%= varSet.getAbsRow(0).getDescripcion() %></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","VALOR") %></div></td>
			<td>
<%
	if(tipo.equals("BOOL"))
	{
%>
			  <input type="radio" name="valor" value="0"<% if(request.getParameter("valor") != null && request.getParameter("valor").equals("0")) { out.print(" checked"); } else if(varSet.getAbsRow(0).getVEntero() == 0) { out.print(" checked"); } else { out.print(" checked"); } %>> 
                    <%= JUtil.Msj("GLB","GLB","GLB","NO") %> 
              <input type="radio" name="valor" value="1"<% if(request.getParameter("valor") != null && request.getParameter("valor").equals("1")) { out.print(" checked"); } else if(varSet.getAbsRow(0).getVEntero() == 1) { out.print(" checked"); } else { out.print(""); } %>>
                    <%= JUtil.Msj("GLB","GLB","GLB","SI") %>
<%
	}
	else if(tipo.equals("INT") || tipo.equals("DECIMAL"))
	{
%>
			  <input name="valor" type="text" id="valor" size="11" maxlength="10">
<%
	}
	else if(tipo.equals("DATE"))
	{
%>
              <input name="valor" type="text" id="valor" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('valor','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a> 
<%
	}
	else if(tipo.equals("TIME"))
	{
%>
			<input name="valor" type="text" id="valor" size="12" maxlength="15" readonly="true">
			<a href="javascript:NewCal('valor','hhmmss',true)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a>
<%
	}
	else if(tipo.equals("CC"))
	{
%>
			  <input name="valor" type="text" id="valor" class="cpoBco" size="11" maxlength="25">
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=adm_variables_dlg&lista=valor&idcatalogo=3&nombre=CUENTAS+CONTABLES&destino=nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a>
			  <input name="nombre" type="text" id="nombre" class="cpoBco" size="25" readonly="true">
<%
	}
	else
	{
%>
			  <input name="valor" type="text" id="valor" size="80" maxlength="254">
<%
	}    
%>
			</td>  
		  </tr>
		  <tr> 
            <td colspan="2">&nbsp; </td>
          </tr>
        </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
<%
	if(tipo.equals("CC"))
	{
%>
document.adm_variables_dlg.valor.value = '<% if(request.getParameter("valor") != null) { out.print( request.getParameter("valor") ); } else { out.print( JUtil.obtCuentaFormato(varSet.getAbsRow(0).getVAlfanumerico(), request) ); } %>'  
document.adm_variables_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else { out.print( nombre_cuenta ); } %>' 
<%
	} 
	else if(tipo.equals("INT"))
	{
%>
document.adm_variables_dlg.valor.value = '<% if(request.getParameter("valor") != null) { out.print( request.getParameter("valor") ); } else { out.print( varSet.getAbsRow(0).getVEntero() ); } %>'
<%
	} 
	else if(tipo.equals("DECIMAL"))
	{
%>
document.adm_variables_dlg.valor.value = '<% if(request.getParameter("valor") != null) { out.print( request.getParameter("valor") ); } else { out.print( varSet.getAbsRow(0).getVDecimal() ); } %>'
<%
	} 
	else if(tipo.equals("DATE"))
	{
%>
document.adm_variables_dlg.valor.value = '<% if(request.getParameter("valor") != null) { out.print( request.getParameter("valor") ); } else { out.print( JUtil.obtFechaTxt(varSet.getAbsRow(0).getVFecha(), "dd/MMM/yyyy") ); } %>'
<%
	} 
	else if(tipo.equals("TIME"))
	{
%>
document.adm_variables_dlg.valor.value = '<% if(request.getParameter("valor") != null) { out.print( request.getParameter("valor") ); } else { out.print( JUtil.obtHoraTxt(varSet.getAbsRow(0).getVHora(), "hh:mm:ss") ); } %>'
<%
	}
	else if(tipo.equals("STR") || tipo.equals("ALFA"))
	{
%>
document.adm_variables_dlg.valor.value = '<% if(request.getParameter("valor") != null) { out.print( request.getParameter("valor") ); } else { out.print( varSet.getAbsRow(0).getVAlfanumerico() ); } %>'
<%
	}
%>
</script>
</body>
</html>
