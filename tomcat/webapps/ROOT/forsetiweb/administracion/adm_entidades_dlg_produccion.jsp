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
	String adm_entidades_dlg = (String)request.getAttribute("adm_entidades_dlg");
	if(adm_entidades_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String ent = JUtil.getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();

	String titulo = JUtil.getSesion(request).getSesion("ADM_ENTIDADES").generarTitulo(JUtil.Msj("CEF","ADM_ENTIDADES","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	String sts = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","STATUS");

	JAdmProduccionEntidades set = new JAdmProduccionEntidades(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") )
	{
		set.m_Where = "ID_EntidadProd = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	
	JAdmFormatosSet setFmt = new JAdmFormatosSet(request);
	setFmt.m_OrderBy = "ID_Formato ASC";
	setFmt.m_Where = "Tipo = 'COMP_COMPRAS' or Tipo = 'COMP_GASTOS'";
	setFmt.Open();
	
	JContaPolizasClasificacionesSet setCls = new JContaPolizasClasificacionesSet(request);
	setCls.m_OrderBy = "ID_Clasificacion ASC";
	setCls.Open();
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
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.identidad.value, 1, 254) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","BODEGA") %>", formAct.idbodegamp.value, 1, 254) ||
		//!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","BODEGA",3) %>", formAct.idbodegapt.value, 1, 254) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","FICHA") %>", formAct.ficha.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.ficha.value, 1, 80) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","SERIE") %>", formAct.serie.value, 1, 8) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","REPORTE") %>", formAct.numero.value, 1, 9999999999) )
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmEntidadesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_entidades_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmEntidadesCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td width="20%"> <div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                <input name="ENTIDAD" type="hidden" value="<%= ent %>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></div></td>
            <td width="30%"> <input class="cpoColAzc" name="identidad" type="text" id="identidad" size="7" maxlength="3"<% if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD")) { out.print(" readonly=\"true\""); } %>> 
            </td>
            <td width="20%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
            <td width="30%"><input name="ficha" type="text" id="ficha" size="15" maxlength="10"> 
            </td>
          </tr>
		  <tr> 
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></div></td>
            <td colspan="3"><input name="descripcion" type="text" id="descripcion" size="40" maxlength="80"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","SERIE") %></td>
            <td><input name="serie" type="text" id="serie" size="8" maxlength="8"></td>
			<td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","REPORTE") %></div></td>
            <td><input name="numero" type="text" id="numero" size="7" maxlength="10"></td>
          </tr>
		  <tr>
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","BODEGA") %></td>
            <td><input name="idbodegamp" type="text" id="idbodegamp" size="7" maxlength="10"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=adm_entidades_dlg&lista=idbodegamp&idcatalogo=20&nombre=BODEGAS&destino=idbodega_nombremp',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="idbodega_nombremp" type="text" id="idbodega_nombremp" size="25" maxlength="250" readonly="true"></td>
			<td align="right"><!--<%= JUtil.Msj("GLB","GLB","GLB","BODEGA",3) %>-->&nbsp;</td>
            <td><!--input name="idbodegapt" type="text" id="idbodegapt" size="7" maxlength="10"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=adm_entidades_dlg&lista=idbodegapt&idcatalogo=20&nombre=BODEGAS&destino=idbodega_nombrept',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="idbodega_nombrept" type="text" id="idbodega_nombrept" size="25" maxlength="250" readonly="true"-->&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","FORMATO") %></td>
            <td colspan="3">
              <select style="width: 90%;" name="formato" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("formato") != null) {
										if(request.getParameter("formato").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFormato().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("formato") != null) {
										if(request.getParameter("formato").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFormato().equals(setFmt.getAbsRow(i).getID_Formato())) {
												out.println(" selected"); 
											}
										}
									 }	  %>> 
                <%=  setFmt.getAbsRow(i).getDescripcion()  %>
                </option>
                <%
								  }
				%>
              </select></td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></div></td>
            <td> 
              <select name="status" class="cpoBco">
                <option value="V"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("V")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getStatus().equals("V")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,1) %></option>
                <option value="C"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("C")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getStatus().equals("C")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(sts,2) %></option>
              </select></td>
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","CLASIFICACION") %></td>
            <td> 
              <select style="width: 90%;" name="idclasificacion" class="cpoBco">
                <%				      
		for(int i = 0; i< setCls.getNumRows(); i++)
		{
%>
                <option value="<%= setCls.getAbsRow(i).getID_Clasificacion() %>"<% 
									if(request.getParameter("idclasificacion") != null) 
									{
										if(request.getParameter("idclasificacion").equals(setCls.getAbsRow(i).getID_Clasificacion())) 
										{
											out.print(" selected");
										}
									 } 
									 else 
									 {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
										{ 
											if(set.getAbsRow(0).getID_Clasificacion().equals(setCls.getAbsRow(i).getID_Clasificacion()) ) 
											{
												out.println(" selected"); 
											}
										}
									 }	  %>> 
                <%=  setCls.getAbsRow(i).getDescripcion()  %>
                </option>
                <%
		}
%>
              </select></td>
          </tr>
		  <tr> 
            <td colspan="4">&nbsp; </td>
          </tr>
        </table>
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_entidades_dlg.identidad.value = '<% if(request.getParameter("identidad") != null) { out.print( request.getParameter("identidad") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getID_EntidadProd() ); } else { out.print(""); } %>'
document.adm_entidades_dlg.ficha.value = '<% if(request.getParameter("ficha") != null) { out.print( request.getParameter("ficha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.serie.value = '<% if(request.getParameter("serie") != null) { out.print( request.getParameter("serie") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getSerie() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDoc() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.idbodegamp.value = '<% if(request.getParameter("idbodegamp") != null) { out.print( request.getParameter("idbodegamp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getID_BodegaMP() ); } else { out.print(""); } %>'
document.adm_entidades_dlg.idbodega_nombremp.value = '<% if(request.getParameter("idbodega_nombremp") != null) { out.print( request.getParameter("idbodega_nombremp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNombreBodegaMP() ); } else { out.print(""); } %>' 
//document.adm_entidades_dlg.idbodegapt.value = '<% if(request.getParameter("idbodegapt") != null) { out.print( request.getParameter("idbodegapt") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getID_BodegaPT() ); } else { out.print(""); } %>'
//document.adm_entidades_dlg.idbodega_nombrept.value = '<% if(request.getParameter("idbodega_nombrept") != null) { out.print( request.getParameter("idbodega_nombrept") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNombreBodegaPT() ); } else { out.print(""); } %>' 
</script>
</body>
</html>
