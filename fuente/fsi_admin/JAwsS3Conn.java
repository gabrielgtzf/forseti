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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.commons.lang.mutable.MutableDouble;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import forseti.JAccesoBD;
import forseti.JBajarArchivo;
import forseti.JUtil;
import forseti.sets.JPACServidoresSet;
import forseti.sets.JSrvServiciosBDSet;

@SuppressWarnings("serial")
public class JAwsS3Conn extends HttpServlet /*implements Filter*/
{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException
	{
		String ERROR = null, codErr = null;
		
		try 
		{
			Properties parametros = new Properties();
			Vector archivos = new Vector();
 			DiskFileUpload fu = new DiskFileUpload();
	  		List items = fu.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) 
			{
				FileItem item = (FileItem)iter.next();
				if (item.isFormField()) 
					parametros.put(item.getFieldName(), item.getString());
				else
					archivos.addElement(item);
			}
			  
			if(parametros.getProperty("SERVER") == null || parametros.getProperty("DATABASE") == null || parametros.getProperty("USER") == null || 
					parametros.getProperty("PASSWORD") == null || parametros.getProperty("ACTION") == null)
			{
				System.out.println("No recibi parametros de conexión antes del archivo");
				ERROR = "ERROR: El servidor no recibió todos los parametros de conexion (SERVER,DATABASE,USER,PASSWORD,ACTION) antes del archivo";
				codErr = "3";
				ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
						parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
			}
	  		
	  		//Hasta aqui se han enviado todos los parametros ninguno nulo
			if(ERROR == null)
			{
				StringBuffer msj = new StringBuffer(), S3BUKT = new StringBuffer(), S3USR = new StringBuffer(), S3PASS = new StringBuffer();
				MutableBoolean COBRAR = new MutableBoolean(false);
				MutableDouble COSTO = new MutableDouble(0.0), SALDO = new MutableDouble(0.0);
				// Primero obtiene info del S3
				if(!obtenInfoAWSS3(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"), parametros.getProperty("DATABASE"),
						parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), S3BUKT, S3USR, S3PASS, msj, COSTO, SALDO, COBRAR))
				{
					System.out.println("El usuario y contraseña de servicio estan mal");
					ERROR = msj.toString();
					codErr = "2";
					ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
							parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 2);

				}
				else
				{
					AWSCredentials credentials = new BasicAWSCredentials(S3USR.toString(), S3PASS.toString());
					AmazonS3 s3 = new AmazonS3Client(credentials);
					Region usWest2 = Region.getRegion(Regions.US_WEST_2);
					s3.setRegion(usWest2);
					//System.out.println("AwsConn:" + parametros.getProperty("NOMBRE") + ":parametros.getProperty(NOMBRE)");
					String nombre = parametros.getProperty("SERVER") + parametros.getProperty("DATABASE") + parametros.getProperty("ID_MODULO") + parametros.getProperty("OBJIDS") + parametros.getProperty("IDSEP") + parametros.getProperty("NOMBRE");  
					//System.out.println("AwsConn_Nombre:" + nombre + ":nombre");
		   				
					if(parametros.getProperty("ACTION").equals("SUBIR"))
					{
						Double TOTBITES = new Double(Double.parseDouble(parametros.getProperty("TOTBITES")));
						Double TAMBITES = new Double(Double.parseDouble(parametros.getProperty("TAMBITES")));
						
						if(COBRAR.booleanValue() && SALDO.doubleValue() < (COSTO.doubleValue() * (((TOTBITES + TAMBITES) / 1024) / 1024)) )
						{
							System.out.println("El servicio S3 de subida tiene un costo que no alcanza en el saldo");
							ERROR = "El servicio S3 de subida tiene un costo que no alcanza en el saldo";
							codErr = "2";
							ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
									parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 2);
						}
						else
						{
							if(!subirArchivo(msj, s3, S3BUKT.toString(), nombre, archivos))
							{
								System.out.println("No se permitió subir el archivo al s3");
								ERROR = msj.toString();
								codErr = "3";
								ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
																parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
							}
							else
							{
								ingresarRegistroExitoso(parametros.getProperty("SERVER"), parametros.getProperty("DATABASE"), parametros.getProperty("ID_MODULO"), 
											parametros.getProperty("OBJIDS"), parametros.getProperty("IDSEP"), parametros.getProperty("NOMBRE"), parametros.getProperty("TAMBITES"));
							}
						}
							
					}
					else if(parametros.getProperty("ACTION").equals("ELIMINAR"))
					{
						Double TOTBITES = new Double(Double.parseDouble(parametros.getProperty("TOTBITES")));
						
						if(COBRAR.booleanValue() && SALDO.doubleValue() < (COSTO.doubleValue() * ((TOTBITES / 1024) / 1024)) )
						{
							System.out.println("El servicio S3 de borrado tiene un costo que no alcanza en el saldo");
							ERROR = "El servicio S3 de borrado tiene un costo que no alcanza en el saldo";
							codErr = "2";
							ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
									parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 2);
						}
						else
						{
							if(!eliminarArchivo(msj, s3, S3BUKT.toString(), nombre))
							{
								System.out.println("No se permitió eliminar el archivo del s3");
								ERROR = msj.toString();
								codErr = "3";
								ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
										parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
							}
							else
							{
								eliminarRegistroExitoso(parametros.getProperty("SERVER"), parametros.getProperty("DATABASE"), parametros.getProperty("ID_MODULO"), 
										parametros.getProperty("OBJIDS"), parametros.getProperty("IDSEP"), parametros.getProperty("NOMBRE"));
							}
						}
					}
					else if(parametros.getProperty("ACTION").equals("DESCARGAR"))
					{
						Double TOTBITES = new Double(Double.parseDouble(parametros.getProperty("TOTBITES")));
						//System.out.println("COBRAR: " + COBRAR.booleanValue() + " SALDO: " + SALDO.doubleValue() + " COSTO: " + COSTO.doubleValue() + " TOTBITES: " + TOTBITES + " TOTMB: " + ((TOTBITES / 1024) / 1024) + " RES: " + (COSTO.doubleValue() * ((TOTBITES / 1024) / 1024)));
						if(COBRAR.booleanValue() && SALDO.doubleValue() < (COSTO.doubleValue() * ((TOTBITES / 1024) / 1024)) )
						{
							System.out.println("El servicio S3 de descarga tiene un costo que no alcanza en el saldo");
							ERROR = "El servicio S3 de descarga tiene un costo que no alcanza en el saldo";
							codErr = "2";
							ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
									parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 2);
						}
						else
						{
							if(!descargarArchivo(response, msj, s3, S3BUKT.toString(), nombre, parametros.getProperty("NOMBRE")))
							{
								System.out.println("No se permitió descargar el archivo del s3");
								ERROR = msj.toString();
								codErr = "3";
								ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
										parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
							}
							else
								return;
						}
					}
						
					if(ERROR == null)
					{
						//Devuelve la respuesta al cliente
						Element S3 = new Element("S3");
						S3.setAttribute("Archivo",nombre);
						S3.setAttribute("MsjError","");
						Document Reporte = new Document(S3);
										
						Format format = Format.getPrettyFormat();
						format.setEncoding("utf-8");
						format.setTextMode(TextMode.NORMALIZE);
						XMLOutputter xmlOutputter = new XMLOutputter(format);
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						xmlOutputter.output(Reporte, out);
								
						byte[] data = out.toByteArray();
						ByteArrayInputStream istream = new ByteArrayInputStream(data);
								
						String destino = "Archivo.xml";
						JBajarArchivo fd = new JBajarArchivo();
						fd.doDownload(response, getServletConfig().getServletContext(), istream, "text/xml", data.length, destino);
						
						
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR = "ERROR DE EXCEPCION EN SERVIDOR AWS S3: " + e.getMessage();
		}
		
		//Genera el archivo XML de error para ser devuelto al Servidor
		if(ERROR != null)
		{
			Element SIGN_ERROR = new Element("SIGN_ERROR");
			SIGN_ERROR.setAttribute("CodError",codErr);
			SIGN_ERROR.setAttribute("MsjError",ERROR);
			Document Reporte = new Document(SIGN_ERROR);
			
			Format format = Format.getPrettyFormat();
			format.setEncoding("utf-8");
			format.setTextMode(TextMode.NORMALIZE);
			XMLOutputter xmlOutputter = new XMLOutputter(format);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlOutputter.output(Reporte, out);
			
			byte[] data = out.toByteArray();
			ByteArrayInputStream istream = new ByteArrayInputStream(data);
						
			String destino = "SIGN_ERROR.xml";
			JBajarArchivo fd = new JBajarArchivo();
			fd.doDownload(response, getServletConfig().getServletContext(), istream, "text/xml", data.length, destino);
			
		}
		
	}
	
	private void ingresarRegistroExitoso(String servidor, String basedatos, String id_modulo, String objids, String idsep, String nombre, String tambites)
	{
		String SQL = "SELECT * FROM sp_s3_registros_exitosos_conn('" + JUtil.q(servidor) + "','" + 
	    			  JUtil.q(basedatos) + "','" + JUtil.q(id_modulo) + "','" + JUtil.q(objids) + "','" + JUtil.q(idsep) + "','" + JUtil.q(nombre) + "'," + JUtil.q(tambites) + ") as (RES integer);";
		try
	    {
	    	Connection con = JAccesoBD.getConexion();
	    	Statement s    = con.createStatement();
	    	ResultSet rs   = s.executeQuery(SQL);
  			if(rs.next())
  			{
  				int res = rs.getInt("RES");
  				System.out.println("Resultado del archivo S3 en Conn: " + res);
  			}
  			
	    	s.close();
	    	JAccesoBD.liberarConexion(con);
	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	    
	}
	
	private void eliminarRegistroExitoso(String servidor, String basedatos, String id_modulo, String objids, String idsep, String nombre)
	{
		String SQL = "SELECT * FROM sp_s3_registros_exitosos_eliminar_conn('" + JUtil.q(servidor) + "','" + 
	    			  JUtil.q(basedatos) + "','" + JUtil.q(id_modulo) + "','" + JUtil.q(objids) + "','" + JUtil.q(idsep) + "','" + JUtil.q(nombre) + "') as (RES integer);";
		try
	    {
	    	Connection con = JAccesoBD.getConexion();
	    	Statement s    = con.createStatement();
	    	ResultSet rs   = s.executeQuery(SQL);
  			if(rs.next())
  			{
  				int res = rs.getInt("RES");
  				System.out.println("Resultado de la eliminación del archivo S3 en Conn: " + res);
  			}
	    	s.close();
	    	JAccesoBD.liberarConexion(con);
	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	    
	}
	
	private void ingresarRegistroFallido(String IP, String host, String servidor, String usuario, String password, HttpServletRequest request, String error, int nivelError)
	{
		//System.out.println(sb.toString());
	    Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_PAC_REGISTROS_FALLIDOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.q(IP) + "','" + JUtil.q(host) + "','" + JUtil.obtFechaHoraSQL(cal) + "','" + (servidor != null ? JUtil.q(servidor) : "nulo") + "','" + 
	    			  (usuario != null ? JUtil.q(usuario) : "nulo") + "','" + (password != null ? JUtil.q(password) : "nulo") + "','','" + JUtil.q(error) + "','" + nivelError + "','S3')";
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
	    }
	    
	}
	
	private boolean obtenInfoAWSS3(String IP, String host, String servidor, String basedatos, String usuario, String password, 
			StringBuffer S3BUKT, StringBuffer S3USR, StringBuffer S3PASS, StringBuffer msj, MutableDouble COSTO, MutableDouble SALDO, MutableBoolean COBRAR)
	{
	      JPACServidoresSet bd = new JPACServidoresSet(null);
	      bd.ConCat(true);
	      bd.m_Where = "ID_Servidor = '" + JUtil.p(servidor) + "' and Usuario = '" + JUtil.p(usuario) + "' and Pass = '" + JUtil.q(password) + "' and Status = 'A'";
	      bd.Open();
	      
	      if(bd.getNumRows() == 0) // No existe registro de este servidor
	      {
	    	  msj.append("ERROR: La autenticación a este servidor ha fallado, verifica que tu clave de servidor, usuario y contraseña sean las correctas y vuelve a intentarlo");
	    	  return false;
	      }
	      else //Si existe el servidor, entonces da los datos para el SMTP
	      {
	    	  try
	          {
	              FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_awss3");
	              BufferedReader buff     = new BufferedReader(file);
	              boolean eof             = false;
	              //masProp = new Properties();

	              while(!eof)
	              {
	                  String line = buff.readLine();
	                  System.out.println(line);
	                  if(line == null)
	                  {
	                      eof = true;
	                  }
	                  else
	                  {
	                	  try
	                	  {
	                		  StringTokenizer st = new StringTokenizer(line,"=");
	    					  String key         = st.nextToken();
	    					  String value       = st.nextToken();
	  						
	    					  if(key.equals("BUKT"))
	    						  S3BUKT.append(value);
	    					  else if(key.equals("PASS"))
	    						  S3PASS.append(value);
	    					  else if(key.equals("USER"))
	    						  S3USR.append(value);
	  												
	    				  }
	    				  catch(NoSuchElementException e)
	    				  {
	    					  msj.append("ERROR: La informacion del servicio Amazon S3 parece estar mal configurada");
	    					  return false;
	    				  }
	    			  }
	    			  
	    		  }
	              buff.close();
	    	  }
	    	  catch (FileNotFoundException e1)
	    	  {
	    		  e1.printStackTrace();
	    		  msj.append("Error de archivos AWS S3: " + e1.getMessage());
	    		  return false;
	    	  }
	    	  catch (IOException e1) 
	    	  {
	    		  e1.printStackTrace();
	    		  msj.append("Error de Entrada/Salida AWS S3: " + e1.getMessage());
	    		  return false;
	    	  }
	  		
	    	  JSrvServiciosBDSet srv = new JSrvServiciosBDSet(null);
		      srv.ConCat(true);
		      srv.m_Where = "ID_Servidor = '" + JUtil.p(servidor) + "' and Basedatos = '" + JUtil.p(basedatos) + "' and Status = 'A'";
		      srv.Open();
		      
		      if(srv.getNumRows() == 0) // No existe registro de este servidor
		      {
		    	  msj.append("ERROR: La autenticación a este servidor es correcta, sin embargo, la base de datos a la que se intenta dar servicio está bloqueada o no se ha dado de alta aún en este servidor");
		    	  return false;
		      }
		      else
		      {
		    	  //System.out.println("CostoS3MB: " + srv.getAbsRow(0).getCostoS3MB() + " Saldo: " + srv.getAbsRow(0).getSaldo() + " CobrarS3MB: " + srv.getAbsRow(0).getCobrarS3MB());
		    	  COSTO.setValue(srv.getAbsRow(0).getCostoS3MB());
		    	  SALDO.setValue(srv.getAbsRow(0).getSaldo());
		    	  COBRAR.setValue(srv.getAbsRow(0).getCobrarS3MB());
		    	  //System.out.println("Costo: " + COSTO + " Saldo: " + SALDO + " Cobrar: " + COBRAR);
		    	  return true;
		      }
		      
	      }
	    
	}

	@SuppressWarnings("rawtypes")
	private boolean subirArchivo(StringBuffer msj, AmazonS3 s3, String S3BUKT, String nombre, Vector archivos)
	{
		//System.out.println("AwsConn SubirArchivo:" + nombre + ":nombre");
		
		if(!archivos.isEmpty())
		{
			FileItem actual = null;
			
			try 
			{
				for(int i = 0; i < archivos.size(); i++) 
				{
					InputStream inputStream = null;
					try
					{
						actual = (FileItem)archivos.elementAt(i);
						/////////////////////////////////////////////////////////
						//Obtain the Content length of the Input stream for S3 header
					    InputStream is = actual.getInputStream();
					    byte[] contentBytes = IOUtils.toByteArray(is);
					    
					    Long contentLength = Long.valueOf(contentBytes.length);
	
					    ObjectMetadata metadata = new ObjectMetadata();
					    metadata.setContentLength(contentLength);
	
					    //Reobtain the tmp uploaded file as input stream
					    inputStream = actual.getInputStream();
	
					    //Put the object in S3
					    //System.out.println("BUCKET: " + S3BUKT + " OBJETO: " + nombre.replace('_', '-'));
					    //System.out.println("BUCKET: " + S3BUKT + " OBJETO: " + nombre.replace('_', '-'));
						s3.putObject(new PutObjectRequest(S3BUKT, nombre, inputStream, metadata));
					}
				    finally
					{
						if(inputStream != null)
							try {
								inputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
				    ////////////////////////////////////////////////////////////
	            }
				return true;
			}
			catch (AmazonServiceException ase) 
	        {
				ase.printStackTrace();
				msj.append("Error de AmazonServiceException al subir archivo a S3.<br>");
				msj.append("Mensaje: " + ase.getMessage() + "<br>");
				msj.append("Código de Estatus HTTP: " + ase.getStatusCode() + "<br>");
				msj.append("Código de Error AWS:   " + ase.getErrorCode() + "<br>");
				msj.append("Tipo de Error:       " + ase.getErrorType() + "<br>");
				msj.append("Request ID:       " + ase.getRequestId());
				return false;
	        } 
	        catch (AmazonClientException ace) 
	        {
	        	ace.printStackTrace();
				msj.append("Error de AmazonClientException al subir archivo a S3.<br>");
				msj.append("Mensaje: " + ace.getMessage());
				return false;
	        }
			catch (IOException e) 
			{
				e.printStackTrace();
				msj.append("Error de Entrada/Salida al subir archivo a S3: " + e.getMessage());
				return false;
			}
			
		}
		else
		{
			msj.append("Error al subir archivo a la nube: No se envió ningun archivo");
			return false;
		}
	}
	
	private boolean eliminarArchivo(StringBuffer msj, AmazonS3 s3, String S3BUKT, String nombre)
	{
		//System.out.println("AwsConn EliminarArchivo:" + nombre + ":nombre");
			
		try
		{
			//System.out.println("BUCKET: " + S3BUKT + " OBJETO: " + nombre.replace('_', '-'));
			//System.out.println("BUCKET: " + S3BUKT + " OBJETO: " + nombre.replace('_', '-'));
			s3.deleteObject(S3BUKT, nombre);
			return true;
		}
		catch (AmazonServiceException ase) 
		{
			ase.printStackTrace();
			msj.append("Error de AmazonServiceException al eliminar archivo de S3.<br>");
			msj.append("Mensaje: " + ase.getMessage() + "<br>");
			msj.append("Código de Estatus HTTP: " + ase.getStatusCode() + "<br>");
			msj.append("Código de Error AWS:   " + ase.getErrorCode() + "<br>");
			msj.append("Tipo de Error:       " + ase.getErrorType() + "<br>");
			msj.append("Request ID:       " + ase.getRequestId());
			return false;
		} 
		catch (AmazonClientException ace) 
		{
			ace.printStackTrace();
			msj.append("Error de AmazonClientException al eliminar archivo de S3.<br>");
			msj.append("Mensaje: " + ace.getMessage());
			return false;
		}
				
	}
	
	private boolean descargarArchivo(HttpServletResponse response, StringBuffer msj, AmazonS3 s3, String S3BUKT, String nombre, String destino)
	{
		//System.out.println("AwsConn DescargarArchivo:" + nombre + ":nombre");
			
		try
		{
			System.out.println("DESCARGA BUCKET: " + S3BUKT + " OBJETO: " + nombre);
			S3Object object = s3.getObject(new GetObjectRequest(S3BUKT, nombre));
			//out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
	        //System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
			byte [] byteArray = IOUtils.toByteArray(object.getObjectContent());
			ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
			JBajarArchivo fd = new JBajarArchivo();
  		  	fd.doDownload(response, getServletConfig().getServletContext(), bais, object.getObjectMetadata().getContentType(), byteArray.length, destino);
  		  	System.out.println("Content-Length: "  + object.getObjectMetadata().getContentLength() + " BA: " + byteArray.length);
			return true;
		}
		catch (AmazonServiceException ase) 
		{
			ase.printStackTrace();
			msj.append("Error de AmazonServiceException al descargar archivo de S3.<br>");
			msj.append("Mensaje: " + ase.getMessage() + "<br>");
			msj.append("Código de Estatus HTTP: " + ase.getStatusCode() + "<br>");
			msj.append("Código de Error AWS:   " + ase.getErrorCode() + "<br>");
			msj.append("Tipo de Error:       " + ase.getErrorType() + "<br>");
			msj.append("Request ID:       " + ase.getRequestId());
			return false;
		} 
		catch (AmazonClientException ace) 
		{
			ace.printStackTrace();
			msj.append("Error de AmazonClientException al descargar archivo de S3.<br>");
			msj.append("Mensaje: " + ace.getMessage());
			return false;
		} 
		catch (IOException ace)
		{
			ace.printStackTrace();
			msj.append("Error de IOException al descargar archivo de S3.<br>");
			msj.append("Mensaje: " + ace.getMessage());
			return false;
		}
				
	}
}
