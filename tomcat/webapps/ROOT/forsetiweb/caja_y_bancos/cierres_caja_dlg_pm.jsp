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
<%@ page import="forseti.*, forseti.sets.*, forseti.ventas.*, java.util.*, java.io.*"%>
<%
	String cierres_caja_dlg = (String)request.getAttribute("cierres_caja_dlg");
	if(cierres_caja_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("BANCAJ_CIERRES").generarTitulo(JUtil.Msj("CEF","BANCAJ_CIERRES","VISTA",request.getParameter("proceso"),3));
	String etq = JUtil.Msj("CEF","BANCAJ_CIERRES","DLG","ETQ");

	JVentasCierresSet setMod = new JVentasCierresSet(request);;
	JVenCierresStmpSet set = new JVenCierresStmpSet(request);
	String cierrez = (String)request.getAttribute("CIERREZ");
	
	if(request.getParameter("proceso").equals("AGREGAR_CIERRE") && cierrez != null)
	{
		String sql = "select * from  sp_cajas_cierrez('" + JUtil.getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial() +  "','0','" + request.getParameter("obs") + "') as ( ID_Cierre integer, Partida integer, Clave varchar, Descripcion varchar, Total numeric ) ";
 		set.setSQL(sql);
		set.Open();
	}
	else if(request.getParameter("proceso").equals("CONSULTAR_CIERRE"))
	{
		setMod.m_Where = "ID_Cierre = '" + JUtil.p(request.getParameter("ID")) + "'";
		setMod.Open();
		set.m_Where = "ID_Cierre = '" + JUtil.p(request.getParameter("ID")) + "'";
		set.m_OrderBy = "Partida ASC";
		set.Open();
	}
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
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
		return true;
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCierresCajaDlg" method="post" enctype="application/x-www-form-urlencoded" name="cierres_caja_dlg" target="_self">
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
              <%  if(request.getParameter("proceso").equals("CONSULTAR_CIERRE") || JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCierresCajaCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
			<td>
				<input name="subproceso" type="hidden" value="ENVIAR"/>
				<input name="tipomov" type="hidden" value="<%= request.getParameter("tipomov")%>"/> 
				<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/> 
				<input name="ENTIDAD" type="hidden" value="<%= JUtil.getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial() %>"/> 
<%
	if(request.getParameter("proceso").equals("AGREGAR_CIERRE"))
	{
%> 
				<input type="radio" name="traspasar" value="PRUEBA"<% if(request.getParameter("traspasar") == null || request.getParameter("traspasar").equals("PRUEBA")) { out.print(" checked");} %>/>
              		<%= JUtil.Elm(etq,1) %>&nbsp;<input type="radio" name="traspasar" value="CAMBIO"<% if(request.getParameter("traspasar") != null && request.getParameter("traspasar").equals("CAMBIO")) { out.print(" checked");} %>/>
              		<%= JUtil.Elm(etq,2) %>
<%
	}
%> 
			</td>
		</tr>  
		<tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","OBS") %></td>
        </tr>
        <tr> 
            <td><textarea name="obs" cols="60" rows="3" id="obs"></textarea></td>
        </tr>
       </table>
	   <table width="100%" border="0" cellspacing="5" cellpadding="5">
         <tr> 
           <td width="20%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
           <td width="50%" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
           <td align="right" class="titChicoAzc"><%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %></td>
         </tr>
                <%
	if( (request.getParameter("proceso").equals("AGREGAR_CIERRE") && cierrez != null) || request.getParameter("proceso").equals("CONSULTAR_CIERRE") )
	{
		for(int i = 0; i < set.getNumRows(); i++)
		{
%>
        <tr> 
           <td class="<% if(set.getAbsRow(i).getClave().equals("TIT")) { out.print("txtChicoAzc"); } 
							else if(set.getAbsRow(i).getClave().equals("ETQ")) { out.print("titChicoNeg"); } 
							else if(set.getAbsRow(i).getClave().equals("VAL")) { out.print("txtChicoAzc"); } 
							else if(set.getAbsRow(i).getClave().equals("ACU")) { out.print("txtCuerpoNg"); } 
							else if(set.getAbsRow(i).getClave().equals("OPT")) { out.print("txtChicoRj"); } 
							else { out.print("txtChicoNeg"); }%>"> 
                    <% if(set.getAbsRow(i).getClave().equals("TIT") ||  
							set.getAbsRow(i).getClave().equals("ETQ") || 
							set.getAbsRow(i).getClave().equals("ESP") || 
							set.getAbsRow(i).getClave().equals("ACU") || 
							set.getAbsRow(i).getClave().equals("VAL") ) { out.print("&nbsp;"); } else { out.print(set.getAbsRow(i).getClave()); } %></td>
           <td class="<% if(set.getAbsRow(i).getClave().equals("TIT")) { out.print("txtChicoAzc"); } 
				  			else if(set.getAbsRow(i).getClave().equals("ETQ")) { out.print("titChicoNeg"); } 
							else if(set.getAbsRow(i).getClave().equals("VAL")) { out.print("txtChicoAzc"); } 
							else if(set.getAbsRow(i).getClave().equals("ACU")) { out.print("txtCuerpoNg"); } 
							else if(set.getAbsRow(i).getClave().equals("OPT")) { out.print("txtChicoRj"); } 
							else { out.print("txtChicoNeg"); }%>"><%= set.getAbsRow(i).getDescripcion() %></td>
           <td class="<% if(set.getAbsRow(i).getClave().equals("TIT")) { out.print("txtChicoAzc"); } 
				  			else if(set.getAbsRow(i).getClave().equals("ETQ")) { out.print("titChicoNeg"); } 
							else if(set.getAbsRow(i).getClave().equals("VAL")) { out.print("txtChicoAzc"); } 
							else if(set.getAbsRow(i).getClave().equals("ACU")) { out.print("txtCuerpoNg"); } 
							else if(set.getAbsRow(i).getClave().equals("OPT")) { out.print("txtChicoRj"); } 
							else { out.print("txtChicoNeg"); }%>" align="right"> 
                    <% if(set.getAbsRow(i).getClave().equals("OPT") ||  
								set.getAbsRow(i).getClave().equals("ETQ") || 
								set.getAbsRow(i).getClave().equals("ESP") ) { out.print("&nbsp;"); } else { out.print(set.getAbsRow(i).getTotal()); } %></td>
         </tr>
<%
		}
	}
%>
       </table>
	 </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.cierres_caja_dlg.obs.value = '<% if(request.getParameter("obs") != null) { out.print( request.getParameter("obs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CIERRE")) { out.print( setMod.getAbsRow(0).getObs() ); } else { out.print(""); } %>'
</script>
</body>
</html>
