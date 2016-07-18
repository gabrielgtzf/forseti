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
	
	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();
	
	session = request.getSession(true);
    JCompFactSes rec;
	if(request.getParameter("proceso").equals("DEVOLVER_COMPRA") || request.getParameter("proceso").equals("REBAJAR_COMPRA"))
		rec = (JCompDevSes)session.getAttribute("comp_dev_dlg");
	else
		rec = (JCompFactSes)session.getAttribute("comp_fact_dlg");
	
	// Ahora revisa los bancos y cajas por si es de contado
	JPublicBancosCuentasVsComprasSetV2 bv = new JPublicBancosCuentasVsComprasSetV2(request);
	JPublicBancosCuentasVsComprasSetV2 cv = new JPublicBancosCuentasVsComprasSetV2(request);
	bv.m_OrderBy = "Clave ASC";
	cv.m_OrderBy = "Clave ASC";
	bv.m_Where = "Tipo = '0' and ID_EntidadCompra = '" + JUtil.getSesion(request).getSesion(idmod).getEspecial() + "'";
	cv.m_Where = "Tipo = '1' and ID_EntidadCompra = '" + JUtil.getSesion(request).getSesion(idmod).getEspecial() + "'";
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
	if(moddes.equals("FACTURAS") || moddes.equals("GASTOS"))
	{
%>	
function configurarPago()
{
	var cantidad = <%= rec.getTotal() %>;
	var idmon = <%= rec.getID_Moneda() %>
	var tc = document.comp_fact_dlg.tc.value;
	var total = redondear(cantidad * tc, 2);
	
	var refer = "../../forsetiweb/pagos_mult_dlg.jsp?formul=comp_fact_dlg&va_tipo=compras&va_proc=<% if(request.getParameter("proceso").equals("AGREGAR_COMPRA") || request.getParameter("proceso").equals("ENLAZAR_COMPRA")) { out.print("retiro"); } else { out.print("deposito"); } %>&va_total=" + total + "&va_ident=<%= JUtil.getSesion(request).getSesion(idmod).getEspecial() %>&va_cantidad=" + cantidad + "&va_idmon=" + idmon;
	
	abrirCatalogo(refer,150,350);
}

function configurarSaldo()
{
	var cantidad = <%= rec.getTotal() %>;
	var refer = "../../forsetiweb/saldos_dlg.jsp?formul=comp_fact_dlg&va_tipo=compras&va_proc=deposito&va_total=" + cantidad;
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
	document.comp_fact_dlg.cantidad.value = "";
	document.comp_fact_dlg.idprod.value = "";
	document.comp_fact_dlg.idprod_nombre.value = "";
	document.comp_fact_dlg.precio.value = "";
	document.comp_fact_dlg.descuento.value = "";
	document.comp_fact_dlg.iva.value = "";
	document.comp_fact_dlg.ieps.value = "";
	document.comp_fact_dlg.ivaret.value = "";
	document.comp_fact_dlg.isrret.value = "";
	document.comp_fact_dlg.obs_partida.value = "";
}

function editarPartida(idpartida, cantidad, idprod, idprod_nombre, precio, descuento, iva, ieps, ivaret, isrret, obs_partida)
{
	document.comp_fact_dlg.idpartida.value = idpartida;
	document.comp_fact_dlg.subproceso.value = "EDIT_PART";

	document.comp_fact_dlg.cantidad.value = cantidad;
	document.comp_fact_dlg.idprod.value = idprod;
	document.comp_fact_dlg.idprod_nombre.value = idprod_nombre;
	document.comp_fact_dlg.precio.value = precio;
	document.comp_fact_dlg.descuento.value = descuento;
	document.comp_fact_dlg.iva.value = iva;
	document.comp_fact_dlg.ieps.value = ieps;
	document.comp_fact_dlg.ivaret.value = ivaret;
	document.comp_fact_dlg.isrret.value = isrret;
	document.comp_fact_dlg.obs_partida.value = obs_partida;
}

function enviarlo(formAct)
{
	if(formAct.proceso.value == "AGREGAR_COMPRA"  ||  formAct.proceso.value == "CAMBIAR_COMPRA" || formAct.proceso.value == "DEVOLVER_COMPRA" || formAct.proceso.value == "REBAJAR_COMPRA" || formAct.proceso.value == "ENLAZAR_COMPRA")
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
				if(	!esNumeroDecimal("IVA:", formAct.iva.value, 0, 9999.999999, 6) )
					return false;
			}
			if(formAct.ieps.value != "")
			{
				if(	!esNumeroDecimal("IEPS:", formAct.ieps.value, 0, 9999.999999, 6) )
					return false;
			}
			if(formAct.ivaret.value != "")
			{
				if(	!esNumeroDecimal("Retención IVA:", formAct.ivaret.value, 0, 9999.999999, 6) )
					return false;
			}
			if(formAct.isrret.value != "")
			{
				if(	!esNumeroDecimal("Retención ISR:", formAct.isrret.value, 0, 9999.999999, 6) )
					return false;
			}
		}
		
		if(	!esNumeroEntero('<% if(moddes.equals("DEVOLUCIONES") || (moddes.equals("FACTURAS") && (request.getParameter("proceso").equals("DEVOLVER_COMPRA") || request.getParameter("proceso").equals("REBAJAR_COMPRA")))) { out.print("Nota de Credito:"); } else if(moddes.equals("FACTURAS")) { out.print("Factura:"); } else if(moddes.equals("ORDENES")) { out.print("Orden:"); } else if(moddes.equals("RECEPCIONES")) { out.print("Recepcion:"); } else if(moddes.equals("GASTOS")) { out.print("Gasto:"); }%>', formAct.factura.value, 1, 9999999999) ||
<%
	if(request.getParameter("proceso").equals("AGREGAR_COMPRA")  ||  request.getParameter("proceso").equals("CAMBIAR_COMPRA") )
	{ 
%>
			!esNumeroEntero('Proveedor:', formAct.numero.value, 0, 99999) ||
<%
	}
%>
			!esNumeroDecimal('Tipo de Cambio:', formAct.tc.value, 0, 9999999999, 4))
			return false;

		// revisa el pago
		if(formAct.subproceso.value == "ENVIAR")
		{
			if(formAct.tipomov.value == "FACTURAS" || formAct.tipomov.value == "GASTOS")
			{
				if(formAct.proceso.value == "DEVOLVER_COMPRA" || formAct.proceso.value == "REBAJAR_COMPRA")
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCompFactDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_fact_dlg" target="_self">
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
			  <%  if(request.getParameter("proceso").equals("ENLAZAR_COMPRA")) {
			   		if(JUtil.getSesion(request).getID_Mensaje() == 0)	{ %>
        			<input type="button" name="actualizar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACTUALIZAR") %>">
        			<%  } else { %>
        			<input type="button" name="actualizar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACTUALIZAR") %>" onClick="javascript: establecerProcesoSVE(this.form.subproceso, 'ACTUALIZAR'); this.form.submit();">
       				<%  }
				  } %>
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_COMPRA")) { %>
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
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr>
                  <td width="30%"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr>
                        <td width="40%">
						     <input name="tipomov" type="hidden" value="<%= request.getParameter("tipomov") %>">
