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
<%@ page import="forseti.*, forseti.sets.*, forseti.compras.*, forseti.ventas.*, java.util.*, java.io.*, org.jdom.input.*, org.jdom.*"%>
<%
	String fact_xml = (String) request.getAttribute("fact_xml");
		
	if(fact_xml == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String idmod = (String)request.getAttribute("idmod");
	//String moddes = (String)request.getAttribute("moddes");
	String titulo =  JUtil.getSesion(request).getSesion(idmod).generarTitulo(JUtil.Msj("CEF",idmod,"VISTA",request.getParameter("proceso"),3));
	String entidad = JUtil.getSesion(request).getSesion(idmod).getEspecial();
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
// Funciones de forseti
function enviarlo(formAct)
{
<%
	if(request.getParameter("ID") != null)
	{
%>
	if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
	{
		formAct.aceptar.disabled = true;
		return true;
	}
	else
		return false;
<%
	}
	else
	{
		out.print("    return true;");
	}
%>
}	
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/<% if(fact_xml.equals("COMPRAS")) { out.print("CEFCompFactDlg"); } else if(fact_xml.equals("VENTAS")) { out.print("CEFVenFactDlg"); } else if(fact_xml.equals("NOMINA")) { out.print("CEFNomMovDirDlg"); } else { out.print("CEFContaPolizasDlg"); } %>" method="post" enctype="application/x-www-form-urlencoded" name="fact_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<% 
					if(fact_xml.equals("COMPRAS"))
					{
						if(request.getParameter("tipomov").equals("FACTURAS")) 
							out.print("CEFCompFactCtrl");
						else if(request.getParameter("tipomov").equals("ORDENES")) 
							out.print("CEFCompOrdenesCtrl");
						else if(request.getParameter("tipomov").equals("RECEPCIONES")) 
							out.print("CEFCompRecepcionesCtrl");
						else if(request.getParameter("tipomov").equals("GASTOS")) 
							out.print("CEFCompGastosCtrl");
						else // DEVOLUCIONES
							out.print("CEFCompDevolucionesCtrl");
					}
					else if(fact_xml.equals("VENTAS"))
					{
						if(request.getParameter("tipomov").equals("FACTURAS")) 
							out.print("CEFVenFactCtrl");
						else if(request.getParameter("tipomov").equals("COTIZACIONES")) 
							out.print("CEFVenCotizacionesCtrl");
						else if(request.getParameter("tipomov").equals("REMISIONES")) 
							out.print("CEFVenRemisionesCtrl");
						else if(request.getParameter("tipomov").equals("PEDIDOS")) 
							out.print("CEFVenPedidosCtrl");
						else // DEVOLUCIONES
							out.print("CEFVenDevolucionesCtrl");
					}
					else if(fact_xml.equals("NOMINA"))
					{
						out.print("CEFNomMovDirCtrl");
					}
					else
					{
						out.print("CEFContaPolizasCtrl");
					} %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table> 
      </td>
    </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
 	  <td height="115" bgcolor="#333333">&nbsp;</td>
   </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td>
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr bgcolor="#0099FF"> 
            <td width="3%" class="titChico"> <input name="subproceso" type="hidden" value="ENLAZAR"> 
              <input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>"> 
              <input name="tipomov" type="hidden" value="<%= request.getParameter("tipomov") %>">
			  <input name="id" type="hidden" value="<%= request.getParameter("id") %>">
<% if(request.getParameter("ID") != null) { %><input name="ID" type="hidden" value="<%= request.getParameter("ID") %>"><% } %>
<% if(request.getParameter("idempleado") != null) { %><input name="idempleado" type="hidden" value="<%= request.getParameter("idempleado") %>"><% } %>
			  &nbsp;</td>
			<td class="titChico">Tipo</td>
			<td class="titChico">Fecha</td>
			<td class="titChico">RFC</td>
			<td class="titChico">Nombre</td>
			<td class="titChico" align="center">Mon</td>
			<td class="titChico" align="right">TC</td>
			<td class="titChico" align="right">Sub Total</td>
			<td class="titChico" align="right">Descuento</td>
			<td class="titChico" align="right">IVA</td>
			<td class="titChico" align="right">Retencion</td>
			<td class="titChico" align="right">Total</td>
          </tr>
<%
	JCFDCompSet set = new JCFDCompSet(request, fact_xml);
	set.m_Where = "FSI_Tipo = 'ENT' and FSI_ID = '" + JUtil.p(entidad) + "'";
	set.m_OrderBy = "Fecha ASC";
	set.Open();
	for(int i = 0; i < set.getNumRows(); i++)
	{
		SAXBuilder builder = new SAXBuilder();
		String dir;
		if(fact_xml.equals("COMPRAS"))
			dir = "comp";
		else if(fact_xml.equals("VENTAS"))
			dir = "ven";
		else if(fact_xml.equals("NOMINA"))
			dir = "nom";
		else 
			dir = "cont";
						
		File xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/" + dir + "/TFDs/" + set.getAbsRow(i).getUUID() + ".xml");
 		try 
		{
			Document document = (Document)builder.build(xmlFile);
			Element Comprobante = document.getRootElement();
			Namespace ns = Comprobante.getNamespace();
			Element Emisor = Comprobante.getChild("Emisor",ns);
			Element Receptor = Comprobante.getChild("Receptor",ns);
			Element Impuestos = Comprobante.getChild("Impuestos",ns);
			Element Conceptos = Comprobante.getChild("Conceptos",ns);
			List con = Conceptos.getChildren("Concepto", ns);
%>
 		  <tr> 
            <td>
			 <input name="uuid" type="<%= (!fact_xml.equals("NOMINA") ? (request.getParameter("tipomov").equals("GASTOS") ? "checkbox" : "radio") : "radio") %>" value="<%= set.getAbsRow(i).getUUID() %>">&nbsp;</td>
        	<td class="titChicoNeg"><%= Comprobante.getAttributeValue("tipoDeComprobante") %></td>
			<td class="titChicoNeg"><%= Comprobante.getAttributeValue("fecha") %></td>
<%
			if(fact_xml.equals("COMPRAS"))
			{
%>			
			<td class="titChicoNeg"><%= Emisor.getAttributeValue("rfc") %></td>
			<td class="titChicoNeg"><%= Emisor.getAttributeValue("nombre") == null ? "Proveedor de Mostrador" : Emisor.getAttributeValue("nombre") %></td>
<%
			}
			else
			{
%>			
			<td class="titChicoNeg"><%= Receptor.getAttributeValue("rfc") %></td>
			<td class="titChicoNeg"><%= Receptor.getAttributeValue("nombre") == null ? "Cliente de Mostrador" : Receptor.getAttributeValue("nombre") %></td>
<%
			}
%>			
			<td align="center" class="titChicoNeg"><%= Comprobante.getAttributeValue("Moneda") == null ? "&nbsp;" : Comprobante.getAttributeValue("Moneda") %></td>
			<td align="right" class="titChicoNeg"> 
              <%= Comprobante.getAttributeValue("TipoCambio") == null ? "&nbsp;" : JUtil.Converts(Comprobante.getAttributeValue("TipoCambio"), ",", ".", 4, false) %>
            </td>
			<td align="right" class="titChicoNeg"><%= JUtil.Converts(Comprobante.getAttributeValue("subTotal"), ",", ".", 2, false) %></td>
			<td align="right" class="titChicoNeg"><%= Comprobante.getAttributeValue("descuento") == null ? "&nbsp;" : JUtil.Converts(Comprobante.getAttributeValue("descuento"), ",", ".", 2, false) %></td>
			<td align="right" class="titChicoNeg"><%= Impuestos.getAttributeValue("totalImpuestosTrasladados") == null ? "&nbsp;" : JUtil.Converts(Impuestos.getAttributeValue("totalImpuestosTrasladados"), ",", ".", 2, false) %></td>
	 		<td align="right" class="titChicoNeg"><%= Impuestos.getAttributeValue("totalImpuestosRetenidos") == null ? "&nbsp;" : JUtil.Converts(Impuestos.getAttributeValue("totalImpuestosRetenidos"), ",", ".", 2, false) %></td>
			<td align="right" class="titChicoNeg"><%= JUtil.Converts(Comprobante.getAttributeValue("total"), ",", ".", 2, false) %></td>
          </tr>
		  <tr> 
		  	<td colspan="12">
			  <table width="100%" border="0" cellspacing="2" cellpadding="0">
<%
			if(!fact_xml.equals("NOMINA"))
			{
				for (int j = 0; j < con.size(); j++) 
				{
	 		   		Element Concepto = (Element) con.get(j);
%>	
		  
          		<tr> 
           		 	<td align="right"><%= JUtil.Converts(Concepto.getAttributeValue("cantidad"), ",", ".", 2, false) %></td>
	 		   		<td align="center"><%= Concepto.getAttributeValue("unidad") %></td>
	 		   		<td><%= Concepto.getAttributeValue("noIdentificacion") == null ? "&nbsp;" : Concepto.getAttributeValue("noIdentificacion") %></td>
	 		   		<td><%= Concepto.getAttributeValue("descripcion") %></td>
	 		   		<td align="right"><%= JUtil.Converts(Concepto.getAttributeValue("valorUnitario"), ",", ".", 2, false) %></td>
	 		   		<td align="right"><%= JUtil.Converts(Concepto.getAttributeValue("importe"), ",", ".", 2, false) %></td>
		  		</tr>
<%	 		   	
				}
			}
			else
			{
				Element Complemento = Comprobante.getChild("Complemento",ns);
	 			// Ahora incluye la nomina
	 			Namespace nsnomina = Namespace.getNamespace("nomina","http://www.sat.gob.mx/nomina");
    			Element Nomina = Complemento.getChild("Nomina", nsnomina);
    			Element Percepciones = Nomina.getChild("Percepciones", nsnomina);
    			Element Deducciones = Nomina.getChild("Deducciones", nsnomina);
				List per = Percepciones.getChildren("Percepcion", nsnomina);
				List ded = Deducciones.getChildren("Deduccion", nsnomina);
				//System.out.println("ID: " + per.size());
				for (int j = 0; j < per.size(); j++) 
				{
	 		   		Element Percepcion = (Element) per.get(j);
%>	
	       		<tr> 
           		 	<td align="right"><%= Percepcion.getAttributeValue("TipoPercepcion") %></td>
	 		   		<td align="center"><%= Percepcion.getAttributeValue("Clave") %></td>
	 		   		<td><%= Percepcion.getAttributeValue("Concepto") %></td>
	 		   		<td align="right"><%= JUtil.Converts(Percepcion.getAttributeValue("ImporteGravado"), ",", ".", 2, false) %></td>
	 		   		<td align="right"><%= JUtil.Converts(Percepcion.getAttributeValue("ImporteExento"), ",", ".", 2, false) %></td>
	 		   		<td align="right">&nbsp;</td>
	 		   		<td align="right">&nbsp;</td>
		  		</tr>
<%	 							
				}
				
				for (int j = 0; j < ded.size(); j++) 
				{
	 		   		Element Deduccion = (Element) ded.get(j);
%>	
	       		<tr> 
           		 	<td align="right"><%= Deduccion.getAttributeValue("TipoDeduccion") %></td>
	 		   		<td align="center"><%= Deduccion.getAttributeValue("Clave") %></td>
	 		   		<td><%= Deduccion.getAttributeValue("Concepto") %></td>
					<td align="right">&nbsp;</td>
	 		   		<td align="right">&nbsp;</td>
	 		   		<td align="right"><%= JUtil.Converts(Deduccion.getAttributeValue("ImporteGravado"), ",", ".", 2, false) %></td>
	 		   		<td align="right"><%= JUtil.Converts(Deduccion.getAttributeValue("ImporteExento"), ",", ".", 2, false) %></td>
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
		catch (Exception e) 
		{
%>
		  <tr> 
            <td colspan="12">
              El archivo XML con UUID <%= set.getAbsRow(i).getUUID() %> Tuvo errores.:<br>
			  <%= e.getMessage() %>
			  <% e.printStackTrace(System.out); %>
			</td>
          </tr>
<%
		}
	}
	
	if(!fact_xml.equals("NOMINA") && !fact_xml.equals("VENTAS"))
	{
%>
 		 <tr bgcolor="#0099FF">  
        	<td class="titChico" colspan="12" align="center"> CBBs y Facturas 
              Extranjeras </td>
         </tr>
		 <tr bgcolor="#CCCCCC"> 
            <td width="3%" class="titChico">&nbsp;</td>
			<td class="titChico">Tipo</td>
			<td class="titChico">Factura</td>
			<td class="titChico" colspan="2">Nombre</td>
			<td class="titChico" align="center">Mon</td>
			<td class="titChico" align="right">TC</td>
			<td class="titChico" colspan="5" align="right">Total</td>
          </tr>		
<%		  	
		JCFDCompOtrSet sotr = new JCFDCompOtrSet(request);
		sotr.m_Where = "FSI_Tipo = 'ENT' and FSI_ID = '" + JUtil.p(entidad) + "'";
		sotr.m_OrderBy = "Total DESC";
		sotr.Open();
		for(int i = 0; i < sotr.getNumRows(); i++)
		{
%>
		  <tr> 
            <td><input name="cbbext" type="<%= (request.getParameter("tipomov").equals("GASTOS") ? "checkbox" : "radio") %>" value="CBBEXT-<%= sotr.getAbsRow(i).getID_CFD() %>">&nbsp;</td>
        	<td class="titChicoNeg"><%= sotr.getAbsRow(i).getTipo() %></td>
			<td class="titChicoNeg"><%= sotr.getAbsRow(i).getFactura() %></td>
			<td class="titChicoNeg" colspan="2"><%= sotr.getAbsRow(i).getNombre_Original() %></td>
			<td class="titChicoNeg" align="center"><%= sotr.getAbsRow(i).getID_Moneda() %></td>
			<td class="titChicoNeg" align="right"><%= sotr.getAbsRow(i).getTC() %></td>
			<td colspan="5" class="titChicoNeg" align="right"><%= sotr.getAbsRow(i).getTotal() %></td>
          </tr>	
<%
		}
	}
%>	  
        </table>
	</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
</form>
</body>
</html>
