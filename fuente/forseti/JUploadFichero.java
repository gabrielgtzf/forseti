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
import java.io.*;
//import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import java.util.*;

@SuppressWarnings("serial")
public class JUploadFichero extends HttpServlet 
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Forseti</title>");
        out.println("</head>");
        out.println("<body>");

        //System.out.println("Comenzamos procesamiento ficheros");

        procesaFicheros(request,out);

        out.println("</body>");
        out.println("</html>");
		
        out.close();
    }


    void depura(String cadena)
    {
		
        //System.out.println(cadena);
    }
  
     
	@SuppressWarnings("rawtypes")
	public boolean procesaFicheros(HttpServletRequest req, PrintWriter out ) 
    {
        try 
        {
            // construimos el objeto que es capaz de parsear la perición
            DiskFileUpload fu = new DiskFileUpload();

            // maximo numero de bytes
            fu.setSizeMax(1024*512); // 512 K

            depura("Ponemos el tamaño máximo");
            // tamaño por encima del cual los ficheros son escritos directamente en disco
            fu.setSizeThreshold(4096);

            // directorio en el que se escribirán los ficheros con tamaño superior al soportado en memoria
            fu.setRepositoryPath("/tmp");

            // ordenamos procesar los ficheros
            List fileItems = fu.parseRequest(req);

            if(fileItems == null)
            {
                depura("La lista es nula");
                return false;
            }
            
            // Iteramos por cada fichero

            Iterator i = fileItems.iterator();
            FileItem actual = null;
            depura("estamos en la iteración");

            while((actual = (FileItem)i.next()) != null)
            {
                String fileName = actual.getName();
                out.println("<br>Nos han subido el archivo: " + fileName);

                // construimos un objeto file para recuperar el trayecto completo
                File fichero = new File(fileName);
                depura("El nombre del fichero es " + fichero.getName());

                // nos quedamos solo con el nombre y descartamos el path
                fichero = new  File("../tomcat/webapps/ROOT/forsetidoc/IMG/" + fichero.getName());

                // escribimos el fichero colgando del nuevo path
                actual.write(fichero);
            }

        }
        catch(Exception e)
        {
            if(e != null)
            	depura("Error de Aplicación " + e.getMessage());
            
            return false;
        }

        return true;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException 
    {
        processRequest(request, response);
    }
}