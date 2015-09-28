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
<%@ page import="forseti.*, forseti.sets.*, forseti.ventas.*, java.util.*, java.io.*"%>
<%
	String ven_client_dlg = (String)request.getAttribute("ven_client_dlg");
	if(ven_client_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("VEN_CLIENT").generarTitulo(JUtil.Msj("CEF","VEN_CLIENT","VISTA",request.getParameter("proceso"),3));

	JClientClientSetV2 smod = new JClientClientSetV2(request);
	smod.m_Where = "ID_Tipo = 'CL' and Clave = '" + JUtil.p(request.getParameter("id")) + "'";
	smod.Open();
	JClientClientMasSetV2 smas = new JClientClientMasSetV2(request);
	smas.m_Where = "ID_Tipo = 'CL' and ID_Clave = '" + JUtil.p(request.getParameter("id")) + "'";
	smas.Open();
	
	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
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
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
// Funciones de forseti
<% 
	if(request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))
	{
%>
function configurarPago()
{
	//alert('Esta es una alerta');
	var cantidad = document.ven_cxc_dlg.cantidad.value;
	var tc = document.ven_cxc_dlg.tc.value;
	var total = redondear(cantidad * tc, 2);
	
	var refer = "../../forsetiweb/pagos_dlg.jsp?formul=ven_cxc_dlg&va_tipo=ventas&va_proc=deposito&va_total=" + total + "&va_ident=<%= JUtil.getSesion(request).getSesion("VEN_CLIENT").getEspecial() %>";
	
	abrirCatalogo(refer,150,350);
}
<%
	}
%>

monedas = new Array(<% 		
	for(int i = 0; i< setMon.getNumRows(); i++)
	{
		out.print(setMon.getAbsRow(i).getTC() + ",");
	}
	%>1.0000);
	
function establecerTC(selMon, tc)
{
	tc.value = monedas[selMon.selectedIndex];
}

function enviarlo(formAct)
{
	if(	!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","TC") %>', formAct.tc.value, 0, 9999999999, 4) ||
<% 
if(!request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))	
{ 
%> 	
		!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>', formAct.clave.value, 0, 255) || 
<% 
} 
%>
		!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %>', formAct.cantidad.value, 0, 9999999999, 2))
		return false;
	else 
<% 
if(request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))	
{ 
%>
	{
		if(formAct.fsipg_bancaj.value == null || formAct.fsipg_bancaj.value == '')
		{
			configurarPago();
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
				return false;
		}
	}
<%			
} 
else // no es anticipo 
{ 
%>
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
<%
} 
%>
}	
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFVenClientDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_cxc_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_CLIENTE")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<%= ( request.getParameter("proceso").equals("AGREGAR_ANTICIPO") || request.getParameter("proceso").equals("AGREGAR_CUENTA") ? "CEFVenClientCtrl" : "CEFVenCXCCtrl" ) %>';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td width="10%" height="21">		
				<input name="subproceso" type="hidden" value="ENVIAR">
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>"> 
				<input name="entidad" type="hidden" value="<%= JUtil.getSesion(request).getSesion("VEN_CLIENT").getEspecial() %>">
				
				<input name="fsipg_bancaj" type="hidden">
				<input name="fsipg_ref" type="hidden"> 
				<input name="fsipg_metpagopol" type="hidden">
				<input name="fsipg_depchq" type="hidden">
				<input name="fsipg_cuentabanco" type="hidden">
				<input name="fsipg_id_satbanco" type="hidden">
				<input name="fsipg_bancoext" type="hidden">
				<input name="fsipg_beneficiario" type="hidden" value="<%= smod.getAbsRow(0).getNombre() %>">
				<input name="fsipg_rfc" type="hidden" value="<%= smas.getAbsRow(0).getRFC() %>">
<%
	if(request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))
	{
%>
				<input name="clave" type="hidden" value="2">
<%
	}
%>
              <%= JUtil.Msj("GLB","GLB","GLB","CLIENTE") %></td>
            <td class="titChicoAzc"><%= smod.getAbsRow(0).getNombre() %></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
            <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
<%
	if(!request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))
	{
%>
          <tr>
            <td><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
            <td><input name="clave" type="text" id="clave" size="7" maxlength="10"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_cxc_dlg&lista=clave&idcatalogo=10&nombre=CONCEPTOS&destino=clave_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="clave_nombre" type="text" id="clave_nombre" size="40" maxlength="250" readonly="true"></td>
          </tr>
<%
	}
%>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
            <td><input name="concepto" type="text" id="concepto" size="60" maxlength="80"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","MONEDA") %></td>
            <td><select name="idmoneda" class="cpoBco" onChange="javascript:establecerTC(this.form.idmoneda, this.form.tc)">
<% 				
		for(int i = 0; i< setMon.getNumRows(); i++)
		{	
%>
                <option value="<%=setMon.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("idmoneda") != null && 
										request.getParameter("idmoneda").equals(Integer.toString(setMon.getAbsRow(i).getClave())))  {
											out.print(" selected");
									} %>><%= setMon.getAbsRow(i).getMoneda() %></option>
<%	
		}
%>
              </select> </td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","TC") %></td>
            <td><input name="tc" type="text" id="tc" size="10" maxlength="15"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %></td>
            <td><input name="cantidad" type="text" id="cantidad" size="15" maxlength="15"></td>
          </tr>
        </table>

	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
<%
	if(!request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))
	{
%>
document.ven_cxc_dlg.clave.value = '<% if(request.getParameter("clave") != null) { out.print( request.getParameter("clave") ); } else { out.print(""); } %>'
document.ven_cxc_dlg.clave_nombre.value = '<% if(request.getParameter("clave_nombre") != null) { out.print( request.getParameter("clave_nombre") ); } else { out.print(""); } %>' 
<%
	}
%>
document.ven_cxc_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy")); } %>'
document.ven_cxc_dlg.concepto.value = '<% if(request.getParameter("concepto") != null) { out.print( request.getParameter("concepto") ); } else { out.print(""); } %>'
document.ven_cxc_dlg.tc.value = '<% if(request.getParameter("tc") != null) { out.print( request.getParameter("tc") ); } else { out.print("1"); } %>'
document.ven_cxc_dlg.cantidad.value = '<% if(request.getParameter("cantidad") != null) { out.print( request.getParameter("cantidad") ); } else { out.print(""); } %>'
</script>
</body>
</html>
