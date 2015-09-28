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
<%@ page import="forseti.*, forseti.sets.*, forseti.nomina.*, java.util.*, java.io.*" %>
<%
	String nom_plantillas_dlg = (String)request.getAttribute("nom_plantillas_dlg");
	if(nom_plantillas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_PLANTILLAS").generarTitulo(JUtil.Msj("CEF","NOM_PLANTILLAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	
	JPlantillasSetCons set = new JPlantillasSetCons(request);
	if( request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") )
	{
		set.m_Where = "ID_Plantilla = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	JAdmCompaniasSet cs = new JAdmCompaniasSet(request);
	cs.Open();
	
	session = request.getSession(true);
    JNomPlantillasSes rec = (JNomPlantillasSes)session.getAttribute("nom_plantillas_dlg");

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
function limpiarFormulario()
{
	document.nom_plantillas_dlg.id_empleadoex.value = "";
	document.nom_plantillas_dlg.nombre_empleadoex.value = "";
}

function enviarlo(formAct)
{
	if(formAct.proceso.value == "AGREGAR_PLANTILLA" || formAct.proceso.value == "CAMBIAR_PLANTILLA")
	{
		if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
		{
			if(!esCadena("Clave:", formAct.id_empleadoex.value, 1, 6) )
				return false;
			else
				return true;
		}
		else if(formAct.subproceso.value == "BORR_PART")
			return true;
			
		if(!esNumeroEntero('Movimiento:', formAct.id_movimiento.value, 0, 32000) ||
				!esNumeroEntero('Numero de nmina:', formAct.numero_nomina.value, 0, 254) ||
				!esNumeroEntero('Año:', formAct.ano.value, 0, 2100) ||
				!esNumeroDecimal('Cantidad:', formAct.cantidad.value, 0, 99999999.99, 2) ||  
				!esNumeroDecimal('Exento:', formAct.exento.value, 0, 99999999.99, 2))
			return false;
		else
		{
			if(formAct.subproceso.value == "ENVIAR")
			{
				if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
				{
					formAct.aceptar.disabled = true;
					return true;
				}
				else
					return false;
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomPlantillasDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_plantillas_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomPlantillasCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
                <input name="subproceso" type="hidden" value="ENVIAR">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
                Movimiento:</div></td>
            <td colspan="2"> <input name="id_movimiento" type="text" id="id_movimiento" size="7" maxlength="4"> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_plantillas_dlg&lista=id_movimiento&idcatalogo=29&nombre=MOVIMIENTOS&destino=nombre_movimiento',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a> 
              <input name="nombre_movimiento" type="text" id="nombre_movimiento" size="40" maxlength="250" readonly="true"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Fecha:</div></td>
            <td colspan="2"><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Descripci&oacute;n:</div></td>
            <td colspan="2"><input name="descripcion" type="text" id="descripcion" size="60" maxlength="80"> </td>
          </tr>
		  <tr> 
            <td> <div align="right">Tipo:</div></td>
            <td colspan="2"><select name="tipo_de_nomina" class="cpoCol" id="tipo_de_nomina">
                      <option value="-1"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("-1")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getTipo_de_Nomina() == -1) {
												out.println(" selected"); 
											}
										}
									 } %>>--- Selecciona el tipo ---</option>
						<option value="12"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("12")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getTipo_de_Nomina() == 1 || set.getAbsRow(0).getTipo_de_Nomina() == 2 || set.getAbsRow(0).getTipo_de_Nomina() == 12) {
												out.println(" selected"); 
											}
										}
									 } %>>Normal</option>
                <option value="34"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("34")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getTipo_de_Nomina() == 3 || set.getAbsRow(0).getTipo_de_Nomina() == 4 || set.getAbsRow(0).getTipo_de_Nomina() == 34) {
												out.println(" selected"); 
											}
										}
									 } %>>Especial</option>
				<option value="56"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("56")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getTipo_de_Nomina() == 5 || set.getAbsRow(0).getTipo_de_Nomina() == 6 || set.getAbsRow(0).getTipo_de_Nomina() == 56) {
												out.println(" selected"); 
											}
										}
									 } %>>Aguinaldo</option>
				<option value="78"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("78")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getTipo_de_Nomina() == 7 || set.getAbsRow(0).getTipo_de_Nomina() == 8 || set.getAbsRow(0).getTipo_de_Nomina() == 78) {
												out.println(" selected"); 
											}
										}
									 } %>>Vales</option>
				</select></td>
          </tr>
          <tr> 
            <td colspan="3" align="center" class="titChico">Esta plantilla aplicar&aacute; 
              para: </td>
          </tr>
		  <tr> 
            <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%"> <input name="bempleado" type="checkbox" id="bempleado" value="checkbox">
                    el empleado:</td>
                  <td><input name="id_empleado" type="text" id="id_empleado" size="10" maxlength="6">
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_plantillas_dlg&lista=id_empleado&idcatalogo=28&nombre=EMPLEADOS&destino=nombre_empleado&esp1=_FSI_CAT',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a>
                    <input name="nombre_empleado" type="text" id="nombre_empleado" size="40" maxlength="250" readonly="true"></td>
                </tr>
                <tr> 
                  <td><input name="bnumero" type="checkbox" id="bnumero" value="checkbox">
                    el n&uacute;mero:</td>
                  <td><input name="numero_nomina" type="text" id="numero_nomina" size="6" maxlength="3">
                    del A&ntilde;o 
                    <input name="ano" type="text" id="ano" size="8" maxlength="4"></td>
                </tr>
                <tr> 
                  <td><input name="bcompania_sucursal" type="checkbox" id="bcompania_sucursal" value="checkbox">
                    esta n&oacute;mina:</td>
                  <td><select class="cpoCol" style="width: 90%;" name="compania_sucursal">
						<option value="_FSI_CS"<% 
									if(request.getParameter("compania_sucursal") != null) 
									{
										if(request.getParameter("compania_sucursal").equals("_FSI_CS")) 
										{
											out.print(" selected");
										}
									 } 
									 %>>--- Selecciona la nmina ---</option>
