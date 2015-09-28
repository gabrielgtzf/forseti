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
	String nom_movimientos_dlg = (String)request.getAttribute("nom_movimientos_dlg");
	if(nom_movimientos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_MOVIMIENTOS").generarTitulo(JUtil.Msj("CEF","NOM_MOVIMIENTOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	
	JMovimientosNomSet set = new JMovimientosNomSet(request);
	if( !request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO") )
	{
		set.m_Where = "ID_Movimiento = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	
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
	if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
	{
		if( !esCadena("Clave:", formAct.id_departamento.value, 4, 4) ||
			!verifCuenta("Cuenta contable:", formAct.cuenta.value))
			return false;
		else
			return true;
	}
	else if( !esNumeroEntero('Clave:', formAct.id_movimiento.value, 1, 999) )
	{
		return false;
	}
	else if(formAct.subproceso.value == "ENVIAR")
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
		return true;
		
}

function limpiarFormulario()
{
	document.nom_movimientos_dlg.cuenta.value = "";
	document.nom_movimientos_dlg.nombre.value = "";
	document.nom_movimientos_dlg.id_departamento.value = "";
	document.nom_movimientos_dlg.nombre_departamento.value = "";
}

function editarPartida(idpartida, cuenta, nombre, id_departamento, nombre_departamento)
{
	document.nom_movimientos_dlg.idpartida.value = idpartida;
	document.nom_movimientos_dlg.subproceso.value = "EDIT_PART";

	document.nom_movimientos_dlg.cuenta.value = cuenta;
	document.nom_movimientos_dlg.nombre.value = nombre;
	document.nom_movimientos_dlg.id_departamento.value = id_departamento;
	document.nom_movimientos_dlg.nombre_departamento.value = nombre_departamento;
}

-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomMovimNomDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_movimientos_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomMovimNomCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
		
			     Clave:</div></td>
            <td> <input name="id_movimiento" type="text" id="id_movimiento" size="8" maxlength="4"<%= (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO")) ? " readonly=\"true\"" : "" %>></td>
            <td width="20%" align="right"> Clave SAT:</td>
			<td width="20%"><input name="id_sat" type="text" id="id_sat" size="8" maxlength="3">
              (3 digitos)</td>
			<td width="20%"><input type="checkbox" name="esdeduccion" value="checkbox">
              Es deducci&oacute;n</td>
          </tr>
          <tr> 
            <td> <div align="right">Descripcion:</div></td>
            <td colspan="4"> <input name="descripcion" type="text" id="descripcion" size="60" maxlength="40"></td>
          </tr>
          <tr align="center"> 
            <td colspan="5" class="titChicoAzc">Aplicaci&oacute;n de impuestos</td>
          </tr>
		   <tr align="center"> 
            <td colspan="5">
			
		<table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td width="20%">&nbsp;</td>
            <td width="40%"><input type="checkbox" name="imss" value="checkbox">
              IMSS </td>
            <td width="40%"><input type="checkbox" name="sar" value="checkbox">
              SAR</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="checkbox" name="ispt" value="checkbox">
              ISPT </td>
            <td><input type="checkbox" name="infonavit" value="checkbox">
              Infonavit</td>
          </tr>
          <tr> 
            <td> <div align="right"></div></td>
            <td><input type="checkbox" name="dospor" value="checkbox">
              2% / N&oacute;mina</td>
            <td><input type="checkbox" name="ptu" value="checkbox">
              PTU</td>
          </tr>
		 </table>
		 </td>
		 </tr>
		 </table>
		 <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr align="center"> 
            <td colspan="5" class="titChicoAzc">Cuentas contables y departamentos a los que se direccionar&aacute; 
              el movimiento:</td>
          </tr>
          <tr align="center"> 
            <td colspan="5" class="titChico">&nbsp;</td>
          </tr>
 		  <tr bgcolor="#0099FF"> 
            <td width="15%" align="left" class="titChico">Cuenta</td>
            <td width="40%" class="titChico">Nombre</td>
            <td width="10%" align="left" class="titChico">Clave</td>
            <td width="25%" class="titChico">Departamento</td>
            <td class="titChico">&nbsp;</td>
          </tr>
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") )
	{
%>		  
          <tr> 
            <td width="15%" align="left"> <input name="cuenta" type="text" id="cuenta" size="10" maxlength="25"<% if(request.getParameter("cuenta") != null) { out.print(" value=\"" + request.getParameter("cuenta") + "\""); } %>> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_movimientos_dlg&lista=cuenta&idcatalogo=3&nombre=CUENTAS&destino=nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
            <td width="40%"><input name="nombre" type="text" id="nombre" size="40" maxlength="250" readonly="true"<% if(request.getParameter("nombre") != null) { out.print(" value=\"" + request.getParameter("nombre") + "\""); } %>></td>
            <td width="10%" align="left"> <input name="id_departamento" type="text" id="id_departamento" size="6" maxlength="4"<% if(request.getParameter("id_departamento") != null) { out.print(" value=\"" + request.getParameter("id_departamento") + "\""); } %>> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_movimientos_dlg&lista=id_departamento&idcatalogo=26&nombre=DEPARTAMENTOS&destino=nombre_departamento',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
            <td width="25%"><input name="nombre_departamento" type="text" id="nombre_departamento" size="30" maxlength="250" readonly="true"<% if(request.getParameter("nombre_departamento") != null) { out.print(" value=\"" + request.getParameter("nombre_departamento") + "\""); } %>></td>
            <td align="right" valign="top"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0"> 
              <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
          </tr>
<%
	}
	
	session = request.getSession(true);
   	JNomMovimNomSes pol = (JNomMovimNomSes)session.getAttribute("nom_movimientos_dlg");
	if(pol.numPartidas() == 0)
	{
		out.println("<tr><td align=\"center\" class=\"titCuerpoBco\" colspan=\"5\">Inserta aqu las cuentas y departamentos direccionados.</td></tr>");
	}
	else
	{						
		for(int i = 0; i < pol.numPartidas(); i++)
		{
%>
          <tr> 
            <td width="15%" align="left"><%= JUtil.obtCuentaFormato(new StringBuffer(pol.getPartida(i).getCuenta()), request) %></td>
            <td width="40%"><%= pol.getPartida(i).getNombre() %></td>
            <td width="10%" align="left"><%= pol.getPartida(i).getID_Departamento() %></td>
            <td width="25%" align="left"><%= pol.getPartida(i).getNombre_Departamento() %></td>
            <td align="right" valign="top">
              <% if(!request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO")) { %>
              <a href="javascript:editarPartida('<%= i %>','<%= JUtil.obtCuentaFormato(new StringBuffer(pol.getPartida(i).getCuenta()), request) %>','<%= pol.getPartida(i).getNombre() %>','<%= pol.getPartida(i).getID_Departamento() %>','<%= pol.getPartida(i).getNombre_Departamento() %>');"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a> 
              <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0">
              <% } else { out.print("&nbsp;"); } %>
            </td>
          </tr>
<%
		}
	}
%>
        </table>
    </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_movimientos_dlg.id_movimiento.value = '<% if(request.getParameter("id_movimiento") != null) { out.print( request.getParameter("id_movimiento") ); } else if(!request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO")) { out.print( set.getAbsRow(0).getID_Movimiento() ); } else { out.print(""); } %>'  
document.nom_movimientos_dlg.id_sat.value = '<% if(request.getParameter("id_sat") != null) { out.print( request.getParameter("id_sat") ); } else if(!request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO")) { out.print( set.getAbsRow(0).getID_SAT() ); } else { out.print("000"); } %>'  
document.nom_movimientos_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.nom_movimientos_dlg.esdeduccion.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") ) { out.print( (set.getAbsRow(0).getDeduccion() ? "true" : "false" ) ); } else if(request.getParameter("esdeduccion") != null ) { out.print("true"); } else { out.print("false"); } %>  

document.nom_movimientos_dlg.imss.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") ) { out.print( (set.getAbsRow(0).getIMSS() ? "true" : "false" ) ); } else if(request.getParameter("imss") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_movimientos_dlg.sar.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") ) { out.print( (set.getAbsRow(0).getSAR() ? "true" : "false" ) ); } else if(request.getParameter("sar") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_movimientos_dlg.ispt.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") ) { out.print( (set.getAbsRow(0).getISPT() ? "true" : "false" ) ); } else if(request.getParameter("ispt") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_movimientos_dlg.infonavit.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") ) { out.print( (set.getAbsRow(0).getINFONAVIT() ? "true" : "false" ) ); } else if(request.getParameter("infonavit") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_movimientos_dlg.dospor.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") ) { out.print( (set.getAbsRow(0).getDOSPOR() ? "true" : "false" ) ); } else if(request.getParameter("dospor") != null ) { out.print("true"); } else { out.print("false"); } %>  
document.nom_movimientos_dlg.ptu.checked = <% if( (request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO") && request.getParameter("subproceso") == null) || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO") ) { out.print( (set.getAbsRow(0).getPTU() ? "true" : "false" ) ); } else if(request.getParameter("ptu") != null ) { out.print("true"); } else { out.print("false"); } %>  
</script>
</body>
</html>
