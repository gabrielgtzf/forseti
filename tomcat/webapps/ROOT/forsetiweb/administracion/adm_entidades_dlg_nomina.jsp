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
	String etq = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","ETQ2",1);
	String sts = JUtil.Msj("CEF","ADM_ENTIDADES","VISTA","STATUS");

	JAdmCompaniasSet set = new JAdmCompaniasSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ENTIDAD") || request.getParameter("proceso").equals("CONSULTAR_ENTIDAD") )
	{
		set.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}

	JAdmBancosCuentasSet contban = new JAdmBancosCuentasSet(request);
	contban.m_OrderBy = "Clave ASC";
	contban.m_Where = "Tipo = '0' and Fijo = '0'";
	contban.Open();
	JAdmBancosCuentasSet contcaj = new JAdmBancosCuentasSet(request);
	contcaj.m_OrderBy = "Clave ASC";
	contcaj.m_Where = "Tipo = '1' and Fijo = '0'";
	contcaj.Open();
	
	JAdmBancosCuentasSet fijaban = new JAdmBancosCuentasSet(request);
	fijaban.m_OrderBy = "Clave ASC";
	fijaban.m_Where = "Tipo = '0' and Fijo = '1'";
	fijaban.Open();
	JAdmBancosCuentasSet fijacaj = new JAdmBancosCuentasSet(request);
	fijacaj.m_OrderBy = "Clave ASC";
	fijacaj.m_Where = "Tipo = '1' and Fijo = '1'";
	fijacaj.Open();
	
	JAdmFormatosSet setFmt = new JAdmFormatosSet(request);
	setFmt.m_OrderBy = "ID_Formato ASC";
	setFmt.m_Where = "Tipo = 'NOM_NOMINA'";
	setFmt.Open();
	JContaPolizasClasificacionesSet setCls = new JContaPolizasClasificacionesSet(request);
	setCls.m_OrderBy = "ID_Clasificacion ASC";
	setCls.Open();
	
	JCFDExpRecSet exp = new JCFDExpRecSet(request,"EXP");
	exp.Open();
	JCFDCertificadosSet cer = new JCFDCertificadosSet(request);
	cer.Open();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>", formAct.identidad.value, 1, 254) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","FICHA") %>", formAct.ficha.value, 1, 10) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %>", formAct.descripcion.value, 1, 254)  ||
		!esNumeroEntero("<%= JUtil.Msj("GLB","GLB","GLB","NUMERO") %>", formAct.numero.value, 1, 54)  )
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
            <td width="30%"> <input class="cpoColAzc" name="identidad" type="text" size="7" maxlength="3"<% if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD")) { out.print(" readonly=\"true\""); } %>> 
            </td>
            <td width="20%" align="right"><%= JUtil.Msj("GLB","GLB","GLB","FICHA") %></td>
            <td width="30%"><input name="ficha" type="text" id="ficha" size="15" maxlength="10"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></div></td>
            <td colspan="3"><input name="descripcion" type="text" id="descripcion " size="80" maxlength="254"></td>
          </tr>
          <tr> 
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></div></td>
            <td><select name="tipo" class="cpoBco">
                <option value="1"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("1")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getTipo() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,1) %></option>
                <option value="2"<% if(request.getParameter("tipo") != null) {
										if(request.getParameter("tipo").equals("2")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getTipo() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,2) %></option>
              </select></td>
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PERIODO") %></td>
            <td><select name="periodo" class="cpoBco">
                <option value="sem"<% if(request.getParameter("periodo") != null) {
										if(request.getParameter("periodo").equals("sem")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getPeriodo().equals("sem")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,3) %></option>
                <option value="qui"<% if(request.getParameter("periodo") != null) {
										if(request.getParameter("periodo").equals("qui")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getPeriodo().equals("qui")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,4) %></option>
                <option value="men"<% if(request.getParameter("periodo") != null) {
										if(request.getParameter("periodo").equals("men")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getPeriodo().equals("men")) {
												out.println(" selected"); 
											}
										}
									 } %>><%= JUtil.Elm(etq,5) %></option>
              </select></td>
          </tr>
          <tr> 
            <td height="15"> <div align="right"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO") %></div></td>
            <td><input name="numero" type="text" id="numero" size="8" maxlength="3"></td>
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
            <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
		  <tr>
		  <td align="right"><%= JUtil.Elm(etq,7) %></td>
            <td>
              <select style="width: 90%;" name="fmt_recibos" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_recibos") != null) {
										if(request.getParameter("fmt_recibos").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Recibo().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_recibos") != null) {
										if(request.getParameter("fmt_recibos").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Recibo().equals(setFmt.getAbsRow(i).getID_Formato())) {
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
            <td height="15"> <!--div align="right"><%= JUtil.Elm(etq,6) %></div--></td>
            <td>
			  <!--select style="width: 90%;" name="fmt_nomina" class="cpoBco">
                <option value="NINGUNO"<% if(request.getParameter("fmt_nomina") != null) {
										if(request.getParameter("fmt_nomina").equals("NINGUNO")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Nomina().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","NINGUN") %> ---</option>
                <%
								  for(int i = 0; i< setFmt.getNumRows(); i++)
								  {
		%>
                <option value="<%= setFmt.getAbsRow(i).getID_Formato() %>"<% 
									if(request.getParameter("fmt_nomina") != null) {
										if(request.getParameter("fmt_nomina").equals(setFmt.getAbsRow(i).getID_Formato())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { 
											if(set.getAbsRow(0).getFmt_Nomina().equals(setFmt.getAbsRow(i).getID_Formato())) {
												out.println(" selected"); 
											}
										}
									 }	  %>> 
                <%=  setFmt.getAbsRow(i).getDescripcion()  %>
                </option>
                <%
								  }
				%>
              </select--></td>
            
          </tr>
          <tr>
		  	<td height="15"> <div align="right"><%= JUtil.Elm(etq,8) %></div></td>
            <td><select class="cpoBco" style="width: 90%;" name="contbancaj">
                <option value="-1"<% 
					  				if(request.getParameter("contbancaj") != null) 
									{
										if(request.getParameter("contbancaj").equals("-1")) 
										{
											out.print(" selected");
										}
									} 
									else 
									{
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
										{ 
											if(set.getAbsRow(0).getContCuenClave() == -1) 
											{
												out.println(" selected"); 
											}
										}
									} %>>--- <%= JUtil.Msj("GLB","GLB","GLB","BANCOS") %> ---</option>
                <%
								  	for(int i = 0; i< contban.getNumRows(); i++)
								  	{
		%>
                <option value="FSI_CONTBAN_<%= contban.getAbsRow(i).getClave() %>"<% 
										if(request.getParameter("contbancaj") != null) 
										{
											if(request.getParameter("contbancaj").equals("FSI_CONTBAN_" + contban.getAbsRow(i).getClave())) 
											{
												out.print(" selected");
											}
									 	} 
										else 
										{
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
											{ 
												if(set.getAbsRow(0).getContCuenTipo() == 0 && set.getAbsRow(0).getContCuenClave() == contban.getAbsRow(i).getClave() ) 
												{
													out.print(" selected"); 
												}
											}
									 	}	  %>><%=  contban.getAbsRow(i).getCuenta() %> 
                </option>
			<%
								 	}
		%>
				<option value="-2"<% 
					  				if(request.getParameter("contbancaj") != null) 
									{
										if(request.getParameter("contbancaj").equals("-2")) 
										{
											out.print(" selected");
										}
									}  %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %> ---</option>
                <%
								  	for(int i = 0; i< contcaj.getNumRows(); i++)
								  	{
		%>
                <option value="FSI_CONTCAJ_<%= contcaj.getAbsRow(i).getClave() %>"<% 
										if(request.getParameter("contbancaj") != null) 
										{
											if(request.getParameter("contbancaj").equals("FSI_CONTCAJ_" + contcaj.getAbsRow(i).getClave())) 
											{
												out.print(" selected");
											}
									 	} 
										else 
										{
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
											{ 
												if(set.getAbsRow(0).getContCuenTipo() == 1 && set.getAbsRow(0).getContCuenClave() == contcaj.getAbsRow(i).getClave() ) 
												{
													out.print(" selected"); 
												}
											}
									 	}	  %>><%=  contcaj.getAbsRow(i).getCuenta() %> 
                </option>
                <%
								 	}
		%>
              </select></td>
			<td align="right"><%= JUtil.Elm(etq,9) %></td>
            <td><select class="cpoBco" style="width: 90%;" name="fijabancaj">
                <option value="-1"<% 
					  				if(request.getParameter("fijabancaj") != null) 
									{
										if(request.getParameter("fijabancaj").equals("-1")) 
										{
											out.print(" selected");
										}
									} 
									else 
									{
										if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
										{ 
											if(set.getAbsRow(0).getFijaCuenClave() == -1) 
											{
												out.println(" selected"); 
											}
										}
									} %>>--- <%= JUtil.Msj("GLB","GLB","GLB","BANCOS") %> ---</option>
                <%
								  	for(int i = 0; i< fijaban.getNumRows(); i++)
								  	{
		%>
                <option value="FSI_FIJABAN_<%= fijaban.getAbsRow(i).getClave() %>"<% 
										if(request.getParameter("fijabancaj") != null) 
										{
											if(request.getParameter("fijabancaj").equals("FSI_FIJABAN_" + fijaban.getAbsRow(i).getClave())) 
											{
												out.print(" selected");
											}
									 	} 
										else 
										{
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
											{ 
												if(set.getAbsRow(0).getFijaCuenTipo() == 0 && set.getAbsRow(0).getFijaCuenClave() == fijaban.getAbsRow(i).getClave() ) 
												{
													out.print(" selected"); 
												}
											}
									 	}	  %>><%=  fijaban.getAbsRow(i).getCuenta() %> 
                </option>
                <%
								 	}
		%>
                <option value="-2"<% 
					  				if(request.getParameter("fijabancaj") != null) 
									{
										if(request.getParameter("fijabancaj").equals("-2")) 
										{
											out.print(" selected");
										}
									}  %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %> ---</option>
                <%
								  	for(int i = 0; i< fijacaj.getNumRows(); i++)
								  	{
		%>
                <option value="FSI_FIJACAJ_<%= fijacaj.getAbsRow(i).getClave() %>"<% 
										if(request.getParameter("fijabancaj") != null) 
										{
											if(request.getParameter("fijabancaj").equals("FSI_FIJACAJ_" + fijacaj.getAbsRow(i).getClave())) 
											{
												out.print(" selected");
											}
									 	} 
										else 
										{
											if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) 
											{ 
												if(set.getAbsRow(0).getFijaCuenTipo() == 1 && set.getAbsRow(0).getFijaCuenClave() == fijacaj.getAbsRow(i).getClave() ) 
												{
													out.print(" selected"); 
												}
											}
									 	}	  %>><%=  fijacaj.getAbsRow(i).getCuenta() %> 
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
			<td><select style="width: 90%;" name="idclasificacion" class="cpoBco">
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
		  
		  
		  
		  <tr align="center"> 
            <td colspan="4" class="titChicoAzc" ><%= JUtil.Msj("GLB","GLB","GLB","CFD") %></td>
		  </tr>
		  <tr align="center"> 
            <td colspan="4"><table width="100%" border="0" cellspacing="2" cellpadding="0">
                <%
	if(!request.getParameter("proceso").equals("CONSULTAR_ENTIDAD"))
	{
%>
                <tr> 
                  <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CFD",2) %>:</td>
                  <td colspan="2"><input type="radio" name="cfd" value="00"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("00")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD") && set.getAbsRow(0).getCFD().equals("00")) { out.print(" checked"); } else { out.print(" checked"); } %>>
                    <%= JUtil.Msj("GLB","GLB","GLB","NO") %> 
                    <input type="radio" name="cfd" value="01"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("01")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD") && set.getAbsRow(0).getCFD().equals("01")) { out.print(" checked"); } else { out.print(""); } %>>
                    <%= JUtil.Msj("GLB","GLB","GLB","CFD",4) %> 
					<input type="radio" name="cfd" value="10"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("10")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD") && set.getAbsRow(0).getCFD().equals("10")) { out.print(" checked"); } else { out.print(""); } %>>
                    <%= JUtil.Msj("GLB","GLB","GLB","CFD",3) %> </td>
                </tr>
                <%
	}
	else
	{
%>
                <tr> 
                  <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CFD",2) %>:</td>
                  <td colspan="2" class="txtChicoAzc"><%
		if(set.getAbsRow(0).getCFD().equals("10"))
			out.print(JUtil.Msj("GLB","GLB","GLB","CFD",3));
		else if(set.getAbsRow(0).getCFD().equals("01"))
			out.print(JUtil.Msj("GLB","GLB","GLB","CFD",4));
		else
			out.print(JUtil.Msj("GLB","GLB","GLB","NO")); 
%>				 </td>
                </tr>
<%
	}
%>
                <tr> 
                  <td class="txtChicoBco">&nbsp;</td>
                  <td align="center" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","EXPEDICION") %></td>
                  <td align="center" class="titChicoNeg">
                    <%= JUtil.Msj("GLB","GLB","GLB","CERTIFICADO") %>
                  </td>
                </tr>
                <tr> 
                  <td class="txtChicoAzc">&nbsp;</td>
                  <td width="40%"> <select name="cfd_id_expedicion" class="cpoBco" style="width: 90%;">
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
									} %>>--- 
                      <%= JUtil.Msj("GLB","GLB","GLB","EXPEDICION") %>
                      ---</option>
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
									 	}	  %>>
                      <%=  exp.getAbsRow(i).getCFD_Nombre() %>
                      </option>
                      <%
								  }
		%>
                    </select></td>
                  <td width="40%"> <select class="cpoBco" style="width: 90%;" name="cfd_nocertificado">
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
              </table>
			</td>	  
			</tr>
		  
		  	  
       </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_entidades_dlg.identidad.value = '<% if(request.getParameter("identidad") != null) { out.print( request.getParameter("identidad") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getID_Sucursal() ); } else { out.print(""); } %>'
document.adm_entidades_dlg.ficha.value = '<% if(request.getParameter("ficha") != null) { out.print( request.getParameter("ficha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.adm_entidades_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( set.getAbsRow(0).getNumero() ); } else { out.print("1"); } %>'
document.adm_entidades_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ENTIDAD")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
</script>
</body>
</html>
