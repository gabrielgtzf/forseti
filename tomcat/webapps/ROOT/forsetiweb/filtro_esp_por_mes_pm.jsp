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
<%@ page import="forseti.*, java.util.*, java.io.*"%>
<%
	String vista = request.getParameter("fsi_vista");
	Calendar fecha = GregorianCalendar.getInstance();
	int Ano = JUtil.obtAno(fecha);
    int Mes = JUtil.obtMes(fecha);
	String Meses = JUtil.Msj("GLB","GLB","GLB","MESES-ANO",2); 
 %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
<script language="JavaScript1.2">
<!--
function enviarlo(formAct)
{
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","FECHA",2)%>", formAct.ano.value, 2000, 2100))
		return false;
	else
		return true;
}
-->
</script>
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this);" action="/servlet/<%= vista %>" method="post" enctype="application/x-www-form-urlencoded" name="filtro_esp_mes">
<table bgcolor="#FFFFFF" width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><input name="tiempo" type="hidden" value="MAS"/><%= JUtil.Msj("GLB","GLB","GLB","FEM") %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="4" cellpadding="0">
          <tr>
    		<td width="50%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","FECHA",3)%>
              <select name="mes" size="1" class="cpoBco">
                <option value="1"<% if(Mes == 1) out.print(" selected"); %>><%= JUtil.Elm(Meses,1) %></option>
                <option value="2"<% if(Mes == 2) out.print(" selected"); %>><%= JUtil.Elm(Meses,2) %></option>
                <option value="3"<% if(Mes == 3) out.print(" selected"); %>><%= JUtil.Elm(Meses,3) %></option>
                <option value="4"<% if(Mes == 4) out.print(" selected"); %>><%= JUtil.Elm(Meses,4) %></option>
                <option value="5"<% if(Mes == 5) out.print(" selected"); %>><%= JUtil.Elm(Meses,5) %></option>
                <option value="6"<% if(Mes == 6) out.print(" selected"); %>><%= JUtil.Elm(Meses,6) %></option>
                <option value="7"<% if(Mes == 7) out.print(" selected"); %>><%= JUtil.Elm(Meses,7) %></option>
                <option value="8"<% if(Mes == 8) out.print(" selected"); %>><%= JUtil.Elm(Meses,8) %></option>
                <option value="9"<% if(Mes == 9) out.print(" selected"); %>><%= JUtil.Elm(Meses,9) %></option>
                <option value="10"<% if(Mes == 10) out.print(" selected"); %>><%= JUtil.Elm(Meses,10) %></option>
                <option value="11"<% if(Mes == 11) out.print(" selected"); %>><%= JUtil.Elm(Meses,11) %></option>
                <option value="12"<% if(Mes == 12) out.print(" selected"); %>><%= JUtil.Elm(Meses,12) %></option>
              </select></td>
    			
            <td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA",2)%>
              <input name="ano" type="text" id="ano" size="8" maxlength="4" value="<%= Ano %>"/></td>
  			</tr>
		</table>
	</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td align="right" valign="middle">
		<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
    	<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<%= vista %>';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/>
    </td>
  </tr>
</table>
</form>
</body>
</html>
