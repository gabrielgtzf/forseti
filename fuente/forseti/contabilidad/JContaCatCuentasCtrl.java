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
import forseti.JUtil;

@SuppressWarnings("serial")
public class JContaCatCuentasCtrl extends JForsetiApl
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

      String conta_catcuentas = "";
      request.setAttribute("conta_catcuentas",conta_catcuentas);

      String mensaje = ""; short idmensaje = -1;
     
      if(!getSesion(request).getPermiso("CONT_CATCUENTAS"))
      {
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_CATCUENTAS");
        RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_CATCUENTAS","CATC||||",mensaje);
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("CONT_CATCUENTAS") == false)
      { 
    	  getSesion(request).EstablecerCEF(request, "cont_catcuentas.png", "CONT_CATCUENTAS");
    	  getSesion(request).getSesion("CONT_CATCUENTAS").setParametros("", "", "", JUtil.Elm(JUtil.Msj("CEF", "CONT_CATCUENTAS", "VISTA", "ENTIDADES"),1), "", "");
    	  getSesion(request).getSesion("CONT_CATCUENTAS").setOrden(p(request.getParameter("etq")),"");
    	  getSesion(request).getSesion("CONT_CATCUENTAS").setEspecial("");

          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_CATCUENTAS","CATC||||","");
          irApag("/forsetiweb/contabilidad/conta_catcuentas_vsta.jsp",request,response);
          return;
      }
      
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  String ent = JUtil.Msj("CEF", "CONT_CATCUENTAS", "VISTA", "ENTIDADES");
    	  if(request.getParameter("entidad").equals("GENERAL"))
    	  {
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setEntidad("",JUtil.Elm(ent,1));
    	  }
    	  else if(request.getParameter("entidad").equals("ACTIVO"))
    	  {
    		  String Entidad = "(Tipo = 'AC' OR Tipo = 'AF' OR Tipo = 'AD')";
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setEntidad(Entidad,JUtil.Elm(ent,2));
    	  }
    	  else if(request.getParameter("entidad").equals("PASIVO"))
    	  {
    		  String Entidad = "(Tipo = 'PC' OR Tipo = 'PL' OR Tipo = 'PD')";
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setEntidad(Entidad,JUtil.Elm(ent,3));
    	  }
    	  else if(request.getParameter("entidad").equals("CAPITAL"))
    	  {
    		  String Entidad = "(Tipo = 'CC')";
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setEntidad(Entidad,JUtil.Elm(ent,4));
    	  }
    	  else if(request.getParameter("entidad").equals("RESULTADOS"))
    	  {
    		  String Entidad = "(Tipo = 'RI' OR Tipo = 'RG' OR Tipo = 'RC' OR Tipo = 'RO' OR Tipo = 'IP')";
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setEntidad(Entidad,JUtil.Elm(ent,5));
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
    	  }
      }
      
      if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
      {
    	  String sts = JUtil.Msj("CEF", "CONT_CATCUENTAS", "VISTA", "STATUS");
    	  
    	  if(request.getParameter("status").equals("TODAS"))
    	  {
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setStatus("",JUtil.Elm(sts,1));
    	  }
    	  else if(request.getParameter("status").equals("ACTIVAS"))
    	  {
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setStatus("Estatus = 'A'",JUtil.Elm(sts,2));
    	  }
    	  else if(request.getParameter("status").equals("DESCONTINUADAS"))
    	  {
    		  getSesion(request).getSesion("CONT_CATCUENTAS").setStatus("Estatus = 'D'",JUtil.Elm(sts,3));
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 3); // 3 Error de Estatus
    	  }
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("CONT_CATCUENTAS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/contabilidad/conta_catcuentas_vsta.jsp", request, response);

    }

}
