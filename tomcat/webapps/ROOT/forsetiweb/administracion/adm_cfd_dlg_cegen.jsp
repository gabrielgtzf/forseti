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
	String adm_cfdi_dlg = (String)request.getAttribute("adm_cfdi_dlg");
	if(adm_cfdi_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo = JUtil.getSesion(request).getSesion("ADM_CFDI").generarTitulo(JUtil.Msj("CEF","ADM_CFDI","VISTA",request.getParameter("proceso"),3));
	String ent = JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial();	
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
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.cfd_id_exprec.value, 0, 127))
		return false;
	else
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
		{
			return false;
		}
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmCFDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_cfd_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmCFDCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
          <tr align="center"> 
             <td colspan="2"><div align="center" class="titChicoNeg"> 
                      <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                      <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                      <input name="subproceso" type="hidden" value="ENVIAR">
                      </div></td>
           </tr>
<%
	if(ent.equals("CECAT"))
	{
%>
           <tr> 
           	 <td align="center" colspan="2">El catálogo de cuentas no necesita ningun atributo, simplemente acepta para comenzar la generación del XML</td>
           </tr>
<%	
	}
	else if(ent.equals("CEBAL"))
	{
%>
           <tr> 
           	 <td width="30%" align="right">Tipo de Envio</td>
             <td><input type="radio" name="tipoenvio" value="N"<% if(request.getParameter("tipoenvio") == null || request.getParameter("tipoenvio").equals("N")) { out.print(" checked");} %>>
              Normal&nbsp;&nbsp;<input type="radio" name="tipoenvio" value="C"<% if(request.getParameter("tipoenvio") != null && request.getParameter("tipoenvio").equals("C")) { out.print(" checked");} %>>
              Complementaria</td>
           </tr>
<%	
	}
	else //"CEPOL"
	{
%>
           <tr> 
           	 <td width="30%" align="right">Tipo de Solicitud</td>
             <td><input type="radio" name="tiposolicitud" value="AF"<% if(request.getParameter("tiposolicitud") == null || request.getParameter("tiposolicitud").equals("AF")) { out.print(" checked");} %>>
              Acto de Fiscalización&nbsp;<input type="radio" name="tiposolicitud" value="FC"<% if(request.getParameter("tiposolicitud") != null && request.getParameter("tiposolicitud").equals("FC")) { out.print(" checked");} %>>
              Fiscalización Compulsa&nbsp;<input type="radio" name="tiposolicitud" value="DE"<% if(request.getParameter("tiposolicitud") != null && request.getParameter("tiposolicitud").equals("DE")) { out.print(" checked");} %>>
              Devolución&nbsp;<input type="radio" name="tiposolicitud" value="CO"<% if(request.getParameter("tiposolicitud") != null && request.getParameter("tiposolicitud").equals("CO")) { out.print(" checked");} %>>
              Compensación</td>
           </tr>
		   <tr> 
			 <td width="30%" align="right">Número de Orden</td>
             <td><input name="numorden" type="text" id="numorden" size="35" maxlength="13"<% if(request.getParameter("numorden") != null) { out.print(" value=\"" + request.getParameter("numorden") + "\""); } %>></td>
           </tr>
		   <tr> 
			 <td width="30%" align="right">Número de Trámite</td>
             <td><input name="numtramite" type="text" id="numtramite" size="35" maxlength="10"<% if(request.getParameter("numtramite") != null) { out.print(" value=\"" + request.getParameter("numorden") + "\""); } %>></td>
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
