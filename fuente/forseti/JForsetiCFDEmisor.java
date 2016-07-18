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

import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.io.StringBufferInputStream;
//import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
//import java.util.zip.Inflater;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;
import org.xhtmlrenderer.pdf.ITextRenderer;

import forseti.sets.JAdmVariablesSet;
import forseti.sets.JBDSSet;
import forseti.sets.JFormatosDetSet;
import forseti.sets.JSetDinamico;

//import org.w3c.tidy.Tidy;



public abstract class JForsetiCFDEmisor extends JForsetiCFD 
{
	protected boolean m_PacTest;
	protected String m_PacServ;
	protected String m_PacURL;
	protected String m_PacUsr;
	protected String m_PacPass;
	protected Document m_XMLDoc;
	protected Element m_Comprobante;
	protected Namespace m_ns;
	protected Namespace m_nsnomina;
	protected Namespace m_nscce;
	protected String m_CadenaOriginal;
	protected String m_CadenaOriginalNom;
	protected String m_CadenaOriginalComExt;
	protected String [] m_LineaRep;
	
	public static final byte CFD_NO_EMISOR = 0;
	public static final byte CFD_EMISOR = 1;
	
	// Datos de CFD Emisor Global.
	protected int m_CFD;
	protected String m_CFD_Nombre;
	protected String m_CFD_RFC;
	protected String m_CFD_Calle;
	protected String m_CFD_NoExt;
	protected String m_CFD_NoInt;
	protected String m_CFD_Colonia;
	protected String m_CFD_Localidad;
	protected String m_CFD_Municipio;
	protected String m_CFD_Estado;
	protected String m_CFD_Pais;
	protected String m_CFD_CP;
	protected String m_CFD_Regimen;
	
	protected JForsetiCFDEmisor()
	{
		m_PacTest = true; //protege el timbrado a test por si no existe en la configuracion del pac
	}
	
