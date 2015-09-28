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
	String etq = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","ETQ",3);
	String etqcont = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","ETQ-CONT",3);
	String sts = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","STATUS");

	JAdmComprasEntidades set = new JAdmComprasEntidades(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") )
	{
		set.m_Where = "ID_EntidadCompra = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	JAdmNotasBlocks notas = new JAdmNotasBlocks(request);
	notas.Open();

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
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","BODEGA") %>", formAct.idbodega.value, 1, 254) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","FICHA") %>", formAct.ficha.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","SERIE") %>", formAct.serie.value, 1, 8) ||
		!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","IVA") %>", formAct.ivaporcentual.value, 0, 99.99,2) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","RECEPCION") %> / <%= JUtil.Msj("GLB","GLB","GLB","GASTO") %>", formAct.numero.value, 1, 9999999999) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","ORDEN",2) %>", formAct.orden.value, 1, 9999999999) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","DEVOLUCION") %>", formAct.devolucion.value, 1, 9999999999)  )
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
            <td valign="top"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></div></td>
            <td valign="top"> <select name="tipo" class="cpoColAzc">
                <option value="0"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getID_TipoEntidad() == 0) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Msj("GLB","GLB","GLB","COMPRA") %></option>
                <option value="2"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getID_TipoEntidad() == 2) {
												out.print(" selected"); 
											}
										}
									 } %>><%= JUtil.Msj("GLB","GLB","GLB","GASTO") %></option>
              </select> </td>
            <td colspan="2"><input type="checkbox" name="fija" value="fija">
              <%= JUtil.Elm(etq,1) %><br> <input type="checkbox" name="fijacost" value="fijacost">
              <%= JUtil.Elm(etq,2) %></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","SERIE") %></td>
            <td><input name="serie" type="text" id="serie" size="8" maxlength="8"></td>
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","BODEGA") %></td>
            <td><input name="idbodega" type="text" id="idbodega" size="7" maxlength="10"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=adm_entidades_dlg&lista=idbodega&idcatalogo=32&nombre=BODEGAS&destino=idbodega_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="idbodega_nombre" type="text" id="idbodega_nombre" size="25" maxlength="250" readonly="true"></td>
          </tr>
          <tr> 
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","IVA") %></div></td>
            <td colspan="3"><input name="ivaporcentual" type="text" id="ivaporcentual" size="7" maxlength="10"></td>
          </tr>
          <tr> 
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","COMPRA") %> / <%= JUtil.Msj("GLB","GLB","GLB","GASTO") %></div></td>
            <td><input name="numero" type="text" id="numero" size="7" maxlength="10"></td>
            <td align="right"><%= JUtil.Elm(etq,3) %></td>
            <td>
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
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","ORDEN",2) %></div></td>
            <td><input name="orden" type="text" id="orden" size="7" maxlength="10"></td>
            <td align="right"><%= JUtil.Elm(etq,4) %></td>
            <td>
              <select style="width: 90%;" name="fmt_orden" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_orden") != null) {
										if(request.getParameter("fmt_orden").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Orden().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_orden") != null) {
										if(request.getParameter("fmt_orden").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Orden().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","RECEPCION",2) %></div></td>
            <td><input name="recepcion" type="text" id="recepcion" size="7" maxlength="10"></td>
            <td align="right"><%= JUtil.Elm(etqcont,1) %></td>
            <td>
              <select style="width: 90%;" name="fmt_recepcion" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_recepcion") != null) {
										if(request.getParameter("fmt_recepcion").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Recepcion().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_recepcion") != null) {
										if(request.getParameter("fmt_recepcion").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Recepcion().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DEVOLUCION") %></div></td>
            <td><input name="devolucion" type="text" id="devolucion" size="7" maxlength="10"></td>
            <td align="right"><%= JUtil.Elm(etq,5) %></td>
            <td>
              <select style="width: 90%;" name="fmt_devolucion" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_devolucion") != null) {
										if(request.getParameter("fmt_devolucion").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Devolucion().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_devolucion") != null) {
										if(request.getParameter("fmt_devolucion").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Devolucion().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
		  <!--tr align="center"> 
            <td colspan="4" class="titChicoAzc" ><%= JUtil.Msj("GLB","GLB","GLB","INFORMES") %></td>
		  </tr>
		  <tr> 
            <td height="15"> <div align="right"><%= JUtil.Elm(etq,6) %></div></td>
            <td><select name="infoplantoc" class="cpoBco" style="width: 90%;">
                <option value="-1"<% 
					  				if(request.getParameter("infoplantoc") != null) {
										if(request.getParameter("infoplantoc").equals("-1")) {
											out.print(" selected");
										}
									} else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getInfoPlantOC() == -1) {
												out.println(" selected"); 
											}
										}
									} %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  	for(int i = 0; i< notas.getNumRows(); i++)
								  	{
		%>
                <option value="<%= notas.getAbsRow(i).getID_Block() %>"<% 
										if(request.getParameter("infoplantoc") != null) {
											if(request.getParameter("infoplantoc").equals(Short.toString(notas.getAbsRow(i).getID_Block()))) {
												out.print(" selected");
											}
									 	} else {
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
												if(set.getAbsRow(0).getInfoPlantOC() == notas.getAbsRow(i).getID_Block() ) {
												out.println(" selected"); 
												}
											}
									 	}	  %>><%=  notas.getAbsRow(i).getDescripcion() %> 
                </option>
                <%
								  }
		%>
              </select></td>
            <td align="right"><%= JUtil.Elm(etq,7) %></td>
            <td><select name="infogasrec" class="cpoBco" style="width: 90%;">
                <option value="-1"<% 
					  				if(request.getParameter("infogasrec") != null) {
										if(request.getParameter("infogasrec").equals("-1")) {
											out.print(" selected");
										}
									} else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getInfoGasRec() == -1) {
												out.println(" selected"); 
											}
										}
									} %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  	for(int i = 0; i< notas.getNumRows(); i++)
								  	{
		%>
                <option value="<%= notas.getAbsRow(i).getID_Block() %>"<% 
										if(request.getParameter("infogasrec") != null) {
											if(request.getParameter("infogasrec").equals(Short.toString(notas.getAbsRow(i).getID_Block()))) {
												out.print(" selected");
											}
									 	} else {
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
												if(set.getAbsRow(0).getInfoGasRec() == notas.getAbsRow(i).getID_Block() ) {
												out.println(" selected"); 
												}
											}
									 	}	  %>><%=  notas.getAbsRow(i).getDescripcion() %> 
                </option>
                <%
								  }
		%>
              </select></td>
          </tr-->
		  <tr align="center"> 
            <td colspan="4" class="titChicoAzc" ><%= JUtil.Msj("GLB","GLB","GLB","ENLACES") %></td>
		  </tr>
          <tr> 
            <td align="right" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","BANCOS") %></td>
            <td valign="top"> <table width="100%" border="0" cellspacing="2" cellpadding="0">
