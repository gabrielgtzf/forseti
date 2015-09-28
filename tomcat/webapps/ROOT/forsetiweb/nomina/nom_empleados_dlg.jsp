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
<%@ page import="forseti.*, forseti.sets.*, forseti.nomina.*, java.util.*, java.io.*"%>
<%
	String nom_empleados_dlg = (String)request.getAttribute("nom_empleados_dlg");
	if(nom_empleados_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_EMPLEADOS").generarTitulo(JUtil.Msj("CEF","NOM_EMPLEADOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JMasempSetCons set = new JMasempSetCons(request);
	JMasempSet set0 = new JMasempSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") )
	{
		set0.m_Where = "ID_Empleado = '" + JUtil.p(request.getParameter("id")) + "'";
		set0.Open();
		set.m_Where = "ID_Empleado = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	
	JAdmCompaniasSet cs = new JAdmCompaniasSet(request);
	cs.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + JUtil.getSesion(request).getSesion("NOM_EMPLEADOS").getEspecial() + "'";
	cs.Open();
	JTurnosSet tr = new JTurnosSet(request);
	tr.Open();
	JCategoriasSet ct = new JCategoriasSet(request);
	ct.Open();
 	JSatBancosSet setBan = new JSatBancosSet(request);
    setBan.m_OrderBy = "Clave ASC";
    setBan.Open();
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
	if(formAct.proceso.value == "AGREGAR_EMPLEADO" || formAct.proceso.value == "CAMBIAR_EMPLEADO")
	{
		if(	!esNumeroDecimal('Salario diario: ', formAct.salario_diario.value, 0, 99999999, 2) ||
				!esNumeroDecimal('Salario nominal: ', formAct.salario_nominal.value, 0, 99999999, 2) ||
				!esNumeroDecimal('Salario por hora: ', formAct.salario_por_hora.value, 0, 99999999, 4) ||
				!esNumeroDecimal('Salario integrado: ', formAct.salario_integrado.value, 0, 99999999, 2) ||
				!esNumeroDecimal('Salario mixto: ', formAct.salario_mixto.value, 0, 99999999, 2) ||
				!esNumeroDecimal('Importe de Vales: ', formAct.importe_vales_de_despensa.value, 0, 99999999, 2) ||
				 !esNumeroDecimal('Prestamo Infonavit: ', formAct.prestamo_infonavit.value, 0, 99999999, 2) ||
				!esNumeroDecimal('% Descuento Infonavit: ', formAct.porcentaje_descuento.value, 0, 0.9999, 4) ||
				!esNumeroDecimal('Prestamo V.S.M.: ', formAct.prestamo_vsm.value, 0, 999999, 2) ||
				!esNumeroDecimal('Descuento V.S.M: ', formAct.descuento_vsm.value, 0, 999999, 4)  	)
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomEmpleadosDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_empleados_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomEmpleadosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr> 
            <td width="10%">Clave:</td>
            <td width="15%"><input name="id_empleado" type="text" id="id_empleado" size="10" maxlength="6"<% if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO")) { out.print(" readonly=\"true\""); } %>></td>
            <td width="10%">N&oacute;mina:</td>
            <td width="15%" class="titChicoAzc"><%= cs.getAbsRow(0).getDescripcion() %></td>
            <td width="10%">Turno:</td>
            <td width="15%"><select style="width: 90%;" name="id_turno">
<%				      
		for(int i = 0; i< tr.getNumRows(); i++)
		{
%>        
					<option value="<%= tr.getAbsRow(i).getID_Turno() %>"<% 
									if(request.getParameter("id_turno") != null) 
									{
										if(request.getParameter("id_turno").equals(Integer.toString(tr.getAbsRow(i).getID_Turno()))) 
										{
											out.print(" selected");
										}
									 } 
									 else 
									 {
										if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) 
										{ 
											if(set.getAbsRow(0).getID_Turno() == tr.getAbsRow(i).getID_Turno() ) 
											{
												out.println(" selected"); 
											}
										}
									 }	  %>><%=  tr.getAbsRow(i).getDescripcion()  %>
						</option>
<%
		}
