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
	String crm_companias_dlg = (String)request.getAttribute("crm_companias_dlg");
	if(crm_companias_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("CRM_COMPANIAS").generarTitulo(JUtil.Msj("CEF","CRM_COMPANIAS","VISTA",request.getParameter("proceso"),3));
	
	JCRMCompaniesViewSet set = new JCRMCompaniesViewSet(request);
	JCRMAddressesViewSet addrset = new JCRMAddressesViewSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_COMPANIA") || request.getParameter("proceso").equals("CONSULTAR_COMPANIA") )
	{
		set.m_Where = "gu_company = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
		addrset.m_Where = "ix_address = 1 and gu_address in (select gu_address from tbl_crmk_x_company_addr where gu_company = '" + JUtil.p(request.getParameter("id")) + "')";
		//System.out.println(addrset.m_Where);
		addrset.Open();
	}

	JCRMLookUpSet secset = new JCRMLookUpSet(request, "companies");
    secset.m_Where = "ID_Section = 'id_sector' ";
	secset.m_OrderBy = "PG_Lookup ASC";
    secset.Open();
	//for(int i = 0; i < secset.getNumRows(); i++)
		//System.out.println(secset.getAbsRow(i).getVL_lookup());
	JCRMLookUpSet tpset = new JCRMLookUpSet(request, "companies");
    tpset.m_Where = "ID_Section = 'tp_company' ";
	tpset.m_OrderBy = "PG_Lookup ASC";
    tpset.Open();
	
	JCRMLookUpSet stset = new JCRMLookUpSet(request, "companies");
    stset.m_Where = "ID_Section = 'id_status' ";
	stset.m_OrderBy = "PG_Lookup ASC";
    stset.Open();
	
	JCRMLUCountriesSet ctrset = new JCRMLUCountriesSet(request);
	ctrset.m_OrderBy = "TR_country_es ASC";
	ctrset.Open();
	
	session = request.getSession(true);
   	JCRMCompaniasSes ses = (JCRMCompaniasSes)session.getAttribute("crm_companias_dlg");
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
	if(formAct.subproceso.value == "AGR_PART")
	{
		if( !esCadena("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>:", formAct.clave.value, 1, 10) )
			return false;
		else
			return true;
	}
	else if(formAct.subproceso.value == "ENVIAR")
	{
		if(	!esCadena('Razon Social', formAct.nm_legal.value, 2, 70) ||
			!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","VENDEDOR") %>', formAct.id_vendedor, 0, 30000) ||
			!esNumeroDecimal('Facturacion', formAct.im_revenue.value, 0, 99999999999.99, 2) ||
			!esNumeroEntero('Num. Empleados', formAct.nu_employees.value, 1, 99999) ||
			!esCadena('Correo electronico', formAct.tx_email.value, 7, 40) 		)
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
		return true;
	
}

function limpiarFormulario()
{
	document.crm_companias_dlg.clave.value = "";
	document.crm_companias_dlg.descripcion.value = "";
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCRMCompaniasDlg" method="post" enctype="application/x-www-form-urlencoded" name="crm_companias_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_COMPANIA")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCRMCompaniasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
           <tr> 
            <td width="30%">Razon Social</td>
            <td><input name="nm_legal" type="text" id="nm_legal" size="80" maxlength="70"></td>
          </tr>
          <tr> 
            <td width="30%">Nombre comercial</td>
            <td><input name="nm_commercial" type="text" id="nm_commercial" size="80" maxlength="70"></td>
          </tr>
          <tr> 
            <td width="10%">Sector</td>
            <td><select name="id_sector">
			       <option value="FSI_SECTOR">--- SELECCIONAR ---</option>
<%
		for(int i = 0; i < secset.getNumRows(); i++)
		{	
%>
							<option value="<%=secset.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("id_sector") != null) {
										if(request.getParameter("id_sector").equals(secset.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { 
											if(set.getAbsRow(0).getID_sector().equals(secset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= secset.getAbsRow(i).getTR_es() %></option>
<%	
		}
%>				
  				</select></td>
          </tr>
          <tr> 
            <td width="30%">RFC</td>
            <td><input name="id_legal" type="text" id="id_legal" size="15" maxlength="15"></td>
          </tr>
		  <tr> 
            <td width="30%">Referencia</td>
            <td><input name="id_ref" type="text" id="id_ref" size="30" maxlength="50"></td>
          </tr>
		  <tr> 
            <td width="30%">Tipo de compañia</td>
            <td><select name="tp_company" class="cpoColAzc">
<%
		for(int i = 0; i < tpset.getNumRows(); i++)
		{	
%>
							<option value="<%=tpset.getAbsRow(i).getVL_lookup()%>"<% 
									if(request.getParameter("tp_company") != null) {
										if(request.getParameter("tp_company").equals(tpset.getAbsRow(i).getVL_lookup())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { 
											if(set.getAbsRow(0).getTP_company().equals(tpset.getAbsRow(i).getVL_lookup())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= tpset.getAbsRow(i).getTR_es() %></option>
<%	
		}
%>				
  				</select></td>
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
										if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { 
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
            <td width="30%">Facturacion</td>
            <td><input name="im_revenue" type="text" id="im_revenue" size="15" maxlength="20"></td>
          </tr>
		  <tr> 
            <td width="30%">Num. Empleados</td>
            <td><input name="nu_employees" type="text" id="nu_employees" size="10" maxlength="5"></td>
          </tr>
		  <tr> 
            <td width="30%">Fecha constitucion</td>
            <td><input name="dt_founded" type="text" id="dt_founded" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('dt_founded','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
		  <tr> 
            <td width="30%">Vendedor</td>
            <td><input name="id_vendedor" type="text" id="id_vendedor" size="7" maxlength="3"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=crm_companias_dlg&lista=id_vendedor&idcatalogo=23&nombre=VENDEDORES&destino=nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a> 
              <input name="nombre" type="text" id="nombre" size="50" maxlength="250" readonly="true"></td>
          </tr>
		  <tr> 
            <td width="30%">Observaciones</td>
            <td><textarea name="de_company" cols="60" rows="3" id="de_company"></textarea></td>
          </tr>
		  <tr> 
            <td colspan="2">&nbsp;</td>
          </tr>
		  <tr> 
            <td colspan="2">
			
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
		  <tr> 
                  <td colspan="6" class="titChicoAzc" align="center">Direccion 
                    Principal </td>
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
            <td width="15%"> 
              <input name="nu_street" type="text" id="nu_street" size="10" maxlength="10">
            </td>
            <td width="10%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
            <td width="15%"> 
              <input name="tp_street" type="text" id="tp_street" size="10" maxlength="10">
            </td>
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
            <td><input name="nm_state" type="text" id="nm_state" size="30" maxlength="40"> 
            </td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
            <td colspan="3"><select name="id_country">
			   <option value="FSI_COUNTRY">--- SELECCIONAR ---</option>
<%
		for(int i = 0; i < ctrset.getNumRows(); i++)
		{	
%>
                <option value="<%=ctrset.getAbsRow(i).getID_country()%>"<% 
									if(request.getParameter("id_country") != null) {
										if(request.getParameter("id_country").equals(ctrset.getAbsRow(i).getID_country())) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { 
											if(addrset.getAbsRow(0).getID_country().equals(ctrset.getAbsRow(i).getID_country())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= ctrset.getAbsRow(i).getTR_country_es() %></option>
<%	
		}
%>
              </select></td>
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
		  <tr> 
            <td colspan="2">&nbsp;</td>
		  </tr>	
		  <tr> 
            <td colspan="2" align="center">
			  
			  <table width="100%" border="0" cellspacing="0" cellpadding="1">
				 <tr>
				    <td colspan="3" align="center" class="titChicoNeg">Lineas de productos, servicios o gastos para esta compañia</td>
				 </tr>
				 <tr bgcolor="#0099FF">
					<td width="20%" class="titChico">Clave</td>
					<td class="titChico">Descripcion</td>
					<td width="15%">&nbsp;</td>
				  </tr>
<%
		if( !request.getParameter("proceso").equals("CONSULTAR_COMPANIA") )
		{
%>
				  <tr valign="top"> 
					<td><input name="clave" type="text" id="clave" class="cpoBco" size="11" maxlength="25"<% if(request.getParameter("clave") != null) { out.println(" value=\"" + request.getParameter("clave") + "\""); } %>><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=crm_companias_dlg&lista=clave&idcatalogo=33&nombre=LINEAS&destino=descripcion',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
					<td><input name="descripcion" type="text" id="descripcion" class="cpoBco" size="40" maxlength="250" readonly="true"<% if(request.getParameter("descripcion") != null) { out.println(" value=\"" + request.getParameter("descripcion") + "\""); } %>></td>
					<td align="right"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" alt="" border="0">
					  <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
				  </tr>
<%
		}
		
		if(ses.numPartidas() == 0)
		{
			out.println("<tr><td align=\"center\" class=\"titChicoAzc\" colspan=\"3\">" + JUtil.Msj("GLB","GLB","DLG","CERO-PART") + "</td></tr>");
		}
		else
		{						
			for(int i = 0; i < ses.numPartidas(); i++)
			{
%>
				  <tr valign="top"> 
					<td><%= ses.getPartida(i).getClave() %></td>
					<td><%= ses.getPartida(i).getDescripcion() %></td>
					<td align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_COMPANIA")) { %>
                    <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0">
					<% } else { out.print("&nbsp;"); } %></td>
				  </tr>
<%
			}	
		}	
%>
				</table>
				
				
			</td>
          </tr>
        </table>
	 </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.crm_companias_dlg.nm_legal.value = '<% if(request.getParameter("nm_legal") != null) { out.print( request.getParameter("nm_legal") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getNM_legal() ); } else { out.print(""); } %>'
document.crm_companias_dlg.nm_commercial.value = '<% if(request.getParameter("nm_commercial") != null) { out.print( request.getParameter("nm_commercial") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getNM_commercial() ); } else { out.print(""); } %>' 
document.crm_companias_dlg.id_legal.value = '<% if(request.getParameter("id_legal") != null) { out.print( request.getParameter("id_legal") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getID_legal() ); } else { out.print(""); } %>' 
document.crm_companias_dlg.id_ref.value = '<% if(request.getParameter("id_ref") != null) { out.print( request.getParameter("id_ref") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getID_ref() ); } else { out.print(""); } %>' 
document.crm_companias_dlg.im_revenue.value = '<% if(request.getParameter("im_revenue") != null) { out.print( request.getParameter("im_revenue") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getIM_revenue() ); } else { out.print("0.00"); } %>'  
document.crm_companias_dlg.nu_employees.value = '<% if(request.getParameter("nu_employees") != null) { out.print( request.getParameter("nu_employees") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getNU_employees() ); } else { out.print("1"); } %>' 
document.crm_companias_dlg.dt_founded.value = '<% if(request.getParameter("dt_founded") != null) { out.print( request.getParameter("dt_founded") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getDT_founded(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
document.crm_companias_dlg.id_vendedor.value = '<% if(request.getParameter("id_vendedor") != null) { out.print( request.getParameter("id_vendedor") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getID_vendedor() ); } else { out.print("0"); } %>'  
document.crm_companias_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print("----------"); } %>'
document.crm_companias_dlg.de_company.value = '<% if(request.getParameter("de_company") != null) { out.print( request.getParameter("de_company") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( set.getAbsRow(0).getDE_company() ); } else { out.print(""); } %>'

document.crm_companias_dlg.tp_street.value = '<% if(request.getParameter("tp_street") != null) { out.print( request.getParameter("tp_street") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getTP_street() ); } else { out.print(""); } %>'
document.crm_companias_dlg.nm_street.value = '<% if(request.getParameter("nm_street") != null) { out.print( request.getParameter("nm_street") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getNM_street() ); } else { out.print(""); } %>'
document.crm_companias_dlg.nu_street.value = '<% if(request.getParameter("nu_street") != null) { out.print( request.getParameter("nu_street") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getNU_street() ); } else { out.print(""); } %>'
document.crm_companias_dlg.tx_addr1.value = '<% if(request.getParameter("tx_addr1") != null) { out.print( request.getParameter("tx_addr1") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getTX_addr1() ); } else { out.print(""); } %>'
document.crm_companias_dlg.tx_addr2.value = '<% if(request.getParameter("tx_addr2") != null) { out.print( request.getParameter("tx_addr2") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getTX_addr2() ); } else { out.print(""); } %>'
document.crm_companias_dlg.nm_state.value = '<% if(request.getParameter("nm_state") != null) { out.print( request.getParameter("nm_state") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getNM_state() ); } else { out.print(""); } %>'
document.crm_companias_dlg.mn_city.value = '<% if(request.getParameter("mn_city") != null) { out.print( request.getParameter("mn_city") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getMN_city() ); } else { out.print(""); } %>'
document.crm_companias_dlg.zipcode.value = '<% if(request.getParameter("zipcode") != null) { out.print( request.getParameter("zipcode") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getZipcode() ); } else { out.print(""); } %>'
document.crm_companias_dlg.work_phone.value = '<% if(request.getParameter("work_phone") != null) { out.print( request.getParameter("work_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getWork_phone() ); } else { out.print(""); } %>'
document.crm_companias_dlg.direct_phone.value = '<% if(request.getParameter("direct_phone") != null) { out.print( request.getParameter("direct_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getDirect_phone() ); } else { out.print(""); } %>'
document.crm_companias_dlg.home_phone.value = '<% if(request.getParameter("home_phone") != null) { out.print( request.getParameter("home_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getHome_phone() ); } else { out.print(""); } %>'
document.crm_companias_dlg.mov_phone.value = '<% if(request.getParameter("mov_phone") != null) { out.print( request.getParameter("mov_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getMov_phone() ); } else { out.print(""); } %>'
document.crm_companias_dlg.fax_phone.value = '<% if(request.getParameter("fax_phone") != null) { out.print( request.getParameter("fax_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getFax_phone() ); } else { out.print(""); } %>'
document.crm_companias_dlg.other_phone.value = '<% if(request.getParameter("other_phone") != null) { out.print( request.getParameter("other_phone") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getOther_phone() ); } else { out.print(""); } %>'
document.crm_companias_dlg.tx_email.value = '<% if(request.getParameter("tx_email") != null) { out.print( request.getParameter("tx_email") ); } else if(!request.getParameter("proceso").equals("AGREGAR_COMPANIA")) { out.print( addrset.getAbsRow(0).getTX_email() ); } else { out.print(""); } %>'

/*													_mn_city,_zipcode,_work_phone,_direct_phone,_home_phone,_mov_phone,_fax_phone,_other_phone,_tx_email*/
</script>
</body>
</html>