<%				      
		for(int i = 0; i< cs.getNumRows(); i++)
		{
%>        
					<option value="<%= cs.getAbsRow(i).getDescripcion() %>"<% 
									if(request.getParameter("compania_sucursal") != null) 
									{
										if(request.getParameter("compania_sucursal").equals(cs.getAbsRow(i).getDescripcion())) 
										{
											out.print(" selected");
										}
									 } 
									 else 
									 {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) 
										{ 
											if(set.getAbsRow(0).getsCompania_Sucursal().equals(cs.getAbsRow(i).getDescripcion()) ) 
											{
												out.println(" selected"); 
											}
										}
									 }	  %>><%=  cs.getAbsRow(i).getNombre()  %>
						</option>
<%
		}
%>
                      </select></td>
                </tr>
                <tr> 
                  <td><input name="bnivel_confianza" type="checkbox" id="bnivel_confianza" value="checkbox">
                    nivel de confianza: </td>
                  <td><select name="nivel_de_confianza" class="cpoCol">
				  <option value="-1"<% if(request.getParameter("nivel_de_confianza") != null) {
										if(request.getParameter("nivel_de_confianza").equals("-1")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getNivel_de_Confianza() == -1) {
												out.print(" selected"); 
											}
										}
									 } %>>--- Selecciona el nivel de empleado ---</option>
                <option value="0"<% if(request.getParameter("nivel_de_confianza") != null) {
										if(request.getParameter("nivel_de_confianza").equals("0")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getNivel_de_Confianza() == 0) {
												out.print(" selected"); 
											}
										}
									 } %>>No Sindicalizados</option>
                <option value="1"<% if(request.getParameter("nivel_de_confianza") != null) {
										if(request.getParameter("nivel_de_confianza").equals("1")) {
											out.print(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { 
											if(set.getAbsRow(0).getNivel_de_Confianza() == 1) {
												out.print(" selected"); 
											}
										}
									 } %>>Sindicalizados</option>
				   </select></td>
                </tr>
              </table></td>
          </tr>
		  <tr>
		    <td colspan="3">
					<table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td width="20%">Cantidad de aplicaci&oacute;n:</td>
                  <td colspan="3"><input name="cantidad" type="text" id="cantidad" size="12" maxlength="11"></td>
                </tr>
                <tr> 
                  <td width="20%">Tipo de aplicaci&oacute;n:</td>
                  <td><input type="radio" name="aplicacion" value="1"<% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getAplicacion() == 1 ? " checked" : "" ) ); } else if(request.getParameter("aplicacion") != null && request.getParameter("aplicacion").equals("1")) { out.print(" checked"); } %>>
                    Salario(s) diario 
                    <input name="mixto" type="checkbox" id="mixto" value="checkbox">
                    Mixto</td>
                  <td><input type="radio" name="aplicacion" value="0"<% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getAplicacion() == 0 ? " checked" : "" ) ); } else if(request.getParameter("aplicacion") != null && request.getParameter("aplicacion").equals("0")) { out.print(" checked"); } %>>
                    Hora(s)</td>
                  <td><input type="radio" name="aplicacion" value="2"<% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getAplicacion() == 2 ? " checked" : "" ) ); } else if(request.getParameter("aplicacion") != null && request.getParameter("aplicacion").equals("2")) { out.print(" checked"); } %>>
                    Importe</td>
                </tr>
                <tr> 
                  <td>&nbsp;</td>
                  <td colspan="3"><input name="bexento" type="checkbox" id="bexento" value="checkbox">
                    Selecciona aqui si el resultado de la plantilla aplicar&aacute; 
                    un importe m&aacute;ximo exento</td>
                </tr>
                <tr> 
                  <td>Importe m&aacute;ximo exento:</td>
                  <td colspan="3"><input name="exento" type="text" id="exento" size="12" maxlength="11"></td>
                </tr>
                <tr> 
                  <td colspan="4"><input name="inclusiones" type="checkbox" id="inclusiones" value="checkbox">
                    Selecciona aqu&iacute; en caso de que la siguiente lista contenga 
                    los empleados que aplicar&aacute;n a esta plantilla de n&oacute;mina. 
                    Si no est&aacute; seleccionada esta casilla, los empleados 
                    de la lista ser&aacute;n los que se excluyan de esta plantilla 
                    de n&oacute;mina.</td>
                </tr>
              </table>
			</td>
		  </tr>
		  <tr> 
            <td colspan="3"> <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="20%" class="titChico">Clave</td>
                  <td class="titChico">Nombre</td>
                  <td width="10%">&nbsp;</td>
                </tr>
                <%
	if( !request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") )
	{
%>
                <tr> 
                  <td><input name="id_empleadoex" type="text" id="id_empleadoex" size="10" maxlength="6"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_plantillas_dlg&lista=id_empleadoex&idcatalogo=28&nombre=EMPLEADOS&destino=nombre_empleadoex&esp1=_FSI_CAT',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a> 
                  </td>
                  <td><input name="nombre_empleadoex" type="text" id="nombre_empleadoex" size="40" maxlength="250" readonly="true"></td>
                  <td align="right"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0"> 
                    <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
                </tr>
<%
	}
	
	if(rec.numPartidas() == 0)
	{
		out.println("<tr><td align=\"center\" class=\"titCuerpoBco\" colspan=\"3\">Inserta los empleados excluidos</td></tr>");
	}
	else
	{						
		for(int i = 0; i < rec.numPartidas(); i++)
		{
%>
                <tr> 
                  <td width="20%"><%= rec.getPartida(i).getID_Empleado() %></td>
                  <td><%= rec.getPartida(i).getNombre() %></td>
                  <td width="10%" align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_PLANTILLA")) { %> <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0">
                    <% } else { out.print("&nbsp;"); } %></td>
                </tr>
                <%
		}
	}
