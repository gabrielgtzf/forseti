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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.commons.lang.mutable.MutableDouble;
import org.bouncycastle.util.encoders.Base64;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

import com.edicom.ediwinws.cfdi.client.CfdiClient;
import com.edicom.ediwinws.cfdi.client.CfdiException;
import com.edicom.ediwinws.service.cfdi.CancelaResponse;

import forseti.JAccesoBD;
import forseti.JBajarArchivo;
import forseti.JFsiScript;
import forseti.JUtil;
import forseti.JZipUnZipUtil;
import forseti.sets.JPACServidoresSet;
import forseti.sets.JSrvServiciosBDSet;

@SuppressWarnings("serial")
public class JPacConn extends HttpServlet /*implements Filter*/
{
	/*
	public void init(FilterConfig arg0) 
			throws ServletException 
	{
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException 
	{
		String IP_RANGO = "127.0";
		String ip = request.getRemoteAddr();

	    HttpServletResponse httpResp = null;

	    if (response instanceof HttpServletResponse)
	    	httpResp = (HttpServletResponse) response;

	    StringTokenizer toke = new StringTokenizer(ip, ".");
	    int dots = 0;
	    String byte1 = "";
	    String byte2 = "";
	    String client = "";

	    while (toke.hasMoreTokens()) 
	    {
	    	++dots;
	    	//if we've reached the second dot, break and check out the indx
		    // value
		    if (dots == 1) 
		       	byte1 = toke.nextToken();
		    else
		    {
		       	byte2 = toke.nextToken();
		       	break;
		    }
	    }//while

	    //Piece together half of the client IP address so it can be compared with
	    //the forbidden range represented by IPFilter.IP_RANGE
	    client = byte1 + "." + byte2;

	    if (IP_RANGO.equals(client)) 
	       	httpResp.sendError(HttpServletResponse.SC_FORBIDDEN,"Esto significa ¡Adios para siempre!");
	    else
	    	chain.doFilter(request, response);
	    

	}*/
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException
	{
		String ERROR = null, codErr = null;
		
		try 
		{
			boolean CANCEL = false, TEST = true; //Protege el timbrado a pruebas
			if(request.getParameter("TEST") != null)
	  			TEST = (request.getParameter("TEST").equals("false") ? false : true);
	  		if(request.getParameter("CANCEL") != null)
	  			CANCEL = (request.getParameter("CANCEL").equals("false") ? false : true);
	  		
	  		if(request.getParameter("SERVER") == null || request.getParameter("DATABASE") == null || request.getParameter("USER") == null || 
	  				request.getParameter("PASSWORD") == null || request.getParameter("NAME") == null)
			{
				System.out.println("No recibi parametros de conexion antes del archivo a recibir");
				ERROR = "ERROR: El servidor no recibió todos los parametros de conexion (SERVER,DATABASE,USER,PASSWORD) antes del archivo XML a recibir";
				codErr = "3";
				ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
						request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 3);
			}
	  		
	  		//Hasta aqui se han enviado todos los parametros ninguno nulo
			if(ERROR == null)
			{
				StringBuffer msj = new StringBuffer(), PACURL = new StringBuffer(), PACUSR = new StringBuffer(), PACPASS = new StringBuffer();
				MutableBoolean COBRAR = new MutableBoolean(false);
				MutableDouble COSTO = new MutableDouble(0.0), SALDO = new MutableDouble(0.0);
				// Primero obtiene info del PAC
				if(!obtenInfoPAC(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),  request.getParameter("DATABASE"),
						request.getParameter("USER"), request.getParameter("PASSWORD"), PACURL, PACUSR, PACPASS, msj, COSTO, SALDO, COBRAR))
				{
					System.out.println("El usuario y contraseña de servicio estan mal");
					ERROR = msj.toString();
					codErr = "2";
					ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
							request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 2);

				}
				else
				{
					if(CANCEL == false) //Intenta sellar documento mandado
					{
						if(COBRAR.booleanValue() && SALDO.doubleValue() < COSTO.doubleValue())
						{
							System.out.println("El servicio tiene un costo que no alcanza en el saldo");
							ERROR = "El servicio de timbrado tiene un costo que no alcanza en el saldo";
							codErr = "2";
							ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
									request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 2);

						}
						else
						{
							if(!salvarArchivoSubido(request.getParameter("SERVER"), request.getParameter("DATABASE"), 
									request.getParameter("NAME"), request.getParameter("CFDXML"), msj))
							{
								System.out.println("No se permitió subir el archivo");
								ERROR = msj.toString();
								codErr = "3";
								ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
										request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 3);
							}
							else
							{
								StringBuffer registrar = new StringBuffer("");
								
								if(!generarTFD(request.getParameter("SERVER"), request.getParameter("DATABASE"), request.getParameter("NAME"), 
										PACURL.toString(), PACUSR.toString(), PACPASS.toString(), TEST, registrar, msj))
								{
									System.out.println("Errores al sellar el documento");
									ERROR = msj.toString();
									codErr = "1";
									ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
											request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 1);
			
								}
								else // de lo contrario, regresa el archivo del sello
								{
									if(!registrar.toString().equals("false"))
										ingresarRegistroExitoso(request.getParameter("SERVER"), request.getParameter("DATABASE"), request.getParameter("NAME"), TEST, COSTO, SALDO, COBRAR);
									else
										System.out.println("No se registro en exitosos porque ya existia registro de este timbre");
									
									String nombre = "/usr/local/forseti/pac/TFDs/" + request.getParameter("SERVER") + "-" + request.getParameter("DATABASE") + "-SIGN_" + request.getParameter("NAME") + ".xml";
									String destino = "SIGN_" + request.getParameter("NAME") + ".xml";
									JBajarArchivo fd = new JBajarArchivo();
							  		fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
							  		
							  		//Finalmente, borra el archivo xml del cfd y los zip
							  		String strFilePathZip = "/usr/local/forseti/pac/CFDs/" + request.getParameter("SERVER") + "-" + request.getParameter("DATABASE") + "-" + request.getParameter("NAME") + ".zip";
									String strFilePathXML = "/usr/local/forseti/pac/CFDs/" + request.getParameter("SERVER") + "-" + request.getParameter("DATABASE") + "-" + request.getParameter("NAME") + ".xml";
									String strFilePathZip2 = "/usr/local/forseti/pac/TFDs/" + request.getParameter("SERVER") + "-" + request.getParameter("DATABASE") + "-" + request.getParameter("NAME") + ".zip";
									File fileZip = new File(strFilePathZip);
									File fileXML = new File(strFilePathXML);
									File fileZip2 = new File(strFilePathZip2);
									if(fileZip.exists())
										fileZip.delete();
									if(fileXML.exists())
										fileXML.delete();
									if(fileZip2.exists())
										fileZip2.delete();
															
								}
							}
						}
					}
					else // Es una cancelacion
					{
						if(!salvarArchivoSubidoCER_KEY(request.getParameter("SERVER"), request.getParameter("DATABASE"), 
								request.getParameter("NAME"), request.getParameter("CERPEM"), request.getParameter("KEYPEM"), msj))
						{
							System.out.println("No se permitió subir el archivo pfx");
							ERROR = msj.toString();
							codErr = "3";
							ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
									request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 3);
						}
						else
						{
							if(!generarCancelacion(request.getParameter("SERVER"), request.getParameter("DATABASE"), request.getParameter("NAME"), 
									PACURL.toString(), PACUSR.toString(), PACPASS.toString(), msj))
							{
								System.out.println("Errores al cancelar el documento");
								ERROR = msj.toString();
								codErr = "1";
								ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
										request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 1);
		
							}
							else // de lo contrario, regresa el archivo de la cancelacion
							{
								ingresarCancelacionExitosa(request.getParameter("SERVER"), request.getParameter("DATABASE"), request.getParameter("NAME"));
								
								String nombre = "/usr/local/forseti/pac/TFDs/" + request.getParameter("SERVER") + "-" + request.getParameter("DATABASE") + "-CANCEL_" + request.getParameter("NAME") + ".xml";
								String destino = "CANCEL_" + request.getParameter("NAME") + ".xml";
								JBajarArchivo fd = new JBajarArchivo();
						  		fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
						  		
						  		//Finalmente, borra los archivos pem
						  		String strFilePathCer = "/usr/local/forseti/pac/TFDs/" + request.getParameter("SERVER") + "-" + request.getParameter("DATABASE") + "-" + request.getParameter("NAME") + ".cer.pem";
						  		String strFilePathKey = "/usr/local/forseti/pac/TFDs/" + request.getParameter("SERVER") + "-" + request.getParameter("DATABASE") + "-" + request.getParameter("NAME") + ".key.pem";
								File fileCer = new File(strFilePathCer);
								File fileKey = new File(strFilePathKey);
								if(fileCer.exists())
									fileCer.delete();
								if(fileKey.exists())
									fileKey.delete();
							}
						}
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR = "ERROR DE EXCEPCION EN SERVIDOR PAC: " + e.getMessage();
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
	
	private boolean salvarArchivoSubidoCER_KEY(String servidor, String basedatos, String nombrearchivo, String CERPEM, String KEYPEM, StringBuffer msj)
	{
		boolean res = false;
		
		String pathcer = "/usr/local/forseti/pac/TFDs/" + servidor + "-" + basedatos + "-" + nombrearchivo + ".cer.pem";
		String pathkey = "/usr/local/forseti/pac/TFDs/" + servidor + "-" + basedatos + "-" + nombrearchivo + ".key.pem";
		
		try 
        {
			File filecer = new File(pathcer);
        	FileWriter fcer = new FileWriter(filecer);
        	fcer.write(CERPEM);
        	fcer.close();
        	
        	File filekey = new File(pathkey);
        	FileWriter fkey = new FileWriter(filekey);
        	fkey.write(KEYPEM);
        	fkey.close();
        	
        	res = true;
        } 
        catch (IOException e) 
		{
			e.printStackTrace();
			msj.append("ERROR IO AL GRABAR CER o KEY: " + e.getMessage());
		}
		
		System.out.println("CER KEY RES: " + res);
				
		return res;
	}
	
	private boolean salvarArchivoSubido(String servidor, String basedatos, String nombrearchivo, String CFDXML, StringBuffer msj)
	{
		boolean res = false;
		try 
		{
			System.out.println(CFDXML);
			
			SAXBuilder builder = new SAXBuilder();
			Document document;
			document = (Document)builder.build(new StringReader(CFDXML));
			Element Comprobante = document.getRootElement();
		
			if(!Comprobante.getName().equals("Comprobante"))
				msj.append("ERROR GRAVE EN SERVIDOR PAC: El archivo recibido XML no parece ser un comprobante CFDI valido");
			else // Guarda la entrada en disco
			{
				Format format = Format.getPrettyFormat();
    			format.setEncoding("utf-8");
    			format.setTextMode(TextMode.NORMALIZE);
    			XMLOutputter xmlOutputter = new XMLOutputter(format);
    			String path = "/usr/local/forseti/pac/CFDs/" + servidor + "-" + basedatos + "-" + nombrearchivo + ".xml";
    			//System.out.println(path);
    			FileWriter writer = new FileWriter(path);
    			xmlOutputter.output(document, writer);
    			writer.close();
    			
    			res = true;
			}
		} 
		catch (JDOMException e) 
		{
			e.printStackTrace();
			msj.append("ERROR JDOM GRAVE EN SERVIDOR PAC: " + e.getMessage());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			msj.append("ERROR IO GRAVE EN SERVIDOR PAC: " + e.getMessage());
		}
				
		return res;
	}
	
	private void ingresarCancelacionExitosa(String servidor, String basedatos, String nombrearchivo)
	{
		Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_PAC_REGISTROS_CANCELADOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.obtFechaHoraSQL(cal) + "','" + JUtil.q(servidor) + "','" + 
	    			  JUtil.q(basedatos) + "','" + JUtil.q(nombrearchivo) + "')";
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
	
	private void ingresarRegistroExitoso(String servidor, String basedatos, String nombrearchivo, boolean test, MutableDouble COSTO, MutableDouble SALDO, MutableBoolean COBRAR)
	{
		Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_PAC_REGISTROS_EXITOSOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.obtFechaHoraSQL(cal) + "','" + JUtil.q(servidor) + "','" + 
	    			  JUtil.q(basedatos) + "','" + JUtil.q(nombrearchivo) + "','" + (test ? "1" : "0") + "','PAC'," + COSTO.doubleValue() + "," + (COBRAR.booleanValue() ? JUtil.redondear((SALDO.doubleValue() - COSTO.doubleValue()),4) : JUtil.redondear(SALDO.doubleValue(),4)) + ");\n";
	    if(COBRAR.booleanValue())
	    {
	    	  SQL += "UPDATE TBL_SRV_SERVICIOS_BD\n";
	    	  SQL += "SET Saldo = Saldo - " + JUtil.redondear(COSTO.doubleValue(),4) + "\n";
	    	  SQL += "WHERE ID_Servidor = '" + JUtil.q(servidor) + "' and Basedatos = '" + JUtil.q(basedatos) + "';";
	    }
	    
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
	
	private void ingresarRegistroFallido(String IP, String host, String servidor, String usuario, String password, HttpServletRequest request, String error, int nivelError)
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		try 
	    {
	        bufferedReader =  request.getReader();
	        char[] charBuffer = new char[128];
	        int bytesRead;
	        while ( (bytesRead = bufferedReader.read(charBuffer)) != -1 ) 
	        {
	            sb.append(charBuffer, 0, bytesRead);
	        }
	        
	    } 
	    catch (IOException ex) 
	    {
	        ex.printStackTrace();
	    } 
	    finally 
	    {
	    	if (bufferedReader != null) 
	    	{
	    		try 
	    		{
	                bufferedReader.close();
	            } 
	    		catch (IOException ex) 
	    		{
	                ex.printStackTrace();
	            }
	        }
	    }
		
	    //System.out.println(sb.toString());
	    Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_PAC_REGISTROS_FALLIDOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.q(IP) + "','" + JUtil.q(host) + "','" + JUtil.obtFechaHoraSQL(cal) + "','" + (servidor != null ? JUtil.q(servidor) : "nulo") + "','" + 
	    			  (usuario != null ? JUtil.q(usuario) : "nulo") + "','" + (password != null ? JUtil.q(password) : "nulo") + "','" + JUtil.q(sb.toString()) + "','" + JUtil.q(error) + "','" + nivelError + "','PAC')";
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
	
	private boolean obtenInfoPAC(String IP, String host, String servidor, String basedatos, String usuario, String password, 
			StringBuffer PACURL, StringBuffer PACUSR, StringBuffer PACPASS, StringBuffer msj, MutableDouble COSTO, MutableDouble SALDO, MutableBoolean COBRAR)
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
	      else //Si existe el servidor, entonces da los datos para edicom
	      {
	    	  try
	          {
	              FileReader file         = new FileReader("/usr/local/forseti/pac/.forseti_pac");
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
	  						
	    					  if(key.equals("PURL"))
	    						  PACURL.append(value);
	    					  else if(key.equals("PASS"))
	    						  PACPASS.append(value);
	    					  else if(key.equals("USER"))
	    						  PACUSR.append(value);
	  												
	    				  }
	    				  catch(NoSuchElementException e)
	    				  {
	    					  msj.append("ERROR: La informacion del proveedor de PAC parece estar mal configurada");
	    					  return false;
	    				  }
	    			  }
	    			  
	    		  }
	              buff.close();
	    	  }
	    	  catch (FileNotFoundException e1)
	    	  {
	    		  e1.printStackTrace();
	    		  msj.append("Error de archivos CFDI: " + e1.getMessage());
	    		  return false;
	    	  }
	    	  catch (IOException e1) 
	    	  {
	    		  e1.printStackTrace();
	    		  msj.append("Error de Entrada/Salida CFDI: " + e1.getMessage());
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
		    	  COSTO.setValue(srv.getAbsRow(0).getCostoSello());
		    	  SALDO.setValue(srv.getAbsRow(0).getSaldo());
		    	  COBRAR.setValue(srv.getAbsRow(0).getCobrarSello());
		    	  return true;
		      }
	      }
	      
	    
	}
	
	private boolean generarTFD(String SERVER, String DATABASE, String NAME, String PACURL, String PACUSR, String PACPASS, boolean PACTEST, StringBuffer registrar, StringBuffer msj)
	{
		String ERROR = "";
		try 
		{
			String inputFile = "/usr/local/forseti/pac/CFDs/" + SERVER + "-" + DATABASE + "-" + NAME + ".xml";
			String outputFile = "/usr/local/forseti/pac/CFDs/" + SERVER + "-" + DATABASE + "-" + NAME + ".zip";
			//System.out.println(inputFile);
		    //System.out.println(outputFile);
		    
			JZipUnZipUtil zfapl = new JZipUnZipUtil();
			zfapl.zipFile(inputFile, outputFile, NAME);
		    // Ya lo dejo, ahora lo lee y lo deposita en un FileInputStream
		    File file = new File(outputFile);
		    FileInputStream finz = new FileInputStream(file);
		    byte fileContent[] = new byte[(int)file.length()];
		    finz.read(fileContent);
		    
		    // Ahora llama al cliente EDICOM para sellar, este debe regresar el CFDI sellado en zip
		    // Primero verifica que no este ya sellado (Revisando si existe el archivo xml)
		    // direccion del archivo XML
		    String destinationname = "/usr/local/forseti/pac/TFDs/" + SERVER + "-" + DATABASE + "-";
	        String xml_path = destinationname + "SIGN_" + NAME + ".xml";
			File verFile = new File(xml_path);
	    	if(!verFile.exists())
		    {
	    		CfdiClient client = new CfdiClient(PACURL);
	    		byte zipres [];
	    		if(PACTEST)
	    		{
	    			System.out.println("Timbrando en Test");
	    			zipres = client.getCfdiTest(PACUSR, PACPASS, fileContent);
	    		}
	    		else
	    		{
	    			System.out.println("Timbrando en Produccion");
	    			zipres = client.getCfdi(PACUSR, PACPASS, fileContent);
	    		}
		    
	    		//Ahora ya tenemos el CFDI timbrado como byte array, proseguimos a guardarlo
	    		String strFilePathZip = "/usr/local/forseti/pac/TFDs/" + SERVER + "-" + DATABASE + "-" + NAME + ".zip";
	    		FileOutputStream fos = new FileOutputStream(strFilePathZip);
	    		fos.write(zipres);
	    		fos.close(); 
	    	   
	    		//Ahora guardamos el CFDI timbrado como xml
	    		zfapl.unZipFile(strFilePathZip, destinationname);
	    		
		    }
	    	else
	    	{
	    		System.out.println("PRECAUCION: Ya existia archivo de timbre: " + xml_path);
	    		registrar.append("false");
	    	}
		}
		catch (CfdiException e) 
		{
			e.printStackTrace();
			ERROR = "--- ERROR EDICOM CFDI ---<br>Code: " + e.getCod() + "<br>Text: " + e.getText() + "<br>TextCode: "  + e.getTextCode() + "<br>MessageCode: " + CfdiException.getTextCode(e.getCod());
		}
		catch(NullPointerException e) 
		{
			e.printStackTrace();
			ERROR = "ELEMENTO NULO EN EL XML DEL TIMBRE. ESTE TIMBRE DEBE ESTAR MAL, POR LO TANTO EL CFDI NO ES VALIDO: " + e.getMessage() + "<br>";
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR = "ERROR DESCONOCIDO DE ARCHIVOS AL CARGAR TIMBRE: " + e.getMessage() + "<br>";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR = "ERROR EN TIMBRE: " + e.getMessage();
		}
    
		if(!ERROR.equals(""))
		{
			msj.append(ERROR);
			return false;
		}
		else
			return true;
	}

	private boolean generarCancelacion(String SERVER, String DATABASE, String NAME, String PACURL, String PACUSR, String PACPASS, StringBuffer msj)
	{
		String CONTENT;
		
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		
		String ERROR = "";
		try 
		{
			String inputFile = "/usr/local/forseti/pac/TFDs/" + SERVER + "-" + DATABASE + "-SIGN_" + NAME + ".xml";
			String pfxFile = "/usr/local/forseti/pac/TFDs/" + SERVER + "-" + DATABASE + "-" + NAME + ".pfx";
			String respFile = "/usr/local/forseti/pac/TFDs/" + SERVER + "-" + DATABASE + "-CANCEL_" + NAME + ".xml";
			String cerFile = "/usr/local/forseti/pac/TFDs/" + SERVER + "-" + DATABASE + "-" + NAME + ".cer.pem";
			String keyFile = "/usr/local/forseti/pac/TFDs/" + SERVER + "-" + DATABASE + "-" + NAME + ".key.pem";
			
			System.out.println(inputFile + "\n" + pfxFile + "\n" + respFile + "\n" + cerFile + "\n" + keyFile);
			
			//Obtiene el UUID del CFDI
            SAXBuilder builder = new SAXBuilder();
	    	Document XMLTFD = (Document)builder.build(inputFile);
	    	Element Comprobante = XMLTFD.getRootElement();
	    	Namespace ns = Comprobante.getNamespace();
	    	Element Emisor = Comprobante.getChild("Emisor",ns);
			String CFD_RFC = Emisor.getAttributeValue("rfc");
	    	Element Complemento = Comprobante.getChild("Complemento", ns);
	    	Namespace nstfd = Namespace.getNamespace("tfd","http://www.sat.gob.mx/TimbreFiscalDigital");
	    	Element tfd = Complemento.getChild("TimbreFiscalDigital", nstfd);
	    	
			String [] uuids = new String [1];
			uuids[0] = tfd.getAttributeValue("UUID");
			//System.out.println("UUIDS: " + uuids[0]);
			
			//Genera el PFX
			CONTENT = "sudo openssl pkcs12 -export -out " + pfxFile + " -inkey " + keyFile + " -in " + cerFile + " -passout pass:" + PACPASS;
			sc.setContent(CONTENT);
			sc.executeCommand();
			ERROR += sc.getError();
			//System.out.println("PFX GENERADO");
			
			//Ahora toma el Archivo Pfx en un array de bytes
			if(ERROR.equals(""))
			{
				File file = new File(pfxFile);
			    FileInputStream fis = new FileInputStream(file);
			    byte fileContent[] = new byte[(int)file.length()];
			    fis.read(fileContent);
			    
			    //Genera la peticion de cancelacion
			    CfdiClient client = new CfdiClient(PACURL);
			    CancelaResponse res = client.cancelCfdi(PACUSR, PACPASS, CFD_RFC, uuids, fileContent, PACPASS);
			    System.out.println("NUM UUIDS CANCELADOS: " + res.getUuids().length);
				//Obtiene la respuesta XML y la decodifica
			    String Ack = new String(Base64.decode(res.getAck()));
	            //Guarda el archivo de respuesta
	            File f = new File(respFile);
	            FileWriter fw = new FileWriter(f);
	            fw.write(Ack);
	            fw.close();
			}
            
		}
		catch (CfdiException e) 
		{
			e.printStackTrace();
			ERROR = "--- ERROR EDICOM CFDI ---<br>Code: " + e.getCod() + "<br>Text: " + e.getText() + "<br>TextCode: "  + e.getTextCode() + "<br>MessageCode: " + CfdiException.getTextCode(e.getCod());
		}
		catch(NullPointerException e) 
		{
			e.printStackTrace();
			ERROR = "ELEMENTO NULO EN EL XML DEL TIMBRE A CANCELAR. EL CFDI NO ES VALIDO: " + e.getMessage() + "<br>";
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR = "ERROR IO: " + e.getMessage() + "<br>";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR = "ERROR GENERAL: " + e.getMessage();
		}
    
		if(!ERROR.equals(""))
		{
			msj.append(ERROR);
			return false;
		}
		else
			return true;
	}


}
