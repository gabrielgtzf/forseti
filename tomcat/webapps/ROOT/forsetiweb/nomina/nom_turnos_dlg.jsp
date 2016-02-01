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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*" %>
<%
	String nom_turnos_dlg = (String)request.getAttribute("nom_turnos_dlg");
	if(nom_turnos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_TURNOS").generarTitulo(JUtil.Msj("CEF","NOM_TURNOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JTurnosSetCons set = new JTurnosSetCons(request);
	if( request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO") )
	{
		set.m_Where = "ID_Turno = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/ceftimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(formAct.proceso.value == "AGREGAR_TURNO" || formAct.proceso.value == "CAMBIAR_TURNO")
	{
		if( !esNumeroEntero('Clave:', formAct.id_turno.value, 1, 254)  ||
			!esNumeroDecimal('Horas reales lunes: ', formAct.hnalunes.value, 0, 99.99, 2)  ||
			!esNumeroDecimal('Horas virtuales lunes: ', formAct.healunes.value, 0, 99.99, 2)  || 
			!esNumeroDecimal('Horas reales martes: ', formAct.hnamartes.value, 0, 99.99, 2)  ||
			!esNumeroDecimal('Horas virtuales martes: ', formAct.heamartes.value, 0, 99.99, 2)  || 
			!esNumeroDecimal('Horas reales miercoles: ', formAct.hnamiercoles.value, 0, 99.99, 2)  ||
			!esNumeroDecimal('Horas virtuales miercoles: ', formAct.heamiercoles.value, 0, 99.99, 2)  || 
			!esNumeroDecimal('Horas reales jueves: ', formAct.hnajueves.value, 0, 99.99, 2)  ||
			!esNumeroDecimal('Horas virtuales jueves: ', formAct.heajueves.value, 0, 99.99, 2)  || 
			!esNumeroDecimal('Horas reales viernes: ', formAct.hnaviernes.value, 0, 99.99, 2)  ||
			!esNumeroDecimal('Horas virtuales viernes: ', formAct.heaviernes.value, 0, 99.99, 2)  || 
			!esNumeroDecimal('Horas reales sabado: ', formAct.hnasabado.value, 0, 99.99, 2)  ||
			!esNumeroDecimal('Horas virtuales sabado: ', formAct.heasabado.value, 0, 99.99, 2)  || 
			!esNumeroDecimal('Horas reales domingo: ', formAct.hnadomingo.value, 0, 99.99, 2)  ||
			!esNumeroDecimal('Horas virtuales domingo: ', formAct.headomingo.value, 0, 99.99, 2)  	)
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
		return false;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomTurnosDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_turnos_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomTurnosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td> <div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                Clave:</div></td>
            <td><input name="id_turno" type="text" id="id_turno" size="8" maxlength="3"<%= (request.getParameter("proceso").equals("CAMBIAR_TURNO")) ? " readonly=\"true\"" : "" %>></td>
            <td>Descripcion: </td>
            <td colspan="3" align="left"><input name="descripcion" type="text" id="descripcion2" size="40" maxlength="40"></td>
          </tr>
          <tr align="center"> 
            <td colspan="6" class="titChico">Configuraci&oacute;n del turno</td>
          </tr>
          <tr> 
            <td align="left"> Dia laboral</td>
            <td width="20%" align="left">Tipo de jornada</td>
            <td width="20%" align="center">Entrada</td>
            <td width="20%" align="center">Salida</td>
            <td width="12%" align="center">Horas por dia</td>
            <td width="12%" align="center">Horas Laborables</td>
          </tr>
          <tr> 
            <td align="left"><input name="lunes" type="checkbox" value="checkbox">
              Lunes</td>
            <td align="left"><select name="ttlun" class="cpoCol" id="ttlun">
                		<option value="0"<% 
					   				 if(request.getParameter("ttlun") != null) {
										if(request.getParameter("ttlun").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTLun() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Mismo Dia</option>
                        <option value="1"<% 
					   				 if(request.getParameter("ttlun") != null) {
										if(request.getParameter("ttlun").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTLun() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Sig. Dia</option>
						<option value="2"<% 
					   				 if(request.getParameter("ttlun") != null) {
										if(request.getParameter("ttlun").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTLun() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Dia Ant.</option>
              </select></td>
            <td align="center"><input name="elunes" type="text" id="elunes" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('elunes','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="slunes" type="text" id="slunes" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('slunes','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="hnalunes" type="text" id="hnalunes" size="8" maxlength="8"></td>
            <td align="center"> <input name="healunes" type="text" id="healunes" size="8" maxlength="8"></td>
          </tr>
          <tr> 
            <td align="left"><input name="martes" type="checkbox" value="checkbox">
              Martes</td>
            <td align="left"><select name="ttmar" class="cpoCol" id="ttmar">
                		<option value="0"<% 
					   				 if(request.getParameter("ttmar") != null) {
										if(request.getParameter("ttmar").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTMar()== 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Mismo Dia</option>
                        <option value="1"<% 
					   				 if(request.getParameter("ttmar") != null) {
										if(request.getParameter("ttmar").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTMar() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Sig. Dia</option>
						<option value="2"<% 
					   				 if(request.getParameter("ttmar") != null) {
										if(request.getParameter("ttmar").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTMar() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Dia Ant.</option>
              </select></td>
            <td align="center"><input name="emartes" type="text" id="emartes" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('emartes','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="smartes" type="text" id="smartes" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('smartes','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="hnamartes" type="text" id="hnamartes" size="8" maxlength="8"></td>
            <td align="center"><input name="heamartes" type="text" id="heamartes" size="8" maxlength="8"></td>
          </tr>
          <tr> 
            <td align="left"><input name="miercoles" type="checkbox" value="checkbox">
              Miercoles</td>
            <td align="left"><select name="ttmie" class="cpoCol" id="ttmie">
                		<option value="0"<% 
					   				 if(request.getParameter("ttmie") != null) {
										if(request.getParameter("ttmie").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTMie() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Mismo Dia</option>
                        <option value="1"<% 
					   				 if(request.getParameter("ttmie") != null) {
										if(request.getParameter("ttmie").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTMie() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Sig. Dia</option>
						<option value="2"<% 
					   				 if(request.getParameter("ttmie") != null) {
										if(request.getParameter("ttmie").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTMie() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Dia Ant.</option>
              </select></td>
            <td align="center"><input name="emiercoles" type="text" id="emiercoles" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('emiercoles','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="smiercoles" type="text" id="smiercoles" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('smiercoles','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="hnamiercoles" type="text" id="hnamiercoles" size="8" maxlength="8"></td>
            <td align="center"><input name="heamiercoles" type="text" id="heamiercoles" size="8" maxlength="8"></td>
          </tr>
          <tr> 
            <td align="left"><input name="jueves" type="checkbox" value="checkbox">
              Jueves</td>
            <td align="left"><select name="ttjue" class="cpoCol" id="ttjue">
                		<option value="0"<% 
					   				 if(request.getParameter("ttjue") != null) {
										if(request.getParameter("ttjue").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTJue() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Mismo Dia</option>
                        <option value="1"<% 
					   				 if(request.getParameter("ttjue") != null) {
										if(request.getParameter("ttjue").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTJue() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Sig. Dia</option>
						<option value="2"<% 
					   				 if(request.getParameter("ttjue") != null) {
										if(request.getParameter("ttjue").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTJue() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Dia Ant.</option>
              </select></td>
            <td align="center"><input name="ejueves" type="text" id="ejueves" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('ejueves','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="sjueves" type="text" id="sjueves" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('sjueves','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="hnajueves" type="text" id="hnajueves" size="8" maxlength="8"></td>
            <td align="center"><input name="heajueves" type="text" id="heajueves" size="8" maxlength="8"></td>
          </tr>
          <tr> 
            <td align="left"><input name="viernes" type="checkbox" value="checkbox">
              Viernes</td>
            <td align="left"><select name="ttvie" class="cpoCol" id="ttvie">
                		<option value="0"<% 
					   				 if(request.getParameter("ttvie") != null) {
										if(request.getParameter("ttvie").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTVie() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Mismo Dia</option>
                        <option value="1"<% 
					   				 if(request.getParameter("ttvie") != null) {
										if(request.getParameter("ttvie").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTVie() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Sig. Dia</option>
						<option value="2"<% 
					   				 if(request.getParameter("ttvie") != null) {
										if(request.getParameter("ttvie").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTVie() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Dia Ant.</option>
              </select></td>
            <td align="center"><input name="eviernes" type="text" id="eviernes" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('eviernes','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="sviernes" type="text" id="sviernes" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('sviernes','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="hnaviernes" type="text" id="hnaviernes" size="8" maxlength="8"></td>
            <td align="center"><input name="heaviernes" type="text" id="heaviernes" size="8" maxlength="8"></td>
          </tr>
          <tr> 
            <td align="left"><input name="sabado" type="checkbox" value="checkbox">
              Sabado</td>
            <td align="left"><select name="ttsab" class="cpoCol" id="ttsab">
                		<option value="0"<% 
					   				 if(request.getParameter("ttsab") != null) {
										if(request.getParameter("ttsab").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTSab() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Mismo Dia</option>
                        <option value="1"<% 
					   				 if(request.getParameter("ttsab") != null) {
										if(request.getParameter("ttsab").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTSab() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Sig. Dia</option>
						<option value="2"<% 
					   				 if(request.getParameter("ttsab") != null) {
										if(request.getParameter("ttsab").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTSab() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Dia Ant.</option>
              </select></td>
            <td align="center"><input name="esabado" type="text" id="esabado" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('esabado','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="ssabado" type="text" id="ssabado" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('ssabado','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="hnasabado" type="text" id="hnasabado" size="8" maxlength="8"></td>
            <td align="center"><input name="heasabado" type="text" id="heasabado" size="8" maxlength="8"></td>
          </tr>
          <tr> 
            <td align="left"><input name="domingo" type="checkbox" value="checkbox">
              Domingo</td>
            <td align="left"><select name="ttdom" class="cpoCol" id="ttdom">
                		<option value="0"<% 
					   				 if(request.getParameter("ttdom") != null) {
										if(request.getParameter("ttdom").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTDom() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Mismo Dia</option>
                        <option value="1"<% 
					   				 if(request.getParameter("ttdom") != null) {
										if(request.getParameter("ttdom").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTDom() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Sig. Dia</option>
						<option value="2"<% 
					   				 if(request.getParameter("ttdom") != null) {
										if(request.getParameter("ttdom").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_TURNO") || request.getParameter("proceso").equals("CONSULTAR_TURNO")) { 
											if(set.getAbsRow(0).getTTDom() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Nocturno Dia Ant.</option>
              </select></td>
            <td align="center"><input name="edomingo" type="text" id="edomingo" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('edomingo','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="sdomingo" type="text" id="sdomingo" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('sdomingo','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td align="center"><input name="hnadomingo" type="text" id="hnadomingo" size="8" maxlength="8"></td>
            <td align="center"><input name="headomingo" type="text" id="headomingo" size="8" maxlength="8"></td>
          </tr>
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_turnos_dlg.id_turno.value = '<% if(request.getParameter("id_turno") != null) { out.print( request.getParameter("id_turno") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getID_Turno() ); } else { out.print(""); } %>'  
document.nom_turnos_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 

document.nom_turnos_dlg.elunes.value = '<% if(request.getParameter("elunes") != null) { out.print( request.getParameter("elunes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getELunes(), "HH:mm") ); } else { out.print("08:00"); } %>'
document.nom_turnos_dlg.slunes.value = '<% if(request.getParameter("slunes") != null) { out.print( request.getParameter("slunes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getSLunes(), "HH:mm") ); } else { out.print("17:00"); } %>'

document.nom_turnos_dlg.emartes.value = '<% if(request.getParameter("emartes") != null) { out.print( request.getParameter("emartes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getEMartes(), "HH:mm") ); } else { out.print("08:00"); } %>'
document.nom_turnos_dlg.smartes.value = '<% if(request.getParameter("smartes") != null) { out.print( request.getParameter("smartes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getSMartes(), "HH:mm") ); } else { out.print("17:00"); } %>'

document.nom_turnos_dlg.emiercoles.value = '<% if(request.getParameter("emiercoles") != null) { out.print( request.getParameter("emiercoles") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getEMiercoles(), "HH:mm") ); } else { out.print("08:00"); } %>'
document.nom_turnos_dlg.smiercoles.value = '<% if(request.getParameter("smiercoles") != null) { out.print( request.getParameter("smiercoles") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getSMiercoles(), "HH:mm") ); } else { out.print("17:00"); } %>'

document.nom_turnos_dlg.ejueves.value = '<% if(request.getParameter("ejueves") != null) { out.print( request.getParameter("ejueves") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getEJueves(), "HH:mm") ); } else { out.print("08:00"); } %>'
document.nom_turnos_dlg.sjueves.value = '<% if(request.getParameter("sjueves") != null) { out.print( request.getParameter("sjueves") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getSJueves(), "HH:mm") ); } else { out.print("17:00"); } %>'

document.nom_turnos_dlg.eviernes.value = '<% if(request.getParameter("eviernes") != null) { out.print( request.getParameter("eviernes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getEViernes(), "HH:mm") ); } else { out.print("08:00"); } %>'
document.nom_turnos_dlg.sviernes.value = '<% if(request.getParameter("sviernes") != null) { out.print( request.getParameter("sviernes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getSViernes(), "HH:mm") ); } else { out.print("17:00"); } %>'

document.nom_turnos_dlg.esabado.value = '<% if(request.getParameter("esabado") != null) { out.print( request.getParameter("esabado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getESabado(), "HH:mm") ); } else { out.print("08:00"); } %>'
document.nom_turnos_dlg.ssabado.value = '<% if(request.getParameter("ssabado") != null) { out.print( request.getParameter("ssabado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getSSabado(), "HH:mm") ); } else { out.print("17:00"); } %>'

document.nom_turnos_dlg.edomingo.value = '<% if(request.getParameter("edomingo") != null) { out.print( request.getParameter("edomingo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getEDomingo(), "HH:mm") ); } else { out.print("08:00"); } %>'
document.nom_turnos_dlg.sdomingo.value = '<% if(request.getParameter("sdomingo") != null) { out.print( request.getParameter("sdomingo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( JUtil.obtHoraTxt(set.getAbsRow(0).getSDomingo(), "HH:mm") ); } else { out.print("17:00"); } %>'

document.nom_turnos_dlg.hnalunes.value = '<% if(request.getParameter("hnalunes") != null) { out.print( request.getParameter("hnalunes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHNALunes() ); } else { out.print("8"); } %>' 
document.nom_turnos_dlg.healunes.value = '<% if(request.getParameter("healunes") != null) { out.print( request.getParameter("healunes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHEALunes() ); } else { out.print("11.2"); } %>' 
document.nom_turnos_dlg.hnamartes.value = '<% if(request.getParameter("hnamartes") != null) { out.print( request.getParameter("hnamartes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHNAMartes() ); } else { out.print("8"); } %>' 
document.nom_turnos_dlg.heamartes.value = '<% if(request.getParameter("heamartes") != null) { out.print( request.getParameter("heamartes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHEAMartes() ); } else { out.print("11.2"); } %>' 
document.nom_turnos_dlg.hnamiercoles.value = '<% if(request.getParameter("hnamiercoles") != null) { out.print( request.getParameter("hnamiercoles") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHNAMiercoles() ); } else { out.print("8"); } %>' 
document.nom_turnos_dlg.heamiercoles.value = '<% if(request.getParameter("heamiercoles") != null) { out.print( request.getParameter("heamiercoles") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHEAMiercoles() ); } else { out.print("11.2"); } %>' 
document.nom_turnos_dlg.hnajueves.value = '<% if(request.getParameter("hnajueves") != null) { out.print( request.getParameter("hnajueves") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHNAJueves() ); } else { out.print("8"); } %>' 
document.nom_turnos_dlg.heajueves.value = '<% if(request.getParameter("heajueves") != null) { out.print( request.getParameter("heajueves") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHEAJueves() ); } else { out.print("11.2"); } %>' 
document.nom_turnos_dlg.hnaviernes.value = '<% if(request.getParameter("hnaviernes") != null) { out.print( request.getParameter("hnaviernes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHNAViernes() ); } else { out.print("8"); } %>' 
document.nom_turnos_dlg.heaviernes.value = '<% if(request.getParameter("heaviernes") != null) { out.print( request.getParameter("heaviernes") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHEAViernes() ); } else { out.print("11.2"); } %>' 
document.nom_turnos_dlg.hnasabado.value = '<% if(request.getParameter("hnasabado") != null) { out.print( request.getParameter("hnasabado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHNASabado() ); } else { out.print("8"); } %>' 
document.nom_turnos_dlg.heasabado.value = '<% if(request.getParameter("heasabado") != null) { out.print( request.getParameter("heasabado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHEASabado() ); } else { out.print("11.2"); } %>' 
document.nom_turnos_dlg.hnadomingo.value = '<% if(request.getParameter("hnadomingo") != null) { out.print( request.getParameter("hnadomingo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHNADomingo() ); } else { out.print("8"); } %>' 
document.nom_turnos_dlg.headomingo.value = '<% if(request.getParameter("headomingo") != null) { out.print( request.getParameter("headomingo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_TURNO")) { out.print( set.getAbsRow(0).getHEADomingo() ); } else { out.print("11.2"); } %>' 

document.nom_turnos_dlg.lunes.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_TURNO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_TURNO") ) { out.print( (set.getAbsRow(0).getLunes() ? "true" : "false" ) ); } else if(request.getParameter("lunes") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_turnos_dlg.martes.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_TURNO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_TURNO") ) { out.print( (set.getAbsRow(0).getMartes() ? "true" : "false" ) ); } else if(request.getParameter("martes") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_turnos_dlg.miercoles.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_TURNO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_TURNO") ) { out.print( (set.getAbsRow(0).getMiercoles() ? "true" : "false" ) ); } else if(request.getParameter("miercoles") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_turnos_dlg.jueves.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_TURNO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_TURNO") ) { out.print( (set.getAbsRow(0).getJueves() ? "true" : "false" ) ); } else if(request.getParameter("jueves") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_turnos_dlg.viernes.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_TURNO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_TURNO") ) { out.print( (set.getAbsRow(0).getViernes() ? "true" : "false" ) ); } else if(request.getParameter("viernes") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_turnos_dlg.sabado.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_TURNO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_TURNO") ) { out.print( (set.getAbsRow(0).getSabado() ? "true" : "false" ) ); } else if(request.getParameter("sabado") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_turnos_dlg.domingo.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_TURNO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_TURNO") ) { out.print( (set.getAbsRow(0).getDomingo() ? "true" : "false" ) ); } else if(request.getParameter("domingo") != null ) { out.print("true"); } else { out.print("false"); } %>  
</script>
</body>
</html>
