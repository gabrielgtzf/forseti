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
import forseti.JUtil;
import forseti.sets.JVentasEntidadesSetIdsV2;

@SuppressWarnings("serial")
public class JVenClientCtrl extends JForsetiApl
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

      String ven_client = "";
      request.setAttribute("ven_client",ven_client);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(!getSesion(request).getPermiso("VEN_CLIENT"))
      {
          idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CLIENT");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CLIENT","VCLI||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("VEN_CLIENT") == false)
      {
        JVentasEntidadesSetIdsV2 set = new JVentasEntidadesSetIdsV2(request,usuario,"CEF-1");
        set.Open();
        
        if(set.getNumRows() < 1)
        {
          idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
        }

        String Entidad = "ID_Tipo = 'CL' and ID_EntidadVenta = '" + set.getAbsRow(0).getID_Entidad() + "'";
        String Tiempo = "Nombre ~~* 'A%'";

        getSesion(request).EstablecerCEF(request, "ven_client.png", "VEN_CLIENT");
        getSesion(request).getSesion("VEN_CLIENT").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getDescripcion(), "A", JUtil.Elm(JUtil.Msj("CEF", "VEN_CLIENT", "VISTA", "STATUS"),1));
        getSesion(request).getSesion("VEN_CLIENT").setOrden(p(request.getParameter("etq")),"");
        getSesion(request).getSesion("VEN_CLIENT").setEspecial(Integer.toString(set.getAbsRow(0).getID_Entidad()));
  	  
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"VEN_CLIENT","VCLI||" + set.getAbsRow(0).getID_Entidad() + "||","");
        irApag("/forsetiweb/ventas/ven_client_vsta.jsp",request,response);
        return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
        JVentasEntidadesSetIdsV2 set = new JVentasEntidadesSetIdsV2(request,usuario,p(request.getParameter("entidad")));
        set.Open();
        if(set.getNumRows() > 0)
        {
        	String Entidad = "ID_Tipo = 'CL' and ID_EntidadVenta = '" + set.getAbsRow(0).getID_Entidad() + "'";
        	getSesion(request).getSesion("VEN_CLIENT").setEntidad(Entidad,set.getAbsRow(0).getDescripcion());
        	getSesion(request).getSesion("VEN_CLIENT").setEspecial(Integer.toString(set.getAbsRow(0).getID_Entidad()));
        }
        else
        {
        	idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
        	RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"VEN_CLIENT","VCLI||" + p(request.getParameter("entidad")) + "||",mensaje);
        }
      }
      else if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  if(request.getParameter("tiempo").equals("TODO"))
    		  getSesion(request).getSesion("VEN_CLIENT").setTiempo("", "****");
    	  else  
    		  getSesion(request).getSesion("VEN_CLIENT").setTiempo("Nombre ~~* '" + p(request.getParameter("tiempo")) + "%'", p(request.getParameter("tiempo")));
      }
      else if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
      {
    	  String sts = JUtil.Msj("CEF", "VEN_CLIENT", "VISTA", "STATUS");
    	  
    	  if(request.getParameter("status").equals("TODO"))
    	  {
    		  getSesion(request).getSesion("VEN_CLIENT").setStatus("",JUtil.Elm(sts,1));
    	  }
    	  else if(request.getParameter("status").equals("ALTA"))
    	  {
    		  getSesion(request).getSesion("VEN_CLIENT").setStatus("Status = 'A'",JUtil.Elm(sts,2));
    	  }
    	  else if(request.getParameter("status").equals("BAJA"))
    	  {
    		  getSesion(request).getSesion("VEN_CLIENT").setStatus("Status = 'B'",JUtil.Elm(sts,3));
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 3); // 3 Error de Estatus
    	  }
      }
      else if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
       	  getSesion(request).getSesion("VEN_CLIENT").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/ventas/ven_client_vsta.jsp", request, response);

    }

}
