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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JAdmMonedasDlg extends JForsetiApl
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

      String adm_monedas_dlg = "";
      request.setAttribute("adm_monedas_dlg",adm_monedas_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_MONEDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_MONEDAS_AGREGAR"))
  		  {
  			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_MONEDAS_AGREGAR");
  			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
  			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_MONEDAS_AGREGAR","AMON||||",mensaje);
  			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  			  return;
  		  }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(request.getParameter("idmoneda") != null && request.getParameter("moneda") != null && request.getParameter("simbolo") != null
              && request.getParameter("tc") != null &&
            	!request.getParameter("idmoneda").equals("") && !request.getParameter("moneda").equals("") && !request.getParameter("simbolo").equals("")
              && !request.getParameter("tc").equals(""))
            {
              Agregar(request, response);
              return;
            }
            else
            {
              idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/administracion/adm_monedas_dlg.jsp", request, response);
              return;
            }
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/administracion/adm_monedas_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_MONEDA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_MONEDAS_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_MONEDAS_AGREGAR");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_MONEDAS_AGREGAR","AMON||||",mensaje);
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
            	if(request.getParameter("idmoneda") != null && request.getParameter("moneda") != null && request.getParameter("simbolo") != null
            		&& request.getParameter("tc") != null &&
            		!request.getParameter("idmoneda").equals("") && !request.getParameter("moneda").equals("") && !request.getParameter("simbolo").equals("")
            		&& !request.getParameter("tc").equals(""))
                {
                	Cambiar(request, response);
                	return;
                }
                else
                {
                	idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	irApag("/forsetiweb/administracion/adm_monedas_dlg.jsp", request, response);
                	return;
                }
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/administracion/adm_monedas_dlg.jsp", request, response);
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

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String str = "select * from  sp_cont_monedas_cambiar('" + p(request.getParameter("idmoneda")) + "','" + p(request.getParameter("moneda")) + "','" +
          p(request.getParameter("simbolo")) + "','" + p(request.getParameter("tc")) + "','" + p(request.getParameter("id_satmon")) + "') as ( err integer, res varchar, clave int ) ";
        JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_MONEDAS_AGREGAR", "AMON|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/administracion/adm_monedas_dlg.jsp", request, response);
      

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String str = "select * from  sp_cont_monedas_agregar('" + p(request.getParameter("idmoneda")) + "','" + p(request.getParameter("moneda")) + "','" +
        p(request.getParameter("simbolo")) + "','" + p(request.getParameter("tc")) + "','" + p(request.getParameter("id_satmon")) + "') as ( err integer, res varchar, clave int ) ";
    	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_MONEDAS_AGREGAR", "AMON|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/administracion/adm_monedas_dlg.jsp", request, response);
    
    }

}
