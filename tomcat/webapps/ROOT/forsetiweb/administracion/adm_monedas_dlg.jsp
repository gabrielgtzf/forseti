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
	String adm_monedas_dlg = (String)request.getAttribute("adm_monedas_dlg");
	if(adm_monedas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("ADM_MONEDAS").generarTitulo(JUtil.Msj("CEF","ADM_MONEDAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	
	JContaMonedasSetV2 set = new JContaMonedasSetV2(request);
	if( request.getParameter("proceso").equals("CAMBIAR_MONEDA") )
	{
		set.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	
	JSatMonedasSet setMon = new JSatMonedasSet(request);
    setMon.m_OrderBy = "Nombre ASC";
    setMon.Open();
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
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","TC") %>", formAct.idmoneda.value, 1, 255) ||
		!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","TC") %>", formAct.tc.value, 0.0001, 9999999999, 4))
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmMonedasDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_monedas_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmMonedasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td align="right"> 
              <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
			  <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
              <input name="subproceso" type="hidden" value="ENVIAR">
				<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
            <td>
			<input class="cpoColAzc" name="idmoneda" type="text" size="7" maxlength="5"<% if(request.getParameter("proceso").equals("CAMBIAR_MONEDA")) { out.print(" readonly=\"true\""); } %>>
			    </td>
          </tr>
          <tr> 
            <td align="right"> 
            	<%= JUtil.Msj("GLB","GLB","GLB","MONEDA") %></td>
            <td><input name="moneda" type="text" id="moneda" size="20" maxlength="20"></td>
          </tr>
          <tr> 
            <td align="right"> 
              <%= JUtil.Msj("GLB","GLB","GLB","SIMBOLO") %>
            </td>
            <td><input name="simbolo" type="text" id="simbolo" size="6" maxlength="4"></td>
          </tr>
		  <tr> 
            <td align="right"> <%= JUtil.Msj("GLB","GLB","GLB","TC") %></td>
            <td><input name="tc" type="text" id="tc" size="10" maxlength="20"></td>
          </tr>
		  <tr> 
            <td align="right"> SAT: </td>
            <td>			
			 <select name="id_satmon" class="cpoBco">
<%
		for(int i = 0; i < setMon.getNumRows(); i++)
		{	
%>
					<option value="<%=setMon.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("id_satmon") != null) {
										if(request.getParameter("id_satmon").equals(setMon.getAbsRow(i).getClave())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_MONEDA")) { 
											if(set.getAbsRow(0).getID_SatMoneda().equals(setMon.getAbsRow(i).getClave())) {
												out.print(" selected"); 
											}
										}
									 }
									 %>><%= setMon.getAbsRow(i).getNombre() %></option>
<%	
		}
%>				
			</select></td>
	     </tr>
		</table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_monedas_dlg.idmoneda.value = '<% if(request.getParameter("idmoneda") != null) { out.print( request.getParameter("idmoneda") ); } else if(!request.getParameter("proceso").equals("AGREGAR_MONEDA")) { out.print( set.getAbsRow(0).getClave() ); } else { out.print(""); } %>'
document.adm_monedas_dlg.moneda.value = '<% if(request.getParameter("moneda") != null) { out.print( request.getParameter("moneda") ); } else if(!request.getParameter("proceso").equals("AGREGAR_MONEDA")) { out.print( set.getAbsRow(0).getMoneda() ); } else { out.print(""); } %>'
document.adm_monedas_dlg.simbolo.value = '<% if(request.getParameter("simbolo") != null) { out.print( request.getParameter("simbolo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_MONEDA")) { out.print( set.getAbsRow(0).getSimbolo() ); } else { out.print(""); } %>'
document.adm_monedas_dlg.tc.value = '<% if(request.getParameter("tc") != null) { out.print( request.getParameter("tc") ); }	else if(!request.getParameter("proceso").equals("AGREGAR_MONEDA")) { out.print( set.getAbsRow(0).getTC() ); } else { out.print(""); } %>'
</script>
</body>
</html>
