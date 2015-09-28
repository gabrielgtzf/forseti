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
	String adm_saldos_dlg = (String)request.getAttribute("adm_saldos_dlg");
	if(adm_saldos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("ADM_SALDOS").generarTitulo(JUtil.Msj("CEF","ADM_SALDOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	String periodo = "";
	
	JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
	perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
	perIni.Open();
	if(perIni.getNumRows() > 0)
		periodo = " " + perIni.getAbsRow(0).getMes() + "/" + perIni.getAbsRow(0).getAno();
   	
	int inislds = Integer.parseInt( request.getParameter("id").substring(1,2));                  
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
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmSaldosDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_saldos_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmSaldosCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td>
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>">
				<input name="subproceso" type="hidden" value="ENVIAR">
                &nbsp;</td>
          </tr>
		  <tr> 
            <td class="titChicoNeg"><%= JUtil.Msj("CEF","ADM_SALDOS","DLG","ETQ3",inislds) + periodo %></td>
          </tr>
 		  <tr> 
            <td>&nbsp;</td>
          </tr>
		  <tr> 
           <td bgcolor="#FFFFFF">
<%
	if(request.getParameter("id").substring(1, 2).equals("1"))
    {
		JContaCatalogDetalleSet set = new JContaCatalogDetalleSet(request);
		if(perIni.getNumRows() > 0)
		{
			set.m_Where = "Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			set.m_OrderBy = "Cuenta ASC";
		}
		else
			set.m_Where = "Mes = 13 and Ano = 1999";
	
		set.Open();
%>
			  <table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
					<td width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
					<td width="65%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
				    <td bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
				</tr>
<%
		for(int i=0; i < set.getNumRows(); i++)
		{
			String clase = (!set.getAbsRow(i).getAcum()) ? " class=\"txtChicoNg\"" : " class=\"titChicoAzc\"";
%>
				  <tr<%= clase %>>
					<td><%= JUtil.obtCuentaFormato(new StringBuffer(set.getAbsRow(i).getCuenta()), request) %></td>
					<td><%= set.getAbsRow(i).getNombre() %></td>
					<td align="right">
<%
			if(!set.getAbsRow(i).getAcum())
			{
%>
						<input name="<%= set.getAbsRow(i).getCuenta() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter(set.getAbsRow(i).getCuenta()) != null) ? request.getParameter(set.getAbsRow(i).getCuenta()) : Float.toString(set.getAbsRow(i).getSaldoFinal()) ) %>">
<%
			}
			else
				out.print("&nbsp;");
%>
					</td>
				  </tr>		
<%
		}
%>		
		     </table>			
<%
	}
	else if(request.getParameter("id").substring(1, 2).equals("2"))
    {
		JBancosCuentasSaldosSet set = new JBancosCuentasSaldosSet(request);
		if(perIni.getNumRows() > 0)
		{
			set.m_Where = "Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			set.m_OrderBy = "Tipo ASC, Clave ASC";
		}
		else
			set.m_Where = "Mes = 13 and Ano = 1999";
	
		set.Open();
%>
			  <table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
					<td align="center" width="5%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
					<td bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
					<td width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %></td>
					<td align="right" width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
					<td align="center" width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","MONEDA") %></td>
					<td align="right" width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","TC",2) %></td>
					<td width="15%" bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
				</tr>
<%
		for(int i=0; i < set.getNumRows(); i++)
		{
			String clase = (set.getAbsRow(i).getStatus().equals("V")) ? " class=\"txtChicoNg\"" : " class=\"txtChicoRj\"";
%>
				  <tr<%= clase %>>
					<td align="center"><%= set.getAbsRow(i).getClave() %></td>
					<td><%= set.getAbsRow(i).getDescripcion() %></td>
					<td><% if(set.getAbsRow(i).getFijo()) { out.print("&nbsp;"); } else { out.print(JUtil.obtCuentaFormato(new StringBuffer(set.getAbsRow(i).getCC()), request)); } %></td>
					<td align="right"><% if(set.getAbsRow(i).getFijo()) { out.print("&nbsp;"); } else { out.print(set.getAbsRow(i).getCC_SaldoFin()); } %></td>
					<td align="center"><%= set.getAbsRow(i).getSimbolo() %></td>
					<td align="right">
<%
			if(set.getAbsRow(i).getTC() != 1)
			{
%>
						<input name="TC_<%= set.getAbsRow(i).getTipo() %>_<%= set.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter("TC_" + set.getAbsRow(i).getTipo() + "_" + set.getAbsRow(i).getClave()) != null) ? request.getParameter("TC_" + set.getAbsRow(i).getTipo() + "_" + set.getAbsRow(i).getClave()) : Float.toString(set.getAbsRow(i).getTC()) ) %>">
<%
			}
			else
				out.print(set.getAbsRow(i).getTC());
%>
					</td>					
					<td align="right">
						<input name="<%= set.getAbsRow(i).getTipo() %>_<%= set.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter(set.getAbsRow(i).getTipo() + "_" + set.getAbsRow(i).getClave()) != null) ? request.getParameter(set.getAbsRow(i).getTipo() + "_" + set.getAbsRow(i).getClave()) : Float.toString(set.getAbsRow(i).getSaldoFin()) ) %>">
					</td>
				</tr>		
<%
		}
%>		
		     </table>			
<%
	}
	else if(request.getParameter("id").substring(1, 2).equals("3"))
    {
		JAdmComprasEntidades set = new JAdmComprasEntidades(request);
		set.m_Where = "ID_TipoEntidad = '0'";
		set.m_OrderBy = "ID_EntidadCompra ASC";
		set.Open();
%>
			  <table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
			  	<tr>
					<td width="5%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
					<td bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
					<td width="12%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %> / <%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
					<td align="right" width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
					<td width="10%" bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","TC") %></td>
					<td width="15%" bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
				</tr>
<%
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
				  <tr>
					<td colspan="6">
					<table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
						<tr>
							<td align="center" width="5%" class="titChicoAzc"><%= set.getAbsRow(i).getID_EntidadCompra() %></td>
							<td class="titChicoAzc"><%= set.getAbsRow(i).getDescripcion() %></td>
						</tr>
					</table>
					</td>
				  </tr>
<%
			JProveeSaldosSet sal = new JProveeSaldosSet(request);
			sal.m_Where = "ID_Entidad = '" + set.getAbsRow(i).getID_EntidadCompra() + "' and ID_Tipo = 'PR' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			sal.m_OrderBy = "ID_Numero ASC";
			sal.Open();
			
			for(int j = 0; j < sal.getNumRows(); j++)
			{
%>
				  <tr>
					<td><%= sal.getAbsRow(j).getID_Numero() %></td>
					<td><%= sal.getAbsRow(j).getProveedor() %></td>
					<td><%= JUtil.obtCuentaFormato(new StringBuffer(sal.getAbsRow(j).getID_CC()), request) %></td>
					<td align="right"><%= sal.getAbsRow(j).getCC_SaldoFin() %></td>
					<td align="right">&nbsp;</td>		
					<td align="right"><%= sal.getAbsRow(j).getSaldoFin() %></td>	
				  </tr>
<%				  
				JProveeSaldosDetallesSet det = new JProveeSaldosDetallesSet(request);
				det.m_Where = "ID_Tipo = 'PR' and ID_Clave = '" + sal.getAbsRow(j).getID_Clave() + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
				det.m_OrderBy = "ID_Moneda ASC";
				det.Open();
			
				for(int k = 0; k < det.getNumRows(); k++)
				{
%>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>						
						<input name="CON_<%= det.getAbsRow(k).getID_Moneda() %>_<%= sal.getAbsRow(j).getID_Clave() %>" type="text" size="30" maxlength="80" value="<%= ( (request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()) != null) ? request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave() ) : det.getAbsRow(k).getDescripcion() ) %>">
					</td>
					<td align="right">&nbsp;</td>
					<td align="right"><%= det.getAbsRow(k).getSimbolo() %>
<%
					if(det.getAbsRow(k).getTC() != 1)
					{
%>
						<input name="TC_<%= det.getAbsRow(k).getID_Moneda() %>_<%= sal.getAbsRow(j).getID_Clave() %>" type="text" size="10" maxlength="20" value="<%= ( (request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()) != null) ? request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave() ) : Float.toString(det.getAbsRow(k).getTC()) ) %>">
<%
					}
					else
						out.print(det.getAbsRow(k).getTC());
%>
					</td>		
					<td align="right">
						<input name="SAL_<%= det.getAbsRow(k).getID_Moneda() %>_<%= sal.getAbsRow(j).getID_Clave() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()) != null) ? request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave() ) : Float.toString(det.getAbsRow(k).getSaldoFin()) ) %>">
					</td>	
				  </tr>
<%			
				}
			}
		}
%>		
		     </table>			
<%
	}
	else if(request.getParameter("id").substring(1, 2).equals("4"))
    {
		JAdmVentasEntidades set = new JAdmVentasEntidades(request);
		set.m_Where = "ID_TipoEntidad = '0'";
		set.m_OrderBy = "ID_EntidadVenta ASC";
		set.Open();
%>
			  <table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
			  	<tr>
					<td width="5%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
					<td bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
					<td width="12%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %> / <%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
					<td align="right" width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
					<td width="10%" bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","TC") %></td>
					<td width="15%" bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
				</tr>
<%
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
				  <tr>
					<td colspan="6">
					<table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
						<tr>
							<td align="center" width="5%" class="titChicoAzc"><%= set.getAbsRow(i).getID_EntidadVenta() %></td>
							<td class="titChicoAzc"><%= set.getAbsRow(i).getDescripcion() %></td>
						</tr>
					</table>
					</td>
				  </tr>
<%
			JClientSaldosSet sal = new JClientSaldosSet(request);
			sal.m_Where = "ID_Entidad = '" + set.getAbsRow(i).getID_EntidadVenta() + "' and ID_Tipo = 'CL' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			sal.m_OrderBy = "ID_Numero ASC";
			sal.Open();
			
			for(int j = 0; j < sal.getNumRows(); j++)
			{
%>
				  <tr>
					<td><%= sal.getAbsRow(j).getID_Numero() %></td>
					<td><%= sal.getAbsRow(j).getCliente() %></td>
					<td><%= JUtil.obtCuentaFormato(new StringBuffer(sal.getAbsRow(j).getID_CC()), request) %></td>
					<td align="right"><%= sal.getAbsRow(j).getCC_SaldoFin() %></td>
					<td align="right">&nbsp;</td>		
					<td align="right"><%= sal.getAbsRow(j).getSaldoFin() %></td>	
				  </tr>
<%				  
				JClientSaldosDetallesSet det = new JClientSaldosDetallesSet(request);
				det.m_Where = "ID_Tipo = 'CL' and ID_Clave = '" + sal.getAbsRow(j).getID_Clave() + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
				det.m_OrderBy = "ID_Moneda ASC";
				det.Open();
			
				for(int k = 0; k < det.getNumRows(); k++)
				{
%>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>						
						<input name="CON_<%= det.getAbsRow(k).getID_Moneda() %>_<%= sal.getAbsRow(j).getID_Clave() %>" type="text" size="30" maxlength="80" value="<%= ( (request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()) != null) ? request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave() ) : det.getAbsRow(k).getDescripcion() ) %>">
					</td>
					<td align="right">&nbsp;</td>
					<td align="right"><%= det.getAbsRow(k).getSimbolo() %>
<%
					if(det.getAbsRow(k).getTC() != 1)
					{
%>
						<input name="TC_<%= det.getAbsRow(k).getID_Moneda() %>_<%= sal.getAbsRow(j).getID_Clave() %>" type="text" size="10" maxlength="20" value="<%= ( (request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()) != null) ? request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave() ) : Float.toString(det.getAbsRow(k).getTC()) ) %>">
<%
					}
					else
						out.print(det.getAbsRow(k).getTC());
%>
					</td>		
					<td align="right">
						<input name="SAL_<%= det.getAbsRow(k).getID_Moneda() %>_<%= sal.getAbsRow(j).getID_Clave() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()) != null) ? request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave() ) : Float.toString(det.getAbsRow(k).getSaldoFin()) ) %>">
					</td>	
				  </tr>
<%			
				}
			}
		}
%>		
		     </table>			
<%
	}
	else if(request.getParameter("id").substring(1, 2).equals("5"))
    {
		JInvServCostosDetallesSet set = new JInvServCostosDetallesSet(request);
		if(perIni.getNumRows() > 0)
		{
			set.m_Where = "ID_Tipo = 'P' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			set.m_OrderBy = "ID_Prod ASC";
		}
		else
			set.m_Where = "Mes = 13 and Ano = 1999";
	
		set.Open();
%>
			  <table width="100%" border="0" cellpadding="1" cellspacing="0" bordercolor="#FFFFFF">
				<tr>
					<td width="5%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
					<td bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
					<td width="12%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %></td>
					<td align="right" width="15%" bgcolor="#0099FF" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
					<td width="10%" bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CP",2) %> / <%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %></td>
					<td width="15%" bgcolor="#0099FF" align="right" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
				</tr>
<%
		for(int i=0; i < set.getNumRows(); i++)
		{
			String clase = (set.getAbsRow(i).getStatus().equals("V")) ? " class=\"txtChicoNg\"" : " class=\"txtChicoRj\"";
%>
				  <tr<%= clase %>>
					<td><%= set.getAbsRow(i).getID_Prod() %></td>
					<td><%= set.getAbsRow(i).getDescripcion() %></td>
					<td><%= JUtil.obtCuentaFormato(new StringBuffer(set.getAbsRow(i).getID_CC()), request) %></td>
					<td align="right"><%= set.getAbsRow(i).getCC_SaldoFin() %></td>
					<td align="right"><%= set.getAbsRow(i).getCostoPromFin()%></td>		
					<td align="right">
						<input name="<%= set.getAbsRow(i).getID_Prod() %>" type="text" size="15" maxlength="20" value="<%= ( (request.getParameter(set.getAbsRow(i).getID_Prod()) != null) ? request.getParameter(set.getAbsRow(i).getID_Prod()) : Float.toString(set.getAbsRow(i).getSaldoFin()) ) %>">
					</td>	
				  </tr>
<%
			JInvServInvExistenciasSaldosSet bod = new JInvServInvExistenciasSaldosSet(request);
			bod.m_Where = "ID_Tipo = 'P' and ID_Prod = '" + JUtil.p(set.getAbsRow(i).getID_Prod()) + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			bod.m_OrderBy = "ID_Bodega ASC";
			bod.Open();
			for(int j = 0; j < bod.getNumRows(); j++)
			{
%>
				<tr<%= clase %>>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><%= bod.getAbsRow(j).getNombre() %></td>
					<td align="right"><input name="BOD_<%= bod.getAbsRow(j).getID_Bodega() %>_<%= set.getAbsRow(i).getID_Prod() %>" type="text" size="10" maxlength="20" value="<%= ( (request.getParameter("BOD_" + bod.getAbsRow(j).getID_Bodega() + "_" + set.getAbsRow(i).getID_Prod()) != null) ? request.getParameter("BOD_" + bod.getAbsRow(j).getID_Bodega() + "_" + set.getAbsRow(i).getID_Prod()) : Float.toString(bod.getAbsRow(j).getExistenciaFin()) ) %>"></td>
					<td><%= bod.getAbsRow(j).getUnidad() %></td>
				</tr>
<%				
			}
		}
%>		
		     </table>			
<%
	}
%>			
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
</body>
</html>
