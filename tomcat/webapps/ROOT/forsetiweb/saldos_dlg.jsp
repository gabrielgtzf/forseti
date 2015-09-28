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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	boolean registrado = JUtil.yaRegistradoEnFsi(request, response);
	// Inicia con registrar el objeto de sesion si no esta registrado
	if(!registrado) 
	{ 
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
 	}
	
	JPublicCXCConeSetV2 cxc = new JPublicCXCConeSetV2(request);
    JPublicCXPConeSetV2 cxp = new JPublicCXPConeSetV2(request);

	if(request.getParameter("va_tipo").equals("compras"))
	{
		cxp.m_Where = "Tipo = 'SAL' and DeSistema = '0' ";
		cxp.m_OrderBy = "ID_Concepto ASC ";
        cxp.Open();
	}
	else // es ventas
	{
		cxc.m_Where = "Tipo = 'SAL' and DeSistema = '0' ";
		cxc.m_OrderBy = "ID_Concepto ASC ";
        cxc.Open();
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript1.2" src="../compfsi/comps.js"></script>
<script language="JavaScript1.2">
<!-- 
function transferirResultados()
{
	if(document.saldos.idscon.value.substring(0,4) != 'FSI_')
	{
		alert("<%= JUtil.Msj("GLB","GLB","GLB","PAGOS-JSP",4) %>");
		return;
	}
	
	opener.document.<%= request.getParameter("formul") %>.fsipg_idscon.value = document.saldos.idscon.value;
	opener.document.<%= request.getParameter("formul") %>.fsipg_concepto.value = document.saldos.concepto.value;

	window.close();
	opener.document.<%= request.getParameter("formul") %>.submit();
}
-->
</script>
<title>Forseti</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>

<body background="../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form name="saldos" action="<%= request.getRequestURI() %>" method="get" target="_self">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","PAGOS-JSP",3) %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
      	<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td class="titChico">
				<table width="100%" border="0" cellpadding="0" cellspacing="3">
                <tr> 
                  <td> <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      
                      <tr> 
                        <td width="35%" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
                        <td valign="top"><select style="width: 90%;" name="idscon">
                            <%
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(cxp.getNumRows() > 0)
		{
%>
                            <option value="CONCEPTOS"<% if(request.getParameter("idscon") == null || request.getParameter("idscon").equals("CONCEPTOS")) 
								  							{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %> ---</option>
<%
								  for(int i = 0; i< cxp.getNumRows(); i++)
								  {
		                            out.print("<option value=\"FSI_" + cxp.getAbsRow(i).getID_Concepto() + "\""); 
									if(request.getParameter("idscon") != null && request.getParameter("idscon").equals("FSI_" + Short.toString(cxp.getAbsRow(i).getID_Concepto()))) 
								  		{ out.print(" selected>" + cxp.getAbsRow(i).getID_Concepto() + " - " + cxp.getAbsRow(i).getDescripcion() + "</option>"); } 
									else { out.print(">" + cxp.getAbsRow(i).getID_Concepto() + " - " + cxp.getAbsRow(i).getDescripcion() + "</option>"); } 
								  }
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
		if(cxc.getNumRows() > 0)
		{
%>
                            <option value="CONCEPTOS"<% if(request.getParameter("idscon") == null || request.getParameter("idscon").equals("CONCEPTOS")) 
								  							{ out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %> ---</option>
<%
								  for(int i = 0; i< cxc.getNumRows(); i++)
								  {
		                            out.print("<option value=\"FSI_" + cxc.getAbsRow(i).getID_Concepto() + "\""); 
									if(request.getParameter("idscon") != null && request.getParameter("idscon").equals("FSI_" + Short.toString(cxc.getAbsRow(i).getID_Concepto()))) 
								  		{ out.print(" selected>" + cxc.getAbsRow(i).getID_Concepto() + " - " + cxc.getAbsRow(i).getDescripcion() + "</option>"); } 
									else { out.print(">" + cxc.getAbsRow(i).getID_Concepto() + " - " + cxc.getAbsRow(i).getDescripcion() + "</option>"); } 
								  }
		}
		
	}
%>
                          </select></td>
                      </tr>
                      <tr> 
                        <td width="35%"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
                        <td> <input name="concepto" type="text" id="ref3" size="40" maxlength="50"> 
                        </td>
                      </tr>
                    </table></td>
                </tr>
                <tr> 
                  <td align="right">
				 	<input type="button" name="aceptar" onClick="javascript:transferirResultados();" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
    				<input type="button" name="cancelar" onClick="javascript:window.close()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
				  </td>
                </tr>
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
