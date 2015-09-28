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
<%@ page import="forseti.*, forseti.sets.*, forseti.produccion.*, java.util.*, java.io.*"%>
<%
	String prod_produccion_dlg = (String)request.getAttribute("prod_produccion_dlg");
	if(prod_produccion_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("PROD_PRODUCCION").generarTitulo(JUtil.Msj("CEF","PROD_PRODUCCION","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	String coletq = JUtil.Msj("CEF","PROD_FORMULAS","DLG","COLUMNAS",1);
	int etq = 1;
	
	session = request.getSession(true);
    JProdProduccionSes rec = (JProdProduccionSes)session.getAttribute("prod_produccion_dlg");

	JProdFormulasCatalogSetV2 setFor = new JProdFormulasCatalogSetV2(request);
	setFor.m_OrderBy = "Principal DESC, ID_Formula ASC";
	setFor.m_Where = "ID_Prod = '" + JUtil.p(rec.getID_Prod_Part()) + "' and Status = 'V'";
	setFor.Open();
	
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
formulas = new Array(<% 		
	for(int i = 0; i< setFor.getNumRows(); i++)
	{
		out.print(setFor.getAbsRow(i).getCantidad() + ",");
	}
	%>1.000);
	
instruccion = new Array(<% 		
	for(int i = 0; i< setFor.getNumRows(); i++)
	{
		out.print(  
		((setFor.getAbsRow(i).getUnidadUnica() == true) ? "'Produccion inmediata: Captura la cantidad de " + setFor.getAbsRow(i).getUnidad() + " a fabricar" :
				"'Produccin por lote: La cantidad debe ser igual o similar a " + setFor.getAbsRow(i).getCantidad() + " " + setFor.getAbsRow(i).getUnidad() ) + "',");
	}
	%>'');
	
function establecerCantidad(selFor, cantidad, instrucciones)
{
	cantidad.value = formulas[selFor.selectedIndex];
	instrucciones.value = instruccion[selFor.selectedIndex];
}

function limpiarFormulario()
{
	document.prod_produccion_dlg.cantidad.value = "";
	document.prod_produccion_dlg.idprod_part.value = "";
	document.prod_produccion_dlg.idprod_part_nombre.value = "";
	document.prod_produccion_dlg.formula.value = "";
	document.prod_produccion_dlg.lote.value = "";
}

function ventanaDos(ancho, alto)
{
	parametrs = "toolbar=0,location=0,directories=0,status=1,menubar=0,scrollbars=1,resizable=1,width=" + ancho + ",height=" + alto;
	ventana = window.open('', 'ventEm2', parametrs);
	ventana.focus();
}

function enviarlo(formAct)
{
	if(formAct.proceso.value == "AGREGAR_PRODUCCION")
	{
		if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
		{
			if(!esNumeroDecimal("Cantidad:", formAct.cantidad.value, 0, 9999999999, 3) ||
				!esCadena("Clave:", formAct.idprod_part.value, 1, 20) )
				return false;
			
		}
		
		if(formAct.subproceso.value == "ENVIAR")
		{
			if(!esNumeroEntero("Num. Reporte:", formAct.reporte.value, 1, 9999999999) ||
				!esCadena("Referencia:", formAct.concepto.value, 1, 255) )
				return false;
		
			
		}
		
		return true;
		
	}
	else
	{	
		return false;
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFProdProduccionDlg" method="post" enctype="application/x-www-form-urlencoded" name="prod_produccion_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_PRODUCCION")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFProdProduccionCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	   <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td>
			 <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="15%"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
                    <input name="idprod_partses" type="hidden" id="idprod_partses" value="<%= rec.getID_Prod_Part() %>"> 
                    <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>">
                    Num. Reporte:</td>
                  <td width="35%"><input name="reporte" type="text" id="reporte" size="10" maxlength="15"></td>
                  <td width="15%">Fecha:</td>
                  <td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
                    <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
                 
                </tr>
                <tr> 
                  <td>Bodega MP:</td>
                  <td class="titChicoAzc"><%= rec.getBodegaMP() %></td>
                  <td>Bodega PT:</td>
                  <td class="titChicoAzc"><%= rec.getBodegaPT() %></td>
                </tr>
				<tr> 
                 <td>Concepto:</td>
                 <td colspan="2"><input name="concepto" type="text" id="concepto" style="width:95%" maxlength="255"></td>
				 <td><input type="checkbox" name="directa" value="directa">&nbsp;
                    Este es un reporte directo directo</td>
                </tr>
              </table>
			 </td>
          </tr>
          <tr>
            <td>
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr bgcolor="#0099FF">
				  <td  width="3%">&nbsp;</td>
                  <td width="12%" class="titChico">Clave</td>
                  <td  width="28%"  class="titChico">Descripci&oacute;n</td>
                  <td  width="25%"  class="titChico">F&oacute;rmula</td>
                  <td width="9%" align="right" class="titChico" >Cant</td>
                  <td width="5%" align="center" class="titChico">Uni</td>
				  <td width="13" class="titChico">Lote</td>
                  <td width="5%">&nbsp;</td>
                </tr>
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_PRODUCCION") )
	{
%>				
                 <tr>
				  <td>&nbsp;</td>
                  <td><input name="idprod_part" type="text" class="cpoBco" id="idprod_part" onBlur="javascript: if(this.form.idprod_part.value != this.form.idprod_partses.value) { establecerProcesoSVE(this.form.subproceso, 'AGR_FORM'); this.form.submit(); }" size="10" maxlength="20">
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=prod_produccion_dlg&lista=idprod_part&idcatalogo=22&nombre=FORMULAS&destino=idprod_part_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
                  <td> <input name="idprod_part_nombre" type="text" class="cpoBco" id="idprod_part_nombre" size="40" maxlength="120" readonly="true"></td>
                  <td>
				  	<select name="formula" class="cpoBco" style="width: 90%;" onChange="javascript:establecerCantidad(this.form.formula, this.form.cantidad, this.form.instrucciones);">
<%		for(int i = 0; i< setFor.getNumRows(); i++)
		{	
%>
                          <option value="<%=setFor.getAbsRow(i).getID_Formula()%>"<% 
									if(request.getParameter("formula") != null && 
										request.getParameter("formula").equals(Long.toString(setFor.getAbsRow(i).getID_Formula())))  {
											out.print(" selected");
									} %>><%= setFor.getAbsRow(i).getFormula() %></option>
<%	
		}
%>
                        </select>
				  	</td>
                  <td align="right"><input name="cantidad" type="text" class="cpoBco" id="cantidad" size="7" maxlength="12"></td>
                  <td>&nbsp;</td>
				  <td><input name="lote" type="text" class="cpoBco" id="lote" size="10" maxlength="25"></td>
                  <td><input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0"> 
                    <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
                </tr>
				<tr>
				  <td colspan="8"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr> 
                        <td width="15%" class="titChicoNeg">Observaciones:</td>
                        <td width="38%" ><input name="obs_partida" type="text" class="cpoBco" id="obs_partida" maxlength="80" style="width: 90%;">
                          </td>
						 <td>
                          <input name="instrucciones" type="text" class="cpoBco" id="instrucciones" readonly="true" style="width: 100%;"></td> 
                      </tr>
                    </table>
				  </td>
                </tr>
<%
	}
	
	if(rec.numPartidas() == 0)
	{
		out.println("<tr><td align=\"center\" class=\"titCuerpoAzc\" colspan=\"8\">Inserta aqui las formulas a procesar para este reporte</td></tr>");
	}
	else
	{						
		for(int i = 0; i < rec.numPartidas(); i++)
		{
			
%>
                <tr> 
				  <td class="titChicoAzc">&nbsp;</td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getID_Prod() %></td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getDescripcion() %></td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getFormula() %></td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getCantidad() %></td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getUnidad() %></td>
				  <td class="titChicoAzc"><%= rec.getPartida(i).getLote() %></td>
                  <td class="titChicoAzc">
                    <% if(!request.getParameter("proceso").equals("CONSULTAR_PRODUCCION")) { %>
                    <a onClick="javascript:ventanaDos(900,400);" href="../../servlet/CEFProdFormulasDlg?proceso=EDITAR_FORMULA&idpartida=<%= i %>" target="ventEm2"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a>
                    <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_x.gif" border="0"><% } else { out.print("&nbsp;"); } %></td>
                </tr>
				<tr> 
					<td class="titChicoAzc" colspan="8">Observaciones 
                    de la Formula: <%= rec.getPartida(i).getObs() %></td>
                </tr>
				<tr> 
					<td colspan="8">
<% 
			for(int j = 0; j < rec.getPartida(i).getPartidaFormula().numPartidas(); j++)
			{
%>					
				  		<table width="100%" border="0" cellspacing="0" cellpadding="2">
						  <tr>
						    <td width="25%" class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getNombre() %></td>
           					<td  width="5%" align="right" class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getTiempo() %></td>
							<td  width="5%" align="right" class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad() %></td>
            				<td  width="3%" align="center" class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getUnidad() %></td>
							<td  width="5%" align="right" class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getMasMenos() %></td>
            				<td width="10%" class="titChicoNeg" ><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod() %></td>
            				<td class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getDescripcion() %></td>
						  </tr>
						  <tr>
						    <td colspan="7">
<%
				for(int d = 0; d < rec.getPartida(i).getPartidaFormula().getPartida(j).numPartidas(); d++)
				{
%>
							  <table width="100%" border="0" cellspacing="0" cellpadding="2">
								<tr> 
								  <td width="5%" align="right"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidadXU() %></td>
								  <td width="5%" align="right"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidad() %></td>
								  <td width="3%" align="center"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getUnidad() %></td>
								  <td width="10%"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getID_Prod() %></td>
								  <td><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getDescripcion() %></td>
								 </tr>
							  </table>
<%
				}
%>
							</td>
						  </tr>
						</table>
<%
			 }
 		}
	}
%>
					</td>
                </tr>
              </table> 
			 </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
       </table>
	 </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.prod_produccion_dlg.reporte.value = '<%= rec.getReporteNum() %>'
document.prod_produccion_dlg.fecha.value = '<%= JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy") %>'
document.prod_produccion_dlg.concepto.value = '<%= rec.getConcepto() %>'
document.prod_produccion_dlg.directa.checked = <% out.print( rec.getDirecta() ? "true" : "false" ); %>  
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_PRODUCCION") )
	{
%>	
document.prod_produccion_dlg.cantidad.value = '<%= rec.getCantidad_Part()%>'
document.prod_produccion_dlg.idprod_part.value = '<%= rec.getID_Prod_Part() %>'
document.prod_produccion_dlg.idprod_part_nombre.value = '<%= rec.getDescripcion_Part() %>'
document.prod_produccion_dlg.lote.value = '<%= rec.getLote_Part() %>'
document.prod_produccion_dlg.obs_partida.value = '<%= rec.getObs_Part() %>'
document.prod_produccion_dlg.instrucciones.value = '<%= rec.getInstrucciones_Part() %>'
<%
	}
%>	
</script>
</body>
</html>
