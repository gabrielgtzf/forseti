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
	if(formAct.proceso.value == "APLICAR_PRODUCCION")
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
					<input name="ID" type="hidden" id="ID" value="<%= request.getParameter("ID") %>">
                   		Num. Reporte:</td>
                  <td width="35%" class="titChicoAzc"><%= rec.getReporteNum() %></td>
                  <td width="15%">Fecha:</td>
                  <td class="titChicoAzc"><%= rec.getFecha() %></td>
                </tr>
                <tr> 
                  <td>Bodega MP:</td>
                  <td class="titChicoAzc"><%= rec.getBodegaMP() %></td>
                  <td>Bodega PT:</td>
                  <td class="titChicoAzc"><%= rec.getBodegaPT() %></td>
                </tr>
				<tr> 
                 <td>Concepto:</td>
                 <td colspan="2" class="titChicoAzc"><%= rec.getConcepto() %></td>
				 <td>&nbsp;</td>
                </tr>
              </table>
			 </td>
          </tr>
          <tr>
            <td>
<%
		for(int i = 0; i < rec.numPartidas(); i++)
		{
			if(rec.getPartida(i).getActualProc() == rec.getPartida(i).getNumProc() && rec.getPartida(i).getTerminada())
				continue;
			if(rec.getPartida(i).getActualProc() == rec.getPartida(i).getNumProc() && !rec.getPartida(i).getTerminada())
			{
%>
			  <table width="100%" border="0" cellspacing="0" cellpadding="2">
			    <tr> 
				  <td class="titChicoNeg" colspan="7" align="center">Aplicación 
                    de Producto Terminado</td>
                </tr>
                <tr bgcolor="#0099FF">
				  <td width="15%" align="center" class="titChico"> 
				    <input name="tipo_part" type="hidden" id="tipo_part" value="PT"> 
                    <input name="id_form" type="hidden" id="id_form" value="<%= (i+1) %>"> 
					Fecha</td>
                  <td width="15%" class="titChico">Clave</td>
                  <td class="titChico">Descripci&oacute;n</td>
                  <td width="20%"  class="titChico">F&oacute;rmula</td>
                  <td width="10%" align="right" class="titChico" >Cant</td>
                  <td width="5%" align="center" class="titChico">Uni</td>
				  <td width="15" class="titChico">Lote</td>
                </tr>
                <tr> 
				  <td align="center" class="titChicoAzc"><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(rec.getPartida(i).getFecha(),"dd/MMM/yyyy") %>"> 
                    <!--a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a--></td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getID_Prod() %></td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getDescripcion() %></td>
                  <td class="titChicoAzc"><%= rec.getPartida(i).getFormula() %></td>
                  <td class="titChicoAzc" align="right"><input name="cantidad" type="text" class="cpoBco" id="cantidad" size="7" maxlength="12" value="<%= rec.getPartida(i).getCantidad() %>"></td>
                  <td class="titChicoAzc" align="center"><%= rec.getPartida(i).getUnidad() %></td>
				  <td class="titChicoAzc"><%= rec.getPartida(i).getLote() %></td>
                </tr>
				<tr> 
				  <td class="titChicoAzc" colspan="7">Observaciones: <%= rec.getPartida(i).getObs() %></td>
                </tr>
			</table>
<% 
			}

			for(int j = 0; j < rec.getPartida(i).getPartidaFormula().numPartidas(); j++)
			{
				if(rec.getPartida(i).getActualProc() == j && rec.getPartida(i).getTerminada())
				{
%>					
				  		<table width="100%" border="0" cellspacing="0" cellpadding="2">
						  <tr> 
				  			
                  <td class="titChicoNeg" colspan="7" align="center">Aplicación 
                    de Subproducto</td>
                		  </tr>
						  <tr bgcolor="#0099FF">
						    <td width="20%" class="titChico">
							<input name="tipo_part" type="hidden" id="tipo_part" value="SP"> 
                    		<input name="id_form" type="hidden" id="id_form" value="<%= (i+1) %>"> 
							<input name="id_proc" type="hidden" id="id_proc" value="<%= (j+1) %>"> 
								Nombre</td>
           					<td  width="16%" align="center" class="titChico">Fecha</td>
							<td  width="10%" align="right" class="titChico">Cant</td>
							<td  width="10%" align="right" class="titChico">+/-</td>
            				<td  width="6%" align="center" class="titChico">Uni</td>
							<td width="15%" class="titChico" >Clave</td>
            				<td class="titChico">Descripcion</td>
						  </tr>
						  <tr>
						    <td class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getNombre() %></td>
           					<td align="center" class="titChicoNeg"><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(rec.getPartida(i).getPartidaFormula().getPartida(j).getFechaSP(),"dd/MMM/yyyy") %>"> 
                    			<!--a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a--></td>
							<td align="right" class="titChicoNeg"><input name="cantidad" type="text" class="cpoBco" id="cantidad" size="7" maxlength="12" value="<%= rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad() %>"></td>
            				<td align="right" class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getMasMenos() %></td>
            				<td align="center" class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getUnidad() %></td>
							<td class="titChicoNeg" ><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod() %></td>
            				<td class="titChicoNeg"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getDescripcion() %></td>
						  </tr>
						</table>
<%
				}
								
				if(rec.getPartida(i).getActualProc() == j && !rec.getPartida(i).getTerminada())
				{
%>
							<table width="100%" border="0" cellspacing="0" cellpadding="2">
							    <tr> 
				  					
                  <td class="titChicoNeg" colspan="5" align="center">Aplicación 
                    de Materias Primas del proceso: <%= rec.getPartida(i).getPartidaFormula().getPartida(j).getNombre() %></td>
                				</tr>
							    <tr bgcolor="#0099FF">
								  <td width="15%" align="right" class="titChico">
								   <input name="tipo_part" type="hidden" id="tipo_part" value="MP"> 
                    				<input name="id_form" type="hidden" id="id_form" value="<%= (i+1) %>"> 
									<input name="id_proc" type="hidden" id="id_proc" value="<%= (j+1) %>"> 
									Cant</td>
								  <td width="15%" align="right" class="titChico">+/-</td>
								  <td width="10%" align="center" class="titChico">Uni</td>
								  <td width="15%" class="titChico">Clave</td>
								  <td class="titChico">Descripcion</td>
								</tr>
								<tr>
								  <td class="titChicoNeg">Fecha:</td>
								  
                  <td colspan="4"> <input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(rec.getPartida(i).getPartidaFormula().getPartida(j).getFecha(),"dd/MMM/yyyy") %>">
                    <!--a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a--></td>
								</tr>
<%
					for(int d = 0; d < rec.getPartida(i).getPartidaFormula().getPartida(j).numPartidas(); d++)
					{
%>
							  
								<tr> 
								  <td align="right"><input name="cantidad_<%= (d+1) %>" type="text" class="cpoBco" id="cantidad_<%= (d+1) %>" size="7" maxlength="12" value="<%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidad() %>"></td>
								  <td align="right"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getMasMenos() %></td>
								  <td align="center"><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getUnidad() %></td>
								  <td><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getID_Prod() %></td>
								  <td><%= rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getDescripcion() %></td>
								 </tr>
<%
					}
%>
							</table>
<%
				}
			}
			break;
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
</body>
</html>
