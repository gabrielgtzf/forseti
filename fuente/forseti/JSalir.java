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
import javax.servlet.http.HttpServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SuppressWarnings("serial")
public class JSalir extends HttpServlet
{
    public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");
      String destino;
  	  if(princ.isMobile())
  		destino = "/servlet/CEFRegistro?mobile=1";
  	  else
  		destino = "/servlet/CEFRegistro";
  	  princ = null;
  	  ses.invalidate();
     
  	  System.out.println(destino);
  	
  	  RequestDispatcher despachador = getServletContext().getRequestDispatcher(destino);
      despachador.forward(request,response);
    
    }
}