	protected boolean cargarInfoPac(HttpServletRequest request) 
			throws ServletException
	{
		JAdmVariablesSet prb = new JAdmVariablesSet(request);
		prb.ConCat(true);
		prb.m_Where = "ID_Variable = 'SRV-PRUEBA'";
		prb.Open();
		if(prb.getAbsRow(0).getVEntero() == 1) // Es servidor de prueba, por lo tanto no puede sellar
		{
			m_StatusCFD = ERROR;
    		m_Error = "ERROR: Este es un Servidor de pruebas... No está disponible la generación de CFDIs";
    		return false;
		}
		
		try
		{
			// Revisa las variables del pac
			JAdmVariablesSet var = new JAdmVariablesSet(request);
			var.m_Where = "ID_Variable = 'PACTEST'";
			var.Open();
			
			if(var.getAbsRow(0).getVEntero() == 1) //establece si es prueba o produccion
				m_PacTest = true;
			else
				m_PacTest = false;
			
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
							m_PacServ = value;
						if(key.equals("PURL"))
							m_PacURL = value;
						else if(key.equals("PASS"))
							m_PacPass = value;
						else if(key.equals("USER"))
							m_PacUsr = value;
												
					}
					catch(NoSuchElementException e)
					{
						m_StatusCFD = ERROR;
						m_Error = "La informacion del pac de proveedor parece estar mal configurada";
						return false;
					}
				}
				
			}
			buff.close();
			
		}
		catch (FileNotFoundException e1)
		{
			m_StatusCFD = ERROR;
    		m_Error = "Error de archivos CFDI: " + e1.getMessage();
    		return false;
		}
		catch (IOException e1) 
		{
			m_StatusCFD = ERROR;
    		m_Error = "Error de Entrada/Salida CFDI: " + e1.getMessage();
    		return false;
		}
		
		return true;
	}
	protected void comprobanteNuevo(String [] complementos)
	{
		m_Comprobante = new Element("Comprobante", "cfdi", "http://www.sat.gob.mx/cfd/3");
		m_ns = m_Comprobante.getNamespace();
		
		Namespace xsi = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
		m_Comprobante.addNamespaceDeclaration(xsi);
		String schemaLocation = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd";
		
		for(int i = 0; i < complementos.length; i++)
		{
			if(complementos[i].equals("Nomina"))
			{
				schemaLocation += " http://www.sat.gob.mx/nomina http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina11.xsd";
				m_nsnomina = Namespace.getNamespace("nomina","http://www.sat.gob.mx/nomina");
				m_Comprobante.addNamespaceDeclaration(m_nsnomina);
			}
			else if(complementos[i].equals("ComercioExterior"))
			{
				schemaLocation += " http://www.sat.gob.mx/ComercioExterior http://www.sat.gob.mx/sitio_internet/cfd/ComercioExterior/ComercioExterior10.xsd";
				m_nscce = Namespace.getNamespace("cce","http://www.sat.gob.mx/ComercioExterior");
				m_Comprobante.addNamespaceDeclaration(m_nscce);
			}
		}
		
		m_Comprobante.setAttribute("schemaLocation", schemaLocation, xsi);
		
		m_Comprobante.setAttribute("version","3.2");
		m_XMLDoc = new org.jdom.Document(m_Comprobante);
		m_CadenaOriginal = "||3.2";
		m_CadenaOriginalNom = "|1.1";
		m_CadenaOriginalComExt = "|1.0";
		m_LineaRep = new String [20];
		
	}
	
	public short getStatusCFD()
	{
		return m_StatusCFD;
	}
	
	protected void ObtenInfoEmisor(HttpServletRequest request) 
	 	throws ServletException, IOException
	{
		JBDSSet set = new JBDSSet(request);
		set.ConCat(true);
	    set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
	    set.Open();
	    System.out.println("INFO EMISOR RFC: " + set.getAbsRow(0).getRFC());
	    m_CFD = set.getAbsRow(0).getCFD();
	    m_CFD_Nombre = set.getAbsRow(0).getCompania();
	    m_CFD_RFC = set.getAbsRow(0).getRFC();
		m_CFD_Calle = set.getAbsRow(0).getCFD_Calle();
		m_CFD_NoExt = set.getAbsRow(0).getCFD_NoExt();
		m_CFD_NoInt = set.getAbsRow(0).getCFD_NoInt();
		m_CFD_Colonia = set.getAbsRow(0).getCFD_Colonia();
		m_CFD_Localidad = set.getAbsRow(0).getCFD_Localidad();
		m_CFD_Municipio = set.getAbsRow(0).getCFD_Municipio();
		m_CFD_Estado = set.getAbsRow(0).getCFD_Estado();
		m_CFD_Pais = set.getAbsRow(0).getCFD_Pais();
		m_CFD_CP = set.getAbsRow(0).getCFD_CP();
		m_CFD_Regimen = set.getAbsRow(0).getCFD_RegimenFiscal();
	}
	
	// Estos son los métodos para capturar los distintos atributos y elementos del CFD
	protected boolean generarElementoComprobante(String tipo, String serie, Integer folio, Calendar fecha, Integer noAprobacion, Integer anoAprobacion,
										String tipoDeComprobante, String formaDePago, String condicionesDePago, Float subtotal,
										Float descuento, Float TipoCambio, String Moneda, Float total, String noCertificado, String certificado, String metodoDePago, String LugarExpedicion)
	{
		if(serie != null && !JUtil.fco(serie).equals(""))
		{
			m_Comprobante.setAttribute("serie",JUtil.fco(serie));
			//m_CadenaOriginal += "|" + JUtil.fco(serie);
			m_LineaRep[1] = JUtil.fco(serie);
		}
		else
			m_LineaRep[1] = "";
		
		/*if(folio > 0)
		{*/
			m_Comprobante.setAttribute("folio",folio.toString());
			//m_CadenaOriginal += "|" + folio.toString();
			m_LineaRep[2] = folio.toString();
		/*}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El folio del comprobante parece ser nulo";
			return false;
		}*/
		m_Comprobante.setAttribute("fecha",JUtil.obtFechaTxt(fecha, "yyyy-MM-dd") + "T" + JUtil.obtFechaTxt(fecha, "HH:mm:ss"));
		m_CadenaOriginal += "|" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd") + "T" + JUtil.obtFechaTxt(fecha, "HH:mm:ss");
		m_LineaRep[5] = JUtil.obtFechaHoraSQL(fecha);
		
		/* Descontinuadas en CFDI */
		/*if(noAprobacion > 0)
		{*/
			//m_Comprobante.setAttribute("noAprobacion",noAprobacion.toString());
			//m_CadenaOriginal += "|" + noAprobacion.toString();
			m_LineaRep[3] = noAprobacion.toString();
		/*}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El No. de Aprobaci&oacute;n del comprobante parece ser nulo";
			return false;
		}
		if(anoAprobacion > 1999)
		{*/
			//m_Comprobante.setAttribute("anoAprobacion",anoAprobacion.toString());
			//m_CadenaOriginal += "|" + anoAprobacion.toString();
			m_LineaRep[4] = anoAprobacion.toString();
		/*}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El Año de Aprobaci&oacute;n del comprobante parece ser nulo";
			return false;
		}*/
		/* Fin Descontinuadas */
		m_Comprobante.setAttribute("tipoDeComprobante",tipoDeComprobante);
		m_CadenaOriginal += "|" + tipoDeComprobante;
		m_Comprobante.setAttribute("formaDePago",formaDePago);
		m_CadenaOriginal += "|" + formaDePago;
		if(condicionesDePago != null && !condicionesDePago.equals(""))
		{
			m_Comprobante.setAttribute("condicionesDePago",condicionesDePago);
			m_CadenaOriginal += "|" + condicionesDePago;
		}
		m_Comprobante.setAttribute("subTotal",c6d(subtotal));
		m_CadenaOriginal += "|" + c6d(subtotal);
		if(descuento != null)
		{
			m_Comprobante.setAttribute("descuento",c6d(descuento));
			m_CadenaOriginal += "|" + c6d(descuento);
			// motivoDescuento solo lo maneja en nóminas, no forma parte de la cadena original
			if(tipo.equals("NOM"))
				m_Comprobante.setAttribute("motivoDescuento","Deducciones nómina");
		}
		if(TipoCambio != null)
		{
			m_Comprobante.setAttribute("TipoCambio",c6d(TipoCambio));
			m_CadenaOriginal += "|" + c6d(TipoCambio);
		}
		if(Moneda != null && !Moneda.equals(""))
		{
			m_Comprobante.setAttribute("Moneda",Moneda);
			m_CadenaOriginal += "|" + Moneda;
		}
		m_Comprobante.setAttribute("total",c6d(total));
		m_CadenaOriginal += "|" + c6d(total);
		m_LineaRep[6] = c6d(total);
		// estos atributos no forman parte de la cadena original, solo del xml
		m_Comprobante.setAttribute("noCertificado",noCertificado);
		m_LineaRep[15] = noCertificado;
		if(certificado != null && !certificado.equals(""))
			m_Comprobante.setAttribute("certificado",certificado);
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El certificado parece ser nulo";
			return false;
		}
		if(metodoDePago != null && !metodoDePago.equals(""))
		{
			m_Comprobante.setAttribute("metodoDePago",metodoDePago);
			m_CadenaOriginal += "|" + metodoDePago;
			m_LineaRep[19] = metodoDePago;
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El metodo de pago parece ser nulo";
			return false;
		}
		if(LugarExpedicion != null && !LugarExpedicion.equals(""))
		{
			m_Comprobante.setAttribute("LugarExpedicion",LugarExpedicion);
			m_CadenaOriginal += "|" + LugarExpedicion;
			m_LineaRep[18] = LugarExpedicion;
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo LugarExpedicion del Comprobante parece ser nulo";
			return false;
		}
		
		return true;
	}
	
	protected boolean generarElementoEmisor(String rfc, String nombre, String calle, String noExt, String noInt, String colonia,
			String localidad, String municipio, String estado, String pais, String cp)
	{
		Element Emisor = new Element("Emisor",m_ns);
		String rfcfmt = JUtil.fco(JUtil.frfc(rfc));
		if(rfc != null && !rfcfmt.equals("") && (rfcfmt.length() == 13 || rfcfmt.length() == 12))
		{
			Emisor.setAttribute("rfc", rfcfmt); 
			m_CadenaOriginal += "|" + rfcfmt;
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido RFC " + rfcfmt + " del Emisor parece ser nulo o no contiene entre 12 y 13 caracteres";
			return false;
		}
		if(nombre != null && !JUtil.fco(nombre).equals(""))
		{
			Emisor.setAttribute("nombre", JUtil.fco(nombre)); 
			m_CadenaOriginal += "|" + JUtil.fco(nombre);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Nombre del Emisor parece ser nulo";
			return false;
		}
		Element DomicilioFiscal = new Element("DomicilioFiscal",m_ns);
		if(calle != null && !JUtil.fco(calle).equals(""))
		{
			DomicilioFiscal.setAttribute("calle", JUtil.fco(calle)); 
			m_CadenaOriginal += "|" + JUtil.fco(calle);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Calle del Domicilio Fiscal del Emisor parece ser nulo";
			return false;
		}
		if(noExt != null && !JUtil.fco(noExt).equals(""))
		{
			DomicilioFiscal.setAttribute("noExterior", JUtil.fco(noExt)); 
			m_CadenaOriginal += "|" + JUtil.fco(noExt);
		}
		if(noInt != null && !JUtil.fco(noInt).equals(""))
		{
			DomicilioFiscal.setAttribute("noInterior", JUtil.fco(noInt)); 
			m_CadenaOriginal += "|" + JUtil.fco(noInt);
		}
		if(colonia != null && !JUtil.fco(colonia).equals(""))
		{
			DomicilioFiscal.setAttribute("colonia", JUtil.fco(colonia)); 
			m_CadenaOriginal += "|" + JUtil.fco(colonia);
		}
		if(localidad != null && !JUtil.fco(localidad).equals(""))
		{
			DomicilioFiscal.setAttribute("localidad", JUtil.fco(localidad)); 
			m_CadenaOriginal += "|" + JUtil.fco(localidad);
		}
		// referencia no lo maneja forseti CFD
		if(municipio != null && !JUtil.fco(municipio).equals(""))
		{
			DomicilioFiscal.setAttribute("municipio", JUtil.fco(municipio)); 
			m_CadenaOriginal += "|" + JUtil.fco(municipio);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Municipio del Domicilio Fiscal del Emisor parece ser nulo";
			return false;
		}
		if(estado != null && !JUtil.fco(estado).equals(""))
		{
			DomicilioFiscal.setAttribute("estado", JUtil.fco(estado)); 
			m_CadenaOriginal += "|" + JUtil.fco(estado);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Estado del Domicilio Fiscal del Emisor parece ser nulo";
			return false;
		}
		if(pais != null && !JUtil.fco(pais).equals(""))
		{
			DomicilioFiscal.setAttribute("pais", JUtil.fco(pais)); 
			m_CadenaOriginal += "|" + JUtil.fco(pais);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Pais del Domicilio Fiscal del Emisor parece ser nulo";
			return false;
		}
		if(cp != null && !JUtil.fco(cp).equals(""))
		{
			DomicilioFiscal.setAttribute("codigoPostal", JUtil.fco(cp)); 
			m_CadenaOriginal += "|" + JUtil.fco(cp);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Codigo Postal del Domicilio Fiscal del Emisor parece ser nulo";
			return false;
		}
		Emisor.addContent(DomicilioFiscal);
		m_Comprobante.addContent(Emisor);
		return true;
	}
	
	protected boolean generarElementoExpedidoEn(String calle, String noExt, String noInt, String colonia,
			String localidad, String municipio, String estado, String pais, String cp)
	{
		Element ExpedidoEn = new Element("ExpedidoEn",m_ns);
		if(calle != null && !JUtil.fco(calle).equals(""))
		{
			ExpedidoEn.setAttribute("calle", JUtil.fco(calle)); 
			m_CadenaOriginal += "|" + JUtil.fco(calle);
		}
		if(noExt != null && !JUtil.fco(noExt).equals(""))
		{
			ExpedidoEn.setAttribute("noExterior", JUtil.fco(noExt)); 
			m_CadenaOriginal += "|" + JUtil.fco(noExt);
		}
		if(noInt != null && !JUtil.fco(noInt).equals(""))
		{
			ExpedidoEn.setAttribute("noInterior", JUtil.fco(noInt)); 
			m_CadenaOriginal += "|" + JUtil.fco(noInt);
		}
		if(colonia != null && !JUtil.fco(colonia).equals(""))
		{
			ExpedidoEn.setAttribute("colonia", JUtil.fco(colonia)); 
			m_CadenaOriginal += "|" + JUtil.fco(colonia);
		}
		if(localidad != null && !JUtil.fco(localidad).equals(""))
		{
			ExpedidoEn.setAttribute("localidad", JUtil.fco(localidad)); 
			m_CadenaOriginal += "|" + JUtil.fco(localidad);
		}
		// referencia no lo maneja forseti CFD
		if(municipio != null && !JUtil.fco(municipio).equals(""))
		{
			ExpedidoEn.setAttribute("municipio", JUtil.fco(municipio)); 
			m_CadenaOriginal += "|" + JUtil.fco(municipio);
		}
		if(estado != null && !JUtil.fco(estado).equals(""))
		{
			ExpedidoEn.setAttribute("estado", JUtil.fco(estado)); 
			m_CadenaOriginal += "|" + JUtil.fco(estado);
		}
		if(pais != null && !JUtil.fco(pais).equals(""))
		{
			ExpedidoEn.setAttribute("pais", JUtil.fco(pais)); 
			m_CadenaOriginal += "|" + JUtil.fco(pais);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Pais del Domicilio de Expedici&oacute;n del Emisor parece ser nulo";
			return false;
		}
		if(cp != null && !JUtil.fco(cp).equals(""))
		{
			ExpedidoEn.setAttribute("codigoPostal", JUtil.fco(cp)); 
			m_CadenaOriginal += "|" + JUtil.fco(cp);
		}
		Element Emisor = m_Comprobante.getChild("Emisor",m_ns);
		Emisor.addContent(ExpedidoEn);
		return true;
	}

	protected boolean generarElementoRegimenFiscal(Element Emisor, String regimen)
	{
		Element RegimenFiscal = new Element("RegimenFiscal",m_ns);
		if(regimen != null && !JUtil.fco(regimen).equals(""))
		{
			RegimenFiscal.setAttribute("Regimen", JUtil.fco(regimen)); 
			m_CadenaOriginal += "|" + JUtil.fco(regimen);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Régimen del Régimen Fiscal del Emisor parece ser nulo";
			return false;
		}
		Emisor.addContent(RegimenFiscal);
		return true;
	}

	protected boolean generarElementoReceptor(String rfc, String nombre, String calle, String noExt, String noInt, String colonia,
			String localidad, String municipio, String estado, String pais, String cp)
	{
		Element Receptor = new Element("Receptor",m_ns);
		String rfcfmt = JUtil.fco(JUtil.frfc(rfc));
		if(rfc != null && !rfcfmt.equals("") && (rfcfmt.length() == 13 || rfcfmt.length() == 12))
		{
			Receptor.setAttribute("rfc", rfcfmt); 
			m_CadenaOriginal += "|" + rfcfmt;
			m_LineaRep[0] = rfcfmt;
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido RFC " + rfcfmt + " del Receptor parece ser nulo o no contiene entre 12 y 13 caracteres";
			return false;
		}
		if(nombre != null && !JUtil.fco(nombre).equals(""))
		{
			Receptor.setAttribute("nombre", JUtil.fco(nombre)); 
			m_CadenaOriginal += "|" + JUtil.fco(nombre);
		}
		Element Domicilio = new Element("Domicilio",m_ns);
		if(calle != null && !JUtil.fco(calle).equals(""))
		{
			Domicilio.setAttribute("calle", JUtil.fco(calle)); 
			m_CadenaOriginal += "|" + JUtil.fco(calle);
		}
		if(noExt != null && !JUtil.fco(noExt).equals(""))
		{
			Domicilio.setAttribute("noExterior", JUtil.fco(noExt)); 
			m_CadenaOriginal += "|" + JUtil.fco(noExt);
		}
		if(noInt != null && !JUtil.fco(noInt).equals(""))
		{
			Domicilio.setAttribute("noInterior", JUtil.fco(noInt)); 
			m_CadenaOriginal += "|" + JUtil.fco(noInt);
		}
		if(colonia != null && !JUtil.fco(colonia).equals(""))
		{
			Domicilio.setAttribute("colonia", JUtil.fco(colonia)); 
			m_CadenaOriginal += "|" + JUtil.fco(colonia);
		}
		if(localidad != null && !JUtil.fco(localidad).equals(""))
		{
			Domicilio.setAttribute("localidad", JUtil.fco(localidad)); 
			m_CadenaOriginal += "|" + JUtil.fco(localidad);
		}
		// referencia no lo maneja forseti CFD
		if(municipio != null && !JUtil.fco(municipio).equals(""))
		{
			Domicilio.setAttribute("municipio", JUtil.fco(municipio)); 
			m_CadenaOriginal += "|" + JUtil.fco(municipio);
		}
		if(estado != null && !JUtil.fco(estado).equals(""))
		{
			Domicilio.setAttribute("estado", JUtil.fco(estado)); 
			m_CadenaOriginal += "|" + JUtil.fco(estado);
		}
		if(pais != null && !JUtil.fco(pais).equals(""))
		{
			Domicilio.setAttribute("pais", JUtil.fco(pais)); 
			m_CadenaOriginal += "|" + JUtil.fco(pais);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Pais del Domicilio Fiscal del Receptor parece ser nulo";
			return false;
		}
		if(cp != null && !JUtil.fco(cp).equals(""))
		{
			Domicilio.setAttribute("codigoPostal", JUtil.fco(cp)); 
			m_CadenaOriginal += "|" + JUtil.fco(cp);
		}
		Receptor.addContent(Domicilio);
		m_Comprobante.addContent(Receptor);
		return true;
	}
	
	protected boolean generarElementoConcepto(Element Conceptos, Float cantidad, String unidad, String noIdentificacion, 
			String descripcion, Float valorUnitario, Float importe)
	{
		Element Concepto = new Element("Concepto", m_ns);
		
		if(cantidad != null && cantidad >= 0.0)
		{
			Concepto.setAttribute("cantidad",c6d(cantidad));
			m_CadenaOriginal += "|" + c6d(cantidad);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Cantidad de uno de los Conceptos parece ser nulo o menor a cero";
			return false;
		}
		
		if(unidad != null && !JUtil.fco(unidad).equals(""))
		{
			Concepto.setAttribute("unidad",JUtil.fco(unidad));
			m_CadenaOriginal += "|" + JUtil.fco(unidad);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Unidad de uno de los Conceptos parece ser nulo";
			return false;
		}
		if(noIdentificacion != null && !JUtil.fco(noIdentificacion).equals(""))
		{
			Concepto.setAttribute("noIdentificacion",JUtil.fco(noIdentificacion));
			m_CadenaOriginal += "|" + JUtil.fco(noIdentificacion);
		}
		if(descripcion != null && !JUtil.fco(descripcion).equals(""))
		{
			Concepto.setAttribute("descripcion",JUtil.fco(descripcion));
			m_CadenaOriginal += "|" + JUtil.fco(descripcion);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Descripcion de uno de los Conceptos parece ser nulo";
			return false;
		}
		if(valorUnitario != null && valorUnitario >= 0.0)
		{
			Concepto.setAttribute("valorUnitario",c6d(valorUnitario));
			m_CadenaOriginal += "|" + c6d(valorUnitario);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Valor Untitario de uno de los Conceptos parece ser nulo o menor a cero";
			return false;
		}
		if(importe != null && importe >= 0.0)
		{
			Concepto.setAttribute("importe",c6d(importe));
			m_CadenaOriginal += "|" + c6d(importe);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Importe de uno de los Conceptos parece ser nulo o menor a cero";
			return false;
		}
		// Aqui van la informacion aduanera, Cuenta Predial, Complemento Concepto, Parte que no maneja forseti
		Conceptos.addContent(Concepto);
		return true;
	}
	
	protected boolean generarElementoTraslado(Element Traslados, String impuesto, Float tasa, Float importe)
	{
		Element Traslado = new Element("Traslado", m_ns);
		Traslado.setAttribute("impuesto", impuesto); // lo maneja la subclase por eso no verifica, supone que esta bien
		m_CadenaOriginal += "|" + impuesto;
		if(tasa != null && tasa >= 0.0)
		{
			Traslado.setAttribute("tasa",c6d(tasa));
			m_CadenaOriginal += "|" + c6d(tasa);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Tasa del elemento Traslado de los Impuestos parece ser nulo o menor a cero";
			return false;
		}
		if(importe != null && importe != 0.0)
		{
			Traslado.setAttribute("importe",c6d(importe));
			m_CadenaOriginal += "|" + c6d(importe);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Importe del elemento Traslado de los Impuestos parece ser nulo o menor a cero";
			return false;
		}
		Traslados.addContent(Traslado);
		return true;
	}
	
	protected boolean generarElementoRetencion(Element Retenciones, String impuesto, Float importe)
	{
		Element Retencion = new Element("Retencion", m_ns);
		Retencion.setAttribute("impuesto", impuesto); // lo maneja la subclase por eso no verifica, supone que esta bien
		m_CadenaOriginal += "|" + impuesto;
		if(importe != null && importe != 0.0)
		{
			Retencion.setAttribute("importe",c6d(importe));
			m_CadenaOriginal += "|" + c6d(importe);
		}
		else
		{
			m_StatusCFD = ERROR;
			m_Error = "El atributo requerido Importe del elemento Retencion de los Impuestos parece ser nulo o menor a cero";
			return false;
		}
		Retenciones.addContent(Retencion);
		return true;
	}
	
	protected boolean VerificarDatosDeEmisor(HttpServletRequest request, String ArchivoCertificado, String ArchivoLLave,
			String NoCertificado, Date NotAfter, int Folio, int FolioINI, int FolioFIN, String tipo, Integer id) 
		throws ServletException, IOException
	{
		m_StatusCFD = OKYDOKY;
		ObtenInfoEmisor(request);
		
		if(m_CFD == CFD_NO_EMISOR)
		{
			m_StatusCFD = ERROR;
			m_Error = "Esta empresa no se ha establecido como emisor de CFD";
			return false;
		}
		
		// como si aplica CFD, Comprueba primero el certificado
		CargarCertificadosComprobandolos(request, ArchivoCertificado, ArchivoLLave, NoCertificado, NotAfter, tipo, id); 
		if(m_StatusCertComp == ERROR)
		{
			m_StatusCFD = ERROR;
			return false;
		}
		// Ahora busca y verifica los folios
		/*
		VerificarFolios(Folio, FolioINI, FolioFIN);
		if(m_StatusCFD == ERROR)
			return false;
		*/
		return true;
	}
	
	/*
	private void VerificarFolios(int Folio, int FolioINI, int FolioFIN)
	{
		if(Folio <= 0 || Folio < FolioINI || Folio > FolioFIN )
		{
			m_StatusCFD = ERROR;
			m_Error = "La informaci&oacute;n de folios ya caduc&oacute; o esta mal configurada";
		}
	}
	
	public String getLineaReporteMensual()
	{
		String res = "|";
		for(int i = 0; i < m_LineaRep.length; i++)
		{
			res += m_LineaRep[i] + "|";
		}
		return res;
	}
	*/
	
	public String getCadenaOriginalUTF8()
	{
		try 
		{
			return new String(m_CadenaOriginal.getBytes("UTF8"), "UTF8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
		
	}
	
	public String getCadenaOriginalNomUTF8()
	{
		try 
		{
			return new String(m_CadenaOriginalNom.getBytes("UTF8"), "UTF8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
		
	}
	
	public String getCadenaOriginalComExtUTF8()
	{
		try 
		{
			return new String(m_CadenaOriginalComExt.getBytes("UTF8"), "UTF8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
		
	}
	
	protected String getCadenaXML(Document doc)
	{
		Format format = Format.getPrettyFormat();
		format.setEncoding("utf-8");
		format.setTextMode(TextMode.NORMALIZE);
		XMLOutputter xmlOutputer = new XMLOutputter(format);
		
		return xmlOutputer.outputString(doc);
		
	}
	
	public void generarPDF(HttpServletRequest request, HttpServletResponse response, String tipo, Integer id, String esp1, String SQLCab, String SQLDet, String ID_Formato) 
		throws ServletException, IOException 
	{
		//response.setContentType("text/html");
	    //PrintWriter out = response.getWriter();
	      
		m_StatusCFD = NULO;
			
		String nombreArchivos;
		if(tipo.equals("NOM"))
			nombreArchivos = tipo + "-" + id.toString() + "-" + esp1;
		else
			nombreArchivos = tipo + "-" + id.toString();
		
		String ERROR = "";
		
		try 
		{
			String pathname = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/TFDs/";
			String pdfpath = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/PDFs/";
			/* Carga el xml timbrado
			/*SAXBuilder se encarga de cargar el archivo XML del disco o de un String */
			SAXBuilder builder = new SAXBuilder();
			/*direccion del archivo XML*/
	    	String xml_path = pathname + "SIGN_" + nombreArchivos + ".xml";
	    	/*Cargamos el documento*/
	    	Document XMLTFD = builder.build(xml_path);
	    		    	
			/*Sacamos el elemento raiz*/
	    	Element Comprobante = XMLTFD.getRootElement();
	    	float total = Float.parseFloat(Comprobante.getAttributeValue("total"));
	    	Element Emisor = Comprobante.getChild("Emisor", m_ns);
	    	Element Receptor = Comprobante.getChild("Receptor",m_ns);
	    	Element Complemento = Comprobante.getChild("Complemento", m_ns);
	    	Namespace nstfd = Namespace.getNamespace("tfd","http://www.sat.gob.mx/TimbreFiscalDigital");
	    	Element tfd = Complemento.getChild("TimbreFiscalDigital", nstfd);
					
			//Ahora generamos el codigo de barras bidimensional
			String qrcode = "?re=" + Emisor.getAttributeValue("rfc") + "&rr=" + Receptor.getAttributeValue("rfc") + 
				"&tt=" + c6d(total) + "&id=" + tfd.getAttributeValue("UUID");
			//System.out.println("qrcode: " + qrcode);
			
			// Creamos la imagen QRCode
	    	BufferedImage generatedImage;
	    	QRCodeEncoder encoder = new QRCodeEncoder();
	        // Necesitamos ver las especificaciones del codigo
	    	encoder.setQrcodeVersion(7);
	        encoder.setQrCodeScale(4);
	        encoder.setQrcodeErrorCorrect(QRCodeConstantes.ErrorCorrectM);
	        encoder.setQrcodeEncodeMode(QRCodeConstantes.EncodeModeByte);
	        
	        generatedImage = encoder.Encode(qrcode);
	        
	        File outputFileImage = new File(pdfpath + nombreArchivos + ".png");
	        if(!outputFileImage.exists())
	        {
	           outputFileImage.createNewFile(); // si no existe el fichero lo creamos
	        }
	            
	        //Generamos el archivo de imagen en función de su extensión
	        ImageIO.write(generatedImage, "png", outputFileImage); //el más eficiente para QRCode
	        
			//JFsiQRCode.encode(qrcode, "png", (pdfpath + nombreArchivos + ".png"));
	       	/////////////////////////////////////////////////////////////////////////////////////////
	        // AQUI INICIA EL PROCESO LARGO
			StringBuffer buf = new StringBuffer();
			// INICIA PROCESO DE CAPTURA DE HTML DEL FORMATO
			JFormatosDetSet fs = new JFormatosDetSet(request);
			fs.m_Where = "ID_Formato = '" + JUtil.p(ID_Formato) + "' and Formato = 'S'";
			fs.m_OrderBy = "ID_Part ASC";
			fs.Open();
				
			JFormatosDetSet fc = new JFormatosDetSet(request);
			fc.m_Where = "ID_Formato = '" + JUtil.p(ID_Formato) + "' and Formato = 'C'";
			fc.m_OrderBy = "ID_Part ASC";
			fc.Open();
				
			JFormatosDetSet fd = new JFormatosDetSet(request);
			fd.m_Where = "ID_Formato = '" + JUtil.p(ID_Formato) + "' and Formato = 'D'";
			fd.m_OrderBy = "ID_Part ASC";
			fd.Open();

			JFormatosDetSet ff = new JFormatosDetSet(request);
			ff.m_Where = "ID_Formato = '" + JUtil.p(ID_Formato) + "' and Formato = 'F'";
			ff.m_OrderBy = "ID_Part ASC";
			ff.Open();

			String fsi_imptit = "", fsi_impetq = "", fsi_impcab = "", fsi_impdet = "";
			float fsi_cab_Alt = 0, fsi_det_Y = 0, fsi_det_Alt = 0, fsi_det_lin = 0;
			//int fsi_ventana_Alt = 0, fsi_ventana_Anc = 0;
				
		    for(int s = 0; s < fs.getNumRows(); s++)
			{
				if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPTIT"))
					fsi_imptit = fs.getAbsRow(s).getValor();
				else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPETQ"))
					fsi_impetq = fs.getAbsRow(s).getValor();
				else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPCAB"))
					fsi_impcab = fs.getAbsRow(s).getValor();
				else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPDET"))
					fsi_impdet = fs.getAbsRow(s).getValor();
				else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_DET"))
				{
					fsi_det_Y = fs.getAbsRow(s).getYPos();
					fsi_det_Alt = fs.getAbsRow(s).getAlto();
					fsi_det_lin = Integer.parseInt(fs.getAbsRow(s).getValor());
				}
				else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_CAB"))
					fsi_cab_Alt = fs.getAbsRow(s).getAlto();
				else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_VENTANA"))
				{
					//fsi_ventana_Alt = (int)fs.getAbsRow(s).getAlto();
					//fsi_ventana_Anc = (int)fs.getAbsRow(s).getAncho();
				}
			}
		    buf.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n");
		    buf.append("<html xmlns='http://www.w3.org/1999/xhtml'>\n");
		    buf.append("<head><style language='text/css'>\n");
		    buf.append("table {	overflow: auto;	position: absolute;	visibility: visible; z-index: auto;	}\n");
		    buf.append(".fsiIMPTIT { " + fsi_imptit + "	}\n");
		    buf.append(".fsiIMPETQ { " + fsi_impetq + "	}\n");
		    buf.append(".fsiIMPCAB { " + fsi_impcab + "	}\n");
		    buf.append(".fsiIMPDET { " + fsi_impdet + "	}\n");
		    buf.append("</style>\n");
		    buf.append("<title>CFDI Forseti</title>\n");
		    buf.append("</head>\n");
		    buf.append("<body leftmargin='0' topmargin='0' rightmargin='0' bottommargin='0' marginwidth='0' marginheight='0'>\n");
		 
		    //System.out.println(SQLCab);
			//System.out.println(SQLDet);
			
			JSetDinamico cab = new JSetDinamico(request);
		    cab.setSQL(SQLCab);
		    cab.Open();
		    		    
		    JSetDinamico det = new JSetDinamico(request);
		    det.setSQL(SQLDet);
		    det.Open();
			
			// las paginas totales se calculan dividiendo el total de partidas de detalle / el total de lines en el reporte
			int tot_pags;
			if(fsi_det_lin < 1)
			{
				tot_pags = 1;
				fsi_det_lin = 255;
			}
			else if(det.getNumRows() <= fsi_det_lin)
				tot_pags = 1;
			else if((det.getNumRows() % fsi_det_lin) == 0)
				tot_pags = (int)(det.getNumRows() / fsi_det_lin);
			else
				tot_pags = (int)Math.floor(det.getNumRows() / fsi_det_lin) + 1;
			buf.append("<!-- Cabecero: " + cab.getNumRows() + " Detalles: " + det.getNumRows() + " Total de paginas: " + tot_pags + " FC: " + fc.getNumRows() + "  LINEAS: " + fsi_det_lin + "-->\n");
		
			for(int pag = 0; pag < tot_pags; pag++)
			{	
				for(int c = 0; c < fc.getNumRows(); c++)
				{
					if(fc.getAbsRow(c).getEtiqueta().equals("FSI_ETIQUETA"))
					{
						
						buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + fc.getAbsRow(c).getXPos() + "mm; top:" + ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) + "mm; height:" + fc.getAbsRow(c).getAlto() + "mm; width:" + fc.getAbsRow(c).getAncho() + "mm;'>\n");
						buf.append("	<tr>\n");
						if(fc.getAbsRow(c).getValor().equals("FSI_QRCODE"))
							buf.append("  <td align='" + fc.getAbsRow(c).getAlinHor() + "' valign='" + fc.getAbsRow(c).getAlinVer() + "'><img src='" + pdfpath + nombreArchivos + ".png" + "' border='0'></img></td>\n");
						else	
							buf.append("  <td align='" + fc.getAbsRow(c).getAlinHor() + "' valign='" + fc.getAbsRow(c).getAlinVer() + "' style='color:#" + fc.getAbsRow(c).getFGColor() + ";' class='fsiIMPETQ'>" + fc.getAbsRow(c).getValor() + "</td>\n");
						buf.append("	</tr>\n");
						buf.append("</table>\n");
					}
					else if(fc.getAbsRow(c).getEtiqueta().equals("FSI_TITULO"))
					{
						buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + fc.getAbsRow(c).getXPos() + "mm; top:" + ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) + "mm; height:" + fc.getAbsRow(c).getAlto() + "mm; width:" + fc.getAbsRow(c).getAncho() + "mm;'>\n");
						buf.append("	<tr>\n");
						buf.append("		<td align='" + fc.getAbsRow(c).getAlinHor() + "' valign='" + fc.getAbsRow(c).getAlinVer() + "' style='color:#" + fc.getAbsRow(c).getFGColor() + ";' class='fsiIMPTIT'>" + fc.getAbsRow(c).getValor() + "</td>\n");
						buf.append("	</tr>\n");
						buf.append("</table>\n");
					}
					else if(fc.getAbsRow(c).getEtiqueta().equals("FSI_LH"))
					{
						buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + fc.getAbsRow(c).getXPos() + "mm; top:" + ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) + "mm; height:" + fc.getAbsRow(c).getAlto() + "mm; width:" + fc.getAbsRow(c).getAncho() + "mm;'>\n");
						buf.append("	<tr>\n");
						buf.append("      <td><img src='../forsetiweb/" + fc.getAbsRow(c).getValor() + ".gif' style='height:" + fc.getAbsRow(c).getAlto() + "mm; width:" + fc.getAbsRow(c).getAncho() + "mm;' border='0'></img></td>\n");
						buf.append("	</tr>\n");
						buf.append("</table>\n");
					}
					else
					{
						for(int dc = 0; dc < cab.getNumCols(); dc++) // recorre las columnas
						{
							if(fc.getAbsRow(c).getEtiqueta().equalsIgnoreCase(cab.getCol(dc).getNombreCol())) // si la etiqueta es igual all nombre de la columna
							{
								for(int ntc = 0; ntc < cab.getNumRows(); ntc++) // recorre el set del  cabecero
								{
									String cabval = JUtil.FormatearImp(cab.getAbsRow(ntc).getSTS(cab.getCol(dc).getNombreCol()), fc.getAbsRow(c).getValor(), cab.getCol(dc).getNombreTipoCol(), request, cab.getAbsRow(ntc).getSTS("ID_Moneda"), cab.getAbsRow(ntc).getSTS("Moneda"));
									buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + fc.getAbsRow(c).getXPos() + "mm; top:" + ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) + "mm; height:" + fc.getAbsRow(c).getAlto() + "mm; width:" + fc.getAbsRow(c).getAncho() + "mm;'>\n");
									buf.append("<tr>\n");
									buf.append("<td align='" + fc.getAbsRow(c).getAlinHor() + "' valign='" + fc.getAbsRow(c).getAlinVer() + "' style='color:#" + fc.getAbsRow(c).getFGColor() + ";' class='fsiIMPCAB'>" + cabval + "</td>\n");
									buf.append("</tr>\n");
									buf.append("</table>\n");
								}
								break;
							}
						}
					}
			    }
			}
			
			// Ahora trabaja con los detalles. actua diferente porque no se imprimen en cada pagina de datos, sino que se imprimen los respectivos
			// registros en la posicion adecuada
			int pagaux = 1;
			int cabAlt = 0;
			
			for(int ntc = 0; ntc < det.getNumRows(); ntc++) // recorre el set del detalle primero
			{
				int pag;
				if((ntc+1) <= fsi_det_lin)
				{
					pag = 1;
					cabAlt++;
				}
				else if(((ntc+1) % fsi_det_lin) == 0)
				{
					pag = (int)((ntc+1) / fsi_det_lin);
					if(pag > pagaux)
					{
						pagaux = pag;
						cabAlt = 1;
					}
					else
						cabAlt++;
				}
				else
				{
					pag = (int)Math.floor((ntc+1) / fsi_det_lin) + 1;
					if(pag > pagaux)
					{
						pagaux = pag;
						cabAlt = 1;
					}
					else
						cabAlt++;
				}
				for(int d = 0; d < fd.getNumRows(); d++)
				{
					if(fd.getAbsRow(d).getEtiqueta().equals("FSI_ETIQUETA"))
					{
						buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + fd.getAbsRow(d).getXPos() + "mm; top:" + ((pag-1) * fsi_cab_Alt) + ( fsi_det_Y + ( fsi_det_Alt * (cabAlt-1) ) + fd.getAbsRow(d).getYPos() ) + "mm; height:" + fd.getAbsRow(d).getAlto() + "mm; width:" + fd.getAbsRow(d).getAncho() + "mm;'>\n");
						buf.append("	<tr>\n");
						buf.append("		<td align='" + fd.getAbsRow(d).getAlinHor() + "' valign='" + fd.getAbsRow(d).getAlinVer() + "' style='color:#" + fd.getAbsRow(d).getFGColor() + ";' class='fsiIMPETQ'>" + fd.getAbsRow(d).getValor() + "</td>\n");
						buf.append("	</tr>\n");
						buf.append("</table>\n");
					}
					else
					{
						for(int dd = 0; dd < det.getNumCols(); dd++) // recorre las columnas
						{
							if(fd.getAbsRow(d).getEtiqueta().equalsIgnoreCase(det.getCol(dd).getNombreCol())) // si la etiqueta es el nombre de la columna
							{
								float top =  JUtil.redondear( ((pag-1) * fsi_cab_Alt) + ( fsi_det_Y + ( fsi_det_Alt * (cabAlt-1) ) + fd.getAbsRow(d).getYPos() ), 1);
								buf.append("<!-- Pag m1: " + (pag -1) + " fsi_cab_Alt: " + fsi_cab_Alt + " fsi_det_Y: " + fsi_det_Y + " fsi_det_Alt: " + fsi_det_Alt + "  (cabAlt-1): " + (cabAlt-1)  + "  fd.getAbsRow(d).getYPos(): " + fd.getAbsRow(d).getYPos() + " TOTAL: " + top + " -->\n");
								
								String detval = JUtil.FormatearImp(det.getAbsRow(ntc).getSTS(det.getCol(dd).getNombreCol()), fd.getAbsRow(d).getValor(), det.getCol(dd).getNombreTipoCol(), request, null, null);
								buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + fd.getAbsRow(d).getXPos() + "mm; top:" + top + "mm; height:" + fd.getAbsRow(d).getAlto() + "mm; width:" + fd.getAbsRow(d).getAncho() + "mm;'>\n");
								buf.append("  <tr>\n");
								buf.append("	<td align='" + fd.getAbsRow(d).getAlinHor() + "' valign='" + fd.getAbsRow(d).getAlinVer() + "' style='color:#" + fd.getAbsRow(d).getFGColor() + ";' class='fsiIMPDET'>" + detval + "</td>\n");
								buf.append("  </tr>\n");
								buf.append("</table>\n");
								break;						
							}
						}
					}
				}
			}
			// Termina por imprimir el final. este se refiere a cuando hay cabecero que se imprima no en cada hoja, sino a partir del final del ultimo registro de detalle
			for(int f = 0; f < ff.getNumRows(); f++)
			{
				if(ff.getAbsRow(f).getEtiqueta().equals("FSI_ETIQUETA"))
				{
					buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + ff.getAbsRow(f).getXPos() + "mm; top:" + ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) +"mm; height:" + ff.getAbsRow(f).getAlto() + "mm; width:" + ff.getAbsRow(f).getAncho() + "mm;'>\n");
					buf.append("	<tr>\n");
					buf.append("		<td align='" + ff.getAbsRow(f).getAlinHor() + "' valign='" + ff.getAbsRow(f).getAlinVer() + "' style='color:#" + ff.getAbsRow(f).getFGColor() + ";' class='fsiIMPETQ'>" + ff.getAbsRow(f).getValor() + "</td>\n");
					buf.append("	</tr>\n");
					buf.append("</table>\n");
		
				}
				else if(ff.getAbsRow(f).getEtiqueta().equals("FSI_TITULO"))
				{
		 
					buf.append("	<table border='0' cellpadding='0' cellspacing='0' style='left:" + ff.getAbsRow(f).getXPos() + "mm; top:" + ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) + "mm; height:"  + ff.getAbsRow(f).getAlto() + "mm; width:" + ff.getAbsRow(f).getAncho() + "mm;'>\n");
					buf.append("		<tr>\n");
					buf.append("			<td align='" + ff.getAbsRow(f).getAlinHor() + "' valign='" + ff.getAbsRow(f).getAlinVer() + "' style='color:#" + ff.getAbsRow(f).getFGColor() + ";' class='fsiIMPTIT'>" + ff.getAbsRow(f).getValor() + "</td>\n");
					buf.append("		</tr>\n");
					buf.append("	</table>\n");
		
				}
				else if(ff.getAbsRow(f).getEtiqueta().equals("FSI_LH"))
				{
		 
					buf.append("	<table border='0' cellpadding='0' cellspacing='0' style='left:" + ff.getAbsRow(f).getXPos() + "mm; top:" + ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) + "mm; height:" + ff.getAbsRow(f).getAlto() + "mm; width:" + ff.getAbsRow(f).getAncho() + "mm;'>\n");
					buf.append("		<tr>\n");
					buf.append("	      <td><img src='../forsetiweb/" + ff.getAbsRow(f).getValor() + ".gif' style='height:" + ff.getAbsRow(f).getAlto() + "mm; width:" + ff.getAbsRow(f).getAncho() + "mm;' border='0'></img></td>\n");
					buf.append("		</tr>\n");
					buf.append("	</table>\n");
		
				}
				else
				{
					for(int dc = 0; dc < cab.getNumCols(); dc++) // recorre las columnas
					{
						if(ff.getAbsRow(f).getEtiqueta().equalsIgnoreCase(cab.getCol(dc).getNombreCol())) // si la etiqueta es igual all nombre de la columna
						{
							for(int ntc = 0; ntc < cab.getNumRows(); ntc++) // recorre el set del  cabecero
							{
								String cabval = JUtil.FormatearImp(cab.getAbsRow(ntc).getSTS(cab.getCol(dc).getNombreCol()), ff.getAbsRow(f).getValor(), cab.getCol(dc).getNombreTipoCol(), request, cab.getAbsRow(ntc).getSTS("ID_Moneda"), cab.getAbsRow(ntc).getSTS("Moneda"));
								buf.append("<table border='0' cellpadding='0' cellspacing='0' style='left:" + ff.getAbsRow(f).getXPos() + "mm; top:" + ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) + "mm; height:" + ff.getAbsRow(f).getAlto() + "mm; width:" + ff.getAbsRow(f).getAncho() + "mm;'>\n");
								buf.append("	<tr>\n");
								buf.append("		<td align='" + ff.getAbsRow(f).getAlinHor() + "' valign='" + ff.getAbsRow(f).getAlinVer() + "' style='color:#" + ff.getAbsRow(f).getFGColor() + ";' class='fsiIMPCAB'>" + cabval + "</td>\n");
								buf.append("	</tr>\n");
								buf.append("</table>\n");
							}
							break;
						}
					}
				}
			}
			buf.append("</body></html>\n");

			//out.println(buf.toString());
			//System.out.println(buf.toString());
			
			// FINALIZA CAPTURA DEL FORMATO
			ITextRenderer renderer = new ITextRenderer();
	    	renderer.setDocumentFromString(buf.toString());
	    	String outputFilePdf = pdfpath + nombreArchivos + ".pdf";
	    	OutputStream os = new FileOutputStream(outputFilePdf);
	    	renderer.layout();
	    	renderer.createPDF(os);
	    	os.close();
        	// FIN DE PROCESO LARGO
		    // Fin de generacion del PDF
	    	////////////////////////////////////////////////////////////////////////////////
	    	
			//Ahora manda datos del pdf base de datos
	        String str = "select * from sp_cfd_pdfgen('" + JUtil.q(tipo) + "','" + id + "','" + JUtil.q(esp1) + "') as (err int, res varchar)";
	        //System.out.println(str);
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
				        
	        // Por ultimo Borra los archivos inecesarios
	        String [] paths = 
	        { 
	        		"/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/" + nombreArchivos + ".cer", 
	        		"/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/" + nombreArchivos + "-CO.b64", 
	        		"/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/" + nombreArchivos + "-CO.utf8",
	        		"/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/" + nombreArchivos + "-CO.sha1",
	        		//"/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/" + nombreArchivos + ".zip",
	        		//"/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/TFDs/" + nombreArchivos + ".zip",
	        		"/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/PDFs/" + nombreArchivos + ".png"
	        };
	        
	        for(int i = 0; i < paths.length; i++)
	        {
	        	File f = new File(paths[i]);
	        	f.delete();
	        }
			        
		} 
		catch(JDOMException e)
		{
			e.printStackTrace();
	    	ERROR += "ERROR DE ESTRUCTURA XML (JDom) AL GENERAR PDF: " + e.getMessage() + "<br>";
		}
		catch(NullPointerException e) 
		{
			e.printStackTrace();
	    	ERROR += "ELEMENTO NULO EN EL XML DEL TIMBRE AL GENERAR PDF. ESTE TIMBRE DEBE ESTAR MAL, POR LO TANTO EL CFDI NO ES VALIDO: " + e.getMessage() + "<br>";;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
	    	ERROR += "ERROR DESCONOCIDO DE ARCHIVOS AL CARGAR TIMBRE EN GENERACION DEL PDF: " + e.getMessage() + "<br>";
		} 
		catch(SQLException e)
	    {
			e.printStackTrace();
	    	ERROR += "ERROR DEL SQL AL GENERAR REGISTRO DE PDF: " + e.getMessage() + "<br>";
	    }
		catch (EncodingFailedException efe)
        {
			efe.printStackTrace();
	    	ERROR += "Error en Timbre para la generacion del PDF; La versión que ha seleccionado de QRCode no tiene la suficiente capacidad para albergar los datos introducidos. Prueba con una versión mayor, no con otro nivel de corrección: " + efe.getMessage() + "<br>";
        }
        catch (Exception e) 
		{
        	e.printStackTrace();
	    	ERROR += "Error en Timbre al generar PDF; No se pudo crear el QRCode o PDF: " + e.getMessage() + "<br>";
		}
	    
	    
		if(!ERROR.equals(""))
		{
			m_StatusCFD = JForsetiCFD.ERROR;
			m_Error = ERROR;
		}
		else
		{
			m_StatusCFD = OKYDOKY;
			m_Error = "";
		}
	}
	
	protected void generarCFDI(HttpServletRequest request, String tipo, int id, String esp1) 
		throws ServletException 
	{
		// Primero guarda la cadena original;
		m_StatusCFD = NULO;
		m_LineaRep[13] = tipo;
		m_LineaRep[14] = Integer.toString(id);
		
		String nombreArchivos;
		if(tipo.equals("NOM"))
			nombreArchivos = tipo + "-" + Integer.toString(id) + "-" + esp1;
		else
			nombreArchivos = tipo + "-" + Integer.toString(id);
		
		String CONTENT;
		
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		
		String ERROR = "";
		
		try 
		{
			String pathkeypem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoLLaveXML + ".pem";
			String pathname = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/";
			
			File f = new File(pathname + nombreArchivos + "-CO.utf8");
			FileOutputStream fout = new FileOutputStream(f);
			fout.write(m_CadenaOriginal.getBytes("UTF-8"));
			
			if (fout != null)
				fout.close();
			
			m_LineaRep[16] = getCadenaOriginalUTF8();
			
			//Ahora genera el archivo md5 (CFD Version 2)
			//CONTENT = "openssl dgst -md5 -sign " + pathkeypem + " -out " +  pathname + nombreArchivos + "-CO.md5 " + pathname + nombreArchivos + "-CO.utf8"; 
			//Ahora genera el archivo sha1 (CFDI Version 3)
			CONTENT = "openssl dgst -sha1 -sign " + pathkeypem + " -out " +  pathname + nombreArchivos + "-CO.sha1 " + pathname + nombreArchivos + "-CO.utf8"; 
			sc.setContent(CONTENT);
			sc.executeCommand();
			ERROR += sc.getError();
			//Ahora genera el Archivo Base64
			if(ERROR.equals(""))
			{
				// Cambia md5 a base 64 en CFD
				//CONTENT = "openssl enc -base64 -in " + pathname + nombreArchivos + "-CO.md5 -out " + pathname + nombreArchivos + "-CO.b64"; 
				// Cambia sha1 a base 64 en CFDI
				CONTENT = "openssl enc -base64 -in " + pathname + nombreArchivos + "-CO.sha1 -out " + pathname + nombreArchivos + "-CO.b64"; 
				sc.setContent(CONTENT);
				sc.executeCommand();
				ERROR += sc.getError();
			}
			//abre el archivo -CO.b64 para guardarlo en el atributo sello del XML
			if(ERROR.equals(""))
			{
				f = new File(pathname + nombreArchivos + "-CO.b64");
				int ch;
				StringBuffer strContent = new StringBuffer("");
				FileInputStream fin = new FileInputStream(f);
				while( (ch = fin.read()) != -1)
				{
					if(ch != '\n')
						strContent.append((char)ch);
				}
				fin.close();
				
				m_Comprobante.setAttribute("sello",strContent.toString());
				m_LineaRep[17] = strContent.toString();
				System.out.println("CO.b64");
			}
			
			// Ahora, graba el archivo xml
			if(ERROR.equals(""))
			{
				Format format = Format.getPrettyFormat();
				format.setEncoding("utf-8");
				format.setTextMode(TextMode.NORMALIZE);
				XMLOutputter xmlOutputter = new XMLOutputter(format);
				FileWriter writer = new FileWriter(pathname + nombreArchivos + ".xml");
				xmlOutputter.output(m_XMLDoc, writer);
				writer.close();
			}
			
			if(ERROR.equals(""))
			{
				String str = "select * from sp_cfd_agregar('" + JUtil.q(m_LineaRep[0]) + "','" + JUtil.q(m_LineaRep[1]) + "','" + JUtil.q(m_LineaRep[2]) + "','" + JUtil.q(m_LineaRep[3]) + "','" + JUtil.q(m_LineaRep[4]) + "','" + JUtil.q(m_LineaRep[5]) + "','" +
				JUtil.q(m_LineaRep[6]) + "','" + JUtil.q(m_LineaRep[7]) + "','" + JUtil.q(m_LineaRep[8]) + "','" + JUtil.q(m_LineaRep[9]) + "','" + JUtil.q(m_LineaRep[10]) + "','" + JUtil.q(m_LineaRep[11]) + "','" + JUtil.q(m_LineaRep[12]) + "','" + JUtil.q(m_LineaRep[13]) + "','" +
				JUtil.q(m_LineaRep[14]) + "','" + JUtil.q(m_LineaRep[15]) + "','" + JUtil.obtCadenaEnLineas(JUtil.q(m_LineaRep[16]), ' ', 110) + "','" + JUtil.obtCadenaEnLineas(JUtil.q(m_LineaRep[17]), ' ', 110) + "','" + JUtil.q(m_LineaRep[18]) + "','" + JUtil.q(m_LineaRep[19]) + "','" + JUtil.q(esp1) + "') as (err int, res varchar)";
				//System.out.println(str);
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
					
			} // termina modo de produccion
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
	    	ERROR += "ERROR DESCONOCIDO DE ARCHIVOS AL GENERAR CFDI: " + e.getMessage() + "<br>";
		} 
		catch(SQLException e)
	    {
			e.printStackTrace();
	    	ERROR += "ERROR DEL SQL AL GENERAR CFDI: " + e.getMessage() + "<br>";
	    }
		catch (Exception e) 
		{
			e.printStackTrace();
	    	ERROR += "ERROR DESCONOCIDO AL GENERAR CFDI: " + e.getMessage() + "<br>";
			ERROR += sc.getError();
		}
	    
	    
		if(!ERROR.equals(""))
		{
			//System.out.println("error en CFDI");
			m_StatusCFD = JForsetiCFD.ERROR;
			m_Error = ERROR;
		}
		else
		{
			//System.out.println("CFDI Ok");
			m_StatusCFD = OKYDOKY;
			m_Error = "";
		}
	}
	
	/*
	@SuppressWarnings("static-access")
	public void generarCancelacion(HttpServletRequest request, String tipo, Integer id) 
		throws ServletException, IOException 
	{
		// Primero guarda la cadena original;
		m_StatusCFD = NULO;
		
		String pathcerpem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoCertificadoPDF + ".pem";
		String pathkeypem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoLLaveXML + ".pem";
		String path = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/TFDs/";
		String nombreArchivos = tipo + "-" + id.toString();
		String CONTENT;
		
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		
		String ERROR = "";
		
		try 
		{
			//Obtiene el UUID del CFDI
            SAXBuilder builder = new SAXBuilder();
	    	String xml_path = path + "SIGN_" + nombreArchivos + ".xml";
			Document XMLTFD = builder.build(xml_path);
	    	Element Comprobante = XMLTFD.getRootElement();
	    	Element Complemento = Comprobante.getChild("Complemento", m_ns);
	    	Namespace nstfd = Namespace.getNamespace("tfd","http://www.sat.gob.mx/TimbreFiscalDigital");
	    	Element tfd = Complemento.getChild("TimbreFiscalDigital", nstfd);
	    	
			String [] uuids = new String [1];
			uuids[0] = tfd.getAttributeValue("UUID");
			//System.out.println("UUIDS: " + uuids[0]);
			
			//Genera el PFX
			//openssl x509 -inform DER -in aaa010101aaa_csd_01.cer -out certificado.pem
			//openssl pkcs8 -inform DER -in aaa010101aaa_csd_01.key -passin pass:a0123456789 -out llave.pem
			CONTENT = "sudo openssl pkcs12 -export -out " + path + nombreArchivos + ".pfx -inkey " + pathkeypem + " -in " + pathcerpem + " -passout pass:" + m_PacPass;
			sc.setContent(CONTENT);
			sc.executeCommand();
			ERROR += sc.getError();
			//System.out.println("PFX GENERADO");
			
			//Ahora toma el Archivo Pfx en un array de bytes
			if(ERROR.equals(""))
			{
				File file = new File(path + nombreArchivos + ".pfx");
			    FileInputStream fis = new FileInputStream(file);
			    byte fileContent[] = new byte[(int)file.length()];
			    fis.read(fileContent);
			    //System.out.println("BYTE DE PFX OK");
				//System.out.println(fileContent);
				//System.out.println(m_PacURL + " " + m_PacUsr + " " + m_PacPass + " " + m_CFD_RFC + " " + uuids[0] + " " + fileContent[0] + " " + m_PacPass);
			    
			    //Genera la peticion de cancelacion
			    CfdiClient client = new CfdiClient(m_PacURL);
			    CancelaResponse res = client.cancelCfdi(m_PacUsr, m_PacPass, m_CFD_RFC, uuids, fileContent, m_PacPass);
			    System.out.println("NUM UUDIS CANCELADOS: " + res.getUuids().length);
				//Obtiene la respuesta XML y la decodifica
			    Base64 decoder = new Base64();
	            String Ack = new String(decoder.decode(res.getAck()));
	            //Guarda el archivo de respuesta
	            File f = new File(path + "CANCEL_" + nombreArchivos + ".xml");
	            FileWriter fw = new FileWriter(f);
	            fw.write(Ack);
	            fw.close(); 
	            //Ahora obtiene el xml JDom
	            SAXBuilder builderAck = new SAXBuilder();
		    	Document XMLRES = builderAck.build(path + "CANCEL_" + nombreArchivos + ".xml");
		    	//Ahora saca los datos de respuesta
		    	Element root = XMLRES.getRootElement();
		    	StringBuffer UUID  = new StringBuffer();
	    		StringBuffer EstatusUUID = new StringBuffer();
	    		domElementText(root, 0, "UUID", UUID, "trim");
	    		domElementText(root, 0, "EstatusUUID", EstatusUUID, "trim");
	    		// Ya obtuvo la respuesta en Ack, grabo el archivo xml, y saco UUID y EstatusUUID
				System.out.println("Peticion de respuesta OK: " + UUID.toString() + " " + EstatusUUID + "\n ACK: " + Ack);
				// Por ultimo, Si no han habido errores, graba en la base de datos llamando al procedimiento almacenado si es que no esta en test
			    String str = "select * from sp_cfd_canceltimbre('" + JUtil.q(uuids[0]) + "','" + JUtil.q(UUID.toString()) + "','" + JUtil.q(EstatusUUID.toString()) + "','" + JUtil.q(res.getAck()) + "') as (err int, res varchar)";
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
		}
		catch(SQLException e)
	    {
			e.printStackTrace();
	    	ERROR += "ERROR DEL SQL AL GENERAR EL TIMBRE: " + e.getMessage() + "<br>";
	    }
		catch (CfdiException e) 
		{
			e.printStackTrace();
			ERROR += "--- ERROR EDICOM CFDI ---<br>Code: " + e.getCod() + "<br>Text: " + e.getText() + "<br>TextCode: "  + e.getTextCode() + "<br>MessageCode: " + CfdiException.getTextCode(e.getCod());
		}
		catch(NullPointerException e) 
		{
			e.printStackTrace();
			ERROR += "ELEMENTO NULO EN EL XML DEL TIMBRE. ESTE TIMBRE DEBE ESTAR MAL, POR LO TANTO EL CFDI NO ES VALIDO: " + e.getMessage() + "<br>";;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR += "ERROR DESCONOCIDO DE ARCHIVOS AL CARGAR TIMBRE: " + e.getMessage() + "<br>";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR += "ERROR EN TIMBRE: " + e.getMessage();
		}
	    
	    
		if(!ERROR.equals(""))
		{
			//System.out.println("ERROR CAPTURADO: " + ERROR);
			m_StatusCFD = JForsetiCFD.ERROR;
			m_Error = ERROR;
		}
		else
		{
			m_StatusCFD = OKYDOKY;
			m_Error = "";
		}
	}
	*/
	
	public void generarCancelacion(HttpServletRequest request, String tipo, Integer id) 
		throws ServletException, IOException 
	{
		// Primero guarda la cadena original;
		m_StatusCFD = NULO;
		
		String pathcerpem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoCertificadoPDF + ".pem";
		String pathkeypem = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/certs/" + m_ArchivoLLaveXML + ".pem";
		String path = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/TFDs/";
		String nombreArchivos = tipo + "-" + id.toString();
		
		String ERROR = "";
		
		try 
		{
			//Carga el string de keypem y cerpem para ser enviados
			FileReader filecer = new FileReader(pathcerpem);
			BufferedReader buffcer     = new BufferedReader(filecer);
			String cerpem = "", line = "";
			while((line = buffcer.readLine()) != null)
				cerpem  += line + "\n";
			buffcer.close();
			
			FileReader filekey = new FileReader(pathkeypem);
			BufferedReader buffkey     = new BufferedReader(filekey);
			String keypem = "";
			while((line = buffkey.readLine()) != null)
				keypem  += line + "\n";
			buffkey.close();
			
		    //Genera la peticion de cancelacion
		    ///////////////////////////////////////////////////////////////////////////////////////////////////
		    String urlParameters = 	"SERVER=" + URLEncoder.encode(m_PacServ,"ISO-8859-1") +
		    		"&DATABASE=" + URLEncoder.encode(JUtil.getSesion(request).getBDCompania(),"ISO-8859-1") +
		    		"&USER=" + URLEncoder.encode(m_PacUsr,"ISO-8859-1") +
		    		"&PASSWORD=" + URLEncoder.encode(m_PacPass,"ISO-8859-1") +
		    		"&NAME=" + URLEncoder.encode(nombreArchivos,"ISO-8859-1") +
		    		"&CANCEL=" + URLEncoder.encode("true","ISO-8859-1") +
		    		"&CERPEM=" + URLEncoder.encode(cerpem,"ISO-8859-1") +
		    		"&KEYPEM=" + URLEncoder.encode(keypem,"ISO-8859-1");
		    String urlString = "https://" + m_PacURL + "/servlet/SAFPacConn";
		    System.out.println(urlString);
		    System.out.println(urlParameters);
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
		    	System.out.println("Conectado a revidor PAC");
		    	// Recibe la respuesta del servidor, esta debe estar en formato xml
		    	SAXBuilder builder = new SAXBuilder();
		    	Document document = (Document)builder.build(conn.getInputStream());
		    	Element Comprobante = document.getRootElement();
					
		    	if(Comprobante.getName().equals("SIGN_ERROR"))// Significan errores
		    	{
		    		ERROR += "Codigo de Error JPacConn: " + Comprobante.getAttribute("CodError") + "<br>" + Comprobante.getAttribute("MsjError");
		    	}
		    	else //if(Comprobante.getName().equals("?????????????????"))
		    	{
		    		Format format = Format.getPrettyFormat();
		    		format.setEncoding("utf-8");
		    		format.setTextMode(TextMode.NORMALIZE);
		    		XMLOutputter xmlOutputter = new XMLOutputter(format);
		    		FileWriter writer = new FileWriter(path + "CANCEL_" + nombreArchivos + ".xml");
		    		xmlOutputter.output(document, writer);
		    		writer.close();
		    		System.out.println("La respuesta de cancelacion se cargo con exito");
		    	}
		    	/*else
				{
					ERROR += "ERROR: El archivo recibido es un archivo XML, pero no parece ser una respuesta de Cancelacion ni una respuesta de ERROR del servidor JPacConn. Esto debe ser investigado de inmediato, Contacta con tu intermediario de sellos Forseti para que te explique el problema";
				}*/
				// Fin de archivo grabado
		    	conn.disconnect();
		    }
		    else // La conexion no tuvo exito
		    {
		    	System.out.println("No se establecio la conexion");
		    	ERROR += "ERROR: Al intentar conectarse al servidor. Revisa los datos del intermediario de Sellos Forseti en el archivo de configuración";
		    }
		    
		    ///////////////////////////////////////////////////////////////////////////////////////////////////
		    if(ERROR.equals(""))
		    {
		    	//Ahora obtiene el xml JDom
		    	SAXBuilder builderAck = new SAXBuilder();
		    	Document XMLRES = builderAck.build(path + "CANCEL_" + nombreArchivos + ".xml");
		    	//Ahora saca los datos de respuesta
		    	Element root = XMLRES.getRootElement();
		    	StringBuffer UUID  = new StringBuffer();
		    	StringBuffer EstatusUUID = new StringBuffer();
		    	domElementText(root, 0, "UUID", UUID, "trim");
		    	domElementText(root, 0, "EstatusUUID", EstatusUUID, "trim");
		    	// Ya obtuvo la respuesta en Ack, grabo el archivo xml, y saco UUID y EstatusUUID
		    	System.out.println("Peticion de respuesta OK: " + UUID.toString() + " " + EstatusUUID.toString());
		    	// Por ultimo, Si no han habido errores, graba en la base de datos llamando al procedimiento almacenado si es que no esta en test
		    	String str = "select * from sp_cfd_canceltimbre('" + JUtil.q(UUID.toString()) + "','" + JUtil.q(UUID.toString()) + "','" + JUtil.q(EstatusUUID.toString()) + "','" + JUtil.q(path + "CANCEL_" + nombreArchivos + ".xml") + "') as (err int, res varchar)";
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
		}
		catch(SQLException e)
	    {
			e.printStackTrace();
	    	ERROR += "ERROR DE SQLException AL CANCELAR EL TIMBRE: " + e.getMessage() + "<br>";
	    }
		catch (JDOMException e) 
		{
			e.printStackTrace();
	    	ERROR += "ERROR DE JDOMException AL CANCELAR EL TIMBRE: " + e.getMessage() + "<br>";
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR += "ERROR DESCONOCIDO DE ARCHIVOS AL CANCELAR TIMBRE: " + e.getMessage() + "<br>";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR += "ERROR GENERAL AL CANCELAR TIMBRE: " + e.getMessage() + "<br>";
		}
	     
	    
		if(!ERROR.equals(""))
		{
			//System.out.println("ERROR CAPTURADO: " + ERROR);
			m_StatusCFD = JForsetiCFD.ERROR;
			m_Error = ERROR;
		}
		else
		{
			m_StatusCFD = OKYDOKY;
			m_Error = "";
		}
	}
	
	public void generarTFD(HttpServletRequest request, String tipo, int id, String esp1) 
		throws ServletException, IOException 
	{
		// Primero guarda la cadena original;
		m_StatusCFD = NULO;
		
		String nombreArchivos;
		if(tipo.equals("NOM"))
			nombreArchivos = tipo + "-" + Integer.toString(id) + "-" + esp1;
		else
			nombreArchivos = tipo + "-" + Integer.toString(id);
		
		String ERROR = "";
		
		try 
		{
			String inputFile = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CFDs/" + nombreArchivos + ".xml";
			String destinationname = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/TFDs/";
	        String xml_path = destinationname + "SIGN_" + nombreArchivos + ".xml";
	    	System.out.println("Empezando timbrado");
			/////////////////////////////////////////////
			SAXBuilder sourcebuilder = new SAXBuilder();
			Document sourcedoc = sourcebuilder.build(inputFile);
			XMLOutputter outp = new XMLOutputter();	
			String urlParameters = 	"SERVER=" + URLEncoder.encode(m_PacServ,"ISO-8859-1") +
									"&DATABASE=" + URLEncoder.encode(JUtil.getSesion(request).getBDCompania(),"ISO-8859-1") +
									"&USER=" + URLEncoder.encode(m_PacUsr,"ISO-8859-1") +
									"&PASSWORD=" + URLEncoder.encode(m_PacPass,"ISO-8859-1") +
									"&NAME=" + URLEncoder.encode(nombreArchivos,"ISO-8859-1") +
									"&CANCEL=" + URLEncoder.encode("false","ISO-8859-1") +
									"&TEST=" + URLEncoder.encode((m_PacTest ? "true" : "false"),"ISO-8859-1") +
									"&CFDXML=" + URLEncoder.encode(outp.outputString(sourcedoc),"ISO-8859-1");
			String urlString = "https://" + m_PacURL + "/servlet/SAFPacConn";
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
	        	System.out.println("Conectado a revidor PAC");
				// Recibe la respuesta del servidor, esta debe estar en formato xml
	        	SAXBuilder builder = new SAXBuilder();
	    		Document document = (Document)builder.build(conn.getInputStream());
	    		Element Comprobante = document.getRootElement();

	    		if(Comprobante.getName().equals("Comprobante"))
	    		{
	    			Format format = Format.getPrettyFormat();
	    			format.setEncoding("utf-8");
	    			format.setTextMode(TextMode.NORMALIZE);
	    			XMLOutputter xmlOutputter = new XMLOutputter(format);
	    			FileWriter writer = new FileWriter(xml_path);
	    			xmlOutputter.output(document, writer);
	    			writer.close();
	    			System.out.println("El comprobante se cargo con exito");
	    		}
	    		else if(Comprobante.getName().equals("SIGN_ERROR"))// Significan errores
	    		{
	    			ERROR += "Codigo de Error JPacConn: " + Comprobante.getAttribute("CodError") + "<br>" + Comprobante.getAttribute("MsjError");
	    		}
	    		else
	    		{
	    			ERROR += "ERROR: El archivo recibido es un archivo XML, pero no parece ser un CFDI ni una respuesta de ERROR del servidor JPacConn. Esto debe ser investigado de inmediato, Contacta con tu intermediario de sellos Forseti para que te explique el problema";
	    		}
	        	// Fin de archivo grabado
	        	conn.disconnect();
	        }
	        else // La conexion no tuvo exito
	        {
	        	System.out.println("No se establecio la conexion");
				ERROR += "ERROR: Al intentar conectarse al servidor. Revisa los datos del intermediario de Sellos Forseti en el archivo de configuración";
	        }
   
	        if(ERROR.equals(""))
			{
	        	///////////////////////////////////////////////////////////////////////////////
	        	//Ya tenemos el timbre aqui en formato XML, ahora sacamos los datos del timbre
	        	SAXBuilder builder = new SAXBuilder();
	        	/*Cargamos el documento*/
	        	Document XMLTFD = builder.build(xml_path);
	    	    /*Sacamos el elemento raiz*/
		    	Element Comprobante = XMLTFD.getRootElement();
		    	//float total = Float.parseFloat(Comprobante.getAttributeValue("total"));
		    	//Element Emisor = Comprobante.getChild("Emisor", m_ns);
		    	//Element Receptor = Comprobante.getChild("Receptor",m_ns);
		    	Element Complemento = Comprobante.getChild("Complemento", m_ns);
		    	Namespace nstfd = Namespace.getNamespace("tfd","http://www.sat.gob.mx/TimbreFiscalDigital");
		    	Element tfd = Complemento.getChild("TimbreFiscalDigital", nstfd);
				// Ahora ya tenemos el timbre, Guardamos la cadena original del timbre
		    	// No vamos a guardar ya el archivo de la cadena del timbre, solo la guardaremos en la base de datos
				String cadenaTimbre = "||" + tfd.getAttributeValue("version") + "|" + tfd.getAttributeValue("UUID") + "|" + tfd.getAttributeValue("FechaTimbrado") + "|" +
					tfd.getAttributeValue("selloCFD") + "|" + tfd.getAttributeValue("noCertificadoSAT") + "||";
				// Por ultimo, Si no han habido errores, graba en la base de datos llamando al procedimiento almacenado si es que no esta en test
		    	String str = "select * from sp_cfd_timbrar('" + JUtil.q(tipo) + "','" + id + "','" + JUtil.q(tfd.getAttributeValue("UUID")) + "','" + JUtil.obtFechaHoraSQLtfd(JUtil.q(tfd.getAttributeValue("FechaTimbrado"))) + "','" +
		    			JUtil.q(tfd.getAttributeValue("noCertificadoSAT")) + "','" + JUtil.obtCadenaEnLineas(JUtil.q(tfd.getAttributeValue("selloSAT")), ' ', 110) + "','" + JUtil.obtCadenaEnLineas(JUtil.q(cadenaTimbre), ' ', 110) + "','" + JUtil.q(esp1) + "') as (err int, res varchar)";
		    	//System.out.println(str);
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
		}
		catch(SQLException e)
	    {
			e.printStackTrace();
	    	ERROR += "ERROR DE SQL AL GENERAR EL TIMBRE: " + e.getMessage() + "<br>";
	    }
		catch(NullPointerException e) 
		{
			e.printStackTrace();
			ERROR += "ELEMENTO NULO EN EL XML DEL TIMBRE. ESTE TIMBRE DEBE ESTAR MAL, POR LO TANTO EL CFDI NO ES VALIDO: " + e.getMessage() + "<br>";;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			ERROR += "ERROR DESCONOCIDO DE ARCHIVOS AL CARGAR TIMBRE: " + e.getMessage() + "<br>";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR += "ERROR EN TIMBRE: " + e.getMessage();
		}
	    
	    
		if(!ERROR.equals(""))
		{
			m_StatusCFD = JForsetiCFD.ERROR;
			m_Error = ERROR;
		}
		else
		{
			m_StatusCFD = OKYDOKY;
			m_Error = "";
		}
	}	
	
	protected String construirLugarExpedicion(String calle, String noExt, String noInt, String colonia,
			String localidad, String municipio, String estado, String pais, String cp)
	{
		String res = "";
		
		if(!calle.equals(""))
			res += calle;
		if(!noExt.equals(""))
			res += " " + noExt;
		if(!noInt.equals(""))
			res += " " + noInt;
		if(!colonia.equals(""))
			res += " " + colonia;
		if(!localidad.equals(""))
			res += " " + localidad;
		if(!municipio.equals(""))
			res += " " + municipio;
		if(!estado.equals(""))
			res += " " + estado;
		if(!pais.equals(""))
			res += " " + pais;
		if(!cp.equals(""))
			res += " " + cp;
		
		return res;
	}
}
