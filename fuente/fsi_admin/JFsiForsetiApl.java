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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import javax.servlet.RequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forseti.JAccesoBD;
import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JSesionPrincipal;
//import forseti.sets.JPublicFormatosSetV2;

@SuppressWarnings("serial")
public abstract class JFsiForsetiApl extends JForsetiApl
{
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
      request.setAttribute("fsi_modulo", request.getRequestURI());
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_admin");
      if(princ == null)
      {
        princ = new JSesionPrincipal(false);
        ses.setAttribute("fsi_admin", princ);
      }
      // si ya tenia inicio de sesion, verifica que este registrado
      if(!princ.getRegistrado())
        irApag("/forsetiadmin/registro.jsp",request,response);

    }

    public void doDebugSQL(HttpServletRequest request, HttpServletResponse response, String Salida)
    	throws ServletException, IOException
	{
	  response.setContentType("text/html");
	  PrintWriter out = response.getWriter();
	  out.println(Salida);
	}  
    
    protected void verificaSesion(HttpServletRequest request)
        throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_admin");
      if(princ == null)
      {
        princ = new JSesionPrincipal(false);
        ses.setAttribute("fsi_admin", princ);
        //System.out.println("Intervalo máximo: "+ ses.getMaxInactiveInterval());
      }

    }
    
    protected void doCallStoredProcedure(HttpServletRequest request, HttpServletResponse response, String SQLCall, JRetFuncBas rfb)
		throws ServletException, IOException
	{
    	int idmensaje = -1; 
	    String mensaje = "", clave = "";
	    try
	    {
	       Connection con = JAccesoBD.getConexion();
	       con.setAutoCommit(false);
	       Statement s    = con.createStatement();
	       ResultSet rs   = s.executeQuery(SQLCall);
	       if(rs.next())
	       {
	    	   idmensaje = rs.getInt("ERR");
	           mensaje = rs.getString("RES");
	           clave = rs.getString("CLAVE");
	       }
	       s.close();
	       if(idmensaje == 0)
	    	   con.commit();
	       else
	    	   con.rollback();
	       
	       JAccesoBD.liberarConexion(con);
	
	       getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
           rfb.setRS(idmensaje, mensaje, clave);
	    }
	    catch(SQLException e)
	    {
	       e.printStackTrace(System.out);
	       //throw new RuntimeException(e.toString());
	       idmensaje = 3; mensaje = "ERROR de SQLCall: " + p(e.getMessage()); clave = "";
	       getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
           rfb.setRS(4, mensaje, clave);
	    }
	    
	}
    
    protected void doCallStoredProcedure(HttpServletRequest request, HttpServletResponse response, String TMP_TBL, String SQLCall, String DELClause, JRetFuncBas rfb)
		throws ServletException, IOException
	{
    	int idmensaje = -1; 
	    String mensaje = "", clave = "";
	    try
	    {
	    	
		    Connection con = JAccesoBD.getConexion();
		    con.setAutoCommit(false);
		    Statement s    = con.createStatement();
		    s.executeUpdate(TMP_TBL);
		    ResultSet rs   = s.executeQuery(SQLCall);
		    if(rs.next())
		    {
		    	idmensaje = rs.getShort("ERR");
		    	mensaje = rs.getString("RES");
		    	clave = rs.getString("CLAVE");
		    }
		    s.executeUpdate(DELClause);
		    s.close();
		    if(idmensaje == 0)
		    	con.commit();
		    else
		    	con.rollback();
	       
		    JAccesoBD.liberarConexion(con);
	
		    getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
		    rfb.setRS(idmensaje, mensaje, clave);
	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace(System.out);
		  	//throw new RuntimeException(e.toString());
		    idmensaje = 3; mensaje = "ERROR de SQLCall: " + p(e.getMessage()); clave = "";
		    getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
	        rfb.setRS(4, mensaje, clave);
	    }
		
	}  
   
    protected String getMensaje(HttpServletRequest request)
        throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_admin");

      return princ.getMensaje();
    }

    protected JSesionPrincipal getSesion(HttpServletRequest request)
        throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      return (JSesionPrincipal)ses.getAttribute("fsi_admin");
    }
 
    protected void irApag(String pagina, HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
		HttpSession ses = request.getSession(true);
	    JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_admin");
	    String destino;
		if(princ.isMobile())
			destino = "/forsetiadmin/registro_mob.jsp";
		else
			destino = pagina;
		
		System.out.println(destino);
		
		RequestDispatcher despachador = getServletContext().getRequestDispatcher(destino);
		despachador.forward(request,response);
	}
    
    /*
    protected short Imprimir(String SQLCab, String SQLDet, String ID_Formato, StringBuffer Mensaje,
                                                 HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException
    {
      JPublicFormatosSetV2 set = new JPublicFormatosSetV2(request);
      set.m_Where = "ID_Formato = '" + ID_Formato + "'";
      set.Open();

      if(set.getNumRows() < 1)
      {
        Mensaje.append("ERROR: Este formato de impresión no existe.");
        return -3;
      }

      request.setAttribute("sqlcab", SQLCab);
      request.setAttribute("sqldet", SQLDet);
      request.setAttribute("ID_Formato", ID_Formato);

      irApag("/forsetiadmin/impresion.jsp", request, response);

      return -1;
    }

     */
    
    
    
    
}
