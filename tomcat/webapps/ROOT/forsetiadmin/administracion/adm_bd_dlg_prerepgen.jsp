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
<%@ page import="forseti.*, fsi_admin.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	String adm_bd_dlg = (String)request.getAttribute("adm_bd_dlg");
	if(adm_bd_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarTitulo(JUtil.Msj("SAF","ADMIN_BD","VISTA",request.getParameter("proceso"),3));
	
	session = request.getSession(true);
    JRepGenSes rec = (JRepGenSes)session.getAttribute("rep_gen_dlg");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmBDDlg" method="post" enctype="application/x-www-form-urlencoded" name="rep_gen_dlg" target="_self">
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
					<input type="submit" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'DOCUMENTACION')" name="ayuda" value="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","DOCUMENTACION_REPORTE",3) %>" title="<%= JUtil.Msj("SAF","ADMIN_BD","VISTA","DOCUMENTACION_REPORTE",2) %>">
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
	  		<% if(request.getParameter("id") != null) { %><input name="id" type="hidden" value="<%= request.getParameter("id") %>"><% } %> 
			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>">
			<input name="subproceso" type="hidden" value="PROCESO">
	   </td>
  </tr>

  <tr> 
    <td>
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td width="20%">Base de datos de salida:</td>
            <td class="titCuerpoNar"><%= rec.getBDP() %></td>
          </tr>
          <tr> 
            <td>ID de Nuevo Reporte:</td>
            <td><input name="idreport" type="text" size="10" maxlength="7" value="<%=  rec.getID_Report() %>"> o dejalo en cero para cambios</td>
		  </tr>
		  <tr> 
            <td colspan="2"><hr></td>
		  </tr>
		  <tr> 
            <td colspan="2" class="titChicoNeg">O selecciona un reporte para cambiarlo o usarlo como plantilla para otro nuevo reporte...</td>
		  </tr>
		  <tr> 
            <td colspan="2">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
			JReportesSet set = new JReportesSet(request);
			set.ConCat(3);
			set.setBD(rec.getBDP());
			set.m_OrderBy = "ID_Report ASC";
			set.Open();
			
			for(int i = 0; i < set.getNumRows(); i++)
			{
%>
				  <tr<%= (set.getAbsRow(i).getID_Report() >= 10000) ? " class=\"txtChicoNar\"" : "" %>> 
				  	<td width="5%" align="center"><input type="radio" name="idreportplnt" value="<%=  set.getAbsRow(i).getID_Report() %>"></td>
					<td width="7%"><%= set.getAbsRow(i).getID_Report() %></td>
					<td><%= set.getAbsRow(i).getDescription() %></td>
					<td width="20%"><%= set.getAbsRow(i).getTipo() %></td>
				  </tr>
<%
			}
%>
				</table>
			</td>
		  </tr>
		</table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
