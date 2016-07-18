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
package forseti.compras;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import forseti.JAccesoBD;
import forseti.JBajarArchivo;
import forseti.JFacturasXML;
import forseti.JForsetiApl;
import forseti.JForsetiCFD;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JSubirArchivo;
import forseti.JUtil;
import forseti.sets.*;
import forseti.compras.JCompFactSes;
import forseti.compras.JCompDevSes;


@SuppressWarnings("serial")
public class JCompFactDlg extends JForsetiApl
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
      //request.setAttribute("fsi_modulo",request.getRequestURI());
      super.doPost(request,response);

      String comp_fact_dlg = "";
      request.setAttribute("comp_fact_dlg",comp_fact_dlg);

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
  	  			  JFacturasXML compfactxml = (JFacturasXML)ses.getAttribute("comp_fact_xml");
  	  			  Vector archivos = new Vector();
 	  			  DiskFileUpload fu = new DiskFileUpload();
    	  		  List items = fu.parseRequest(request);
				  Iterator iter = items.iterator();
				  while (iter.hasNext()) 
  	  			  {
  	  				  FileItem item = (FileItem)iter.next();
  	  				  if (item.isFormField()) 
  	  				  	  compfactxml.getParametros().put(item.getFieldName(), item.getString());
  	  				  else
  	  				  	  archivos.addElement(item);
  	  			  }
  	  			  
  	  			  // revisa por las entidades
  	  			  JComprasEntidadesSetIdsV2 setids;
  	  			  String idmod = compfactxml.getParametros().getProperty("idmod"), 
  	  					 idmod4 = compfactxml.getParametros().getProperty("idmod4"), 
  	  					 moddes = compfactxml.getParametros().getProperty("moddes");
  	  			
  	  			  request.setAttribute("idmod",idmod);
  	  			  request.setAttribute("moddes",moddes);
  	  			  
  	  			  
  	  			  setids = new JComprasEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion(idmod).getEspecial(),
  	  					  (idmod.equals("COMP_GAS") ? 2 : 0));
  	  			  setids.Open();
  	  
  	  			  if(setids.getNumRows() < 1)
  	  			  {
  	  				  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", idmod);
  	  				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	  				  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), idmod, idmod4 + "||||",mensaje);
  	  				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	  				  return;
  	  			  }
  	          
  	  			  if(!idmod.equals("COMP_DEV")) // Si no es devolucion
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
	          		  if(!getSesion(request).getPermiso("COMP_DEV_DEVOLVER") && !getSesion(request).getPermiso("COMP_DEV_REBAJAR"))
	          		  {
	          			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_DEV_DEVOLVER") + " / " + MsjPermisoDenegado(request, "CEF", "COMP_DEV_REBAJAR");
	          			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_DEV_DEVOLVER","CDEV||||",mensaje);
	          			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	          			  return;
	          		  }
	          	  }
  	  			  System.out.println("PROPIEDAD: " + compfactxml.getParametros().getProperty("proceso"));
  	  			  if(compfactxml.getParametros().getProperty("proceso").equals("CARGAR_OTROS"))
  	  				  SubirArchivosOTROS(request, response, compfactxml, archivos);
  	  			  else
  	  				  SubirArchivosCFD(request, response, compfactxml, archivos);
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
          JComprasEntidadesSetIdsV2 setids;
          String idmod, idmod4, moddes;
          
          if(request.getParameter("tipomov").equals("FACTURAS"))
          {
        	  idmod = "COMP_FAC"; 
        	  idmod4 = "CFAC";
        	  moddes = "FACTURAS";
          }
          else if(request.getParameter("tipomov").equals("ORDENES"))
          {
        	  idmod = "COMP_ORD"; 
        	  idmod4 = "CORD";
        	  moddes = "ORDENES";
          }
          else if(request.getParameter("tipomov").equals("RECEPCIONES"))
          {
        	  idmod = "COMP_REC"; 
        	  idmod4 = "CREC";
        	  moddes = "RECEPCIONES";
          }
          else if(request.getParameter("tipomov").equals("GASTOS"))
          {
        	  idmod = "COMP_GAS"; 
        	  idmod4 = "CGAS";
        	  moddes = "GASTOS";
          }
          else
          {
        	  idmod = "COMP_DEV"; 
        	  idmod4 = "CDEV";
        	  moddes = "DEVOLUCIONES";
          }
          request.setAttribute("idmod",idmod);
          request.setAttribute("moddes",moddes);
          request.setAttribute("fact_xml","COMPRAS");
          
          setids = new JComprasEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion(idmod).getEspecial(),
        		  (idmod.equals("COMP_GAS") ? 2 : 0));
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
          if(!request.getParameter("proceso").equals("AGREGAR_COMPRA") && request.getParameter("ID") != null)
          {
        	  boolean intrusion = false;
        	  if(moddes.equals("FACTURAS"))
        	  {
        		  JComprasFactSet set = new JComprasFactSet(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Factura = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else if(moddes.equals("RECEPCIONES"))
        	  {
        		  JComprasRecepSetV2 set = new JComprasRecepSetV2(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else if(moddes.equals("ORDENES"))
        	  {
        		  JComprasOrdenesSet set = new JComprasOrdenesSet(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Orden = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else if(moddes.equals("GASTOS"))
        	  {
        		  JComprasGastosSet set = new JComprasGastosSet(request);
        		  set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Gasto = '" + p(request.getParameter("ID")) + "'";
        		  set.Open();
        		  if(set.getNumRows() < 1)
        			  intrusion = true;
        	  }
        	  else
        	  {
        		  JComprasDevolucionesSet set = new JComprasDevolucionesSet(request);
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
          
          if(request.getParameter("proceso").equals("ENLAZAR_COMPRA"))
          {
        	  if(moddes.equals("FACTURAS") || moddes.equals("RECEPCIONES") || moddes.equals("DEVOLUCIONES") || moddes.equals("GASTOS")) 
        	  {
        		  // Revisa si tiene permisos
        		  if(!idmod.equals("COMP_DEV")) // Si no es devolucion
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
	          		  if(!getSesion(request).getPermiso("COMP_DEV_DEVOLVER") && !getSesion(request).getPermiso("COMP_DEV_REBAJAR"))
	          		  {
	          			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_DEV_DEVOLVER") + " / " + MsjPermisoDenegado(request, "CEF", "COMP_DEV_REBAJAR");
	          			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_DEV_DEVOLVER","CDEV||||",mensaje);
	          			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	          			  return;
	          		  }
	          	  }
        		  	
        		  if(setids.getAbsRow(0).getFija())
            	  {
            		  idmensaje = 3; mensaje += "ERROR: No se puede subir ningun CFDI, CBB o Factura Extranjera porque esta entidad de compra o gasto está establecida como Fija<br>";
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
	        			  ///////////////////////////////////////////////////////////////////////////
	        			  String[] valoresParamUUID = request.getParameterValues("uuid");
	        			  String[] valoresParamCBBEXT = request.getParameterValues("cbbext");
	        			  String tipoEnlace = "";
	        			  boolean EnlazarCompraExistente = (request.getParameter("ID") == null ? false : true);
	        			  if(valoresParamUUID == null && valoresParamCBBEXT == null)
	        			  {
	        				  idmensaje = 3; mensaje += "ERROR: Se deben seleccionar, ya sea los CFDIs o los CBBs o Facturas Extranjeras que se desean enlazar.<br>";
	        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  	  return;
	        			  }
	        			  //Si existen ambos, UUIDs y CBB o EXT, marcará error 
	        			  if(valoresParamUUID != null && valoresParamCBBEXT != null && valoresParamUUID.length > 0 && valoresParamCBBEXT.length > 0)
	        			  {
	        				  idmensaje = 3; mensaje += "ERROR: No se pueden enlazar ambos tipos de documento, CFDIs mas CBB o Facturas Extranjeras, a una misma compra.<br>";
	        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  	  return;
	        			  }
	        			  if(valoresParamCBBEXT != null && valoresParamCBBEXT.length > 0 && !EnlazarCompraExistente)
	        			  {
	        				  idmensaje = 3; mensaje += "ERROR: Los documentos CBB o Facturas extranjeras, deben enlazarse forzosamente a una compra existente.<br>";
	        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        			  	  return;
	        			  }
	        			  float TotalUUIDs = 0F;
	        			  String UUIDs = "";
	        			  float TC = 1.0F, TCAnt = 1.0F;
	        			  String RFC_Emisor = "";
	        			  HttpSession ses = request.getSession(true);
	        			  JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
        			  	  if(rec == null)
        			  	  {
        			  		  rec = new JCompFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
        			  		  ses.setAttribute("comp_fact_dlg", rec);
        			  	  }
        			  	  else
        			  		  rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
        			  	  
        			  	  rec.setReferencia("s/r");
        			  	  if(valoresParamUUID != null)
        			  	  {
        			  		  tipoEnlace = "UUID";
		        			  for(int u = 0; u < valoresParamUUID.length; u++)
		        	          {
		        	          ///////////////////////////// VP INI /////////////////////////////////////////////
			        			  JCFDCompSet comprobante = new JCFDCompSet(request,"COMPRAS");
			        			  comprobante.m_Where = "UUID = '" + p(valoresParamUUID[u]) + "'";
			        			  comprobante.Open();
			        			  //System.out.println("NUM: " + valoresParamUUID.length + " UUID: " + valoresParamUUID[u]);
			        			  if(comprobante.getNumRows() < 1 || !comprobante.getAbsRow(0).getFSI_Tipo().equals("ENT") || comprobante.getAbsRow(0).getFSI_ID() != Integer.parseInt(getSesion(request).getSesion(idmod).getEspecial()))
			        			  {
			        				  idmensaje = 3; mensaje += "ERROR: No se ha cargado el CFDI de la compra, este ya esta ligado a otra compra, o el CFDI se cargo en otra entidad<br>";
			        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			        			  	  return;
			        			  }
			        	          
			        			  JFacturasXML compfactxml = new JFacturasXML();
			            		  StringBuffer sb_mensaje = new StringBuffer();
			            		  if(!JForsetiCFD.CargarDocumentoCFDI(request, compfactxml, sb_mensaje, valoresParamUUID[u]/*request.getParameter("uuid")*/, "E"))
			            		  {
			            			  idmensaje = 3; mensaje += sb_mensaje.toString();
			        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			        			  	  return;
			            		  }
			            		  
			            		  if((compfactxml.getComprobante().getProperty("tipoDeComprobante").equals("ingreso") && moddes.equals("DEVOLUCIONES")) 
			        			  		  || (compfactxml.getComprobante().getProperty("tipoDeComprobante").equals("egreso") && !moddes.equals("DEVOLUCIONES")) 
			        			  		  || compfactxml.getComprobante().getProperty("tipoDeComprobante").equals("traslado"))
			        			  {
			        			  	  idmensaje = 3; mensaje += "ERROR: El tipo de comprobante fiscal digital CFDI, No corresponde con el tipo de documento a enlazar.<br>";
			        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			        			  	  return;
			        			  }
			            		  //Verifica que el RFC del Receptor sea igual al RFC registrado, o que sea rfc generico
			        			  JBDSSet set = new JBDSSet(request);
			        			  set.ConCat(true);
			        			  set.m_Where = "Nombre = 'FSIBD_" + p(getSesion(request).getBDCompania()) + "'";
			        			  set.Open();
			        			  if(!compfactxml.getRFC_Receptor().equalsIgnoreCase(set.getAbsRow(0).getRFC()))
			        			  {
			        			  	  idmensaje = 3; mensaje = "ERROR: El RFC del receptor en el XML no pertenece a la compañia";
			        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
			        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			        			  	  return;
			        			  }
			        			  
			        			  RFC_Emisor = compfactxml.getRFC_Emisor();
			        			  
			        			  //Verifica los tipos de cambio, Si son mas de un CFDI, no deben tener distintos tipos de cambio
			        			  if(u == 0)
			        			  {
			        				  try { TC = Float.parseFloat(compfactxml.getComprobante().getProperty("TipoCambio")); } catch(NumberFormatException e) { TC = 1.0F; }
			        				  try { TCAnt = Float.parseFloat(compfactxml.getComprobante().getProperty("TipoCambio")); } catch(NumberFormatException e) { TCAnt = 1.0F; }
			        			  }
			        			  else
			        			  {
			        				  try { TC = Float.parseFloat(compfactxml.getComprobante().getProperty("TipoCambio")); } catch(NumberFormatException e) { TC = 1.0F; }
			        			  }
			        			  if(TC != TCAnt)
			        			  {
			        				  idmensaje = 3; mensaje = "ERROR: En múltiples enlaces, los tipos de cambio deben coincidir. Los gastos tienen distintos tipos de cambio";
			        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
			        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			        			  	  return;
			        			  }
			        			  
			        			  if(request.getParameter("ID") == null) // Significa que debe agregar una nueva compra 
			        			  {
			        				  if(moddes.equals("DEVOLUCIONES") || moddes.equals("ORDENES"))
			        				  {
			        					  idmensaje = 1; mensaje = "PRECAUCION: Solo se pueden enlazar devoluciones previamente generadas desde compras. Selecciona la devolución e intenta enlazar de nuevo.";
				        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
				        			  	  return;
			        				  }
			        				  
			        				  if(valoresParamUUID.length > 1 && !moddes.equals("GASTOS"))
			        				  {
			        					  idmensaje = 1; mensaje = "PRECAUCION: Solo se pueden enlazar multiples CFDI a un gasto, y no, a una compra";		        			  	  
			        					  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
				        			  	  return;
			        				  }
			        				  
			        				  float descuento = Float.parseFloat(compfactxml.getComprobante().getProperty("descuento"));
				        			  if(descuento != 0.0)
				        			  {
				        			  	  idmensaje = 1; mensaje = "PRECAUCION: Por el momento, la carga de compras no soportan descuentos implicitos porque en el CFDI no se especifica a que producto(s) o servicio(s) aplica cada descuento, lo que hace imposible determinar un costo verdadero.";
				        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
				        			  	  return;
				        			  }
		
				        			  rec.agregaCFDI(compfactxml);
				        			  
				        			  if(valoresParamUUID.length == 1) // es un solo CFDI (Siempre en el caso de Compras y Recepciones... y algunas veces en gastos)
				        			  {
				        				  float tc, iva; int idmon;
					        			  Date fecha = JUtil.estFechaCFDI(compfactxml.getComprobante().getProperty("fecha"));
				        			  	  rec.setFecha(fecha);
				        			  	  try { tc = Float.parseFloat(compfactxml.getComprobante().getProperty("TipoCambio")); } catch(NumberFormatException e) { tc = 1.0F; }
				        			  	  rec.setTC(tc);
				        			  	  idmon = ((tc == 1) ? 1 : 2); //cambiar por 2 cuando se inserte moneda de dolares automaticamente al instalar empresa... Para ya distribuir
				        			  	  rec.setID_Moneda((byte)idmon);
				        			  	  rec.setTotal(Float.parseFloat(compfactxml.getComprobante().getProperty("total")));
				        			      try { iva = Float.parseFloat(compfactxml.getImpuestos().getProperty("totalImpuestosTrasladados")); } catch(NumberFormatException e) { iva = 0.0F; }
				        			  	  rec.setIVA(iva);
				        			  	  rec.setSubTotal(Float.parseFloat(compfactxml.getComprobante().getProperty("subTotal")));
				        			   	  rec.setDescuento(descuento);
				        			  	  rec.setImporte(rec.getSubTotal() - descuento);
				        			  	  rec.setReferencia("s/r");
				        			      rec.setFechaEntrega(fecha);
				        			  	  JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
				        			  	  setMon.m_Where = "Clave = '" + idmon  + "'";
				        			  	  setMon.Open(); 
				        			      rec.setMoneda(setMon.getAbsRow(0).getMoneda());
				        			      rec.setObs("Carga desde Factura Electrónica: " + compfactxml.getTFD().getProperty("UUID"));
				        			     
				        				  JProveeProveeMasSetV2 setpro = new JProveeProveeMasSetV2(request);
				        				  setpro.m_Where = "ID_Tipo = 'PR' and ID_EntidadCompra = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and RFC ~~* '" + p(compfactxml.getRFC_Emisor()) + "'";
				        				  setpro.Open();
				        				  	        				  
				        				  if(setpro.getNumRows() > 0)
				        			  	  {
				        					  JProveeProveeSetV2 setpro2 = new JProveeProveeSetV2(request);
					        			  	  setpro2.m_Where = "ID_Tipo = 'PR' and Clave = '" + setpro.getAbsRow(0).getID_Clave() + "'";
					        			  	  setpro2.Open();
					        			  	  
					        			  	  rec.setClave(setpro.getAbsRow(0).getID_Clave());
					        			  	  rec.setNombre(setpro2.getAbsRow(0).getNombre());
					        			  	  rec.setRFC(compfactxml.getRFC_Emisor());
					        			      rec.setNumero(setpro2.getAbsRow(0).getNumero());
					        			      rec.setColonia(setpro.getAbsRow(0).getColonia());
					        			      rec.setCP(setpro.getAbsRow(0).getCP());
					        			      rec.setDireccion(setpro.getAbsRow(0).getDireccion());
					        			      rec.setPoblacion(setpro.getAbsRow(0).getPoblacion());
					        			      rec.setTels(setpro2.getAbsRow(0).getTel());
				        			  	  }
				        			  }
				        			  	        			  	  	        			  	   
			        			      for(int i = 0; i< compfactxml.getConceptos().size(); i++)
			        			      {
			        			    	  Properties concepto = (Properties)compfactxml.getConceptos().elementAt(i);
			        			    	  float cantidad = Float.parseFloat(concepto.getProperty("cantidad"));
			        			    	  float precio = Float.parseFloat(concepto.getProperty("valorUnitario"));
			        			    	  String unidad = concepto.getProperty("unidad");
			        			    	  String idprod = "";
			        			    	  String descripcion = concepto.getProperty("descripcion");
			        			    	  String obs = concepto.getProperty("descripcion");
			        			    	  String tipo = (!moddes.equals("GASTOS") ? "P" : "G");
			        			    	  float importe = Float.parseFloat(concepto.getProperty("importe"));
			        			    	  float ivapor = 0.00F, iepspor = 0.00F, ivaretpor = 0.00F, isrretpor = 0.00F,
			        			    			 ivaimp = 0.00F, iepsimp = 0.00F, ivaretimp = 0.00F, isrretimp = 0.00F;
			        			    	  float totalPart = importe;
			        			    	  // Ahora verifica la existencia del producto en InvServProveeCodigos para la asociación
			        			  		  JInvsServProveeCodigosSet pcod = new JInvsServProveeCodigosSet(request);
			        			  		  pcod.m_Where = "ID_RFC = '" + p(rec.getRFC()) + "' and ID_Descripcion = '"  + p(concepto.getProperty("descripcion")) + "' and ID_Moneda = '" + rec.getID_Moneda() + "'";
			        			  		  //System.out.println(pcod.m_Where);
			        			  		  pcod.Open();
			        			  		  if(pcod.getNumRows() > 0)
			        			  		  {
			        			  			  JPublicInvServInvCatalogSetV2 cat = new JPublicInvServInvCatalogSetV2(request);
			        			  			  if(!moddes.equals("GASTOS"))
			        			  				  cat.m_Where = "Clave = '" + p(pcod.getAbsRow(0).getID_Prod()) + "' and ID_Tipo = 'P' and SeProduce = '0' and Status = 'V'";
			        			  			  else
			        			  				  cat.m_Where = "Clave = '" + p(pcod.getAbsRow(0).getID_Prod()) + "' and ID_Tipo = 'G' and Status = 'V'";
			        			  			
			        			  			  //System.out.println(cat.getSQL());
			        			     		  cat.Open();
			        			  			  if(cat.getNumRows() > 0 )
			        			  			  {
			        			  				  idprod = cat.getAbsRow(0).getClave();
			        			  				  descripcion = cat.getAbsRow(0).getDescripcion();
			        			  				  tipo = cat.getAbsRow(0).getID_Tipo();
			        			  				  //Aqui inicia los impuestos segun lo establecido en la entidad de compra y el catálogo
			        			  				  ivapor = cat.getAbsRow(0).getIVA() ? rec.getIVAPorcentual() : 0.0F ;
			        			  				  iepspor = cat.getAbsRow(0).getImpIEPS();
			        			  				  ivaretpor = cat.getAbsRow(0).getImpIVARet();
			        			  				  isrretpor = cat.getAbsRow(0).getImpISRRet();
			        			  				  ivaimp = (ivapor != 0.0) ? (((importe - descuento) * ivapor) / 100.0F) : 0.0F;
			        			  				  iepsimp = (iepspor != 0.0) ? (((importe - descuento) * iepspor) / 100.0F) : 0.0F;
			        			  				  ivaretimp = (ivaretpor != 0.0) ? (((importe - descuento) * ivaretpor) / 100.0F) : 0.0F;
			        			  				  isrretimp = (isrretpor != 0.0) ? (((importe - descuento) * isrretpor) / 100.0F) : 0.0F;
			        			  				
			        			  			  }
			        			  		  }
			        			    	  //System.out.println(descripcion + " " + ivapor + " " + ivaimp + " " + totalPart);
		        			    		  rec.agregaPartida(cantidad, unidad, idprod, idprod, descripcion, precio, importe, 0.00F, ivapor, iepspor, ivaretpor, isrretpor,
			        			    			  0.00F, ivaimp, iepsimp, ivaretimp, isrretimp, totalPart, obs, tipo);
			        			      }
			        			      UUIDs += compfactxml.getTFD().getProperty("UUID"); 
			        			  	  TotalUUIDs += JUtil.redondear(Float.parseFloat(compfactxml.getComprobante().getProperty("total")),2);
			        			  }    ///////////////////////////////////////////////////////////////////////////////////
			        			  else // Significa que debe Enlazar a una compra existente
			        			  {		//////////////////////////////////////////////////////////////////////////////////
			        				  UUIDs += compfactxml.getTFD().getProperty("UUID"); 
			        			  	  TotalUUIDs += JUtil.redondear(Float.parseFloat(compfactxml.getComprobante().getProperty("total")),2);
			        			  }
			        		  /////////////////////////////////////// VP END ///////////////////////////////////////////
		        	          }
        			  	  }
        			  	  else if(valoresParamCBBEXT != null)
        			  	  {
        			  		  tipoEnlace = "CBBEXT";
        			  		  for(int u = 0; u < valoresParamCBBEXT.length; u++)
		        	          {
        			  			  ///////////////////////////// VP INI /////////////////////////////////////////////
			        			  JCFDCompOtrSet compotr = new JCFDCompOtrSet(request);
			        			  compotr.m_Where = "ID_CFD = '" + p(valoresParamCBBEXT[u].substring(7)) + "'";
			        			  compotr.Open();
			        			  //System.out.println("NUM: " + valoresParamUUID.length + " UUID: " + valoresParamUUID[u]);
			        			  if(compotr.getNumRows() < 1 || !compotr.getAbsRow(0).getFSI_Tipo().equals("ENT") || compotr.getAbsRow(0).getFSI_ID() != Integer.parseInt(getSesion(request).getSesion(idmod).getEspecial()))
			        			  {
			        				  idmensaje = 3; mensaje += "ERROR: No se ha cargado el CBB o Factura extranjera de la compra, esta ya esta ligada a otra compra, o el CBB o Factura extranjera se cargo en otra entidad<br>";
			        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			        			  	  return;
			        			  }

			        			  //Verifica los tipos de cambio, Si son mas de un CBB o EXT, no deben tener distintos tipos de cambio
			        			  if(u == 0)
			        			  {
			        				  try { TC = compotr.getAbsRow(0).getTC(); } catch(NumberFormatException e) { TC = 1.0F; }
			        				  try { TCAnt = compotr.getAbsRow(0).getTC(); } catch(NumberFormatException e) { TCAnt = 1.0F; }
			        			  }
			        			  else
			        			  {
			        				  try { TC = compotr.getAbsRow(0).getTC(); } catch(NumberFormatException e) { TC = 1.0F; }
			        			  }
			        			  if(TC != TCAnt)
			        			  {
			        				  idmensaje = 3; mensaje = "ERROR: En múltiples enlaces, los tipos de cambio deben coincidir. Los gastos tienen distintos tipos de cambio";
			        			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
			        			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			        			  	  return;
			        			  }
			        			  
			        			  UUIDs += compotr.getAbsRow(0).getUUID(); 
			        			  TotalUUIDs += JUtil.redondear(compotr.getAbsRow(0).getTotal(),2);
		        	          }
        			  	  }
        			  	  
	        	          if(!EnlazarCompraExistente)
	        	          {
	        	        	  StringBuffer sb_mensaje = new StringBuffer();
	        	        	  rec.setUUID(UUIDs);
	        	        	  rec.setTotalUUIDs(TotalUUIDs);
	        	        	  idmensaje = rec.establecerConcordancia(request, sb_mensaje);
	        	        	  rec.establecerResultados();
	        	        	  if((rec.getTotal() - rec.getTotalUUIDs()) > 0.1 || (rec.getTotal() - rec.getTotalUUIDs()) < -0.1)
	        	        	  {
	        	        		  idmensaje = 3; 
	        	        		  sb_mensaje.append("ERROR: El total en el, o los CFDIs no corresponden al Total calculado en el registro a partir de estos registros. No se puede agregar. DOC: " + rec.getTotal() + " XML, CBB o EXT: " + rec.getTotalUUIDs());
	        	        	  }
	        	        	  getSesion(request).setID_Mensaje(idmensaje, sb_mensaje.toString());
	        			  	  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
	        			  	  return;
	        	          }
	        	          else
	        	          {
	        	        	  String[] valoresParam = request.getParameterValues("ID");
	        	        	  if(valoresParam.length == 1)
	        	        	  {	
	        	        		  if(moddes.equals("RECEPCIONES"))
	        	        		  {
	        	        			  JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
	        	        			  SetMod.m_Where = "ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
	        	        			  SetMod.Open();
		        			        	
	        	        			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Esta recepcion ya esta cancelada, no se puede enlazar el CFDI, CBB o EXT<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
	
	        	        			  if(SetMod.getAbsRow(0).getStatus().equals("F") || SetMod.getAbsRow(0).getStatus().equals("N"))
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Esta recepcion ya tiene una compra asociada, no se puede enlazar el CFDI, CBB o EXT. Debes enlazarlo a la compra<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
	        	        			  
	        	        			  if(SetMod.getAbsRow(0).getID_CFD() != 0 || SetMod.getAbsRow(0).getTFD() > 1)
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Este documento ya tiene CFDIs, CBBs o EXTs asociados. No puedes asociar al mismo documento<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
		        			  			
	        	        			  if(JUtil.redondear(SetMod.getAbsRow(0).getTotal(),1) != JUtil.redondear(TotalUUIDs,1))
	        	        			  {
	        	        				  idmensaje = 3; mensaje = "ERROR: El total en el, o los CFDIs, CBBs o EXTs no corresponden al Total del registro. No se puede enlazar. DOC: " + JUtil.redondear(SetMod.getAbsRow(0).getTotal(),2) + " XML: " + JUtil.redondear(TotalUUIDs,2);
	    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	    	        	        		  return;
	        	        			  }
	        	        			  
	        	        			  if(SetMod.getAbsRow(0).getID_Proveedor() != 0 && tipoEnlace != "CBBEXT")
	        	        			  {
	        	        				  JProveeProveeMasSetV2 setpro = new JProveeProveeMasSetV2(request);
	        	        				  setpro.m_Where = "ID_Tipo = 'PR' and ID_EntidadCompra = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and ID_Clave = '" + SetMod.getAbsRow(0).getID_Proveedor() + "'";
	        	        				  setpro.Open();
		        			  			  
		        	        			  if(!setpro.getAbsRow(0).getRFC().equalsIgnoreCase(RFC_Emisor))
		        	        			  {
		        	        				  idmensaje = 3; mensaje = "ERROR: El RFC del proveedor no corresponde al RFC del emisor en el CFDI. No se puede enlazar. DOC: " + setpro.getAbsRow(0).getRFC() + " XML: " + RFC_Emisor;
		    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		    	        	        		  return;
		        	        			  }
	        	        			  }
	        	        		  }
	        	        		  else if(moddes.equals("FACTURAS"))
	        	        		  {
	        	        			  JComprasFactSet SetMod = new JComprasFactSet(request);
	        	        			  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	        	        			  SetMod.Open();
		        			        		
	        	        			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Esta compra ya esta cancelada, no se puede enlazar el CFDI, CBB o EXT<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
	
	        	        			  if(SetMod.getAbsRow(0).getID_CFD() != 0 || SetMod.getAbsRow(0).getTFD() > 1)
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Este documento ya tiene CFDIs, CBBs o EXTs asociados. No puedes asociar al mismo documento<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
		        			  			  
	        	        			  if(JUtil.redondear(SetMod.getAbsRow(0).getTotal(),1) != JUtil.redondear(TotalUUIDs,1))
	        	        			  {
	        	        				  idmensaje = 3; mensaje = "ERROR: El total en el, o los CFDIs, CBBs o EXTs no corresponden al Total del registro. No se puede enlazar. DOC: " + JUtil.redondear(SetMod.getAbsRow(0).getTotal(),2) + " CE: " + JUtil.redondear(TotalUUIDs,2);
	    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	    	        	        		  return;
	        	        			  }
		        			  			
	        	        			  if(SetMod.getAbsRow(0).getID_Proveedor() != 0 && tipoEnlace != "CBBEXT")
	        	        			  {
	        	        				  JProveeProveeMasSetV2 setpro = new JProveeProveeMasSetV2(request);
	        	        				  setpro.m_Where = "ID_Tipo = 'PR' and ID_EntidadCompra = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and ID_Clave = '" + SetMod.getAbsRow(0).getID_Proveedor() + "'";
	        	        				  setpro.Open();
		        			  			  
		        	        			  if(!setpro.getAbsRow(0).getRFC().equalsIgnoreCase(RFC_Emisor))
		        	        			  {
		        	        				  idmensaje = 3; mensaje = "ERROR: El RFC del proveedor no corresponde al RFC del emisor en el CFDI. No se puede enlazar. DOC: " + setpro.getAbsRow(0).getRFC() + " CE: " + RFC_Emisor;
		    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		    	        	        		  return;
		        	        			  }
	        	        			  }
	        	        		  }
	        	        		  else if(moddes.equals("GASTOS"))
	        	        		  {
	        	        			  JComprasGastosSet SetMod = new JComprasGastosSet(request);
	        	        			  SetMod.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
	        	        			  SetMod.Open();
		        			        	
	        	        			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Este gasto ya esta cancelado, no se puede enlazar el CFDI, CBB o EXT<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
	
	        	        			  if(SetMod.getAbsRow(0).getID_CFD() != 0 || SetMod.getAbsRow(0).getTFD() > 1)
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Este documento ya tiene CFDIs, CBBs o EXTs asociados. No puedes asociar al mismo documento<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
		        			  			  
	        	        			  if(JUtil.redondear(SetMod.getAbsRow(0).getTotal(),1) != JUtil.redondear(TotalUUIDs,1))
	        	        			  {
	        	        				  idmensaje = 3; mensaje = "ERROR: El total en el, o los CFDIs, CBBs o EXTs no corresponden al Total del registro. No se puede enlazar. DOC: " + JUtil.redondear(SetMod.getAbsRow(0).getTotal(),2) + " CE: " + JUtil.redondear(TotalUUIDs,2);
	    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	    	        	        		  return;
	        	        			  }
	        	        			  
	        	        			  if(JUtil.redondear(SetMod.getAbsRow(0).getTC(),1) != JUtil.redondear(TCAnt,1))
	        	        			  {
	        	        				  idmensaje = 3; mensaje = "ERROR: El tipo de cambio del documento no coincide con el o los CFDI, CBB o Facturas extranjeras cargadas. No se puede enlazar. DOC: " + JUtil.redondear(SetMod.getAbsRow(0).getTC(),2) + " CE: " + JUtil.redondear(TCAnt,2);
	    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	    	        	        		  return;
	        	        			  }
		        			  			        	        			  
	        	        		  }
	        	        		  else if(moddes.equals("DEVOLUCIONES"))
	        	        		  {
	        	        			  JComprasDevolucionesSet SetMod = new JComprasDevolucionesSet(request);
	        	        			  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
	        	        			  SetMod.Open();
		        			        		
	        	        			  if(SetMod.getAbsRow(0).getStatus().equals("C"))
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Esta devolucion ya esta cancelada, no se puede enlazar el CFDI, CBB o EXT<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
	
	        	        			  if(SetMod.getAbsRow(0).getID_CFD() != 0 || SetMod.getAbsRow(0).getTFD() > 1)
	        	        			  {
	        	        				  idmensaje = 1;
	        	        				  mensaje += "PRECAUCION: Este documento ya tiene CFDIs, CBBs o EXTs asociados. No puedes asociar al mismo documento<br>";
	        	        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        	        				  return;
	        	        			  } 
		        			  			  
	        	        			  if(JUtil.redondear(SetMod.getAbsRow(0).getTotal(),1) != JUtil.redondear(TotalUUIDs,1))
	        	        			  {
	        	        				  idmensaje = 3; mensaje = "ERROR: El total en el, o los CFDIs, CBBs o EXTs no corresponden al Total del registro. No se puede enlazar. DOC: " + JUtil.redondear(SetMod.getAbsRow(0).getTotal(),2) + " CE: " + JUtil.redondear(TotalUUIDs,2);
	    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	    	        	        		  return;
	        	        			  }
		        			  			  
	        	        			  if(SetMod.getAbsRow(0).getID_Proveedor() != 0 && tipoEnlace != "CBBEXT")
	        	        			  {
	        	        				  JProveeProveeMasSetV2 setpro = new JProveeProveeMasSetV2(request);
	        	        				  setpro.m_Where = "ID_Tipo = 'PR' and ID_EntidadCompra = '" + getSesion(request).getSesion(idmod).getEspecial() + "' and ID_Clave = '" + SetMod.getAbsRow(0).getID_Proveedor() + "'";
	        	        				  setpro.Open();
		        			  			  
		        	        			  if(!setpro.getAbsRow(0).getRFC().equalsIgnoreCase(RFC_Emisor))
		        	        			  {
		        	        				  idmensaje = 3; mensaje = "ERROR: El RFC del proveedor no corresponde al RFC del emisor en el CFDI. No se puede enlazar. DOC: " + setpro.getAbsRow(0).getRFC() + " XML: " + RFC_Emisor;
		    	        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		    	        	        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		    	        	        		  return;
		        	        			  }
	        	        			  }
	        	        		  }
	        	        		  else // sale si no es recepcion, factura gasto o devolucion 
	        	        			  return;
		        			  	  
	        	        		  // Aqui asocia.
	        	        		  Enlazar(request, response, moddes, idmod, idmod4, UUIDs, tipoEnlace);
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
	        		  else if(request.getParameter("subproceso").equals("AGR_PART"))
	        		  {
	        			  if(!moddes.equals("DEVOLUCIONES"))
	        			  {
	        				  if(VerificarParametrosPartida(request, response))
	        					  AgregarPartida(request, response);
	        			  }
	        			  else
	        			  {
	        				  idmensaje = 1; mensaje += "No se permite agregar partidas en devoluciones"; 
        	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			  }
	            	  	  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
	        			  return;
	        		  }
	        		  else if(request.getParameter("subproceso").equals("ACTUALIZAR") || request.getParameter("subproceso").equals("AGR_PROVEE"))
	        		  {
	        			  AgregarRecursos(request,response);
	        			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
        				  return;
	        		  }
	        		  else if(request.getParameter("subproceso").equals("ENVIAR"))
	        		  {
	        			  if(AgregarRecursos(request,response) == -1)
	        			  {
	        				  HttpSession ses = request.getSession(true);
	        				  JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
	        				  //rec.setReferencia(p(request.getParameter("referencia")));
	        			  
	        				  if(moddes.equals("FACTURAS") || moddes.equals("GASTOS"))
	        				  {
	        					  if(request.getParameter("forma_pago").equals("contado"))
	        					  {
	        						  request.setAttribute("fsipg_tipo","compras");
	        						  request.setAttribute("fsipg_proc", "retiro");
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
	        				  else if(moddes.equals("RECEPCIONES"))
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
	        			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
        				  return;
	        		  }
	        	  }
        	  }
        	  else // Sale si no es FACTURA, DEVOLUCION O RECEPCION
    			  return;
          }
          else if(request.getParameter("proceso").equals("AGREGAR_COMPRA"))
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
        		  JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
        		  
        		  if(rec == null)
        		  {
        			  rec = new JCompFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
        			  ses.setAttribute("comp_fact_dlg", rec);
        		  }
        		  else
           			  rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
        		  return;
        	  }
        	  else
        	  {
        		  // Solicitud de envio a procesar
        		  if(request.getParameter("subproceso").equals("ENVIAR"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  if(moddes.equals("FACTURAS") || moddes.equals("GASTOS"))
        				  {
        					  if(request.getParameter("forma_pago").equals("contado"))
        					  {
        						  HttpSession ses = request.getSession(true);
        						  JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
        						  request.setAttribute("fsipg_tipo","compras");
        						  request.setAttribute("fsipg_proc", "retiro");
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
        							  // establece los atributos por default para compras de crédito
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
        						  // establece los atributos por default para compras de crédito
        						  request.setAttribute("fsipg_cambio", 0F);
        						  request.setAttribute("fsipg_efectivo", 0F);
        						  request.setAttribute("fsipg_bancos", 0F);      						  
        						  Agregar(request, response, moddes, setids, idmod, idmod4);
        						  return;
        					  }
        				  }
        			  }
        			  
        			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
        			  return;
        			  
        		  }
        		  else if(request.getParameter("subproceso").equals("AGR_PROVEE"))
        		  {
        			  AgregarCabecero(request,response);
        			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
        			  return;
        		  }
        		  else if(request.getParameter("subproceso").equals("AGR_PART"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  if(VerificarParametrosPartida(request, response))
        					  AgregarPartida(request, response);
        			  }
        			  
        			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
        			  return;
        		  }
        		  else if(request.getParameter("subproceso").equals("EDIT_PART"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  if(VerificarParametrosPartida(request, response))
        					  EditarPartida(request, response);
        			  }
	       		  
        			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
        			  return;
        		  }
        		  else if(request.getParameter("subproceso").equals("BORR_PART"))
        		  {
        			  if(AgregarCabecero(request,response) == -1)
        			  {
        				  BorrarPartida(request, response);
        			  }
	       		  
        			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
        			  return;
        		  }
        	  }
          }
          else if(request.getParameter("proceso").equals("DATOS_IMPORTACION"))
          {
        	 // Revisa si tiene permisos
        	 if(!getSesion(request).getPermiso("COMP_FAC_AGREGAR"))
             {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_FAC_AGREGAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "COMP_FAC_AGREGAR", "CFAC||||",mensaje);
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
              			if(moddes.equals("FACTURAS"))
              			{
              				JComprasFactSet SetMod = new JComprasFactSet(request);
              				SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
              				SetMod.Open();
        	            	
              				if(SetMod.getAbsRow(0).getStatus().equals("C"))
  		      	      		{
  		      	      			idmensaje = 1;
  		      	      			mensaje += "PRECAUCION: Esta compra ya esta cancelada, no se puede gestionar información de importación<br>";
  		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
  		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  		      	      			return;
  		      	      		} 
              				
              				JProveeProveeMasSetV2 pro = new JProveeProveeMasSetV2(request);
              				pro.m_Where = "ID_Clave = '" + SetMod.getAbsRow(0).getID_Proveedor() + "'";
              				pro.Open();
              				
              				if(SetMod.getAbsRow(0).getID_Proveedor() == 0 || SetMod.getAbsRow(0).getMoneda() == 1 || pro.getAbsRow(0).getPais().equals("MEX") || pro.getAbsRow(0).getPedimento().equals("--"))
              				{
              					idmensaje = 1;
  		      	      			mensaje += "PRECAUCION: Este proveedor es nacional, o es un proveedor extranjero con el cual no manejamos importaciones definitivas, o en su defecto, la compra no fue realizada en moneda extranjera. No se puede gestionar información de importación<br>";
  		      	      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
  		      	      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  		      	      			return;
              				}
              				
              				JComercioExteriorCabSet ext = new JComercioExteriorCabSet(request,"COMPRA");
              				ext.m_Where = "ID_VC = '" + p(request.getParameter("ID")) + "'";
              				ext.Open();
              				
        	            	
  		       			}
              			else // sale si no es factura
              				return;
                  	  
              			getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	    	            irApag("/forsetiweb/compras/comp_fact_dlg_datimp.jsp", request, response);
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
            	  		if(VerificarParametrosDatosImportacion(request, response))
            	  		{
            	  			DatosImportacion(request, response);
            	  			return;
            	  		}
           		  	 
            	  		irApag("/forsetiweb/compras/comp_fact_dlg_datimp.jsp", request, response);  
            	  		return;
            	  	}
              }
          }
          else if(request.getParameter("proceso").equals("CARGAR_COMPRA"))
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
        	          	  
        	  if(setids.getAbsRow(0).getFija())
        	  {
        		  idmensaje = 3; mensaje += "ERROR: No se puede subir ningun CFDI porque esta entidad de compra o gasto está establecida como Fija<br>";
			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			  	  return;
        	  }
        	  
        	  Integer subir_archivos = new Integer(2);
        	  request.setAttribute("subir_archivos", subir_archivos);
        		  
        	  HttpSession ses = request.getSession(true);
        	  JFacturasXML rec = (JFacturasXML)ses.getAttribute("comp_fact_xml");
        		  
        	  if(rec == null)
        	  {
        		  rec = new JFacturasXML();
        		  ses.setAttribute("comp_fact_xml", rec);
        	  }
        	  else
        	  {
        		  rec = null;
        		  rec = new JFacturasXML();
        		  ses.setAttribute("comp_fact_xml", rec);
        	  }
           			  
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/subir_archivos.jsp?verif=/servlet/CEFCompFactDlg&archivo_1=xml&archivo_2=pdf&proceso=CARGAR_COMPRA&subproceso=ENVIAR&moddes=" + moddes + "&idmod=" + idmod + "&idmod4=" + idmod4, request, response);
        	  return;
        	  
          }
          else if(request.getParameter("proceso").equals("CARGAR_OTROS"))
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
        	          	  
        	  if(setids.getAbsRow(0).getFija())
        	  {
        		  idmensaje = 3; mensaje += "ERROR: No se puede subir ningun otro Documento porque esta entidad de compra o gasto está establecida como Fija<br>";
			  	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			  	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
			  	  return;
        	  }
        	  
        	  Integer subir_archivos = new Integer(1);
        	  request.setAttribute("subir_otros", subir_archivos);
        		
        	  HttpSession ses = request.getSession(true);
        	  JFacturasXML rec = (JFacturasXML)ses.getAttribute("comp_fact_xml");
        		  
        	  if(rec == null)
        	  {
        		  rec = new JFacturasXML();
        		  ses.setAttribute("comp_fact_xml", rec);
        	  }
        	  else
        	  {
        		  rec = null;
        		  rec = new JFacturasXML();
        		  ses.setAttribute("comp_fact_xml", rec);
        	  }
           			  
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/compras/subir_otros.jsp?verif=/servlet/CEFCompFactDlg&proceso=CARGAR_OTROS&subproceso=ENVIAR&moddes=" + moddes + "&idmod=" + idmod + "&idmod4=" + idmod4, request, response);
        	  return;
        	  
          }
          else if(request.getParameter("proceso").equals("XML_COMPRA"))
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
            		  JComprasFactSet SetMod = new JComprasFactSet(request);
            		  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta compra no tiene un enlace a un CFDI, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
            		  String destino = "FAC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".xml";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La compra factura se bajo satisfactoriamente";
            		  return;
                  }
            	  else if(moddes.equals("GASTOS"))
                  {
            		  JComprasGastosSet SetMod = new JComprasGastosSet(request);
            		  SetMod.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getStatus().equals("C"))
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Este gasto no tiene un enlace a un CFDI, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  if(SetMod.getAbsRow(0).getID_CFD() != 0) // Solo tiene un CFDI asociado
            		  {
            			  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
            			  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            			  cfd.Open();
            		  
            			  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
            			  String destino = "GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".xml";
            			  JBajarArchivo fd = new JBajarArchivo();
            		  
            			  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  }
            		  else // Tiene varios CFDI asociados
            		  {
            			  JCompGastosCFDSet uuids = new JCompGastosCFDSet(request);
            			  uuids.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
            			  uuids.Open();
            			  String nombres [] = new String[uuids.getNumRows()];
            			  String destinos [] = new String[uuids.getNumRows()];
            			  
            			  for(int i = 0; i < uuids.getNumRows(); i++)
            			  {
            				  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
                			  cfd.m_Where = "ID_CFD = '" + uuids.getAbsRow(0).getID_CFD() + "'";
                			  cfd.Open();
                		  
                			  nombres[i] = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
                			  destinos[i] = "GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + "-" + (i+1) + ".xml";
            			  }
            			  
            			  JBajarArchivo fd = new JBajarArchivo();
                		  fd.doDownloadMultipleFilesInZip(response, getServletConfig().getServletContext(), ("GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + "-XMLs.zip"), nombres, destinos);
            			  
            		  }
            		  
            		  idmensaje = 0;
            		  mensaje = "La compra gasto se bajo satisfactoriamente";
            		  return;
                  }
            	  /*else if(moddes.equals("RECEPCIONES"))
                  {
            		  JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
            		  SetMod.m_Where = "ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta remisión no esta completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
            		  String destino = "REC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".xml";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La recepción se bajo satisfactoriamente";
            		  return;
                  }*/
            	  else if(moddes.equals("DEVOLUCIONES"))
                  {
            		  JComprasDevolucionesSet SetMod = new JComprasDevolucionesSet(request);
            		  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta devolución no tiene un enlace a un CFDI, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		   
            		  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/TFDs/" + cfd.getAbsRow(0).getUUID() + ".xml";
            		  String destino = "DSC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".xml";
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
        else if(request.getParameter("proceso").equals("PDF_COMPRA"))
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
            		  JComprasFactSet SetMod = new JComprasFactSet(request);
            		  SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() < 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta compra no tiene una asociación de CFDI, CBB o EXT, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  if(SetMod.getAbsRow(0).getTFD() == 3) //es factura electronica CFDI
            		  {
	            		  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
	            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
	            		  cfd.Open();
	            		  
	            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
	            		  String destino = "FAC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
	            		  JBajarArchivo fd = new JBajarArchivo();
	            		  
	            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  }
            		  else if(SetMod.getAbsRow(0).getTFD() == 4 || SetMod.getAbsRow(0).getTFD() == 5) //es CBB o EXT
            		  {
	            		  JCFDCompOtrSet cfd = new JCFDCompOtrSet(request);
	            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
	            		  cfd.Open();
	            		  
	            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/OTRs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
	            		  String destino = "FAC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
	            		  JBajarArchivo fd = new JBajarArchivo();
	            		  
	            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  }
            		  idmensaje = 0;
            		  mensaje = "La compra gasto se bajo satisfactoriamente";
            		  return;
                  }
            	  else if(moddes.equals("GASTOS"))
                  {
            		  JComprasGastosSet SetMod = new JComprasGastosSet(request);
            		  SetMod.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() < 3|| SetMod.getAbsRow(0).getStatus().equals("C"))
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Este gasto no tiene ninguna asociación de CFDIs, CBBs o EXTs, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  if(SetMod.getAbsRow(0).getTFD() == 3)
            		  {
	            		  if(SetMod.getAbsRow(0).getID_CFD() != 0) // Solo tiene un CFDI asociado
	            		  {
	            			  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
	            			  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
	            			  cfd.Open();
	            		  
	            			  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
	            			  String destino = "GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
	            			  JBajarArchivo fd = new JBajarArchivo();
	            		  
	            			  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
	            		  }
	            		  else // Tiene varios CFDI asociados
	            		  {
	            			  JCompGastosCFDSet uuids = new JCompGastosCFDSet(request);
	            			  uuids.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
	            			  uuids.Open();
	            			  String nombres [] = new String[uuids.getNumRows()];
	            			  String destinos [] = new String[uuids.getNumRows()];
	            			  
	            			  for(int i = 0; i < uuids.getNumRows(); i++)
	            			  {
	            				  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
	                			  cfd.m_Where = "ID_CFD = '" + uuids.getAbsRow(0).getID_CFD() + "'";
	                			  cfd.Open();
	                		  
	                			  nombres[i] = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
	                			  destinos[i] = "GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + "-" + (i+1) + ".pdf";
	            			  }
	            			  
	            			  JBajarArchivo fd = new JBajarArchivo();
	                		  fd.doDownloadMultipleFilesInZip(response, getServletConfig().getServletContext(), ("GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + "-PDFs.zip"), nombres, destinos);
	            			  
	            		  }
            		  }
            		  else if(SetMod.getAbsRow(0).getTFD() == 4 || SetMod.getAbsRow(0).getTFD() == 5 || SetMod.getAbsRow(0).getTFD() == 6)
            		  {
	            		  if(SetMod.getAbsRow(0).getID_CFD() != 0) // Solo tiene un CBB o EXT asociado
	            		  {
	            			  JCFDCompOtrSet cfd = new JCFDCompOtrSet(request);
	            			  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
	            			  cfd.Open();
	            		  
	            			  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/OTRs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
	            			  String destino = "GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
	            			  JBajarArchivo fd = new JBajarArchivo();
	            		  
	            			  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
	            		  }
	            		  else // Tiene varios CBB o Ext ASociados
	            		  {
	            			  JCompGastosCFDSet uuids = new JCompGastosCFDSet(request);
	            			  uuids.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
	            			  uuids.Open();
	            			  String nombres [] = new String[uuids.getNumRows()];
	            			  String destinos [] = new String[uuids.getNumRows()];
	            			  
	            			  for(int i = 0; i < uuids.getNumRows(); i++)
	            			  {
	            				  JCFDCompOtrSet cfd = new JCFDCompOtrSet(request);
	                			  cfd.m_Where = "ID_CFD = '" + uuids.getAbsRow(0).getID_CFD() + "'";
	                			  cfd.Open();
	                		  
	                			  nombres[i] = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/OTRs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
	                			  destinos[i] = "GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + "-" + (i+1) + ".pdf";
	            			  }
	            			  
	            			  JBajarArchivo fd = new JBajarArchivo();
	                		  fd.doDownloadMultipleFilesInZip(response, getServletConfig().getServletContext(), ("GAS-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + "-PDFs.zip"), nombres, destinos);
	            			  
	            		  }
            		  }
            		  
            		  idmensaje = 0;
            		  mensaje = "La compra gasto se bajo satisfactoriamente";
            		  return;
            		  
                  }
            	  else if(moddes.equals("RECEPCIONES"))
                  {
            		  JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
            		  SetMod.m_Where = "ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta remisi&oacute;n no est&aacute; completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
            		  String destino = "REC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "La remisi&oacute;n se bajo satisfactoriamente";
            		  return;
                  }
            	  else if(moddes.equals("DEVOLUCIONES"))
                  {
            		  JComprasDevolucionesSet SetMod = new JComprasDevolucionesSet(request);
            		  SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3 || SetMod.getAbsRow(0).getID_CFD() == 0)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Esta devolucion no est&aacute; completamente sellada, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
	              	  } 
            		  
            		  JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
            		  cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
            		  cfd.Open();
            		  
            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/PDFs/" + cfd.getAbsRow(0).getUUID() + ".pdf";
            		  String destino = "DSC-" + SetMod.getAbsRow(0).getID_Entidad() + "-" + SetMod.getAbsRow(0).getNumero() + ".pdf";
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
        else if(request.getParameter("proceso").equals("CAMBIAR_COMPRA"))
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
            			if(moddes.equals("ORDENES"))
            			{
            				JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
            				SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
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
            			else // sale si no es orden
            				return;
                	  
            			HttpSession ses = request.getSession(true);
            			JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
            			if(rec == null)
            			{
            				rec = new JCompFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            				ses.setAttribute("comp_fact_dlg", rec);
            			}
            			else
            			{
            				rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            			}
                    
            			// Llena el pedido o cotizacion
            			if(moddes.equals("ORDENES"))
            			{
            				JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
            				SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
    	            	
	    	            	// checa si se permite la 
	    	            	rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getProveedor());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	             }
            			
            			JComprasFactSetCab SetCab = new JComprasFactSetCab(request,request.getParameter("tipomov"));
            			JComprasFactSetDet SetDet = new JComprasFactSetDet(request,request.getParameter("tipomov"));
            			SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetCab.Open();
            			SetDet.Open();
    	            	
            			rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
            			rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
	    	            rec.setColonia(SetCab.getAbsRow(0).getColonia());
	    	            if(SetCab.getAbsRow(0).getCondicion() == 0)
	    	            	rec.setForma_Pago("contado");
	    	            else if(SetCab.getAbsRow(0).getCondicion() == 1)
	    	            	rec.setForma_Pago("credito");
	    	            else
	    	            	rec.setForma_Pago("ninguno");
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
	    	            irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
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
          	  				// establece los atributos por default para compras de crédito
          	  				request.setAttribute("fsipg_cambio", 0F);
          	  				request.setAttribute("fsipg_efectivo", 0F);
          	  				request.setAttribute("fsipg_bancos", 0F);      						  
          	  				Cambiar(request, response, moddes, idmod, idmod4);
          	  				return;
          	  			}
    
          	  		}
  	     		  	 
          	  		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("AGR_CLIENT"))
          	  	{
          	  		AgregarCabecero(request,response);
          	  		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("AGR_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartida(request, response))
          	  				AgregarPartida(request, response);
          	  		}
          	  		
          	  		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("EDIT_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartida(request, response))
          	  				EditarPartida(request, response);
          	  		}
          	  		
  	       			irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
  	       			return;
          	  	}
  	   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
  	   		  {
  	   			  if(AgregarCabecero(request,response) == -1)
  	   			  {
  	   				  BorrarPartida(request, response);
  	   			  }
  	       		  
  	   			  irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
   	   			  return;
  	   		  }
  	   	  }
           
        }        
        else if(request.getParameter("proceso").equals("CONSULTAR_COMPRA"))
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
            		JCompFactSes	rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
            		if(rec == null)
            		{
            			rec = new JCompFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            			ses.setAttribute("comp_fact_dlg", rec);
            		}
            		else
            			rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, moddes);
            		
            		// Llena la factura
            		if(moddes.equals("FACTURAS"))
            		{
            			JComprasFactSet SetMod = new JComprasFactSet(request);
            			SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getProveedor());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            			
            		}
            		else if(moddes.equals("GASTOS"))
            		{
            			JComprasGastosSet SetMod = new JComprasGastosSet(request);
            			SetMod.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getProveedor());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            		}
            		else if(moddes.equals("ORDENES"))
            		{
            			JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
            			SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getProveedor());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            		}
            		else if(moddes.equals("RECEPCIONES"))
            		{
            			JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
            			SetMod.m_Where = "ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getProveedor());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            		} 
            		else if(moddes.equals("DEVOLUCIONES"))
            		{
            			JComprasDevolucionesSet SetMod = new JComprasDevolucionesSet(request);
            			SetMod.m_Where = "ID_Devolucion = '" + p(request.getParameter("ID")) + "'";
            			SetMod.Open();
            			rec.setFactNum(SetMod.getAbsRow(0).getNumero());
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
            			rec.setFecha(SetMod.getAbsRow(0).getFecha());
            			rec.setNombre(SetMod.getAbsRow(0).getProveedor());
            			rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
            			rec.setTC(SetMod.getAbsRow(0).getTC());
            			rec.setTotal(SetMod.getAbsRow(0).getTotal());
            			rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
            			rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
            		}
                
            		
            		JComprasFactSetCab SetCab = new JComprasFactSetCab(request,moddes);
            		JComprasFactSetDet SetDet = new JComprasFactSetDet(request,moddes);
            		SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            		SetCab.Open();
            		SetDet.Open();
	            	
            		rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
            		rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
            		rec.setColonia(SetCab.getAbsRow(0).getColonia());
            		if(SetCab.getAbsRow(0).getCondicion() == 0)
    	            	rec.setForma_Pago("contado");
    	            else if(SetCab.getAbsRow(0).getCondicion() == 1)
    	            	rec.setForma_Pago("credito");
    	            else
    	            	rec.setForma_Pago("ninguno");
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
            		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CANCELAR_COMPRA"))
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
            	  if(moddes.equals("ORDENES"))
                  {
  	            	JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
  	                SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
  	            	SetMod.Open();
  	            	
  	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta orden ya está cancelada <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	}
  	            	else if(SetMod.getAbsRow(0).getStatus().equals("F"))
  	          		{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta orden ya tiene una factura o recepción asociada, no se puede cancelar. Primero debes cancelar la factura o recepción para poder cancelar la orden de compra <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	            		return;
  	            	} 
  	                else
  	                {
  	                   CancelarFactura(request, response, "ORDENES", idmod, idmod4);
  	                   return;
  	                }
                  }
            	  else if(moddes.equals("RECEPCIONES"))
                  {
  	            	JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
  	                SetMod.m_Where = "ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
  	            	SetMod.Open();
  	            	
  	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta recepción ya esta cancelada <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	} 
  	            	else if(SetMod.getAbsRow(0).getID_Factura() != 0)
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta recepción ya tiene una factura asociada. No se puede cancelar, primero cancela la factura para poder cancelar la recepción <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	                    return;
  	              	} 
  	            	else if( !setids.getAbsRow(0).getFijaCost() && setids.getAbsRow(0).getAuditarAlm() && !SetMod.getAbsRow(0).getStatus().equals("R") )
  	            	{
  	                    idmensaje = 1;
  	                    mensaje += "PRECAUCION: Esta recepción necesita estar revertida desde el m&oacute;dulo del almac&eacute;n para poder cancelarla <br>";
  	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	            		return;
  	            	} 
  	                else
  	                {
  	                   CancelarFactura(request, response, "RECEPCIONES", idmod, idmod4);
  	                   return;
  	                }
                  }
            	  if(moddes.equals("FACTURAS"))
                  {
	            	JComprasFactSet SetMod = new JComprasFactSet(request);
	                SetMod.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	            	SetMod.Open();
	            	JComprasRecepSetV2 SetRec = new JComprasRecepSetV2(request);
	                SetRec.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	            	SetRec.Open();
	            	JComprasDevolucionesSet SetDev = new JComprasDevolucionesSet(request);
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
  	            	else if(SetRec.getNumRows() == 0 && SetMod.getAbsRow(0).getID_PolCost() != -1 && !setids.getAbsRow(0).getFijaCost() && setids.getAbsRow(0).getAuditarAlm() && !SetMod.getAbsRow(0).getStatus().equals("R") )
	            	{
	                    idmensaje = 1;
	                    mensaje += "PRECAUCION: Esta factura necesita estar revertida desde el módulo del almacén para poder cancelarla <br>";
	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            		return;
	            	} 
  	            	else
	                {
	                   CancelarFactura(request, response, "FACTURAS", idmod, idmod4);
	                   return;
	                }
                  }
            	  else if(moddes.equals("DEVOLUCIONES"))
            	  {
            		JComprasDevolucionesSet SetMod = new JComprasDevolucionesSet(request);
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
  	                	CancelarFactura(request, response, "DEVOLUCIONES", idmod, idmod4);
  	                	return;
  	                }
            	  }
            	  else if(moddes.equals("GASTOS"))
                  {
	            	JComprasGastosSet SetMod = new JComprasGastosSet(request);
	                SetMod.m_Where = "ID_Gasto = '" + p(request.getParameter("ID")) + "'";
	            	SetMod.Open();
	            	
  	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
	            	{
	                    idmensaje = 1;
	                    mensaje += "PRECAUCION: Esta factura ya está cancelada <br>";
	                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	                    return;
	              	}
  	            	else
	                {
	                   CancelarFactura(request, response, "GASTOS", idmod, idmod4);
	                   return;
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
        else if(request.getParameter("proceso").equals("FACTURAR_COMPRA"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("COMP_FAC_AGREGAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_FAC_AGREGAR");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_FAC_AGREGAR","CFAC||||",mensaje);
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
            			if(moddes.equals("ORDENES"))
            			{
            				JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
            				SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
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
	                	else if(moddes.equals("RECEPCIONES"))
	                    {
	      	            	JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
	      	            	SetMod.m_Where = "ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
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
	                	else // sale si no es orden o recepcion
	                		return;
	                	  
            			HttpSession ses = request.getSession(true);
            			JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
            			if(rec == null)
            			{
            				rec = new JCompFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "FACTURAS");
            				ses.setAttribute("comp_fact_dlg", rec);
            			}
            			else
            			{
            				rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "FACTURAS");
            			}
                    
            			// Llena la factura
            			if(moddes.equals("ORDENES"))
            			{
            				JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
            				SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
            				SetMod.Open();
    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getProveedor());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            }
	                    else if(moddes.equals("RECEPCIONES"))
	                    {
	    	            	JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
	    	            	SetMod.m_Where = "ID_Recepcion = '" + p(request.getParameter("ID")) + "'";
	    	            	SetMod.Open();
	    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getProveedor());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            	
	    	            	if(SetMod.getAbsRow(0).getID_CFD() != 0)
	    	            	{
	    	            		JCFDCompSet cfd = new JCFDCompSet(request,"COMPRAS");
	    	            		cfd.m_Where = "ID_CFD = '" + SetMod.getAbsRow(0).getID_CFD() + "'";
	    	            		cfd.Open();
	    	            		
	    	            		rec.setUUID(cfd.getAbsRow(0).getUUID());
	    	            	}
	    	            }
	                    JComprasFactSetCab SetCab = new JComprasFactSetCab(request,request.getParameter("tipomov"));
	    	            JComprasFactSetDet SetDet = new JComprasFactSetDet(request,request.getParameter("tipomov"));
	    	            SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetCab.Open();
	    	            SetDet.Open();
	    	            	
	    	            rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
	    	            rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
	    	            rec.setColonia(SetCab.getAbsRow(0).getColonia());
	    	            if(SetCab.getAbsRow(0).getCondicion() == 0)
	    	            	rec.setForma_Pago("contado");
	    	            else if(SetCab.getAbsRow(0).getCondicion() == 1)
	    	            	rec.setForma_Pago("credito");
	    	            else
	    	            	rec.setForma_Pago("ninguno");
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
	                    irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
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
  	       	  		if(moddes.equals("ORDENES") || 
  	       						moddes.equals("RECEPCIONES") )
  	       	  		{
  	       	  			if(request.getParameter("fecha") == null  || request.getParameter("referencia") == null || 
  	       	  					request.getParameter("fecha").equals("") )
  	       	  			{
  	       	  				idmensaje = 1; mensaje += "PRECAUCION: Se debe enviar la fecha y referencia de la factura <br>";
  	       	  				getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	       	  				irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
  	       	  				return;
  	       	  			}
  	       	  			else if(request.getParameter("forma_pago").equals("contado"))
  	       	  			{
  	       	  				HttpSession ses = request.getSession(true);
  	       	  				JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");	
         			  
  	       	  				request.setAttribute("fsipg_tipo","compras");
  	       	  				request.setAttribute("fsipg_proc", "retiro");
  	       	  				request.setAttribute("fsipg_total",rec.getTotal());
  	       	  				request.setAttribute("fsipg_ident",getSesion(request).getSesion(idmod).getEspecial());
  	       				
  	       	  				if(VerificarParametros(request, response) && VerificarPagoMult(request, response))
  	       	  				{
  	       	  					AgregarDesde(request, response, "CFAC", "COMP_FAC", request.getParameter("ID"), idmod4, idmod, setids);
  	       	  					return;
  	       	  				}
  	       	  				irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
	       	  				return;
  	       	  			}
  	       	  			else
  	       	  			{
  	       	  				if(VerificarParametros(request, response))
  	       	  				{
  	       	  					// establece los atributos por default para compras de crédito
  	       	  					request.setAttribute("fsipg_cambio", 0F);
  	       	  					request.setAttribute("fsipg_efectivo", 0F);
  	       	  					request.setAttribute("fsipg_bancos", 0F);		
  	       	  					AgregarDesde(request, response, "CFAC", "COMP_FACT", request.getParameter("ID"), idmod4, idmod, setids);
  	       	  					return;
  	       	  				}
  	       	  				irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
  	       	  				return;
  	       	  			}
  	       	  		}
  	       		 
  	       	  	}
            }
           
        } 
        else if(request.getParameter("proceso").equals("RECIBIR_COMPRA"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("COMP_REC_AGREGAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_REC_AGREGAR");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_REC_AGREGAR","CREC||||",mensaje);
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
            			if(moddes.equals("ORDENES"))
            			{
            				JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
            				SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
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
            			else // sale si no es orden
            				return;
                	  
            			HttpSession ses = request.getSession(true);
            			JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
	                    if(rec == null)
	                    {
	        	            rec = new JCompFactSes(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "RECEPCIONES");
	        	            ses.setAttribute("comp_fact_dlg", rec);
	                    }
	                    else
	                    {
	                        rec.resetear(request, getSesion(request).getSesion(idmod).getEspecial(), usuario, "RECEPCIONES");
	                    }
	                    
	                    // Llena la factura
	                    if(moddes.equals("ORDENES"))
	                    {
	    	            	JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
	    	            	SetMod.m_Where = "ID_Orden = '" + p(request.getParameter("ID")) + "'";
	    	            	SetMod.Open();
	    	            	
	    	            	//rec.setFactNum(SetMod.getAbsRow(0).getNumero());
	    	            	rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
	    	            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
	    	            	rec.setNombre(SetMod.getAbsRow(0).getProveedor());
	    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
	    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
	    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
	    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
	    	            	rec.setFechaEntrega(SetMod.getAbsRow(0).getFecha());
	    	            }
	                    
	    	            JComprasFactSetCab SetCab = new JComprasFactSetCab(request,request.getParameter("tipomov"));
	    	            JComprasFactSetDet SetDet = new JComprasFactSetDet(request,request.getParameter("tipomov"));
	    	            SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
	    	            SetCab.Open();
	    	            SetDet.Open();
	    	            
	    	            rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
	    	            rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
	    	            rec.setColonia(SetCab.getAbsRow(0).getColonia());
	    	            if(SetCab.getAbsRow(0).getCondicion() == 0)
	    	            	rec.setForma_Pago("contado");
	    	            else if(SetCab.getAbsRow(0).getCondicion() == 1)
	    	            	rec.setForma_Pago("credito");
	    	            else
	    	            	rec.setForma_Pago("ninguno");
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
	                    irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
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
  	       	  		if(moddes.equals("ORDENES"))
  	       	  		{
  	       	  			if(request.getParameter("fecha") == null  || request.getParameter("referencia") == null || 
  	       	  					request.getParameter("fecha").equals("") )
  	       	  			{
  	       	  				idmensaje = 1; mensaje += "PRECAUCION: Se debe enviar la fecha y referencia de la remisi&oacute;n <br>";
  	       	  				getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	       	  				irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
  	       	  				return;
  	       	  			}
  	       	  			else
  	       	  			{
  	       	  				if(VerificarParametros(request, response))
  	       	  				{
  	       	  					AgregarDesde(request, response, "CREC", "COMP_REC", request.getParameter("ID"), idmod4, idmod, setids);
  	       	  					return;
  	       	  				}
  	       	  				irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
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
        					SQLCab += "view_compras_facturas_impcab where ID_Factura = ";
        					SQLDet += "view_compras_facturas_impdet where ID_Factura = ";
        				}
        				else if(moddes.equals("ORDENES"))
        				{
        					SQLCab += "view_compras_ordenes_impcab where ID_Orden = ";
        					SQLDet += "view_compras_ordenes_impdet where ID_Orden = ";
        				}
        				else if(moddes.equals("RECEPCIONES"))
        				{
        					SQLCab += "view_compras_recepciones_impcab where ID_Recepcion = ";
        					SQLDet += "view_compras_recepciones_impdet where ID_Recepcion = ";
        				}
        				else if(moddes.equals("GASTOS"))
        				{
        					SQLCab += "view_compras_gastos_impcab where ID_Gasto = ";
        					SQLDet += "view_compras_gastos_impdet where ID_Gasto = ";
        				}
        				else // DEVOLUCIONES
        				{
        					SQLCab += "view_compras_devoluciones_impcab where ID_Devolucion = ";
        					SQLDet += "view_compras_devoluciones_impdet where ID_Devolucion = ";
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
        					request.setAttribute("impresion", "CEFCompFactDlg");
        					request.setAttribute("tipo_imp", "COMP_FAC");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFormato());
        				}
        				else if(moddes.equals("ORDENES"))
        				{
        					request.setAttribute("impresion", "CEFCompFactDlg");
        					request.setAttribute("tipo_imp", "COMP_ORD");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Orden());
        				}
        				else if(moddes.equals("RECEPCIONES"))
        				{
        					request.setAttribute("impresion", "CEFCompFactDlg");
        					request.setAttribute("tipo_imp", "COMP_REC");
        					request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Recepcion());
        				}
        				else if(moddes.equals("GASTOS"))
        				{
        					request.setAttribute("impresion", "CEFCompFactDlg");
        					request.setAttribute("tipo_imp", "COMP_GAS");
        				}
        				else // DEVOLUCIONES
        				{
        					request.setAttribute("impresion", "CEFCompFactDlg");
        					request.setAttribute("tipo_imp", "COMP_DEV");
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
        else if(request.getParameter("proceso").equals("DEVOLVER_COMPRA") || request.getParameter("proceso").equals("REBAJAR_COMPRA"))
        {
            // Revisa si tiene permisos
            if(request.getParameter("proceso").equals("DEVOLVER_COMPRA") && !getSesion(request).getPermiso("COMP_DEV_DEVOLVER"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_DEV_DEVOLVER");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_DEV_DEVOLVER","CDEV||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            if(request.getParameter("proceso").equals("REBAJAR_COMPRA") && !getSesion(request).getPermiso("COMP_DEV_REBAJAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_DEV_REBAJAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_DEV_REBAJAR","CDEV||||",mensaje);
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
            			JComprasFactSet SetMod = new JComprasFactSet(request);
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
		
            			if(request.getParameter("proceso").equals("DEVOLVER_COMPRA"))
            			{	
	            			if(SetMod.getAbsRow(0).getID_PolCost() == -1 || (!SetMod.getAbsRow(0).getStatus().equals("E") && setids.getAbsRow(0).getAuditarAlm()) )
	            			{
	            				idmensaje = 1;
	            				mensaje += "PRECAUCION: Esta factura debe estar guardada, revertida, o ser factura sin movimiento al almacén. Solo se pueden devolver las facturas emitidas con movimientos al almacén <br>";
	            				getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	            				return;
	            			}  
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
            			JCompFactSes rec = (JCompDevSes)ses.getAttribute("comp_dev_dlg");
            			if(rec == null)
            			{
            				rec = new JCompDevSes(request, getSesion(request).getSesion("COMP_FAC").getEspecial(), usuario, "DEVOLUCIONES");
            				ses.setAttribute("comp_dev_dlg", rec);
            			}
            			else
            				rec.resetear(request, getSesion(request).getSesion("COMP_FAC").getEspecial(), usuario, "DEVOLUCIONES");
                         
            			rec.setClave((int)SetMod.getAbsRow(0).getID_Proveedor());
    	            	rec.setNombre(SetMod.getAbsRow(0).getProveedor());
    	            	rec.setID_Moneda(SetMod.getAbsRow(0).getMoneda());
    	            	rec.setTC(SetMod.getAbsRow(0).getTC());
    	            	rec.setTotal(SetMod.getAbsRow(0).getTotal());
    	            	rec.setReferencia(SetMod.getAbsRow(0).getReferencia());
    	            	rec.setID_Factura(SetMod.getAbsRow(0).getID_Factura());
    	            	if(request.getParameter("proceso").equals("DEVOLVER_COMPRA"))
    	            		rec.setDevReb("DEV");
    	            	else
    	            		rec.setDevReb("REB");
    	            	JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"FACTURAS");
            			JComprasFactSetDet SetDet = new JComprasFactSetDet(request,"FACTURAS"/*"AGR_DEVOL"*/);
            			SetCab.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetDet.m_Where = "ID_Factura = '" + p(request.getParameter("ID")) + "'";
            			SetCab.Open();
            			SetDet.Open();
    	            	
            			rec.setMoneda(SetCab.getAbsRow(0).getMoneda());
    	            	rec.setNumero((int)SetCab.getAbsRow(0).getNumero());
    	            	rec.setColonia(SetCab.getAbsRow(0).getColonia());
    	            	if(SetCab.getAbsRow(0).getCondicion() == 0)
	    	            	rec.setForma_Pago("contado");
	    	            else if(SetCab.getAbsRow(0).getCondicion() == 1)
	    	            	rec.setForma_Pago("credito");
	    	            else
	    	            	rec.setForma_Pago("ninguno");
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
            			irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
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
            			JCompFactSes rec = (JCompDevSes)ses.getAttribute("comp_dev_dlg");
            			
            			if(rec.getForma_Pago().equals("contado"))
       				  	{
       				  		request.setAttribute("fsipg_tipo","compras");
       				  		request.setAttribute("fsipg_proc", "deposito");
						  	request.setAttribute("fsipg_total",rec.getTotal());
       				  		request.setAttribute("fsipg_ident",getSesion(request).getSesion("COMP_FAC").getEspecial());
       				  		request.setAttribute("fsipg_id_concepto", 0);
       				  		request.setAttribute("fsipg_desc_concepto", "");
       				  		if(VerificarParametrosDev(request, response) && VerificarPagoMult(request, response))
       				  		{
       				  			AgregarDev(request, response, setids);
       				  			return;
       				  		}
       				  	}
            			else if(rec.getForma_Pago().equals("credito"))
                		{
           					 request.setAttribute("fsipg_tipo","compras");
           					 if(VerificarParametrosDev(request, response) && VerificarSaldo(request, response))
           					 {
           						 AgregarDev(request, response, setids);
           						 return;
           					 }
           				}
            			else //if(rec.getForma_Pago().equals("ninguno"))
            			{
          					 request.setAttribute("fsipg_tipo","compras");
          					 if(VerificarParametrosDev(request, response))
          					 {
          						 AgregarDev(request, response, setids);
          						 return;
          					 }
          				}	
            			
            		}
            		
            		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
      			  	return;
            		           		
  	       	  	}
            	else if(request.getParameter("subproceso").equals("EDIT_PART"))
  	   		  	{
            		if(AgregarCabeceroDev(request,response) == -1)
            		{
            			if(VerificarParametrosPartidaDev(request, response))
            				EditarPartidaDev(request, response);
            		}
  	       		  	
  	       		  	irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
  	       		  	return;
  	   		  	}
  	   		  	else if(request.getParameter("subproceso").equals("BORR_PART"))
  	   		  	{
  	   		  		if(AgregarCabeceroDev(request,response) == -1)
  	   		  		{
  	   		  			BorrarPartidaDev(request, response);
  	   		  		}
  	   		  		
  	   		  		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
  	   		  		return;
  	   		  	}
            	
            	idmensaje = 1; mensaje += "PRECAUCION: No se pueden agregar partidas a una devolucion o rebaja. Intenta editar o borrar lo que no desees devolver o rebajar"; 
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);  
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
            	  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion(idmod).generarTitulo(JUtil.Msj("CEF",idmod,"VISTA","CONSULTAR_COMPRA",3)),
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
         request.getParameter("iva") != null && request.getParameter("ieps") != null && request.getParameter("ivaret") != null && request.getParameter("isrret") != null && request.getParameter("obs_partida") != null &&
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
        JCompDevSes rec = (JCompDevSes)ses.getAttribute("comp_dev_dlg");
         
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append("PRECAUCION: La devolución no contiene partidas <br>");
   	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
        
        if(request.getParameter("proceso").equals("DEVOLVER_COMPRA"))
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
        JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
         
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append("PRECAUCION: El elemento de compras no contiene partidas <br>");
   	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
            
        if(rec.getForma_Pago().equals("credito") && rec.getClave() == 0)
        {
  	        idmensaje = 1; mensaje.append("PRECAUCION: Una compra de mostrador no se puede pagar a crédito <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
        
        if(request.getParameter("tipomov").equals("FACTURAS") || request.getParameter("tipomov").equals("RECEPCIONES"))
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

        	if(idmensaje != -1)
        		return false;
        }
        else if(request.getParameter("proceso").equals("FACTURAR") || request.getParameter("proceso").equals("RECIBIR"))
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

        	if(idmensaje != -1)
        		return false;
        }
        
        return true;
	
    }
    
    public boolean VerificarParametrosDatosImportacion(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
    	
    	if(request.getParameter("tipooperacion") != null &&
    		request.getParameter("certificadoorigen") != null &&
    		request.getParameter("numcertificadoorigen") != null &&
    		request.getParameter("numeroexportadorconfiable") != null &&
    		request.getParameter("incoterm") != null &&
    		request.getParameter("subdivision") != null &&
    		request.getParameter("observaciones") != null &&
    		request.getParameter("tipocambiousd") != null &&
    		request.getParameter("totalusd") != null &&
    		request.getParameter("emisor_curp") != null &&
    		request.getParameter("receptor_curp") != null &&
    		request.getParameter("receptor_numregidtrib") != null &&
    		request.getParameter("destinatario_numregidtrib") != null &&
    		request.getParameter("destinatario_rfc") != null &&
    		request.getParameter("destinatario_curp") != null &&
    		request.getParameter("destinatario_nombre") != null &&
    		request.getParameter("destinatario_domicilio_calle") != null &&
    		request.getParameter("destinatario_domicilio_numeroexterior") != null &&
    		request.getParameter("destinatario_domicilio_numerointerior") != null &&
    		request.getParameter("destinatario_domicilio_colonia") != null &&
    		request.getParameter("destinatario_domicilio_localidad") != null &&
    		request.getParameter("destinatario_domicilio_referencia") != null &&
    		request.getParameter("destinatario_domicilio_municipio") != null &&
    		request.getParameter("destinatario_domicilio_estado") != null &&
    		request.getParameter("destinatario_domicilio_pais") != null &&
    		request.getParameter("destinatario_domicilio_codigopostal") != null )
    	{
    		if(request.getParameter("tipooperacion").equals("-"))
    		{
    			idmensaje = 3; mensaje.append("ERROR: Es indispensable seleccionar el tipo de operación de comercio exterior <br>");
       	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       	        return false;
    		}
    		
    		boolean flag = true;
    		double tcusd, totusd;
    		try 
     	   	{
    			tcusd = (request.getParameter("tipocambiousd").equals("") ? 0.00 : Double.parseDouble(request.getParameter("tipocambiousd")));
    			totusd = (request.getParameter("totalusd").equals("") ? 0.00 : Double.parseDouble(request.getParameter("totalusd")));
    			  
    			if( tcusd < 0.0 || totusd < 0.0)
    				flag = false;
    		
     	   	}
     	   	catch(NumberFormatException e) 
     	   	{
     	   		flag = false;
     	   	}
    		if(!flag)
    		{
    			idmensaje = 1;
    			mensaje.append("PRECAUCION: El tipo de cambio, el total o ambos, son incorrectos o son menores que cero. <br>");
    			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    			return false;
    		}
    		//Para el atributo cce:ComercioExterior:TipoOperacion, si la clave registrada es {A}, no deben existir los atributos [ClaveDePedimento]-1, [CertificadoOrigen]-1, [NumCertificadoOrigen], [NumeroExportadorConfiable], [Incoterm], [Subdivision]-1, [TipoCambioUSD] y [TotalUSD], ni el nodo [Mercancias].            
    		if(request.getParameter("tipooperacion").equals("A") && (!request.getParameter("certificadoorigen").equals("-1") || !request.getParameter("numcertificadoorigen").equals("") || !request.getParameter("numeroexportadorconfiable").equals("") || !request.getParameter("incoterm").equals("") || !request.getParameter("subdivision").equals("-1") || !request.getParameter("tipocambiousd").equals("") || !request.getParameter("totalusd").equals("")))
    		{
    			idmensaje = 1; mensaje.append("PRECAUCION: En operaciones de servicios, no se deben establecer valores de Certificado de origen, No. del certificado de origen, No. de importador, Termino comercial, Subdivision en factura, Total ni Tipo de cambio <br>");
       	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       	        return false;
    		}
    		//Para el atributo cce:ComercioExterior:TipoOperacion, si la clave registrada es {1} ó {2}, deben existir los atributos [ClaveDePedimento], [CertificadoOrigen], [Incoterm], [Subdivision], [TipoCambioUSD] y [TotalUSD], así como el nodo [Mercancias].
    		if(request.getParameter("tipooperacion").equals("2") && (request.getParameter("certificadoorigen").equals("-1") || request.getParameter("incoterm").equals("") || request.getParameter("subdivision").equals("-1") || request.getParameter("tipocambiousd").equals("") || request.getParameter("totalusd").equals("")))
    		{
    			idmensaje = 1; mensaje.append("PRECAUCION: En operaciones de bienes, se deben establecer los valores de Certificado de origen, No. del certificado de origen, No. de importador, Termino comercial, Subdivision en factura, Total y Tipo de cambio <br>");
       	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       	        return false;
    		}
    		//Para el atributo cce:ComercioExterior:CertificadoOrigen, si el valor es cero, no debe registrarse el atributo [NumCertificadoOrigen].
    		if(request.getParameter("certificadoorigen").equals("0") && !request.getParameter("numcertificadoorigen").equals(""))
    		{
    			idmensaje = 1; mensaje.append("PRECAUCION: Cuando no funje como certificado de origen, no debe capturarse ningun no. de certificado de origen <br>");
       	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       	        return false;
    		}
    		//Para el nodo cce:ComercioExterior:Destinartario, debe existir al menos uno de los atributos [NumRegIdTrib] o [Rfc]
    		if(request.getParameter("destinatario_numregidtrib").equals("") && request.getParameter("destinatario_rfc").equals(""))
    		{
    			idmensaje = 1; mensaje.append("PRECAUCION: Se debe registrar por lo menos el RFC o el Registro Tributario del Remitente <br>");
       	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       	        return false;
    		}
    		//El atributo cce:ComercioExterior:Destinatario:Rfc no debe ser rfc genérico {XAXX010101000} ni {XEXX010101000}.
    		if(request.getParameter("destinatario_rfc").equals("XAXX010101000") || request.getParameter("destinatario_rfc").equals("XEXX010101000"))
    		{
    			idmensaje = 1; mensaje.append("PRECAUCION: El RFC del Remitente no debe ser rfc genérico {XAXX010101000} ni {XEXX010101000} <br>");
       	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       	        return false;
    		}
    		
    		// Ahora verifica las mercancias
    		JComercioExteriorDetSet det = new JComercioExteriorDetSet(request,"COMPRA");
    		det.m_Where = "ID_VC = '" + p(request.getParameter("ID")) + "'";
    		det.m_OrderBy = "Partida ASC";
    		det.Open();
    		String noidentificacion = "", fraccionarancelaria = "";
    		double cantidadaduana = 0.0, valorunitarioaduana = 0.0, valordolares = 0.0; 
    		
    		flag = true;
    		for(int i = 0; i< det.getNumRows(); i++)
    		{
    		   noidentificacion = request.getParameter("noidentificacion_" + det.getAbsRow(i).getPartida());
         	   fraccionarancelaria = request.getParameter("fraccionarancelaria_" + det.getAbsRow(i).getPartida());
        	   if(fraccionarancelaria.equals(""))
        	   {
        		   flag = false;
        		   break;
        	   }
        	   
        	   try 
        	   {
        		   cantidadaduana = Double.parseDouble(request.getParameter("cantidadaduana_" + det.getAbsRow(i).getPartida()));
        		   valorunitarioaduana = Double.parseDouble(request.getParameter("valorunitarioaduana_" + det.getAbsRow(i).getPartida()));
        		   valordolares = Double.parseDouble(request.getParameter("valordolares_" + det.getAbsRow(i).getPartida()));
        		   	  
        		   if( cantidadaduana < 0.0 || valorunitarioaduana < 0.0 || valordolares < 0.0)
        		   {
        			   flag = false;
        			   break;
        		   }
        	   }
        	   catch(NumberFormatException e) 
        	   {
        		   flag = false;
        		   break;
        	   }
           }
           if(!flag)
           {
               idmensaje = 1;
               mensaje.append("PRECAUCION: El arancel o la candidad, costo o valor del producto " + noidentificacion + " es incorreco o es menor que cero. <br>");
               getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
               return false;
           }
    		
           return true;
           
    	}
    	else
    	{
    		idmensaje = 3;
            mensaje.append("ERROR: Alguno de los parametros del cabecero es nulo. <br>");
            getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
            return false;
    	}
    	
    	
    	
    }
    
    public short AgregarCabeceroDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
       	HttpSession ses = request.getSession(true);
       	JCompFactSes rec = (JCompDevSes)ses.getAttribute("comp_dev_dlg");
 
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
     
    public short AgregarRecursos(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
                  
    	HttpSession ses = request.getSession(true);
    	JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
     
    	idmensaje = rec.agregaRecursos(request, mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

    	return idmensaje;
    }
    
    
    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
       	HttpSession ses = request.getSession(true);
       	JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
 
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
  
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");

        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.agregaPartida(request, cantidad, request.getParameter("idprod"), request.getParameter("precio"), request.getParameter("descuento"), request.getParameter("iva"), request.getParameter("ieps"), request.getParameter("ivaret"), request.getParameter("isrret"), pt(request.getParameter("obs_partida")), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   
    }

    public void EditarPartidaDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JCompDevSes rec = (JCompDevSes)ses.getAttribute("comp_dev_dlg");

      float cantidad = Float.parseFloat(request.getParameter("cantidad"));
      float precio = Float.parseFloat(request.getParameter("precio"));
      idmensaje = rec.editarPartida(Integer.parseInt(request.getParameter("idpartida")), request, cantidad, request.getParameter("idprod"), precio, request.getParameter("descuento"), request.getParameter("iva"), request.getParameter("ieps"), request.getParameter("ivaret"), request.getParameter("isrret"), p(request.getParameter("obs_partida")), mensaje);
      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      
    }
    
    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");

        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, cantidad, request.getParameter("idprod"), request.getParameter("precio"), request.getParameter("descuento"), request.getParameter("iva"), request.getParameter("ieps"), request.getParameter("ivaret"), request.getParameter("isrret"), pt(request.getParameter("obs_partida")), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
  
    }

    public void BorrarPartidaDev(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JCompFactSes rec = (JCompDevSes)ses.getAttribute("comp_dev_dlg");

      rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      //irApag("/forsetiweb/compras/comp_dev_dlg.jsp", request, response);

    }
   
    
    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");

        rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
 
    }

    public void AgregarDesde(HttpServletRequest request, HttpServletResponse response, String idmodAgregar4, String idmodAgregar, String id_enlace, String idmod4, String idmod, JComprasEntidadesSetIdsV2 set)
    	throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
    	
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_FACTURAS_DET (\n";
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
    		tbl += "\n\ninsert into _TMP_COMPRAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getID_Prod()) + 
    		"','" + rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getDescuento() + "','" + rec.getPartida(i).getIVA() + "','" + p(rec.getPartida(i).getObsPartida()) + 
    		"','" + rec.getPartida(i).getImporte() + "','" + rec.getPartida(i).getImporteDesc() + "','" + rec.getPartida(i).getImporteIVA() + "','" + rec.getPartida(i).getTotalPart() + 
    		"','" + rec.getPartida(i).getIEPS() + "','" + rec.getPartida(i).getImporteIEPS() + "','" + rec.getPartida(i).getIVARet() + "','" + rec.getPartida(i).getImporteIVARet() + "','" + rec.getPartida(i).getISRRet() + "','" + rec.getPartida(i).getImporteISRRet() + 
    		"','" + p(rec.getPartida(i).getID_Tipo()) + "','" + p(rec.getPartida(i).getUnidad()) + "','" + rec.getPartida(i).getPrecio() + "');";
        }
    	
    	int forma_pago;
    	if(rec.getForma_Pago().equals("contado"))
    		forma_pago = 0;
    	else if(rec.getForma_Pago().equals("credito"))
    		forma_pago = 1;
    	else //Ninguno
    		forma_pago = 3;
    	
    	String str, del;
    	if(idmodAgregar4.equals("CFAC"))
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
    		
    		str = "select * from sp_compras_facturas_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("referencia")) + 
    	        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
    	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
    	        	"','" + rec.getID_Bodega() + "','" + p(id_enlace) + "','" + rec.getID_Vendedor() + "','" + p(idmod4) + "','" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    	    	    		
    		del = "\nDROP TABLE _TMP_COMPRAS_FACTURAS_DET;\n\nDROP TABLE _TMP_PAGOS;";
            
        }
    	else 
    	{
    		if(idmodAgregar4.equals("CORD"))
    			str = "select * from sp_compras_ordenes_agregar('";
    		else //if(idmodAgregar4.equals("CREC"))
    			str = "select * from sp_compras_recepciones_agregar('";
    		
    		str += rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("referencia")) + 
        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','0','0','0" + 
        	"','" + rec.getID_Bodega() + "','" + p(id_enlace) + "','" + rec.getID_Vendedor() + "','" + p(idmod4) + "','" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    		
    		del = "DROP TABLE _TMP_COMPRAS_FACTURAS_DET";
        	
    	}
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, tbl, str, del, rfb);

		short idmensaje = (short)rfb.getIdmensaje();
		String mensaje = rfb.getRes();
		
        RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmodAgregar + "_AGREGAR", idmodAgregar4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
        irApag("/forsetiweb/compras/comp_fact_dlg_generar.jsp", request, response);
        
    }
   
    public void AgregarDev(HttpServletRequest request, HttpServletResponse response, JComprasEntidadesSetIdsV2 set)
    	throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
    	JCompFactSes rec = (JCompDevSes)ses.getAttribute("comp_dev_dlg");
  	
    	String tbl;
    	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_FACTURAS_DET (\n";
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
    		tbl += "\n\ninsert into _TMP_COMPRAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getID_Prod()) + 
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
		
       	int forma_pago;
    	if(rec.getForma_Pago().equals("contado"))
    		forma_pago = 0;
    	else if(rec.getForma_Pago().equals("credito"))
    		forma_pago = 1;
    	else //Ninguno
    		forma_pago = 3;
    	
       	String str, devrebperm;
       	
       	if(rec.getDevReb().equals("DEV"))
       		devrebperm = "COMP_DEV_DEVOLVER";
       	else
       		devrebperm = "COMP_DEV_REBAJAR";
     
       	if(rec.getForma_Pago().equals("contado"))
       	{	
       		str = "select * from sp_compras_devoluciones_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(request.getParameter("referencia")) + 
    			"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
 	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
 	        	"','" + rec.getID_Bodega() + "','" + rec.getID_Factura() + "','" + rec.getID_Vendedor() + "',null,'','" + p(rec.getDevReb()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
       	}
       	else if(rec.getForma_Pago().equals("credito"))
       	{
       		str = "select * from sp_compras_devoluciones_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(request.getParameter("referencia")) + 
    			"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
 	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','0','0','0" + 
 	        	"','" + rec.getID_Bodega() + "','" + rec.getID_Factura() + "','" + rec.getID_Vendedor() + "','" + (Integer)request.getAttribute("fsipg_id_concepto") + "','" + p((String)request.getAttribute("fsipg_desc_concepto")) + "','" + p(rec.getDevReb()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
       	}
       	else //if(rec.getForma_Pago().equals("ninguno"))
       	{
       		str = "select * from sp_compras_devoluciones_agregar('" + rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(request.getParameter("referencia")) + 
    			"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
 	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','0','0','0" + 
 	        	"','" + rec.getID_Bodega() + "','" + rec.getID_Factura() + "','" + rec.getID_Vendedor() + "','-1','','" + p(rec.getDevReb()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
       	}
    	//doDebugSQL(request, response, str + "<p>" + tbl);
        JRetFuncBas rfb = new JRetFuncBas();
  		
  		doCallStoredProcedure(request, response, tbl, str, "\nDROP TABLE _TMP_COMPRAS_FACTURAS_DET;\n\nDROP TABLE _TMP_PAGOS;", rfb);

  		short idmensaje = (short)rfb.getIdmensaje();
  		String mensaje = rfb.getRes();
  		        
  		RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), devrebperm, "CDEV|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("COMP_FAC").getEspecial() + "||",mensaje);
        irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
    	
    }
    
    public void Enlazar(HttpServletRequest request, HttpServletResponse response, String tipomov, String idmod, String idmod4, String uuid, String tipoEnlace)
    		throws ServletException, IOException
    {
    	String enlace;   	
    	if(tipomov.equals("FACTURAS"))
    		enlace = "FAC";
    	else if(tipomov.equals("RECEPCIONES"))
    	    enlace = "REC";
    	else if(tipomov.equals("GASTOS"))
    	    enlace = "GAS";
    	else //if(tipomov.equals("DEVOLUCIONES"))
    		enlace = "DSC";
    	
    	String str = "";
    	
    	if(tipoEnlace.equals("UUID"))
    	{
    		str += "select * from " + (uuid.length() == 36 ? "sp_cfd_enlazar" : "sp_cfd_enlazar_uuids");
    	   	str += "('" + p(enlace) + "','" + p(request.getParameter("ID")) + "','" + q(uuid) + "') as ( err integer, res varchar, clave integer );";
    	}
    	else
    	{
    		str += "select * from " + (uuid.length() == 36 ? "sp_cfd_enlazarcbbext" : "sp_cfd_enlazarcbbext_uuids");
    		str += "('" + p(enlace) + "','" + p(request.getParameter("ID")) + "','" + q(uuid) + "') as ( err integer, res varchar, clave integer );";
        }
    	//doDebugSQL(request, response, str);
    	
    	JRetFuncBas rfb = new JRetFuncBas();
    	doCallStoredProcedure(request, response, str, rfb);

    	short idmensaje = (short)rfb.getIdmensaje();
    	String mensaje = rfb.getRes();
    			            
    	RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_AGREGAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	      
    }

    
    public void Agregar(HttpServletRequest request, HttpServletResponse response, String tipomov, JComprasEntidadesSetIdsV2 set, String idmod, String idmod4)
      throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
    	
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_FACTURAS_DET (\n";
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
    		tbl += "\n\ninsert into _TMP_COMPRAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + rec.getPartida(i).getID_Prod() + 
    		"','" + rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getDescuento() + "','" + rec.getPartida(i).getIVA() + "','" + p(rec.getPartida(i).getObsPartida()) + 
    		"','" + rec.getPartida(i).getImporte() + "','" + rec.getPartida(i).getImporteDesc() + "','" + rec.getPartida(i).getImporteIVA() + "','" + rec.getPartida(i).getTotalPart() + 
    		"','" + rec.getPartida(i).getIEPS() + "','" + rec.getPartida(i).getImporteIEPS() + "','" + rec.getPartida(i).getIVARet() + "','" + rec.getPartida(i).getImporteIVARet() + "','" + rec.getPartida(i).getISRRet() + "','" + rec.getPartida(i).getImporteISRRet() + 
    		"','" + rec.getPartida(i).getID_Tipo() + "','" + rec.getPartida(i).getUnidad() + "','" + rec.getPartida(i).getPrecio() + "');";
        }

    	int forma_pago;
    	if(rec.getForma_Pago().equals("contado"))
    		forma_pago = 0;
    	else if(rec.getForma_Pago().equals("credito"))
    		forma_pago = 1;
    	else //Ninguno
    		forma_pago = 3;
    	
    	String str, del;
    	if(tipomov.equals("FACTURAS") || tipomov.equals("GASTOS"))
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
    		
    		if(tipomov.equals("FACTURAS"))
    			str = "select * from sp_compras_facturas_agregar('";
    		else //if(tipomov.equals("GASTOS"))
    			str = "select * from sp_compras_gastos_agregar('";
    	
    		str += rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(rec.getReferencia()) + 
    	        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
    	        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
    	        	"','" + rec.getID_Bodega() + "',null,'" + rec.getID_Vendedor() + "',null,'" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    	    	    		
    		del = "\nDROP TABLE _TMP_COMPRAS_FACTURAS_DET;\n\nDROP TABLE _TMP_PAGOS;";
            
        }
    	else 
    	{
    		if(tipomov.equals("ORDENES"))
    			str = "select * from sp_compras_ordenes_agregar('";
    		else //if(tipomov.equals("RECEPCIONES"))
    			str = "select * from sp_compras_recepciones_agregar('";
    		
    		str += rec.getID_Entidad() + "','" + rec.getFactNum() + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(rec.getReferencia()) + 
        	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs()) + "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
        	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
        	"','" + rec.getID_Bodega() + "',null,'" + rec.getID_Vendedor() + "',null,'" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
    		
    		del = "DROP TABLE _TMP_COMPRAS_FACTURAS_DET";
        	
    	}
    	
    	//doDebugSQL(request, response, tbl + "\n\n\n" + str + "");
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, tbl, str, del, rfb);

		short idmensaje = (short)rfb.getIdmensaje();
		String mensaje = rfb.getRes();
		            
        RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_AGREGAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
		
        if(idmensaje == 0 && tipomov.equals("FACTURAS") && rec.getClave() != 0 && rec.getID_Moneda() != 1)
        {
        	JProveeProveeMasSetV2 pro = new JProveeProveeMasSetV2(request);
			pro.m_Where = "ID_Clave = '" + rec.getClave() + "'";
			pro.Open();
			
			if(!pro.getAbsRow(0).getPais().equals("MEX") && !pro.getAbsRow(0).getPedimento().equals("--"))
			{
				String ID = rfb.getClaveret();
				request.setAttribute("ID_Factura", ID);
				irApag("/forsetiweb/compras/comp_fact_dlg_datimp.jsp", request, response);
      			return;
			}
        }
        
        irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
        
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response, String tipomov, String idmod, String idmod4)
    	throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JCompFactSes rec = (JCompFactSes)ses.getAttribute("comp_fact_dlg");
    	
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_FACTURAS_DET (\n";
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
    		tbl += "\n\ninsert into _TMP_COMPRAS_FACTURAS_DET\nvalues('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getID_Prod()) + 
    		"','" + rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getDescuento() + "','" + rec.getPartida(i).getIVA() + "','" + p(rec.getPartida(i).getObsPartida()) + 
    		"','" + rec.getPartida(i).getImporte() + "','" + rec.getPartida(i).getImporteDesc() + "','" + rec.getPartida(i).getImporteIVA() + "','" + rec.getPartida(i).getTotalPart() + 
    		"','" + rec.getPartida(i).getIEPS() + "','" + rec.getPartida(i).getImporteIEPS() + "','" + rec.getPartida(i).getIVARet() + "','" + rec.getPartida(i).getImporteIVARet() + "','" + rec.getPartida(i).getISRRet() + "','" + rec.getPartida(i).getImporteISRRet() + 
    		"','" + p(rec.getPartida(i).getID_Tipo()) + "','" + p(rec.getPartida(i).getUnidad()) + "','" + rec.getPartida(i).getPrecio() + "');";
        }

    	int forma_pago;
    	if(rec.getForma_Pago().equals("contado"))
    		forma_pago = 0;
    	else if(rec.getForma_Pago().equals("credito"))
    		forma_pago = 1;
    	else //Ninguno
    		forma_pago = 3;
    	
    	String str, del;
    
    	str = "select * from sp_compras_ordenes_cambiar('";
		
		str += rec.getID_Entidad() + "','" + p(request.getParameter("ID")) + "','" + rec.getClave() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(rec.getReferencia()) + 
    	"','" + rec.getID_Moneda() + "','" + rec.getTC() + "','" + forma_pago + "','" + p(rec.getObs())+ "','" + rec.getImporte() + "','" + rec.getDescuento() + "','" + rec.getSubTotal() + 
    	"','" + rec.getIVA() + "','" + rec.getTotal() + "','" + (Float)request.getAttribute("fsipg_efectivo") + "','" + (Float)request.getAttribute("fsipg_bancos") + "','" + (Float)request.getAttribute("fsipg_cambio") + 
    	"','" + rec.getID_Bodega() + "',null,'" + rec.getID_Vendedor() + "',null,'" + p(rec.getUUID()) + "','" + rec.getIEPS() + "','" + rec.getIVARet() + "','" + rec.getISRRet() + "') as ( err integer, res varchar, clave integer );";
		
		del = "DROP TABLE _TMP_COMPRAS_FACTURAS_DET";

		//doDebugSQL(request, response, tbl + str);
		
		JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, tbl, str, del, rfb);

		String mensaje = rfb.getRes();
		
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_CAMBIAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
		irApag("/forsetiweb/compras/comp_fact_dlg.jsp", request, response);
      
    }
      
    public void CancelarFactura(HttpServletRequest request, HttpServletResponse response, String moddes, String idmod, String idmod4)
    throws ServletException, IOException
    {
    	String str = "select * from ";
    	if(moddes.equals("FACTURAS"))
    		str += "sp_compras_facturas_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("COMP_FAC").getEspecial() + "')";
    	else if(moddes.equals("GASTOS"))
    		str += "sp_compras_gastos_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("COMP_GAS").getEspecial() + "')";
    	else if(moddes.equals("ORDENES"))
    		str += "sp_compras_ordenes_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("COMP_ORD").getEspecial() + "')";
    	else if(moddes.equals("RECEPCIONES"))
    		str += "sp_compras_recepciones_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("COMP_REC").getEspecial() + "')";
    	else if(moddes.equals("DEVOLUCIONES"))
    		str += "sp_compras_devoluciones_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("COMP_DEV").getEspecial() + "')";
    	
    	str += " as ( err integer, res varchar, clave integer );";

    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, str, rfb);

		String mensaje = rfb.getRes();
		
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmod + "_CANCELAR", idmod4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      
    }
    
	@SuppressWarnings("rawtypes")
	public void SubirArchivosCFD(HttpServletRequest request, HttpServletResponse response, JFacturasXML compfactxml, Vector archivos)
			throws ServletException, IOException
	{
		short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
			
		HttpSession ses = request.getSession(true);
		JForsetiCFD cfd = (JForsetiCFD)ses.getAttribute("comp_cfd");
		if(cfd == null)
		{
			cfd = new JForsetiCFD();
			ses.setAttribute("comp_cfd", cfd);
		}
		else
			cfd.resetearCertComp();
	        
		idmensaje = cfd.SubirArchivosCFDI(request, archivos, mensaje, "E");
		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		
		if(idmensaje == JForsetiCFD.OKYDOKY)
		{
			idmensaje = cfd.VerificarFacturasSubidas(request, compfactxml, mensaje, "E");
			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
			
			if(idmensaje == JForsetiCFD.OKYDOKY)
			{
				idmensaje = cfd.GuardarDocumentoCFDI(request, Integer.parseInt(getSesion(request).getSesion(compfactxml.getParametros().getProperty("idmod")).getEspecial()), compfactxml.getArchivoXML(), compfactxml.getArchivoPDF(), mensaje, "E");
				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				
			}
			
		}
		
		JFacturasXML rec = (JFacturasXML)ses.getAttribute("comp_fact_xml");
		if(rec != null)
			rec = null;
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
	}

	@SuppressWarnings("rawtypes")
	public void SubirArchivosOTROS(HttpServletRequest request, HttpServletResponse response, JFacturasXML compfactxml, Vector archivos)
			throws ServletException, IOException
	{
		short idmensaje = -1; String mensaje = "";
		
		String cfd_cbb_serie = compfactxml.getParametros().getProperty("cfd_cbb_serie");
		String cfd_cbb_numfol = compfactxml.getParametros().getProperty("cfd_cbb_numfol");
		String factnumext = compfactxml.getParametros().getProperty("factnumext");
		String total = compfactxml.getParametros().getProperty("total");
		String idmoneda = compfactxml.getParametros().getProperty("idmoneda");
		String tc = compfactxml.getParametros().getProperty("tc");
		
		if( (!factnumext.equals("") && (!cfd_cbb_serie.equals("") || !cfd_cbb_numfol.equals(""))) ||
				(factnumext.equals("") && (cfd_cbb_serie.equals("") || cfd_cbb_numfol.equals(""))) )
		{
			idmensaje = 3; mensaje = "ERROR: Se debe seleccionar, ya sea, el numero de factura extranjera, o la serie y folio de la factura impresa.";
			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		}
		else //Sube el archivo
		{
			UUID nombre = UUID.randomUUID();
		    	
			String[] exts = { "pdf", "jpg", "jpeg", "gif", "bmp", "zip", "png" };
			boolean [] frz = { true, true, true, true, true, true, true };
			JSubirArchivo sa = new JSubirArchivo(512, "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/", exts, frz);
			
			if(sa.processFiles(archivos) < 1) // significa que no encontró el archivo
			{
				idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR",3) + "<br>" + sa.getError();
				getSesion(request).setID_Mensaje(idmensaje, mensaje);
			}
			else 
			{
				String str = "select * from sp_cfdcompotr_agregar('ENT','" + Integer.parseInt(getSesion(request).getSesion(compfactxml.getParametros().getProperty("idmod")).getEspecial()) + "','" +
						p(cfd_cbb_serie) + "','" + (cfd_cbb_numfol.equals("") ? "0" : p(cfd_cbb_numfol)) + "','" + p(factnumext) + "','" + p(nombre.toString()) + "','" + p(sa.getExt(0)) + "','" + p(sa.getFile(0)) + "','" + p(total) + "','" + p(idmoneda) + "','" + p(tc) + "') as (err int, res varchar)";
				System.out.println(str);
				try 
				{
					Connection con = JAccesoBD.getConexionSes(request);
					Statement s = con.createStatement();
					ResultSet rs   = s.executeQuery(str);
					if(rs.next())
					{
						idmensaje = rs.getShort("ERR");
						mensaje = rs.getString("RES");
					}
					s.close();
					JAccesoBD.liberarConexion(con);
					
					if(idmensaje == 0)
					{
						File orig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/" + sa.getFile(0));
						File dest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/OTRs/" + nombre.toString() + "." + sa.getExt(0));
						orig.renameTo(dest);
					}
					
					getSesion(request).setID_Mensaje(idmensaje, mensaje );
					
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
					idmensaje = 3; mensaje = e.getMessage();
					getSesion(request).setID_Mensaje(idmensaje, mensaje);
				}
			}
		}
				
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
	}

	public void DatosImportacion(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
		String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_FACTURAS_COMEXT_DET (\n";
		tbl += "  partida smallint NOT NULL, \n";
		tbl += "  noidentificacion character varying(100) NOT NULL, \n";
		tbl += "  fraccionarancelaria character varying(12) NOT NULL, \n";
		tbl += "  cantidadaduana numeric(9,3) NOT NULL, \n";
		tbl += "  unidadaduana smallint NOT NULL, \n";
		tbl += "  valorunitarioaduana numeric(19,2) NOT NULL, \n";
		tbl += "  valordolares numeric(19,2) NOT NULL \n";
		tbl += ");\n";
		tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_COMPRAS_FACTURAS_COMEXT_DET_DESCESP (\n";
		tbl += "  partida smallint NOT NULL, \n";
		tbl += "  descripcion smallint NOT NULL, \n";
		tbl += "  marca character varying(35) NOT NULL, \n";
		tbl += "  modelo character varying(80) NOT NULL, \n";
		tbl += "  submodelo character varying(50) NOT NULL, \n";
		tbl += "  numeroserie character varying(40) NOT NULL \n";
		tbl += ");\n";
		
		JComercioExteriorDetSet det = new JComercioExteriorDetSet(request,"COMPRA");
		det.m_Where = "ID_VC = '" + p(request.getParameter("ID")) + "'";
		det.m_OrderBy = "Partida ASC";
		det.Open();
				
        for(int i = 0; i< det.getNumRows(); i++)
        {
            tbl += "INSERT INTO _TMP_COMPRAS_FACTURAS_COMEXT_DET \n";
            tbl += "VALUES('" + det.getAbsRow(i).getPartida() + "','" 
            + p(request.getParameter("noidentificacion_" + det.getAbsRow(i).getPartida())) + "','"
            + p(request.getParameter("fraccionarancelaria_" + det.getAbsRow(i).getPartida())) + "','"
            + p(request.getParameter("cantidadaduana_" + det.getAbsRow(i).getPartida())) + "','"
            + p(request.getParameter("unidadaduana_" + det.getAbsRow(i).getPartida())) + "','"
            + p(request.getParameter("valorunitarioaduana_" + det.getAbsRow(i).getPartida())) + "','"
            + p(request.getParameter("valordolares_" + det.getAbsRow(i).getPartida())) + "'); \n";
            
            JComercioExteriorDetDescEspSet esp = new JComercioExteriorDetDescEspSet(request,"COMPRA");
			esp.m_Where = "ID_VC = '" + p(request.getParameter("ID")) + "' and Partida = '" + det.getAbsRow(i).getPartida() + "'";
			esp.m_OrderBy = "Descripcion ASC";
			esp.Open();
		
			for(int j = 0; j < esp.getNumRows(); j++)
			{
				tbl += "INSERT INTO _TMP_COMPRAS_FACTURAS_COMEXT_DET_DESCESP \n";
	            tbl += "VALUES('" + det.getAbsRow(i).getPartida() + "','" 
	            + esp.getAbsRow(j).getDescripcion() + "','"
	            + p(request.getParameter("marca_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion())) + "','"
	            + p(request.getParameter("modelo_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion())) + "','"
				+ p(request.getParameter("submodelo_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()))  + "','"
	            + p(request.getParameter("numeroserie_" + esp.getAbsRow(j).getPartida() + "_" + esp.getAbsRow(j).getDescripcion()))  + "'); \n";
			}
        }
        
        double tcusd = (!request.getParameter("tipocambiousd").equals("") ? Double.parseDouble(request.getParameter("tipocambiousd")) : 0.0 );
        double totusd = (!request.getParameter("totalusd").equals("") ? Double.parseDouble(request.getParameter("totalusd")) : 0.0 ); 
    	
        String str = "select * from sp_compras_facturas_comext('" +
        	p(request.getParameter("ID")) + "','" +
        	p(request.getParameter("tipooperacion")) + "','" +
        	p(request.getParameter("certificadoorigen")) + "','" +
        	p(request.getParameter("numcertificadoorigen")) + "','" +
        	p(request.getParameter("numeroexportadorconfiable")) + "','" +
        	p(request.getParameter("incoterm")) + "','" +
        	p(request.getParameter("subdivision")) + "','" +
        	p(request.getParameter("observaciones")) + "','" +
        	tcusd + "','" +
        	totusd + "','" +
        	p(request.getParameter("emisor_curp")) + "','" +
        	p(request.getParameter("receptor_curp")) + "','" +
        	p(request.getParameter("receptor_numregidtrib")) + "','" +
        	p(request.getParameter("destinatario_numregidtrib")) + "','" +
        	p(request.getParameter("destinatario_rfc")) + "','" +
        	p(request.getParameter("destinatario_curp")) + "','" +
        	p(request.getParameter("destinatario_nombre")) + "','" +
        	p(request.getParameter("destinatario_domicilio_calle")) + "','" +
        	p(request.getParameter("destinatario_domicilio_numeroexterior")) + "','" +
        	p(request.getParameter("destinatario_domicilio_numerointerior")) + "','" +
        	p(request.getParameter("destinatario_domicilio_colonia")) + "','" +
        	p(request.getParameter("destinatario_domicilio_localidad")) + "','" +
        	p(request.getParameter("destinatario_domicilio_referencia")) + "','" +
        	p(request.getParameter("destinatario_domicilio_municipio")) + "','" +
        	p(request.getParameter("destinatario_domicilio_estado")) + "','" +
        	p(request.getParameter("destinatario_domicilio_pais")) + "','" +
        	p(request.getParameter("destinatario_domicilio_codigopostal")) + "') as (err integer, res varchar, clave integer)";
            //doDebugSQL(request,response,tbl + "<br>" + str);
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_COMPRAS_FACTURAS_COMEXT_DET; DROP TABLE _TMP_COMPRAS_FACTURAS_COMEXT_DET_DESCESP;", rfb);
   
        //No registra agregado de la importacion porque se duplicaría el registro de la factura.... Ya que se considera parte de esta factura
    	//RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), idmodAgregar + "_AGREGAR", idmodAgregar4 + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(idmod).getEspecial() + "||",mensaje);
        irApag("/forsetiweb/compras/comp_fact_dlg_datimp.jsp", request, response);
            
    }
}












