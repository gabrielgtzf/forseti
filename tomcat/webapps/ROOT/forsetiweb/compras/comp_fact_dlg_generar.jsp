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
<%@ page import="forseti.*, forseti.sets.*, forseti.compras.*, forseti.ventas.*, java.util.*, java.io.*"%>
<%
	String comp_fact_dlg = (String)request.getAttribute("comp_fact_dlg");
	if(comp_fact_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String idmod = (String)request.getAttribute("idmod");
	String moddes = (String)request.getAttribute("moddes");
	
	String titulo =  JUtil.getSesion(request).getSesion(idmod).generarTitulo(JUtil.Msj("CEF",idmod,"VISTA",request.getParameter("proceso"),3));

	session = request.getSession(true);
    JCompFactSes rec = (JCompFactSes)session.getAttribute("comp_fact_dlg");
	String entcomp = JUtil.getSesion(request).getSesion(idmod).getEspecial();
	
	// Ahora revisa los bancos y cajas por si es de contado
	JPublicBancosCuentasVsComprasSetV2 bv = new JPublicBancosCuentasVsComprasSetV2(request);
	JPublicBancosCuentasVsComprasSetV2 cv = new JPublicBancosCuentasVsComprasSetV2(request);
	bv.m_OrderBy = "Clave ASC";
	bv.m_Where = "Tipo = '0' and ID_EntidadCompra = '" + JUtil.p(entcomp) + "'";
	bv.Open();
	cv.m_OrderBy = "Clave ASC";
	cv.m_Where = "Tipo = '1' and ID_EntidadCompra = '" + JUtil.p(entcomp) + "'";
	cv.Open();
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
	if(rec.getForma_Pago().equals("contado") && request.getParameter("proceso").equals("FACTURAR_COMPRA"))
	{
%>
function configurarPago()
{
	var cantidad = <%= rec.getTotal() %>;
	var idmon = <%= rec.getID_Moneda() %>
	var tc = <%= rec.getTC() %>;
	var total = redondear(cantidad * tc, 2);
	
	var refer = "../../forsetiweb/pagos_mult_dlg.jsp?formul=comp_fact_dlg_generar&va_tipo=compras&va_proc=retiro&va_total=" + total + "&va_ident=<%= entcomp %>&va_cantidad=" + cantidad + "&va_idmon=" + idmon;
	
	abrirCatalogo(refer,150,350);
}
<%
	}
%>

function enviarlo(formAct)
{
<% 
if(rec.getForma_Pago().equals("contado") && request.getParameter("proceso").equals("FACTURAR_COMPRA"))	
{ 
%>
	if(formAct.fsipg_cambio.value == null || formAct.fsipg_cambio.value == '')
	{
		configurarPago();
	 	return false;
	}
	else
	{
		return true;
	}
<%	
} 
else 
{ 
%>
	if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
	{
		formAct.aceptar.disabled = true;
		return true;
	}
	else
		return false;
<%
} 
%>
}	
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCompFactDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_fact_dlg_generar" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<% 
					if(request.getParameter("tipomov").equals("FACTURAS")) 
						out.print("CEFCompFactCtrl");
					else if(request.getParameter("tipomov").equals("ORDENES")) 
						out.print("CEFCompOrdenesCtrl");
					else if(request.getParameter("tipomov").equals("RECEPCIONES")) 
						out.print("CEFCompRecepcionesCtrl");
					else if(request.getParameter("tipomov").equals("GASTOS")) 
						out.print("CEFCompGastosCtrl");
					else // DEVOLUCIONES
						out.print("CEFCompDevolucionesCtrl"); %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td width="10%" height="21"> <input name="subproceso" type="hidden" value="ENVIAR"> 
              <input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>"> 
              <input name="ID" type="hidden" value="<%= request.getParameter("ID") %>"> 
              <input name="tipomov" type="hidden" value="<%= request.getParameter("tipomov") %>"> 
              <input name="forma_pago" type="hidden" value="<%= rec.getForma_Pago() %>"> 
			  <input name='fsipg_cambio' type='hidden'>
		      <input name='fsipg_efectivo' type='hidden'>
			  <input name='fsipg_beneficiario' type='hidden' value="<%= rec.getNombre() %>">
			  <input name='fsipg_rfc' type='hidden' value="<%= rec.getRFC() %>">
<%		
		if(cv.getNumRows() > 0)
		{
			for(int i = 0; i< cv.getNumRows(); i++)
			{ 
%>
							<input name="FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>" type="hidden"> 
							<input name="FSI_CAJ_REF_<%= cv.getAbsRow(i).getClave() %>" type="hidden"> 
							<input name="FSI_CAJ_EFECTIVO_<%= cv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_CAJ_METPAGOPOL_<%= cv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_CAJ_DEPCHQ_<%= cv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_CAJ_CUENTABANCO_<%= cv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_CAJ_ID_SATBANCO_<%= cv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_CAJ_BANCOEXT_<%= cv.getAbsRow(i).getClave() %>" type="hidden"> 
<%
			}
		}	
		if(bv.getNumRows() > 0)
		{
           	 for(int i = 0; i< bv.getNumRows(); i++)
			{ 
%>
							<input name="FSI_BAN_<%= bv.getAbsRow(i).getClave() %>" type="hidden"> 
							<input name="FSI_BAN_REF_<%= bv.getAbsRow(i).getClave() %>" type="hidden"> 
							<input name="FSI_BAN_METPAGOPOL_<%= bv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_BAN_DEPCHQ_<%= bv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_BAN_CUENTABANCO_<%= bv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_BAN_ID_SATBANCO_<%= bv.getAbsRow(i).getClave() %>" type="hidden">
							<input name="FSI_BAN_BANCOEXT_<%= bv.getAbsRow(i).getClave() %>" type="hidden"> 
<%
			}
		}

	if(request.getParameter("proceso").equals("ANTICIPO"))
	{
%>
              <input name="clave" type="hidden" value="2"> 
<%
	}
	if(request.getParameter("proceso").equals("FACTURAR_COMPRA")) 
		out.print(JUtil.Msj("GLB","GLB","GLB","COMPRA"));
	else if(request.getParameter("proceso").equals("RECIBIR_COMPRA")) 
		out.print(JUtil.Msj("GLB","GLB","GLB","RECEPCION"));
%>
              </td>
            <td class="titChicoAzc"><%= rec.getFactNum() %></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
            <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true" value="<%=   ( (request.getParameter("fecha") == null) ? JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy")  :  request.getParameter("fecha") )  %>"> 
              <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %></td>
            <td><input name="referencia" type="text" id="referencia" size="15" maxlength="20" value="<%= ( (request.getParameter("referencia") == null) ? rec.getReferencia() : request.getParameter("referencia") ) %>"></td>
          </tr>
          <tr> 
            <td width="10%">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
        </table>

	</td>
  </tr>
  <tr>
    <td>&nbsp;
		  
	</td>
  </tr>
</table>
</form>
</body>
</html>
