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
package forseti.caja_y_bancos;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;
import forseti.sets.JBancosIdsSet;

@SuppressWarnings("serial")
public class JMovBancariosCtrl extends JForsetiApl
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

      String mov_bancarios = "";
      request.setAttribute("mov_bancarios",mov_bancarios);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(!getSesion(request).getPermiso("BANCAJ_BANCOS"))
      {
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN||||",mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("BANCAJ_BANCOS") == false)
      {
        JBancosIdsSet set = new JBancosIdsSet(request,usuario,"0","CEF-1");
        set.Open();

        if(set.getNumRows() < 1)
        {
          idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
        }

        String Entidad = "Tipo = '0' and Clave = '" + set.getAbsRow(0).getID() + "'";
        Calendar fecha = GregorianCalendar.getInstance();
        String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);

        getSesion(request).EstablecerCEF(request, "bancaj_bancos.png", "BANCAJ_BANCOS");
        getSesion(request).getSesion("BANCAJ_BANCOS").setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getCuenta(), JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3), JUtil.Elm(JUtil.Msj("CEF", "BANCAJ_BANCOS", "VISTA", "STATUS"),1));
        getSesion(request).getSesion("BANCAJ_BANCOS").setOrden(p(request.getParameter("etq")),"");
        getSesion(request).getSesion("BANCAJ_BANCOS").setEspecial(Integer.toString(set.getAbsRow(0).getID()));
        
    
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN||" + set.getAbsRow(0).getID() + "||","");
        irApag("/forsetiweb/caja_y_bancos/mov_bancarios_vsta.jsp",request,response);
        return;
      }

      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
        JBancosIdsSet set = new JBancosIdsSet(request,usuario,"0",request.getParameter("entidad"));
        set.Open();
        if(set.getNumRows() > 0)
        {
          String Entidad = "Tipo = '0' and Clave = '" + p(request.getParameter("entidad")) + "'";
          getSesion(request).getSesion("BANCAJ_BANCOS").setEntidad(Entidad,set.getAbsRow(0).getCuenta());
          getSesion(request).getSesion("BANCAJ_BANCOS").setEspecial(Integer.toString(set.getAbsRow(0).getID()));
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN||" + set.getAbsRow(0).getID() + "||","");
          
        }
        else
        {
           idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 1);
           RDP("CEF",getSesion(request).getConBD(),"IA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN||" + p(request.getParameter("entidad")) + "||",mensaje);
        }
      }
      if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  if(request.getParameter("tiempo").equals("HOY"))
    	  {
    		  Calendar fecha = GregorianCalendar.getInstance();
    		  String Tiempo = "date_part('day',Fecha) = " + JUtil.obtDia(fecha) + "  AND date_part('month',Fecha) = " + JUtil.obtMes(fecha) + " AND date_part('year',Fecha) = " + JUtil.obtAno(fecha);
    		  getSesion(request).getSesion("BANCAJ_BANCOS").setTiempo(Tiempo, JUtil.Msj("GLB", "GLB", "GLB", "HOY", 3));
    	  }
    	  else if(request.getParameter("tiempo").equals("SEM"))
    	  {
    		  Calendar hoy = GregorianCalendar.getInstance();
    		  hoy.add(Calendar.DATE,1);
    		  Calendar ini = GregorianCalendar.getInstance();
    		  ini.add(Calendar.DATE, -7);
    		  String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
    		  getSesion(request).getSesion("BANCAJ_BANCOS").setTiempo(Tiempo,JUtil.Msj("GLB", "GLB", "GLB", "SEMANA", 3));
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
    			  getSesion(request).getSesion("BANCAJ_BANCOS").setTiempo(Tiempo,TiempoTit);
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
    	  String sts = JUtil.Msj("CEF", "BANCAJ_BANCOS", "VISTA", "STATUS");
    	  
    	  if(request.getParameter("status").equals("TODOS"))
    	  {
    		  getSesion(request).getSesion("BANCAJ_BANCOS").setStatus("",JUtil.Elm(sts,1));
    	  }
    	  else if(request.getParameter("status").equals("GUARDADOS"))
    	  {
    		  getSesion(request).getSesion("BANCAJ_BANCOS").setStatus("Estatus = 'G'",JUtil.Elm(sts,2));
    	  }
    	  else if(request.getParameter("status").equals("TRANSITO"))
    	  {
    		  getSesion(request).getSesion("BANCAJ_BANCOS").setStatus("Estatus = 'T'",JUtil.Elm(sts,3));
    	  }
    	  else if(request.getParameter("status").equals("CANCELADOS"))
    	  {
    		  getSesion(request).getSesion("BANCAJ_BANCOS").setStatus("Estatus = 'C'",JUtil.Elm(sts,4));
    	  }
    	  else
    	  {
    		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 3); // 3 Error de Estatus
    	  }
      }
      
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("BANCAJ_BANCOS").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }

      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/caja_y_bancos/mov_bancarios_vsta.jsp", request, response);

    }

}
