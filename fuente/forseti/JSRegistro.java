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
package forseti;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.sets.JAdmVariablesSet;
import forseti.sets.JUsuariosPermisosCatalogoSet;
import forseti.sets.JUsuariosPermisosSetV2;
import forseti.sets.JUsuariosSubmoduloRoles;
//import forseti.sets.JUsuariosPermisosCatalogoSet;
//import forseti.sets.JUsuariosPermisosSetV2;


@SuppressWarnings("serial")
public class JSRegistro extends JForsetiApl
{
	public void init()
	{
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      verificaSesion(request);
      String mensaje = ""; short idmensaje = -1;
      // Checa que no esté bloqueado este cliente
      Calendar fechaDesde = GregorianCalendar.getInstance();          
      Calendar fechaHasta = GregorianCalendar.getInstance();
      fechaHasta.add(Calendar.HOUR,3);
   
      JBDRegistrarSet bd = new JBDRegistrarSet(request);
      bd.ConCat(true);
      bd.m_Where = "IP = '" + p(request.getRemoteAddr()) + "' and FechaDesde <= '" + p(JUtil.obtFechaHoraSQL(fechaDesde)) + "' and FechaHasta > '" + p(JUtil.obtFechaHoraSQL(fechaDesde)) + "'";
      bd.Open();
      
      if(JUtil.getREINICIAR())
      {
    	  idmensaje = 1;
    	  mensaje = "PRECAUCION: El servidor debe ser reiniciado porque se actualizó recientemente... No se puede iniciar el CEF. Avisa al administrador del sistema de esta situacion";
    	  request.setAttribute("BLOQ", "true");
      }
      else if(JUtil.getSesionAdmin(request) != null || JUtil.getSesionB2B(request) != null)
      {
    	  idmensaje = 1;
    	  mensaje = "PRECAUCION: Ya cuentas con una sesión abierta en el SAF con este mismo navegador... No se puede iniciar el CEF por ahora, cierra tu sesion del SAF, reinicia tu navegador y por último intenta entrar de nuevo al CEF";
    	  request.setAttribute("BLOQ", "true");
      }
      else if(bd.getNumRows() > 0) // Existe en la base de datos logueada con esta IP...
      {
    	  if(bd.getAbsRow(0).getStatus().equals("B"))
    	  {
      			idmensaje = 3;
      			mensaje = JUtil.Msj("GLB","REGISTRO","SESION","BLOQ", 1) + " " + JUtil.obtFechaTxt(bd.getAbsRow(0).getFechaHasta(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(bd.getAbsRow(0).getHoraHasta(), "HH:mm");
    	  }
    	  else
    	  {
      			idmensaje = 3;
      			mensaje = JUtil.Msj("GLB","REGISTRO","SESION","BLOQ", 2) + " " + JUtil.obtFechaTxt(bd.getAbsRow(0).getFechaDesde(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(bd.getAbsRow(0).getHoraDesde(), "HH:mm");
    	  } 
    	  request.setAttribute("BLOQ", "true");
      }
      else
      {
          if(request.getParameter("basededatos") != null && 
        		  request.getParameter("usuario") != null && request.getParameter("password") != null) // Inicio desde 0
          {
        	  // Codigo principal. Aqui verifica los datos y se fija si pasa o no
        	  boolean res = true; 
        	  
        	  if(request.getParameter("basededatos") == null || request.getParameter("basededatos").equals("") )
        	  {
       	        res = false; idmensaje = 1; mensaje += JUtil.Msj("GLB","REGISTRO","SESION","PARAM",3);
       	      }
       	      if(request.getParameter("usuario") == null || request.getParameter("usuario").equals("") || 
       	    		request.getParameter("usuario").substring(0, 4).compareToIgnoreCase("cef-") == 0   )
       	      {
       	        res = false; idmensaje = 1; mensaje += JUtil.Msj("GLB","REGISTRO","SESION","PARAM",1);
       	      }
      	      if(request.getParameter("password") == null || request.getParameter("password").equals(""))
       	      {
       	        res = false; idmensaje = 1; mensaje += JUtil.Msj("GLB","REGISTRO","SESION","PARAM",2);
       	      }
      	      
      	      // Verifica los parametros
      	      if(res == true)
      	      {
      	         boolean intento = true;
      	         // ~~* es igual al signo = de sql sin verificar mayusculas o minusculas Ej: Ver = ver dará falso y Ver ~~* ver Dará verdadero
      	         JBDRegistradasSet bdr = new JBDRegistradasSet(request);
      	         bdr.ConCat(true);
      	         bdr.m_Where = "Nombre = 'FSIBD_" + p(request.getParameter("basededatos")) + "'";
      	         bdr.Open();

      	         if(bdr.getNumRows() == 1) // existe la base de datos, Debe buscar ahora el usuario y el password, y asignar a la sesion los parametros
      	         {
      	        	 getSesion(request).setConBD(bdr.getAbsRow(0).getNombre(),bdr.getAbsRow(0).getUsuario(),bdr.getAbsRow(0).getPassword());
  	                
      	        	 if(request.getParameter("usuario").equals(bdr.getAbsRow(0).getUsuario()))
      	        	 {
      	        		if(request.getParameter("password").equals(bdr.getAbsRow(0).getPassword()))
      	        		{
      	        			// LOG ADMINISTRATIVO
      	        			idmensaje = -1; mensaje = "";
   	                		JSesionPrincipal pr = getSesion(request);
   	                		pr.setID_Mensaje(idmensaje,mensaje);
   	                		pr.setRegistrado(true);
   	                		pr.setID_Usuario("cef-su","CEF", request);
   	                		pr.setBDCompania(bdr.getAbsRow(0).getNombre().substring(6));
   	                		pr.setNombreUsuario(JUtil.Msj("GLB","GLB","GLB","SU",1));
   	                		pr.setNombreCompania(bdr.getAbsRow(0).getCompania());
   	                		
   	                		// ahora asigna los permisos
   	                		JUsuariosPermisosCatalogoSet setUsr = new JUsuariosPermisosCatalogoSet(request);
   	                		setUsr.Open();
   	   	    			 	for(int iu = 0; iu < setUsr.getNumRows(); iu++)
   	   	    			 		pr.setPermiso(setUsr.getAbsRow(iu).getID_Permiso(), "true");
   	   	    			 		   	    			
   	   	    			 	// Ahora asigna ciertas variables de sistema.
   	                		JAdmVariablesSet setVar = new JAdmVariablesSet(request);
   	                		setVar.m_Where = "ID_Variable = 'CONF_CC'";
   	                		setVar.Open();
   	                		pr.setNivelCC((byte)setVar.getAbsRow(0).getVEntero());
   	                		//Aqui registra otras variables enteras
   	                		//pr.setEntero(setInt.getAbsRow(iu).getID_Entero(), Long.toString(setInt.getAbsRow(iu).getValor()));
   	                		
   	                		// Ahora registra la sesion en la base de datos
   	                		// Aqui se registra el host del cual se intentan conectar
      	            	 
   	                		String SQL = "INSERT INTO TBL_REGISTROS\nVALUES(";
   	                		SQL += "default,'" + request.getRemoteAddr() + "','" + request.getRemoteHost() + "','" + JUtil.obtFechaSQL(fechaDesde) + "','" + q(request.getSession().getId()) + "','" + 
      	          			JUtil.obtFechaHoraSQL(fechaDesde) + "','" + JUtil.obtFechaHoraSQL(fechaDesde) + "','A','" + p(request.getParameter("basededatos")) + "','" +
      	          			p(request.getParameter("usuario")) + "','" + q(request.getParameter("password")) + "','CEF')";
      	            	
   	                		//System.out.println(SQL);
      	            		try
   	                		{
   	                			Connection con = JAccesoBD.getConexion();
   	                			Statement s    = con.createStatement();
   	                			s.executeUpdate(SQL);
   	                			s.close();
   	                			JAccesoBD.liberarConexion(con);
   	                		}
   	                		catch(SQLException e)
   	                		{
   	                			e.printStackTrace();
   	                			throw new RuntimeException(e.toString());
   	                		}
	      	               
   	                		request.setAttribute("BLOQ", "false");
      	        			// FIN LOG ADMINISTRATIVO
      	        		}
      	        		else
      	        		{
      	        			intento = false;
      	        		}
      	        	 }
      	        	 else
      	        	 {
      	                JUsuariosSetV2 usr = new JUsuariosSetV2(request);
   	                	usr.m_Where = "ID_Usuario = '" + p(request.getParameter("usuario")) + "' and Password = '" + q(request.getParameter("password")) + "'";
   	                	usr.Open();
   	                	if(usr.getNumRows() == 1) // existe el usuario
   	                	{
   	                		idmensaje = -1; mensaje = "";
   	                		JSesionPrincipal pr = getSesion(request);
   	                		pr.setID_Mensaje(idmensaje,mensaje);
   	                		pr.setRegistrado(true);
   	                		pr.setID_Usuario(usr.getAbsRow(0).getID_Usuario(), "CEF", request);
   	                		pr.setBDCompania(bdr.getAbsRow(0).getNombre().substring(6));
   	                		pr.setNombreUsuario(usr.getAbsRow(0).getNombre());
   	                		pr.setNombreCompania(bdr.getAbsRow(0).getCompania());
   	                		// ahora asigna los permisos del usuario
   	                		JUsuariosPermisosSetV2 setUsr = new JUsuariosPermisosSetV2(request);
   	                		setUsr.m_Where = "ID_Usuario = '" + p(request.getParameter("usuario")) + "'";
   	                		setUsr.Open();
   	                		for(int iu = 0; iu < setUsr.getNumRows(); iu++)
   	                		{
   	                			String valor = (setUsr.getAbsRow(iu).getPermitido()) ? "true" : "false";
   	                			pr.setPermiso(setUsr.getAbsRow(iu).getID_Permiso(), valor);
   	                		}
   	                		//ahora asigna los permisos de los que este usuario es miembro de
   	                		JUsuariosSubmoduloRoles setRol = new JUsuariosSubmoduloRoles(request);
   	                		setRol.m_Where = "ID_Usuario = '" + p(request.getParameter("usuario")) + "'";
   	                		setRol.Open();
   	                		for(int iu = 0; iu < setRol.getNumRows(); iu++)
   	                		{
   	                			if(setRol.getAbsRow(iu).getID_Rol().equals("cef-cont")) 
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'CONT' or ID_Permiso like 'CONT_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			if(setRol.getAbsRow(iu).getID_Rol().equals("cef-cats"))
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'INVSERV' or ID_Permiso like 'INVSERV_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-ban")) // Miembro de bancos
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'BANCAJ' or ID_Permiso like 'BANCAJ_BANCOS%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-caj")) // Miembro de cajas
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'BANCAJ' or ID_Permiso like 'BANCAJ_CAJAS%' or ID_Permiso like 'BANCAJ_VALES%' or ID_Permiso like 'BANCAJ_CIERRES%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-alm")) 
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'ALM' or ID_Permiso like 'ALM_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-comp")) 
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'COMP' or ID_Permiso like 'COMP_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-ven"))
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'VEN' or ID_Permiso like 'VEN_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-prod")) 
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'PROD' or ID_Permiso like 'PROD_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-nom"))
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'NOM' or ID_Permiso like 'NOM_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			if(setRol.getAbsRow(iu).getID_Rol().equals("cef-crm")) 
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'CRM' or ID_Permiso like 'CRM_%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-adm")) 
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'ADM' or (ID_Permiso like 'ADM_%' and ID_Permiso not like 'ADM_USUARIOS%')";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else if(setRol.getAbsRow(iu).getID_Rol().equals("cef-rol")) 
   	                			{
   	                				JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
   	    	                		pc.m_Where = "ID_Permiso = 'ADM' or ID_Permiso like 'ADM_USUARIOS%'";
   	                				pc.Open();
   	    	   	    			 	for(int pci = 0; pci < pc.getNumRows(); pci++)
   	    	   	    			 		pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
   	    	   	    			 	
   	                			}
   	                			else // miembro de otros roles no administrativos
   	                			{
   	                				JUsuariosPermisosSetV2 setEnrl = new JUsuariosPermisosSetV2(request);
   	                				setEnrl.m_Where = "ID_Usuario = '" + p(setRol.getAbsRow(iu).getID_Rol()) + "'";
   	                				setEnrl.Open();
   	                				for(int rl = 0; rl < setEnrl.getNumRows(); rl++)
   	                				{
   	                					String valor = (setEnrl.getAbsRow(rl).getPermitido()) ? "true" : "false";
   	                					pr.setPermisoDeRol(setEnrl.getAbsRow(rl).getID_Permiso(), valor);
   	                				}
   	                			}
   	                		}
   	                		
   	                		// Ahora asigna ciertas variables de sistema.
   	                		JAdmVariablesSet setVar = new JAdmVariablesSet(request);
   	                		setVar.m_Where = "ID_Variable = 'CONF_CC'";
   	                		setVar.Open();
   	                		pr.setNivelCC((byte)setVar.getAbsRow(0).getVEntero());
   	                		//Aqui registra otras variables enteras
   	                		//pr.setEntero(setInt.getAbsRow(iu).getID_Entero(), Long.toString(setInt.getAbsRow(iu).getValor()));
   	                		
   	                		// Ahora registra la sesion en la base de datos
   	                		// Aqui se registra el host del cual se intentan conectar
      	            	 
   	                		String SQL = "INSERT INTO TBL_REGISTROS\nVALUES(";
   	                		SQL += "default,'" + request.getRemoteAddr() + "','" + request.getRemoteHost() + "','" + JUtil.obtFechaSQL(fechaDesde) + "','" + q(request.getSession().getId()) + "','" + 
      	          			JUtil.obtFechaHoraSQL(fechaDesde) + "','" + JUtil.obtFechaHoraSQL(fechaDesde) + "','A','" + p(request.getParameter("basededatos")) + "','" +
      	          			p(request.getParameter("usuario")) + "','" + q(request.getParameter("password")) + "','CEF')";
      	            	
   	                		//System.out.println(SQL);
      	            	
   	                		try
   	                		{
   	                			Connection con = JAccesoBD.getConexion();
   	                			Statement s    = con.createStatement();
   	                			s.executeUpdate(SQL);
   	                			s.close();
   	                			JAccesoBD.liberarConexion(con);
   	                		}
   	                		catch(SQLException e)
   	                		{
   	                			e.printStackTrace();
   	                			throw new RuntimeException(e.toString());
   	                		}
	      	               
   	                		request.setAttribute("BLOQ", "false");

      	                }
      	                else
      	                {
      	                	intento = false;
      	                }
      	        	 }  
      	         }
      	         else
      	         {
      	        	 intento = false;
      	         }
      	              
      	         if(!intento)
      	         {
      	            	// Intento fallido. Registra en la base de datos de administracion el intento fallido,
      	            	// eL CUAL SE VA A REGISTRAR
      	            	JSesionPrincipal pr = getSesion(request);
      	            	pr.intentoFallido();
      	            	
      	            	if(pr.getIntentosFallidos() < 3)
      	            	{
      	            		idmensaje = 3;
      	            		mensaje = JUtil.Msj("GLB", "REGISTRO", "SESION", "FALSO", 1);
      	            		
      	            		request.setAttribute("BLOQ", "false");
      	            	}
      	            	else // Mas de tres intentos, Bloquea la sesion
      	            	{
      	            		// Aqui se registra el host del cual se intentaban registrar
      	            		idmensaje = 3;
      	            		mensaje = JUtil.Msj("GLB", "REGISTRO", "SESION", "ERROR", 1);
      	                    
      	            		String SQL = "INSERT INTO TBL_REGISTROS\nVALUES(";
      	            		SQL += "default,'" + request.getRemoteAddr() + "','" + request.getRemoteHost() + "','" + JUtil.obtFechaSQL(fechaDesde) + "','" + q(request.getSession().getId()) + "','" + 
      	            			JUtil.obtFechaHoraSQL(fechaDesde) + "','" + JUtil.obtFechaHoraSQL(fechaHasta) + "','B','" + p(request.getParameter("basededatos")) + "','" +
      	            			p(request.getParameter("usuario")) + "','" + q(request.getParameter("password")) +"','CEF')";
      	            		
      	            		// System.out.println(SQL);
      	            		
      	            		try
      	                    {
      	                       Connection con = JAccesoBD.getConexion();
      	                       Statement s    = con.createStatement();
      	                       s.executeUpdate(SQL);
      	                       s.close();
      	                       JAccesoBD.liberarConexion(con);
      	                    }
      	                    catch(SQLException e)
      	                    {
      	                       e.printStackTrace();
      	                       throw new RuntimeException(e.toString());
      	                    }
      	                    
      	                    request.setAttribute("BLOQ", "true");
      	                    
      	            	}
      	         }
      	       	  
      	          
      	      }
      	      else // res == true
      	      {
      	    	  request.setAttribute("BLOQ", "false");  
      	      }      	      
      	      
      	      ////////////////////////////////////////////////////////
          }
          else // res == true
  	      {
  	    	  request.setAttribute("BLOQ", "false");  
  	      }   
      }

      request.setAttribute("RGST","RGST");
      //////////////////////////////////////////
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/registro.jsp", request, response);

    }

}
