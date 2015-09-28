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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;
import forseti.sets.JContaPolizasClasificacionesSet;

@SuppressWarnings("serial")
public class JContaPolizasCtrl extends JForsetiApl
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

      String conta_polizas = "";
      request.setAttribute("conta_polizas",conta_polizas);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("CONT_POLIZAS"))
      {
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLIZAS");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLIZAS","POLZ||||",mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("CONT_POLIZAS") == false)
      {
       	  JContaPolizasClasificacionesSet set = new JContaPolizasClasificacionesSet(request);
          set.m_OrderBy = "ID_Clasificacion ASC LIMIT 1 ";
          //System.out.println(set.getSQL());
          set.Open();
          
          if(set.getNumRows() < 1)
          {
        	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        	  return;
          }

          String Entidad = "ID_Clasificacion = '" + p(set.getAbsRow(0).getID_Clasificacion()) + "'";
          Calendar fecha = GregorianCalendar.getInstance();
          String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);

    	  getSesion(request).EstablecerCEF(request, "cont_polizas.png", "CONT_POLIZAS");
    	  getSesion(request).getSesion("CONT_POLIZAS").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getDescripcion(), JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), JUtil.Elm(JUtil.Msj("CEF", "CONT_POLIZAS", "VISTA", "STATUS"),1));
    	  getSesion(request).getSesion("CONT_POLIZAS").setOrden(p(request.getParameter("etq")),"");
    	  getSesion(request).getSesion("CONT_POLIZAS").setEspecial(p(set.getAbsRow(0).getID_Clasificacion()));

          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_POLIZAS","POLZ||" + set.getAbsRow(0).getID_Clasificacion() + "||","");
          irApag("/forsetiweb/contabilidad/conta_polizas_vsta.jsp",request,response);
          return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  JContaPolizasClasificacionesSet set = new JContaPolizasClasificacionesSet(request);
    	  set.m_Where = "ID_Clasificacion = '" + p(request.getParameter("entidad")) + "'";
    	  set.Open();
    	  if(set.getNumRows() > 0)
    	  {
    		  String Entidad = "ID_Clasificacion = '" + p(set.getAbsRow(0).getID_Clasificacion()) + "'";
    		  getSesion(request).getSesion("CONT_POLIZAS").setEntidad(Entidad,set.getAbsRow(0).getDescripcion());
    		  getSesion(request).getSesion("CONT_POLIZAS").setEspecial(p(set.getAbsRow(0).getID_Clasificacion()));
    		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_POLIZAS","POLZ||" + p(set.getAbsRow(0).getID_Clasificacion()) + "||","");
          }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
    		  RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"CONT_POLIZAS","POLZ||" + p(request.getParameter("entidad")) + "||",mensaje);
          }
      }
      
      if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  if(request.getParameter("tiempo").equals("HOY"))
    	  {
    		  Calendar fecha = GregorianCalendar.getInstance();
    		  String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);
    		  getSesion(request).getSesion("CONT_POLIZAS").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
    	  }
    	  else if(request.getParameter("tiempo").equals("SEM"))
    	  {
    		  Calendar hoy = GregorianCalendar.getInstance();
    		  hoy.add(Calendar.DATE,1);
    		  Calendar ini = GregorianCalendar.getInstance();
    		  ini.add(Calendar.DATE, -7);
    		  String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
    		  getSesion(request).getSesion("CONT_POLIZAS").setTiempo(Tiempo,JUtil.Msj("GLB", "GLB", "GLB", "SEMANA", 3));
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
    			  getSesion(request).getSesion("CONT_POLIZAS").setTiempo(Tiempo,TiempoTit);
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
      
      if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
      {
    	  String sts = JUtil.Msj("CEF", "CONT_POLIZAS", "VISTA", "STATUS");
    	  
    	  if(request.getParameter("status").equals("TODAS"))
    	  {
    		  getSesion(request).getSesion("CONT_POLIZAS").setStatus("",JUtil.Elm(sts,1));
    	  }
    	  else if(request.getParameter("status").equals("GUARDADAS"))
    	  {
    		  getSesion(request).getSesion("CONT_POLIZAS").setStatus("Estatus = 'G'",JUtil.Elm(sts,2));
    	  }
    	  else if(request.getParameter("status").equals("CANCELADAS"))
    	  {
    		  getSesion(request).getSesion("CONT_POLIZAS").setStatus("Estatus = 'C'",JUtil.Elm(sts,3));
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 3); // 3 Error de Estatus
    	  }
      }
       
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("CONT_POLIZAS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }

      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/contabilidad/conta_polizas_vsta.jsp", request, response);

    }

}
