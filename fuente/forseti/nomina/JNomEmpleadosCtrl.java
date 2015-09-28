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
import forseti.JUtil;
import forseti.sets.JNominaEntidadesSetIds;

@SuppressWarnings("serial")
public class JNomEmpleadosCtrl extends JForsetiApl
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

      String nom_empleados = "";
      request.setAttribute("nom_empleados",nom_empleados);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(!getSesion(request).getPermiso("NOM_EMPLEADOS"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_EMPLEADOS");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS","NEMP||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("NOM_EMPLEADOS") == false)
      {
          JNominaEntidadesSetIds set = new JNominaEntidadesSetIds(request,usuario,"CEF-1");
          set.Open();
          
          if(set.getNumRows() < 1)
          {
            idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
            return;
          }

          String Entidad = "ID_Sucursal = '" + set.getAbsRow(0).getID_Sucursal() + "'";
         
          getSesion(request).EstablecerCEF(request, "nom_empleados.png", "NOM_EMPLEADOS");
          getSesion(request).getSesion("NOM_EMPLEADOS").setParametros(Entidad, "", "", set.getAbsRow(0).getDescripcion(), "", "");
          getSesion(request).getSesion("NOM_EMPLEADOS").setOrden(p(request.getParameter("ordenetq")),"ID_Sucursal");
          getSesion(request).getSesion("NOM_EMPLEADOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Sucursal()));

          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS","NEMP||" + set.getAbsRow(0).getID_Sucursal() + "||","");
          irApag("/forsetiweb/nomina/nom_empleados_vsta.jsp",request,response);
          return;
      }
      
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  JNominaEntidadesSetIds set = new JNominaEntidadesSetIds(request,usuario,p(request.getParameter("entidad")));
          set.Open();
          if(set.getNumRows() > 0)
          {
        	  String Entidad = "ID_Sucursal = '" + set.getAbsRow(0).getID_Sucursal() + "'";
        	  getSesion(request).getSesion("NOM_EMPLEADOS").setEntidad(Entidad,set.getAbsRow(0).getDescripcion());
        	  getSesion(request).getSesion("NOM_EMPLEADOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Sucursal()));
          }
          else
          {
        	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
        	  RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS","NEMP||" + p(request.getParameter("entidad")) + "||",mensaje);
          }

      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("NOM_EMPLEADOS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/nomina/nom_empleados_vsta.jsp", request, response);

    }

}
