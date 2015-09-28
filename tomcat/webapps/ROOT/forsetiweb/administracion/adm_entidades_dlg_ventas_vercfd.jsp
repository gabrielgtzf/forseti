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
<%@ page import="forseti.*"%>
<%
	String adm_entidades_dlg = (String)request.getAttribute("adm_entidades_dlg");
	if(adm_entidades_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String ent = JUtil.getSesion(request).getSesionAdmEntidades().getEspecial();
	String titulo = ent + ": Verificar Certificado";
%>
<html>
<head>
<title>Forseti - <%= titulo %></title>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esCadena("Contrasea:", formAct.password.value, 1, 254))
		return false;
	else
		return true;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../forsetiweb/estilos.css" rel="stylesheet" type="text/css"></head>

<body bgcolor="#999999" text="#000000" link="#FF6600" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#FF6600"><%= titulo %></td>
  </tr>
  <%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td> 
	  <form onSubmit="return enviarlo(this)" action="/servlet/forseti.admon.JAdmEntidadesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_entidades_dlg" target="_self">
        <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td width="20%"> <div align="right"> 
                <input name="proceso" type="hidden" value="VERIFICAR_CFD">
                Contrase&ntilde;a:</div></td>
            <td> <input name="password" type="password" id="password" size="20" maxlength="254">
              No se guardar&aacute; en la base de datos.</td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp; </td>
          </tr>
          <tr> 
            <td colspan="2" align="center"><input name="submit" type="image" src="../../forsetiweb/Aceptar.gif" border="0"> 
              <img src="../../forsetiweb/btn_gris.gif" width="2" height="20"> 
              <a href="javascript:window.close()"><img src="../../forsetiweb/Cancelar.gif" alt="Cierra la ventana " border="0"></a> 
            </td>
          </tr>
        </table>
      </form></td>
  </tr>
</table>
</body>
</html>
