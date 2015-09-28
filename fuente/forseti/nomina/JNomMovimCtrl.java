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

@SuppressWarnings("serial")
public class JNomMovimCtrl extends JForsetiApl
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

      String nom_movim = "";
      request.setAttribute("nom_movim",nom_movim);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("NOM_MOVIM"))
      {
        idmensaje = 3; mensaje += " No tienes acceso al módulo de movimientos ";
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getNomMovimEst() == false)
      {
        getSesion(request).EstablecerNomMovim();
        getSesion(request).getSesionNomMovim().setParametros("", "", "", "", "", "");
        getSesion(request).getSesionNomMovim().setOrden(request.getParameter("ordenetq"),"ID_Movimiento");
        getSesion(request).getSesionNomMovim().setEspecial("");

        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/nomina/nom_movim_vsta.jsp",request,response);
        return;
      }

      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
        getSesion(request).getSesionNomMovim().setOrden(request.getParameter("ordenetq"),request.getParameter("orden"));
      }
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/nomina/nom_movim_vsta.jsp", request, response);

    }
*/
}
