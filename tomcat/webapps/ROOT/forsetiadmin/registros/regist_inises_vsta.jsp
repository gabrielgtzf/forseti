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
	String regist_inises = (String)request.getAttribute("regist_inises");
	if(regist_inises == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp"); 
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesionAdmin(request).getSesion("REGIST_INISES").generarTitulo();
	String donde = JUtil.getSesionAdmin(request).getSesion("REGIST_INISES").generarWhere();
	String orden = JUtil.getSesionAdmin(request).getSesion("REGIST_INISES").generarOrderBy();
	String colvsta = JUtil.Msj("SAF","REGIST_INISES","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("SAF","REGIST_INISES","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
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
if(parent.tiempo.document.URL.indexOf('regist_inises_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiadmin/registros/regist_inises_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('regist_inises_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiadmin/registros/regist_inises_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('regist_inises_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiadmin/registros/regist_inises_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/SAFRegistIniSesDlg" method="post" enctype="application/x-www-form-urlencoded" name="regist_inises" target="_self">
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
				  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'TRUNCAR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","TRUNCAR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","TRUNCAR_REGISTRO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","TRUNCAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","TRUNCAR_REGISTRO",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ELIMINAR_REGISTRO');" src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","ELIMINAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","ELIMINAR_REGISTRO",2) %>" border="0">
                  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'DESBLOQUEAR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","DESBLOQUEAR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","DESBLOQUEAR_REGISTRO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","DESBLOQUEAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_INISES","VISTA","DESBLOQUEAR_REGISTRO",2) %>" border="0">
                  <a href="/servlet/SAFRegistIniSesCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
              	<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=IP&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=Host&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="15%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=ID_Sesion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=FechaDesde&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=FechaHasta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=FSIBD&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistIniSesCtrl?orden=ID_Usuario&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JBDRegistrarSet set = new JBDRegistrarSet(null);
	set.ConCat(true);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	
	for(int i = 0; i < set.getNumRows(); i++)
	{
%>
          <tr<%= (set.getAbsRow(i).getStatus().equals("B")) ? " class=\"txtChicoRj\"" : "" %>> 
            <td width="5%" align="center"> 
              <input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Registro() %>"></td>
            <td width="10%" align="left"><%= set.getAbsRow(i).getIP() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getHost() %></td>
			<td width="10%" align="left"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy") %></td>
			<td width="15%" align="left"><%= set.getAbsRow(i).getID_Sesion() %></td>
			<td width="10%" align="left"><%= JUtil.obtHoraTxt(set.getAbsRow(i).getHoraDesde(), "hh:mm:ss") %></td>
			<td width="10%" align="left"><%= JUtil.obtHoraTxt(set.getAbsRow(i).getHoraHasta(), "hh:mm:ss") %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getStatus() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getFSIBD() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getID_Usuario() %></td>
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