%>
                      </select></td>
            <td colspan="2"><input name="sindicalizado" type="checkbox" id="sindicalizado" value="checkbox">
              Empleado sindicalizado</td>
          </tr>
          <tr> 
            <td>Depto:</td>
            <td colspan="3"> <input name="id_departamento" type="text" id="id_departamento" size="10" maxlength="4"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_empleados_dlg&lista=id_departamento&idcatalogo=26&nombre=DEPARTAMENTOS&destino=nombre_departamento',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a> 
              <input name="nombre_departamento" type="text" id="nombre_departamento" size="40" maxlength="250" readonly="true"></td>
            <td>Puesto:</td>
            <td colspan="3"><input name="puesto" type="text" id="puesto" size="50" maxlength="40"></td>
          </tr>
          <tr> 
            <td>Nombre:</td>
            <td><input name="nombre" type="text" id="nombre" size="25" maxlength="20"></td>
            <td>A. Paterno:</td>
            <td><input name="apellido_paterno" type="text" id="apellido_paterno" size="25" maxlength="20"></td>
            <td>A. Materno:</td>
            <td><input name="apellido_materno" type="text" id="apellido_materno" size="25" maxlength="20"></td>
            <td width="10%">Nacimiento:</td>
            <td width="15%"><input name="fecha_de_nacimiento" type="text" id="fecha_de_nacimiento" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha_de_nacimiento','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
          <tr> 
            <td>RFC:</td>
            <td><input name="rfc_letras" type="text" id="rfc_letras" size="4" maxlength="4">
              - 
              <input name="rfc_fecha" type="text" id="rfc_fecha" size="6" maxlength="6">
              - 
              <input name="rfc_homoclave" type="text" id="rfc_homoclave" size="3" maxlength="3">
              / 
              <input name="rfc_digito" type="text" id="rfc_digito" size="1" maxlength="1"> 
            </td>
            <td>I.M.S.S.</td>
            <td><input name="num_registro_imss" type="text" id="num_registro_imss" size="20" maxlength="15"></td>
            <td>CURP:</td>
            <td colspan="3"><input name="curp" type="text" id="curp" size="40" maxlength="40"></td>
          </tr>
          <tr> 
            <td>Ingreso:</td>
            <td><input name="fecha_de_ingreso" type="text" id="fecha_de_ingreso" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha_de_ingreso','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td>Cuenta bancaria:</td>
            <td><input name="cuenta_bancaria" type="text" id="cuenta_bancaria" size="25" maxlength="18"></td>
			<td>Banco:</td>
            <td colspan="3">
			<!--input name="id_satbanco" type="text" id="id_satbanco" size="8" maxlength="3"-->
			<select name="id_satbanco" class="cpoBco">
<% 				
				for(int i = 0; i < setBan.getNumRows(); i++)
				{	%>
					<option value="<%=setBan.getAbsRow(i).getClave()%>"<% 
									if(request.getParameter("id_satbanco") != null) {
										if(request.getParameter("id_satbanco").equals(setBan.getAbsRow(i).getClave())) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getID_SatBanco().equals(setBan.getAbsRow(i).getClave())) {
												out.println(" selected"); 
											}
										}
									 }
									 %>><%= setBan.getAbsRow(i).getNombre() %></option>
			<%	}
