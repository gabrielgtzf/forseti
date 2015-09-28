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
/*
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
import javax.servlet.http.HttpServletRequest;

import forseti.sets.JBDSSet;
*/
public class JFsiSMTP 
{
/*	
	public static final short ERROR = 3;
	public static final short OKYDOKY = 0;
	
	private short m_StatusSMTP;
	private String m_Error;
	
	private String m_SMTP_USERNAME;
    private String m_SMTP_PASSWORD;
    private String m_HOST;    
    // Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    private int m_PORT;
    // Create a Properties object to contain connection configuration information.
	private Properties m_Props;
	private Session m_Session;
	private MimeMessage m_Msg;
	private BodyPart m_MessageBodyPart;
	private MimeMultipart m_Multipart;
*/	
	public JFsiSMTP()
	{
/*		m_PORT = 25;
		m_SMTP_USERNAME = "";
	    m_SMTP_PASSWORD = "";
	    m_HOST = "";  
	    
		m_Props = System.getProperties();
		m_Props.put("mail.transport.protocol", "smtp");
		m_Props.put("mail.smtp.port", m_PORT); 
		
		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
	    // will issue a STARTTLS command to upgrade to an encrypted connection.
		m_Props.put("mail.smtp.auth", "true");
		m_Props.put("mail.smtp.starttls.enable", "true");
		m_Props.put("mail.smtp.starttls.required", "true");

	    // Create a Session object to represent a mail session with the specified properties. 
		m_Session = Session.getDefaultInstance(m_Props);
	
		try
		{
			FileReader file = new FileReader("/usr/local/forseti/bin/.forseti_smtp");
			BufferedReader buff     = new BufferedReader(file);
			boolean eof             = false;
			
			while(!eof)
			{
				String line = buff.readLine();
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
						{
							if(!value.equals("false"))
							{
								m_HOST = value;
							}
							else
							{
								m_StatusSMTP = ERROR;
					    		m_Error = "El servidor smtp no está configurado aun... Necesitas configurarlo para poder enviar correos electrónicos";
					    		return;
							}
						}
						else if(key.equals("PASS"))
							m_SMTP_PASSWORD = value;
						else if(key.equals("USER"))
							m_SMTP_USERNAME = value;
						else if(key.equals("PORT"))
						{	
							try{ m_PORT = Integer.parseInt(value); } catch(NumberFormatException e) { m_PORT = 25; }
						}
					}
					catch(NoSuchElementException e)
					{
						m_StatusSMTP = ERROR;
			    		m_Error = "El archivo de configuracion SMTP parece estar corrupto";
			    		return;
					}
				}
			
			}
			buff.close();
		}
		catch (FileNotFoundException e1)
		{
			m_StatusSMTP = ERROR;
    		m_Error = "Error de archivos SMTP: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			m_StatusSMTP = ERROR;
    		m_Error = "Error de Entrada/Salida SMTP: " + e1.getMessage();
		} */
	}
/*	
	protected void prepareMsg(String FROM, String TO, String SUBJECT, String MIMETYPE, String BODY)
	{
		// Create a message with the specified information. 
		try 
		{
			m_Msg = new MimeMessage(m_Session);
			m_Msg.setFrom(new InternetAddress(FROM));
			m_Msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
			m_Msg.setSubject(SUBJECT);
			m_MessageBodyPart = new MimeBodyPart();
			m_MessageBodyPart.setContent(BODY, MIMETYPE);
			m_Multipart = new MimeMultipart();
			m_Multipart.addBodyPart(m_MessageBodyPart);
		} 
		catch (AddressException e) 
		{
			m_StatusSMTP = ERROR;
    		m_Error = "Error de Direcciones al preparar SMTP: " + e.getMessage();
		} 
		catch (MessagingException e) 
		{
			m_StatusSMTP = ERROR;
    		m_Error = "Error de Mensajeria al preparar SMTP: " + e.getMessage();
		}
	}
	
	public void addAttachment(String path, String name)
	{
		// Create a message with the specified information. 
		try 
		{
			m_MessageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(path);
	    	m_MessageBodyPart.setDataHandler(new DataHandler(source));
		    m_MessageBodyPart.setFileName(name);
		    m_Multipart.addBodyPart(m_MessageBodyPart);
		} 
		catch (MessagingException e) 
		{
			m_StatusSMTP = ERROR;
    		m_Error = "Error de Mensajeria al cargar adjunto SMTP: " + e.getMessage();
		}
	}
	
    public void sendMsg()
    {
    	try
    	{
    		if(m_StatusSMTP != ERROR)
    		{
    			m_Msg.setContent(m_Multipart);
    			// Create a transport.        
    			Transport transport = m_Session.getTransport();
    			// Send the message.
    			System.out.println("Clase Vieja: " + m_HOST + " " + m_SMTP_USERNAME + " " + m_SMTP_PASSWORD);
    			transport.connect(m_HOST, m_SMTP_USERNAME, m_SMTP_PASSWORD);
    			// Send the email.
    			transport.sendMessage(m_Msg, m_Msg.getAllRecipients());
    			transport.close();
    		}
    	}
    	catch (MessagingException e) 
    	{
    		m_StatusSMTP = ERROR;
    		m_Error = "Error de Mensajeria al enviar SMTP: " + e.getMessage();
    	}
    	catch (Exception ex) 
    	{
    		m_StatusSMTP = ERROR;
    		m_Error = "Error general de mensaje al enviar SMTP: " + ex.getMessage();
    	}
    	
    }
    
    private String obtenerEMail(HttpServletRequest request) 
    		throws ServletException, IOException
    {
    	//Primero Verifica el remitente
		JBDSSet dbset = new JBDSSet(request);
		dbset.ConCat(true);
	    dbset.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
	    dbset.Open();
	    
	    return dbset.getAbsRow(0).getMail();
    }
    
    public short getStatusSMTP()
    {
    	return m_StatusSMTP;
    }
    
    public String getError()
    {
    	return m_Error;
    }
    
    public void enviarCFDI(HttpServletRequest request, String tipo, String id, String esp1, String nombre, String email) 
    		throws ServletException
    {
    	try
		{
    		String path = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/";
        	String origenMSG = path + "smtp/" + tipo + "-FORSETI.msg";
    		String archivoXML, archivoPDF;
    		if(tipo.equals("NOM"))
    		{
    			archivoXML = path + "TFDs/SIGN_" + tipo + "-" + id + "-" + esp1 + ".xml"; 
    			archivoPDF = path + "PDFs/" + tipo + "-" + id + "-" + esp1 + ".pdf";
    		}
    		else
    		{
    			archivoXML = path + "TFDs/SIGN_" + tipo + "-" + id + ".xml"; 
    			archivoPDF = path + "PDFs/" + tipo + "-" + id + ".pdf";
    		}
    		String resXML = "forseti-cfdi-" + tipo.toLowerCase() + ".xml";
    		String resPDF = "forseti-cfdi-" + tipo.toLowerCase() + ".pdf";
    		
    		FileReader file = new FileReader(origenMSG);
			BufferedReader buff     = new BufferedReader(file);
			String SUBJECT = "", MIMETYPE = "text/plain", pagina = "", line = "";
			int cont = 0;
			while((line = buff.readLine()) != null)
			{
				cont++;
				if(cont == 1)
					SUBJECT = line;
				else if(cont == 2)
					MIMETYPE = line;
				else
					pagina += line + "\n";
			}
			buff.close();
			//ahora cambia personaliza la pagina
			pagina = JUtil.replace(pagina, "[[:nombre:]]", nombre);
			//Ahora manda el mensaje
			
		    //Prepara el mensaje
			prepareMsg(obtenerEMail(request), email, SUBJECT, MIMETYPE, pagina);
			if(m_StatusSMTP != ERROR)
			{
				addAttachment(archivoXML, resXML);
				if(m_StatusSMTP != ERROR)
				{
					addAttachment(archivoPDF, resPDF);
					if(m_StatusSMTP != ERROR)
					{
						sendMsg();
					}
				}
			}
		}
		catch (FileNotFoundException e1)
		{
			m_StatusSMTP = ERROR;
    		m_Error = "Error de archivos SMTP: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			m_StatusSMTP = ERROR;
    		m_Error = "Error de Entrada/Salida SMTP: " + e1.getMessage();
		}
    }
	
  */ 
}
