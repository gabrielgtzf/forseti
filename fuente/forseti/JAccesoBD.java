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

import java.sql.*;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**

* Se utiliza para crear las conexiones a la base de datos

* @author: Gabriel Gutierrez Fuentes

* @version: 4.0.0

* @see <a href = "http://www.forseti.org.mx" /> http://www.forseti.org.mx - Documentación del Sistema </a>

*/
public class JAccesoBD
{
	private static boolean init = false;
	private static Driver m_Driver;
	
	private static void init()
    {
        // Inicializacion de propiedades de configuracion
        try
        {
            m_Driver = (Driver)Class.forName("org.postgresql.Driver").newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
       
        init    = true;
    
    }

	/**

     * Obtiene la conexión a la base de datos Principal FORSETI_ADMIN

     * @param No requiere parámetros

     */

    public static Connection getConexion()
    {
        if (!init)
        {
            init();
        }
        
        //System.out.println("Intentando conectar: getConnection()");
        try
        {
      	  	String URL = "jdbc:postgresql://" + JUtil.getADDR() + ":" + JUtil.getPORT() + "/FORSETI_ADMIN?user=forseti&password=" + JUtil.getPASS();
            //System.out.println("Conectado a base de datos: URL: " + URL);
            Connection con = m_Driver.connect(URL, new Properties());
            
            return con;
        }
        catch (Throwable e)
        {
        	System.out.println(e.getMessage());
      	  	return null;
        }

    }
    
    /**

     * Obtiene la conexión a la base de datos a la cual se ha conectado esta sesión

     * @param request Es el requester de la sesión que contiene los datos de la base de datos (BD, Usuario, Contraseña)

     */
    public static Connection getConexionSes(HttpServletRequest request)
    {
    	if (!init)
    	{
    		init();
    	}
        //System.out.println("Intentando conectar: getConnectionSes()");
        HttpSession ses = request.getSession(true);
        JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");

//      Intentar la conexion.
        try
        {
        	String URL = "jdbc:postgresql://" + JUtil.getADDR() + ":" + JUtil.getPORT() + "/" + princ.getConBD() + "?user=" + princ.getUser() + "&password=" + princ.getPassword();
        	//System.out.println("Conectado a base de datos: " + URL);
            Connection con = m_Driver.connect(URL, new Properties());
      	    
            return con;
        }
        catch (Throwable e)
        {
        	System.out.println(e.getMessage());
        	return null;
        }
    }
    
    /**

     * Obtiene la conexión a la base de datos a la cual se ha conectado esta sesión, pero la inicia como una conexión del usuario principal forseti

     * @param request Es el requester de la sesión que contiene los datos de la base de datos (BD)

     */
    public static Connection getConexionSesB2B(HttpServletRequest request)
    {
    	if (init == false)
    	{
    		init();
    	}
        //System.out.println("Intentando conectar: getConnectionSes()");
        HttpSession ses = request.getSession(true);
        JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_b2b");

//      Intentar la conexion.
        try
        {
        	String URL = "jdbc:postgresql://" + JUtil.getADDR() + ":" + JUtil.getPORT() + "/" + princ.getConBD() + "?user=" + princ.getUser() + "&password=" + princ.getPassword();
        	//System.out.println("Conectado a REF: " + URL);
            Connection con = m_Driver.connect(URL, new Properties());
      	    
            return con;
        }
        catch (Throwable e)
        {
        	System.out.println(e.getMessage());
        	return null;
        }
    }

    /**

     * Obtiene la conexión a la base de datos espacificada, conectándose como usuario principal forseti

     * @param BD Es el nombre de la base de datos a la cual nos conectaremos

     */

    public static Connection getConexion(String BD)
    {
    	//System.out.println("Intentando conectar a: " + bd);
        if (init == false)
        {
            init();
        }
        
//      Intentar la conexion.
        try
        {
        	String URL = "jdbc:postgresql://" + JUtil.getADDR() + ":" + JUtil.getPORT() + "/" + BD + "?user=forseti&password=" + JUtil.getPASS();
        	//System.out.println("Conectado a base de datos: " + URL);
            Connection con = m_Driver.connect(URL, new Properties());
      	    
            return con;
        }
        catch (Throwable e)
        {
        	System.out.println(e.getMessage());
        	return null;
        }

    }

    /**

     * Libera la conexión

     * @param con Es el objeto al cual apunta la conección que se pretende liberar

     */
    public static void liberarConexion(Connection con)
    {
        try
        {
           con.close();
           //System.out.println("coneccion liberada");
        }
        catch (Throwable e)
        {
      	  	System.out.println(e.getMessage());
        }

    }
}
