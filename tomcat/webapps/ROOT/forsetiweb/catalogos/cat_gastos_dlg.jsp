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
<%@ page import="forseti.*, forseti.sets.*, forseti.catalogos.*, java.util.*, java.io.*"%>
<%
	String cat_gastos_dlg = (String)request.getAttribute("cat_gastos_dlg");
	if(cat_gastos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("INVSERV_GASTOS").generarTitulo(JUtil.Msj("CEF","INVSERV_GASTOS","VISTA",request.getParameter("proceso"),3));
	String etq = JUtil.Msj("CEF","INVSERV_INVSERV","DLG","ETQ");
	String coletq = JUtil.Msj("CEF","INVSERV_INVSERV","DLG","COLUMNAS");
	String sts = JUtil.Msj("CEF", "INVSERV_GASTOS", "VISTA", "STATUS", 2);

	JInvServInvSetV2 smod = new JInvServInvSetV2(request);
	JInvServInvSetMasV2 set = new JInvServInvSetMasV2(request);
	if( request.getParameter("proceso").equals("CAMBIAR_GASTO") || request.getParameter("proceso").equals("CONSULTAR_GASTO") )
	{
		smod.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		set.m_Where = "Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		smod.Open();
		set.Open();
	}

	session = request.getSession(true);
    JCatGastosSes ses = (JCatGastosSes)session.getAttribute("cat_gastos_dlg");
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
	if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
	{
		if( !esNumeroDecimal("<%= JUtil.Elm(coletq,6) %>:", formAct.porcentaje.value, 0, 100, 6) ||
			!verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>:", formAct.cuenta.value) )
			return false;
		else
			return true;
	}
	else if(formAct.subproceso.value == "ENVIAR")
	{
		if(	!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","PRECIO") %>:', formAct.precio.value, 0, 9999999999, 2) ||
			!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","MAX") %>:', formAct.preciomax.value, 0, 9999999999, 2) ||
			!esNumeroDecimal('<%= JUtil.Elm(etq,7) %>:', formAct.deduccioniva.value, 0, 100, 6) || 
			!esNumeroDecimal('<%= JUtil.Elm(etq,6) %>:', formAct.ieps.value, 0, 100, 6) ||
			!esNumeroDecimal('<%= JUtil.Elm(etq,8) %>:', formAct.ivaret.value, 0, 100, 6) ||
			!esNumeroDecimal('<%= JUtil.Elm(etq,9) %>:', formAct.isrret.value, 0, 100, 6) )
			return false;
		else
		{
			if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
			{
				formAct.aceptar.disabled = true;
				return true;
			}
			else
				return false;
		}
	}
	else
		return true;
	
}

function limpiarFormulario()
{
	document.cat_gastos_dlg.cuenta.value = "";
	document.cat_gastos_dlg.nombre.value = "";
	document.cat_gastos_dlg.porcentaje.value = "";
}

function editarPartida(idpartida, cuenta, nombre, porcentaje)
{
	document.cat_gastos_dlg.idpartida.value = idpartida;
	document.cat_gastos_dlg.subproceso.value = "EDIT_PART";

	document.cat_gastos_dlg.cuenta.value = cuenta;
	document.cat_gastos_dlg.nombre.value = nombre;
	document.cat_gastos_dlg.porcentaje.value = porcentaje;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCatGastosDlg" method="post" enctype="application/x-www-form-urlencoded" name="cat_gastos_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_GASTO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCatGastosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="10%">
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>">
				<input type="hidden" name="subproceso" value="ENVIAR">
				<input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
	 			<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>
			</td>
    		<td>
			  <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr>
                  <td width="13%">
					<input name="clave" type="text" size="15" maxlength="20"<% if(request.getParameter("proceso").equals("CAMBIAR_GASTO")) { out.print(" readonly=\"true\""); } %>>
                  </td>
				  <td width="15%"> 
                    <input type="checkbox" name="usointerno">
                    <%= JUtil.Elm(etq, 3) %></td>
                </tr>
              </table>
		</td>
  </tr>
  <tr>
        <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
        <td>
			  <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr>
                  <td><input name="descripcion" type="text" id="descripcion" size="80" maxlength="80"></td>
                </tr>
              </table>
	   	</td>
  </tr>
  <tr>
       	<td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","MAX") %></td>
       	<td><input name="preciomax" type="text" id="preciomax" size="10" maxlength="15"></td>
  </tr>
  <tr>
     <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","LINEA") %></td>
     <td>
	 <table width="100%" border="0" cellspacing="2" cellpadding="0">
	  <tr>
		<td>
			<input name="linea" type="text" id="linea" size="11" maxlength="8"> 
          		<a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=cat_gastos_dlg&lista=linea&idcatalogo=6&nombre=LINEAS&destino=linea_descripcion',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
             		<input name="linea_descripcion" type="text" id="linea_descripcion" size="30" maxlength="250" readonly="true">
     			</td>
                <td width="10%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></td>
                  <td width="20%"> 
                    <select name="status" class="cpoColAzc">
                      <option value="V"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("V")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_GASTO") || request.getParameter("proceso").equals("CONSULTAR_GASTO")) { 
											if(set.getAbsRow(0).getStatus().equals("V")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,2) %></option>
                      <option value="D"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("D")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_GASTO") || request.getParameter("proceso").equals("CONSULTAR_GASTO")) { 
											if(set.getAbsRow(0).getStatus().equals("D")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,3) %></option>
                    </select>
                  </td>
                </tr>
              </table>
		</td>
  </tr>
  <tr>
        <td width="10%" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","OBS") %></td>
    	<td>
			<table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td width="50%" rowspan="3"> <textarea name="obs" cols="50" rows="3"></textarea></td>
                  <td width="10%" align="right" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","IVA") %></td>
                  <td width="40%" valign="top" colspan="3"> <input type="checkbox" name="iva" ><%= JUtil.Elm(etq,4) %></td>
                </tr>
                <tr> 
                  <td width="10%" align="right" valign="top"><%= JUtil.Elm(etq,5) %></td>
                  <td width="15%" valign="top"><input name="deduccioniva" type="text" id="deduccioniva" size="10" maxlength="15"> %</td>
                  <td width="10%" align="right" valign="top"><%= JUtil.Elm(etq,6) %></td>
                  <td width="15%" valign="top"><input name="ieps" type="text" id="ieps" size="10" maxlength="15"> %</td>
			    </tr>
				 <tr> 
                  <td align="right" valign="top"><%= JUtil.Elm(etq,8) %></td>
                  <td valign="top"><input name="ivaret" type="text" id="ivaret" size="10" maxlength="15"> %</td>
                  <td align="right" valign="top"><%= JUtil.Elm(etq,9) %></td>
                  <td valign="top"><input name="isrret" type="text" id="isrret" size="10" maxlength="15"> %</td>
			    </tr>
			 </table>
		</td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="1">
         <tr>
            <td colspan="4">&nbsp;</td>
         </tr>
	     <tr bgcolor="#0099FF">
            <td width="20%" class="titChico"><%= JUtil.Elm(coletq,4) %></td>
            <td width="50%" class="titChico"><%= JUtil.Elm(coletq,5) %></td>
            <td width="15%" align="right" class="titChico"><%= JUtil.Elm(coletq,6) %></td>
			<td>&nbsp;</td>
          </tr>
<%
		if( !request.getParameter("proceso").equals("CONSULTAR_GASTO") )
		{
%>
          <tr valign="top"> 
            <td><input name="cuenta" type="text" id="cuenta" class="cpoBco" size="11" maxlength="25"<% if(request.getParameter("cuenta") != null) { out.println(" value=\"" + request.getParameter("cuenta") + "\""); } %>><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=cat_gastos_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS+CONTABLES&destino=nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
            <td><input name="nombre" type="text" id="nombre" class="cpoBco" size="25" maxlength="250" readonly="true"<% if(request.getParameter("nombre") != null) { out.println(" value=\"" + request.getParameter("nombre") + "\""); } %>></td>
            <td align="right"> <input name="porcentaje" type="text" id="porcentaje" class="cpoBco" size="10" maxlength="15" <% if(request.getParameter("porcentaje") != null) { out.print(" value=\"" + request.getParameter("porcentaje") + "\""); } else { out.print(" value=\"0\""); } %>></td>
            <td align="right"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" alt="" border="0">
              <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
          </tr>
<%
		}
		
		if(ses.numPartidas() == 0)
		{
			out.println("<tr><td align=\"center\" class=\"titChicoAzc\" colspan=\"4\">" + JUtil.Msj("GLB","GLB","DLG","CERO-PART") + "</td></tr>");
		}
		else
		{						
			for(int i = 0; i < ses.numPartidas(); i++)
			{
%>
          <tr valign="top"> 
            <td><%= JUtil.obtCuentaFormato(new StringBuffer(ses.getPartida(i).getCuenta()), request) %></td>
            <td><%= ses.getPartida(i).getNombre() %></td>
            <td align="right"><%= ses.getPartida(i).getPorcentaje() %></td>
            <td align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_GASTO")) { %>
              <a href="javascript:editarPartida('<%= i %>','<%= JUtil.obtCuentaFormato(new StringBuffer(ses.getPartida(i).getCuenta()), request) %>','<%= ses.getPartida(i).getNombre() %>','<%= ses.getPartida(i).getPorcentaje() %>');"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0">
              <% } else { out.print("&nbsp;"); } %></td>
          </tr>
<%
			}	
%>
		  <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td align="right" class="titChicoAzc"><%= ses.getTotalDeduccion() %></td>
            <td>&nbsp;</td>
          </tr>
<%
		}	
%>
		</table>
     </td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="1">
         <tr>
            <td colspan="3">&nbsp;</td>
         </tr>
	     <tr>
            <td width="70%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","ENTIDAD") %></td>
            <td width="30%" align="right" class="titChicoAzc">Manejo</td>
          </tr>
