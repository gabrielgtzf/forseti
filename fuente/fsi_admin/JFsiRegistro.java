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
package fsi_admin;

import forseti.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.sets.JUsuariosPermisosCatalogoSet;
import forseti.sets.JUsuariosPermisosSetV2;
import forseti.sets.JUsuariosSubmoduloRoles;


@SuppressWarnings("serial")
public class JFsiRegistro extends JFsiForsetiApl
{
	
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	// Inicializacion de propiedades de configuracion
       
    	if(JUtil.getINIT() == false)
		{
    		System.out.println("No se ha iniciado el servidor");
			IniciarServidor(request, response);
			return;
		}
    	else
    		System.out.println("Inicio de registro en SAF");
			
    	/*
    	try
        {
            FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_conf");
            BufferedReader buff     = new BufferedReader(file);
            boolean eof             = false;
            
            while(!eof)
            {
                String line = buff.readLine();
                if(line == null)
                {
                    eof = true;
                }
                else
                {
                	try
                	{
                		StringTokenizer st = new StringTokenizer(line,"=");
                		String key         = st.nextToken();
                		String value       = st.nextToken();
                		// Si no está iniciado el servidor con su contraseña establecida, Desvia el proceso a Iniciar Servidor
                		if(key.equals("INIT") && value.equals("false"))
                		{
                			buff.close();
                			IniciarServidor(request, response);
                			return;
                		}
                	}
                	catch(NoSuchElementException e)
                	{
                		continue;
                	}
                }
                //System.out.println(line);
            }
            buff.close();
            
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
		*/
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
      bd.m_Where = "IP = '" + request.getRemoteAddr() + "' and FechaDesde <= '" + JUtil.obtFechaHoraSQL(fechaDesde) + "' and FechaHasta > '" + JUtil.obtFechaHoraSQL(fechaDesde) + "'";
      bd.Open();
      
