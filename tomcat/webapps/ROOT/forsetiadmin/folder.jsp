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
<%@ page import="forseti.JUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Folder Forseti</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="400" height="50"><img src="../imgfsi/folder_saf.gif" border="0" usemap="#Map"></td>
    <td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
  			<tr>
    			<td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="1"><img src="../imgfsi/pixel_blanco.gif" width="1" height="34"></td>
                <td><div align="right"><a href="/servlet/SAFRegistro" target="cuerpo"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0"></a><img src="../imgfsi/pixel_blanco.gif" width="5" height="24"><a href="/servlet/SAFSalir" target="cuerpo"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesionAdmin(request).getNombreUsuario() %>" width="24" height="24" border="0"></a><img src="../imgfsi/pixel_blanco.gif" width="5" height="24"><a href="http://www.forseti.org.mx/forsetidoc/040101.html" target="_blank"><img src="../imgfsi/ayuda.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"></a></div></td>
              </tr>
            </table></td>
  			</tr>
  			<tr>
    			
          <td><img src="../imgfsi/folder_portal_2.gif" height="16" width="100%"></td>
  			</tr>
		</table>
	</td>
  </tr>
</table>


<map name="Map">
  <area shape="rect" coords="84,29,210,42" href="http://www.forseti.org.mx" target="_blank">
</map>
</body>
</html>
