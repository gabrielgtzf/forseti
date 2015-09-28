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
	boolean registrado = JUtil.yaRegistradoEnFsi(request, response);
	// Inicia con registrar el objeto de sesion si no esta registrado
	if(!registrado) 
	{ 
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
 	}
	
	JPublicBancosCuentasVsComprasSetV2 bc = new JPublicBancosCuentasVsComprasSetV2(request);
	JPublicBancosCuentasVsComprasSetV2 cc = new JPublicBancosCuentasVsComprasSetV2(request);
	JPublicBancosCuentasVsVentasSetV2 bv = new JPublicBancosCuentasVsVentasSetV2(request);
	JPublicBancosCuentasVsVentasSetV2 cv = new JPublicBancosCuentasVsVentasSetV2(request);

	if(request.getParameter("va_tipo").equals("compras"))
	{
		bc.m_OrderBy = "Clave ASC";
		bc.m_Where = "Tipo = '0' and ID_EntidadCompra = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		bc.Open();
		cc.m_OrderBy = "Clave ASC";
		cc.m_Where = "Tipo = '1' and ID_EntidadCompra = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		cc.Open();
	}
	else // es ventas
	{
		bv.m_OrderBy = "Clave ASC";
		bv.m_Where = "Tipo = '0' and ID_EntidadVenta = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		bv.Open();
		cv.m_OrderBy = "Clave ASC";
		cv.m_Where = "Tipo = '1' and ID_EntidadVenta = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		cv.Open();
	}
	
	JSatBancosSet setBan = new JSatBancosSet(request);
    setBan.m_OrderBy = "Clave ASC";
    setBan.Open();
	
	JSatMetodosPagoSet setMet = new JSatMetodosPagoSet(request);
    setMet.m_OrderBy = "Clave ASC";
    setMet.Open();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript1.2" src="../compfsi/comps.js"></script>
