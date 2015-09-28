package fsi_b2b;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class JFsiB2BBarra extends JFsiB2BApl
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

      // simplemente obtiene un dato de la sesion para actualizarla y no dejar que expire
      int i = getSesion(request).getIntentosFallidos();
      if(i == 0)
    	  irApag("/forsetib2b/barra.jsp", request, response);
      else
    	  irApag("/forsetib2b/barra.jsp", request, response);
    
    }

}
