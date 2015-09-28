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
	String etq = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","ETQ");
	String sts = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","STATUS");
	
	JAdmBancosCuentasSet set = new JAdmBancosCuentasSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") )
	{
		if(ent.equals("BANCOS"))
			set.m_Where = "Tipo = '0' and Clave = '" + JUtil.p(request.getParameter("id")) + "'";
		else
			set.m_Where = "Tipo = '1' and Clave = '" + JUtil.p(request.getParameter("id")) + "'";
			
		set.Open();
	}
	
	JPublicBancosCuentasSetV2 bc = new JPublicBancosCuentasSetV2(request);
	bc.m_OrderBy = "Clave ASC";
	bc.m_Where = "Tipo = '0'";
	bc.Open();
	JPublicBancosCuentasSetV2 cc = new JPublicBancosCuentasSetV2(request);
	cc.m_OrderBy = "Clave ASC";
	cc.m_Where = "Tipo = '1'";
	cc.Open();
	
	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();
	
	JAdmFormatosSet setFmt = new JAdmFormatosSet(request);
	setFmt.m_OrderBy = "ID_Formato ASC";
	setFmt.m_Where = ent.equals("BANCOS") ? "Tipo = 'BANCAJ_BANCOS'" : "Tipo = 'BANCAJ_CAJAS'";
	setFmt.Open();
	
	JContaPolizasClasificacionesSet setCls = new JContaPolizasClasificacionesSet(request);
	setCls.m_OrderBy = "ID_Clasificacion ASC";
	setCls.Open();
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
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.clave.value, 0, 254) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","FICHA") %>", formAct.ficha.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.descripcion.value, 1, 20) ||
		!verifCuenta("<%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %>", formAct.cuenta.value) ||
<%
	if(ent.equals("BANCOS"))
	{
%>
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CHEQUE") %>", formAct.sigcheque.value, 1, 9999999999)
<%
	}
	else
	{
%>
		!esNumeroEntero("<%= JUtil.Elm(etq,6) %>", formAct.ultimonumtrasp.value, 0, 9999999999) ||
		!esNumeroDecimal("<%= JUtil.Elm(etq,7) %>", formAct.fondotrasp.value, 0, 99999999999,2)
<%
	}	
%>		
		)
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
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmEntidadesDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_entidades_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmEntidadesCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
            <td width="50%">
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>"/>
				<input name="ENTIDAD" type="hidden" value="<%= ent %>"/>
                <input name="subproceso" type="hidden" value="ENVIAR"/>
                <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
			<td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
          </tr>
		  <tr>
             <td><input class="cpoColAzc" name="clave" type="text" style="width:50%" maxlength="3"<% if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD")) { out.print(" readonly=\"true\""); } %>/> 
             <td><input name="ficha" type="text" id="ficha" style="width:50%" maxlength="10"/></td>
          </tr>
		  <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
          </tr>
		  <tr>
		    <td colspan="2"><input name="descripcion" id="descripcion" type="text" style="width:100%" maxlength="20"/></td>
          </tr>
<%
	if(ent.equals("BANCOS"))
	{
%>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CHEQUE") %></td>
		  </tr>
		  <tr>
            <td colspan="2"><input name="sigcheque" type="text" id="sigcheque" style="width:35%" maxlength="12"></td>
		  </tr>
		  <tr>
		  	<td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","MONEDA") %></td>
          </tr>  
		    <td colspan="2"><select style="width: 90%;" name="idmoneda" class="cpoBco">
<%				      
		for(int i = 0; i< setMon.getNumRows(); i++)
		{
%>        
					<option value="<%= setMon.getAbsRow(i).getClave() %>"<% 
									if(request.getParameter("idmoneda") != null) 
									{
										if(request.getParameter("idmoneda").equals(Integer.toString(setMon.getAbsRow(i).getClave()))) 
										{
											out.print(" selected");
										}
									 } 
									 else 
									 {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
										{ 
											if(set.getAbsRow(0).getID_Moneda() == setMon.getAbsRow(i).getClave() ) 
											{
												out.println(" selected"); 
											}
										}
									 }	  %>><%=  setMon.getAbsRow(i).getMoneda()  %>
						</option>
<%
		}
%>
                      </select> </td>
          </tr>
<%
	}
