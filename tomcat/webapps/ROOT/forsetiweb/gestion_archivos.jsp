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
	String gestion_archivos = (String)request.getAttribute("gestion_archivos");
	if(gestion_archivos == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	JGestionArchivos gestion = (JGestionArchivos)request.getAttribute("gestion");
	String titulo = gestion.getTitulo();
	String vista =  gestion.getVista();
	String dialogo = gestion.getDialogo();
	String colvsta = JUtil.Msj("CEF","GESTION_S3","VISTA","COLUMNAS",1);
	int col = 1;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../compfsi/staticbar.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body background="../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/<%= dialogo %>" method="post" enctype="<%= (!gestion_archivos.equals("subir_archivo") ? "application/x-www-form-urlencoded" : "multipart/form-data") %>" name="gestion_archivos" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clockCef"> 
             		<input type="submit" name="aceptar"<%= (!gestion_archivos.equals("subir_archivo") ? " disabled=\"true\"" : "") %> value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<%= vista %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            		<input name="proceso" type="hidden" value="GESTIONAR_ARCHIVOS"> 
					<input name="procesoarchivo" type="hidden" value="NULL"> 
              		<input name="obj_id1" type="hidden" value="<%= gestion.getObj_ID1() %>">
					<input name="obj_id2" type="hidden" value="<%= gestion.getObj_ID2() %>">
			</td>
          </tr>
        </table> 
      </td>
    </tr>
<%
	if(!gestion_archivos.equals("subir_archivo"))
	{
%>
   <tr> 
     <td bgcolor="#333333"> 	
	   <table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right">
			 <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.procesoarchivo, 'SUBIR_ARCHIVO',<%= JUtil.Msj("CEF","GESTION_S3","VISTA","SUBIR_ARCHIVO",4) %>,<%= JUtil.Msj("CEF","GESTION_S3","VISTA","SUBIR_ARCHIVO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","GESTION_S3","VISTA","SUBIR_ARCHIVO") %>" alt="" title="<%= JUtil.Msj("CEF","GESTION_S3","VISTA","SUBIR_ARCHIVO",2) %>" border="0">
   			 <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.procesoarchivo, 'DESCARGAR_ARCHIVO',<%= JUtil.Msj("CEF","GESTION_S3","VISTA","DESCARGAR_ARCHIVO",4) %>,<%= JUtil.Msj("CEF","GESTION_S3","VISTA","DESCARGAR_ARCHIVO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","GESTION_S3","VISTA","DESCARGAR_ARCHIVO") %>" alt="" title="<%= JUtil.Msj("CEF","GESTION_S3","VISTA","DESCARGAR_ARCHIVO",2) %>" border="0">
   			 <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.procesoarchivo, 'ELIMINAR_ARCHIVO',<%= JUtil.Msj("CEF","GESTION_S3","VISTA","ELIMINAR_ARCHIVO",4) %>,<%= JUtil.Msj("CEF","GESTION_S3","VISTA","ELIMINAR_ARCHIVO",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","GESTION_S3","VISTA","ELIMINAR_ARCHIVO") %>" alt="" title="<%= JUtil.Msj("CEF","GESTION_S3","VISTA","ELIMINAR_ARCHIVO",2) %>" border="0">
		     </div>
			  </td>
  			</tr>
		</table>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
        <tr>
			<td class="titChico" width="3%" align="center">&nbsp;</td>
			<td class="titChico" width="50%" align="left"><%= JUtil.Elm(colvsta,col++) %></td>
			<td class="titChico" width="25%" align="left"><%= JUtil.Elm(colvsta,col++) %></td>
			<td class="titChico" width="25%" align="left"><%= JUtil.Elm(colvsta,col++) %></td>
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
	<tr> 
    	<td> 
			<table width="100%" border="0">
<%
		for(int i=0; i < gestion.getSet().getNumRows(); i++)
		{
%>
				<tr>
					<td width="3%" align="center"><input type="radio" name="id" value="<%= gestion.getSet().getAbsRow(i).getNombre() %>"></td>
					<td width="50%" align="left"><%= gestion.getSet().getAbsRow(i).getNombre() %></td>
					<td width="25%" align="left"><%= gestion.getSet().getAbsRow(i).getFecha() %></td>
					<td width="22%" align="left"><%= gestion.getSet().getAbsRow(i).getTamBites() %></td>
				</tr>		
<%
		}
%>		
			</table>
    	</td>
	</tr>
</table>
<%
	}
	else
	{
%>
	<tr> 
      <td>
	   <table width="100%" bgcolor="#FFFFFF" border="1" cellpadding="4" cellspacing="0">
          <tr>
			<td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","ARCHIVO") %></td>
			<td width="80%"><input name="archivo" type="file" size="50"></td>
	      </tr>
        </table> 
      </td>
    </tr>
</table>
</div>
<%
	}
%>

</form>
</body>
</html>
