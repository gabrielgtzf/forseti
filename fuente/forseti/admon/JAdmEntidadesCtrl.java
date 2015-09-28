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
public class JAdmEntidadesCtrl extends JForsetiApl
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

      String adm_entidades = "";
      request.setAttribute("adm_entidades",adm_entidades);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADM_ENTIDADES"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_ENTIDADES");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_ENTIDADES","AENT||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que las entidades se estan configurando por primera ocasion
      if(getSesion(request).getEst("ADM_ENTIDADES") == false)
      {
    	  	String Entidad = "Tipo = '0'";  
    	  	getSesion(request).EstablecerCEF(request, "adm_entidades.png", "ADM_ENTIDADES");
  	  		getSesion(request).getSesion("ADM_ENTIDADES").setParametros(Entidad, "", "", JUtil.Elm(JUtil.Msj("CEF", "ADM_ENTIDADES", "VISTA", "ENTIDADES"),1), "", "");
  	  		getSesion(request).getSesion("ADM_ENTIDADES").setOrden(p(request.getParameter("etq")),"");
  	  		getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("BANCOS");
        
  	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	  		RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ADM_ENTIDADES","AENT||||","");
  	  		irApag("/forsetiweb/administracion/adm_entidades_vsta.jsp",request,response);
  	  		return;
      }
           
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  
        if(!request.getParameter("entidad").equals(getSesion(request).getSesion("ADM_ENTIDADES").getEspecial()))
        {
        	getSesion(request).getSesion("ADM_ENTIDADES").setOrden(null,"");
        }
        String ent = JUtil.Msj("CEF", "ADM_ENTIDADES", "VISTA", "ENTIDADES");
  	    if(request.getParameter("entidad").equals("BANCOS"))
        {
  	    	String Entidad = "Tipo = '0'";  
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad(Entidad,JUtil.Elm(ent,1));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("BANCOS");
        }
        else if(request.getParameter("entidad").equals("CAJAS"))
        {
        	String Entidad = "Tipo = '1'"; 
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad(Entidad,JUtil.Elm(ent,2));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("CAJAS");
        }
        else if(request.getParameter("entidad").equals("BODEGAS"))
        {
        	//String Entidad = "ID_Bodega <> -1";
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad("",JUtil.Elm(ent,3));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("BODEGAS");
        }
        else if(request.getParameter("entidad").equals("COMPRAS"))
        {
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad("",JUtil.Elm(ent,4));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("COMPRAS");
        }
        else if(request.getParameter("entidad").equals("VENTAS"))
        {
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad("",JUtil.Elm(ent,5));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("VENTAS");
        }
        else if(request.getParameter("entidad").equals("PRODUCCION"))
        {
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad("",JUtil.Elm(ent,6));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("PRODUCCION");
        }
        else if(request.getParameter("entidad").equals("NOMINA"))
        {
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad("",JUtil.Elm(ent,7));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("NOMINA");
        }
        else if(request.getParameter("entidad").equals("CRM"))
        {
        	getSesion(request).getSesion("ADM_ENTIDADES").setEntidad("",JUtil.Elm(ent,8));
        	getSesion(request).getSesion("ADM_ENTIDADES").setEspecial("CRM");
        }
   	  	else
   	  	{
   	  		idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
   	  	}
        
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
      	  getSesion(request).getSesion("ADM_ENTIDADES").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/administracion/adm_entidades_vsta.jsp", request, response);

    }

}
