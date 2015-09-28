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
	String ayuda_pagina_dlg = (String)request.getAttribute("ayuda_pagina_dlg");
	if(ayuda_pagina_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_AYUDAPAG").generarTitulo(JUtil.Msj("SAF","ADMIN_AYUDAPAG","VISTA",request.getParameter("proceso"),3));
	String tipos = JUtil.Msj("SAF","ADMIN_AYUDAPAG","VISTA","TIPOS");
	
	JAyudaPaginaModuloSet set = new JAyudaPaginaModuloSet(request,"fsi");
	if( !request.getParameter("proceso").equals("AGREGAR_AYUDA") )
	{
		set.ConCat(true);
		set.m_Where = "ID_Pagina = '" + JUtil.p(request.getParameter("id")) + "'";
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
// ------------------------------------------------
// FUNCIONES FORSETI

function enviarlo(formAct)
{
	if(!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.idpagina.value, 1, 8) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.descripcion.value, 1, 50) ||
		!esCadena("<%= JUtil.Msj("SAF","ADMIN_AYUDAPAG","DLG","TIT-ESP") %>", formAct.busqueda.value, 1, 254) )
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
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAyudaPaginaDlg" method="post" enctype="application/x-www-form-urlencoded" name="ayuda_pagina_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clock"> 
              <%  if(JUtil.getSesionAdmin(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAyudaPaginaCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
   <tr> 
    <td> 
	    <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td><div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></div></td>
            <td><input name="idpagina" type="text" size="12" maxlength="10"> </td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></div></td>
            <td><input name="descripcion" type="text" id="descripcion" size="40" maxlength="80"></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("SAF","ADMIN_AYUDAPAG","DLG","TIT-ESP") %></div></td>
            <td><input name="busqueda" type="text" id="busqueda" size="80" maxlength="254"></td>
          </tr>
		  <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></div></td>
            <td>
			<select name="tipo" class="cpoBco">
                <option value="COMPLETA"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("COMPLETA")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_AYUDA")) { 
											if(set.getAbsRow(0).getTipo().equals("COMPLETA")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(tipos,1) %></option>
                <option value="SENCILLA"<% 
					   				 if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("SENCILLA")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_AYUDA")) { 
											if(set.getAbsRow(0).getTipo().equals("SENCILLA")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(tipos,2) %></option>
              </select>
			</td>
          </tr>
		  <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %> 2</div></td>
            <td><input name="idalternativo" type="text" id="idalternativo" size="20" maxlength="30"></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp; </td>
          </tr>
         </table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
		JAyudaTipoSet ayu = new JAyudaTipoSet(null);
		ayu.m_OrderBy = "ID_Tipo ASC";
		ayu.ConCat(true);
		ayu.Open();
		for(int i = 0; i < ayu.getNumRows(); i++)
		{
%>
                <tr>
					<td width="10%" class="titChicoNeg"><%= ayu.getAbsRow(i).getID_Tipo() %></td>
				    <td class="titChicoNeg"><%= ayu.getAbsRow(i).getDescripcion() %></td>
				 </tr>
<%
			if(request.getParameter("proceso").equals("AGREGAR_AYUDA"))
			{
				JAyudaSubTipoSet st = new JAyudaSubTipoSet(null);
				st.m_OrderBy = "ID_SubTipo ASC";
				st.m_Where = "ID_Tipo = '" + JUtil.p(ayu.getAbsRow(i).getID_Tipo()) + "'";
				st.ConCat(true);
				st.Open();
				for(int j= 0; j < st.getNumRows(); j++)
				{
%>
				 <tr>
					<td width="5%"><input type="checkbox" name="SUB_<%= st.getAbsRow(j).getID_SubTipo() %>"  value=""<%= ((request.getParameter("SUB_" + st.getAbsRow(j).getID_SubTipo()) != null) ? " checked" : "") %>>&nbsp;&nbsp;<%= st.getAbsRow(j).getID_SubTipo() %></td>
				   	<td><%= st.getAbsRow(j).getDescripcion() %></td>
				 </tr>
<%
				}
			}
			else
			{
				JAyudaSubTipoVsPaginaSet st = new JAyudaSubTipoVsPaginaSet(null);
				st.m_OrderBy = "ID_SubTipo ASC";
				st.m_Where = "ID_Pagina = '" + JUtil.p(request.getParameter("id")) + "' and ID_Tipo = '" + JUtil.p(ayu.getAbsRow(i).getID_Tipo()) + "'";
				st.ConCat(true);
				st.Open();
				for(int j= 0; j < st.getNumRows(); j++)
				{
%>
				 <tr>
					<td width="5%"><input type="checkbox" name="SUB_<%= st.getAbsRow(j).getID_SubTipo() %>" value=""<% if( (request.getParameter("proceso").equals("CAMBIAR_AYUDA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_AYUDA") ) { out.print( (st.getAbsRow(j).getEnlazado() ? " checked" : "" ) ); } else if(request.getParameter("SUB_" + st.getAbsRow(j).getID_SubTipo()) != null ) { out.print(" checked"); } else { out.print(""); } %>> &nbsp;&nbsp;<%= st.getAbsRow(j).getID_SubTipo() %></td>
				   	<td><%= st.getAbsRow(j).getDescripcion() %></td>
				 </tr>
<%
				}			
			}
		}
%>
		 <tr> 
            <td colspan="2">&nbsp; 
              </td>
          </tr>
		 </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.ayuda_pagina_dlg.idpagina.value = '<% if(request.getParameter("idpagina") != null) { out.print( request.getParameter("idpagina") ); } else if(!request.getParameter("proceso").equals("AGREGAR_AYUDA")) { out.print( set.getAbsRow(0).getID_Pagina() ); } else { out.print(""); } %>'
document.ayuda_pagina_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_AYUDA")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.ayuda_pagina_dlg.busqueda.value = '<% if(request.getParameter("busqueda") != null) { out.print( request.getParameter("busqueda") ); } else if(!request.getParameter("proceso").equals("AGREGAR_AYUDA")) { out.print( set.getAbsRow(0).getBusqueda() ); } else { out.print(""); } %>' 
document.ayuda_pagina_dlg.idalternativo.value = '<% if(request.getParameter("idalternativo") != null) { out.print( request.getParameter("idalternativo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_AYUDA")) { out.print( set.getAbsRow(0).getID_Alternativo() ); } else { out.print(""); } %>' 
</script>
</body>
</html>
