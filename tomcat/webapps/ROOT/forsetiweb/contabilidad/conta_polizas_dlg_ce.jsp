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
<%@ page import="forseti.*, forseti.sets.*, forseti.contabilidad.*, java.util.*, java.io.*"%>
<%
	String conta_polizas_dlg = (String)request.getAttribute("conta_polizas_dlg");
	if(conta_polizas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
  
	String titulo = JUtil.getSesion(request).getSesion("CONT_POLIZAS").generarTitulo(JUtil.Msj("CEF","CONT_POLIZAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	String coletq = JUtil.Msj("CEF","CONT_POLIZAS","DLG","COLUMNAS",1);
	int etq = 1;
	
	JContaPolizasSetV2 set = new JContaPolizasSetV2(request);
	set.m_Where = "ID = '" + JUtil.p(request.getParameter("ID")) + "'";
	set.Open();
	JContaPolizasDetalleCESet pol = new JContaPolizasDetalleCESet(request);
	pol.m_Where = "ID = '" + JUtil.p(request.getParameter("ID")) + "'";
	pol.m_OrderBy = "Part ASC";
	pol.Open();
	
	String Tipos = JUtil.Msj("CEF","CONT_POLIZAS","DLG","TIPOS");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFContaPolizasDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_polizas_dlg" target="_self">
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
              <input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        	  <input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaPolizasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table> 
      </td>
    </tr>
	<tr> 
     <td bgcolor="#333333"> 	
	   <table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right">
			  <!--input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CARGAR_XML',<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CARGAR_XML",4) %>,<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CARGAR_XML",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CARGAR_XML") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CARGAR_XML",2) %>" border="0">
   			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ENLAZAR_XML',<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ENLAZAR_XML",4) %>,<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ENLAZAR_XML",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ENLAZAR_XML") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ENLAZAR_XML",2) %>" border="0"-->
   			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'COMPROBANTE_CE',<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","COMPROBANTE_CE",4) %>,<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","COMPROBANTE_CE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","COMPROBANTE_CE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","COMPROBANTE_CE",2) %>" border="0">
      		  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CHEQUE_CE',<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CHEQUE_CE",4) %>,<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CHEQUE_CE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CHEQUE_CE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CHEQUE_CE",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'TRANSFERENCIA_CE',<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","TRANSFERENCIA_CE",4) %>,<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","TRANSFERENCIA_CE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","TRANSFERENCIA_CE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","TRANSFERENCIA_CE",2) %>" border="0">
      		  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'OTRMETPAGO_CE',<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","OTRMETPAGO_CE",4) %>,<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","OTRMETPAGO_CE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","OTRMETPAGO_CE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","OTRMETPAGO_CE",2) %>" border="0">
      		  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_CE',<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CAMBIAR_CE",4) %>,<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CAMBIAR_CE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CAMBIAR_CE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CAMBIAR_CE",2) %>" border="0">
      		  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_CE'); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ELIMINAR_CE") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_POLIZAS","VISTA","ELIMINAR_CE",2) %>" border="0">
              <a href="/servlet/CEFContaPolizasDlg?proceso=CONTABILIDAD_ELECTRONICA&ID=<%= request.getParameter("ID") %>" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
              <a href="../../forsetidoc/040203.html" target="_blank"><img src="../imgfsi/ayuda32Cef.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",3) %>" border="0"></a> 
    		 </div>
			  </td>
  			</tr>
		</table>
	</td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="149" bgcolor="#333333">&nbsp;</td>
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
            <td colspan="8" align="right"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td width="10%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr> 
                        <td> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                           <input type="hidden" name="ID" value="<%= request.getParameter("ID") %>">
						  <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
                          <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></td>
                      </tr>
                    </table></td>
                  <td> 
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="10%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td class="titChicoAzc"><% if(set.getAbsRow(0).getTipo().equals("DR")) { 
							  			out.print(JUtil.Elm(Tipos,1));  
									} else if(set.getAbsRow(0).getTipo().equals("IG")) {
										out.print(JUtil.Elm(Tipos,2));
									} else if(set.getAbsRow(0).getTipo().equals("EG")) {
										out.print(JUtil.Elm(Tipos,3));
									} else if(set.getAbsRow(0).getTipo().equals("AJ")) {
										out.print(JUtil.Elm(Tipos,4));
									} else if(set.getAbsRow(0).getTipo().equals("PE")) {
										out.print(JUtil.Elm(Tipos,5));
									} %>
							  </td>
                            </tr>
                          </table>
						</td>
                        <td width="30%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
                              <td class="titChicoAzc"><%= JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy") %></td>
                            </tr>
                          </table></td>
                        <td width="25%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO") %></td>
                              <td class="titChicoAzc"><%= set.getAbsRow(0).getNum() %></td>
                            </tr>
                          </table></td>
                        <td><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %></td>
                              <td class="titChicoAzc"><%= set.getAbsRow(0).getRef() %></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table></td>
                </tr>
                <tr> 
                  <td width="10%" valign="top"> <table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr> 
                        <td><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
                      </tr>
                    </table></td>
                  <td><table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr> 
                        <td class="titChicoAzc"><%= set.getAbsRow(0).getConcepto() %></td>
                      </tr>
                    </table></td>
                </tr>
              </table></td>
          </tr>
          <tr bgcolor="#0099FF">
		    <td width="3%" align="center">&nbsp;</td> 
            <td width="15%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="20%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="8%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="10%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="8%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="8%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="5%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
          </tr>
