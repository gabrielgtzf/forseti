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
public class JCatGastosCtrl extends JForsetiApl
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

      String cat_gastos = "";
      request.setAttribute("cat_gastos",cat_gastos);

      String mensaje = ""; short idmensaje = -1;
    
      if(!getSesion(request).getPermiso("INVSERV_GASTOS"))
      {
          idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_GASTOS");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_GASTOS","CATG||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("INVSERV_GASTOS") == false)
      {
    	  String Entidad = "ID_Tipo = 'G'";
          String Tiempo = "Descripcion ~~* 'A%'";

          getSesion(request).EstablecerCEF(request, "invserv_gastos.png", "INVSERV_GASTOS");
          getSesion(request).getSesion("INVSERV_GASTOS").setParametros(Entidad, Tiempo, "", "", "A", JUtil.Elm(JUtil.Msj("CEF", "INVSERV_GASTOS", "VISTA", "STATUS"),1));
          getSesion(request).getSesion("INVSERV_GASTOS").setOrden(p(request.getParameter("etq")),"");
          getSesion(request).getSesion("INVSERV_GASTOS").setEspecial("");
    	  
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"INVSERV_GASTOS","CATG||||","");
          irApag("/forsetiweb/catalogos/cat_gastos_vsta.jsp",request,response);
          return;
      }

      if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  if(request.getParameter("tiempo").equals("TODO"))
			  getSesion(request).getSesion("INVSERV_GASTOS").setTiempo("", "****");
		  else  
			  getSesion(request).getSesion("INVSERV_GASTOS").setTiempo("Descripcion ~~* '" + p(request.getParameter("tiempo")) + "%'", p(request.getParameter("tiempo")));
      }
      else if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
      {
    	  String sts = JUtil.Msj("CEF", "INVSERV_GASTOS", "VISTA", "STATUS");
    	  
    	  if(request.getParameter("status").equals("TODOS"))
    	  {
    		  getSesion(request).getSesion("INVSERV_GASTOS").setStatus("",JUtil.Elm(sts,1));
    	  }
    	  else if(request.getParameter("status").equals("VIGENTES"))
    	  {
    		  getSesion(request).getSesion("INVSERV_GASTOS").setStatus("Status = 'V'",JUtil.Elm(sts,2));
    	  }
    	  else if(request.getParameter("status").equals("DESCONTINUADOS"))
    	  {
    		  getSesion(request).getSesion("INVSERV_GASTOS").setStatus("Status = 'D'",JUtil.Elm(sts,3));
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 3); // 3 Error de Estatus
    	  }
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("INVSERV_GASTOS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/catalogos/cat_gastos_vsta.jsp",request,response);
      
    }

}
