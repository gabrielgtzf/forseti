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
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUsuariosSetV2;
import forseti.JUtil;

public class JAdmUsuariosDlg extends JForsetiApl
{
 	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
		//Establece la sesion para cambio de contraseña
		if(request.getParameter("proceso") != null && request.getParameter("proceso").equals("CAMBIAR_CONTRASENA") &&
				getSesion(request).getEst("ADM_USUARIOS") == false)
		{
	        getSesion(request).EstablecerCEF(request, "adm_usuarios.png", "ADM_USUARIOS");
	  	  	getSesion(request).getSesion("ADM_USUARIOS").setParametros("", "", "", "", "", "");
	  	  	getSesion(request).getSesion("ADM_USUARIOS").setOrden(null,"");
	  	  	getSesion(request).getSesion("ADM_USUARIOS").setEspecial("");
		}
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String adm_usuarios_dlg = "";
      request.setAttribute("adm_usuarios_dlg",adm_usuarios_dlg);
      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	 if(request.getParameter("proceso").equals("CAMBIAR_CONTRASENA"))
         {
    		 if(getSesion(request).getID_Usuario().compareToIgnoreCase("cef-su") == 0)
     	  	 {
                 idmensaje = 3; mensaje = "ERROR: No se puede cambiar la contraseña del usuario de sistema. Esta se debe cambiar desde los parámetros de la base de datos (Empresa) en el SAF";
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
             }
    		 
    		 if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
    		 {
    			 // Verificacion
    			 if(VerificarParametrosPass(request, response))
    			 {
    				 CambiarPass(request, response);
    				 return;
    			 }
    			 irApag("/forsetiweb/administracion/adm_usuarios_dlg_pass.jsp", request, response);
    			 return;
    		 }
    		 else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
    		 {
    			 getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			 irApag("/forsetiweb/administracion/adm_usuarios_dlg_pass.jsp", request, response);
    			 return;
    		 }
         
        }
    	else if(request.getParameter("proceso").equals("AGREGAR_USUARIO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_USUARIOS_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR||||",mensaje);
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
            irApag("/forsetiweb/administracion/adm_usuarios_dlg.jsp", request, response);
            return;

          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/administracion/adm_usuarios_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_USUARIO"))
        {
        	if(!getSesion(request).getPermiso("ADM_USUARIOS_AGREGAR"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR||||",mensaje);
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
        				irApag("/forsetiweb/administracion/adm_usuarios_dlg.jsp", request, response);
        	            return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/administracion/adm_usuarios_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_USUARIO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_USUARIOS_ELIMINAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS_ELIMINAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS_ELIMINAR","AUSR||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	if(request.getParameter("id").compareToIgnoreCase("cef-su") == 0 ||
		        		  request.getParameter("id").substring(0, 4).compareToIgnoreCase("cef-") == 0 )
		  	  	{
  					idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR"); //"PRECAUCION: No se puede modificar permisos, enlaces, reportes o enrolamientos de cef-su, o cualquier otro nombre de rol de sistema";
  					getSesion(request).setID_Mensaje(idmensaje, mensaje);
  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  					return;
		  	  	}
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
        else if(request.getParameter("proceso").equals("PERMISOS_USUARIO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ADM_USUARIOS_AGREGAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR||||",mensaje);
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
        				if(request.getParameter("id").compareToIgnoreCase("cef-su") == 0 ||
        		        		  request.getParameter("id").substring(0, 4).compareToIgnoreCase("cef-") == 0 )
        		  	  	{
        		              idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR"); //"PRECAUCION: No se puede modificar permisos, enlaces, reportes o enrolamientos de cef-su, o cualquier otro nombre de rol de sistema";
        		              getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		              irApag("/forsetiweb/administracion/adm_usuarios_dlg_perm.jsp", request, response);
        		              return;
        		        }
        				
        				CambiarPermisos(request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/administracion/adm_usuarios_dlg_perm.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ENLACES_USUARIO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ADM_USUARIOS_AGREGAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR||||",mensaje);
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
        				if(request.getParameter("id").compareToIgnoreCase("cef-su") == 0 ||
      		        		  request.getParameter("id").substring(0, 4).compareToIgnoreCase("cef-") == 0 )
      		  	  		{
        					idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR"); //"PRECAUCION: No se puede modificar permisos, enlaces, reportes o enrolamientos de cef-su, o cualquier otro nombre de rol de sistema";
      		              	getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		              	irApag("/forsetiweb/administracion/adm_usuarios_dlg_perm.jsp", request, response);
      		              	return;
      		  	  		}
        				CambiarEnlaces(request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/administracion/adm_usuarios_dlg_enl.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("REPORTES_USUARIO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ADM_USUARIOS_AGREGAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR||||",mensaje);
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
        				if(request.getParameter("id").compareToIgnoreCase("cef-su") == 0 ||
        		        		  request.getParameter("id").substring(0, 4).compareToIgnoreCase("cef-") == 0 )
        		  	  	{
          					idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR"); //"PRECAUCION: No se puede modificar permisos, enlaces, reportes o enrolamientos de cef-su, o cualquier otro nombre de rol de sistema";
          					getSesion(request).setID_Mensaje(idmensaje, mensaje);
          					irApag("/forsetiweb/administracion/adm_usuarios_dlg_rep.jsp", request, response);
          					return;
        		  	  	}
        				CambiarReportes(request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/administracion/adm_usuarios_dlg_rep.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ENROL_USUARIO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ADM_USUARIOS_AGREGAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_USUARIOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR||||",mensaje);
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
        				if(request.getParameter("id").compareToIgnoreCase("cef-su") == 0 ||
        		        		  request.getParameter("id").substring(0, 4).compareToIgnoreCase("cef-") == 0 )
        		  	  	{
          					idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR"); //"PRECAUCION: No se puede modificar permisos, enlaces, reportes o enrolamientos de cef-su, o cualquier otro nombre de rol de sistema";
          					getSesion(request).setID_Mensaje(idmensaje, mensaje);
          					irApag("/forsetiweb/administracion/adm_usuarios_dlg_perm.jsp", request, response);
          					return;
        		  	  	}
        				CambiarEnrol(request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/administracion/adm_usuarios_dlg_enrl.jsp", request, response);
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
    
    public boolean VerificarParametrosPass(HttpServletRequest request, HttpServletResponse response)
    	  throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("contrasenaact") != null && request.getParameter("contrasena") != null  && request.getParameter("passconf") != null &&
    			!request.getParameter("contrasenaact").equals("") && !request.getParameter("contrasena").equals("")  && !request.getParameter("passconf").equals(""))
    	{
    		JUsuariosSetV2 usr = new JUsuariosSetV2(request);
           	usr.m_Where = "ID_Usuario = '" + p(getSesion(request).getID_Usuario()) + "'";
           	usr.Open();
           	
    		if(!request.getParameter("contrasenaact").equals(usr.getAbsRow(0).getPassword()))
    		{
    			idmensaje = 3; mensaje = "ERROR: La contraseña actual ingresada no coincide con tu contraseña actual registrada.<br>";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}
    		if(!request.getParameter("contrasena").equals(request.getParameter("passconf")))
    		{
    			idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR",2);//"ERROR: La contraseña y su confirmación no coinciden. <br>";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}
    		if(request.getParameter("contrasena").length() < 4 || request.getParameter("contrasena").compareToIgnoreCase(p(getSesion(request).getID_Usuario())) == 0)
    		{
    			idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR",3);//"PRECAUCION: La contraseña es demasiado sencilla, esta debe tener por lo menos 4 caracteres. <br>";
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
    
    public void CambiarPass(HttpServletRequest request, HttpServletResponse response)
    	      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	  	
    	String str = "SELECT * FROM sp_usuarios_cambiar('" + p(getSesion(request).getID_Usuario()) + "','" + q(request.getParameter("contrasena")) + "','" + p(getSesion(request).getNombreUsuario()) + 
    					"') as ( err integer, res varchar, clave varchar ) ";

    	doCallStoredProcedure(request, response, str, rfb);

    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/administracion/adm_usuarios_dlg_pass.jsp", request, response);
    	   
    }
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("elusuario") != null && request.getParameter("nombre") != null
          && request.getParameter("contrasena") != null  && request.getParameter("passconf") != null &&
          !request.getParameter("elusuario").equals("") && !request.getParameter("nombre").equals("")
          && !request.getParameter("contrasena").equals("")  && !request.getParameter("passconf").equals(""))
      {
          if(!request.getParameter("contrasena").equals(request.getParameter("passconf")))
          {
	          idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR",2);//"ERROR: La contraseña y su confirmación no coinciden. <br>";
	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          return false;
          }
          if(request.getParameter("contrasena").length() < 4 || request.getParameter("contrasena").compareToIgnoreCase(request.getParameter("elusuario")) == 0)
          {
              idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR",3);//"PRECAUCION: La contraseña es demasiado sencilla, esta debe tener por lo menos 4 caracteres. <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
          }
          
          if(request.getParameter("elusuario").compareToIgnoreCase("cef-su") == 0 ||
        		  request.getParameter("elusuario").substring(0, 4).compareToIgnoreCase("cef-") == 0 )
  	  	  {
              idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR");//"PRECAUCION: El usuario no puede llamarse cef-su, o cualquier otro nombre de usuario de sistema";
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

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
  	
		String str = "SELECT * FROM sp_usuarios_cambiar('" + p(request.getParameter("elusuario")) + "','" + q(request.getParameter("contrasena")) + "','" + p(request.getParameter("nombre")) + 
				"') as ( err integer, res varchar, clave varchar ) ";

		doCallStoredProcedure(request, response, str, rfb);

		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/administracion/adm_usuarios_dlg.jsp", request, response);
   
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
	
  		String str = "SELECT * FROM sp_usuarios_agregar('" + p(request.getParameter("elusuario")) + "','" + q(request.getParameter("contrasena")) + "','" + p(request.getParameter("nombre")) + 
  				"') as ( err integer, res varchar, clave varchar ) ";
 
  		doCallStoredProcedure(request, response, str, rfb);
  
  		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_USUARIOS_AGREGAR","AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
  		irApag("/forsetiweb/administracion/adm_usuarios_dlg.jsp", request, response);
      
    }

    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
  		String str = "SELECT * FROM sp_usuarios_eliminar('" + p(request.getParameter("id")) + "') as ( err integer, res varchar, clave varchar ) ";
 
  		doCallStoredProcedure(request, response, str, rfb);
  
  		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_USUARIOS_ELIMINAR","AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
   
    }

    
    @SuppressWarnings({ "rawtypes" })
	public void CambiarReportes(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String tbl = "CREATE TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_REPORTES (\n";
		tbl += "	ID_Report smallint NOT NULL \n";
		tbl += ");\n";
				
	    Enumeration nombresParam = request.getParameterNames();
	    while(nombresParam.hasMoreElements())
	    {
	        String nombreParam = (String)nombresParam.nextElement();
	        if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
	        	continue;
	        
	        String claveParam = nombreParam.substring(0,8);
	        String valorParam = nombreParam.substring(8);
	        if(claveParam.equals("PER_REP_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_REPORTES\n";
	        	tbl += "values('" + p(valorParam) + "');\n";
	        }
	    }
	
	    String str = "SELECT * FROM sp_usuarios_enlrep('" + p(request.getParameter("id")) + "' ) as ( err integer, res varchar, clave varchar ) ";
		
		JRetFuncBas rfb = new JRetFuncBas();
			
	    doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_USUARIOS_SUBMODULO_REPORTES ", rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_USUARIOS_AGREGAR", "AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/administracion/adm_usuarios_dlg_rep.jsp", request, response);
		
	}       
    
    
    @SuppressWarnings("rawtypes")
	public void CambiarEnlaces(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_BANCOS (\n";
		tbl += "	Tipo smallint NOT NULL ,\n";
		tbl += "	Clave smallint NOT NULL\n";
		tbl += ");\n";
		tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_BODEGAS (\n";
		tbl += "	ID_Bodega smallint NOT NULL\n";
		tbl += ");\n";
		tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_COMPRAS (\n";
		tbl += "	ID_EntidadCompra smallint NOT NULL \n";
		tbl += ");\n";
		tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_VENTAS (\n";
		tbl += "	ID_EntidadVenta smallint NOT NULL \n";
		tbl += ");\n";
		tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_PRODUCCION (\n";
		tbl += "	ID_EntidadProd smallint NOT NULL \n";
		tbl += ");\n";
		tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_NOMINA (\n";
		tbl += "	ID_Compania smallint NOT NULL ,\n";
		tbl += "	ID_Sucursal smallint NOT NULL\n";
		tbl += ");\n";
		tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_BLOCKS (\n";
		tbl += "	ID_Block smallint NOT NULL\n";
		tbl += ");\n";
	    
		Enumeration nombresParam = request.getParameterNames();
	    while(nombresParam.hasMoreElements())
	    {
	        String nombreParam = (String)nombresParam.nextElement();
	        if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
	        	continue;
	        
	        String claveParam = nombreParam.substring(0,8);
	        String valorParam = nombreParam.substring(8);
	        if(claveParam.equals("PER_BAN_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_BANCOS\n";
	        	tbl += "values('0','" + p(valorParam) + "');\n";
	        }
	        else if(claveParam.equals("PER_CAJ_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_BANCOS\n";
	        	tbl += "values('1','" + p(valorParam) + "');\n";
	        }
	        else if(claveParam.equals("PER_BOD_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_BODEGAS\n";
	        	tbl += "values('" + p(valorParam) + "');\n";
	        }
	        else if(claveParam.equals("PER_COM_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_COMPRAS\n";
	        	tbl += "values('" + p(valorParam) + "');\n";
	        }
	        else if(claveParam.equals("PER_VEN_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_VENTAS\n";
	        	tbl += "values('" + p(valorParam) + "');\n";
	        }
	        else if(claveParam.equals("PER_PRD_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_PRODUCCION\n";
	        	tbl += "values('" + p(valorParam) + "');\n";
	        }
	        else if(claveParam.equals("PER_NOM_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_NOMINA\n";
	        	tbl += "values('0','" + p(valorParam) + "');\n";
	        }
	        else if(claveParam.equals("PER_NOT_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_BLOCKS\n";
	        	tbl += "values('" + p(valorParam) + "');\n";
	        }
	
	    }
	
	    /*String str = "EXEC  sp_usuarios_enlaces '" + p(request.getParameter("id")) + "'";
		String drop = "DROP TABLE [#TMP_USUARIOS_SUBMODULO_BANCOS]\nDROP TABLE [#TMP_USUARIOS_SUBMODULO_BODEGAS]\n" + 
		"DROP TABLE [#TMP_USUARIOS_SUBMODULO_COMPRAS]\nDROP TABLE [#TMP_USUARIOS_SUBMODULO_VENTAS]\n" + 
		"DROP TABLE [#TMP_USUARIOS_SUBMODULO_PRODUCCION]\nDROP TABLE [#TMP_USUARIOS_SUBMODULO_NOMINA]\n";*/
		
		String str = "SELECT * FROM sp_usuarios_enlaces('" + p(request.getParameter("id")) + "' ) as ( err integer, res varchar, clave varchar ) ";
		String drop = "DROP TABLE _TMP_USUARIOS_SUBMODULO_BANCOS;\nDROP TABLE _TMP_USUARIOS_SUBMODULO_BODEGAS;\n" + 
			"DROP TABLE _TMP_USUARIOS_SUBMODULO_COMPRAS;\nDROP TABLE _TMP_USUARIOS_SUBMODULO_VENTAS;\n" + 
			"DROP TABLE _TMP_USUARIOS_SUBMODULO_PRODUCCION;\nDROP TABLE _TMP_USUARIOS_SUBMODULO_NOMINA;\nDROP TABLE _TMP_USUARIOS_SUBMODULO_BLOCKS;\n";
		JRetFuncBas rfb = new JRetFuncBas();
			
	    doCallStoredProcedure(request, response, tbl, str, drop, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_USUARIOS_AGREGAR", "AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/administracion/adm_usuarios_dlg_enl.jsp", request, response);
	}   

    @SuppressWarnings("rawtypes")
	public void CambiarEnrol(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_SUBMODULO_ROLES (\n";
		tbl += "	ID_Rol varchar(10) NOT NULL\n";
		tbl += ");\n";
		
	    Enumeration nombresParam = request.getParameterNames();
	    while(nombresParam.hasMoreElements())
	    {
	        String nombreParam = (String)nombresParam.nextElement();
	        if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
	        	continue;
	        
	        String claveParam = nombreParam.substring(0,8);
	        String valorParam = nombreParam.substring(8);
	        if(claveParam.equals("PER_ROL_"))
	        {
	        	tbl += "insert into _TMP_USUARIOS_SUBMODULO_ROLES\n";
	        	tbl += "values('" + p(valorParam) + "');\n";
	        }
	    }
	
	    String str = "SELECT * FROM sp_usuarios_enrol('" + p(request.getParameter("id")) + "' ) as ( err integer, res varchar, clave varchar ) ";
		
		JRetFuncBas rfb = new JRetFuncBas();
			
	    doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_USUARIOS_SUBMODULO_ROLES", rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_USUARIOS_AGREGAR", "AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/administracion/adm_usuarios_dlg_enrl.jsp", request, response);
	}   
    
    @SuppressWarnings({ "rawtypes" })
	public void CambiarPermisos(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	String tbl;			  
    	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_USUARIOS_PERMISOS (\n";
    	tbl += "ID_Permiso varchar(30) NOT NULL \n";
		tbl += ");\n";
		    	
        Enumeration nombresParam = request.getParameterNames();
        while(nombresParam.hasMoreElements())
        {
            String nombreParam = (String)nombresParam.nextElement();
            if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
            	continue;
            
            String claveParam = nombreParam.substring(0,8);
            String valorParam = nombreParam.substring(8);
            if(claveParam.equals("PER_PER_"))
            {
            	tbl += "insert into _TMP_USUARIOS_PERMISOS\n";
            	tbl += "values('" + p(valorParam) + "');\n";
            }
        }

        String str = "SELECT * FROM sp_usuarios_permisos('" + p(request.getParameter("id")) + "' ) as ( err integer, res varchar, clave varchar ) ";
		
		JRetFuncBas rfb = new JRetFuncBas();
			
	    doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_USUARIOS_PERMISOS ", rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_USUARIOS_AGREGAR", "AUSR|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/administracion/adm_usuarios_dlg_perm.jsp", request, response);
	
	}   

}
