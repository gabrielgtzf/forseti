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
<%@ page import="forseti.*, forseti.sets.*, java.util.*"%>
<%
	String crm_calendario = (String)request.getAttribute("crm_calendario");
	if(crm_calendario == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CRM_CALENDARIO").generarTitulo();
	String coletq = JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","COLUMNAS",1);
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
	top.location.href = "../forsetiweb/forseti.html"
}
if(parent.tiempo.document.URL.indexOf('crm_calendario_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/crm/crm_calendario_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('crm_calendario_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/crm/crm_calendario_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('crm_calendario_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/crm/crm_calendario_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFCRMCalendarioDlg" method="post" enctype="application/x-www-form-urlencoded" name="crm_calendario" target="_self">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
%>  
  <tr>
    <td bgcolor="#333333">
		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right">
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_TAREA',<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_TAREA",4) %>,<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_TAREA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_TAREA") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_TAREA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_ACTIVIDAD',<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_ACTIVIDAD",4) %>,<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_ACTIVIDAD",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_ACTIVIDAD") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","AGREGAR_ACTIVIDAD",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_REGISTRO',<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CAMBIAR_REGISTRO",4) %>,<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CAMBIAR_REGISTRO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CAMBIAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CAMBIAR_REGISTRO",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_REGISTRO',<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CONSULTAR_REGISTRO",4) %>,<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CONSULTAR_REGISTRO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CONSULTAR_REGISTRO") %>" alt="" title="<%= JUtil.Msj("CEF","CRM_CALENDARIO","VISTA","CONSULTAR_REGISTRO",2) %>" border="0">
              <a href="/servlet/CEFCRMCalendarioCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
            </div></td>
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
              <td class="titChico" width="50%" align="left">Ayer</td>
              <td class="titChico" align="right">Mañana</td>
			
  		  </tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="125" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" bordercolor="#0099FF" border="1" cellpadding="0" cellspacing="0">
<%
	for(int h=0; h < 24; h++)
	{
%>
		  <tr>
	      	<td bgcolor="#0099FF" class="titChico" width="5%" align="center"><% if(h<10) { out.print("0" + Integer.toString(h) + ":00"); } else { out.print(Integer.toString(h) + ":00"); } %></td>
			<td valign="top"><input type="radio" name="id" value="oky">12:00 - 13:30 <b>Reunion</b> Mi reunion con Turcato <span class="titChicoAzc">|</span> <input type="radio" name="id" value="oky">12:00 - 13:30 <b>Reunion</b> Mi reunion con Turcato</td>
  		  </tr>
<%
	}
%>		
     	</table>


        <table width="100%" bordercolor="#0099FF" border="1" cellpadding="0" cellspacing="0">
		  <tr bgcolor="#0099FF">
<%
	Calendar calendar = new GregorianCalendar();
	int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
	//out.println(year + "/" + month + "/" + dayOfMonth + " " + dayOfWeek + " " + weekOfYear + " " + weekOfMonth);
	calendar.add(Calendar.DAY_OF_MONTH,-(dayOfWeek));
	for(int i = 0; i < 7; i++)
	{
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // Jan = 0, not 1
%>
				<td width="14.28%" class="titChico" align="center">
<% 
		String sem;
		switch(i)
		{
			case 1:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-LUN",4);
			break;
			case 2:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-MAR",4);
			break;
			case 3:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-MIE",4);
			break;
			case 4:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-JUE",4);
			break;
			case 5:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-VIE",4);
			break;
			case 6:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-SAB",4);
			break;
			default:
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-DOM",4);
			break;
		}
		out.println(sem + " " + dayOfMonth); 
%>
				</td>
<%
	}
%>
		  </tr>
		  <tr>
<%
	for(int i = 0; i < 7; i++)
	{
%>
			<td width="14.28%" valign="top"><input type="radio" name="id" value="oky">
              12:00 - 13:30 <b>Reunion</b> Mi reunion con Turcato<br><input type="radio" name="id" value="oky">
              12:00 - 13:30 <b>Reunion</b> Mi reunion con Turcato<br></td>
<%
	}
%>
		  </tr>
		</table>
		
		
		
        <table width="100%" bordercolor="#0099FF" border="1" cellpadding="0" cellspacing="0">
		  <tr bgcolor="#0099FF">
<%
	for(int i = 0; i < 7; i++)
	{
%>
				<td width="14.28%" class="titChico" align="center">
<% 
		String sem;
		switch(i)
		{
			case 1:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-LUN",4);
			break;
			case 2:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-MAR",4);
			break;
			case 3:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-MIE",4);
			break;
			case 4:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-JUE",4);
			break;
			case 5:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-VIE",4);
			break;
			case 6:	
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-SAB",4);
			break;
			default:
				sem = JUtil.Msj("GLB","GLB","GLB","DIA-DOM",4);
			break;
		}
		out.println(sem); 
%>
				</td>
<%
	}
%>
		  </tr>
<%
	int dia [][] = new int[6][7];
	for(int i = 0; i < 5; i++)	{
		for(int j = 0; j < 7; j++)	{
			dia[i][j] = -1;
		}
	}
	//Get first day of month and number of days
	GregorianCalendar cal = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
	int nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
	int som = cal.get(GregorianCalendar.DAY_OF_WEEK);
	for(int i = 1; i <= nod; i++)
	{
		int row = new Integer((i+som-2)/7);
		int column = (i+som-2)%7;
		dia[row][column] = i;
		//out.println("ROW: " + row + " COL: " + column + " VAL: " + i + "<br>");
	}
	for(int i = 0; i < 5; i++)	
	{
%>
		 <tr>
<%
		for(int j = 0; j < 7; j++)	
		{
%>
			<td>
<%
			if(dia[i][j] != -1)
			{
%>
				<table width="100%" border="0" cellpadding="1" cellspacing="0">
					<tr>
						<td width="20%" align="center" bgcolor="#0099FF" class="titChico"><%= dia[i][j] %></td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><input type="radio" name="id" value="oky">
              							12:00 - 13:30 <b>Reunion</b> Mi reunion con Turcato<br><input type="radio" name="id" value="oky">
              							12:00 - 13:30 <b>Reunion</b> Mi reunion con Turcato<br></td>
					</tr>
				</table>
<%
			}
			else
				out.print("&nbsp;");
%>
			</td>
<%
			//out.println("ROW: " + i + " COL: " + j + " VAL: " + dia[i][j] + "<br>");
		}
%>
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
