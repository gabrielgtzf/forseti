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
	String ven_fact_dlg = (String)request.getAttribute("ven_fact_dlg");
	if(ven_fact_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	String idmod = (String)request.getAttribute("idmod");
	String moddes = (String)request.getAttribute("moddes");
	String titulo =  JUtil.getSesion(request).getSesion(idmod).generarTitulo(JUtil.Msj("CEF",idmod,"VISTA",request.getParameter("proceso"),3));
	
	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();
	
	session = request.getSession(true);
    JVenFactSes rec;
	if(request.getParameter("proceso").equals("DEVOLVER_VENTA") || request.getParameter("proceso").equals("REBAJAR_VENTA"))
		rec = (JVenDevSes)session.getAttribute("ven_dev_dlg");
	else
		rec = (JVenFactSes)session.getAttribute("ven_fact_dlg");
	
	// Ahora revisa los bancos y cajas por si es de contado
	JPublicBancosCuentasVsVentasSetV2 bv = new JPublicBancosCuentasVsVentasSetV2(request);
	JPublicBancosCuentasVsVentasSetV2 cv = new JPublicBancosCuentasVsVentasSetV2(request);
	bv.m_OrderBy = "Clave ASC";
	cv.m_OrderBy = "Clave ASC";
	bv.m_Where = "Tipo = '0' and ID_EntidadVenta = '" + JUtil.getSesion(request).getSesion(idmod).getEspecial() + "'";
	cv.m_Where = "Tipo = '1' and ID_EntidadVenta = '" + JUtil.getSesion(request).getSesion(idmod).getEspecial() + "'";
	bv.Open();
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
<%
	if(moddes.equals("FACTURAS"))
	{
%>	
function configurarPago()
{
	var cantidad = <%= rec.getTotal() %>;
	var idmon = <%= rec.getID_Moneda() %>
	var tc = document.ven_fact_dlg.tc.value;
	var total = redondear(cantidad * tc, 2);
	
	var refer = "../../forsetiweb/pagos_mult_dlg.jsp?formul=ven_fact_dlg&va_tipo=ventas&va_proc=<% if(request.getParameter("proceso").equals("AGREGAR_VENTA") || request.getParameter("proceso").equals("ENLAZAR_VENTA")) { out.print("deposito"); } else { out.print("retiro"); } %>&va_total=" + total + "&va_ident=<%= JUtil.getSesion(request).getSesion("VEN_FAC").getEspecial() %>&va_cantidad=" + cantidad + "&va_idmon=" + idmon;
	
	abrirCatalogo(refer,150,350);
}

function configurarSaldo()
{
	var cantidad = <%= rec.getTotal() %>;
	var refer = "../../forsetiweb/saldos_dlg.jsp?formul=ven_fact_dlg&va_tipo=ventas&va_proc=retiro&va_total=" + cantidad;
	abrirCatalogo(refer,150,400);
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

function limpiarFormulario()
{
	document.ven_fact_dlg.cantidad.value = "";
	document.ven_fact_dlg.idprod.value = "";
	document.ven_fact_dlg.idprod_nombre.value = "";
	document.ven_fact_dlg.precio.value = "";
	document.ven_fact_dlg.descuento.value = "";
	document.ven_fact_dlg.iva.value = "";
	document.ven_fact_dlg.obs_partida.value = "";
}

function editarPartida(idpartida, cantidad, idprod, idprod_nombre, precio, descuento, iva, obs_partida)
{
	document.ven_fact_dlg.idpartida.value = idpartida;
	document.ven_fact_dlg.subproceso.value = "EDIT_PART";

	document.ven_fact_dlg.cantidad.value = cantidad;
	document.ven_fact_dlg.idprod.value = idprod;
	document.ven_fact_dlg.idprod_nombre.value = idprod_nombre;
	document.ven_fact_dlg.precio.value = precio;
	document.ven_fact_dlg.descuento.value = descuento;
	document.ven_fact_dlg.iva.value = iva;
	document.ven_fact_dlg.obs_partida.value = obs_partida;
}

function enviarlo(formAct)
{
	if(formAct.proceso.value == "AGREGAR_VENTA"  ||  formAct.proceso.value == "CAMBIAR_VENTA" || formAct.proceso.value == "DEVOLVER_VENTA" || formAct.proceso.value == "REBAJAR_VENTA" || formAct.proceso.value == "ENLAZAR_VENTA")
	{
		if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
		{
			if(	!esNumeroDecimal("Cantidad:", formAct.cantidad.value, 0, 9999999999, 2) ||
				!esCadena("Clave:", formAct.idprod.value, 1, 20) )
				return false;
			if(formAct.precio.value != "")
			{
				if(	!esNumeroDecimal("Precio:", formAct.precio.value, 0, 9999999999, 6) )
					return false;
			}
			if(formAct.descuento.value != "")
			{
				if(	!esNumeroDecimal("Descuento:", formAct.descuento.value, 0, 100, 6) )
					return false;
			}
			if(formAct.iva.value != "")
			{
				if(	!esNumeroDecimal("IVA:", formAct.iva.value, 0, 100, 6) )
					return false;
			}
			
		}
		
		if(	!esNumeroEntero('<% if(moddes.equals("DEVOLUCIONES") || (moddes.equals("FACTURAS") && (request.getParameter("proceso").equals("DEVOLVER_VENTA") || request.getParameter("proceso").equals("REBAJAR_VENTA")))) { out.print("Nota de Credito:"); } else if(moddes.equals("FACTURAS")) { out.print("Factura:"); } else if(moddes.equals("PEDIDOS")) { out.print("Pedido:"); }%>', formAct.factura.value, 1, 9999999999) ||
<%
	if(request.getParameter("proceso").equals("AGREGAR_VENTA")  ||  request.getParameter("proceso").equals("CAMBIAR_VENTA"))
	{ 
%>
			!esNumeroEntero('Cliente:', formAct.numero.value, 0, 99999) ||
			!esNumeroEntero('Vendedor:', formAct.idvendedor.value, 0, 30000) ||
<%
	}
%>
			!esNumeroDecimal('Tipo de Cambio:', formAct.tc.value, 0, 9999999999, 4))
			return false;

		// revisa el pago
		if(formAct.subproceso.value == "ENVIAR")
		{
			if(formAct.tipomov.value == "FACTURAS")
			{
				if(formAct.proceso.value == "DEVOLVER_VENTA" || formAct.proceso.value == "REBAJAR_VENTA")
				{
<%
	if(rec.getForma_Pago().equals("contado"))
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
	else if(rec.getForma_Pago().equals("credito"))
	{
%>
					if(formAct.fsipg_idscon.value == null || formAct.fsipg_idscon.value == '')
					{
						configurarSaldo();
						return false;
					}
					else
					{
						return true;
					}	
<%
	}
%>					
				}
<%
	if(!rec.getForma_Pago().equals("ninguno"))
	{
%>			
				else if(formAct.forma_pago[0].checked)
				{
					if(formAct.fsipg_cambio.value == null || formAct.fsipg_cambio.value == '')
					{
						configurarPago();
		 				return false;
					}
					else
					{
						return true;
					}
				}
<%
	}
%>
			}
			
			if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
			{
				formAct.aceptar.disabled = true;
				return true;
			}
			else
				return false;
			
		}
		else
		{
			return true;
		}
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFVenFactDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_fact_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_VENTA")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<% 
					if(request.getParameter("tipomov").equals("FACTURAS")) 
						out.print("CEFVenFactCtrl");
					else if(request.getParameter("tipomov").equals("PEDIDOS")) 
						out.print("CEFVenPedidosCtrl");
					else if(request.getParameter("tipomov").equals("REMISIONES")) 
						out.print("CEFVenRemisionesCtrl");
					else if(request.getParameter("tipomov").equals("COTIZACIONES")) 
						out.print("CEFVenCotizacionesCtrl");
					else // DEVOLUCIONES
						out.print("CEFVenDevolucionesCtrl"); %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr>
                  <td width="30%"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr>
                        <td width="40%">
 						    <input name="tipomov" type="hidden" value="<%= request.getParameter("tipomov") %>">
<% if(!request.getParameter("proceso").equals("ENLAZAR_VENTA")) { %><input name="ID" type="hidden" value="<%= request.getParameter("ID") %>"><% } %>
							<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
							<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR">
							<input name="clave" type="hidden" id="clave" value="<%= rec.getClave() %>">
							<input name="numeroses" type="hidden" id="numeroses" value="<%= rec.getNumero() %>">
							<input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
							<input name="fsipg_idscon" type="hidden">
							<input name="fsipg_concepto" type="hidden">
							<input name="fsipg_cambio" type="hidden">
							<input name="fsipg_efectivo" type="hidden">
							<input name="fsipg_beneficiario" type="hidden" value="<%= rec.getNombre() %>">
							<input name="fsipg_rfc" type="hidden" value="<%= rec.getRFC() %>">
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
%>
                          N&ordm; <% if(moddes.equals("DEVOLUCIONES") || (moddes.equals("FACTURAS") && (request.getParameter("proceso").equals("DEVOLVER_VENTA") || request.getParameter("proceso").equals("REBAJAR_VENTA")))) { out.print("Nota de Credito:"); } else if(moddes.equals("FACTURAS")) { out.print("Factura:"); } else if(moddes.equals("PEDIDOS")) { out.print("Pedido:"); } else if(moddes.equals("REMISIONES")) { out.print("Remisi&oacute;n:"); } else if(moddes.equals("COTIZACIONES")) { out.print("Cotizaci&oacute;n:"); }%></td>
                        <td><input name="factura" type="text" size="10" maxlength="15"></td>
                      </tr>
                      <tr>
                        <td width="40%">Referencia:</td>
                        <td><input name="referencia" type="text" id="referencia" size="15" maxlength="20"></td>
                      </tr>
                      <tr>
                        <td width="40%" valign="top">Pago:</td>
<% 
		if( request.getParameter("proceso").equals("AGREGAR_VENTA")  ||  request.getParameter("proceso").equals("CAMBIAR_VENTA") ||  request.getParameter("proceso").equals("ENLAZAR_VENTA") ) 
		{ 
			if(rec.getForma_Pago().equals("ninguno"))
			{
%>
					  <td class="titChicoAzc">
					  	<input type="hidden" name="forma_pago" value="ninguno">Ninguno
<%
			}
			else
			{
%>
						 <td>
							<input name="forma_pago" type="radio" value="contado"<% if(rec.getForma_Pago().equals("contado"))  { out.print(" checked");}%>>
                				Contado<br>
                			<input name="forma_pago" type="radio" value="credito"<%  if(rec.getForma_Pago().equals("credito"))  { out.print(" checked");}%>>
                				Cr&eacute;dito
<%
			} 
		} 
		else
		{
%>
						  <td class="titChicoAzc">
<%
				if(rec.getForma_Pago().equals("contado")) { out.print("Contado"); } else if(rec.getForma_Pago().equals("credito")) { out.print("Cr&eacute;dito"); } else { out.print("Ninguno"); }
		}
%>
                          </td>
                      </tr>
                      <tr>
                        <td width="40%">Moneda:</td>
                        <td class="txtChicoAzc"> 
<%
	if( request.getParameter("proceso").equals("AGREGAR_VENTA")   ||   request.getParameter("proceso").equals("CAMBIAR_VENTA")  ||  request.getParameter("proceso").equals("ENLAZAR_VENTA"))
	{	
%>	
						 <select name="idmoneda" class="cpoBco" onChange="javascript:establecerTC(this.form.idmoneda, this.form.tc)">
<% 				for(int i = 0; i< setMon.getNumRows(); i++)
				{	
%>
                          <option value="<%=setMon.getAbsRow(i).getClave()%>"<% 
									if(rec.getID_Moneda() == setMon.getAbsRow(i).getClave())	{
											out.print(" selected");
									} %>><%= setMon.getAbsRow(i).getMoneda() %></option>
<%	
				}
%>
                        </select>
<%
	                       
	}
	else
	{
						out.print(rec.getMoneda() );
	}
%>	
						</td>
                      </tr>
                      <tr>
                        <td width="40%">TC:</td>
                        <td><input name="tc" type="text" id="tc" size="10" maxlength="15"></td>
                      </tr>
                      <tr>
                        <td width="40%">Bodega:</td>
                        <td class="txtChicoAzc"><%= rec.getID_Bodega() %></td>
                      </tr>
					  <tr>
                        <td width="40%">Nombre:</td>
                        <td class="txtChicoAzc"><%= rec.getBodegaDesc() %></td>
                      </tr>
					  <tr>
                        <td width="40%">Vendedor:</td>
                        <td class="txtChicoAzc">
 <% 
	if( request.getParameter("proceso").equals("AGREGAR_VENTA") ||  request.getParameter("proceso").equals("CAMBIAR_VENTA")  ||  request.getParameter("proceso").equals("ENLAZAR_VENTA")) 
	{ 
%>
                          <input name="idvendedor" type="text" id="idvendedor"  size="7" maxlength="5"> 
                          <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_fact_dlg&lista=idvendedor&idcatalogo=23&nombre=VENDEDORES&destino=vendedor_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
<%
 	} 
	else
	{
			out.print(rec.getID_Vendedor());
	}
%>
                        </td>
                      </tr>
					  <tr>
                        <td width="40%">Nombre:</td>
                        <td class="txtChicoAzc">
 <% 
	if( request.getParameter("proceso").equals("AGREGAR_VENTA") ||  request.getParameter("proceso").equals("CAMBIAR_VENTA")  ||  request.getParameter("proceso").equals("ENLAZAR_VENTA")) 
	{ 
%>
                          	<input name="vendedor_nombre" type="text" size="20" maxlength="255" readonly="true">
                          <%
 	} 
	else
	{
			out.print(rec.getVendedorNombre());
	}
%>
                        </td>
                      </tr>
                    </table></td>
                  <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr> 
                        <td width="15%">Fecha:</td>
                        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width="30%">
								<input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
                                <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
                              <td width="33%">Fecha de entraga:</td>
                              <td><input name="entrega" type="text" id="entrega" size="12" maxlength="15" readonly="true"> 
                                <a href="javascript:NewCal('entrega','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
                            </tr>
                          </table></td>
                      </tr>
                      <tr> 
                        <td width="15%">Cliente:</td>
                        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width="35%" class="titChicoAzc"> 
<% 
		if( request.getParameter("proceso").equals("AGREGAR_VENTA") ||  request.getParameter("proceso").equals("CAMBIAR_VENTA")) 
		{ 
%>
                                <input name="numero" type="text" id="numero" onBlur="javascript: if(this.form.numero.value != this.form.numeroses.value) { establecerProcesoSVE(this.form.subproceso, 'AGR_CLIENT'); this.form.submit(); }" size="7" maxlength="10"> 
 	    			<a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_fact_dlg&lista=numero&idcatalogo=14&nombre=CLIENTES&destino=numero_nombre&esp1=<%= idmod %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a>
<% 
		} 
		else
		{
				out.print(rec.getNumero());
		}
%>								</td>
                              <td width="20%">R.F.C.:</td>
                              <td class="txtChicoAzc"><%= rec.getRFC() %></td>
                            </tr>
                          </table></td>
                      </tr>
                      <tr> 
                        <td width="15%">Nombre:</td>
                        <td class="txtChicoAzc">
<% 
		if( request.getParameter("proceso").equals("AGREGAR_VENTA")  ||  request.getParameter("proceso").equals("CAMBIAR_VENTA") ) 
		{ 
%>
				<input name="numero_nombre" type="text" value="<%= rec.getNombre() %>" size="50" maxlength="255" readonly="true">
<% 
		} 
		else
		{
				out.print(rec.getNombre());
		}
%>
					        </td>
                      </tr>
                      <tr> 
                        <td width="15%">Direcci&oacute;n:</td>
                        <td class="txtChicoAzc"><%= rec.getDireccion() %></td>
                      </tr>
                      <tr> 
                        <td width="15%">Colonia:</td>
                        <td class="txtChicoAzc"><%= rec.getColonia() %></td>
                      </tr>
                      <tr> 
                        <td width="15%">Poblaci&oacute;n:</td>
                        <td class="txtChicoAzc"><%= rec.getPoblacion() %></td>
                      </tr>
                      <tr> 
                        <td width="15%">CP:</td>
                        <td class="txtChicoAzc"><%= rec.getCP() %></td>
                      </tr>
                      <tr> 
                        <td width="15%">Tels:</td>
                        <td class="txtChicoAzc"><%= rec.getTels() %></td>
                      </tr>
                    </table></td>
                </tr>
              </table></td>
          </tr>
          <tr>
            <td>
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr bgcolor="#0099FF"> 
                  <td width="5%" align="right" class="titChico">Cant</td>
                  <td width="4%" class="titChico">Uni</td>
                  <td width="13%" class="titChico">Clave</td>
                  <td class="titChico">Descripci&oacute;n</td>
                  <td width="7%" align="right" class="titChico">Precio</td>
                  <td width="7%" align="right" class="titChico">Importe</td>
                  <td width="5%" align="right" class="titChico">% Desc</td>
                  <td width="5%" align="right" class="titChico">IVA</td>
				  <td width="5%" align="right" class="titChico">IEPS</td>
                  <td width="5%" align="right" class="titChico">R/IVA</td>
                  <td width="5%" align="right" class="titChico">R/ISR</td>
                  <td width="6%">&nbsp;</td>
                </tr>
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_VENTA") && !request.getParameter("proceso").equals("ENLAZAR_VENTA") )
	{
%>				
                <tr valign="top"> 
                  <td width="5%" align="right"> <input name="cantidad" type="text" class="cpoBco" id="cantidad" size="7" maxlength="12"></td>
                  <td width="4%">&nbsp;</td>
                  <td width="13%"> <input name="idprod" type="text" class="cpoBco" id="idprod" size="10" maxlength="20"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_fact_dlg&lista=idprod&idcatalogo=13&nombre=PRODUCTOS&destino=idprod_nombre&esp1=<%= rec.getID_Bodega() %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
                  <td> <input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre" size="40" maxlength="120" readonly="true"></td>
                  <td width="7%" align="right"> <input name="precio" type="text" class="cpoBco" id="precio" size="10" maxlength="20"></td>
                  <td width="7%" align="right">&nbsp;</td>
                  <td width="5%" align="right"> <input name="descuento" type="text" class="cpoBco" id="descuento" size="6" maxlength="7"></td>
                  <td width="5%" align="right"> 
                    <input name="iva" type="text" class="cpoBco" id="iva" size="6" maxlength="9"></td>
                  <td width="5%" align="right">&nbsp;</td>
                  <td width="5%" align="right">&nbsp;</td>
                  <td width="5%" align="right">&nbsp;</td>
                  <td width="6%" align="right">
				  <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0">
                  <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" border="0"></a></td>
                </tr>
                <tr> 
                  <td colspan="12" align="right"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="15%" class="txtChicoAzc">Observaciones:</td>
                        <td><input name="obs_partida" type="text" class="cpoBco" id="obs_partida" size="50" maxlength="80">
                          </td>
						 <td width="38%"><%= rec.getNotaPrecio() %></td> 
                      </tr>
                    </table></td>
                </tr>
<%
	}
	
	if(rec.numPartidas() == 0)
	{
		out.println("<tr><td align=\"center\" class=\"titCuerpoBco\" colspan=\"12\">Inserta aqu&iacute; las partidas</td></tr>");
	}
	else
	{						
		for(int i = 0; i < rec.numPartidas(); i++)
		{
%>
                <tr> 
                  <td width="5%" align="right"><%= rec.getPartida(i).getCantidad() %></td>
                  <td width="4%"><%= rec.getPartida(i).getUnidad() %></td>
<%
			if(request.getParameter("proceso").equals("ENLAZAR_VENTA"))
			{ 
%>
				  <td width="13%">  
				    <input name="idprod_<%= i %>" type="text" class="cpoBco" id="idprod_<%= i %>" size="10" maxlength="20" value="<%= rec.getPartida(i).getID_Prod() %>"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_fact_dlg&lista=idprod_<%= i %>&idcatalogo=13&nombre=PRODUCTOS&destino=idprod_nombre_<%= i %>&esp1=<%= rec.getID_Bodega() %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
				  <td><input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre_<%= i %>" size="40" maxlength="120" value="<%= rec.getPartida(i).getID_ProdNombre() %>" readonly="true"></td>	
<%
			}
			else
			{
%>
				  <td width="13%"><%= rec.getPartida(i).getID_Prod() %></td>
				  <td><%= rec.getPartida(i).getID_ProdNombre() %></td>
<%  
			}	
%>  
                  <td width="7%" align="right"><%= rec.getPartida(i).getPrecio() %></td>
                  <td width="7%" align="right"><%= rec.getPartida(i).getImporte() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getDescuento() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getIVA() %>/<%= rec.getPartida(i).getImporteIVA() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getIEPS() %>/<%= rec.getPartida(i).getImporteIEPS() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getIVARet() %>/<%= rec.getPartida(i).getImporteIVARet() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getISRRet() %>/<%= rec.getPartida(i).getImporteISRRet() %></td>
                  <td width="6%" align="right">
					<% if(!request.getParameter("proceso").equals("CONSULTAR_VENTA") && !request.getParameter("proceso").equals("ENLAZAR_VENTA")) { %><a href="javascript:editarPartida('<%= i %>','<%= rec.getPartida(i).getCantidad() %>','<%= rec.getPartida(i).getID_Prod() %>','<%= rec.getPartida(i).getID_ProdNombre() %>','<%= rec.getPartida(i).getPrecio() %>','<%= rec.getPartida(i).getDescuento() %>','<%= rec.getPartida(i).getIVA() %>','<%= rec.getPartida(i).getObsPartida() %>');"><img src="../../imgfsi/lista_ed.gif" border="0"></a>
             			 <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_x.gif" border="0"><% } else { out.print("&nbsp;"); } %>
				  </td>
                </tr>
                <tr> 
                  <td colspan="12"><%= (request.getParameter("proceso").equals("ENLAZAR_VENTA") ? "Descripción del concepto:" : "Observaciones de la Partida:") + rec.getPartida(i).getObsPartida() %></td>
                </tr>
<%
		}
	}
%>                
              </table></td>
          </tr>
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr>
                  <td width="70%" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td class="titChicoAzc">Observaciones del documento:</td>
                      </tr>
                      <tr>
                        <td><textarea name="obs" cols="50" rows="3" id="obs"></textarea></td>
                      </tr>
                    </table>
                  </td>
                  <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr>
                        <td width="40%">Importe:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getImporte() %></td>
                      </tr>
                      <tr>
                        <td width="40%">Descuento:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getDescuento() %></td>
                      </tr>
                      <tr>
                        <td width="40%">Sub Total:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getSubTotal() %></td>
                      </tr>
                      <tr>
                        <td width="40%">IVA:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getIVA() %></td>
                      </tr>
					  <tr>
                        <td width="40%">IEPS:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getIEPS() %></td>
                      </tr>
					  <tr>
                        <td width="40%">Retención IVA:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getIVARet() %></td>
                      </tr>
					  <tr>
                        <td width="40%">Retención ISR:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getISRRet() %></td>
                      </tr>
                      <tr>
                        <td width="40%">Total:</td>
                        <td align="right" class="txtChicoAzc"><%= rec.getTotal() %></td>
                      </tr>
                    </table></td>
                </tr>
              </table></td>
          </tr>
        </table> 
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
<%
	if( request.getParameter("proceso").equals("AGREGAR_VENTA")    ||   request.getParameter("proceso").equals("CAMBIAR_VENTA"))
	{
%>	
document.ven_fact_dlg.numero.value = '<%= rec.getNumero() %>'
<%
	}
	if( request.getParameter("proceso").equals("AGREGAR_VENTA")    ||   request.getParameter("proceso").equals("CAMBIAR_VENTA") || request.getParameter("proceso").equals("ENLAZAR_VENTA"))
	{
%>		
document.ven_fact_dlg.idvendedor.value = '<%= rec.getID_Vendedor() %>'
document.ven_fact_dlg.vendedor_nombre.value = '<%= rec.getVendedorNombre() %>'
<%
	}
%>	
document.ven_fact_dlg.fecha.value = '<%=  JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy") %>'
document.ven_fact_dlg.entrega.value = '<%= JUtil.obtFechaTxt(rec.getFechaEntrega(),"dd/MMM/yyyy") %>'
document.ven_fact_dlg.factura.value = '<%= rec.getFactNum() %>'
document.ven_fact_dlg.referencia.value = '<%= rec.getReferencia() %>'
document.ven_fact_dlg.tc.value = '<%=  rec.getTC() %>'
document.ven_fact_dlg.obs.value = '<%= rec.getObs() %>'
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_VENTA")  && !request.getParameter("proceso").equals("ENLAZAR_VENTA") )
	{
%>	
document.ven_fact_dlg.cantidad.value = '<% if(request.getParameter("cantidad") != null) { out.print( request.getParameter("cantidad") ); } else { out.print("1"); } %>'
document.ven_fact_dlg.idprod.value = '<% if(request.getParameter("idprod") != null) { out.print( request.getParameter("idprod") ); } else { out.print(""); } %>'
document.ven_fact_dlg.idprod_nombre.value = '<% if(request.getParameter("idprod_nombre") != null) { out.print( request.getParameter("idprod_nombre") ); } else { out.print(""); } %>'
document.ven_fact_dlg.precio.value = '<% if(request.getParameter("precio") != null) { out.print( request.getParameter("precio") ); } else { out.print(""); } %>'
document.ven_fact_dlg.descuento.value = '<% if(request.getParameter("descuento") != null) { out.print( request.getParameter("descuento") ); } else { out.print(""); } %>'
document.ven_fact_dlg.iva.value = '<% if(request.getParameter("iva") != null) { out.print( request.getParameter("iva") ); } else { out.print(""); } %>'
document.ven_fact_dlg.obs_partida.value = '<% if(request.getParameter("obs_partida") != null) { out.print( request.getParameter("obs_partida") ); } else { out.print(""); } %>'
<%
	}
%>	
</script>
</body>
</html>
