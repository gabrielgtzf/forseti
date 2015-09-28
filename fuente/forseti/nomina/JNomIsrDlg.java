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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JNomIsrDlg extends JForsetiApl
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

      String nom_isr_dlg = "";
      request.setAttribute("nom_isr_dlg",nom_isr_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_ISR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_ISR_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ISR_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_ISR_AGREGAR","NISR||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response))
            {
              AgregarCambiar(request, response, "agregar");
              return;
            }
            irApag("/forsetiweb/nomina/nom_isr_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/nomina/nom_isr_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_ISR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_ISR_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ISR_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_ISR_CAMBIAR","NISR||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
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
                	AgregarCambiar(request, response, "cambiar");
                	return;
                }
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/nomina/nom_isr_dlg.jsp", request, response);
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

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("id_isr") != null && request.getParameter("limite_inferior") != null &&
    	  request.getParameter("limite_superior") != null && request.getParameter("cuota_fija") != null && request.getParameter("subsidio") != null && request.getParameter("subsidio_sim") != null &&  
		  request.getParameter("porcentaje_exd") != null && request.getParameter("limite_inferior_anual") != null &&
    	  request.getParameter("limite_superior_anual") != null && request.getParameter("cuota_fija_anual") != null && request.getParameter("subsidio_anual") != null && request.getParameter("subsidio_sim_anual") != null &&  
		  request.getParameter("porcentaje_exd_anual") != null &&  
		  !request.getParameter("id_isr").equals("") && !request.getParameter("limite_inferior").equals("") &&
    	  !request.getParameter("limite_superior").equals("") && !request.getParameter("cuota_fija").equals("") && !request.getParameter("subsidio").equals("") && !request.getParameter("subsidio_sim").equals("") &&  
		  !request.getParameter("porcentaje_exd").equals("") && !request.getParameter("limite_inferior_anual").equals("") &&
    	  !request.getParameter("limite_superior_anual").equals("") && !request.getParameter("cuota_fija_anual").equals("") && !request.getParameter("subsidio_anual").equals("") && !request.getParameter("subsidio_sim_anual").equals("") &&  
		  !request.getParameter("porcentaje_exd_anual").equals("") )
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

    public void AgregarCambiar(HttpServletRequest request, HttpServletResponse response, String proceso)
      throws ServletException, IOException
    {
    	String str = "select * from " + (proceso.equals("agregar") ? " sp_nom_isr_agregar( '" : " sp_nom_isr_cambiar( '");
	    str += p(request.getParameter("id_isr")) + "','" + p(request.getParameter("limite_inferior")) + "','" + 
	    	  p(request.getParameter("limite_superior")) + "','" + p(request.getParameter("cuota_fija")) + "','" + p(request.getParameter("porcentaje_exd"))  + "','" + p(request.getParameter("subsidio")) + "','" + p(request.getParameter("subsidio_sim")) + "','" +
	    	  p(request.getParameter("limite_inferior_anual")) + "','" + 
	    	  p(request.getParameter("limite_superior_anual")) + "','" + p(request.getParameter("cuota_fija_anual")) + "','" + p(request.getParameter("porcentaje_exd_anual"))  + "','" + p(request.getParameter("subsidio_anual")) + "','" + p(request.getParameter("subsidio_sim_anual")) +
	    	  "') as (err integer, res varchar, clave smallint)";  
			  
	    JRetFuncBas rfb = new JRetFuncBas();
			
	    doCallStoredProcedure(request, response, str, rfb);
  	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_ISR_" + (proceso.equals("agregar") ? "AGREGAR" : "CAMBIAR") , "NISR|" + rfb.getClaveret() + "|||",rfb.getRes());
  	    irApag("/forsetiweb/nomina/nom_isr_dlg.jsp", request, response);
	   
    }

  

}
