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
<%@ page import="forseti.*,org.apache.commons.lang.mutable.MutableInt,java.util.Date"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel=stylesheet href="../compfsi/estilos.css" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="3" cellpadding="0">
<%
	MutableInt idmensaje = (MutableInt)request.getAttribute("idmensaje");
	StringBuffer mensaje = (StringBuffer)request.getAttribute("mensaje");
   	out.println(JUtil.getMensajeProcSR(idmensaje, mensaje));
	//out.print(JUtil.depurarParametros(request));
	if(idmensaje == null || (idmensaje.intValue() != 0 && idmensaje.intValue() != 1) )
	{
%>
<tr>
  <td>
	  <form onSubmit="return enviarlo(this)" action="/servlet/REFProcSR" method="post" name="crm_oportunidades_dlg" target="_self">
	  	<table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td colspan="2"> <input name="polprv" type="checkbox" value="OK">
              &nbsp;Acepto la política de privacidad </td>
          </tr>
          <tr> 
            <td colspan="2" align="center"> <h1>Ingresa los datos para el registro</h1></td>
          </tr>
          <tr> 
            <td colspan="2" align="center">&nbsp; </td>
          </tr>
          <tr> 
            <td colspan="2" align="center"> <h2>Datos de producto y contacto</h2></td>
          </tr>
		  <tr> 
            <td width="30%" align="right" valign="top">
			  <input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>"> 
              Tipo de producto:</td>
            <td>
			  <select name="tipo" size="5" class="cpoCol">
<%
		if(request.getParameter("proceso").equals("RENTAR_INSTANCIA"))
		{
%>
                <option value="INFO"<% 
					   				 if(request.getParameter("tipo") == null || request.getParameter("tipo").equals("INFO")) {
										out.print(" selected");
									 } %>> Más información sobre renta de instancias </option>
                <option value="MICRO"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("MICRO")) {
											out.print(" selected");
										}
									 } %>> Micro Instancia de 1 a 10 usuarios </option>
				<option value="PEQUEÑA"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("PEQUEÑA")) {
											out.print(" selected");
										}
									 } %>> Pequeña Instancia de 11 a 25 usuarios </option>
				<option value="MEDIANA"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("MEDIANA")) {
											out.print(" selected");
										}
									 } %>> Instancia Mediana para 26 a 100 usuarios </option>
				<option value="GRANDE"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("MEDIANA")) {
											out.print(" selected");
										}
									 } %>> Instancia Grande para mas de 100 usuarios </option>
<%
		}
		else if(request.getParameter("proceso").equals("RENTAR_ESPACIO"))
		{
%>
                <option value="INFO"<% 
					   				 if(request.getParameter("tipo") == null || request.getParameter("tipo").equals("INFO")) {
										out.print(" selected");
									 } %>> Más información sobre renta de espacios </option>
                <option value="ESPACIO"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("ESPACIO")) {
											out.print(" selected");
										}
									 } %>> Espacio para un negocio pequeño </option>
<%
		}
		else if(request.getParameter("proceso").equals("SOLICITAR_SOPORTE"))
		{
%>
                <option value="Recursos Externos"<% 
					   				 if(request.getParameter("tipo") == null || request.getParameter("tipo").equals("Recursos Externos")) {
										out.print(" selected");
									 } %>> Recursos Externos </option>
                <option value="Soporte telefónico gratuito"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("Soporte telefónico gratuito")) {
											out.print(" selected");
										}
									 } %>> Soporte telefónico gratuito </option>
				 <option value="Soporte telefónico comercial"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("Soporte telefónico comercial")) {
											out.print(" selected");
										}
									 } %>> Soporte telefónico comercial </option>
				 <option value="Soporte especializado"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("Soporte especializado")) {
											out.print(" selected");
										}
									 } %>> Soporte especializado </option>
<%
		}
