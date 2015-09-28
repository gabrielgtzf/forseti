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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*" %>
<%
	String nom_asistencias_dlg = (String)request.getAttribute("nom_asistencias_dlg");
	if(nom_asistencias_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_ASISTENCIAS").generarTitulo(JUtil.Msj("CEF","NOM_ASISTENCIAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");

	JAsistenciasChequeosSet set = new JAsistenciasChequeosSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ASISTENCIA") )
	{
		set.m_Where = "ID_Empleado = '" + JUtil.p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|")) + "' and ID_Fecha = '" +
			 JUtil.p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "'";
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
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
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
		return false;
	
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomAsistenciasDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_asistencias_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomAsistenciasCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td width="20%"> <div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                Tipo de captura:</div></td>
            <td> <input name="ipc" type="radio" value="1"<% if(request.getParameter("ipc") == null || request.getParameter("ipc").equals("1")) { out.print(" checked");} %>>
              Por Empleado 
              <input name="ipc" type="radio" value="0"<% if(request.getParameter("ipc") != null && request.getParameter("ipc").equals("0")) { out.print(" checked");} %>>
              Por N&oacute;mina </td>
          </tr>
          <tr> 
            <td> <div align="right">Empleado:</div></td>
            <td><input name="id_empleado" type="text" id="id_empleado" size="10" maxlength="6"<%= (request.getParameter("proceso").equals("CAMBIAR_ASISTENCIA")) ? " readonly=\"true\"" : "" %>> 
              <% if(request.getParameter("proceso").equals("AGREGAR_ASISTENCIA")) { %><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_asistencias_dlg&lista=id_empleado&idcatalogo=28&nombre=EMPLEADOS&destino=nombre_empleado&esp1=NOM_ASISTENCIAS',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a><% } %> 
              <input name="nombre_empleado" type="text" id="nombre_empleado" size="40" maxlength="250" readonly="true"></td>
          </tr>
		  <tr> 
            <td> <div align="right">Asistencia:</div></td>
            <td><input name="fechahora" type="text" id="fechahora" size="20" maxlength="20" readonly="true">
              <a href="javascript:NewCal('fechahora','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a> 
            </td>
          </tr>
		 </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_asistencias_dlg.id_empleado.value = '<% if(request.getParameter("id_empleado") != null) { out.print( request.getParameter("id_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ASISTENCIA")) { out.print( set.getAbsRow(0).getID_Empleado() ); } else { out.print(""); } %>' 
document.nom_asistencias_dlg.nombre_empleado.value = '<% if(request.getParameter("nombre_empleado") != null) { out.print( request.getParameter("nombre_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ASISTENCIA")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.nom_asistencias_dlg.fechahora.value = '<% if(request.getParameter("fechahora") != null) { out.print( request.getParameter("fechahora") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ASISTENCIA")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getID_Fecha(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(set.getAbsRow(0).getID_Hora(),"HH:mm") ); } else { out.print( JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy HH:mm")) ; } %>'
</script>
</body>
</html>
