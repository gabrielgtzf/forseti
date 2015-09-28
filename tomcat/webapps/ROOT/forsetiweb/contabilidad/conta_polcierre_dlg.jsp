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
<%@ page import="forseti.*, forseti.sets.*, forseti.contabilidad.*, java.util.*, java.io.*"%>
<% 
	String conta_polcierre_dlg = (String)request.getAttribute("conta_polcierre_dlg");
	if(conta_polcierre_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("CONT_POLCIERRE").generarTitulo(JUtil.Msj("CEF","CONT_POLCIERRE","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JContaPolizasDetalleCASet set = new JContaPolizasDetalleCASet (request);
	if( request.getParameter("proceso").equals("CAMBIAR_POLCIERRE") )
	{
		set.m_Where = "Mes = '13' and Ano = '" + JUtil.p(JUtil.getSesion(request).getSesion("CONT_POLCIERRE").getEspecial()) + "' and Cuenta = '" + JUtil.p(JUtil.obtCuentas(request.getParameter("id"),(byte)19)) + "'";
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
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esNumeroDecimal("Cargo (debe):", formAct.debe.value, -9999999999, 9999999999, 2) ||
		!esNumeroDecimal("Abono (haber):", formAct.haber.value, -9999999999, 9999999999, 2) ||
		!verifCuenta("Cuenta contable:", formAct.cuenta.value))
			return false;
	else
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFContaPolizasCierreDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_polcierre_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFContaPolizasCierreCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
        <tr> 
          <td width="30%" align="right"> 
            <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
              <input name="subproceso" type="hidden" value="ENVIAR">
              Cuenta:</td>
          <td > <input name="id" type="text" id="id" class="cpoColAzc" size="11" maxlength="25" value="<%  if(request.getParameter("id") != null) { out.print( request.getParameter("id") ); } else if(!request.getParameter("proceso").equals("AGREGAR_POLCIERRE")) { out.print( JUtil.obtCuentaFormato(set.getAbsRow(0).getCuenta(), request) ); } else { out.print(""); } %>"<%= (request.getParameter("proceso").equals("CAMBIAR_POLCIERRE")) ? " readonly=\"true\"" : "" %>>
            <% if(!request.getParameter("proceso").equals("CAMBIAR_POLCIERRE")) { %><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=conta_polcierre_dlg&lista=id&idcatalogo=3&nombre=CUENTAS&destino=descripcion',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a><% } %></td>
        </tr>
        <tr> 
          <td width="30%" align="right"> Descripcion:</td>
          <td><input name="descripcion" type="text" id="descripcion" readonly="true" size="50" maxlength="80"<% if(request.getParameter("descripcion") != null) { out.println(" value=\"" + request.getParameter("descripcion") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_POLCIERRE")) { out.println(" value=\"" + set.getAbsRow(0).getDescripcion() + "\""); } %>></td>
        </tr>
        <tr> 
          <td width="30%" align="right">Debe:</td>
          <td><input name="debe" type="text" id="debe" size="10" maxlength="12"<% if(request.getParameter("debe") != null) { out.println(" value=\"" + request.getParameter("debe") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_POLCIERRE")) { out.println(" value=\"" + set.getAbsRow(0).getDebe() + "\""); } else { out.println(" value=\"0\""); } %>>
            </td>
        </tr>
        <tr> 
          <td width="30%" align="right">Haber:</td>
          <td><input name="haber" type="text" id="haber" size="10" maxlength="12"<% if(request.getParameter("haber") != null) { out.println(" value=\"" + request.getParameter("haber") + "\""); }  
					else if(request.getParameter("proceso").equals("CAMBIAR_POLCIERRE")) { out.println(" value=\"" + set.getAbsRow(0).getHaber() + "\""); } else { out.println(" value=\"0\""); } %>>
            </td>
        </tr>
      </table>
     </td>
</tr> 
</table>
</form>
</body>
</html>