<%
		if(ses.numObjetos() == 0)
		{
			out.println("<tr><td align=\"center\" colspan=\"2\">" + JUtil.Msj("GLB","GLB","DLG","CERO-PART") + "</td></tr>");
		}
		else
		{						
			for(int i = 0; i < ses.numObjetos(); i++)
			{
				int mod = i % 2;	
%>
          <tr>
            <td width="70%"><%= ses.getObjeto(i).getNombre() %></td>
            <td width="30%" align="right">
				<input name="FSI_MAN_<%= ses.getObjeto(i).getID_Bodega() %>" type="radio" value="1"<% if(request.getParameter("FSI_MAN_" + ses.getObjeto(i).getID_Bodega()) == null) { if(ses.getObjeto(i).getStockMin() == 0.00) out.print(" checked"); } else { if(request.getParameter("FSI_MAN_" + ses.getObjeto(i).getID_Bodega()).equals("1")) out.print(" checked"); } %>>SI&nbsp;&nbsp;
				<input name="FSI_MAN_<%= ses.getObjeto(i).getID_Bodega() %>" type="radio" value="0"<% if(request.getParameter("FSI_MAN_" + ses.getObjeto(i).getID_Bodega()) == null) { if(ses.getObjeto(i).getStockMin() != 0.00) out.print(" checked"); } else { if(request.getParameter("FSI_MAN_" + ses.getObjeto(i).getID_Bodega()).equals("0")) out.print(" checked"); } %>>NO
			</td>
		 </tr>
<%
			}
		}
