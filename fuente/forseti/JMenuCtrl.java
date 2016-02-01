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
package forseti;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;

public class JMenuCtrl extends JForsetiApl
{
 	private static final long serialVersionUID = 1L;
 	
 	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String glb_menu = "";
      request.setAttribute("glb_menu",glb_menu);

      String mensaje = ""; short idmensaje = -1;
   
      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("GLB_MENU") == false)
      {
    	  getSesion(request).EstablecerCEF_NO_PERM(request, "", "GLB_MENU","GLB_MENU");
    	  getSesion(request).getSesion("GLB_MENU").setEspecial("");
    	  //System.out.println("CEF Establecido");
  	  }
      else
      {
    	  if(request.getParameter("modulo") != null)
    	  {
    		  getSesion(request).getSesion("GLB_MENU").setEspecial(request.getParameter("modulo")); 
    		  //System.out.println("CEF Reasignado: " + request.getParameter("modulo"));
    	  }
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/menu_vsta.jsp", request, response);

    }

}
