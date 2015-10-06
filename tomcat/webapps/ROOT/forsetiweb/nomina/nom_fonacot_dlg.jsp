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
	String nom_fonacot_dlg = (String)request.getAttribute("nom_fonacot_dlg");
	if(nom_fonacot_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_FONACOT").generarTitulo(JUtil.Msj("CEF","NOM_FONACOT","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");

	JFonacotSet set = new JFonacotSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_FONACOT") || request.getParameter("proceso").equals("CONSULTAR_FONACOT") )
	{
		set.m_Where = "ID_Credito = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
	
	session = request.getSession(true);
    JNomFonacotSes rec = (JNomFonacotSes)session.getAttribute("nom_fonacot_dlg");

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
	document.nom_fonacot_dlg.fechadesc.value = "";
	document.nom_fonacot_dlg.descuento.value = "0.00";
}

function editarPartida(idpartida, fechadesc, descuento)
{
	document.nom_fonacot_dlg.idpartida.value = idpartida;
	document.nom_fonacot_dlg.subproceso.value = "EDIT_PART";

	document.nom_fonacot_dlg.fechadesc.value = fechadesc;
	document.nom_fonacot_dlg.descuento.value = descuento;
}

function enviarlo(formAct)
{
	if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
	{
		if(!esCadena("Fecha del descuento:", formAct.fechadesc.value, 1, 11)  || 
			!esNumeroDecimal('Descuento:', formAct.descuento.value, 0, 99999999.99, 2) )
			return false;
		else
			return true;
	}
	else
	{
		if(formAct.subproceso.value == "ENVIAR")
		{
			if(formAct.proceso.value == "AGREGAR_FONACOT" || formAct.proceso.value == "CAMBIAR_FONACOT")
			{
				if(!esCadena("Crdito:", formAct.id_credito.value, 1, 10) ||
						!esCadena("Empleado:", formAct.id_empleado.value, 1, 6)  ||
						!esNumeroEntero('Faltante:', formAct.meses.value, 0, 99) ||
						!esNumeroEntero('Plazo:', formAct.plazo.value, 0, 99) ||
						!esNumeroDecimal('Importe:', formAct.importe.value, 0, 99999999.99, 2) ||  
						!esNumeroDecimal('Retencin:', formAct.retencion.value, 0, 99999999.99, 2))
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
		else
			return true;
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomFonacotDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_fonacot_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_FONACOT")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomFonacotCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td width="20%"><input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
              <input name="subproceso" type="hidden" value="ENVIAR"> 
			  <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
              <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
              Cr&eacute;dito:</td>
            <td width="30%"><input name="id_credito" type="text" id="id_credito" size="15" maxlength="15"<%= (request.getParameter("proceso").equals("CAMBIAR_FONACOT")) ? " readonly=\"true\"" : "" %>> 
            </td>
            <td width="20%">Fecha:</td>
            <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
          <tr> 
            <td width="20%">Empleado:</td>
            <td colspan="3"><input name="id_empleado" type="text" id="id_empleado" size="10" maxlength="6"<%= (request.getParameter("proceso").equals("CAMBIAR_FONACOT")) ? " readonly=\"true\"" : "" %>> 
              <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_fonacot_dlg&lista=id_empleado&idcatalogo=28&nombre=EMPLEADOS&destino=nombre_empleado&esp1=_FSI_CAT',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a> 
              <input name="nombre_empleado" type="text" id="nombre_empleado" size="40" maxlength="250" readonly="true"></td>
          </tr>
		  <tr> 
            <td width="20%"> Faltante:</td>
            <td width="30%"><input name="meses" type="text" id="meses" size="5" maxlength="3">
              meses </td>
            <td width="20%">Plazo:</td>
            <td><input name="plazo" type="text" id="plazo" size="5" maxlength="3">
              meses </td>
          </tr>
		  <tr> 
            <td width="20%"> Importe:</td>
            <td width="30%"><input name="importe" type="text" id="importe" size="12" maxlength="10"> 
            </td>
            <td width="20%">Retenci&oacute;n:</td>
            <td><input name="retencion" type="text" id="retencion" size="12" maxlength="10"> 
            </td>
          </tr>
          <tr> 
            <td colspan="4"> 
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr bgcolor="#0099FF"> 
                  <td width="30%" class="titChico">Fecha</td>
                  <td align="right" class="titChico">Descuento</td>
                  <td width="30%">&nbsp;</td>
                </tr>
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_FONACOT") )
	{
%>
                <tr> 
                  <td><input name="fechadesc" type="text" id="fechadesc" size="12" maxlength="15" readonly="true"<% if(request.getParameter("fechadesc") != null) { out.print(" value=\"" + request.getParameter("fechadesc") + "\""); } else { out.print(" value=\"\""); } %>> 
              					<a href="javascript:NewCal('fechadesc','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
                  <td align="right">
						<input name="descuento" type="text" id="descuento" size="12" maxlength="10"<% if(request.getParameter("descuento") != null) { out.print(" value=\"" + request.getParameter("descuento") + "\""); } else { out.print(" value=\"0.00\""); } %>>
                  </td>
                  <td align="right"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0">
              		<a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
                </tr>
<%
	}
	
	if(rec.numPartidas() == 0)
	{
		out.println("<tr><td align=\"center\" class=\"titCuerpoBco\" colspan=\"3\">Inserta los descuentos del empleado</td></tr>");
	}
	else
	{						
		for(int i = 0; i < rec.numPartidas(); i++)
		{
%>
                <tr> 
                  <td><%= JUtil.obtFechaTxt(rec.getPartida(i).getFechaDesc() ,"dd/MMM/yyyy") %></td>
                  <td align="right"><%= rec.getPartida(i).getDescuento() %></td>
                  <td align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_FONACOT")) { %><a href="javascript:editarPartida('<%= i %>','<%= JUtil.obtFechaTxt(rec.getPartida(i).getFechaDesc(),"dd/MMM/yyyy") %>','<%= rec.getPartida(i).getDescuento() %>');"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a>
              			<input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0"><% } else { out.print("&nbsp;"); } %></td>
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
document.nom_fonacot_dlg.id_credito.value = '<% if(request.getParameter("id_credito") != null) { out.print( request.getParameter("id_credito") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( set.getAbsRow(0).getID_Credito() ); } else { out.print(""); } %>' 
document.nom_fonacot_dlg.id_empleado.value = '<% if(request.getParameter("id_empleado") != null) { out.print( request.getParameter("id_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( set.getAbsRow(0).getID_Empleado() ); } else { out.print(""); } %>' 
document.nom_fonacot_dlg.nombre_empleado.value = '<% if(request.getParameter("nombre_empleado") != null) { out.print( request.getParameter("nombre_empleado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( set.getAbsRow(0).getNombre() ); } else { out.print(""); } %>' 
document.nom_fonacot_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy") ); } else { out.print("") ; } %>'
document.nom_fonacot_dlg.meses.value = '<% if(request.getParameter("meses") != null) { out.print( request.getParameter("meses") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( set.getAbsRow(0).getMeses() ); } else { out.print(""); } %>' 
document.nom_fonacot_dlg.plazo.value = '<% if(request.getParameter("plazo") != null) { out.print( request.getParameter("plazo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( set.getAbsRow(0).getPlazo() ); } else { out.print(""); } %>' 
document.nom_fonacot_dlg.importe.value = '<% if(request.getParameter("importe") != null) { out.print( request.getParameter("importe") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( set.getAbsRow(0).getImporte() ); } else { out.print(""); } %>' 
document.nom_fonacot_dlg.retencion.value = '<% if(request.getParameter("retencion") != null) { out.print( request.getParameter("retencion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_FONACOT")) { out.print( set.getAbsRow(0).getRetencion() ); } else { out.print(""); } %>' 
</script>
</body>
</html>
