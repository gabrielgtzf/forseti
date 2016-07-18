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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import forseti.sets.JProcessSet;

@SuppressWarnings("rawtypes")
public class JGestionArchivos 
{
	public static final short ERROR = 3;
	public static final short OKYDOKY = 0;
	
	//private JS3RegistrosExitososSet m_Set;
	private short m_StatusS3;
	private String m_Error;
	private String m_S3_USERNAME;
    private String m_S3_PASSWORD;
    private String m_HOST;    
    private String m_URL;
   	
    private String m_ID_MODULO;
    private String m_OBJIDS;
    private String m_IDSEP;
    private String m_Nombre;
    
    private Vector m_Archivos;
    
	public JGestionArchivos() 
	{
		m_StatusS3 = OKYDOKY;
		m_Error = "";
		m_Archivos = new Vector();
		
		try
		{
			FileReader file = new FileReader("/usr/local/forseti/bin/.forseti_pac");
			BufferedReader buff = new BufferedReader(file);
			boolean eof = false;
			while(!eof)
			{
				String line = buff.readLine();
				if(line == null)
					eof = true;
				else
				{
					try
					{
						StringTokenizer st = new StringTokenizer(line,"=");
						String key         = st.nextToken();
						String value       = st.nextToken();
						
						if(key.equals("SERV"))
							m_HOST = value;
						if(key.equals("PURL"))
							m_URL = value;
						else if(key.equals("PASS"))
							m_S3_PASSWORD = value;
						else if(key.equals("USER"))
							m_S3_USERNAME = value;
												
					}
					catch(NoSuchElementException e)
					{
						m_StatusS3 = ERROR;
						m_Error = "La informacion de conexión al servicio Amazon S3 forseti parece estar mal configurada";
					}
				}
				
			}
			buff.close();
			
		}
		catch (FileNotFoundException e1)
		{
			m_StatusS3 = ERROR;
    		m_Error = "Error de archivos S3: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			m_StatusS3 = ERROR;
    		m_Error = "Error de Entrada/Salida S3: " + e1.getMessage();
		}
	
	}
 
	public short getStatusS3()
	{
		return m_StatusS3;
	}
	
	public String getError()
	{
		return m_Error;
	}
	
	public Vector getArchivos()
	{
		return m_Archivos;
	}
	
	public String getID_MODULO()
	{
		return m_ID_MODULO;
	}
	
	public String getOBJIDS()
	{
		return m_OBJIDS;
	}
	
	public String getNombre()
	{
		return m_Nombre;
	}
	
	public String getIDSEP()
	{
		return m_IDSEP;
	}
	