%>											 
              </select></td>
          </tr>
		  <tr> 
            <td align="right" width="30%"> 
              Nombre</td>
            <td><input name="tx_name" type="text" class="cpoCol" id="tx_name" size="50" maxlength="100"></td>
          </tr>
          <tr> 
            <td align="right" width="30%">Apellidos</td>
            <td><input name="tx_surname" type="text" class="cpoCol" id="tx_surname" size="80" maxlength="100"></td>
          </tr>
          <tr> 
            <td align="right" width="30%">G&eacute;nero</td>
            <td> <select name="id_gender" class="cpoCol">
                <option value="M"<% 
					   				 if(request.getParameter("id_gender") != null) {
										if(request.getParameter("id_gender").equals("M")) {
											out.print(" selected");
										}
									 } %>> Hombre </option>
                <option value="F"<% 
					   				 if(request.getParameter("id_gender") != null) {
										if(request.getParameter("id_gender").equals("F")) {
											out.print(" selected");
										}
									 } %>> Mujer </option>
              </select></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
            <td><input name="tx_email" type="text" class="cpoCol" id="tx_email" size="80" maxlength="255"></td>
          </tr>
          <tr> 
            <td align="right">Medio por el cual te informaste de este producto</td>
            <td><select name="tp_origin">
                <option value="Internet"<% 
									if(request.getParameter("tp_origin") != null) {
										if(request.getParameter("tp_origin").equals("Internet")) {
											out.print(" selected");
										}
									 }  %>>Buscador en internet</option>
                <option value="Folleto"<% 
									if(request.getParameter("tp_origin") != null) {
										if(request.getParameter("tp_origin").equals("Folleto")) {
											out.print(" selected");
										}
									 }  %>>Por Folleto</option>
                <option value="Referencia personal"<% 
									if(request.getParameter("tp_origin") != null) {
										if(request.getParameter("tp_origin").equals("Referencia personal")) {
											out.print(" selected");
										}
									 }  %>>Por Referencia personal</option>
                <option value="Spam"<% 
									if(request.getParameter("tp_origin") != null) {
										if(request.getParameter("tp_origin").equals("Spam")) {
											out.print(" selected");
										}
									 }  %>>Correo electrónico</option>
              </select></td>
          </tr>
		  <tr> 
            <td width="30%" align="right">Fecha para contacterte:</td>
            <td><input name="dt_next_action" type="text" id="dt_next_action" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('dt_next_action','ddmmmyyyy',false)"><img src="../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
		  <tr> 
            <td width="30%" align="right">M&aacute;ndanos tus comentarios</td>
            <td><textarea name="tx_note" cols="60" rows="3" id="tx_note"></textarea></td>
          </tr>
          <tr> 
            <td colspan="2" align="center">&nbsp;</td>
          </tr>
          <tr> 
            <td colspan="2" align="center"> <h2>Datos de la empresa o negocio</h2></td>
          </tr>
		 <tr> 
           <td align="right" colspan="2">
			<table width="100%" border="0" cellspacing="2" cellpadding="0">
			    <tr> 
					<td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %></td>
					<td colspan="5"><input class="cpoCol" name="nm_legal" type="text" id="nm_legal" size="80" maxlength="70"></td>
				</tr>
                <tr> 
                  <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
                  <td><input name="nm_street" type="text" id="nm_street" size="30" maxlength="80"></td>
                  <td width="10%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
                  <td width="15%"><input name="nu_street" type="text" id="nu_street" size="10" maxlength="10"></td>
                  <td width="10%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
                  <td width="15%"><input name="tp_street" type="text" id="tp_street" size="10" maxlength="10"></td>
                </tr>
                <tr> 
                  <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
                  <td><input name="tx_addr1" type="text" id="tx_addr1" size="30" maxlength="40"></td>
                  <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
                  <td colspan="3"><input name="tx_addr2" type="text" id="tx_addr2" size="30" maxlength="80"></td>
                </tr>
                <tr> 
                  <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
                  <td><input name="mn_city" type="text" id="mn_city" size="30" maxlength="40"></td>
                  <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
                  <td colspan="3"><input name="zipcode" type="text" id="zipcode" size="7" maxlength="7"></td>
                </tr>
                <tr> 
                  <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
                  <td><input name="nm_state" type="text" id="nm_state" size="30" maxlength="40"></td>
                  <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
                  <td class="txtChicoNeg" colspan="3">M&eacute;xico</td>
                </tr>
                <tr> 
                  <td width="30%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","TEL") %></td>
                  <td><input class="cpoCol" name="work_phone" type="text" id="work_phone" size="30" maxlength="25"></td>
                  <td align="right">Directo</td>
                  <td colspan="3"><input name="direct_phone" type="text" id="direct_phone" size="30" maxlength="25"></td>
                </tr>
              </table>
			</td>
          </tr>
		  <tr> 
            <td colspan="2"><hr></td>
          </tr>
          <tr> 
            <td valign="top" align="right"><img src="/servlet/stickyImg" /></td>
            <td valign="top" class=""><h3>Ingresa el texto de la Imagen</h3>
              <input name="answer" />
              <input type="button" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>" onClick="javascript:this.form.submit(); this.form.aceptar.disabled = true;" /> 
            </td>
          </tr>
        </table>
	  </form>
	  </td>
	</tr>
