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
package fsi_admin.regist;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fsi_admin.JFsiForsetiApl;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JRegistProcCtrl extends JFsiForsetiApl
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

      String regist_proc = "";
      request.setAttribute("regist_proc",regist_proc);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("REGIST_PROC"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REGIST_PROC");
    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REGIST_PROC","RPRC||||",mensaje);
    	  irApag("/forsetiadmin/caja_mensajes_vsta.jsp", request, response);
    	  return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("REGIST_PROC") == false)
      {
       	  String Entidad = "ID_Tipo = 'SAF'";
          Calendar fecha = GregorianCalendar.getInstance();
          String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);

    	  getSesion(request).EstablecerSAF("regist_proc.png", "REGIST_PROC");
    	  getSesion(request).getSesion("REGIST_PROC").setParametros(Entidad, Tiempo, "", "SAF", JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), JUtil.Elm(JUtil.Msj("SAF", "REGIST_PROC", "VISTA", "STATUS"),1));
    	  getSesion(request).getSesion("REGIST_PROC").setOrden(p(request.getParameter("etq")),"");
    	  getSesion(request).getSesion("REGIST_PROC").setEspecial(Entidad);

          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("SAF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"REGIST_PROC","RPRC||||","");
          irApag("/forsetiadmin/registros/regist_proc_vsta.jsp",request,response);
          return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  if(request.getParameter("entidad").equals("SAF") || request.getParameter("entidad").equals("CEF") || request.getParameter("entidad").equals("REF"))
    	  {
    		  String Entidad = "ID_Tipo = '" + p(request.getParameter("entidad")) + "'";
    		  getSesion(request).getSesion("REGIST_PROC").setEntidad(Entidad, p(request.getParameter("entidad")));
    		  getSesion(request).getSesion("REGIST_PROC").setEspecial(Entidad);
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
    	  }
      }
      
      if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  if(request.getParameter("tiempo").equals("HOY"))
    	  {
    		  Calendar fecha = GregorianCalendar.getInstance();
    		  String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);
    		  getSesion(request).getSesion("REGIST_PROC").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
    	  }
    	  else if(request.getParameter("tiempo").equals("SEM"))
    	  {
    		  Calendar hoy = GregorianCalendar.getInstance();
    		  hoy.add(Calendar.DATE,1);
    		  Calendar ini = GregorianCalendar.getInstance();
    		  ini.add(Calendar.DATE, -7);
    		  String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
    		  getSesion(request).getSesion("REGIST_PROC").setTiempo(Tiempo,JUtil.Msj("GLB", "GLB", "GLB", "SEMANA", 3));
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
    			  getSesion(request).getSesion("REGIST_PROC").setTiempo(Tiempo,TiempoTit);
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
    	  String sts = JUtil.Msj("SAF", "REGIST_PROC", "VISTA", "STATUS");
    	  
    	  if(request.getParameter("status").equals("TODOS"))
    	  {
    		  getSesion(request).getSesion("REGIST_PROC").setStatus("",JUtil.Elm(sts,1));
    	  }
    	  else if(request.getParameter("status").equals("NORMALES"))
    	  {
    		  getSesion(request).getSesion("REGIST_PROC").setStatus("Status = 'OK'",JUtil.Elm(sts,2));
    	  }
    	  else if(request.getParameter("status").equals("ERRONEOS"))
    	  {
    		  getSesion(request).getSesion("REGIST_PROC").setStatus("Status = 'ER'",JUtil.Elm(sts,3));
    	  }
    	  else if(request.getParameter("status").equals("NOAUT"))
    	  {
    		  getSesion(request).getSesion("REGIST_PROC").setStatus("Status = 'NA'",JUtil.Elm(sts,4));
    	  }
    	  else if(request.getParameter("status").equals("ALERTAS"))
    	  {
    		  getSesion(request).getSesion("REGIST_PROC").setStatus("Status = 'AL'",JUtil.Elm(sts,5));
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 3); // 3 Error de Estatus
    	  }
      }
       
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("REGIST_PROC").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }

      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiadmin/registros/regist_proc_vsta.jsp", request, response);

    }

}