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
	String coletq = JUtil.Msj("CEF","ALM_CHFIS","DLG","COLUMNAS",1);
	int etq = 1;
	
	session = request.getSession(true);
    JAlmChFisSes rec = ( JAlmChFisSes)session.getAttribute("alm_chfis_dlg");
	
%>
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAlmChFisDlg" method="post" enctype="application/x-www-form-urlencoded" name="alm_chfis_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_CHFIS")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAlmChFisCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	   <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td>
			   <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%">
				  			<input name="ID" type="hidden" value="<%= request.getParameter("ID")%>">			
				  			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
							<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"><%= JUtil.Msj("GLB","GLB","GLB","BODEGA") %></td>
                  <td class="titChicoAzc"><%= rec.getBodega() %></td>
                </tr>
                 <tr> 
                  <td width="20%">
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
					<input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true" value="<%=  JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy") %>"> 
                    <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a>
<%
		}
%>			  
				  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr bgcolor="#0099FF"> 
                  <td width="20%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                  <td class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
				  <td width="15%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
				  <td width="5%" align="center" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
				  <td width="15%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                </tr>
<%
	for(int i = 0; i < rec.numPartidas(); i++)
	{
%>
                <tr> 
                  <td width="20%" align="left" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><%= rec.getPartida(i).getID_Prod() %></td>
                  <td class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><%= rec.getPartida(i).getDescripcion() %></td>
                  <td width="15%" align="right" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>">
<%
		if(request.getParameter("proceso").equals("CONSULTAR_CHFIS"))	
		{
			if(rec.getPartida(i).getCantidad() != 0) { out.print(rec.getPartida(i).getCantidad()); } else { out.print("---"); }
		}
		else
		{
%>
					<input name="FSI_CANT_<%= rec.getPartida(i).getID_Prod() %>" type="text" value="<% if(request.getParameter("FSI_CANT_" + rec.getPartida(i).getID_Prod()) == null) { out.print(rec.getPartida(i).getCantidad()); } else { out.print(request.getParameter("FSI_CANT_" + rec.getPartida(i).getID_Prod())); } %>" size="7" maxlength="15">
<%
		}
%>
				  </td>
                  <td width="5%" align="center" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><%= rec.getPartida(i).getUnidad() %></td>
				  <td width="15%" align="right" class="<%= (rec.getPartida(i).getManejado() ? "cpoBcoNg" : "cpoBco") %>"><% if(rec.getPartida(i).getDIFF() != 0) { out.print(rec.getPartida(i).getDIFF()); } else { out.print("&nbsp;"); } %></td>
                </tr>
               
<%
	}
%>                
              </table>
			</td>
          </tr>
           <tr>
            <td>&nbsp;
			 
			</td>
          </tr>
      </table> </td>
  </tr>
</table>
</form>
</body>
</html>
