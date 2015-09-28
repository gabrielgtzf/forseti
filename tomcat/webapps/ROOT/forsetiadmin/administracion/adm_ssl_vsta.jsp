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
<%@ page import="forseti.*,java.io.*,fsi_admin.*,forseti.sets.*" %>
<%
	String adm_ssl = (String)request.getAttribute("adm_ssl");
	if(adm_ssl == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp"); 
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_SSL").generarTitulo();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!-- 
if(top.location == self.location) {
	top.location.href = "../forsetiadmin/forseti.html"
}
if(parent.tiempo.document.URL.indexOf('tiempo.html') == -1) {
	parent.tiempo.document.location.href = "../forsetiadmin/tiempo.html"
}
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiadmin/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiadmin/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/SAFAdmSSLDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_ssl" target="_self">
  <div id="topbar"> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr> 
        <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo  %></td>
      </tr>
      <%	
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje); 
	%>
      <tr> 
        <td bgcolor="#333333"> <table width="100%" border="0" cellpadding="0" cellspacing="5">
            <tr> 
              <td> <div align="right"> 
                  <input name="proceso" type="hidden" value="ACTUALIZAR">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_CERAF',<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONSULTAR_CERAF",4) %>,<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONSULTAR_CERAF",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONSULTAR_CERAF") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONSULTAR_CERAF",2) %>" border="0">
               	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'GENERAR_CERAF',<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","GENERAR_CERAF",4) %>,<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","GENERAR_CERAF",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","GENERAR_CERAF") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","GENERAR_CERAF",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'INSTALAR_CERAF',<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","INSTALAR_CERAF",4) %>,<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","INSTALAR_CERAF",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","INSTALAR_CERAF") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","INSTALAR_CERAF",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONFIAR_CERAF',<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONFIAR_CERAF",4) %>,<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONFIAR_CERAF",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONFIAR_CERAF") %>" alt="" title="<%= JUtil.Msj("SAF","ADMIN_SSL","VISTA","CONFIAR_CERAF",2) %>" border="0">
                  <a href="/servlet/SAFAdmSSLCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
 	             </div></td>
            </tr>
          </table></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr> 
        <td bgcolor="#FF6600">&nbsp;
		  </td>
      </tr>
    </table>
  </div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
  </tr>
  <tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
	String dirarch = "/usr/local/forseti/bin";
	JFsiFiltroMatch f = new JFsiFiltroMatch("forsetikeystore\\w+");
	File dir = new File(dirarch);	
	String [] dirlist = dir.list(f);
	
	for(int i=0; i < dirlist.length; i++)
	{
%>
          <tr>
	      	<td width="5%" align="center"><input type="radio" name="id" value="<%= dirlist[i] %>"></td>
	      	<td width="95%" align="left"><%= dirarch + "/" + dirlist[i] %></td>
          </tr>	
<%
	}
%>
        </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