%>				</select>
              </td>
          </tr>
          <tr> 
            <td>Status:</td>
            <td><select name="status">
                <option value="0"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getStatus() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>Alta</option>
                      <option value="2"<% 
					   				 if(request.getParameter("status") != null) {
										if(request.getParameter("status").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getStatus() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Baja</option>
                    </select></td>
            <td>Fecha baja:</td>
            <td><input name="fecha_para_liquidaciones" type="text" id="fecha_para_liquidaciones" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha_para_liquidaciones','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td>Motivo baja:</td>
            <td colspan="3"><input name="motivo_baja" type="text" id="motivo_baja" size="40" maxlength="40"></td>
          </tr>
          <tr> 
            <td colspan="2">Regimen</td>
			<td colspan="2"><input name="reparto_de_utilidades" type="checkbox" id="reparto_de_utilidades" value="checkbox">
              Reparto de utilidades</td>
            <td colspan="2"><input name="castigo_impuntualidad" type="checkbox" id="castigo_impuntualidad" value="checkbox">
              Castigo por impuntualidad</td>
            <td colspan="2"><input name="aplica_horas_extras" type="checkbox" id="aplica_horas_extras" value="checkbox">
              Aplicaci&oacute;n de horas extras</td>
		  </tr>
		  <tr>
            <td colspan="8">
			     <select name="regimen" style="width:100%">
                	<option value="2"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Sueldos y salarios</option>
						<option value="3"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("3")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 3) {
												out.println(" selected"); 
											}
										}
									 } %>>Jubilados</option>
					<option value="4"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("4")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 4) {
												out.println(" selected"); 
											}
										}
									 } %>>Pensionados</option>
					<option value="5"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("5")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 5) {
												out.println(" selected"); 
											}
										}
									 } %>>Asimilados a salarios, Miembros de las Sociedades Cooperativas de Producción.</option>
                      <option value="6"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("6")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 6) {
												out.println(" selected"); 
											}
										}
									 } %>>Asimilados a salarios, Integrantes de Sociedades y Asociaciones</option>
                      <option value="7"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("7")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 7) {
												out.println(" selected"); 
											}
										}
									 } %>>Asimilados a salarios, Miembros de consejos directivos, de vigilancia, consultivos, honorarios a administradores, comisarios y gerentes generales</option>
                      <option value="8"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("8")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 8) {
												out.println(" selected"); 
											}
										}
									 } %>>Asimilados a salarios, Actividad empresarial (comisionistas)</option>
                      <option value="9"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("9")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 9) {
												out.println(" selected"); 
											}
										}
									 } %>>Asimilados a salarios, Honorarios asimilados a salarios</option>
                      <option value="10"<% 
					   				 if(request.getParameter("regimen") != null) {
										if(request.getParameter("regimen").equals("10")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getRegimen() == 10) {
												out.println(" selected"); 
											}
										}
									 } %>>Asimilados a salarios, Ingresos acciones o títulos valor</option>
                    </select></td>
           
          </tr>
          <tr align="center" bgcolor="#0099FF"> 
            <td colspan="8" class="titChico">Salario y recibo</td>
          </tr>
          <tr> 
            <td>Diario:</td>
            <td><input name="salario_diario" type="text" id="salario_diario" size="10" maxlength="10"></td>
            <td>Nominal:</td>
            <td><input name="salario_nominal" type="text" id="salario_nominal" size="10" maxlength="10"></td>
            <td>Por hora:</td>
            <td><input name="salario_por_hora" type="text" id="salario_por_hora" size="10" maxlength="10"></td>
            <td>Integrado:</td>
            <td><input name="salario_integrado" type="text" id="salario_integrado" size="10" maxlength="10"></td>
          </tr>
          <tr> 
            <td>Mixto:</td>
            <td><input name="salario_mixto" type="text" id="salario_mixto" size="10" maxlength="10"></td>
            <td><input name="calculomixto" type="checkbox" id="calculomixto" value="checkbox">
              Aplicaci&oacute;n del c&aacute;lculo mixto</td>
			<td>PCS:</td>
			<td><input name="pcs" type="text" id="pcs" size="10" maxlength="10">%</td>
            <td><input name="calculosimplificado" type="checkbox" id="calculosimplificado" value="checkbox">
              Calculo simplificado de impuestos</td>
            <td>Categor&iacute;a:</td>
            <td><select style="width: 90%;" name="id_categoria">
<%				      
		for(int i = 0; i< ct.getNumRows(); i++)
		{
%>        
					<option value="<%= ct.getAbsRow(i).getID_Categoria() %>"<% 
									if(request.getParameter("id_categoria") != null) 
									{
										if(request.getParameter("id_categoria").equals(Integer.toString(ct.getAbsRow(i).getID_Categoria()))) 
										{
											out.print(" selected");
										}
									 } 
									 else 
									 {
										if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) 
										{ 
											if(set.getAbsRow(0).getID_Categoria() == ct.getAbsRow(i).getID_Categoria() ) 
											{
												out.println(" selected"); 
											}
										}
									 }	  %>><%=  ct.getAbsRow(i).getDescripcion()  %>
						</option>
