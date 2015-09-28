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
<%@ page import="forseti.*,org.apache.commons.lang.mutable.MutableInt"%>
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
	  <form onSubmit="return enviarlo(this)" action="/servlet/REFProcSR" method="post" name="adm_bd_dlg" target="_self">
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
            <td colspan="2" align="center"> <h2>Datos de contacto</h2></td>
          </tr>
          <tr> 
            <td align="right" width="30%"> 
			  <input name="proceso" type="hidden" value="AGREGAR_EMPRESA"> 
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
            <td colspan="2" align="center">&nbsp;</td>
          </tr>
          <tr> 
            <td colspan="2" align="center"> <h2>Datos de la compa&ntilde;&iacute;a</h2></td>
          </tr>
          <tr> 
            <td width="30%" align="right"> <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %></td>
            <td> <input name="nombre" type="text" class="cpoCol" size="15" maxlength="20"> 
              <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE",2) %></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %></td>
            <td><input name="nm_legal" type="text" class="cpoCol" id="nm_legal" size="80" maxlength="70"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","DIRECCION") %></td>
            <td><input name="direccion" type="text" id="direccion" size="80" maxlength="255"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","POBLACION") %></td>
            <td><input name="poblacion" type="text" id="poblacion" size="25" maxlength="50"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
            <td><input name="cp" type="text" id="cp" size="7" maxlength="9"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","WEB") %></td>
            <td><input name="web" type="text" id="web" size="80" maxlength="255"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %></td>
            <td><input name="password" type="password" class="cpoCol" id="password" size="15" maxlength="30"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %></td>
            <td><input name="confpwd" type="password" class="cpoCol" id="confpwd" size="15" maxlength="30"></td>
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
		<h1>Datos del contacto y de la empresa creada</h1>
	</td>
</tr>
<tr>
  <td>
	 <table width="100%" border="0" cellspacing="3" cellpadding="0">
	      <tr> 
            <td width="30%" align="right">Nombre:</td>
            <td class="titChicoNeg"><%= request.getParameter("tx_name") %></td>
          </tr>
		  <tr> 
            <td width="30%" align="right">Apellidos:</td>
            <td class="titChicoNeg"><%= request.getParameter("tx_surname") %></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %>:</td>
            <td class="titChicoNeg"><%= request.getParameter("tx_email") %></td>
          </tr>
          <tr> 
            <td width="30%" align="right"> 
               <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %>:</td>
            <td class="titChicoNeg"><%= request.getParameter("nombre") %></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %>:</td>
            <td class="titChicoNeg"><%= request.getParameter("nm_legal") %></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","DIRECCION") %>:</td>
            <td class="titChicoNeg"><%= request.getParameter("direccion") %></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","POBLACION") %>:</td>
            <td class="titChicoNeg"><%= request.getParameter("poblacion") %></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","CP") %>:</td>
            <td class="titChicoNeg"><%= request.getParameter("cp") %></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","WEB") %>:</td>
            <td class="titChicoNeg"><%= request.getParameter("web") %></td>
          </tr>
		  <tr> 
            <td colspan="2"><hr></td>
          </tr>
		  <tr> 
            <td align="center" colspan="2"><h2>Ya se puede accesar a la empresa 
              desde cualquier navegador con la siguiente informaci&oacute;n</h2></td>
          </tr>
		  <tr> 
		  	
          <td align="right">URL:</td>
            <td class="titChicoNar">https://prueba.forseti.org.mx/CEF</td>
          </tr>
		  <tr> 
		  	<td align="right">Nombre de la Base de Datos:</td>
            <td class="titChicoNar"><%= request.getParameter("nombre") %></td>
          </tr>
		  <tr> 
		  	<td align="right">Usuario empresarial:</td>
            <td class="titChicoNar"><%= request.getParameter("nombre").toLowerCase() %></td>
          </tr>
		  <tr> 
		  	<td align="right">Contraseña del Usuario:</td>
            <td class="titChicoNar">Es la clave que acabas de registrar</td>
          </tr>
		  <tr> 
		  	<td align="right">&nbsp;</td>
            <td class="titCuerpoNeg">Es muy recomendable revisar la documentaci&oacute;n del CEF 
              para tener un punto de partida y saber como operar el sistema. Te 
              sugerimos empezar por el Centro de Control ya que es el &aacute;rea 
              encargada de las configuraciones de inicio, entidades, usuarios 
              y en general, de las propiedades de la empresa.<br>
              <strong>Gracias por utilizar este servicio. Esperamos que sea de 
              utilidad y que te brinde una grata experiancia. &iexcl;&iexcl;Suerte!!</strong>
            </td>
          </tr>
		</table>
	</td>
  </tr>
<%
	}
%>
</table>
<script language="JavaScript" type="text/javascript">
document.adm_bd_dlg.polprv.checked = <% if(request.getParameter("polprv") != null) { out.print("true"); } else { out.print("false"); } %>  
document.adm_bd_dlg.tx_name.value = '<% if(request.getParameter("tx_name") != null) { out.print( request.getParameter("tx_name") ); } else { out.print(""); } %>'
document.adm_bd_dlg.tx_surname.value = '<% if(request.getParameter("tx_surname") != null) { out.print( request.getParameter("tx_surname") ); } else { out.print(""); } %>'
document.adm_bd_dlg.tx_email.value = '<% if(request.getParameter("tx_email") != null) { out.print( request.getParameter("tx_email") ); } else { out.print(""); } %>'
document.adm_bd_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else { out.print(""); } %>'
document.adm_bd_dlg.nm_legal.value = '<% if(request.getParameter("nm_legal") != null) { out.print( request.getParameter("nm_legal") ); } else { out.print(""); } %>'
document.adm_bd_dlg.direccion.value = '<% if(request.getParameter("direccion") != null) { out.print( request.getParameter("direccion") ); } else { out.print(""); } %>'
document.adm_bd_dlg.poblacion.value = '<% if(request.getParameter("poblacion") != null) { out.print( request.getParameter("poblacion") ); } else { out.print(""); } %>'
document.adm_bd_dlg.cp.value = '<% if(request.getParameter("cp") != null) { out.print( request.getParameter("cp") ); } else { out.print(""); } %>'
document.adm_bd_dlg.web.value = '<% if(request.getParameter("web") != null) { out.print( request.getParameter("web") ); } else { out.print(""); } %>'
</script>
</body>
</html>