%>
              </table></td>
          </tr>
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_plantillas_dlg.id_movimiento.value = '<% if(request.getParameter("id_movimiento") != null) { out.print( request.getParameter("id_movimiento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getID_Movimiento() ); } else { out.print(""); } %>' 
document.nom_plantillas_dlg.nombre_movimiento.value = '<% if(request.getParameter("nombre_movimiento") != null) { out.print( request.getParameter("nombre_movimiento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getMovimiento() ); } else { out.print(""); } %>' 
document.nom_plantillas_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy") ); } else { out.print("") ; } %>'
document.nom_plantillas_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 

document.nom_plantillas_dlg.bempleado.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getbID_Empleado() ? "true" : "false" ) ); } else if(request.getParameter("bempleado") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_plantillas_dlg.bnumero.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getbNomina() ? "true" : "false" ) ); } else if(request.getParameter("bnumero") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_plantillas_dlg.bnivel_confianza.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getbNivel_Confianza() ? "true" : "false" ) ); } else if(request.getParameter("bnivel_confianza") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_plantillas_dlg.bcompania_sucursal.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getbCompania_Sucursal() ? "true" : "false" ) ); } else if(request.getParameter("bcompania_sucursal") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_plantillas_dlg.inclusiones.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getInclusiones() ? "true" : "false" ) ); } else if(request.getParameter("inclusiones") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_plantillas_dlg.mixto.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getMixto() ? "true" : "false" ) ); } else if(request.getParameter("mixto") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_plantillas_dlg.bexento.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_PLANTILLA") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") ) { out.print( (set.getAbsRow(0).getbExento() ? "true" : "false" ) ); } else if(request.getParameter("bexento") != null ) { out.print("true"); } else { out.print("false"); } %>  


