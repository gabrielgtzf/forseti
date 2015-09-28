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
public class JNomCredSalDlg extends JForsetiApl
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

      String nom_credsal_dlg = "";
      request.setAttribute("nom_credsal_dlg",nom_credsal_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_CREDSAL"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_CREDSAL_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_CREDSAL_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_CREDSAL_AGREGAR","NCRS||||",mensaje);
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
            irApag("/forsetiweb/nomina/nom_credsal_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/nomina/nom_credsal_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_CREDSAL"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_CREDSAL_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_CREDSAL_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_CREDSAL_CAMBIAR","NCRS||||",mensaje);
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
                	Cambiar(request, response);
                	return;
                }
                irApag("/forsetiweb/nomina/nom_credsal_dlg.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/nomina/nom_credsal_dlg.jsp", request, response);
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
      if(request.getParameter("id_cs") != null && request.getParameter("ingresos_desde") != null &&
    		  request.getParameter("ingresos_hasta") != null && request.getParameter("csm") != null && 
    		  !request.getParameter("id_cs").equals("") && !request.getParameter("ingresos_desde").equals("") &&
    		  !request.getParameter("ingresos_hasta").equals("") && !request.getParameter("csm").equals("") )
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
      String str = "select * from  sp_nom_credito_salario_cambiar( '" + p(request.getParameter("id_cs")) + "','" +
          p(request.getParameter("ingresos_desde")) + "','" + p(request.getParameter("ingresos_hasta")) + "','" + p(request.getParameter("csm")) +
          "') as (err integer, res varchar, clave smallint)";
      
      JRetFuncBas rfb = new JRetFuncBas();
		
      doCallStoredProcedure(request, response, str, rfb);
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_CREDSAL_CAMBIAR", "NCRS|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/nomina/nom_credsal_dlg.jsp", request, response);
  

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String str = "select * from  sp_nom_credito_salario_agregar( '" + p(request.getParameter("id_cs")) + "','" +
          p(request.getParameter("ingresos_desde")) + "','" + p(request.getParameter("ingresos_hasta")) + "','" + p(request.getParameter("csm")) +
          "') as (err integer, res varchar, clave smallint)";

      JRetFuncBas rfb = new JRetFuncBas();
		
      doCallStoredProcedure(request, response, str, rfb);
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_CREDSAL_AGREGAR", "NCRS|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/nomina/nom_credsal_dlg.jsp", request, response);
    }

}
