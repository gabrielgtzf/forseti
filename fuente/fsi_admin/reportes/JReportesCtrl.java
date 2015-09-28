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
package fsi_admin.reportes;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fsi_admin.JFsiForsetiApl;
import forseti.sets.JUsuariosPermisosCatalogoSet;
import forseti.sets.JUsuariosSubmoduloReportes;
                                                                                                                                                        
public class JReportesCtrl extends JFsiForsetiApl
{
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);
      
      String reps_reportes = "";
      request.setAttribute("reps_reportes",reps_reportes);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();
 
      // establece en la sesion que los reportes se estan configurando por primera ocasion
      if(!getSesion(request).getPermiso("REPS_REPORTES"))
      {
    	
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REPS_REPORTES");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REPS_REPORTES","RPRP||||",mensaje);
        irApag("/forsetiadmin/caja_mensajes_vsta.jsp", request, response);
        return;
      }
      
      if(getSesion(request).getEst("REPS_REPORTES") == false)
      {
        //primero establece los permisos
        JUsuariosSubmoduloReportes setUsr = new JUsuariosSubmoduloReportes(request,usuario);
        //System.out.println(usuario);
        setUsr.ConCat(true);
        setUsr.Open();
        getSesion(request).setPermisosReportes(setUsr);

        String modulo;
        JUsuariosPermisosCatalogoSet mod = new JUsuariosPermisosCatalogoSet(request);
        mod.ConCat(true);
        mod.m_Where = "ID_Permiso = '" + p(request.getParameter("tipo")) + "'";
        mod.Open();
        if(mod.getNumRows() < 1)
        	modulo = "";
        else
        	modulo = mod.getAbsRow(0).getModulo();
        
        getSesion(request).EstablecerSAF("rep_reportes.png", "REPS_REPORTES");
        getSesion(request).getSesion("REPS_REPORTES").setParametros(p(request.getParameter("tipo")), "", "", modulo, "", "");
        getSesion(request).getSesion("REPS_REPORTES").setOrden(p(request.getParameter("etq")),"");
        getSesion(request).getSesion("REPS_REPORTES").setEspecial("");
  	  
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("SAF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"REPS_REPORTES","RPRP||" + p(request.getParameter("tipo")) + "||","");
        irApag("/forsetiadmin/reportes/rep_reportes_vsta.jsp",request,response);
        return;
      }

      if(request.getParameter("tipo") != null && !request.getParameter("tipo").equals(""))
      {
        String modulo;
        JUsuariosPermisosCatalogoSet mod = new JUsuariosPermisosCatalogoSet(request);
        mod.ConCat(true);
        mod.m_Where = "ID_Permiso = '" + p(request.getParameter("tipo")) + "'";
        mod.Open();
        if(mod.getNumRows() < 1)
        	modulo = "";
        else
        	modulo = mod.getAbsRow(0).getModulo();
        
        getSesion(request).getSesion("REPS_REPORTES").setEntidad(p(request.getParameter("tipo")), modulo);
    	//request.setAttribute("actent", "");
      }
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiadmin/reportes/rep_reportes_vsta.jsp", request, response);

    }

}