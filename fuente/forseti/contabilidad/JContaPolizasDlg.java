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
package forseti.contabilidad;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import java.sql.Date;

import forseti.JFacturasXML;
import forseti.JForsetiApl;
import forseti.JForsetiCFD;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JContaPoliazasSetDetalleV2;
import forseti.sets.JContaPolizasSetV2;
import forseti.sets.JSatBancosSet;
import forseti.sets.JSatMetodosPagoSet;
import forseti.sets.JSatMonedasSet;

@SuppressWarnings("serial")
public class JContaPolizasDlg extends JForsetiApl
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
      //request.setAttribute("fsi_modulo",request.getRequestURI());
      super.doPost(request,response);

      String conta_polizas_dlg = "";
      request.setAttribute("conta_polizas_dlg",conta_polizas_dlg);

      String mensaje = ""; short idmensaje = -1;

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
  	  			  JFacturasXML factxml = (JFacturasXML)ses.getAttribute("fact_xml");
  	  			  Vector archivos = new Vector();
  	  			  DiskFileUpload fu = new DiskFileUpload();
  	  			  List items = fu.parseRequest(request);
  				  Iterator iter = items.iterator();
  				  while (iter.hasNext()) 
  	  			  {
  	  				  FileItem item = (FileItem)iter.next();
  	  				  if (item.isFormField()) 
  	  				  	  factxml.getParametros().put(item.getFieldName(), item.getString());
  	  				  else
  	  				  	  archivos.addElement(item);
  	  			  }
  	  			  
  				  if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
  				  {
  					  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
  					  getSesion(request).setID_Mensaje(idmensaje, mensaje);
  					  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
  					  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  					  return;
  				  }
	  	  			
  				  String identidad = factxml.getParametros().getProperty("identidad");
  			  	
  				  if(identidad.substring(0,4).equals("NADA"))
  				  {
  					  idmensaje = 1; mensaje += "PRECAUCION: Se debe especificar la entidad de compra o venta de los archivos a cargar";
					  getSesion(request).setID_Mensaje(idmensaje, mensaje);
					  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
					  return;
  				  }
  				  
  	  			  SubirArchivosCFD(request, response, factxml, archivos);
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
        if(request.getParameter("proceso").equals("AGREGAR_POLIZA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CREAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CREAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CREAR","POLZ||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response))
            {
              AgregarCambiar(request, response, JForsetiApl.AGREGAR);
              return;
            }
            irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              AgregarPartida(request, response);
            
            irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              EditarPartida(request, response);
            
            irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
             BorrarPartida(request, response);
             
             irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
             return;
          }
          else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            HttpSession ses = request.getSession(true);
            JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");
            if(pol == null)
            {
              pol = new JContaPolizasSes();
              ses.setAttribute("conta_polizas_dlg", pol);
            }
            else
              pol.resetear();

            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_POLIZA"))
        {
        	
          
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS","POLZ||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if(valoresParam.length == 1)
            {

              HttpSession ses = request.getSession(true);
              JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");
              if(pol == null)
              {
                pol = new JContaPolizasSes();
                ses.setAttribute("conta_polizas_dlg", pol);
              }
              else
                pol.resetear();

              // Llena la poliza
              JContaPoliazasSetDetalleV2 set = new JContaPoliazasSetDetalleV2(request);
              set.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
              set.m_OrderBy = "Part ASC";
              set.Open();
              for(int i = 0; i < set.getNumRows(); i++)
              {
                pol.agregaPartida( set.getAbsRow(i).getNumero(), set.getAbsRow(i).getNombre(), set.getAbsRow(i).getConcepto(),
                                   set.getAbsRow(i).getParcial(), set.getAbsRow(i).getMoneda(),
                                   set.getAbsRow(i).getTC(), set.getAbsRow(i).getDebe(), set.getAbsRow(i).getHaber() );
              }
              String numero = Integer.toString(set.getNumRows());
              request.setAttribute("numero",numero);

              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_POLIZAS","POLZ|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "||","");
              irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CARGAR_XML"))
        {
      	  // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
      	  Integer subir_archivos = new Integer(2);
      	  request.setAttribute("subir_archivos", subir_archivos);
      		  
      	  HttpSession ses = request.getSession(true);
      	  JFacturasXML rec = (JFacturasXML)ses.getAttribute("fact_xml");
      		  
      	  if(rec == null)
      	  {
      		  rec = new JFacturasXML();
      		  ses.setAttribute("fact_xml", rec);
      	  }
      	  else
      	  {
      		  rec = null;
      		  rec = new JFacturasXML();
      		  ses.setAttribute("fact_xml", rec);
      	  }
         			  
      	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      	  irApag("/forsetiweb/subir_archivos.jsp?verif=/servlet/CEFContaPolizasDlg&archivo_1=xml&archivo_2=pdf&proceso=CARGAR_XML&subproceso=ENVIAR&CE=" + p(getSesion(request).getSesion("CONT_POLIZAS").getEspecial()), request, response);
      	  return;
      	  
        }
        else if(request.getParameter("proceso").equals("CONTABILIDAD_ELECTRONICA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }
          
          // Solicitud de envio a procesar
          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if(valoresParam.length == 1)
            {
              //Se ha cancelado el proceso de tercer nivel
              //if(request.getParameter("cancelado") != null || request.getParameter("actualizar") != null)
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              
              // Hace revisiones iniciales
              JContaPolizasSetV2 setp = new JContaPolizasSetV2(request);
              setp.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
              setp.Open();

              if(setp.getAbsRow(0).getEstatus().equals("C"))
              {
                idmensaje = 1; mensaje += JUtil.Msj("CEF","CONT_POLIZAS","DLG","MSJ-PROCERR2",2);// No se puede gestionar contabilidad electrónica en una póliza cancelada.
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
              }
              
              irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce.jsp", request, response);
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
        ////////////////////////////////////////////////////////////////////
        else if(request.getParameter("proceso").equals("CHEQUE_CE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
           
          // Solicitud de envio a procesar
          if(request.getParameter("idpart") != null)
          {
            String[] valoresParam = request.getParameterValues("idpart");
            if(valoresParam.length == 1)
            {
	          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
	          {
	            
	            if(VerificarParametrosCECheque(request, response))
	            {
	            	GestionarCheque(request, response);
	            	return;
	            }
	            irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_cheque.jsp", request, response);
	            return;
	          }
	          else
	          {
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_cheque.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("TRANSFERENCIA_CE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
           
          // Solicitud de envio a procesar
          if(request.getParameter("idpart") != null)
          {
            String[] valoresParam = request.getParameterValues("idpart");
            if(valoresParam.length == 1)
            {
	          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
	          {
	            
	            if(VerificarParametrosCETransferencia(request, response))
	            {
	            	GestionarTransferencia(request, response);
	            	return;
	            }
	            irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_transferencia.jsp", request, response);
	            return;
	          }
	          else
	          {
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_transferencia.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("OTRMETPAGO_CE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
           
          // Solicitud de envio a procesar
          if(request.getParameter("idpart") != null)
          {
            String[] valoresParam = request.getParameterValues("idpart");
            if(valoresParam.length == 1)
            {
	          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
	          {
	            
	            if(VerificarParametrosCEOtrMetodoPago(request, response))
	            {
	            	GestionarOtrMetodoPago(request, response);
	            	return;
	            }
	            irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_otrmetodopago.jsp", request, response);
	            return;
	          }
	          else
	          {
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_otrmetodopago.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("COMPROBANTE_CE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
           
          // Solicitud de envio a procesar
          if(request.getParameter("idpart") != null)
          {
            String[] valoresParam = request.getParameterValues("idpart");
            if(valoresParam.length == 1)
            {
	          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
	          {
	            
	            if(VerificarParametrosCEComprobante(request, response))
	            {
	            	GestionarComprobante(request, response);
	            	return;
	            }
	            irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_comprobante.jsp", request, response);
	            return;
	          }
	          else
	          {
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_comprobante.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_CE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
           
          // Solicitud de envio a procesar
          if(request.getParameter("idce") != null)
          {
            String[] valoresParam = request.getParameterValues("idce");
            if(valoresParam.length == 1)
            {
            	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            	{
            		if(request.getParameter("idce").substring(0,3).equals("CHQ"))
            		{	
	            		if(VerificarParametrosCECheque(request, response))
	            		{
	            			GestionarCheque(request, response);
	            			return;
	            		}
	            		irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_cheque.jsp", request, response);
            		}
            		else if(request.getParameter("idce").substring(0,3).equals("TRN"))
            		{	
	            		if(VerificarParametrosCETransferencia(request, response))
	            		{
	            			GestionarTransferencia(request, response);
	            			return;
	            		}
	            		irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_transferencia.jsp", request, response);
            		}
            		else if(request.getParameter("idce").substring(0,3).equals("OMP"))
            		{	
	            		if(VerificarParametrosCEOtrMetodoPago(request, response))
	            		{
	            			GestionarOtrMetodoPago(request, response);
	            			return;
	            		}
	            		irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_otrmetodopago.jsp", request, response);
            		}
            		else //(request.getParameter("idce").substring(0,3).equals("XML"))
            		{	
	            		if(VerificarParametrosCEComprobante(request, response))
	            		{
	            			GestionarComprobante(request, response);
	            			return;
	            		}
	            		irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_comprobante.jsp", request, response);
            		}
            		
  	          		return;
  	          	}
  	          	else
  	          	{
  	          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	          		if(request.getParameter("idce").substring(0,3).equals("CHQ"))
  	          			irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_cheque.jsp", request, response);
  	          		else if(request.getParameter("idce").substring(0,3).equals("TRN"))
  	          			irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_transferencia.jsp", request, response);
  	          		else if(request.getParameter("idce").substring(0,3).equals("OMP"))
	          			irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_otrmetodopago.jsp", request, response);
	          		else
  	          			irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_comprobante.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_CE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CE","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
           
          // Solicitud de envio a procesar
          if(request.getParameter("idce") != null)
          {
            String[] valoresParam = request.getParameterValues("idce");
            if(valoresParam.length == 1)
            {
	          EliminarCE(request, response);
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
        ////////////////////////////////////////////////////////////////////
        else if(request.getParameter("proceso").equals("CAMBIAR_POLIZA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CAMBIAR","POLZ||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            HttpSession ses = request.getSession(true);
            JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");
            // Verificacion de los especiales de cambio
            if(pol.getPeriodo() != JUtil.obtMes(request.getParameter("fecha")))
            {
              idmensaje = 1; mensaje += JUtil.Msj("CEF","CONT_POLIZAS","DLG","MSJ-PROCERR",4);//"PRECAUCION: No se puede cambiar la p�liza porque debe de ser del mismo periodo contable <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
              return;
            }
            else
            {
            	if(VerificarParametros(request, response))
            	{
            		AgregarCambiar(request, response, JForsetiApl.CAMBIAR);
            		return;
            	}
            	irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
                return;
            }
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              AgregarPartida(request, response);
            
            irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              EditarPartida(request, response);
            
            irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
             BorrarPartida(request, response);
             
             irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
             return;
          }
          else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de CAMBIADO `por primera vez
          {
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
                // Hace revisiones iniciales
                JContaPolizasSetV2 setp = new JContaPolizasSetV2(request);
                setp.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
                setp.Open();

                if(setp.getAbsRow(0).getEstatus().equals("C") || !setp.getAbsRow(0).getRef().equals(""))
                {
                  idmensaje = 1; mensaje += JUtil.Msj("CEF","CONT_POLIZAS","DLG","MSJ-PROCERR",5);// "PRECAUCION: No se puede cambiar una póliza cancelada o externa <br>";
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
                }
                else
                {
                    HttpSession ses = request.getSession(true);
                    JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");
                    if(pol == null)
                    {
                      pol = new JContaPolizasSes();
                      ses.setAttribute("conta_polizas_dlg", pol);
                    }
                    else
                      pol.resetear();

                    // Llena la poliza
                    Calendar fecha = new GregorianCalendar();
                    fecha.setTime(setp.getAbsRow(0).getFecha());
                    pol.setParametros(setp.getAbsRow(0).getEstatus(), setp.getAbsRow(0).getRef(), (byte)JUtil.obtMes(fecha));

                    JContaPoliazasSetDetalleV2 set = new JContaPoliazasSetDetalleV2(request);
                    set.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
                    set.m_OrderBy = "Part ASC";
                    set.Open();
                    for(int i = 0; i < set.getNumRows(); i++)
                    {
                      pol.agregaPartida( set.getAbsRow(i).getNumero(), set.getAbsRow(i).getNombre(), set.getAbsRow(i).getConcepto(),
                                         set.getAbsRow(i).getParcial(), set.getAbsRow(i).getMoneda(),
                                         set.getAbsRow(i).getTC(), set.getAbsRow(i).getDebe(), set.getAbsRow(i).getHaber() );
                    }

                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);
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
        }
        else if(request.getParameter("proceso").equals("CANCELAR_POLIZA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLIZAS_CANCELAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CANCELAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CANCELAR","POLZ||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if (valoresParam.length == 1)
            {
            	//Hace revisiones iniciales
                JContaPolizasSetV2 setp = new JContaPolizasSetV2(request);
                setp.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
                setp.Open();

                if(setp.getAbsRow(0).getEstatus().equals("C") || !setp.getAbsRow(0).getRef().equals(""))
                {
                  idmensaje = 1; mensaje += JUtil.Msj("CEF","CONT_POLIZAS","DLG","MSJ-PROCERR2",1); //"PRECAUCION: No se puede cancelar una póliza ya cancelada o externa <br>";
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
                }
      
                Cancelar(request, response, setp.getAbsRow(0).getFecha());
                return;
 
            }
            else
            {
               idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite cancelar una póliza a la vez <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador de la póliza que se quiere cancelar<br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("RASTREAR_POLIZA"))
        {
        	if(!getSesion(request).getPermiso("CONT_POLIZAS_CONSULTAR"))
            {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS_CONSULTAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CONSULTAR","POLZ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
            }

            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if (valoresParam.length == 1)
              {
              	
                  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("CONT_POLIZAS").generarTitulo(JUtil.Msj("CEF","CONT_POLIZAS","VISTA","CONSULTAR_POLIZA",3)),
                  								"POLZ",request.getParameter("ID"));
                  String rastreo_imp = "true";
                  request.setAttribute("rastreo_imp", rastreo_imp);
                  // Ahora pone los atributos para el jsp
                  request.setAttribute("rastreo", rastreo);
                  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_POLIZAS_CONSULTAR","POLZ|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "||","");
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
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("CONT_POLIZAS"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS","POLZ" + "||||",mensaje);
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
                        String SQLCab = "select * from view_cont_polizas_impcab where ID = " + request.getParameter("ID");
                        String SQLDet = "select * from view_cont_polizas_impdet where ID = " + request.getParameter("ID") + " order by Part asc";
                        idmensaje = Imprimir(SQLCab, SQLDet,
                                             request.getParameter("idformato"), bsmensaje,
                                             request, response);
        			
                        if (idmensaje != -1)
        				{
        					getSesion(request).setID_Mensaje(idmensaje, bsmensaje.toString());
        					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        					return;
        				}
        			}
        			else // significa que debe llamar a la ventana de formatos de impresion
        			{
        				request.setAttribute("impresion", "CEFContaPolizasDlg");
        				request.setAttribute("tipo_imp", "CONT_POLIZAS");
        				request.setAttribute("formato_default", null);
        				
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

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("cuenta") != null && request.getParameter("idmoneda") != null &&
         request.getParameter("concepto_part") != null && request.getParameter("tc") != null &&
         !request.getParameter("cuenta").equals("") && !request.getParameter("idmoneda").equals("") &&
         !request.getParameter("concepto_part").equals("") && !request.getParameter("tc").equals("") )
      {
        if((request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ||
           (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) )
        {
          return true;
        }
        else // error
        {
          idmensaje = 1; mensaje = JUtil.Msj("CEF","CONT_POLIZAS","DLG","MSJ-PROCERR",2); //Se debe especificar si es un retiro o un abono <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
      }
      else
      {
          idmensaje = 1; mensaje = JUtil.Msj("CEF","CONT_POLIZAS","DLG","MSJ-PROCERR",3); //"PRECAUCION: Se deben enviar los parametros de cuenta, clave de moneda, concepto de partida, y tipo de cambio <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public boolean VerificarParametrosCECheque(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("ID") != null && request.getParameter("idpart") != null && request.getParameter("num") != null && request.getParameter("banco") != null && request.getParameter("banemisext") != null &&
    		request.getParameter("ctaori") != null && request.getParameter("fecha") != null && request.getParameter("monto") != null && request.getParameter("tipcamb") != null && request.getParameter("benef") != null && request.getParameter("rfc") != null &&
    		!request.getParameter("ID").equals("") && !request.getParameter("idpart").equals("") && !request.getParameter("num").equals("") && 
    	    !request.getParameter("ctaori").equals("") && !request.getParameter("fecha").equals("") && !request.getParameter("monto").equals("") && !request.getParameter("tipcamb").equals("") && !request.getParameter("benef").equals("") && !request.getParameter("rfc").equals("") )
    	{
    		JSatBancosSet setBan = new JSatBancosSet(request);
    	    setBan.m_Where = "Clave = '" + p(request.getParameter("banco")) + "'";
    	    setBan.Open();
    		
    		if(setBan.getNumRows() < 1)
    		{
    			idmensaje = 3; mensaje += "ERROR: El banco para el SAT no es válido<br>";
       		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
       		 	return false;
       	 	}
    		else 
    		{
    			if(setBan.getAbsRow(0).getClave().equals("000") && request.getParameter("banemisext").equals(""))
    			{
    				idmensaje = 2; mensaje += "PRECAUCION: Se debe establecer el banco extranjero de origen<br>";
           		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
           		 	return false;
    			}
    			if(!setBan.getAbsRow(0).getClave().equals("000") && !request.getParameter("banemisext").equals(""))
    			{
    				idmensaje = 2; mensaje += "PRECAUCION: No se debe establecer el banco extranjero de origen porque ya se ha seleccionado un banco nacional<br>";
           		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
           		 	return false;
    			}
    		}
    		
    		JSatMonedasSet setMon = new JSatMonedasSet(request);
    	    setMon.m_Where = "Clave = '" + p(request.getParameter("moneda")) + "'";
    	    setMon.Open();
    	    
    	    if(setMon.getNumRows() < 1)
    	    {
    	    	idmensaje = 3; mensaje += "ERROR: La moneda especificada no existe";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    	    }
    	    
    		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
    		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
    		{
    			idmensaje = 1; mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}
    		
    		return true;
    	}
    	else
    	{
    		idmensaje = 3; mensaje = JUtil.Msj("GLB","GLB","GLB","PARAM-NULO");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    }
    
    public boolean VerificarParametrosCETransferencia(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
       	short idmensaje = -1; String mensaje = "";
       	// Verificacion
       	if(request.getParameter("ID") != null && request.getParameter("idpart") != null && request.getParameter("ctaori") != null && request.getParameter("bancoori") != null && request.getParameter("bancooriext") != null &&
       		request.getParameter("monto") != null && request.getParameter("tipcamb") != null && request.getParameter("ctadest") != null && request.getParameter("bancodest") != null && request.getParameter("bancodestext") != null && request.getParameter("fecha") != null && 
       		request.getParameter("benef") != null && request.getParameter("rfc") != null &&
       		!request.getParameter("ID").equals("") && !request.getParameter("idpart").equals("") &&  
       	    !request.getParameter("ctaori").equals("") && !request.getParameter("monto").equals("") && !request.getParameter("tipcamb").equals("") && !request.getParameter("ctadest").equals("") && 
       	    !request.getParameter("fecha").equals("") && !request.getParameter("benef").equals("") && !request.getParameter("rfc").equals("") )
       	{
       		JSatBancosSet setBan = new JSatBancosSet(request);
    	    setBan.m_Where = "Clave = '" + p(request.getParameter("bancoori")) + "'";
    	    setBan.Open();
    		
    		if(setBan.getNumRows() < 1)
    		{
    			idmensaje = 3; mensaje += "ERROR: El banco origen para el SAT no es válido<br>";
       		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
       		 	return false;
       	 	}
    		else 
    		{
    			if(setBan.getAbsRow(0).getClave().equals("000") && request.getParameter("bancooriext").equals(""))
    			{
    				idmensaje = 1; mensaje += "PRECAUCION: Se debe establecer el banco extranjero de origen<br>";
           		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
           		 	return false;
    			}
    			if(!setBan.getAbsRow(0).getClave().equals("000") && !request.getParameter("bancooriext").equals(""))
    			{
    				idmensaje = 1; mensaje += "PRECAUCION: No se debe establecer el banco extranjero de origen porque ya se ha seleccionado un banco nacional<br>";
           		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
           		 	return false;
    			}
    		}
    		
    		setBan.m_Where = "Clave = '" + p(request.getParameter("bancodest")) + "'";
    	    setBan.Open();
    		
    		if(setBan.getNumRows() < 1)
    		{
    			idmensaje = 3; mensaje += "ERROR: El banco destino para el SAT no es válido<br>";
       		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
       		 	return false;
       	 	}
    		else 
    		{
    			if(setBan.getAbsRow(0).getClave().equals("000") && request.getParameter("bancodestext").equals(""))
    			{
    				idmensaje = 1; mensaje += "PRECAUCION: Se debe establecer el banco extranjero de destino<br>";
           		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
           		 	return false;
    			}
    			if(!setBan.getAbsRow(0).getClave().equals("000") && !request.getParameter("bancodestext").equals(""))
    			{
    				idmensaje = 1; mensaje += "PRECAUCION: No se debe establecer el banco extranjero de destino porque ya se ha seleccionado un banco nacional<br>";
           		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
           		 	return false;
    			}
    		}
    		
    		JSatMonedasSet setMon = new JSatMonedasSet(request);
    	    setMon.m_Where = "Clave = '" + p(request.getParameter("moneda")) + "'";
    	    setMon.Open();
    	    
    	    if(setMon.getNumRows() < 1)
    	    {
    	    	idmensaje = 3; mensaje += "ERROR: La moneda especificada no existe";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    	    }
       		
       		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
       		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
       		{
       			idmensaje = 1; mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
       			getSesion(request).setID_Mensaje(idmensaje, mensaje);
       			return false;
      		}
	        		
      		return true;
       	}
       	else
       	{
       		idmensaje = 3; mensaje = JUtil.Msj("GLB","GLB","GLB","PARAM-NULO");
       		getSesion(request).setID_Mensaje(idmensaje, mensaje);
       		return false;
       	}
    }
    
    public boolean VerificarParametrosCEOtrMetodoPago(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
       	short idmensaje = -1; String mensaje = "";
       	// Verificacion
       	if(request.getParameter("ID") != null && request.getParameter("idpart") != null && request.getParameter("monto") != null && request.getParameter("tipcamb") != null && request.getParameter("fecha") != null && 
       		request.getParameter("benef") != null && request.getParameter("rfc") != null &&
       		!request.getParameter("ID").equals("") && !request.getParameter("idpart").equals("") && !request.getParameter("monto").equals("") && !request.getParameter("tipcamb").equals("") && !request.getParameter("fecha").equals("") && 
       		!request.getParameter("benef").equals("") && !request.getParameter("rfc").equals("") )
       	{
       		JSatMetodosPagoSet setMet = new JSatMetodosPagoSet(request);
    	    setMet.m_Where = "Clave = '" + p(request.getParameter("metpagopol")) + "'";
    	    setMet.Open();
    		
    		if(setMet.getNumRows() < 1)
    		{
    			idmensaje = 3; mensaje += "ERROR: El metodo de pago para el SAT no es válido<br>";
       		 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
       		 	return false;
       	 	}
    		    		
    		JSatMonedasSet setMon = new JSatMonedasSet(request);
    	    setMon.m_Where = "Clave = '" + p(request.getParameter("moneda")) + "'";
    	    setMon.Open();
    	    
    	    if(setMon.getNumRows() < 1)
    	    {
    	    	idmensaje = 3; mensaje += "ERROR: La moneda especificada no existe";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    	    }
       		
       		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
       		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
       		{
       			idmensaje = 1; mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
       			getSesion(request).setID_Mensaje(idmensaje, mensaje);
       			return false;
      		}
	        		
      		return true;
       	}
       	else
       	{
       		idmensaje = 3; mensaje = JUtil.Msj("GLB","GLB","GLB","PARAM-NULO");
       		getSesion(request).setID_Mensaje(idmensaje, mensaje);
       		return false;
       	}
    }
    
    public boolean VerificarParametrosCEComprobante(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("ID") != null && request.getParameter("idpart") != null && request.getParameter("uuidcfdi") != null && request.getParameter("monto") != null && request.getParameter("tipcamb") != null && request.getParameter("rfc") != null &&
    		request.getParameter("cfdcbbserie") != null	&& request.getParameter("cfdcbbnumfol") != null	&& request.getParameter("numfactext") != null && request.getParameter("taxid") != null	&&   
    		!request.getParameter("ID").equals("") && !request.getParameter("idpart").equals("") && !request.getParameter("monto").equals("") && !request.getParameter("tipcamb").equals(""))
    	{
    		String ID_Tipo = "";
    		
    		if(!request.getParameter("uuidcfdi").equals("") && !request.getParameter("uuidcfdi").equals("                                    "))
    			ID_Tipo = "CompNal";
    		else if(!request.getParameter("cfdcbbserie").equals("") && !request.getParameter("cfdcbbnumfol").equals(""))
    			ID_Tipo = "CompNalOtr";
    		else if(!request.getParameter("numfactext").equals(""))
    			ID_Tipo = "CompExt";
    		
    		if(ID_Tipo.equals(""))
    		{
    			idmensaje = 3; mensaje += "ERROR: Se debe seleccionar el tipo de comprobante a ingresar, ya sea, ingresando el UUID CFDI para comprobantes nacionales CFDI, la serie y folio para otros comprobantes nacionales sin soporte a CFDI, o el número de factura extranjera para comprobantes de origen extrangero";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}
    		
    		JSatMonedasSet setMon = new JSatMonedasSet(request);
    	    setMon.m_Where = "Clave = '" + p(request.getParameter("moneda")) + "'";
    	    setMon.Open();
    	    
    	    if(setMon.getNumRows() < 1)
    	    {
    	    	idmensaje = 3; mensaje += "ERROR: La moneda especificada no existe";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    	    }
    	    
    		if(ID_Tipo.equals("CompNal"))
    		{
    			if(!request.getParameter("uuidcfdi").matches("[a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}"))
    			{
        			idmensaje = 3; mensaje += "ERROR: La clave del CFDI debe constar de exactamente 36 caracteres con el siguiente patrón: [a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}";
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			return false;
        		}
            		
        		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
        		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
        		{
        			idmensaje = 1; mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			return false;
        		}
    		}
    		else if(ID_Tipo.equals("CompNalOtr"))
    		{
    			if(!request.getParameter("cfdcbbserie").matches("[A-Z]{1,10}") || Integer.parseInt(request.getParameter("cfdcbbnumfol")) < 1)
    			{
        			idmensaje = 3; mensaje += "ERROR: La serie del comprobante sin soporte de CFDI debe constar de un máximo de 10 caracteres de la A a la Z mayúsculas, y el número de folio debe ser mayor que cero";
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			return false;
        		}
            		
        		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
        		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
        		{
        			idmensaje = 1; mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			return false;
        		}
    		}
    		else //CompExt
    		{
    			if(request.getParameter("numfactext").length() > 36 || request.getParameter("taxid").length() > 30)
    			{ 
        			idmensaje = 3; mensaje += "ERROR: El número de la factura extrangera y el TaxID deben constar de un máximo de 36 y 30 caracteres alfanuméricos respectivamente";
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			return false;
        		}
    		}
    		        		
    		return true;
    	}
    	else
    	{
    		idmensaje = 3; mensaje = JUtil.Msj("GLB","GLB","GLB","PARAM-NULO");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    }
    
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");

      double debe = (request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ?
          Float.parseFloat(request.getParameter("debe")) : 0F;
      double haber = (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) ?
          Float.parseFloat(request.getParameter("haber")) : 0F;
      double tc = Float.parseFloat(request.getParameter("tc"));
      byte idmoneda = Byte.parseByte(request.getParameter("idmoneda"));

      idmensaje = pol.agregaPartida(request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), pt(request.getParameter("concepto_part")),
                        idmoneda, tc, debe, haber, mensaje);

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      
    }

    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");

      double debe = (request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ?
          Float.parseFloat(request.getParameter("debe")) : 0F;
      double haber = (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) ?
          Float.parseFloat(request.getParameter("haber")) : 0F;
      double tc = Float.parseFloat(request.getParameter("tc"));
      byte idmoneda = Byte.parseByte(request.getParameter("idmoneda"));

      idmensaje = pol.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), pt(request.getParameter("concepto_part")),
                        idmoneda, tc, debe, haber, mensaje);

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      
    }

    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");

      pol.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      
    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("fecha") != null && request.getParameter("concepto") != null &&
          !request.getParameter("fecha").equals("") && !request.getParameter("concepto").equals(""))
      {
        HttpSession ses = request.getSession(true);
        JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");

        if(pol.getPartidas().size() < 1 || (pol.getSumDebe() != pol.getSumHaber()) )
        {
          idmensaje = 3; mensaje = JUtil.Msj("CEF","CONT_POLIZAS","DLG","MSJ-PROCERR",1); //"ERROR: La p�liza no contiene partidas o las sumas del cargo y abono no coinciden <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        else
          return true;
      }
      else
      {
          idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public void Cancelar(HttpServletRequest request, HttpServletResponse response, Date fecha)
    	throws ServletException, IOException
	{
		JRetFuncBas rfb = new JRetFuncBas();
    	
		String str = "SELECT * FROM sp_cont_polizas_cancelar('" + p(request.getParameter("ID")) + "','" + p(JUtil.obtFechaSQL(fecha)) + "' ) as ( err integer, res varchar, clave integer ) ";
		
    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_POLIZAS_CANCELAR","POLZ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    }    
    
    public void AgregarCambiar(HttpServletRequest request, HttpServletResponse response, short proc)
      throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      JContaPolizasSes pol = (JContaPolizasSes)ses.getAttribute("conta_polizas_dlg");

      String tbl;
      tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_CONT_POLIZAS_DETALLE (\n";
      tbl += "Part smallint NOT NULL ,\n";
      tbl += "Cuenta char(19) NOT NULL ,\n";
      tbl += "Concepto varchar(80) NOT NULL ,\n";
      tbl += "Parcial numeric(19,4) NOT NULL ,\n";
      tbl += "Moneda smallint NOT NULL ,\n";
      tbl += "TC numeric(19,4) NOT NULL ,\n";
      tbl += "Debe numeric(19,4) NOT NULL ,\n";
      tbl += "Haber numeric(19,4) NOT NULL \n";
      tbl += "); \n";
        
      for(int i = 0; i < pol.getPartidas().size(); i++)
      {
         tbl += "INSERT INTO _TMP_CONT_POLIZAS_DETALLE\n";
         tbl += "VALUES( '" + i + "','" + p(JUtil.obtCuentas(pol.getPartida(i).getCuenta(),(byte)19)) + "','" +
             p(pol.getPartida(i).getConcepto()) + "','" + pol.getPartida(i).getParcial() + "','" + pol.getPartida(i).getID_Moneda() +
             "','" + pol.getPartida(i).getTC() + "','" + pol.getPartida(i).getDebe() + "','" + pol.getPartida(i).getHaber() + "');\n";
      }

      String str = "SELECT * FROM sp_cont_polizas_", proceso;
      if(proc == AGREGAR)
      {
    	  str += "agregar(";
    	  proceso = "CONT_POLIZAS_CREAR";
      }
      else
      {
    	  str += "cambiar('" + p(request.getParameter("ID")) + "',";
    	  proceso = "CONT_POLIZAS_CAMBIAR";
      }
      
      str += "'" + p(request.getParameter("idtipo")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) +
          "','" + p(request.getParameter("concepto")) + "','0','','" + pol.getSumDebe() + "','" + p(getSesion(request).getSesion("CONT_POLIZAS").getEspecial()) + "' ) as ( err integer, res varchar, clave integer ) ";
      
      //doDebugSQL(request,response,tbl + "\n" + str);  
      JRetFuncBas rfb = new JRetFuncBas();
  	
		
      doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_CONT_POLIZAS_DETALLE;", rfb);
    
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), proceso, "POLZ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "||",rfb.getRes());
      irApag("/forsetiweb/contabilidad/conta_polizas_dlg.jsp", request, response);


    }
    
    public void GestionarOtrMetodoPago(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String idce = request.getParameter("proceso").equals("CAMBIAR_CE") ? "'" + p(request.getParameter("idce").substring(4)) + "'" : "null";
    	String idpart = request.getParameter("proceso").equals("CAMBIAR_CE") ? "null" : "'" + p(request.getParameter("idpart")) + "'";
    	
    	double tc = 1.0000;
		
		if(!request.getParameter("moneda").equals("MXN"))
			tc = Double.parseDouble(request.getParameter("tipcamb"));

    	String str = "select * from sp_cont_polizas_ce_otrmetodopago(" + idce + ",'" + p(request.getParameter("ID")) + "'," + idpart + ",'" + p(request.getParameter("metpagopol")) + "','" +
    			p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("benef")) + "','" + p(JUtil.fco(JUtil.frfc(request.getParameter("rfc")))) + "','" + 
    			p(request.getParameter("monto")) + "','" + p(request.getParameter("moneda")) + "','" + tc + "') as ( err integer, res varchar, clave integer ) ";
    	
    	//doDebugSQL(request, response, str);
    	JRetFuncBas rfb = new JRetFuncBas();
    	  			
    	doCallStoredProcedure(request, response, str, rfb);
    	    
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "CONT_POLIZAS_CE", "POLZ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "|OMP|",rfb.getRes());
    	irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_otrmetodopago.jsp", request, response);

    }
    
    public void GestionarCheque(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String idce = request.getParameter("proceso").equals("CAMBIAR_CE") ? "'" + p(request.getParameter("idce").substring(4)) + "'" : "null";
    	String idpart = request.getParameter("proceso").equals("CAMBIAR_CE") ? "null" : "'" + p(request.getParameter("idpart")) + "'";
    	
    	double tc = 1.0000;
		
		if(!request.getParameter("moneda").equals("MXN"))
			tc = Double.parseDouble(request.getParameter("tipcamb"));

    	String str = "select * from sp_cont_polizas_ce_cheque(" + idce + ",'" + p(request.getParameter("ID")) + "'," + idpart + ",'" + p(request.getParameter("num")) + "','" +
    			p(request.getParameter("banco")) + "','" + p(request.getParameter("ctaori")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + 
    			p(request.getParameter("monto")) + "','" + p(request.getParameter("benef")) + "','" + p(JUtil.fco(JUtil.frfc(request.getParameter("rfc")))) + "','" + 
    			p(request.getParameter("banemisext")) + "','" + p(request.getParameter("moneda")) + "','" + tc + "') as ( err integer, res varchar, clave integer ) ";
    	
    	//doDebugSQL(request, response, str);
    	JRetFuncBas rfb = new JRetFuncBas();
    	  			
    	doCallStoredProcedure(request, response, str, rfb);
    	    
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "CONT_POLIZAS_CE", "POLZ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "|CHQ|",rfb.getRes());
    	irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_cheque.jsp", request, response);

    }
    
    public void GestionarTransferencia(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String idce = request.getParameter("proceso").equals("CAMBIAR_CE") ? "'" + p(request.getParameter("idce").substring(4)) + "'" : "null";
    	String idpart = request.getParameter("proceso").equals("CAMBIAR_CE") ? "null" : "'" + p(request.getParameter("idpart")) + "'";

    	double tc = 1.0000;
		
		if(!request.getParameter("moneda").equals("MXN"))
			tc = Double.parseDouble(request.getParameter("tipcamb"));

    	String str = "select * from sp_cont_polizas_ce_transferencia(" + idce + ",'" + p(request.getParameter("ID")) + "'," + idpart + ",'" +
    			p(request.getParameter("ctaori")) + "','" + p(request.getParameter("bancoori")) + "','" + p(request.getParameter("monto")) + "','" + 
    			p(request.getParameter("ctadest")) + "','" + p(request.getParameter("bancodest")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + 
    			p(request.getParameter("benef")) + "','" + p(JUtil.fco(JUtil.frfc(request.getParameter("rfc")))) + "','" +
    			p(request.getParameter("bancooriext")) + "','" + p(request.getParameter("bancodestext")) + "','" + p(request.getParameter("moneda")) + "','" + tc + "' ) as ( err integer, res varchar, clave integer ) ";
    	
    	//doDebugSQL(request, response, str);
    	JRetFuncBas rfb = new JRetFuncBas();
    	  			
    	doCallStoredProcedure(request, response, str, rfb);
    	    
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "CONT_POLIZAS_CE", "POLZ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "|TRN|",rfb.getRes());
    	irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_transferencia.jsp", request, response);

    }
    
    public void GestionarComprobante(HttpServletRequest request, HttpServletResponse response)
       	throws ServletException, IOException
    {
    	String idce = request.getParameter("proceso").equals("CAMBIAR_CE") ? "'" + p(request.getParameter("idce").substring(4)) + "'" : "null";
    	String idpart = request.getParameter("proceso").equals("CAMBIAR_CE") ? "null" : "'" + p(request.getParameter("idpart")) + "'";
    	String ID_Tipo = "", tipo3 = "";
    	double tc = 1.0000;
		int cfdcbbnumfol = 0;
		
		if(!request.getParameter("moneda").equals("MXN"))
			tc = Double.parseDouble(request.getParameter("tipcamb"));
		
		if(!request.getParameter("uuidcfdi").equals("") && !request.getParameter("uuidcfdi").equals("                                    "))
		{
			ID_Tipo = "CompNal";
			tipo3 = "XML";
		}
		else if(!request.getParameter("cfdcbbserie").equals("") && !request.getParameter("cfdcbbnumfol").equals(""))
		{
			ID_Tipo = "CompNalOtr";
			tipo3 = "CBB";
			cfdcbbnumfol = Integer.parseInt(request.getParameter("cfdcbbnumfol"));
		}
		else if(!request.getParameter("numfactext").equals(""))
		{
			ID_Tipo = "CompExt";
			tipo3 = "EXT";
		}
		
    	String str = "select * from sp_cont_polizas_ce_comprobante(" + idce + ",'" + p(request.getParameter("ID")) + "'," + idpart + ",'" +
    			p(request.getParameter("uuidcfdi")) + "','" + p(request.getParameter("monto")) + "','" + p(JUtil.fco(JUtil.frfc(request.getParameter("rfc")))) + "','" + ID_Tipo + "','" + p(request.getParameter("moneda")) + "','" + tc + "','" +
    			p(request.getParameter("cfdcbbserie")) + "','" + cfdcbbnumfol + "','" + p(request.getParameter("numfactext")) + "','" + p(request.getParameter("taxid")) + "') as ( err integer, res varchar, clave integer ) ";
    	
    	//doDebugSQL(request, response, str);
    	JRetFuncBas rfb = new JRetFuncBas();
        	  			
    	doCallStoredProcedure(request, response, str, rfb);
        	    
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "CONT_POLIZAS_CE", "POLZ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "|" + tipo3 + "|",rfb.getRes());
    	irApag("/forsetiweb/contabilidad/conta_polizas_dlg_ce_comprobante.jsp", request, response);

    }
    
    public void EliminarCE(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	String ce = p(request.getParameter("idce").substring(0,3));
    	String idce = p(request.getParameter("idce").substring(4));
    	
    	String str = "select * from sp_cont_polizas_ce_eliminar('" + p(request.getParameter("ID")) + "','" + ce + "','" + idce + "') as ( err integer, res varchar, clave varchar ) ";
            	
    	//doDebugSQL(request, response, str);
    	JRetFuncBas rfb = new JRetFuncBas();
            	  			
    	doCallStoredProcedure(request, response, str, rfb);
            	    
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "CONT_POLIZAS_CE", "POLZ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLIZAS").getEspecial() + "|ELM|",rfb.getRes());
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    }
    
	@SuppressWarnings("rawtypes")
	public void SubirArchivosCFD(HttpServletRequest request, HttpServletResponse response, JFacturasXML factxml, Vector archivos)
			throws ServletException, IOException
	{
		short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
			
		HttpSession ses = request.getSession(true);
		JForsetiCFD cfd = (JForsetiCFD)ses.getAttribute("cont_cfd");
		if(cfd == null)
		{
			cfd = new JForsetiCFD();
			ses.setAttribute("cont_cfd", cfd);
		}
		else
			cfd.resetearCertComp();
	        
		idmensaje = cfd.SubirArchivosCFDI(request, archivos, mensaje, "C");
		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		
		if(idmensaje == JForsetiCFD.OKYDOKY)
		{
			idmensaje = cfd.VerificarFacturasSubidas(request, factxml, mensaje, "C");
			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
			
			if(idmensaje == JForsetiCFD.OKYDOKY)
			{
				idmensaje = cfd.GuardarDocumentoCFDI(request, Integer.parseInt( factxml.getParametros().getProperty("identidad").substring(5) ), factxml.getArchivoXML(), factxml.getArchivoPDF(), mensaje, "C|" + factxml.getParametros().getProperty("identidad").substring(0,4));
				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				
			}
			
		}
		
		JFacturasXML rec = (JFacturasXML)ses.getAttribute("fact_xml");
		if(rec != null)
			rec = null;
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
	}
}
