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

@SuppressWarnings("serial")
public class JContaTipoPolizasCtrl extends JForsetiApl
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

      String conta_tipopolizas = "";
      request.setAttribute("conta_tipopolizas",conta_tipopolizas);

      String mensaje = ""; short idmensaje = -1;
     
      if(!getSesion(request).getPermiso("CONT_TIPOPOLIZA"))
      {
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_TIPOPOLIZA");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA","TPOL||||",mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      if(getSesion(request).getEst("CONT_TIPOPOLIZA") == false)
      { 
    	  getSesion(request).EstablecerCEF(request, "cont_tipopoliza.png", "CONT_TIPOPOLIZA");
    	  getSesion(request).getSesion("CONT_TIPOPOLIZA").setParametros("", "", "", "", "", "");
    	  getSesion(request).getSesion("CONT_TIPOPOLIZA").setOrden(p(request.getParameter("etq")),"");
    	  getSesion(request).getSesion("CONT_TIPOPOLIZA").setEspecial("");

    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_TIPOPOLIZA","TPOL||||","");
          irApag("/forsetiweb/contabilidad/conta_tipopolizas_vsta.jsp",request,response);
    	  return;
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("CONT_TIPOPOLIZA").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/contabilidad/conta_tipopolizas_vsta.jsp", request, response);

    }

}
