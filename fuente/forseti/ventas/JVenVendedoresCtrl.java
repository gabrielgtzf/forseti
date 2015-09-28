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
package forseti.ventas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;

@SuppressWarnings("serial")
public class JVenVendedoresCtrl extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }
/*
    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String ven_vendedores = "";
      request.setAttribute("ven_vendedores",ven_vendedores);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADMON_VENDEDORES"))
      {
        idmensaje = 3; mensaje += " No tienes acceso al módulo de vendedores ";
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getVenVendedoresEst() == false)
      {
        getSesion(request).EstablecerVenVendedores();
        getSesion(request).getSesionVenVendedores().setParametros("", "", "", "", "", "");
        getSesion(request).getSesionVenVendedores().setOrden(request.getParameter("ordenetq"),"ID_Vendedor");
        getSesion(request).getSesionVenVendedores().setEspecial("");

        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/ventas/ven_vendedores_vsta.jsp",request,response);
      }

      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
        getSesion(request).getSesionVenVendedores().setOrden(request.getParameter("ordenetq"),request.getParameter("orden"));
      }
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/ventas/ven_vendedores_vsta.jsp", request, response);

    }
*/
}
