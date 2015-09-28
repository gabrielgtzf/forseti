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
import forseti.sets.JAsistenciasChequeosSet;
import forseti.sets.JMasempSet;
import forseti.sets.JAdmCompaniasSet;
import forseti.sets.JNominaEntidadesSetIds;
import forseti.JUtil;

@SuppressWarnings("serial")
public class JNomAsistenciasDlg extends JForsetiApl
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

      String nom_asistencias_dlg = "";
      request.setAttribute("nom_asistencias_dlg",nom_asistencias_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
        JNominaEntidadesSetIds setids = new  JNominaEntidadesSetIds(request,usuario,getSesion(request).getSesion("NOM_ASISTENCIAS").getEspecial());
        setids.Open();

        if(setids.getNumRows() < 1)
        {
        	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ASISTENCIAS");
          	getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_ASISTENCIAS","NASI||||",mensaje);
          	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          	return;
          }

          // Revisa por intento de intrusion (Salto de permiso de entidad)
          if(!request.getParameter("proceso").equals("AGREGAR_ASISTENCIA") && request.getParameter("id") != null)
          {
        	JAsistenciasChequeosSet set = new JAsistenciasChequeosSet(request);
          	set.m_Where = "ID_Empleado = '" + p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|")) + "' and ID_Fecha = '" +
          			 p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "'";
          	set.Open();
          	if(set.getNumRows() < 1)
          	{
          		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ASISTENCIAS");
          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"NOM_ASISTENCIAS","NASI|" + JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|") + "_" + JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|") + "_" + JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|") + "|" + setids.getAbsRow(0).getID_Sucursal() + "||",mensaje);
          		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          		return;
          	}
        }
          
    	if(request.getParameter("proceso").equals("AGREGAR_ASISTENCIA"))
        {
    		// Revisa si tiene asistencias
    		if(!getSesion(request).getPermiso("NOM_ASISTENCIAS_AGREGAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ASISTENCIAS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_ASISTENCIAS_AGREGAR","NASI||||",mensaje);
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
    			irApag("/forsetiweb/nomina/nom_asistencias_dlg.jsp", request, response);
    			return;
    		}
    		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
    		{
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			irApag("/forsetiweb/nomina/nom_asistencias_dlg.jsp", request, response);
    			return;
    		}
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_ASISTENCIA"))
        {
        	// Revisa si tiene asistencias
        	if(!getSesion(request).getPermiso("NOM_ASISTENCIAS_CAMBIAR"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ASISTENCIAS_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_ASISTENCIAS_CAMBIAR","NASI||||",mensaje);
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
	                irApag("/forsetiweb/nomina/nom_asistencias_dlg.jsp", request, response);
	    			return;
	              }
	              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
	              {
	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                irApag("/forsetiweb/nomina/nom_asistencias_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_ASISTENCIA"))
    	{
    		// Revisa si tiene asistencias
    		if(!getSesion(request).getPermiso("NOM_ASISTENCIAS_ELIMINAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ASISTENCIAS_ELIMINAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_ASISTENCIAS_ELIMINAR","NASI||||",mensaje);
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
        else if(request.getParameter("proceso").equals("CAPTURAR_ASISTENCIA"))
        {
    		// Revisa si tiene asistencias
    		if(!getSesion(request).getPermiso("NOM_ASISTENCIAS"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_ASISTENCIAS");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_ASISTENCIAS","NASI||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
    		}
	
    		// Solicitud de envio a procesar
    		if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
    		{
	              // Verificacion
    			if(VerificarParametrosCaptura(request, response))
    			{
    				Capturar(request, response);
    				return;
    			}
    			irApag("/forsetiweb/nomina/nom_asistencias_dlg_server.jsp", request, response);
    			return;
    		}
    		else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
    		{
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			irApag("/forsetiweb/nomina/nom_asistencias_dlg_server.jsp", request, response);
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
      if(request.getParameter("id_empleado") != null && request.getParameter("fechahora") != null &&
    	 !request.getParameter("fechahora").equals(""))
      {
    	  if(request.getParameter("ipc") == null || request.getParameter("ipc").equals("1"))
    	  {
    		  if(request.getParameter("id_empleado").equals(""))
    		  {
    			  idmensaje = 1;
	    	      mensaje += "PRECAUCION: Como se seleccionó por empleado, se debe mandar su clave<br>";
	    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	      return false;
    		  }
    		  JMasempSet setemp = new JMasempSet(request);
    		  setemp.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_ASISTENCIAS").getEspecial() + "' and ID_Empleado = '" + p(request.getParameter("id_empleado")) + "'";
    		  setemp.Open();
    	  
	    	  if(setemp.getNumRows() < 1)
	    	  {
	    		  idmensaje = 3;
	    	      mensaje += "ERROR: El empleado no existe, o no pertenece a esta nómina<br>";
	    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              return false;
	    	  }
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
    
    public boolean VerificarParametrosCaptura(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("id_empleado") != null && !request.getParameter("id_empleado").equals(""))
	    {
	    	JMasempSet setemp = new JMasempSet(request);
	    	setemp.m_Where = "Status = '0' and ID_Empleado = '" + p(request.getParameter("id_empleado")) + "'";
	    	setemp.Open();
	  	  
	    	if(setemp.getNumRows() < 1)
	    	{
	    		idmensaje = 3;
	    		mensaje += "ERROR: El empleado no existe, o ya está dado de baja de la nómina<br>";
	    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    		return false;
	    	}
	    	
	  	    return true;
	    }
	    else
	    {
	        idmensaje = 3; mensaje = "ERROR: No se mandó al empleado <br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }
	}
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
     	String str = "select * from sp_nom_asistencias_client_eliminar('" + p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|")) + "','" +  p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "') as (err integer, res varchar, clave varchar)";
		
     	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
  	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_ASISTENCIAS_ELIMINAR", "NASI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_ASISTENCIAS").getEspecial() + "||",rfb.getRes());
  	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	    

  	}
    
    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
       	String str = "select * from sp_nom_asistencias_client_cambiar('" + p(request.getParameter("id_empleado")) + "','" + p(JUtil.obtFechaHoraSQL(request.getParameter("fechahora"))) + "','" + 
   	  		 p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "') as (err integer, res varchar, clave varchar)";
	
       	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
  	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_ASISTENCIAS_CAMBIAR", "NASI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_ASISTENCIAS").getEspecial() + "||",rfb.getRes());
  	    irApag("/forsetiweb/nomina/nom_asistencias_dlg.jsp", request, response);
   	}

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String comemp;
    	
    	if(request.getParameter("ipc").equals("1"))
  	  		comemp = p(request.getParameter("id_empleado"));
    	else
    	{
    		JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
    		setcom.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_ASISTENCIAS").getEspecial() + "'";
    		setcom.Open();
      	 	comemp = setcom.getAbsRow(0).getDescripcion();
      	}
      	
    	String str = "select * from  sp_nom_asistencias_client_agregar( '" + p(request.getParameter("ipc")) + "','" + p(comemp) + "','" + p(JUtil.obtFechaHoraSQL(request.getParameter("fechahora"))) + "') as (err integer, res varchar, clave varchar)";
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
  	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_ASISTENCIAS_AGREGAR", "NASI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_ASISTENCIAS").getEspecial() + "||",rfb.getRes());
  	    irApag("/forsetiweb/nomina/nom_asistencias_dlg.jsp", request, response);
	    
    }
	
    public void Capturar(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	  	String str = "select * from sp_nom_asistencias_server_agregar( '" + p(request.getParameter("id_empleado")) + "') as (err integer, res varchar, clave varchar)";

	  	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
  	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_ASISTENCIAS", "NASI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_ASISTENCIAS").getEspecial() + "||",rfb.getRes());
  	    irApag("/forsetiweb/nomina/nom_asistencias_dlg_server.jsp", request, response);
	  	
	}

}
