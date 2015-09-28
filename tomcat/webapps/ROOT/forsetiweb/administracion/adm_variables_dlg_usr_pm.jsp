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
	if(request.getParameter("proceso").equals("CAMBIAR_VARIABLE"))
	{
		varSet.m_Where = "ID_Variable = '" + JUtil.p(request.getParameter("id")) + "'";
		varSet.Open();
	}
	
	Calendar fecha = GregorianCalendar.getInstance();
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
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker_pm.js">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
 	if(	!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.idvariable.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.descripcion.value, 1, 254) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","ENTERO") %>", formAct.ventero.value, -2147483648, 2147483647) ||
		!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","DECIMAL") %>", formAct.vdecimal.value, -999999999999.999999, 999999999999.999999, 6) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","CADENA") %>", formAct.valfanumerico.value, 0, 254))
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
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmVariablesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_variables_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmVariablesCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
            <td> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>"/>
			    <input name="subproceso" type="hidden" value="ENVIAR"/>
                <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
		  </tr>
		  <tr>
             <td><input name="idvariable" type="text" id="idvariable" style="width:50%" maxlength="10" class="cpoColAzc"<% if(request.getParameter("proceso").equals("CAMBIAR_VARIABLE")) { out.print(" readonly=\"true\""); } %>/></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
          </tr>
		  <tr>
            <td><input name="descripcion" type="text" id="descripcion" style="width:100%" maxlength="254"/></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","ENTERO") %></td>
		  </tr>
		  <tr>
          	<td><input name="ventero" type="text" id="ventero" style="width:50%" maxlength="10"/></td>
		  </tr> 
		  <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","DECIMAL") %></td>
		  </tr>
		  <tr>
          	<td><input name="vdecimal" type="text" id="vdecimal" style="width:50%" maxlength="10"/></td>
		  </tr>     
		  <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
		  </tr>
		  <tr>
          	<td><table width="70%"><tr><td><input name="vfecha" type="text" id="vfecha" style="width:100%" maxlength="15" readonly="true"/> 
              </td><td width="24"><a href="javascript:NewCal('vfecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle" width="24" height="24"/></a></td><tr></table></td>
		  </tr>
		  <tr>
		   <td><%= JUtil.Msj("GLB","GLB","GLB","CADENA") %></td>
		  </tr>
		  <tr>
           <td> <input name="valfanumerico" type="text" id="valfanumerico" style="width:100%" maxlength="254"/></td>
		  </tr>
		  <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_variables_dlg.idvariable.value = '<% if(request.getParameter("idvariable") != null) { out.print( request.getParameter("idvariable") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VARIABLE")) { out.print( varSet.getAbsRow(0).getID_Variable() ); } else { out.print(""); } %>'
document.adm_variables_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VARIABLE")) { out.print( varSet.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>'
document.adm_variables_dlg.ventero.value = '<% if(request.getParameter("ventero") != null) { out.print( request.getParameter("ventero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VARIABLE")) { out.print( varSet.getAbsRow(0).getVEntero() ); } else { out.print("0"); } %>'
document.adm_variables_dlg.vdecimal.value = '<% if(request.getParameter("vdecimal") != null) { out.print( request.getParameter("vdecimal") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VARIABLE")) { out.print( varSet.getAbsRow(0).getVDecimal() ); } else { out.print("0.0"); } %>'
document.adm_variables_dlg.vfecha.value = '<% if(request.getParameter("vfecha") != null) { out.print( request.getParameter("vfecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VARIABLE")) { out.print( JUtil.obtFechaTxt(varSet.getAbsRow(0).getVFecha(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(fecha, "dd/MMM/yyyy")); } %>'
document.adm_variables_dlg.valfanumerico.value = '<% if(request.getParameter("valfanumerico") != null) { out.print( request.getParameter("valfanumerico") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VARIABLE")) { out.print( varSet.getAbsRow(0).getVAlfanumerico() ); } else { out.print(""); } %>'
</script>
</body>
</html>