<%
	}
	else
	{
%>
	<tr>
		<td align="center">
			<h1>Tu solicitud ha sido registrada.</h1>
		</td>
	</tr>
	<tr>
		<td align="center">&nbsp;</td>
	</tr>
	<tr>
    	
    <td class="titCuerpoNar"> Gracias por enviar esta solicitud, nos pondremos 
      en contacto contigo en la fecha que lo solicitarte. Te buscaremos en el 
      tel&eacute;fono registrado, o en el tel&eacute;fono alternativo. Tambi&eacute;n 
      hemos creado un <strong>&quot;Caso de Uso&quot;</strong> como apoyo y seguimiento 
      de tu solicitud. Se te ha enviado un correo electr&oacute;nico de &eacute;ste. 
      Aqu&iacute; puedes poner tus dudas y comentarios sobre este y otros servicios, 
      sobre el estatus de tu solicitud, y tambi&eacute;n son bienvenidas las cr&iacute;ticas, 
      ya que estas generan la necesidad de ser mejores cada d&iacute;a. <br>
      <strong> Gracias por enviarnos tu solicitud... &iexcl;&iexcl;Suerte!!</strong></td>
 	</tr>
<%
	}
%>
</table>
<%
	if(idmensaje == null || (idmensaje.intValue() != 0 && idmensaje.intValue() != 1) )
	{
%>
<script language="JavaScript" type="text/javascript">
document.crm_oportunidades_dlg.polprv.checked = <% if(request.getParameter("polprv") != null) { out.print("true"); } else { out.print("false"); } %>  
document.crm_oportunidades_dlg.dt_next_action.value = '<% if(request.getParameter("dt_next_action") != null) { out.print( request.getParameter("dt_next_action") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
document.crm_oportunidades_dlg.tx_note.value = '<% if(request.getParameter("tx_note") != null) { out.print( request.getParameter("tx_note") ); } else { out.print(""); } %>' 
document.crm_oportunidades_dlg.nm_legal.value = '<% if(request.getParameter("nm_legal") != null) { out.print( request.getParameter("nm_legal") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_name.value = '<% if(request.getParameter("tx_name") != null) { out.print( request.getParameter("tx_name") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_surname.value = '<% if(request.getParameter("tx_surname") != null) { out.print( request.getParameter("tx_surname") ); } else { out.print(""); } %>' 

document.crm_oportunidades_dlg.tp_street.value = '<% if(request.getParameter("tp_street") != null) { out.print( request.getParameter("tp_street") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.nm_street.value = '<% if(request.getParameter("nm_street") != null) { out.print( request.getParameter("nm_street") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.nu_street.value = '<% if(request.getParameter("nu_street") != null) { out.print( request.getParameter("nu_street") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_addr1.value = '<% if(request.getParameter("tx_addr1") != null) { out.print( request.getParameter("tx_addr1") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_addr2.value = '<% if(request.getParameter("tx_addr2") != null) { out.print( request.getParameter("tx_addr2") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.nm_state.value = '<% if(request.getParameter("nm_state") != null) { out.print( request.getParameter("nm_state") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.mn_city.value = '<% if(request.getParameter("mn_city") != null) { out.print( request.getParameter("mn_city") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.zipcode.value = '<% if(request.getParameter("zipcode") != null) { out.print( request.getParameter("zipcode") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.direct_phone.value = '<% if(request.getParameter("direct_phone") != null) { out.print( request.getParameter("direct_phone") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.work_phone.value = '<% if(request.getParameter("work_phone") != null) { out.print( request.getParameter("work_phone") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_email.value = '<% if(request.getParameter("tx_email") != null) { out.print( request.getParameter("tx_email") ); } else { out.print(""); } %>'
</script>
<%
	}
%>
</body>
</html>
