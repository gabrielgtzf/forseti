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
	String prod_formulas_dlg = (String)request.getAttribute("prod_formulas_dlg");
	if(prod_formulas_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo;
	if(request.getParameter("proceso").equals("EDITAR_FORMULA"))
		titulo =  JUtil.getSesion(request).getSesion("PROD_PRODUCCION").generarTitulo(JUtil.Msj("CEF","PROD_PRODUCCION","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	else
		titulo =  JUtil.getSesion(request).getSesion("PROD_FORMULAS").generarTitulo(JUtil.Msj("CEF","PROD_FORMULAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	
	String coletq = JUtil.Msj("CEF","PROD_FORMULAS","DLG","COLUMNAS",1);
	int etq = 1;
	
	session = request.getSession(true);
    JProdFormulasSes rec = (JProdFormulasSes)session.getAttribute("prod_formulas_dlg");
	
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
<%
	for(int i = 0; i < rec.numPartidas(); i++)
	{
%>
function limpiarFormulario<%= i %>()
{
	document.prod_formulas_dlg.cantidad<%= i %>.value = "";
	document.prod_formulas_dlg.mmcantidad<%= i %>.value = "";
	document.prod_formulas_dlg.idprod_part<%= i %>.value = "";
	document.prod_formulas_dlg.idprod_part_nombre<%= i %>.value = "";
	document.prod_formulas_dlg.principal<%= i %>.selectedIndex = 0;
}

function editarPartida<%= i %>(idpartida, cantidad, mmcantidad, idprod, idprod_nombre, principal)
{
	document.prod_formulas_dlg.idpartidaproc.value = <%= i %>;
	document.prod_formulas_dlg.idpartida.value = idpartida;
	document.prod_formulas_dlg.subproceso.value = "EDIT_PART_DET";

	document.prod_formulas_dlg.cantidad<%= i %>.value = cantidad;
	document.prod_formulas_dlg.mmcantidad<%= i %>.value = mmcantidad;
	document.prod_formulas_dlg.idprod_part<%= i %>.value = idprod;
	document.prod_formulas_dlg.idprod_part_nombre<%= i %>.value = idprod_nombre;
	document.prod_formulas_dlg.principal<%= i %>.selectedIndex = principal;
}

<%
	}
%>
function limpiarFormularioProc()
{
	document.prod_formulas_dlg.rendimientosp.value = "";
	document.prod_formulas_dlg.mmrendimientosp.value = "";
	document.prod_formulas_dlg.idsubprod.value = "";
	document.prod_formulas_dlg.idsubprod_nombre.value = "";
	document.prod_formulas_dlg.nombre.value = "";
	document.prod_formulas_dlg.tiempo.value = "0";
	document.prod_formulas_dlg.porcentaje.value = "";
}

function editarPartidaProc(idpartida, nombre, tiempo, rendimientosp, mmrendimientosp, idsubprod, idsubprod_nombre, porcentaje)
{
	document.prod_formulas_dlg.idpartidaproc.value = idpartida;
	document.prod_formulas_dlg.subproceso.value = "EDIT_PART_PROC";

	document.prod_formulas_dlg.nombre.value = nombre;
	document.prod_formulas_dlg.tiempo.value = tiempo;
	document.prod_formulas_dlg.rendimientosp.value = rendimientosp;
	document.prod_formulas_dlg.mmrendimientosp.value = mmrendimientosp;
	document.prod_formulas_dlg.idsubprod.value = idsubprod;
	document.prod_formulas_dlg.idsubprod_nombre.value = idsubprod_nombre;
	document.prod_formulas_dlg.porcentaje.value = porcentaje;
}

function enviarlo(formAct)
{
	if(formAct.subproceso.value == "AGR_PART_DET" || formAct.subproceso.value == "EDIT_PART_DET")
	{
<%
	for(int i = 0; i < rec.numPartidas(); i++)
	{
%>
		if(formAct.idpartidaproc.value == <%= i %>)
		{
			if(!esNumeroDecimal("Cantidad:", formAct.cantidad<%= i %>.value, 0, 9999999999, 6) ||
				!esNumeroDecimal("Mas Menos:", formAct.mmcantidad<%= i %>.value, 0, 9999999999, 6) ||
				!esCadena("Clave:", formAct.idprod_part<%= i %>.value, 1, 20) )
				return false;
			else
				return true;
		}
		
<%
	}
%>
	}
	else if(formAct.subproceso.value == "AGR_PART_PROC" || formAct.subproceso.value == "EDIT_PART_PROC")
	{
		if(!esCadena("Nombre:", formAct.nombre.value, 1, 254) ||
			!esNumeroEntero("Tiempo:", formAct.tiempo.value, 0, 999))
			return false;
		else
			return true;
	}
	else 
	{
		if(formAct.subproceso.value == "ENVIAR")
		{
			if(!esNumeroDecimal("Rendimiento:", formAct.rendimiento.value, 0, 9999999999, 3) ||
				!esNumeroDecimal("Dif en Rendimiento:", formAct.mmrendimiento.value, 0, 9999999999, 3) ||
				!esCadena("Clave:", formAct.idprod.value, 1, 20)  ||
				!esCadena("Formula:", formAct.formula.value, 1, 80) )
				return false;
			else if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
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
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFProdFormulasDlg" method="post" enctype="application/x-www-form-urlencoded" name="prod_formulas_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_FORMULA") || request.getParameter("proceso").equals("EDITAR_FORMULA")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFProdFormulasCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
			  <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td width="20%"> 
				    <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input type="hidden" name="subproceso" value="ENVIAR">
					<input name="idpartidaproc" type="hidden" value="<%= request.getParameter("idpartidaproc")%>"> 
					<input name="idpartida" type="hidden" value="<%= request.getParameter("idpartida")%>"> 
				    <input name="ID" type="hidden" id="ID" value="<%= request.getParameter("ID")%>">
                    Clave: </td>
                  <td colspan="2"><input name="idprod" type="text" id="idprod" size="10" maxlength="20"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=prod_formulas_dlg&lista=idprod&idcatalogo=21&nombre=PRODUCTOS&destino=idprod_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a> 
                    <input name="idprod_nombre" type="text" id="idprod_nombre" size="40" maxlength="120" readonly="true"></td>
                </tr>
                <tr> 
                  <td width="20%">F&oacute;rmula:</td>
                  <td colspan="2"><input name="formula" type="text" id="formula" size="50" maxlength="80"></td>
                </tr>
                <tr> 
                  <td width="20%" valign="top">Rendimiento:</td>
                  <td><input name="rendimiento" type="text" size="12" maxlength="15"> 
                    &nbsp;<%= rec.getUnidad() %> </td>
                  <td> <input name="cantlotes" type="checkbox" id="cantlotes" value="true">
                    Esta es una f&oacute;rmula simple</td>
                </tr>
                <tr> 
                  <td width="20%" valign="top">+ / - Rendimiento::</td>
                  <td colspan="2"> <input name="mmrendimiento" type="text" size="12" maxlength="15"> 
                  </td>
                </tr>
              </table>
                    &nbsp; </td>
                </tr>
              </table>
		</td>
     </tr>
     <tr>
      <td>
			
		<table width="100%" border="0" cellspacing="0">
          <tr bgcolor="#0099FF">
			<td width="25%" class="titChico">Nombre</td>
           	<td  width="5%" align="right" class="titChico">Tiempo</td>
			<td  width="5%" align="right" class="titChico">Cant</td>
            <td  width="3%" align="center" class="titChico">Uni</td>
			<td  width="5%" align="right" class="titChico">Dif</td>
            <td width="10%" class="titChico" >Clave</td>
            <td class="titChico">Descripci&oacute;n</td>
			<td  width="5%" align="right" class="titChico">% Tot</td>
            <td  width="5%" align="right" class="titChico">CPT</td>
            <td  width="5%" align="right" class="titChico">UCT</td>
            <td  width="7%">&nbsp;</td>
          </tr>
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_FORMULA") )
	{
%>				
          <tr>
            <td width="25%"><input name="nombre" type="text" class="cpoBco" id="nombre" size="25" maxlength="255"></td>
			<td width="5%" align="right"><input name="tiempo" type="text" class="cpoBco" id="tiempo" size="5" maxlength="5"></td>
            <td width="5%" align="right"> 
                    <input name="rendimientosp" type="text" class="cpoBco" id="rendimientosp" size="7" maxlength="12"></td>
            <td width="3%" align="center">&nbsp;</td>
			<td width="5%" align="right"> 
                    <input name="mmrendimientosp" type="text" class="cpoBco" id="mmrendimientosp" size="7" maxlength="12"></td>
            <td width="10%">
					<input name="idsubprod" type="text" class="cpoBco" id="idsubprod" size="8" maxlength="20">
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=prod_formulas_dlg&lista=idsubprod&idcatalogo=21&nombre=PRODUCTOS&destino=idsubprod_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
            <td><input name="idsubprod_nombre" type="text" class="cpoBco" id="idsubprod_nombre" size="40" maxlength="120" readonly="true"></td>
            <td width="5%" align="right"> 
                    <input name="porcentaje" type="text" class="cpoBco" id="porcentaje" size="5" maxlength="9"></td>
            <td width="5%" align="right">&nbsp;</td>
			<td width="5%" align="right">&nbsp;</td>
			<td width="7%"><input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART_PROC') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART_PROC'); }" src="../../imgfsi/lista_ok.gif" border="0"> 
                    <a href="javascript:limpiarFormularioProc();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
         </tr>
<%
	}

	if(rec.numPartidas() == 0)
	{
		out.println("<tr><td align=\"center\" class=\"titCuerpoAzc\" colspan=\"11\">Inserta aqui los procesos de la formula</td></tr>");
	}
	else
	{						
		for(int p = 0; p < rec.numPartidas(); p++)
		{
			// aqui van los detalles del proceso
%>
		 <tr>
			<td width="25%" class="titChicoAzc"><%= rec.getPartida(p).getNombre() %></td>
           	<td  width="5%" align="right" class="titChicoAzc"><%= rec.getPartida(p).getTiempo() %></td>
			<td  width="5%" align="right" class="titChicoAzc"><%= rec.getPartida(p).getCantidad() %></td>
            <td  width="3%" align="center" class="titChicoAzc"><%= rec.getPartida(p).getUnidad() %></td>
			<td  width="5%" align="right" class="titChicoAzc"><%= rec.getPartida(p).getMasMenos() %></td>
            <td width="10%" class="titChicoAzc" ><%= rec.getPartida(p).getID_Prod() %></td>
            <td class="titChicoAzc"><%= rec.getPartida(p).getDescripcion() %></td>
			<td  width="5%" align="right" class="titChicoAzc"><%= rec.getPartida(p).getPorcentaje() %></td>
            <td  width="5%" align="right" class="titChicoAzc"><%= rec.getPartida(p).getCPT() %></td>
            <td  width="5%" align="right" class="titChicoAzc"><%= rec.getPartida(p).getUCT() %></td>
            <td width="7%">
               	<% if(!request.getParameter("proceso").equals("CONSULTAR_FORMULA")) { %>
		            <a href="javascript:editarPartidaProc('<%= p %>','<%= rec.getPartida(p).getNombre() %>','<%= rec.getPartida(p).getTiempo() %>','<%= rec.getPartida(p).getCantidad() %>','<%= rec.getPartida(p).getMasMenos() %>','<%= rec.getPartida(p).getID_Prod() %>','<%= rec.getPartida(p).getDescripcion() %>','<%= rec.getPartida(p).getPorcentaje() %>');"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a> 
                    <input name="submit" type="image" onClick="javascript:this.form.idpartidaproc.value = '<%= p %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART_PROC');" src="../../imgfsi/lista_x.gif" border="0"><% } else { out.print("&nbsp;"); } %></td>
        </tr>
	    <tr>
		 <td colspan="11">
		
		

	
	
	
			
			 <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr>
                  <td width="5%" align="right" class="titChicoNeg">CXU</td>
                  <td  width="5%" align="right" class="titChicoNeg">Cant</td>
                  <td  width="3%" align="center" class="titChicoNeg">Uni</td>
				  <td  width="5%" align="right" class="titChicoNeg">Dif</td>
                  <td width="10%" class="titChicoNeg" >Clave</td>
                  <td class="titChicoNeg">Descripci&oacute;n</td>
				   <td  width="5%" align="center" class="titChicoNeg">PP</td>
                   <td  width="5%" align="right" class="titChicoNeg">CP</td>
                  <td  width="5%" align="right" class="titChicoNeg">UC</td>
                 <td  width="5%" align="right" class="titChicoNeg">CPT</td>
                 <td  width="5%" align="right" class="titChicoNeg">UCT</td>
                 <td  width="7%">&nbsp;</td>
                </tr>
<%
			if( !request.getParameter("proceso").equals("CONSULTAR_FORMULA") )
			{
%>				
                 <tr>
                  <td width="7%" align="right">&nbsp;</td>
                  <td width="5%" align="right"> 
                    <input name="cantidad<%= p %>" type="text" class="cpoBco" id="cantidad<%= p %>" size="7" maxlength="12"></td>
                  <td width="3%" align="center">&nbsp;</td>
				  <td width="5%" align="right"> 
                    <input name="mmcantidad<%= p %>" type="text" class="cpoBco" id="mmcantidad<%= p %>" size="7" maxlength="12"></td>
                  <td width="10%">
						<input name="idprod_part<%= p %>" type="text" class="cpoBco" id="idprod_part<%= p %>" size="8" maxlength="20">
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=prod_formulas_dlg&lista=idprod_part<%= p %>&idcatalogo=19&nombre=PRODUCTOS&destino=idprod_part_nombre<%= p %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" border="0"></a></td>
                  <td><input name="idprod_part_nombre<%= p %>" type="text" class="cpoBco" id="idprod_part_nombre<%= p %>" size="40" maxlength="120" readonly="true"></td>
                  <td width="5%" align="center">
				  	<select name="principal<%= p %>" class="cpoBco">
					<option value="1"<% 
									if(request.getParameter("principal" + p) != null && 
										request.getParameter("principal" + p).equals("1"))  {
											out.print(" selected");
									} %>>V</option>
					<option value="0"<% 
									if(request.getParameter("principal" + p) != null && 
										request.getParameter("principal" + p).equals("0"))  {
											out.print(" selected");
									} %>>F</option>
					</select></td>
				  <td width="5%" align="right">&nbsp;</td>
				  <td width="5%" align="right">&nbsp;</td>
				  <td width="5%" align="right">&nbsp;</td>
				  <td width="5%" align="right">&nbsp;</td>
				  <td width="7%"><input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART_DET') { establecerDobleProcesoSVE(this.form.subproceso, 'AGR_PART_DET', this.form.idpartidaproc, '<%= p %>'); }" src="../../imgfsi/lista_ok.gif" border="0"> 
                    <a href="javascript:limpiarFormulario<%= p %>();"><img src="../../imgfsi/lista_x.gif" alt="" title="este texto alt" width="16" height="16" border="0"></a></td>
                </tr>
<%
			}
	
			if(rec.getPartida(p).numPartidas() == 0)
			{
				out.println("<tr><td align=\"center\" class=\"titCuerpoAzc\" colspan=\"12\">Inserta aqui los materiales del proceso</td></tr>");
			}
			else
			{						
				for(int i = 0; i < rec.getPartida(p).numPartidas(); i++)
				{
%>
                <tr> 
                  <td width="5%" align="right"><%= rec.getPartida(p).getPartida(i).getCantidadXU() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(p).getPartida(i).getCantidad() %></td>
                  <td width="3%" align="center"><%= rec.getPartida(p).getPartida(i).getUnidad() %></td>
				  <td width="5%" align="right"><%= rec.getPartida(p).getPartida(i).getMasMenos() %></td>
                  <td width="10%"><%= rec.getPartida(p).getPartida(i).getID_Prod() %></td>
                  <td><%= rec.getPartida(p).getPartida(i).getDescripcion() %></td>
                  <td width="5%" align="center"><%= (rec.getPartida(p).getPartida(i).getPrincipal() ? "V" :  "F") %></td>
                  <td width="5%" align="right"><%= rec.getPartida(p).getPartida(i).getCP() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(p).getPartida(i).getUC() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(p).getPartida(i).getCPT() %></td>
                  <td width="5%" align="right"><%= rec.getPartida(p).getPartida(i).getUCT() %></td>
                  <td width="7%">
                    <% if(!request.getParameter("proceso").equals("CONSULTAR_FORMULA")) { %>
                    <a href="javascript:editarPartida<%= p %>('<%= i %>','<%= rec.getPartida(p).getPartida(i).getCantidad() %>','<%= rec.getPartida(p).getPartida(i).getMasMenos() %>','<%= rec.getPartida(p).getPartida(i).getID_Prod() %>','<%= rec.getPartida(p).getPartida(i).getDescripcion() %>','<%= (rec.getPartida(p).getPartida(i).getPrincipal() ? "0" :  "1") %>');"><img src="../../imgfsi/lista_ed.gif" alt="" title="este texto alt ed" width="16" height="16" border="0"></a> 
                    <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerDobleProcesoSVE(this.form.subproceso, 'BORR_PART_DET', this.form.idpartidaproc, '<%= p %>');" src="../../imgfsi/lista_x.gif" border="0"><% } else { out.print("&nbsp;"); } %></td>
                </tr>
<%
				}
			}
%>
              </table> 
			
			
			
			
			
		 </td>
		</tr>
		
<%
		}
	}
%>	
		  <tr> 
        	<td width="5%">&nbsp;</td>
            <td width="5%">&nbsp;</td>
            <td width="3%">&nbsp;</td>
			<td width="5%">&nbsp;</td>
            <td width="10%">&nbsp;</td>
            <td>Totales por unidad:</td>
            <td width="5%">&nbsp;</td>
            <td width="5%">&nbsp;</td>
            <td width="5%" align="right" class="titChicoAzc"><%= rec.getCPT() %></td>
            <td width="5%" align="right" class="titChicoAzc"><%= rec.getUCT() %></td>
            <td width="7%">&nbsp;</td>
          </tr>
		 <tr> 
            <td width="5%">&nbsp;</td>
            <td width="5%">&nbsp;</td>
            <td width="3%">&nbsp;</td>
		    <td width="5%">&nbsp;</td>
            <td width="10%">&nbsp;</td>
            <td>Merma:</td>
            <td width="5%">&nbsp;</td>
            <td width="5%">&nbsp;</td>
            <td width="5%">&nbsp;</td>
            <td width="5%" align="right" class="txtChicoAzc"><%= rec.getMerma() %></td>
            <td width="7%">&nbsp;</td>
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
document.prod_formulas_dlg.idprod.value = '<%= rec.getID_Prod() %>'
document.prod_formulas_dlg.rendimiento.value = '<%=  rec.getRendimiento() %>'
document.prod_formulas_dlg.mmrendimiento.value = '<%=  rec.getMasMenos() %>'
document.prod_formulas_dlg.formula.value = '<%=  rec.getFormula() %>'
document.prod_formulas_dlg.cantlotes.checked = <%=  ( ( rec.getCantLotes() ) ? "true" : "false" ) %>
document.prod_formulas_dlg.idprod_nombre.value = '<%= rec.getDescripcion() %>'
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_FORMULA") )
	{
%>	
document.prod_formulas_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else { out.print(""); } %>'
document.prod_formulas_dlg.tiempo.value = '<% if(request.getParameter("tiempo") != null) { out.print( request.getParameter("tiempo") ); } else { out.print("0"); } %>'
document.prod_formulas_dlg.rendimientosp.value = '<% if(request.getParameter("rendimientosp") != null) { out.print( request.getParameter("rendimientosp") ); } else { out.print(""); } %>'
document.prod_formulas_dlg.mmrendimientosp.value = '<% if(request.getParameter("mmrendimientosp") != null) { out.print( request.getParameter("mmrendimientosp") ); } else { out.print(""); } %>'
document.prod_formulas_dlg.idsubprod.value = '<% if(request.getParameter("idsubprod") != null) { out.print( request.getParameter("idsubprod") ); } else { out.print(""); } %>'
document.prod_formulas_dlg.idsubprod_nombre.value = '<% if(request.getParameter("idsubprod_nombre") != null) { out.print( request.getParameter("idsubprod_nombre") ); } else { out.print(""); } %>'
document.prod_formulas_dlg.porcentaje.value = '<% if(request.getParameter("porcentaje") != null) { out.print( request.getParameter("porcentaje") ); } else { out.print(""); } %>'
<%
		for(int i = 0; i < rec.numPartidas(); i++)
		{
%>
document.prod_formulas_dlg.cantidad<%= i %>.value = '<% if(request.getParameter("cantidad" + i) != null) { out.print( request.getParameter("cantidad" + i ) ); } else { out.print("1"); } %>'
document.prod_formulas_dlg.mmcantidad<%= i %>.value = '<% if(request.getParameter("mmcantidad" + i ) != null) { out.print( request.getParameter("mmcantidad" + i ) ); } else { out.print("0"); } %>'
document.prod_formulas_dlg.idprod_part<%= i %>.value = '<% if(request.getParameter("idprod_part" + i ) != null) { out.print( request.getParameter("idprod_part" + i ) ); } else { out.print(""); } %>'
document.prod_formulas_dlg.idprod_part_nombre<%= i %>.value = '<% if(request.getParameter("idprod_part_nombre" + i ) != null) { out.print( request.getParameter("idprod_part_nombre" + i ) ); } else { out.print(""); } %>'
<%
		}
	}
%>	
</script>
</body>
</html>
