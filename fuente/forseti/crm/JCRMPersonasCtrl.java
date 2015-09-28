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
package forseti.crm;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;
import forseti.sets.JCRMLookUpSet;
import forseti.sets.JNotasBlocksIdsSet;

@SuppressWarnings("serial")
public class JCRMPersonasCtrl extends JForsetiApl
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

      String crm_personas = "";
      request.setAttribute("crm_personas",crm_personas);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(!getSesion(request).getPermiso("CRM_PERSONAS"))
      {
          idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_PERSONAS");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CRM_PERSONAS","CRMP||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("CRM_PERSONAS") == false)
      {
        JNotasBlocksIdsSet set = new JNotasBlocksIdsSet(request,usuario,"CEF-1");
        set.Open();
        
        if(set.getNumRows() < 1)
        {
          idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
        }

        String Entidad = "gu_workarea = '" + set.getAbsRow(0).getGU_Workarea() + "'";
        String Tiempo = "full_name ~~* 'A%'";
        
        getSesion(request).EstablecerCEF(request, "crm_personas.png", "CRM_PERSONAS");
        getSesion(request).getSesion("CRM_PERSONAS").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getEtiqueta(), "A", "");
        getSesion(request).getSesion("CRM_PERSONAS").setOrden(p(request.getParameter("etq")),"");
        getSesion(request).getSesion("CRM_PERSONAS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Block()) + "|" + set.getAbsRow(0).getGU_Workarea());
  	  
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CRM_PERSONAS","CRMP||" + set.getAbsRow(0).getGU_Workarea() + "||","");
        irApag("/forsetiweb/crm/crm_personas_vsta.jsp",request,response);
        return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	JNotasBlocksIdsSet set = new JNotasBlocksIdsSet(request,usuario,p(request.getParameter("entidad")));
        set.Open();
        if(set.getNumRows() > 0)
        {
        	String Entidad = "gu_workarea = '" + set.getAbsRow(0).getGU_Workarea() + "'";
        	getSesion(request).getSesion("CRM_PERSONAS").setEntidad(Entidad,set.getAbsRow(0).getEtiqueta());
        	getSesion(request).getSesion("CRM_PERSONAS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Block()) + "|" + set.getAbsRow(0).getGU_Workarea());
        }
        else
        {
        	idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
        	RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"CRM_PERSONAS","CRMP||" + p(request.getParameter("entidad")) + "||",mensaje);
        }
      }
      
      else if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  if(request.getParameter("tiempo").equals("TODO"))
    		  getSesion(request).getSesion("CRM_PERSONAS").setTiempo("", "****");
    	  else  
    		  getSesion(request).getSesion("CRM_PERSONAS").setTiempo("full_name ~~* '" + p(request.getParameter("tiempo")) + "%'", p(request.getParameter("tiempo")));
      }
      else if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
      {
    	  JCRMLookUpSet set = new JCRMLookUpSet(request, "contacts");
    	  set.m_Where = "ID_Section = 'id_status' and pg_lookup = '" + p(request.getParameter("status")) + "'";
          set.Open();
          if(set.getNumRows() > 0)
          {
          	String Status = "id_status = '" + p(set.getAbsRow(0).getVL_lookup()) + "'";
          	getSesion(request).getSesion("CRM_PERSONAS").setStatus(Status,set.getAbsRow(0).getTR_es());
          }
          else
          {
          	idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de status
          	RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"CRM_PERSONAS","CRMP||||",mensaje);
          }
      }
      else if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
       	  getSesion(request).getSesion("CRM_PERSONAS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/crm/crm_personas_vsta.jsp", request, response);

    }

}