<%
	if(request.getParameter("proceso").equals("AGREGAR_ENTIDAD"))
	{
		JAdmBancosCuentasSet bc = new JAdmBancosCuentasSet(request);
		bc.m_Where = "Tipo = '0'";
		bc.m_OrderBy = "Clave ASC"; 
		bc.Open();
		for(int i=0; i < bc.getNumRows(); i++)
		{		
%>
                <tr> 
                  <td><input type="checkbox" name="PER_BAN_<%= bc.getAbsRow(i).getClave() %>"  value=""<%= ((request.getParameter("PER_BAN_" + bc.getAbsRow(i).getClave()) != null) ? " checked" : "") %>> 
                    &nbsp;<%= bc.getAbsRow(i).getCuenta() %></td>
                </tr>
                <%
		}
	}
	else
	{
		JAdmBancosVsComprasSet bc = new JAdmBancosVsComprasSet(request);
		bc.m_Where = "Tipo = '0' and ID_EntidadCompra = '" + JUtil.p(request.getParameter("id")) + "'";
		bc.m_OrderBy = "Clave ASC"; 
		bc.Open();
		for(int i=0; i < bc.getNumRows(); i++)
		{
%>
                <tr> 
                  <td><input type="checkbox" name="PER_BAN_<%= bc.getAbsRow(i).getClave() %>" value=""<% if( (request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") ) { out.print( (bc.getAbsRow(i).getEnlazado() ? " checked" : "" ) ); } else if(request.getParameter("PER_BAN_" + bc.getAbsRow(i).getClave()) != null ) { out.print(" checked"); } else { out.print(""); } %>> 
                    &nbsp;<%= bc.getAbsRow(i).getCuenta() %></td>
                </tr>
                <%
		}
	}
%>
              </table></td>
            <td align="right" valign="top"><%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %></td>
            <td valign="top"> <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <%
	if(request.getParameter("proceso").equals("AGREGAR_ENTIDAD"))
	{
		JAdmBancosCuentasSet bc = new JAdmBancosCuentasSet(request);
		bc.m_Where = "Tipo = '1'";
		bc.m_OrderBy = "Clave ASC"; 
		bc.Open();
		for(int i=0; i < bc.getNumRows(); i++)
		{		
%>
                <tr> 
                  <td><input type="checkbox" name="PER_CAJ_<%= bc.getAbsRow(i).getClave() %>" value=""<%= ((request.getParameter("PER_CAJ_" + bc.getAbsRow(i).getClave()) != null) ? " checked" : "") %>> 
                    &nbsp;<%= bc.getAbsRow(i).getCuenta() %></td>
                </tr>
                <%
		}
	}
	else
	{
		JAdmBancosVsComprasSet bc = new JAdmBancosVsComprasSet(request);
		bc.m_Where = "Tipo = '1' and ID_EntidadCompra = '" + JUtil.p(request.getParameter("id")) + "'";
		bc.m_OrderBy = "Clave ASC"; 
		bc.Open();
		for(int i=0; i < bc.getNumRows(); i++)
		{
%>
                <tr> 
                  <td><input type="checkbox" name="PER_CAJ_<%= bc.getAbsRow(i).getClave() %>" value=""<% if( (request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") ) { out.print( (bc.getAbsRow(i).getEnlazado() ? " checked" : "" ) ); } else if(request.getParameter("PER_CAJ_" + bc.getAbsRow(i).getClave()) != null ) { out.print(" checked"); } else { out.print(""); } %>> 
                    &nbsp;<%= bc.getAbsRow(i).getCuenta() %></td>
                </tr>
                <%
		}
	}
%>
              </table></td>
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
document.adm_entidades_dlg.identidad.value = '<% if(request.getParameter("identidad") != null) { out.print( request.getParameter("identidad") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getID_EntidadCompra() ); } else { out.print(""); } %>'
document.adm_entidades_dlg.ficha.value = '<% if(request.getParameter("ficha") != null) { out.print( request.getParameter("ficha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.serie.value = '<% if(request.getParameter("serie") != null) { out.print( request.getParameter("serie") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getSerie() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.ivaporcentual.value = '<% if(request.getParameter("ivaporcentual") != null) { out.print( request.getParameter("ivaporcentual") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getIVA() ); } else { out.print("0.00"); } %>'
document.adm_entidades_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDoc() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.orden.value = '<% if(request.getParameter("orden") != null) { out.print( request.getParameter("orden") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getOrden() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.devolucion.value = '<% if(request.getParameter("devolucion") != null) { out.print( request.getParameter("devolucion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDevolucion() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.recepcion.value = '<% if(request.getParameter("recepcion") != null) { out.print( request.getParameter("recepcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getRecepcion() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.idbodega.value = '<% if(request.getParameter("idbodega") != null) { out.print( request.getParameter("idbodega") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getID_Bodega() ); } else { out.print(""); } %>'
document.adm_entidades_dlg.idbodega_nombre.value = '<% if(request.getParameter("idbodega_nombre") != null) { out.print( request.getParameter("idbodega_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNombreBodega() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.fija.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") ) { out.print( (set.getAbsRow(0).getFija() ? "true" : "false" ) ); } else if(request.getParameter("fija") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.adm_entidades_dlg.fijacost.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") ) { out.print( (set.getAbsRow(0).getFijaCost() ? "true" : "false" ) ); } else if(request.getParameter("fijacost") != null ) { out.print("true"); } else { out.print("false"); } %>  

</script>
</body>
</html>
