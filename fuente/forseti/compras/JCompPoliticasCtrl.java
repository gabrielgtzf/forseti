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
package forseti.compras;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
//import forseti.sets.JComprasEntidadesSetIdsV2;
import forseti.JUtil;
import forseti.sets.JPublicInvServLineasSetV2;

@SuppressWarnings("serial")
public class JCompPoliticasCtrl extends JForsetiApl
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
    	      
    	String comp_pol = "";
    	request.setAttribute("comp_pol", comp_pol);

    	String mensaje = ""; short idmensaje = -1;
    	String usuario = getSesion(request).getID_Usuario();

    	if(!getSesion(request).getPermiso("COMP_POL"))
    	{
    		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_POL");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_POL","CPOL||||",mensaje);
    		irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
    		return;
    	}

    	if(getSesion(request).getEst("COMP_POL") == false)
    	{
    		JPublicInvServLineasSetV2 set = new JPublicInvServLineasSetV2(request);
    		set.m_OrderBy = "Clave ASC Limit 1";
    		set.m_Where = "ID_InvServ = 'P'"; // P es de tipo productos
    		set.Open();

    		if(set.getNumRows() < 1)
    		{
    			idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
    			return;
    		}
    	          
    		String Entidad = "ID_Tipo = 'P' and Linea = '" + p(set.getAbsRow(0).getClave()) + "' and Status = 'V' and SeProduce = '0'";
    		String Tiempo = "Descripcion ~~* 'A%'";
    	          
    		getSesion(request).EstablecerCEF(request, "comp_pol.png", "COMP_POL");
    		getSesion(request).getSesion("COMP_POL").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getDescripcion(), "A", JUtil.Elm(JUtil.Msj("CEF", "COMP_POL", "VISTA", "STATUS", 2),1));
    		getSesion(request).getSesion("COMP_POL").setOrden(p(request.getParameter("etq")),"");
    		getSesion(request).getSesion("COMP_POL").setEspecial("PRODUCTOS");
    		
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		irApag("/forsetiweb/compras/comp_pol_vsta.jsp",request,response);
    		return;
    	}

    	if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
    	{
    		if(request.getParameter("tiempo").equals("TODO"))
    			getSesion(request).getSesion("COMP_POL").setTiempo("", "****");
    		else  
    			getSesion(request).getSesion("COMP_POL").setTiempo("Descripcion ~~* '" + p(request.getParameter("tiempo")) + "%'", p(request.getParameter("tiempo")));
    	}
    	else if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
    	{
    		JPublicInvServLineasSetV2 set = new JPublicInvServLineasSetV2(request);
    		set.m_Where = "ID_InvServ = 'P' and Clave = '" + p(request.getParameter("entidad")) + "'"; // P es de tipo productos
    		set.Open();

    		if(set.getNumRows() > 0)
    		{
    			String Entidad = "ID_Tipo = 'P' and Linea = '" + p(request.getParameter("entidad")) + "' and Status = 'V' and SeProduce = '0'";
    			getSesion(request).getSesion("COMP_POL").setEntidad(Entidad,set.getAbsRow(0).getDescripcion());
    		}
    		else
    		{
    			idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
    			RDP("CEF",getSesion(request).getConBD(),"IA",usuario,"COMP_POL","CPOL||" + p(request.getParameter("entidad")) + "||",mensaje);
    		}
    			
    	}
    	else if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
    	{
    		getSesion(request).getSesion("COMP_POL").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
    	}

    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	irApag("/forsetiweb/compras/comp_pol_vsta.jsp", request, response);

    }


}
