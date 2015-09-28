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
	String coletq = JUtil.Msj("CEF","CONT_POLIZAS","DLG","COLUMNAS",1);
	int etq = 1;
	
	session = request.getSession(true);
    JMovBancariosSes pol = ( mov_bancaj_dlg.equals("BANCOS") ? 
								(JMovBancariosSes)session.getAttribute("mov_bancarios_dlg") :
								(JMovBancariosSes)session.getAttribute("mov_cajas_dlg") );

	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();
	
	Calendar fecha = GregorianCalendar.getInstance();

%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker_pm.js" >
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
			!esNumeroDecimal("<%= JUtil.Msj("CEF","CONT_POLIZAS","DLG","TIT-ESP",1) %>", formAct.haber.value, -9999999999, 9999999999, 2) ||
<%
	if(pol.getID_Moneda() == 1)
	{			
%>
			!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","TC") %>", formAct.tc.value, 0, 9999999999, 4) ||
<%
	}
%>
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
	document.mov_bancaj_dlg.cuenta.value = "";
	document.mov_bancaj_dlg.nombre.value = "";
	document.mov_bancaj_dlg.concepto_part.value = "";
<%
	if(pol.getID_Moneda() == 1)
	{
%>
	document.mov_bancaj_dlg.idmoneda.selectedIndex = 0;
	document.mov_bancaj_dlg.tc.value = "1";
<%
	}
%>
	document.mov_bancaj_dlg.debe.value = "0";
	document.mov_bancaj_dlg.haber.value = "0";
}

function editarPartida(idpartida, cuenta, nombre, concepto, idmoneda, tc, debe, haber)
{
	document.mov_bancaj_dlg.idpartida.value = idpartida;
	document.mov_bancaj_dlg.subproceso.value = "EDIT_PART";

	document.mov_bancaj_dlg.cuenta.value = cuenta;
	document.mov_bancaj_dlg.nombre.value = nombre;
	document.mov_bancaj_dlg.concepto_part.value = concepto;
<%
	if(pol.getID_Moneda() == 1)
	{
%>	
	document.mov_bancaj_dlg.idmoneda.selectedIndex = idmoneda;
	document.mov_bancaj_dlg.tc.value = tc;
<%
	}
%>	
	document.mov_bancaj_dlg.debe.value = (debe != 0) ? redondear(debe / tc, 2) : 0;
	document.mov_bancaj_dlg.haber.value = (haber != 0) ? redondear(haber / tc, 2) : 0;
}

-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/<%= ( mov_bancaj_dlg.equals("BANCOS") ? "CEFMovBancariosDlg" : "CEFMovCajaDlg" ) %>" method="post" enctype="application/x-www-form-urlencoded" name="mov_bancaj_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#333333">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="left" valign="middle">
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<%= ( mov_bancaj_dlg.equals("BANCOS") ? "CEFMovBancariosCtrl" : "CEFMovCajaCtrl" ) %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
  		  	<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040205.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 	  <td height="150" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td  bgcolor="#FFFFFF"> 
        <table width="100%" border="0" cellpadding="5" cellspacing="5">
          <tr> 
            <td width="20%" align="left"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/> 
                 <input name="modulo" type="hidden" value="<%= mov_bancaj_dlg %>"/> 
                 <input type="hidden" name="subproceso" value="ENVIAR"/> 
					<input type="hidden" name="ID" value="<%= request.getParameter("ID") %>"/> 
                    <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>"/>
                    <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></td>
            <td><input name="tipomov" type="radio" value="deposito"<% if(pol.getTipoMov().equals("deposito"))  { out.print(" checked"); } %>/><%= JUtil.Msj("GLB","GLB","GLB","DEPOSITO") %>&nbsp;
				  		<% 
							if( mov_bancaj_dlg.equals("BANCOS") ) 
							{ 
						%> 
								<input name="tipomov" type="radio" value="cheque"<% if(pol.getTipoMov().equals("cheque"))  { out.print(" checked"); }%>/><%= JUtil.Msj("GLB","GLB","GLB","CHEQUE") %>&nbsp;<%= pol.getSigCheque() %>&nbsp;
						<% 	
							} 
						%>
						<input name="tipomov" type="radio" value="retiro"<% if(pol.getTipoMov().equals("retiro"))  { out.print(" checked"); }%>/><%= JUtil.Msj("GLB","GLB","GLB","RETIRO") %></td>
		  </tr>
       	</table>
		<table width="100%" border="0" cellspacing="5" cellpadding="5">
		  <tr> 
           <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
           <td width="15%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO") %>&nbsp;</td>
           <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %>&nbsp;</td>
           <td width="25%"><%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %>&nbsp;</td>
          </tr>
		  <tr>                                            
            <td><table width="100%"><tr><td><input name="fecha" type="text" id="fecha" style="width:100%" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(pol.getFecha(), "dd/MMM/yyyy") %>"/></td><td width="24"><a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" width="24" height="24" align="absmiddle"></a></td></tr></table></td>
			<td><input name="numero" type="text" id="numero" style="width:100%" maxlength="15" readonly="true" value="<%= pol.getNum() %>"/></td>
            <td><input name="ref" type="text" id="ref" style="width:100%" maxlength="50" value="<%= pol.getRef() %>"/></td>
            <td><input name="cantidad" type="text" id="cantidad" style="width:100%" maxlength="50" value="<%= pol.getCantidad() %>"/></td>
          </tr>
         </table>
		 <table width="100%" border="0" cellspacing="5" cellpadding="5">
		   <tr> 
             <td width="30"><%= JUtil.Msj("GLB","GLB","GLB","BENEFICIARIO") %></td>
             <td><input type="checkbox" name="status"<% if(pol.getStatus().equals("T")) { out.print(" checked"); } %>><%= JUtil.Msj("GLB","GLB","GLB","TRANSITO") %></td>
           </tr>
		   <tr>
		   	 <td colspan="2"><input name="beneficiario" type="text" id="beneficiario" style="width:100%" maxlength="80" value="<%= pol.getBeneficiario() %>"/></td>
		   </tr>
		  </table> 
          <table width="100%" border="0" cellspacing="5" cellpadding="5">
		   <tr>
		     <td width="30"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
			 <td>
