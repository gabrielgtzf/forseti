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

public class JAdmUsuariosCtrl extends JForsetiApl
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

      String adm_usuarios = "";
      request.setAttribute("adm_usuarios",adm_usuarios);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADM_USUARIOS"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS","AUSR||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("ADM_USUARIOS") == false)
      {
        getSesion(request).EstablecerCEF(request, "adm_usuarios.png", "ADM_USUARIOS");
  	  	getSesion(request).getSesion("ADM_USUARIOS").setParametros("", "", "", "", "", "");
  	  	getSesion(request).getSesion("ADM_USUARIOS").setOrden(p(request.getParameter("etq")),"");
  	  	getSesion(request).getSesion("ADM_USUARIOS").setEspecial("");
        
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ADM_USUARIOS","AUSR||||","");
        irApag("/forsetiweb/administracion/adm_usuarios_vsta.jsp",request,response);
        return;
      }

      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("ADM_USUARIOS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/administracion/adm_usuarios_vsta.jsp", request, response);

    }

}