%>
          <tr> 
            <td colspan="2"><%= JUtil.Elm(etq,1) %></td>
         </tr>
		 <tr>
		    <td colspan="2"> 
              <select style="width: 90%;" name="fmt_dep" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_dep") != null) {
										if(request.getParameter("fmt_dep").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Dep().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN",1) %> ---</option>
         <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_dep") != null) {
										if(request.getParameter("fmt_dep").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Dep().equals(setFmt.getAbsRow(i).getID_Formato())) {
												out.println(" selected"); 
											}
										}
									 }	  %>><%=  setFmt.getAbsRow(i).getDescripcion()  %></option>
                <%
								  }
				%>
                    </select> </td>
			</tr>
			<tr>
            	<td colspan="2"><%= JUtil.Elm(etq,2) %></td>
            </tr>
			<tr>
				<td colspan="2"> 
              <select style="width: 90%;" name="fmt_ret" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_ret") != null) {
										if(request.getParameter("fmt_ret").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Ret().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN",1) %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_ret") != null) {
										if(request.getParameter("fmt_ret").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Ret().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CUENTA") %></td>
     	  </tr>
		  <tr>
		    <td colspan="2"><table width="100%"><tr><td width="30%"><input name="cuenta" type="text" id="cuenta" style="width:100%" maxlength="25"/></td><td width="24"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg_pm.jsp?formul=adm_entidades_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS&destino=cuenta_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="24" height="24" border="0"></a></td> 
              <td><input name="cuenta_nombre" type="text" id="cuenta_nombre" style="width:100%" maxlength="250" readonly="true"/></td></table></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></td>
			<td><input type="checkbox" name="fija" value="">
              <%= JUtil.Elm(etq,3) %></td>
          </tr>
		    <td colspan="2"><select name="status" class="cpoBco" style="width:60%">
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
            
          </tr>
		  <tr>
		  	<td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CLASIFICACION") %></td>
		  </tr>
		  <tr>		
            <td colspan="2">
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
<%
	if(ent.equals("CAJAS"))
	{
%>
		  <tr> 
            <td><%= JUtil.Elm(etq,4) %></td>
			<td><input type="checkbox" name="todotrasp" value=""/><%= JUtil.Elm(etq,5) %></td>
		  </tr>
		  <tr>
            <td colspan="2">
			     <select style="width: 90%;" name="bancaj" class="cpoBco">
				      <option value="NINGUNA"<% if(request.getParameter("bancaj") != null) {
										if(request.getParameter("bancaj").equals("NINGUNA")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getTipoTRASP() == set.getAbsRow(0).getTipoTRASP() && set.getAbsRow(0).getClaveTRASP() == set.getAbsRow(0).getClave() ) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN",1) %> ---</option>
	                  <option value="BANCOS"<%  if(request.getParameter("bancaj") != null) {
										if(request.getParameter("bancaj").equals("BANCOS")) {
											out.println(" selected");
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","BANCOS",1) %> ---</option>
                                  <%
								  for(int i = 0; i< bc.getNumRows(); i++)
								  {
		%>        
					<option value="FSI_BAN_<%= bc.getAbsRow(i).getClave() %>"<% 
									if(request.getParameter("bancaj") != null) {
										if(request.getParameter("bancaj").equals("FSI_BAN_" + Byte.toString(bc.getAbsRow(i).getClave()))) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getTipoTRASP() == 0 && set.getAbsRow(0).getClaveTRASP() == bc.getAbsRow(i).getClave() ) {
												out.println(" selected"); 
											}
										}
									 }	  %>><%=  bc.getAbsRow(i).getCuenta()  %>
						</option>
		<%
								  }
		%>
                       <option value="CAJAS"<%  if(request.getParameter("bancaj") != null) {
										if(request.getParameter("bancaj").equals("CAJAS")) {
											out.println(" selected");
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS",1) %> ---</option>
          <%
								  for(int i = 0; i< cc.getNumRows(); i++)
								  {
		%>        
					<option value="FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>"<% 
									if(request.getParameter("bancaj") != null) {
										if(request.getParameter("bancaj").equals("FSI_CAJ_" + Byte.toString(cc.getAbsRow(i).getClave()))) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getTipoTRASP() == 1 && set.getAbsRow(0).getClaveTRASP() == cc.getAbsRow(i).getClave() ) {
												out.println(" selected"); 
											}
										}
									 }	  %>><%=  cc.getAbsRow(i).getCuenta()  %>
						</option>
		<%
								  }
		%>
              </select></td>
            
          </tr>
		  <tr> 
            <td><%= JUtil.Elm(etq,6) %></td>
			<td><%= JUtil.Elm(etq,7) %></td>
          </tr>
		  <tr>
            <td><input name="ultimonumtrasp" type="text" id="ultimonumtrasp" style="width:50%" maxlength="20"/></td>
		    <td><input name="fondotrasp" type="text" id="fondotrasp" style="width:50%" maxlength="12"></td>
          </tr> 
<%
	}
%>

          <tr> 
            <td colspan="2">&nbsp; </td>
          </tr>
        </table>
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_entidades_dlg.clave.value = '<% if(request.getParameter("clave") != null) { out.print( request.getParameter("clave") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getClave() ); } else { out.print(""); } %>'
document.adm_entidades_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.ficha.value = '<% if(request.getParameter("ficha") != null) { out.print( request.getParameter("ficha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getCuenta() ); } else { out.print(""); } %>' 
<%
	if(ent.equals("BANCOS"))
	{
%>
document.adm_entidades_dlg.sigcheque.value = '<% if(request.getParameter("sigcheque") != null) { out.print( request.getParameter("sigcheque") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getSigCheque() ); } else { out.print("1"); } %>'
<%
	}
	else
	{
%>
document.adm_entidades_dlg.fondotrasp.value = '<% if(request.getParameter("fondotrasp") != null) { out.print( request.getParameter("fondotrasp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getFondoTRASP() ); } else { out.print("0.00"); } %>'
document.adm_entidades_dlg.todotrasp.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") ) { out.print( (set.getAbsRow(0).getTodoTRASP() ? "true" : "false" ) ); } else if(request.getParameter("todotrasp") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.adm_entidades_dlg.ultimonumtrasp.value = '<% if(request.getParameter("ultimonumtrasp") != null) { out.print( request.getParameter("ultimonumtrasp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getUltimoNumTRASP() ); } else { out.print("0"); } %>'
<%
	}
%>
document.adm_entidades_dlg.cuenta.value = '<% if(request.getParameter("cuenta") != null) { out.print( request.getParameter("cuenta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( JUtil.obtCuentaFormato(set.getAbsRow(0).getCC(), request) ); } else { out.print(""); } %>'  
document.adm_entidades_dlg.cuenta_nombre.value = '<% if(request.getParameter("cuenta_nombre") != null) { out.print( request.getParameter("cuenta_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.fija.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") ) { out.print( (set.getAbsRow(0).getFijo() ? "true" : "false" ) ); } else if(request.getParameter("fija") != null ) { out.print("true"); } else { out.print("false"); } %>  
</script>
</body>
</html>
