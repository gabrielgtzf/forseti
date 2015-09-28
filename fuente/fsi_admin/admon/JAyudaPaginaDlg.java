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
package fsi_admin.admon;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JRetFuncBas;
import forseti.JSubirArchivo;
import forseti.JUtil;
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JAyudaPaginaModuloSet;
import forseti.sets.JAyudaPaginaSet;
import forseti.sets.JAyudaPaginasEnlacesSet;
import forseti.sets.JAyudaPaginasSubTiposTiposSet;
import forseti.sets.JAyudaSubTipoSet;
import forseti.sets.JAyudaTipoSet;
import fsi_admin.JFsiForsetiApl;

@SuppressWarnings("serial")
public class JAyudaPaginaDlg extends JFsiForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);
     
      String ayuda_pagina_dlg = "";
      request.setAttribute("ayuda_pagina_dlg",ayuda_pagina_dlg);

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
  	  		  if(!getSesion(request).getPermiso("ADMIN_AYUDA_CREAR"))
  	  		  {
  	  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CREAR");
  	  			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	  			  RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CREAR","AYUP||||",mensaje);
  	  			  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
  	  			  return;
  	  		  }
  	  		    
  	  		  String proceso = "SUBIR_AYUDA";
  	  		  request.setAttribute("proceso",proceso);

  	  		  SubirImagenAyuda(request, response);
  	  		  return;
  	  	  }
  	  	  
  	  }
      
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_CREAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CREAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CREAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response))
            {
              Agregar(request, response);
              return;
            }
            irApag("/forsetiadmin/administracion/ayuda_pagina_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiadmin/administracion/ayuda_pagina_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("SUBIR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_CREAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CREAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CREAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiadmin/administracion/ayuda_pagina_dlg_subir.jsp", request, response);
          return;
        }
        else if(request.getParameter("proceso").equals("EDITAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
                // Verificacion
                if(VerificarParametrosEdicion(request, response))
                {
                	Editar(request, response);
                	return;
                }
                irApag("/forsetiadmin/administracion/ayuda_pagina_dlg_editar.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiadmin/administracion/ayuda_pagina_dlg_editar.jsp", request, response);
                return;
              }

            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite editar una página a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador de la página que se quiere editar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
                // Verificacion
                if(VerificarParametros(request, response))
                {
                  Cambiar(request, response);
                  return;
                }
                irApag("/forsetiadmin/administracion/ayuda_pagina_dlg.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiadmin/administracion/ayuda_pagina_dlg.jsp", request, response);
                return;
              }

            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite cambiar una página a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador de la página que se quiere cambiar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_ELIMINAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_ELIMINAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_ELIMINAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              Eliminar(request, response);
              return;
            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite eliminar una página a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador de la página que se quiere eliminar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("ENLAZAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
                // Verificacion
                Enlazar(request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiadmin/administracion/ayuda_pagina_dlg_enlazar.jsp", request, response);
                return;
              }

            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite enlazar una página a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador de la página que se quiere enlazar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("APLICAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              Aplicar(request, response);
              return;
            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite aplicar una página a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"ERROR: Se debe enviar el identificador de la página que se quiere aplicar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("GENERAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_GENERAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_GENERAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_GENERAR","AYUP||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          GenerarAyuda(request, response);
          return;
          
        }
        else
        {
          idmensaje = 1;
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"PRECAUCION: El parámetro de proceso no es válido<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"ERROR: No se han mandado parámetros reales<br>";
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
         return;
      }

    }
  
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("idpagina") != null && request.getParameter("descripcion") != null && request.getParameter("busqueda") != null && request.getParameter("idalternativo") != null &&   
          !request.getParameter("idpagina").equals("") && !request.getParameter("descripcion").equals("")  && !request.getParameter("busqueda").equals("") )
      {
    	  if(request.getParameter("proceso").equals("CAMBIAR"))
    	  {
    		  if(request.getParameter("id") == null || request.getParameter("id").equals(""))
    		  {
    			  idmensaje = 3; mensaje = "ERROR: En cambios, se debe enviar el ID_Pagina anterior <br>";
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	          return false; 
    		  }
    	  }
    	  
    	  int sub = 0;
    	  
          @SuppressWarnings("rawtypes")
		  Enumeration nombresParam = request.getParameterNames();
          while(nombresParam.hasMoreElements())
          {
              String nombreParam = (String)nombresParam.nextElement();
              if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("SUB_"))
              	continue;
              else
            	sub++;
          }
          
          if(sub == 0)
		  {
			  idmensaje = 1; mensaje = "PRECAUCION: Esta página debe establecer por lo menos una asociación a un elemento de menú <br>";
	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          return false; 
		  }
          return true;
      }
      else
      {
          idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"); //"ERROR: Alguno de los parametros necesarios es Nulo <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }

    }

    public boolean VerificarParametrosEdicion(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("id") != null && request.getParameter("cuerpo") != null &&  
	        !request.getParameter("id").equals("") && !request.getParameter("cuerpo").equals(""))
	    {
	  	    return true;
	    }
	    else
	    {
	        idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"); //"ERROR: Alguno de los parametros necesarios es Nulo <br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }
	
	}
    
    public void Enlazar(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
    	
    	String tbl = 	"CREATE LOCAL TEMPORARY TABLE _TMP_AYUDA_PAGINAS_ENLACES (\n";
    	tbl += 			"	ID_Enlace varchar(8) NOT NULL \n";
    	tbl += 			"); \n";
	  	
    	@SuppressWarnings("rawtypes")
		Enumeration nombresParam = request.getParameterNames();
    	while(nombresParam.hasMoreElements())
    	{
    		String nombreParam = (String)nombresParam.nextElement();
    		if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("ENL_"))
    			continue;
	          
    		String valorParam = nombreParam.substring(4);
    		tbl += "insert into _TMP_AYUDA_PAGINAS_ENLACES\n";
    		tbl += "values('" + p(valorParam) + "');\n";
	          
    	}
    	 
    	JRetFuncBas rfb = new JRetFuncBas();
    	
        String str = "SELECT * FROM  sp_ayuda_pagina_enlaces( '" + p(request.getParameter("id")) + "' ) as ( err integer, res varchar, clave varchar ) ";
        
    	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_AYUDA_PAGINAS_ENLACES ", rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/administracion/ayuda_pagina_dlg_enlazar.jsp", request, response);

	}

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String tbl = 	"CREATE LOCAL TEMPORARY TABLE _TMP_AYUDA_PAGINAS_SUBTIPOS (\n";
   		tbl += 			"	ID_SubTipo varchar(8) NOT NULL \n";
		tbl += 			"); ";
    	
        @SuppressWarnings("rawtypes")
		Enumeration nombresParam = request.getParameterNames();
        while(nombresParam.hasMoreElements())
        {
            String nombreParam = (String)nombresParam.nextElement();
            if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("SUB_"))
            	continue;
            
            String valorParam = nombreParam.substring(4);
            tbl += "insert into _TMP_AYUDA_PAGINAS_SUBTIPOS\n";
            tbl += "values('" + p(valorParam) + "'); \n";
            
        }
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM  sp_ayuda_pagina_cambiar( '" + p(request.getParameter("id")) + "','" + p(request.getParameter("idpagina")) + "','" + p(request.getParameter("descripcion"))  + "','" + p(request.getParameter("busqueda"))  + "','" + p(request.getParameter("tipo"))  + "','" + p(request.getParameter("idalternativo"))  + "' ) as ( err integer, res varchar, clave varchar ) ";
         
    	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_AYUDA_PAGINAS_SUBTIPOS ", rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/administracion/ayuda_pagina_dlg.jsp", request, response);

         
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String tbl = 	"CREATE LOCAL TEMPORARY TABLE _TMP_AYUDA_PAGINAS_SUBTIPOS (\n";
   		tbl += 			"	ID_SubTipo varchar(8) NOT NULL \n";
		tbl += 			"); ";
    	
        @SuppressWarnings("rawtypes")
		Enumeration nombresParam = request.getParameterNames();
        while(nombresParam.hasMoreElements())
        {
            String nombreParam = (String)nombresParam.nextElement();
            if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("SUB_"))
            	continue;
            
            String valorParam = nombreParam.substring(4);
            tbl += "insert into _TMP_AYUDA_PAGINAS_SUBTIPOS\n";
            tbl += "values('" + p(valorParam) + "'); \n";
            
        }
   	
        JRetFuncBas rfb = new JRetFuncBas();
    	
        String str = "SELECT * FROM  sp_ayuda_pagina_agregar('" + p(request.getParameter("idpagina")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("busqueda"))  + "','" + p(request.getParameter("tipo"))  + "','" + p(request.getParameter("idalternativo"))  + "' ) as ( err integer, res varchar, clave varchar ) ";
         
    	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_AYUDA_PAGINAS_SUBTIPOS ", rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CREAR","AYUP|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/administracion/ayuda_pagina_dlg.jsp", request, response);
             
    }

    public void Editar(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		JRetFuncBas rfb = new JRetFuncBas();
    	
		String str = "SELECT * FROM  sp_ayuda_pagina_editar( '" + p(request.getParameter("id")) + "','" + q(request.getParameter("cuerpo")) + "' ) as ( err integer, res varchar, clave varchar ) ";
         
    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/administracion/ayuda_pagina_dlg.jsp", request, response);
        	
	}   

    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
		String str = "SELECT * FROM  sp_ayuda_pagina_eliminar( '" + p(request.getParameter("id")) + "' ) as ( err integer, res varchar, clave varchar ) ";
	       
    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_ELIMINAR","AYUP|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/caja_mensajes.jsp", request, response);

	}   
    
    public void Aplicar(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	JRetFuncBas rfb = new JRetFuncBas();
    	
        String str = "SELECT * FROM  sp_ayuda_pagina_aplicar( '" + p(request.getParameter("id")) + "' ) as ( err integer, res varchar, clave varchar ) ";
           
    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUP|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/caja_mensajes.jsp", request, response);

        
	} 
    
	public void SubirImagenAyuda(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		short idmensaje = -1; String mensaje = "";
		
		JAdmVariablesSet var = new JAdmVariablesSet(null);
		var.ConCat(true);
		var.m_Where = "ID_Variable = 'TOMCAT'";
		var.Open();
		
		if(var.getAbsRow(0).getVAlfanumerico().equals("NC"))
		{
			idmensaje = 1;
			mensaje = "PRECAUCION: La variable TOMCAT (ruta de instalacion de tomcat) no está definida... No se puede generar la ayuda";
			getSesion(request).setID_Mensaje(idmensaje, mensaje);
			irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
		    return;
		}
		
		// Sube los archivos de la imagen
    	String[] exts = { "jpg","gif","png" };
    	boolean[] frz = { true, true, true };
    	JSubirArchivo sa = new JSubirArchivo(512, var.getAbsRow(0).getVAlfanumerico() + "/webapps/ROOT/forsetidoc/IMG/", exts, frz);
		
		if(sa.processFiles(request,true) < 1) // significa que no encontró ningun archivo
		{
			idmensaje = 3; 
			mensaje = sa.getError();
		}
		else
		{
			idmensaje = 0; 
			mensaje = JUtil.Msj("GLB","GLB","GLB","ARCHIVO",2); //"El archivo se subió correctamente";
		}

		RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CREAR","AYUP|" + sa.getFile(0) + "|||",mensaje);

		getSesion(request).setID_Mensaje(idmensaje, mensaje);
		irApag("/forsetiadmin/administracion/ayuda_pagina_dlg_subir.jsp", request, response);
        return;
	}
	
	public void GenerarAyuda(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		short idmensaje = -1; String mensaje = "";
		
		idmensaje = 0;
		mensaje = "Las páginas de ayuda y la configuracion de inicios de ayuda se generaron con éxito";
		
		JAdmVariablesSet var = new JAdmVariablesSet(null);
		var.ConCat(true);
		var.m_Where = "ID_Variable = 'TOMCAT'";
		var.Open();
		
		if(var.getAbsRow(0).getVAlfanumerico().equals("NC"))
		{
			idmensaje = 1;
			mensaje = "PRECAUCION: La variable TOMCAT (ruta de instalacion de tomcat) no está definida... No se puede generar la ayuda";
			getSesion(request).setID_Mensaje(idmensaje, mensaje);
			irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
		    return;
		}
								    
		//carga los archivos de ayuda que contienen la plantilla completa y sencilla html
		String p = "", ps = ""; 
        FileReader file         = new FileReader("/usr/local/forseti/bin/forseti_doc/forseti_doc.html");
        BufferedReader buff     = new BufferedReader(file);
        boolean eof             = false;
        while(!eof)
        {
            String line = buff.readLine();
            if(line == null)
            	eof = true;
            else
            	p += line + "\n";
        }
        buff.close();
        file.close();
        buff = null;
        file = null;
        
        file         = new FileReader("/usr/local/forseti/bin/forseti_doc/forseti_doc_simple.html");
        buff     = new BufferedReader(file);
        eof             = false;
        while(!eof)
        {
            String line = buff.readLine();
            if(line == null)
            	eof = true;
            else
            	ps += line + "\n";
        }
        buff.close();
        file.close();
        buff = null;
        file = null;
        
        // genera las paginas html con el contenido de la ayuda
        int ini_index = p.indexOf("<!--_menu_ini-->");
        String fin_plant = p.substring(0,ini_index);
        // genera los menus
        int fin_index = p.indexOf("<!--_menu_fin-->");
        String t_menus = "", f_menus = "";
        String p_menus = p.substring(ini_index+16,fin_index);
        // p_menu:  <td height="30" align="center" valign="middle" bgcolor="fsi-bgcolor-menu"><a class="txtMenu" href="fsi-href-menu" target="_self">fsi-descripcion-menu</a></td>
		JAyudaTipoSet set = new JAyudaTipoSet(null);
		set.m_OrderBy = "ID_Tipo ASC"; 
		set.ConCat(true);
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			int mod = i % 2;
			
			t_menus = p_menus;
			String bgcolor;
			if(set.getAbsRow(i).getDescripcion().equals("CEF"))
				bgcolor = "#0099FF"; // Azul Cef
			else if(set.getAbsRow(i).getDescripcion().equals("SAF"))
				bgcolor = "#FF6600"; // Naranja Saf ( Color principal forseti )
			else if(set.getAbsRow(i).getDescripcion().equals("REF"))
				bgcolor = "#FFCC00"; 
			else if(set.getAbsRow(i).getDescripcion().equals("SOF"))
				bgcolor = "#339933"; // Verde CEF ( Da concordancia con openSUSE )
			else
				bgcolor = mod == 0 ? "#999999" : "#888888";
			//CC0099 Morado para DIF Desarrollo Integral Forseti V5
			t_menus = JUtil.replace(t_menus, "fsi-bgcolor-menu", bgcolor);
			
			JAyudaPaginasSubTiposTiposSet st = new JAyudaPaginasSubTiposTiposSet(null);
			st.m_OrderBy = "ID_SubTipo DESC, ID_Pagina ASC";
			st.m_Where = "ID_Tipo = '" + p(set.getAbsRow(i).getID_Tipo()) + "' and Status = '2'";
			st.ConCat(true);
			st.Open();
			if(st.getNumRows() > 0)
			{
				JAyudaPaginaSet pagset = new JAyudaPaginaSet(null);
				pagset.ConCat(true);
				pagset.m_Where = "ID_Pagina = '" + p(st.getAbsRow(0).getID_Pagina()) + "'";
				pagset.Open();
				t_menus = JUtil.replace(t_menus, "fsi-href-menu", pagset.getAbsRow(0).getID_Pagina() + ".html");
			}
			else
				t_menus = JUtil.replace(t_menus, "fsi-href-menu", "#");
			
			t_menus = JUtil.replace(t_menus, "fsi-descripcion-menu", set.getAbsRow(i).getDescripcion());
		
			f_menus += t_menus + "\n";
		}
		
		ini_index = p.indexOf("<!--_cuerpo_completo_ini-->");
		fin_plant += f_menus + p.substring(fin_index+16, ini_index);
		fin_plant += "_cuerpo_completo";
		fin_index = p.indexOf("<!--_cuerpo_completo_fin-->");
		fin_plant += p.substring(fin_index+27);
		//ahora genera la plantilla de cuerpo
		ini_index = p.indexOf("<!--_cuerpo_completo_ini-->") + 27;
		fin_index = p.indexOf("<!--_cuerpo_completo_fin-->");
		
		String cuerpo_plant = p.substring(ini_index, fin_index);
		p = null; //libera memoria de p que ya no se utilizará
		
		//Empieza con el archivo index
		String pag = fin_plant;
		
		JAyudaSubTipoSet setsub = new JAyudaSubTipoSet(null);
		setsub.ConCat(true);
		setsub.m_Where = "ID_Tipo = '01'"; // Menus de generalidades
		setsub.m_OrderBy = "ID_SubTipo ASC";
		setsub.Open();
		
		JAyudaPaginaModuloSet pags = new JAyudaPaginaModuloSet(request,"");
		pags.m_Where = "Status = '3'";
		pags.ConCat(true);
		pags.Open();
		//System.out.println("Pags Status: 3 = " + pags.getNumRows());
		if(pags.getNumRows() > 0)
		{
			JAyudaPaginaSet pagset = new JAyudaPaginaSet(null);
			pagset.ConCat(true);
			pagset.m_Where = "ID_Pagina = '" + p(pags.getAbsRow(0).getID_Pagina()) + "'";
			pagset.Open();
			
			String menu01 = "<table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\">\n";
			
			//System.out.println("SetSubTipo: 01 = " + setsub.getNumRows());
			if(setsub.getNumRows() > 0 && setsub.getNumRows() < 4)
			{
				menu01 += "<tr>\n";
				for(int i = 1; i < 3; i++ )
				{
					menu01 += "<td valign=\"top\" width=\"30%\"><strong>" + setsub.getAbsRow(i).getDescripcion() + "</strong><br>\n";
					JAyudaPaginaModuloSet pagsub = new JAyudaPaginaModuloSet(request,"");
					pagsub.ConCat(true);
					pagsub.m_Where = "ID_SubTipo = '" + p(setsub.getAbsRow(i).getID_SubTipo()) + "' and Status <> '3'";
					pagsub.m_OrderBy = "ID_Pagina ASC";
					
					pagsub.Open();
					for(int j = 0; j < pagsub.getNumRows(); j++)
					{
						menu01 += "<br><a href=\"" + pagsub.getAbsRow(j).getID_Pagina() + ".html\" class=\"txtPag\" target=\"_self\"><font color=\"#FF6600\">" + pagsub.getAbsRow(j).getDescripcion() + "</font></a><br><font color=\"#999999\">" + pagsub.getAbsRow(j).getBusqueda() + "</font>\n";
					}
					menu01 += "</td>\n";
					
				}
				menu01 += "</tr>\n";
				menu01 += "</table>\n";
				
			}
			
			String pagcon01 = JUtil.replace(pagset.getAbsRow(0).getCuerpo(), "_GENERALIDADES_TABLE", menu01);
			pag = JUtil.replace(fin_plant, "_cuerpo_completo", pagcon01);
			System.out.println("PAGINA:" + menu01);
			
		}	
		//Ahora guarda index.html
		FileWriter fw = new FileWriter(var.getAbsRow(0).getVAlfanumerico() + "/webapps/ROOT/forsetidoc/index.html");
        PrintWriter pw = new PrintWriter(fw);
        pw.println(pag);
        fw.close();
        
        //Ahora trabaja con cada plantilla y sus paginas
        ini_index = cuerpo_plant.indexOf("<!--_submenu_ini-->");
        fin_index = cuerpo_plant.indexOf("<!--_submenu_fin-->");
        String fin_cuerpo_plant = cuerpo_plant.substring(0,ini_index);
        String p_submenu = cuerpo_plant.substring(ini_index+19,fin_index);
        ini_index = cuerpo_plant.indexOf("<!--_cuerpo_ini-->");
        fin_cuerpo_plant += "_submenu" + cuerpo_plant.substring(fin_index + 19,ini_index);
        fin_index = cuerpo_plant.indexOf("<!--_cuerpo_fin-->");
        String p_cuerpo = cuerpo_plant.substring(ini_index+18,fin_index); 
        ini_index = cuerpo_plant.indexOf("<!--_enlaces_ini-->");
        String p_enlacestit = cuerpo_plant.substring(fin_index+18,ini_index);
        fin_index = cuerpo_plant.indexOf("<!--_enlaces_fin-->");
        String p_enlaces = cuerpo_plant.substring(ini_index+19,fin_index);
        ini_index = cuerpo_plant.indexOf("<!--_paginas_ini-->");
        fin_cuerpo_plant += "_cuerpo_enlacestit_enlaces" + cuerpo_plant.substring(fin_index + 19,ini_index);
        fin_index = cuerpo_plant.indexOf("<!--_paginas_fin-->");
        String p_paginas = cuerpo_plant.substring(ini_index+19,fin_index);
        fin_cuerpo_plant += "_paginas" + cuerpo_plant.substring(fin_index + 19);
       	/*System.out.println(fin_cuerpo_plant);
       	System.out.println(p_submenu);
       	System.out.println(p_cuerpo);
       	System.out.println(p_enlacestit);
       	System.out.println(p_enlaces);
       	System.out.println(p_paginas);*/
       	
		//Ahora genera las páginas completas
        cuerpo_plant = null; //libera memoria del cuerpo       	
       	for(int i = 0; i < set.getNumRows(); i++)
		{
			String mid = set.getAbsRow(i).getID_Tipo();
			String color;
			if(set.getAbsRow(i).getDescripcion().equals("CEF"))
				color = "#0099FF"; // Azul Cef
			else if(set.getAbsRow(i).getDescripcion().equals("SAF"))
				color = "#FF6600"; // Naranja Saf ( Color principal forseti )
			else if(set.getAbsRow(i).getDescripcion().equals("REF"))
				color = "#FFCC00"; // ó FF0000 que es amarillo
			else if(set.getAbsRow(i).getDescripcion().equals("SOF"))
				color = "#339933"; // Verde CEF ( Da concordancia con openSUSE )
			else
				color = "#444444";
		
			setsub.m_Where = "ID_Tipo = '" + p(mid) + "'";
			setsub.m_OrderBy = "ID_SubTipo ASC"; 
			setsub.ConCat(true);
			setsub.Open();
			
			//Construye este submenu
			String f_submenu = "", t_submenu = ""; 
			for(int ism = 0; ism < setsub.getNumRows(); ism++)
			{
				t_submenu = p_submenu;
				
				t_submenu = JUtil.replace(t_submenu, "fsi-color-submenu", color);
					
				JAyudaPaginasSubTiposTiposSet st = new JAyudaPaginasSubTiposTiposSet(null);
				st.m_OrderBy = "Status DESC, ID_Pagina ASC";
				st.m_Where = "ID_SubTipo = '" + p(setsub.getAbsRow(ism).getID_SubTipo()) + "' and (Status = '1' or Status = '2')";
				st.ConCat(true);
				st.Open();
				if(st.getNumRows() > 0)
					t_submenu = JUtil.replace(t_submenu, "fsi-href-submenu", st.getAbsRow(0).getID_Pagina() + ".html");
				else
					t_submenu = JUtil.replace(t_submenu, "fsi-href-submenu", "#");
					
				t_submenu = JUtil.replace(t_submenu, "fsi-descripcion-submenu", setsub.getAbsRow(ism).getDescripcion());
				
				f_submenu += t_submenu + "\n";
			}
			//Fin construccion submenu
			//System.out.println(f_submenu);
			
				
			for(int ism = 0; ism < setsub.getNumRows(); ism++)
			{
				pags.m_Where = "ID_SubTipo = '" + p(setsub.getAbsRow(ism).getID_SubTipo()) + "' and Status <> '3' and Tipo = 'COMPLETA'";
				pags.m_OrderBy = "ID_Pagina ASC";
				pags.ConCat(true);
				pags.Open();
				
				//construye paginas del submenu
				String f_paginas = "", t_paginas = ""; 
				for(int ip = 0; ip < pags.getNumRows(); ip++)
				{
					t_paginas = p_paginas;
					t_paginas = JUtil.replace(t_paginas, "fsi-color-paginas", color);
					t_paginas = JUtil.replace(t_paginas, "fsi-href-paginas", pags.getAbsRow(ip).getID_Pagina() + ".html");
					t_paginas = JUtil.replace(t_paginas, "fsi-descripcion-paginas", pags.getAbsRow(ip).getDescripcion());
					t_paginas = JUtil.replace(t_paginas, "fsi-busqueda-paginas", pags.getAbsRow(ip).getBusqueda());
					
					f_paginas += t_paginas + "\n";		 
				}
				//System.out.println(f_paginas);
				//fin construccion de paginas del submenu
					
				//Ahora si construye cada página del sitio
				String f_cuerpo = "", t_cuerpo = "", f_enlacestit = "", t_enlacestit = "", f_enlaces = "", t_enlaces = "";
				for(int ip = 0; ip < pags.getNumRows(); ip++)
				{
					JAyudaPaginaSet pagset = new JAyudaPaginaSet(null);
					pagset.ConCat(true);
					pagset.m_Where = "ID_Pagina = '" + p(pags.getAbsRow(ip).getID_Pagina()) + "'";
					pagset.Open();
					
					f_cuerpo = ""; t_cuerpo = p_cuerpo;
					t_cuerpo = JUtil.replace(t_cuerpo, "fsi-color-titulo", color);
					t_cuerpo = JUtil.replace(t_cuerpo, "fsi-cuerpo-titulo", pags.getAbsRow(ip).getDescripcion());
					t_cuerpo = JUtil.replace(t_cuerpo, "fsi-cuerpo-todo", pagset.getAbsRow(0).getCuerpo());
					
					f_cuerpo = t_cuerpo + "\n";
					
					JAyudaPaginasEnlacesSet pvp = new JAyudaPaginasEnlacesSet(null);
					pvp.m_OrderBy = "ID_Pagina ASC";
					pvp.m_Where = "ID_Enlace = '" + p(pags.getAbsRow(ip).getID_Pagina()) + "'";
					pvp.ConCat(true);
					pvp.Open();
					
					f_enlacestit = ""; f_enlaces = ""; t_enlacestit = ""; t_enlaces = "";
					
					if(pvp.getNumRows() > 0)
					{
						t_enlacestit = p_enlacestit;
						t_enlacestit = JUtil.replace(t_enlacestit, "fsi-color-enlacestit", color);
						f_enlacestit += t_enlacestit + "\n";
						
						for(int ie = 0; ie < pvp.getNumRows(); ie++)
						{
							t_enlaces = p_enlaces;
							t_enlaces = JUtil.replace(t_enlaces, "fsi-color-enlaces", color);
							t_enlaces = JUtil.replace(t_enlaces, "fsi-href-enlaces", pvp.getAbsRow(ie).getID_Pagina() + ".html");
							t_enlaces = JUtil.replace(t_enlaces, "fsi-descripcion-enlaces", pvp.getAbsRow(ie).getDescripcion());
							t_enlaces = JUtil.replace(t_enlaces, "fsi-busqueda-enlaces", pvp.getAbsRow(ie).getBusqueda());
							f_enlaces += t_enlaces + "\n";
						}
						//System.out.println(f_enlacestit + f_enlaces);
					}
					
					//Guarda la pagina en forsetidoc
					String cuerpo_final = fin_cuerpo_plant;
					cuerpo_final = JUtil.replace(cuerpo_final, "_submenu", f_submenu);
					cuerpo_final = JUtil.replace(cuerpo_final, "_cuerpo", f_cuerpo);
					cuerpo_final = JUtil.replace(cuerpo_final, "_enlacestit", f_enlacestit);
					cuerpo_final = JUtil.replace(cuerpo_final, "_enlaces", f_enlaces);
					cuerpo_final = JUtil.replace(cuerpo_final, "_paginas", f_paginas);
					String pagina_final = fin_plant;
					pagina_final = JUtil.replace(pagina_final, "_cuerpo_completo", cuerpo_final);
						 
						
					//Ahora guarda el html
					//System.out.println(pags.getAbsRow(ip).getID_Pagina() + ".html");
			  		
					FileWriter fwp = new FileWriter(var.getAbsRow(0).getVAlfanumerico() + "/webapps/ROOT/forsetidoc/" + pags.getAbsRow(ip).getID_Pagina() + ".html");
			        PrintWriter pwp = new PrintWriter(fwp);
			        pwp.println(pagina_final);
			        fwp.close();
			        // Fin de pagina guardada
			        // Si contiene id_alterntivo, guarda una copia del id alternativo
			        if(pags.getAbsRow(ip).getID_Alternativo() != null && !pags.getAbsRow(ip).getID_Alternativo().equals("null"))
			        {
			        	fwp = new FileWriter(var.getAbsRow(0).getVAlfanumerico() + "/webapps/ROOT/forsetidoc/" + pags.getAbsRow(ip).getID_Alternativo() + ".html");
				        pwp = new PrintWriter(fwp);
				        pwp.println(pagina_final);
				        fwp.close();
			        }
				}
			}
			
		}
       	
       	//Ahora genera las paginas sencillas
       	pags.m_Where = "Tipo = 'SENCILLA'";
		pags.m_OrderBy = "ID_Pagina ASC";
		pags.ConCat(true);
		pags.Open();
		for(int ip = 0; ip < pags.getNumRows(); ip++)
		{
			JAyudaPaginaSet pagset = new JAyudaPaginaSet(request);
			pagset.ConCat(true);
			pagset.m_Where = "ID_Pagina = '" + p(pags.getAbsRow(ip).getID_Pagina()) + "'";
			pagset.Open();
			
			//Guarda la pagina en forsetidoc
			String pagina_final = JUtil.replace(ps, "_cuerpo_completo", pagset.getAbsRow(0).getCuerpo());
			//Ahora guarda el html
			//System.out.println(pags.getAbsRow(ip).getID_Pagina() + ".html");
	  		
			FileWriter fwp = new FileWriter(var.getAbsRow(0).getVAlfanumerico() + "/webapps/ROOT/forsetidoc/" + pags.getAbsRow(ip).getID_Pagina() + ".html");
	        PrintWriter pwp = new PrintWriter(fwp);
	        pwp.println(pagina_final);
	        fwp.close();
	        
	        // Fin de pagina guardada
		}
       	///////////////////////////////////////////////////////////////////////////
       	//Ya generó el sitio web de ayuda, ahora genera la exportacion de ayuda.
       	///////////////////////////////////////////////////////////////////////////
       	
       	FileWriter fwp = new FileWriter("/usr/local/forseti/bin/.forseti_doc");
        PrintWriter pwp = new PrintWriter(fwp);
        
        String archivo = "_INI_TIPOS\n";
        JAyudaTipoSet ats = new JAyudaTipoSet(null);
        ats.m_OrderBy = "ID_Tipo ASC";
        ats.ConCat(true);
        ats.Open();
        for(int i = 0; i < ats.getNumRows(); i++)
        {
        	archivo += ats.getAbsRow(i).getID_Tipo() + "|" + ats.getAbsRow(i).getDescripcion() + "\n";
        	pwp.flush();
        }
        
        archivo += "_FIN_TIPOS\n\n_INI_SUBTIPOS\n";
        
        JAyudaSubTipoSet asts = new JAyudaSubTipoSet(null);
        asts.m_OrderBy = "ID_SubTipo ASC";
        asts.ConCat(true);
        asts.Open();
        for(int i = 0; i < asts.getNumRows(); i++)
        {
        	archivo += asts.getAbsRow(i).getID_SubTipo() + "|" + asts.getAbsRow(i).getDescripcion() + "|" + asts.getAbsRow(i).getID_Tipo() + "\n";
        	pwp.flush();
        }
       
        archivo += "_FIN_SUBTIPOS\n\n_INI_PAGINAS\n\n";
       
        JAyudaPaginaSet aps = new JAyudaPaginaSet(null);
        aps.m_OrderBy = "ID_Pagina ASC";
        aps.ConCat(true);
        aps.Open();
        for(int i = 0; i < aps.getNumRows(); i++)
        {
        	archivo += "_INI_PAG\n" + aps.getAbsRow(i).getID_Pagina() + "|" + aps.getAbsRow(i).getDescripcion() + "|" + aps.getAbsRow(i).getBusqueda() + "|" + aps.getAbsRow(i).getStatus() + "|" + aps.getAbsRow(i).getTipo() + "|" + aps.getAbsRow(i).getID_Alternativo() + "\n_CUERPO_PAG\n";
        	archivo += aps.getAbsRow(i).getCuerpo() + "\n_FIN_PAG\n\n";
        	pwp.flush();
        }
        
        archivo += "_FIN_PAGINAS\n\n_INI_PAGINAS_ENLACES\n";
        
        JAyudaPaginasEnlacesSet ape = new JAyudaPaginasEnlacesSet(null);
        ape.ConCat(true);
        ape.Open();
        for(int i = 0; i < ape.getNumRows(); i++)
        {
           	archivo += ape.getAbsRow(i).getID_Pagina() + "|" + ape.getAbsRow(i).getID_Enlace() + "\n";
           	pwp.flush();
        }
        
        archivo += "_FIN_PAGINAS_ENLACES\n\n_INI_PAGINAS_SUBTIPOS\n\n";
        
        JAyudaPaginasSubTiposTiposSet apst = new JAyudaPaginasSubTiposTiposSet(null);
        apst.ConCat(true);
        apst.Open();
        for(int i = 0; i < apst.getNumRows(); i++)
        {
           	archivo += apst.getAbsRow(i).getID_SubTipo() + "|" + apst.getAbsRow(i).getID_Pagina() + "\n";
           	pwp.flush();
        }
        
        archivo += "\n_FIN_PAGINAS_SUBTIPOS\n\n";
        
        pwp.println(archivo);
        fwp.close();
        
        RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_GENERAR","AYUP||||",mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje);
		irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
	    return;
	}
}
