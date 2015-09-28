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
<%@ page import="forseti.*, forseti.sets.*, forseti.almacen.*, java.util.*, java.io.*"%>
<%
	String vales_caja_dlg = (String)request.getAttribute("vales_caja_dlg");
	if(vales_caja_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarTitulo(JUtil.Msj("CEF","BANCAJ_VALES","VISTA",request.getParameter("proceso"),3));

	JCajasValesSetV2 smod = new JCajasValesSetV2(request);
	if(request.getParameter("proceso").equals("CAMBIAR_VALE") )
	{
		smod.m_Where = "ID_Vale = '" + JUtil.p(request.getParameter("ID")) + "'";
		smod.Open();
	}
	
	Calendar fecha = GregorianCalendar.getInstance();
	
	String Tipos = JUtil.Msj("CEF","BANCAJ_VALES","DLG","TIPOS");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(	!esNumeroDecimal("Total:", formAct.total.value, 0.01, 9999999999, 2) ||
		!esCadena("Concepto:", formAct.concepto.value, 1, 80) )
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFValesCajaDlg" method="post" enctype="application/x-www-form-urlencoded" name="vales_caja_dlg" target="_self">
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
        			<input type="button" name="cancelar" onclick="javascript:document.location.href='/servlet/CEFValesCajaCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	   <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td>
			   <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%">
				  			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
							<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR">
							<input name="ID" type="hidden" id="ID" value="<%= request.getParameter("ID")%>">
                   <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %> </td>
				  <td width="20%">
				  <select name="idtipo">
                <option value="P"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("P")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("P")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,1) %></option>
                <option value="F"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("F")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("F")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,2) %></option>
                <option value="A"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("A")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("A")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,3) %></option>
                <option value="G"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("G")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("G")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,4) %></option>
                <option value="C"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("C")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("C")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,5) %></option>
               <option value="T"<% if(request.getParameter("idtipo") != null) {
										if(request.getParameter("idtipo").equals("T")) {
											out.println(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_VALE")) { 
											if(smod.getAbsRow(0).getID_Tipo().equals("T")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(Tipos,6) %></option>
                	</select></td>
                  <td>
				     <table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr> 
                        <td width="35%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
                        <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
                          <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
					  </tr>
                    </table>
					</td>
                </tr>
                <tr> 
                  <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
                  <td colspan="2"  class="titChico">
                    <input name="idgasto" type="text" id="idgasto" size="10" maxlength="20">
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=vales_caja_dlg&lista=idgasto&idcatalogo=15&nombre=GASTOS&destino=idgasto_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
                    <input name="idgasto_nombre" type="text" id="idgasto_nombre"  size="40" maxlength="250" readonly="true">
					</td>
                </tr>
                <tr> 
                  <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
                  <td colspan="2"><input name="concepto" type="text" id="iconcepto"  size="60" maxlength="80"></td>
                </tr>
                <tr> 
				  <td colspan="3" align="right">
					<table width="50%" border="0" cellspacing="0" cellpadding="2">
                      <tr>                  
					   	<td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %></td>
                  		<td align="right">
							<input name="total" type="text" id="total" size="10" maxlength="15"></td>
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
<script language="JavaScript1.2">
document.vales_caja_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( JUtil.obtFechaTxt( smod.getAbsRow(0).getFecha(), "dd/MMM/yyyy")); } else { out.print( JUtil.obtFechaTxt(fecha,"dd/MMM/yyyy") ); } %>' 
document.vales_caja_dlg.idgasto.value = '<% if(request.getParameter("idgasto") != null) { out.print( request.getParameter("idgasto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getID_Gasto() ); } else { out.print(""); } %>'  
document.vales_caja_dlg.idgasto_nombre.value = '<% if(request.getParameter("idgasto_nombre") != null) { out.print( request.getParameter("idgasto_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.vales_caja_dlg.concepto.value = '<% if(request.getParameter("concepto") != null) { out.print( request.getParameter("concepto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getConcepto() ); } else { out.print(""); } %>' 
document.vales_caja_dlg.total.value = '<% if(request.getParameter("total") != null) { out.print( request.getParameter("total") ); } else if(!request.getParameter("proceso").equals("AGREGAR_VALE")) { out.print( smod.getAbsRow(0).getTotal() ); } else { out.print("0.00"); } %>' 
</script>
</body>
</html>
