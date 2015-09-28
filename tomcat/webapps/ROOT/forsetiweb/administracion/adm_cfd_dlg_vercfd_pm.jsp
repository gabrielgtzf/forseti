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
	String adm_cfdi_dlg = (String)request.getAttribute("adm_cfdi_dlg");
	if(adm_cfdi_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %>", formAct.password.value, 1, 254))
		return false;
	else
		return true;
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmCFDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_cfd_dlg">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("CEF","ADM_CFDI","DLG","ETQ",2) %></td>
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
            <td width="20%">
                <input name="proceso" type="hidden" value="VERIFICAR_CFD"/>
                <%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %></td>
            <td> <input name="password" type="password" id="password" size="20" maxlength="254"/>
              <%= JUtil.Msj("CEF","ADM_CFDI","DLG","ETQ",3) %></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp; </td>
          </tr>
          <tr> 
            <td colspan="2" align="right">             	
			    <input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
    			<input type="button" name="cancelar" onClick="javascript:window.close()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/>
            </td>
          </tr>
        </table>
      </td>
  </tr>
</table>
</form>
</body>
</html>