<%
		}
%>
                      </select></td>
          </tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr bgcolor="#0099FF"> 
            <td colspan="4" align="center" class="titChico">Datos</td>
          </tr>
          <tr> 
            <td width="15%">Calle:</td>
            <td width="35%"><input name="calle" type="text" id="calle" size="40" maxlength="35"></td>
            <td width="15%">N&uacute;mero:</td>
            <td width="35%"><input name="numero" type="text" id="numero" size="10" maxlength="10"></td>
          </tr>
          <tr> 
            <td>Colonia:</td>
            <td><input name="colonia" type="text" id="colonia" size="40" maxlength="35"></td>
            <td>Interior:</td>
            <td><input name="noint" type="text" id="noint" size="10" maxlength="10"></td>
          </tr>
          <tr> 
            <td>Delegaci&oacute;n:</td>
            <td><input name="delegacion" type="text" id="delegacion" size="40" maxlength="35"></td>
            <td>C&oacute;digo Postal:</td>
            <td><input name="codigo_postal" type="text" id="codigo_postal" size="10" maxlength="5"></td>
          </tr>
          <tr> 
            <td>Localidad:</td>
            <td><input name="localidad" type="text" id="localidad" size="50" maxlength="80"></td>
            <td>Estado:</td>
            <td><input name="estado" type="text" id="estado" size="30" maxlength="40"></td>
          </tr>
		  <tr>
		  	<td>Pais:</td>
            <td><input name="pais" type="text" id="pais" size="15" maxlength="20"></td>
            <td>Avisos (tel, correo, etc):</td>
            <td><input name="en_accidente_avisar" type="text" id="en_accidente_avisar" size="40" maxlength="40"></td>
		  </tr>
		  <tr> 
            <td>Ultimo trabajo:</td>
            <td><input name="ultimo_trabajo" type="text" id="ultimo_trabajo" size="40" maxlength="40"></td>
            <td>Recomendado por:</td>
            <td><input name="recomendado_por" type="text" id="recomendado_por" size="40" maxlength="40"></td>
          </tr>
		  <tr> 
            <td>Envio de CFDI por correo:</td>
            <td><select name="smtp">
                <option value="0"<% 
					   				 if(request.getParameter("smtp") != null) {
										if(request.getParameter("smtp").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set0.getAbsRow(0).getSMTP() == 0) {
												out.println(" selected"); 
											}
										}
									 } %>>No Enviar</option>
                      <option value="1"<% 
					   				 if(request.getParameter("smtp") != null) {
										if(request.getParameter("smtp").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set0.getAbsRow(0).getSMTP() == 1) {
												out.println(" selected"); 
											}
										}
									 } %>>Enviar manualmente</option>
						<option value="2"<% 
					   				 if(request.getParameter("smtp") != null) {
										if(request.getParameter("smtp").equals("2")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set0.getAbsRow(0).getSMTP() == 2) {
												out.println(" selected"); 
											}
										}
									 } %>>Envio automatico al sellar el recibo</option>
                    </select></td>
            <td>Correo electronico:</td>
            <td><input name="email" type="text" id="email" size="20" maxlength="254"></td>
          </tr>
		  <tr align="center" bgcolor="#0099FF"> 
            <td colspan="4" align="center" class="titChico">Personal</td>
          </tr>
          <tr> 
            <td>Estado civil:</td>
            <td><select name="estado_civil" id="estado_civil">
                		<option value="Soltero"<% 
					   				 if(request.getParameter("estado_civil") != null) {
										if(request.getParameter("estado_civil").equals("Soltero")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEstado_Civil().equals("Soltero")) {
												out.println(" selected"); 
											}
										}
									 } %>>Soltero</option>
                        <option value="Casado"<% 
					   				 if(request.getParameter("estado_civil") != null) {
										if(request.getParameter("estado_civil").equals("Casado")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEstado_Civil().equals("Casado")) {
												out.println(" selected"); 
											}
										}
									 } %>>Casado</option>
					
					    <option value="Divorciado"<% 
					   				 if(request.getParameter("estado_civil") != null) {
										if(request.getParameter("estado_civil").equals("Divorciado")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEstado_Civil().equals("Divorciado")) {
												out.println(" selected"); 
											}
										}
									 } %>>Divorciado</option>
									 
					 <option value="Viudo"<% 
					   				 if(request.getParameter("estado_civil") != null) {
										if(request.getParameter("estado_civil").equals("Viudo")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEstado_Civil().equals("Viudo")) {
												out.println(" selected"); 
											}
										}
									 } %>>Viudo</option>
									 
						 <option value="Union libre"<% 
					   				 if(request.getParameter("estado_civil") != null) {
										if(request.getParameter("estado_civil").equals("Union libre")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEstado_Civil().equals("Union libre")) {
												out.println(" selected"); 
											}
										}
									 } %>>Union libre</option>
              </select></td>
            <td>Nombre Esposo(a):</td>
            <td><input name="nombre_esposo" type="text" id="calle7" size="40" maxlength="40"></td>
          </tr>
          <tr> 
            <td>Nombre del Padre:</td>
            <td><input name="nombre_padre" type="text" id="nombre_padre" size="40" maxlength="40"></td>
            <td>Nombre de la Madre:</td>
            <td><input name="nombre_madre" type="text" id="nombre_madre" size="40" maxlength="40"></td>
          </tr>
          <tr> 
            <td>Escolaridad:</td>
            <td colspan="3"><select name="escolaridad" id="escolaridad">
                <option value="Ninguna"<% 
					   				 if(request.getParameter("escolaridad") != null) {
										if(request.getParameter("escolaridad").equals("Ninguna")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEscolaridad().equals("Ninguna")) {
												out.println(" selected"); 
											}
										}
									 } %>>Ninguna</option>
									 
									 <option value="Kinder"<% 
					   				 if(request.getParameter("escolaridad") != null) {
										if(request.getParameter("escolaridad").equals("Kinder")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEscolaridad().equals("Kinder")) {
												out.println(" selected"); 
											}
										}
									 } %>>Kinder</option>
						<option value="Primaria"<% 
					   				 if(request.getParameter("escolaridad") != null) {
										if(request.getParameter("escolaridad").equals("Primaria")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEscolaridad().equals("Primaria")) {
												out.println(" selected"); 
											}
										}
									 } %>>Primaria</option>
				
						<option value="Secundaria"<% 
					   				 if(request.getParameter("escolaridad") != null) {
										if(request.getParameter("escolaridad").equals("Secundaria")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEscolaridad().equals("Secundaria")) {
												out.println(" selected"); 
											}
										}
									 } %>>Secundaria</option>
						<option value="Preparatoria"<% 
					   				 if(request.getParameter("escolaridad") != null) {
										if(request.getParameter("escolaridad").equals("Preparatoria")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEscolaridad().equals("Preparatoria")) {
												out.println(" selected"); 
											}
										}
									 } %>>Preparatoria</option>
									 
						<option value="Universidad"<% 
					   				 if(request.getParameter("escolaridad") != null) {
										if(request.getParameter("escolaridad").equals("Universidad")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEscolaridad().equals("Universidad")) {
												out.println(" selected"); 
											}
										}
									 } %>>Universidad</option>
									 
						<option value="Doctorado"<% 
					   				 if(request.getParameter("escolaridad") != null) {
										if(request.getParameter("escolaridad").equals("Doctorado")) {
											out.print(" selected");
										}
									 } else {
										if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO")) { 
											if(set.getAbsRow(0).getEscolaridad().equals("Doctorado")) {
												out.println(" selected"); 
											}
										}
									 } %>>Doctorado</option>
              </select></td>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr bgcolor="#0099FF"> 
            <td colspan="4" align="center" class="titChico">Infonavit</td>
          </tr>
          <tr> 
            <td width="20%">Alta:</td>
            <td width="30%"> 
              <input name="clave_alta_infonavit" type="checkbox" id="clave_alta_infonavit" value="TRUE"> 
              <input name="fecha_alta_infonavit" type="text" id="fecha_alta_infonavit" size="12" maxlength="15" readonly="true">
              <a href="javascript:NewCal('fecha_alta_infonavit','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td width="20%">Numero de registro:</td>
            <td width="30%"><input name="registro_infonavit" type="text" id="registro_infonavit" size="20" maxlength="13"></td>
          </tr>
          <tr> 
            <td>Prestamo:</td>
            <td><input name="prestamo_infonavit" type="text" id="prestamo_infonavit" size="15" maxlength="12"></td>
            <td>% D&iacute;gito descuento ( Ej: 0.12):</td>
            <td><input name="porcentaje_descuento" type="text" id="porcentaje_descuento" size="8" maxlength="6"></td>
          </tr>
          <tr> 
            <td>Prestamo V.S.M.:</td>
            <td><input name="prestamo_vsm" type="text" id="prestamo_vsm" size="15" maxlength="12"></td>
            <td>Descuento V.S.M.:</td>
            <td><input name="descuento_vsm" type="text" id="descuento_vsm" size="15" maxlength="12"></td>
          </tr>
          <tr bgcolor="#0099FF"> 
            <td colspan="4" align="center" class="titChico">Vales y fonacot</td>
          </tr>
          <tr> 
            <td colspan="2"><input name="ayuda_vales_de_despensa" type="checkbox" id="ayuda_vales_de_despensa" value="TRUE">
              Ayuda con vales de despensa</td>
            <td>Importe:</td>
            <td><input name="importe_vales_de_despensa" type="text" id="importe_vales_de_despensa" size="15" maxlength="12"></td>
          </tr>
          <tr> 
            <td colspan="4"><input name="prestamo_fonacot" type="checkbox" id="prestamo_fonacot" value="TRUE">
              Calcular cr&eacute;ditos fonacot ( En caso de tenerlos )</td>
          </tr>
        </table>
		
	</td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.nom_empleados_dlg.id_empleado.value = '<% if(request.getParameter("id_empleado") != null) { out.print( request.getParameter("id_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getID_Empleado() ); } else { out.print(""); } %>'