      if(JUtil.getREINICIAR())
      {
    	  idmensaje = 1;
    	  mensaje = "PRECAUCION: El servidor debe ser reiniciado porque se actualizo recientemente... No se puede iniciar el SAF. Avisa al administrador de actualizaciones de esta situacion";
    	  request.setAttribute("BLOQ", "true");
      }
      else if(JUtil.getSesion(request) != null || JUtil.getSesionB2B(request) != null)
      {
    	  idmensaje = 1;
    	  mensaje = "PRECAUCION: Ya cuentas con una sesión abierta en el CEF con este mismo navegador... No se puede iniciar el SAF por ahora, cierra tu sesion del CEF, reinicia tu navegador y por último intenta entrar de nuevo al SAF";
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
   
	      if(request.getParameter("usuario") != null && request.getParameter("password") != null) // Inicio desde 0
	      {
	       	  // Codigo principal. Aqui verifica los datos y se fija si pasa o no
	       	  boolean res = true; 
	        	  
	   	      if(request.getParameter("usuario") == null || request.getParameter("usuario").equals(""))
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
	     	     
	   	    	 if(request.getParameter("usuario").equals("fsi"))
	   	    	 {
	   	    		 //Intento de loguearse como Super Usuario
	   	    		 String pass = "";
	   	    		 String str = "SELECT * FROM  getfsipass() as pass";
	   	    		 try
	   	    		 {
	   	    			 
	   	    			 Connection con = JAccesoBD.getConexion();
	   	    			 Statement s    = con.createStatement();
	   	    			 ResultSet rs   = s.executeQuery(str);
	   	    			 if(rs.next())
	   	    				 pass = rs.getString("pass");
	   	    			 s.close();
	   	    			 JAccesoBD.liberarConexion(con);
	   	    		 }
	   	    		 catch(SQLException e)
	   	    		 {
	   	    			 e.printStackTrace();
	   	    			 throw new RuntimeException(e.toString());
	   	    		 }
	   	    	    	    	
	   	    		 if(request.getParameter("password").equals(pass)) // existe el usuario
	   	    		 {
	   	    			 idmensaje = -1; mensaje = "";
	      	       
	   	    			 JSesionPrincipal pr = getSesion(request);
	   	    			 pr.setID_Mensaje(idmensaje,mensaje);
	   	    			 pr.setRegistrado(true);
	   	    			 pr.setID_Usuario("saf-su", "SAF", request);
	   	    			 pr.setNombreUsuario("Super Usuario Forseti");
	   	    			 // ahora asigna los permisos
	   	    			 JUsuariosPermisosCatalogoSet setUsr = new JUsuariosPermisosCatalogoSet(request);
	   	    			 setUsr.ConCat(true);
	   	    			 setUsr.Open();
	   	    			 for(int iu = 0; iu < setUsr.getNumRows(); iu++)
	   	    				 pr.setPermiso(setUsr.getAbsRow(iu).getID_Permiso(), "true");
	   	    			 
	   	    			 // Por ultimo registra la sesion en la base de datos
	   	    			 // Aqui se registra el host del cual se intentan conectar
    	            	 
	   	    			 String SQL = "INSERT INTO TBL_REGISTROS\nVALUES(default,'" + request.getRemoteAddr() + "','" + request.getRemoteHost() + "','" + JUtil.obtFechaSQL(fechaDesde) + "','" + q(request.getSession().getId()) + "','" + 
	   	    			 JUtil.obtFechaHoraSQL(fechaDesde) + "','" + JUtil.obtFechaHoraSQL(fechaDesde) + "','A','FORSETI_ADMIN','fsi','n/d','SAF')";
	      	            	
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
	   	    	 else
	   	    	 {
	   	    		 JUsuariosSetV2 usr = new JUsuariosSetV2(request);
	   	    		 usr.ConCat(true);
	   	    		 usr.m_Where = "ID_Usuario = '" + p(request.getParameter("usuario")) + "' and Password = '" + q(request.getParameter("password")) + "'";
	   	    		 usr.Open();
	   	    	    	    	
	   	    		 if(usr.getNumRows() == 1) // existe el usuario
	   	    		 {
	   	    			 idmensaje = -1; mensaje = "";
	      	       
	   	    			 JSesionPrincipal pr = getSesion(request);
	   	    			 pr.setID_Mensaje(idmensaje,mensaje);
	   	    			 pr.setRegistrado(true);
	   	    			 pr.setID_Usuario(usr.getAbsRow(0).getID_Usuario(), "SAF", request);
	   	    			 pr.setNombreUsuario(usr.getAbsRow(0).getNombre());
	   	    			 // ahora asigna los permisos
	   	    			 JUsuariosPermisosSetV2 setUsr = new JUsuariosPermisosSetV2(request);
	   	    			 setUsr.ConCat(true);
	   	    			 setUsr.m_Where = "ID_Usuario = '" + p(request.getParameter("usuario")) + "'";
	   	    			 setUsr.Open();
	   	    			 for(int iu = 0; iu < setUsr.getNumRows(); iu++)
	   	    			 {
	   	    				 String valor = (setUsr.getAbsRow(iu).getPermitido()) ? "true" : "false";
	   	    				 pr.setPermiso(setUsr.getAbsRow(iu).getID_Permiso(), valor);
	   	    			 }
	   	    			 
	   	    			 //ahora asigna los permisos de los que este usuario es miembro de
	   	    			 JUsuariosSubmoduloRoles setRol = new JUsuariosSubmoduloRoles(request);
	   	    			 setRol.ConCat(true);
	   	    			 setRol.m_Where = "ID_Usuario = '" + p(request.getParameter("usuario")) + "'";
	   	    			 setRol.Open();
	   	    			 for(int iu = 0; iu < setRol.getNumRows(); iu++)
	   	    			 {
	   	    				 if(setRol.getAbsRow(iu).getID_Rol().equals("saf-regs")) 
	   	    				 {
	   	    					 JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
	   	    					 pc.ConCat(true);
	   	    					 pc.m_Where = "ID_Permiso = 'REGIST' or ID_Permiso like 'REGIST_%'";
	   	    					 pc.Open();
	   	    					 for(int pci = 0; pci < pc.getNumRows(); pci++)
	   	    						 pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
	    	   	    			 	
	   	    				 }
	   	    				 else if(setRol.getAbsRow(iu).getID_Rol().equals("saf-serv")) 
	   	    				 {
	   	    					 JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
	   	    					 pc.ConCat(true);
	   	    					 pc.m_Where = "ID_Permiso = 'ADMIN' or (ID_Permiso like 'ADMIN_%' and ID_Permiso not like 'ADMIN_USUARIOS%' and ID_Permiso not like 'ADMIN_AYUDA%')";
	   	    					 pc.Open();
	   	    					 for(int pci = 0; pci < pc.getNumRows(); pci++)
	   	    						 pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
	    	   	    			 	
	   	    				 }
	   	    				 else if(setRol.getAbsRow(iu).getID_Rol().equals("saf-rol")) 
	   	    				 {
	   	    					 JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
	   	    					 pc.ConCat(true);
	   	    					 pc.m_Where = "ID_Permiso = 'ADMIN' or ID_Permiso like 'ADMIN_USUARIOS%'";
	   	    					 pc.Open();
	   	    					 for(int pci = 0; pci < pc.getNumRows(); pci++)
	   	    						 pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
	    	   	    			 	
	   	    				 }
	   	    				 else if(setRol.getAbsRow(iu).getID_Rol().equals("saf-doc")) 
	   	    				 {
	   	    					 JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
	   	    					 pc.ConCat(true);
	   	    					 pc.m_Where = "ID_Permiso = 'ADMIN' or ID_Permiso like 'ADMIN_AYUDA%'";
	   	    					 pc.Open();
	   	    					 for(int pci = 0; pci < pc.getNumRows(); pci++)
	   	    						 pr.setPermisoDeRol(pc.getAbsRow(pci).getID_Permiso(), "true");
	    	   	    			 	
	   	    				 }
	   	    				 else // miembro de otros roles no de sistema
	   	    				 {
	   	    					 JUsuariosPermisosSetV2 setEnrl = new JUsuariosPermisosSetV2(request);
	   	    					 setEnrl.ConCat(true);
	   	    					 setEnrl.m_Where = "ID_Usuario = '" + p(setRol.getAbsRow(iu).getID_Rol()) + "'";
	   	    					 setEnrl.Open();
	   	    					 for(int rl = 0; rl < setEnrl.getNumRows(); rl++)
	   	    					 {
	   	    						 String valor = (setEnrl.getAbsRow(rl).getPermitido()) ? "true" : "false";
	   	    						 pr.setPermisoDeRol(setEnrl.getAbsRow(rl).getID_Permiso(), valor);
	   	    					 }
	   	    				 }
	   	    			 }
	                		
	   	    			 // Por ultimo registra la sesion en la base de datos
	   	    			 // Aqui se registra el host del cual se intentan conectar
	      	            	 
	   	    			 String SQL = "INSERT INTO TBL_REGISTROS\nVALUES(default,'" + request.getRemoteAddr() + "','" + request.getRemoteHost() + "','" + JUtil.obtFechaSQL(fechaDesde) + "','" + q(request.getSession().getId()) + "','" + 
	   	    			 JUtil.obtFechaHoraSQL(fechaDesde) + "','" + JUtil.obtFechaHoraSQL(fechaDesde) + "','A','FORSETI_ADMIN','" +
	      	          			p(request.getParameter("usuario")) + "','" + q(request.getParameter("password")) + "','SAF')";
	      	            	
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
	      	                    
	      	            String SQL = "INSERT INTO TBL_REGISTROS\nVALUES(default,'" + request.getRemoteAddr() + "','" + request.getRemoteHost() + "','" + JUtil.obtFechaSQL(fechaDesde) + "','" + q(request.getSession().getId()) + "','" + 
	      	            	JUtil.obtFechaHoraSQL(fechaDesde) + "','" + JUtil.obtFechaHoraSQL(fechaHasta) + "','B','FORSETI_ADMIN','" +
	      	            	p(request.getParameter("usuario")) + "','" + q(request.getParameter("password")) +"','SAF')";
	      	            		
	      	            System.out.println(SQL);
	      	            		
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
      irApag("/forsetiadmin/registro.jsp", request, response);

    }

    public void IniciarServidor(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	verificaSesion(request);
    	String mensaje = ""; short idmensaje = -1;
    	
    	// Si no se han mandado parametros simplemente desbloquea para poder iniciar la captura
    	if(request.getParameter("pass") != null && request.getParameter("addr") != null	&& request.getParameter("port") != null)
    	{
    		// Codigo principal. Aqui verifica los datos y se fija si pasa o no
    		boolean res = true; 
	        	  
    		if(request.getParameter("addr") == null || request.getParameter("addr").equals(""))
    		{
    			res = false; idmensaje = 1; mensaje += " No se ha introducido la IP del servidor<br>";
    		}
    		if(request.getParameter("port") == null || request.getParameter("port").equals(""))
    		{
    			res = false; idmensaje = 1; mensaje += " No se ha introducido el puerto de comunicación<br>";
    		}
    		if(request.getParameter("pass") == null || request.getParameter("pass").equals(""))
    		{
    			res = false; idmensaje = 1; mensaje += " No se ha introducido la contraseña del usuario forseti<br>";
    		}
    		
    		//Se han introducido los datos de comunicación, ahora checa si se puede conectar
    		//Si si se puede, generará la base de datos principal FORSETI_ADMIN para este cluster.
    		if(res == true)
    		{
    			String addr = p(request.getParameter("addr"));
    			String port = p(request.getParameter("port"));
    			String pass = p(request.getParameter("pass"));
    			String urlresp = p(request.getParameter("urlresp"));
    			String urltomcat = p(request.getParameter("urltomcat"));
    			
    			// genera una conexion natural con los datos establecidos
    			String driver = "org.postgresql.Driver";
              	String url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
              	Connection conn = null;
          		Statement s = null;
          		Connection connbd = null;
          		Statement sbd = null;
    			try
              	{
    				Class.forName(driver).newInstance();
    				conn = DriverManager.getConnection(url);
    				s = conn.createStatement();
                	// Ya se establecio la conexion, Apartir de aqui es crítico el proceso
    				// Se conecta a la base de datos principal creada FORSETI_ADMIN
    				
                   	if(urlresp == null || urlresp.equals(""))
                   	{
	                   	// No es un respaldo, entonces toma del archivo de inicio
	              		// bin/.forseti_init que contiene la estructura de inicio del servidor
	                    String sql = "";
	                    FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_init");
	                    BufferedReader buff     = new BufferedReader(file);
	                    boolean eof             = false;
	                    while(!eof)
	                    {
	                        String line = buff.readLine();
	                        if(line == null)
	                        	eof = true;
	                        else
	                        	sql += line + "\n";
	                    }
	                    buff.close();
	                    file.close();
	                    buff = null;
	                    file = null;
	                    // Ahora, ejecuta la larga consulta......
	                    System.out.println("CREANDO LA ESTRUCTURA DE LA BASE DE DATOS PRINCIPAL...");
	                    s.execute(sql);
	                    // Ahora toma del archivo de lenguaje segun sea el caso (Hasta ahora solo permite Español (es) En proximas actualizaciones será multiidioma)
	              		sql = "";
	                    file = new FileReader("/usr/local/forseti/bin/.forseti_es");
	                    buff = new BufferedReader(file);
	                    eof  = false;
	                    while(!eof)
	                    {
	                        String line = buff.readLine();
	                        if(line == null)
	                        	eof = true;
	                        else
	                        {
	                        	if(line.equals("__INIT"))
	                        	{
	                        		String alc = "", mod = "", sub = "", elm = "", msj1 = "", msj2 = "", msj3 = "", msj4 = "", msj5 = "";
	                        		for(int i = 1; i <= 9; i++)
	                        		{
	                        			line = buff.readLine();
	                        			switch(i)
	                        			{
	                        			case 1: msj1 = "'" + line + "'";
	                        			break;
	                        			case 2: msj2 = (line.equals("null") ? "null" : "'" + line + "'");
	                        			break;
	                        			case 3: msj3 = (line.equals("null") ? "null" : "'" + line + "'");
	                        			break;
	                        			case 4: msj4 = (line.equals("null") ? "null" : "'" + line + "'");
	                        			break;
	                        			case 5: msj5 = (line.equals("null") ? "null" : "'" + line + "'");
	                        			break;
	                        			case 6: alc = "'" + line + "'";
	                        			break;
	                        			case 7: mod = "'" + line + "'";
	                        			break;
	                        			case 8: sub = "'" + line + "'";
	                        			break;
	                        			case 9: elm = "'" + line + "'";
	                        			break;
	                        			}
	                        		} 
	                        		String sqllang = "INSERT INTO tbl_msj\nVALUES(";
	                        		sqllang += alc + "," + mod + "," + sub + "," + elm + "," + msj1 + "," + msj2 + "," + msj3 + "," + msj4 + "," + msj5 + ")";
	                        		//Aqui genera el registro
	                        		//System.out.println(sqllang);
	                                s.execute(sqllang);
	                        	}
	                        }
	                    }
	                    buff.close();
	                    file.close();
	                    buff = null;
	                    file = null;
	                    // Ahora toma del archivo de documentacion
	                    String tabla = "", pagina = "", cuerpo = ""; sql = "";
	        			file = new FileReader("/usr/local/forseti/bin/.forseti_doc");
	                    buff = new BufferedReader(file);
	                    eof  = false;
	                    while(!eof)
	                    {
	                        String line = buff.readLine();
	                        if(line == null)
	                        	eof = true;
	                        else
	                        {
	                        	if(line.equals("_INI_TIPOS") || line.equals("_FIN_TIPOS") || line.equals("_INI_SUBTIPOS") || line.equals("_FIN_SUBTIPOS")
	                        			|| line.equals("_INI_PAGINAS") || line.equals("_FIN_PAGINAS") || line.equals("_INI_PAGINAS_ENLACES") || line.equals("_FIN_PAGINAS_ENLACES")  
	                        			|| line.equals("_INI_PAGINAS_SUBTIPOS") || line.equals("_FIN_PAGINAS_SUBTIPOS"))
	                        	{
	                        		tabla = line;
	                        		continue;
	                        	}
	                        	else if(line.equals("") && !tabla.equals("_INI_PAGINAS"))
	                        		continue;
	                        	else if(tabla.equals("_INI_PAGINAS"))
	                        	{
	                        		do
	                        		{
	                        			line = buff.readLine();
	                        			if(line.equals("_FIN_PAGINAS"))
	                        			{
	                        				tabla = line;
	                        				break;
	                        			}
	                        			else if(line.equals("_INI_PAG"))
	                        			{
	                        				pagina = buff.readLine();
	                        				continue;
	                        			}
	                        			else if(line.equals("_CUERPO_PAG"))
	                        			{
	                        				cuerpo = "";
	                        			}
	                        			else if(line.equals("_FIN_PAG"))
	                        			{
	                        				sql = "INSERT INTO TBL_AYUDA_PAGINAS\nVALUES('" + JUtil.Elm(pagina, 1) + "','" + JUtil.Elm(pagina, 2) + "','" + JUtil.Elm(pagina, 3) + "','" + q(cuerpo) + "','" + JUtil.Elm(pagina, 4) + "','" + JUtil.Elm(pagina, 5) + "');";
	                        				s.execute(sql);
	                        				break;
	                        			}
	                        			else
	                        			{
	                        				cuerpo += line;
	                        			}
	                        		
	                        		} while(true);
	                        	}
	                        	else if(tabla.equals("_INI_TIPOS"))
	                        	{
	                        		sql = "INSERT INTO TBL_AYUDA_TIPOS\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 2) + "');";
	                        		s.execute(sql);
	                        	}
	                        	else if(tabla.equals("_INI_SUBTIPOS"))
	                        	{
	                        		sql = "INSERT INTO TBL_AYUDA_SUBTIPOS\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 3) + "','" + JUtil.Elm(line, 2) + "');";
	                        		s.execute(sql);
	                        	}
	                        	else if(tabla.equals("_INI_PAGINAS_ENLACES"))
	                        	{
	                        		sql = "INSERT INTO TBL_AYUDA_PAGINAS_ENLACES\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 2) + "');";
	                        		s.execute(sql);
	                        	}
	                        	else if(tabla.equals("_INI_PAGINAS_SUBTIPOS"))
	                        	{
	                        		sql = "INSERT INTO TBL_AYUDA_PAGINAS_SUBTIPOS\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 2) + "');";
	                        		s.execute(sql);
	                        	}
	                        }
	                    }
	                    buff.close();
	                    file.close();
	                    buff = null;
	                    file = null;
	                    System.out.println("CERRANDO ARCHIVOS...");
	                                     
                   	}
                   	else // Intenta cargar el servidor desde un archivo de respaldo
                   	{
                   		if(urltomcat != null && !urltomcat.equals(""))
                   		{
                   			File fresp = new File(urlresp);
                   			File ftomcat = new File(urltomcat + "/webapps");
                   			if(fresp.exists() && fresp.isFile() && ftomcat.exists() && ftomcat.isDirectory())
                   				RestaurarServidor(request, response, urlresp, urltomcat, addr, port, pass, conn, s, connbd, sbd);
                   			else
                   				throw new Exception("ERROR: Ruta de tomcat o del archivo zip erroneas");
                   		}
                   		else
                   			throw new Exception("ERROR: Ruta de tomcat o del archivo zip en blanco");
                   	}
                  
                   	//Ahora solo marca el archivo bin/.forseti_conf como iniciado el servidor
              		String conf = "INIT=true\nADDR=" + addr + "\nPORT=" + port + "\nPASS=" + pass + "\nLANG=" + JUtil.getLANG() + "\n";
              		FileWriter fw = new FileWriter("/usr/local/forseti/bin/.forseti_conf");
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(conf);
                    fw.close();
                    System.out.println("ARCHIVO INI MARCADO...");
                    //Termina el proceso critico.
                    ////////////////////////////////////////////
                    request.setAttribute("BLOQ", "false");
              		request.setAttribute("RGST","RGST");
                    idmensaje = 0;
              		if(urlresp == null || urlresp.equals(""))
              			mensaje = "El servidor se ha iniciado correctamente<br>Se ha dado de alta el usuario administrador del servicio forseti (fsi con clave fsi). Este usuario es el super usuario y puede realizar cualquier cosa sobre el SAF. Te sugerimos cambiar inmediatamente la clave y no usarlo a menos que sea estrictamente necesario. Despu&eacute;s de haber creado tu propio usuario administrativo, puedes usar este ultimo, para tareas dentro del SAF. Se sugiere tambi&eacute;n crear un usuario administrativo por cada empresa del CEF en lugar de usar el super usuario administrativo de la empresa creada, asi como usuarios para los dem&aacute; recursos del servicio forseti";
              		else
              			mensaje = "El servidor se ha restaurado correctamente<br>Se han dado de alta las empresas y usuarios en este servidor PostgreSQL. Es probable que se tenga que reiniciar el servicio tomcat antes de poder trabajar con el servidor como se tenia anteriormente.";
              		request.setAttribute("REST","REST");
          			getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	irApag("/forsetiadmin/iniciar_servidor.jsp", request, response);
              		return;
              	}
              	catch(Throwable e)
              	{
              		e.printStackTrace(System.out);
              		idmensaje = 3;
              		mensaje = "Sucedieron errores durante el inicio del servidor:<br>" + e.getMessage();
              		request.setAttribute("INIT", "false");
              	}
    			finally
    			{
    				// Ahora, cierra la conexion......
    				if(s != null)
    					try { s.close(); } catch (SQLException e) { }
    				if(conn != null)
    					try { conn.close(); } catch (SQLException e) { }
    				if(sbd != null)
    					try { sbd.close(); } catch (SQLException e) { }
    				if(connbd != null)
    					try { connbd.close(); } catch (SQLException e) { }
              		System.out.println("CONEXION CERRADA...");
                    
    			}
    		}
    		else
    			request.setAttribute("INIT", "false"); 	
    	}
    	else 
    		request.setAttribute("INIT", "false"); 
    	
    	
    	request.setAttribute("RGST","RGST");
    	//////////////////////////////////////////
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	irApag("/forsetiadmin/iniciar_servidor.jsp", request, response);
    }  

    public void RestaurarServidor(HttpServletRequest request, HttpServletResponse response, String urlresp, String urltomcat, String addr, String port, String pass, Connection conn, Statement s, Connection connbd, Statement sbd)
    	throws Exception
    {
    		
    	int indexdir = urlresp.lastIndexOf('/');
    	String pathresp = urlresp.substring(0, indexdir);
    	String respaldo = urlresp.substring(indexdir+1);
    	
    	//Extrae el nombre del respaldado
		int indexzip = respaldo.indexOf('.');
		String nomdir = respaldo.substring(0, indexzip);
		//Genera el directorio del respaldo para extraer ahi
		//NOTA: El respaldo debe estar en un directorio que el proceso tomcat7 pueda accesar
		File newdir = new File(pathresp + "/" + nomdir);
		newdir.mkdir();
		//Descomprime el respaldo en el nuevo directorio
		JZipUnZipUtil fzip = new JZipUnZipUtil();
		fzip.unZipDir(pathresp + "/" + respaldo, pathresp + "/" + nomdir);
		
		String urldump = pathresp + "/" + nomdir + "/" + nomdir + ".dump";
		String urlfsi = pathresp + "/" + nomdir + "/";
    	System.out.println("DUMP: " + urldump + "\nFSI: " + urlfsi);
    	//Empieza con restauracion de la la base de datos
    	String ERROR = "";
		JFsiScript sc = new JFsiScript();
		String CONTENT = "";
		CONTENT = "PGPASSWORD=" + pass + " psql --username=forseti --host=" + addr + " --port=" + port + " --dbname=FORSETI_ADMIN --file=" + urldump;
		System.out.println(CONTENT);
		sc.setVerbose(true);
		sc.setContent(CONTENT);
		sc.executeCommand();
		//ERROR += sc.getError();
				
		CONTENT = "";
		CONTENT += "rsync -av --stats " + urlfsi + "act /usr/local/forseti\n";
		CONTENT += "rsync -av --stats " + urlfsi + "bin /usr/local/forseti\n";
		CONTENT += "rsync -av --stats " + urlfsi + "log /usr/local/forseti\n";
		CONTENT += "rsync -av --stats " + urlfsi + "emp /usr/local/forseti\n";
		CONTENT += "rsync -av --stats " + urlfsi + "pac /usr/local/forseti\n";
		CONTENT += "rsync -av --stats " + urlfsi + "rec /usr/local/forseti\n";
		System.out.println(CONTENT);
		sc.setContent(CONTENT);
		sc.executeCommand();
		ERROR += sc.getError();
		if(!ERROR.equals(""))
		{
			throw new Exception(ERROR); 
		}
		//Termina el proceso critico. de FORSETI_ADMIN
		
		///////////////////////////////////////////////////////////////
		//Ahora restaura las bases de datos
		///////////////////////////////////////////////////////////////
		ResultSet rs = s.executeQuery("select nombre, password from TBL_BD where su = '3';");

		while (rs.next()) 
		{
			////////////////////////////////////////////////////////////////////////
			//Extrae el nombre de la empresa respaldada
			String bdresp = rs.getString("nombre");
			String nomdirbd = bdresp + nomdir.substring(13);
			
			urldump = pathresp + "/" + nomdir + "/" + nomdirbd + "/" + nomdirbd + ".dump";
			String urlemp =  pathresp + "/" + nomdir + "/" + nomdirbd + "/" + bdresp.substring(6) + "/";
	    	System.out.println("DUMP: " + urldump + "\nnEMP: " + urlemp);
	    		
			// Genera el usuario de la base de datos a 
		    String url = "jdbc:postgresql://" + addr + ":" + port + "/postgres?user=forseti&password=" + pass;
		    connbd = DriverManager.getConnection(url);
		    sbd = connbd.createStatement();
		    String	sql;
		    sql =  	"CREATE ROLE " + bdresp.substring(6).toLowerCase() + " \n" +
		    		"LOGIN ENCRYPTED PASSWORD '" + rs.getString("password") + "' \n" +
		    		"NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE NOREPLICATION;\n" +
		    		"GRANT " + bdresp.substring(6).toLowerCase() +  " TO forseti;";
    	    System.out.println(sql);
    	    sbd.execute(sql);
    	    sbd.close();
    	    connbd.close();
		    // Genera la base de datos del usuario
		    url = "jdbc:postgresql://" + addr + ":" + port + "/postgres?user=forseti&password=" + pass;
		    connbd = DriverManager.getConnection(url);
		    sbd = connbd.createStatement();
		    sql =	
		    "CREATE DATABASE \"" + rs.getString("nombre") + "\"\n" +
        	"	WITH OWNER = " + bdresp.substring(6).toLowerCase() + "\n" +
        	"	TEMPLATE = template0\n" +
        	"	ENCODING = 'UTF8'\n" +
        	"	LC_COLLATE = 'es_MX.UTF-8'\n" +
        	"	LC_CTYPE = 'es_MX.UTF-8'\n" +
        	"	CONNECTION LIMIT = -1;";
		    System.out.println(sql);
		    sbd.execute(sql);
		    sbd.close();
		    connbd.close();
		    // Se conecta a la base de datos principal creada para generar desde su respaldo
		    ERROR = "";
			CONTENT = "PGPASSWORD=" + rs.getString("password") + " psql --username=" + bdresp.substring(6).toLowerCase() + " --host=" + addr + " --port=" + port + " --dbname=" + rs.getString("nombre") + " --file=" + urldump;
			System.out.println(CONTENT);
			sc.setVerbose(true);
			sc.setContent(CONTENT);
			sc.executeCommand();
			ERROR += sc.getError();
			
			//copia los archivos emp
			ERROR = "";
			CONTENT = "rsync -av --stats " + urlemp + " /usr/local/forseti/emp/" + bdresp.substring(6);
			System.out.println(CONTENT);
			sc.setContent(CONTENT);
			sc.executeCommand();
			ERROR += sc.getError();
			if(!ERROR.equals(""))
			{
				throw new Exception(ERROR); 
			}
    		//Termina el proceso critico.
		}
		////////////////////////////////////////////////////////////////////////
		//copia ROOT.war a tomcat
		ERROR = "";
		CONTENT = "rsync -av --stats " + urlfsi + "ROOT.war " + urltomcat + "/webapps\n";
		System.out.println(CONTENT);
		sc.setContent(CONTENT);
		sc.executeCommand();
		ERROR += sc.getError();
		if(!ERROR.equals(""))
		{
			throw new Exception(ERROR); 
		}
		//Borra los archivos desempaquetados
    	System.out.println("BORRANDO ARCHIVOS: " + pathresp + "/" + nomdir);
    	File dir = new File(pathresp + "/" + nomdir);
    	File[] currList;
    	Stack<File> stack = new Stack<File>();
    	stack.push(dir);
    	while (!stack.isEmpty()) 
    	{
    		if (stack.lastElement().isDirectory()) 
    		{
    	        currList = stack.lastElement().listFiles();
    	        if (currList.length > 0) 
    	        {
    	            for (File curr: currList) 
    	            {
    	                stack.push(curr);
    	            }
    	        } 
    	        else 
    	        {
    	        	stack.pop().delete();
    	        }
    	    } 
    		else 
    		{
    			stack.pop().delete();
    	    }
    	}
    }

}
