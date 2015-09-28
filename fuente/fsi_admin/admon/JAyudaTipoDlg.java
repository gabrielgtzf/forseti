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
import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JRetFuncBas;
import forseti.JUtil;
import fsi_admin.JFsiForsetiApl;

@SuppressWarnings("serial")
public class JAyudaTipoDlg extends JFsiForsetiApl
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

      String ayuda_tipo_dlg = "";
      request.setAttribute("ayuda_tipo_dlg",ayuda_tipo_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_AYUDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_AYUDA_CREAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA_CREAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CREAR","AYUT||||",mensaje);
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

            irApag("/forsetiadmin/administracion/ayuda_tipo_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiadmin/administracion/ayuda_tipo_dlg.jsp", request, response);
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
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUT||||",mensaje);
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

                irApag("/forsetiadmin/administracion/ayuda_tipo_dlg.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiadmin/administracion/ayuda_tipo_dlg.jsp", request, response);
                return;
              }

            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite cambiar un menu a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador del menu que se quiere cambiar <br>";
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
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA_ELIMINAR","AYUT||||",mensaje);
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
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite eliminar un menu a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador del menu que se quiere eliminar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          }
        }
        else
        {
          idmensaje = 1;
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
      }

    }
  
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("idtipo") != null && request.getParameter("descripcion") != null &&
          !request.getParameter("idtipo").equals("") && !request.getParameter("descripcion").equals(""))
      {
    	  if(request.getParameter("proceso").equals("CAMBIAR_AYUDA"))
    	  {
    		  if(request.getParameter("id") == null || request.getParameter("id").equals(""))
    		  {
    			  idmensaje = 3; mensaje = JUtil.Msj("SAF", "ADMIN_AYUDA", "DLG", "MSJ-PROCERR", 1); //"ERROR: En cambios, se debe enviar el ID_Tipo anterior <br>";
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	          irApag("/forsetiadmin/administracion/ayuda_tipo_dlg.jsp", request, response);
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

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM sp_ayuda_tipo_cambiar('" + p(request.getParameter("id")) + "','" + p(request.getParameter("idtipo")) + "','" + p(request.getParameter("descripcion")) + "') as ( err integer, res varchar, clave varchar ) "; 

    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CAMBIAR","AYUT|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/administracion/ayuda_tipo_dlg.jsp", request, response);
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM sp_ayuda_tipo_agregar('" + p(request.getParameter("idtipo")) + "','" + p(request.getParameter("descripcion")) + "') as ( err integer, res varchar, clave varchar ) "; 

    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_CREAR","AYUT|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/administracion/ayuda_tipo_dlg.jsp", request, response);

    	
    }

    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM sp_ayuda_tipo_eliminar('" + p(request.getParameter("id")) + "') as ( err integer, res varchar, clave varchar ) ";

    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_AYUDA_ELIMINAR","AYUT|" + rfb.getClaveret() +  "|||",rfb.getRes());
        irApag("/forsetiadmin/caja_mensajes.jsp", request, response);

   		
	}   

}
