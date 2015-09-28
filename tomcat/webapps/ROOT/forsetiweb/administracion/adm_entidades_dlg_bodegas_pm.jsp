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
	String etq = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","ETQ",2);
	String sts = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","STATUS");

	JAdmInvservBodegasSet set = new JAdmInvservBodegasSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") )
	{
		set.m_Where = "ID_Bodega = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	JAdmFormatosSet setFmt = new JAdmFormatosSet(request);
	setFmt.m_OrderBy = "ID_Formato ASC";
	setFmt.m_Where = "Tipo = 'ALM_MOVIM' or Tipo = 'ALM_MOVPLANT' or Tipo = 'ALM_TRASPASOS' or Tipo = 'ALM_REQUERIMIENTOS' or Tipo = 'ALM_CHFIS'";
	setFmt.Open();
	JContaPolizasClasificacionesSet setCls = new JContaPolizasClasificacionesSet(request);
	setCls.m_OrderBy = "ID_Clasificacion ASC";
	setCls.Open();
	JCFDExpRecSet exp = new JCFDExpRecSet(request,"EXP");
	exp.Open();
	JCFDExpRecSet rec = new JCFDExpRecSet(request,"REC");
	rec.Open();
	JCFDCertificadosSet cer = new JCFDCertificadosSet(request);
	cer.Open();
	JCFDFoliosSet fol = new JCFDFoliosSet(request);
	fol.Open();
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
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.idbodega.value, 1, 254) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","FICHA") %>", formAct.ficha.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.descripcion.value, 1, 254) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","MOVIMIENTO") %>", formAct.numero.value, 1, 9999999999) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","PLANTILLA") %>", formAct.plantilla.value, 1, 9999999999) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","TRASPASO") %>", formAct.salida.value, 1, 9999999999) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","REQUERIMIENTO") %>", formAct.requerimiento.value, 1, 9999999999) ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CHEQUEO") %>", formAct.numchfis.value, 1, 9999999999)  )
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
		    <td> <input class="cpoColAzc" name="idbodega" type="text" style="width:50%" maxlength="3"<% if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD")) { out.print(" readonly=\"true\""); } %>/></td>
            <td><input name="ficha" type="text" id="ficha" style="width:50%" maxlength="10"/></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
			<td><input type="checkbox" name="fija" value=""><%= JUtil.Elm(etq,8) %></td>
          </tr>
		  <tr>
		  	<td colspan="2"><input name="descripcion" type="text" id="numero3" style="width:100%" maxlength="254"/></td>
          </tr>	
		  <tr>
		  	<td><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></td>
			<td><%= JUtil.Msj("GLB","GLB","GLB","STATUS") %></td>
          </tr>
		  <tr>
		    <td> 
              <select style="width:100%" class="cpoColAzc" name="idinvserv">
                <option value="P"<% 
					   				 if(request.getParameter("idinvserv") != null) {
										if(request.getParameter("idinvserv").equals("P")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getID_InvServ().equals("P")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,1) %></option>
                <option value="G"<% 
					   				 if(request.getParameter("idinvserv") != null) {
										if(request.getParameter("idinvserv").equals("G")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getID_InvServ().equals("G")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,2) %></option>
              </select></td>
			  <td> 
              <select style="width:100%" name="status" class="cpoBco">
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
		  	 <td><%= JUtil.Msj("GLB","GLB","GLB","MOVIMIENTO") %></td> 
             <td><%= JUtil.Elm(etq,3) %></td>
          </tr>
		  <tr>
		     <td><input name="numero" type="text" id="numero" style="width:50%" maxlength="10"/></td>
		     <td>
              <select style="width: 90%;" name="fmt_movimientos" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_movimientos") != null) {
										if(request.getParameter("fmt_movimientos").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Movimientos().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_movimientos") != null) {
										if(request.getParameter("fmt_movimientos").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Movimientos().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td><%= JUtil.Msj("GLB","GLB","GLB","PLANTILLA") %></div></td>
            <td><%= JUtil.Elm(etq,4) %></td>
          </tr>
		  <tr>
			<td><input name="plantilla" type="text" id="plantilla" style="width:50%" maxlength="10"/></td>
		    <td>
              <select style="width: 90%;" name="fmt_plantillas" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_plantillas") != null) {
										if(request.getParameter("fmt_plantillas").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Plantillas().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_plantillas") != null) {
										if(request.getParameter("fmt_plantillas").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Plantillas().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td><%= JUtil.Msj("GLB","GLB","GLB","TRASPASO") %></div></td>
			<td><%= JUtil.Elm(etq,5) %></td>
          </tr>
		  <tr>
		  	<td><input name="salida" type="text" id="salida" style="width:50%" maxlength="10"/></td>
            <td> 
              <select style="width: 100%;" name="fmt_traspasos" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_traspasos") != null) {
										if(request.getParameter("fmt_traspasos").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Traspasos().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_traspasos") != null) {
										if(request.getParameter("fmt_traspasos").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Traspasos().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td><%= JUtil.Msj("GLB","GLB","GLB","REQUERIMIENTO") %></td>
            <td><%= JUtil.Elm(etq,6) %></td>
		  </tr>
		  <tr>
            <td><input name="requerimiento" type="text" id="requerimiento" style="width:50%" maxlength="10"/></td>
             <td> 
              <select style="width: 100%;" name="fmt_requerimientos" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_requerimientos") != null) {
										if(request.getParameter("fmt_requerimientos").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Requerimientos().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_requerimientos") != null) {
										if(request.getParameter("fmt_requerimientos").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Requerimientos().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td><%= JUtil.Msj("GLB","GLB","GLB","CHEQUEO") %></td>
            <td><%= JUtil.Elm(etq,7) %></td>
          </tr>
		  <tr>
		  	<td><input name="numchfis" type="text" id="numchfis" style="width:50%" maxlength="10"/></td>
            <td> 
              <select style="width: 100%;" name="fmt_chfis" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_chfis") != null) {
										if(request.getParameter("fmt_chfis").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_ChFis().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_chfis") != null) {
										if(request.getParameter("fmt_chfis").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_ChFis().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CLASIFICACION") %></td>
		  </tr>
		  <tr>
		    <td colspan="2"><select style="width: 90%;" name="idclasificacion" class="cpoBco">
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
            <td colspan="2" class="titChicoAzc" align="center"><%= JUtil.Msj("GLB","GLB","GLB","CFD") %></td>
          </tr>
		  <tr> 
<%
	if(!request.getParameter("proceso").equals("CONSULTAR_ENTIDAD"))
	{
%>
                  <td valign="top"><%= JUtil.Msj("GLB","GLB","GLB","CFD",2) %>:</td>
                  <td><input type="radio" name="cfd" value="00"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("00")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD") && set.getAbsRow(0).getCFD().equals("00")) { out.print(" checked"); } else { out.print(" checked"); } %>/>
                    <%= JUtil.Msj("GLB","GLB","GLB","NO") %><br> 
                    <input type="radio" name="cfd" value="01"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("01")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD") && set.getAbsRow(0).getCFD().equals("01")) { out.print(" checked"); } else { out.print(""); } %>/>
                    <%= JUtil.Msj("GLB","GLB","GLB","CFD",4) %><br>
					<input type="radio" name="cfd" value="10"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("10")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD") && set.getAbsRow(0).getCFD().equals("10")) { out.print(" checked"); } else { out.print(""); } %>/>
                    <%= JUtil.Msj("GLB","GLB","GLB","CFD",3) %> </td>
<%
	}
	else
	{
%>
				  <td><%= JUtil.Msj("GLB","GLB","GLB","CFD",2) %>:</td>
                  <td class="txtChicoAzc"> <%
		if(set.getAbsRow(0).getCFD().equals("10"))
			out.print(JUtil.Msj("GLB","GLB","GLB","CFD",4));
		else if(set.getAbsRow(0).getCFD().equals("01"))
			out.print(JUtil.Msj("GLB","GLB","GLB","CFD",3));
		else
			out.print(JUtil.Msj("GLB","GLB","GLB","NO")); 
%> 
				</td>
<%
	}
%>
				</tr>
                <tr> 
                  <td colspan="2" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","EXPEDICION") %></td>
                </tr>  
				<tr> 
                  <td colspan="2"><select name="cfd_id_expedicion" class="cpoBco" style="width: 90%;">
                      <option value="0"<% 
					  				if(request.getParameter("cfd_id_expedicion") != null) {
										if(request.getParameter("cfd_id_expedicion").equals("0")) {
											out.print(" selected");
										}
									} else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getCFD_ID_Expedicion() == 0) {
												out.println(" selected"); 
											}
										}
									} %>>--- <%= JUtil.Msj("GLB","GLB","GLB","EXPEDICION") %> ---</option>
                      <%
								  	for(int i = 0; i< exp.getNumRows(); i++)
								  	{
		%>
                      <option value="<%= exp.getAbsRow(i).getCFD_ID_ExpRec() %>"<% 
										if(request.getParameter("cfd_id_expedicion") != null) {
											if(request.getParameter("cfd_id_expedicion").equals(Byte.toString(exp.getAbsRow(i).getCFD_ID_ExpRec()))) {
												out.print(" selected");
											}
									 	} else {
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
												if(set.getAbsRow(0).getCFD_ID_Expedicion() == exp.getAbsRow(i).getCFD_ID_ExpRec() ) {
												out.println(" selected"); 
												}
											}
									 	}	  %>><%=  exp.getAbsRow(i).getCFD_Nombre() %> 
                      </option>
                      <%
								  }
		%>
                    </select></td>
				</tr>
				<tr>
				  <td colspan="2" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","CERTIFICADO") %></td>
                </tr>
				<tr>  
				  <td colspan="2"><select class="cpoBco" style="width: 90%;" name="cfd_nocertificado">
                      <option value=""<% 
					  				if(request.getParameter("cfd_nocertificado") != null) {
										if(request.getParameter("cfd_nocertificado").equals("")) {
											out.print(" selected");
										}
									} else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getCFD_NoCertificado().equals("")) {
												out.println(" selected"); 
											}
										}
									} %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CERTIFICADO") %> ---</option>
                      <%
								  	for(int i = 0; i< cer.getNumRows(); i++)
								  	{
		%>
                      <option value="<%= cer.getAbsRow(i).getCFD_NoCertificado() %>"<% 
										if(request.getParameter("cfd_nocertificado") != null) {
											if(request.getParameter("cfd_nocertificado").equals(cer.getAbsRow(i).getCFD_NoCertificado())) {
												out.print(" selected");
											}
									 	} else {
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
												if(set.getAbsRow(0).getCFD_NoCertificado().equals(cer.getAbsRow(i).getCFD_NoCertificado()) ) {
												out.println(" selected"); 
												}
											}
									 	}	  %>><%=  cer.getAbsRow(i).getCFD_ArchivoCertificado() %> </option>
                      <%
								  }
		%>
                    </select> </td>
				</tr>
				<tr> 
            		<td colspan="2">&nbsp; </td>
             </tr>
         </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_entidades_dlg.idbodega.value = '<% if(request.getParameter("idbodega") != null) { out.print( request.getParameter("idbodega") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getID_Bodega() ); } else { out.print(""); } %>'
document.adm_entidades_dlg.ficha.value = '<% if(request.getParameter("ficha") != null) { out.print( request.getParameter("ficha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNumero() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.plantilla.value = '<% if(request.getParameter("plantilla") != null) { out.print( request.getParameter("plantilla") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getPlantilla() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.salida.value = '<% if(request.getParameter("salida") != null) { out.print( request.getParameter("salida") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getSalida() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.requerimiento.value = '<% if(request.getParameter("requerimiento") != null) { out.print( request.getParameter("requerimiento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getRequerimiento() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.numchfis.value = '<% if(request.getParameter("numchfis") != null) { out.print( request.getParameter("numchfis") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNumChFis() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.fija.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") ) { out.print( (set.getAbsRow(0).getFija() ? "true" : "false" ) ); } else if(request.getParameter("fija") != null ) { out.print("true"); } else { out.print("false"); } %>  
</script>
</body>
</html>
