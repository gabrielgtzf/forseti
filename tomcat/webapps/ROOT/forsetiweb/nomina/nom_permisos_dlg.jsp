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
	String nom_permisos_dlg = (String)request.getAttribute("nom_permisos_dlg");
	if(nom_permisos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_PERMISOS").generarTitulo(JUtil.Msj("CEF","NOM_PERMISOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	
	JPermisosSet set = new JPermisosSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_PERMISO") )
	{
		set.m_Where = "ID_Empleado = '" + JUtil.p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|")) + "' and ID_Movimiento = '" +
			JUtil.p(JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|")) + "' and ID_FechaMovimiento = '" +
			 JUtil.p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "'";
		//System.out.println(set.m_Where);
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
	if(formAct.proceso.value == "AGREGAR_PERMISO" || formAct.proceso.value == "CAMBIAR_PERMISO")
	{
		if(!esNumeroEntero('Movimiento:', formAct.id_movimiento.value, 0, 999) ||
			!esCadena('Empleado:', formAct.id_empleado.value, 6, 6)  )
			return false;
		else
		{
			if(formAct.subproceso.value == "ENVIAR")
			{
				if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
				{
					formAct.aceptar.disabled = true;
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
	}
	else
		return false;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomPermisosDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_permisos_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_PERMISO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomPermisosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
                Movimiento:</div></td>
            <td colspan="2"> <input name="id_movimiento" type="text" id="id_movimiento" size="7" maxlength="4"<%= (request.getParameter("proceso").equals("CAMBIAR_PERMISO")) ? " readonly=\"true\"" : "" %>> 
              <% if(request.getParameter("proceso").equals("AGREGAR_PERMISO")) { %><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_permisos_dlg&lista=id_movimiento&idcatalogo=27&nombre=MOVIMIENTOS&destino=nombre_movimiento',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a><% } %> 
              <input name="nombre_movimiento" type="text" id="nombre_movimiento" size="40" maxlength="250" readonly="true">
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Empleado:</div></td>
            <td colspan="2"><input name="id_empleado" type="text" id="id_empleado" size="10" maxlength="6"<%= (request.getParameter("proceso").equals("CAMBIAR_PERMISO")) ? " readonly=\"true\"" : "" %>> 
              <% if(request.getParameter("proceso").equals("AGREGAR_PERMISO")) { %><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_permisos_dlg&lista=id_empleado&idcatalogo=28&nombre=EMPLEADOS&destino=nombre_empleado&esp1=NOM_PERMISOS',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a><% } %> 
              <input name="nombre_empleado" type="text" id="nombre_empleado" size="40" maxlength="250" readonly="true"></td>
          </tr>
		  <tr> 
            <td> <div align="right"></div></td>
            <td colspan="2"> En movimientos de dias completos, la hora y minutos 
              ser&aacute;n ignorados.</td>
          </tr>
          <tr> 
            <td> <div align="right">Desde:</div></td>
            <td colspan="2"><input name="desde" type="text" id="desde" size="20" maxlength="20" readonly="true">
              <a href="javascript:NewCal('desde','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a> 
            </td>
          </tr>
		  <tr> 
            <td> <div align="right">Hasta:</div></td>
            <td colspan="2"><input name="hasta" type="text" id="hasta" size="20" maxlength="20" readonly="true"> 
              <a href="javascript:NewCal('hasta','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
		  <tr> 
            <td valign="top"> 
              <div align="right">Observaciones:</div></td>
            <td colspan="2"><textarea name="obs" cols="60" rows="3" id="obs"></textarea></td>
          </tr>
		 </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_permisos_dlg.id_movimiento.value = '<% if(request.getParameter("id_movimiento") != null) { out.print( request.getParameter("id_movimiento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERMISO")) { out.print( set.getAbsRow(0).getID_Movimiento() ); } else { out.print(""); } %>' 
document.nom_permisos_dlg.nombre_movimiento.value = '<% if(request.getParameter("nombre_movimiento") != null) { out.print( request.getParameter("nombre_movimiento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERMISO")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.nom_permisos_dlg.id_empleado.value = '<% if(request.getParameter("id_empleado") != null) { out.print( request.getParameter("id_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERMISO")) { out.print( set.getAbsRow(0).getID_Empleado() ); } else { out.print(""); } %>' 
document.nom_permisos_dlg.nombre_empleado.value = '<% if(request.getParameter("nombre_empleado") != null) { out.print( request.getParameter("nombre_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERMISO")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.nom_permisos_dlg.desde.value = '<% if(request.getParameter("desde") != null) { out.print( request.getParameter("desde") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERMISO")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getDesde(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(set.getAbsRow(0).getHoraDesde(),"HH:mm") ); } else { out.print("") ; } %>'
document.nom_permisos_dlg.hasta.value = '<% if(request.getParameter("hasta") != null) { out.print( request.getParameter("hasta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERMISO")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getHasta(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(set.getAbsRow(0).getHoraHasta(),"HH:mm") ); } else { out.print("") ; } %>'
document.nom_permisos_dlg.obs.value = '<% if(request.getParameter("obs") != null) { out.print( request.getParameter("obs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERMISO")) { out.print( set.getAbsRow(0).getObs() ); } else { out.print(""); } %>'
</script>
</body>
</html>
