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
import forseti.JUtil;

@SuppressWarnings("serial")
public class JAdmVariablesCtrl extends JForsetiApl
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

      String adm_variables = "";
      request.setAttribute("adm_variables",adm_variables);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADM_VARIABLES"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_VARIABLES");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_VARIABLES","AVAR||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que las variables se estan configurando por primera ocasion
      if(getSesion(request).getEst("ADM_VARIABLES") == false)
      {
    	  	String Entidad = "Modulo = 'CONT'";  
    	  	getSesion(request).EstablecerCEF(request, "adm_variables.png", "ADM_VARIABLES");
  	  		getSesion(request).getSesion("ADM_VARIABLES").setParametros(Entidad, "", "", JUtil.Elm(JUtil.Msj("CEF", "ADM_VARIABLES", "VISTA", "ENTIDADES"),1), "", "");
  	  		getSesion(request).getSesion("ADM_VARIABLES").setOrden(p(request.getParameter("etq")),"");
  	  		getSesion(request).getSesion("ADM_VARIABLES").setEspecial("CONT");
        
  	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	  		RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ADM_VARIABLES","AVAR||||","");
  	  		irApag("/forsetiweb/administracion/adm_variables_vsta.jsp",request,response);
  	  		return;
      }
        
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	String ent = JUtil.Msj("CEF", "ADM_VARIABLES", "VISTA", "ENTIDADES");
    	getSesion(request).getSesion("ADM_VARIABLES").setEspecial(p(request.getParameter("entidad")));
  	    if(request.getParameter("entidad").equals("CONT"))
  	    	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'CONT'",JUtil.Elm(ent,1));
        else if(request.getParameter("entidad").equals("BAN"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'BAN'",JUtil.Elm(ent,2));
        else if(request.getParameter("entidad").equals("ALM"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'ALM'",JUtil.Elm(ent,3));
        else if(request.getParameter("entidad").equals("COMP"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'COMP'",JUtil.Elm(ent,4));
        else if(request.getParameter("entidad").equals("VEN"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'VEN'",JUtil.Elm(ent,5));
        else if(request.getParameter("entidad").equals("PROD"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'PROD'",JUtil.Elm(ent,6));
        else if(request.getParameter("entidad").equals("NOM"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'NOM'",JUtil.Elm(ent,7));
        else if(request.getParameter("entidad").equals("ADM"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'ADM'",JUtil.Elm(ent,8));
        else if(request.getParameter("entidad").equals("ESP"))
        	getSesion(request).getSesion("ADM_VARIABLES").setEntidad("Modulo = 'ESP'",JUtil.Elm(ent,9));
        else
   	  	{
   	  		idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
   	  	}
        
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
      	  getSesion(request).getSesion("ADM_VARIABLES").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/administracion/adm_variables_vsta.jsp", request, response);

    }

}
