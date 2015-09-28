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
	String nom_vacaciones_dlg = (String)request.getAttribute("nom_vacaciones_dlg");
	if(nom_vacaciones_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_VACACIONES").generarTitulo(JUtil.Msj("CEF","NOM_VACACIONES","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JVacacionesSet set = new JVacacionesSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_VACACIONES") )
	{
		set.m_Where = "ID_Vacaciones = '" + JUtil.p(request.getParameter("id")) + "'";
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
	if(formAct.proceso.value == "AGREGAR_VACACIONES" || formAct.proceso.value == "CAMBIAR_VACACIONES")
	{
		if(	!esNumeroDecimal('Desde:', formAct.desde.value, 0, 99.999,3) ||
			!esNumeroDecimal('Hasta:', formAct.hasta.value, 0, 99.999,3) ||
			!esNumeroEntero('Dias:', formAct.dias.value, 0, 254) ||
			!esNumeroDecimal('Prima Vacacional:', formAct.pv.value, 0, 0.99,2) ||
			!esNumeroEntero('Clave:', formAct.id_vacaciones.value, 0, 254)  )
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
	else
		return false;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomVacacionesDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_vacaciones_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomVacacionesCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
                Clave:</div></td>
            <td> <input name="id_vacaciones" type="text" id="id_vacaciones" size="8" maxlength="3"<%= (request.getParameter("proceso").equals("CAMBIAR_VACACIONES")) ? " readonly=\"true\"" : "" %>> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Desde:</div></td>
            <td> <input name="desde" type="text" id="desde" size="11" maxlength="6"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Hasta:</div></td>
            <td> <input name="hasta" type="text" id="hasta" size="11" maxlength="6"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Dias:</div></td>
            <td> <input name="dias" type="text" id="dias" size="11" maxlength="2"> 
            </td>
          </tr>
		  <tr> 
            <td> <div align="right">Prima Vac:</div></td>
            <td> <input name="pv" type="text" id="pv" size="8" maxlength="4">
              % Ej, 0.25 </td>
          </tr>
          
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_vacaciones_dlg.id_vacaciones.value = '<% if(request.getParameter("id_vacaciones") != null) { out.print( request.getParameter("id_vacaciones") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VACACIONES")) { out.print( set.getAbsRow(0).getID_Vacaciones() ); } else { out.print(""); } %>' 
document.nom_vacaciones_dlg.desde.value = '<% if(request.getParameter("desde") != null) { out.print( request.getParameter("desde") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VACACIONES")) { out.print( set.getAbsRow(0).getDesde() ); } else { out.print("0.000"); } %>' 
document.nom_vacaciones_dlg.hasta.value = '<% if(request.getParameter("hasta") != null) { out.print( request.getParameter("hasta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VACACIONES")) { out.print( set.getAbsRow(0).getHasta() ); } else { out.print("0.000"); } %>' 
document.nom_vacaciones_dlg.dias.value = '<% if(request.getParameter("dias") != null) { out.print( request.getParameter("dias") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VACACIONES")) { out.print( set.getAbsRow(0).getDias() ); } else { out.print("0"); } %>' 
document.nom_vacaciones_dlg.pv.value = '<% if(request.getParameter("pv") != null) { out.print( request.getParameter("pv") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VACACIONES")) { out.print( set.getAbsRow(0).getPV() ); } else { out.print("0.000"); } %>' 
</script>
</body>
</html>
