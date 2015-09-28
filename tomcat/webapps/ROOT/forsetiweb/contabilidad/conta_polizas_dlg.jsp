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
	
	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();

	JContaPolizasSetV2 set = new JContaPolizasSetV2(request);
	if( request.getParameter("proceso").equals("CAMBIAR_POLIZA") || request.getParameter("proceso").equals("CONSULTAR_POLIZA") )
	{
		set.m_Where = "ID = '" + JUtil.p(request.getParameter("ID")) + "'";
		set.Open();
	}
	
	String Tipos = JUtil.Msj("CEF","CONT_POLIZAS","DLG","TIPOS");
		
	Calendar fecha = GregorianCalendar.getInstance();

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
	if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
	{
		if(	!esNumeroDecimal("<%= JUtil.Msj("CEF","CONT_POLIZAS","DLG","TIT-ESP",1) %>", formAct.debe.value, -9999999999, 9999999999, 2) ||
			!esNumeroDecimal("<%= JUtil.Msj("CEF","CONT_POLIZAS","DLG","TIT-ESP",2) %>", formAct.haber.value, -9999999999, 9999999999, 2) ||
			!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","TC") %>", formAct.tc.value, 0, 9999999999, 4) ||
		   	!verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>", formAct.cuenta.value))
			return false;
		else
			return true;
	}
	else
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

}

function limpiarFormulario()
{
	document.conta_polizas_dlg.cuenta.value = "";
	document.conta_polizas_dlg.nombre.value = "";
	document.conta_polizas_dlg.concepto_part.value = "";
	document.conta_polizas_dlg.idmoneda.selectedIndex = 0;
	document.conta_polizas_dlg.tc.value = "1";
	document.conta_polizas_dlg.debe.value = "0";
	document.conta_polizas_dlg.haber.value = "0";
}

function editarPartida(idpartida, cuenta, nombre, concepto, idmoneda, tc, debe, haber)
{
	document.conta_polizas_dlg.idpartida.value = idpartida;
	document.conta_polizas_dlg.subproceso.value = "EDIT_PART";

	document.conta_polizas_dlg.cuenta.value = cuenta;
	document.conta_polizas_dlg.nombre.value = nombre;
	document.conta_polizas_dlg.concepto_part.value = concepto;
	document.conta_polizas_dlg.idmoneda.selectedIndex = idmoneda;
	document.conta_polizas_dlg.tc.value = tc;
	document.conta_polizas_dlg.debe.value = (debe != 0) ? redondear(debe / tc, 2) : 0;
	document.conta_polizas_dlg.haber.value = (haber != 0) ? redondear(haber / tc, 2) : 0;
}

