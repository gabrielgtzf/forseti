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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JNomTurnosDlg extends JForsetiApl
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

      String nom_turnos_dlg = "";
      request.setAttribute("nom_turnos_dlg",nom_turnos_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_TURNO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_TURNOS_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_TURNOS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_TURNOS_AGREGAR","NTRN||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response))
            {
            	AgregarCambiar(request, response, "agregar");
            	return;
            }

            irApag("/forsetiweb/nomina/nom_turnos_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/nomina/nom_turnos_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_TURNO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_TURNOS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_TURNOS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_TURNOS_CAMBIAR","NTRN||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
                // Verificacion
                if(VerificarParametros(request, response))
                {
                  AgregarCambiar(request, response, "cambiar");
                  return;
                }
                irApag("/forsetiweb/nomina/nom_turnos_dlg.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/nomina/nom_turnos_dlg.jsp", request, response);
            	  return;
              }

            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
          }
          else
          {
        	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_TURNO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_TURNOS"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_TURNOS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_TURNOS","NTRN||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_turnos_dlg.jsp", request, response);
              return;
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
          }
          else
          {
        	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
        }
        else
        {
        	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
    	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
      }

    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("id_turno") != null && request.getParameter("descripcion") != null &&
    	  request.getParameter("elunes") != null && request.getParameter("slunes") != null && request.getParameter("hnalunes") != null && request.getParameter("healunes") != null &&  
		  request.getParameter("emartes") != null && request.getParameter("smartes") != null && request.getParameter("hnamartes") != null && request.getParameter("heamartes") != null &&  
		  request.getParameter("emiercoles") != null && request.getParameter("smiercoles") != null && request.getParameter("hnamiercoles") != null && request.getParameter("heamiercoles") != null &&  
		  request.getParameter("ejueves") != null && request.getParameter("sjueves") != null && request.getParameter("hnajueves") != null && request.getParameter("heajueves") != null &&  
		  request.getParameter("eviernes") != null && request.getParameter("sviernes") != null && request.getParameter("hnaviernes") != null && request.getParameter("heaviernes") != null &&  
		  request.getParameter("esabado") != null && request.getParameter("ssabado") != null && request.getParameter("hnasabado") != null && request.getParameter("heasabado") != null &&  
		  request.getParameter("edomingo") != null && request.getParameter("sdomingo") != null && request.getParameter("hnadomingo") != null && request.getParameter("headomingo") != null &&  
	      !request.getParameter("id_turno").equals("") && !request.getParameter("descripcion").equals("") &&
	      !request.getParameter("hnalunes").equals("") && !request.getParameter("healunes").equals("") &&
	      !request.getParameter("hnamartes").equals("") && !request.getParameter("heamartes").equals("") &&
	      !request.getParameter("hnamiercoles").equals("") && !request.getParameter("heamiercoles").equals("") &&
	      !request.getParameter("hnajueves").equals("") && !request.getParameter("heajueves").equals("") &&
	      !request.getParameter("hnaviernes").equals("") && !request.getParameter("heaviernes").equals("") &&
	      !request.getParameter("hnasabado").equals("") && !request.getParameter("heasabado").equals("") &&
	      !request.getParameter("hnadomingo").equals("") && !request.getParameter("headomingo").equals("") )
      {
    	  if(request.getParameter("lunes") != null && (request.getParameter("elunes").equals("") || request.getParameter("slunes").equals("")) )
    	  {
    		  idmensaje = 3; mensaje = "ERROR: Como se especifico lunes como dia laboral, se deben enviar los horarios de entrada y salida <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
    	  
     	  if(request.getParameter("martes") != null && (request.getParameter("emartes").equals("") || request.getParameter("smartes").equals("")) )
    	  {
    		  idmensaje = 3; mensaje = "ERROR: Como se especifico martes como dia laboral, se deben enviar los horarios de entrada y salida <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
     	  if(request.getParameter("miercoles") != null && (request.getParameter("emiercoles").equals("") || request.getParameter("smiercoles").equals("")) )
    	  {
    		  idmensaje = 3; mensaje = "ERROR: Como se especifico miercoles como dia laboral, se deben enviar los horarios de entrada y salida <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
     	  if(request.getParameter("jueves") != null && (request.getParameter("ejueves").equals("") || request.getParameter("sjueves").equals("")) )
    	  {
    		  idmensaje = 3; mensaje = "ERROR: Como se especifico jueves como dia laboral, se deben enviar los horarios de entrada y salida <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
     	  if(request.getParameter("viernes") != null && (request.getParameter("eviernes").equals("") || request.getParameter("sviernes").equals("")) )
    	  {
    		  idmensaje = 3; mensaje = "ERROR: Como se especifico viernes como dia laboral, se deben enviar los horarios de entrada y salida <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
     	  if(request.getParameter("sabado") != null && (request.getParameter("esabado").equals("") || request.getParameter("ssabado").equals("")) )
    	  {
    		  idmensaje = 3; mensaje = "ERROR: Como se especifico sabado como dia laboral, se deben enviar los horarios de entrada y salida <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
     	  if(request.getParameter("domingo") != null && (request.getParameter("edomingo").equals("") || request.getParameter("sdomingo").equals("")) )
    	  {
    		  idmensaje = 3; mensaje = "ERROR: Como se especifico domingo como dia laboral, se deben enviar los horarios de entrada y salida <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
    
    	  
          return true;
      }
      else
      {
    	  idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public void AgregarCambiar(HttpServletRequest request, HttpServletResponse response, String proceso)
      throws ServletException, IOException
    {
    	String str = "select * from " + (proceso.equals("agregar") ? " sp_nom_turnos_agregar('" : " sp_nom_turnos_cambiar('");
	    	str += p(request.getParameter("id_turno")) + "','" + p(request.getParameter("descripcion")) + "'," + 
	        ( request.getParameter("lunes") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("elunes"))) + "'" ) + "," +  ( request.getParameter("lunes") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("slunes"))) + "'" ) + "," + 
	        ( request.getParameter("martes") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("emartes"))) + "'" ) + "," +  ( request.getParameter("martes") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("smartes"))) + "'" ) + "," + 
	        ( request.getParameter("miercoles") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("emiercoles"))) + "'" ) + "," +  ( request.getParameter("miercoles") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("smiercoles"))) + "'" ) + "," + 
	        ( request.getParameter("jueves") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("ejueves"))) + "'" ) + "," +  ( request.getParameter("jueves") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("sjueves"))) + "'" ) + "," + 
	        ( request.getParameter("viernes") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("eviernes"))) + "'" ) + "," +  ( request.getParameter("viernes") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("sviernes"))) + "'" ) + "," + 
	        ( request.getParameter("sabado") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("esabado"))) + "'" ) + "," +  ( request.getParameter("sabado") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("ssabado"))) + "'" ) + "," + 
	        ( request.getParameter("domingo") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("edomingo"))) + "'" ) + "," +  ( request.getParameter("domingo") == null ? "null" : "'" + JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("sdomingo"))) + "'" ) + ",'" + 
	        p(request.getParameter("hnalunes")) + "','" + p(request.getParameter("healunes")) + "','" + 
	        p(request.getParameter("hnalunes")) + "','" + p(request.getParameter("heamartes")) + "','" + 
	        p(request.getParameter("hnamiercoles")) + "','" + p(request.getParameter("heamiercoles")) + "','" + 
	        p(request.getParameter("hnajueves")) + "','" + p(request.getParameter("heajueves")) + "','" + 
	        p(request.getParameter("hnaviernes")) + "','" + p(request.getParameter("heaviernes")) + "','" + 
	        p(request.getParameter("hnasabado")) + "','" + p(request.getParameter("heasabado")) + "','" + 
	        p(request.getParameter("hnadomingo")) + "','" + p(request.getParameter("headomingo")) + "','" +
	        p(request.getParameter("ttlun")) + "','" +
	        p(request.getParameter("ttmar")) + "','" +
	        p(request.getParameter("ttmie")) + "','" +
	        p(request.getParameter("ttjue")) + "','" +
	        p(request.getParameter("ttvie")) + "','" +
	        p(request.getParameter("ttsab")) + "','" +
	        p(request.getParameter("ttdom")) + "') as (err integer, res varchar, clave smallint)";
	    	
	    	JRetFuncBas rfb = new JRetFuncBas();
	     	 		
	        doCallStoredProcedure(request, response, str, rfb);
	       
	        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_TURNOS_" + (proceso.equals("agregar") ? "AGREGAR" : "CAMBIAR"), "NDEP|" + rfb.getClaveret() + "|||",rfb.getRes());

	    	irApag("/forsetiweb/nomina/nom_turnos_dlg.jsp", request, response);
	    
    }

}
