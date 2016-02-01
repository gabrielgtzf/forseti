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
package forseti.almacen;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;
import forseti.sets.JAlmacenesMovimSetIdsV2;

@SuppressWarnings("serial")
public class JAlmUtensiliosCtrl extends JForsetiApl
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

      String alm_utensilios = "";
      request.setAttribute("alm_utensilios",alm_utensilios);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(!getSesion(request).getPermiso("ALM_UTENSILIOS"))
      {
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM||||",mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }


      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("ALM_UTENSILIOS") == false)
      {
        JAlmacenesMovimSetIdsV2 set = new JAlmacenesMovimSetIdsV2(request,usuario,"CEF-1","G");
        set.Open();

        if(set.getNumRows() < 1)
        {
          idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
        }

        String Entidad = "ID_Bodega = '" + set.getAbsRow(0).getID_Bodega() + "'";
        Calendar fecha = GregorianCalendar.getInstance();
        String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);

        getSesion(request).EstablecerCEF(request, "alm_utensilios.png", "ALM_UTENSILIOS");
        getSesion(request).getSesion("ALM_UTENSILIOS").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getNombre(), JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), JUtil.Elm(JUtil.Msj("CEF", "ALM_UTENSILIOS", "VISTA", "TIPOS"),1));
        getSesion(request).getSesion("ALM_UTENSILIOS").setOrden(p(request.getParameter("etq")),"");
        getSesion(request).getSesion("ALM_UTENSILIOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Bodega()));
        getSesion(request).getSesion("ALM_UTENSILIOS").setVista("MOVIMIENTOS");
        
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM||" + set.getAbsRow(0).getID_Bodega() + "||","");
        irApag("/forsetiweb/almacen/alm_utensilios_vsta.jsp",request,response);
        return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  JAlmacenesMovimSetIdsV2 set = new JAlmacenesMovimSetIdsV2(request,usuario,p(request.getParameter("entidad")),"G");
	      set.Open();
	        
	      if(set.getNumRows() > 0)
	      {
	    	  String Entidad = "ID_Bodega = '" + set.getAbsRow(0).getID_Bodega() + "'";
	    	  getSesion(request).getSesion("ALM_UTENSILIOS").setEntidad(Entidad,set.getAbsRow(0).getNombre());
	    	  getSesion(request).getSesion("ALM_UTENSILIOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Bodega()));
	      }
	      else
	      {
	    	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1);
	    	  RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM||" + p(request.getParameter("entidad")) + "||",mensaje);
	      }
    	  
      }
      if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  if(getSesion(request).getSesion("ALM_UTENSILIOS").getVista().equals("MOVIMIENTOS"))
    	  {
	    	  if(request.getParameter("tiempo").equals("HOY"))
	    	  {
	    		  Calendar fecha = GregorianCalendar.getInstance();
	    		  String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);
	    		  getSesion(request).getSesion("ALM_UTENSILIOS").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
	    	  }
	    	  else if(request.getParameter("tiempo").equals("SEM"))
	    	  {
	    		  Calendar hoy = GregorianCalendar.getInstance();
	    		  hoy.add(Calendar.DATE,1);
	    		  Calendar ini = GregorianCalendar.getInstance();
	    		  ini.add(Calendar.DATE, -7);
	    		  String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
	    		  getSesion(request).getSesion("ALM_UTENSILIOS").setTiempo(Tiempo,JUtil.Msj("GLB", "GLB", "GLB", "SEMANA", 3));
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
	    			  getSesion(request).getSesion("ALM_UTENSILIOS").setTiempo(Tiempo,TiempoTit);
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
    	  else
    	  {
    		  if(request.getParameter("tiempo").equals("TODO"))
    			  getSesion(request).getSesion("ALM_UTENSILIOS").setTiempo("", "****");
    		  else  
    			  getSesion(request).getSesion("ALM_UTENSILIOS").setTiempo("Descripcion ~~* '" + p(request.getParameter("tiempo")) + "%'", p(request.getParameter("tiempo")));
       	  }
      }
      else if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
      {
    	  if(!request.getParameter("status").equals(getSesion(request).getSesion("ALM_UTENSILIOS").getVista()))
          {
    		  if(request.getParameter("status").equals("MOVIMIENTOS"))
        	  {
    			  JAlmacenesMovimSetIdsV2 set = new JAlmacenesMovimSetIdsV2(request,usuario,"CEF-1","G");
    		      set.Open();

    		      if(set.getNumRows() < 1)
    		      {
    		    	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
    		    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
    		          return;
    		      }

    		      String Entidad = "ID_Bodega = '" + set.getAbsRow(0).getID_Bodega() + "'";
    		      Calendar fecha = GregorianCalendar.getInstance();
    		      String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);
    		      
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getNombre(), JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), JUtil.Elm(JUtil.Msj("CEF", "ALM_UTENSILIOS", "VISTA", "STATUS"),1));
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setOrden(p(request.getParameter("etq")),"");
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Bodega()));
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setVista("MOVIMIENTOS");
        	  }
    		  else
    		  {
    			  JAlmacenesMovimSetIdsV2 set = new JAlmacenesMovimSetIdsV2(request,usuario,"CEF-1","G");
    		      set.Open();

    		      if(set.getNumRows() < 1)
    		      {
    		    	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
    		    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
    		          return;
    		      }

    		      String Entidad = "ID_Bodega = '" + set.getAbsRow(0).getID_Bodega() + "'";
    		      String Tiempo = "Descripcion ~~* 'A%'";
    		       	 
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getNombre(), "A", JUtil.Elm(JUtil.Msj("CEF", "ALM_UTENSILIOS", "VISTA", "STATUS"),2));
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setOrden(p(request.getParameter("etq")),"");
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setEspecial(Integer.toString(set.getAbsRow(0).getID_Bodega()));
    		      getSesion(request).getSesion("ALM_UTENSILIOS").setVista("EXISTENCIAS");
    		  }
          }
    	          
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
       	  getSesion(request).getSesion("ALM_UTENSILIOS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }

      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/almacen/alm_utensilios_vsta.jsp", request, response);
      return;
    }

}