<script language="JavaScript1.2">
<!-- 
<%
	if(request.getParameter("va_proc").equals("retiro"))
	{
		if(request.getParameter("va_tipo").equals("compras"))
		{
			if(bc.getNumRows() > 0)
			{
%>
cheques = new Array(<% 		
	for(int i = 0; i < bc.getNumRows(); i++)
	{
		out.print(bc.getAbsRow(i).getSigCheque() + ",");
	}
	%>-1);
<%
			}
		}
		else // es retiro de ventas
		{
			if(bv.getNumRows() > 0)
			{
%>
cheques = new Array(<% 		
	for(int i = 0; i < bv.getNumRows(); i++)
	{
		out.print(bv.getAbsRow(i).getSigCheque() + ",");
	}
	%>-1);
<%			
			}
		}
	}

	if(request.getParameter("va_proc").equals("retiro"))
	{
%>
function establecerCheque(selBan, depchq, metpagopol)
{
	var ind = metpagopol.selectedIndex
	
	if(ind == 1) // 1 es el valor del 02 cheque
	{
		if(selBan.value.substring(0,7) == 'FSI_BAN')
		{
			depchq.value = cheques[selBan.selectedIndex-1];
		}
		else if(selBan.value.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
		else
		{
			depchq.value = '0';
		}
	}
	else
	{
		if(selBan.value.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
		else
		{	
			depchq.value = '0';
		}
	}
}
<%
	}
	else // es deposito
	{
%>
function establecerRefer(selBan, depchq, metpagopol)
{
	var ind = metpagopol.selectedIndex
	
	if(ind == 1) // 1 es el valor del 02 cheque
	{
		if(selBan.value.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
	}
	else
	{
		if(selBan.value.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
		else
		{	
			depchq.value = '0';
		}
	}
}
<%
	}
%>

function transferirResultados()
{
	if(document.pagos.bancaj.value.substring(0,4) != 'FSI_')
	{
		alert("<%= JUtil.Msj("GLB","GLB","GLB","PAGOS-JSP",2) %>");
		return;
	}
	
	opener.document.<%= request.getParameter("formul") %>.fsipg_bancaj.value = document.pagos.bancaj.value;
	opener.document.<%= request.getParameter("formul") %>.fsipg_ref.value = document.pagos.ref.value;
	opener.document.<%= request.getParameter("formul") %>.fsipg_metpagopol.value = document.pagos.metpagopol.value;
	opener.document.<%= request.getParameter("formul") %>.fsipg_depchq.value = document.pagos.depchq.value;
	opener.document.<%= request.getParameter("formul") %>.fsipg_cuentabanco.value = document.pagos.cuentabanco.value;
	opener.document.<%= request.getParameter("formul") %>.fsipg_id_satbanco.value = document.pagos.id_satbanco.value;
	opener.document.<%= request.getParameter("formul") %>.fsipg_bancoext.value = document.pagos.bancoext.value;
	
	//alert("ALERTA: " + opener.document.<%= request.getParameter("formul") %>.fsipg_dch.value);
	//return;			
	window.close();
	opener.document.<%= request.getParameter("formul") %>.submit();
}
-->
</script>
<title>Forseti</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>

<body background="../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form name="pagos" action="<%= request.getRequestURI() %>" method="get" target="_self">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><input name="tiempo" type="hidden" value="MAS"><%= JUtil.Msj("GLB","GLB","GLB","PAGOS-JSP") %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td>
				<table width="100%" border="0" cellpadding="0" cellspacing="3">
                <tr> 
                  <td> <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr> 
                        <td width="20%">
						  <%= ((request.getParameter("va_proc").equals("retiro")) ? JUtil.Msj("GLB","GLB","GLB","RETIRO") : JUtil.Msj("GLB","GLB","GLB","DEPOSITO") ) %></td>
                        <td class="titChicoAzc">
                          <%= request.getParameter("va_total") %>
                        </td>
                      </tr>
                      <tr> 
                        <td width="20%" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","BANCOS",2) + " / " + JUtil.Msj("GLB","GLB","GLB","CAJAS",2) %></td>
                        <td valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							  <tr> 
								<td width="50%">
								
						<select style="width: 90%;" name="bancaj"<%= ((request.getParameter("va_proc").equals("retiro")) ? " onChange=\"javascript:establecerCheque(this.form.bancaj, this.form.depchq, this.form.metpagopol);\"" : " onChange=\"javascript:establecerRefer(this.form.bancaj, this.form.depchq, this.form.metpagopol);\"") %>>
<%
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(bc.getNumRows() > 0)
		{
%>
                            <option value="BANCOS"<% if(request.getParameter("bancaj") == null || request.getParameter("bancaj").equals("BANCOS")) 
								  							{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","BANCOS") %> ---</option>
                            <%
								  for(int i = 0; i< bc.getNumRows(); i++)
								  {
		                            out.print("<option value=\"FSI_BAN_" + bc.getAbsRow(i).getClave() + "\""); 
									if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("FSI_BAN_" + Byte.toString(bc.getAbsRow(i).getClave()))) 
								  		{ out.print(" selected>" + bc.getAbsRow(i).getCuenta() + "</option>"); } 
									else { out.print(">" + bc.getAbsRow(i).getCuenta() + "</option>"); } 
								  }
		}
		if(cc.getNumRows() > 0)
		{
								  %>
                            <option value="CAJAS"<% if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("CAJAS")) 
								  							{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %> ---</option>
                            <%
								  for(int i = 0; i< cc.getNumRows(); i++)
								  {
		                            out.print("<option value=\"FSI_CAJ_" + cc.getAbsRow(i).getClave() + "\""); 
									if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("FSI_CAJ_" + Byte.toString(cc.getAbsRow(i).getClave()))) 
								  		{ out.print(" selected>" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
									else { out.print(">" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
								  }
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
		if(bv.getNumRows() > 0)
		{
%>
                            <option value="BANCOS"<% if(request.getParameter("bancaj") == null || request.getParameter("bancaj").equals("BANCOS")) 
								  							{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","BANCOS") %> ---</option>
                            <%
								  for(int i = 0; i< bv.getNumRows(); i++)
								  {
		                            out.print("<option value=\"FSI_BAN_" + bv.getAbsRow(i).getClave() + "\""); 
									if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("FSI_BAN_" + Byte.toString(bv.getAbsRow(i).getClave()))) 
								  		{ out.print(" selected>" + bv.getAbsRow(i).getCuenta() + "</option>"); } 
									else { out.print(">" + bv.getAbsRow(i).getCuenta() + "</option>"); } 
								  }
		}
		if(cv.getNumRows() > 0)
		{
								  %>
                            <option value="CAJAS"<% if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("CAJAS")) 
								  							{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %> ---</option>
                            <%
								  for(int i = 0; i< cv.getNumRows(); i++)
								  {
		                            out.print("<option value=\"FSI_CAJ_" + cv.getAbsRow(i).getClave() + "\""); 
									if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("FSI_CAJ_" + Byte.toString(cv.getAbsRow(i).getClave()))) 
								  		{ out.print(" selected>" + cv.getAbsRow(i).getCuenta() + "</option>"); } 
									else { out.print(">" + cv.getAbsRow(i).getCuenta() + "</option>"); } 
								  }
		}
	}
%>
                          		</select>
							  </td>
							  <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %></td>
							  <td><input name="ref" type="text" id="ref" size="20" maxlength="20"></td>
							</tr>
						  </table>
						</td>
                      </tr>
                      <tr> 
                        <td width="20%">&nbsp;</td>
                        <td>
							<!--table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                               
                              <td width="50%"><input name="ref" type="text" id="ref" size="15" maxlength="20" value="----------" disabled> 
                              </td>
								<td><% if(request.getParameter("va_proc").equals("retiro")) { %>
									<input name="cheque" type="checkbox" id="cheque" value="cheque" onClick="javascript:establecerCheque(this.form.bancaj, this.form.ref, this.form.cheque);">
                                <%   } else { %>
									<input name="depchq" type="checkbox" id="depchq" value="depchq" onClick="javascript:establecerRefer(this.form.bancaj, this.form.ref, this.form.depchq);"><% } %><%= JUtil.Msj("GLB","GLB","GLB","CHEQUE") %></td>
							  </tr>
							</table-->
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							  <tr> 
								<td width="25%">Metodo</td>
								<td width="7%"><%= request.getParameter("va_proc").equals("deposito") ? "Cheque Recibido" : "Cheque Emitido" %></td>
								<td><%= request.getParameter("va_proc").equals("deposito") ? "Cuenta Origen" : "Cuenta Destino" %></td>
								<td width="25%">Banco Nacional</td>
								<td width="25%">Banco Extranjero</td>
							  </tr>
							  <tr> 
								<td><select style="width: 90%;" name="metpagopol" class="cpoBco"<%= ((request.getParameter("va_proc").equals("retiro")) ? " onChange=\"javascript:establecerCheque(this.form.bancaj, this.form.depchq, this.form.metpagopol);\"" : " onChange=\"javascript:establecerRefer(this.form.bancaj, this.form.depchq, this.form.metpagopol);\"") %>>
		<%
				for(int i = 0; i < setMet.getNumRows(); i++)
				{	
		%>
									<option value="<%=setMet.getAbsRow(i).getClave()%>"><%= setMet.getAbsRow(i).getNombre() %></option>
		<%	
				}
		%>				
									</select></td>
								<td><input name="depchq" type="text" id="depchq" style="width: 90%;" maxlength="25" value="0"<%= request.getParameter("va_proc").equals("deposito") ? "" : " readOnly='true'" %>></td>
								<td><input name="cuentabanco" type="text" id="cuentabanco" style="width: 90%;" maxlength="50"></td>
								<td><select style="width: 90%;" name="id_satbanco" class="cpoBco">
		<%
				for(int i = 0; i < setBan.getNumRows(); i++)
				{	
		%>
									<option value="<%=setBan.getAbsRow(i).getClave()%>"><%= setBan.getAbsRow(i).getNombre() %></option>
									<%	
				}
		%>
								  </select>
								</td>
								<td><input name="bancoext" type="text" id="bancoext" style="width: 90%;" maxlength="150"></td>
							  </tr>
							 </table>
					 
                        </td>
                      </tr>
                    </table></td>
                </tr>
                <tr> 
                  <td align="right">
				  	<input type="button" name="aceptar" onClick="javascript:transferirResultados();" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
    				<input type="button" name="cancelar" onClick="javascript:window.close()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
				  </td>
                </tr>
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
