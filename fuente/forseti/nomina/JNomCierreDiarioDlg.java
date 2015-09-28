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
import forseti.sets.JAdmCompaniasSet;

@SuppressWarnings("serial")
public class JNomCierreDiarioDlg extends JForsetiApl
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

      String nom_cierre_dlg = "";
      request.setAttribute("nom_cierre_dlg",nom_cierre_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_CIERRE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_CIERRE_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_CIERRE_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_CIERRE_AGREGAR","NCIE||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(VerificarParametros(request, response))
        	  {
        		  Calcular(request, response);
        		  return;
        	  }
        	  
        	  irApag("/forsetiweb/nomina/nom_cierre_dlg.jsp", request, response);
        	  return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/nomina/nom_cierre_dlg.jsp", request, response);
        	  return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_CIERRE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_CIERRE_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_CIERRE_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_CIERRE_CAMBIAR","NCIE||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(VerificarParametros(request, response))
        	  {
        		  Proteger(request, response);
        		  return;
        	  }
        	  
        	  irApag("/forsetiweb/nomina/nom_cierre_dlg.jsp", request, response);
        	  return;

          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/nomina/nom_cierre_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_CIERRE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_CIERRE"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_CIERRE");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_CIERRE","NCIE||||",mensaje);
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
                irApag("/forsetiweb/nomina/nom_cierre_dlg.jsp", request, response);
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
      if(request.getParameter("desde") != null && request.getParameter("hasta") != null &&
    		  !request.getParameter("desde").equals("") && !request.getParameter("hasta").equals(""))
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

    public void Calcular(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
 	  	JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
  	  	setcom.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_CIERRE").getEspecial() + "'";
  	  	setcom.Open();

  	  	String str = "select * from sp_nom_diario_cierre('" + p(setcom.getAbsRow(0).getDescripcion()) + "','" + p(JUtil.obtFechaSQL(request.getParameter("desde"))) + "','" +
  	  		p(JUtil.obtFechaSQL(request.getParameter("hasta"))) + "') as (err integer, res varchar, clave varchar)";	
	    
  	  	JRetFuncBas rfb = new JRetFuncBas();
		
  	  	doCallStoredProcedure(request, response, str, rfb);
  	  	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_CIERRE_AGREGAR", "NCIE|" + rfb.getClaveret() + "|||",rfb.getRes());
  	  	irApag("/forsetiweb/nomina/nom_cierre_dlg.jsp", request, response);
    }
    
    public void Proteger(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	  	JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
	  	setcom.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_CIERRE").getEspecial() + "'";
	  	setcom.Open();

	  	String str = "select * from sp_nom_diario_cierre_proteger('" + p(setcom.getAbsRow(0).getDescripcion()) + "','" + p(JUtil.obtFechaSQL(request.getParameter("desde"))) + "','" +
	  		p(JUtil.obtFechaSQL(request.getParameter("hasta"))) + "') as (err integer, res varchar, clave varchar)";	
	  	
	  	JRetFuncBas rfb = new JRetFuncBas();
		
	  	doCallStoredProcedure(request, response, str, rfb);
  	  	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_CIERRE_CAMBIAR", "NCIE|" + rfb.getClaveret() + "|||",rfb.getRes());
  	  	irApag("/forsetiweb/nomina/nom_cierre_dlg.jsp", request, response);

    }

}
