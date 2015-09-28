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

public class JRegistAdminCtrl extends JFsiForsetiApl
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

      String regist_admin = "";
      request.setAttribute("regist_admin",regist_admin);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("REGIST_ADMIN"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REGIST_ADMIN");
    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REGIST_ADMIN","RADM||||",mensaje);
    	  irApag("/forsetiadmin/caja_mensajes_vsta.jsp", request, response);
    	  return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("REGIST_ADMIN") == false)
      {
    	  String Entidad = "ACT";
          Calendar fecha = GregorianCalendar.getInstance();
          int mes = JUtil.obtMes(fecha);
          int dia = JUtil.obtDia(fecha);
          String Tiempo = JUtil.obtAno(fecha) + "-" + (mes > 9 ? mes : "0" + mes) + "-" + (dia > 9 ? dia : "0" + dia);
		  
    	  getSesion(request).EstablecerSAF("regist_admin.png", "REGIST_ADMIN");
    	  getSesion(request).getSesion("REGIST_ADMIN").setParametros(Entidad, Tiempo, "", JUtil.Elm(JUtil.Msj("SAF", "REGIST_ADMIN", "VISTA", "ENTIDAD"),1), JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), "");
    	  getSesion(request).getSesion("REGIST_ADMIN").setOrden(p(request.getParameter("etq")),"");
    	  getSesion(request).getSesion("REGIST_ADMIN").setEspecial(Entidad);

          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("SAF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"REGIST_ADMIN","RADM||||","");
          irApag("/forsetiadmin/registros/regist_admin_vsta.jsp",request,response);
          return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  String ent = JUtil.Msj("SAF", "REGIST_ADMIN", "VISTA", "ENTIDAD");
    	  String Entidad = request.getParameter("entidad");
    	  if(Entidad.equals("ACT"))
    	  {
    		  getSesion(request).getSesion("REGIST_ADMIN").setEntidad("ACT", JUtil.Elm(ent, 1));
    		  getSesion(request).getSesion("REGIST_ADMIN").setEspecial(Entidad);
    	  }
    	  else if(Entidad.equals("RESP"))
    	  {
    		  getSesion(request).getSesion("REGIST_ADMIN").setEntidad("RESP-\\w+_\\w+", JUtil.Elm(ent, 2));
    		  getSesion(request).getSesion("REGIST_ADMIN").setEspecial(Entidad);
    	  }
    	  else if(Entidad.equals("REST"))
    	  {
    		  getSesion(request).getSesion("REGIST_ADMIN").setEntidad("REST-\\w+_\\w+", JUtil.Elm(ent, 3));
    		  getSesion(request).getSesion("REGIST_ADMIN").setEspecial(Entidad);
    	  }
    	  else if(Entidad.equals("SLDS"))
    	  {
    		  getSesion(request).getSesion("REGIST_ADMIN").setEntidad("SLDS", JUtil.Elm(ent, 4));
    		  getSesion(request).getSesion("REGIST_ADMIN").setEspecial(Entidad);
    	  }
    	  else if(Entidad.equals("CREAR"))
    	  {
    		  getSesion(request).getSesion("REGIST_ADMIN").setEntidad("CREAR-\\w+_\\w+", JUtil.Elm(ent, 5));
    		  getSesion(request).getSesion("REGIST_ADMIN").setEspecial(Entidad);
    	  }
    	  else if(Entidad.equals("ELIM"))
    	  {
    		  getSesion(request).getSesion("REGIST_ADMIN").setEntidad("ELIM-\\w+_\\w+", JUtil.Elm(ent, 6));
    		  getSesion(request).getSesion("REGIST_ADMIN").setEspecial(Entidad);
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
    		  int mes = JUtil.obtMes(fecha);
    		  int dia = JUtil.obtDia(fecha);
              String Tiempo = JUtil.obtAno(fecha) + "-" + (mes > 9 ? mes : "0" + mes) + "-" + (dia > 9 ? dia : "0" + dia);
    		  getSesion(request).getSesion("REGIST_ADMIN").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
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
    			  String Tiempo = ano + "-" + (mes > 9 ? mes : "0" + mes); //Regular expression
    			  getSesion(request).getSesion("REGIST_ADMIN").setTiempo(Tiempo,TiempoTit);
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
        
      /*
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("REGIST_ADMIN").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      */
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiadmin/registros/regist_admin_vsta.jsp", request, response);

    }

}