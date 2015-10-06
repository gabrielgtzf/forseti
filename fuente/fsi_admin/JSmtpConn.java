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
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;

import java.sql.Connection;
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
import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axiom.attachments.ByteArrayDataSource;
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

import forseti.JAccesoBD;
import forseti.JBajarArchivo;
import forseti.JUtil;
import forseti.sets.JPACServidoresSet;
import forseti.sets.JSrvServiciosBDSet;

@SuppressWarnings("serial")
public class JSmtpConn extends HttpServlet /*implements Filter*/
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
					parametros.getProperty("PASSWORD") == null || parametros.getProperty("BODY") == null || parametros.getProperty("MIMETYPE") == null || 
							parametros.getProperty("SUBJECT") == null || parametros.getProperty("EMAIL") == null || parametros.getProperty("SOURCEMAIL") == null)
			{
				System.out.println("No recibi parametros de conexión antes del correo a enviar");
				ERROR = "ERROR: El servidor no recibió todos los parametros de conexion (SERVER,DATABASE,USER,PASSWORD) antes del correo a enviar";
				codErr = "3";
				ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
						parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
			}
	  		
	  		//Hasta aqui se han enviado todos los parametros ninguno nulo
			if(ERROR == null)
			{
				StringBuffer msj = new StringBuffer(), SMTPHOST = new StringBuffer(), SMTPPORT = new StringBuffer(), SMTPUSR = new StringBuffer(), SMTPPASS = new StringBuffer();
				MutableBoolean COBRAR = new MutableBoolean(false);
				MutableDouble COSTO = new MutableDouble(0.0), SALDO = new MutableDouble(0.0);
				// Primero obtiene info del SMTP
				if(!obtenInfoSMTP(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"), parametros.getProperty("DATABASE"),
						parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), SMTPHOST, SMTPPORT, SMTPUSR, SMTPPASS, msj, COSTO, SALDO, COBRAR))
				{
					System.out.println("El usuario y contraseña de servicio estan mal");
					ERROR = msj.toString();
					codErr = "2";
					ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
							parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 2);

				}
				else
				{
					if(COBRAR.booleanValue() && SALDO.doubleValue() < COSTO.doubleValue())
					{
						System.out.println("El servicio tiene un costo que no alcanza en el saldo");
						ERROR = "El servicio SMTP tiene un costo que no alcanza en el saldo";
						codErr = "2";
						ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
								parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 2);

					}
					else
					{
						Properties props;
						
						props = System.getProperties();
						props.put("mail.transport.protocol", "smtp");
						props.put("mail.smtp.port", Integer.parseInt(SMTPPORT.toString())); 
							
						// Set properties indicating that we want to use STARTTLS to encrypt the connection.
						// The SMTP session will begin on an unencrypted connection, and then the client
						// will issue a STARTTLS command to upgrade to an encrypted connection.
						props.put("mail.smtp.auth", "true");
						props.put("mail.smtp.starttls.enable", "true");
						props.put("mail.smtp.starttls.required", "true");
		
						// Create a Session object to represent a mail session with the specified properties. 
						Session session = Session.getDefaultInstance(props);
						MimeMessage mmsg = new MimeMessage(session);
						BodyPart messagebodypart = new MimeBodyPart();
						MimeMultipart multipart = new MimeMultipart();
						
						if(!prepareMsg(parametros.getProperty("SOURCEMAIL"), parametros.getProperty("EMAIL"), parametros.getProperty("SUBJECT"), 
								parametros.getProperty("MIMETYPE"), parametros.getProperty("BODY"), msj, props, session, mmsg, messagebodypart, multipart))
						{
							System.out.println("No se permitió preparar el mensaje");
							ERROR = msj.toString();
							codErr = "3";
							ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
									parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
			
						}
						else
						{
							if(!adjuntarArchivo(msj, messagebodypart, multipart, archivos))
							{
								System.out.println("No se permitió adjuntar archivos al mensaje");
								ERROR = msj.toString();
								codErr = "3";
								ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
										parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
			
							}
							else
							{
								if(!sendMsg(SMTPHOST.toString(), SMTPUSR.toString(), SMTPPASS.toString(), 
																				msj, session, mmsg, multipart))
								{
									System.out.println("No se permitió enviar el mensaje");
									ERROR = msj.toString();
									codErr = "3";
									ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), parametros.getProperty("SERVER"),
											parametros.getProperty("USER"), parametros.getProperty("PASSWORD"), request, ERROR, 3);
								}
								else
								{
									ingresarRegistroExitoso(parametros.getProperty("SERVER"), parametros.getProperty("DATABASE"), parametros.getProperty("SUBJECT"), COSTO, SALDO, COBRAR);
								
									//Devuelve la respuesta al cliente
									Element Correo = new Element("Correo");
									Correo.setAttribute("Subject",parametros.getProperty("SUBJECT"));
									Correo.setAttribute("MsjError","");
									Document Reporte = new Document(Correo);
										
									Format format = Format.getPrettyFormat();
									format.setEncoding("utf-8");
									format.setTextMode(TextMode.NORMALIZE);
									XMLOutputter xmlOutputter = new XMLOutputter(format);
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									xmlOutputter.output(Reporte, out);
									
									byte[] data = out.toByteArray();
									ByteArrayInputStream istream = new ByteArrayInputStream(data);
												
									String destino = "Correo.xml";
									JBajarArchivo fd = new JBajarArchivo();
									fd.doDownload(response, getServletConfig().getServletContext(), istream, "text/xml", data.length, destino);
										
								}
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
	
	/*
	@SuppressWarnings("rawtypes")
	private boolean enviarCorreo(String HOST, String USERNAME, String PASSWORD,
			String BODY, String MIMETYPE, String SUBJECT, String EMAIL, String SOURCEMAIL, StringBuffer msj,
				Properties props, Session session, MimeMessage mmsg, BodyPart messagebodypart, MimeMultipart multipart, Vector archivos)
	{
		//Prepara el mensaje
		if(prepareMsg(SOURCEMAIL, EMAIL, SUBJECT, MIMETYPE, BODY, msj, 
							props, session, mmsg, messagebodypart, multipart))
		{
			if(!archivos.isEmpty())
			{
				FileItem actual = null;

				try 
				{
					for(int i = 0; i < archivos.size(); i++) 
					{
						actual = (FileItem)archivos.elementAt(i);
	            	
		            	messagebodypart = new MimeBodyPart();
						DataSource source = new FileDataSource(new File(actual.getName()));
				    	
						byte[] sourceBytes = actual.get();
						OutputStream sourceOS = source.getOutputStream();
						sourceOS.write(sourceBytes);
						
				    	messagebodypart.setDataHandler(new DataHandler(source));
					    messagebodypart.setFileName(actual.getName());
					    multipart.addBodyPart(messagebodypart);
					}
				}
				catch (MessagingException e) 
				{
					e.printStackTrace();
					msj.append("Error de Mensajeria al cargar adjunto SMTP: " + e.getMessage());
					return false;
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					msj.append("Error de Entrada/Salida al cargar adjunto SMTP: " + e.getMessage());
					return false;
				}
			}
			
			if(sendMsg(HOST, USERNAME, PASSWORD, msj, session, mmsg, multipart))
				return true;
			
		}
		
		return false;
	}
	*/
	
	@SuppressWarnings("rawtypes")
	private boolean adjuntarArchivo(StringBuffer msj, BodyPart messagebodypart, MimeMultipart multipart, Vector archivos)
	{
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
						inputStream = actual.getInputStream();
					    byte[] sourceBytes = IOUtils.toByteArray(inputStream);
					    String name = actual.getName();
					    
					    messagebodypart = new MimeBodyPart();
					    
					    ByteArrayDataSource rawData = new ByteArrayDataSource(sourceBytes);
					    DataHandler data = new DataHandler(rawData);
					    
						messagebodypart.setDataHandler(data);
						messagebodypart.setFileName(name);
						multipart.addBodyPart(messagebodypart);
						////////////////////////////////////////////////
						/*
						messagebodypart = new MimeBodyPart();
						DataSource source = new FileDataSource(new File(actual.getName()));
					    	
						byte[] sourceBytes = actual.get();
						OutputStream sourceOS = source.getOutputStream();
						sourceOS.write(sourceBytes);
							
						messagebodypart.setDataHandler(new DataHandler(source));
						messagebodypart.setFileName(actual.getName());
						multipart.addBodyPart(messagebodypart);
						*/
						///////////////////////////////////////////////////////
						
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
				}
				return true;
			}
			catch (MessagingException e) 
			{
				e.printStackTrace();
				msj.append("Error de Mensajeria al cargar adjunto SMTP: " + e.getMessage());
				return false;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				msj.append("Error de Entrada/Salida al cargar adjunto SMTP: " + e.getMessage());
				return false;
			}
		
		}
		else
			return true;
	}
	
	private boolean prepareMsg(String FROM, String TO, String SUBJECT, String MIMETYPE, String BODY, StringBuffer msj,
					Properties props, Session session, MimeMessage mmsg, BodyPart messagebodypart, MimeMultipart multipart)
	{
		// Create a message with the specified information. 
		try 
		{
			mmsg.setFrom(new InternetAddress(FROM));
			mmsg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
			mmsg.setSubject(SUBJECT);
			messagebodypart.setContent(BODY, MIMETYPE);
			multipart.addBodyPart(messagebodypart);
			return true;
		} 
		catch (AddressException e) 
		{
			e.printStackTrace();
			msj.append("Error de Direcciones al preparar SMTP: " + e.getMessage());
			return false;
    	} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
			msj.append("Error de Mensajeria al preparar SMTP: " + e.getMessage());
			return false;
    	}
	}
	
    private boolean sendMsg(String HOST, String USERNAME, String PASSWORD, StringBuffer msj,
			Session session, MimeMessage mmsg, MimeMultipart multipart)
    {
    	try
    	{
    		mmsg.setContent(multipart);
    		// Create a transport.        
	    	Transport transport = session.getTransport();
	    	// Send the message.
	    	System.out.println(HOST + " " + USERNAME + " " + PASSWORD);
	    	transport.connect(HOST, USERNAME, PASSWORD);
	    	// Send the email.
	    	transport.sendMessage(mmsg, mmsg.getAllRecipients());
	    	transport.close();
	    	
	    	return true;
    	}
	    catch (MessagingException e) 
    	{
	    	e.printStackTrace();
			msj.append("Error de Mensajeria al enviar SMTP: " + e.getMessage());
			return false;
    	}
    	catch (Exception ex) 
    	{
    		ex.printStackTrace();
			msj.append("Error general de mensaje al enviar SMTP: " + ex.getMessage());
			return false;
    	}
    	
    }
    
	private void ingresarRegistroExitoso(String servidor, String basedatos, String titulo, MutableDouble COSTO, MutableDouble SALDO, MutableBoolean COBRAR)
	{
		Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_PAC_REGISTROS_EXITOSOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.obtFechaHoraSQL(cal) + "','" + JUtil.q(servidor) + "','" + 
	    			  JUtil.q(basedatos) + "','" + JUtil.q(titulo) + "','0','SMTP'," + COSTO.doubleValue() + "," + (COBRAR.booleanValue() ? JUtil.redondear((SALDO.doubleValue() - COSTO.doubleValue()),4) : JUtil.redondear(SALDO.doubleValue(),4)) + ");\n";
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
		//System.out.println(sb.toString());
	    Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_PAC_REGISTROS_FALLIDOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.q(IP) + "','" + JUtil.q(host) + "','" + JUtil.obtFechaHoraSQL(cal) + "','" + (servidor != null ? JUtil.q(servidor) : "nulo") + "','" + 
	    			  (usuario != null ? JUtil.q(usuario) : "nulo") + "','" + (password != null ? JUtil.q(password) : "nulo") + "','','" + JUtil.q(error) + "','" + nivelError + "','SMTP')";
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
	
	private boolean obtenInfoSMTP(String IP, String host, String servidor, String basedatos, String usuario, String password, 
			StringBuffer SMTPHOST, StringBuffer SMTPPORT, StringBuffer SMTPUSR, StringBuffer SMTPPASS, StringBuffer msj, MutableDouble COSTO, MutableDouble SALDO, MutableBoolean COBRAR)
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
	              FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_smtp");
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
	  						
	    					  if(key.equals("HOST"))
	    						  SMTPHOST.append(value);
	    					  else if(key.equals("PORT"))
	    						  SMTPPORT.append(value);
	    					  else if(key.equals("PASS"))
	    						  SMTPPASS.append(value);
	    					  else if(key.equals("USER"))
	    						  SMTPUSR.append(value);
	  												
	    				  }
	    				  catch(NoSuchElementException e)
	    				  {
	    					  msj.append("ERROR: La informacion del proveedor de SMTP parece estar mal configurada");
	    					  return false;
	    				  }
	    			  }
	    			  
	    		  }
	              buff.close();
	    	  }
	    	  catch (FileNotFoundException e1)
	    	  {
	    		  e1.printStackTrace();
	    		  msj.append("Error de archivos SMTP: " + e1.getMessage());
	    		  return false;
	    	  }
	    	  catch (IOException e1) 
	    	  {
	    		  e1.printStackTrace();
	    		  msj.append("Error de Entrada/Salida SMTP: " + e1.getMessage());
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
		    	  COSTO.setValue(srv.getAbsRow(0).getCostoMail());
		    	  SALDO.setValue(srv.getAbsRow(0).getSaldo());
		    	  COBRAR.setValue(srv.getAbsRow(0).getCobrarMail());
		    	  return true;
		      }
	      }
	    
	}

}
