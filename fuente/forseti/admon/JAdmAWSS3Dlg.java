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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import forseti.JForsetiApl;
import forseti.JGestionArchivos;
import forseti.JUtil;


public class JAdmAWSS3Dlg extends JForsetiApl
{
 	private static final long serialVersionUID = 1L;

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

      String adm_awss3_dlg = "";
      request.setAttribute("adm_awss3_dlg",adm_awss3_dlg);
      String mensaje = ""; short idmensaje = -1;

      if (request.getContentType() != null && 
  		    request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) 
	  {
	  	  if(!getSesion(request).getRegistrado()) 
	  	  { 
	  		 irApag("/forsetiadmin/errorAtributos.jsp",request,response);
	  		 return;
	  	  }
	  	  else
	  	  {
	  		  if(!getSesion(request).getPermiso("ADM_AWSS3_GESTIONAR"))
	  		  {
	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_AWSS3_GESTIONAR");
	  			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3_GESTIONAR","AAS3||||",mensaje);
	  			  irApag("/forsetiaweb/caja_mensajes.jsp", request, response);
	  			  return;
	  		  }
	  		    
	  		  try
	  		  {
	  			  JGestionArchivos gestion = new JGestionArchivos(); 
	  			  DiskFileUpload fu = new DiskFileUpload();
	  			  List items = fu.parseRequest(request);
				  Iterator iter = items.iterator();
				  while (iter.hasNext()) 
	  			  {
	  				  FileItem item = (FileItem)iter.next();
	  				  if (item.isFormField())
	  				  {
	  					  if(item.getFieldName().equals("ID_MODULO"))
	  						  gestion.setID_MODULO(item.getString());
	  					  else if(item.getFieldName().equals("OBJIDS"))
	  						  gestion.setOBJIDS(item.getString());
	  					  else if(item.getFieldName().equals("IDSEP"))
	  						  gestion.setIDSEP(item.getString());
	  				  }
	  				  else
	  				  	  gestion.getArchivos().addElement(item);
	  			  }
	  			  
				  if(!getSesion(request).getPermiso(gestion.getID_MODULO()))
		  		  {
		  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_AWSS3_GESTIONAR");
		  			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
		  			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3_GESTIONAR","AAS3||||",mensaje);
		  			  irApag("/forsetiaweb/caja_mensajes.jsp", request, response);
		  			  return;
		  		  }
	  			 	  			
	  			  SubirArchivo(request, response, gestion);
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
        if(request.getParameter("proceso").equals("SUBIR_ARCHIVO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_AWSS3_GESTIONAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_AWSS3_GESTIONAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3_GESTIONAR","AAS3|||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          if(!getSesion(request).getPermiso(request.getParameter("ID_MODULO")))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", request.getParameter("ID_MODULO"));
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3_GESTIONAR","AAS3|||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }
          
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/administracion/adm_awss3_dlg.jsp", request, response);
          return;
       
        }
        else if(request.getParameter("proceso").equals("DESCARGAR_ARCHIVO"))
        {
        	if(!getSesion(request).getPermiso("ADM_AWSS3"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_AWSS3");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3","AAS3|||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }

        	if(!getSesion(request).getPermiso(request.getParameter("ID_MODULO")))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", request.getParameter("ID_MODULO"));
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3","AAS3|||",mensaje);
                irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
                return;
            }
        	// Solicitud de envio a procesar
        	if(request.getParameter("id") != null)
        	{
        		String[] valoresParam = request.getParameterValues("id");
        		if(valoresParam.length == 1)
        		{
        			// Verificacion
        			Descargar(request, response, request.getParameter("id"));
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
        else if(request.getParameter("proceso").equals("ELIMINAR_ARCHIVO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_AWSS3_GESTIONAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_AWSS3_GESTIONAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3_GESTIONAR","AAS3|||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          if(!getSesion(request).getPermiso(request.getParameter("ID_MODULO")))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", request.getParameter("ID_MODULO"));
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3_GESTIONAR","AAS3|||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }
          
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	//System.out.println("POST:" + request.getParameter("id") + ":request.getParameter(id)");
            	Eliminar(request, response, request.getParameter("id"));
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
    
    public void Descargar(HttpServletRequest request, HttpServletResponse response, String nombre)
        	throws ServletException, IOException
    {
    	JGestionArchivos gestion = new JGestionArchivos(); 
    	gestion.setID_MODULO(request.getParameter("ID_MODULO"));
    	gestion.setOBJIDS(request.getParameter("OBJIDS"));
    	gestion.setIDSEP(request.getParameter("IDSEP"));
    	gestion.setNombre(nombre);
    	 	
    	if(gestion.getStatusS3() == JGestionArchivos.OKYDOKY)
    	{
    		gestion.DescargarArchivo(request, response, getServletConfig().getServletContext(), "application/octet-stream");
    		if(gestion.getStatusS3() == JGestionArchivos.OKYDOKY)
    		    return;
    		
    	}
    	
    	if(gestion.getStatusS3() == JGestionArchivos.OKYDOKY)
    		getSesion(request).setID_Mensaje(gestion.getStatusS3(), "El archivo se descargó con éxito");
    	else
    		getSesion(request).setID_Mensaje(gestion.getStatusS3(), gestion.getError());
    			
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	return;
    	
    }
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response, String nombre)
    	throws ServletException, IOException
    {
    	//System.out.println("Eliminar:" + nombre + ":String nombre");
    	
    	JGestionArchivos gestion = new JGestionArchivos(); 
    	gestion.setID_MODULO(request.getParameter("ID_MODULO"));
		gestion.setOBJIDS(request.getParameter("OBJIDS"));
		gestion.setIDSEP(request.getParameter("IDSEP"));
		gestion.setNombre(nombre);
	 	
		if(gestion.getStatusS3() == JGestionArchivos.OKYDOKY)
			gestion.EliminarArchivo(request);
			
		if(gestion.getStatusS3() == JGestionArchivos.OKYDOKY)
			getSesion(request).setID_Mensaje(gestion.getStatusS3(), "El archivo se eliminó con éxito");
		else
			getSesion(request).setID_Mensaje(gestion.getStatusS3(), gestion.getError());
			
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
    }

        
	public void SubirArchivo(HttpServletRequest request, HttpServletResponse response, JGestionArchivos gestion)
			throws ServletException, IOException
	{
		if(gestion.getStatusS3() == JGestionArchivos.OKYDOKY)
			gestion.SubirArchivo(request);
		
		if(gestion.getStatusS3() == JGestionArchivos.OKYDOKY)
			getSesion(request).setID_Mensaje(gestion.getStatusS3(), "El archivo se cargó con éxito");
		else
			getSesion(request).setID_Mensaje(gestion.getStatusS3(), gestion.getError());
		
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		return;
	}
    
}
