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
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.sets.JMovimientosSet;
import forseti.sets.JMasempSet;
import forseti.sets.JAdmCompaniasSet;
import forseti.sets.JNominaEntidadesSetIds;
import forseti.sets.JPermisosGrupoSet;
import forseti.sets.JPermisosSet;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JNomPermisosDlg extends JForsetiApl
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

      String nom_permisos_dlg = "";
      request.setAttribute("nom_permisos_dlg",nom_permisos_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
      	JNominaEntidadesSetIds setids = new  JNominaEntidadesSetIds(request,usuario,getSesion(request).getSesion("NOM_PERMISOS").getEspecial());
      	setids.Open();

        if(setids.getNumRows() < 1)
        {
        	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PERMISOS");
        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PERMISOS","NPER||||",mensaje);
        	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	return;
        }

        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_PERMISO") && request.getParameter("id") != null)
        {
        	boolean prmgrp = JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|").indexOf("FSINOMINA-") == -1 ? false : true;
        	if(!prmgrp)
        	{
        		JPermisosSet set = new JPermisosSet(request);
        		set.m_Where = "ID_Empleado = '" + p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|")) + "' and ID_Movimiento = '" +
        			p(JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|")) + "' and ID_FechaMovimiento = '" +
        			 p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "'";
        		set.Open();
        		if(set.getNumRows() < 1)
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PERMISOS");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"NOM_PERMISOS","NPER|" + JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|") + "_" + JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|") + "_" + JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|") + "|" + setids.getAbsRow(0).getID_Sucursal() + "||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
        	}
        	else // es permiso de grupo
        	{
        		JPermisosGrupoSet set = new JPermisosGrupoSet(request);
        		set.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_FSINOMINA-","|")) + "' and ID_Movimiento = '" +
        			p(JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|")) + "' and ID_FechaMovimiento = '" +
        			 p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "'";
        		set.Open();
        		System.out.println(set.getSQL());
        		if(set.getNumRows() < 1)
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PERMISOS");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"NOM_PERMISOS","NPER|" + JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|") + "_" + JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|") + "_" + JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|") + "|" + setids.getAbsRow(0).getID_Sucursal() + "||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
        	}
        }
          
    	if(request.getParameter("proceso").equals("AGREGAR_PERMISO"))
        {
    		// Revisa si tiene permisos
    		if(!getSesion(request).getPermiso("NOM_PERMISOS_AGREGAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PERMISOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PERMISOS_AGREGAR","NPER||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
    		}
	
    		// Solicitud de envio a procesar
    		if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
    		{
	              // Verificacion
    			if(VerificarParametros(request, response))
    			{
	                Agregar(request, response);
	                return;
    			}
    			irApag("/forsetiweb/nomina/nom_permisos_dlg.jsp", request, response);
    			return;
    		}
    		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
    		{
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			irApag("/forsetiweb/nomina/nom_permisos_dlg.jsp", request, response);
    			return;
    		}
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_PERMISO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("NOM_PERMISOS_CAMBIAR"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PERMISOS_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PERMISOS_CAMBIAR","NPER||||",mensaje);
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
	                	Cambiar(request, response);
	                	return;
	                }
	                irApag("/forsetiweb/nomina/nom_permisos_dlg.jsp", request, response);
	                return;
	              }
	              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
	              {
	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                irApag("/forsetiweb/nomina/nom_permisos_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_PERMISO"))
    	{
        	// Revisa si tiene permisos
    		if(!getSesion(request).getPermiso("NOM_PERMISOS_ELIMINAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PERMISOS_ELIMINAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PERMISOS_ELIMINAR","NPER||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
    		}
    		// Solicitud de envio a procesar
    		if(request.getParameter("id") != null)
    		{
    			String[] valoresParam = request.getParameterValues("id");
    			if(valoresParam.length == 1)
    			{
    				Eliminar(request, response);
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
      if(request.getParameter("id_movimiento") != null && request.getParameter("id_empleado") != null && request.getParameter("desde") != null &&
    	 request.getParameter("hasta") != null &&  request.getParameter("obs") != null &&
    	!request.getParameter("id_movimiento").equals("") && !request.getParameter("desde").equals("") && 
    	!request.getParameter("hasta").equals(""))
      {
    	  JMovimientosSet set = new JMovimientosSet(request);
    	  set.m_Where = "ID_Movimiento = '" + p(request.getParameter("id_movimiento")) + "'";
    	  set.Open();
    	  
    	  if(set.getNumRows() < 1)
    	  {
    		  idmensaje = 3;
    	      mensaje += "ERROR: El movimiento para el permiso no existe<br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
    	  
    	  if(!request.getParameter("id_movimiento").equals("300")) // No es permiso de grupo (DIA FESTIVO)
    	  {
    		  if(request.getParameter("id_empleado").equals(""))
    		  {
    			  idmensaje = 3;
        	      mensaje += "ERROR: Debes enviar la clave del empleado para este permiso<br>";
        	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	      return false; 
    		  }
    		  
    		  JMasempSet setemp = new JMasempSet(request);
    		  setemp.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "' and ID_Empleado = '" + p(request.getParameter("id_empleado")) + "'";
    		  setemp.Open();
    	  
    		  if(setemp.getNumRows() < 1)
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: El empleado no existe, o no pertenece a esta nómina<br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
    	  }
    	  
    	  // Ahora revisa los tiempos segun tipo de movimiento. Si es de horas, los dias deben ser iguales y las horas diferentes
    	  // Y en ambos casos, las fechas hasta mayores o iguales a las fechas desde
    	  if(set.getAbsRow(0).getDC())
    	  {
    		  Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
    		  Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
    		  if(desde.getTime() > hasta.getTime())
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: La fecha de inicio del permiso no puede ser mayor a la del final del permiso, en permisos de dias completos. Diferencia " + ((hasta.getTime()/(3600*24*1000)) - (desde.getTime()/(3600*24*1000))) + " dia(s) <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
    	  }
    	  else
    	  {
    		  Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
    		  Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
    		  if(desde.getTime() != hasta.getTime())
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: La fecha de inicio del permiso no puede ser diferente a la del final del permiso, en permisos de horas. <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
    		  desde = JUtil.estFechaHora(request.getParameter("desde"));
    		  hasta = JUtil.estFechaHora(request.getParameter("hasta"));
    		  if(desde.getTime() > hasta.getTime())
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: La hora de inicio del permiso no puede ser mayor a la del final del permiso, en permisos horas. Diferencia " + ((hasta.getTime()/(3600*1000)) - (desde.getTime()/(3600*1000))) + " hora(s) <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
    	  }
    	  
    	  /*
    	  if( !set.getAbsRow(0).getPorEmpleado() )
    	  {
       		  idmensaje = 3;
    	      mensaje += "ERROR: El tipo de movimiento no es aplicable a empleados, Puede ser un tipo de movimiento de grupo ( Toda la nómina ) <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
     	  }
    	  */
    	  
    	  if(set.getAbsRow(0).getAplicaAlTipo() == -1)
    	  {
       		  idmensaje = 3;
    	      mensaje += "ERROR: El tipo de movimiento no es compatible con permisos de empleados ni grupos. Este es un movimiento solamente de sistema <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
     	  }
    	  
    	  JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
    	  setcom.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "'";
    	  setcom.Open();
    	  
    	  if(set.getAbsRow(0).getAplicaAlTipo() != 0 && set.getAbsRow(0).getAplicaAlTipo() != setcom.getAbsRow(0).getTipo())
    	  {
       		  idmensaje = 3;
    	      mensaje += "ERROR: El tipo de movimiento no es compatible al tipo de nómina del empleado (Por Ej. Empleado confianza con nómina estricta) <br>";
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
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	boolean prmgrp = JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|").indexOf("FSINOMINA-") == -1 ? false : true;
    	
    	if(!prmgrp)
    	{
    		String str = "select * from sp_nom_permisos_eliminar( '" + p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|")) + "','" + p(JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|")) + "','" + 
    				p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "','0','" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "') as (err integer, res varchar, clave varchar)";
			JRetFuncBas rfb = new JRetFuncBas();
			doCallStoredProcedure(request, response, str, rfb);
    		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PERMISOS_ELIMINAR", "NPER|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "||",rfb.getRes());
    		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	}
    	else
    	{
    		String str = "select * from sp_nom_permisos_eliminar( '','" + p(JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|")) + "','" + 
    				p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "','1','" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "') as (err integer, res varchar, clave varchar)";
			JRetFuncBas rfb = new JRetFuncBas();
			doCallStoredProcedure(request, response, str, rfb);
    		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PERMISOS_ELIMINAR", "NPER|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "||",rfb.getRes());
    		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	}
   	}
    
    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JMovimientosSet set = new JMovimientosSet(request);
   	  	set.m_Where = "ID_Movimiento = '" + p(request.getParameter("id_movimiento")) + "'";
   	  	set.Open();
   	  	
   	  	int num_dias = 0;
   	  	float num_horas = 0f;
   	  	boolean prmgrp = request.getParameter("id_movimiento").equals("300") ? true : false; // Este es un permiso de grupo o no ? (DIA FESTIVO)
	  	
   	  	Calendar hastadc = Calendar.getInstance();
	  	if(set.getAbsRow(0).getDC())
   	  	{
   	  		Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
   	  		num_dias = (int)JUtil.getFechaDiff(hasta, desde, "dias") + 1;
   	  		hastadc.setTime(hasta);
	  		hastadc.add(Calendar.DATE, 1); //minus number would decrement the days
	  		
   	  	}
   	  	else
   	  	{
   	  		Date desde = JUtil.estFechaHora(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFechaHora(request.getParameter("hasta"));
   	  		num_horas = JUtil.redondear((float)JUtil.getFechaDiff(hasta, desde, "minutos")/60,2);
   	  		
   	  	}
   	  	
   	  	String str = "select * from  sp_nom_permisos_cambiar( '" + p(request.getParameter("id_empleado")) + "','" + p(request.getParameter("id_movimiento")) + "','" +  
   	  		p(JUtil.obtFechaSQLh24(request.getParameter("desde"))) + "','" + (set.getAbsRow(0).getDC() ? "1" :"0") + "','" + 
   	  		(set.getAbsRow(0).getDC() ? p(JUtil.obtFechaSQLh24(request.getParameter("desde"))) : p(JUtil.obtFechaHoraSQL(request.getParameter("desde")))) + "','" + 
   	  		(set.getAbsRow(0).getDC() ? p(JUtil.obtFechaSQL(hastadc.getTime())) : p(JUtil.obtFechaHoraSQL(request.getParameter("hasta")))) + "','" + num_dias + "','" + num_horas + "','" + num_horas + "','" + p(request.getParameter("obs")) + "','" +
   	  		(prmgrp ? "1" : "0") + "','" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "') as (err integer, res varchar, clave varchar)";
	
     	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
  	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PERMISOS_CAMBIAR", "NPER|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "||",rfb.getRes());
  	    irApag("/forsetiweb/nomina/nom_permisos_dlg.jsp", request, response);


    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JMovimientosSet set = new JMovimientosSet(request);
   	  	set.m_Where = "ID_Movimiento = '" + p(request.getParameter("id_movimiento")) + "'";
   	  	set.Open();
   	  	
   	  	int num_dias = 0;
   	  	float num_horas = 0f;
   	  	boolean prmgrp = request.getParameter("id_movimiento").equals("300") ? true : false; // Este es un permiso de grupo o no ? (DIA FESTIVO)
   	  		
   	  	Calendar hastadc = Calendar.getInstance();
   	  	if(set.getAbsRow(0).getDC())
   	  	{
   	  		Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
   	  		num_dias = (int)JUtil.getFechaDiff(hasta, desde, "dias") + 1;
   	  		hastadc.setTime(hasta);
   	  		hastadc.add(Calendar.DATE, 1); //minus number would decrement the days
   	  		
   	  	}
   	  	else
   	  	{
   	  		Date desde = JUtil.estFechaHora(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFechaHora(request.getParameter("hasta"));
   	  		num_horas = JUtil.redondear((float)JUtil.getFechaDiff(hasta, desde, "minutos")/60,2);
   	  		
   	  	}
   	  	
   	  	String str = "select * from sp_nom_permisos_agregar( '" + p(request.getParameter("id_empleado")) + "','" + p(request.getParameter("id_movimiento")) + "','" +  
   	  		p(JUtil.obtFechaSQLh24(request.getParameter("desde"))) + "','" + (set.getAbsRow(0).getDC() ? "1" :"0") + "','" + 
   	  		(set.getAbsRow(0).getDC() ? p(JUtil.obtFechaSQLh24(request.getParameter("desde"))) : p(JUtil.obtFechaHoraSQL(request.getParameter("desde")))) + "','" + 
   	  		(set.getAbsRow(0).getDC() ? p(JUtil.obtFechaSQL(hastadc.getTime())) : p(JUtil.obtFechaHoraSQL(request.getParameter("hasta")))) + "','" + num_dias + "','" + num_horas + "','" + num_horas + "','" + p(request.getParameter("obs")) + "','" +
   	  		(prmgrp ? "1" : "0") + "','" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "') as (err integer, res varchar, clave varchar)";
   	  	   	  	
   	  	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
  	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PERMISOS_AGREGAR", "NPER|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PERMISOS").getEspecial() + "||",rfb.getRes());
  	    irApag("/forsetiweb/nomina/nom_permisos_dlg.jsp", request, response);
  	
    }

}
