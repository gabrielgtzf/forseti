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
<%@ page import="forseti.*, forseti.sets.*, forseti.caja_y_bancos.*, java.util.*, java.io.*"%>
<%
	String mov_bancaj_dlg = (String)request.getAttribute("mov_bancaj_dlg");
	if(mov_bancaj_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  ( mov_bancaj_dlg.equals("BANCOS") ? 
						JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").generarTitulo(JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA",request.getParameter("proceso"),3)) :
						JUtil.getSesion(request).getSesion("BANCAJ_CAJAS").generarTitulo(JUtil.Msj("CEF","BANCAJ_CAJAS","VISTA",request.getParameter("proceso"),3)) );

	session = request.getSession(true);
    JMovBancariosSes pol = ( mov_bancaj_dlg.equals("BANCOS") ? 
								(JMovBancariosSes)session.getAttribute("mov_bancarios_dlg") :
								(JMovBancariosSes)session.getAttribute("mov_cajas_dlg") );

	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();
	
	JPublicBancosCuentasSetV2 bc = new JPublicBancosCuentasSetV2(request);
	bc.m_OrderBy = "Clave ASC";
	bc.m_Where = ( mov_bancaj_dlg.equals("BANCOS") ? 
					"Tipo = '0' and Clave <> '" + JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "'" :
					"Tipo = '0'" );
	bc.Open();
	
	JPublicBancosCuentasSetV2 cc = new JPublicBancosCuentasSetV2(request);
	cc.m_OrderBy = "Clave ASC";
	cc.m_Where = ( mov_bancaj_dlg.equals("CAJAS") ? 
					"Tipo = '1' and Clave <> '" + JUtil.getSesion(request).getSesion("BANCAJ_CAJAS").getEspecial() + "'" :
					"Tipo = '1'" );
	cc.Open();

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
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(formAct.subproceso.value == "ENVIAR")
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
	else
		return true;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/<%= ( mov_bancaj_dlg.equals("BANCOS") ? "CEFMovBancariosDlg" : "CEFMovCajaDlg" ) %>" method="post" enctype="application/x-www-form-urlencoded" name="mov_bancaj_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<%= ( mov_bancaj_dlg.equals("BANCOS") ? "CEFMovBancariosCtrl" : "CEFMovCajaCtrl" ) %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
        <table width="100%" border="0" align="center" cellpadding="1" cellspacing="0">
          <tr> 
            <td colspan="8" align="right"> 
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
				<tr> 
                  <td width="10%" align="left" valign="top">
				  	<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="modulo" type="hidden" value="<%= mov_bancaj_dlg %>"> 
					<input type="hidden" name="subproceso" value="ENVIAR"> 
					<input type="hidden" name="ID" value="<%= request.getParameter("ID") %>"> 
                    <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
				  	<%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
                  <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="20%"><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(pol.getFecha(), "dd/MMM/yyyy") %>"> 
                          <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
                        <td width="20%"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO") %>&nbsp;</td>
                              <td><input name="numero" type="text" id="numero" size="5" maxlength="15" readonly="true" value="<%= pol.getNum() %>"> 
                              </td>
                            </tr>
                          </table></td>
                        <td width="30%"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr> 
                              <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %>&nbsp;</td>
                              <td><input name="cantidad" type="text" id="cantidad" size="15" maxlength="50" value="<%= pol.getCantidad() %>"></td>
                            </tr>
                          </table></td>
                        <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %>&nbsp;</td>
                              <td><input name="ref" type="text" id="ref" size="15" maxlength="50" value="<%= pol.getRef() %>"></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table></td>
                </tr>
				<tr> 
                  <td width="10%" align="left" class="titChicoAzc"><%= pol.getTipoPrimario() %></td>
                  <td>
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
					  <tr> 
                        <td width="25%">Método</td>
						<td width="7%">Cheque</td>
						<td>Cuenta</td>
						<td width="25%">Banco Nacional</td>
						<td width="25%">Banco Extranjero</td>
					  </tr>
					  <tr> 
                        <td><select style="width: 90%;" name="metpagopol" class="cpoBco">