-->
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_POLIZA")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaPolizasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td colspan="8" align="right"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td width="10%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr> 
                        <td align="right"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                          <input type="hidden" name="subproceso" value="ENVIAR">
						  <input type="hidden" name="ID" value="<%= request.getParameter("ID") %>">
						  <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
                          <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></td>
                      </tr>
                    </table></td>
                  <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="10%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td><select name="idtipo" class="cpoColAzc">
                <option value="DR"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("DR")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { 
											if(set.getAbsRow(0).getTipo().equals("DR")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,1) %></option>
                <option value="IG"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("IG")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { 
											if(set.getAbsRow(0).getTipo().equals("IG")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,2) %></option>
                <option value="EG"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("EG")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { 
											if(set.getAbsRow(0).getTipo().equals("EG")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,3) %></option>
                <option value="AJ"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("AJ")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { 
											if(set.getAbsRow(0).getTipo().equals("AJ")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,4) %></option>
                <option value="PE"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("PE")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { 
											if(set.getAbsRow(0).getTipo().equals("PE")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,5) %></option>
                		</select></td>
                            </tr>
                          </table></td>
                        <td width="30%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
                              <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"<% if(request.getParameter("fecha") != null) { out.println(" value=\"" + request.getParameter("fecha") + "\""); }  
									else if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { out.println(" value=\"" + JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy") + "\""); } else { out.println(" value=\"" + JUtil.obtFechaTxt(fecha,"dd/MMM/yyyy") + "\""); } %>><a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
                            </tr>
                          </table></td>
                        <td width="25%"><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO") %></td>
                              <td><input name="numero" type="text" id="numero" size="5" maxlength="15" readonly="true"<% if(request.getParameter("numero") != null) { out.println(" value=\"" + request.getParameter("numero") + "\""); }  
									else if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { out.println(" value=\"" + set.getAbsRow(0).getNum() + "\""); } %>>
                              </td>
                            </tr>
                          </table></td>
                        <td><table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr> 
                              <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %></td>
                              <td><input name="ref" type="text" id="ref" size="15" maxlength="50" readonly="true"<% if(request.getParameter("ref") != null) { out.println(" value=\"" + request.getParameter("ref") + "\""); }  
									else if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { out.println(" value=\"" + set.getAbsRow(0).getRef() + "\""); } %>></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table></td>
                </tr>
                <tr> 
                  <td width="10%" valign="top"> <table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr> 
                        <td align="right" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
                      </tr>
                    </table></td>
                  <td><table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr> 
                        <td><textarea name="concepto" cols="50" rows="2" id="concepto"><% if(request.getParameter("concepto") != null) { out.print( request.getParameter("concepto") ); }  
								else if(!request.getParameter("proceso").equals("AGREGAR_POLIZA")) { out.print( set.getAbsRow(0).getConcepto() ); } %></textarea></td>
                      </tr>
                    </table></td>
                </tr>
              </table></td>
          </tr>
          <tr bgcolor="#0099FF"> 
            <td width="16%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="20%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="8%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="15%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="8%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="8%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            <td width="40" align="right" class="titChico">&nbsp;</td>
          </tr>
<%
		if( !request.getParameter("proceso").equals("CONSULTAR_POLIZA") )
		{
%>
          <tr valign="top"> 
            <td width="16%" align="left"> <input name="cuenta" type="text" id="cuenta" class="cpoBco" size="11" maxlength="25"<% if(request.getParameter("cuenta") != null) { out.println(" value=\"" + request.getParameter("cuenta") + "\""); } %>><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=conta_polizas_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS+CONTABLES&destino=nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
            <td align="left"><input name="nombre" type="text" id="nombre" class="cpoBco" size="25" maxlength="250" readonly="true"<% if(request.getParameter("nombre") != null) { out.println(" value=\"" + request.getParameter("nombre") + "\""); } %>></td>
            <td width="20%" align="left"> <input name="concepto_part" type="text" id="concepto_part" class="cpoBco" size="25" maxlength="250" <% if(request.getParameter("concepto_part") != null) { out.println(" value=\"" + request.getParameter("concepto_part") + "\""); } %>></td>
            <td width="8%" align="right">&nbsp; </td>
            <td width="15%" align="right"> <select name="idmoneda" class="cpoBco" onChange="javascript:establecerTC(this.form.idmoneda, this.form.tc)">
<% 				for(int i = 0; i< setMon.getNumRows(); i++)
				{	%>
					<option value="<%=setMon.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("idmoneda") != null && 
										request.getParameter("idmoneda").equals(Integer.toString(setMon.getAbsRow(i).getClave())))  {
											out.print(" selected");
									} %>><%= setMon.getAbsRow(i).getMoneda() %></option>
			<%	}
%>				</select> <input name="tc" type="text" id="tc" class="cpoBco" size="5" maxlength="15" <% if(request.getParameter("tc") != null) { out.print(" value=\"" + request.getParameter("tc") + "\""); } else { out.print(" value=\"1\""); } %>></td>
            <td width="8%" align="right"> <input name="debe" type="text" id="debe" class="cpoBco" size="10" maxlength="15" <% if(request.getParameter("debe") != null) { out.print(" value=\"" + request.getParameter("debe") + "\""); } else { out.print(" value=\"0\""); } %>></td>
            <td width="8%" align="right"> <input name="haber" type="text" id="haber" class="cpoBco" size="10" maxlength="15" <% if(request.getParameter("haber") != null) { out.print(" value=\"" + request.getParameter("haber") + "\""); } else { out.print(" value=\"0\""); } %>></td>
            <td width="40" align="right"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" alt="" border="0">
              <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
          </tr>
<%
		}
		
		session = request.getSession(true);
       	JContaPolizasSes pol = (JContaPolizasSes)session.getAttribute("conta_polizas_dlg");
		if(pol.numPartidas() == 0)
		{
			out.println("<tr><td align=\"center\" class=\"titChicoAzc\" colspan=\"8\">" + JUtil.Msj("GLB","GLB","DLG","CERO-PART") + "</td></tr>");
		}
		else
		{						
			for(int i = 0; i < pol.numPartidas(); i++)
			{
%>
          <tr valign="top"> 
            <td width="16%" align="left"><%= JUtil.obtCuentaFormato(new StringBuffer(pol.getPartida(i).getCuenta()), request) %></td>
            <td align="left"><%= pol.getPartida(i).getNombre() %></td>
            <td width="20%" align="left"><%= pol.getPartida(i).getConcepto() %></td>
            <td width="8%" align="right"><%= pol.getPartida(i).getParcial() %></td>
            <td width="15%" align="right"><%= pol.getPartida(i).getMoneda() + " TC: " + pol.getPartida(i).getTC() %></td>
            <td width="8%" align="right"><%= pol.getPartida(i).getDebe() %></td>
            <td width="8%" align="right"><%= pol.getPartida(i).getHaber() %></td>
            <td width="40" align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_POLIZA")) { %>
              <a href="javascript:editarPartida('<%= i %>','<%= JUtil.obtCuentaFormato(new StringBuffer(pol.getPartida(i).getCuenta()), request) %>','<%= pol.getPartida(i).getNombre() %>','<%= pol.getPartida(i).getConcepto() %>',<%= pol.getPartida(i).getID_Moneda() - 1 %>,'<%= pol.getPartida(i).getTC() %>','<%= pol.getPartida(i).getDebe() %>','<%= pol.getPartida(i).getHaber() %>');"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0">
              <% } else { out.print("&nbsp;"); } %></td>
          </tr>
<%
			}	
%>
          <tr valign="top"> 
            <td width="16%" align="left">&nbsp;</td>
            <td align="left">&nbsp;</td>
            <td width="20%" align="left">&nbsp;</td>
            <td width="8%" align="right">&nbsp;</td>
            <td width="15%" align="right">&nbsp;</td>
            <td width="8%" align="right" class="titChicoAzc"><%= pol.getSumDebe() %></td>
            <td width="8%" align="right" class="titChicoAzc"><%= pol.getSumHaber() %></td>
            <td width="40" align="right">&nbsp;</td>
          </tr>
				
<%			
		}
%>
         
        </table>
      </td>
  </tr>
</table>
</form>
</body>
</html>
