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
package forseti.produccion;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;

@SuppressWarnings("serial")
public class JProdFormulasCtrl extends JForsetiApl
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

      String prod_formulas = "";
      request.setAttribute("prod_formulas",prod_formulas);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("PROD_FORMULAS"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_FORMULAS");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_FORMULAS","PFRM||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("PROD_FORMULAS") == false)
      {
    	  getSesion(request).EstablecerCEF(request, "prod_formulas.png", "PROD_FORMULAS");
          getSesion(request).getSesion("PROD_FORMULAS").setParametros("", "", "", "", "", "");
          getSesion(request).getSesion("PROD_FORMULAS").setOrden(p(request.getParameter("etq")),"");
          getSesion(request).getSesion("PROD_FORMULAS").setEspecial("");
    	  
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"PROD_FORMULAS","PFRM||||","");
          irApag("/forsetiweb/produccion/prod_formulas_vsta.jsp",request,response);
          return;
      }

      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("PROD_FORMULAS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/produccion/prod_formulas_vsta.jsp", request, response);

    }

}
