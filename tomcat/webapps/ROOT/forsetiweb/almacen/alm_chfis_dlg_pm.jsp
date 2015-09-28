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
<%@ page import="forseti.*, forseti.sets.*, forseti.almacen.*, java.util.*, java.io.*"%>
<%
	String alm_chfis_dlg = (String)request.getAttribute("alm_chfis_dlg");
	if(alm_chfis_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("ALM_CHFIS").generarTitulo(JUtil.Msj("CEF","ALM_CHFIS","VISTA",request.getParameter("proceso"),3));
	String coletq = JUtil.Msj("CEF","ALM_CHFIS","DLG","COLUMNAS",2);
	int etq = 1;
	
	session = request.getSession(true);
    JAlmChFisSes rec = ( JAlmChFisSes)session.getAttribute("alm_chfis_dlg");
	
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker_pm.js" >
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
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAlmChFisDlg" method="post" enctype="application/x-www-form-urlencoded" name="alm_chfis_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#333333">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="left" valign="middle">
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_CHFIS")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAlmChFisCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
  		  	<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040205.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 	  <td height="150" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td  bgcolor="#FFFFFF"> 
	
	   <table width="100%" border="0" cellspacing="5" cellpadding="5">
          <tr>
            <td width="30%">
			   	<input name="ID" type="hidden" value="<%= request.getParameter("ID")%>"/>			
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
				<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"/><%= JUtil.Msj("GLB","GLB","GLB","BODEGA") %></td>
            <td class="titChicoAzc"><%= rec.getBodega() %></td>
           </tr>
           <tr> 
             <td>
				<%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
             <td class="titChicoAzc">
<%
		if(request.getParameter("proceso").equals("CONSULTAR_CHFIS"))	
		{
				out.print(JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy"));
		}
		else
		{
%>
					<table width="100%"><tr><td><input name="fecha" type="text" id="fecha" style="width:100%" maxlength="15" readonly="true" value="<%=  JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy") %>"/></td><td width="24"> 
                    <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle" width="24" height="24"></a></td></tr></table>
<%
		}
%>			  
			  </td>
             </tr>
          </table>
		  <table bgcolor="#0099FF" width="100%" border="0" cellspacing="5" cellpadding="5">
            <tr> 
              <td width="20%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
              <td class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			  <td width="20%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			  <td width="10%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
			  <td width="20%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
            </tr>
		  </table>
		  <table width="100%" border="0" cellspacing="5" cellpadding="5">
<%
	for(int i = 0; i < rec.numPartidas(); i++)
	{
%>
                <tr> 
                  <td width="20%" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><%= rec.getPartida(i).getID_Prod() %></td>
                  <td class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><%= rec.getPartida(i).getDescripcion() %></td>
                  <td width="20%" align="right" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>">
<%
		if(request.getParameter("proceso").equals("CONSULTAR_CHFIS"))	
		{
			if(rec.getPartida(i).getCantidad() != 0) { out.print(rec.getPartida(i).getCantidad()); } else { out.print("---"); }
		}
		else
		{
%>
					<input name="FSI_CANT_<%= rec.getPartida(i).getID_Prod() %>" type="text" value="<% if(request.getParameter("FSI_CANT_" + rec.getPartida(i).getID_Prod()) == null) { out.print(rec.getPartida(i).getCantidad()); } else { out.print(request.getParameter("FSI_CANT_" + rec.getPartida(i).getID_Prod())); } %>" style="width:100%" maxlength="15"/>
<%
		}
%>
				  </td>
                  <td width="10%" align="center" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><%= rec.getPartida(i).getUnidad() %></td>
				  <td width="20%" align="right" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><% if(rec.getPartida(i).getDIFF() != 0) { out.print(rec.getPartida(i).getDIFF()); } else { out.print("&nbsp;"); } %></td>
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
