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

	JPublicBancosCuentasSetV2 cc = new JPublicBancosCuentasSetV2(request);
	cc.m_OrderBy = "Clave ASC";
	cc.m_Where = "Tipo = '1' and Clave <> '" + JUtil.getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "'";
	cc.Open();
	
	String etq = JUtil.Msj("CEF","BANCAJ_VALES","DLG","ETQ");
	String Tipos = JUtil.Msj("CEF","BANCAJ_VALES","DLG","TIPOS");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
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
                  <td width="40%"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR">
                   <%= JUtil.Elm(etq,1) %>
  </td>
                  <td> <select  class="cpoColAzc" style="width: 90%;" name="bancaj">
                      <option value="CAJAS"<% if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("CAJAS")) 
								  							{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %> ---</option>
                      <%
								  for(int i = 0; i< cc.getNumRows(); i++)
								  {
		                            out.print("<option value=\"FSI_CAJ_" + cc.getAbsRow(i).getClave() + "\""); 
									if(request.getParameter("bancaj") != null && request.getParameter("bancaj").equals("FSI_CAJ_" + Byte.toString(cc.getAbsRow(i).getClave()))) 
								  		{ out.print(" selected>" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
									else { out.print(">" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
								  }
								  %>
                    </select> </td>
                </tr>
				<tr> 
                  <td width="40%" valign="top"><%= JUtil.Elm(etq,2) %></td>
                  <td> 
				  	<input type="checkbox" name="FSI_F" value="true">&nbsp;<%= JUtil.Elm(Tipos,2) %><br>
					<input type="checkbox" name="FSI_A" value="true">&nbsp;<%= JUtil.Elm(Tipos,3) %><br>
                    <input type="checkbox" name="FSI_G" value="true">&nbsp;<%= JUtil.Elm(Tipos,4) %><br>
					<input type="checkbox" name="FSI_C" value="true">&nbsp;<%= JUtil.Elm(Tipos,5) %><br>
					<input type="checkbox" name="FSI_T" value="true">&nbsp;<%= JUtil.Elm(Tipos,6) %><br>
    				</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table> 
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.vales_caja_dlg.FSI_F.checked = <% if(request.getParameter("FSI_F") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.vales_caja_dlg.FSI_A.checked = <% if(request.getParameter("FSI_A") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.vales_caja_dlg.FSI_G.checked = <% if(request.getParameter("FSI_G") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.vales_caja_dlg.FSI_C.checked = <% if(request.getParameter("FSI_C") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.vales_caja_dlg.FSI_T.checked = <% if(request.getParameter("FSI_T") != null ) { out.print("true"); } else { out.print("false"); } %>  
</script>
</body>
</html>
