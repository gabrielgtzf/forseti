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
<%@ page import="forseti.*, forseti.crm.*, forseti.sets.*, forseti.ventas.*, java.util.*, java.io.*"%>
<%
	String crm_personas_dlg = (String)request.getAttribute("crm_personas_dlg");
	if(crm_personas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("CRM_PERSONAS").generarTitulo(JUtil.Msj("CEF","CRM_PERSONAS","VISTA",request.getParameter("proceso"),3));
	
	JCRMContactsViewSet set = new JCRMContactsViewSet(request);
	JCRMAddressesViewSet addrset = new JCRMAddressesViewSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_PERSONA") || request.getParameter("proceso").equals("CONSULTAR_PERSONA") )
	{
		set.m_Where = "gu_contact = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
		addrset.m_Where = "ix_address = 1 and gu_address in (select gu_address from tbl_crmk_x_contact_addr where gu_contact = '" + JUtil.p(request.getParameter("id")) + "')";
		//System.out.println(addrset.m_Where);
		addrset.Open();
	}

	JCRMLookUpSet titset = new JCRMLookUpSet(request, "contacts");
    titset.m_Where = "ID_Section = 'de_title' ";
	titset.m_OrderBy = "PG_Lookup ASC";
    titset.Open();
	
	JCRMLookUpSet passet = new JCRMLookUpSet(request, "contacts");
    passet.m_Where = "ID_Section = 'tp_passport' ";
	passet.m_OrderBy = "PG_Lookup ASC";
    passet.Open();
	
	JCRMLookUpSet stset = new JCRMLookUpSet(request, "contacts");
    stset.m_Where = "ID_Section = 'id_status' ";
	stset.m_OrderBy = "PG_Lookup ASC";
    stset.Open();

	JCRMLUCountriesSet natset = new JCRMLUCountriesSet(request);
	natset.m_OrderBy = "TR_country_es ASC";
	natset.Open();
	
	JCRMLUCountriesSet ctrset = new JCRMLUCountriesSet(request);
	ctrset.m_OrderBy = "TR_country_es ASC";
	ctrset.Open();
	
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
	if(formAct.proceso.value == "AGREGAR_PERSONA" || formAct.proceso.value == "CAMBIAR_PERSONA")
	{
		if(	!esCadena('Nombre', formAct.tx_name.value, 2, 100) ||
			!esCadena('Apellidos', formAct.tx_surname.value, 2, 70) ||
			!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","VENDEDOR") %>', formAct.id_vendedor, 0, 30000) ||
			!esCadena('Correo electronico', formAct.tx_email.value, 7, 40) 	)
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
	else
		return false;
	
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCRMPersonasDlg" method="post" enctype="application/x-www-form-urlencoded" name="crm_personas_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_PERSONA")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCRMPersonasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<input name="subproceso" type="hidden" value="ENVIAR">
		<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
		<input name="id" type="hidden" value="<%= request.getParameter("id")%>"> 
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td width="30%">&nbsp;</td>
            <td><input type="checkbox" name="bo_private" value="bo_private">
              Privado</td>
          </tr>
          <tr> 
            <td width="30%">Referencia</td>
            <td><input name="id_ref" type="text" id="id_ref" size="40" maxlength="50"></td>
          </tr>
          <tr> 
            <td width="30%">Nombre</td>
            <td><input name="tx_name" type="text" id="tx_name" size="50" maxlength="100"></td>
          </tr>
          <tr> 
            <td width="30%">Apellidos</td>
            <td><input name="tx_surname" type="text" id="tx_surname" size="80" maxlength="100"></td>
          </tr>
          <tr> 
            <td width="30%">Titulo</td>
            <td><select name="de_title">
                <option value="FSI_TITLE">--- SELECCIONAR ---</option>
<%
		for(int i = 0; i < titset.getNumRows(); i++)
		{	
%>
                <option value="<%=titset.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("de_title") != null) {
										if(request.getParameter("de_title").equals(titset.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { 
											if(set.getAbsRow(0).getDE_title().equals(titset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= titset.getAbsRow(i).getTR_es() %></option>
<%	
		}
%>
              </select></td>
          </tr>
          <tr> 
            <td width="30%">Compañia</td>
            <td><input name="gu_company" type="hidden" id="gu_company"><input name="nm_legal" type="text" id="nm_legal" size="80" maxlength="70" readonly="true"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=crm_personas_dlg&lista=gu_company&idcatalogo=34&nombre=COMPANIAS&destino=nm_legal&esp1=CRM_PERSONAS&nocve=1',250,450)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
          </tr>
          <tr> 
            <td width="30%">Division</td>
            <td><input name="tx_division" type="text" id="tx_division" size="50" maxlength="70"></td>
          </tr>
          <tr> 
            <td width="30%">Departamento</td>
            <td><input name="tx_dept" type="text" id="tx_dept" size="50" maxlength="70"></td>
          </tr>
          <tr> 
            <td width="30%">Sexo</td>
            <td> <select name="id_gender" class="cpoColAzc">
                <option value="M"<% 
					   				 if(request.getParameter("id_gender") != null) {
										if(request.getParameter("id_gender").equals("M")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { 
											if(set.getAbsRow(0).getID_gender().equals("M")) {
												out.println(" selected"); 
											}
										}
									 } %>> Hombre </option>
                <option value="F"<% 
					   				 if(request.getParameter("id_gender") != null) {
										if(request.getParameter("id_gender").equals("F")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { 
											if(set.getAbsRow(0).getID_gender().equals("F")) {
												out.println(" selected"); 
											}
										}
									 } %>> Mujer </option>
              </select></td>
          </tr>
          <tr> 
            <td width="30%">Fecha de nacimiento</td>
            <td><input name="dt_birth" type="text" id="dt_birth" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('dt_birth','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
          <tr> 
            <td width="30%">Identificación</td>
            <td><select name="tp_passport">
                <option value="FSI_PASSPORT">--- SELECCIONAR ---</option>
<%
		for(int i = 0; i < passet.getNumRows(); i++)
		{	
%>
                <option value="<%=passet.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("tp_passport") != null) {
										if(request.getParameter("tp_passport").equals(passet.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { 
											if(set.getAbsRow(0).getTP_passport().equals(passet.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= passet.getAbsRow(i).getTR_es() %></option>
<%	
		}
%>
              </select>
              Num: 
              <input name="sn_passport" type="text" id="sn_passport" size="20" maxlength="16"></td>
          </tr>
          <tr> 
            <td width="30%">Nacionalidad</td>
            <td><select name="id_nationality">
			   <option value="FSI_NATIONALITY">--- SELECCIONAR ---</option>
<%
		for(int i = 0; i < natset.getNumRows(); i++)
		{	
%>
                <option value="<%=natset.getAbsRow(i).getID_country()%>"<% 
									if(request.getParameter("id_nationality") != null) {
										if(request.getParameter("id_nationality").equals(natset.getAbsRow(i).getID_country())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { 
											if(set.getAbsRow(0).getID_nationality().equals(natset.getAbsRow(i).getID_country())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= natset.getAbsRow(i).getTR_country_es() %></option>
<%	
		}
%>
              </select></td>
          </tr>
          <tr> 
            <td width="30%">Estaus</td>
            <td><select name="id_status" class="cpoColAzc">
<%
		for(int i = 0; i < stset.getNumRows(); i++)
		{	
%>
                <option value="<%=stset.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("id_status") != null) {
										if(request.getParameter("id_status").equals(stset.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { 
											if(set.getAbsRow(0).getID_status().equals(stset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>> <%= stset.getAbsRow(i).getTR_es() %> </option>
<%	
		}
%>
              </select></td>
          </tr>
          <tr> 
            <td width="30%">Vendedor</td>
            <td><input name="id_vendedor" type="text" id="id_vendedor" size="7" maxlength="3"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=crm_personas_dlg&lista=id_vendedor&idcatalogo=23&nombre=VENDEDORES&destino=nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="nombre" type="text" id="nombre" size="50" maxlength="250" readonly="true"></td>
          </tr>
          <tr> 
            <td width="30%">Observaciones</td>
            <td><textarea name="tx_comments" cols="60" rows="3" id="tx_comments"></textarea></td>
          </tr>
		  <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
		  <tr> 
            <td colspan="2">
			
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
		  <tr> 
                  <td colspan="6" class="titChicoAzc" align="center">Direccion Principal </td>
          </tr>
		  <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
                  <td><input name="tx_email" type="text" id="tx_email" size="40" maxlength="40"></td>
          	<td colspan="4">&nbsp;</td>
		  </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
                  <td><input name="nm_street" type="text" id="nm_street" size="50" maxlength="80"></td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
                  <td width="15%"><input name="nu_street" type="text" id="nu_street" size="10" maxlength="10"></td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
                  <td width="15%"><input name="tp_street" type="text" id="tp_street" size="10" maxlength="10"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
                  <td><input name="tx_addr1" type="text" id="tx_addr1" size="30" maxlength="40"></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
                  <td colspan="3"><input name="tx_addr2" type="text" id="tx_addr2" size="50" maxlength="80"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
                  <td><input name="mn_city" type="text" id="mn_city" size="30" maxlength="40"></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
                  <td colspan="3"><input name="zipcode" type="text" id="zipcode" size="7" maxlength="7"></td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
            <td><input name="nm_state" type="text" id="nm_state" size="30" maxlength="40"></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
            <td colspan="3">
			    <select name="id_country">
                      <option value="FSI_COUNTRY">--- SELECCIONAR ---</option>
<%
		for(int j = 0; j < ctrset.getNumRows(); j++)
		{	
%>
                      <option value="<%= ctrset.getAbsRow(j).getID_country() %>"<% 
									if(request.getParameter("id_country") != null) {
										if(request.getParameter("id_country").equals(ctrset.getAbsRow(j).getID_country())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { 
											if(addrset.getAbsRow(0).getID_country().equals(ctrset.getAbsRow(j).getID_country())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>>
                      <%= ctrset.getAbsRow(j).getTR_country_es() %>
                      </option>
<%	
		}
%>
                    </select>
			</td>
          </tr>
          <tr> 
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","TEL") %></td>
                  <td><input name="home_phone" type="text" id="home_phone" size="30" maxlength="25"></td>
                  <td>Extension</td>
                  <td colspan="3"><input name="work_phone" type="text" id="work_phone" size="20" maxlength="10"></td>
          </tr>
		  <tr> 
            <td width="10%">Directo</td>
                  <td><input name="direct_phone" type="text" id="direct_phone" size="30" maxlength="25"></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","FAX") %></td>
                  <td colspan="3"><input name="fax_phone" type="text" id="fax_phone" size="30" maxlength="25"></td>
          </tr>
		  <tr> 
            <td width="10%">Celular</td>
                  <td><input name="mov_phone" type="text" id="mov_phone" size="30" maxlength="25"></td>
                  <td>Otro</td>
                  <td colspan="3"><input name="other_phone" type="text" id="other_phone" size="30" maxlength="25"></td>
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
document.crm_personas_dlg.bo_private.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PERSONA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PERSONA") ) { out.print( (set.getAbsRow(0).getBO_private() == 1 ? "true" : "false" ) ); } else if(request.getParameter("bo_private") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.crm_personas_dlg.id_ref.value = '<% if(request.getParameter("id_ref") != null) { out.print( request.getParameter("id_ref") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getID_ref() ); } else { out.print(""); } %>' 
document.crm_personas_dlg.tx_name.value = '<% if(request.getParameter("tx_name") != null) { out.print( request.getParameter("tx_name") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getTX_name() ); } else { out.print(""); } %>'
document.crm_personas_dlg.tx_surname.value = '<% if(request.getParameter("tx_surname") != null) { out.print( request.getParameter("tx_surname") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getTX_surname() ); } else { out.print(""); } %>' 
document.crm_personas_dlg.gu_company.value = '<% if(request.getParameter("gu_company") != null) { out.print( request.getParameter("gu_company") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getGU_company() ); } else { out.print(""); } %>'  
document.crm_personas_dlg.nm_legal.value = '<% if(request.getParameter("nm_legal") != null) { out.print( request.getParameter("nm_legal") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getNM_legal() ); } else { out.print(""); } %>'
document.crm_personas_dlg.tx_division.value = '<% if(request.getParameter("tx_division") != null) { out.print( request.getParameter("tx_division") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getTX_division() ); } else { out.print(""); } %>' 
document.crm_personas_dlg.tx_dept.value = '<% if(request.getParameter("tx_dept") != null) { out.print( request.getParameter("tx_dept") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getTX_dept() ); } else { out.print(""); } %>'  
document.crm_personas_dlg.dt_birth.value = '<% if(request.getParameter("dt_birth") != null) { out.print( request.getParameter("dt_birth") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getDT_birth(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
document.crm_personas_dlg.sn_passport.value = '<% if(request.getParameter("sn_passport") != null) { out.print( request.getParameter("sn_passport") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getSN_passport() ); } else { out.print("1"); } %>' 
document.crm_personas_dlg.id_vendedor.value = '<% if(request.getParameter("id_vendedor") != null) { out.print( request.getParameter("id_vendedor") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getID_vendedor() ); } else { out.print("0"); } %>'  
document.crm_personas_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print("----------"); } %>'
document.crm_personas_dlg.tx_comments.value = '<% if(request.getParameter("tx_comments") != null) { out.print( request.getParameter("tx_comments") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( set.getAbsRow(0).getTX_comments() ); } else { out.print(""); } %>'

document.crm_personas_dlg.tp_street.value = '<% if(request.getParameter("tp_street") != null) { out.print( request.getParameter("tp_street") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getTP_street() ); } else { out.print(""); } %>'
document.crm_personas_dlg.nm_street.value = '<% if(request.getParameter("nm_street") != null) { out.print( request.getParameter("nm_street") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getNM_street() ); } else { out.print(""); } %>'
document.crm_personas_dlg.nu_street.value = '<% if(request.getParameter("nu_street") != null) { out.print( request.getParameter("nu_street") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getNU_street() ); } else { out.print(""); } %>'
document.crm_personas_dlg.tx_addr1.value = '<% if(request.getParameter("tx_addr1") != null) { out.print( request.getParameter("tx_addr1") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getTX_addr1() ); } else { out.print(""); } %>'
document.crm_personas_dlg.tx_addr2.value = '<% if(request.getParameter("tx_addr2") != null) { out.print( request.getParameter("tx_addr2") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getTX_addr2() ); } else { out.print(""); } %>'
document.crm_personas_dlg.nm_state.value = '<% if(request.getParameter("nm_state") != null) { out.print( request.getParameter("nm_state") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getNM_state() ); } else { out.print(""); } %>'
document.crm_personas_dlg.mn_city.value = '<% if(request.getParameter("mn_city") != null) { out.print( request.getParameter("mn_city") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getMN_city() ); } else { out.print(""); } %>'
document.crm_personas_dlg.zipcode.value = '<% if(request.getParameter("zipcode") != null) { out.print( request.getParameter("zipcode") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getZipcode() ); } else { out.print(""); } %>'
document.crm_personas_dlg.work_phone.value = '<% if(request.getParameter("work_phone") != null) { out.print( request.getParameter("work_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getWork_phone() ); } else { out.print(""); } %>'
document.crm_personas_dlg.direct_phone.value = '<% if(request.getParameter("direct_phone") != null) { out.print( request.getParameter("direct_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getDirect_phone() ); } else { out.print(""); } %>'
document.crm_personas_dlg.home_phone.value = '<% if(request.getParameter("home_phone") != null) { out.print( request.getParameter("home_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getHome_phone() ); } else { out.print(""); } %>'
document.crm_personas_dlg.mov_phone.value = '<% if(request.getParameter("mov_phone") != null) { out.print( request.getParameter("mov_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getMov_phone() ); } else { out.print(""); } %>'
document.crm_personas_dlg.fax_phone.value = '<% if(request.getParameter("fax_phone") != null) { out.print( request.getParameter("fax_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getFax_phone() ); } else { out.print(""); } %>'
document.crm_personas_dlg.other_phone.value = '<% if(request.getParameter("other_phone") != null) { out.print( request.getParameter("other_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getOther_phone() ); } else { out.print(""); } %>'
document.crm_personas_dlg.tx_email.value = '<% if(request.getParameter("tx_email") != null) { out.print( request.getParameter("tx_email") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PERSONA")) { out.print( addrset.getAbsRow(0).getTX_email() ); } else { out.print(""); } %>'
</script>
</body>
</html>





























































































