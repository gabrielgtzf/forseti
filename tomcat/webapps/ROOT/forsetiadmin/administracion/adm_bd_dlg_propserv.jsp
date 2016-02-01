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
	String adm_bd_dlg = (String)request.getAttribute("adm_bd_dlg");
	if(adm_bd_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarTitulo(JUtil.Msj("SAF","ADMIN_BD","VISTA",request.getParameter("proceso"),3));
	
	JAdmVariablesSet var = new JAdmVariablesSet(null);
	var.ConCat(true);
	var.Open();
	Map<String, String> map = new HashMap<String, String>();
	for(int i = 0; i < var.getNumRows(); i++)
	{
		String idv = var.getAbsRow(i).getID_Variable();
		String val;
		if(idv.equals("AUTO_ACT") || idv.equals("AUTO_RESP") || idv.equals("AUTO_SLDS")) 
			val = var.getAbsRow(i).getVEntero() == 1 ? "true" : "false";
		else if(idv.equals("AUTO_HORA"))
			val = JUtil.obtHoraTxt(var.getAbsRow(i).getVHora(), "HH:mm");
		else if(idv.equals("SMTP_PORT")) 
			val = Integer.toString(var.getAbsRow(i).getVEntero());
		else
			val = var.getAbsRow(i).getVAlfanumerico();
			
		map.put(idv, val);
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
<script language="JavaScript" type="text/javascript" src="../../compfsi/saftimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if( !esCadena("Ruta de Instalacion de TOMCAT", formAct.tomcat.value, 2, 255) ||
		!esCadena("Ruta del OpenJDK o Java JDK", formAct.jvm.value, 2, 255) ||
        !esCadena("Nombre de este servidor", formAct.servidor.value, 4, 255) ||
        !esCadena("URL de revision y descarga de actualizaciones forseti", formAct.actualizar.value, 2, 255) ||
		!esCadena("Ruta de Respaldos", formAct.respaldos.value, 2, 255) ||
		!esCadena("URL de conexion al servidor JPacConn", formAct.pac_purl.value, 2, 255) ||
        !esCadena("ID de este servidor para Timbrar en un servicio JPacConn", formAct.pac_serv.value, 36, 36) ||
        !esCadena("Usuario para el JPacConn", formAct.pac_user.value, 2, 255) ||
        !esCadena("URL de conexion al Proveedor", formAct.edi_purl.value, 2, 255) ||
        !esCadena("Usuario para el Proveedor", formAct.edi_user.value, 2, 255) ||
        !esCadena("URL de conexion al servidor SMTP", formAct.smtp_host.value, 2, 255) ||
        !esNumeroEntero("Puerto", formAct.smtp_port.value, 0, 59999) ||
        !esCadena("Usuario", formAct.smtp_user.value, 2, 255) ||
		!esCadena("Nombre del Bucket en el servicio Amazon S3", formAct.s3_bukt.value, 2, 255) ||
		!esCadena("Access Key", formAct.s3_user.value, 2, 255) )
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
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmBDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_bd_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clock"> 
              <%  if(JUtil.getSesionAdmin(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAdmBDCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
	%>
    <tr> 
      <td> 
	  	<table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
		  	<td colspan="2" class="titChicoNAR" align="center">
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                General</td>
          </tr>
		  <tr> 
            <td width="40%" align="right"> Ruta de Instalacion de TOMCAT</td>
            <td><input name="tomcat" type="text" size="80" maxlength="255"></td>
          </tr>
          <tr> 
            <td align="right">Ruta del OpenJDK o Java JDK</td>
            <td><input name="jvm" type="text" id="jvm" size="80" maxlength="255"></td>
          </tr>
          <tr> 
            <td align="right">Nombre de este servidor</td>
            <td><input name="servidor" type="text" id="servidor" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right">URL de revision y descarga de actualizaciones forseti</td>
            <td><input name="actualizar" type="text" id="actualizar" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right">Contraseña de fsi</td>
            <td><input name="fsi_pass" type="text" id="fsi_pass" size="25" maxlength="50"> Dejar en blanco para no cambiar la contraseña</td>
          </tr>
		  <tr> 
            <td align="right">Infraestructura PostgreSQL</td>
            <td><select name="postgresql" class="cpoBco">
                <option value="general"<% 
					   				 if(request.getParameter("postgresql") != null) {
										if(request.getParameter("postgresql").equals("general")) {
											out.print(" selected");
										}
									 } %>>Local o en Segundo servidor</option>
                <option value="rds"<% 
					   				 if(request.getParameter("postgresql") != null) {
										if(request.getParameter("postgresql").equals("rds")) {
											out.print(" selected");
										}
									 } %>>AWS RDS</option>
              </select></td>
          </tr>
		  <tr> 
            <td align="right">Ruta de Respaldos</td>
            <td><input name="respaldos" type="text" id="respaldos" size="80" maxlength="255"></td>
          </tr>
		  
		  <tr> 
		  	<td colspan="2" class="titChicoNAR" align="center">
				Automatizacion de Tareas (Los cambios solo se reflejan al reiniciar el servidor)</td>
          </tr>
		  <tr> 
            <td align="right">Hora de comienzo de procesos</td>
            <td><input name="auto_hora" type="text" id="auto_hora" size="12" maxlength="15" readonly="true">
              <a href="javascript:NewCal('auto_hora','ddmmmyyyy',true,24)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a> 
              (Muy sugerible durante el tiempo de menor actividad)</td>
          </tr>
		  <tr> 
            <td align="right">Revision de actualizaciones disponibles</td>
            <td><input name="auto_act" type="checkbox" value="checkbox"> Automatizar</td>
		  </tr>
		  <tr> 
            <td align="right">Respaldos</td>
            <td><input name="auto_resp" type="checkbox" value="checkbox"> Automatizar</td>
		  </tr>
		  <tr> 
            <td align="right">Actalizacion de saldos y otros, en todas las empresas</td>
            <td><input name="auto_slds" type="checkbox" value="checkbox"> Automatizar</td>
		  </tr>
		  <tr> 
		  	<td colspan="2" class="titChicoNAR" align="center"> Datos de este 
              servidor, para autenticarse en un servicio web de forseti para recibir 
              servicio de timbrado, correo electrónico automático y/o alojamiento 
              de archivos en la nube</td>
          </tr>
		  <tr> 
            <td align="right">URL de conexión al servicio web forseti con puerto 
              explicito</td>
            <td><input name="pac_purl" type="text" id="pac_purl" size="80" maxlength="255">
              Ejemplo: servicios.forseti.com.mx:443</td>
          </tr>
		  <tr> 
            <td align="right">ID de este servidor forseti</td>
            <td><input name="pac_serv" type="text" id="pac_serv" size="36" maxlength="36"></td>
          </tr>
		  <tr> 
            <td align="right">Usuario para el servicio</td>
            <td><input name="pac_user" type="text" id="pac_user" size="25" maxlength="50"></td>
          </tr>
		  <tr> 
            <td align="right">Contraseña para el servicio</td>
            <td><input name="pac_pass" type="text" id="pac_pass" size="25" maxlength="50"> Dejar en blanco para no cambiar la contraseña</td>
          </tr>
		  <tr> 
		  	<td colspan="2" class="titChicoNAR" align="center"> Datos del Proveedor 
              Autorizado de Certificacion (si, y solo si este servidor brinda 
              el servicio de timbrado)</td>
          </tr>
		  <tr> 
            <td align="right">URL de conexion al Proveedor</td>
            <td><input name="edi_purl" type="text" id="edi_purl" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right">Usuario para el Proveedor</td>
            <td><input name="edi_user" type="text" id="edi_user" size="25" maxlength="50"></td>
          </tr>
		  <tr> 
            <td align="right">Contraseña para el Proveedor</td>
            <td><input name="edi_pass" type="text" id="edi_pass" size="25" maxlength="50"> Dejar en blanco para no cambiar la contraseña</td>
          </tr>
		  		  
		  <tr> 
		  	<td colspan="2" class="titChicoNAR" align="center">
				Datos del servidor SMTP para el envio de correos electronicos Por JavaMail</td>
          </tr>
		  <tr> 
            <td align="right">URL de conexión al servidor SMTP (IP o Dominio)</td>
            <td><input name="smtp_host" type="text" id="smtp_host" size="80" maxlength="255">
              Ejemplo: email-smtp.us-east-1.amazonaws.com</td>
          </tr>
		  <tr> 
            <td align="right">Puerto</td>
            <td><input name="smtp_port" type="text" id="smtp_port" size="7" maxlength="6"></td>
          </tr>
		  <tr> 
            <td align="right">Usuario</td>
            <td><input name="smtp_user" type="text" id="smtp_user" size="25" maxlength="50"></td>
          </tr>
		  <tr> 
            <td align="right">Contraseña</td>
            <td><input name="smtp_pass" type="text" id="smtp_pass" size="25" maxlength="50"> Dejar en blanco para no cambiar la contraseña</td>
          </tr>
		  
		  <tr> 
		  	<td colspan="2" class="titChicoNAR" align="center"> Datos del servicio 
              Amazon S3 para el almacenamiento de archivos en la Nube</td>
          </tr>
		  <tr> 
            <td align="right">Nombre del Bucket en el servicio Amazon S3</td>
            <td><input name="s3_bukt" type="text" id="s3_bukt" size="80" maxlength="255">
            </td>
          </tr>
		  <tr> 
            <td align="right">Access Key</td>
            <td><input name="s3_user" type="text" id="s3_user" size="50" maxlength="50"></td>
          </tr>
		  <tr> 
            <td align="right">Contraseña</td>
            <td><input name="s3_pass" type="text" id="s3_pass" size="50" maxlength="50"> Dejar en blanco para no cambiar la contraseña</td>
          </tr>
	    </table>
	  </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
document.adm_bd_dlg.tomcat.value = '<% if(request.getParameter("tomcat") != null) { out.print( request.getParameter("tomcat") ); } else { out.print(map.get("TOMCAT")); } %>'
document.adm_bd_dlg.jvm.value = '<% if(request.getParameter("jvm") != null) { out.print( request.getParameter("jvm") ); } else { out.print(map.get("JVM")); } %>'
document.adm_bd_dlg.servidor.value = '<% if(request.getParameter("servidor") != null) { out.print( request.getParameter("servidor") ); } else { out.print(map.get("SERVIDOR")); } %>'
document.adm_bd_dlg.actualizar.value = '<% if(request.getParameter("actualizar") != null) { out.print( request.getParameter("actualizar") ); } else { out.print(map.get("ACTUALIZAR")); } %>'
document.adm_bd_dlg.postgresql.value = '<% if(request.getParameter("postgresql") != null) { out.print( request.getParameter("postgresql") ); } else { out.print(map.get("POSTGRESQL")); } %>'
document.adm_bd_dlg.respaldos.value = '<% if(request.getParameter("respaldos") != null) { out.print( request.getParameter("respaldos") ); } else { out.print(map.get("RESPALDOS")); } %>'
document.adm_bd_dlg.auto_hora.value = '<% if(request.getParameter("auto_hora") != null) { out.print( request.getParameter("auto_hora") ); } else { out.print(map.get("AUTO_HORA")); } %>'
document.adm_bd_dlg.auto_act.checked = <% if(request.getParameter("auto_act") != null ) { out.print("true"); } else { out.print(map.get("AUTO_ACT")); } %>  
document.adm_bd_dlg.auto_resp.checked = <% if(request.getParameter("auto_resp") != null ) { out.print("true"); } else { out.print(map.get("AUTO_RESP")); } %>  
document.adm_bd_dlg.auto_slds.checked = <% if(request.getParameter("auto_slds") != null ) { out.print("true"); } else { out.print(map.get("AUTO_SLDS")); } %>  
document.adm_bd_dlg.pac_purl.value = '<% if(request.getParameter("pac_purl") != null) { out.print( request.getParameter("pac_purl") ); } else { out.print(map.get("PAC_PURL")); } %>'
document.adm_bd_dlg.pac_serv.value = '<% if(request.getParameter("pac_serv") != null) { out.print( request.getParameter("pac_serv") ); } else { out.print(map.get("PAC_SERV")); } %>'
document.adm_bd_dlg.pac_user.value = '<% if(request.getParameter("pac_user") != null) { out.print( request.getParameter("pac_user") ); } else { out.print(map.get("PAC_USER")); } %>'

document.adm_bd_dlg.edi_purl.value = '<% if(request.getParameter("edi_purl") != null) { out.print( request.getParameter("edi_purl") ); } else { out.print(map.get("EDI_PURL")); } %>'
document.adm_bd_dlg.edi_user.value = '<% if(request.getParameter("edi_user") != null) { out.print( request.getParameter("edi_user") ); } else { out.print(map.get("EDI_USER")); } %>'

document.adm_bd_dlg.smtp_host.value = '<% if(request.getParameter("smtp_host") != null) { out.print( request.getParameter("smtp_host") ); } else { out.print(map.get("SMTP_HOST")); } %>'
document.adm_bd_dlg.smtp_port.value = '<% if(request.getParameter("smtp_port") != null) { out.print( request.getParameter("smtp_port") ); } else { out.print(map.get("SMTP_PORT")); } %>'
document.adm_bd_dlg.smtp_user.value = '<% if(request.getParameter("smtp_user") != null) { out.print( request.getParameter("smtp_user") ); } else { out.print(map.get("SMTP_USER")); } %>'

document.adm_bd_dlg.s3_bukt.value = '<% if(request.getParameter("s3_bukt") != null) { out.print( request.getParameter("s3_bukt") ); } else { out.print(map.get("S3_BUKT")); } %>'
document.adm_bd_dlg.s3_user.value = '<% if(request.getParameter("s3_user") != null) { out.print( request.getParameter("s3_user") ); } else { out.print(map.get("S3_USER")); } %>'

</script>
</body>
</html>
