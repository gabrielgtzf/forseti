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
package fsi_admin.admon;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fsi_admin.JFsiForsetiApl;

@SuppressWarnings("serial")
public class JAyudaSubTipoCtrl extends JFsiForsetiApl
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

      String ayuda_subtipo = "";
      request.setAttribute("ayuda_subtipo",ayuda_subtipo);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADMIN_AYUDA"))
      {
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_AYUDA");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_AYUDA","AYUS||||",mensaje);
        irApag("/forsetiadmin/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("ADMIN_AYUDASUB") == false)
      { 
    	getSesion(request).EstablecerSAF("admin_ayuda.png","ADMIN_AYUDASUB","ADMIN_AYUDA");
        getSesion(request).getSesion("ADMIN_AYUDASUB").setParametros("", "", "", "", "", "");
        getSesion(request).getSesion("ADMIN_AYUDASUB").setOrden(p(request.getParameter("etq")),"");
        getSesion(request).getSesion("ADMIN_AYUDASUB").setEspecial("");

        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("SAF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ADMIN_AYUDA","AYUS||||","");
        irApag("/forsetiadmin/administracion/ayuda_subtipo_vsta.jsp",request,response);
        return;
      }
     
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
        getSesion(request).getSesion("ADMIN_AYUDASUB").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
        
      }
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiadmin/administracion/ayuda_subtipo_vsta.jsp", request, response);
      return;
    }
}
