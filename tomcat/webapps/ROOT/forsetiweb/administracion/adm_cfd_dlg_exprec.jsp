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

	String tipo;
	
	if(request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") || request.getParameter("proceso").equals("CAMBIAR_EXPEDITOR"))
		tipo = "EXP";
	else
		tipo = "REC";
			
	String titulo = JUtil.getSesion(request).getSesion("ADM_CFDI").generarTitulo(JUtil.Msj("CEF","ADM_CFDI","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	
	JCFDExpRecSet set = new JCFDExpRecSet(request,tipo);
	if(request.getParameter("proceso").equals("CAMBIAR_EXPEDITOR"))
	{
		set.m_Where = "CFD_ID_Expedicion = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	else if(request.getParameter("proceso").equals("CAMBIAR_RECEPTOR"))
	{
		set.m_Where = "CFD_ID_Receptor = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}

	JSatPaisesSet paisSet = new JSatPaisesSet(request);
	paisSet.ConCat(true);
	paisSet.m_Where = "Alfa3 = 'MEX'";
	paisSet.Open();
	
	JSatEstadosSet estSet = new JSatEstadosSet(request);
	estSet.ConCat(true);
	estSet.m_Where = "CodPais3 = 'MEX'";
	estSet.m_OrderBy = "Nombre ASC";
	estSet.Open();
	
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_EMISOR")) { %>
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
            <td colspan="4">
			  <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td colspan="6"><div align="center" class="titChicoNeg"> 
                      <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                      <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                      <input name="subproceso" type="hidden" value="ENVIAR">
                      </div></td>
                </tr>
                <tr> 
                  <td><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
                  <td><input name="cfd_id_exprec" type="text" id="cfd_id_exprec" size="10" class="cpoColAzc"  maxlength="3"<% if(request.getParameter("proceso").equals("CAMBIAR_EXPEDITOR") || request.getParameter("proceso").equals("CAMBIAR_RECEPTOR")) { out.print(" readonly=\"true\""); } %>></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
                  <td colspan="3"><input name="cfd_nombre" type="text" id="cfd_nombre" size="80" maxlength="254"></td>
                </tr>
				<tr> 
                  <td><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
                  <td>
				     <select name="cfd_pais" class="cpoBco">
                		<option value="<%= paisSet.getAbsRow(0).getAlfa3() %>"><%= paisSet.getAbsRow(0).getNombre() %></option>
					</select></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
                  <td>
				    <select name="cfd_estado" class="cpoBco" onChange="javascript:establecerProcesoSVE(this.form.subproceso, 'ACTUALIZAR'); this.form.submit();">
                		<option value=""<% if(request.getParameter("cfd_estado") != null) {
										if(request.getParameter("cfd_estado").equals("")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR")) { 
											if(set.getAbsRow(0).getCFD_Estado().equals("")) {
												out.println(" selected"); 
											}
										}
									 } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %> ---</option>
                <%
								  for(int i = 0; i< estSet.getNumRows(); i++)
								  {
		%>
                <option value="<%= estSet.getAbsRow(i).getCodEstado() %>"<% 
									if(request.getParameter("cfd_estado") != null) {
										if(request.getParameter("cfd_estado").equals(estSet.getAbsRow(i).getCodEstado())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR")) { 
											if(set.getAbsRow(0).getCFD_Estado().equals(estSet.getAbsRow(i).getCodEstado())) {
												out.println(" selected"); 
											}
										}
									 }	  %>> 
                <%=  estSet.getAbsRow(i).getNombre()  %>
                </option>
                <%
								  }
				%>
              </select></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
                  <td><input name="cfd_municipio" type="text" id="municipio" size="30" maxlength="40"></td>
				</tr>
				<tr> 
                  <td><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
                  <td><input name="cfd_localidad" type="text" id="poblacion" size="50" maxlength="80"></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
                  <td><input name="cfd_cp" type="text" id="cp" size="7" maxlength="7"></td>
				  <td><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
                  <td><input name="cfd_colonia" type="text" id="colonia" size="30" maxlength="40"></td>
                </tr>				
                <tr> 
                  <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
                  <td><input name="cfd_calle" type="text" id="cfd_calle" size="50" maxlength="80"></td>
                  <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
                  <td><input name="cfd_noext" type="text" id="cfd_noext" size="8" maxlength="10"></td>
                  <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
                  <td><input name="cfd_noint" type="text" id="cfd_noint" size="8" maxlength="10"></td>
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
document.adm_cfd_dlg.cfd_id_exprec.value = '<% if(request.getParameter("cfd_id_exprec") != null) { out.print( request.getParameter("cfd_id_exprec") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_ID_ExpRec() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_nombre.value = '<% if(request.getParameter("cfd_nombre") != null) { out.print( request.getParameter("cfd_nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_Nombre() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_municipio.value = '<% if(request.getParameter("cfd_municipio") != null) { out.print( request.getParameter("cfd_municipio") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_Municipio() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_localidad.value = '<% if(request.getParameter("cfd_localidad") != null) { out.print( request.getParameter("cfd_localidad") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_Localidad() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_cp.value = '<% if(request.getParameter("cfd_cp") != null) { out.print( request.getParameter("cfd_cp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_CP() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_colonia.value = '<% if(request.getParameter("cfd_colonia") != null) { out.print( request.getParameter("cfd_colonia") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_Colonia() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_calle.value = '<% if(request.getParameter("cfd_calle") != null) { out.print( request.getParameter("cfd_calle") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_Calle() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_noext.value = '<% if(request.getParameter("cfd_noext") != null) { out.print( request.getParameter("cfd_noext") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_NoExt() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_noint.value = '<% if(request.getParameter("cfd_noint") != null) { out.print( request.getParameter("cfd_noint") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EXPEDITOR") && !request.getParameter("proceso").equals("AGREGAR_RECEPTOR")) { out.print( set.getAbsRow(0).getCFD_NoInt() ); } else { out.print(""); } %>'
</script>
</body>
</html>
