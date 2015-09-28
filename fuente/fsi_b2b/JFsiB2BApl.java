package fsi_b2b;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import javax.servlet.RequestDispatcher;
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
public abstract class JFsiB2BApl extends JForsetiApl
{
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
      request.setAttribute("b2b_modulo", request.getRequestURI());
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_b2b");
      if(princ == null)
      {
        princ = new JSesionPrincipal(false);
        ses.setAttribute("fsi_b2b", princ);
      }
      // si ya tenia inicio de sesion, verifica que este registrado
      if(!princ.getRegistrado())
        irApag("/forsetib2b/registro.jsp",request,response);

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
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_b2b");
      if(princ == null)
      {
        princ = new JSesionPrincipal(false);
        ses.setAttribute("fsi_b2b", princ);
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
	         idmensaje = rs.getShort("ERR");
	         mensaje = rs.getString("RES");
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
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_b2b");

      return princ.getMensaje();
    }

    protected JSesionPrincipal getSesion(HttpServletRequest request)
        throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      return (JSesionPrincipal)ses.getAttribute("fsi_b2b");
    }
    
}
