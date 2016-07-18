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
package forseti.catalogos;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JCatLineasCtrl extends JForsetiApl
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

      String cat_lineas = "";
      request.setAttribute("cat_lineas",cat_lineas);

      String mensaje = ""; short idmensaje = -1;
    
      if(!getSesion(request).getPermiso("INVSERV_LINEAS"))
      {
          idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_LINEAS");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_LINEAS","CATL||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      if(getSesion(request).getEst("INVSERV_LINEAS") == false)
      {
          getSesion(request).EstablecerCEF(request, "invserv_lineas.png", "INVSERV_LINEAS");
          getSesion(request).getSesion("INVSERV_LINEAS").setParametros("", "", "", JUtil.Elm(JUtil.Msj("CEF", "INVSERV_LINEAS", "VISTA", "ENTIDADES"),1), "", "");
          getSesion(request).getSesion("INVSERV_LINEAS").setOrden(p(request.getParameter("etq")),"");
          getSesion(request).getSesion("INVSERV_LINEAS").setEspecial("LINEAS");
    	  
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"INVSERV_LINEAS","CATL||||","");
          irApag("/forsetiweb/catalogos/cat_lineas_vsta.jsp",request,response);
          return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  if(!request.getParameter("entidad").equals(getSesion(request).getSesion("INVSERV_LINEAS").getEspecial()))
          {
          	getSesion(request).getSesion("INVSERV_LINEAS").setOrden(null,"");
          }
    	  String ent = JUtil.Msj("CEF", "INVSERV_LINEAS", "VISTA", "ENTIDADES");
    	  
    	  if(request.getParameter("entidad").equals("LINEAS"))
    	  {
    		  getSesion(request).getSesion("INVSERV_LINEAS").setEntidad("",JUtil.Elm(ent, 1));
    		  getSesion(request).getSesion("INVSERV_LINEAS").setEspecial("LINEAS");  
    	  }
    	  else if(request.getParameter("entidad").equals("UNIDADES"))
    	  {
    		  getSesion(request).getSesion("INVSERV_LINEAS").setEntidad("ID_InvServ = 'P'",JUtil.Elm(ent, 2));
    		  getSesion(request).getSesion("INVSERV_LINEAS").setEspecial("UNIDADES");  
    	  }
          else
          {
             idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
          }
    	 
      }
            
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("INVSERV_LINEAS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/catalogos/cat_lineas_vsta.jsp",request,response);
      
    }

}
