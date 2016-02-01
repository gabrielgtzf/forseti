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
<%@ page import="forseti.*, forseti.sets.*, forseti.almacen.*, java.util.*, java.io.*"%>
<%
	String alm_utensilios_dlg = (String)request.getAttribute("alm_utensilios_dlg");
	if(alm_utensilios_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("ALM_UTENSILIOS").generarTitulo(JUtil.Msj("CEF","ALM_UTENSILIOS","VISTA",request.getParameter("proceso"),3));
	String coletq = JUtil.Msj("CEF","ALM_UTENSILIOS","DLG","COLUMNAS",1);
	int etq = 1;
	
	JInvservAlmacenMovimDetallesUtensiliosSetExist set = new JInvservAlmacenMovimDetallesUtensiliosSetExist(request);
	set.m_Where = "ID_Prod = '" + JUtil.p(request.getParameter("ID")) + "'";
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
	if(formAct.subproceso.value == "ENVIAR")
	{
		if(	!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %>:", formAct.cantidad.value, 0, 9999999999, 2) ||
			!esCadena("Clave:", formAct.idprod.value, 1, 20) )
			return false;
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
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAlmUtensiliosDlg" method="post" enctype="application/x-www-form-urlencoded" name="alm_utensilios_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAlmUtensiliosCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
      <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
				<tr>
				<td width="20%">
							<input name="tipomov" type="hidden" value="UTENSILIOS">
				  			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
							<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR">
							<input name="clave" type="hidden" value="-2">
							<input name="tipomoves" type="hidden" value="salida">
							&nbsp;
				  </td>
				  <td colspan="2">&nbsp;</td>
				</tr>
                <tr> 
                  <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
                  <td colspan="2"><input name="concepto" type="text" id="concepto" size="60" maxlength="80"></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr bgcolor="#0099FF"> 
                  <td width="7%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                  <td width="5%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                  <td width="25%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                  <td class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
				</tr>
                <tr valign="top"> 
                  <td width="7%" align="right"> <input name="cantidad" type="text" class="cpoBco" id="cantidad" size="7" maxleng th="12"></td>
                  <td width="5%">&nbsp;</td>
                  <td width="25%"> <input name="ID" type="text" class="cpoBco" id="ID" size="10" maxlength="20" readonly="true"> 
                  <td> <input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre" size="40" maxlength="120" readonly="true"></td>
                </tr>
	        </table>
			</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table> 
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.alm_utensilios_dlg.concepto.value = '<% if(request.getParameter("concepto") != null) { out.print( request.getParameter("concepto") ); } else { out.print(""); } %>'
document.alm_utensilios_dlg.cantidad.value = '<% if(request.getParameter("cantidad") != null) { out.print( request.getParameter("cantidad") ); } else { out.print("1"); } %>'
document.alm_utensilios_dlg.ID.value = '<% if(request.getParameter("ID") != null) { out.print( request.getParameter("ID") ); } else { out.print(set.getAbsRow(0).getID_Prod()); } %>'
document.alm_utensilios_dlg.idprod_nombre.value = '<% if(request.getParameter("idprod_nombre") != null) { out.print( request.getParameter("idprod_nombre") ); } else { out.print(set.getAbsRow(0).getDescripcion()); } %>'
</script>
</body>
</html>
