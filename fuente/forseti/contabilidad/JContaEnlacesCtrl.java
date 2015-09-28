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
import forseti.JUtil;

@SuppressWarnings("serial")
public class JContaEnlacesCtrl extends JForsetiApl
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

      String conta_enlaces = "";
      request.setAttribute("conta_enlaces",conta_enlaces);

      String mensaje = ""; short idmensaje = -1;
    
      if(!getSesion(request).getPermiso("CONT_ENLACES"))
      {
    	
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_ENLACES");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_ENLACES","ENLA||||",mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los enlaces se estan configurando por primera ocasion
      if(getSesion(request).getEst("CONT_ENLACES") == false)
      {
    	  	getSesion(request).EstablecerCEF(request, "cont_enlaces.png", "CONT_ENLACES");
  	  		getSesion(request).getSesion("CONT_ENLACES").setParametros("", "", "", JUtil.Elm(JUtil.Msj("CEF", "CONT_ENLACES", "VISTA", "ENTIDADES"),1), "", "");
  	  		getSesion(request).getSesion("CONT_ENLACES").setOrden(p(request.getParameter("etq")),"");
  	  		getSesion(request).getSesion("CONT_ENLACES").setEspecial("ALMACEN");
        
  	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	  		RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_ENLACES","ENLA||||","");
  	  		irApag("/forsetiweb/contabilidad/conta_enlaces_vsta.jsp",request,response);
  	  		return;
      }
        
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  if(!request.getParameter("entidad").equals(getSesion(request).getSesion("CONT_ENLACES").getEspecial()))
          {
          	getSesion(request).getSesion("CONT_ENLACES").setOrden(null,"");
          }
    	  String ent = JUtil.Msj("CEF", "CONT_ENLACES", "VISTA", "ENTIDADES");
    	  getSesion(request).getSesion("CONT_ENLACES").setEspecial(p(request.getParameter("entidad")));
    	  if(request.getParameter("entidad").equals("ALMACEN"))
    		  getSesion(request).getSesion("CONT_ENLACES").setEntidad("",JUtil.Elm(ent,1));
    	  else if(request.getParameter("entidad").equals("CXP"))
    		  getSesion(request).getSesion("CONT_ENLACES").setEntidad("",JUtil.Elm(ent,2));
    	  else if(request.getParameter("entidad").equals("CXC"))
    		  getSesion(request).getSesion("CONT_ENLACES").setEntidad("",JUtil.Elm(ent,3));
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
    	  }
        
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
      	  getSesion(request).getSesion("CONT_ENLACES").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/contabilidad/conta_enlaces_vsta.jsp", request, response);

    }

}
