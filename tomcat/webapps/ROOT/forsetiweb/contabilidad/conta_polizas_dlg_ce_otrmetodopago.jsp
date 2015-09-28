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
	String conta_polizas_dlg = (String)request.getAttribute("conta_polizas_dlg");
	if(conta_polizas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
  
	String titulo =  JUtil.getSesion(request).getSesion("CONT_POLIZAS").generarTitulo(JUtil.Msj("CEF","CONT_POLIZAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JContPolizasDetalleCEOtrMetodoPagoSet set = new JContPolizasDetalleCEOtrMetodoPagoSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_CE") )
	{
		set.m_Where = "ID = '" + JUtil.p(request.getParameter("idce").substring(4)) + "'";
    	set.Open();
	}
    
 	JSatMetodosPagoSet setMet = new JSatMetodosPagoSet(request);
    setMet.m_OrderBy = "Clave ASC";
    setMet.Open();
	
	Calendar fecha = GregorianCalendar.getInstance();
	
	JSatMonedasSet setMon = new JSatMonedasSet(request);
    setMon.m_OrderBy = "Nombre ASC";
    setMon.Open();
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
	if(!esNumeroDecimal('<%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %>:', formAct.monto.value, 0, 99999999999999.99, 2))
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
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaPolizasDlg?proceso=CONTABILIDAD_ELECTRONICA&ID=<%= request.getParameter("ID") %>&cancelado=1';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td align="right" width="20%">
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
				<input name="ID" type="hidden" value="<%= request.getParameter("ID")%>">
                <input name="idpart" type="hidden" value="<%= request.getParameter("idpart")%>">
				<input name="idce" type="hidden" value="<%= request.getParameter("idce")%>">
                Metodo de Pago</td>
			<td><select name="metpagopol" class="cpoBco">
<% 				for(int i = 0; i < setMet.getNumRows(); i++)
				{	%>
					<option value="<%=setMet.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("metpagopol") != null && 
										request.getParameter("metpagopol").equals(setMet.getAbsRow(i).getClave()))  {
											out.print(" selected");
									} else if(request.getParameter("proceso").equals("CAMBIAR_CE") && 
										set.getAbsRow(0).getMetPagoPol().equals(setMet.getAbsRow(i).getClave())) { 
											out.print(" selected");
									} %>><%= setMet.getAbsRow(i).getNombre() %></option>
			<%	}
%>				</select></td>
          </tr>
		  <tr>
		    <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
		  	<td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"<% if(request.getParameter("fecha") != null) { out.print(" value=\"" + request.getParameter("fecha") + "\""); }  
				 else if(request.getParameter("proceso").equals("CAMBIAR_CE")) { out.print(" value=\"" + JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy") + "\""); } else { out.println(" value=\"" + JUtil.obtFechaTxt(fecha,"dd/MMM/yyyy") + "\""); } %>><a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
		  </tr>
		  <tr>
		  	<td align="right"><%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %></td>
			<td><input name="monto" type="text" id="monto" size="12" maxlength="20"<% if(request.getParameter("monto") != null) { out.print(" value=\"" + request.getParameter("monto") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_CE")) { out.print(" value=\"" + set.getAbsRow(0).getMonto() + "\""); } else { out.print(""); } %>>
 			</td>
		  </tr>
		  <tr>
		  	<td align="right"><%= JUtil.Msj("GLB","GLB","GLB","MONEDA") %></td>
			<td><select name="moneda" class="cpoBco">
<%
		for(int i = 0; i < setMon.getNumRows(); i++)
		{	
%>
					<option value="<%=setMon.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("moneda") != null) {
										if(request.getParameter("moneda").equals(setMon.getAbsRow(i).getClave())) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_CE")) { 
											if(set.getAbsRow(0).getMoneda().equals(setMon.getAbsRow(i).getClave())) {
												out.print(" selected"); 
											}
										} else {
											if(setMon.getAbsRow(i).getClave().equals("MXN")) {
												out.print(" selected"); 
											}
										}
									 }
									 %>><%= setMon.getAbsRow(i).getNombre() %></option>
<%	
		}
%>				
			</select></td>
		  </tr>
		  <tr>
		  	<td align="right"><%= JUtil.Msj("GLB","GLB","GLB","TC") %></td>
			<td><input name="tipcamb" type="text" id="tipcamb" size="12" maxlength="20"<% if(request.getParameter("tipcamb") != null) { out.print(" value=\"" + request.getParameter("tipcamb") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_CE")) { out.print(" value=\"" + set.getAbsRow(0).getTipCamb() + "\""); } else { out.print(" value=\"1.0\""); } %>>
 			</td>
		  </tr>
		  <tr>
		  	<td align="right"><%= JUtil.Msj("GLB","GLB","GLB","BENEFICIARIO") %></td>
			<td><input name="benef" type="text" id="benef" size="80" maxlength="300"<% if(request.getParameter("benef") != null) { out.print(" value=\"" + request.getParameter("benef") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_CE")) { out.print(" value=\"" + set.getAbsRow(0).getBenef() + "\""); } else { out.print(""); } %>>
 			</td>
		  </tr>
		  <tr>
		  	<td align="right"><%= JUtil.Msj("GLB","GLB","GLB","RFC") %></td>
			<td><input name="rfc" type="text" id="rfc" size="15" maxlength="15"<% if(request.getParameter("rfc") != null) { out.print(" value=\"" + request.getParameter("rfc") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_CE")) { out.print(" value=\"" + set.getAbsRow(0).getRFC() + "\""); } else { out.print(""); } %>>
 			</td>
		  </tr>
        </table>
      </td>
  </tr>
</table>
</form>
</body>
</html>