<%
			for(int i = 0; i < pol.getNumRows(); i++)
			{
%>
          <tr> 
            <td width="3%" align="center"><input type="radio" name="idpart" value="<%= pol.getAbsRow(i).getPart() %>"></td>
			<td width="16%" align="left"><%= JUtil.obtCuentaFormato(new StringBuffer(pol.getAbsRow(i).getNumero()), request) %></td>
            <td align="left"><%= pol.getAbsRow(i).getNombre() %></td>
            <td width="20%" align="left"><%= pol.getAbsRow(i).getConcepto() %></td>
            <td width="8%" align="right"><%= pol.getAbsRow(i).getParcial() %></td>
            <td width="10%" align="right"><%= pol.getAbsRow(i).getMoneda() + " TC: " + pol.getAbsRow(i).getTC() %></td>
            <td width="8%" align="right"><%= pol.getAbsRow(i).getDebe() %></td>
            <td width="8%" align="right"><%= pol.getAbsRow(i).getHaber() %></td>
            <td width="5%" align="center"><%= pol.getAbsRow(i).getCE() %></td>
		  </tr>
<%
				JContPolizasDetalleCEChequesSet chq = new JContPolizasDetalleCEChequesSet(request);
				chq.m_Where = "ID_Pol = '" + JUtil.p(request.getParameter("ID")) + "' and ID_Part = '" + pol.getAbsRow(i).getPart() + "'";
				chq.Open();
				
				for(int j = 0; j < chq.getNumRows(); j++)
				{
%>
		  <tr>
		  	<td width="3%" align="center"><input type="radio" name="idce" value="CHQ_<%= chq.getAbsRow(j).getID() %>"></td>
		  	<td colspan="8">
				<table width="65%" border="0">
					<td class="txtChicoAzc">CHQ:</td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getNum() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getCtaOri() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getBanco() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getBanEmisExt() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getFecha() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getMonto() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getMoneda() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getTipCamb() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getBenef() %></td>
					<td class="txtChicoAzc"><%= chq.getAbsRow(j).getRFC() %></td>
				</table>
			</td>
		  </tr>			
<%
				}
				
				JContPolizasDetalleCETransferenciasSet trn = new JContPolizasDetalleCETransferenciasSet(request);
				trn.m_Where = "ID_Pol = '" + JUtil.p(request.getParameter("ID")) + "' and ID_Part = '" + pol.getAbsRow(i).getPart() + "'";
				trn.Open();
				
				for(int j = 0; j < trn.getNumRows(); j++)
				{
%>
		  <tr>
		  	<td width="3%" align="center"><input type="radio" name="idce" value="TRN_<%= trn.getAbsRow(j).getID() %>"></td>
		  	<td colspan="8">
				<table width="70%" border="0">
					<td class="txtChicoAzc">TRN:</td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getCtaOri() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getBancoOri() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getBancoOriExt() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getMonto() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getMoneda() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getTipCamb() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getCtaDest() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getBancoDest() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getBancoDestExt() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getFecha() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getBenef() %></td>
					<td class="txtChicoAzc"><%= trn.getAbsRow(j).getRFC() %></td>
				</table>
			</td>
		  </tr>			
<%
				}

				JContPolizasDetalleCEOtrMetodoPagoSet omp = new JContPolizasDetalleCEOtrMetodoPagoSet(request);
				omp.m_Where = "ID_Pol = '" + JUtil.p(request.getParameter("ID")) + "' and ID_Part = '" + pol.getAbsRow(i).getPart() + "'";
				omp.Open();
				
				for(int j = 0; j < omp.getNumRows(); j++)
				{
%>
		  <tr>
		  	<td width="3%" align="center"><input type="radio" name="idce" value="OMP_<%= omp.getAbsRow(j).getID() %>"></td>
		  	<td colspan="8">
				<table width="65%" border="0">
					<td class="txtChicoAzc">OMP:</td>
					<td class="txtChicoAzc"><%= omp.getAbsRow(j).getMetPagoPol() %></td>
					<td class="txtChicoAzc"><%= omp.getAbsRow(j).getFecha() %></td>
					<td class="txtChicoAzc"><%= omp.getAbsRow(j).getMonto() %></td>
					<td class="txtChicoAzc"><%= omp.getAbsRow(j).getMoneda() %></td>
					<td class="txtChicoAzc"><%= omp.getAbsRow(j).getTipCamb() %></td>
					<td class="txtChicoAzc"><%= omp.getAbsRow(j).getBenef() %></td>
					<td class="txtChicoAzc"><%= omp.getAbsRow(j).getRFC() %></td>
				</table>
			</td>
		  </tr>			
<%
				}
				
				JContPolizasDetalleCEComprobantesSet xml = new JContPolizasDetalleCEComprobantesSet(request);
				xml.m_Where = "ID_Pol = '" + JUtil.p(request.getParameter("ID")) + "' and ID_Part = '" + pol.getAbsRow(i).getPart() + "'";
				xml.Open();
				
				for(int j = 0; j < xml.getNumRows(); j++)
				{
					if(xml.getAbsRow(j).getID_Tipo().equals("CompNal"))
					{
%>
		  <tr>
		  	<td width="3%" align="center"><input type="radio" name="idce" value="XML_<%= xml.getAbsRow(j).getID() %>"></td>
		  	<td colspan="8">
				<table width="70%" border="0">
					<td class="txtChicoAzc">XML:</td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getUUID_CFDI() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getRFC() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getMonto() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getMoneda() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getTipCamb() %></td>
				</table>
			</td>
		  </tr>			
<%
					}			
					else if(xml.getAbsRow(j).getID_Tipo().equals("CompNalOtr"))
					{
%>
		  <tr>
		  	<td width="3%" align="center"><input type="radio" name="idce" value="CBB_<%= xml.getAbsRow(j).getID() %>"></td>
		  	<td colspan="8">
				<table width="70%" border="0">
					<td class="txtChicoAzc">CBB:</td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getCFD_CBB_Serie() %> / <%= xml.getAbsRow(j).getCFD_CBB_NumFol() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getRFC() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getMonto() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getMoneda() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getTipCamb() %></td>
				</table>
			</td>
		  </tr>			
<%
					}
					else //CompExt
					{
%>
		  <tr>
		  	<td width="3%" align="center"><input type="radio" name="idce" value="EXT_<%= xml.getAbsRow(j).getID() %>"></td>
		  	<td colspan="8">
				<table width="70%" border="0">
					<td class="txtChicoAzc">EXT:</td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getNumFactExt() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getTAXID() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getMonto() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getMoneda() %></td>
					<td class="txtChicoAzc"><%= xml.getAbsRow(j).getTipCamb() %></td>
				</table>
			</td>
		  </tr>			
<%
					}
				}				
			}	
%>
	   </table>
      </td>
  </tr>
</table>
</form>
</body>
</html>
