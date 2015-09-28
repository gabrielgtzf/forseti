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
	String vales_caja_dlg = (String)request.getAttribute("vales_caja_dlg");
	if(vales_caja_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("BANCAJ_VALES").generarTitulo(JUtil.Msj("CEF","BANCAJ_VALES","VISTA",request.getParameter("proceso"),3));

	Calendar fecha = GregorianCalendar.getInstance();
	JCajasVsGenGastoSet sgas = new JCajasVsGenGastoSet(request);
	sgas.m_Where = "Clave = '" + JUtil.getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "' and Enlazado = '1'";
	sgas.m_OrderBy = "ID_EntidadCompra ASC";
	sgas.Open();
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFValesCajaDlg" method="post" enctype="application/x-www-form-urlencoded" name="vales_caja_dlg" target="_self">
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
        			<input type="button" name="cancelar" onclick="javascript:document.location.href='/servlet/CEFValesCajaCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
				  			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
							<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR">
                     <%= JUtil.Msj("GLB","GLB","GLB","ENTIDAD") %></td>
				  <td>
				  <select style="width: 90%;" name="identidad">
				     <option value="ENTIDAD"<% if(request.getParameter("identidad") == null || request.getParameter("identidad").equals("ENTIDAD")) 
								{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","ENTIDAD") %> ---</option>

<%
		if(sgas.getNumRows() > 0)
		{
			for(int i = 0; i< sgas.getNumRows(); i++)
			{
		    	out.print("<option value=\"" + sgas.getAbsRow(i).getID_EntidadCompra() + "\""); 
									if(request.getParameter("identidad") != null && request.getParameter("identidad").equals(Byte.toString(sgas.getAbsRow(i).getID_EntidadCompra()))) 
								  		{ out.print(" selected>" + sgas.getAbsRow(i).getDescripcion() + "</option>"); } 
									else { out.print(">" + sgas.getAbsRow(i).getDescripcion() + "</option>"); } 
			}
		}
%>
                	</select></td>
                </tr>
				<tr> 
                        <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
                        <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
                          <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
				</tr>				
                <tr> 
                  <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %></td>
                  <td><input name="referencia" type="text" id="referencia"  size="15" maxlength="20"></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table> </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.vales_caja_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else { out.print( JUtil.obtFechaTxt(fecha,"dd/MMM/yyyy") ); } %>' 
document.vales_caja_dlg.referencia.value = '<% if(request.getParameter("referencia") != null) { out.print( request.getParameter("referencia") ); } else { out.print(""); } %>' 
</script>
</body>
</html>