<% if(!request.getParameter("proceso").equals("ENLAZAR_COMPRA")) { %><input name="ID" type="hidden" value="<%= request.getParameter("ID") %>"><% } %>
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
         N&ordm; <% if(moddes.equals("DEVOLUCIONES") || (moddes.equals("FACTURAS") && (request.getParameter("proceso").equals("DEVOLVER_COMPRA") || request.getParameter("proceso").equals("REBAJAR_COMPRA")))) { out.print("Nota de Credito:"); } else if(moddes.equals("FACTURAS")) { out.print("Compra:"); } else if(moddes.equals("ORDENES")) { out.print("Orden:"); } else if(moddes.equals("RECEPCIONES")) { out.print("Recepcion:"); } else if(moddes.equals("GASTOS")) { out.print("Gasto:"); } %></td>
                        <td><input name="factura" type="text" size="10" maxlength="15"></td>
                      </tr>
                      <tr>
                        <td width="40%">Referencia:</td>
                        <td><input name="referencia" type="text" id="referencia" size="15" maxlength="20"></td>
                      </tr>
                      <tr>
                        <td width="40%" valign="top">Pago:</td>
<% 
		if( request.getParameter("proceso").equals("AGREGAR_COMPRA")  ||  request.getParameter("proceso").equals("CAMBIAR_COMPRA") ||  request.getParameter("proceso").equals("ENLAZAR_COMPRA")) 
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
	if( request.getParameter("proceso").equals("AGREGAR_COMPRA") || request.getParameter("proceso").equals("CAMBIAR_COMPRA") || request.getParameter("proceso").equals("ENLAZAR_COMPRA"))
	{	
%>	
						 <select name="idmoneda" class="cpoBco" onChange="javascript:establecerTC(this.form.idmoneda, this.form.tc)">
<% 				
				for(int i = 0; i< setMon.getNumRows(); i++)
				{	
%>
                          <option value="<%=setMon.getAbsRow(i).getClave()%>"<% 
									if(rec.getID_Moneda() == setMon.getAbsRow(i).getClave())  {
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
                        <td width="40%">&nbsp;</td>
                        <td class="txtChicoAzc">&nbsp;</td>
                      </tr>
					  <tr>
                        <td width="40%">&nbsp;</td>
                        <td class="txtChicoAzc">&nbsp;</td>
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
                        <td width="15%">Proveedor:</td>
                        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width="35%" class="titChicoAzc"> 
<% 
		if(request.getParameter("proceso").equals("AGREGAR_COMPRA") || request.getParameter("proceso").equals("CAMBIAR_COMPRA") 
			|| (request.getParameter("proceso").equals("ENLAZAR_COMPRA") && moddes.equals("GASTOS"))) 
		{ 
%>
                                <input name="numero" type="text" id="numero" onBlur="javascript: if(this.form.numero.value != this.form.numeroses.value) { establecerProcesoSVE(this.form.subproceso, 'AGR_PROVEE'); this.form.submit(); }" size="7" maxlength="10"> 
 	    			<a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=comp_fact_dlg&lista=numero&idcatalogo=12&nombre=PROVEEDORES&destino=numero_nombre&esp1=<%= idmod %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a>
<% 
		} 
		else
		{
				out.print(rec.getNumero());
		}
%>								
							    </td>
                              <td width="20%">R.F.C.:</td>
                              <td class="txtChicoAzc"><%= rec.getRFC() %></td>
                            </tr>
                          </table></td>
                      </tr>
                      <tr> 
                        <td width="15%">Nombre:</td>
                        <td class="txtChicoAzc">
<% 
		if(request.getParameter("proceso").equals("AGREGAR_COMPRA") || request.getParameter("proceso").equals("CAMBIAR_COMPRA")
			|| (request.getParameter("proceso").equals("ENLAZAR_COMPRA") && moddes.equals("GASTOS")) ) 
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
                  <td width="5%" align="right" class="titChico">Desc</td>
                  <td width="5%" align="right" class="titChico">IVA</td>
                  <td width="5%" align="right" class="titChico">IEPS</td>
                  <td width="5%" align="right" class="titChico">R/IVA</td>
                  <td width="5%" align="right" class="titChico">R/ISR</td>
				  <td width="6%">&nbsp;</td>
                </tr>
<%
	if(request.getParameter("proceso").equals("AGREGAR_COMPRA") || request.getParameter("proceso").equals("CAMBIAR_COMPRA") || request.getParameter("proceso").equals("DEVOLVER_COMPRA") || request.getParameter("proceso").equals("REBAJAR_COMPRA")
			|| (request.getParameter("proceso").equals("ENLAZAR_COMPRA") && moddes.equals("GASTOS"))  )
	{
%>				
                <tr valign="top"<% if(request.getParameter("proceso").equals("ENLAZAR_COMPRA")) { out.print(" bgcolor=\"#0099FF\""); } %>> 
                  <td width="5%" align="right"> <input name="cantidad" type="text" class="cpoBco" id="cantidad" size="7" maxlength="12"></td>
                  <td width="4%">&nbsp;</td>
                  <td width="13%"> <input name="idprod" type="text" class="cpoBco" id="idprod" size="10" maxlength="20"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=comp_fact_dlg&lista=idprod&idcatalogo=<% if(request.getParameter("tipomov").equals("GASTOS")) { out.print("15"); } else { out.print("11"); } %>&nombre=PRODUCTOS&destino=idprod_nombre&esp1=<%= rec.getID_Bodega() %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
                  <td> <input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre" size="40" maxlength="120" readonly="true"></td>
                  <td width="7%" align="right"> <input name="precio" type="text" class="cpoBco" id="precio" size="10" maxlength="20"></td>
                  <td width="7%" align="right">&nbsp;</td>
                  <td width="5%" align="right"> <input name="descuento" type="text" class="cpoBco" id="descuento" size="6" maxlength="9"></td>
                  <td width="5%" align="right"> <input name="iva" type="text" class="cpoBco" id="iva" size="6" maxlength="11"></td>
				  <td width="5%" align="right"><input name="ieps" type="text" class="cpoBco" id="ieps" size="6" maxlength="11"></td>
                  <td width="5%" align="right"><input name="ivaret" type="text" class="cpoBco" id="ivaret" size="6" maxlength="11"></td>
                  <td width="5%" align="right"><input name="isrret" type="text" class="cpoBco" id="isrret" size="6" maxlength="11"></td>
                  <td width="6%" align="right">
				  <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0">
                  <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" border="0"></a></td>
                </tr>
                <tr<% if(request.getParameter("proceso").equals("ENLAZAR_COMPRA")) { out.print(" bgcolor=\"#0099FF\""); } %>> 
                  <td colspan="12" align="right"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="15%" class="txtChicoAzc">Observaciones:</td>
                        <td><input name="obs_partida" type="text" class="cpoBco" id="obs_partida" size="50" maxlength="80">
                          </td>
						 <td width="38%">&nbsp;</td> 
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
<%
			if(request.getParameter("proceso").equals("ENLAZAR_COMPRA"))
			{ 
%>	
				  <td width="5%" align="right"><input name="cantidad_<%= i %>" type="text" class="cpoBco" id="cantidad_<%= i %>" size="7" maxlength="12" value="<%= rec.getPartida(i).getCantidad() %>"></td>
                  <td width="4%"><%= rec.getPartida(i).getUnidad() %></td>			
				  <td width="13%">  
				    <input name="idprod_<%= i %>" type="text" class="cpoBco" id="idprod_<%= i %>" size="10" maxlength="20" value="<%= rec.getPartida(i).getID_Prod() %>"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=comp_fact_dlg&lista=idprod_<%= i %>&idcatalogo=<% if(request.getParameter("tipomov").equals("GASTOS")) { out.print("15"); } else { out.print("11"); } %>&nombre=PRODUCTOS&destino=idprod_nombre_<%= i %>&esp1=<%= rec.getID_Bodega() %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
				  <td><input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre_<%= i %>" size="40" maxlength="120" value="<%= rec.getPartida(i).getID_ProdNombre() %>" readonly="true"></td>	
				  <td width="7%" align="right"><input name="precio_<%= i %>" type="text" class="cpoBco" id="precio_<%= i %>" size="10" maxlength="20" value="<%= rec.getPartida(i).getPrecio() %>"></td>
                  <td width="7%" align="right"><%= rec.getPartida(i).getImporte() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getDescuento() %></td>
  				  <td width="5%" align="right"><input name="iva_<%= i %>" type="text" class="cpoBco" id="iva_<%= i %>" size="6" maxlength="30" value="<%= rec.getPartida(i).getIVA() %>:<%= rec.getPartida(i).getImporteIVA() %>"></td>
                  <td width="5%" align="right"><input name="ieps_<%= i %>" type="text" class="cpoBco" id="ieps_<%= i %>" size="6" maxlength="30" value="<%= rec.getPartida(i).getIEPS() %>:<%= rec.getPartida(i).getImporteIEPS() %>"></td>
                  <td width="5%" align="right"><input name="ivaret_<%= i %>" type="text" class="cpoBco" id="ivaret_<%= i %>" size="6" maxlength="30" value="<%= rec.getPartida(i).getIVARet() %>:<%= rec.getPartida(i).getImporteIVARet() %>"></td>
                  <td width="5%" align="right"><input name="isrret_<%= i %>" type="text" class="cpoBco" id="isrret_<%= i %>" size="6" maxlength="30" value="<%= rec.getPartida(i).getISRRet() %>:<%= rec.getPartida(i).getImporteISRRet() %>"></td>
<%
			}
			else
			{
%>
				  <td width="5%" align="right"><%= rec.getPartida(i).getCantidad() %></td>
                  <td width="4%"><%= rec.getPartida(i).getUnidad() %></td>
				  <td width="13%"><%= rec.getPartida(i).getID_Prod() %></td>
				  <td><%= rec.getPartida(i).getID_ProdNombre() %></td>
				  <td width="7%" align="right"><%= rec.getPartida(i).getPrecio() %></td>
                  <td width="7%" align="right"><%= rec.getPartida(i).getImporte() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getDescuento() %></td>
				  <td width="5%" align="right"><%= rec.getPartida(i).getIVA() %>:<%= rec.getPartida(i).getImporteIVA() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getIEPS() %>:<%= rec.getPartida(i).getImporteIEPS() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getIVARet() %>:<%= rec.getPartida(i).getImporteIVARet() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(i).getISRRet() %>:<%= rec.getPartida(i).getImporteISRRet() %></td>
<%  
			}	
%>  
                  <td width="6%" align="right">
					<% if(!request.getParameter("proceso").equals("CONSULTAR_COMPRA") && !request.getParameter("proceso").equals("ENLAZAR_COMPRA")) { %>					
					<a href="javascript:editarPartida('<%= i %>','<%= rec.getPartida(i).getCantidad() %>','<%= rec.getPartida(i).getID_Prod() %>','<%= rec.getPartida(i).getID_ProdNombre() %>','<%= rec.getPartida(i).getPrecio() %>','<%= rec.getPartida(i).getDescuento() %>','<%= rec.getPartida(i).getIVA() %>','<%= rec.getPartida(i).getIEPS() %>','<%= rec.getPartida(i).getIVARet() %>','<%= rec.getPartida(i).getISRRet() %>','<%= rec.getPartida(i).getObsPartida() %>');"><img src="../../imgfsi/lista_ed.gif" border="0"></a>
             			<input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_x.gif" border="0"><% } else { out.print("&nbsp;"); } %>
				  </td>
                </tr>
                <tr> 
                  <td colspan="12"><%= (request.getParameter("proceso").equals("ENLAZAR_COMPRA") ? "Descripción del proveedor:" : "Observaciones de la Partida:") + rec.getPartida(i).getObsPartida() %></td>
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
	if(request.getParameter("proceso").equals("AGREGAR_COMPRA") || request.getParameter("proceso").equals("CAMBIAR_COMPRA")
		|| (request.getParameter("proceso").equals("ENLAZAR_COMPRA") && moddes.equals("GASTOS")))
	{
%>	
document.comp_fact_dlg.numero.value = '<%= rec.getNumero() %>'
<%
	}
%>	
document.comp_fact_dlg.fecha.value = '<%=  JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy") %>'
document.comp_fact_dlg.entrega.value = '<%= JUtil.obtFechaTxt(rec.getFechaEntrega(),"dd/MMM/yyyy") %>'
document.comp_fact_dlg.factura.value = '<%= rec.getFactNum() %>'
document.comp_fact_dlg.referencia.value = '<%= rec.getReferencia() %>'
document.comp_fact_dlg.tc.value = '<%=  rec.getTC() %>'
document.comp_fact_dlg.obs.value = '<%= rec.getObs() %>'
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_COMPRA") && !request.getParameter("proceso").equals("ENLAZAR_COMPRA") )
	{
%>	
document.comp_fact_dlg.cantidad.value = '<% if(request.getParameter("cantidad") != null) { out.print( request.getParameter("cantidad") ); } else { out.print("1"); } %>'
document.comp_fact_dlg.idprod.value = '<% if(request.getParameter("idprod") != null) { out.print( request.getParameter("idprod") ); } else { out.print(""); } %>'
document.comp_fact_dlg.idprod_nombre.value = '<% if(request.getParameter("idprod_nombre") != null) { out.print( request.getParameter("idprod_nombre") ); } else { out.print(""); } %>'
document.comp_fact_dlg.precio.value = '<% if(request.getParameter("precio") != null) { out.print( request.getParameter("precio") ); } else { out.print(""); } %>'
document.comp_fact_dlg.descuento.value = '<% if(request.getParameter("descuento") != null) { out.print( request.getParameter("descuento") ); } else { out.print(""); } %>'
document.comp_fact_dlg.iva.value = '<% if(request.getParameter("iva") != null) { out.print( request.getParameter("iva") ); } else { out.print(""); } %>'
document.comp_fact_dlg.ieps.value = '<% if(request.getParameter("ieps") != null) { out.print( request.getParameter("ieps") ); } else { out.print(""); } %>'
document.comp_fact_dlg.ivaret.value = '<% if(request.getParameter("ivaret") != null) { out.print( request.getParameter("ivaret") ); } else { out.print(""); } %>'
document.comp_fact_dlg.isrret.value = '<% if(request.getParameter("isrret") != null) { out.print( request.getParameter("isrret") ); } else { out.print(""); } %>'
document.comp_fact_dlg.obs_partida.value = '<% if(request.getParameter("obs_partida") != null) { out.print( request.getParameter("obs_partida") ); } else { out.print(""); } %>'
<%
	}
%>	
</script>
</body>
</html>
