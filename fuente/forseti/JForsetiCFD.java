/**
 Esta es la Documentacion del CFD
 */

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.FactoryConfigurationError;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import forseti.JFacturasXML;
import forseti.sets.JBDSSet;

public class JForsetiCFD
{
	public static final short NULO = -1;
	public static final short ERROR = 3;
	public static final short SUBIDOS = 2;
	public static final short OKYDOKY = 0;
	
	// Datos del certificado (Cualquiera que este cargado en esta sesión).
	protected short m_StatusCFD;
	protected byte m_StatusCertComp;
	protected String m_ArchivoCertificadoPDF;
	protected String m_ArchivoLLaveXML;
	protected String m_Error;
	protected String m_ModulusC;
	protected String m_ModulusK;
	protected String m_Serial;
	protected String m_Certificado;
	protected Date m_notBefore;
	protected Date m_notAfter;
	protected String m_NoCertificado;
	
	public JForsetiCFD()
	{
		m_StatusCertComp = NULO;
		m_ArchivoCertificadoPDF = "";
		m_ArchivoLLaveXML = "";
		m_Error = "";
		m_ModulusC = "";
		m_ModulusK = "";
		m_Serial = "";
		m_Certificado = "";
		m_notBefore = new Date();
		m_notAfter = new Date();
		m_NoCertificado = "";
	}
	
	
	
	public void resetearCertComp()
	{
		m_StatusCertComp = NULO;
		m_ArchivoCertificadoPDF = "";
		m_ArchivoLLaveXML = "";
		m_Error = "";
	}
	
	public String getError()
	{
		return m_Error;
	}
	
	public short getStatusCertComp()
	{
		return m_StatusCertComp;
	}
	
	private void setNoCertificado()
	{
		m_NoCertificado = "";
		for(int i = 0; i < m_Serial.length() -1; i++)
		{
			if(i % 2 != 0)
				m_NoCertificado += m_Serial.charAt(i);
			
		}
		
	}
	
	public String getNoCertificado()
	{
		return m_NoCertificado;
	}
	
	public String getArchivoCertificadoPDF()
	{
		return m_ArchivoCertificadoPDF;
	}
	
	public String getArchivoLLaveXML()
	{
		return m_ArchivoLLaveXML;
	}
	
	public Date getNotBefore()
	{
		return m_notBefore;
	}
	
	public Date getNotAfter()
	{
		return m_notAfter;
	}
	
	public String getSerial()
	{
		return m_Serial;
	}
	
	public boolean isValid()
	{
		Date hoy = Calendar.getInstance().getTime();
		
		if(hoy.getTime() < m_notBefore.getTime() || hoy.getTime() > m_notAfter.getTime())
			return false;
		else
			return true;
		
	}
	