	public void setOBJIDS(String OBJIDS)
	{
		m_OBJIDS = OBJIDS;
	}
	
	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}
	
	public void setIDSEP(String IDSEP)
	{
		m_IDSEP = IDSEP;
	}

	public void setID_MODULO(String ID_MODULO)
	{
		m_ID_MODULO = ID_MODULO;
	}

	//Esta funcion se encarga de todo el trabajo para enviar el archivo al JAwsS3Conn
	public void SubirArchivo(HttpServletRequest request)
			throws ServletException
	{
    	try
		{
    		FileItem actual = (FileItem)m_Archivos.elementAt(0);

    		//Ahora agrega el archivo al mensaje
			String charset = "UTF-8";
	        String requestURL = "https://" + m_URL + "/servlet/SAFAwsS3Conn";
	 
	        MultipartUtility multipart = new MultipartUtility(requestURL, charset);
	             
	        multipart.addHeaderField("User-Agent", "CodeJava");
	        multipart.addHeaderField("Test-Header", "Header-Value");
	             
	        multipart.addFormField("SERVER", m_HOST);
	        multipart.addFormField("DATABASE", JUtil.getSesion(request).getBDCompania());
	        multipart.addFormField("USER", m_S3_USERNAME);
	        multipart.addFormField("PASSWORD", m_S3_PASSWORD);
	        multipart.addFormField("ACTION", "SUBIR");
	        multipart.addFormField("ID_MODULO", m_ID_MODULO);
	        multipart.addFormField("OBJIDS", m_OBJIDS);
	        multipart.addFormField("IDSEP", m_IDSEP);
	        multipart.addFormField("NOMBRE", actual.getName());
	        multipart.addFormField("TAMBITES", Long.toString(actual.getSize()));
	        
	        JProcessSet setPer = new JProcessSet(null);
            String sql = "select coalesce(sum(tambites),0) as totbites from tbl_s3_registros_exitosos where servidor = '" + JUtil.p(m_HOST) + "' and bd = '" + JUtil.getSesion(request).getBDCompania() + "'";
            //System.out.println(sql);
            setPer.ConCat(true);
            setPer.setSQL(sql);
            setPer.Open();
          	
	        multipart.addFormField("TOTBITES", setPer.getAbsRow(0).getSTS("Col1"));
	        
	        multipart.addFilePart("fileUpload", actual);
	        InputStream response = multipart.connect();

	        // Recibe la respuesta del servidor, esta debe estar en formato xml
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document)builder.build(response);
			Element S3 = document.getRootElement();
			
			if(S3.getName().equals("S3"))
			{
				String SQL = "SELECT * FROM sp_s3_registros_exitosos('" + JUtil.q(m_HOST) + "','" + 
		    			  JUtil.q(JUtil.getSesion(request).getBDCompania()) + "','" + JUtil.q(m_ID_MODULO) + "','" + JUtil.q(m_OBJIDS) + "','" + JUtil.q(m_IDSEP) + "','" + JUtil.q(actual.getName()) + "'," + Long.toString(actual.getSize()) + ") as (RES integer);";
				Connection con = JAccesoBD.getConexion();
		    	Statement s    = con.createStatement();
		    	ResultSet rs   = s.executeQuery(SQL);
	  			if(rs.next())
	  			{
	  				int res = rs.getInt("RES");
	  				System.out.println("Resultado del archivo S3 en Cliente: " + res);
	  			}
		    	s.close();
		    	JAccesoBD.liberarConexion(con);
		    
		    	m_StatusS3 = OKYDOKY;
			}
			else if(S3.getName().equals("SIGN_ERROR"))// Significan errores
			{
				m_StatusS3 = ERROR;
				m_Error += "Codigo de Error JAwsS3Conn: " + S3.getAttribute("CodError") + "<br>" + S3.getAttribute("MsjError");
			}
			else
			{
				m_StatusS3 = ERROR;
				m_Error += "ERROR: El archivo recibido es un archivo XML, pero no parece ser una respuesta de archivo ni una respuesta de ERROR del servidor JAwsS3Conn. Esto debe ser investigado de inmediato, Contacta con tu intermediario de archivos AWS S3 Forseti para que te explique el problema";
			}
			
			multipart.disconnect();
			// Fin de archivo grabado
			///////////////////////////////////////////////////////////////////////////
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
    		m_Error = "Error de archivos S3: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
    		m_Error = "Error de Entrada/Salida S3: " + e1.getMessage();
		} 
    	catch (JDOMException e1) 
		{
    		e1.printStackTrace();
    		m_StatusS3 = ERROR;
    		m_Error = "Error de JDOMException S3: " + e1.getMessage();
		}
    	catch(SQLException e1)
	    {
    		e1.printStackTrace();
    		m_StatusS3 = ERROR;
    		m_Error = "Error de SQLException S3: " + e1.getMessage();
	    }
	}
	
	//Esta funcion se encarga de todo el trabajo para eliminar el archivo del JAwsS3Conn
	public void EliminarArchivo(HttpServletRequest request)
		throws ServletException
	{
		//System.out.println("GestionEliminarArchivo:" + m_Nombre + ":m_Nombre");
    	
		try
		{
			//Ahora agrega el archivo al mensaje
			String charset = "UTF-8";
			String requestURL = "https://" + m_URL + "/servlet/SAFAwsS3Conn";
		 
			MultipartUtility multipart = new MultipartUtility(requestURL, charset);
		             
			multipart.addHeaderField("User-Agent", "CodeJava");
			multipart.addHeaderField("Test-Header", "Header-Value");
		             
			multipart.addFormField("SERVER", m_HOST);
			multipart.addFormField("DATABASE", JUtil.getSesion(request).getBDCompania());
			multipart.addFormField("USER", m_S3_USERNAME);
			multipart.addFormField("PASSWORD", m_S3_PASSWORD);
			multipart.addFormField("ACTION", "ELIMINAR");
			multipart.addFormField("ID_MODULO", m_ID_MODULO);
			multipart.addFormField("OBJIDS", m_OBJIDS);
			multipart.addFormField("IDSEP", m_IDSEP);
			multipart.addFormField("NOMBRE", m_Nombre);
			
			JProcessSet setPer = new JProcessSet(null);
			String sql = "select coalesce(sum(tambites),0) as totbites from tbl_s3_registros_exitosos where servidor = '" + JUtil.p(m_HOST) + "' and bd = '" + JUtil.getSesion(request).getBDCompania() + "'";
            //System.out.println(sql);
            setPer.ConCat(true);
            setPer.setSQL(sql);
            setPer.Open();
          	
	        multipart.addFormField("TOTBITES", setPer.getAbsRow(0).getSTS("Col1"));
	        //Este parametro LN se manda solo para que el NOMBRE no se mande con una linea (LN) extra del return
			multipart.addFormField("LN", "LN");
		    
			InputStream response = multipart.connect();

			// Recibe la respuesta del servidor, esta debe estar en formato xml
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document)builder.build(response);
			Element S3 = document.getRootElement();
				
			if(S3.getName().equals("S3"))
			{
				String SQL = "SELECT * FROM sp_s3_registros_exitosos_eliminar('" + JUtil.q(m_HOST) + "','" + 
			    			  JUtil.q(JUtil.getSesion(request).getBDCompania()) + "','" + JUtil.q(m_ID_MODULO) + "','" + JUtil.q(m_OBJIDS) + "','" + JUtil.q(m_IDSEP) + "','" + JUtil.q(m_Nombre) + "') as (RES integer);";
				Connection con = JAccesoBD.getConexion();
				Statement s    = con.createStatement();
				ResultSet rs   = s.executeQuery(SQL);
				if(rs.next())
				{
					int res = rs.getInt("RES");
					System.out.println("Resultado de la eliminación del archivo S3 en Cliente: " + res);
				}
				s.close();
				JAccesoBD.liberarConexion(con);
			    
				m_StatusS3 = OKYDOKY;
			}
			else if(S3.getName().equals("SIGN_ERROR"))// Significan errores
			{
				m_StatusS3 = ERROR;
				m_Error += "Codigo de Error JAwsS3Conn: " + S3.getAttribute("CodError") + "<br>" + S3.getAttribute("MsjError");
			}
			else
			{
				m_StatusS3 = ERROR;
				m_Error += "ERROR: El archivo recibido es un archivo XML, pero no parece ser una respuesta de archivo ni una respuesta de ERROR del servidor JAwsS3Conn. Esto debe ser investigado de inmediato, Contacta con tu intermediario de archivos AWS S3 Forseti para que te explique el problema";
			}
				
			multipart.disconnect();
			
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
			m_Error = "Error de archivos S3: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
			m_Error = "Error de Entrada/Salida S3: " + e1.getMessage();
		} 
		catch (JDOMException e1) 
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
			m_Error = "Error de JDOMException S3: " + e1.getMessage();
		}
		catch(SQLException e1)
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
			m_Error = "Error de SQLException S3: " + e1.getMessage();
		}
	}
	
	public void DescargarArchivo(HttpServletRequest request, HttpServletResponse response, ServletContext context, String content_type)
			throws ServletException
	{
		//System.out.println("GestionDescargarArchivo:" + m_Nombre + ":m_Nombre");
		
		try
		{
			//Ahora agrega el archivo al mensaje
			String charset = "UTF-8";
			String requestURL = "https://" + m_URL + "/servlet/SAFAwsS3Conn";
			 
			MultipartUtility multipart = new MultipartUtility(requestURL, charset);
			
			multipart.addHeaderField("User-Agent", "CodeJava");
			multipart.addHeaderField("Test-Header", "Header-Value");
			
			multipart.addFormField("SERVER", m_HOST);
			multipart.addFormField("DATABASE", JUtil.getSesion(request).getBDCompania());
			multipart.addFormField("USER", m_S3_USERNAME);
			multipart.addFormField("PASSWORD", m_S3_PASSWORD);
			multipart.addFormField("ACTION", "DESCARGAR");
			multipart.addFormField("ID_MODULO", m_ID_MODULO);
			multipart.addFormField("OBJIDS", m_OBJIDS);
			multipart.addFormField("IDSEP", m_IDSEP);
			multipart.addFormField("NOMBRE", m_Nombre);
			
			JProcessSet setPer = new JProcessSet(null);
			String sql = "select coalesce(sum(tambites),0) as totbites from tbl_s3_registros_exitosos where servidor = '" + JUtil.p(m_HOST) + "' and bd = '" + JUtil.getSesion(request).getBDCompania() + "'";
            //System.out.println(sql);
            setPer.ConCat(true);
            setPer.setSQL(sql);
            setPer.Open();
          	
	        multipart.addFormField("TOTBITES", setPer.getAbsRow(0).getSTS("Col1"));
	        //Este parametro LN se manda solo para que el NOMBRE no se mande con una linea (LN) extra del return
			multipart.addFormField("LN", "LN");
			    
			InputStream respuesta = multipart.connect();
			//System.out.println("RESPUESTA AVALIBLE DIRECTA: " + respuesta.available());
			byte [] byteArray = IOUtils.toByteArray(respuesta);
			//System.out.println("RESPUESTA AVALIBLE DESPUES: " + respuesta.available());
			//System.out.println("byteArray desde IOUtils.toByteArray: " + byteArray.length);
			
			if(byteArray.length <= 1024) //Si es menor que 1MB, realiza verificación de error. Esto es porque si existen errores, estos se mandan en el archivo xml de respuesta que siempre es menor a 1MB.
			{
				//Primero hace una copia del InputStream
				//que recibe en la respuesta del servidor, esta debe estar en formato xml
				InputStream is = new ByteArrayInputStream(byteArray);
				try
				{
					SAXBuilder builder = new SAXBuilder();
					Document document = (Document)builder.build(is);
					Element S3 = document.getRootElement();
								
					if(S3.getName().equals("SIGN_ERROR"))// Significa una respuesta de error
					{
						m_StatusS3 = ERROR;
						m_Error += "Codigo de Error JAwsS3Conn: " + S3.getAttribute("CodError") + "<br>" + S3.getAttribute("MsjError");
						return;
					}
					//Si no es respuesta de error, significa un archivo XML cualquiera
				}
				catch (JDOMException e1) 
				{
					//No es un archivo XML
					System.out.println("No es archivo XML de respuesta de error");
					//Intentará guardar el InputStream 
				} 
				
			}
			
			ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
			JBajarArchivo fd = new JBajarArchivo();
  		  	fd.doDownload(response, context, bais, content_type, byteArray.length, m_Nombre);
  		  	//System.out.println("Content-Length: "  + byteArray.length);
				        
			multipart.disconnect();
				
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
			m_Error = "Error de archivos S3: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			m_StatusS3 = ERROR;
			m_Error = "Error de Entrada/Salida S3: " + e1.getMessage();
		} 
		
	}	
	
}
