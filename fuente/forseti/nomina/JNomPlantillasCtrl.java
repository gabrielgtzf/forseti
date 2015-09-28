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

@SuppressWarnings("serial")
public class JNomPlantillasCtrl extends JForsetiApl
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

        String nom_plantillas = "";
        request.setAttribute("nom_plantillas",nom_plantillas);

        String mensaje = ""; short idmensaje = -1;
       
        if(!getSesion(request).getPermiso("NOM_PLANTILLAS"))
        {
          idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PLANTILLAS");
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PLANTILLAS","NPLN||||",mensaje);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
        }

        // establece en la sesion que los mensajes se estan configurando por primera ocasion
        if(getSesion(request).getEst("NOM_PLANTILLAS") == false)
        { 
        	 Calendar fecha = GregorianCalendar.getInstance();
             String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);

             getSesion(request).EstablecerCEF(request, "nom_plantillas.png", "NOM_PLANTILLAS");
             getSesion(request).getSesion("NOM_PLANTILLAS").setParametros("", Tiempo, "", JUtil.Elm(JUtil.Msj("CEF", "NOM_PLANTILLAS", "VISTA", "ENTIDADES"),1), JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), "");
             getSesion(request).getSesion("NOM_PLANTILLAS").setOrden(p(request.getParameter("etq")),"Fecha");
             
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
	         RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"NOM_PLANTILLAS","NPLN||||","");
	         irApag("/forsetiweb/nomina/nom_plantillas_vsta.jsp",request,response);
             return;
        }
      
        if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
        {
      	  String ent = JUtil.Msj("CEF", "NOM_PLANTILLAS", "VISTA", "ENTIDADES");
      	  if(request.getParameter("entidad").equals("GEN"))
      	  {
      		  getSesion(request).getSesion("NOM_PLANTILLAS").setEntidad("",JUtil.Elm(ent,1));
      	  }
      	  else if(request.getParameter("entidad").equals("EMP"))
      	  {
      		  String Entidad = "bID_Empleado = '1'";
      		  getSesion(request).getSesion("NOM_PLANTILLAS").setEntidad(Entidad,JUtil.Elm(ent,2));
      	  }
      	  else if(request.getParameter("entidad").equals("NUM"))
      	  {
      		  String Entidad = "bNomina = '1'";
      		  getSesion(request).getSesion("NOM_PLANTILLAS").setEntidad(Entidad,JUtil.Elm(ent,3));
      	  }
      	  else if(request.getParameter("entidad").equals("TIPO"))
      	  {
      		  String Entidad = "bTipo_Nomina = '1'";
      		  getSesion(request).getSesion("NOM_PLANTILLAS").setEntidad(Entidad,JUtil.Elm(ent,4));
      	  }
      	  else if(request.getParameter("entidad").equals("NOM"))
      	  {
      		  String Entidad = "bCompania_Sucursal = '1'";
      		  getSesion(request).getSesion("NOM_PLANTILLAS").setEntidad(Entidad,JUtil.Elm(ent,5));
      	  }
      	  else
      	  {
      		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
      	  }
        }
        else if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
        {
    	  if(request.getParameter("tiempo").equals("HOY"))
    	  {
    		  Calendar fecha = GregorianCalendar.getInstance();
    		  String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);
    		  getSesion(request).getSesion("NOM_PLANTILLAS").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
    	  }
    	  else if(request.getParameter("tiempo").equals("SEM"))
    	  {
    		  Calendar hoy = GregorianCalendar.getInstance();
    		  hoy.add(Calendar.DATE,1);
    		  Calendar ini = GregorianCalendar.getInstance();
    		  ini.add(Calendar.DATE, -7);
    		  String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
    		  getSesion(request).getSesion("NOM_PLANTILLAS").setTiempo(Tiempo,JUtil.Msj("GLB", "GLB", "GLB", "SEMANA", 3));
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

    			  String Tiempo = "date_part('month',Fecha) = " + mes + " AND date_part('year',Fecha) = " + ano;
    			  getSesion(request).getSesion("NOM_PLANTILLAS").setTiempo(Tiempo,TiempoTit);
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
      	  getSesion(request).getSesion("NOM_PLANTILLAS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
        }
      
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/nomina/nom_plantillas_vsta.jsp", request, response);

    }

}
