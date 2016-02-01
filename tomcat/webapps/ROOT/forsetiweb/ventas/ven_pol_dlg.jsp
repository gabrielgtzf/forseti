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
	String ven_pol_dlg = (String)request.getAttribute("ven_pol_dlg");
	if(ven_pol_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("VEN_POL").generarTitulo(JUtil.Msj("CEF","VEN_POL","VISTA",request.getParameter("proceso"),3));
	
	session = request.getSession(true);
    JVenPoliticasSes rec = ( JVenPoliticasSes)session.getAttribute("ven_pol_dlg");
	String ent = JUtil.getSesion(request).getSesion("VEN_POL").getEspecial();

	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	if(ent.equals("CLIENTES") && request.getParameter("proceso").equals("PRECIOS_CLIENTE"))
	{
		setMon.m_OrderBy = "Clave ASC";
		setMon.Open();
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
<%
	if(ent.equals("CLIENTES"))
	{ 
		if(request.getParameter("proceso").equals("PRECIOS_CLIENTE"))
		{
%>
function limpiarFormulario()
{
	document.ven_pol_dlg.idprod.value = "";
	document.ven_pol_dlg.idprod_nombre.value = "";
	document.ven_pol_dlg.precio.value = "";
	document.ven_pol_dlg.idmoneda.selectedIndex = 0;
}

function editarPartida(idpartida, idprod, idprod_nombre, precio, idmoneda)
{
	document.ven_pol_dlg.idpartida.value = idpartida;
	document.ven_pol_dlg.subproceso.value = "EDIT_PART";

	document.ven_pol_dlg.idprod.value = idprod;
	document.ven_pol_dlg.idprod_nombre.value = idprod_nombre;
	document.ven_pol_dlg.precio.value = precio;
	document.ven_pol_dlg.idmoneda.selectedIndex = idmoneda;
}

function enviarlo(formAct)
{
	if(formAct.proceso.value == "PRECIOS_CLIENTE")
	{
		if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
		{
			if(	!esCadena("Clave:", formAct.idprod.value, 1, 20)  ||
				!esNumeroDecimal("Precio:", formAct.precio.value, 0, 9999999999, 2) )
				return false;
			
		}
		return true;
	}
	else
	{	
		return false;
	}
}
<%
		} 
		else if(request.getParameter("proceso").equals("DESCUENTOS_CLIENTE"))
		{
%>
function limpiarFormulario()
{
	document.ven_pols_dlg.idprod.value = "";
	document.ven_pol_dlg.idprod_nombre.value = "";
	document.ven_pol_dlg.descuento.value = "";
	document.ven_pol_dlg.descuento2.value = "";
	document.ven_pol_dlg.descuento3.value = "";
	document.ven_pol_dlg.descuento4.value = "";
	document.ven_pol_dlg.descuento5.value = "";
}

function editarPartida(idpartida, idprod, idprod_nombre, descuento, descuento2, descuento3, descuento4, descuento5)
{
	document.ven_pol_dlg.idpartida.value = idpartida;
	document.ven_pol_dlg.subproceso.value = "EDIT_PART";

	document.ven_pol_dlg.idprod.value = idprod;
	document.ven_pol_dlg.idprod_nombre.value = idprod_nombre;
	document.ven_pol_dlg.descuento.value = descuento;
	document.ven_pol_dlg.descuento2.value = descuento2;
	document.ven_pol_dlg.descuento3.value = descuento3;
	document.ven_pol_dlg.descuento4.value = descuento4;
	document.ven_pol_dlg.descuento5.value = descuento5;
}

function enviarlo(formAct)
{
	if(formAct.proceso.value == "DESCUENTOS_CLIENTE")
	{
		if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
		{
			if(	!esCadena("Clave:", formAct.idprod.value, 1, 20) )
				return false;
			
		}
		return true;
	}
	else
	{	
		return false;
	}
}		
<%
		}
	}
	else if(ent.equals("PRODUCTOS") )
	{
		if(request.getParameter("proceso").equals("PRECIOS_PROD"))
		{
%>
function enviarlo(formAct)
{
		return true;
}	
<%
		}
		else if(request.getParameter("proceso").equals("AUMENTOS_PROD"))
		{
%>
function enviarlo(formAct)
{
		if(	!esNumeroEntero("Nmero de decimales:", formAct.FSI_DEC.value, 0,2)  ||	
			!esNumeroDecimal("Precio 1:", formAct.FSI_P1.value, -100,100, 6)  ||
			!esNumeroDecimal("Precio 2:", formAct.FSI_P2.value, -100,100, 6)  ||
			!esNumeroDecimal("Precio 3:", formAct.FSI_P3.value, -100,100, 6)  ||
			!esNumeroDecimal("Precio 4:", formAct.FSI_P4.value, -100,100, 6)  ||
			!esNumeroDecimal("Precio 5:", formAct.FSI_P5.value, -100,100, 6)  ||
			!esNumeroDecimal("Precio Min:", formAct.FSI_PMIN.value, -100,100, 6)  ||
			!esNumeroDecimal("Precio Max:", formAct.FSI_PMAX.value, -100,100, 6) )
			return false;
		else 
			return true;
}
<%
		}
		else if(request.getParameter("proceso").equals("CANTIDADES_PROD"))
		{
%>
function enviarlo(formAct)
{
		if(	!esNumeroDecimal("Desde cantidad, Precio 1:", formAct.FSI_DESDE_P1.value, 0,999999, 3)  ||
			!esNumeroDecimal("Hasta cantidad, Precio 1:", formAct.FSI_HASTA_P1.value, 0,999999, 3)  ||
			!esNumeroDecimal("Desde cantidad, Precio 2:", formAct.FSI_DESDE_P2.value, 0,999999, 3)  ||
			!esNumeroDecimal("Hasta cantidad, Precio 2:", formAct.FSI_HASTA_P2.value, 0,999999, 3)  ||
			!esNumeroDecimal("Desde cantidad, Precio 3:", formAct.FSI_DESDE_P3.value, 0,999999, 3)  ||
			!esNumeroDecimal("Hasta cantidad, Precio 3:", formAct.FSI_HASTA_P3.value, 0,999999, 3)  ||
			!esNumeroDecimal("Desde cantidad, Precio 4:", formAct.FSI_DESDE_P4.value, 0,999999, 3)  ||
			!esNumeroDecimal("Hasta cantidad, Precio 4:", formAct.FSI_HASTA_P4.value, 0,999999, 3)  ||
			!esNumeroDecimal("Desde cantidad, Precio 5:", formAct.FSI_DESDE_P5.value, 0,999999, 3)  ||
			!esNumeroDecimal("Hasta cantidad, Precio 5:", formAct.FSI_HASTA_P5.value, 0,999999, 3) )
			return false;
		else 
			return true;
}
<%
		}
	}
%>
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFVenPoliticasDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_pol_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFVenPoliticasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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

<%
	if(ent.equals("CLIENTES"))
	{
		if(request.getParameter("proceso").equals("PRECIOS_CLIENTE"))
		{
%>
  <tr> 
      <td>
	    <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
                    <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
                    Cliente </td>
                  <td width="30%" class="titChicoAzc"><%= rec.getNumeroClient() %></td>
                  <td width="20%">Entidad</td>
                  <td width="30%" class="titChicoAzc"><%= rec.getEntidadVenta() %></td>
                </tr>
                <tr> 
                  <td width="20%">Nombre:</td>
                  <td colspan="3" class="titChicoAzc"><%= rec.getNombreCliente() %></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%" class="titChicoAzc">Clave</td>
                  <td class="titChicoAzc">Descripci&oacute;n</td>
				  <td width="5%" class="titChicoAzc">Uni</td>
                  <td width="7%" align="right" class="titChicoAzc">Precio</td>
				  <td width="15%" align="right" class="titChicoAzc">Moneda</td>
                  <td width="6%">&nbsp;</td>
                </tr>
                <tr valign="top"> 
                  <td width="20%"> <input name="idprod" type="text" class="cpoBco" id="idprod" size="10" maxlength="20"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_pol_dlg&lista=idprod&idcatalogo=19&nombre=PRODUCTOS&destino=idprod_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
                  <td> <input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre" size="40" maxlength="120" readonly="true"></td>
                  <td width="5%">&nbsp;</td>
				  <td width="7%" align="right"> <input name="precio" type="text" class="cpoBco" id="precio" size="7" maxlength="12"></td>
				  <td width="15%" align="right">
				  <select name="idmoneda" class="cpoBco" onChange="javascript:establecerTC(this.form.idmoneda, this.form.tc)">
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
						</select>
					</td>				  
				  
				    <td width="6%" align="right">
					<input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0">
                    <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" border="0"></a></td>
                </tr>
<%
			if(rec.numPartidas() == 0)
			{
				out.println("<tr><td align=\"center\" class=\"titCuerpoBco\" colspan=\"6\">Inserta aqu los precios del cliente</td></tr>");
			}
			else
			{						
				for(int i = 0; i < rec.numPartidas(); i++)
				{
%>
                <tr> 
                  <td width="20%"><%= rec.getPartida(i).getID_Prod() %></td>
                  <td><%= rec.getPartida(i).getID_ProdNombre() %></td>
				  <td width="5%"><%= rec.getPartida(i).getUnidad() %></td>
				  <td width="7%" align="right"><%= rec.getPartida(i).getPrecio() %></td>
				  <td width="15%" align="right"><%= rec.getPartida(i).getMoneda() %></td>
                  <td width="6%" align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR")) { %><a href="javascript:editarPartida('<%= i %>','<%= rec.getPartida(i).getID_Prod() %>','<%= rec.getPartida(i).getID_ProdNombre() %>','<%= rec.getPartida(i).getPrecio() %>','<%= rec.getPartida(i).getID_Moneda() - 1 %>');"><img src="../../imgfsi/lista_ed.gif" border="0"></a>
             			 <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_x.gif" border="0"><% } else { out.print("&nbsp;"); } %></td>
                </tr>
<%
				}
			}
%>                
              </table></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table> 
	</td>
  </tr>
<%
		} // fin proceso PRECIOS CLIENTE
		else if(request.getParameter("proceso").equals("DESCUENTOS_CLIENTE"))
		{
%>
  <tr> 
      <td>
	    <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
                    <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
                    Cliente </td>
                  <td width="30%" class="titChicoAzc"><%= rec.getNumeroClient() %></td>
                  <td width="20%">Entidad</td>
                  <td width="30%" class="titChicoAzc"><%= rec.getEntidadVenta() %></td>
                </tr>
                <tr> 
                  <td width="20%">Nombre:</td>
                  <td colspan="2" class="titChicoAzc"><%= rec.getNombreCliente() %></td>
				  <td width="30%"><input name="tipoorigen" type="radio" value="porcentaje"<% if(request.getParameter("tipoorigen") == null || request.getParameter("tipoorigen").equals("porcentaje")) { out.print(" checked"); } %>>
                    Porcentaje<br><input type="radio" name="tipoorigen" value="precio"<% if(request.getParameter("tipoorigen") != null && request.getParameter("tipoorigen").equals("precio")) { out.print(" checked"); } %>> Precio</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="15%" class="titChicoAzc">Clave</td>
                  <td class="titChicoAzc">Descripci&oacute;n</td>
				  <td width="4%" class="titChicoAzc">Uni</td>
                  <td width="6%" align="right" class="titChicoAzc">D1</td>
				  <td width="6%" align="right" class="titChicoAzc">D2</td>
				  <td width="6%" align="right" class="titChicoAzc">D3</td>
				  <td width="6%" align="right" class="titChicoAzc">D4</td>
				  <td width="6%" align="right" class="titChicoAzc">D5</td>
				  <td width="6%">&nbsp;</td>
                </tr>
                <tr valign="top"> 
                  <td width="15%"> <input name="idprod" type="text" class="cpoBco" id="idprod" size="10" maxlength="20"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=ven_pol_dlg&lista=idprod&idcatalogo=19&nombre=PRODUCTOS&destino=idprod_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
                  <td> <input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre" size="40" maxlength="120" readonly="true"></td>
                  <td width="4%">&nbsp;</td>
				  <td width="6%" align="right"> <input name="descuento" type="text" class="cpoBco" id="descuento" size="7" maxlength="12"></td>
				    <td width="6%" align="right"> <input name="descuento2" type="text" class="cpoBco" id="descuento2" size="7" maxlength="12"></td>
				  <td width="6%" align="right"> <input name="descuento3" type="text" class="cpoBco" id="descuento3" size="7" maxlength="12"></td>
				  <td width="6%" align="right"> <input name="descuento4" type="text" class="cpoBco" id="descuento4" size="7" maxlength="12"></td>
				  <td width="6%" align="right"> <input name="descuento5" type="text" class="cpoBco" id="descuento5" size="7" maxlength="12"></td>
					<td width="6%" align="right">
					<input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0">
                    <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" border="0"></a></td>
                </tr>
<%
			if(rec.numOtros() == 0)
			{
				out.println("<tr><td align=\"center\" class=\"titCuerpoBco\" colspan=\"5\">Inserta aqu los descuentos del cliente</td></tr>");
			}
			else
			{							
				for(int i = 0; i < rec.numOtros(); i++)
				{
%>
                <tr> 
                  <td width="15%"><%= rec.getOtro(i).getID_Prod() %></td>
                  <td><%= rec.getOtro(i).getID_ProdNombre() %></td>
				  <td width="4%"><%= rec.getOtro(i).getUnidad() %></td>
				  <td width="6%" align="right"><%= rec.getOtro(i).getDescuento() %></td>
				   <td width="6%" align="right"><%= rec.getOtro(i).getDescuento2() %></td>
				  <td width="6%" align="right"><%= rec.getOtro(i).getDescuento3() %></td>
				  <td width="6%" align="right"><%= rec.getOtro(i).getDescuento4() %></td>
				  <td width="6%" align="right"><%= rec.getOtro(i).getDescuento5() %></td>
				 <td width="6%" align="right"><% if(!request.getParameter("proceso").equals("CONSDESC")) { %><a href="javascript:editarPartida('<%= i %>','<%= rec.getOtro(i).getID_Prod() %>','<%= rec.getOtro(i).getID_ProdNombre() %>','<%= rec.getOtro(i).getDescuento() %>','<%= rec.getOtro(i).getDescuento2() %>','<%= rec.getOtro(i).getDescuento3() %>','<%= rec.getOtro(i).getDescuento4() %>','<%= rec.getOtro(i).getDescuento5() %>');"><img src="../../imgfsi/lista_ed.gif" border="0"></a>
             			 <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_x.gif" border="0"><% } else { out.print("&nbsp;"); } %></td>
                </tr>
               
<%
				}
			}
%>                
              </table></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table> 
	</td>
  </tr>
<%
		} // fin proceso CAMBDESC
	} // Fin ENT CLIENTES
	else if(ent.equals("PRODUCTOS"))
	{
		if(request.getParameter("proceso").equals("PRECIOS_PROD"))
		{
%>
<tr>
   <td>
	   <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
                     </td>
          </tr>
          <tr>
            <td>
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="15%" align="left" class="titChicoAzc">Clave</td>
                  <td class="titChicoAzc">Descripci&oacute;n</td>
				  <td width="5%" align="center" class="titChicoAzc">Uni</td>
				  <td width="7%" align="right" class="titChicoAzc">P1</td>
				  <td width="7%" align="right" class="titChicoAzc">P2</td>
				  <td width="7%" align="right" class="titChicoAzc">P3</td>
				  <td width="7%" align="right" class="titChicoAzc">P4</td>
				  <td width="7%" align="right" class="titChicoAzc">P5</td>
				  <td width="7%" align="right" class="titChicoAzc">Min</td>
				  <td width="7%" align="right" class="titChicoAzc">Max</td>
                </tr>
<%
			for(int i = 0; i < rec.numObjetos(); i++)
			{
				byte dec = 2;
				float UC;
				if(rec.getObjeto(i).getID_Moneda() != 1) 
				{
					JContaMonedasSetV2 setMon2 = new JContaMonedasSetV2(request);
					setMon2.m_Where = "Clave = '" + rec.getObjeto(i).getID_Moneda() + "'";
					setMon2.Open();
				
					UC = JUtil.redondear(rec.getObjeto(i).getPComp() * setMon2.getAbsRow(0).getTC(), (byte)4);
				} 
				else
					UC = rec.getObjeto(i).getPComp();
			
				float P1 =  ((UC != 0 && rec.getObjeto(i).getP1() != 0) ? JUtil.redondear((((rec.getObjeto(i).getP1() / UC) - 1) * 100), dec) : 0); 
				float P2 =  ((UC != 0 && rec.getObjeto(i).getP2() != 0) ? JUtil.redondear((((rec.getObjeto(i).getP2() / UC) - 1) * 100), dec) : 0); 	
				float P3 =  ((UC != 0 && rec.getObjeto(i).getP3() != 0) ? JUtil.redondear((((rec.getObjeto(i).getP3() / UC) - 1) * 100), dec) : 0); 	
				float P4 =  ((UC != 0 && rec.getObjeto(i).getP4() != 0) ? JUtil.redondear((((rec.getObjeto(i).getP4() / UC) - 1) * 100), dec) : 0); 	
				float P5 =  ((UC != 0 && rec.getObjeto(i).getP5() != 0) ? JUtil.redondear((((rec.getObjeto(i).getP5() / UC) - 1) * 100), dec) : 0); 	
				float PMin =  ((UC != 0 && rec.getObjeto(i).getPMin() != 0) ? JUtil.redondear((((rec.getObjeto(i).getPMin() / UC) - 1) * 100), dec) : 0); 	
				float PMax =  ((UC != 0 && rec.getObjeto(i).getPMax() != 0) ? JUtil.redondear((((rec.getObjeto(i).getPMax() / UC) - 1) * 100), dec) : 0); 
%>
                <tr> 
                  <td width="15%" align="left"><%= rec.getObjeto(i).getClave() %></td>
                  <td><%= rec.getObjeto(i).getDescripcion() %></td>
				  <td width="5%" align="center"><%= rec.getObjeto(i).getUnidad() %></td>
                  <td width="7%" align="right">
							<input name="FSI_P1_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P1_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getP1()); } else { out.print(request.getParameter("FSI_P1_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P2_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P2_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getP2()); } else { out.print(request.getParameter("FSI_P2_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P3_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P3_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getP3()); } else { out.print(request.getParameter("FSI_P3_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P4_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P4_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getP4()); } else { out.print(request.getParameter("FSI_P4_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P5_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P5_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getP5()); } else { out.print(request.getParameter("FSI_P5_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_PMIN_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_PMIN_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getPMin()); } else { out.print(request.getParameter("FSI_PMIN_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_PMAX_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_PMAX_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getPMax()); } else { out.print(request.getParameter("FSI_PMAX_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                </tr>
                <tr> 
                  <td colspan="2">&nbsp;</td>
                  <td width="5%" align="right"><b><%= UC %></b></td>
                  <td width="7%" align="right"><b><%= ((P1 <= 0) ? "<font color='red'>" + Float.toString(P1) + "</font>" : Float.toString(P1) ) %></b></td>
                  <td width="7%" align="right"><b><%= ((P2 <= 0) ? "<font color='red'>" + Float.toString(P2) + "</font>" : Float.toString(P2) ) %></b></td>
                  <td width="7%" align="right"><b><%= ((P3 <= 0) ? "<font color='red'>" + Float.toString(P3) + "</font>" : Float.toString(P3) ) %></b></td>
                  <td width="7%" align="right"><b><%= ((P4 <= 0) ? "<font color='red'>" + Float.toString(P4) + "</font>" : Float.toString(P4) ) %></b></td>
                  <td width="7%" align="right"><b><%= ((P5 <= 0) ? "<font color='red'>" + Float.toString(P5) + "</font>" : Float.toString(P5) ) %></b></td>
                  <td width="7%" align="right"><b><%= ((PMin <= 0) ? "<font color='red'>" + Float.toString(PMin) + "</font>" : Float.toString(PMin) ) %></b></td>
                  <td width="7%" align="right"><b><%= ((PMax <= 0) ? "<font color='red'>" + Float.toString(PMax) + "</font>" : Float.toString(PMax) ) %></b></td>
				</tr>
<%
			}
%>                
              </table>
			</td>
          </tr>
           <tr>
            <td>&nbsp;
			 
			</td>
          </tr>
      </table>
   </td>
</tr>	
<%
		} // FinPrecios
		else if(request.getParameter("proceso").equals("AUMENTOS_PROD"))
		{
			JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
			set.m_Where = "Clave = '" + JUtil.p(request.getParameter("ID")) + "'";
			set.Open();
%>
  <tr>
  	 <td> 
	    <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr> 
            <td width="20%"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
              <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
              <input type="hidden" name="ID" value="<%= set.getAbsRow(0).getClave()  %>"> 
              <input type="hidden" name="idprod" value="<%= set.getAbsRow(0).getClave()  %>"> 
              <input type="hidden" name="idlinea" value="<%= set.getAbsRow(0).getLinea()  %>">
              Clave:</td>
            <td width="30%" class="titChicoAzc"><%= set.getAbsRow(0).getClave() %></td>
            <td width="20%">Linea:</td>
            <td width="30%" class="titChicoAzc"><%= set.getAbsRow(0).getLinea() %></td>
          </tr>
          <tr> 
            <td width="20%" valign="top">Descripci&oacute;n:</td>
            <td width="30%" valign="top"><%= set.getAbsRow(0).getDescripcion() %></td>
            <td width="20%" valign="top"> <input type="radio" name="prodlin" value="PROD"<% if(request.getParameter("prodlin") == null || request.getParameter("prodlin").equals("PROD")) { out.print(" checked");} %>>
              Aplica a este producto <br> <input type="radio" name="prodlin" value="LINE"<% if(request.getParameter("prodlin") != null && request.getParameter("prodlin").equals("LINE")) { out.print(" checked");} %>>
              Aplica a toda la linea del producto</td>
			 <td width="30%" valign="top"><input type="radio" name="procfin" value="PRUEBA"<% if(request.getParameter("procfin") == null || request.getParameter("procfin").equals("PRUEBA")) { out.print(" checked");} %>>
              Prueba como quedar&aacute;n los precios <br> <input type="radio" name="procfin" value="CAMBIO"<% if(request.getParameter("procfin") != null && request.getParameter("procfin").equals("CAMBIO")) { out.print(" checked");} %>>
              Aplica definit&iacute;vamente los cambios</td> 
          </tr>
          <tr> 
            <td width="20%" valign="top">Decimales:</td>
            <td width="30%" valign="top"><input name="FSI_DEC" type="text" size="5" maxlength="1" value="<% if(request.getParameter("FSI_DEC") == null) { out.print("1"); } else { out.print(request.getParameter("FSI_DEC")); } %>"></td>
            <td width="20%" valign="top">&nbsp; </td>
			<td width="30%" valign="top">&nbsp;</td> 
          </tr>		  
        </table></td>
  </tr>
  <tr>
   	<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr align="right"> 
            <td width="14%" class="titChicoAzc">% Precio</td>
			<td width="14%" class="titChicoAzc">% Precio 2</td>
			<td width="14%" class="titChicoAzc">% Precio 3</td>
			<td width="14%" class="titChicoAzc">% Precio 4</td>
			<td width="14%" class="titChicoAzc">% Precio 5</td>
			<td width="14%" class="titChicoAzc">% Precio Min</td>
			<td width="14%" class="titChicoAzc">% Precio Max</td>
		  </tr>
		  <tr align="right"> 
            <td><input name="FSI_P1" type="text" value="<% if(request.getParameter("FSI_P1") == null) { out.print("1.00"); } else { out.print(request.getParameter("FSI_P1")); } %>" size="14" maxlength="15">
            </td>
			<td><input name="FSI_P2" type="text" value="<% if(request.getParameter("FSI_P2") == null) { out.print("1.00"); } else { out.print(request.getParameter("FSI_P2")); } %>" size="14" maxlength="15">
            </td>
			<td><input name="FSI_P3" type="text" value="<% if(request.getParameter("FSI_P3") == null) { out.print("1.00"); } else { out.print(request.getParameter("FSI_P3")); } %>" size="14" maxlength="15">
            </td>
			<td><input name="FSI_P4" type="text" value="<% if(request.getParameter("FSI_P4") == null) { out.print("1.00"); } else { out.print(request.getParameter("FSI_P4")); } %>" size="14" maxlength="15">
            </td>
			<td><input name="FSI_P5" type="text" value="<% if(request.getParameter("FSI_P5") == null) { out.print("1.00"); } else { out.print(request.getParameter("FSI_P5")); } %>" size="14" maxlength="15">
            </td>
			<td><input name="FSI_PMIN" type="text" value="<% if(request.getParameter("FSI_PMIN") == null) { out.print("1.00"); } else { out.print(request.getParameter("FSI_PMIN")); } %>" size="14" maxlength="15">
            </td>
			<td><input name="FSI_PMAX" type="text" value="<% if(request.getParameter("FSI_PMAX") == null) { out.print("1.00"); } else { out.print(request.getParameter("FSI_PMAX")); } %>" size="14" maxlength="15">
            </td>
		  </tr>
<%
			String aumento = (String)request.getAttribute("AUMENTO");
			if(aumento != null)
			{
				JPoliticasInvservCantAumentoPreciosSet aum = new JPoliticasInvservCantAumentoPreciosSet(request);
				String sql = "select * from sp_politicas_invserv_cant_aumento_precios('PRUEBA','" + 
	    			(request.getParameter("prodlin").equals("PROD") ? request.getParameter("idprod") :  request.getParameter("idlinea") ) + "','" + 
	      			request.getParameter("prodlin") + "','" + request.getParameter("FSI_DEC") + "','" + request.getParameter("FSI_P1") + "','" + request.getParameter("FSI_P2") + "','" + 
	      			request.getParameter("FSI_P3") + "','" + request.getParameter("FSI_P4") + "','" + request.getParameter("FSI_P5") + "','" + request.getParameter("FSI_PMIN") + "','" + request.getParameter("FSI_PMAX") + "') as (" +
					"ID_Prod varchar, Descripcion varchar, P1A numeric, P1M numeric, P2A numeric, P2M numeric, P3A numeric, P3M numeric, P4A numeric, P4M numeric, P5A numeric, P5M numeric, PMinA numeric, PMinM numeric, PMaxA numeric, PMaxM numeric)";
				aum.setSQL(sql);
				aum.Open();
				
				for(int i = 0; i < aum.getNumRows(); i++)
				{
%>
		  <tr>
		  	<td colspan="2"><%= aum.getAbsRow(i).getID_Prod() %></td>
			<td colspan="5"><%= aum.getAbsRow(i).getDescripcion() %></td>
		  </tr>
		  <tr align="right"> 
            <td width="14%"><%= aum.getAbsRow(i).getP1A() %>&nbsp;&nbsp;/&nbsp;&nbsp;<b><%= aum.getAbsRow(i).getP1M() %></b></td>
			<td width="14%"><%= aum.getAbsRow(i).getP2A() %>&nbsp;&nbsp;/&nbsp;&nbsp;<b><%= aum.getAbsRow(i).getP2M() %></b></td>
			<td width="14%"><%= aum.getAbsRow(i).getP3A() %>&nbsp;&nbsp;/&nbsp;&nbsp;<b><%= aum.getAbsRow(i).getP3M() %></b></td>
			<td width="14%"><%= aum.getAbsRow(i).getP4A() %>&nbsp;&nbsp;/&nbsp;&nbsp;<b><%= aum.getAbsRow(i).getP4M() %></b></td>
			<td width="14%"><%= aum.getAbsRow(i).getP5A() %>&nbsp;&nbsp;/&nbsp;&nbsp;<b><%= aum.getAbsRow(i).getP5M() %></b></td>
			<td width="14%"><%= aum.getAbsRow(i).getPMinA() %>&nbsp;&nbsp;/&nbsp;&nbsp;<b><%= aum.getAbsRow(i).getPMinM() %></b></td>
			<td width="14%"><%= aum.getAbsRow(i).getPMaxA() %>&nbsp;&nbsp;/&nbsp;&nbsp;<b><%= aum.getAbsRow(i).getPMaxM() %></b></td>
		  </tr>
<%
				}
			}
%>		  
		</table>
	</td>
</tr>
<tr>
  	<td>&nbsp;</td>
</tr>
<%
		}
		else if(request.getParameter("proceso").equals("CANTIDADES_PROD"))
		{
			JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
			set.m_Where = "Clave = '" + JUtil.p(request.getParameter("ID")) + "'";
			set.Open();
	
%>
<tr>
   <td>
	   <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
					<input type="hidden" name="ID" value="<%= set.getAbsRow(0).getClave()  %>"> 
                    <input type="hidden" name="idprod" value="<%= set.getAbsRow(0).getClave()  %>"> 
                    <input type="hidden" name="idlinea" value="<%= set.getAbsRow(0).getLinea()  %>">
                    Clave:</td>
                  <td width="30%" class="titChicoAzc"><%= set.getAbsRow(0).getClave() %></td>
                  <td width="20%">Linea:</td>
                  <td width="30%" class="titChicoAzc"><%= set.getAbsRow(0).getLinea() %></td>
                </tr>
                <tr> 
                  <td width="20%" valign="top">Descripci&oacute;n:</td>
                  <td width="30%" valign="top"><%= set.getAbsRow(0).getDescripcion() %></td>
                  <td colspan="2" valign="top"> 
                    <input type="radio" name="prodlin" value="PROD"<% if(request.getParameter("prodlin") == null || request.getParameter("prodlin").equals("PROD")) { out.print(" checked");} %>>
                    Aplica a este producto <br>
                    <input type="radio" name="prodlin" value="LINE"<% if(request.getParameter("prodlin") != null && request.getParameter("prodlin").equals("LINE")) { out.print(" checked");} %>>
                    Aplica a toda la linea del producto</td>
                </tr>
              </table> </td>
          </tr>
          <tr>
            <td>
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%" align="left" class="titChicoAzc">&nbsp;</td>
                  <td width="20%" align="right" class="titChicoAzc">&nbsp;</td>
				  <td width="30%" align="right" class="titChicoAzc">Desde cantidad</td>
				  <td width="30%" align="right" class="titChicoAzc">Hasta cantidad</td>
				 </tr>
				<tr> 
                  <td width="20%" align="left" >Precio: </td>
                  <td width="20%" align="right" class="titChicoAzc"><%= set.getAbsRow(0).getPrecio()%></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_DESDE_P1" type="text" size="15" maxlength="15"></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_HASTA_P1" type="text" size="15" maxlength="15"></td>
				 </tr>
				 <tr> 
                  <td width="20%" align="left" >Precio 2: </td>
                  <td width="20%" align="right" class="titChicoAzc"><%= set.getAbsRow(0).getPrecio2()%></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_DESDE_P2" type="text" size="15" maxlength="15"></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_HASTA_P2" type="text" size="15" maxlength="15"></td>
				 </tr>
				<tr> 
                  <td width="20%" align="left" >Precio 3: </td>
                  <td width="20%" align="right" class="titChicoAzc"><%= set.getAbsRow(0).getPrecio3()%></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_DESDE_P3" type="text" size="15" maxlength="15"></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_HASTA_P3" type="text" size="15" maxlength="15"></td>
				 </tr>
				<tr> 
                  <td width="20%" align="left" >Precio 4: </td>
                  <td width="20%" align="right" class="titChicoAzc"><%= set.getAbsRow(0).getPrecio4()%></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_DESDE_P4" type="text" size="15" maxlength="15"></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_HASTA_P4" type="text" size="15" maxlength="15"></td>
				 </tr>
				<tr> 
                  <td width="20%" align="left" >Precio 5: </td>
                  <td width="20%" align="right" class="titChicoAzc"><%= set.getAbsRow(0).getPrecio5()%></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_DESDE_P5" type="text" size="15" maxlength="15"></td>
				  <td width="30%" align="right" class="titChicoAzc"><input name="FSI_HASTA_P5" type="text" size="15" maxlength="15"></td>
				 </tr>

              </table>
			</td>
          </tr>
           <tr>
            <td>&nbsp;
			 
			</td>
          </tr>
      </table>
   </td>
</tr>	
<%
		} //FIN Cantidades en ENT CANTIDADES
	} // Fin Ent CANTIDADES
	/////////////////////////////////////////////////////////////////////////////////////
	else if(ent.equals("ENTIDADES"))
	{
		if(request.getParameter("proceso").equals("POLITICAS_ENTIDAD"))
		{
%>
<tr>
   <td>
	   <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
					<input name="ID" type="hidden" value="<%= request.getParameter("ID")%>"> 
                     </td>
          </tr>
          <tr>
            <td>
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="15%" align="left" class="titChicoAzc">Clave</td>
                  <td class="titChicoAzc">Descripci&oacute;n</td>
				  <td width="7%" align="right" class="titChicoAzc">P1</td>
				  <td width="7%" align="right" class="titChicoAzc">P2</td>
				  <td width="7%" align="right" class="titChicoAzc">P3</td>
				  <td width="7%" align="right" class="titChicoAzc">P4</td>
				  <td width="7%" align="right" class="titChicoAzc">P5</td>
				  <td width="14%" align="center" class="titChicoAzc">Aplica</td>
				</tr>
<%
			JVentasPoliticasEntDescModSet set = new JVentasPoliticasEntDescModSet(request);
			set.m_Where = "ID_Entidad = '" + JUtil.p(request.getParameter("ID")) + "' and ";
			set.m_Where += JUtil.getSesion(request).getSesion("VEN_POL").generarWhere();
            set.m_OrderBy = JUtil.getSesion(request).getSesion("VEN_POL").generarOrderBy();
            set.Open();
            for(int i=0; i < set.getNumRows(); i++)
			{
%>
                <tr> 
                  <td width="15%" align="left"><%= set.getAbsRow(i).getClave() %></td>
                  <td><%= set.getAbsRow(i).getDescripcion() %></td>
				  <td width="7%" align="right">
							<input name="FSI_P1_<%= set.getAbsRow(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P1_" + set.getAbsRow(i).getClave()) == null) { out.print(set.getAbsRow(i).getP1()); } else { out.print(request.getParameter("FSI_P1_" + set.getAbsRow(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P2_<%= set.getAbsRow(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P2_" + set.getAbsRow(i).getClave()) == null) { out.print(set.getAbsRow(i).getP2()); } else { out.print(request.getParameter("FSI_P2_" + set.getAbsRow(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P3_<%= set.getAbsRow(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P3_" + set.getAbsRow(i).getClave()) == null) { out.print(set.getAbsRow(i).getP3()); } else { out.print(request.getParameter("FSI_P3_" + set.getAbsRow(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P4_<%= set.getAbsRow(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P4_" + set.getAbsRow(i).getClave()) == null) { out.print(set.getAbsRow(i).getP4()); } else { out.print(request.getParameter("FSI_P4_" + set.getAbsRow(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="7%" align="right">
							<input name="FSI_P5_<%= set.getAbsRow(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_P5_" + set.getAbsRow(i).getClave()) == null) { out.print(set.getAbsRow(i).getP5()); } else { out.print(request.getParameter("FSI_P5_" + set.getAbsRow(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="14%" align="right">
				  	<select name="FSI_APL_<%= set.getAbsRow(i).getClave() %>">
						<option value="0"<% 
							if(request.getParameter("FSI_APL_" + set.getAbsRow(i).getClave()) != null ) {
								if(request.getParameter("FSI_APL_" + set.getAbsRow(i).getClave()).equals("0") ) { 
									out.print(" selected"); 
								}
							} else { 
								if(set.getAbsRow(i).getAplicacion()  == 0 ) {
									out.println(" selected"); 
								} 
							}%>>Precio bajo</option>
						<option value="1"<% 
							if(request.getParameter("FSI_APL_" + set.getAbsRow(i).getClave()) != null ) {
								if(request.getParameter("FSI_APL_" + set.getAbsRow(i).getClave()).equals("1") ) { 
									out.print(" selected"); 
								}
							} else { 
								if(set.getAbsRow(i).getAplicacion()  == 1 ) {
									out.println(" selected"); 
								} 
							}%>>Descuento</option>
					   <option value="2"<% 
							if(request.getParameter("FSI_APL_" + set.getAbsRow(i).getClave()) != null ) {
								if(request.getParameter("FSI_APL_" + set.getAbsRow(i).getClave()).equals("2") ) { 
									out.print(" selected"); 
								}
							} else { 
								if(set.getAbsRow(i).getAplicacion()  == 2 ) {
									out.println(" selected"); 
								} 
							}%>>Precio alto</option>	
    				  </select></td>
                </tr>
<%
			}
%>                
              </table>
			</td>
          </tr>
           <tr>
            <td>&nbsp;
			 
			</td>
          </tr>
      </table> </td>
  </tr>	
<%
		} // Fin CAMBENT
	} // fin ENT ENTIDADES
	/////////////////////////////////////////////////////////////////////////////////////
%>
</table>
</form>
<script language="JavaScript1.2">
<%
	if(ent.equals("CLIENTES"))
	{
		if(request.getParameter("proceso").equals("PRECIOS_CLIENTE") )
		{
%>	
document.ven_pol_dlg.idprod.value = '<% if(request.getParameter("idprod") != null) { out.print( request.getParameter("idprod") ); } else { out.print(""); } %>'
document.ven_pol_dlg.idprod_nombre.value = '<% if(request.getParameter("idprod_nombre") != null) { out.print( request.getParameter("idprod_nombre") ); } else { out.print(""); } %>'
document.ven_pol_dlg.precio.value = '<% if(request.getParameter("precio") != null) { out.print( request.getParameter("precio") ); } else { out.print(""); } %>'
<%
		}
		else if(request.getParameter("proceso").equals("DESCUENTOS_CLIENTE") )
		{
%>	
document.ven_pol_dlg.idprod.value = '<% if(request.getParameter("idprod") != null) { out.print( request.getParameter("idprod") ); } else { out.print(""); } %>'
document.ven_pol_dlg.idprod_nombre.value = '<% if(request.getParameter("idprod_nombre") != null) { out.print( request.getParameter("idprod_nombre") ); } else { out.print(""); } %>'
document.ven_pol_dlg.descuento.value = '<% if(request.getParameter("descuento") != null) { out.print( request.getParameter("descuento") ); } else { out.print(""); } %>'
document.ven_pol_dlg.descuento2.value = '<% if(request.getParameter("descuento2") != null) { out.print( request.getParameter("descuento2") ); } else { out.print(""); } %>'
document.ven_pol_dlg.descuento3.value = '<% if(request.getParameter("descuento3") != null) { out.print( request.getParameter("descuento3") ); } else { out.print(""); } %>'
document.ven_pol_dlg.descuento4.value = '<% if(request.getParameter("descuento4") != null) { out.print( request.getParameter("descuento4") ); } else { out.print(""); } %>'
document.ven_pol_dlg.descuento5.value = '<% if(request.getParameter("descuento5") != null) { out.print( request.getParameter("descuento5") ); } else { out.print(""); } %>'

<%
		}		
	}
	else if(ent.equals("PRODUCTOS"))
	{
		if(request.getParameter("proceso").equals("CANTIDADES_PROD"))
		{
				JPoliticasInvServCantPrecioSetV2 setPol = new JPoliticasInvServCantPrecioSetV2(request);
				setPol.m_Where = "ID_Prod = '" + JUtil.p(request.getParameter("ID")) + "'";
				setPol.Open();
			
%>
document.ven_pol_dlg.FSI_DESDE_P1.value = '<% if(request.getParameter("FSI_DESDE_P1") != null) { out.print( request.getParameter("FSI_DESDE_P1") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantDesde()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_HASTA_P1.value = '<% if(request.getParameter("FSI_HASTA_P1") != null) { out.print( request.getParameter("FSI_HASTA_P1") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantHasta()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_DESDE_P2.value = '<% if(request.getParameter("FSI_DESDE_P2") != null) { out.print( request.getParameter("FSI_DESDE_P2") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantDesde2()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_HASTA_P2.value = '<% if(request.getParameter("FSI_HASTA_P2") != null) { out.print( request.getParameter("FSI_HASTA_P2") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantHasta2()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_DESDE_P3.value = '<% if(request.getParameter("FSI_DESDE_P3") != null) { out.print( request.getParameter("FSI_DESDE_P3") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantDesde3()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_HASTA_P3.value = '<% if(request.getParameter("FSI_HASTA_P3") != null) { out.print( request.getParameter("FSI_HASTA_P3") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantHasta3()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_DESDE_P4.value = '<% if(request.getParameter("FSI_DESDE_P4") != null) { out.print( request.getParameter("FSI_DESDE_P4") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantDesde4()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_HASTA_P4.value = '<% if(request.getParameter("FSI_HASTA_P4") != null) { out.print( request.getParameter("FSI_HASTA_P4") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantHasta4()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_DESDE_P5.value = '<% if(request.getParameter("FSI_DESDE_P5") != null) { out.print( request.getParameter("FSI_DESDE_P5") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantDesde5()); } else { out.print(""); } } %>'
document.ven_pol_dlg.FSI_HASTA_P5.value = '<% if(request.getParameter("FSI_HASTA_P5") != null) { out.print( request.getParameter("FSI_HASTA_P5") ); } else { if(setPol.getNumRows() > 0) { out.print(setPol.getAbsRow(0).getCantHasta5()); } else { out.print(""); } } %>'
<%			
		}
	}
%>	
</script>
</body>
</html>
