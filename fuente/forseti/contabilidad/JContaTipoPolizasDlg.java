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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JContaTipoPolizasDlg extends JForsetiApl
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

      String conta_tipopolizas_dlg = "";
      request.setAttribute("conta_tipopolizas_dlg",conta_tipopolizas_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_TIPOPOLIZA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_TIPOPOLIZA_CREAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_TIPOPOLIZA_CREAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA_CREAR","TPOL||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
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
            irApag("/forsetiweb/contabilidad/conta_tipopolizas_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/contabilidad/conta_tipopolizas_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_TIPOPOLIZA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_TIPOPOLIZA_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_TIPOPOLIZA_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA_CAMBIAR","TPOL||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id_clasificacion") != null)
          {
            String[] valoresParam = request.getParameterValues("id_clasificacion");
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
                irApag("/forsetiweb/contabilidad/conta_tipopolizas_dlg.jsp", request, response);
                return;
                
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/contabilidad/conta_tipopolizas_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_TIPOPOLIZA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_TIPOPOLIZA_ELIMINAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_TIPOPOLIZA_ELIMINAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA_ELIMINAR","TPOL||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id_clasificacion") != null)
          {
            String[] valoresParam = request.getParameterValues("id_clasificacion");
            if(valoresParam.length == 1)
            {
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
        else
        {
          idmensaje = 1;
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
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
      if(request.getParameter("id_clasificacion") != null && request.getParameter("descripcion") != null
          && request.getParameter("ficha") != null  &&
          !request.getParameter("id_clasificacion").equals("") && !request.getParameter("descripcion").equals("")
          && !request.getParameter("ficha").equals("") )
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

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM sp_cont_polizas_cambiar_tipos( '" + p(request.getParameter("id_clasificacion")) + "','" +
        	p(request.getParameter("descripcion")) + "','" + p(request.getParameter("ficha")) + "') as ( err integer, res varchar, clave varchar )";

    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA_CAMBIAR","TPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/contabilidad/conta_tipopolizas_dlg.jsp", request, response);

    }
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	    	
    	String str = "SELECT * FROM sp_cont_polizas_eliminar_tipos( '" + p(request.getParameter("id_clasificacion")) + "') as ( err integer, res varchar, clave varchar )";

    	doCallStoredProcedure(request, response, str, rfb);
    	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA_CAMBIAR","TPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM sp_cont_polizas_agregar_tipos( '" + p(request.getParameter("id_clasificacion")) + "','" +
        	p(request.getParameter("descripcion")) + "','" + p(request.getParameter("ficha")) + "') as ( err integer, res varchar, clave varchar )";

    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA_CREAR","TPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/contabilidad/conta_tipopolizas_dlg.jsp", request, response);
   
    }

}