document.nom_empleados_dlg.id_departamento.value = '<% if(request.getParameter("id_departamento") != null) { out.print( request.getParameter("id_departamento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getID_Departamento() ); } else { out.print(""); } %>'  
document.nom_empleados_dlg.nombre_departamento.value = '<% if(request.getParameter("nombre_departamento") != null) { out.print( request.getParameter("nombre_departamento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNombre_Departamento() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.puesto.value = '<% if(request.getParameter("puesto") != null) { out.print( request.getParameter("puesto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getPuesto() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.apellido_paterno.value = '<% if(request.getParameter("apellido_paterno") != null) { out.print( request.getParameter("apellido_paterno") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getApellido_Paterno() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.apellido_materno.value = '<% if(request.getParameter("apellido_materno") != null) { out.print( request.getParameter("apellido_materno") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getApellido_Materno() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.rfc_letras.value = '<% if(request.getParameter("rfc_letras") != null) { out.print( request.getParameter("rfc_letras") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getRFC_Letras() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.rfc_fecha.value = '<% if(request.getParameter("rfc_fecha") != null) { out.print( request.getParameter("rfc_fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getRFC_Fecha() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.rfc_homoclave.value = '<% if(request.getParameter("rfc_homoclave") != null) { out.print( request.getParameter("rfc_homoclave") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getRFC_Homoclave() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.rfc_digito.value = '<% if(request.getParameter("rfc_digito") != null) { out.print( request.getParameter("rfc_digito") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getRFC_Digito() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.num_registro_imss.value = '<% if(request.getParameter("num_registro_imss") != null) { out.print( request.getParameter("num_registro_imss") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNum_Registro_IMSS() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.curp.value = '<% if(request.getParameter("curp") != null) { out.print( request.getParameter("curp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getCURP() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.cuenta_bancaria.value = '<% if(request.getParameter("cuenta_bancaria") != null) { out.print( request.getParameter("cuenta_bancaria") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getCuenta_Bancaria() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.motivo_baja.value = '<% if(request.getParameter("motivo_baja") != null) { out.print( request.getParameter("motivo_baja") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getMotivo_Baja() ); } else { out.print(""); } %>' 

document.nom_empleados_dlg.calle.value = '<% if(request.getParameter("calle") != null) { out.print( request.getParameter("calle") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getCalle() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.numero.value = '<% if(request.getParameter("numero") != null) { out.print( request.getParameter("numero") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNumero() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.colonia.value = '<% if(request.getParameter("colonia") != null) { out.print( request.getParameter("colonia") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getColonia() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.codigo_postal.value = '<% if(request.getParameter("codigo_postal") != null) { out.print( request.getParameter("codigo_postal") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getCodigo_Postal() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.delegacion.value = '<% if(request.getParameter("delegacion") != null) { out.print( request.getParameter("delegacion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getDelegacion() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.noint.value = '<% if(request.getParameter("noint") != null) { out.print( request.getParameter("noint") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNoInt() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.localidad.value = '<% if(request.getParameter("localidad") != null) { out.print( request.getParameter("localidad") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getLocalidad() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.estado.value = '<% if(request.getParameter("estado") != null) { out.print( request.getParameter("estado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getEstado() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.pais.value = '<% if(request.getParameter("pais") != null) { out.print( request.getParameter("pais") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getPais() ); } else { out.print("Mexico"); } %>' 
document.nom_empleados_dlg.email.value = '<% if(request.getParameter("email") != null) { out.print( request.getParameter("email") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set0.getAbsRow(0).getEMail() ); } else { out.print(""); } %>' 

document.nom_empleados_dlg.en_accidente_avisar.value = '<% if(request.getParameter("en_accidente_avisar") != null) { out.print( request.getParameter("en_accidente_avisar") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getEn_Accidente_Avisar() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.nombre_esposo.value = '<% if(request.getParameter("nombre_esposo") != null) { out.print( request.getParameter("nombre_esposo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNombre_Esposo() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.nombre_padre.value = '<% if(request.getParameter("nombre_padre") != null) { out.print( request.getParameter("nombre_padre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNombre_Padre() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.nombre_madre.value = '<% if(request.getParameter("nombre_madre") != null) { out.print( request.getParameter("nombre_madre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getNombre_Madre() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.ultimo_trabajo.value = '<% if(request.getParameter("ultimo_trabajo") != null) { out.print( request.getParameter("ultimo_trabajo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getUltimo_Trabajo() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.recomendado_por.value = '<% if(request.getParameter("recomendado_por") != null) { out.print( request.getParameter("recomendado_por") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getRecomendado_Por() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.registro_infonavit.value = '<% if(request.getParameter("registro_infonavit") != null) { out.print( request.getParameter("registro_infonavit") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getRegistro_Infonavit() ); } else { out.print(""); } %>' 

document.nom_empleados_dlg.salario_diario.value = '<% if(request.getParameter("salario_diario") != null) { out.print( request.getParameter("salario_diario") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getSalario_Diario() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.salario_nominal.value = '<% if(request.getParameter("salario_nominal") != null) { out.print( request.getParameter("salario_nominal") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getSalario_Nominal() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.salario_integrado.value = '<% if(request.getParameter("salario_integrado") != null) { out.print( request.getParameter("salario_integrado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getSalario_Integrado() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.salario_por_hora.value = '<% if(request.getParameter("salario_por_hora") != null) { out.print( request.getParameter("salario_por_hora") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getSalario_por_Hora() ); } else { out.print(""); } %>' 
document.nom_empleados_dlg.salario_mixto.value = '<% if(request.getParameter("salario_mixto") != null) { out.print( request.getParameter("salario_mixto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getSalario_Mixto() ); } else { out.print("0.00"); } %>' 
document.nom_empleados_dlg.pcs.value = '<% if(request.getParameter("pcs") != null) { out.print( request.getParameter("pcs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getPCS() ); } else { out.print("0.00"); } %>' 
document.nom_empleados_dlg.prestamo_infonavit.value = '<% if(request.getParameter("prestamo_infonavit") != null) { out.print( request.getParameter("prestamo_infonavit") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getPrestamo_Infonavit() ); } else { out.print("0.00"); } %>' 
document.nom_empleados_dlg.porcentaje_descuento.value = '<% if(request.getParameter("porcentaje_descuento") != null) { out.print( request.getParameter("porcentaje_descuento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getPorcentaje_Descuento() ); } else { out.print("0"); } %>' 
document.nom_empleados_dlg.prestamo_vsm.value = '<% if(request.getParameter("prestamo_vsm") != null) { out.print( request.getParameter("prestamo_vsm") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getPrestamo_VSM() ); } else { out.print("0.00"); } %>' 
document.nom_empleados_dlg.descuento_vsm.value = '<% if(request.getParameter("descuento_vsm") != null) { out.print( request.getParameter("descuento_vsm") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getDescuento_VSM() ); } else { out.print("0.00"); } %>' 
document.nom_empleados_dlg.importe_vales_de_despensa.value = '<% if(request.getParameter("importe_vales_de_despensa") != null) { out.print( request.getParameter("importe_vales_de_despensa") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( set.getAbsRow(0).getImporte_Vales_de_Despensa() ); } else { out.print("0.00"); } %>' 

document.nom_empleados_dlg.fecha_de_ingreso.value = '<% if(request.getParameter("fecha_de_ingreso") != null) { out.print( request.getParameter("fecha_de_ingreso") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getFecha_de_Ingreso(), "dd/MMM/yyyy") ); } else { out.print(JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
document.nom_empleados_dlg.fecha_de_nacimiento.value = '<% if(request.getParameter("fecha_de_nacimiento") != null) { out.print( request.getParameter("fecha_de_nacimiento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getFecha_de_Nacimiento(), "dd/MMM/yyyy") ); } else { out.print("") ; } %>'
document.nom_empleados_dlg.fecha_para_liquidaciones.value = '<% if(request.getParameter("fecha_para_liquidaciones") != null) { out.print( request.getParameter("fecha_para_liquidaciones") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getFecha_para_Liquidaciones(), "dd/MMM/yyyy") ); } else { out.print(""); } %>'
document.nom_empleados_dlg.fecha_alta_infonavit.value = '<% if(request.getParameter("fecha_alta_infonavit") != null) { out.print( request.getParameter("fecha_alta_infonavit") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getFecha_Alta_Infonavit(), "dd/MMM/yyyy") ); } else { out.print(""); } %>'


document.nom_empleados_dlg.sindicalizado.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getSindicalizado() ? "true" : "false" ) ); } else if(request.getParameter("sindicalizado") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.aplica_horas_extras.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getAplica_Horas_Extras() ? "true" : "false" ) ); } else if(request.getParameter("aplica_horas_extras") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.castigo_impuntualidad.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getCastigo_Impuntualidad() ? "true" : "false" ) ); } else if(request.getParameter("castigo_impuntualidad") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.reparto_de_utilidades.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getReparto_de_Utilidades() ? "true" : "false" ) ); } else if(request.getParameter("reparto_de_utilidades") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.calculomixto.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getCalculoMixto() ? "true" : "false" ) ); } else if(request.getParameter("calculomixto") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.calculosimplificado.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getCalculoSimplificado() ? "true" : "false" ) ); } else if(request.getParameter("calculosimplificado") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.clave_alta_infonavit.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getClave_Alta_Infonavit() ? "true" : "false" ) ); } else if(request.getParameter("clave_alta_infonavit") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.ayuda_vales_de_despensa.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getAyuda_Vales_de_Despensa() ? "true" : "false" ) ); } else if(request.getParameter("ayuda_vales_de_despensa") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_empleados_dlg.prestamo_fonacot.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_EMPLEADO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_EMPLEADO") ) { out.print( (set.getAbsRow(0).getPrestamo_Fonacot()  != 0 ? "true" : "false" ) ); } else if(request.getParameter("prestamo_fonacot") != null ) { out.print("true"); } else { out.print("false"); } %>  
</script>
</body>
</html>
