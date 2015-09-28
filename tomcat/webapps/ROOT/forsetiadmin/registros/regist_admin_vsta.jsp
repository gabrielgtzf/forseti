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
<%@ page import="forseti.*,java.io.*,fsi_admin.*,forseti.sets.*,java.util.*" %>
<%
	String regist_admin = (String)request.getAttribute("regist_admin");
	if(regist_admin == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp"); 
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesionAdmin(request).getSesion("REGIST_ADMIN").generarTitulo();
	String ent = JUtil.getSesionAdmin(request).getSesion("REGIST_ADMIN").getEntidad();
	String tmp = JUtil.getSesionAdmin(request).getSesion("REGIST_ADMIN").getTiempo();
		
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
if(parent.tiempo.document.URL.indexOf('regist_admin_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiadmin/registros/regist_admin_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('regist_admin_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiadmin/registros/regist_admin_ent.jsp"
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
<form action="/servlet/SAFRegistAdminDlg" method="post" enctype="application/x-www-form-urlencoded" name="regist_admin" target="_self">
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
				  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ABRIR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","ABRIR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","ABRIR_REGISTRO",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","ABRIR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","ABRIR_REGISTRO",2) %>" border="0">
                  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'TRUNCAR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","TRUNCAR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","TRUNCAR_REGISTRO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","TRUNCAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","TRUNCAR_REGISTRO",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'LIBERAR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","LIBERAR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","LIBERAR_REGISTRO",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","LIBERAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_ADMIN","VISTA","LIBERAR_REGISTRO",2) %>" border="0">
                  <a href="/servlet/SAFRegistProcCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
                </div></td>
            </tr>
          </table></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr> 
        <td bgcolor="#FF6600">
		  <table width="100%" border="0" cellpadding="1" cellspacing="0">
            <tr> 
			   	<td width="5%" align="center">&nbsp;</td>
              	<td align="left">&nbsp;</td>
			</tr>
          </table></td>
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
	String dirarch = "/usr/local/forseti/log";
	String pat = ent + "-" + tmp + "-.+";
	System.out.println(pat);
	JFsiFiltroMatch f = new JFsiFiltroMatch(pat);
	File dir = new File(dirarch);	
	String [] dirlist = dir.list(f);
	Arrays.sort(dirlist);
	for(int i=0; i < dirlist.length; i++)
	{
%>
          <tr> 
            <td width="5%" align="center"><input type="radio" name="id" value="<%= dirlist[i] %>"></td>
	      	<td align="left"><%= dirarch + "/" + dirlist[i] %></td>
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
