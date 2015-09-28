package fsi_b2b;

import forseti.*;



import java.io.IOException;

import java.sql.Connection;


import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.sets.JAdmVariablesSet;

@SuppressWarnings("serial")
public class JFsiB2BRegistro extends JFsiB2BApl
{
	
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
    bd.m_Where = "IP = '" + request.getRemoteAddr() + "' and FechaDesde <= '" + JUtil.obtFechaHoraSQL(fechaDesde) + "' and FechaHasta > '" + JUtil.obtFechaHoraSQL(fechaDesde) + "'";
    bd.Open();
    
    if(bd.getNumRows() > 0) // Existe en la base de datos logueada con esta IP...
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
      	  
      	  	if(request.getParameter("basededatos") == null || request.getParameter("basededatos").equals(""))
      	  	{
     	        res = false; idmensaje = 1; mensaje += JUtil.Msj("GLB","REGISTRO","SESION","PARAM",3);
      	  	}
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
      	  		// ~~* es igual al signo = de sql sin verificar mayusculas o minusculas Ej: Ver = ver dará falso y Ver ~~* ver Dará verdadero
      	  		JBDRegistradasSet bdr = new JBDRegistradasSet(request);
      	  		bdr.ConCat(true);
      	  		bdr.m_Where = "Nombre = 'FSIBD_" + p(request.getParameter("basededatos")) + "'";
      	  		bdr.Open();

      	  		if(bdr.getNumRows() == 1) // existe la base de datos, Debe buscar ahora el usuario (cliente) y el password, y asignar a la sesion los parametros
      	  		{
      	  			getSesion(request).setConBD(bdr.getAbsRow(0).getNombre(),bdr.getAbsRow(0).getUsuario(),bdr.getAbsRow(0).getPassword());
	                
    	        	JUsuariosSetV2 usr = new JUsuariosSetV2(request);
    	        	usr.ConCat(2);
    	        	usr.m_Where = "ID_Usuario = '" + p(request.getParameter("usuario")) + "' and Password = '" + p(request.getParameter("password")) + "'";
    	        	usr.Open();
    	        	if(usr.getNumRows() == 1) // existe el usuario
    	        	{
    	        		 idmensaje = -1; mensaje = "";
    	        		 JSesionPrincipal pr = getSesion(request);
    	        		 pr.setID_Mensaje(idmensaje,mensaje);
    	        		 pr.setRegistrado(true);
    	        		 pr.setID_Usuario(usr.getAbsRow(0).getID_Usuario(), "REF", request);
    	        		 pr.setBDCompania(bdr.getAbsRow(0).getNombre().substring(6));
    	        		 pr.setNombreUsuario(usr.getAbsRow(0).getNombre());
    	        		 pr.setNombreCompania(bdr.getAbsRow(0).getCompania());
    	        		
    	        		// Ahora asigna ciertas variables de sistema.
	                	JAdmVariablesSet setVar = new JAdmVariablesSet(request);
	                	setVar.m_Where = "ID_Variable = 'CONF_CC'";
	                	setVar.ConCat(2);
	                	setVar.Open();
	                	pr.setNivelCC((byte)setVar.getAbsRow(0).getVEntero());
	                	//Aqui registra otras variables enteras
	                	//pr.setEntero(setInt.getAbsRow(iu).getID_Entero(), Long.toString(setInt.getAbsRow(iu).getValor()));
	                		
    	        		 // Ahora registra la sesion en la base de datos
    	        		 // Aqui se registra el host del cual se intentan conectar
    	            	 
    	        		 String SQL = "INSERT INTO TBL_REGISTROS\nVALUES(";
    	        		 SQL += "default,'" + request.getRemoteAddr() + "','" + request.getRemoteHost() + "','" + JUtil.obtFechaSQL(fechaDesde) + "','" + q(request.getSession().getId()) + "','" + 
    	        		 	JUtil.obtFechaHoraSQL(fechaDesde) + "','" + JUtil.obtFechaHoraSQL(fechaDesde) + "','A','" + p(request.getParameter("basededatos")) + "','" +
    	        		 	p(request.getParameter("usuario")) + "','" + q(request.getParameter("password")) + "','REF')";
    	            	
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
    	            		idmensaje = 1;
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
    	            			p(request.getParameter("usuario")) + "','" + q(request.getParameter("password")) +"','REF')";
    	            		
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
    irApag("/forsetib2b/registro.jsp", request, response);

  } 	
}
