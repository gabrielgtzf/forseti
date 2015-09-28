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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import forseti.sets.JBDSSet;

public class JFsiSMTPClient
{
	public static final short ERROR = 3;
	public static final short OKYDOKY = 0;
	
	private short m_StatusSMTP;
	private String m_Error;
	
	private String m_SMTP_USERNAME;
    private String m_SMTP_PASSWORD;
    private String m_HOST;    
    private String m_URL;
   	
	public JFsiSMTPClient()
	{
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
							m_SMTP_PASSWORD = value;
						else if(key.equals("USER"))
							m_SMTP_USERNAME = value;
												
					}
					catch(NoSuchElementException e)
					{
						m_StatusSMTP = ERROR;
						m_Error = "La informacion de conexión al servicio SMTP forseti parece estar mal configurada";
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
			
		    /*Prepara el mensaje
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
			}*/
			
			///////////////////////////////////////////////////////////////////////////
			String urlParameters = 	"SERVER=" + URLEncoder.encode(m_HOST,"ISO-8859-1") +
					"&DATABASE=" + URLEncoder.encode(JUtil.getSesion(request).getBDCompania(),"ISO-8859-1") +
					"&USER=" + URLEncoder.encode(m_SMTP_USERNAME,"ISO-8859-1") +
					"&PASSWORD=" + URLEncoder.encode(m_SMTP_PASSWORD,"ISO-8859-1") +
					"&FILEXML=" + URLEncoder.encode(archivoXML,"ISO-8859-1") +
					"&FILEPDF=" + URLEncoder.encode(archivoPDF,"ISO-8859-1") +
					"&RESXML=" + URLEncoder.encode(resXML,"ISO-8859-1") +
					"&RESPDF=" + URLEncoder.encode(resPDF,"ISO-8859-1") +
					"&BODY=" + URLEncoder.encode(pagina,"ISO-8859-1") +
					"&MIMETYPE=" + URLEncoder.encode(MIMETYPE,"ISO-8859-1") +
					"&SUBJECT=" + URLEncoder.encode(SUBJECT,"ISO-8859-1") +
					"&EMAIL=" + URLEncoder.encode(email,"ISO-8859-1") +
					"&SOURCEMAIL=" + URLEncoder.encode(obtenerEMail(request),"ISO-8859-1");
			String urlString = "https://" + m_URL + "/servlet/SAFSmtpConn";
			//System.out.println(urlString);
			//System.out.println(urlParameters);
						
			URL url = new URL(urlString);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			conn.setRequestProperty("Content-Language", "es-MX");  
			//conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			//Send request
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush ();
			wr.close ();
			
			conn.connect();
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				System.out.println("Conectado a revidor SMTP");
				// Recibe la respuesta del servidor, esta debe estar en formato xml
				SAXBuilder builder = new SAXBuilder();
				Document document = (Document)builder.build(conn.getInputStream());
				Element Correo = document.getRootElement();
				
				if(Correo.getName().equals("Correo"))
				{
					System.out.println("El correo se cargo con exito");
				}
				else if(Correo.getName().equals("SIGN_ERROR"))// Significan errores
				{
					m_StatusSMTP = ERROR;
					m_Error += "Codigo de Error JSmtpConn: " + Correo.getAttribute("CodError") + "<br>" + Correo.getAttribute("MsjError");
				}
				else
				{
					m_StatusSMTP = ERROR;
					m_Error += "ERROR: El archivo recibido es un archivo XML, pero no parece ser una respuesta de correo ni una respuesta de ERROR del servidor JSmtpConn. Esto debe ser investigado de inmediato, Contacta con tu intermediario de correos Forseti para que te explique el problema";
				}
				// Fin de archivo grabado
				conn.disconnect();
			}
			else // La conexion no tuvo exito
			{
				System.out.println("No se establecio la conexion");
				m_StatusSMTP = ERROR;
				m_Error += "ERROR: Al intentar conectarse al servidor. Revisa los datos del intermediario de Correos Forseti en el archivo de configuración";
			}
			///////////////////////////////////////////////////////////////////////////
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			m_StatusSMTP = ERROR;
    		m_Error = "Error de archivos SMTP: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			m_StatusSMTP = ERROR;
    		m_Error = "Error de Entrada/Salida SMTP: " + e1.getMessage();
		} 
    	catch (JDOMException e1) 
		{
    		e1.printStackTrace();
    		m_StatusSMTP = ERROR;
    		m_Error = "Error de Entrada/Salida SMTP: " + e1.getMessage();
		}
    }
	
    public void enviarCFDIMPE(HttpServletRequest request, String tipo, String id, String esp1, String nombre, String email) 
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
			
			
			//Ahora agrega los archivos mensaje
			String charset = "UTF-8";
	        File uploadFile1 = new File(archivoXML);
	        File uploadFile2 = new File(archivoPDF);
	        String requestURL = "https://" + m_URL + "/servlet/SAFSmtpConn";
	 
	        MultipartUtility multipart = new MultipartUtility(requestURL, charset);
	             
	        multipart.addHeaderField("User-Agent", "CodeJava");
	        multipart.addHeaderField("Test-Header", "Header-Value");
	             
	        multipart.addFormField("SERVER", m_HOST);
	        multipart.addFormField("DATABASE", JUtil.getSesion(request).getBDCompania());
	        multipart.addFormField("USER", m_SMTP_USERNAME);
	        multipart.addFormField("PASSWORD", m_SMTP_PASSWORD);
	        multipart.addFormField("FILEXML", archivoXML);
	        multipart.addFormField("FILEPDF", archivoPDF);
	        multipart.addFormField("RESXML", resXML);
	        multipart.addFormField("RESPDF", resPDF);
	        multipart.addFormField("BODY", pagina);
	        multipart.addFormField("MIMETYPE", MIMETYPE);
	        multipart.addFormField("SUBJECT", SUBJECT);
	        multipart.addFormField("EMAIL", email);
	        multipart.addFormField("SOURCEMAIL", obtenerEMail(request));    
	        
	        multipart.addFilePart("fileUpload1", uploadFile1);
	        multipart.addFilePart("fileUpload2", uploadFile2);
	 
	        InputStream response = multipart.connect();

	        // Recibe la respuesta del servidor, esta debe estar en formato xml
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document)builder.build(response);
			Element Correo = document.getRootElement();
			
			if(Correo.getName().equals("Correo"))
			{
				System.out.println("El correo se cargo con exito");
			}
			else if(Correo.getName().equals("SIGN_ERROR"))// Significan errores
			{
				m_StatusSMTP = ERROR;
				m_Error += "Codigo de Error JSmtpConn: " + Correo.getAttribute("CodError") + "<br>" + Correo.getAttribute("MsjError");
			}
			else
			{
				m_StatusSMTP = ERROR;
				m_Error += "ERROR: El archivo recibido es un archivo XML, pero no parece ser una respuesta de correo ni una respuesta de ERROR del servidor JSmtpConn. Esto debe ser investigado de inmediato, Contacta con tu intermediario de correos Forseti para que te explique el problema";
			}
			
			multipart.disconnect();
			// Fin de archivo grabado
			///////////////////////////////////////////////////////////////////////////
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			m_StatusSMTP = ERROR;
    		m_Error = "Error de archivos SMTP: " + e1.getMessage();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			m_StatusSMTP = ERROR;
    		m_Error = "Error de Entrada/Salida SMTP: " + e1.getMessage();
		} 
    	catch (JDOMException e1) 
		{
    		e1.printStackTrace();
    		m_StatusSMTP = ERROR;
    		m_Error = "Error de JDOMException SMTP: " + e1.getMessage();
		}
    }   
}
