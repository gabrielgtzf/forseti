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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JAdmCFDCtrl extends JForsetiApl
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

      String adm_cfdi = "";
      request.setAttribute("adm_cfdi",adm_cfdi);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADM_CFDI"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_CFDI");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_CFDI","ACFD||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("ADM_CFDI") == false)
      {
    	  getSesion(request).EstablecerCEF(request, "adm_cfdi.png", "ADM_CFDI");
    	  getSesion(request).getSesion("ADM_CFDI").setParametros("", "", "", JUtil.Elm(JUtil.Msj("CEF", "ADM_CFDI", "VISTA", "ENTIDADES"),1), "", "");
    	  getSesion(request).getSesion("ADM_CFDI").setOrden(p(request.getParameter("etq")),"");
    	  getSesion(request).getSesion("ADM_CFDI").setEspecial("EMISOR");
	    
    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ADM_CFDI","ACFD||||","");
    	  irApag("/forsetiweb/administracion/adm_cfd_vsta.jsp",request,response);
    	  return;
      }
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  if(!request.getParameter("entidad").equals(getSesion(request).getSesion("ADM_CFDI").getEspecial())) // Ha cambiado la entidad de procesos
          {
          		getSesion(request).getSesion("ADM_CFDI").setOrden(null,"");
          		if(!request.getParameter("entidad").equals("CFDI"))
          		{
          			getSesion(request).getSesion("ADM_CFDI").setTiempo("","");
          			getSesion(request).getSesion("ADM_CFDI").setStatus("","");
          			getSesion(request).getSesion("ADM_CFDI").setPanelTiempo("");
          			getSesion(request).getSesion("ADM_CFDI").setPanelStatus("");
          		}
          		else
          		{
          		 	String sts = JUtil.Msj("CEF", "ADM_CFDI", "VISTA", "RANGO");
          		 	//String tmp = JUtil.Msj("CEF", "ADM_CFDI", "VISTA", "RANGO");
          			getSesion(request).getSesion("ADM_CFDI").setStatus("COMPRAS",JUtil.Elm(sts, 1));
          			getSesion(request).getSesion("ADM_CFDI").setTiempo("","");
          			getSesion(request).getSesion("ADM_CFDI").setPanelStatus(JUtil.Msj("CEF", "ADM_CFDI", "VISTA", "RANGO", 2));
          			getSesion(request).getSesion("ADM_CFDI").setPanelTiempo(JUtil.Msj("CEF", "ADM_CFDI", "VISTA", "RANGO", 3));
          		}
          }
    	  String ent = JUtil.Msj("CEF", "ADM_CFDI", "VISTA", "ENTIDADES");
    	  
    	  if(request.getParameter("entidad").equals("EMISOR"))
    	  {
    		  getSesion(request).getSesion("ADM_CFDI").setEntidad("",JUtil.Elm(ent,1));
    		  getSesion(request).getSesion("ADM_CFDI").setEspecial("EMISOR");
    	  }
    	  else if(request.getParameter("entidad").equals("CERTIFICADOS"))
    	  {
    		  getSesion(request).getSesion("ADM_CFDI").setEntidad("",JUtil.Elm(ent,2));
    		  getSesion(request).getSesion("ADM_CFDI").setEspecial("CERTIFICADOS");
    	  }
    	  else if(request.getParameter("entidad").equals("EXPEDICION"))
    	  {
    		  getSesion(request).getSesion("ADM_CFDI").setEntidad("",JUtil.Elm(ent,3));
    		  getSesion(request).getSesion("ADM_CFDI").setEspecial("EXPEDICION");
    	  }
    	  else if(request.getParameter("entidad").equals("CECAT"))
    	  {
    		  getSesion(request).getSesion("ADM_CFDI").setEntidad("",JUtil.Elm(ent,4));
    		  getSesion(request).getSesion("ADM_CFDI").setEspecial("CECAT");
    	  }
    	  else if(request.getParameter("entidad").equals("CEBAL"))
    	  {
    		  getSesion(request).getSesion("ADM_CFDI").setEntidad("",JUtil.Elm(ent,5));
    		  getSesion(request).getSesion("ADM_CFDI").setEspecial("CEBAL");
    	  }
    	  else if(request.getParameter("entidad").equals("CEPOL"))
    	  {
    		  getSesion(request).getSesion("ADM_CFDI").setEntidad("",JUtil.Elm(ent,6));
    		  getSesion(request).getSesion("ADM_CFDI").setEspecial("CEPOL");
    	  }
    	  else if(request.getParameter("entidad").equals("CFDI"))
    	  {
    		  getSesion(request).getSesion("ADM_CFDI").setEntidad("",JUtil.Elm(ent,7));
    		  getSesion(request).getSesion("ADM_CFDI").setEspecial("CFDI");
    	  }
    	  else
     	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1); // 1 Error de entidad
     	  }
          
       }
       else if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
       {
    	  String sts = JUtil.Msj("CEF", "ADM_CFDI", "VISTA", "RANGO");
          if(request.getParameter("status").equals("COMPRAS"))
        	  getSesion(request).getSesion("ADM_CFDI").setStatus("COMPRAS",JUtil.Elm(sts, 1));
          else if(request.getParameter("status").equals("OTROS"))
        	  getSesion(request).getSesion("ADM_CFDI").setStatus("OTROS",JUtil.Elm(sts,2));
          else if(request.getParameter("status").equals("VENTAS"))
        	  getSesion(request).getSesion("ADM_CFDI").setStatus("VENTAS",JUtil.Elm(sts,3));
          else if(request.getParameter("status").equals("NOMINA"))
        	  getSesion(request).getSesion("ADM_CFDI").setStatus("NOMINA",JUtil.Elm(sts,4));
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
     		  getSesion(request).getSesion("ADM_CFDI").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
     	  }
     	  else if(request.getParameter("tiempo").equals("SEM"))
     	  {
     		  Calendar hoy = GregorianCalendar.getInstance();
     		  hoy.add(Calendar.DATE,1);
     		  Calendar ini = GregorianCalendar.getInstance();
     		  ini.add(Calendar.DATE, -7);
     		  String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
     		  getSesion(request).getSesion("ADM_CFDI").setTiempo(Tiempo,JUtil.Msj("GLB", "GLB", "GLB", "SEMANA", 3));
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
     			  getSesion(request).getSesion("ADM_CFDI").setTiempo(Tiempo,TiempoTit);
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
    	   getSesion(request).getSesion("ADM_CFDI").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
       }
        
       getSesion(request).setID_Mensaje(idmensaje, mensaje);
       irApag("/forsetiweb/administracion/adm_cfd_vsta.jsp", request, response);

    }

}
