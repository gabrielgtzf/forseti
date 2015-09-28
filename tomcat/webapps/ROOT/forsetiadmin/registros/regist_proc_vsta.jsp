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
	String regist_proc = (String)request.getAttribute("regist_proc");
	if(regist_proc == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp"); 
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesionAdmin(request).getSesion("REGIST_PROC").generarTitulo();
	String donde = JUtil.getSesionAdmin(request).getSesion("REGIST_PROC").generarWhere();
	String orden = JUtil.getSesionAdmin(request).getSesion("REGIST_PROC").generarOrderBy();
	String colvsta = JUtil.Msj("SAF","REGIST_PROC","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("SAF","REGIST_PROC","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("SAF", "REGIST_PROC", "VISTA", "STATUS",2);
	
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
if(parent.tiempo.document.URL.indexOf('regist_proc_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiadmin/registros/regist_proc_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('regist_proc_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiadmin/registros/regist_proc_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('regist_proc_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiadmin/registros/regist_proc_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/SAFRegistProcDlg" method="post" enctype="application/x-www-form-urlencoded" name="regist_proc" target="_self">
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
				  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'TRUNCAR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","TRUNCAR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","TRUNCAR_REGISTRO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","TRUNCAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","TRUNCAR_REGISTRO",2) %>" border="0">
                  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'LIBERAR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","LIBERAR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","LIBERAR_REGISTRO",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","LIBERAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","LIBERAR_REGISTRO",2) %>" border="0">
                  <!--input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'RASTREAR_REGISTRO',<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","RASTREAR_REGISTRO",4) %>,<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","RASTREAR_REGISTRO",5) %>)" src="../imgfsi/<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","RASTREAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("SAF","REGIST_PROC","VISTA","RASTREAR_REGISTRO",2) %>" border="0"-->
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
			   	<td width="3%" align="center">&nbsp;</td>
              	<td width="8%" align="left"><a class="titChico" href="/servlet/SAFRegistProcCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistProcCtrl?orden=Fecha&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="10%" align="left"><a class="titChico" href="/servlet/SAFRegistProcCtrl?orden=FSIBD&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="7%" align="left"><a class="titChico" href="/servlet/SAFRegistProcCtrl?orden=ID_Usuario&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="12%" align="left"><a class="titChico" href="/servlet/SAFRegistProcCtrl?orden=ID_Modulo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="25%" align="left"><a class="titChico" href="/servlet/SAFRegistProcCtrl?orden=Proceso&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
				<td width="25%" align="left"><a class="titChico" href="/servlet/SAFRegistProcCtrl?orden=Resultado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JRegProcSet set = new JRegProcSet(null);
	set.ConCat(true);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i = 0; i < set.getNumRows(); i++)
	{
		String status, stgif;
		
		if(set.getAbsRow(i).getStatus().equals("OK"))
		{
			stgif = "ok.gif";	
			status = JUtil.Elm(sts,2); 
		} 
		else if(set.getAbsRow(i).getStatus().equals("ER"))
		{
			stgif = "er.gif";	
			status = JUtil.Elm(sts,3); 
		} 
		else if(set.getAbsRow(i).getStatus().equals("NA"))
		{
			stgif = "na.gif";	
			status = JUtil.Elm(sts,4); 
		} 
		else
		{
			stgif = "ia.gif";	
			status = JUtil.Elm(sts,5); 
		} 
		
%>
          <tr<%= (!set.getAbsRow(i).getStatus().equals("OK")) ? " class=\"txtChicoRj\"" : "" %>> 
            <td width="3%" align="center"> 
              <input type="radio" name="id" value="<%= set.getAbsRow(i).getID_RegProc() %>"></td>
            <td width="8%" align="left"><img src="../../imgfsi/<%= stgif %>" width="16" height="16" align="absmiddle">&nbsp;<%= status %></td>
			<td width="10%" align="left"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(set.getAbsRow(i).getHora(), "hh:mm:ss") %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getFSIBD() %></td>
			<td width="7%" align="left"><%= set.getAbsRow(i).getID_Usuario() %></td>
			<td width="12%" align="left"><%= set.getAbsRow(i).getID_Modulo() %></td>
			<td width="25%" align="left"><%= set.getAbsRow(i).getProceso() %></td>
			<td width="25%" align="left"><%= set.getAbsRow(i).getResultado() %></td>
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
