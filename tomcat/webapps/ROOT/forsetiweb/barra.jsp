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
<%@ page import="forseti.JUtil, java.util.*, forseti.sets.* " %>
<%
	JAdmVariablesSet set = new JAdmVariablesSet(null);
	set.m_Where = "ID_Variable = 'VERSION'";
	set.ConCat(true);
	set.Open();
	String version = set.getAbsRow(0).getVAlfanumerico();
	set.m_Where = "ID_Variable = 'SERVIDOR'";
	set.Open();
	String servidor = set.getAbsRow(0).getVAlfanumerico() + ":" + version;
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel=stylesheet href="../compfsi/estilos.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<SCRIPT LANGUAGE="JavaScript">
<!--
var rcgr = -1

function runClock()
{
	rcgr += 1;
	var timeNow = new Date();
	var hours = timeNow.getHours();
	var minutes = timeNow.getMinutes();
	var ampm = "";

    (minutes < 10) ? minutes = "0" + minutes : minutes;
    (hours < 12) ? ampm = "AM" : ampm = "PM";
	(hours > 12) ? hours = hours - 12 : hours;
	(hours == 0) ? hours = 12 : hours;
  
	var stringTime = " " + hours + ":" + minutes + " " + ampm;
	document.forms[0].clockBox.value=stringTime;
	if(rcgr >= 5)
		location.href = "/servlet/CEFBarra";
		
	setTimeout("runClock()", 60000);
}
//-->
</SCRIPT>
</head>
<body onLoad="javascript: runClock();" bgcolor="#0099FF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form>
  <table width="100%" border="0" cellspacing="0" cellpadding="2">
   <tr>
      <td width="5%" align="center" valign="top" class="clockCef"><strong><%= JUtil.Msj("GLB","BARRA","GLB","INFO", 1) %></strong></td>
      <td width="10%" align="left" valign="top" class="clockCef"><%= servidor %></td>
      <td width="5%" align="center" valign="top" class="clockCef"><strong><%= JUtil.Msj("GLB","BARRA","GLB","INFO", 2) %></strong></td>
      <td width="10%" align="left" valign="top" class="clockCef"><%= request.getRemoteHost() %></td>
	  <td width="5%" align="center" valign="top" class="clockCef"><strong><%= JUtil.Msj("GLB","BARRA","GLB","INFO", 4) %></strong></td>
      <td width="20%" align="left" valign="top" class="clockCef"><%= JUtil.getSesion(request).getNombreCompania() %></td>
      <td width="5%" align="center" valign="top" class="clockCef"><strong><%= JUtil.Msj("GLB","BARRA","GLB","INFO", 3) %></strong></td>
      <td align="left" valign="top" class="clockCef"><%= JUtil.getSesion(request).getNombreUsuario() %></td>
	  <td width="7%" align="right" valign="top" class="clockCef" ><%= JUtil.obtFechaTxt(new java.util.Date(),"dd/MMM/yyyy" ) %></td>
      <td width="7%" align="left" valign="top"><input name="clockBox" type="text" class="clockCef" onFocus="blur();" size="10" maxlength="10"></td>
	</tr>
</table>
</form>
</body>
</html>