	@SuppressWarnings("rawtypes")
	public short SubirArchivosCert(HttpServletRequest request, Vector archivos, StringBuffer mensaje) 
		throws ServletException, IOException
	{
		m_StatusCertComp = NULO;
		short res = -1;
		// Sube los archivos del certificado y la llave
    	String[] exts = { "cer","key" };
    	boolean [] frz = { true, true };
    	JSubirArchivo sa = new JSubirArchivo(512, "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/", exts, frz);
    	if(sa.processFiles(archivos) < 2) // significa que no encontró los dos archivos
		{
			m_StatusCertComp = ERROR;
			res = 3; 
			mensaje.append(JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR",3) + "<br>" + sa.getError());
    		return res;
    	}
    	m_StatusCertComp = SUBIDOS;
		m_ArchivoCertificadoPDF = sa.getFile(0);
		m_ArchivoLLaveXML = sa.getFile(1);
		//Ya se subieron los archivos del certificado, Ahora procede a verificarlos
		res = 0;
		mensaje.append("CER en: " + m_ArchivoCertificadoPDF + "<br>KEY en: " + m_ArchivoLLaveXML + "<br>");
		return res;
	}
	
	public short VerificarCertificadosSubidos(HttpServletRequest request, String ClaveLLave, StringBuffer mensaje) 
		throws ServletException, IOException // Comprueba el certificado con la llave ( si esta vigente, si concureda con la llave etc. )
	{
		short res = -1;
		if(m_StatusCertComp == NULO)
		{
			res = 3; 
			mensaje.append(JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR",4));
    		return res;
		}
		else if(m_StatusCertComp == OKYDOKY)
		{
			res = 0; 
			mensaje.append(JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCOK",1));
    		return res;
		}
		// Procede a verificar los certificados
		String pathcer = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoCertificadoPDF;
		String pathkey = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoLLaveXML;
		String pathcerpem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoCertificadoPDF + ".pem";
		String pathkeypem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoLLaveXML + ".pem";
		
		String CONTENT;
		
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		
		String ERROR = "";
		try 
		{ 
			CONTENT = "openssl x509 -inform DER -outform PEM -in " + pathcer + " -pubkey > " + pathcerpem;
			sc.setContent(CONTENT);
			sc.executeCommand();
			ERROR = sc.getError();
			if(ERROR.equals(""))
			{
				CONTENT = "openssl pkcs8 -inform DER -in " + pathkey + " -passin pass:" + ClaveLLave + " -out " + pathkeypem;
				sc.setContent(CONTENT);
				sc.executeCommand();
				ERROR += sc.getError();
			}
			if(ERROR.equals(""))
			{
				CONTENT = "openssl x509 -in " + pathcerpem + " -noout -modulus"; 
				sc.setContent(CONTENT);
				String outp = sc.executeCommand();
				ERROR += sc.getError();
				if(sc.getError().equals(""))
					m_ModulusC = getToken(outp, "Modulus");
			}
			if(ERROR.equals(""))
			{
				CONTENT = "openssl rsa  -in " + pathkeypem + " -noout -modulus"; 
				sc.setContent(CONTENT);
				String outp = sc.executeCommand();
				ERROR += sc.getError();
				if(sc.getError().equals(""))
					m_ModulusK = getToken(outp, "Modulus");
			}
			if(ERROR.equals(""))
			{
				if(!m_ModulusC.equals(m_ModulusK))
					ERROR += JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR",5); //"ERROR: El ModulusCer diferente de ModulusKey - La llave no pertenece a este certificado<br>";
			}
			if(ERROR.equals(""))
			{
				CONTENT = "openssl x509 -in " + pathcerpem + " -noout -sha1 -serial"; 
				sc.setContent(CONTENT);
				String outp = sc.executeCommand();
				ERROR += sc.getError();
				if(sc.getError().equals(""))
				{
					m_Serial = getToken(outp, "serial");
					setNoCertificado();
				}
			}
			if(ERROR.equals(""))
			{
				CONTENT = "openssl x509 -in " + pathcerpem + " -noout -sha1 -startdate";
				System.out.println(CONTENT);
				sc.setContent(CONTENT);
				String outp = sc.executeCommand();
				ERROR += sc.getError();
				if(sc.getError().equals(""))
					m_notBefore = JUtil.getFecha(getToken(outp, "notBefore"),"MMM dd HH:mm:ss yyyy z", Locale.ENGLISH);
			}
			if(ERROR.equals(""))
			{
				CONTENT = "openssl x509 -in " + pathcerpem + " -noout -sha1 -enddate";
				sc.setContent(CONTENT);
				String outp = sc.executeCommand();
				ERROR += sc.getError();
				if(sc.getError().equals(""))
					m_notAfter = JUtil.getFecha(getToken(outp, "notAfter"),"MMM dd HH:mm:ss yyyy z", Locale.ENGLISH);
			}
			if(ERROR.equals(""))
			{
				Date hoy = Calendar.getInstance().getTime();
				
				if(hoy.getTime() < m_notBefore.getTime() || hoy.getTime() > m_notAfter.getTime())
					ERROR += JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR2",1) + "<br>notBefore: " + m_notBefore + "<br>notAfter: " + m_notAfter; //"ERROR: Las fechas de este certificado ya son invalidas<br>notBefore: " + m_notBefore + "<br>notAfter: " + m_notAfter + "<br>";
				//System.out.println("ESTA ES LA FECHA: " + fecha.toString());
			}
		} 
		catch(Exception e) 
		{ 
			e.printStackTrace();
			ERROR += JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR2",2) + e.getMessage() + "<br>";
			ERROR += sc.getError();
		}
		//System.out.println("Esta es la salida:  " + OUTPUT);
		//System.out.println("Esta es la salida: " + m_ModulusC + " " + m_ModulusK + " " + m_Serial + " " + m_notBefore + " " + m_notAfter + " " + ERROR);

		if(!ERROR.equals(""))
		{
			m_StatusCertComp = JForsetiCFD.ERROR;
			m_Error = ERROR;
			res = 3;
			mensaje.append(JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR2",3) + "<br>" + ERROR);
			
    	}
		else
		{
			m_StatusCertComp = JForsetiCFD.OKYDOKY;
			m_Error = "";
			res = 0;
			mensaje.append(JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCOK",2));
		}
		return res;
	}
	
	// Este metodo se usa en la generacion de CFD
	public void CargarCertificadosComprobandolos(HttpServletRequest request, String ArchivoCertificado, String ArchivoLLave, String NoCertificado, Date notAfter, String tipo, Integer id) 
		throws ServletException, IOException // Comprueba el certificado con la llave ( si esta vigente, si concureda con la llave etc. )
	{
		m_StatusCertComp = NULO;
		
		// Procede a verificar los certificados
		String pathcerpem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + ArchivoCertificado + ".pem";
		//String pathkeypem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + ArchivoLLave + ".pem";
		String pathname = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/";
		String nombreArchivos = tipo + "-" + id.toString();
		String CONTENT;
		
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		
		String ERROR = "";
		try 
		{ 
			if(ERROR.equals(""))
			{
				m_NoCertificado = NoCertificado;
				if(m_NoCertificado.length() != 20)
					ERROR += "ERROR: La serie no parece ser v&aacute;lida o se cargo incorrectamente<br>Prueba el volver a configurar el certificado con su llave";
			}
			if(ERROR.equals(""))
			{
				
				CONTENT = "openssl x509 -in " + pathcerpem + " -out " + pathname + nombreArchivos + ".cer -sha1";
				sc.setContent(CONTENT);
				sc.executeCommand();
				ERROR += sc.getError();
				File f = new File(pathname + nombreArchivos + ".cer");
				int ch;
				StringBuffer outp = new StringBuffer("");
				FileInputStream fin = new FileInputStream(f);
				while( (ch = fin.read()) != -1)
					outp.append((char)ch);
				fin.close();
				//System.out.println("OUTP");
				//System.out.println(outp.toString());
				m_Certificado = obtCertificado(outp.toString());
				//System.out.println("M_CERTIFICADO");
				//System.out.println(m_Certificado);
			}
			if(ERROR.equals(""))
			{
				if(m_Certificado.equals(""))
					ERROR += "ERROR: El certificado no parece ser v&aacute;lido o se ley&oacute; incorrectamente<br>Prueba el volver a Sellar";
			}
			if(ERROR.equals(""))
			{
				m_notAfter = notAfter;
				Date hoy = Calendar.getInstance().getTime();
				
				if(hoy.getTime() > m_notAfter.getTime())
					ERROR += "ERROR: Este certificado parece estar caducado<br>notAfter: " + m_notAfter + "<br>";
				//System.out.println("ESTA ES LA FECHA: " + fecha.toString());
			}
		} 
		catch(Exception e) 
		{ 
			e.printStackTrace();
			ERROR += "ERROR DESCONOCIDO DE SCRIPT AL CARGAR Y COMPROBAR CERTIFICADOS: " + e.getMessage() + "<br>";
			ERROR += sc.getError();
		}
		//System.out.println("Esta es la salida:  " + OUTPUT);
		//System.out.println("Esta es la salida: " + m_ModulusC + " " + m_ModulusK + " " + m_Serial + " " + m_notBefore + " " + m_notAfter + " " + ERROR);
	
		if(!ERROR.equals(""))
		{
			m_StatusCertComp = JForsetiCFD.ERROR;
			m_Error = ERROR;
		}
		else
		{
			m_StatusCertComp = OKYDOKY;
			m_ArchivoCertificadoPDF = ArchivoCertificado;
			m_ArchivoLLaveXML = ArchivoLLave;
			m_Error = "";
		}
		
	}
	
    private String getToken(String str, String elem)
    {
    	try
    	{
    		StringTokenizer st = new StringTokenizer(str,"=");
    		String key    = st.nextToken();
    		String value       = st.nextToken();
    		if(key.equals(elem))
    			return value;
    		else
    			return "";
    	}
    	catch(NoSuchElementException e)
    	{
    		return "";
    	}
    }
    
    private String obtCertificado(String cert)
    {
    	//System.out.println(cert);
    	String res;
    	try
    	{
    		res = cert.substring("-----BEGIN CERTIFICATE-----\n".length(),cert.indexOf("\n-----END CERTIFICATE-----"));
    	   	return res;
    	}
    	catch(StringIndexOutOfBoundsException e)
    	{
    		return "";
    	}
    }
    
    /* Procesos de generacion del reporte mensual
	public void generarReporteMensual(HttpServletRequest request, int mes, int ano)
		throws ServletException, IOException
	{
		JBDSSet set = new JBDSSet(request);
		set.ConCat(true);
	    set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
	    set.Open();
	    //System.out.println(set.m_Where);
	    
	    
	    m_StatusCertComp = NULO;
		
		// Procede a generar el reporte
		String nombreArchivo = "/usr/local/forseti/bin/" + JUtil.getSesion(request).getBDCompania() + "/" + mes + "-" + ano + ".txt";
		String nombreArchivoSQL = "1" + frfc(set.getAbsRow(0).getRFC()) + JUtil.convertirMesNumerico(mes) + ano + ".txt";
		JCFDSet cfd = new JCFDSet(request);
		cfd.m_Where = "Year(Fecha) = " + ano + " and Month(Fecha) = " + mes;
		cfd.m_OrderBy = "Fecha ASC";
		cfd.Open();
		
		if(cfd.getNumRows() == 0)
		{
			m_StatusCertComp = JForsetiCFD.ERROR;
			m_Error = "No se pudo generar el reporte mensual porque no existen CFDs del mes " + mes + " / " + ano;
			return;
		}
		
		String reporte = "";
		for(int i = 0; i < cfd.getNumRows(); i++)
		{
			reporte += "|" + frfc(cfd.getAbsRow(i).getRFC()) + "|" + cfd.getAbsRow(i).getSerie() + "|" + cfd.getAbsRow(i).getFolio() + "|" + cfd.getAbsRow(i).getNoAprobacion() + 
				"|" + JUtil.obtFechaTxt(cfd.getAbsRow(i).getFecha(), "dd/MM/yyyy") + " " + JUtil.obtHoraTxt(cfd.getAbsRow(i).getHora(), "hh:mm:ss")  + "|" + c6d(cfd.getAbsRow(i).getTotal()) +
				"|" + c6d(cfd.getAbsRow(i).getImpuesto()) + "|" + cfd.getAbsRow(i).getEstatus() + "|" + cfd.getAbsRow(i).getEfecto() + "|" + cfd.getAbsRow(i).getPedimento() + "|" + cfd.getAbsRow(i).getFechaPedimento() + "|" + cfd.getAbsRow(i).getAduana() + "|\r\n";
		}
		
		String ERROR = "";
		
		try 
		{
			File f = new File(nombreArchivo);
			FileOutputStream fout = new FileOutputStream(f);
			fout.write(reporte.getBytes("UTF-8"));
			if (fout != null)
				fout.close();
			// Por ultimo, Si no han habido errores, graba en la base de datos llamando al procedimiento almacenado
			String str = "EXEC sp_cfd_repmens_archivo " + mes + "," + ano + ",'" + nombreArchivoSQL + "'";
			short idmensaje = -1; String mensaje = "";
		    Connection con = JAccesoBD.getConexionSes(request);
		    Statement s    = con.createStatement();
		    ResultSet rs   = s.executeQuery(str);
		    if(rs.next())
		    {
		    	idmensaje = rs.getShort("ERR");
		    	mensaje = rs.getString("RES");
		    }
		    s.close();
		    JAccesoBD.liberarConexion(con);
		        
		    if(idmensaje != 0)
		    	ERROR += " " + mensaje;
		    
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR += "ERROR DESCONOCIDO DE ARCHIVOS AL GENERAR CFD: " + e.getMessage() + "<br>";
		} 
		catch(SQLException e)
	    {
			e.printStackTrace();
			ERROR += "ERROR DEL SQL AL GENERAR CFD: " + e.getMessage() + "<br>";
	    }
	    
		if(!ERROR.equals(""))
		{
			m_StatusCertComp = JForsetiCFD.ERROR;
			m_Error = ERROR;
		}
		else
		{
			m_StatusCertComp = OKYDOKY;
			m_Error = "";
		}
	}
	*/
	/* 
	 El metodo fcvenomsat formatea el movimiento de nomina interno para quedar como clave para cfdi de nomina de 3 caracteres o mas, añadiento el caracter 0 a ids menores a 100
	 */
	protected String fcvenomsat(int id_mov)
	{
		String res = "000";
		
		if(id_mov <= 0)
			res = "000";
		else if(id_mov >= 1 && id_mov <= 9)
			res = "00" +  Integer.toString(id_mov);
		else if(id_mov >= 10 && id_mov <= 99)
			res = "0" +  Integer.toString(id_mov);
		else
			res = Integer.toString(id_mov);
		
		return res;
	}
	/* 
	 El metodo fclabe formatea el elemento clabe de la cadena original quitando cualquier caracter diferente de 0-9
	 */
	protected String fclabe(String clabe)
	{
		String res = "";

		for(int i = 0; i < clabe.length(); i++)
		{
			if(clabe.charAt(i) >= 48 && clabe.charAt(i) <= 57)
				res += clabe.charAt(i);
	    }

		return res;
	}
	
	/* 
	 El metodo c6d formatea las cantidades a 4 digitos regresando como cadena
	 */
	protected String c6d(Float cant)
	{
		Float res = JUtil.redondear(cant, 6);
		StringBuffer str = new StringBuffer(res.toString());
		int index = str.indexOf(".");
		if(index == -1) // No hay punto, por lo tanto lo agrega con los seis ceros 
			str.append(".000000");
		else // Si existe el punto, Agrega los ceros restantes
		{
			int totCeros = str.length() - (index + 1);
			switch(totCeros)
			{
			case 1:	
				str.append("00000");
				break;
			case 2:	
				str.append("0000");
				break;
			case 3:	
				str.append("000");
				break;
			case 4:	
				str.append("00");
				break;
			case 5:	
				str.append("0");
				break;
			
			}
		}
		return str.toString();
	}
    /* Procesos de validacion de un CFD para cuando tengamos que validar su sintaxis
	public static boolean validarXMLvsXSD(String sFichXml)
	{
		boolean bIsXmlOk = false;
		boolean NAME_SPACE_AWARE = true;
		boolean VALIDATING = true;
		String SCHEMA_LANGUAGE ="http://java.sun.com/xml/jaxp/properties/schemaLanguage";
		String SCHEMA_LANGUAGE_VAL = "http://www.w3.org/2001/XMLSchema";
		String SCHEMA_SOURCE ="http://java.sun.com/xml/jaxp/properties/schemaSource";
		String sFichXsd = "../conf/cfdv3.xsd";
		try 
		{
			Reader xmlReader;
			Reader xsdReader;
	  
			xmlReader = new FileReader(sFichXml);
			xsdReader = new FileReader(sFichXsd);
		  
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(NAME_SPACE_AWARE);
			factory.setValidating(VALIDATING);
		  
			SAXParser parser = factory.newSAXParser();
			parser.setProperty(SCHEMA_LANGUAGE, SCHEMA_LANGUAGE_VAL);
			parser.setProperty(SCHEMA_SOURCE, new InputSource(xsdReader));
		   
			DefaultHandler handler = new XmlDefaultHandler();
			parser.parse(new InputSource(xmlReader), handler);
			bIsXmlOk = true;
		}
		catch(FactoryConfigurationError e) 
		{
			System.out.println(e.toString());  
		}
		catch (ParserConfigurationException e) 
		{
		  	System.out.println(e.toString()); 
		}
		catch (SAXException e) 
		{
		  	System.out.println(e.toString()); 
		}
		catch (IOException e) 
		{
		  	System.out.println(e.toString()); 
		}
		
		return bIsXmlOk;
	}// Fin de validarXMLvsXSD
    */
	
	public static class XmlDefaultHandler extends DefaultHandler 
	{
		public void error(SAXParseException spe) throws SAXException 
		{
			throw spe;
		}

		public void fatalError(SAXParseException spe) throws SAXException 
		{
			throw spe;
		}

	 }
	// Fin de XmlDefaultHandler
	
	/* 
	 El metodo xmlsec formatea un elemento para que lleve la secuencia de escape valida para los caracteres
	 & " < > ' rfc de la cadena original quitando guiones del rfc
	 */
	protected String xmlse(String str)
	{
		if(str == null)
			return "";
		
		String res = "";

		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) == '&')
				res += "&amp;";
			else if(str.charAt(i) == '"')
				res += "&quot;";
			else if(str.charAt(i) == '<')
				res += "&lt;";
			else if(str.charAt(i) == '>')
				res += "&gt;";
			else if(str.charAt(i) == 39)
				res += "&apos;";
			else		
				res += str.charAt(i);
	    }

		return res;
	}
	
	// Obtiene el texto de un elemento de un xml
	@SuppressWarnings("rawtypes")
	public void domElementText(Element current, int depth, String name, StringBuffer res, String func)
	{
		
		List children = current.getChildren();
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) 
		{
			Element child = (Element) iterator.next();
			if(child.getName().equals(name))
			{
				if(func.equals("norm"))
					res.append(child.getTextNormalize());
				else if(func.equals("trim"))
					res.append(child.getTextTrim());
				else
					res.append(child.getText());
				
				return;
			}
			domElementText(child, depth+1, name, res, func);
		}
	}

	@SuppressWarnings("rawtypes")
	public short SubirArchivosCFDI(HttpServletRequest request, Vector archivos, StringBuffer mensaje, String tipo) 
			throws ServletException, IOException
	{
		m_StatusCertComp = NULO;
		short res = -1;
		// Sube los archivos del xml y pdf
		String[] exts = { "xml","pdf" };
		boolean [] frz = { true, false };
		JSubirArchivo sa;
		if(tipo.equals("E"))
			sa = new JSubirArchivo(512, "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/", exts, frz);
		else if(tipo.equals("I"))
			sa = new JSubirArchivo(512, "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/", exts, frz);
		else if(tipo.equals("N"))
			sa = new JSubirArchivo(512, "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/", exts, frz);
		else
			sa = new JSubirArchivo(512, "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/", exts, frz);

		if(sa.processFiles(archivos) < 1) // significa que no encontró los dos archivos
		{
			m_StatusCertComp = ERROR;
			res = 3; 
			mensaje.append(/**/JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR",3) + "<br>" + sa.getError());
			return res;
		}
		m_StatusCertComp = SUBIDOS;
		m_ArchivoLLaveXML = sa.getFile(0);
		m_ArchivoCertificadoPDF = sa.getFile(1); //
		//Ya se subieron los archivos del certificado, Ahora procede a verificarlos
		res = 0;
		mensaje.append("PDF en: " + m_ArchivoCertificadoPDF + "<br>XML en: " + m_ArchivoLLaveXML + "<br>");
		return res;
	}

	public short VerificarFacturasSubidas(HttpServletRequest request, JFacturasXML factxml, StringBuffer mensaje, String tipo) 
			throws ServletException, IOException // Comprueba el archivo xml de la factura
	{
		short res = -1;
		if(m_StatusCertComp == NULO)
		{
			res = 3; 
			mensaje.append(/**/JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCERR",4));
			return res;
		}
		else if(m_StatusCertComp == OKYDOKY)
		{
			res = 0; 
			mensaje.append(/**/JUtil.Msj("CEF","ADM_CFDI","DLG","MSJ-PROCOK",1));
			return res;
		}
	
		// Procede a verificar los xml
		String pathxml;
		if(tipo.equals("E")) // es de egresos (Compras. Gastos)
			pathxml = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/" + m_ArchivoLLaveXML;
		else if(tipo.equals("I")) // es de ingresos (Facturas generadas por un pac externo, y no desde el sistema)
			pathxml = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/" + m_ArchivoLLaveXML;
		else if(tipo.equals("N")) // es de nomina (Recibos generados por un pac externo, y no desde el sistema)
			pathxml = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/" + m_ArchivoLLaveXML;
		else // es de ingresos o egresos capturada desde contabilidad electronica
			pathxml = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/" + m_ArchivoLLaveXML;
				
		String ERROR = "", archivoXML = "";
		boolean NAME_SPACE_AWARE = true;
		boolean VALIDATING = true;
		String SCHEMA_LANGUAGE ="http://java.sun.com/xml/jaxp/properties/schemaLanguage";
		String SCHEMA_LANGUAGE_VAL = "http://www.w3.org/2001/XMLSchema";
		String SCHEMA_SOURCE ="http://java.sun.com/xml/jaxp/properties/schemaSource";
		String sFichXsd = "/usr/local/forseti/rec/cfdv32.xsd";
		String sFichXsd2 = "/usr/local/forseti/rec/TimbreFiscalDigital.xsd";
		String sFichXsd3 = "/usr/local/forseti/rec/nomina11.xsd";
		try 
		{
			FileReader file         = new FileReader(pathxml);
            BufferedReader buff     = new BufferedReader(file);
            boolean eof             = false;
            
            while(!eof)
            {
                String line = buff.readLine();
                if(line == null)
                	eof = true;
                else
                	archivoXML += line + "\n";
            }
            InputStream is = new ByteArrayInputStream(archivoXML.getBytes());
            			
			//Reader xmlReader;
			Reader xsdReader;
			Reader xsdReader2;
			Reader xsdReader3;
			 
			//xmlReader = new FileReader(pathxml);
			xsdReader = new FileReader(sFichXsd);
			xsdReader2 = new FileReader(sFichXsd2);
			xsdReader3 = new FileReader(sFichXsd3);
			//System.out.println("xmlReader:" + pathxml + "\n" + "xsdReader:" + sFichXsd + "\n"  + "xsdReader2:" + sFichXsd2);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(NAME_SPACE_AWARE);
			factory.setValidating(VALIDATING);
		  
			SAXParser parser = factory.newSAXParser();
			parser.setProperty(SCHEMA_LANGUAGE, SCHEMA_LANGUAGE_VAL);
			parser.setProperty(SCHEMA_SOURCE, new InputSource(xsdReader));
			parser.setProperty(SCHEMA_SOURCE, new InputSource(xsdReader2));
			if(tipo.equals("N")) 
				parser.setProperty(SCHEMA_SOURCE, new InputSource(xsdReader3)); //manda error al intentar verificar un recibo de nomina si se agrega nomina11.xsd
			
			DefaultHandler handler = new XmlDefaultHandler();
			//parser.parse(new InputSource(xmlReader), handler); 
			parser.parse(is, handler);
		}
		catch(FactoryConfigurationError e) 
		{
			e.printStackTrace();
			ERROR += "FactoryConfiguration: "  + e.getMessage() + "<br>";
		}
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
			ERROR += "ParserConfiguration: "  + e.getMessage() + "<br>"; 
		}
		catch (SAXException e) 
		{
			e.printStackTrace();
			ERROR += "SAX: "  + e.getMessage() + "<br>"; 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR += "IO: "  + e.getMessage() + "<br>";
		}
		
		if(!ERROR.equals(""))
		{
			m_StatusCertComp = JForsetiCFD.ERROR;
			m_Error = ERROR;
			res = 3;
			mensaje.append("ERROR: Al verificar sintaxis del XML del CFDI subido... ¿ Tiene el servidor conexón a internet ?" + "<br>" + ERROR);
		}
		else
		{
			mensaje.append("El XML se verificó y paso las pruebas de sintaxis<br>");
			factxml.setArchivoXML(m_ArchivoLLaveXML);
			factxml.setArchivoPDF(m_ArchivoCertificadoPDF);
			res = 0;
		}
		
		return res;
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean CargarDocumentoCFDI(HttpServletRequest request, JFacturasXML factxml, StringBuffer mensaje, String uuidxml, String tipo) 
			throws ServletException, IOException 
	{
		boolean res = true;
		String ERROR = "";
		
		SAXBuilder builder = new SAXBuilder();
		File xmlFile;
		if(tipo.equals("E")) // es de egresos (Compras. Gastos)
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/TFDs/" + uuidxml + ".xml");
		else if(tipo.equals("I")) // es de ingresos (Facturas generadas por un pac externo, y no desde el sistema)
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/TFDs/" + uuidxml + ".xml");
		else if(tipo.equals("N")) // es de Nomina (Recibo de nomina)
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/TFDs/" + uuidxml + ".xml");
		else // es de contabilidad
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/TFDs/" + uuidxml + ".xml");
	
		try 
		{
			Document document = (Document)builder.build(xmlFile);
			Element Comprobante = document.getRootElement();
			Namespace ns = Comprobante.getNamespace();
			factxml.getComprobante().put("version", Comprobante.getAttributeValue("version"));
			factxml.getComprobante().put("serie", (Comprobante.getAttributeValue("serie") == null ? "fsi-null" : Comprobante.getAttributeValue("serie")));
			factxml.getComprobante().put("folio", (Comprobante.getAttributeValue("folio") == null ? "fsi-null" : Comprobante.getAttributeValue("folio")));
			factxml.getComprobante().put("fecha", Comprobante.getAttributeValue("fecha"));
			factxml.getComprobante().put("sello", Comprobante.getAttributeValue("sello"));
			factxml.getComprobante().put("formaDePago", Comprobante.getAttributeValue("formaDePago"));
			factxml.getComprobante().put("noCertificado", Comprobante.getAttributeValue("noCertificado"));
			factxml.getComprobante().put("certificado", Comprobante.getAttributeValue("certificado"));
			factxml.getComprobante().put("condicionesDePago", (Comprobante.getAttributeValue("condicionesDePago") == null ? "fsi-null" : Comprobante.getAttributeValue("condicionesDePago")));
			factxml.getComprobante().put("subTotal", Comprobante.getAttributeValue("subTotal"));
			factxml.getComprobante().put("descuento", (Comprobante.getAttributeValue("descuento") == null ? "0.00" : Comprobante.getAttributeValue("descuento")));
			factxml.getComprobante().put("TipoCambio", (Comprobante.getAttributeValue("TipoCambio") == null ? "1.00" : Comprobante.getAttributeValue("TipoCambio")));
			factxml.getComprobante().put("Moneda", (Comprobante.getAttributeValue("Moneda") == null ? "1" : Comprobante.getAttributeValue("Moneda")));
			factxml.getComprobante().put("total", Comprobante.getAttributeValue("total"));
			factxml.getComprobante().put("tipoDeComprobante", Comprobante.getAttributeValue("tipoDeComprobante"));
			factxml.getComprobante().put("metodoDePago", Comprobante.getAttributeValue("metodoDePago"));
			factxml.getComprobante().put("LugarExpedicion", Comprobante.getAttributeValue("LugarExpedicion"));
			Element Emisor = Comprobante.getChild("Emisor",ns);
			factxml.setRFC_Emisor(Emisor.getAttributeValue("rfc"));
			factxml.setNombre_Emisor(Emisor.getAttributeValue("nombre") == null ? "Proveedor de Mostrador" : Emisor.getAttributeValue("nombre"));
			Element Receptor = Comprobante.getChild("Receptor",ns);
			factxml.setRFC_Receptor(Receptor.getAttributeValue("rfc"));
			Element Conceptos = Comprobante.getChild("Conceptos",ns);
			List con = Conceptos.getChildren("Concepto", ns);
			for (int i = 0; i < con.size(); i++) 
			{
				Properties concepto = new Properties();
	 		   	Element Concepto = (Element) con.get(i);
	 		   	concepto.put("cantidad", Concepto.getAttributeValue("cantidad"));
	 		   	concepto.put("unidad", Concepto.getAttributeValue("unidad"));
	 		   	concepto.put("noIdentificacion", (Concepto.getAttributeValue("noIdentificacion") == null ? "" : Concepto.getAttributeValue("noIdentificacion"))); //Si es nulo, no tiene la clave del producto
	 		   	concepto.put("descripcion", Concepto.getAttributeValue("descripcion"));
	 		   	concepto.put("valorUnitario", Concepto.getAttributeValue("valorUnitario"));
	 		   	concepto.put("importe", Concepto.getAttributeValue("importe"));
	 		   	factxml.getConceptos().addElement(concepto);
			}
			Element Impuestos = Comprobante.getChild("Impuestos",ns);
			factxml.getImpuestos().put("totalImpuestosRetenidos", (Impuestos.getAttributeValue("totalImpuestosRetenidos") == null ? "0.00" : Impuestos.getAttributeValue("totalImpuestosRetenidos"))); // si no es nulo esta reteniendo impuestos;
			factxml.getImpuestos().put("totalImpuestosTrasladados", (Impuestos.getAttributeValue("totalImpuestosTrasladados") == null ? "0.00" : Impuestos.getAttributeValue("totalImpuestosTrasladados"))); // si es nulo, son productos exentos de IVA como medicinas
	 		Element Traslados = Impuestos.getChild("Traslados", ns);
	 		if(Traslados != null)
	 		{
	 			List tras = Traslados.getChildren("Traslado", ns);
	 			for (int i = 0; i < tras.size(); i++) 
	 			{
	 				Properties traslado = new Properties();
	 				Element Traslado = (Element) tras.get(i);
	 				traslado.put("impuesto", Traslado.getAttributeValue("impuesto"));
	 				traslado.put("tasa", Traslado.getAttributeValue("tasa"));
	 				traslado.put("importe", Traslado.getAttributeValue("importe"));
	 				factxml.getTraslados().addElement(traslado);
	 			}
	 		}
	 		Element Retenciones = Impuestos.getChild("Retenciones", ns);
	 		if(Retenciones != null)
	 		{
	 			List rets = Retenciones.getChildren("Retencion", ns);
	 			for (int i = 0; i < rets.size(); i++) 
	 			{
	 				Properties retencion = new Properties();
	 				Element Retencion = (Element) rets.get(i);
	 				retencion.put("impuesto", Retencion.getAttributeValue("impuesto"));
	 				retencion.put("importe", Retencion.getAttributeValue("importe"));
	 				factxml.getRetenciones().addElement(retencion);
	 			}
	 		}
	 		Element Complemento = Comprobante.getChild("Complemento",ns);
	 		// Ahora incluye la nomina
	 		if(tipo.equals("N"))
	 		{
		 		Namespace nsnomina = Namespace.getNamespace("nomina", "http://www.sat.gob.mx/nomina");
	    		Element Nomina = Complemento.getChild("Nomina", nsnomina);
	    		Element Percepciones = Nomina.getChild("Percepciones", nsnomina);
	    		Element Deducciones = Nomina.getChild("Deducciones", nsnomina);
	    		
	    		factxml.getNomina().put("TotalGravadoPers", (Percepciones.getAttributeValue("TotalGravado") == null ? "0.00" : Percepciones.getAttributeValue("TotalGravado")));
	       		factxml.getNomina().put("TotalExentoPers", (Percepciones.getAttributeValue("TotalExento") == null ? "0.00" : Percepciones.getAttributeValue("TotalExento")));
	    		factxml.getNomina().put("TotalGravadoDeds", (Deducciones.getAttributeValue("TotalGravado") == null ? "0.00" : Deducciones.getAttributeValue("TotalGravado")));
	       		factxml.getNomina().put("TotalExentoDeds", (Deducciones.getAttributeValue("TotalExento") == null ? "0.00" : Deducciones.getAttributeValue("TotalExento")));
	       		if(Percepciones != null)
		 		{
		 			List pers = Percepciones.getChildren("Percepcion", nsnomina);
		 			for (int i = 0; i < pers.size(); i++) 
		 			{
		 				Properties percepcion = new Properties();
		 				Element Percepcion = (Element) pers.get(i);
		 				percepcion.put("TipoPercepcion", Percepcion.getAttributeValue("TipoPercepcion"));
		 				percepcion.put("Clave", Percepcion.getAttributeValue("Clave"));
		 				percepcion.put("Concepto", Percepcion.getAttributeValue("Concepto"));
		 				percepcion.put("ImporteGravado", Percepcion.getAttributeValue("ImporteGravado"));
		 				percepcion.put("ImporteExento", Percepcion.getAttributeValue("ImporteExento"));
		 				factxml.getPercepciones().addElement(percepcion);
		 			}
		 		}
	       		if(Deducciones != null)
		 		{
		 			List deds = Deducciones.getChildren("Deduccion", nsnomina);
		 			for (int i = 0; i < deds.size(); i++) 
		 			{
		 				Properties deduccion = new Properties();
		 				Element Deduccion = (Element) deds.get(i);
		 				deduccion.put("TipoDeduccion", Deduccion.getAttributeValue("TipoDeduccion"));
		 				deduccion.put("Clave", Deduccion.getAttributeValue("Clave"));
		 				deduccion.put("Concepto", Deduccion.getAttributeValue("Concepto"));
		 				deduccion.put("ImporteGravado", Deduccion.getAttributeValue("ImporteGravado"));
		 				deduccion.put("ImporteExento", Deduccion.getAttributeValue("ImporteExento"));
		 				factxml.getDeducciones().addElement(deduccion);
		 			}
		 		}
	 		}
    		Namespace nstfd = Namespace.getNamespace("tfd","http://www.sat.gob.mx/TimbreFiscalDigital");
    		Element tfd = Complemento.getChild("TimbreFiscalDigital", nstfd);
    		factxml.getTFD().put("UUID", tfd.getAttributeValue("UUID")); 
    		factxml.getTFD().put("FechaTimbrado", tfd.getAttributeValue("FechaTimbrado")); 
    		factxml.getTFD().put("selloCFD", tfd.getAttributeValue("selloCFD")); 
    		factxml.getTFD().put("noCertificadoSAT", tfd.getAttributeValue("noCertificadoSAT")); 
    		factxml.getTFD().put("selloSAT", tfd.getAttributeValue("selloSAT")); 
    		
    		
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR += "IO: "  + e.getMessage() + "<br>";
		} 
		catch (JDOMException e) 
		{
			e.printStackTrace();
			ERROR += "JDOM: "  + e.getMessage() + "<br>";
		}
		
		if(!ERROR.equals(""))
		{
			res = false;
			mensaje.append("ERROR Al cargar el archivo XML: " + "<br>" + ERROR);
		}
		else
		{
			res = true;
			mensaje.append("El XML se cargó correctamente<br>");
		}
		
		return res;
	}
		
	public short GuardarDocumentoCFDI(HttpServletRequest request, int ID_Entidad, String archivoxml, String archivopdf, StringBuffer mensaje, String tipo) 
			throws ServletException, IOException 
	{
		short res = -1;
		String ERROR = "";
		
		SAXBuilder builder = new SAXBuilder();
		File xmlFile;
		if(tipo.equals("E"))
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/" + archivoxml);
		else if(tipo.equals("I"))
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/" + archivoxml);
		else if(tipo.equals("N"))
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/" + archivoxml);
		else 
			xmlFile = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/" + archivoxml);
		 
		try 
		{
			Document document = (Document)builder.build(xmlFile);
			Element Comprobante = document.getRootElement();
			Namespace ns = Comprobante.getNamespace();
			Element Emisor = Comprobante.getChild("Emisor",ns);
			Emisor.getAttributeValue("rfc");
			if(tipo.equals("I") || (JUtil.Elm(tipo,1).equals("C") && JUtil.Elm(tipo,2).equals("VENT")))
    		{
				//System.out.println("TIPO: " + tipo + " ELM 1: " + JUtil.Elm(tipo,1) + " ELM 2: " + JUtil.Elm(tipo,2));
				//System.out.println("Paso prueba: " + "if(tipo.equals('I') || (JUtil.Elm(tipo,1).equals('C') && JUtil.Elm(tipo,2).equals('VENT')))");
		 		//Verifica que el RFC del Emisor sea igual al RFC registrado
    			JBDSSet set = new JBDSSet(request);
  		  		set.ConCat(true);
  		  		set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
  		  		set.Open();
  		  		if(!Emisor.getAttributeValue("rfc").equalsIgnoreCase(set.getAbsRow(0).getRFC()))
  		  		{
  		  			//System.out.println("I Rechazado");
  		  			res = 3; 
  		  			mensaje.append("ERROR: El RFC del emisor en el XML no pertenece a la compañia. No se puede guardar el CFDI");
  		  			return res;
  		  		}
    		}
			Element Receptor = Comprobante.getChild("Receptor",ns);
			Element Impuestos = Comprobante.getChild("Impuestos",ns);
			Element Complemento = Comprobante.getChild("Complemento",ns);
			Namespace nstfd = Namespace.getNamespace("tfd","http://www.sat.gob.mx/TimbreFiscalDigital");
    		Element tfd = Complemento.getChild("TimbreFiscalDigital", nstfd);
    		tfd.getAttributeValue("UUID"); 
    		Receptor.getAttributeValue("rfc");
    		if(tipo.equals("E") || (JUtil.Elm(tipo,1).equals("C") && JUtil.Elm(tipo,2).equals("COMP")))
    		{
    			//System.out.println("TIPO: " + tipo + " ELM 1: " + JUtil.Elm(tipo,1) + " ELM 2: " + JUtil.Elm(tipo,2));
				//System.out.println("Paso prueba: " + "if(tipo.equals('E') || (JUtil.Elm(tipo,1).equals('C') && JUtil.Elm(tipo,2).equals('COMP')))");
		 		//Verifica que el RFC del Receptor sea igual al RFC registrado
    			JBDSSet set = new JBDSSet(request);
  		  		set.ConCat(true);
  		  		set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
  		  		set.Open();
  		  		if(!Receptor.getAttributeValue("rfc").equalsIgnoreCase(set.getAbsRow(0).getRFC()))
  		  		{
  		  			//System.out.println("E Rechazado");
		  			res = 3; 
  		  			mensaje.append("ERROR: El RFC del receptor en el XML no pertenece a la compañia. No se puede guardar el CFDI");
  		  			return res;
  		  		}
    		}
    		
			String efecto;
			if(Comprobante.getAttributeValue("tipoDeComprobante").equals("ingreso"))
				efecto = "I";
			else if(Comprobante.getAttributeValue("tipoDeComprobante").equals("egreso"))
				efecto = "E";
			else
			{
				res = 3; 
		  		mensaje.append("ERROR: No se pueden cargar comprobantes de traslados. Se deben cargar solo comprobantes de ingresos o egresos");
		  		return res;
			}
    		//Ahora que se ha cargado la compra, Agrega el cfd, tfd y pdf a la base de datos, y cambia el nobre del xml al 
    		//nombre del UUID para que sea identificado por la compra de forseti
			String str;
			
			if(tipo.equals("E"))
				str = "select * from sp_cfdcomp_agregar('" + JUtil.q(Emisor.getAttributeValue("rfc")) + "','" + JUtil.estFechaCFDI(JUtil.q(Comprobante.getAttributeValue("fecha"))) + "','" + JUtil.q(Comprobante.getAttributeValue("total")) + "','" + (Impuestos.getAttributeValue("totalImpuestosTrasladados") == null ? "0.00" : JUtil.q(Impuestos.getAttributeValue("totalImpuestosTrasladados"))) + "','" + JUtil.q(efecto) + "','','','','ENT','" + ID_Entidad + "','" + JUtil.q(Comprobante.getAttributeValue("noCertificado")) + "','" + JUtil.q(tfd.getAttributeValue("selloCFD")) + "','" + JUtil.q(Comprobante.getAttributeValue("LugarExpedicion")) + "','" + JUtil.q(Comprobante.getAttributeValue("metodoDePago")) + "','" +
					JUtil.q(tfd.getAttributeValue("UUID")) + "','" + JUtil.obtFechaHoraSQLtfd(JUtil.q(tfd.getAttributeValue("FechaTimbrado"))) + "','" + JUtil.q(tfd.getAttributeValue("noCertificadoSAT")) + "','" + JUtil.q(tfd.getAttributeValue("selloSAT")) + "') as (err int, res varchar)";
			else if(tipo.equals("I"))
				str = "select * from sp_cfdven_agregar('" + JUtil.q(Receptor.getAttributeValue("rfc")) + "','" + JUtil.estFechaCFDI(JUtil.q(Comprobante.getAttributeValue("fecha"))) + "','" + JUtil.q(Comprobante.getAttributeValue("total")) + "','" + (Impuestos.getAttributeValue("totalImpuestosTrasladados") == null ? "0.00" : JUtil.q(Impuestos.getAttributeValue("totalImpuestosTrasladados"))) + "','" + JUtil.q(efecto) + "','','','','ENT','" + ID_Entidad + "','" + JUtil.q(Comprobante.getAttributeValue("noCertificado")) + "','" + JUtil.q(tfd.getAttributeValue("selloCFD")) + "','" + JUtil.q(Comprobante.getAttributeValue("LugarExpedicion")) + "','" + JUtil.q(Comprobante.getAttributeValue("metodoDePago")) + "','" +
					JUtil.q(tfd.getAttributeValue("UUID")) + "','" + JUtil.obtFechaHoraSQLtfd(JUtil.q(tfd.getAttributeValue("FechaTimbrado"))) + "','" + JUtil.q(tfd.getAttributeValue("noCertificadoSAT")) + "','" + JUtil.q(tfd.getAttributeValue("selloSAT")) + "') as (err int, res varchar)";
			else if(tipo.equals("N"))
				str = "select * from sp_cfdnom_agregar('" + JUtil.q(Receptor.getAttributeValue("rfc")) + "','" + JUtil.estFechaCFDI(JUtil.q(Comprobante.getAttributeValue("fecha"))) + "','" + JUtil.q(Comprobante.getAttributeValue("total")) + "','" + (Impuestos.getAttributeValue("totalImpuestosTrasladados") == null ? "0.00" : JUtil.q(Impuestos.getAttributeValue("totalImpuestosTrasladados"))) + "','" + JUtil.q(efecto) + "','','','','ENT','" + ID_Entidad + "','" + JUtil.q(Comprobante.getAttributeValue("noCertificado")) + "','" + JUtil.q(tfd.getAttributeValue("selloCFD")) + "','" + JUtil.q(Comprobante.getAttributeValue("LugarExpedicion")) + "','" + JUtil.q(Comprobante.getAttributeValue("metodoDePago")) + "','" +
					JUtil.q(tfd.getAttributeValue("UUID")) + "','" + JUtil.obtFechaHoraSQLtfd(JUtil.q(tfd.getAttributeValue("FechaTimbrado"))) + "','" + JUtil.q(tfd.getAttributeValue("noCertificadoSAT")) + "','" + JUtil.q(tfd.getAttributeValue("selloSAT")) + "') as (err int, res varchar)";
			else
			{
				if(JUtil.Elm(tipo, 2).equals("COMP"))
					str = "select * from sp_cfdcomp_agregar('" + JUtil.q(Emisor.getAttributeValue("rfc")) + "','" + JUtil.estFechaCFDI(JUtil.q(Comprobante.getAttributeValue("fecha"))) + "','" + JUtil.q(Comprobante.getAttributeValue("total")) + "','" + (Impuestos.getAttributeValue("totalImpuestosTrasladados") == null ? "0.00" : JUtil.q(Impuestos.getAttributeValue("totalImpuestosTrasladados"))) + "','" + JUtil.q(efecto) + "','','','','ENT','" + ID_Entidad + "','" + JUtil.q(Comprobante.getAttributeValue("noCertificado")) + "','" + JUtil.q(tfd.getAttributeValue("selloCFD")) + "','" + JUtil.q(Comprobante.getAttributeValue("LugarExpedicion")) + "','" + JUtil.q(Comprobante.getAttributeValue("metodoDePago")) + "','" +
						JUtil.q(tfd.getAttributeValue("UUID")) + "','" + JUtil.obtFechaHoraSQLtfd(JUtil.q(tfd.getAttributeValue("FechaTimbrado"))) + "','" + JUtil.q(tfd.getAttributeValue("noCertificadoSAT")) + "','" + JUtil.q(tfd.getAttributeValue("selloSAT")) + "') as (err int, res varchar)";
				else //if(JUtil.Elm(tipo, 2).equals("VENT"))
					str = "select * from sp_cfdven_agregar('" + JUtil.q(Receptor.getAttributeValue("rfc")) + "','" + JUtil.estFechaCFDI(JUtil.q(Comprobante.getAttributeValue("fecha"))) + "','" + JUtil.q(Comprobante.getAttributeValue("total")) + "','" + (Impuestos.getAttributeValue("totalImpuestosTrasladados") == null ? "0.00" : JUtil.q(Impuestos.getAttributeValue("totalImpuestosTrasladados"))) + "','" + JUtil.q(efecto) + "','','','','ENT','" + ID_Entidad + "','" + JUtil.q(Comprobante.getAttributeValue("noCertificado")) + "','" + JUtil.q(tfd.getAttributeValue("selloCFD")) + "','" + JUtil.q(Comprobante.getAttributeValue("LugarExpedicion")) + "','" + JUtil.q(Comprobante.getAttributeValue("metodoDePago")) + "','" +
						JUtil.q(tfd.getAttributeValue("UUID")) + "','" + JUtil.obtFechaHoraSQLtfd(JUtil.q(tfd.getAttributeValue("FechaTimbrado"))) + "','" + JUtil.q(tfd.getAttributeValue("noCertificadoSAT")) + "','" + JUtil.q(tfd.getAttributeValue("selloSAT")) + "') as (err int, res varchar)";
			}
			//System.out.println(str);
			String mens = "";
			Connection con = JAccesoBD.getConexionSes(request);
			Statement s    = con.createStatement();
			ResultSet rs   = s.executeQuery(str);
			if(rs.next())
			{
				res = rs.getShort("ERR");
				mens = rs.getString("RES");
			}
			s.close();
			JAccesoBD.liberarConexion(con);
				     
			if(res != 0)
				ERROR += " " + mens;
			
			if(tipo.equals("E"))
			{
				File xmlorig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/" + archivoxml);
	    		File xmldest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/TFDs/" + tfd.getAttributeValue("UUID") + ".xml");
	    		xmlorig.renameTo(xmldest);
	    		if(archivopdf != null && !archivopdf.equals(""))
	    		{
	    			File pdforig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/" + archivopdf);
	    			File pdfdest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/comp/PDFs/" + tfd.getAttributeValue("UUID") + ".pdf");
	    			pdforig.renameTo(pdfdest);
	    		}
			}
			else if(tipo.equals("I"))
			{
				File xmlorig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/" + archivoxml);
	    		File xmldest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/TFDs/" + tfd.getAttributeValue("UUID") + ".xml");
	    		xmlorig.renameTo(xmldest);
	    		if(archivopdf != null && !archivopdf.equals(""))
	    		{
	    			File pdforig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/" + archivopdf);
	    			File pdfdest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/ven/PDFs/" + tfd.getAttributeValue("UUID") + ".pdf");
	    			pdforig.renameTo(pdfdest);
	    		}
			}
			else if(tipo.equals("N"))
			{
				File xmlorig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/" + archivoxml);
	    		File xmldest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/TFDs/" + tfd.getAttributeValue("UUID") + ".xml");
	    		xmlorig.renameTo(xmldest);
	    		if(archivopdf != null && !archivopdf.equals(""))
	    		{
	    			File pdforig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/" + archivopdf);
	    			File pdfdest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/nom/PDFs/" + tfd.getAttributeValue("UUID") + ".pdf");
	    			pdforig.renameTo(pdfdest);
	    		}
			}
			else
			{
				File xmlorig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/" + archivoxml);
	    		File xmldest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/TFDs/" + tfd.getAttributeValue("UUID") + ".xml");
	    		xmlorig.renameTo(xmldest);
	    		if(archivopdf != null && !archivopdf.equals(""))
	    		{
	    			File pdforig = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/" + archivopdf);
	    			File pdfdest = new File("/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/cont/PDFs/" + tfd.getAttributeValue("UUID") + ".pdf");
	    			pdforig.renameTo(pdfdest);
	    		}
			}
		}
		catch(SQLException e)
	    {
			e.printStackTrace();
	    	ERROR += "SQL: " + e.getMessage() + "<br>";
	    }
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR += "IO: "  + e.getMessage() + "<br>";
		} 
		catch (JDOMException e) 
		{
			e.printStackTrace();
			ERROR += "JDOM: "  + e.getMessage() + "<br>";
		}
		
		if(!ERROR.equals(""))
		{
			m_StatusCertComp = JForsetiCFD.ERROR;
			m_Error = ERROR;
			res = 3;
			mensaje.append("ERROR Al guardar el archivo XML y/o PDF: " + "<br>" + ERROR);
		}
		else
		{
			m_StatusCertComp = JForsetiCFD.OKYDOKY;
			m_Error = "";
			res = 0;
			mensaje.append("El documento XML y PDF se guardó con exito en la base de datos para poder enlazar<br>");
		}
		
		return res;
	}
}
