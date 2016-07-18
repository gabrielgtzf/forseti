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

import java.util.Properties;
import java.util.Vector;

@SuppressWarnings("rawtypes")
public class JFacturasXML
{
	private boolean m_bOK;
	private String m_ArchivoXML;
	private String m_ArchivoPDF;
	private Properties m_Parametros;
	private Properties m_TFD;
	private Properties m_Nomina;
	private Vector m_Percepciones;
	private Vector m_Deducciones;
	private Properties m_Comprobante;
	private Properties m_Impuestos;
	private Vector m_Conceptos;
	private Vector m_Traslados;
	private Vector m_Retenciones;
	private String m_RFC_Emisor;
	private String m_Nombre_Emisor;
	private String m_RFC_Receptor;
	private Vector m_Incapacidades;
	private Vector m_HorasExtras;
	
	public JFacturasXML()
	{
		m_bOK = false;
		m_Parametros = new Properties();
		m_TFD = new Properties();
		m_Nomina = new Properties();
		m_Comprobante = new Properties();
		m_Impuestos = new Properties();
		m_Conceptos = new Vector();
		m_Traslados = new Vector();
		m_Retenciones = new Vector();
		m_Percepciones = new Vector();
		m_Deducciones = new Vector();
		m_Incapacidades = new Vector();
		m_HorasExtras = new Vector();
 	}
	
	public void setOK(boolean bOK)
	{
		m_bOK = bOK;
	}
	
	public boolean getOK()
	{
		return m_bOK;
	}
	
	public String getArchivoXML() 
	{
		return m_ArchivoXML;
	}

	public void setArchivoXML(String ArchivoXML) 
	{
		m_ArchivoXML = ArchivoXML;
	}

	public String getArchivoPDF() 
	{
		return m_ArchivoPDF;
	}

	public void setArchivoPDF(String ArchivoPDF) 
	{
		m_ArchivoPDF = ArchivoPDF;
	}
	
	public void setRFC_Emisor(String RFC_Emisor)
	{
		m_RFC_Emisor = RFC_Emisor;
	}
	
	public void setNombre_Emisor(String Nombre_Emisor)
	{
		m_Nombre_Emisor = Nombre_Emisor;
	}
	
	public void setRFC_Receptor(String RFC_Receptor)
	{
		m_RFC_Receptor = RFC_Receptor;
	}

	public String getRFC_Emisor()
	{
		return m_RFC_Emisor;
	}
	
	public String getRFC_Receptor()
	{
		return m_RFC_Receptor;
	}
	
	public Properties getComprobante()
	{
		return m_Comprobante;
	}
	public String getNombre_Emisor()
	{
		return m_Nombre_Emisor;
	}
	public Properties getTFD()
	{
		return m_TFD;
	}
	public Properties getNomina()
	{
		return m_Nomina;
	}
	public Properties getParametros()
	{
		return m_Parametros;
	}
	public Properties getImpuestos()
	{
		return m_Impuestos;
	}
	public Vector getConceptos()
	{
		return m_Conceptos;
	}
	public Vector getTraslados()
	{
		return m_Traslados;
	}
	public Vector getRetenciones() 
	{
		return m_Retenciones;
	}
	public Vector getPercepciones()
	{
		return m_Percepciones;
	}
	public Vector getDeducciones() 
	{
		return m_Deducciones;
	}

	public Vector getIncapacidades() 
	{
		return m_Incapacidades;
	}

	public Vector getHorasExtras() 
	{
		return m_HorasExtras;
	}
}