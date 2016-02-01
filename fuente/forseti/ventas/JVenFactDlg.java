/*
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
*/
package forseti.ventas;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import forseti.JBajarArchivo;
import forseti.JFacturasXML;
import forseti.JForsetiApl;
import forseti.JForsetiCFD;
import forseti.JFsiSMTPClient;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JBDSSet;
import forseti.sets.JCFDCompSet;
import forseti.sets.JClientClientMasSetV2;
import forseti.sets.JClientClientSetV2;
import forseti.sets.JPublicContMonedasSetV2;
import forseti.sets.JPublicInvServInvCatalogSetV2;
import forseti.sets.JVentasCotizacionesSet;
import forseti.sets.JVentasDevolucionesSet;
import forseti.sets.JVentasEntidadesSetIdsV2;
import forseti.sets.JVentasFactSetCabV2;
import forseti.sets.JVentasFactSetDetV2;
import forseti.sets.JVentasFactSetV2;
import forseti.sets.JVentasPedidosSet;
import forseti.sets.JVentasRemisionesSet;


@SuppressWarnings("serial")
public class JVenFactDlg extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String ven_fact_dlg = "";
      request.setAttribute("ven_fact_dlg",ven_fact_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if (request.getContentType() != null && 
  		    request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) 
	  {
	  	  if(!getSesion(request).getRegistrado()) 
	  	  { 
	  		 irApag("/forsetiweb/errorAtributos.jsp",request,response);
	  		 return;
	  	  }
	  	  else
	  	  {
	  		  try
	  		  {
	  			  HttpSession ses = request.getSession(true);
	  			  JFacturasXML venfactxml = (JFacturasXML)ses.getAttribute("ven_fact_xml");
	  			  Vector archivos = new Vector();
	  			  DiskFileUpload fu = new DiskFileUpload();
	  			  List items = fu.parseRequest(request);
				  Iterator iter = items.iterator();
				  while (iter.hasNext()) 
	  			  {
	  				  FileItem item = (FileItem)iter.next();
	  				  if (item.isFormField()) 
	  				  	  venfactxml.getParametros().put(item.getFieldName(), item.getString());
	  				  else
	  				  	  archivos.addElement(item);
	  			  }
	  			  
	  			  // revisa por las entidades
	  			  JVentasEntidadesSetIdsV2 setids;
	  			  String idmod = venfactxml.getParametros().getProperty("idmod"), 
	  					 idmod4 = venfactxml.getParametros().getProperty("idmod4"), 
	  					 moddes = venfactxml.getParametros().getProperty("moddes");
	  			
	  			  request.setAttribute("idmod",idmod);
	  			  request.setAttribute("moddes",moddes);
	  			  
	  			  
	  			  setids = new JVentasEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion(idmod).getEspecial());
	  			  setids.Open();
	  
	  			  if(setids.getNumRows() < 1)
	  			  {
	  				  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
	  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  				  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), idmod, idmod4 + "||||",mensaje);
	  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	  				  return;
	  			  }
	          
	  			  if(!idmod.equals("VEN_DEV")) // Si no es devolucion
	          	  {
	          		  if(!getSesion(request).getPermiso(idmod + "_AGREGAR"))
	          		  {
	          			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod + "_AGREGAR");
	          			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod + "_AGREGAR",idmod4 + "||||",mensaje);
	          			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	          			  return;
	          		  }
	          	  }
	          	  else
	          	  {	  
	          		  if(!getSesion(request).getPermiso("VEN_DEV_DEVOLVER") && !getSesion(request).getPermiso("VEN_DEV_REBAJAR"))
	          		  {
	          			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_DEV_DEVOLVER") + " / " + MsjPermisoDenegado(request, "CEF", "VEN_DEV_REBAJAR");
	          			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_DEV_DEVOLVER","VDEV||||",mensaje);
	          			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	          			  return;
	          		  }
	          	  }
	  			 	  			
	  			  SubirArchivosCFD(request, response, venfactxml, archivos);
	  			  return;
	  		  } 
	  		  catch (FileUploadException e) 
	  		  {
	  			  e.printStackTrace();
	  			  return;
	  		  }
	  		  catch (Exception e) 
	  		  {
	  			  e.printStackTrace();
	  			  return;
	  		  }
	  	  }
	  }
      
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	  // revisa por las entidades
          JVentasEntidadesSetIdsV2 setids;
          String idmod, idmod4, moddes;
          
          if(request.getParameter("tipomov").equals("FACTURAS"))
          {
        	  idmod = "VEN_FAC"; 
        	  idmod4 = "VFAC";
        	  moddes = "FACTURAS";
          }
          else if(request.getParameter("tipomov").equals("PEDIDOS"))
          {
        	  idmod = "VEN_PED"; 
        	  idmod4 = "VPED";
        	  moddes = "PEDIDOS";
          }
          else if(request.getParameter("tipomov").equals("REMISIONES"))
          {
        	  idmod = "VEN_REM"; 
        	  idmod4 = "VREM";
        	  moddes = "REMISIONES";
          }
          else if(request.getParameter("tipomov").equals("COTIZACIONES"))
          {
        	  idmod = "VEN_COT"; 
        	  idmod4 = "VCOT";
        	  moddes = "COTIZACIONES";
          }
          else
          {
        	  idmod = "VEN_DEV"; 
        	  idmod4 = "VDEV";
        	  moddes = "DEVOLUCIONES";
          }
          request.setAttribute("idmod",idmod);
          request.setAttribute("moddes",moddes);
          request.setAttribute("fact_xml","VENTAS");
          
          setids = new JVentasEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion(idmod).getEspecial());
          setids.Open();
  
          if(setids.getNumRows() < 1)
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), idmod, idmod4 + "||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }
          
          // Revisa por intento de intrusion (Salto de permiso de entidad)
          if(!request.getParameter("proceso").equals("AGREGAR_VENTA") && request.getParameter("ID") != null)
          {
        	  boolean intrusion = false;
        	  if(moddes.equals("FACTURAS"))
        	  {
        		  JVentasFactSetV2 set = new JVentasFactSetV2(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Factura = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else if(moddes.equals("REMISIONES"))
        	  {
        		  JVentasRemisionesSet set = new JVentasRemisionesSet(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Remision = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else if(moddes.equals("PEDIDOS"))
        	  {
        		  JVentasPedidosSet set = new JVentasPedidosSet(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Pedido = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else if(moddes.equals("COTIZACIONES"))
        	  {
        		  JVentasCotizacionesSet set = new JVentasCotizacionesSet(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else
        	  {
        		  JVentasDevolucionesSet set = new JVentasDevolucionesSet(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  
        	  if(intrusion)
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),idmod,idmod4 + "|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Entidad() + "||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
          }
          
          if(request.getParameter("proceso").equals("ENLAZAR_VENTA"))
          {
        	  if(moddes.equals("FACTURAS") || moddes.equals("REMISIONES") || moddes.equals("DEVOLUCIONES")) 
        	  {
        		  // Revisa si tiene permisos
        		  if(!idmod.equals("VEN_DEV")) // Si no es devolucion
	          	  {
	          		  if(!getSesion(request).getPermiso(idmod + "_AGREGAR"))
	          		  {
	          			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod + "_AGREGAR");
	          			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod + "_AGREGAR",idmod4 + "||||",mensaje);
	          			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	          			  return;
	          		  }
	          	  }
	          	  else
	          	  {	  
	          		  if(!getSesion(request).getPermiso("VEN_DEV_DEVOLVER") && !getSesion(request).getPermiso("VEN_DEV_REBAJAR"))
	          		  {
	          			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_DEV_DEVOLVER") + " / " + MsjPermisoDenegado(request, "CEF", "VEN_DEV_REBAJAR");
	          			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_DEV_DEVOLVER","VDEV||||",mensaje);
	          			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	          			  return;
	          		  }
	          	  }
        		  	
        		  if(setids.getAbsRow(0).getCFD() || setids.getAbsRow(0).getFija())
            	  {
            		  idmensaje = 3; mensaje += "ERROR: No se puede enlazar ningun CFDI porque esta entidad de venta está establecida como Fija o genera sus propios CFDIs<br>";
    			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  	  return;
            	  }
            	  
            	  if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni ENLAZAR,
        		  {
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/fact_dlg_xmls.jsp", request, response);
            		  return;
        		  }  
	        	  else
	        	  {
	        		  if(request.getParameter("subproceso").equals("ENLAZAR"))
	        		  {
	        			  // Se supone que la compra aun no estará ligada a una compra existente...
	        			  HttpSession ses = request.getSession(true);
	        			  JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
        			  	  if(rec == null)
        			  	  {
        			  		  rec = new JVenFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
        			  		  ses.setAttribute("ven_fact_dlg", rec);
        			  	  }
        			  	  else
        			  		  rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
        			  	  
	        			  JCFDCompSet comprobante = new JCFDCompSet(request,"VENTAS");
	        			  comprobante.m_Where = "UUID = '" + p(request.getParameter("uuid")) + "'";
	        			  comprobante.Open();
	        				
	        			  if(comprobante.getNumRows() < 1 || !comprobante.getAbsRow(0).getFSI_Tipo().equals("ENT") || comprobante.getAbsRow(0).getFSI_ID() != Integer.parseInt(getSesion(request).getSesion(idmod).getEspecial()))
	        			  {
	        				  idmensaje = 3; mensaje += "ERROR: No se ha cargado el CFDI de la venta, este ya esta ligado a otra venta, o el CFDI se cargó en otra entidad<br>";
	        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  	  return;
	        			  }
	        				
	        			  JFacturasXML venfactxml = new JFacturasXML();
	            		 
	            		  StringBuffer sb_mensaje = new StringBuffer();
	            		  if(!JForsetiCFD.CargarDocumentoCFDI(request, venfactxml, sb_mensaje, request.getParameter("uuid"), "I"))
	            		  {
	            			  idmensaje = 3; mensaje += sb_mensaje.toString();
	        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  	  return;
	            		  }
	            		  
	            		  if((venfactxml.getComprobante().getProperty("tipoDeComprobante").equals("ingreso") && moddes.equals("DEVOLUCIONES")) 
	        			  		  || (venfactxml.getComprobante().getProperty("tipoDeComprobante").equals("egreso") && !moddes.equals("DEVOLUCIONES")) 
	        			  		  || venfactxml.getComprobante().getProperty("tipoDeComprobante").equals("traslado"))
	        			  {
	        			  	  idmensaje = 3; mensaje += "ERROR: El tipo de comprobante fiscal digital CFDI, No corresponde con el tipo de documento a enlazar.<br>";
	        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  	  return;
	        			  }

	        			  //Verifica que el RFC del Emisor sea igual al RFC registrado, o que sea rfc generico
	        			  JBDSSet set = new JBDSSet(request);
	        			  set.ConCat(true);
	        			  set.m_Where = "Nombre = 'FSIBD_" + p(getSesion(request).getBDCompania()) + "'";
	        			  set.Open();
	        			  if(!venfactxml.getRFC_Emisor().equalsIgnoreCase(set.getAbsRow(0).getRFC()))
	        			  {
	        			  	  idmensaje = 3; mensaje = "ERROR: El RFC del emisor en el XML no pertenece a la compañia";
	        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  	  return;
	        			  }
	        			  
	        			  if(request.getParameter("ID") == null) // Significa que debe agregar una nueva venta 
	        			  {
	        				  if(moddes.equals("DEVOLUCIONES") || moddes.equals("PEDIDOS") || moddes.equals("COTIZACIONES"))
	        				  {
	        					  idmensaje = 1; mensaje = "PRECAUCION: Solo se pueden enlazar devoluciones previamente generadas desde facturas. Selecciona una devolución e intenta enlazar de nuevo.";
		        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		        			  	  return;
	        				  }
	        				  
	        				  float descuento = Float.parseFloat(venfactxml.getComprobante().getProperty("descuento"));
		        			  if(descuento != 0.0)
		        			  {
		        			  	  idmensaje = 1; mensaje = "PRECAUCION: Por el momento, la carga de ventas no soportan descuentos implicitos porque en el CFDI no se especifica a que producto(s) o servicio(s) aplica cada descuento, lo que hace imposible determinar un costo verdadero.";
		        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		        			  	  return;
		        			  }
		        			  
		        			  rec.agregaCFDI(venfactxml);
		        			  
		        			  Date fecha = JUtil.estFechaCFDI(venfactxml.getComprobante().getProperty("fecha"));
	        			  	  rec.setFecha(fecha);
	        			  	  float tc;
	        			  	  try { tc = Float.parseFloat(venfactxml.getComprobante().getProperty("TipoCambio")); } catch(NumberFormatException e) { tc = 1.0F; }
	        			  	  rec.setTC(tc);
	        			  	  int idmon = ((tc == 1) ? 1 : 2); //cambiar por 2 cuando se inserte moneda de dolares automaticamente al instalar empresa... Para ya distribuir
	        			  	  rec.setID_Moneda((byte)idmon);
	        			  	  rec.setTotal(Float.parseFloat(venfactxml.getComprobante().getProperty("total")));
	        			      float iva;
	        			  	  try { iva = Float.parseFloat(venfactxml.getImpuestos().getProperty("totalImpuestosTrasladados")); } catch(NumberFormatException e) { iva = 0.0F; }
	        			  	  rec.setIVA(iva);
	        			  	  rec.setSubTotal(Float.parseFloat(venfactxml.getComprobante().getProperty("subTotal")));
	        			   	  rec.setDescuento(descuento);
	        			  	  rec.setImporte(rec.getSubTotal() - descuento);
	        			      rec.setReferencia("s/r");
	        			      rec.setFechaEntrega(fecha);
	        			  	  JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	        			  	  setMon.m_Where = "Clave = '" + idmon  + "'";
	        			  	  setMon.Open(); 
	        			      rec.setMoneda(setMon.getAbsRow(0).getMoneda());
	        			      rec.setObs("Carga desde Factura Electrónica: " + venfactxml.getTFD().getProperty("UUID"));
	        			      rec.setID_Bodega(setids.getAbsRow(0).getID_Bodega());
	        			      rec.setBodegaDesc(setids.getAbsRow(0).getBodega());
	        			      
	        			      JClientClientMasSetV2 setcli = new JClientClientMasSetV2(request);
	        			  	  setcli.m_Where = "ID_Tipo = 'CL' and ID_EntidadVenta = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and RFC ~~* '" + p(venfactxml.getRFC_Receptor()) + "'";
	        			  	  setcli.Open();
	        			  	  
	        			  	  if(setcli.getNumRows() > 0)
	        			  	  {
	        			  		  JClientClientSetV2 setcli2 = new JClientClientSetV2(request);
		        			  	  setcli2.m_Where = "ID_Tipo = 'CL' and Clave = '" + setcli.getAbsRow(0).getID_Clave() + "'";
		        			  	  setcli2.Open();
		        			  	
		        			  	  rec.setClave(setcli.getAbsRow(0).getID_Clave());
		        			  	  rec.setNombre(setcli2.getAbsRow(0).getNombre());
		        			  	  rec.setRFC(venfactxml.getRFC_Emisor());
		        			      rec.setNumero(setcli2.getAbsRow(0).getNumero());
		        			      rec.setColonia(setcli.getAbsRow(0).getColonia());
		        			      rec.setForma_Pago((setcli2.getAbsRow(0).getDias() < 1) ? "contado" : "credito");
		        			      rec.setCP(setcli.getAbsRow(0).getCP());
		        			      rec.setDireccion(setcli.getAbsRow(0).getDireccion());
		        			      rec.setPoblacion(setcli.getAbsRow(0).getPoblacion());
		        			      rec.setTels(setcli2.getAbsRow(0).getTel());
		        			      rec.setID_Vendedor(setcli.getAbsRow(0).getID_Vendedor());
		        			      rec.setVendedorNombre(setcli.getAbsRow(0).getVendedorNombre());
	        			  	  }
	        			  	          			      
	        			      for(int i = 0; i< venfactxml.getConceptos().size(); i++)
	        			      {
	        			    	  Properties concepto = (Properties)venfactxml.getConceptos().elementAt(i);
	        			    	  float cantidad = Float.parseFloat(concepto.getProperty("cantidad"));
	        			    	  float precio = Float.parseFloat(concepto.getProperty("valorUnitario"));
	        			    	  String unidad = concepto.getProperty("unidad");
	        			    	  String idprod = "";
	        			    	  String descripcion = concepto.getProperty("descripcion");
	        			    	  String obs = concepto.getProperty("descripcion");
	        			    	  String tipo = "";
	        			    	  float importe = Float.parseFloat(concepto.getProperty("importe"));
	        			    	  float ivapor = 0.00F, iepspor = 0.00F, ivaretpor = 0.00F, isrretpor = 0.00F,
	        			    			 ivaimp = 0.00F, iepsimp = 0.00F, ivaretimp = 0.00F, isrretimp = 0.00F;
	        			    	  float totalPart = importe;
	        			    	  
	        			    	  JPublicInvServInvCatalogSetV2 cat = new JPublicInvServInvCatalogSetV2(request);
	        			    	  cat.m_Where = "(Clave = '" + p(concepto.getProperty("noIdentificacion")) + "' or Codigo = '" + p(concepto.getProperty("noIdentificacion")) + "') and NoSeVende = '0' and Status = 'V'";
	        					  //System.out.println(cat.getSQL());
        			     		  cat.Open();
        			  			  if(cat.getNumRows() > 0 )
        			  			  {
        			  				  idprod = cat.getAbsRow(0).getClave();
        			  				  descripcion = cat.getAbsRow(0).getDescripcion();
        			  				  tipo = cat.getAbsRow(0).getID_Tipo();
        			  			  }
        			  			  //System.out.println(descripcion + " " + ivapor + " " + ivaimp + " " + totalPart);
        			    		  rec.agregaPartida(cantidad, unidad, idprod, idprod, descripcion, precio, importe, 0.00F, ivapor, iepspor, ivaretpor, isrretpor,
	        			    			  0.00F, ivaimp, iepsimp, ivaretimp, isrretimp, totalPart, obs, tipo);
	        			      }
	        			      
	        			      rec.setUUID(venfactxml.getTFD().getProperty("UUID"));
	        			  	  rec.setTotalUUIDs(JUtil.redondear(Float.parseFloat(venfactxml.getComprobante().getProperty("total")),2));
	        	        	  idmensaje = rec.establecerConcordancia(request, sb_mensaje);
	        	        	  rec.establecerResultados();
	        	        	  if((rec.getTotal() - rec.getTotalUUIDs()) > 0.1 || (rec.getTotal() - rec.getTotalUUIDs()) < -0.1)
	        	        	  {
	        	        		  idmensaje = 3; 
	        	        		  sb_mensaje.append("ERROR: El total en el o los CFDI no corresponden al Total calculado en el registro a partir de estos CFDI. No se puede agregar. DOC: " + rec.getTotal() + " XML: " + rec.getTotalUUIDs());
	        	        	  }   
	        	        	          	        	  
	        			      getSesion(request).setID_Mensaje(idmensaje, sb_mensaje.toString());
	        			  	  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
	        			  	  return;  		
	        			  }   
	        			  else // Significa que debe Enlazar a una compra existente
	        			  {
	        			  	  ////////////////////////////////////////////////
	        			  	  String[] valoresParam = request.getParameterValues("ID");
	        			  	  if(valoresParam.length == 1)
	        			  	  {	
	        			  		  if(moddes.equals("REMISIONES"))
	        			  		  {
	        			  			  JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
	        			  			  SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
	        			  			  SetMod.Open();
	        			        	
	        			  			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Esta remisión ya esta cancelada, no se puede enlazar el CFDI<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  } 

	        			  			  if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Esta remisión ya tiene una factura asociada, no se puede enlazar el CFDI. Debes enlazarlo a la factura<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  } 
	        			  			  
	        			  			  if(SetMod.getAbsRow(0).getID_CFD() != 0)
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Esta remisión ya tiene un CFDI asociado. No puedes asociar otro CFDI a la misma remisión<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  } 
	        			  			  
	        			  			  JClientClientMasSetV2 setcli = new JClientClientMasSetV2(request);
	        			  			  setcli.m_Where = "ID_Tipo = 'CL' and ID_EntidadVenta = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and ID_Clave = '" + SetMod.getAbsRow(0).getID_Cliente() + "'";
	        			  			  setcli.Open();
	        			  			 	  
	        			  			  if( SetMod.getAbsRow(0).getTotal() != Float.parseFloat(venfactxml.getComprobante().getProperty("total"))
	        			  					  || !setcli.getAbsRow(0).getRFC().equals(venfactxml.getRFC_Receptor()) )
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Los totales o los RFCs de la remisión y el CFDI no coinciden. No se puede asociar este CFDI al registro<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  }
	        			  			  
	        			  			  
	        			  		  }
	        			  		  else if(moddes.equals("FACTURAS"))
	        			  		  {
	        			  			  JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
	        			  			  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	        			  			  SetMod.Open();
	        			        	
	        			  			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Esta factura ya esta cancelada, no se puede enlazar el CFDI<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  } 

	        			  			  if(SetMod.getAbsRow(0).getID_CFD() != 0)
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Esta factura ya tiene un CFDI asociado. No puedes asociar otro CFDI a la misma factura<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  } 
	        			  			  
	        			  			  JClientClientMasSetV2 setcli = new JClientClientMasSetV2(request);
	        			  			  setcli.m_Where = "ID_Tipo = 'CL' and ID_EntidadVenta = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and ID_Clave = '" + SetMod.getAbsRow(0).getID_Cliente() + "'";
	        			  			  setcli.Open();
	        			  			 	  
	        			  			  if(SetMod.getAbsRow(0).getTotal() != Float.parseFloat(venfactxml.getComprobante().getProperty("total"))
	        			  					  || !setcli.getAbsRow(0).getRFC().equals(venfactxml.getRFC_Receptor()) )
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Los totales o los RFCs de la factura y el CFDI no coinciden. No se puede asociar este CFDI al registro<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  }
	        			  	        			  			  
	        			  		  }
	        			  		  else if(moddes.equals("DEVOLUCIONES"))
	        			  		  {
	        			  			  JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
	        			  			  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
	        			  			  SetMod.Open();
	        			        	
	        			  			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Esta devolucion ya esta cancelada, no se puede enlazar el CFDI<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  } 

	        			  			  if(SetMod.getAbsRow(0).getID_CFD() != 0)
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Esta devolucion ya tiene un CFDI asociado. No puedes asociar otro CFDI a la misma compra<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  } 
	        			  			  
	        			  			  JClientClientMasSetV2 setcli = new JClientClientMasSetV2(request);
	        			  			  setcli.m_Where = "ID_Tipo = 'CL' and ID_EntidadVenta = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and ID_Clave = '" + SetMod.getAbsRow(0).getID_Cliente() + "'";
	        			  			  setcli.Open();
	        			  			 	  
	        			  			  if(SetMod.getAbsRow(0).getTotal() != Float.parseFloat(venfactxml.getComprobante().getProperty("total"))
	        			  					  || !setcli.getAbsRow(0).getRFC().equals(venfactxml.getRFC_Receptor()) )
	        			  			  {
	        			  				  idmensaje = 1;
	        			  				  mensaje += "PRECAUCION: Los totales o los RFCs de la devolucion y el CFDI no coinciden. No se puede asociar este CFDI al registro<br>";
	        			  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  				  return;
	        			  			  }
	        			  	        			  			  
	        			  		  }
	        			  		  else // sale si no es recepcion, factura gasto o devolucion 
	        			  			  return;
	        			  	  
	        			  		  // Aqui asocia.
        			  			  Enlazar(request, response, moddes, idmod, idmod4);
        			  			  return;
	        			  	  }
	        			  	  else
	        			  	  {
	        			  		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
	        			  		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  		  return;    
	        			  	  }
	        			  	
	        			  }
	        			  

	        		  }
	        		  else if(request.getParameter("subproceso").equals("ENVIAR"))
	        		  {
	        			  if(AgregarRecursos(request,response) == -1)
	        			  {
	        				  HttpSession ses = request.getSession(true);
	        				  JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
	        				  rec.setReferencia(p(request.getParameter("referencia")));
	        			  
	        				  if(moddes.equals("FACTURAS"))
	        				  {
	        					  if(request.getParameter("forma_pago").equals("contado"))
	        					  {
	        						  request.setAttribute("fsipg_tipo","ventas");
	        						  request.setAttribute("fsipg_proc", "deposito");
	        						  request.setAttribute("fsipg_total",rec.getTotal());
	        						  request.setAttribute("fsipg_ident",getSesion(request).getSesion(idmod).getEspecial());
	        						  if(VerificarParametros(request, response) && VerificarPagoMult(request, response))
	        						  {
	        							  Agregar(request, response, moddes, setids, idmod, idmod4);
	        							  return;
	        						  }
	        					  }
	        					  else
	        					  {
	        						  if(VerificarParametros(request, response))
	        						  {
	        							  request.setAttribute("fsipg_cambio", 0F);
	        							  request.setAttribute("fsipg_efectivo", 0F);
	        							  request.setAttribute("fsipg_bancos", 0F);
	        							  Agregar(request, response, moddes, setids, idmod, idmod4);
	        							  return;							  
	        						  }
	        					  }
	        				  }
	        				  else if(moddes.equals("REMISIONES"))
	        				  {
	        					  if(VerificarParametros(request, response))
	        					  {
	        						  request.setAttribute("fsipg_cambio", 0F);
	        						  request.setAttribute("fsipg_efectivo", 0F);
	        						  request.setAttribute("fsipg_bancos", 0F);      						  
	        						  Agregar(request, response, moddes, setids, idmod, idmod4);
	        						  return;
	        					  }
	        				  }
	        				  else // sale si es devolucion u orden... Las devoluciones no se pueden enlazar desde cero, solo enlaces a devoluciones creadas previamente
	        					  return; 
	        			  }
	        			  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
        				  return;
	        		  }
	        	  }
        	  }
        	  else // Sale si no es FACTURA, DEVOLUCION O REMISION
    			  return;
          }
          else if(request.getParameter("proceso").equals("AGREGAR_VENTA"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso(idmod + "_AGREGAR"))
              {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod + "_AGREGAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod + "_AGREGAR",idmod4 + "||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }

        	  if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
        	  {
        		  HttpSession ses = request.getSession(true);
        		  JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
        		  
        		  if(rec == null)
        		  {
        			  rec = new JVenFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
        			  ses.setAttribute("ven_fact_dlg", rec);
        		  }
        		  else
           			  rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
        		  return;
        	  }
        	  else
        	  {
        		  // Solicitud de envio a procesar
        		  if(request.getParameter("subproceso").equals("ENVIAR"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  if(moddes.equals("FACTURAS"))
        				  {
        					  if(request.getParameter("forma_pago").equals("contado"))
        					  {
        						  HttpSession ses = request.getSession(true);
        						  JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
        						  request.setAttribute("fsipg_tipo","ventas");
        						  request.setAttribute("fsipg_proc", "deposito");
        						  request.setAttribute("fsipg_total",rec.getTotal());
        						  request.setAttribute("fsipg_ident",getSesion(request).getSesion(idmod).getEspecial());
        						  if(VerificarParametros(request, response) && VerificarPagoMult(request, response))
        						  {
        							  Agregar(request, response, moddes, setids, idmod, idmod4);
        							  return;
        						  }
       							  
        					  }
        					  else
        					  {
        						  if(VerificarParametros(request, response))
        						  {
        							  // establece los atributos por default para ventas de crédito
        							  request.setAttribute("fsipg_cambio", 0F);
        							  request.setAttribute("fsipg_efectivo", 0F);
        							  request.setAttribute("fsipg_bancos", 0F);
        							  Agregar(request, response, moddes, setids, idmod, idmod4);
       							      return;							  
        						  }
        					  }
        				  }
        				  else
        				  {
        					  if(VerificarParametros(request, response))
        					  {
        						  // establece los atributos por default para ventas de crédito
        						  request.setAttribute("fsipg_cambio", 0F);
        						  request.setAttribute("fsipg_efectivo", 0F);
        						  request.setAttribute("fsipg_bancos", 0F);      						  
        						  Agregar(request, response, moddes, setids, idmod, idmod4);
        						  return;
        					  }
        				  }
        			  }
        			  
        			  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
        			  return;
        			  
        		  }
        		  else if(request.getParameter("subproceso").equals("AGR_CLIENT"))
        		  {
        			  AgregarCabecero(request,response);
        			  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
        			  return;
        		  }
        		  else if(request.getParameter("subproceso").equals("AGR_PART"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  if(VerificarParametrosPartida(request, response))
        					  AgregarPartida(request, response);
        			  }
        			  
        			  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
        			  return;
        		  }
        		  else if(request.getParameter("subproceso").equals("EDIT_PART"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  if(VerificarParametrosPartida(request, response))
        					  EditarPartida(request, response);
        			  }
	       		  
        			  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
        			  return;
        		  }
        		  else if(request.getParameter("subproceso").equals("BORR_PART"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  BorrarPartida(request, response);
        			  }
	       		  
        			  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
        			  return;
        		  }
        	  }
          }
          else if(request.getParameter("proceso").equals("CARGAR_VENTA"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("ADM_CFDI_CARGAR"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_CARGAR");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_CARGAR",idmod4 + "||||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
        	          	  
        	  if(setids.getAbsRow(0).getCFD() || setids.getAbsRow(0).getFija())
        	  {
        		  idmensaje = 3; mensaje += "ERROR: No se puede cargar ningun CFDI porque esta entidad de venta está establecida como Fija o genera sus propios CFDIs<br>";
			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			  	  return;
        	  }
        	  
        	  Integer subir_archivos = new Integer(2);
        	  request.setAttribute("subir_archivos", subir_archivos);
        		  
        	  HttpSession ses = request.getSession(true);
        	  JFacturasXML rec = (JFacturasXML)ses.getAttribute("ven_fact_xml");
        		  
        	  if(rec == null)
        	  {
        		  rec = new JFacturasXML();
        		  ses.setAttribute("ven_fact_xml", rec);
        	  }
        	  else
        	  {
        		  rec = null;
        		  rec = new JFacturasXML();
        		  ses.setAttribute("ven_fact_xml", rec);
        	  }
           			  
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/subir_archivos.jsp?verif=/servlet/CEFVenFactDlg&archivo_1=xml&archivo_2=pdf&proceso=CARGAR_VENTA&subproceso=ENVIAR&moddes=" + moddes + "&idmod=" + idmod + "&idmod4=" + idmod4, request, response);
        	  return;
        	  
          }
          else if(request.getParameter("proceso").equals("SELLAR_VENTA"))
          {
        	  // Revisa si tiene permisos
        	  
        	  if(!idmod.equals("VEN_DEV")) // Si no es devolucion
        	  {
        		  if(!getSesion(request).getPermiso(idmod + "_AGREGAR"))
            	  {
            		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod + "_AGREGAR");
                      getSesion(request).setID_Mensaje(idmensaje, mensaje);
                      RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod + "_AGREGAR",idmod4 + "||||",mensaje);
                      irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                      return;
            	  }
              }
        	  else
        	  {	  
        		  if(!getSesion(request).getPermiso("VEN_DEV_DEVOLVER") && !getSesion(request).getPermiso("VEN_DEV_REBAJAR"))
        		  {
        			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_DEV_DEVOLVER") + " / " + MsjPermisoDenegado(request, "CEF", "VEN_DEV_REBAJAR");
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_DEV_DEVOLVER","VDEV||||",mensaje);
        			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			  return;
        		  }
        	  }
        	  
        	  if(request.getParameter("ID") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("ID");
        		  if(valoresParam.length == 1)
        		  {
        			  if(setids.getAbsRow(0).getCFD() == false)
        			  {
        				  idmensaje = 1;
        				  mensaje += "PRECAUCION: Esta entidad de venta no est&aacute; establecida como CFDI. No se puede sellar el registro<br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
            	  
        			  if(moddes.equals("FACTURAS"))
        			  {
        				  JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
        				  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
        				  SetMod.Open();
	            	
        				  if(SetMod.getAbsRow(0).getTFD() == 3 || SetMod.getAbsRow(0).getStatus().equals("C"))
        				  {
        					  idmensaje = 1;
        					  mensaje += "PRECAUCION: Este registro ya está sellado o cancelado <br>";
        					  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        					  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        					  return;
        				  } 
            		       
        				  StringBuffer sb_mensaje = new StringBuffer(254);
        				  idmensaje = generarCFDI(request, response, "FACTURAS", Integer.parseInt(request.getParameter("ID")), Long.toString(SetMod.getAbsRow(0).getID_Cliente()), setids, SetMod.getAbsRow(0).getTFD(), sb_mensaje);
        				  mensaje = sb_mensaje.toString();

        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
        				  return;
        			  }
        			  else if(moddes.equals("REMISIONES"))
        			  {
        				  JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
        				  SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
	            		  SetMod.Open();
		            	
	            		  if(SetMod.getAbsRow(0).getTFD() == 3 || SetMod.getAbsRow(0).getStatus().equals("C"))
	            		  {
	            			  idmensaje = 1;
	            			  mensaje += "PRECAUCION: Esta remisi&oacute;n ya est&aacute; sellada o cancelada<br>";
	            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            			  return;
		              	  } 
	            		  	                
	            		  StringBuffer sb_mensaje = new StringBuffer(254);
        				  idmensaje = generarCFDI(request, response, "REMISIONES", Integer.parseInt(request.getParameter("ID")), Long.toString(SetMod.getAbsRow(0).getID_Cliente()), setids, SetMod.getAbsRow(0).getTFD(), sb_mensaje);
	               	   	  mensaje = sb_mensaje.toString();
	               	   	  
	               	   	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	               	   	  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
	               	   	  return;
        			  }
        			  else if(moddes.equals("DEVOLUCIONES"))
        			  {
        				  JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
        				  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
	            		  SetMod.Open();
		            	
	            		  if(SetMod.getAbsRow(0).getTFD() == 3 || SetMod.getAbsRow(0).getStatus().equals("C"))
	            		  {
	            			  idmensaje = 1;
	            			  mensaje += "PRECAUCION: Este registro ya esta sellado o cancelada<br>";
	            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            			  return;
		              	  } 
	            		  	                
	            		  StringBuffer sb_mensaje = new StringBuffer(254);
        				  idmensaje = generarCFDI(request, response, "DEVOLUCIONES", Integer.parseInt(request.getParameter("ID")), Long.toString(SetMod.getAbsRow(0).getID_Cliente()), setids, SetMod.getAbsRow(0).getTFD(), sb_mensaje);
	               	   	  mensaje = sb_mensaje.toString();
	               	   	  
	               	   	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	               	   	  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
	               	   	  return;
        			  }
        			  else
        				  return;
        		  }
        		  else
        		  {
        			  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			  return;
        		  }
        	  }
        	  else
        	  {
        		  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
          }
          else if(request.getParameter("proceso").equals("XML_VENTA"))
          {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso(idmod))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod,idmod4 + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(moddes.equals("FACTURAS"))
                  {
            		  JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
            		  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta factura no est&aacute; completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		   
            		  JCFDCompSet cfd = new JCFDCompSet(request,"VENTAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre, destino;
            		  
            		  if(cfd.getNumRows() > 0)
            		  	  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
            		  else // Es CFDI generado internamente
            		  {
            			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
            				  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/CANCEL_FAC-" + request.getParameter("ID") + ".xml";
            			  else
            				  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/SIGN_FAC-" + request.getParameter("ID") + ".xml";
            		  }
            		  
            		  destino = "FAC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".xml";
            		  
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La factura se bajo satisfactoriamente";
            		  return;
                  }
            	  else if(moddes.equals("REMISIONES"))
                  {
            		  JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
            		  SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta remisi&oacute;n no est&aacute; completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"VENTAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre, destino;
            		  
            		  if(cfd.getNumRows() > 0)
            		  	  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
            		  else
            		  {
            			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
            				  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/CANCEL_REM-" + request.getParameter("ID") + ".xml";
            			  else
            				  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/SIGN_REM-" + request.getParameter("ID") + ".xml";
            		  }
            		  
            		  destino = "REM-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".xml";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La remisi&oacute;n se bajo satisfactoriamente";
            		  return;
                  }
            	  else if(moddes.equals("DEVOLUCIONES"))
                  {
            		  JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
            		  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta dev no est&aacute; completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		   
            		  JCFDCompSet cfd = new JCFDCompSet(request,"VENTAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre, destino;
            		  
            		  if(cfd.getNumRows() > 0)
            		  	  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
            		  else
            		  {
            			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
            				  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/CANCEL_DSV-" + request.getParameter("ID") + ".xml";
            			  else
            				  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/SIGN_DSV-" + request.getParameter("ID") + ".xml";
            		  }
            		  
            		  destino = "DSV-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".xml";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La devolucion se bajo satisfactoriamente";
            		  return;
                  }
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
            }
           
        }
        else if(request.getParameter("proceso").equals("ENVIAR_VENTA"))
        {
              // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso(idmod))
        	{
              	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
              	getSesion(request).setID_Mensaje(idmensaje, mensaje);
              	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod,idmod4 + "||||",mensaje);
              	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              	return;
        	}
                          
        	if(request.getParameter("ID") != null)
        	{
                String[] valoresParam = request.getParameterValues("ID");
                if(valoresParam.length == 1)
                {
              	  if(moddes.equals("FACTURAS"))
              	  {
              		  JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
              		  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
              		  SetMod.Open();
	  	            	
              		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_Cliente() == 0)
              		  {
              			  idmensaje = 1;
              			  mensaje += "PRECAUCION: Esta factura no est&aacute; completamente sellada, o es de mostrador. No hay nada que enviar <br>";
              			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              			  return;
              		  } 
	              		   
              		  JClientClientSetV2 set = new JClientClientSetV2(request);
              		  set.m_Where = "Clave = '" + SetMod.getAbsRow(0).getID_Cliente() + "'";
              		  set.Open();
              		  if(set.getAbsRow(0).getSMTP() == 0) // Maneja smtp manual o automático
              		  {
              			  idmensaje = 1;
              			  mensaje += "PRECAUCION: Este cliente no esta confgurado para recibir sus cfdi por correo <br>";
              			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              			  return;
              		  }
	          		  	
              		  JFsiSMTPClient smtp = new JFsiSMTPClient();
              		  smtp.enviarCFDIMPE(request, "FAC", request.getParameter("ID"), "", set.getAbsRow(0).getNombre(), set.getAbsRow(0).getEMail());
              		  if(smtp.getStatusSMTP() == JFsiSMTPClient.ERROR)
              		  {
              			  idmensaje = 3;
              			  mensaje += "ERROR: " + smtp.getError();
              			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              			  return;
              		  }
	          		  	      		  
              		  getSesion(request).setID_Mensaje((short)0, "El correo se ha mandado satisfactoriamente");
              		  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
              		  return;
              	  }
              	  else if(moddes.equals("REMISIONES"))
              	  {
              		  JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
              		  SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
              		  SetMod.Open();
  	            	
              		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_Cliente() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta remision no est&aacute; completamente sellada, o es de mostrador. No hay nada que enviar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
	              		   
            		  JClientClientSetV2 set = new JClientClientSetV2(request);
            		  set.m_Where = "Clave = '" + SetMod.getAbsRow(0).getID_Cliente() + "'";
            		  set.Open();
            		  if(set.getAbsRow(0).getSMTP() == 0) // Maneja smtp manual o automático
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Este cliente no esta confgurado para recibir sus cfdi por correo <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  }
	          		  	
            		  JFsiSMTPClient smtp = new JFsiSMTPClient();
            		  smtp.enviarCFDIMPE(request, "REM", request.getParameter("ID"), "", set.getAbsRow(0).getNombre(), set.getAbsRow(0).getEMail());
            		  if(smtp.getStatusSMTP() == JFsiSMTPClient.ERROR)
            		  {
            			  idmensaje = 3;
            			  mensaje += "ERROR: " + smtp.getError();
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  }
	          		  	      		  
            		  getSesion(request).setID_Mensaje((short)0, "El correo se ha mandado satisfactoriamente");
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
            		  return;
              	  }
              	  else if(moddes.equals("DEVOLUCIONES"))
              	  {
              		  JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
              		  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
              		  SetMod.Open();
  	            	
              		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_Cliente() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta devolucion/rebaja no est&aacute; completamente sellada, o es de mostrador. No hay nada que enviar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
	              		   
            		  JClientClientSetV2 set = new JClientClientSetV2(request);
            		  set.m_Where = "Clave = '" + SetMod.getAbsRow(0).getID_Cliente() + "'";
            		  set.Open();
            		  if(set.getAbsRow(0).getSMTP() == 0) // Maneja smtp manual o automático
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Este cliente no esta confgurado para recibir sus cfdi por correo <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  }
	          		  	
            		  JFsiSMTPClient smtp = new JFsiSMTPClient();
            		  smtp.enviarCFDIMPE(request, "DSV", request.getParameter("ID"), "", set.getAbsRow(0).getNombre(), set.getAbsRow(0).getEMail());
            		  if(smtp.getStatusSMTP() == JFsiSMTPClient.ERROR)
            		  {
            			  idmensaje = 3;
            			  mensaje += "ERROR: " + smtp.getError();
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  }
	          		  	      		  
            		  getSesion(request).setID_Mensaje((short)0, "El correo se ha mandado satisfactoriamente");
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
            		  return;                    }
                }
                else
                {
              	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
              	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              	  return;
                }
              }
              else
              {
              	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          		return;
              }
             
        }
        else if(request.getParameter("proceso").equals("PDF_VENTA"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso(idmod))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod,idmod4 + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
                        
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(moddes.equals("FACTURAS"))
                  {
            		  JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
            		  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta factura no esta completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"VENTAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre, destino;
            		  
            		  if(cfd.getNumRows() > 0)
            		  	  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
            		  else
            			  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/PDFs/FAC-" + request.getParameter("ID") + ".pdf";
            		  
            		  destino = "FAC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La factura se bajo satisfactoriamente";
            		  return;
                  }
            	  else if(moddes.equals("REMISIONES"))
                  {
            		  JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
            		  SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta remisi&oacute;n no est&aacute; completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"VENTAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre, destino;
            		  
            		  if(cfd.getNumRows() > 0)
            		  	  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
            		  else
            			  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/PDFs/REM-" + request.getParameter("ID") + ".pdf";
            		  
            		  destino = "REM-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La remisi&oacute;n se bajo satisfactoriamente";
            		  return;
                  }
            	  else if(moddes.equals("DEVOLUCIONES"))
                  {
            		  JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
            		  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta devolucion no esta completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"VENTAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre, destino;
            		  
            		  if(cfd.getNumRows() > 0)
            		  	  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
            		  else
            			  nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/PDFs/DSV-" + request.getParameter("ID") + ".pdf";
            		
            		  destino = "DSV-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La devolucion se bajo satisfactoriamente";
            		  return;
                  }
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
            }
           
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_VENTA"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso(idmod + "_CAMBIAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod + "_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod + "_CAMBIAR",idmod4 + "||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
                   	
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
            	if(request.getParameter("ID") != null)
                {
            		String[] valoresParam = request.getParameterValues("ID");
            		if(valoresParam.length == 1)
            		{	
            			if(moddes.equals("PEDIDOS"))
            			{
            				JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
            				SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
      	            	
            				if(SetMod.getAbsRow(0).getStatus().equals("C"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Este pedido ya esta cancelado, no se puede cambiar <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 
		
		      	      		if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Este pedido ya tiene una factura asociada, no se puede cambiar <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 

            			}
            			else if(moddes.equals("COTIZACIONES"))
            			{
            				JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
            				SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
      	            	
            				if(SetMod.getAbsRow(0).getStatus().equals("C"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya esta cancelada, no se puede cambiar <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 
		
		      	      		if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya tiene un pedido, remisi&oacute;n o factura asociada, no se puede cambiar <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 

            			}
            			else // sale si no es ni pedido ni cotizacion porque seria factura o remision ( se estaria violando la seguridad )
            				return;
                	  
            			HttpSession ses = request.getSession(true);
            			JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
            			if(rec == null)
            			{
            				rec = new JVenFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            				ses.setAttribute("ven_fact_dlg", rec);
            			}
            			else
            			{
            				rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            			}
                    
            			// Llena el pedido o cotizacion
            			if(moddes.equals("PEDIDOS"))
            			{
            				JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
            				SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
    	            	
	    	            	// checa si se permite la 
	    	            	rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
	    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
	                    }
            			else if(moddes.equals("COTIZACIONES"))
            			{
	    	            	JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
	    	            	SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
	    	            	SetMod.Open();
	    	            	
	    	            	// checa si se permite la 
	    	            	rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
	    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
	                    } 
            			JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,request.getParameter("tipomov"));
            			JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,request.getParameter("tipomov"));
            			SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetCab.Open();
            			SetDet.Open();
    	            	
            			rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
            			rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
	    	            rec.setColonia(SetCab.getAbsRow(0).getColonia());
	    	            rec.setForma_Pago( ( (SetCab.getAbsRow(0).getCondicion() == 0) ? "contado" : "credito" ));
	    	            rec.setCP(SetCab.getAbsRow(0).getCP());
	    	            rec.setDescuento(SetCab.getAbsRow(0).getDescuento());
	    	            rec.setDireccion(SetCab.getAbsRow(0).getDireccion());
	    	            rec.setImporte(SetCab.getAbsRow(0).getImporte());
	    	            rec.setIVA(SetCab.getAbsRow(0).getIVA());
	    	            rec.setIEPS(SetCab.getAbsRow(0).getIEPS());
	    	            rec.setIVARet(SetCab.getAbsRow(0).getIVARet());
	    	            rec.setISRRet(SetCab.getAbsRow(0).getISRRet());
	    	            rec.setObs(SetCab.getAbsRow(0).getObs());
	    	            rec.setPoblacion(SetCab.getAbsRow(0).getPoblacion());
	    	            rec.setRFC(SetCab.getAbsRow(0).getRFC());
	    	            rec.setSubTotal(SetCab.getAbsRow(0).getSubTotal());
	    	            rec.setTels(SetCab.getAbsRow(0).getTel());
	    	            rec.setID_Bodega(SetCab.getAbsRow(0).getID_Bodega());
	    	            rec.setBodegaDesc(SetCab.getAbsRow(0).getNombre());
	    	            	
	    	            for(int i = 0; i< SetDet.getNumRows(); i++)
	    	            {
	    	            	rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getID_UnidadSalida(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), 
	    	            			SetDet.getAbsRow(i).getPrecio(), SetDet.getAbsRow(i).getImporte(), SetDet.getAbsRow(i).getDescuento(), SetDet.getAbsRow(i).getIVA(), SetDet.getAbsRow(i).getIEPS(), SetDet.getAbsRow(i).getIVARet(), SetDet.getAbsRow(i).getISRRet(), 
	    	            			SetDet.getAbsRow(i).getImporteDesc(), SetDet.getAbsRow(i).getImporteIVA(), SetDet.getAbsRow(i).getImporteIEPS(), SetDet.getAbsRow(i).getImporteIVARet(), SetDet.getAbsRow(i).getImporteISRRet(), SetDet.getAbsRow(i).getTotalPart(), SetDet.getAbsRow(i).getObs(), SetDet.getAbsRow(i).getID_Tipo());
	    	            }
	               	
	    	            getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	            irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
	    	            return;
                  }
                  else
                  {
                	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                	  return;    
                  }
              }
              else
              {
              	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
              	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              	  return;    
              }              
            }
            else
            {
            	// Solicitud de envio a procesar
          	  	if(request.getParameter("subproceso").equals("ENVIAR"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametros(request, response))
          	  			{
          	  				// establece los atributos por default para ventas de crédito
          	  				request.setAttribute("fsipg_cambio", 0F);
          	  				request.setAttribute("fsipg_efectivo", 0F);
          	  				request.setAttribute("fsipg_bancos", 0F);      						  
          	  				Cambiar(request, response, moddes, idmod, idmod4);
          	  				return;
          	  			}
    
          	  		}
  	     		  	 
          	  		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("AGR_CLIENT"))
          	  	{
          	  		AgregarCabecero(request,response);
          	  		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("AGR_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartida(request, response))
          	  				AgregarPartida(request, response);
          	  		}
          	  		
          	  		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("EDIT_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartida(request, response))
          	  				EditarPartida(request, response);
          	  		}
          	  		
  	       			irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
  	       			return;
          	  	}
  	   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
  	   		  {
  	   			  if(AgregarCabecero(request,response) == -1)
  	   			  {
  	   				  BorrarPartida(request, response);
  	   			  }
  	       		  
  	   			  irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
   	   			  return;
  	   		  }
  	   	  }
           
        }        
        else if(request.getParameter("proceso").equals("CONSULTAR_VENTA"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso(idmod))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod,idmod4 + "||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
            	String[] valoresParam = request.getParameterValues("ID");
            	if(valoresParam.length == 1)
            	{

            		HttpSession ses = request.getSession(true);
            		JVenFactSes	rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
            		if(rec == null)
            		{
            			rec = new JVenFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            			ses.setAttribute("ven_fact_dlg", rec);
            		}
            		else
            			rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            		
            		// Llena la factura
            		if(moddes.equals("FACTURAS"))
            		{
            			JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
            			SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getCliente());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            			rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
            			rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
            		}
            		else if(moddes.equals("PEDIDOS"))
            		{
            			JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
            			SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getCliente());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            			rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
            			rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
            		}
            		else if(moddes.equals("REMISIONES"))
            		{
            			JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
            			SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getCliente());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            			rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
            			rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
            		} 
            		else if(moddes.equals("COTIZACIONES"))
            		{
            			JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
            			SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getCliente());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            			rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
            			rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
            		}
            		else if(moddes.equals("DEVOLUCIONES"))
            		{
            			JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
            			SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getCliente());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            			rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
            			rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
            		}
                
            		
            		JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,moddes);
            		JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,moddes);
            		SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		SetCab.Open();
            		SetDet.Open();
	            	
            		rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
            		rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
            		rec.setColonia(SetCab.getAbsRow(0).getColonia());
            		rec.setForma_Pago( ( (SetCab.getAbsRow(0).getCondicion() == 0) ? "contado" : "credito" ));
            		rec.setCP(SetCab.getAbsRow(0).getCP());
            		rec.setDescuento(SetCab.getAbsRow(0).getDescuento());
            		rec.setDireccion(SetCab.getAbsRow(0).getDireccion());
            		rec.setImporte(SetCab.getAbsRow(0).getImporte());
            		rec.setIVA(SetCab.getAbsRow(0).getIVA());
            		rec.setIEPS(SetCab.getAbsRow(0).getIEPS());
    	            rec.setIVARet(SetCab.getAbsRow(0).getIVARet());
    	            rec.setISRRet(SetCab.getAbsRow(0).getISRRet());
    	            rec.setObs(SetCab.getAbsRow(0).getObs());
            		rec.setPoblacion(SetCab.getAbsRow(0).getPoblacion());
            		rec.setRFC(SetCab.getAbsRow(0).getRFC());
            		rec.setSubTotal(SetCab.getAbsRow(0).getSubTotal());
            		rec.setTels(SetCab.getAbsRow(0).getTel());
            		rec.setID_Bodega(SetCab.getAbsRow(0).getID_Bodega());
            		rec.setBodegaDesc(SetCab.getAbsRow(0).getNombre());
	            	
            		for(int i = 0; i< SetDet.getNumRows(); i++)
            		{
            			rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getID_UnidadSalida(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), 
	            			SetDet.getAbsRow(i).getPrecio(), SetDet.getAbsRow(i).getImporte(), SetDet.getAbsRow(i).getDescuento(), SetDet.getAbsRow(i).getIVA(), SetDet.getAbsRow(i).getIEPS(), SetDet.getAbsRow(i).getIVARet(), SetDet.getAbsRow(i).getISRRet(), 
	            			SetDet.getAbsRow(i).getImporteDesc(), SetDet.getAbsRow(i).getImporteIVA(), SetDet.getAbsRow(i).getImporteIEPS(), SetDet.getAbsRow(i).getImporteIVARet(), SetDet.getAbsRow(i).getImporteISRRet(), SetDet.getAbsRow(i).getTotalPart(), SetDet.getAbsRow(i).getObs(),  SetDet.getAbsRow(i).getID_Tipo());
            		}
           	
            		RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),idmod,idmod4 + "|" + request.getParameter("ID") + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||","");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
            		return;
            	}
            	else
            	{
            		idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
            	}
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
            }
           
        }
        else if(request.getParameter("proceso").equals("CANCELAR_VENTA"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso(idmod + "_CANCELAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod + "_CANCELAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod + "_CANCELAR",idmod4 + "||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(moddes.equals("COTIZACIONES"))
                  {
  	            	JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
  	                SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
  	            	SetMod.Open();
  	            	
  	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya est&aacute; cancelada <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	}
  	            	else if(SetMod.getAbsRow(0).getStatus().equals("F"))
  	          		{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya tiene un pedido, remisi&oacute;n o factura asociada, no se puede cancelar. Primero debes cancelar el pedido, remisi&oacute;n o factura para poder cancelar la cotizaci&oacute;n <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	            		return;
  	            	} 
  	                else
  	                {
  	                   CancelarFactura(request, response, "COTIZACIONES", idmod, idmod4, new StringBuffer());
  	                   return;
  	                }
                  }
            	  else if(moddes.equals("PEDIDOS"))
                  {
  	            	JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
  	                SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
  	            	SetMod.Open();
  	            	
  	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Este pedido ya está cancelado <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	}
  	            	else if(SetMod.getAbsRow(0).getStatus().equals("F"))
  	          		{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Este pedido ya tiene una factura o remisión asociada, no se puede cancelar. Primero debes cancelar la factura o remision para poder cancelar el pedido <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	            		return;
  	            	} 
  	                else
  	                {
  	                   CancelarFactura(request, response, "PEDIDOS", idmod, idmod4, new StringBuffer());
  	                   return;
  	                }
                  }
            	  else if(moddes.equals("REMISIONES"))
                  {
  	            	JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
  	                SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
  	            	SetMod.Open();
  	            	
  	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta remisi&oacute;n ya esta cancelada <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	} 
  	            	else if(SetMod.getAbsRow(0).getID_Factura() != 0)
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta remisi&oacute;n ya tiene una factura asociada. No se puede cancelar, primero cancela la factura para poder cancelar la remisión <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	} 
  	            	else if( !setids.getAbsRow(0).getFijaCost() && setids.getAbsRow(0).getAuditarAlm() && !SetMod.getAbsRow(0).getStatus().equals("R") )
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta remisi&oacute;n necesita estar revertida desde el m&oacute;dulo del almac&eacute;n para poder cancelarla <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	            		return;
  	            	} 
  	                else
  	                {
  	                	StringBuffer sb_mensaje = new StringBuffer();
	                	int idms = cancelarCFDI(request, response, "REMISIONES", Integer.parseInt(request.getParameter("ID")), SetMod.getAbsRow(0).getTFD(), sb_mensaje);
	            		if(idms == JForsetiCFD.ERROR) // quiere decir algun tipo de error de cfd
	            		{
	            			idmensaje = 3; mensaje += sb_mensaje.toString();
	            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            			return;
	            		}
	            		else
	            		{
	            			CancelarFactura(request, response, "REMISIONES", idmod, idmod4, sb_mensaje);
	            			return;
	            		}
  	                   
  	                }
                  }
            	  if(moddes.equals("FACTURAS"))
                  {
	            	JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
	                SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	            	SetMod.Open();
	            	JVentasRemisionesSet SetRem = new JVentasRemisionesSet(request);
	                SetRem.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	            	SetRem.Open();
	            	JVentasDevolucionesSet SetDev = new JVentasDevolucionesSet(request);
  	                SetDev.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
  	            	SetDev.Open();
  	            	
  	            	if(SetDev.getNumRows() > 0)
  	            	{
  	            		for(int i = 0; i < SetDev.getNumRows(); i++)
  	            		{
  	            			if(!SetDev.getAbsRow(i).getStatus().equals("C"))
  	            			{
  	            				idmensaje = 1;
  	            				mensaje += "PRECAUCION: Esta factura tiene devoluciones asociadas sin cancelar. Primero debes cancelar las devoluciones asociadas para poder cancelar la factura <br>";
  	            				getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	            				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	            				return;
  	            			}
  	            		}
  	            	}
	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
	            	{
	                    idmensaje = 1;
	                    mensaje += "PRECAUCION: Esta factura ya está cancelada <br>";
	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	                    return;
	              	} 
	            	else if(SetRem.getNumRows() == 0 && SetMod.getAbsRow(0).getID_PolCost() != -1 && !setids.getAbsRow(0).getFijaCost() && setids.getAbsRow(0).getAuditarAlm() && !SetMod.getAbsRow(0).getStatus().equals("R") )
	            	{
	                    idmensaje = 1;
	                    mensaje += "PRECAUCION: Esta factura necesita estar revertida desde el módulo del almacén para poder cancelarla <br>";
	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            		return;
	            	} 
	                else
	                {
	                	StringBuffer sb_mensaje = new StringBuffer();
	                	int idms = cancelarCFDI(request, response, "FACTURAS", Integer.parseInt(request.getParameter("ID")), SetMod.getAbsRow(0).getTFD(), sb_mensaje);
	            		if(idms == JForsetiCFD.ERROR) // quiere decir algun tipo de error de cfd
	            		{
	            			idmensaje = 3; mensaje += sb_mensaje.toString();
	            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            			return;
	            		}
	            		else
	            		{
	            			CancelarFactura(request, response, "FACTURAS", idmod, idmod4, sb_mensaje);
	            			return;
	            		}
	                }
                  }
            	  else if(moddes.equals("DEVOLUCIONES"))
            	  {
            		JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
  	                SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
  	            	SetMod.Open();
  	            	
  	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta devoluci&oacute;n ya est&aacute; cancelada <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	} 
  	            	else if(SetMod.getAbsRow(0).getDevReb().equals("DEV") && !setids.getAbsRow(0).getFijaCost() && setids.getAbsRow(0).getAuditarAlm() && !SetMod.getAbsRow(0).getStatus().equals("R") )
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta devoluci&oacute;n necesita estar revertida desde el módulo del almacén para poder cancelarla <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	            		return;
  	            	} 
  	                else
  	                {
  	                	StringBuffer sb_mensaje = new StringBuffer();
	                	int idms = cancelarCFDI(request, response, "DEVOLUCIONES", Integer.parseInt(request.getParameter("ID")), SetMod.getAbsRow(0).getTFD(), sb_mensaje);
	            		if(idms == JForsetiCFD.ERROR) // quiere decir algun tipo de error de cfd
	            		{
	            			idmensaje = 3; mensaje += sb_mensaje.toString();
	            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            			return;
	            		}
	            		else
	            		{
	            			CancelarFactura(request, response, "DEVOLUCIONES", idmod, idmod4, sb_mensaje);
	            			return;
	            		}
	            	}
            	  
            	  }
            	    
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
            }
           
        }
        else if(request.getParameter("proceso").equals("FACTURAR_VENTA"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_FAC_AGREGAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_FAC_AGREGAR");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_FAC_AGREGAR","VFAC||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
         	
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR, abre la ventana del proceso de FACTURADO por primera vez
            {
            	if(request.getParameter("ID") != null)
                {
            		String[] valoresParam = request.getParameterValues("ID");
            		if(valoresParam.length == 1)
            		{
            			if(moddes.equals("PEDIDOS"))
            			{
            				JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
            				SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
      	            	
	      	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
			      	      	{
			      	      		idmensaje = 1;
			                    mensaje += "PRECAUCION: Este pedido ya esta cancelado, no se puede facturar <br>";
			                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			                    return;
			      	      	} 
			
			      	      	if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
			      	      	{
			      	      		idmensaje = 1;
			                    mensaje += "PRECAUCION: Este pedido ya tiene una remisi&oacute;n o factura asociada, no se puede facturar <br>";
			                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			                    return;
			      	      	} 

	                    }
	                	else if(moddes.equals("REMISIONES"))
	                    {
	      	            	JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
	      	            	SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
	      	            	SetMod.Open();
	      	            	
	      	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
			      	      	{
			      	      		idmensaje = 1;
			                    mensaje += "PRECAUCION: Esta remisi&oacute;n ya esta cancelada, no se puede facturar <br>";
			                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			                    return;
			      	      	} 
			
			      	      	if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("R") || SetMod.getAbsRow(0).getFactura() != 0)
			      	      	{
			      	      		idmensaje = 1;
			                    mensaje += "PRECAUCION: Esta remisi&oacute;n ya tiene una factura asociada o est&aacute; revertida, no se puede facturar <br>";
			                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			                    return;
			      	      	} 
	
	                    }
	                	else if(request.getParameter("tipomov").equals("COTIZACIONES"))
	                    {
	      	            	JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
	      	            	SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
	      	            	SetMod.Open();
	      	            	
	      	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
			      	      	{
			      	      		idmensaje = 1;
			                    mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya esta cancelada, no se puede facturar <br>";
			                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			                    return;
			      	      	} 
			
			      	      	if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
			      	      	{
			      	      		idmensaje = 1;
			                    mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya tiene un pedido, remisi&oacute;n o factura asociada, no se puede facturar <br>";
			                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			                    return;
			      	      	} 
	
	                    }
	                	else // sale si no es ni pedido ni cotizacion porque seria factura ( se estaria violando la seguridad )
	                		return;
	                	  
            			HttpSession ses = request.getSession(true);
            			JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
            			if(rec == null)
            			{
            				rec = new JVenFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "FACTURAS");
            				ses.setAttribute("ven_fact_dlg", rec);
            			}
            			else
            			{
            				rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "FACTURAS");
            			}
                    
            			// Llena la factura
            			if(moddes.equals("PEDIDOS"))
            			{
            				JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
            				SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
	    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
	                    }
	                    else if(moddes.equals("REMISIONES"))
	                    {
	    	            	JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
	    	            	SetMod.m_Where = "ID_Remision = '" + p(request.getParameter("ID")) + "'";
	    	            	SetMod.Open();
	    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
	    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
	                    }
	                    else if(moddes.equals("COTIZACIONES"))
	                    {
	    	            	JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
	    	            	SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
	    	            	SetMod.Open();
	    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
	    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
	                    }
	    	            JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,request.getParameter("tipomov"));
	    	            JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,request.getParameter("tipomov"));
	    	            SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetCab.Open();
	    	            SetDet.Open();
	    	            	
	    	            rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
	    	            rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
	    	            rec.setColonia(SetCab.getAbsRow(0).getColonia());
	    	            rec.setForma_Pago( ( (SetCab.getAbsRow(0).getCondicion() == 0) ? "contado" : "credito" ));
	    	            rec.setCP(SetCab.getAbsRow(0).getCP());
	    	            rec.setDescuento(SetCab.getAbsRow(0).getDescuento());
	    	            rec.setDireccion(SetCab.getAbsRow(0).getDireccion());
	    	            rec.setImporte(SetCab.getAbsRow(0).getImporte());
	    	            rec.setIVA(SetCab.getAbsRow(0).getIVA());
	    	            rec.setIEPS(SetCab.getAbsRow(0).getIEPS());
	    	            rec.setIVARet(SetCab.getAbsRow(0).getIVARet());
	    	            rec.setISRRet(SetCab.getAbsRow(0).getISRRet());
	    	            rec.setPoblacion(SetCab.getAbsRow(0).getPoblacion());
	    	            rec.setRFC(SetCab.getAbsRow(0).getRFC());
	    	            rec.setSubTotal(SetCab.getAbsRow(0).getSubTotal());
	    	            rec.setTels(SetCab.getAbsRow(0).getTel());
	    	            rec.setID_Bodega(SetCab.getAbsRow(0).getID_Bodega());
	    	            rec.setBodegaDesc(SetCab.getAbsRow(0).getNombre());
	    	            	
	    	            for(int i = 0; i< SetDet.getNumRows(); i++)
	    	            {
	    	            	rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getID_UnidadSalida(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), 
	    	            			SetDet.getAbsRow(i).getPrecio(), SetDet.getAbsRow(i).getImporte(), SetDet.getAbsRow(i).getDescuento(), SetDet.getAbsRow(i).getIVA(), SetDet.getAbsRow(i).getIEPS(), SetDet.getAbsRow(i).getIVARet(), SetDet.getAbsRow(i).getISRRet(), 
	    	            			SetDet.getAbsRow(i).getImporteDesc(), SetDet.getAbsRow(i).getImporteIVA(), SetDet.getAbsRow(i).getImporteIEPS(), SetDet.getAbsRow(i).getImporteIVARet(), SetDet.getAbsRow(i).getImporteISRRet(), SetDet.getAbsRow(i).getTotalPart(), SetDet.getAbsRow(i).getObs(), SetDet.getAbsRow(i).getID_Tipo());
	    	            }
	               	
	                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
	                    return;
            		}
            		else
            		{
            			idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                		getSesion(request).setID_Mensaje(idmensaje, mensaje);
                		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                		return;
            		}
                }
                else
                {
                	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
                }              
            }
            else
            {
            	// Solicitud de envio a procesar
  	       	  	if(request.getParameter("subproceso").equals("ENVIAR"))
  	       	  	{
  	       	  		if(moddes.equals("PEDIDOS") || 
  	       				  moddes.equals("COTIZACIONES") ||
  	       				  		moddes.equals("REMISIONES") )
  	       	  		{
  	       	  			if(request.getParameter("fecha") == null  || request.getParameter("referencia") == null || 
  	       	  					request.getParameter("fecha").equals("") )
  	       	  			{
  	       	  				idmensaje = 1; mensaje += "PRECAUCION: Se debe enviar la fecha y referencia de la factura <br>";
  	       	  				getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	       	  				irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
  	       	  				return;
  	       	  			}
  	       	  			else if(request.getParameter("forma_pago").equals("contado"))
  	       	  			{
  	       	  				HttpSession ses = request.getSession(true);
  	       	  				JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");	
         			  
  	       	  				request.setAttribute("fsipg_tipo","ventas");
  	       	  				request.setAttribute("fsipg_proc", "deposito");
  	       	  				request.setAttribute("fsipg_total",rec.getTotal());
  	       	  				request.setAttribute("fsipg_ident",getSesion(request).getSesion(idmod).getEspecial());
  	       				
  	       	  				if(VerificarParametros(request, response) && VerificarPagoMult(request, response))
  	       	  				{
  	       	  					AgregarDesde(request, response, "VFAC", "VEN_FAC", request.getParameter("ID"), idmod4, idmod, setids);
  	       	  					return;
  	       	  				}
  	       	  				irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
	       	  				return;
  	       	  			}
  	       	  			else
  	       	  			{
  	       	  				if(VerificarParametros(request, response))
  	       	  				{
  	       	  					// establece los atributos por default para ventas de crédito
  	       	  					request.setAttribute("fsipg_cambio", 0F);
  	       	  					request.setAttribute("fsipg_efectivo", 0F);
  	       	  					request.setAttribute("fsipg_bancos", 0F);		
  	       	  					AgregarDesde(request, response, "VFAC", "VEN_FACT", request.getParameter("ID"), idmod4, idmod, setids);
  	       	  					return;
  	       	  				}
  	       	  				irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
  	       	  				return;
  	       	  			}
  	       	  		}
  	       		 
  	       	  	}
            }
           
        } 
        else if(request.getParameter("proceso").equals("REMISIONAR_VENTA"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_REM_AGREGAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_REM_AGREGAR");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_REM_AGREGAR","VREM||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
         	
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR, abre la ventana del proceso de FACTURADO por primera vez
            {
            	if(request.getParameter("ID") != null)
                {
            		String[] valoresParam = request.getParameterValues("ID");
            		if(valoresParam.length == 1)
            		{
            			if(moddes.equals("PEDIDOS"))
            			{
            				JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
            				SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
      	            	
            				if(SetMod.getAbsRow(0).getStatus().equals("C"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Este pedido ya esta cancelado, no se puede remisionar <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		}	 
		
            				if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Este pedido ya tiene una factura o remisi&oacute;n asociada, no se puede a remisionar <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 

            			}
            			else if(moddes.equals("COTIZACIONES"))
            			{
            				JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
            				SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
      	            	
            				if(SetMod.getAbsRow(0).getStatus().equals("C"))
		      	      		{
            					idmensaje = 1;
            					mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya esta cancelada, no se puede remisionar <br>";
            					getSesion(request).setID_Mensaje(idmensaje, mensaje);
            					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            					return;
		      	      		} 
		
            				if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya tiene un pedido, remisi&oacute;n o factura asociada, no se puede remisionar <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 

            			}
            			else // sale si no es ni pedido ni cotizacion porque seria factura ( se estaria violando la seguridad )
            				return;
                	  
            			HttpSession ses = request.getSession(true);
            			JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
	                    if(rec == null)
	                    {
	        	            rec = new JVenFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "REMISIONES");
	        	            ses.setAttribute("ven_fact_dlg", rec);
	                    }
	                    else
	                    {
	                        rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "REMISIONES");
	                    }
	                    
	                    // Llena la factura
	                    if(moddes.equals("PEDIDOS"))
	                    {
	    	            	JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
	    	            	SetMod.m_Where = "ID_Pedido = '" + p(request.getParameter("ID")) + "'";
	    	            	SetMod.Open();
	    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
	    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
	                    }
	                    else if(request.getParameter("tipomov").equals("COTIZACIONES"))
	                    {
	    	            	JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
	    	            	SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
	    	            	SetMod.Open();
	    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
	    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
	                    }
	    	            JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,request.getParameter("tipomov"));
	    	            JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,request.getParameter("tipomov"));
	    	            SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetCab.Open();
	    	            SetDet.Open();
	    	            
	    	            rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
	    	            rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
	    	            rec.setColonia(SetCab.getAbsRow(0).getColonia());
	    	            rec.setForma_Pago( ( (SetCab.getAbsRow(0).getCondicion() == 0) ? "contado" : "credito" ));
	    	            rec.setCP(SetCab.getAbsRow(0).getCP());
	    	            rec.setDescuento(SetCab.getAbsRow(0).getDescuento());
	    	            rec.setDireccion(SetCab.getAbsRow(0).getDireccion());
	    	            rec.setImporte(SetCab.getAbsRow(0).getImporte());
	    	            rec.setIVA(SetCab.getAbsRow(0).getIVA());
	    	            rec.setIEPS(SetCab.getAbsRow(0).getIEPS());
	    	            rec.setIVARet(SetCab.getAbsRow(0).getIVARet());
	    	            rec.setISRRet(SetCab.getAbsRow(0).getISRRet());
	    	            rec.setObs(SetCab.getAbsRow(0).getObs());
	    	            rec.setPoblacion(SetCab.getAbsRow(0).getPoblacion());
	    	            rec.setRFC(SetCab.getAbsRow(0).getRFC());
	    	            rec.setSubTotal(SetCab.getAbsRow(0).getSubTotal());
	    	            rec.setTels(SetCab.getAbsRow(0).getTel());
	    	            rec.setID_Bodega(SetCab.getAbsRow(0).getID_Bodega());
	    	            rec.setBodegaDesc(SetCab.getAbsRow(0).getNombre());
	    	            	
	    	            for(int i = 0; i< SetDet.getNumRows(); i++)
	    	            {
	    	            	rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getID_UnidadSalida(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), 
	    	            			SetDet.getAbsRow(i).getPrecio(), SetDet.getAbsRow(i).getImporte(), SetDet.getAbsRow(i).getDescuento(), SetDet.getAbsRow(i).getIVA(), SetDet.getAbsRow(i).getIEPS(), SetDet.getAbsRow(i).getIVARet(), SetDet.getAbsRow(i).getISRRet(), 
	    	            			SetDet.getAbsRow(i).getImporteDesc(), SetDet.getAbsRow(i).getImporteIVA(), SetDet.getAbsRow(i).getImporteIEPS(), SetDet.getAbsRow(i).getImporteIVARet(), SetDet.getAbsRow(i).getImporteISRRet(), SetDet.getAbsRow(i).getTotalPart(), SetDet.getAbsRow(i).getObs(), SetDet.getAbsRow(i).getID_Tipo());
	    	            }
	               	
	                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
	                    return;
            		}
            		else
            		{
            			idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  	  	return;   
            		}
                }
                else
                {
                	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
              	  	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              	  	return;   
                }              
            }
            else
            {
  	       	  	// Solicitud de envio a procesar
  	       	  	if(request.getParameter("subproceso").equals("ENVIAR"))
  	       	  	{
  	       	  		if(moddes.equals("PEDIDOS") || moddes.equals("COTIZACIONES"))
  	       	  		{
  	       	  			if(request.getParameter("fecha") == null  || request.getParameter("referencia") == null || 
  	       	  					request.getParameter("fecha").equals("") )
  	       	  			{
  	       	  				idmensaje = 1; mensaje += "PRECAUCION: Se debe enviar la fecha y referencia de la remisi&oacute;n <br>";
  	       	  				getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	       	  				irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
  	       	  				return;
  	       	  			}
  	       	  			else
  	       	  			{
  	       	  				if(VerificarParametros(request, response))
  	       	  				{
  	       	  					AgregarDesde(request, response, "VREM", "VEN_REM", request.getParameter("ID"), idmod4, idmod, setids);
  	       	  					return;
  	       	  				}
  	       	  				irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
	       	  				return;
  	       	  			}
  	       	  		}
  	       		 }
             }
           
        }         
        else if(request.getParameter("proceso").equals("PEDIR_VENTA"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_PED_AGREGAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_PED_AGREGAR");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_PED_AGREGAR","VPED||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
         	
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR, abre la ventana del proceso de FACTURADO por primera vez
            {
            	if(request.getParameter("ID") != null)
                {
            		String[] valoresParam = request.getParameterValues("ID");
            		if(valoresParam.length == 1)
            		{
            			if(moddes.equals("COTIZACIONES"))
            			{
            				JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
            				SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
      	            	
            				if(SetMod.getAbsRow(0).getStatus().equals("C"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya esta cancelada, no se puede pedir <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 
		
		      	      		if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
		      	      		{
		      	      			idmensaje = 1;
		      	      			mensaje += "PRECAUCION: Esta cotizaci&oacute;n ya tiene un pedido, remisi&oacute;n o factura asociada, no se puede pedir <br>";
		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		      	      			return;
		      	      		} 

            			}
            			else // sale si no es cotizacion porque seria ( se estaria violando la seguridad )
            				return;
                	  
            			HttpSession ses = request.getSession(true);
            			JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
            			if(rec == null)
            			{
            				rec = new JVenFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "PEDIDOS");
            				ses.setAttribute("ven_fact_dlg", rec);
            			}
            			else
            			{
            				rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "PEDIDOS");
            			}
                    
            			// Llena el pedido
                    	JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
    	            	SetMod.m_Where = "ID_Cotizacion = '" + p(request.getParameter("ID")) + "'";
    	            	SetMod.Open();
    	            	
    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
                    
    	            	JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,moddes);
    	            	JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,moddes);
    	            	SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
    	            	SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
    	            	SetCab.Open();
    	            	SetDet.Open();
    	            	
    	            	rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
    	            	rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
    	            	rec.setColonia(SetCab.getAbsRow(0).getColonia());
    	            	rec.setForma_Pago( ( (SetCab.getAbsRow(0).getCondicion() == 0) ? "contado" : "credito" ));
    	            	rec.setCP(SetCab.getAbsRow(0).getCP());
    	            	rec.setDescuento(SetCab.getAbsRow(0).getDescuento());
    	            	rec.setDireccion(SetCab.getAbsRow(0).getDireccion());
    	            	rec.setImporte(SetCab.getAbsRow(0).getImporte());
    	            	rec.setIVA(SetCab.getAbsRow(0).getIVA());
    	            	rec.setIEPS(SetCab.getAbsRow(0).getIEPS());
	    	            rec.setIVARet(SetCab.getAbsRow(0).getIVARet());
	    	            rec.setISRRet(SetCab.getAbsRow(0).getISRRet());
	    	            rec.setObs(SetCab.getAbsRow(0).getObs());
    	            	rec.setPoblacion(SetCab.getAbsRow(0).getPoblacion());
    	            	rec.setRFC(SetCab.getAbsRow(0).getRFC());
    	            	rec.setSubTotal(SetCab.getAbsRow(0).getSubTotal());
    	            	rec.setTels(SetCab.getAbsRow(0).getTel());
    	            	rec.setID_Bodega(SetCab.getAbsRow(0).getID_Bodega());
    	            	rec.setBodegaDesc(SetCab.getAbsRow(0).getNombre());
    	            	
    	            	for(int i = 0; i< SetDet.getNumRows(); i++)
    	            	{
    	            		rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getID_UnidadSalida(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), 
    	            			SetDet.getAbsRow(i).getPrecio(), SetDet.getAbsRow(i).getImporte(), SetDet.getAbsRow(i).getDescuento(), SetDet.getAbsRow(i).getIVA(), SetDet.getAbsRow(i).getIEPS(), SetDet.getAbsRow(i).getIVARet(), SetDet.getAbsRow(i).getISRRet(), 
    	            			SetDet.getAbsRow(i).getImporteDesc(), SetDet.getAbsRow(i).getImporteIVA(), SetDet.getAbsRow(i).getImporteIEPS(), SetDet.getAbsRow(i).getImporteIVARet(), SetDet.getAbsRow(i).getImporteISRRet(), SetDet.getAbsRow(i).getTotalPart(), SetDet.getAbsRow(i).getObs(), SetDet.getAbsRow(i).getID_Tipo());
    	            	}
               	
    	            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	            	irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
    	            	return;
            		}
            		else
            		{
            			idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                		getSesion(request).setID_Mensaje(idmensaje, mensaje);
                		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                		return;
            		}
                }
                else
                {
                	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
                }              
            }
            else
            {
            	// Solicitud de envio a procesar
            	if(request.getParameter("subproceso").equals("ENVIAR"))
  	       	  	{
            		if(request.getParameter("tipomov").equals("COTIZACIONES"))
            		{
            			if(request.getParameter("fecha") == null  || request.getParameter("referencia") == null || 
            					request.getParameter("fecha").equals("") )
  	       				{
            				idmensaje = 1; mensaje += "PRECAUCION: Se debe enviar la fecha y referencia del pedido <br>";
            				getSesion(request).setID_Mensaje(idmensaje, mensaje);
            				irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
            				return;
  	       				}
  	       				else
  	       				{
  	       					if(VerificarParametros(request, response))
  	       					{
  	       						AgregarDesde(request, response, "VPED", "VEN_PED", request.getParameter("ID"), idmod4, idmod, setids);
  	       						return;
  	       					}
  	       					irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
  	       					return;
  	       				}
            		}
  	       		}
  	   	  	}
        }
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso(idmod))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod,idmod4 + "||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}

        	if(request.getParameter("ID") != null)
        	{
        		String[] valoresParam = request.getParameterValues("ID");
        		if (valoresParam.length == 1)
        		{
        			if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("IMPRESION"))
        			{
        				StringBuffer bsmensaje = new StringBuffer(254);
        				String SQLCab = "select * from ";
        				String SQLDet = "select * from "; 
        				if(moddes.equals("FACTURAS"))
        				{
        					SQLCab += "view_ventas_facturas_impcab where ID_Factura = ";
        					SQLDet += "view_ventas_facturas_impdet where ID_Factura = ";
        				}
        				else if(moddes.equals("PEDIDOS"))
        				{
        					SQLCab += "view_ventas_pedidos_impcab where ID_Pedido = ";
        					SQLDet += "view_ventas_pedidos_impdet where ID_Pedido = ";
        				}
        				else if(moddes.equals("REMISIONES"))
        				{
        					SQLCab += "view_ventas_remisiones_impcab where ID_Remision = ";
        					SQLDet += "view_ventas_remisiones_impdet where ID_Remision = ";
        				}
        				else if(moddes.equals("COTIZACIONES"))
        				{
        					SQLCab += "view_ventas_cotizaciones_impcab where ID_Cotizacion = ";
        					SQLDet += "view_ventas_cotizaciones_impdet where ID_Cotizacion = ";
        				}
        				else //Devoluciones
        				{
        					SQLCab += "view_ventas_devoluciones_impcab where ID_Devolucion = ";
        					SQLDet += "view_ventas_devoluciones_impdet where ID_Devolucion = ";
        				}
        				SQLCab += request.getParameter("ID");
        				SQLDet += request.getParameter("ID");
                
        				idmensaje = Imprimir(SQLCab, SQLDet, request.getParameter("idformato"), bsmensaje, request, response);

        				if (idmensaje != -1)
        				{
        					getSesion(request).setID_Mensaje(idmensaje, bsmensaje.toString());
        					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        					return;
        				}
        			}
        			else // significa que debe llamar a la ventana de formatos de impresion
        			{
        				if(moddes.equals("FACTURAS"))
        				{
        					request.setAttribute("impresion", "CEFVenFactDlg");
        					request.setAttribute("tipo_imp", "VEN_FAC");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFormato());
        				}
        				else if(moddes.equals("PEDIDOS"))
        				{
        					request.setAttribute("impresion", "CEFVenFactDlg");
        					request.setAttribute("tipo_imp", "VEN_PED");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Pedido());
        				}
        				else if(moddes.equals("REMISIONES"))
        				{
        					request.setAttribute("impresion", "CEFVenFactDlg");
        					request.setAttribute("tipo_imp", "VEN_REM");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Remision());
        				}
        				else if(moddes.equals("COTIZACIONES"))
        				{
        					request.setAttribute("impresion", "CEFVenFactDlg");
        					request.setAttribute("tipo_imp", "VEN_COT");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Cotizacion());
        				}
        				else //DEVOLUCIONES
        				{
        					request.setAttribute("impresion", "CEFVenFactDlg");
        					request.setAttribute("tipo_imp", "VEN_DEV");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Devolucion());
        				}
        				
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/impresion_dlg.jsp", request, response);
        				return;
        			}
        		}
        		else
        		{
        			idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
        		}
        	}
        	else
        	{
        		idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
        	}
        } 
        else if(request.getParameter("proceso").equals("DEVOLVER_VENTA") || request.getParameter("proceso").equals("REBAJAR_VENTA"))
        {
            // Revisa si tiene permisos
            if(request.getParameter("proceso").equals("DEVOLVER_VENTA") && !getSesion(request).getPermiso("VEN_DEV_DEVOLVER"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_DEV_DEVOLVER");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_DEV_DEVOLVER","VDEV||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            if(request.getParameter("proceso").equals("REBAJAR_VENTA") && !getSesion(request).getPermiso("VEN_DEV_REBAJAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_DEV_REBAJAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_DEV_REBAJAR","VDEV||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
   	
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
            	if(request.getParameter("ID") != null)
                {
            		String[] valoresParam = request.getParameterValues("ID");
            		if(valoresParam.length == 1)
            		{
            			JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
            			SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
    	            
            			if(SetMod.getAbsRow(0).getStatus().equals("C"))
            			{
            				idmensaje = 1;
            				mensaje += "PRECAUCION: Esta factura ya esta cancelada, no se puede generar la devolución <br>";
            				getSesion(request).setID_Mensaje(idmensaje, mensaje);
            				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            				return;
            			} 
		
            			if(request.getParameter("proceso").equals("DEVOLVER_VENTA"))
            			{	
	            			if(SetMod.getAbsRow(0).getID_PolCost() == -1 || (!SetMod.getAbsRow(0).getStatus().equals("E") && setids.getAbsRow(0).getAuditarAlm()) )
	            			{
	            				idmensaje = 1;
	            				mensaje += "PRECAUCION: Esta factura debe estar guardada, revertida, o ser factura sin movimiento al almacén. Solo se pueden devolver las facturas emitidas con movimientos al almacén <br>";
	            				getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            				return;
	            			}  
	            			//SetRem.getNumRows() == 0 && SetMod.getAbsRow(0).getID_PolCost() != -1 && !setids.getAbsRow(0).getFijaCost() && setids.getAbsRow(0).getAuditarAlm() && !SetMod.getAbsRow(0).getStatus().equals("R") )
	    	         	}
            			else // Rebajar venta
            			{
            				if(SetMod.getAbsRow(0).getID_PolCost() != -1 && !SetMod.getAbsRow(0).getStatus().equals("E") && setids.getAbsRow(0).getAuditarAlm() )
        	            	{
	            				idmensaje = 1;
	            				mensaje += "PRECAUCION: Esta factura debe estar guardada o revertida. Solo se pueden rebajar las facturas emitidas <br>";
	            				getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            				return;
	            			}  
            			}
            			
            			HttpSession ses = request.getSession(true);
            			JVenFactSes rec = (JVenDevSes)ses.getAttribute("ven_dev_dlg");
            			if(rec == null)
            			{
            				rec = new JVenDevSes(request, getSesion(request).getSesion("VEN_FAC").getEspecial(), usuario, "DEVOLUCIONES");
            				ses.setAttribute("ven_dev_dlg", rec);
            			}
            			else
            				rec.resetear(request, getSesion(request).getSesion("VEN_FAC").getEspecial(), usuario, "DEVOLUCIONES");
                         
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Cliente());
    	            	rec.setNombre(SetMod.getAbsRow(0).getCliente());
    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
    	            	rec.setID_Vendedor(SetMod.getAbsRow(0).getID_Vendedor());
    	            	rec.setVendedorNombre(SetMod.getAbsRow(0).getVendedorNombre());
    	            	rec.setID_Factura(SetMod.getAbsRow(0).getID_Factura());
    	            	if(request.getParameter("proceso").equals("DEVOLVER_VENTA"))
    	            		rec.setDevReb("DEV");
    	            	else
    	            		rec.setDevReb("REB");
    	            	JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"FACTURAS");
            			JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,"FACTURAS"/*"AGR_DEVOL"*/);
            			SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetCab.Open();
            			SetDet.Open();
    	            	
            			rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
    	            	rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
    	            	rec.setColonia(SetCab.getAbsRow(0).getColonia());
    	            	rec.setForma_Pago( ( (SetCab.getAbsRow(0).getCondicion() == 0) ? "contado" : "credito" ));
    	            	rec.setCP(SetCab.getAbsRow(0).getCP());
    	            	rec.setDescuento(SetCab.getAbsRow(0).getDescuento());
    	            	rec.setDireccion(SetCab.getAbsRow(0).getDireccion());
    	            	rec.setImporte(SetCab.getAbsRow(0).getImporte());
    	            	rec.setIVA(SetCab.getAbsRow(0).getIVA());
    	            	rec.setIEPS(SetCab.getAbsRow(0).getIEPS());
	    	            rec.setIVARet(SetCab.getAbsRow(0).getIVARet());
	    	            rec.setISRRet(SetCab.getAbsRow(0).getISRRet());
	    	            rec.setObs(SetCab.getAbsRow(0).getObs());
    	            	rec.setPoblacion(SetCab.getAbsRow(0).getPoblacion());
    	            	rec.setRFC(SetCab.getAbsRow(0).getRFC());
    	            	rec.setSubTotal(SetCab.getAbsRow(0).getSubTotal());
    	            	rec.setTels(SetCab.getAbsRow(0).getTel());
    	            	rec.setID_Bodega(SetCab.getAbsRow(0).getID_Bodega());
    	            	rec.setBodegaDesc(SetCab.getAbsRow(0).getNombre());
    	            	
    	            	for(int i = 0; i< SetDet.getNumRows(); i++)
	            		{
            				rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getID_UnidadSalida(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), 
	    	            		SetDet.getAbsRow(i).getPrecio(), SetDet.getAbsRow(i).getImporte(), SetDet.getAbsRow(i).getDescuento(), SetDet.getAbsRow(i).getIVA(), SetDet.getAbsRow(i).getIEPS(), SetDet.getAbsRow(i).getIVARet(), SetDet.getAbsRow(i).getISRRet(), 
	    	            		SetDet.getAbsRow(i).getImporteDesc(), SetDet.getAbsRow(i).getImporteIVA(), SetDet.getAbsRow(i).getImporteIEPS(), SetDet.getAbsRow(i).getImporteIVARet(), SetDet.getAbsRow(i).getImporteISRRet(), SetDet.getAbsRow(i).getTotalPart(), SetDet.getAbsRow(i).getObs(), SetDet.getAbsRow(i).getID_Tipo());
	            		}
        				
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
            			return;
            		}
            		else
            		{
                    	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                		return;
            		}
                }
                else
                {
                	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
                }              
            }
            else
            {
            	// Solicitud de envio a procesar
            	if(request.getParameter("subproceso").equals("ENVIAR"))
  	       	  	{
            		if(AgregarCabeceroDev(request,response) == -1)
            		{
            			HttpSession ses = request.getSession(true);
            			JVenFactSes rec = (JVenDevSes)ses.getAttribute("ven_dev_dlg");
            			
            			if(rec.getForma_Pago().equals("contado"))
       				  	{
       				  		request.setAttribute("fsipg_tipo","ventas");
       				  		request.setAttribute("fsipg_total",rec.getTotal());
       				  		request.setAttribute("fsipg_proc", "retiro");
       				  		request.setAttribute("fsipg_ident",getSesion(request).getSesion("VEN_FAC").getEspecial());
       				  		request.setAttribute("fsipg_id_concepto", 0);
       				  		request.setAttribute("fsipg_desc_concepto", "");
       				  		if(VerificarParametrosDev(request, response) && VerificarPagoMult(request, response))
       				  		{
       				  			AgregarDev(request, response, setids);
       				  			return;
       				  		}
       				  	}
            			else
                		{
           					 request.setAttribute("fsipg_tipo","ventas");
           					 if(VerificarParametrosDev(request, response) && VerificarSaldo(request, response))
           					 {
           						 AgregarDev(request, response, setids);
           						 return;
           					 }
           				}
            			
            		}
            		
            		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
      			  	return;
            		           		
  	       	  	}
            	else if(request.getParameter("subproceso").equals("EDIT_PART"))
  	   		  	{
            		if(AgregarCabeceroDev(request,response) == -1)
            		{
            			if(VerificarParametrosPartidaDev(request, response))
            				EditarPartidaDev(request, response);
            		}
  	       		  	
  	       		  	irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
  	       		  	return;
  	   		  	}
  	   		  	else if(request.getParameter("subproceso").equals("BORR_PART"))
  	   		  	{
  	   		  		if(AgregarCabeceroDev(request,response) == -1)
  	   		  		{
  	   		  			BorrarPartidaDev(request, response);
  	   		  		}
  	   		  		
  	   		  		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
  	   		  		return;
  	   		  	}
            	
            	idmensaje = 1; mensaje += "PRECAUCION: No se pueden agregar partidas a una devolucion o rebaja. Intenta editar o borrar lo que no desees devolver o rebajar"; 
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);  
	   		  	return;
            }
        }
        else if(request.getParameter("proceso").equals("RASTREAR_MOVIMIENTO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso(idmod + "_CONSULTAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod + "_CONSULTAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),idmod + "_CONSULTAR",idmod4 + "||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
        	
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if (valoresParam.length == 1)
              {
            	  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion(idmod).generarTitulo(JUtil.Msj("CEF",idmod,"VISTA","CONSULTAR_VENTA",3)),
                  								idmod4,request.getParameter("ID"));
            	  String rastreo_imp = "true";
            	  request.setAttribute("rastreo_imp", rastreo_imp);
            	  // Ahora pone los atributos para el jsp
            	  request.setAttribute("rastreo", rastreo);
            	  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),idmod + "_CONSULTAR",idmod4 + "|" + request.getParameter("ID") + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||","");
            	  irApag("/forsetiweb/rastreo_imp.jsp", request, response); 
            	  return;
              	
              }
              else
              {
                 idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador de la póliza que se quiere consultar<br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
         
        }   	
        else
        {
        	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    		return;
        }

    }
    else // si no se mandan parametros, manda a error
    {
    	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
    }

  }

  public boolean VerificarParametrosPartidaDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
  {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("cantidad") != null && 	request.getParameter("precio") != null && 
    		request.getParameter("obs_partida") != null &&
    		!request.getParameter("cantidad").equals("") && !request.getParameter("precio").equals(""))
    	{
    		return true;
    	}
    	else
    	{
    		idmensaje = 1; mensaje = "PRECAUCION: Por lo menos se deben enviar los parámetros de cantidad, y precio del producto <br>";
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    }
    
    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("cantidad") != null && request.getParameter("idprod") != null &&
         request.getParameter("precio") != null && request.getParameter("descuento") != null && 
         request.getParameter("iva") != null && request.getParameter("obs_partida") != null &&
         !request.getParameter("cantidad").equals("") && !request.getParameter("idprod").equals(""))
      {
        return true;
      }
      else
      {
          idmensaje = 1; mensaje = "PRECAUCION: Por lo menos se deben enviar los parámetros de cantidad y clave del producto <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public boolean VerificarParametrosDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
        HttpSession ses = request.getSession(true);
        JVenDevSes rec = (JVenDevSes)ses.getAttribute("ven_dev_dlg");
         
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append("PRECAUCION: La devolución no contiene partidas <br>");
   	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
        
        if(request.getParameter("proceso").equals("DEVOLVER_VENTA"))
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

        	if(idmensaje != -1)
        		return false;
        } 
        
        return true;
	
    }
    
    
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
        HttpSession ses = request.getSession(true);
        JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
         
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append("PRECAUCION: El elemento de ventas no contiene partidas <br>");
   	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
            
        if(rec.getForma_Pago().equals("credito") && rec.getClave() == 0)
        {
  	        idmensaje = 1; mensaje.append("PRECAUCION: Una venta de mostrador no se puede pagar a crédito <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
        
        if(request.getParameter("tipomov").equals("FACTURAS") || request.getParameter("tipomov").equals("REMISIONES"))
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

        	if(idmensaje != -1)
        		return false;
        }
        else if(request.getParameter("proceso").equals("FACTURAR") || request.getParameter("proceso").equals("REMISIONAR"))
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

        	if(idmensaje != -1)
        		return false;
        }
        
        return true;
	
    }

    public short AgregarCabeceroDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
       	HttpSession ses = request.getSession(true);
       	JVenFactSes rec = (JVenDevSes)ses.getAttribute("ven_dev_dlg");
 
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
     
    public short AgregarRecursos(HttpServletRequest request, HttpServletResponse response)
       	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
                      
    	HttpSession ses = request.getSession(true);
    	JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
         
    	idmensaje = rec.agregaRecursos(request, mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

    	return idmensaje;
    }
    
    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
       	HttpSession ses = request.getSession(true);
       	JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
 
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
  
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");

        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.agregaPartida(request, cantidad, request.getParameter("idprod"), request.getParameter("precio"), request.getParameter("descuento"), request.getParameter("iva"), pt(request.getParameter("obs_partida")), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   
    }

    public void EditarPartidaDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JVenDevSes rec = (JVenDevSes)ses.getAttribute("ven_dev_dlg");

      float cantidad = Float.parseFloat(request.getParameter("cantidad"));
      float precio = Float.parseFloat(request.getParameter("precio"));
      idmensaje = rec.editarPartida(Integer.parseInt(request.getParameter("idpartida")), request, cantidad, request.getParameter("idprod"), precio, request.getParameter("descuento"), request.getParameter("iva"), p(request.getParameter("obs_partida")), mensaje);
      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      
    }
    
    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");

        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, cantidad, request.getParameter("idprod"), request.getParameter("precio"), request.getParameter("descuento"), request.getParameter("iva"), pt(request.getParameter("obs_partida")), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
  
    }

    public void BorrarPartidaDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JVenFactSes rec = (JVenDevSes)ses.getAttribute("ven_dev_dlg");

      rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      //irApag("/forsetiweb/ventas/ven_dev_dlg.jsp", request, response);

    }
   
    
    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");

        rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
 
    }

    public void AgregarDesde(HttpServletRequest request, HttpServletResponse response, String idmodAgregar4, String idmodAgregar, String id_enlace, String idmod4, String idmod, JVentasEntidadesSetIdsV2 set)
    	throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
    	
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_VENTAS_FACTURAS_DET (\n";
        tbl += "Partida smallint NOT NULL ,\n";
    	tbl += "Cantidad numeric(9, 3) NOT NULL ,\n";
    	tbl += "ID_Prod varchar(20) NOT NULL ,\n";
    	tbl += "Precio numeric(19,4) NOT NULL ,\n";
    	tbl += "Descuento numeric(5, 2) NOT NULL ,\n";
    	tbl += "IVA numeric(14, 6) NOT NULL ,\n";
    	tbl += "Obs varchar(80) NULL ,\n";
    	tbl += "Importe numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteDesc numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteIVA numeric(19,4) NOT NULL ,\n";
    	tbl += "TotalPart numeric(19,4) NOT NULL ,\n";
    	tbl += "IEPS numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIEPS numeric(19,4) NOT NULL,\n";
    	tbl += "IVARet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIVARet numeric(19,4) NOT NULL,\n";
    	tbl += "ISRRet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteISRRet numeric(19,4) NOT NULL,\n";
    	tbl += "Tipo char(1) NOT NULL ,\n";
    	tbl += "Unidad varchar(80) NULL ,\n";
    	tbl += "PrecioSD numeric(19,4) NOT NULL \n";
    	tbl += ");\n\n";
    	
    	for(int i = 0; i < rec.getPartidas().size(); i++)
        {
    		tbl += "\n\ninsert into _TMP_VENTAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getID_Prod()) + 
    		"','" + rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getDescuento() + "','" + rec.getPartida(i).getIVA() + "','" + p(rec.getPartida(i).getObsPartida()) + 
    		"','" + rec.getPartida(i).getImporte() + "','" + rec.getPartida(i).getImporteDesc() + "','" + rec.getPartida(i).getImporteIVA() + "','" + rec.getPartida(i).getTotalPart() + 
    		"','" + rec.getPartida(i).getIEPS() + "','" + rec.getPartida(i).getImporteIEPS() + "','" + rec.getPartida(i).getIVARet() + "','" + rec.getPartida(i).getImporteIVARet() + "','" + rec.getPartida(i).getISRRet() + "','" + rec.getPartida(i).getImporteISRRet() + 
    		"','" + p(rec.getPartida(i).getID_Tipo()) + "','" + p(rec.getPartida(i).getUnidad()) + "','" + rec.getPartida(i).getPrecio() + "');";
        }
      
    	String str, del;
    	if(idmodAgregar4.equals("VFAC"))
    	{
    		if(rec.getForma_Pago().equals("contado"))
    			tbl += "\n\n" + request.getAttribute("fsipg_tmppagos");
    		else
    		{
    			tbl += "\n\n";
    			tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_PAGOS (\n";
    			tbl += "Partida serial NOT NULL ,\n";
    			tbl += "ID_FormaPago smallint NOT NULL ,\n";
    			tbl += "ID_BanCaj smallint NOT NULL ,\n";
    			tbl += "Total numeric(19,4) NOT NULL ,\n";
    			tbl += "RefPago varchar(20) NULL ,\n";
    			tbl += "TipoMov character(3) NOT NULL ,\n";
    			tbl += "ID_SatBanco character(3) NOT NULL,\n";
    			tbl += "ID_SatMetodosPago character(2) NOT NULL,\n";
    			tbl += "BancoExt character varying(150) NOT NULL,\n";
    			tbl += "CuentaBanco character varying(50) NOT NULL,\n";
    			tbl += "Cheque character varying(20) NOT NULL\n";
    			tbl += "); \n\n";
    		}
    		
    		str = "select * from sp_ventas_facturas_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("referencia")) + 
    	        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + (rec.getForma_Pago().equals("contado") ? "0" : "1" ) + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
    	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
    	        	"','" + rec.getID_Bodega() + "','" + p(id_enlace) + "','" + rec.getID_Vendedor() + "','" + p(idmod4) + "','" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    	    	    		
    		del = "\nDROP TABLE _TMP_VENTAS_FACTURAS_DET;\n\nDROP TABLE _TMP_PAGOS;";
            
        }
    	else 
    	{
    		if(idmodAgregar4.equals("VPED"))
    			str = "select * from sp_ventas_pedidos_agregar('";
    		else //if(idmodAgregar4.equals("VREM"))
    			str = "select * from sp_ventas_remisiones_agregar('";
    		
    		str += rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("referencia")) + 
        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + (rec.getForma_Pago().equals("contado") ? "0" : "1" ) + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','0','0','0" + 
        	"','" + rec.getID_Bodega() + "','" + p(id_enlace) + "','" + rec.getID_Vendedor() + "','" + p(idmod4) + "','" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    		
    		del = "DROP TABLE _TMP_VENTAS_FACTURAS_DET";
        	
    	}
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, tbl, str, del, rfb);

		String mensaje = rfb.getRes();
		if(rfb.getIdmensaje() == 0 && idmodAgregar4.equals("VFAC"))
        {
			StringBuffer sb_mensaje = new StringBuffer(254);
			generarCFDI(request, response, "FACTURAS", Integer.parseInt(rfb.getClaveret()), Long.toString(rec.getClave()), set, (byte)0, sb_mensaje);
			mensaje += "<br>" + sb_mensaje.toString();
			getSesion(request).setID_Mensaje((short)rfb.getIdmensaje(), mensaje);
        }
        else if(rfb.getIdmensaje() == 0 && idmodAgregar4.equals("VREM"))
        {
        	StringBuffer sb_mensaje = new StringBuffer(254);
			generarCFDI(request, response, "REMISIONES", Integer.parseInt(rfb.getClaveret()), Long.toString(rec.getClave()), set, (byte)0, sb_mensaje);
			mensaje += "<br>" + sb_mensaje.toString();
			getSesion(request).setID_Mensaje((short)rfb.getIdmensaje(), mensaje);
        }
       
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmodAgregar + "_AGREGAR", idmodAgregar4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
        irApag("/forsetiweb/ventas/ven_fact_dlg_generar.jsp", request, response);
        
    }
   
    public void AgregarDev(HttpServletRequest request, HttpServletResponse response, JVentasEntidadesSetIdsV2 set)
    	throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
    	JVenFactSes rec = (JVenDevSes)ses.getAttribute("ven_dev_dlg");
  	
    	String tbl;
    	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_VENTAS_FACTURAS_DET (\n";
        tbl += "Partida smallint NOT NULL ,\n";
    	tbl += "Cantidad numeric(9, 3) NOT NULL ,\n";
    	tbl += "ID_Prod varchar(20) NOT NULL ,\n";
    	tbl += "Precio numeric(19,4) NOT NULL ,\n";
    	tbl += "Descuento numeric(5, 2) NOT NULL ,\n";
    	tbl += "IVA numeric(14, 6) NOT NULL ,\n";
    	tbl += "Obs varchar(80) NULL ,\n";
    	tbl += "Importe numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteDesc numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteIVA numeric(19,4) NOT NULL ,\n";
    	tbl += "TotalPart numeric(19,4) NOT NULL ,\n";
    	tbl += "IEPS numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIEPS numeric(19,4) NOT NULL,\n";
    	tbl += "IVARet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIVARet numeric(19,4) NOT NULL,\n";
    	tbl += "ISRRet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteISRRet numeric(19,4) NOT NULL,\n";
    	tbl += "Tipo char(1) NOT NULL ,\n";
    	tbl += "Unidad varchar(80) NULL ,\n";
    	tbl += "PrecioSD numeric(19,4) NOT NULL \n";
    	tbl += ");\n\n";
    	
    	for(int i = 0; i < rec.getPartidas().size(); i++)
        {
    		tbl += "\n\ninsert into _TMP_VENTAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getID_Prod()) + 
    		"','" + rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getDescuento() + "','" + rec.getPartida(i).getIVA() + "','" + p(rec.getPartida(i).getObsPartida()) + 
    		"','" + rec.getPartida(i).getImporte() + "','" + rec.getPartida(i).getImporteDesc() + "','" + rec.getPartida(i).getImporteIVA() + "','" + rec.getPartida(i).getTotalPart() + 
    		"','" + rec.getPartida(i).getIEPS() + "','" + rec.getPartida(i).getImporteIEPS() + "','" + rec.getPartida(i).getIVARet() + "','" + rec.getPartida(i).getImporteIVARet() + "','" + rec.getPartida(i).getISRRet() + "','" + rec.getPartida(i).getImporteISRRet() + 
    		"','" + p(rec.getPartida(i).getID_Tipo()) + "','" + p(rec.getPartida(i).getUnidad()) + "','" + rec.getPartida(i).getPrecio() + "');";
        }
      
       	if(rec.getForma_Pago().equals("contado"))
			tbl += "\n\n" + request.getAttribute("fsipg_tmppagos");
		else
		{
			tbl += "\n\n";
			tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_PAGOS (\n";
			tbl += "Partida serial NOT NULL ,\n";
			tbl += "ID_FormaPago smallint NOT NULL ,\n";
			tbl += "ID_BanCaj smallint NOT NULL ,\n";
			tbl += "Total numeric(19,4) NOT NULL ,\n";
			tbl += "RefPago varchar(20) NULL ,\n";
			tbl += "TipoMov character(3) NOT NULL ,\n";
			tbl += "ID_SatBanco character(3) NOT NULL,\n";
			tbl += "ID_SatMetodosPago character(2) NOT NULL,\n";
			tbl += "BancoExt character varying(150) NOT NULL,\n";
			tbl += "CuentaBanco character varying(50) NOT NULL,\n";
			tbl += "Cheque character varying(20) NOT NULL\n";
			tbl += "); \n\n";
		}
		  		
       	String str, devrebperm;
       	
       	if(rec.getDevReb().equals("DEV"))
       		devrebperm = "VEN_DEV_DEVOLVER";
       	else
       		devrebperm = "VEN_DEV_REBAJAR";
     
       	if(rec.getForma_Pago().equals("contado"))
       	{	
       		str = "select * from sp_ventas_devoluciones_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(request.getParameter("referencia")) + 
    			"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + (rec.getForma_Pago().equals("contado") ? "0" : "1" ) + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
 	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
 	        	"','" + rec.getID_Bodega() + "','" + rec.getID_Factura() + "','" + rec.getID_Vendedor() + "',null,'','" + p(rec.getDevReb()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
       	}
       	else
       	{
       		str = "select * from sp_ventas_devoluciones_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(request.getParameter("referencia")) + 
    			"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + (rec.getForma_Pago().equals("contado") ? "0" : "1" ) + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
 	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','0','0','0" + 
 	        	"','" + rec.getID_Bodega() + "','" + rec.getID_Factura() + "','" + rec.getID_Vendedor() + "','" + (Integer)request.getAttribute("fsipg_id_concepto") + "','" + p((String)request.getAttribute("fsipg_desc_concepto")) + "','" + p(rec.getDevReb()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
       	}
    	//doDebugSQL(request, response, str + "<p>" + tbl);
        JRetFuncBas rfb = new JRetFuncBas();
  		
  		doCallStoredProcedure(request, response, tbl, str, "\nDROP TABLE _TMP_VENTAS_FACTURAS_DET;\n\nDROP TABLE _TMP_PAGOS;", rfb);

  		short idmensaje = (short)rfb.getIdmensaje();
  		String mensaje = rfb.getRes();
  		if(rfb.getIdmensaje() == 0)
        {
        	StringBuffer sb_mensaje = new StringBuffer(254);
			idmensaje = generarCFDI(request, response, "DEVOLUCIONES", Integer.parseInt(rfb.getClaveret()), Long.toString(rec.getClave()), set, (byte)0, sb_mensaje);
			mensaje += "<br>" + sb_mensaje.toString();
			getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
        }
        
  		RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), devrebperm, "VDEV|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_FAC").getEspecial() + "||",mensaje);
        irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
    	
    }
    
    public void Enlazar(HttpServletRequest request, HttpServletResponse response, String tipomov, String idmod, String idmod4)
    		throws ServletException, IOException
    {
    	String enlace;   	
    	if(tipomov.equals("FACTURAS"))
    		enlace = "FAC";
    	else if(tipomov.equals("REMISIONES"))
    	    enlace = "REM";
    	else //if(tipomov.equals("DEVOLUCIONES"))
    		enlace = "DSV";
    	
    	String str = "select * from sp_cfd_enlazarventa('" + p(enlace) + "','" + p(request.getParameter("ID")) + "','" + q(request.getParameter("uuid")) + "') as ( err integer, res varchar, clave integer );";
    	
    	//doDebugSQL(request, response, str);
    	
    	JRetFuncBas rfb = new JRetFuncBas();
    	doCallStoredProcedure(request, response, str, rfb);

    	short idmensaje = (short)rfb.getIdmensaje();
    	String mensaje = rfb.getRes();
    			            
    	RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_AGREGAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	      
    }    
    
    public void Agregar(HttpServletRequest request, HttpServletResponse response, String tipomov, JVentasEntidadesSetIdsV2 set, String idmod, String idmod4)
      throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
    	
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_VENTAS_FACTURAS_DET (\n";
        tbl += "Partida smallint NOT NULL ,\n";
    	tbl += "Cantidad numeric(9, 3) NOT NULL ,\n";
    	tbl += "ID_Prod varchar(20) NOT NULL ,\n";
    	tbl += "Precio numeric(19,4) NOT NULL ,\n";
    	tbl += "Descuento numeric(5, 2) NOT NULL ,\n";
    	tbl += "IVA numeric(14, 6) NOT NULL ,\n";
    	tbl += "Obs varchar(80) NULL ,\n";
    	tbl += "Importe numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteDesc numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteIVA numeric(19,4) NOT NULL ,\n";
    	tbl += "TotalPart numeric(19,4) NOT NULL ,\n";
    	tbl += "IEPS numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIEPS numeric(19,4) NOT NULL,\n";
    	tbl += "IVARet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIVARet numeric(19,4) NOT NULL,\n";
    	tbl += "ISRRet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteISRRet numeric(19,4) NOT NULL,\n";
    	tbl += "Tipo char(1) NOT NULL ,\n";
    	tbl += "Unidad varchar(80) NULL ,\n";
    	tbl += "PrecioSD numeric(19,4) NOT NULL \n";
    	tbl += ");\n\n";
    	
    	for(int i = 0; i < rec.getPartidas().size(); i++)
        {
    		tbl += "\n\ninsert into _TMP_VENTAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getID_Prod()) + 
    		"','" + rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getDescuento() + "','" + rec.getPartida(i).getIVA() + "','" + p(rec.getPartida(i).getObsPartida()) + 
    		"','" + rec.getPartida(i).getImporte() + "','" + rec.getPartida(i).getImporteDesc() + "','" + rec.getPartida(i).getImporteIVA() + "','" + rec.getPartida(i).getTotalPart() + 
    		"','" + rec.getPartida(i).getIEPS() + "','" + rec.getPartida(i).getImporteIEPS() + "','" + rec.getPartida(i).getIVARet() + "','" + rec.getPartida(i).getImporteIVARet() + "','" + rec.getPartida(i).getISRRet() + "','" + rec.getPartida(i).getImporteISRRet() + 
    		"','" +	p(rec.getPartida(i).getID_Tipo()) + "','" + p(rec.getPartida(i).getUnidad()) + "','" + rec.getPartida(i).getPrecio() + "');";
        }

       
    	String str, del;
    	if(tipomov.equals("FACTURAS"))
    	{
    		if(rec.getForma_Pago().equals("contado"))
    			tbl += "\n\n" + request.getAttribute("fsipg_tmppagos");
    		else
    		{
    			tbl += "\n\n";
    			tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_PAGOS (\n";
    			tbl += "Partida serial NOT NULL ,\n";
    			tbl += "ID_FormaPago smallint NOT NULL ,\n";
    			tbl += "ID_BanCaj smallint NOT NULL ,\n";
    			tbl += "Total numeric(19,4) NOT NULL ,\n";
    			tbl += "RefPago varchar(20) NULL ,\n";
    			tbl += "TipoMov character(3) NOT NULL ,\n";
    			tbl += "ID_SatBanco character(3) NOT NULL,\n";
    			tbl += "ID_SatMetodosPago character(2) NOT NULL,\n";
    			tbl += "BancoExt character varying(150) NOT NULL,\n";
    			tbl += "CuentaBanco character varying(50) NOT NULL,\n";
    			tbl += "Cheque character varying(20) NOT NULL\n";
    			tbl += "); \n\n";
    		}
    		
    		str = "select * from sp_ventas_facturas_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(rec.getReferencia()) + 
    	        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + (rec.getForma_Pago().equals("contado") ? "0" : "1" ) + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
    	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
    	        	"','" + rec.getID_Bodega() + "',null,'" + rec.getID_Vendedor() + "',null,'" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    	    	    		
    		del = "\nDROP TABLE _TMP_VENTAS_FACTURAS_DET;\n\nDROP TABLE _TMP_PAGOS;";
            
        }
    	else 
    	{
    		if(tipomov.equals("PEDIDOS"))
    			str = "select * from sp_ventas_pedidos_agregar('";
    		else if(tipomov.equals("REMISIONES"))
    			str = "select * from sp_ventas_remisiones_agregar('";
    		else //if(tipomov.equals("COTIZACIONES"))
    			str = "select * from sp_ventas_cotizaciones_agregar('";
    		
    		str += rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(rec.getReferencia()) + 
        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + (rec.getForma_Pago().equals("contado") ? "0" : "1" ) + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
        	"','" + rec.getID_Bodega() + "',null,'" + rec.getID_Vendedor() + "',null,'" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    		
    		del = "DROP TABLE _TMP_VENTAS_FACTURAS_DET";
        	
    	}
    	
    	//doDebugSQL(request, response, tbl + "\n\n\n" + str + "");
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, tbl, str, del, rfb);

		short idmensaje = (short)rfb.getIdmensaje();
		String mensaje = rfb.getRes();
		if(rfb.getIdmensaje() == 0 && tipomov.equals("FACTURAS"))
        {
			StringBuffer sb_mensaje = new StringBuffer();
			idmensaje = generarCFDI(request, response, "FACTURAS", Integer.parseInt(rfb.getClaveret()), Long.toString(rec.getClave()), set, (byte)0, sb_mensaje);
			mensaje += "<br>" + sb_mensaje.toString();
			getSesion(request).setID_Mensaje((short)rfb.getIdmensaje(), mensaje);
        }
        else if(rfb.getIdmensaje() == 0 && tipomov.equals("REMISIONES"))
        {
        	StringBuffer sb_mensaje = new StringBuffer(254);
			idmensaje = generarCFDI(request, response, "REMISIONES", Integer.parseInt(rfb.getClaveret()), Long.toString(rec.getClave()), set, (byte)0, sb_mensaje);
			mensaje += "<br>" + sb_mensaje.toString();
			getSesion(request).setID_Mensaje((short)rfb.getIdmensaje(), mensaje);
        }
                    
        RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_AGREGAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
        
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response, String tipomov, String idmod, String idmod4)
    	throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JVenFactSes rec = (JVenFactSes)ses.getAttribute("ven_fact_dlg");
    	
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_VENTAS_FACTURAS_DET (\n";
        tbl += "Partida smallint NOT NULL ,\n";
    	tbl += "Cantidad numeric(9, 3) NOT NULL ,\n";
    	tbl += "ID_Prod varchar(20) NOT NULL ,\n";
    	tbl += "Precio numeric(19,4) NOT NULL ,\n";
    	tbl += "Descuento numeric(5, 2) NOT NULL ,\n";
    	tbl += "IVA numeric(14, 6) NOT NULL ,\n";
    	tbl += "Obs varchar(80) NULL ,\n";
    	tbl += "Importe numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteDesc numeric(19,4) NOT NULL ,\n";
    	tbl += "ImporteIVA numeric(19,4) NOT NULL ,\n";
    	tbl += "TotalPart numeric(19,4) NOT NULL ,\n";
    	tbl += "IEPS numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIEPS numeric(19,4) NOT NULL,\n";
    	tbl += "IVARet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteIVARet numeric(19,4) NOT NULL,\n";
    	tbl += "ISRRet numeric(9,6) NOT NULL,\n";
    	tbl += "ImporteISRRet numeric(19,4) NOT NULL,\n";
    	tbl += "Tipo char(1) NOT NULL ,\n";
    	tbl += "Unidad varchar(80) NULL ,\n";
    	tbl += "PrecioSD numeric(19,4) NOT NULL \n";
    	tbl += ");\n\n";
    	
    	for(int i = 0; i < rec.getPartidas().size(); i++)
        {
    		tbl += "\n\ninsert into _TMP_VENTAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getID_Prod()) + 
    		"','" + rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getDescuento() + "','" + rec.getPartida(i).getIVA() + "','" + p(rec.getPartida(i).getObsPartida()) + 
    		"','" + rec.getPartida(i).getImporte() + "','" + rec.getPartida(i).getImporteDesc() + "','" + rec.getPartida(i).getImporteIVA() + "','" + rec.getPartida(i).getTotalPart() + 
    		"','" + rec.getPartida(i).getIEPS() + "','" + rec.getPartida(i).getImporteIEPS() + "','" + rec.getPartida(i).getIVARet() + "','" + rec.getPartida(i).getImporteIVARet() + "','" + rec.getPartida(i).getISRRet() + "','" + rec.getPartida(i).getImporteISRRet() + 
    		"','" + p(rec.getPartida(i).getID_Tipo()) + "','" + p(rec.getPartida(i).getUnidad()) + "','" + rec.getPartida(i).getPrecio() + "');";
        }

       
    	String str, del;
    
    	if(tipomov.equals("PEDIDOS"))
			str = "select * from sp_ventas_pedidos_cambiar('";
		else //if(tipomov.equals("COTIZACIONES"))
			str = "select * from sp_ventas_cotizaciones_cambiar('";
		
		str += rec.getID_Entidad() + "','" + p(request.getParameter("ID")) + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + rec.getReferencia() + 
    	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + (rec.getForma_Pago().equals("contado") ? "0" : "1" ) + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
    	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
    	"','" + rec.getID_Bodega() + "',null,'" + rec.getID_Vendedor() + "',null,'" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
		
		del = "DROP TABLE _TMP_VENTAS_FACTURAS_DET";

		//doDebugSQL(request, response, tbl + str);
		
		JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, tbl, str, del, rfb);

		String mensaje = rfb.getRes();
		
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_CAMBIAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
		irApag("/forsetiweb/ventas/ven_fact_dlg.jsp", request, response);
      
    }
      
    public void CancelarFactura(HttpServletRequest request, HttpServletResponse response, String moddes, String idmod, String idmod4, StringBuffer sb_mensaje)
    		throws ServletException, IOException
    {
    	String str = "select * from ";
    	if(moddes.equals("FACTURAS"))
    		str += "sp_ventas_facturas_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("VEN_FAC").getEspecial() + "')";
    	else if(moddes.equals("PEDIDOS"))
    		str += "sp_ventas_pedidos_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("VEN_PED").getEspecial() + "')";
    	else if(moddes.equals("COTIZACIONES"))
    		str += "sp_ventas_cotizaciones_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("VEN_COT").getEspecial() + "')";
    	else if(moddes.equals("REMISIONES"))
    		str += "sp_ventas_remisiones_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("VEN_REM").getEspecial() + "')";
    	else if(moddes.equals("DEVOLUCIONES"))
    		str += "sp_ventas_devoluciones_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("VEN_DEV").getEspecial() + "')";
    	
    	str += " as ( err integer, res varchar, clave integer );";

    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, str, rfb);

		String mensaje = sb_mensaje.toString() + rfb.getRes();
		
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_CANCELAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      
	  }
    
	@SuppressWarnings("rawtypes")
	public void SubirArchivosCFD(HttpServletRequest request, HttpServletResponse response, JFacturasXML venfactxml, Vector archivos)
			throws ServletException, IOException
	{
		short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
			
		HttpSession ses = request.getSession(true);
		JForsetiCFD cfd = (JForsetiCFD)ses.getAttribute("ven_cfd");
		if(cfd == null)
		{
			cfd = new JForsetiCFD();
			ses.setAttribute("ven_cfd", cfd);
		}
		else
			cfd.resetearCertComp();
	        
		idmensaje = cfd.SubirArchivosCFDI(request, archivos, mensaje, "I");
		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		
		if(idmensaje == JForsetiCFD.OKYDOKY)
		{
			idmensaje = cfd.VerificarFacturasSubidas(request, venfactxml, mensaje, "I");
			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
			
			if(idmensaje == JForsetiCFD.OKYDOKY)
			{
				idmensaje = cfd.GuardarDocumentoCFDI(request, Integer.parseInt(getSesion(request).getSesion(venfactxml.getParametros().getProperty("idmod")).getEspecial()), venfactxml.getArchivoXML(), venfactxml.getArchivoPDF(), mensaje, "I");
				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				
			}
			
		}
		
		JFacturasXML rec = (JFacturasXML)ses.getAttribute("ven_fact_xml");
		if(rec != null)
			rec = null;
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
	}
    
}
