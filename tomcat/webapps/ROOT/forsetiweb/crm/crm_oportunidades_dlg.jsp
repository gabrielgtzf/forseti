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
	String crm_oportunidades_dlg = (String)request.getAttribute("crm_oportunidades_dlg");
	if(crm_oportunidades_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("CRM_OPORTUNIDADES").generarTitulo(JUtil.Msj("CEF","CRM_OPORTUNIDADES","VISTA",request.getParameter("proceso"),3));
	
	JCRMOportunitiesViewSet set = new JCRMOportunitiesViewSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_OPORTUNIDAD") || request.getParameter("proceso").equals("CONSULTAR_OPORTUNIDAD") )
	{
		set.m_Where = "gu_oportunity = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}

	JCRMOportunitiesLookUpSet objset = new JCRMOportunitiesLookUpSet(request);
    objset.m_Where = "ID_Section = 'id_objetive' ";
	objset.m_OrderBy = "PG_Lookup ASC";
    objset.Open();
	
	JCRMCampaignsSet campset = new JCRMCampaignsSet(request);
	campset.m_Where = "bo_active = 1 ";
	campset.m_OrderBy = "nm_campaign ASC";
    campset.Open();
	
	JCRMOportunitiesLookUpSet stset = new JCRMOportunitiesLookUpSet(request);
    stset.m_Where = "ID_Section = 'id_status' ";
	stset.m_OrderBy = "PG_Lookup ASC";
    stset.Open();

	JCRMOportunitiesLookUpSet cieset = new JCRMOportunitiesLookUpSet(request);
    cieset.m_Where = "ID_Section = 'tx_cause' ";
	cieset.m_OrderBy = "PG_Lookup ASC";
    cieset.Open();
	
	JCRMOportunitiesLookUpSet medset = new JCRMOportunitiesLookUpSet(request);
    medset.m_Where = "ID_Section = 'tp_origin' ";
	medset.m_OrderBy = "PG_Lookup ASC";
    medset.Open();

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
	if(formAct.proceso.value == "AGREGAR_OPORTUNIDAD" || formAct.proceso.value == "CAMBIAR_OPORTUNIDAD")
	{
		if(	!esCadena('Titulo', formAct.tl_oportunity.value, 1, 128) ||
			!esNumeroDecimal('Importe', formAct.im_revenue.value, 0, 99999999999.99, 2) ||
			!esNumeroDecimal('Costo', formAct.im_cost.value, 0, 99999999999.99, 2) )
			return false;
		else
		{	
			if(formAct.proceso.value == "AGREGAR_OPORTUNIDAD")
			{
				if(formAct.gu_contact.value == "" && (formAct.tx_name.value == "" || formAct.tx_surname.value == "" || formAct.tx_email.value == "" ))
				{
					alert("Se debe establecer una persona registrada interesada en esta oportunidad, o en su defecto como mínimo, el nombre, apellidos y correo electrónico de un nuevo contacto");
					return false;
				}
				
				if(formAct.gu_company.value == "" && formAct.nm_legal.value != "" && formAct.tx_email.value == "")
				{
					alert("Se debe establecer como mínimo el correo electrónico de una nueva compañía");
					return false;
				}
			}
			
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCRMOportunidadesDlg" method="post" enctype="application/x-www-form-urlencoded" name="crm_oportunidades_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_OPORTUNIDAD")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCRMOportunidadesCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
          <td width="30%">Objetivo</td>
          <td><select name="id_objetive">
              <option value="FSI_OBJETIVE">--- SELECCIONAR ---</option>
<%
		for(int i = 0; i < objset.getNumRows(); i++)
		{	
%>
              <option value="<%=objset.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("id_objetive") != null) {
										if(request.getParameter("id_objetive").equals(objset.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { 
											if(set.getAbsRow(0).getID_objetive().equals(objset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= objset.getAbsRow(i).getTR_es() %></option>
              <%	
		}
%>
            </select></td>
          </tr>
          <td width="30%">Campaña</td>
          <td><select name="gu_campaign">
              <option value="FSI_CAMPAIGN">--- SELECCIONAR ---</option>
              <%
		for(int i = 0; i < campset.getNumRows(); i++)
		{	
%>
              <option value="<%=campset.getAbsRow(i).getGU_campaign()%>"<% 
									if(request.getParameter("gu_campaign") != null) {
										if(request.getParameter("gu_campaign").equals(campset.getAbsRow(i).getGU_campaign())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { 
											if(set.getAbsRow(0).getGU_campaign().equals(campset.getAbsRow(i).getGU_campaign())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= campset.getAbsRow(i).getNM_campaign() %></option>
              <%	
		}
%>
            </select></td>
          </tr>
          <tr> 
            <td width="30%">Titulo</td>
            <td><input name="tl_oportunity" type="text" id="tl_oportunity" size="80" maxlength="128"></td>
          </tr>
          <tr> 
            <td width="30%">Estatus</td>
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
										if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { 
											if(set.getAbsRow(0).getID_status().equals(stset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= stset.getAbsRow(i).getTR_es() %></option>
                <%	
		}
%>
              </select></td>
          </tr>
          <tr> 
            <td width="30%">Causa de cierre</td>
            <td><select name="tx_cause">
                <option value="FSI_CAUSE">--- No cerrada aun ---</option>
                <%
		for(int i = 0; i < cieset.getNumRows(); i++)
		{	
%>
                <option value="<%=cieset.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("tx_cause") != null) {
										if(request.getParameter("tx_cause").equals(cieset.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { 
											if(set.getAbsRow(0).getTX_cause().equals(cieset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= cieset.getAbsRow(i).getTR_es() %></option>
                <%	
		}
%>
              </select></td>
          </tr>
          <tr> 
            <td width="30%">Grado de Interes</td>
            <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="titChicoNeg"><input type="radio" name="lv_interest" value="0"<% if(request.getParameter("lv_interest") != null && request.getParameter("lv_interest").equals("0")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD") && set.getAbsRow(0).getLV_interest() == 0) { out.print(" checked"); } else { out.print(""); } %>> 
                    &nbsp;NINGUNO</td>
                  <td class="titChicoNeg"><input type="radio" name="lv_interest" value="1"<% if(request.getParameter("lv_interest") != null && request.getParameter("lv_interest").equals("1")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD") && set.getAbsRow(0).getLV_interest() == 1) { out.print(" checked"); } else { out.print(""); } %>>
                    &nbsp;POCO</td>
                  <td class="titChicoNeg"><input type="radio" name="lv_interest" value="2"<% if(request.getParameter("lv_interest") != null && request.getParameter("lv_interest").equals("2")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD") && set.getAbsRow(0).getLV_interest() == 2) { out.print(" checked"); } else { out.print(""); } %>> 
                    &nbsp;REGULAR</td>
                  <td class="titChicoNeg"><input type="radio" name="lv_interest" value="3"<% if(request.getParameter("lv_interest") != null && request.getParameter("lv_interest").equals("3")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD") && set.getAbsRow(0).getLV_interest() == 3) { out.print(" checked"); } else { out.print(""); } %>> 
                    &nbsp;MUCHO</td>
                </tr>
              </table></td>
          </tr>
          <tr> 
            <td width="30%">Medio de Informaci&oacute;n</td>
            <td><select name="tp_origin">
                <option value="FSI_ORIGIN">--- SELECCIONAR ---</option>
<%
		for(int i = 0; i < medset.getNumRows(); i++)
		{	
%>
                <option value="<%=medset.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("tp_origin") != null) {
										if(request.getParameter("tp_origin").equals(medset.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { 
											if(set.getAbsRow(0).getTP_origin().equals(medset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= medset.getAbsRow(i).getTR_es() %></option>
                <%	
		}
%>
              </select></td>
          </tr>
          <tr> 
            <td width="30%">Importe</td>
            <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td width="40%"><input name="im_revenue" type="text" id="im_revenue" size="15" maxlength="15"></td>
                  <td width="20%">Costo</td>
                  <td><input name="im_cost" type="text" id="im_cost" size="15" maxlength="15"></td>
                </tr>
              </table></td>
          </tr>
          <tr> 
            <td width="30%">Fecha Sig. Acci&oacute;n</td>
            <td><input name="dt_next_action" type="text" id="dt_next_action" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('dt_next_action','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
          <tr> 
            <td width="30%">Comentarios</td>
            <td><textarea name="tx_note" cols="60" rows="3" id="tx_note"></textarea></td>
          </tr>
          <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
          <tr> 
            <td colspan="2" bgcolor="#0099FF" class="titChico" align="center">Datos del interesado</td>
          </tr>
          <tr> 
            <td width="30%">Compañia</td>
            <td><input name="gu_company" type="hidden" id="gu_company"> <input name="tx_company" type="text" id="tx_company" size="80" maxlength="70" readonly="true"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=crm_oportunidades_dlg&lista=gu_company&idcatalogo=34&nombre=COMPANIAS&destino=tx_company&esp1=CRM_OPORTUNIDADES&nocve=1',250,450)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
          </tr>
          <tr> 
            <td width="30%">Persona</td>
            <td><input name="gu_contact" type="hidden" id="gu_contact"> <input name="tx_contact" type="text" id="tx_contact" size="80" maxlength="70" readonly="true"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=crm_oportunidades_dlg&lista=gu_contact&idcatalogo=35&nombre=PERSONAS&destino=tx_contact&esp1=CRM_OPORTUNIDADES&nocve=1',250,450)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
          </tr>
<%
	if(request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD"))
	{
%>
          <tr> 
            <td colspan="2" class="titChicoNeg" align="center">...o ingresa datos 
              de un nuevo contacto</td>
          </tr>
          <tr> 
            <td width="30%">Compañía</td>
            <td><input name="nm_legal" type="text" id="nm_legal" size="80" maxlength="70"></td>
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
            <td width="30%">Genero</td>
            <td> <select name="id_gender" class="cpoColAzc">
                <option value="M"<% 
					   				 if(request.getParameter("id_gender") != null) {
										if(request.getParameter("id_gender").equals("M")) {
											out.print(" selected");
										}
									 } %>> Hombre </option>
                <option value="F"<% 
					   				 if(request.getParameter("id_gender") != null) {
										if(request.getParameter("id_gender").equals("F")) {
											out.print(" selected");
										}
									 } %>> Mujer </option>
              </select></td>
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
									 } %>><%= natset.getAbsRow(i).getTR_country_es() %></option>
<%	
		}
%>
              </select></td>
          </tr>
          <tr> 
            <td colspan="2"> 
			  <table width="100%" border="0" cellspacing="2" cellpadding="0">
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
                  <td colspan="3"> <select name="id_country">
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
									 } %>> <%= ctrset.getAbsRow(j).getTR_country_es() %> </option>
                      <%	
		}
%>
                    </select> </td>
                </tr>
                <tr> 
                  <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","TEL") %></td>
                  <td><input name="home_phone" type="text" id="home_phone" size="30" maxlength="25"></td>
                  <td>Celular</td>
                  <td colspan="3"><input name="mov_phone" type="text" id="mov_phone2" size="30" maxlength="25"></td>
                </tr>
                <tr> 
                  <td width="10%">Directo</td>
                  <td><input name="direct_phone" type="text" id="direct_phone" size="30" maxlength="25"></td>
                  <td>Otro</td>
                  <td colspan="3"><input name="other_phone" type="text" id="other_phone2" size="30" maxlength="25"></td>
                </tr>
              </table>
			 </td>
          </tr>
<%
	}
%>
        </table>
	 </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.crm_oportunidades_dlg.bo_private.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_OPORTUNIDAD") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_OPORTUNIDAD") ) { out.print( (set.getAbsRow(0).getBO_private() == 1 ? "true" : "false" ) ); } else if(request.getParameter("bo_private") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.crm_oportunidades_dlg.tl_oportunity.value = '<% if(request.getParameter("tl_oportunity") != null) { out.print( request.getParameter("tl_oportunity") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getTL_oportunity() ); } else { out.print(""); } %>' 
document.crm_oportunidades_dlg.im_revenue.value = '<% if(request.getParameter("im_revenue") != null) { out.print( request.getParameter("im_revenue") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getIM_revenue() ); } else { out.print("0.00"); } %>'  
document.crm_oportunidades_dlg.im_cost.value = '<% if(request.getParameter("im_cost") != null) { out.print( request.getParameter("im_cost") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getIM_cost() ); } else { out.print("0.00"); } %>'  
document.crm_oportunidades_dlg.dt_next_action.value = '<% if(request.getParameter("dt_next_action") != null) { out.print( request.getParameter("dt_next_action") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getDT_next_action(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
document.crm_oportunidades_dlg.tx_note.value = '<% if(request.getParameter("tx_note") != null) { out.print( request.getParameter("tx_note") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getTX_note() ); } else { out.print(""); } %>' 
document.crm_oportunidades_dlg.gu_company.value = '<% if(request.getParameter("gu_company") != null) { out.print( request.getParameter("gu_company") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getGU_company() ); } else { out.print(""); } %>'  
document.crm_oportunidades_dlg.tx_company.value = '<% if(request.getParameter("tx_company") != null) { out.print( request.getParameter("tx_company") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getTX_company() ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.gu_contact.value = '<% if(request.getParameter("gu_contact") != null) { out.print( request.getParameter("gu_contact") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getGU_contact() ); } else { out.print(""); } %>'  
document.crm_oportunidades_dlg.tx_contact.value = '<% if(request.getParameter("tx_contact") != null) { out.print( request.getParameter("tx_contact") ); } else if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD")) { out.print( set.getAbsRow(0).getTX_contact() ); } else { out.print(""); } %>'
<%
	if(request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD"))
	{
%>
document.crm_oportunidades_dlg.nm_legal.value = '<% if(request.getParameter("nm_legal") != null) { out.print( request.getParameter("nm_legal") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_name.value = '<% if(request.getParameter("tx_name") != null) { out.print( request.getParameter("tx_name") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_surname.value = '<% if(request.getParameter("tx_surname") != null) { out.print( request.getParameter("tx_surname") ); } else { out.print(""); } %>' 

document.crm_oportunidades_dlg.tp_street.value = '<% if(request.getParameter("tp_street") != null) { out.print( request.getParameter("tp_street") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.nm_street.value = '<% if(request.getParameter("nm_street") != null) { out.print( request.getParameter("nm_street") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.nu_street.value = '<% if(request.getParameter("nu_street") != null) { out.print( request.getParameter("nu_street") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_addr1.value = '<% if(request.getParameter("tx_addr1") != null) { out.print( request.getParameter("tx_addr1") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_addr2.value = '<% if(request.getParameter("tx_addr2") != null) { out.print( request.getParameter("tx_addr2") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.nm_state.value = '<% if(request.getParameter("nm_state") != null) { out.print( request.getParameter("nm_state") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.mn_city.value = '<% if(request.getParameter("mn_city") != null) { out.print( request.getParameter("mn_city") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.zipcode.value = '<% if(request.getParameter("zipcode") != null) { out.print( request.getParameter("zipcode") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.direct_phone.value = '<% if(request.getParameter("direct_phone") != null) { out.print( request.getParameter("direct_phone") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.home_phone.value = '<% if(request.getParameter("home_phone") != null) { out.print( request.getParameter("home_phone") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.mov_phone.value = '<% if(request.getParameter("mov_phone") != null) { out.print( request.getParameter("mov_phone") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.other_phone.value = '<% if(request.getParameter("other_phone") != null) { out.print( request.getParameter("other_phone") ); } else { out.print(""); } %>'
document.crm_oportunidades_dlg.tx_email.value = '<% if(request.getParameter("tx_email") != null) { out.print( request.getParameter("tx_email") ); } else { out.print(""); } %>'
<%
	}
%>
</script>
</body>
</html>
