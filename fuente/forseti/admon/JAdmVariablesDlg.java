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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JPublicContCatalogSetV2;
import forseti.sets.JAdmVariablesSet;

@SuppressWarnings("serial")
public class JAdmVariablesDlg extends JForsetiApl
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

      String adm_variables_dlg = "";
      request.setAttribute("adm_variables_dlg",adm_variables_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	if(request.getParameter("proceso").equals("AGREGAR_VARIABLE"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("ADM_VARIABLES_AGREGAR"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_VARIABLES_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_VARIABLES_AGREGAR","AVAR||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(!getSesion(request).getSesion("ADM_VARIABLES").getEspecial().equals("ESP"))
            {
            	idmensaje = 1; mensaje += JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR",5);
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        	{
        		// Verificacion
        		if(VerificarParametrosUsr(request, response))
        		{
        			AgregarCambiarUsr(request, response, "AGREGAR");
        			return;
        		}
        		
        		irApag("/forsetiweb/administracion/adm_variables_dlg_usr.jsp", request, response);
        		return;
        	}
        	else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        	{
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/administracion/adm_variables_dlg_usr.jsp", request, response);
        		return;
        	}
        }
    	else if(request.getParameter("proceso").equals("CAMBIAR_VARIABLE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_VARIABLES_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_VARIABLES_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_VARIABLES_AGREGAR","AVAR||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
          
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JAdmVariablesSet set = new JAdmVariablesSet(request);
            	set.m_Where = "ID_Variable = '" + p(request.getParameter("id")) + "'";
            	set.Open();
            	                            
            	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            	{
            		// Verificacion
            		if(set.getAbsRow(0).getDeSistema() == 1)
            		{
            			if(VerificarParametros(request, response))
            			{
            				Cambiar(request, response);
            				return;
            			}
            			irApag("/forsetiweb/administracion/adm_variables_dlg.jsp", request, response);
            			return;
            		}
            		else
            		{
            			if(VerificarParametrosUsr(request, response))
            			{
            				AgregarCambiarUsr(request, response, "CAMBIAR");
            				return;
            			}
            			irApag("/forsetiweb/administracion/adm_variables_dlg.jsp", request, response);
            			return;
            		}
            	}
            	else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
            	{
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		if(set.getAbsRow(0).getDeSistema() == 1)
            			irApag("/forsetiweb/administracion/adm_variables_dlg.jsp", request, response);
            		else
            			irApag("/forsetiweb/administracion/adm_variables_dlg_usr.jsp", request, response);
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
    public boolean VerificarParametrosUsr(HttpServletRequest request, HttpServletResponse response)
	  	      throws ServletException, IOException
	{
	  	short idmensaje = -1; String mensaje = "";
	  	// Verificacion
	  	if(request.getParameter("idvariable") != null && request.getParameter("descripcion") != null
	  			&& request.getParameter("ventero") != null  && request.getParameter("vdecimal") != null &&
	  					request.getParameter("vfecha") != null  && request.getParameter("valfanumerico") != null &&
	  			!request.getParameter("idvariable").equals("") && !request.getParameter("descripcion").equals(""))
	  	{
	  		if(!request.getParameter("idvariable").substring(0, 1).equals("_"))
	  		{
	  			idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR",3); //ERROR: Una variable de usuario debe de empezae forzosamente con el caracter de guión bajo ( _ )
		  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
		  		return false;
	  		}
	  		
	  		if(request.getParameter("proceso").equals("CAMBIAR_VARIABLE"))
	  		{
	  			JAdmVariablesSet set = new JAdmVariablesSet(request);
	  			set.m_Where = "ID_Variable = '" + p(request.getParameter("idvariable")) + "'";
	  			set.Open();
	  			if(set.getAbsRow(0).getDeSistema() == 1)
	  			{
	  				idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR",4); //ERROR: No se puede cambiar una variable de sistema, con parámetros de variable de usuario.
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
      
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	JAdmVariablesSet set = new JAdmVariablesSet(request);
    	set.m_Where = "ID_Variable = '" + p(request.getParameter("id")) + "'";
    	set.Open();
		
		if(set.getNumRows() == 0 || request.getParameter("valor") == null || request.getParameter("valor").equals(""))
  	    {
			idmensaje = 3; mensaje =  JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR"); //"ERROR: La variable no contiene ningun valor";
			getSesion(request).setID_Mensaje(idmensaje, mensaje);
			return false;
	    }
		else
		{
			String tipo = JUtil.Elm(set.getAbsRow(0).getTipo(),1);
		
			if(tipo.equals("BOOL"))
			{
				if(!request.getParameter("valor").equals("0") && !request.getParameter("valor").equals("1"))
				{
					idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR"); //"ERROR: La variable verdadera/falsa es nula";
					getSesion(request).setID_Mensaje(idmensaje, mensaje);
					return false;
				}
			}
			else if(tipo.equals("INT"))
			{
				boolean isint = true,isini = true,isfin = true;
				int var = 0,ini = 0,fin = 0;
				try{var = Integer.parseInt(request.getParameter("valor"));}catch(NumberFormatException e){isint = false;} 
				try{ini = Integer.parseInt(JUtil.Elm(set.getAbsRow(0).getTipo(),2));}catch(NumberFormatException e){isini = false;} 
				try{fin = Integer.parseInt(JUtil.Elm(set.getAbsRow(0).getTipo(),3));}catch(NumberFormatException e){isfin = false;} 
				System.out.println(var + " " + ini + " " + fin + " " + isint + " " + isini + " " + isfin );
				if(!isint)
				{
					idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR");
					getSesion(request).setID_Mensaje(idmensaje, mensaje);
					return false;
				}
				else if((isini && var < ini) || (isfin && var > fin))
				{
					idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR",2); //"ERROR: La variable entera esta fuera del rango permitido";
					getSesion(request).setID_Mensaje(idmensaje, mensaje);
					return false;
				}
			}
			else if(tipo.equals("NUMERIC"))
			{
				boolean isfloat = true,isini = true,isfin = true;
				float var = 0.0F,ini = 0.0F,fin = 0.0F;
				try{var = Float.parseFloat(request.getParameter("valor"));}catch(NumberFormatException e){isfloat = false;} 
				try{ini = Float.parseFloat(JUtil.Elm(set.getAbsRow(0).getTipo(),2));}catch(NumberFormatException e){isini = false;} 
				try{fin = Float.parseFloat(JUtil.Elm(set.getAbsRow(0).getTipo(),3));}catch(NumberFormatException e){isfin = false;} 
				
				if(!isfloat)
				{
					idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR"); //"ERROR: La variable numérica es nula";
					getSesion(request).setID_Mensaje(idmensaje, mensaje);
					return false;
				}
				else if((isini && var < ini) || (isfin && var > fin))
				{
					idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_VARIABLES", "DLG", "MSJ-PROCERR",2); //"ERROR: La variable numerica esta fuera del rango permitido";
					getSesion(request).setID_Mensaje(idmensaje, mensaje);
					return false;
				}
			}
			else if(tipo.equals("CC"))
			{
				JPublicContCatalogSetV2 num = new JPublicContCatalogSetV2(request);
				num.m_Where = "Numero = '" + p(JUtil.obtCuentas(request.getParameter("valor"),(byte)19)) + "'";
  			  	num.Open();

  			  	if(num.getNumRows() > 0)
  			  	{
  			  		if(num.getAbsRow(0).getAcum() == true)
  			  		{
  			  			idmensaje = 1; mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES", "DLG", "MSJ-PROCERR2",5); //"PRECAUCION: La cuenta contable para este concepto existe, pero no se puede agregar porque es una cuenta acumilativa <br>";
  			  			getSesion(request).setID_Mensaje(idmensaje, mensaje);
  			  			return false;
  			  		}
  			  	}
  			  	else
  			  	{
  			  		idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES", "DLG", "MSJ-PROCERR3"); //"ERROR: La cuenta contable para este concepto no existe <br>";
  			  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  			  		return false;
  			  	}
			}
		
			return true;
		}
	
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JAdmVariablesSet set = new JAdmVariablesSet(request);
    	set.m_Where = "ID_Variable = '" + p(request.getParameter("id")) + "'";
    	set.Open();
		String tipo = JUtil.Elm(set.getAbsRow(0).getTipo(),1);
		String ventero ="null", vdecimal = "null", vfecha = "null", valfanumerico = "''";
		
		if(tipo.equals("BOOL") || tipo.equals("INT"))
			ventero = "'" + p(request.getParameter("valor")) + "'";
		else if(tipo.equals("NUMERIC") || tipo.equals("DECIMAL"))
			vdecimal = "'" + p(request.getParameter("valor")) + "'";
		else if(tipo.equals("DATE"))
			vfecha = "'" + p(JUtil.obtFechaSQL(request.getParameter("valor"))) + "'";
		else if(tipo.equals("TIME"))
			vfecha = "'" + p(JUtil.obtFechaHoraSQL(request.getParameter("valor"))) + "'";
		else if(tipo.equals("CC"))
			valfanumerico = "'" + p(JUtil.obtCuentas(request.getParameter("valor"),(byte)19)) + "'";
		else
			valfanumerico = "'" + p(request.getParameter("valor")) + "'";
		
	   	JRetFuncBas rfb = new JRetFuncBas();
	  	
		String str = "SELECT * FROM sp_variables_cambiar('" + p(request.getParameter("id")) + "'," + ventero + "," + vdecimal + "," + vfecha + "," + valfanumerico + ") as ( err integer, res varchar, clave varchar ) ";

		//doDebugSQL(request, response, str);
		doCallStoredProcedure(request, response, str, rfb);

		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_VARIABLES_AGREGAR","AVAR|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/administracion/adm_variables_dlg.jsp", request, response);
	}
    
    public void AgregarCambiarUsr(HttpServletRequest request, HttpServletResponse response, String proc)
    	throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    		  	
    	String str = "SELECT * FROM sp_variables_" + (proc.equals("AGREGAR") ? "agregar_usr" : "cambiar_usr") + 
    			"('" + p(request.getParameter("idvariable")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("ventero")) + "','" + p(request.getParameter("vdecimal")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("vfecha"))) + "','" + p(request.getParameter("valfanumerico")) + "') as ( err integer, res varchar, clave varchar ) ";

    	doCallStoredProcedure(request, response, str, rfb);

    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_VARIABLES_AGREGAR","AVAR|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/administracion/adm_variables_dlg_usr.jsp", request, response);
    }

}
