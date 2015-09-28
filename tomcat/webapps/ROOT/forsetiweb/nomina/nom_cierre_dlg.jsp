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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*" %>
<%
	String nom_cierre_dlg = (String)request.getAttribute("nom_cierre_dlg");
	if(nom_cierre_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo = JUtil.getSesion(request).getSesion("NOM_CIERRE").generarTitulo(JUtil.Msj("CEF","NOM_CIERRE","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JDiarioCierreSet set = new JDiarioCierreSet(request);
	if( request.getParameter("proceso").equals("CONSULTAR_CIERRE") )
	{
		set.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + JUtil.getSesion(request).getSesion("NOM_CIERRE").getEspecial() +
		 "' and ID_FechaMovimiento = '" + JUtil.p(request.getParameter("id")) + "'";
		 set.Open();
	}
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
	{
		formAct.aceptar.disabled = true;
		return true;
	}
	else
		return false;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomCierreDiarioDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_cierre_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomCierreDiarioCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%> 
  <tr> 
    <td> 
	    <table width="100%" border="0" cellspacing="3" cellpadding="0">
<%
	if(!request.getParameter("proceso").equals("CONSULTAR_CIERRE"))
	{
%>
          <tr> 
            <td width="20%">
			  <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
              <input name="subproceso" type="hidden" value="ENVIAR"> 
			  <input name="id" type="hidden" value="<%= request.getParameter("id")%>"> 
              <div align="right">Desde:</div></td>
            <td>
				<input name="desde" type="text" id="desde" size="12" maxlength="15" readonly="true" value="<%=   ( (request.getParameter("desde") == null) ? JUtil.obtFechaTxt(new Date(),"dd/MMM/yyyy")  :  request.getParameter("desde") )  %>"> 
           		<a href="javascript:NewCal('desde','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a> 
		    </td>
          </tr>
          <tr> 
            <td width="20%"> <div align="right">Hasta:</div></td>
            <td>
			    <input name="hasta" type="text" id="hasta" size="12" maxlength="15" readonly="true" value="<%=   ( (request.getParameter("hasta") == null) ? JUtil.obtFechaTxt(new Date(),"dd/MMM/yyyy")  :  request.getParameter("hasta") )  %>"> 
           		<a href="javascript:NewCal('hasta','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a> 
			</td>
          </tr>
<%
	}
	else
	{
%>
		  <tr> 
            <td colspan="2">
				<table  width="100%" border="0" cellspacing="0" cellpadding="2">
				<tr bgcolor="#0099FF">
		 		  <td width="7%" align="left" class="titChico">Clave</td>
		 		  <td width="25%" align="left" class="titChico">Nombre</td>
				  <td width="30%" align="left" class="titChico">Descripci&oacute;n</td>
		 		  <td width="7%" align="center" class="titChico">Desde</td>
    		 	  <td width="7%" align="center" class="titChico">Hasta</td>
    		 	  <td width="7%" align="center" class="titChico">Entrada</td>
    		 	  <td width="7%" align="center" class="titChico">Salida</td>
    		 	  <td width="5%" align="center" class="titChico">HA</td> 
                  <td width="5%" align="center" class="titChico">HP</td>
 			</tr>
<%
		for(int i=0; i < set.getNumRows(); i++)
		{
%>				  
			<tr>
		 		 <td width="7%" align="left"><%= set.getAbsRow(i).getID_Empleado()  %></td>
		 		 <td width="25%" align="left"><%= set.getAbsRow(i).getNombre()  %></td>
				 <td width="20%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
		 		 <td width="7%" align="center"><%= JUtil.obtHoraTxt(set.getAbsRow(i).getDesdeHora(), "HH:mm") %></td>
    		 	 <td width="7%" align="center"><%= JUtil.obtHoraTxt(set.getAbsRow(i).getHastaHora(), "HH:mm") %></td>
    		 	 <td width="12%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getEntrada(), "dd/MMM") + " " +  JUtil.obtHoraTxt(set.getAbsRow(i).getEntradaHora(), "HH:mm") %></td>
    		 	 <td width="12%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getSalida(), "dd/MMM") + " " +  JUtil.obtHoraTxt(set.getAbsRow(i).getSalidaHora(), "HH:mm") %></td>
    		 	 <td width="5%" align="center"><%= set.getAbsRow(i).getHNA() %></td>
     		 	 <td width="5%" align="center"><%= set.getAbsRow(i).getHNP() %></td>
 			</tr>
<%
		}
%>
				</table>
			</td>
          </tr>
<%
	}
%>
          
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
</script>
</body>
</html>
