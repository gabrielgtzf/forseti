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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;
import forseti.sets.JNominaEntidadesSetIds;

@SuppressWarnings("serial")
public class JNomPermisosCtrl extends JForsetiApl
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

        String nom_permisos = "";
        request.setAttribute("nom_permisos",nom_permisos);

        String mensaje = ""; short idmensaje = -1;
        String usuario = getSesion(request).getID_Usuario();

        if(!getSesion(request).getPermiso("NOM_PERMISOS"))
        {
        	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PERMISOS");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PERMISOS","NPER||||",mensaje);
            irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
            return;
        }

        // establece en la sesion que los mensajes se estan configurando por primera ocasion
        if(getSesion(request).getEst("NOM_PERMISOS") == false)
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
            Calendar fecha = GregorianCalendar.getInstance();
            String Tiempo = "date_part('day',ID_FechaMovimiento) = " + JUtil.obtDia(fecha) + "  AND date_part('month',ID_FechaMovimiento) = " + JUtil.obtMes(fecha) + " AND date_part('year',ID_FechaMovimiento) = " + JUtil.obtAno(fecha);

            getSesion(request).EstablecerCEF(request, "nom_permisos.png", "NOM_PERMISOS");
            getSesion(request).getSesion("NOM_PERMISOS").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getDescripcion(), JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), "");
            getSesion(request).getSesion("NOM_PERMISOS").setOrden(p(request.getParameter("etq")),"");
            getSesion(request).getSesion("NOM_PERMISOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Sucursal()));

            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PERMISOS","NPER||" + set.getAbsRow(0).getID_Sucursal() + "||",mensaje);
            irApag("/forsetiweb/nomina/nom_permisos_vsta.jsp",request,response);
            return;
        }
        
        if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
        {
      	  JNominaEntidadesSetIds set = new JNominaEntidadesSetIds(request,usuario,p(request.getParameter("entidad")));
            set.Open();
            if(set.getNumRows() > 0)
            {
          	  String Entidad = "ID_Sucursal = '" + set.getAbsRow(0).getID_Sucursal() + "'";
          	  getSesion(request).getSesion("NOM_PERMISOS").setEntidad(Entidad,set.getAbsRow(0).getDescripcion());
          	  getSesion(request).getSesion("NOM_PERMISOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Sucursal()));
            }
            else
            {
          	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
          	  RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"NOM_PERMISOS","NPER||" + p(request.getParameter("entidad")) + "||",mensaje);
            }

        }
        else if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
        {
    	  if(request.getParameter("tiempo").equals("HOY"))
    	  {
    		  Calendar fecha = GregorianCalendar.getInstance();
    		  String Tiempo = "date_part('day',ID_FechaMovimiento) = " + JUtil.obtDia(fecha) + "  AND date_part('month',ID_FechaMovimiento) = " + JUtil.obtMes(fecha) + " AND date_part('year',ID_FechaMovimiento) = " + JUtil.obtAno(fecha);
    		  getSesion(request).getSesion("NOM_PERMISOS").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
    	  }
    	  else if(request.getParameter("tiempo").equals("SEM"))
    	  {
    		  Calendar hoy = GregorianCalendar.getInstance();
    		  hoy.add(Calendar.DATE,1);
    		  Calendar ini = GregorianCalendar.getInstance();
    		  ini.add(Calendar.DATE, -7);
    		  String Tiempo = "ID_FechaMovimiento BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
    		  getSesion(request).getSesion("NOM_PERMISOS").setTiempo(Tiempo,JUtil.Msj("GLB", "GLB", "GLB", "SEMANA", 3));
    	  }
    	  else if(request.getParameter("tiempo").equals("MAS"))
    	  {
    		  if(request.getParameter("mes") != null && request.getParameter("ano") != null && !request.getParameter("mes").equals("") && !request.getParameter("ano").equals(""))
    		  {
    			  int mes;
    			  int ano;

    			  try 
    			  { 
    				  mes = Integer.parseInt(request.getParameter("mes"));
    			  }
    			  catch(NumberFormatException e) 
    			  {
    				  mes = JUtil.obtMes(GregorianCalendar.getInstance()); 
    				  ano = JUtil.obtAno(GregorianCalendar.getInstance());
    			  }
    			  
    			  try
    			  { 
    				  ano = Integer.parseInt(request.getParameter("ano"));
    			  }
    			  catch(NumberFormatException e) 
    			  {
    				  mes = JUtil.obtMes(GregorianCalendar.getInstance()); 
    				  ano = JUtil.obtAno(GregorianCalendar.getInstance());
    			  }

    			  String TiempoTit = JUtil.convertirMesCorto(mes) + " " + ano;

    			  String Tiempo = "date_part('month',ID_FechaMovimiento) = " + mes + " AND date_part('year',ID_FechaMovimiento) = " + ano;
    			  getSesion(request).getSesion("NOM_PERMISOS").setTiempo(Tiempo,TiempoTit);
    		  }
    		  else
    		  {
    			  idmensaje = 1;  mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 2); // 1 Error de rango
    		  }
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 2); // 2 Error de rango
    	  }
        }
        
        if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
        {
      	  getSesion(request).getSesion("NOM_PERMISOS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
        }
        
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/nomina/nom_permisos_vsta.jsp", request, response);



    }

}
