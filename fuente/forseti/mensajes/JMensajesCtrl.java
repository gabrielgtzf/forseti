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
package forseti.mensajes;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JMensajesCtrl extends JForsetiApl
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
      
      String mensajes = "";
      request.setAttribute("mensajes",mensajes);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("MENSAJES") == false)
      {

        String Entidad = "ID_Destino = '" + p(usuario) + "'";
        //Calendar fecha = GregorianCalendar.getInstance();
        //String Tiempo = "Day(Fecha) = " + JUtil.obtDia(fecha) + "  AND Month(Fecha) = " + JUtil.obtMes(fecha) + " AND Year(Fecha) = " + JUtil.obtAno(fecha);
        Calendar hoy = GregorianCalendar.getInstance();
        hoy.add(Calendar.DATE,1);
        Calendar ini = GregorianCalendar.getInstance();
        ini.add(Calendar.DATE, -7);
        String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";

        getSesion(request).EstablecerCEF(request, "MENSAJES","");
        getSesion(request).getSesion("MENSAJES").setParametros(Entidad, Tiempo, "Leido = 0", "PARA MI", "DE LA ULTIMA SEMANA", "SIN LEER");
        getSesion(request).getSesion("Mensajes").setOrden(p(request.getParameter("ordenetq")),"Fecha");
        getSesion(request).getSesion("MENSAJES").setEspecial("PARA_MI");

        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/mensajes/mensajes_vsta.jsp",request,response);
        return;
      }
/*
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
        if(request.getParameter("entidad").equals("PARA_MI"))
        {
          String Entidad = "ID_Destino = '" + usuario + "'";
          getSesion(request).getSesionMensajes().setEntidad(Entidad,"PARA MI");
          getSesion(request).getSesionMensajes().setEspecial("PARA_MI");
        }
        else if(request.getParameter("entidad").equals("MIOS"))
        {
          String Entidad = "ID_Origen = '" + usuario + "'";
          getSesion(request).getSesionMensajes().setEntidad(Entidad,"MIOS");
          getSesion(request).getSesionMensajes().setEspecial("MIOS");
        }
        else if(request.getParameter("entidad").equals("SISTEMA"))
        {
          String Entidad = "ID_Origen is null";
          getSesion(request).getSesionMensajes().setEntidad(Entidad,"DEL SISTEMA");
          getSesion(request).getSesionMensajes().setEspecial("SISTEMA");
        }
        else
        {
           idmensaje = 1; mensaje += " El par�metro de entidad no es v�lido, debe ser SISTEMA, PARA_MI o MIOS<br>";
        }
      }
      else if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
        if(request.getParameter("tiempo").equals("HOY"))
        {
          Calendar fecha = GregorianCalendar.getInstance();
          String Tiempo = "Day(Fecha) = " + JUtil.obtDia(fecha) + "  AND Month(Fecha) = " + JUtil.obtMes(fecha) + " AND Year(Fecha) = " + JUtil.obtAno(fecha);
          getSesion(request).getSesionMensajes().setTiempo(Tiempo,"DE HOY");
        }
        else if(request.getParameter("tiempo").equals("SEM"))
        {
          Calendar hoy = GregorianCalendar.getInstance();
          hoy.add(Calendar.DATE,1);
          Calendar ini = GregorianCalendar.getInstance();
          ini.add(Calendar.DATE, -7);

          String Tiempo = "Fecha BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
          getSesion(request).getSesionMensajes().setTiempo(Tiempo,"DE LA ULTIMA SEMANA");
        }
        else if(request.getParameter("tiempo").equals("MAS"))
        {
          if(request.getParameter("mes") != null && request.getParameter("ano") != null && !request.getParameter("mes").equals("") && !request.getParameter("ano").equals(""))
          {
            int mes;
            int ano;

            try{ mes = Integer.parseInt(request.getParameter("mes"));
            }catch(NumberFormatException e) {
              idmensaje = 1; mensaje += " El par�metro de tiempo MAS es v�lido, pero el par�metro de mes esta incorrecto. Se captur� el mes y a�o actual<br>";
              mes = JUtil.obtMes(GregorianCalendar.getInstance()); ano = JUtil.obtAno(GregorianCalendar.getInstance());
            }
            try{ ano = Integer.parseInt(request.getParameter("ano"));
            }catch(NumberFormatException e) {
              idmensaje = 1; mensaje += " El par�metro de tiempo MAS es v�lido, pero el par�metro de ano esta incorrecto. Se captur� el mes y a�o actual<br>";
              mes = JUtil.obtMes(GregorianCalendar.getInstance()); ano = JUtil.obtAno(GregorianCalendar.getInstance());
            }

            String TiempoTit = "DE " + JUtil.convertirMesCorto(mes) + " DEL " + ano;

            String Tiempo = "Month(Fecha) = " + mes + " AND Year(Fecha) = " + ano;
            getSesion(request).getSesionMensajes().setTiempo(Tiempo,TiempoTit);
          }
          else
          {
            idmensaje = 1; mensaje += " El par�metro de tiempo MAS es v�lido, pero no se han especificado los dos parametros de mes y ano<br>";
          }
        }
        else
        {
           idmensaje = 1; mensaje += " El par�metro de tiempo no es v�lido debe ser HOY SEM o MAS<br>";
        }
      }
      else if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
      {
        if(request.getParameter("status").equals("TODOS"))
        {
          getSesion(request).getSesionMensajes().setStatus("","");
        }
        else if(request.getParameter("status").equals("LEIDOS"))
        {
          getSesion(request).getSesionMensajes().setStatus("Leido = 1","LEIDOS");
        }
        else if(request.getParameter("status").equals("SIN_LEER"))
        {
          getSesion(request).getSesionMensajes().setStatus("Leido = 0","SIN LEER");
        }
        else if(request.getParameter("status").equals("RESP"))
        {
          getSesion(request).getSesionMensajes().setStatus("Respondido = 1","RESPONDIDOS");
        }
        else if(request.getParameter("status").equals("NO_RESP"))
        {
          getSesion(request).getSesionMensajes().setStatus("Respondido = 0","NO RESPONDIDOS");
        }
        else
        {
           idmensaje = 1; mensaje += " El par�metro de status " + request.getParameter("status") + " no es v�lido, debe ser TODOS, LEIDOS, SIN_LEER, RESP o NO_RESP<br>";
        }
      }
      else if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
        if(request.getParameter("orden").equals("Fecha") || request.getParameter("orden").equals("Titulo") ||
           request.getParameter("orden").equals("Nombre") || request.getParameter("orden").equals("Leido") ||
           request.getParameter("orden").equals("ID_Respuesta") || request.getParameter("orden").equals("FechaLeido") )
        {
          getSesion(request).getSesionMensajes().setOrden(request.getParameter("ordenetq"),request.getParameter("orden"));
        }
        else
        {
          idmensaje = 1; mensaje += " El par�metro de orden " + request.getParameter("orden") + " no es v�lido, debe ser Fecha, Titulo, Nombre, Leido, FechaLeido o ID_Respuesta<br>";
        }
      }
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/mensajes/mensajes_vsta.jsp", request, response);
*/
    }

}
