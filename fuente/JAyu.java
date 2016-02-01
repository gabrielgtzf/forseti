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

/**
 * La clase JAyu se encarga de generar la ayuda del sistema a nivel local. Si estas desarrollando
 * un módulo en forseti para tu propia empresa, quizas vayas a necesitar documentar tu trabajo
 * y hacerlo parte de la documentación general. Esto es importante porque para accesar a la documentación 
 * del SAF o CEF, en cualquier instalación por defecto, el usuario final solo lo puede hacer través de la 
 * página web forseti.org.mx. Al correr este proceso, la documentación de tu sistema se guardará en la base 
 * de datos y será accesada localmente.
 * ¿Que hace este proceso exactamente?... Bien, cuando instalamos forseti en nuestro servidor, no se genera
 * la documentación dentro de la base de datos, por lo tanto, no es accesible a nivel local. Al ejecutar
 * este proceso, el servidor copiará la documentación contenida en el archivo .forseti_doc de nuestro directorio
 * bin de nuestra instalación en nuestra base de datos. Este archivo contiene básicamente toda la estructura de 
 * la documentación del sistema. Una vez ingresada a la base de datos, simplemente le indicará al sistema que 
 * la revisión de la documentación desde el SAF o CEF se hará ahora a través del sistema local y no en el portal
 * forseti.org.mx, esto último, modificando la variable de sistema URLAYUDA que contendrá la nueva ruta de la
 * documentación estableciendola en ../forsetidoc
 * ¿Como corro este proceso?...Primero que nada, hay que descomentar esta clase y compilarla para que sea 
 * accesible desde nuestro navegador (si es necesaro, hay que reiniciar tomcat). Ya que tengamos corriendo la
 * clase, lo único que debemos hacer es ingresar la siguiente ruta en nuestro navegador:
 * http[s]://[url-forseti]:[puerto-forseti]/servlet/GLBAyu?url=[url-pgsql]&port=[puerto-pgsql]&pass=[clave-forseti]
 * Donde:
 * [url-forseti] Es la ruta de nuestra instalación tomcat.
 * [puerto-forseti] Es el puerto en que escucha tomcat.
 * [url-pgsql] Es la URL de la instalación de PostgreSQL 
 * [puerto-pgsql] Es el puerto en el que escucha PostgreSQL
 * [clave-forseti] Es la contraseña del usuario forseti dueño de la base de datos FORSETI_ADMIN el cual tiene los 
 * permisos para cambiar los objetos de esta base de datos.
 * Ya que este proceso se haya terminado, entonces se podrá accesar al SAF/Administración/Documentación de Ayuda,
 * para ir adaptando la documentación a las modificaciones del sistema.
 * 
 */

/* //Borra este inicio de comentario
import forseti.JUtil;
import java.io.BufferedReader;
import java.io.FileReader;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
*/ //Borra este fin de comentario

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class JAyu extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	//////////////////////////////////////////////////////////////////////////////////
    // Se encarga de actualizar la ayuda en la base de datos
    // genera una conexion natural con los datos establecidos
    // Solo se utiliza en desarrollo
	
	/* //Borra este inicio de comentario...
    
    	PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		out.println("Empezando la generación");
		try  
      	{
			String tabla = "", pagina = "", cuerpo = "", sql = "";
			int doing = 0;
			String driver = "org.postgresql.Driver";
          	String url = "jdbc:postgresql://" + request.getParameter("url") + ":" + request.getParameter("port") + "/FORSETI_ADMIN?user=forseti&password=" + request.getParameter("pass");
          	Class.forName(driver).newInstance();
      		Connection conn = DriverManager.getConnection(url);
      		Statement s = conn.createStatement();
      		// Ahora toma del archivo de documentacion
      		FileReader file = new FileReader("/usr/local/forseti/bin/.forseti_doc");
            BufferedReader buff = new BufferedReader(file);
            boolean eof  = false;
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
                		out.println(tabla);
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
                				out.println("PAGINA: " + pagina + "\n");
                				continue;
                			}
                			else if(line.equals("_CUERPO_PAG"))
                			{
                				cuerpo = "";
                			}
                			else if(line.equals("_FIN_PAG"))
                			{  
                				sql = "INSERT INTO TBL_AYUDA_PAGINAS\nVALUES('" + JUtil.Elm(pagina, 1) + "','" + JUtil.Elm(pagina, 2) + "','" + JUtil.Elm(pagina, 3) + "','" + q(cuerpo) + "','" + JUtil.Elm(pagina, 4) + "','" + JUtil.Elm(pagina, 5) + "'," + (JUtil.Elm(pagina, 6).equals("null") ? JUtil.Elm(pagina, 6) : "'" + JUtil.Elm(pagina, 6) + "'") + ");";
                				out.println(sql); //"CUERPO: " + cuerpo + "\n";
                				s.execute(sql);
                				out.println("----------------------------");
                				break;
                			}
                			else
                			{
                				out.println(doing++ + ":\t" + line);
                				cuerpo += line;
                			}
                		
                		} while(true);
                	}
                	else if(tabla.equals("_INI_TIPOS"))
                	{
                		out.println("TIPOS: " + line + "\n");
                		sql = "INSERT INTO TBL_AYUDA_TIPOS\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 2) + "');";
                		s.execute(sql);
                	}
                	else if(tabla.equals("_INI_SUBTIPOS"))
                	{
                		out.println("SUBTIPOS: " + line + "\n");
                		sql = "INSERT INTO TBL_AYUDA_SUBTIPOS\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 3) + "','" + JUtil.Elm(line, 2) + "');";
                		s.execute(sql);
                	}
                	else if(tabla.equals("_INI_PAGINAS_ENLACES"))
                	{
                		out.println("ENLACES: " + line + "\n");
                		sql = "INSERT INTO TBL_AYUDA_PAGINAS_ENLACES\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 2) + "');";
                		s.execute(sql);
                	}
                	else if(tabla.equals("_INI_PAGINAS_SUBTIPOS"))
                	{
                		out.println("PAGSUB: " + line + "\n");
                		sql = "INSERT INTO TBL_AYUDA_PAGINAS_SUBTIPOS\nVALUES('" + JUtil.Elm(line, 1) + "','" + JUtil.Elm(line, 2) + "');";
                		s.execute(sql);
                	}
                }
            }
            buff.close();
            file.close();
            buff = null;
            file = null;
            // Ahora actualiza la variable de ayuda
            sql = "UPDATE TBL_VARIABLES\nSET VAlfanumerico = '../forsetidoc/'\nWHERE ID_Variable = 'URLAYUDA';";
			out.println(sql);
			s.execute(sql);
			out.println("----------------------------");
            // Ahora, cierra la conexion......
            s.close();
      		conn.close();
      	}
      	catch(Throwable e)
      	{
      		out.println("ERROR: " + e.getMessage());
      	}
		out.println("Terminada la generacion de ayuda");
	}

    protected String q(String str)
    {
      	String res = "";

      	for(int i = 0; i < str.length(); i++)
      	{
        	if(str.charAt(i) == 39)
          		res += "''";
        	else
          		res += str.charAt(i);
      	}

      	return res;
      	
    */ //Borra este fin de comentario
    
	}
        
}