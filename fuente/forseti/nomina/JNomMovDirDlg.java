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
package forseti.nomina;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JAdmCompaniasSet;
import forseti.sets.JBDSSet;
import forseti.sets.JCFDCompSet;
import forseti.sets.JCalculoNominaDetSet;
import forseti.sets.JCalculoNominaEspSet;
import forseti.sets.JMasempSet;
import forseti.sets.JMasempSetCons;
import forseti.sets.JMovimientosNomSet;
import forseti.sets.JNominaEntidadesSetIds;
import forseti.sets.JNominasModuloSet;
import forseti.sets.JProcessSet;

@SuppressWarnings("serial")
public class JNomMovDirDlg extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

 	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String nom_nomina_dlg = "";
      request.setAttribute("nom_nomina_dlg",nom_nomina_dlg);

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
  	  			  JFacturasXML nomrecxml = (JFacturasXML)ses.getAttribute("nom_rec_xml");
  	  			  Vector archivos = new Vector();
  	  			  DiskFileUpload fu = new DiskFileUpload();
  	  			  List items = fu.parseRequest(request);
  				  Iterator iter = items.iterator();
  				  while (iter.hasNext()) 
  	  			  {
  	  				  FileItem item = (FileItem)iter.next();
  	  				  if (item.isFormField()) 
  	  				  	  nomrecxml.getParametros().put(item.getFieldName(), item.getString());
  	  				  else
  	  				  	  archivos.addElement(item);
  	  			  }
  	  			  
  				  // revisa por las entidades
  				  JNominaEntidadesSetIds setids = new  JNominaEntidadesSetIds(request,usuario,getSesion(request).getSesion("NOM_NOMINA").getEspecial());
  				  setids.Open();

  				  if(setids.getNumRows() < 1)
  				  {
  		      			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
  		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
  		      			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM||||",mensaje);
  		      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  		      			return;
  				  }
  	          
  	  			  SubirArchivosCFD(request, response, nomrecxml, archivos);
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
    	JNominaEntidadesSetIds setids = new  JNominaEntidadesSetIds(request,usuario,getSesion(request).getSesion("NOM_NOMINA").getEspecial());
        setids.Open();

        if(setids.getNumRows() < 1)
        {
      		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }
        
        request.setAttribute("idmod","NOM_NOMINA");
        request.setAttribute("fact_xml","NOMINA");
        
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_NOMINA") && request.getParameter("id") != null)
        {
        	JNominasModuloSet set = new JNominasModuloSet(request);
            set.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
            set.Open();
        	if(set.getNumRows() < 1)
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM|" + request.getParameter("id") + "|" + setids.getAbsRow(0).getID_Sucursal() + "||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        }  
  
        if(request.getParameter("proceso").equals("ENLAZAR_RECIBO"))
        {
        	//System.out.println("Enlazar Recibos");
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("NOM_NOMINA_CAMBIAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_CAMBIAR","NNOM||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
      		  	
            JNominasModuloSet setnom = new JNominasModuloSet(request);
            setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
            setnom.Open();
            
            if(setids.getAbsRow(0).getCFD() || 
            		(setnom.getAbsRow(0).getTipo() != 1 && setnom.getAbsRow(0).getTipo() != 2 &&
            				setnom.getAbsRow(0).getTipo() != 5 && setnom.getAbsRow(0).getTipo() != 6))
            {
            	idmensaje = 3; mensaje += "ERROR: No se puede enlazar ningun CFDI porque esta entidad de nómina genera sus propios CFDIs, o  porque el tipo, no es compatible para sellar.<br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni ENLAZAR,
            {
            	//System.out.println("Subproceso Nulo");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiweb/fact_dlg_xmls.jsp", request, response);
            	return;
            }  
            else
            {
            	//System.out.println("Subproceso NO Nulo");
            	if(request.getParameter("subproceso").equals("ENLAZAR"))
            	{
            		// Se supone que la compra aun no estará ligada a una compra existente...
            		JCFDCompSet comprobante = new JCFDCompSet(request,"NOMINA");
            		comprobante.m_Where = "UUID = '" + p(request.getParameter("uuid")) + "'";
            		comprobante.Open();
	        				
            		if(comprobante.getNumRows() < 1 || !comprobante.getAbsRow(0).getFSI_Tipo().equals("ENT") || comprobante.getAbsRow(0).getFSI_ID() != Integer.parseInt(getSesion(request).getSesion("NOM_NOMINA").getEspecial()))
            		{
            			idmensaje = 3; mensaje += "ERROR: No se ha cargado el CFDI del recibo, éste ya esta ligado a otro recibo, ó el CFDI se cargó en otra entidad<br>";
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			return;
            		}
	        				
            		HttpSession ses = request.getSession(true);
            		JFacturasXML nomrecxml = (JFacturasXML)ses.getAttribute("nom_rec_xml");
	            		  
            		if(nomrecxml == null)
            		{
            			nomrecxml = new JFacturasXML();
            			ses.setAttribute("nom_rec_xml", nomrecxml);
            		}
            		else
            		{
            			nomrecxml = null;
            			nomrecxml = new JFacturasXML();
            			ses.setAttribute("nom_rec_xml", nomrecxml);
            		}
	            		 
            		StringBuffer sb_mensaje = new StringBuffer();
            		if(!JForsetiCFD.CargarDocumentoCFDI(request, nomrecxml, sb_mensaje, request.getParameter("uuid"), "N"))
            		{
            			idmensaje = 3; mensaje += sb_mensaje.toString();
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			return;
            		}
	            		  
            		if(nomrecxml.getComprobante().getProperty("tipoDeComprobante").equals("ingreso") 
            				|| nomrecxml.getComprobante().getProperty("tipoDeComprobante").equals("traslado"))
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
            		if(!nomrecxml.getRFC_Emisor().equalsIgnoreCase(set.getAbsRow(0).getRFC()))
            		{
            			idmensaje = 3; mensaje = "ERROR: El RFC del emisor en el XML no pertenece a la compañia";
            			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			return;
            		}
	        			  
	        		if(request.getParameter("idempleado") == null) // Significa que debe agregar un nuevo recibo 
	        		{
	        			if(setnom.getAbsRow(0).getCerrado())
	                	{
	                		idmensaje = 3; mensaje += "ERROR: No se puede enlazar el CFDI a un recibo nuevo porque la nómina ya esta protegida <br>";
	                		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	                		return;
	                	}
	        			//float descuento = Float.parseFloat(nomrecxml.getComprobante().getProperty("descuento"));
	        			//float totalImpuestosRetenidos = Float.parseFloat(nomrecxml.getImpuestos().getProperty("totalImpuestosRetenidos"));
	        			JMasempSet setemp = new JMasempSet(request);
	        			setemp.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "' and ( RFC_Letras || RFC_Fecha || RFC_Homoclave ) ~~* '" + p(nomrecxml.getRFC_Receptor()) + "'";
	        			setemp.Open();
	        			//System.out.println(setemp.getSQL());
	            		if(setemp.getNumRows() == 0)
	        			{
	        				idmensaje = 1; mensaje = "PRECAUCION: No existe el empleado dado de alta en el sistema, está dado de alta en otra entidad, ó el RFC del empleado no coincide con el del recibo";
	        				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	        				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        				return;
	        			}
	        			  	  
	        			JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
	                    if(rec == null)
	                    {
	                      rec = new JNomMovDirSes();
	                      ses.setAttribute("nom_nomina_dlg", rec);
	                    }
	                    else
	                      rec.resetear();
	                    
	                    JProcessSet setRec = new JProcessSet(request);
	                    setRec.setSQL("select Recibo from VIEW_NOM_CALCULO_NOMINA_ESP where ID_Nomina = '" + p(request.getParameter("id")) + "' order by Recibo desc limit 1");
	                    setRec.Open();

	                   if(setRec.getNumRows() > 0)
	                	   rec.setRecibo(Integer.valueOf(setRec.getAbsRow(0).getSTS("Col1")) + 1);
	                   else
	                	   rec.setRecibo(1);

	                    //Llena el recibo
	                   	rec.setUUID(nomrecxml.getTFD().getProperty("UUID"));
	                   	rec.setID_Empleado(setemp.getAbsRow(0).getID_Empleado());
	                   	rec.setNombre(setemp.getAbsRow(0).getNombre() + " " + setemp.getAbsRow(0).getApellido_Paterno() + " " + setemp.getAbsRow(0).getApellido_Materno());
	                        
	                   	JCalculoNominaDetSet dset = new JCalculoNominaDetSet(request);
	        			dset.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
	        			dset.Open();

	        			for(int i = 0; i< nomrecxml.getPercepciones().size(); i++)
	        			{
	        				Properties percepcion = (Properties)nomrecxml.getPercepciones().elementAt(i);
	        				int clave;
	        				String descripcion;
	        				boolean esDeduccion;
	        				int idmovimiento; try{ idmovimiento = Integer.parseInt(percepcion.getProperty("Clave")); } catch(NumberFormatException e) { idmovimiento = 0; }
	        				JMovimientosNomSet cat = new JMovimientosNomSet(request);
	    		        	cat.m_Where = "ID_Movimiento = '" + idmovimiento + "'";
	    		        	//System.out.println(cat.getSQL());
	        				cat.Open();
	        				if(cat.getNumRows() > 0 )
	        				{
	        					clave = cat.getAbsRow(0).getID_Movimiento();
	        					descripcion = cat.getAbsRow(0).getDescripcion();
	        					esDeduccion = cat.getAbsRow(0).getDeduccion();
	        				}
	        				else // Si no existe la clave en el catalogo, enlaza desde catálogo intermediario 
	        				{
	        					clave = 0;
	        			  		descripcion = percepcion.getProperty("Concepto");
	        			  		esDeduccion = false;
	        				}
	        				float gravado = Float.parseFloat(percepcion.getProperty("ImporteGravado"));
	    	 				float exento = Float.parseFloat(percepcion.getProperty("ImporteExento"));
	        					  
	        				rec.agregaPartida(clave, descripcion, gravado, exento, 0.00F, esDeduccion);
	        			}
	        			 
	        			for(int i = 0; i< nomrecxml.getDeducciones().size(); i++)
	        			{
	        				Properties deduccion = (Properties)nomrecxml.getDeducciones().elementAt(i);
	        				int clave;
	        				String descripcion;
	        				boolean esDeduccion;
	        				
	        				JMovimientosNomSet cat = new JMovimientosNomSet(request);
	    		        	cat.m_Where = "ID_Movimiento = '" + p(deduccion.getProperty("Clave")) + "'";
	    		        	//System.out.println(cat.getSQL());
	        				cat.Open();
	        				if(cat.getNumRows() > 0 )
	        				{
	        					clave = cat.getAbsRow(0).getID_Movimiento();
	        					descripcion = cat.getAbsRow(0).getDescripcion();
	        					esDeduccion = cat.getAbsRow(0).getDeduccion();
	        				}
	        				else // Si no existe la clave en el catalogo, enlaza desde catálogo intermediario 
	        				{
	        					clave = 0;
	        			  		descripcion = deduccion.getProperty("Concepto");
	        			  		esDeduccion = true;
	        				}
	        				float gravado = Float.parseFloat(deduccion.getProperty("ImporteGravado"));
	    	 				float exento = Float.parseFloat(deduccion.getProperty("ImporteExento"));
	        					  
	        				rec.agregaPartida(clave, descripcion, 0.00F, 0.00F, -(gravado + exento), esDeduccion);
	        			}
	        			
	        			rec.establecerResultados();
	        			
	        			if(JUtil.redondear(rec.getSumGravado() + rec.getSumExento() + rec.getSumDeduccion(),2) != JUtil.redondear(Float.parseFloat(nomrecxml.getComprobante().getProperty("total")),2))
      			      	{
	        				idmensaje = 3; mensaje = "ERROR: El total en el CFDI no corresponde al Total calculado en el registro a partir de este CFDI. No se puede agregar. DOC: " + JUtil.redondear(rec.getSumGravado() + rec.getSumExento() + rec.getSumDeduccion(),2) + " XML: " + JUtil.redondear(Float.parseFloat(nomrecxml.getComprobante().getProperty("total")),2);
	        				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	        				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	        				return;
      			      	}
	        			
	        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
	                    return;	
	                    
	        		}   
	        		else // Significa que debe Enlazar a un recibo existente
	        		{
	        			if(setnom.getAbsRow(0).getFormaPago().equals("N")) //Si no está ya pagada la nomina lo rechaza porque no hay poliza para enlazar el cfdi a la contabilidad electronica
	                	{
	                		idmensaje = 3; mensaje += "ERROR: No se puede enlazar el CFDI a un recibo existente porque la nómina no esta pagada aún, lo que significa que no existe una póliza asociada para el enlace a la contabilidad elecrónica <br>";
	                		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	                		return;
	                	}
	        			
				  	  	String[] valoresParam = request.getParameterValues("idempleado");
				  	  	if(valoresParam.length == 1)
				  	  	{	
				  	  		JCalculoNominaEspSet cset = new JCalculoNominaEspSet(request);
				  	  		cset.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
				  	  		cset.Open();
		        			//System.out.println(setemp.getSQL());
		   	        	
				  	  		if(cset.getAbsRow(0).getID_CFD() != 0)
				  	  		{
				  	  			idmensaje = 1;
				  	  			mensaje += "PRECAUCION: Este recibo ya tiene un CFDI asociado. No puedes asociar otro CFDI al mismo recibo<br>";
				  	  			getSesion(request).setID_Mensaje(idmensaje, mensaje);
				  	  			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
				  	  			return;
				  	  		} 
				  			
				  	  		JMasempSetCons setemp = new JMasempSetCons(request);
				  	  		setemp.m_Where = "ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
				  	  		setemp.Open();
	        			 	  
				  	  		if(JUtil.redondear(cset.getAbsRow(0).getGravado() + cset.getAbsRow(0).getExento() + cset.getAbsRow(0).getDeduccion(),2) 
				  	  				!= JUtil.redondear(Float.parseFloat(nomrecxml.getComprobante().getProperty("total")),2)
			  					  || !(setemp.getAbsRow(0).getRFC_Letras() + setemp.getAbsRow(0).getRFC_Fecha() + setemp.getAbsRow(0).getRFC_Homoclave()).equals(nomrecxml.getRFC_Receptor()))
				  	  		{
				  	  			idmensaje = 1;
				  	  			mensaje += "PRECAUCION: Los totales o los RFCs del recibo y el CFDI no coinciden. No se puede asociar este CFDI al registro<br>";
				  	  			getSesion(request).setID_Mensaje(idmensaje, mensaje);
				  	  			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
				  	  			return;
				  	  		}
				  	  		  			  
				  	  		// Aqui asocia.
				  	  		Enlazar(request, response);
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
            			AgregarCambiarEmp(request, response);
            			return;
            		}
            	  
            		irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
            		return;
            	}
            }
        }
        else if(request.getParameter("proceso").equals("AGREGAR_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_AGREGAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
    	  setcom.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "'";
    	  setcom.Open();
    	 
          Byte numero_nomina = new Byte(setcom.getAbsRow(0).getNumero());
          Date desde = new Date(setcom.getAbsRow(0).getFecha().getTime());
          Calendar hast = new GregorianCalendar();
          hast.setTime(setcom.getAbsRow(0).getFecha());
          Integer ano = new Integer(JUtil.obtAno(hast));  
          Integer tipo_de_nomina = new Integer(-1);
          
          if(setcom.getAbsRow(0).getPeriodo().equals("sem"))
        	  hast.add(Calendar.DATE, 6);
          else if(setcom.getAbsRow(0).getPeriodo().equals("qui"))
        	  hast.add(Calendar.DATE, 14);
          else
          {
        	  hast.add(Calendar.MONTH, 1);
        	  hast.add(Calendar.DATE, -1);
          }
          Date hasta = hast.getTime();
          
          request.setAttribute("numero_nomina",numero_nomina);
          request.setAttribute("ano",ano);
          request.setAttribute("desde",desde);
          request.setAttribute("hasta",hasta);
          request.setAttribute("tipo_de_nomina",tipo_de_nomina);
          
          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response))
            {
            	AgregarCambiarCabecero(request, response);
            	return;
            }
            irApag("/forsetiweb/nomina/nom_nomina_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/nomina/nom_nomina_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_CAMBIAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
         
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JNominasModuloSet setnom = new JNominasModuloSet(request);
                setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
                setnom.Open();
                 
                if(setnom.getAbsRow(0).getCerrado())
                {
                     idmensaje = 3; mensaje += "ERROR: No se puede cambiar la n&oacute;mina porque ya esta protegida <br>";
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                     return;
                }
                 
                Byte numero_nomina = new Byte((byte)setnom.getAbsRow(0).getNumero_Nomina());
                Integer ano = new Integer(setnom.getAbsRow(0).getAno());
                Date desde = new Date(setnom.getAbsRow(0).getFecha_Desde().getTime());
                Date hasta = new Date(setnom.getAbsRow(0).getFecha_Hasta().getTime());
                Integer tipo_de_nomina = new Integer(setnom.getAbsRow(0).getTipo());
                
                request.setAttribute("numero_nomina",numero_nomina);
                request.setAttribute("ano",ano);
                request.setAttribute("desde",desde);
                request.setAttribute("hasta",hasta);
                request.setAttribute("tipo_de_nomina",tipo_de_nomina);
               
                
            	
            	// Solicitud de envio a procesar
                if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
                {
                  // Verificacion
                  if(VerificarParametros(request, response))
                  {
                    AgregarCambiarCabecero(request, response);
                    return;
                  }
                  irApag("/forsetiweb/nomina/nom_nomina_dlg.jsp", request, response);
                  return;
                }
                else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
                {
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/nomina/nom_nomina_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_ELIMINAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_ELIMINAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_ELIMINAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JNominasModuloSet setnom = new JNominasModuloSet(request);
                setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
                setnom.Open();
                
                if(setnom.getAbsRow(0).getCerrado())
                {
                    idmensaje = 3; mensaje += "ERROR: No se puede eliminar la n&oacute;mina porque ya esta protegida <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
              	Eliminar(request, response);
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
        else if(request.getParameter("proceso").equals("CALCULAR_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_AGREGAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
                    
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JNominasModuloSet setnom = new JNominasModuloSet(request);
                setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
                setnom.Open();
                
                if(setnom.getAbsRow(0).getCerrado())
                {
                    idmensaje = 3; mensaje += "ERROR: No se puede calcular la n&oacute;mina porque ya está protegida <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            	
                if(setnom.getAbsRow(0).getTipo() == 1 || setnom.getAbsRow(0).getTipo() == 2)
                {	
                	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
                	{
                		//Normales
                		Calcular(request, response, 12);
                		return;
                	}
                	else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
                	{
                		getSesion(request).setID_Mensaje(idmensaje, mensaje);
                		irApag("/forsetiweb/nomina/nom_nomina_dlg_calcular.jsp", request, response);
                		return;
                	}
                }
                else if(setnom.getAbsRow(0).getTipo() == 5 || setnom.getAbsRow(0).getTipo() == 6)
                {
                	//Aguinaldo
                	Calcular(request, response, 56);
                	return;
                }
                else
                {
                	idmensaje = 3; mensaje += "ERROR: No se puede calcular la nómina porque el tipo de nómina no es calculable <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
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
        	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
        }
        else if(request.getParameter("proceso").equals("GENERAR_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_AGREGAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
                    
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JNominasModuloSet setnom = new JNominasModuloSet(request);
                setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
                setnom.Open();
                
                if(!setnom.getAbsRow(0).getCerrado() || setnom.getAbsRow(0).getStatus().equals("P") || setnom.getAbsRow(0).getStatus().equals("C"))
                {
                    idmensaje = 3; mensaje += "ERROR: No se puede generar pago de n&oacute;mina porque no esta protegida, ya esta pagada o esta cancelada <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            	
                if(setnom.getAbsRow(0).getTipo() == 1 || setnom.getAbsRow(0).getTipo() == 2 ||
                		setnom.getAbsRow(0).getTipo() == 5 || setnom.getAbsRow(0).getTipo() == 6)
                {	
                	Generar(request, response);
                	return;
                }
                else
                {
                	idmensaje = 3; mensaje += "ERROR: No se puede generar pago de n&oacute;mina porque el tipo no es compatible para generarlos. Se debe generar su n&oacute;mina de sueldos <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CARGAR_RECIBO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_CFDI_CARGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_CARGAR");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_CARGAR","NNOM||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }
      	  
          JNominasModuloSet setnom = new JNominasModuloSet(request);
		  setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
		  setnom.Open();
        
      	  if(setids.getAbsRow(0).getCFD() || 
      			  (setnom.getAbsRow(0).getTipo() != 1 && setnom.getAbsRow(0).getTipo() != 2 &&
      			  setnom.getAbsRow(0).getTipo() != 5 && setnom.getAbsRow(0).getTipo() != 6))
      	  {
      		  idmensaje = 3; mensaje += "ERROR: No se puede cargar ningun CFDI porque esta entidad de nómina genera sus propios CFDIs, o  porque el tipo, no es compatible para sellar.<br>";
      		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      		  return;
      	  }
      	
      	  Integer subir_archivos = new Integer(2);
      	  request.setAttribute("subir_archivos", subir_archivos);
      		  
      	  HttpSession ses = request.getSession(true);
      	  JFacturasXML rec = (JFacturasXML)ses.getAttribute("nom_rec_xml");
      		  
      	  if(rec == null)
      	  {
      		  rec = new JFacturasXML();
      		  ses.setAttribute("nom_rec_xml", rec);
      	  }
      	  else
      	  {
      		  rec = null;
      		  rec = new JFacturasXML();
      		  ses.setAttribute("nom_rec_xml", rec);
      	  }
         			  
      	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      	  irApag("/forsetiweb/subir_archivos.jsp?verif=/servlet/CEFNomMovDirDlg&archivo_1=xml&archivo_2=pdf&proceso=CARGAR_RECIBO&subproceso=ENVIAR", request, response);
      	  return;
      	  
        }
        else if(request.getParameter("proceso").equals("SELLAR_NOMINA"))
        {
      	  // Revisa si tiene permisos
      	  if(!getSesion(request).getPermiso("NOM_NOMINA_AGREGAR"))
      	  {
      		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_AGREGAR");
      		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "NOM_NOMINA_AGREGAR", "NNOM||||",mensaje);
      		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      		  return;
      	  }
          
      	  if(request.getParameter("id") != null)
      	  {
      		  String[] valoresParam = request.getParameterValues("id");
      		  if(valoresParam.length == 1)
      		  {
      			  if(setids.getAbsRow(0).getCFD() == false)
      			  {
      				  idmensaje = 1;
      				  mensaje += "PRECAUCION: Esta entidad de nómina no est&aacute; establecida como CFDI. No se pueden sellar los registros<br>";
      				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      				  return;
      			  }
      			  
      			  JNominasModuloSet setnom = new JNominasModuloSet(request);
      			  setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
      			  setnom.Open();
                
      			  if(!setnom.getAbsRow(0).getCerrado() || !setnom.getAbsRow(0).getStatus().equals("P") || setnom.getAbsRow(0).getStatus().equals("C"))
      			  {
      				  idmensaje = 3; mensaje += "ERROR: No se puede sellar esta nómina porque no esta protegida, no esta pagada o esta cancelada <br>";
      				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      				  return;
      			  }
            	
      			  if(setnom.getAbsRow(0).getTipo() == 1 || setnom.getAbsRow(0).getTipo() == 2 ||
      					  setnom.getAbsRow(0).getTipo() == 5 || setnom.getAbsRow(0).getTipo() == 6)
      			  {	
      				  JCalculoNominaEspSet SetMod = new JCalculoNominaEspSet(request);
        			  SetMod.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
        			  SetMod.m_OrderBy = "Recibo ASC";
        			  SetMod.Open();
        			 
        			  for(int i = 0; i < SetMod.getNumRows(); i++)
        			  {
        				  if(SetMod.getAbsRow(i).getTFD() == 3)
        					  continue;
        				         				  
        				  StringBuffer sb_mensaje = new StringBuffer(254);
            			  idmensaje = generarCFDI(request, response, "NOMINA", Integer.parseInt(request.getParameter("id")), SetMod.getAbsRow(i).getID_Empleado(), setids, SetMod.getAbsRow(i).getTFD(), sb_mensaje);
            			  mensaje += SetMod.getAbsRow(i).getID_Empleado() + " " + sb_mensaje.toString() + "<br>";
            			  if(idmensaje == 3)
            				  break;
        			  } 
    			  
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
        			  return;
      			  }
      			  else
      			  {
      				  idmensaje = 3; mensaje += "ERROR: No se puede sellar la n&oacute;mina porque el tipo no es compatible para sellar. Se debe sellar la n&oacute;mina de sueldos <br>";
      				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("PROTEGER_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_CAMBIAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
                    
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JNominasModuloSet setnom = new JNominasModuloSet(request);
                setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
                setnom.Open();
                if(!setnom.getAbsRow(0).getFormaPago().equals("N") || setnom.getAbsRow(0).getStatus().equals("C"))
                {
                	idmensaje = 3; mensaje += "ERROR: No se puede desproteger la nómina porque ya esta pagada o esta cancelada.<br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
                
              	Proteger(request, response);
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
        else if(request.getParameter("proceso").equals("CONSULTAR_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/nomina/nom_nomina_dlg_cons.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("MOVER_NOMINA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_CAMBIAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/nomina/nom_nomina_dlg_cons.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("AGR_EMP"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_CAMBIAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          JNominasModuloSet setnom = new JNominasModuloSet(request);
      	  setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
          setnom.Open();
          
          if(setnom.getAbsRow(0).getCerrado() || setnom.getAbsRow(0).getStatus().equals("C"))
          {
              idmensaje = 3; mensaje += "ERROR: No se puede agregar el empleado porque la nómina ya está protegida o está cancelada.<br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  if(AgregarCabeceroRecibo(request,response) == -1)
        	  {
        		  AgregarCambiarEmp(request, response);
        		  return;
        	  }
        	  irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
        	  return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
        	  if(AgregarCabeceroRecibo(request,response) == -1)
        	  {
        		  if(VerificarParametrosPartida(request, response))
        			  AgregarPartida(request, response);
        	  }
        	  irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
        	  return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
        	  if(AgregarCabeceroRecibo(request,response) == -1)
        	  {
        		  if(VerificarParametrosPartida(request, response))
        			  EditarPartida(request, response);
        	  } 
        	  irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
        	  return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
        	  if(AgregarCabeceroRecibo(request,response) == -1)
        	  {
        		  BorrarPartida(request, response);
        	  }
        	  irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
        	  return;
          }
          else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            HttpSession ses = request.getSession(true);
            JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
            if(rec == null)
            {
              rec = new JNomMovDirSes();
              ses.setAttribute("nom_nomina_dlg", rec);
            }
            else
              rec.resetear();
            
            JProcessSet setRec = new JProcessSet(request);
            setRec.setSQL("select Recibo from VIEW_NOM_CALCULO_NOMINA_ESP where ID_Nomina = '" + p(request.getParameter("id")) + "' order by Recibo desc limit 1");
            setRec.Open();

            if(setRec.getNumRows() > 0)
            	rec.setRecibo(Integer.valueOf(setRec.getAbsRow(0).getSTS("Col1")) + 1);
            else
            	rec.setRecibo(1);
                      
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAM_EMP"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_CAMBIAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          JNominasModuloSet setnom = new JNominasModuloSet(request);
          setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
          setnom.Open();
          
          if(setnom.getAbsRow(0).getCerrado() || setnom.getAbsRow(0).getStatus().equals("C"))
          {
              idmensaje = 3; mensaje += "ERROR: No se puede cambiar el recibo porque la nómina ya está protegida o está cancelada.<br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          if(request.getParameter("idempleado") != null)
          {
            String[] valoresParam = request.getParameterValues("idempleado");
            if(valoresParam.length == 1)
            {
            	JCalculoNominaEspSet cset = new JCalculoNominaEspSet(request);
            	cset.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
            	cset.Open();
            	if(cset.getAbsRow(0).getTFD() >= 2)
            	{
            		idmensaje = 3; mensaje += "ERROR: No se puede cambiar el recibo porque ya tiene un CFDI enlazado.<br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
            	}
            	//Solicitud de envio a procesar
                if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
                {
                	// Verificacion
                	if(AgregarCabeceroRecibo(request,response) == -1)
      			  	{
                		AgregarCambiarEmp(request, response);
                		return;
                	}
                	irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
            		return;
                }
                else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
                {
                	if(AgregarCabeceroRecibo(request,response) == -1)
              	  	{
                		if(VerificarParametrosPartida(request, response))
                			AgregarPartida(request, response);
              	  	}
                	irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
                	return;
                }
                else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
                {
                	if(AgregarCabeceroRecibo(request,response) == -1)
              	  	{
                		if(VerificarParametrosPartida(request, response))
                			EditarPartida(request, response);
              	  	}
                	irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
                	return;
                }
                else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
                {
                	if(AgregarCabeceroRecibo(request,response) == -1)
              	  	{
                		BorrarPartida(request, response);
              	  	}
                	irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
                	return;
                }
                else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
                {
                  HttpSession ses = request.getSession(true);
                  JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
                  if(rec == null)
                  {
                    rec = new JNomMovDirSes();
                    ses.setAttribute("nom_nomina_dlg", rec);
                  }
                  else
                    rec.resetear();
                  
                  //Llena el empleado
                  JCalculoNominaEspSet set = new JCalculoNominaEspSet(request);
                  set.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
                  set.Open();
            	
                  rec.setID_Empleado(set.getAbsRow(0).getID_Empleado()); 
                  rec.setNombre(set.getAbsRow(0).getNombre()); 
                  rec.setFaltas(set.getAbsRow(0).getFaltas());
                  rec.setHE(set.getAbsRow(0).getHE());
                  rec.setHD(set.getAbsRow(0).getHD());
                  rec.setHT(set.getAbsRow(0).getHT());
                  rec.setIXA(set.getAbsRow(0).getIXA());
                  rec.setIXE(set.getAbsRow(0).getIXE());
                  rec.setIXM(set.getAbsRow(0).getIXM());
                  rec.setRecibo(set.getAbsRow(0).getRecibo());
                  rec.setDiasHorasExtras(set.getAbsRow(0).getDiasHorasExtras());
                  
                  JCalculoNominaDetSet dset = new JCalculoNominaDetSet(request);
      			  dset.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
      			  dset.Open();

                  for(int i = 0; i < dset.getNumRows(); i++)
                  {
                    rec.agregaPartida(dset.getAbsRow(i).getID_Movimiento(), dset.getAbsRow(i).getDescripcion(), 
                    		dset.getAbsRow(i).getGravado(), dset.getAbsRow(i).getExento(), dset.getAbsRow(i).getDeduccion(), dset.getAbsRow(i).getEsDeduccion());
                  }
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("BORR_EMP"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA_CAMBIAR","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          JNominasModuloSet setnom = new JNominasModuloSet(request);
          setnom.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "'";
          setnom.Open();
          
          if(setnom.getAbsRow(0).getCerrado() || setnom.getAbsRow(0).getStatus().equals("C"))
          {
              idmensaje = 3; mensaje += "ERROR: No se puede borrar el recibo porque la nómina ya está protegida o está cancelada.<br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          if(request.getParameter("idempleado") != null)
          {
            String[] valoresParam = request.getParameterValues("idempleado");
            if(valoresParam.length == 1)
            {
            	JCalculoNominaEspSet cset = new JCalculoNominaEspSet(request);
            	cset.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
            	cset.Open();
            	if(cset.getAbsRow(0).getTFD() >= 2)
            	{
            		idmensaje = 3; mensaje += "ERROR: No se puede eliminar el recibo porque ya tiene un CFDI enlazado.<br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
            	}
            	
            	EliminarEmp(request, response);
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
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          if(request.getParameter("idempleado") != null)
          {
            String[] valoresParam = request.getParameterValues("idempleado");
            if(valoresParam.length == 1)
            {
            	//Solicitud de envio a procesar
                if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("IMPRESION"))
                {
                  // Impresion
                	StringBuffer bsmensaje = new StringBuffer(254);
    				String SQLCab = "select * from view_nomina_recibos_impcab where ID_Nomina = " + request.getParameter("id") + " and ID_Empleado = '" + request.getParameter("idempleado") + "'";
    				String SQLDet = "select * from view_nomina_recibos_impdet where ID_Nomina = " + request.getParameter("id") + " and ID_Empleado = '" + request.getParameter("idempleado") + "' order by esdeduccion asc, id_movimiento asc";
    				
    				idmensaje = Imprimir(SQLCab, SQLDet, request.getParameter("idformato"), bsmensaje, request, response);

    				if (idmensaje != -1)
    				{
    					getSesion(request).setID_Mensaje(idmensaje, bsmensaje.toString());
    					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    					return;
    				}

                }
                else 
                {
                	request.setAttribute("impresion", "CEFNomMovDirDlg");
    				request.setAttribute("tipo_imp", "NOM_NOMINA");
    				request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Recibo());
    				
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
        else if(request.getParameter("proceso").equals("ENVIAR_RECIBO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          if(request.getParameter("idempleado") != null)
          {
            String[] valoresParam = request.getParameterValues("idempleado");
            if(valoresParam.length == 1)
            {
            	//Solicitud de envio a procesar
            	JCalculoNominaEspSet SetMod = new JCalculoNominaEspSet(request);
            	SetMod.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
            	SetMod.Open();
            	
      		  	if(SetMod.getAbsRow(0).getTFD() != 3)
      		  	{
      		  		idmensaje = 1;
      		  		mensaje += "PRECAUCION: Este recibo no est&aacute; sellado completamente, no se puede enviar <br>";
      		  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      		  		return;
      		  	} 
      		  
      		  	JMasempSet set = new JMasempSet(request);
      		  	set.m_Where = "ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
      		  	set.Open();
      		  	if(set.getAbsRow(0).getSMTP() == 0) // Maneja smtp manual o automático
      		  	{
	      		  	idmensaje = 1;
	  		  		mensaje += "PRECAUCION: Este empleado no esta confgurado para recibir sus recibos por correo <br>";
	  		  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  		  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	  		  		return;
	      		  	
      		  	}
      		  	
      		  	JFsiSMTPClient smtp = new JFsiSMTPClient();
      		  	smtp.enviarCFDI(request, "NOM", request.getParameter("id"), request.getParameter("idempleado"), set.getAbsRow(0).getNombre(), set.getAbsRow(0).getEMail());
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
        else if(request.getParameter("proceso").equals("XML_RECIBO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          if(request.getParameter("idempleado") != null)
          {
            String[] valoresParam = request.getParameterValues("idempleado");
            if(valoresParam.length == 1)
            {
            	//Solicitud de envio a procesar
            	JCalculoNominaEspSet SetMod = new JCalculoNominaEspSet(request);
            	SetMod.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
            	SetMod.Open();
            	
      		  	if(SetMod.getAbsRow(0).getTFD() != 3)
      		  	{
      		  		idmensaje = 1;
      		  		mensaje += "PRECAUCION: Este recibo no est&aacute; sellado completamente, no hay nada que bajar <br>";
      		  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      		  		return;
      		  	} 
      		  
      		  	String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/SIGN_NOM-" + request.getParameter("id") + "-" + request.getParameter("idempleado") + ".xml";
      		  	String destino = "NOM-" + SetMod.getAbsRow(0).getID_Nomina() + "-" + SetMod.getAbsRow(0).getRecibo() + ".xml";
      		  	JBajarArchivo fd = new JBajarArchivo();
      		  
      		  	fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
      		  
      		  	idmensaje = 0;
      		  	mensaje = "El recibo se bajo satisfactoriamente";
      		  
      		  	getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  	irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
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
        else if(request.getParameter("proceso").equals("PDF_RECIBO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_NOMINA"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_NOMINA");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_NOMINA","NNOM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          if(request.getParameter("idempleado") != null)
          {
            String[] valoresParam = request.getParameterValues("idempleado");
            if(valoresParam.length == 1)
            {
            	//Solicitud de envio a procesar
            	JCalculoNominaEspSet SetMod = new JCalculoNominaEspSet(request);
            	SetMod.m_Where = "ID_Nomina = '" + p(request.getParameter("id")) + "' and ID_Empleado = '" + p(request.getParameter("idempleado")) + "'";
            	SetMod.Open();
            	
      		  	if(SetMod.getAbsRow(0).getTFD() != 3)
      		  	{
      		  		idmensaje = 1;
      		  		mensaje += "PRECAUCION: Este recibo no est&aacute; sellado completamente, no hay nada que bajar <br>";
      		  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      		  		return;
      		  	} 
      		  
      		  	String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/PDFs/NOM-" + request.getParameter("id") + "-" + request.getParameter("idempleado") + ".pdf";
      		  	String destino = "NOM-" + SetMod.getAbsRow(0).getID_Nomina() + "-" + SetMod.getAbsRow(0).getRecibo() + ".pdf";
      		  	JBajarArchivo fd = new JBajarArchivo();
      		  
      		  	fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
      		  
      		  	idmensaje = 0;
      		  	mensaje = "El recibo se bajo satisfactoriamente";
      		  
      		  	getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  	irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
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

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("numero_nomina") != null && request.getParameter("ano") != null &&
    		  request.getParameter("desde") != null && request.getParameter("hasta") != null &&
    		  !request.getParameter("numero_nomina").equals("") && !request.getParameter("ano").equals("") &&
    		  !request.getParameter("desde").equals("") && !request.getParameter("hasta").equals(""))
      {
    	  if(request.getParameter("tipo_de_nomina").equals("-1"))
    	  {
    	   	  idmensaje = 3;
    	      mensaje += "ERROR: Debes proporcionar el tipo de n&oacute;mina <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
    	  
    	  Date desde = JUtil.estFecha(request.getParameter("desde"));
		  Date hasta = JUtil.estFecha(request.getParameter("hasta"));
		  int desdet = (int)(desde.getTime()/(3600*24*1000));
		  int hastat = (int)(hasta.getTime()/(3600*24*1000));	  
		  
		  if(desde.getTime() > hasta.getTime())
		  {
			  idmensaje = 3;
			  mensaje += "ERROR: La fecha de inicio de nomina no puede ser mayor a la del final. Diferencia " + ((hasta.getTime()/(3600*24*1000)) - (desde.getTime()/(3600*24*1000))) + " dia(s) <br>";
			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			  return false;
		  }
		  else if( (hastat - desdet) > 366)
		  {
			  idmensaje = 3;
			  mensaje += "ERROR: Existen demasiados dias en esta nomina. Cambia las fechas del periodo de la nomina <br>";
			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			  return false;
		  }
		  
		  if(request.getParameter("tipo_de_nomina").equals("56") && (hastat - desdet) < 364)
    	  {
			  idmensaje = 3;
			  mensaje += "ERROR: La nómina de aguinaldo debe constar de 365 dias (desde la fecha 01/enero hasta 31/diciembre). Cambia las fechas del periodo de la nomina <br>";
			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
          return true;
      }
      else
      {
    	  idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    /*
    public boolean VerificarParametrosEmp(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("id_empleado") != null && 
	  		  request.getParameter("faltas") != null && request.getParameter("he") != null && request.getParameter("hd") != null &&
	  				request.getParameter("ht") != null && request.getParameter("dhe") != null && request.getParameter("ixa") != null && request.getParameter("ixe") != null && request.getParameter("ixm") != null &&
	  		  !request.getParameter("id_empleado").equals("") && 
	  		  !request.getParameter("faltas").equals("") && !request.getParameter("he").equals("") && !request.getParameter("hd").equals("") &&
	  			!request.getParameter("ht").equals("") && !request.getParameter("dhe").equals("") && !request.getParameter("ixa").equals("") && !request.getParameter("ixe").equals("") && !request.getParameter("ixm").equals("") ) 
	    {
	    	  JMasempSet setemp = new JMasempSet(request);
	    	  setemp.m_Where = setemp.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "' and ID_Empleado = '" + p(request.getParameter("id_empleado")) + "'";
	    	  setemp.Open();
	    	  
	    	  if(setemp.getNumRows() < 1)
	    	  {
	    		  idmensaje = 3;
	    	      mensaje += "ERROR: El empleado no existe, o no pertenece a esta n&oacute;mina<br>";
	    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	      return false;
	    	  }
	 
			  
	    	  return true;
	    }
	    else
	    {
	    	idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
	    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	return false;
	    }
	}
    */
    
    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("idmovimiento") != null && !request.getParameter("idmovimiento").equals(""))
	    {
	        if((request.getParameter("gravado") != null && !request.getParameter("gravado").equals("")) ||
	           (request.getParameter("exento") != null && !request.getParameter("exento").equals("")) ||
	           (request.getParameter("deduccion") != null && !request.getParameter("deduccion").equals("")) )
	        {
	        	JMovimientosNomSet set = new JMovimientosNomSet(request);
	        	set.m_Where = "ID_Movimiento = '" + p(request.getParameter("idmovimiento")) + "'";
	        	set.Open();
	        	if(set.getNumRows() < 1)
	        	{
	        		idmensaje = 3; mensaje = "ERROR: El movimiento de nómina no existe en el catálogo<br>";
		        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        	return false;
	        	}
	        	
	        	if(set.getAbsRow(0).getDeduccion() &&  
	        			( Float.parseFloat(request.getParameter("gravado")) != 0 || Float.parseFloat(request.getParameter("exento")) != 0) )
	        	{
	        		idmensaje = 1; mensaje = "PRECAUCION: Una deducción no debe tener importes gravados o exentos";
		        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        	return false;
	        	}
	        	
	        	if(!set.getAbsRow(0).getDeduccion() && Float.parseFloat(request.getParameter("deduccion")) != 0 )
	        	{
	        		idmensaje = 1; mensaje = "PRECAUCION: Una percepción no debe tener importes de deducción";
		        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        	return false;
	        	}
	        	
	        	return true;
	        }
	        else // error
	        {
	        	idmensaje = 1; mensaje = "PRECAUCION: Se debe especificar la cantidad gravada, exenta, o deducción <br>";
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	return false;
	        }
			  
	        
	    }
	    else
	    {
	    	idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
	    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	return false;
	    }
	}
    
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
	
	    float gravado = (request.getParameter("gravado") != null && !request.getParameter("gravado").equals("")) ?
	        Float.parseFloat(request.getParameter("gravado")) : 0F;
	    float exento = (request.getParameter("exento") != null && !request.getParameter("exento").equals("")) ?
	        Float.parseFloat(request.getParameter("exento")) : 0F;
	    float deduccion = (request.getParameter("deduccion") != null && !request.getParameter("deduccion").equals("")) ?
	        Float.parseFloat(request.getParameter("deduccion")) : 0F;
	    	   
	    idmensaje = rec.agregaPartida(request, Integer.parseInt(request.getParameter("idmovimiento")), gravado, exento, deduccion, mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	  
	}
	
	public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
	
	    float gravado = (request.getParameter("gravado") != null && !request.getParameter("gravado").equals("")) ?
		   Float.parseFloat(request.getParameter("gravado")) : 0F;
		float exento = (request.getParameter("exento") != null && !request.getParameter("exento").equals("")) ?
		   Float.parseFloat(request.getParameter("exento")) : 0F;
		float deduccion = (request.getParameter("deduccion") != null && !request.getParameter("deduccion").equals("")) ?
		   Float.parseFloat(request.getParameter("deduccion")) : 0F;
		    	   
		idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, Integer.parseInt(request.getParameter("idmovimiento")), gravado, exento, deduccion, mensaje);

	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	  
	}
	
	public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
	
	    rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	  
	}
    
    
    
    public void EliminarEmp(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	  	String str = "select * from sp_nom_prod_nomina_eliminar_esp( '" + p(request.getParameter("id")) + "','" + p(request.getParameter("idempleado")) + "') as (err integer, res varchar, clave integer)";	
	  	
	  	JRetFuncBas rfb = new JRetFuncBas();
		
	  	doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_CAMBIAR", "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	}     
    
    
    public void AgregarCambiarEmp(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
 	   	HttpSession ses = request.getSession(true);
    	JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");

    	String tbl;
    	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_NOM_CALCULO_NOMINA_DET (\n";
    	tbl += "ID_Movimiento smallint NOT NULL ,\n";
    	tbl += "Gravado numeric(10,2) NOT NULL ,\n";
    	tbl += "Exento numeric(10,2) NOT NULL ,\n";
    	tbl += "Deduccion numeric(10,2) NOT NULL \n";
    	tbl += ");\n";

    	for(int i = 0; i < rec.getPartidas().size(); i++)
    	{
    		tbl += "INSERT INTO _TMP_NOM_CALCULO_NOMINA_DET\n";
    		tbl += "VALUES( '" + rec.getPartida(i).getID_Movimiento() + "','" + rec.getPartida(i).getGravado() + "','" + 
    			rec.getPartida(i).getExento() + "','" + rec.getPartida(i).getDeduccion() + "' );\n";
    	}

       	String str = "select * from sp_nom_prod_nomina_actualizar_esp( '" + p(request.getParameter("id")) + "','" + p(rec.getID_Empleado()) + "','" + rec.getFaltas() + "','" + rec.getRecibo() + "','" +
    	rec.getHE() + "','" + rec.getHD() + "','" + rec.getHT() + "','" + rec.getDiasHorasExtras() + "','" + rec.getIXA() + "','" + rec.getIXE() + "','" + rec.getIXM() + "','" + p(rec.getUUID()) + "') as (err integer, res varchar, clave integer)";	
    
       	JRetFuncBas rfb = new JRetFuncBas();
		doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_NOM_CALCULO_NOMINA_DET", rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_CAMBIAR", "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/nomina/nom_nomina_dlg_emp.jsp", request, response);
    	
  	}
    
    public void Enlazar(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String str = "select * from sp_cfd_enlazarnomina('" + p(request.getParameter("id")) + "','" + p(request.getParameter("idempleado")) + "','" + q(request.getParameter("uuid")) + "') as ( err integer, res varchar, clave integer );";
    	
    	//doDebugSQL(request, response, str);
    	
    	JRetFuncBas rfb = new JRetFuncBas();
    	doCallStoredProcedure(request, response, str, rfb);

    	short idmensaje = (short)rfb.getIdmensaje();
    	String mensaje = rfb.getRes();
    			            
    	RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_CAMBIAR", "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",mensaje);
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	      
    }
    
    public short AgregarRecursos(HttpServletRequest request, HttpServletResponse response)
         	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
                          
    	HttpSession ses = request.getSession(true);
    	JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
             
    	idmensaje = rec.agregaRecursos(request, mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

    	return idmensaje;
    }
    
    public short AgregarCabeceroRecibo(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
                  
    	HttpSession ses = request.getSession(true);
        JNomMovDirSes rec = (JNomMovDirSes)ses.getAttribute("nom_nomina_dlg");
        
    	idmensaje = rec.agregaCabecero(request, mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

    	return idmensaje;
    }
    
    public void AgregarCambiarCabecero(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
 	  	JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
  	  	setcom.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "'";
  	  	setcom.Open();

  	  	int tn = Integer.parseInt(request.getParameter("tipo_de_nomina"));
  	  	byte tipo;
  	  	if(setcom.getAbsRow(0).getTipo() == 1) // es estricta
  	  	{
  	  		switch(tn)
  	  		{
  	  		case 12:
  	  			tipo = 1;
  	  			break;
  	  		case 34:
	  			tipo = 3;
	  			break;
  	  		case 56:
	  			tipo = 5;
	  			break;
  	  		case 78:
  	  			tipo = 7;
  	  			break;
  	  		default:
  	  			tipo = 1;
  	  			break;
  	  		}
  	  	}
  	  	else
  	  	{
  	  		switch(tn)
  	  		{
  	  		case 12:
  	  			tipo = 2;
  	  			break;
  	  		case 34:
	  			tipo = 4;
	  			break;
  	  		case 56:
	  			tipo = 6;
	  			break;
  	  		case 78:
  	  			tipo = 8;
  	  			break;
  	  		default:
  	  			tipo = 2;
  	  			break;
  	  		}
  	  	}
  	
  	  	int num_dias;
 	  	Date desde = JUtil.estFecha(request.getParameter("desde"));
 	  	Date hasta = JUtil.estFecha(request.getParameter("hasta"));
 	  	num_dias = (int)JUtil.getFechaDiff(hasta, desde, "dias") + 1;
 	  	
  	  	
  	  	String str;
  	  	if(request.getParameter("proceso").equals("AGREGAR_NOMINA"))
  	  		str = "select * from  sp_nom_prod_nomina_agregar( ";
  	  	else
  	  		str = "select * from  sp_nom_prod_nomina_cambiar( ";
  	  	
  	  	str += "'0','" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "','" + p(request.getParameter("ano")) + "','" + p(request.getParameter("numero_nomina")) + "','" + tipo + "','" +
  	  		p(JUtil.obtFechaSQL(request.getParameter("desde"))) + "','" + p(JUtil.obtFechaSQL(request.getParameter("hasta"))) + "','" + num_dias + "','0','" + JUtil.obtMes(request.getParameter("hasta")) + "') as (err integer, res varchar, clave integer)";	
	    
  	  	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_" + ( request.getParameter("proceso").equals("AGREGAR_NOMINA") ? "AGREGAR" : "CAMBIAR" ), "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",rfb.getRes());
	    irApag("/forsetiweb/nomina/nom_nomina_dlg.jsp", request, response);
	   	  	
    }
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	  	
	  	String str = "select * from sp_nom_prod_nomina_eliminar('" + p(request.getParameter("id")) + "') as (err integer, res varchar, clave integer)";
	  	
	  	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_ELIMINAR", "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	    
    }
    
    public void Proteger(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
  	
    	String str = "select * from sp_nom_calculo_nomina_proteger('" + p(request.getParameter("id")) + "') as (err integer, res varchar, clave integer)";
  	
    	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_CAMBIAR", "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	} 
    
    public void Generar(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		
		String str = "select * from sp_nom_calculo_nomina_generar('" + p(request.getParameter("id")) + "') as (err integer, res varchar, clave integer)";
  	
		//doDebugSQL(request, response, str);
		
    	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_AGREGAR", "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		
	} 
    
    public void Calcular(HttpServletRequest request, HttpServletResponse response, int tipo)
		throws ServletException, IOException
	{
		
		String str, irapag;
		
		if(tipo == 12) // Normal
		{
			str = "select * from sp_nom_calculo_nomina('" + p(request.getParameter("id")) + "','" + 
			( request.getParameter("inconsistencias") == null ? "0" : "1" ) + "','" + p(request.getParameter("mesincon")) + "','" + p(request.getParameter("anoincon")) + "','" + 
			( request.getParameter("hepf") == null ? "0" : "1" ) + "','" + ( request.getParameter("vales") == null ? "0" : "1" ) + "') as (err integer, res varchar, clave integer)";
			irapag = "/forsetiweb/nomina/nom_nomina_dlg_calcular.jsp";
 		}
		else // 56 Agunaldos
		{
			str = "select * from sp_nom_calculo_nomina('" + p(request.getParameter("id")) + "','0','1','2000','0','0') as (err integer, res varchar, clave integer)";
			irapag = "/forsetiweb/caja_mensajes.jsp";
	 	}
		
		JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_NOMINA_AGREGAR", "NNOM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "||",rfb.getRes());
	    irApag(irapag, request, response);
	
	}
    
	@SuppressWarnings("rawtypes")
	public void SubirArchivosCFD(HttpServletRequest request, HttpServletResponse response, JFacturasXML nomrecxml, Vector archivos)
			throws ServletException, IOException
	{
		short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
			
		HttpSession ses = request.getSession(true);
		JForsetiCFD cfd = (JForsetiCFD)ses.getAttribute("nom_cfd");
		if(cfd == null)
		{
			cfd = new JForsetiCFD();
			ses.setAttribute("nom_cfd", cfd);
		}
		else
			cfd.resetearCertComp();
	        
		idmensaje = cfd.SubirArchivosCFDI(request, archivos, mensaje, "N");
		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		
		if(idmensaje == JForsetiCFD.OKYDOKY)
		{
			idmensaje = cfd.VerificarFacturasSubidas(request, nomrecxml, mensaje, "N");
			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
			
			if(idmensaje == JForsetiCFD.OKYDOKY)
			{
				idmensaje = cfd.GuardarDocumentoCFDI(request, Integer.parseInt(getSesion(request).getSesion("NOM_NOMINA").getEspecial()), nomrecxml.getArchivoXML(), nomrecxml.getArchivoPDF(), mensaje, "N");
				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				
			}
			
		}
		
		JFacturasXML rec = (JFacturasXML)ses.getAttribute("nom_rec_xml");
		if(rec != null)
			rec = null;
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
	}    
    
    
}
