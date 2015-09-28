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
//import java.util.Calendar;
//import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
//import forseti.JUtil;
//import forseti.sets.JNominaEntidadesSetIds;

@SuppressWarnings("serial")
public class JNomPermGrupoCtrl extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }
/*
    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String nom_permgrupo = "";
      request.setAttribute("nom_permgrupo",nom_permgrupo);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();
   
      if(!getSesion(request).getPermiso("NOM_PERMGRUPO"))
      {
        idmensaje = 3; mensaje += " No tienes acceso al módulo de permisos por grupo ";
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getNomPermGrupoEst() == false)
      {
    	  JNominaEntidadesSetIds set = new JNominaEntidadesSetIds(request);
          set.setSelect(" top 1  * FROM view_nomina_entidades_ids  ");
          set.m_OrderBy = "ID_Sucursal ASC";
          set.m_Where = "ID_Usuario = '" + usuario + "'";
          set.Open();
          if(set.getNumRows() < 1)
          {
            idmensaje = 1; mensaje += " No se han dado de alta ningún tipo de nómina<br>No se puede mostrar este módulo hasta que se registren las nóminas<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
            return;
          }

          String Entidad = "ID_Sucursal = " + set.getAbsRow(0).getID_Sucursal();
          Calendar fecha = GregorianCalendar.getInstance();
          String Tiempo = "Day(ID_FechaMovimiento) = " + JUtil.obtDia(fecha) + "  AND Month(ID_FechaMovimiento) = " + JUtil.obtMes(fecha) + " AND Year(ID_FechaMovimiento) = " + JUtil.obtAno(fecha);

          getSesion(request).EstablecerNomPermGrupo();
          getSesion(request).getSesionNomPermGrupo().setParametros(Entidad, Tiempo, "", set.getAbsRow(0).getDescripcion(), "DE HOY", "");
          getSesion(request).getSesionNomPermGrupo().setEspecial(Integer.toString(set.getAbsRow(0).getID_Sucursal()));
          getSesion(request).getSesionNomPermGrupo().setOrden(request.getParameter("ordenetq"),"ID_FechaMovimiento");
         
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/nomina/nom_permgrupo_vsta.jsp",request,response);
          return;
      }
      if(request.getParameter("entidad") != null && !request.getParameter("entidad").equals(""))
      {
    	  JNominaEntidadesSetIds set = new JNominaEntidadesSetIds(request);
          set.m_Where = "ID_Usuario = '" + usuario + "' and ID_Sucursal = " + request.getParameter("entidad");
          set.Open();
          if(set.getNumRows() > 0)
          {
            String Entidad = "ID_Sucursal = " + set.getAbsRow(0).getID_Sucursal();
            getSesion(request).getSesionNomPermGrupo().setEntidad(Entidad,set.getAbsRow(0).getDescripcion());
            getSesion(request).getSesionNomPermGrupo().setEspecial(Integer.toString(set.getAbsRow(0).getID_Sucursal()));
          }
          else
          {
             idmensaje = 1; mensaje += " El parámetro de entidad no es válido<br>";
          }

      }
      else if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
        if(request.getParameter("tiempo").equals("HOY"))
        {
          Calendar fecha = GregorianCalendar.getInstance();
          String Tiempo = "Day(ID_FechaMovimiento) = " + JUtil.obtDia(fecha) + "  AND Month(ID_FechaMovimiento) = " + JUtil.obtMes(fecha) + " AND Year(ID_FechaMovimiento) = " + JUtil.obtAno(fecha);
          getSesion(request).getSesionNomPermGrupo().setTiempo(Tiempo,"DE HOY");
        }
        else if(request.getParameter("tiempo").equals("SEM"))
        {
          Calendar hoy = GregorianCalendar.getInstance();
          hoy.add(Calendar.DATE,1);
          Calendar ini = GregorianCalendar.getInstance();
          ini.add(Calendar.DATE, -7);
         String Tiempo = "ID_FechaMovimiento BETWEEN '" + JUtil.obtFechaSQL(ini) + "' AND '" + JUtil.obtFechaSQL(hoy) + "' ";
          getSesion(request).getSesionNomPermGrupo().setTiempo(Tiempo,"DE LA ULTIMA SEMANA");
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

            String Tiempo = "Month(ID_FechaMovimiento) = " + mes + " AND Year(ID_FechaMovimiento) = " + ano;
            getSesion(request).getSesionNomPermGrupo().setTiempo(Tiempo,TiempoTit);
          }
          else
          {
            idmensaje = 1; mensaje += " El par�metro de tiempo MAS es v�lido, pero no se han especificado los dos parametros de mes y ano<br>";
          }
        }
        else
        {
           idmensaje = 1; mensaje += " El par�metro de tiempo no es v�lido<br>";
        }
      }
      else if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
        getSesion(request).getSesionNomPermGrupo().setOrden(request.getParameter("ordenetq"),request.getParameter("orden"));
      }
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/nomina/nom_permgrupo_vsta.jsp", request, response);

    }
*/
}
