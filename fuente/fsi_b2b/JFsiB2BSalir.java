package fsi_b2b;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class JFsiB2BSalir extends HttpServlet
{
    public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      ses.invalidate();
      
      RequestDispatcher despachador = getServletContext().getRequestDispatcher("/servlet/fsi_b2b.JFsiB2BRegistro");
      despachador.forward(request,response);
    
    }
}