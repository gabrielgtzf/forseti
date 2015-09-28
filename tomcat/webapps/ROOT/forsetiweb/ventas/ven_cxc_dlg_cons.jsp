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
	String ven_cxc_dlg = (String)request.getAttribute("ven_cxc_dlg");
	if(ven_cxc_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("VEN_CXC").generarTitulo(JUtil.Msj("CEF","VEN_CXC","VISTA",request.getParameter("proceso"),3));

	JClientCXCSetV2 smod = new JClientCXCSetV2(request);
	smod.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
	smod.Open();
	JClientCXCSetV2 set = new JClientCXCSetV2(request);
	set.m_Where = "ID_Aplicacion = '" + JUtil.p(request.getParameter("id")) + "' and ID_TipoCP <> '" + smod.getAbsRow(0).getID_TipoCP() + "'";
	set.m_OrderBy = "Clave ASC";
	set.Open();
	  
	String Tipos = JUtil.Msj("CEF", "VEN_CXC", "VISTA", "TIPO", 3);
	String sts = JUtil.Msj("CEF", "VEN_CXC", "VISTA", "STATUS");
	String coletq = JUtil.Msj("CEF","VEN_CXC","DLG","COLUMNAS");

	String tipocxc, status;
	
	if(smod.getAbsRow(0).getID_TipoCP().equals("ALT")) 
		tipocxc = JUtil.Elm(Tipos,2);
	else if(smod.getAbsRow(0).getID_TipoCP().equals("ANT")) 
		tipocxc = JUtil.Elm(Tipos,3);
	else if(smod.getAbsRow(0).getID_TipoCP().equals("PAG")) 
		tipocxc = JUtil.Elm(Tipos,4);
	else if(smod.getAbsRow(0).getID_TipoCP().equals("SAL")) 
		tipocxc = JUtil.Elm(Tipos,5);
	else if(smod.getAbsRow(0).getID_TipoCP().equals("APL")) 
		tipocxc = JUtil.Elm(Tipos,6);
	else if(smod.getAbsRow(0).getID_TipoCP().equals("DPA")) 
		tipocxc = JUtil.Elm(Tipos,7);
	else if(smod.getAbsRow(0).getID_TipoCP().equals("DEV")) 
		tipocxc = JUtil.Elm(Tipos,8);
	else
		tipocxc = "";
			
	if(smod.getAbsRow(0).getStatus().equals("G"))
		status = JUtil.Elm(sts,1); 
	else if(smod.getAbsRow(0).getStatus().equals("C"))
		status = JUtil.Elm(sts,2);
	else
		status = "";
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFVenCXCDlg" method="post" enctype="application/x-www-form-urlencoded" name="ven_cxc_dlg_pagos" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_CUENTA")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFVenCXCCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		    <td align="center" colspan="8" class="titChicoAzc"><%= tipocxc %></td> 
		  </tr>
		  <tr>
            <td width="10%"> <input name="subproceso" type="hidden" value="ENVIAR"> 
              <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
              <input name="id" type="hidden" value="<%= request.getParameter("id")%>"> 
              <input name="entidad" type="hidden" value="<%= JUtil.getSesion(request).getSesion("VEN_CXC").getEspecial() %>">
              <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
            <td width="15%" class="txtChicoAzc"><%= smod.getAbsRow(0).getClave() %></td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
            <td width="15%" class="txtChicoAzc"><%= JUtil.obtFechaTxt(smod.getAbsRow(0).getFecha(),"dd/MMM/yyyy") %></td>
			<td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></td>
            <td width="15%" class="txtChicoAzc"><%= status %></td>
			<td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %></td>
            <td width="15%" class="txtChicoAzc"><%= smod.getAbsRow(0).getRef() %></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","CLIENTE") %></td>
            <td colspan="3" class="titChicoAzc"><%= smod.getAbsRow(0).getNombre() %></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
            <td colspan="3" class="titChicoAzc"><%= smod.getAbsRow(0).getConcepto() %></td>
          </tr>
		   <tr> 
		    <td><%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %></td>
            <td class="txtChicoAzc"><%= smod.getAbsRow(0).getMonedaSim() + " " +  smod.getAbsRow(0).getTotal() + " -" + smod.getAbsRow(0).getTC() + "- " + smod.getAbsRow(0).getTotalPesos() %></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","SALDO") %></td>
            <td class="txtChicoAzc"><%= smod.getAbsRow(0).getMonedaSim() + " " +  smod.getAbsRow(0).getSaldo() + " -" + smod.getAbsRow(0).getTC() + "- " + smod.getAbsRow(0).getSaldoPesos() %></td>
          	<td><%= JUtil.Msj("GLB","GLB","GLB","VENCIMIENTO") %></td>
            <td class="txtChicoAzc"><%= JUtil.obtFechaTxt(smod.getAbsRow(0).getVencimiento(),"dd/MMM/yyyy") %></td>
           	<td><%= JUtil.Msj("GLB","GLB","GLB","PAGO") %></td>
            <td class="txtChicoAzc"><%= smod.getAbsRow(0).getPagoBanCaj() %></td>
		  </tr>
		  <tr> 
            <td colspan="8">
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
<%
	if(set.getNumRows() > 0)
	{
		String TiposLst = JUtil.Msj("CEF", "VEN_CXC", "VISTA", "TIPO", 2);
		float total = 0.0F, totalpesos = 0.0F;
%>
                <tr bgcolor="#0099FF"> 
                  <td width="10%" align="center" class="titChico"><%= JUtil.Elm(coletq,1) %></td>
                  <td width="7%" class="titChico"><%= JUtil.Elm(coletq,2) %></td>
                  <td width="13%" class="titChico"><%= JUtil.Elm(coletq,3) %></td>
                  <td width="30%" class="titChico"><%= JUtil.Elm(coletq,4) %></td>
                  <td width="20%" class="titChico" align="right"><%= JUtil.Elm(coletq,5) %></td>
                  <td width="20%" class="titChico"><%= JUtil.Elm(coletq,6) %></td>
		        </tr>
<%
		for(int i = 0; i < set.getNumRows(); i++)
		{
			if(!set.getAbsRow(i).getStatus().equals("C"))
			{
				total += set.getAbsRow(i).getTotal();
				totalpesos += set.getAbsRow(i).getTotalPesos();
			}
			
			String tipocxclst, claselst;
	
			if(set.getAbsRow(i).getID_TipoCP().equals("ALT")) 
				tipocxclst = JUtil.Elm(TiposLst,2);
			else if(set.getAbsRow(i).getID_TipoCP().equals("ANT")) 
				tipocxclst = JUtil.Elm(TiposLst,3);
			else if(set.getAbsRow(i).getID_TipoCP().equals("PAG")) 
				tipocxclst = JUtil.Elm(TiposLst,4);
			else if(set.getAbsRow(i).getID_TipoCP().equals("SAL")) 
				tipocxclst = JUtil.Elm(TiposLst,5);
			else if(set.getAbsRow(i).getID_TipoCP().equals("APL")) 
				tipocxclst = JUtil.Elm(TiposLst,6);
			else if(set.getAbsRow(i).getID_TipoCP().equals("DPA")) 
				tipocxclst = JUtil.Elm(TiposLst,7);
			else if(set.getAbsRow(i).getID_TipoCP().equals("DEV")) 
				tipocxclst = JUtil.Elm(TiposLst,8);
			else
				tipocxclst = "";
					
			if(set.getAbsRow(i).getStatus().equals("G"))
				claselst = "";
			else if(set.getAbsRow(i).getStatus().equals("C"))
				claselst = " class=\"txtChicoRj\"";
			else
				claselst = "";
%>
			
                <tr<%= claselst %>> 
                  <td width="10%" align="center"><%= tipocxclst %></td>
                  <td width="7%"><%= set.getAbsRow(i).getClave() %></td>
                  <td width="13%"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(),"dd/MMM/yyyy") %></td>
                  <td width="30%"><%= set.getAbsRow(i).getConcepto() %></td>
                  <td width="20%" align="right"><%= set.getAbsRow(i).getMonedaSim() + " " + set.getAbsRow(i).getTotal() + " -" + set.getAbsRow(i).getTC() + "- " +  set.getAbsRow(i).getTotalPesos() %></td>
                  <td width="20%"><%= set.getAbsRow(i).getPagoBanCaj() %></td>
                </tr>
<%
		}
%>				
                 <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %></td>
                  <td align="right" class="titChicoNeg"><%= JUtil.redondear(total, (byte)2) + " - " + JUtil.redondear(totalpesos, (byte)2) %></td>
                  <td>&nbsp;</td>
                </tr>
<%
	}
%>
              </table>
			 </td>
          </tr>
        </table>
     </td>
  </tr>
</table>
</form>
</body>
</html>
