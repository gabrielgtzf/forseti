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
package forseti.admon;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.FactoryConfigurationError;

//import org.apache.axis.i18n.RB;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import forseti.JAccesoBD;
//import forseti.JBajarArchivo;
import forseti.JBajarArchivo;
import forseti.JForsetiApl;
import forseti.JForsetiCFD;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.JForsetiCFD.XmlDefaultHandler;
import forseti.sets.JAdmPeriodosSet;
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JCFDCompOtrSet;
import forseti.sets.JCFDCompViewSet;
import forseti.sets.JContBalanzaCESet;
import forseti.sets.JContCEModuloSet;
import forseti.sets.JContCatalogoCESet;
import forseti.sets.JContPolizasCECabSet;
import forseti.sets.JContPolizasCEDetSet;
import forseti.sets.JContPolizasDetalleCEChequesSet;
import forseti.sets.JContPolizasDetalleCEComprobantesSet;
import forseti.sets.JContPolizasDetalleCETransferenciasSet;

@SuppressWarnings("serial")
public class JAdmCFDDlg extends JForsetiApl
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

      String adm_cfdi_dlg = "";
      request.setAttribute("adm_cfdi_dlg",adm_cfdi_dlg);

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
	  		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
      		{
	  			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
	  			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
	  			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	  			return;
      		}
	  		
	  		try
	  		{
	  			Vector archivos = new Vector();
	  			DiskFileUpload fu = new DiskFileUpload();
	  			List items = fu.parseRequest(request);
	  			Iterator iter = items.iterator();
	  			while (iter.hasNext()) 
	  			{
	  				FileItem item = (FileItem)iter.next();
	  				if(!item.isFormField())  
	  					archivos.addElement(item);
	  			}
	  					 	  			
	  			SubirArchivosCFD(request, response, archivos);
	  			return;
	  		  } 
	  		  catch (FileUploadException e) 
	  		  {
	  			  e.printStackTrace();
	  			  return;
	  		  }
	  	  }
	  	  
	  }
      
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
      	if(request.getParameter("proceso").equals("VERIFICAR_CFD"))
        {
    		// Verificacion
      		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
        	
      		if(VerificarCertificados(request, response))
        		AgregarCertificado(request, response);
        	
        	return;
        }
      	else if(request.getParameter("proceso").equals("CAMBIAR_EMISOR"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
      		{
      			// Verificacion
      			if(VerificarParametros(request, response))
      			{
                    Cambiar(request, response);
                    return;
      			}
      			
      			irApag("/forsetiweb/administracion/adm_cfd_dlg.jsp", request, response);
	    		return;
      		}
      		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
      		{
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	   	irApag("/forsetiweb/administracion/adm_cfd_dlg.jsp", request, response);
        	   	return;
          	}

        }
      	else if(request.getParameter("proceso").equals("AGREGAR_CERTIFICADO"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
      		{
      			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
      			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
      			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      			  return;
      		}
      		
      		Integer subir_archivos = new Integer(2);
      		request.setAttribute("subir_archivos", subir_archivos);
      		  
      		getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		irApag("/forsetiweb/subir_archivos.jsp?verif=/servlet/CEFAdmCFDDlg&archivo_1=cer&archivo_2=key&proceso=AGREGAR_CERTIFICADO&subproceso=ENVIAR", request, response);
      		return;
      	  
        }
      	else if(request.getParameter("proceso").equals("AGREGAR_EXPEDITOR"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
      		{
      			// Verificacion
      			if(VerificarParametrosExpRec(request, response))
      			{
                    AgregarCambiarExpRec(request, response, JForsetiApl.AGREGAR, "EXP");
                    return;
      			}
      		   	irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
              	return;
      		}
      		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
      		{
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	   	irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
        	   	return;
      		}

        }
      	else if(request.getParameter("proceso").equals("CAMBIAR_EXPEDITOR"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
		      		if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
		      		{
		      			// Verificacion
		      			if(VerificarParametrosExpRec(request, response))
		      			{
		      				AgregarCambiarExpRec(request, response, JForsetiApl.CAMBIAR, "EXP");
		      				return;
		      			}
		      			irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
		        	   	return;
		      		}
		      		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
		      		{
		        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        	   	irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
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
      	else if(request.getParameter("proceso").equals("AGREGAR_RECEPTOR"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
      		{
      			// Verificacion
      			if(VerificarParametrosExpRec(request, response))
      			{
      				AgregarCambiarExpRec(request, response, JForsetiApl.AGREGAR, "REC");
      				return;
      			}
      			irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
        	   	return;
      		}
      		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
      		{
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	   	irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
        	   	return;
      		}

        }
      	else if(request.getParameter("proceso").equals("CAMBIAR_RECEPTOR"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_AGREGAR"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_AGREGAR");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_AGREGAR","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
		      		if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
		      		{
		      			// Verificacion
		      			if(VerificarParametrosExpRec(request, response))
		      			{
		      				AgregarCambiarExpRec(request, response, JForsetiApl.CAMBIAR, "REC");
		      				return;
		      			}
		      			irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
		        	   	return;
		      		}
		      		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
		      		{
		        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        	   	irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
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
      	else if(request.getParameter("proceso").equals("GENERAR_CE"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_GCEXML"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_GCEXML");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_GCEXML","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
      				if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR") )
      				{
	      				int ano = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 1));
	      				int mes = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 2));
	   		    	
	      		    	JAdmPeriodosSet set = new JAdmPeriodosSet(request);
	      				set.m_Where = "Ano = '" + ano + "' and Mes = '" + mes + "'"; 
	      				set.Open();
	      			                    
			      		if(!set.getAbsRow(0).getCerrado())
			      		{
			      			idmensaje = 1; mensaje = "PRECAUCION: No se pueden generar los archivos XML de la Contabilidada Electrónica, porque el mes seleccionado no esta cerrado aun.";
			      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		  					return;
			      		}
			      		
			      		GenerarXMLCE(request, response, ano, mes);
			      		return;
      				}
      				else
      				{
      					getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        	   	irApag("/forsetiweb/administracion/adm_cfd_dlg_cegen.jsp", request, response);
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
      	else if(request.getParameter("proceso").equals("CONSULTAR_ST"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_GCEXML"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_GCEXML");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_GCEXML","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
      				int ano = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 1));
      				int mes = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 2));
   		    	
      				JContCEModuloSet set = new JContCEModuloSet(request, getSesion(request).getSesion("ADM_CFDI").getEspecial());
      				set.m_Where = "Ano = '" + ano + "' and Mes = '" + mes + "'"; 
      				set.Open();
      			                    
		      		if(set.getAbsRow(0).getGenerado().equals("N"))
		      		{
		      			idmensaje = 1; mensaje = "PRECAUCION: No se puede consultar el registro de estatus porque no existe el archivo generado en este mes.";
		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	  					return;
		      		}
		      		
		      		irApag("/forsetiweb/administracion/adm_cfd_dlg_cons.jsp", request, response);
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
      	else if(request.getParameter("proceso").equals("CONSULTAR_ARCHIVOS"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_GCEXML"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_GCEXML");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_GCEXML","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
      				int ano = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 1));
      				int mes = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 2));
      				
      				JContCEModuloSet set = new JContCEModuloSet(request, getSesion(request).getSesion("ADM_CFDI").getEspecial());
      				set.m_Where = "Ano = '" + ano + "' and Mes = '" + mes + "'"; 
      				set.Open();
      			                    
		      		if(set.getAbsRow(0).getGenerado().equals("N"))
		      		{
		      			idmensaje = 1; mensaje = "PRECAUCION: No se puede consultar el archivo porque este mes no esta generado aun.";
		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	  					return;
		      		}
		      		
      				String nomArchFech;
      		 		if(mes < 10)
      		 			nomArchFech = "-" + ano + "-0" + mes;
      		 		else
      		 			nomArchFech = "-" + ano + "-" + mes;
      		 		String nomArch = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/CE/" + JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial() + nomArchFech + ".xml";
      		 		
      		 		String salida = "";
      		 		File f = new File(nomArch);
      		 		FileReader fr = null;
      		 		BufferedReader br = null;
      		 		try
      		 		{
      		 			fr = new FileReader(f);
      		 			br = new BufferedReader(fr);
      		 			String s;
      		 			while((s = br.readLine())  != null)
      		 			{
      		 				salida += s + "\n";
      		 			}
      		 			br.close();
      		 		}
      		 		catch(IOException e1)
      		 		{
      		 			salida += e1;
      		 		}
      		 		response.setContentType("application/xml");
      		 		PrintWriter out = response.getWriter();
      		 		out.print(salida);
      		 		/*
      				String nombres[] = {	nomArch + "catalogo" + nomArchFech + ".xml",
      			    						nomArch + "balanza" + nomArchFech + ".xml",
      			    						nomArch + "polizas" + nomArchFech + ".xml",
      			    						nomArch + "CE" + nomArchFech + ".log"};
      				String destinos[] = {	"catalogo" + nomArchFech + ".xml",
			    							"balanza" + nomArchFech + ".xml",
			    							"polizas" + nomArchFech + ".xml",
			    							"CE" + nomArchFech + ".log" };
     			  
      				JBajarArchivo fd = new JBajarArchivo();
      				fd.doDownloadMultipleFilesInZip(response, getServletConfig().getServletContext(), ("CE" + nomArchFech + ".zip"), nombres, destinos);
      				*/
      		 		
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
      	else if(request.getParameter("proceso").equals("VALIDAR_ARCHIVOS"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_GCEXML"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_GCEXML");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_GCEXML","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
      				int ano = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 1));
      				int mes = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 2));
      				
      				JContCEModuloSet set = new JContCEModuloSet(request, getSesion(request).getSesion("ADM_CFDI").getEspecial());
      				set.m_Where = "Ano = '" + ano + "' and Mes = '" + mes + "'"; 
      				set.Open();
      			                    
		      		if(set.getAbsRow(0).getGenerado().equals("N"))
		      		{
		      			idmensaje = 1; mensaje = "PRECAUCION: No se puede validar el archivo porque este mes no esta generado aun.";
		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	  					return;
		      		}
		      		
      				String nomArchFech;
      		 		if(mes < 10)
      		 			nomArchFech = "-" + ano + "-0" + mes;
      		 		else
      		 			nomArchFech = "-" + ano + "-" + mes;
      		 		String nomArch = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/CE/" + JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial() + nomArchFech + ".xml";
      		 		   		 		
      		 		//Validación
      		 		String ERROR = "", archivoXML = "";
      				boolean NAME_SPACE_AWARE = true;
      				boolean VALIDATING = true;
      				String SCHEMA_LANGUAGE ="http://java.sun.com/xml/jaxp/properties/schemaLanguage";
      				String SCHEMA_LANGUAGE_VAL = "http://www.w3.org/2001/XMLSchema";
      				String SCHEMA_SOURCE ="http://java.sun.com/xml/jaxp/properties/schemaSource";
      				String sFichXsd = "/usr/local/forseti/rec/";
      				if(JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial().equals("CECAT"))
      					sFichXsd += "CatalogoCuentas_1_1.xsd";
      				else if(JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial().equals("CEBAL"))
      					sFichXsd += "BalanzaComprobacion_1_1.xsd";
      				else
      					sFichXsd += "PolizasPeriodo_1_1.xsd";
      				try 
      				{
      					FileReader file         = new FileReader(nomArch);
      		            BufferedReader buff     = new BufferedReader(file);
      		            boolean eof             = false;
      		            
      		            while(!eof)
      		            {
      		                String line = buff.readLine();
      		                if(line == null)
      		                	eof = true;
      		                else
      		                	archivoXML += line + "\n";
      		            }
      		            InputStream is = new ByteArrayInputStream(archivoXML.getBytes());
      		            Reader xsdReader = new FileReader(sFichXsd);
      					//System.out.println("xmlReader:" + pathxml + "\n" + "xsdReader:" + sFichXsd + "\n"  + "xsdReader2:" + sFichXsd2);
      					SAXParserFactory factory = SAXParserFactory.newInstance();
      					factory.setNamespaceAware(NAME_SPACE_AWARE);
      					factory.setValidating(VALIDATING);
      				  
      					SAXParser parser = factory.newSAXParser();
      					parser.setProperty(SCHEMA_LANGUAGE, SCHEMA_LANGUAGE_VAL);
      					parser.setProperty(SCHEMA_SOURCE, new InputSource(xsdReader));
      					
      					DefaultHandler handler = new XmlDefaultHandler();
      					//parser.parse(new InputSource(xmlReader), handler); 
      					parser.parse(is, handler);
      				}
      				catch(FactoryConfigurationError e) 
      				{
      					e.printStackTrace();
      					ERROR += "FactoryConfiguration: "  + e.getMessage() + "<br>";
      				}
      				catch (ParserConfigurationException e) 
      				{
      					e.printStackTrace();
      					ERROR += "ParserConfiguration: "  + e.getMessage() + "<br>"; 
      				}
      				catch (SAXException e) 
      				{
      					e.printStackTrace();
      					ERROR += "SAX: "  + e.getMessage() + "<br>"; 
      				}
      				catch (IOException e) 
      				{
      					e.printStackTrace();
      					ERROR += "IO: "  + e.getMessage() + "<br>";
      				}
      		 		
      				if(!ERROR.equals(""))
      				{
      					idmensaje = 3; mensaje = "ERROR: Al verificar sintaxis del XML:" + "<br>" + ERROR;
    	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	            }
      				else
      				{
      					idmensaje = 0; mensaje = "El XML se verificó y paso las pruebas de sintaxis";
    	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
      				}
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
      	else if(request.getParameter("proceso").equals("DESCARGAR_ARCHIVOS"))
        {
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_GCEXML"))
	        {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_GCEXML");
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_GCEXML","ACFD||||",mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
	        }
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
      				int ano = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 1));
      				int mes = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 2));
      				
      				JContCEModuloSet set = new JContCEModuloSet(request, getSesion(request).getSesion("ADM_CFDI").getEspecial());
      				set.m_Where = "Ano = '" + ano + "' and Mes = '" + mes + "'"; 
      				set.Open();
      			                    
		      		if(set.getAbsRow(0).getGenerado().equals("N"))
		      		{
		      			idmensaje = 1; mensaje = "PRECAUCION: No se puede descargar el archivo porque este mes no esta generado aun.";
		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	  					return;
		      		}
		      		
		      		String nomArchFech;
      		 		if(mes < 10)
      		 			nomArchFech = "-" + ano + "-0" + mes;
      		 		else
      		 			nomArchFech = "-" + ano + "-" + mes;
      		 		
		      		SAXBuilder builder = new SAXBuilder();
		    		File xmlFile = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/CE/" + JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial() + nomArchFech + ".xml");
		    		
		    		try
		    		{
		    			Document document = (Document)builder.build(xmlFile);
		    			Element CEGeneral = document.getRootElement();
		    			String nomArchZIP = CEGeneral.getAttributeValue("RFC") + CEGeneral.getAttributeValue("Anio") + CEGeneral.getAttributeValue("Mes");
		    			String destino = CEGeneral.getAttributeValue("RFC") + CEGeneral.getAttributeValue("Anio") + CEGeneral.getAttributeValue("Mes");
		    			if(JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial().equals("CECAT")) // es de egresos (Compras. Gastos)
		    			{
		    				nomArchZIP += "CT.zip";
		    				destino += "CT.xml";
		    			}
		    			else if(JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial().equals("CEBAL"))
		    			{
		    				nomArchZIP += "B" + CEGeneral.getAttributeValue("TipoEnvio") + ".zip";
		    				destino += "B" + CEGeneral.getAttributeValue("TipoEnvio") + ".xml";
		    			}
		    			else
		    			{
		    				nomArchZIP += "PL.zip";
		    				destino += "PL.xml";
		    			}
		    			String nombres[] = { "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/CE/" + JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial() + nomArchFech + ".xml" };
		    			String destinos[] = {  destino };
		    			JBajarArchivo fd = new JBajarArchivo();
	      		 		fd.doDownloadMultipleFilesInZip(response, getServletConfig().getServletContext(), nomArchZIP, nombres, destinos);
	      				return;
		    		}
		    		catch(JDOMException e)
		    		{
		    			idmensaje = 1; mensaje = "PRECAUCION: No se puede descargar el archivo porque este mes no esta generado aun.";
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
      	else if(request.getParameter("proceso").equals("DESENLAZAR_DOCUMENTO"))
      	{
      		// Revisa si tiene permisos
      		if(!getSesion(request).getPermiso("ADM_CFDI_DESENLAZAR"))
      		{
      			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_DESENLAZAR");
      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
      			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_DESENLAZAR","ACFD||||",mensaje);
      			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      			return;
      		}
          		
      		if(request.getParameter("id") != null)
      		{
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
      				String status = JUtil.getSesion(request).getSesion("ADM_CFDI").getStatus();
      				if(!status.equals("OTROS"))
      				{
      					JCFDCompViewSet set = new JCFDCompViewSet(request,status);
      					set.m_Where = "ID_CFD = '" + p(request.getParameter("id")) + "'";
      					set.Open();
          					
      					if(set.getNumRows() < 1)
      					{
      						idmensaje = 3; mensaje = "ERROR: No se puede desenlazar el registro porque no existe";
      						getSesion(request).setID_Mensaje(idmensaje, mensaje);
      						irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      						return;
      					}
          					
      					if(set.getAbsRow(0).getFSI_Tipo().equals("ENT"))
      					{
      						idmensaje = 1; mensaje = "PRECAUCION: No se puede desenlazar el documento porque aun no se encuentra enlazado a ningún registro.";
      						getSesion(request).setID_Mensaje(idmensaje, mensaje);
      						irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      						return;
      					}
      				}
      				else
      				{
      					JCFDCompOtrSet set = new JCFDCompOtrSet(request);
      					set.m_Where = "ID_CFD = '" + p(request.getParameter("id")) + "'";
      					set.Open();
      					
      					if(set.getAbsRow(0).getFSI_Tipo().equals("ENT"))
      					{
      						idmensaje = 1; mensaje = "PRECAUCION: No se puede desenlazar el documento porque aun no se encuentra enlazado a ningún registro.";
      						getSesion(request).setID_Mensaje(idmensaje, mensaje);
      						irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      						return;
      					}
      				}
      				
      				DesenlazarDocumento(request, response);
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
      	else if(request.getParameter("proceso").equals("ELIMINAR_DOCUMENTO"))
        {
      		// Revisa si tiene permisos
  			if(!getSesion(request).getPermiso("ADM_CFDI_CARGAR"))
  			{
  				idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI_CARGAR");
  				getSesion(request).setID_Mensaje(idmensaje, mensaje);
  				RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI_CARGAR","ACFD||||",mensaje);
  				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  				return;
  			}
      		
      		if(request.getParameter("id") != null)
            {
      			String[] valoresParam = request.getParameterValues("id");
      			if(valoresParam.length == 1)
      			{
      				String status = JUtil.getSesion(request).getSesion("ADM_CFDI").getStatus();
      				if(!status.equals("OTROS"))
      				{
      					JCFDCompViewSet set = new JCFDCompViewSet(request,status);
      					set.m_Where = "ID_CFD = '" + p(request.getParameter("id")) + "'";
      					set.Open();
      					
      					if(set.getNumRows() < 1)
    		      		{
    		      			idmensaje = 3; mensaje = "ERROR: No se puede eliminar el registro porque no existe";
    		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	  					return;
    		      		}
      					
      					if(!set.getAbsRow(0).getFSI_Tipo().equals("ENT"))
    		      		{
    		      			idmensaje = 1; mensaje = "PRECAUCION: No se puede eliminar el documento porque ya se encuentra enlazado a un registro.";
    		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	  					return;
    		      		}
      				}
      				else
      				{
      					JCFDCompOtrSet set = new JCFDCompOtrSet(request);
      					set.m_Where = "ID_CFD = '" + p(request.getParameter("id")) + "'";
      					set.Open();
      					
      					if(!set.getAbsRow(0).getFSI_Tipo().equals("ENT"))
    		      		{
    		      			idmensaje = 1; mensaje = "PRECAUCION: No se puede eliminar el documento porque ya se encuentra enlazado a un registro.";
    		      			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	  					return;
    		      		}
      				}
      				
      				EliminarDocumento(request, response);
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
      	else // si no se mandan parametros, manda a error
      	{
      		idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);
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

	@SuppressWarnings("rawtypes")
	public void SubirArchivosCFD(HttpServletRequest request, HttpServletResponse response, Vector archivos)
		throws ServletException, IOException
	{
		short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
		
		HttpSession ses = request.getSession(true);
        JForsetiCFD rec = (JForsetiCFD)ses.getAttribute("ven_cfd");
        if(rec == null)
        {
           	rec = new JForsetiCFD();
            ses.setAttribute("ven_cfd", rec);
        }
        else
            rec.resetearCertComp();
        
		idmensaje = rec.SubirArchivosCert(request, archivos, mensaje);
		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		
		if(idmensaje != JForsetiCFD.OKYDOKY)
			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		else
			irApag("/forsetiweb/administracion/adm_cfd_dlg_vercfd.jsp", request, response);
		
		
	}

	public boolean VerificarCertificados(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		
		short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
		if(request.getParameter("password") == null || request.getParameter("password").equals(""))
		{
			idmensaje = 3; mensaje.append(JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR"));//"ERROR: No se recibi&oacute; la clave de la llave <br>");
		    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		    irApag("/forsetiweb/administracion/adm_cfd_dlg_vercfd.jsp", request, response);
		    return false;
		}
		else
		{
			HttpSession ses = request.getSession(true);
			JForsetiCFD rec = (JForsetiCFD)ses.getAttribute("ven_cfd");
		
			idmensaje = rec.VerificarCertificadosSubidos(request, request.getParameter("password"), mensaje);
		
			if(rec.getStatusCertComp() != JForsetiCFD.OKYDOKY)
			{
				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
				return false;
			}
			
		}
		return true;
	}

	public boolean VerificarParametrosExpRec(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		short idmensaje = -1; String mensaje = "";
		// Verificacion
		if(request.getParameter("cfd_id_exprec") != null && request.getParameter("cfd_nombre") != null && request.getParameter("cfd_calle") != null && request.getParameter("cfd_noext") != null &&
			    request.getParameter("cfd_noint") != null && request.getParameter("cfd_colonia") != null && request.getParameter("cfd_localidad") != null &&
			    request.getParameter("cfd_cp") != null && request.getParameter("cfd_municipio") != null && request.getParameter("cfd_estado") != null && 
			    request.getParameter("cfd_pais") != null && 
			    !request.getParameter("cfd_id_exprec").equals("") && !request.getParameter("cfd_pais").equals("") )
		{
			return true;
		}
		else
		{
		    idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
		    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		    return false;
		}
	
	}	
	
	public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		short idmensaje = -1; String mensaje = "";
		// Verificacion
		if(request.getParameter("cfd_regimenfiscal") != null && request.getParameter("rfc") != null &&
		   	request.getParameter("cfd") != null && request.getParameter("cfd_calle") != null && request.getParameter("cfd_noext") != null &&
		    request.getParameter("cfd_noint") != null && request.getParameter("cfd_colonia") != null && request.getParameter("cfd_localidad") != null &&
		    request.getParameter("cfd_cp") != null && request.getParameter("cfd_municipio") != null && request.getParameter("cfd_estado") != null && request.getParameter("cfd_pais") != null &&
		    !request.getParameter("cfd").equals(""))
		{
		    int cfd = Integer.parseInt(request.getParameter("cfd"));
		    
		    // permite comprobantes fiscales digitales
		    if(cfd != 0)
		    {
		    	if(request.getParameter("cfd_regimenfiscal").equals("") || request.getParameter("rfc").equals("") ||
		    		request.getParameter("cfd_calle").equals("") || request.getParameter("cfd_noext").equals("") ||
		    		request.getParameter("cfd_colonia").equals("") || request.getParameter("cfd_localidad").equals("") ||
		    		request.getParameter("cfd_cp").equals("") || request.getParameter("cfd_municipio").equals("") || 
		    		request.getParameter("cfd_estado").equals("") || request.getParameter("cfd_pais").equals("") )
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR",2);//"PRECAUCION: Es necesaria la informaci&oacute; completa del emisor para Comprobantes Fiscales Digitales <br>";
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
		    return true;
		}
		else
		{
		    idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
		    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		    return false;
		}
	
	}

 	public void AgregarCertificado(HttpServletRequest request, HttpServletResponse response)
 		throws ServletException, IOException
 	{
 		HttpSession ses = request.getSession(true);
		JForsetiCFD rec = (JForsetiCFD)ses.getAttribute("ven_cfd");
		
 		String  str = "select * from sp_cfd_certificados_subir('" + q(rec.getNoCertificado()) + "','" + q(rec.getArchivoCertificadoPDF()) + "','" + p(JUtil.obtFechaHoraSQL(rec.getNotAfter())) + "','" + q(rec.getArchivoLLaveXML()) + "') as ( err integer, res varchar, clave varchar ) ";
        JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_AGREGAR", "ACFD|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	
  	}	
	
 	public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
 		short idmensaje = -1; String mensaje = "", claveret = "";
 		
  		String rfc = p(JUtil.fco(JUtil.frfc(request.getParameter("rfc"))));
    	
  		String  str = "select * from sp_cfd_cambiar('" + getSesion(request).getConBD() + "','" + p(request.getParameter("cfd_regimenfiscal")) + "','" + rfc + "','" + 
        // certificados fiscales digitales
        p(request.getParameter("cfd")) + "','" + p(request.getParameter("cfd_calle")) + "','" + p(request.getParameter("cfd_noext")) + "','" + p(request.getParameter("cfd_noint")) + "','" + p(request.getParameter("cfd_colonia")) + "','" + p(request.getParameter("cfd_localidad")) + "','" + p(request.getParameter("cfd_municipio")) + "','" +
        p(request.getParameter("cfd_estado")) + "','" + p(request.getParameter("cfd_pais")) + "','" + p(request.getParameter("cfd_cp")) + "') as ( err integer, res varchar, clave varchar ) ";
        
    	// Primero cambia el RFC en la variable del sistema local
    	JRetFuncBas rfb = new JRetFuncBas();
	  	String sel = "SELECT * FROM sp_variables_cambiar('RFC',null,null,null,'" + rfc + "') as ( err integer, res varchar, clave varchar ) ";
		doCallStoredProcedure(request, response, sel, rfb);
		if(rfb.getIdmensaje() == 0)
		{
			sel = "SELECT * FROM sp_variables_cambiar('EMPRESA',null,null,null,'" + p(JUtil.getSesion(request).getNombreCompania()) + "') as ( err integer, res varchar, clave varchar ) ";
			doCallStoredProcedure(request, response, sel, rfb);
		}
		//Ahora cambia el registro completo de FORSETI_ADMIN
		if(rfb.getIdmensaje() == 0)
		{
			// No llama al procedimiento normal, porque se conecta a la base de datos administrativa *doCallStoredProcedure(request, response, str, rfb);
			try
			{
				Connection con = JAccesoBD.getConexion();
				Statement s    = con.createStatement();
      			ResultSet rs   = s.executeQuery(str);
      			if(rs.next())
      			{
      				idmensaje = rs.getShort("ERR");
      				mensaje = rs.getString("RES");
      				claveret = rs.getString("CLAVE");
      			}
      			s.close();
      			JAccesoBD.liberarConexion(con);
			}
			catch(SQLException e)
			{
				idmensaje = 4;
				mensaje = q(e.getMessage());
			}
		}
		else
		{
			idmensaje = (short)rfb.getIdmensaje();
			mensaje = rfb.getRes();
		}
		
        RDP("CEF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_AGREGAR", "ACFD|" + claveret + "|||",mensaje);
 	   	getSesion(request).setID_Mensaje(idmensaje, mensaje);
  		irApag("/forsetiweb/administracion/adm_cfd_dlg.jsp", request, response);
    }
 	 	
 	public void AgregarCambiarExpRec(HttpServletRequest request, HttpServletResponse response, short prc, String tipo)
	    throws ServletException, IOException
	{
 		String  str;
 		if(prc == JForsetiApl.AGREGAR)
 		{
 			if(tipo.equals("EXP"))
 				str = "select * from sp_cfd_expediciones_agregar(";
 			else
 				str = "select * from sp_cfd_receptores_agregar(";
 		}
		else
		{
			if(tipo.equals("EXP"))
	 			str = "select * from sp_cfd_expediciones_cambiar(";	
			else
				str = "select * from sp_cfd_receptores_cambiar(";
		}
 		
 		str += "'" + p(request.getParameter("cfd_id_exprec")) + "','" + p(request.getParameter("cfd_nombre")) + "','" + p(request.getParameter("cfd_calle")) + "','" + p(request.getParameter("cfd_noext")) + "','" + p(request.getParameter("cfd_noint")) + "','" + p(request.getParameter("cfd_colonia")) + "','" + p(request.getParameter("cfd_localidad")) + "','" + p(request.getParameter("cfd_municipio")) + "','" +
 		p(request.getParameter("cfd_estado")) + "','" + p(request.getParameter("cfd_pais")) + "','" + p(request.getParameter("cfd_cp")) + "') as ( err integer, res varchar, clave smallint ) ";
 		JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_AGREGAR", "ACFD|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/administracion/adm_cfd_dlg_exprec.jsp", request, response);
	  	
	}

 	public void GenerarXMLCE(HttpServletRequest request, HttpServletResponse response, int ano, int mes)
 		throws ServletException, IOException
 	{
 		int errores = 0, alertas = 0;
 		String nomArchFech;
 		if(mes < 10)
 			nomArchFech = "-" + ano + "-0" + mes;
 		else
 			nomArchFech = "-" + ano + "-" + mes;
 		String nomArch = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/CE/";
 		
 		JAdmVariablesSet var = new JAdmVariablesSet(request);
 		var.m_Where = "ID_Variable = 'RFC'";
 		var.Open();
 		
 		String rfcfmt = JUtil.fco(JUtil.frfc(var.getAbsRow(0).getVAlfanumerico()));
		
 		if(getSesion(request).getSesion("ADM_CFDI").getEspecial().equals("CECAT"))
		{
 			FileWriter filewri		= new FileWriter(nomArch + "CECAT" + nomArchFech + ".log");
 			PrintWriter pw			= new PrintWriter(filewri);
			try
	    	{
				JContCatalogoCESet cat = new JContCatalogoCESet(request);
				cat.m_OrderBy = "Cuenta ASC";
				cat.Open();
	 		
				if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
				{
					pw.println("ERROR CATALOGO: RFC Mal Formado: " + rfcfmt + "\n");
					pw.flush();
					errores++;
				}
				Namespace ns = Namespace.getNamespace("www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas");
				Element Catalogo = new Element("Catalogo",ns);
				Namespace xsi = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
				Catalogo.setAttribute("schemaLocation", "www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas/CatalogoCuentas_1_1.xsd", xsi);
								
				Catalogo.setAttribute("Version","1.1");
				Catalogo.setAttribute("RFC",rfcfmt);
				Catalogo.setAttribute("Mes",(mes < 10 ? "0" + Integer.toString(mes) : Integer.toString(mes)));
				Catalogo.setAttribute("Anio",Integer.toString(ano));
				Document DocCatalogo = new Document();
				DocCatalogo.setRootElement(Catalogo);
				
				for(int i = 0; i < cat.getNumRows(); i++)
				{
					Element Ctas = new Element("Ctas",ns);
					if(cat.getAbsRow(i).getCE_CodAgrup().equals(""))
					{
						pw.println("ERROR CATALOGO: Codigo Agrupador de Cuenta " + cat.getAbsRow(i).getCuenta() + " Inexistente\n");
						pw.flush();
						errores++;
					}
					Ctas.setAttribute("CodAgrup",cat.getAbsRow(i).getCE_CodAgrup());
					Ctas.setAttribute("NumCta",cat.getAbsRow(i).getCuenta());
					Ctas.setAttribute("Desc",cat.getAbsRow(i).getNombre());
					if(cat.getAbsRow(i).getNivel() > 1)
						Ctas.setAttribute("SubCtaDe",cat.getAbsRow(i).getSubCuentaDe());
					Ctas.setAttribute("Nivel",Integer.toString(cat.getAbsRow(i).getNivel()));
					Ctas.setAttribute("Natur",cat.getAbsRow(i).getCE_Natur());
					Catalogo.addContent(Ctas);
				}
				
				Format format = Format.getPrettyFormat();
				format.setEncoding("utf-8");
				format.setTextMode(TextMode.NORMALIZE);
				XMLOutputter xmlOutputter = new XMLOutputter(format);
				FileWriter writer = new FileWriter(nomArch + "CECAT" + nomArchFech + ".xml");
				xmlOutputter.output(DocCatalogo, writer);
				writer.close();
				
				pw.println("-------------------------------------------------------------------------------");
				pw.println("   ERRORES: " + errores + " ALERTAS: " + alertas);
				pw.flush();
				
				String  str = "select * from sp_cont_ce_generar('" + ano + "','" + mes + "','" + errores + "','" + alertas + "','CAT') as ( err integer, res varchar, clave varchar ) ";
		 		JRetFuncBas rfb = new JRetFuncBas();
		 			
		 		doCallStoredProcedure(request, response, str, rfb);
		 		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_GCEXML", "ACFD|" + rfb.getClaveret() + "|||",rfb.getRes());
		 		irApag("/forsetiweb/administracion/adm_cfd_dlg_cegen.jsp", request, response);
	    	}
			finally
			{
				pw.close();
				filewri.close();
			}
		}
 		else if(getSesion(request).getSesion("ADM_CFDI").getEspecial().equals("CEBAL"))
		{
 			FileWriter filewri		= new FileWriter(nomArch + "CEBAL" + nomArchFech + ".log");
 			PrintWriter pw			= new PrintWriter(filewri);
			try
	    	{		
				JContBalanzaCESet bal = new JContBalanzaCESet(request);
				bal.m_Where = "Mes = '" + mes + "' and Ano = '" + ano + "'" ;
		 		bal.m_OrderBy = "Cuenta ASC";
				bal.Open();
				if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
				{
					pw.println("ERROR BALANZA: RFC Mal Formado: " + rfcfmt + "\n");
					pw.flush();
					errores++;
				}
				Namespace ns = Namespace.getNamespace("www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion");
				Element Balanza = new Element("Balanza", ns);
				Namespace xsi = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
				Balanza.setAttribute("schemaLocation", "www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion/BalanzaComprobacion_1_1.xsd", xsi);
								
				Balanza.setAttribute("Version","1.1");
				Balanza.setAttribute("RFC",rfcfmt);
				Balanza.setAttribute("Mes",(mes < 10 ? "0" + Integer.toString(mes) : Integer.toString(mes)));
				Balanza.setAttribute("Anio",Integer.toString(ano));
				Balanza.setAttribute("TipoEnvio",(request.getParameter("tipoenvio") != null ? p(request.getParameter("tipoenvio")) : "N"));
				
				Document DocBalanza = new Document();
				DocBalanza.setRootElement(Balanza);
				
				for(int i = 0; i < bal.getNumRows(); i++)
				{
					Element Ctas = new Element("Ctas",ns);
					Ctas.setAttribute("NumCta",bal.getAbsRow(i).getCuenta());
					Ctas.setAttribute("SaldoIni",JUtil.Converts(bal.getAbsRow(i).getInicial(),"",".",2,false));
					Ctas.setAttribute("Debe",JUtil.Converts(bal.getAbsRow(i).getCargos(),"",".",2,false));
					Ctas.setAttribute("Haber",JUtil.Converts(bal.getAbsRow(i).getAbonos(),"",".",2,false));
					Ctas.setAttribute("SaldoFin",JUtil.Converts(bal.getAbsRow(i).getFinal(),"",".",2,false));
					Balanza.addContent(Ctas);
				}
			
				Format format = Format.getPrettyFormat();
				format.setEncoding("utf-8");
				format.setTextMode(TextMode.NORMALIZE);
				XMLOutputter xmlOutputter = new XMLOutputter(format);
				FileWriter writer = new FileWriter(nomArch + "CEBAL" + nomArchFech + ".xml");
				xmlOutputter.output(DocBalanza, writer);
				writer.close();
				
				pw.println("-------------------------------------------------------------------------------");
				pw.println("   ERRORES: " + errores + " ALERTAS: " + alertas);
				pw.flush();
				
				String  str = "select * from sp_cont_ce_generar('" + ano + "','" + mes + "','" + errores + "','" + alertas + "','BAL') as ( err integer, res varchar, clave varchar ) ";
		 		JRetFuncBas rfb = new JRetFuncBas();
		 			
		 		doCallStoredProcedure(request, response, str, rfb);
		 		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_GCEXML", "ACFD|" + rfb.getClaveret() + "|||",rfb.getRes());
		 		irApag("/forsetiweb/administracion/adm_cfd_dlg_cegen.jsp", request, response);
	    	}
			finally
			{
				pw.close();
				filewri.close();
			}
		}
 		else if(getSesion(request).getSesion("ADM_CFDI").getEspecial().equals("CEPOL"))
 		{
 			FileWriter filewri		= new FileWriter(nomArch + "CEPOL" + nomArchFech + ".log");
 			PrintWriter pw			= new PrintWriter(filewri);
 			try
 			{
 				if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
				{
					pw.println("ERROR POLIZAS: RFC Mal Formado: " + rfcfmt + "\n");
					pw.flush();
					errores++;
				}
 				////////////////////////////////////////////
 				Namespace ns = Namespace.getNamespace("www.sat.gob.mx/esquemas/ContabilidadE/1_1/PolizasPeriodo");
				Element Polizas = new Element("Polizas", ns);
				Namespace xsi = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
				Polizas.setAttribute("schemaLocation", "www.sat.gob.mx/esquemas/ContabilidadE/1_1/PolizasPeriodo http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/PolizasPeriodo/PolizasPeriodo_1_1.xsd", xsi);
				
				Polizas.setAttribute("Version","1.1");
				Polizas.setAttribute("RFC",rfcfmt);
				Polizas.setAttribute("Mes",(mes < 10 ? "0" + Integer.toString(mes) : Integer.toString(mes)));
				Polizas.setAttribute("Anio",Integer.toString(ano));
				Polizas.setAttribute("TipoSolicitud",(request.getParameter("tiposolicitud") != null ? p(request.getParameter("tiposolicitud")) : "AF"));
				if(request.getParameter("numorden") != null && !request.getParameter("numorden").equals(""))
					Polizas.setAttribute("NumOrden",p(request.getParameter("numorden")));
				if(request.getParameter("numtramite") != null && !request.getParameter("numtramite").equals(""))
					Polizas.setAttribute("NumTramite",p(request.getParameter("numtramite")));
								
				Document DocPolizas = new Document();
				DocPolizas.setRootElement(Polizas);
				
				JContPolizasCECabSet pol = new JContPolizasCECabSet(request);
				pol.m_Where = "date_part('Month',Fecha) = '" + mes + "' and date_part('Year',Fecha) = '" + ano + "' and Status <> 'C'" ;
		 		pol.m_OrderBy = "Fecha ASC, ID ASC";
				pol.Open();
				for(int i = 0; i < pol.getNumRows(); i++)
				{
					Element Poliza = new Element("Poliza");
					Poliza.setAttribute("Tipo",Byte.toString(pol.getAbsRow(i).getTipo()));
					Poliza.setAttribute("Num",pol.getAbsRow(i).getNum());
					Poliza.setAttribute("Fecha",JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(), "yyyy-MM-dd"));
					Poliza.setAttribute("Concepto",pol.getAbsRow(i).getConcepto());
					JContPolizasCEDetSet det = new JContPolizasCEDetSet(request);
					det.m_Where = "ID = '" + pol.getAbsRow(i).getID() + "'";
					det.m_OrderBy = "Part ASC";
			 		det.Open();
					for(int t = 0; t < det.getNumRows(); t++)
					{
						Element Transaccion = new Element("Transaccion");
						Transaccion.setAttribute("NumCta",det.getAbsRow(t).getCuenta());
						Transaccion.setAttribute("Concepto",det.getAbsRow(t).getConcepto());
						Transaccion.setAttribute("Debe",JUtil.Converts(det.getAbsRow(t).getDebe(),"",".",2,false));
						Transaccion.setAttribute("Haber",JUtil.Converts(det.getAbsRow(t).getHaber(),"",".",2,false));
						Transaccion.setAttribute("Moneda",det.getAbsRow(t).getMoneda());
						Transaccion.setAttribute("TipCamb",JUtil.Converts(det.getAbsRow(t).getTC(),"",".",2,false));
						JContPolizasDetalleCEChequesSet schq = new JContPolizasDetalleCEChequesSet(request);
						schq.m_Where = "ID_Pol = '" + det.getAbsRow(t).getID() + "' and ID_Part = '" + det.getAbsRow(t).getPart() + "'";
						schq.Open();
						for(int chq = 0; chq < schq.getNumRows(); chq++)
						{
							Element Cheque = new Element("Cheque");
							if(!schq.getAbsRow(chq).getNum().matches("\\d+"))
							{
								pw.println("ALERTA CE CHEQUE: Numero de CHEQUE contiene caracteres Alfanumericos: " + schq.getAbsRow(chq).getNum());
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								alertas++;
							}
							Cheque.setAttribute("Num",schq.getAbsRow(chq).getNum());
							Cheque.setAttribute("Banco",schq.getAbsRow(chq).getBanco());
							if(!schq.getAbsRow(chq).getCtaOri().matches("\\d+"))
							{
								pw.println("ALERTA CE CHEQUE: Numero de CUENTA contiene caracteres Alfanumericos: " + schq.getAbsRow(chq).getCtaOri());
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								alertas++;
							}
							Cheque.setAttribute("CtaOri",schq.getAbsRow(chq).getCtaOri());
							Cheque.setAttribute("Fecha",JUtil.obtFechaTxt(schq.getAbsRow(chq).getFecha(),"yyyy-MM-dd"));
							Cheque.setAttribute("Monto",JUtil.Converts(schq.getAbsRow(chq).getMonto(),"",".",2,false));
							Cheque.setAttribute("Benef",schq.getAbsRow(chq).getBenef());
							String rfc = schq.getAbsRow(chq).getRFC();
							if(rfc.equals("") || rfc.length() > 13 || rfc.length() < 12)
							{
								pw.println("ERROR CE CHEQUE: RFC Mal Formado: " + rfc);
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								errores++;
							}
							Cheque.setAttribute("RFC",rfc);
							Transaccion.addContent(Cheque);
						}
						JContPolizasDetalleCETransferenciasSet strn = new JContPolizasDetalleCETransferenciasSet(request);
						strn.m_Where = "ID_Pol = '" + det.getAbsRow(t).getID() + "' and ID_Part = '" + det.getAbsRow(t).getPart() + "'";
						strn.Open();
						for(int trn = 0; trn < strn.getNumRows(); trn++)
						{
							Element Transferencia = new Element("Transferencia");
							if(!strn.getAbsRow(trn).getCtaOri().matches("\\d+"))
							{
								pw.println("ALERTA CE TRANSFERENCIA: Numero de CUENTA ORIGEN contiene caracteres Alfanumericos: " + strn.getAbsRow(trn).getCtaOri());
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								alertas++;
							}
							Transferencia.setAttribute("CtaOri",strn.getAbsRow(trn).getCtaOri());
							Transferencia.setAttribute("BancoOri",strn.getAbsRow(trn).getBancoOri());
							Transferencia.setAttribute("Monto",JUtil.Converts(strn.getAbsRow(trn).getMonto(),"",".",2,false));
							if(!strn.getAbsRow(trn).getCtaDest().matches("\\d+"))
							{
								pw.println("ALERTA CE TRANSFERENCIA: Numero de CUENTA DESTINO contiene caracteres Alfanumericos: " + strn.getAbsRow(trn).getCtaOri());
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								alertas++;
							}
							Transferencia.setAttribute("CtaDest",strn.getAbsRow(trn).getCtaDest());
							Transferencia.setAttribute("BancoDest",strn.getAbsRow(trn).getBancoDest());
							Transferencia.setAttribute("Fecha",JUtil.obtFechaTxt(strn.getAbsRow(trn).getFecha(),"yyyy-MM-dd"));
							Transferencia.setAttribute("Benef",strn.getAbsRow(trn).getBenef());
							String rfc = strn.getAbsRow(trn).getRFC();
							if(rfc.equals("") || rfc.length() > 13 || rfc.length() < 12)
							{
								pw.println("ERROR CE TRANSFERENCIA: RFC Mal Formado: " + rfc);
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								errores++;
							}
							Transferencia.setAttribute("RFC",rfc);
							Transaccion.addContent(Transferencia);
						}
						JContPolizasDetalleCEComprobantesSet sxml = new JContPolizasDetalleCEComprobantesSet(request);
						sxml.m_Where = "ID_Pol = '" + det.getAbsRow(t).getID() + "' and ID_Part = '" + det.getAbsRow(t).getPart() + "'";
						sxml.Open();
						for(int xml = 0; xml < sxml.getNumRows(); xml++)
						{
							Element Comprobante = new Element("Comprobante");
							if(sxml.getAbsRow(xml).getUUID_CFDI().length() != 36)
							{
								pw.println("ERROR CE COMPROBANTE: UUID no valido: " + sxml.getAbsRow(xml).getUUID_CFDI());
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								alertas++;
							}
							else if(!sxml.getAbsRow(xml).getUUID_CFDI().matches("[a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}"))
							{
								pw.println("ALERTA CE COMPROBANTE: UUID no parece ser valido: " + sxml.getAbsRow(xml).getUUID_CFDI());
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								alertas++;
							}
							Comprobante.setAttribute("UUID_CFDI",sxml.getAbsRow(xml).getUUID_CFDI());
							Comprobante.setAttribute("Monto",JUtil.Converts(sxml.getAbsRow(xml).getMonto(),"",".",2,false));
							String rfc = sxml.getAbsRow(xml).getRFC();
							if(rfc.equals("") || rfc.length() > 13 || rfc.length() < 12)
							{
								pw.println("ERROR CE COMPROBANTE: RFC Mal Formado: " + rfc);
								pw.println("-- Póliza " + pol.getAbsRow(i).getNum() + " " + JUtil.obtFechaTxt(pol.getAbsRow(i).getFecha(),"dd/MMM/yyyy") + " " + pol.getAbsRow(i).getConcepto() + " Partida: " + det.getAbsRow(t).getPart() + "\n");
								pw.flush();
								errores++;
							}
							Comprobante.setAttribute("RFC",sxml.getAbsRow(xml).getRFC());
							Transaccion.addContent(Comprobante);
						}
						Poliza.addContent(Transaccion);
					}
					Polizas.addContent(Poliza);
				}
		
				Format format = Format.getPrettyFormat();
				format.setEncoding("utf-8");
				format.setTextMode(TextMode.NORMALIZE);
				XMLOutputter xmlOutputter = new XMLOutputter(format);
				FileWriter writer = new FileWriter(nomArch + "CEPOL" + nomArchFech + ".xml");
				xmlOutputter.output(DocPolizas, writer);
				writer.close();
			
				pw.println("-------------------------------------------------------------------------------");
				pw.println("   ERRORES: " + errores + " ALERTAS: " + alertas);
				pw.flush();
			
				String  str = "select * from sp_cont_ce_generar('" + ano + "','" + mes + "','" + errores + "','" + alertas + "','POL') as ( err integer, res varchar, clave varchar ) ";
				JRetFuncBas rfb = new JRetFuncBas();
	 			
				doCallStoredProcedure(request, response, str, rfb);
				RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_GCEXML", "ACFD|" + rfb.getClaveret() + "|||",rfb.getRes());
				irApag("/forsetiweb/administracion/adm_cfd_dlg_cegen.jsp", request, response);
 			}
 			finally
 			{
 				pw.close();
 				filewri.close();
 			}
 		}	
 	}
 	
 	public void EliminarDocumento(HttpServletRequest request, HttpServletResponse response)
 			throws ServletException, IOException
 	{
 		// Elimina primero el archivo fisico en el servidor forseti
 		String status = JUtil.getSesion(request).getSesion("ADM_CFDI").getStatus();
 		if(!status.equals("OTROS"))
 		{
 			JCFDCompViewSet set = new JCFDCompViewSet(request,status);
 			set.m_Where = "ID_CFD = '" + p(request.getParameter("id")) + "'";
 			set.Open();
 			if(status.equals("COMPRAS"))
 			{
 				File xml = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/TFDs/" + set.getAbsRow(0).getUUID() + ".xml");
 				xml.delete();
 				File pdf = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/PDFs/" + set.getAbsRow(0).getUUID() + ".pdf");
 				pdf.delete();
 			}
 			else if(status.equals("VENTAS"))
 			{
 				File xml = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/TFDs/" + set.getAbsRow(0).getUUID() + ".xml");
 				xml.delete();
 				File pdf = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/ven/PDFs/" + set.getAbsRow(0).getUUID() + ".pdf");
 				pdf.delete();
 			}
 			else //if(status.equals("NOMINA"))
 			{
 				File xml = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/nom/TFDs/" + set.getAbsRow(0).getUUID() + ".xml");
 				xml.delete();
 				File pdf = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/nom/PDFs/" + set.getAbsRow(0).getUUID() + ".pdf");
 				pdf.delete();
 			}
      	}
 		else
 		{
 			JCFDCompOtrSet set = new JCFDCompOtrSet(request);
 			set.m_Where = "ID_CFD = '" + p(request.getParameter("id")) + "'";
 			set.Open();
 			
 			File file = new File("/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/comp/OTRs/" + set.getAbsRow(0).getUUID() + "." + set.getAbsRow(0).getExt());
			file.delete();
		}
		
 		
		String  str = "select * from sp_cfd_eliminar_documento('" + p(getSesion(request).getSesion("ADM_CFDI").getStatus()) + "','" + p(request.getParameter("id")) + "') as ( err integer, res varchar, clave varchar ) ";
		JRetFuncBas rfb = new JRetFuncBas();
			
		doCallStoredProcedure(request, response, str, rfb);
		
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_CARGAR", "ACFD|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
 	}
 	
 	public void DesenlazarDocumento(HttpServletRequest request, HttpServletResponse response)
 			throws ServletException, IOException
 	{
		String  str = "select * from sp_cfd_desenlazar_documento('" + p(getSesion(request).getSesion("ADM_CFDI").getStatus()) + "','" + p(request.getParameter("id")) + "') as ( err integer, res varchar, clave varchar ) ";
		JRetFuncBas rfb = new JRetFuncBas();
			
		doCallStoredProcedure(request, response, str, rfb);
		
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_CFDI_DESENLAZAR", "ACFD|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
 	}
}