<%
		if(pol.getID_Moneda() != 1)
		{		
%>
             <table width="100%">
				<tr>
					<td width="50%">
<% 
				for(int i = 0; i< setMon.getNumRows(); i++)
				{	
					if(setMon.getAbsRow(i).getClave() == pol.getID_Moneda()) 
					{
						out.print(setMon.getAbsRow(i).getMoneda());
						break;
					}
				}
%>
                       <%= JUtil.Msj("GLB","GLB","GLB","TC") %></td>
					<td width="50%" align="center"><input name="tcGEN" type="text" id="tcGEN" style="width:90%" maxlength="50" value="<%= pol.getTC() %>"/></td>
				</tr>
			</table>
<%
		}
		else
		{
			out.print("&nbsp;");
		}
%>		 
			 </td>
           </tr>
		   <tr>   
			 <td colspan="2"><textarea name="concepto" style="width:100%" rows="2" id="textarea"><%= pol.getConcepto() %></textarea></td>
		   </tr>
		  </table> 					
<%
	if( !pol.getFijo() )
	{
%>		  
 		<table width="100%" bgcolor="#0099FF" border="0" align="center" cellpadding="5" cellspacing="5">
		 <tr>
			<td width="36%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td align="32" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="32%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
		 </tr>
		</table>
		<table width="100%" bgcolor="#0099FF" border="0" align="center" cellpadding="5" cellspacing="5">
		 <tr>
		  	<td width="15%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="40%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="15%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="15%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			<td width="15%" align="right" class="titChico">&nbsp;</td>
		  </tr>
	   	</table>
<%
		if( !request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") )
		{
%>

		<table width="100%" border="0" align="center" cellpadding="5" cellspacing="5">
          <tr> 
            <td width="36%" align="left"><table width="100%"><tr><td><input name="cuenta" type="text" id="cuenta" class="cpoBco" style="width:100%" maxlength="25"<% if(request.getParameter("cuenta") != null) { out.println(" value=\"" + request.getParameter("cuenta") + "\""); } %>/></td><td width="24"><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg_pm.jsp?formul=mov_bancaj_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS+CONTABLES&destino=nombre',250,350);"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0" width="24" height="24"/></a></td></tr></table></td>
            <td width="32%" align="left"><input name="nombre" type="text" id="nombre" class="cpoBco" style="width:100%" maxlength="250" readonly="true"<% if(request.getParameter("nombre") != null) { out.println(" value=\"" + request.getParameter("nombre") + "\""); } %>/></td>
            <td width="32%" align="left"> <input name="concepto_part" type="text" id="concepto_part" class="cpoBco" style="width:100%" maxlength="250" <% if(request.getParameter("concepto_part") != null) { out.println(" value=\"" + request.getParameter("concepto_part") + "\""); } %>/></td>
         </tr>
		</table>
		<table width="100%" border="0" align="center" cellpadding="5" cellspacing="5">
          <tr> 
		    <td width="15%" align="right">&nbsp; </td>
            <td width="40%" align="right">
<%
			if(pol.getID_Moneda() == 1)
			{		
%>			
				<table width="100%"><tr><td width="50%">
				  <select name="idmoneda" class="cpoBco" onChange="javascript:establecerTC(this.form.idmoneda, this.form.tc)">
<% 				for(int i = 0; i< setMon.getNumRows(); i++)
				{	%>
					<option value="<%=setMon.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("idmoneda") != null && 
										request.getParameter("idmoneda").equals(Integer.toString(setMon.getAbsRow(i).getClave())))  {
											out.print(" selected");
									} %>><%= setMon.getAbsRow(i).getMoneda() %></option>
<%	
				}
%>				</select></td><td><input name="tc" type="text" id="tc" class="cpoBco" style="width:100%" maxlength="15" <% if(request.getParameter("tc") != null) { out.print(" value=\"" + request.getParameter("tc") + "\""); } else { out.print(" value=\"1\""); } %>/></td></tr></table>
<%
			}
			else
			{
				out.print("&nbsp;");
			}
%>
			</td>
            <td width="15%" align="right"> <input name="debe" type="text" id="debe" class="cpoBco"  style="width:100%" maxlength="15" <% if(request.getParameter("debe") != null) { out.print(" value=\"" + request.getParameter("debe") + "\""); } else { out.print(" value=\"0\""); } %>/></td>
            <td width="15%" align="right"> <input name="haber" type="text" id="haber" class="cpoBco"  style="width:100%" maxlength="15" <% if(request.getParameter("haber") != null) { out.print(" value=\"" + request.getParameter("haber") + "\""); } else { out.print(" value=\"0\""); } %>/></td>
            <td width="15%" align="right"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0" width="24" height="24"/>
              <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="24" height="24" border="0"/></a></td>
          </tr>
		 </table>
<%
		}
		
		if(pol.numPartidas() == 0)
		{
			out.println("<table width=\"100%\"><tr><td align=\"center\" class=\"titChicoAzc\">" + JUtil.Msj("GLB","GLB","DLG","CERO-PART") + "</td></tr></table>");
		}
		else
		{						
			for(int i = 0; i < pol.numPartidas(); i++)
			{
				
%>
        <table width="100%" border="0" align="center" cellpadding="5" cellspacing="5">
          <tr valign="top"> 
            <td width="36%" align="left"><%= JUtil.obtCuentaFormato(new StringBuffer(pol.getPartida(i).getCuenta()), request) %></td>
            <td width="32%" align="left"><%= pol.getPartida(i).getNombre() %></td>
            <td width="32%" align="left"><%= pol.getPartida(i).getConcepto() %></td>
          </tr>
		</table>
		<table width="100%" border="0" align="center" cellpadding="5" cellspacing="5">
           <tr valign="top"> 
              <td width="15%" align="right"><%= pol.getPartida(i).getParcial() %></td>
      		  <td width="40%" align="right"><%= pol.getPartida(i).getMoneda() + " TC: " + pol.getPartida(i).getTC() %></td>
            <td width="15%" align="right"><%= pol.getPartida(i).getDebe() %></td>
            <td width="15%" align="right"><%= pol.getPartida(i).getHaber() %></td>
            <td width="15" align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO")) { %><a href="javascript:editarPartida('<%= i %>','<%= JUtil.obtCuentaFormato(new StringBuffer(pol.getPartida(i).getCuenta()), request) %>','<%= pol.getPartida(i).getNombre() %>','<%= pol.getPartida(i).getConcepto() %>',<%= pol.getPartida(i).getID_Moneda() - 1 %>,'<%= pol.getPartida(i).getTC() %>','<%= pol.getPartida(i).getDebe() %>','<%= pol.getPartida(i).getHaber() %>');"><img src="../../imgfsi/lista_ed.gif" width="24" height="24" border="0"></a>
              <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" width="24" height="24" border="0"><% } else { out.print("&nbsp;"); } %></td>
          </tr>
		 </table>
<%
			}	
%>
        <table width="100%" border="0" align="center" cellpadding="5" cellspacing="0">
          <tr valign="top"> 
            <td width="15%" align="right">&nbsp;</td>
            <td width="40%" align="right">&nbsp;</td>
            <td width="15%" align="right" class="titChicoAzc"><%= pol.getTotalDebe() %></td>
            <td width="15%" align="right" class="titChicoAzc"><%= pol.getTotalHaber() %></td>
            <td width="15%" align="right">&nbsp;</td>
          </tr>
		</table>
  <%			
		}
	}
%>
      </td>
  </tr>
</table>
</form>
</body>
</html>