%>			
        </table>
		</td>
	</tr>
</table>
</form>
<script language="JavaScript1.2">
document.cat_gastos_dlg.iva.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_GASTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_GASTO") ) { out.print( (set.getAbsRow(0).getIVA() ? "true" : "false" ) ); } else if(request.getParameter("iva") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.cat_gastos_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( smod.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.cat_gastos_dlg.clave.value = '<% if(request.getParameter("clave") != null) { out.print( request.getParameter("clave") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( smod.getAbsRow(0).getClave() ); } else { out.print(""); } %>' 
document.cat_gastos_dlg.linea_descripcion.value = '<% if(request.getParameter("linea_descripcion") != null) { out.print( request.getParameter("linea_descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( set.getAbsRow(0).getLineaDescripcion() ); } else { out.print(""); } %>' 
document.cat_gastos_dlg.linea.value = '<% if(request.getParameter("linea") != null) { out.print( request.getParameter("linea") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( smod.getAbsRow(0).getLinea() ); } else { out.print(""); } %>'  
document.cat_gastos_dlg.obs.value = '<% if(request.getParameter("obs") != null) { out.print( request.getParameter("obs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( set.getAbsRow(0).getObs() ); } else { out.print(""); } %>'
document.cat_gastos_dlg.usointerno.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_GASTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_GASTO") ) { out.print( (set.getAbsRow(0).getNoSeVende() ? "true" : "false" ) ); } else if(request.getParameter("usointerno") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.cat_gastos_dlg.ieps.value = <% if(request.getParameter("ieps") != null) { out.print( request.getParameter("ieps") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( set.getAbsRow(0).getImpIEPS() ); } else { out.print("0"); } %> 
document.cat_gastos_dlg.ivaret.value = <% if(request.getParameter("ivaret") != null) { out.print( request.getParameter("ivaret") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( set.getAbsRow(0).getImpIVARet() ); } else { out.print("0"); } %> 
document.cat_gastos_dlg.isrret.value = <% if(request.getParameter("isrret") != null) { out.print( request.getParameter("isrret") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( set.getAbsRow(0).getImpISRRet() ); } else { out.print("0"); } %> 
document.cat_gastos_dlg.deduccioniva.value = <% if(request.getParameter("deduccioniva") != null) { out.print( request.getParameter("deduccioniva") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( set.getAbsRow(0).getIVA_Deducible() ); } else { out.print("100"); } %> 
document.cat_gastos_dlg.preciomax.value = <% if(request.getParameter("preciomax") != null) { out.print( request.getParameter("preciomax") ); } else if(!request.getParameter("proceso").equals("AGREGAR_GASTO")) { out.print( set.getAbsRow(0).getPrecioMax() ); } else { out.print("0.00"); } %> 
</script>
</body>
</html>