document.nom_plantillas_dlg.id_empleado.value = '<% if(request.getParameter("id_empleado") != null) { out.print( request.getParameter("id_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getID_Empleado() ); } else { out.print(""); } %>' 
document.nom_plantillas_dlg.nombre_empleado.value = '<% if(request.getParameter("nombre_empleado") != null) { out.print( request.getParameter("nombre_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getEmpleado() ); } else { out.print(""); } %>' 
document.nom_plantillas_dlg.ano.value = '<% if(request.getParameter("ano") != null) { out.print( request.getParameter("ano") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getAno() ); } else { out.print("0"); } %>' 
document.nom_plantillas_dlg.numero_nomina.value = '<% if(request.getParameter("numero_nomina") != null) { out.print( request.getParameter("numero_nomina") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getNumero_Nomina() ); } else { out.print("0"); } %>' 
document.nom_plantillas_dlg.aplicacion.value = '<% if(request.getParameter("aplicacion") != null) { out.print( request.getParameter("aplicacion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getAplicacion() ); } else { out.print("0"); } %>' 
document.nom_plantillas_dlg.exento.value = '<% if(request.getParameter("exento") != null) { out.print( request.getParameter("exento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getExento() ); } else { out.print("0"); } %>' 
document.nom_plantillas_dlg.cantidad.value = '<% if(request.getParameter("cantidad") != null) { out.print( request.getParameter("cantidad") ); } else if(!request.getParameter("proceso").equals("AGREGAR_PLANTILLA")) { out.print( set.getAbsRow(0).getCantidad() ); } else { out.print("1"); } %>' 

<%
	if( !request.getParameter("proceso").equals("CONSULTAR_PLANTILLA") )
	{
%>	
document.nom_plantillas_dlg.id_empleadoex.value = '<% if(request.getParameter("id_empleadoex") != null) { out.print( request.getParameter("id_empleadoex") ); } else { out.print(""); } %>'
document.nom_plantillas_dlg.nombre_empleadoex.value = '<% if(request.getParameter("nombre_empleadoex") != null) { out.print( request.getParameter("nombre_empleadoex") ); } else { out.print(""); } %>'
<%
	}
%>	
</script>
</body>
</html>
