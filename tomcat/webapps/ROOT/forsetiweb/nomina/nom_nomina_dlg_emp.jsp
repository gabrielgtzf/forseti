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
<%@ page import="forseti.*, forseti.nomina.*, forseti.sets.*, java.util.*, java.io.*" %>
<%
	String nom_nomina_dlg = (String)request.getAttribute("nom_nomina_dlg");
	if(nom_nomina_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	//Integer recibo = (Integer)request.getAttribute("recibo");
	
	String titulo =  JUtil.getSesion(request).getSesion("NOM_NOMINA").generarTitulo(JUtil.Msj("CEF","NOM_NOMINA","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");

	session = request.getSession(true);
    JNomMovDirSes rec = (JNomMovDirSes)session.getAttribute("nom_nomina_dlg");
	
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
function limpiarFormulario()
{
	document.nom_nomina_dlg_emp.idmovimiento.value = "";
	document.nom_nomina_dlg_emp.descripcion.value = "";
	document.nom_nomina_dlg_emp.gravado.value = "0.0";
	document.nom_nomina_dlg_emp.exento.value = "0.0";
	document.nom_nomina_dlg_emp.deduccion.value = "0.0";
}

function editarPartida(idpartida, idmovimiento, descripcion, gravado, exento, deduccion)
{
	document.nom_nomina_dlg_emp.idpartida.value = idpartida;
	document.nom_nomina_dlg_emp.subproceso.value = "EDIT_PART";

	document.nom_nomina_dlg_emp.idmovimiento.value = idmovimiento;
	document.nom_nomina_dlg_emp.descripcion.value = descripcion;
	document.nom_nomina_dlg_emp.gravado.value = gravado;
	document.nom_nomina_dlg_emp.exento.value = exento;
	document.nom_nomina_dlg_emp.deduccion.value = deduccion;
}

function enviarlo(formAct)
{
	if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
	{
		if(!esNumeroDecimal("Gravado:", formAct.gravado.value, -9999999999, 9999999999, 2) ||
			!esNumeroDecimal("Exento:", formAct.exento.value, -9999999999, 9999999999, 2) ||
			!esNumeroDecimal("Deduccin:", formAct.deduccion.value, -9999999999, 9999999999, 2) ||
		   	!esNumeroEntero("Clave:", formAct.idmovimiento.value, 0, 999))
			return false;
		else
			return true;
	}
	else if(formAct.subproceso.value == "BORR_PART")
	{
		return true;
	}
	else
	{
		if(!esCadena('Empleado:', formAct.id_empleado.value, 6, 6)  ||
			!esNumeroEntero('Recibo:', formAct.recibo.value, 1, 32000)  ||
			!esNumeroDecimal('Faltas:', formAct.faltas.value, 0, 99, 2) ||
			!esNumeroDecimal('No. Horas Extras Dobles:', formAct.he.value, 0, 999, 6) ||
			!esNumeroDecimal('No. Horas Domingo:', formAct.hd.value, 0, 999, 6) ||
			!esNumeroDecimal('No. Horas Extras Triples:', formAct.ht.value, 0, 999, 6) ||
			!esNumeroDecimal('Dias de Incapacidad por Accidente:', formAct.ixa.value, 0, 999, 2) ||
			!esNumeroDecimal('Dias de Incapacidad por Enfermedad:', formAct.ixe.value, 0, 999, 2) ||
			!esNumeroDecimal('Dias de Incapacidad por Maternidad:', formAct.ixm.value, 0, 999, 2) ||
			!esNumeroEntero('Dias de Horas Extras:', formAct.dhe.value, 0, 99) )
			return false;
		else
			return true;
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomMovDirDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_nomina_dlg_emp" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomMovDirCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td> <div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
				<input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                <input name="idempleado" type="hidden" value="<%= request.getParameter("idempleado")%>">
				<input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
                Empleado:</div></td>
            <td colspan="3">
			 <input name="id_empleado" type="text" id="id_empleado" size="10" maxlength="6"<%= (request.getParameter("proceso").equals("CAMBIAR") || request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? " readonly=\"true\"" : "" %>> 
              <% if(request.getParameter("proceso").equals("AGR_EMP")) { %> <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_nomina_dlg_emp&lista=id_empleado&idcatalogo=28&nombre=EMPLEADOS&destino=nombre_empleado&esp1=NOM_NOMINA',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a> 
              <% } %> <input name="nombre_empleado" type="text" id="nombre_empleado" size="40" maxlength="250" readonly="true">
			</td>
          </tr>
          <tr> 
            <td width="20%"> <div align="right">N&uacute;m Recibo:</div></td>
            <td width="30%"><input name="recibo" type="text" id="recibo" size="8" maxlength="6"<%= (request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? "" : " readonly=\"true\"" %>> 
            </td>
            <td width="20%"><div align="right">Faltas:</div></td>
            <td width="30%"><input name="faltas" type="text" id="faltas" size="8" maxlength="5"></td>
          </tr>
          <tr> 
            <td> <div align="right">No. Horas Extras Dobles:</div></td>
            <td><input name="he" type="text" id="he" size="11" maxlength="10"<%= (request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? " readonly=\"true\"" : "" %>></td>
            <td><div align="right">No. Horas Domingo:</div></td>
            <td><input name="hd" type="text" id="hd" size="11" maxlength="10"></td>
          </tr>
		  <tr> 
            <td> <div align="right">No. Horas Extras Triples:</div></td>
            <td><input name="ht" type="text" id="ht" size="11" maxlength="10"<%= (request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? " readonly=\"true\"" : "" %>></td>
            <td><div align="right">Dias de Horas Extras:</div></td>
            <td><input name="dhe" type="text" id="dhe" size="11" maxlength="10"<%= (request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? " readonly=\"true\"" : "" %>></td>
          </tr>
		   <tr> 
            <td> <div align="right">Dias de Incapacidad por Accidente:</div></td>
            <td><input name="ixa" type="text" id="ixa" size="11" maxlength="10"<%= (request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? " readonly=\"true\"" : "" %>></td>
            <td><div align="right">Dias de Incapacidad por Enfermedad:</div></td>
            <td><input name="ixe" type="text" id="ixe" size="11" maxlength="10"<%= (request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? " readonly=\"true\"" : "" %>></td>
          </tr>
		  <tr> 
            <td> <div align="right">Dias Incapacidad por Maternidad:</div></td>
            <td><input name="ixm" type="text" id="ixm" size="11" maxlength="10"<%= (request.getParameter("proceso").equals("ENLAZAR_RECIBO")) ? " readonly=\"true\"" : "" %>></td>
            <td colspan="2">&nbsp;</td>
          </tr>
          <tr> 
            <td colspan="4"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr bgcolor="#0099FF"> 
                  <td width="10%" class="titChico">Clave</td>
                  <td class="titChico">Descripci&oacute;n</td>
                  <td width="12%" align="right" class="titChico">Gravado</td>
                  <td width="12%" align="right" class="titChico">Exento</td>
                  <td width="12%" align="right" class="titChico">Deducci&oacute;n</td>
                  <td width="8%">&nbsp;</td>
                </tr>
<%
		if( !request.getParameter("proceso").equals("ENLAZAR_RECIBO") )
		{
%>				
                <tr> 
                  <td> <input name="idmovimiento" type="text" id="idmovimiento" class="cpoBco" size="6" maxlength="3"<% if(request.getParameter("idmovimiento") != null) { out.println(" value=\"" + request.getParameter("idmovimiento") + "\""); } %>><a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_nomina_dlg_emp&lista=idmovimiento&idcatalogo=29&nombre=MOVIMIENTOS&destino=descripcion',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
                  <td><input name="descripcion" type="text" id="descripcion" size="40" maxlength="250" readonly="true"<% if(request.getParameter("descripcion") != null) { out.println(" value=\"" + request.getParameter("descripcion") + "\""); } %>></td>
                  <td align="right"> <input name="gravado" type="text" id="gravado" size="12" maxlength="15" <% if(request.getParameter("gravado") != null) { out.print(" value=\"" + request.getParameter("gravado") + "\""); } else { out.print(" value=\"0\""); } %>> 
                  </td>
                  <td align="right"> <input name="exento" type="text" id="exento" size="12" maxlength="15" <% if(request.getParameter("exento") != null) { out.print(" value=\"" + request.getParameter("exento") + "\""); } else { out.print(" value=\"0\""); } %>> 
                  </td>
                  <td align="right"> <input name="deduccion" type="text" id="deduccion" size="12" maxlength="15" <% if(request.getParameter("deduccion") != null) { out.print(" value=\"" + request.getParameter("deduccion") + "\""); } else { out.print(" value=\"0\""); } %>> 
                  </td>
                  <td width="5%" align="right" valign="top"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0"> 
                    <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
                </tr>
<%
		}
		
		if(rec.numPartidas() == 0)
		{
			out.println("<tr><td align=\"center\" class=\"titCuerpoBco\" colspan=\"6\">Inserta aqu los movimientos del empleado</td></tr>");
		}
		else
		{
			float totgravado = 0.0f, totexento = 0.0f, totdeduccion = 0.0f;						
			for(int i = 0; i < rec.numPartidas(); i++)
			{
%>				
                <tr> 
<%
				if(request.getParameter("proceso").equals("ENLAZAR_RECIBO"))
				{	 
%>
				  <td align="left">  
				    <input name="idmovimiento_<%= i %>" type="text" class="cpoBco" id="movimiento_<%= i %>" size="6" maxlength="3" value="<%= rec.getPartida(i).getID_Movimiento() %>"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=nom_nomina_dlg_emp&lista=idmovimiento_<%= i %>&idcatalogo=29&nombre=MOVIMIENTOS&destino=descripcion_<%= i %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a></td>
				  <td><input name="descripcion_<%= i %>" type="text" class="cpoBco" id="descripcion_<%= i %>" size="40" maxlength="120" value="<%= rec.getPartida(i).getDescripcion() %>" readonly="true"></td>	
<%
				}
				else
				{
%>
                  	<td align="left"><%= rec.getPartida(i).getID_Movimiento() %></td>
            		<td align="left"><%= rec.getPartida(i).getDescripcion() %></td>
<%
				}
%>
            		<td align="right"><%= rec.getPartida(i).getGravado() %></td>
            		<td align="right"><%= rec.getPartida(i).getExento() %></td>
            		<td align="right"><%= rec.getPartida(i).getDeduccion() %></td>
            		<td align="right">
<%
				if(!request.getParameter("proceso").equals("ENLAZAR_RECIBO"))
				{
%>						
						<a href="javascript:editarPartida('<%= i %>','<%= rec.getPartida(i).getID_Movimiento() %>','<%= rec.getPartida(i).getDescripcion() %>','<%= rec.getPartida(i).getGravado() %>','<%= rec.getPartida(i).getExento() %>','<%= rec.getPartida(i).getDeduccion() %>');"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a>
              			<input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0">
<%
				}
				else
					out.print("&nbsp;");
%>							   	
					</td>
          	    </tr>
<%
				totgravado += rec.getPartida(i).getGravado();
				totexento += rec.getPartida(i).getExento();
				totdeduccion += rec.getPartida(i).getDeduccion();  
			}
%>
				<tr> 
                  	<td align="left" class="titChicoNeg">Neto:</td>
            		<td align="left" class="titChicoNeg"><%=  JUtil.Converts((totgravado + totexento + totdeduccion),",",".",2,true) %></td>
            		<td align="right" class="titChicoNeg"><%=  JUtil.Converts(totgravado,",",".",2,true) %></td>
            		<td align="right" class="titChicoNeg"><%=  JUtil.Converts(totexento,",",".",2,true) %></td>
            		<td align="right" class="titChicoNeg"><%=  JUtil.Converts(totdeduccion,",",".",2,true) %></td>
            		<td align="right" class="titChicoNeg">&nbsp;</td>
          	    </tr>
<%
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
document.nom_nomina_dlg_emp.id_empleado.value = '<%= rec.getID_Empleado() %>' 
document.nom_nomina_dlg_emp.nombre_empleado.value = '<%= rec.getNombre() %>'
document.nom_nomina_dlg_emp.faltas.value = '<%= rec.getFaltas() %>'
document.nom_nomina_dlg_emp.he.value = '<%= rec.getHE() %>'
document.nom_nomina_dlg_emp.hd.value = '<%= rec.getHD() %>'
document.nom_nomina_dlg_emp.ht.value = '<%= rec.getHT() %>'
document.nom_nomina_dlg_emp.ixa.value = '<%= rec.getIXA() %>'
document.nom_nomina_dlg_emp.ixe.value = '<%= rec.getIXE() %>'
document.nom_nomina_dlg_emp.ixm.value = '<%= rec.getIXM() %>'
document.nom_nomina_dlg_emp.recibo.value = '<%= rec.getRecibo() %>'
document.nom_nomina_dlg_emp.dhe.value = '<%= rec.getDiasHorasExtras() %>'
</script>
</body>
</html>