<%
		for(int i = 0; i < setMet.getNumRows(); i++)
		{	
%>
							<option value="<%=setMet.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("metpagopol") != null) {
										if(request.getParameter("metpagopol").equals(setMet.getAbsRow(i).getClave())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(pol.getMetPagoPol().equals(setMet.getAbsRow(i).getClave())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= setMet.getAbsRow(i).getNombre() %></option>
<%	
		}
%>				
							</select></td>
						<td><input name="depchq" type="text" id="depchq" style="width: 90%;" maxlength="25" value="<%= pol.getSigCheque() %>" readOnly="true"></td>
						<td><input name="cuentabanco" type="text" id="cuentabanco" style="width: 90%;" maxlength="50" value="<%= pol.getCuentaBanco() %>"></td>
						<td><select style="width: 90%;" name="id_satbanco" class="cpoBco">
<%
		for(int i = 0; i < setBan.getNumRows(); i++)
		{	
%>
                            <option value="<%=setBan.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("id_satbanco") != null) {
										if(request.getParameter("id_satbanco").equals(setBan.getAbsRow(i).getClave())) {
											out.print(" selected");
										}
									 } else {
										if(pol.getID_SatBanco().equals(setBan.getAbsRow(i).getClave())) {
												out.println(" selected"); 
											}
									 }
									 %>><%= setBan.getAbsRow(i).getNombre() %></option>
                            <%	
		}
%>
                          </select>
						</td>
						<td><input name="bancoext" type="text" id="bancoext" style="width: 90%;" maxlength="150" value="<%= pol.getBancoExt() %>"></td>
					  </tr>
					 </table>
				  </td>
				</tr>
                <tr> 
                  <td width="10%" align="left" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","BENEFICIARIO") %></td>
                  <td> 
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td><input name="beneficiario" type="text" id="beneficiario" size="80" maxlength="80" value="<%= pol.getBeneficiario() %>"></td>
                        <td width="10%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","RFC") %></td>
						<td width="20%"><input name="rfc" type="text" id="rfc" size="13" maxlength="15" value="<%= pol.getRFC() %>"></td>
						<td width="20%"><input type="checkbox" name="status"<% if(pol.getStatus().equals("T")) { out.print(" checked"); } %>><%= JUtil.Msj("GLB","GLB","GLB","TRANSITO") %></td>
                      </tr>
                    </table>
				   </td>
                </tr>
                <tr> 
                  <td width="10%" align="left" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
                  <td valign="top">
				  	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
							<td width="60%"><textarea name="concepto" cols="50" rows="2" id="textarea"><%= pol.getConcepto() %></textarea>
                			</td>
							
                        <td width="40%" valign="top"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
							  	<tr>
									<td width="30%" align="left" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","DESTINO") %></td>
						        	<td valign="top">
									<select style="width: 90%;" name="bancaj" class="cpoColAzc">
                              		    <option value="BANCOS"<% 
									if(pol.getDestino().equals("BANCOS")) { out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","BANCOS") %> ---</option>
                                  	<%
								  	for(int i = 0; i< bc.getNumRows(); i++)
								  	{
		                            	out.print("<option value=\"FSI_BAN_" + bc.getAbsRow(i).getClave() + "\""); 
										if(pol.getDestino().equals("FSI_BAN_" + Byte.toString(bc.getAbsRow(i).getClave()))) 
								  			{ out.print(" selected>" + bc.getAbsRow(i).getCuenta() + "</option>"); } 
										else { out.print(">" + bc.getAbsRow(i).getCuenta() + "</option>"); } 
								  	}
								  	%>
                                  		<option value="CAJAS"<% 
									if(pol.getDestino().equals("CAJAS")) { out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %> ---</option>
                                 	<%
								  	for(int i = 0; i< cc.getNumRows(); i++)
								  	{
		                            	out.print("<option value=\"FSI_CAJ_" + cc.getAbsRow(i).getClave() + "\""); 
										if(pol.getDestino().equals("FSI_CAJ_" + Byte.toString(cc.getAbsRow(i).getClave()))) 
								  			{ out.print(" selected>" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
										else { out.print(">" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
								  }
								  %>
                    				</select>
									</td>
								</tr>
								<tr>
									<td width="30%" align="left" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","TC") %></td>
						        	<td valign="top"><input name="tcGEN" type="text" id="tcGEN" size="15" maxlength="50" value="<%= pol.getTC() %>"></td>
								</tr>
							  </table>
							</td>
                        </tr>
					</table>
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
