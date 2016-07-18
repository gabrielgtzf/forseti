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
package forseti.sets;

public class tbl_comercio_exterior_cab
{
	private int m_ID_VC;
	private String m_TipoOperacion;
	private String m_ClaveDePedimento;
	private byte m_CertificadoOrigen;
	private String m_NumCertificadoOrigen;
	private String m_NumeroExportadorConfiable;
	private String m_Incoterm;
	private byte m_Subdivision;
	private String m_Observaciones;
	private float m_TipoCambioUsd;
	private float m_TotalUsd;
	private String m_Emisor_Curp;
	private String m_Receptor_Curp;
	private String m_Receptor_NumRegIdTrib;
	private String m_Destinatario_NumRegIdTrib;
	private String m_Destinatario_RFC;
	private String m_Destinatario_Curp;
	private String m_Destinatario_Nombre;
	private String m_Destinatario_Domicilio_Calle;
	private String m_Destinatario_Domicilio_NumeroExterior;
	private String m_Destinatario_Domicilio_NumeroInterior;
	private String m_Destinatario_Domicilio_Colonia;
	private String m_Destinatario_Domicilio_Localidad;
	private String m_Destinatario_Domicilio_Referencia;
	private String m_Destinatario_Domicilio_Municipio;
	private String m_Destinatario_Domicilio_Estado;
	private String m_Destinatario_Domicilio_Pais;
	private String m_Destinatario_Domicilio_CodigoPostal;

	public void setID_VC(int ID_VC)
	{
		m_ID_VC = ID_VC;
	}

	public void setTipoOperacion(String TipoOperacion)
	{
		m_TipoOperacion = TipoOperacion;
	}

	public void setClaveDePedimento(String ClaveDePedimento)
	{
		m_ClaveDePedimento = ClaveDePedimento;
	}

	public void setCertificadoOrigen(byte CertificadoOrigen)
	{
		m_CertificadoOrigen = CertificadoOrigen;
	}

	public void setNumCertificadoOrigen(String NumCertificadoOrigen)
	{
		m_NumCertificadoOrigen = NumCertificadoOrigen;
	}

	public void setNumeroExportadorConfiable(String NumeroExportadorConfiable)
	{
		m_NumeroExportadorConfiable = NumeroExportadorConfiable;
	}

	public void setIncoterm(String Incoterm)
	{
		m_Incoterm = Incoterm;
	}

	public void setSubdivision(byte Subdivision)
	{
		m_Subdivision = Subdivision;
	}

	public void setObservaciones(String Observaciones)
	{
		m_Observaciones = Observaciones;
	}

	public void setTipoCambioUsd(float TipoCambioUsd)
	{
		m_TipoCambioUsd = TipoCambioUsd;
	}

	public void setTotalUsd(float TotalUsd)
	{
		m_TotalUsd = TotalUsd;
	}

	public void setEmisor_Curp(String Emisor_Curp)
	{
		m_Emisor_Curp = Emisor_Curp;
	}

	public void setReceptor_Curp(String Receptor_Curp)
	{
		m_Receptor_Curp = Receptor_Curp;
	}

	public void setReceptor_NumRegIdTrib(String Receptor_NumRegIdTrib)
	{
		m_Receptor_NumRegIdTrib = Receptor_NumRegIdTrib;
	}

	public void setDestinatario_NumRegIdTrib(String Destinatario_NumRegIdTrib)
	{
		m_Destinatario_NumRegIdTrib = Destinatario_NumRegIdTrib;
	}

	public void setDestinatario_RFC(String Destinatario_RFC)
	{
		m_Destinatario_RFC = Destinatario_RFC;
	}

	public void setDestinatario_Curp(String Destinatario_Curp)
	{
		m_Destinatario_Curp = Destinatario_Curp;
	}

	public void setDestinatario_Nombre(String Destinatario_Nombre)
	{
		m_Destinatario_Nombre = Destinatario_Nombre;
	}

	public void setDestinatario_Domicilio_Calle(String Destinatario_Domicilio_Calle)
	{
		m_Destinatario_Domicilio_Calle = Destinatario_Domicilio_Calle;
	}

	public void setDestinatario_Domicilio_NumeroExterior(String Destinatario_Domicilio_NumeroExterior)
	{
		m_Destinatario_Domicilio_NumeroExterior = Destinatario_Domicilio_NumeroExterior;
	}

	public void setDestinatario_Domicilio_NumeroInterior(String Destinatario_Domicilio_NumeroInterior)
	{
		m_Destinatario_Domicilio_NumeroInterior = Destinatario_Domicilio_NumeroInterior;
	}

	public void setDestinatario_Domicilio_Colonia(String Destinatario_Domicilio_Colonia)
	{
		m_Destinatario_Domicilio_Colonia = Destinatario_Domicilio_Colonia;
	}

	public void setDestinatario_Domicilio_Localidad(String Destinatario_Domicilio_Localidad)
	{
		m_Destinatario_Domicilio_Localidad = Destinatario_Domicilio_Localidad;
	}

	public void setDestinatario_Domicilio_Referencia(String Destinatario_Domicilio_Referencia)
	{
		m_Destinatario_Domicilio_Referencia = Destinatario_Domicilio_Referencia;
	}

	public void setDestinatario_Domicilio_Municipio(String Destinatario_Domicilio_Municipio)
	{
		m_Destinatario_Domicilio_Municipio = Destinatario_Domicilio_Municipio;
	}

	public void setDestinatario_Domicilio_Estado(String Destinatario_Domicilio_Estado)
	{
		m_Destinatario_Domicilio_Estado = Destinatario_Domicilio_Estado;
	}

	public void setDestinatario_Domicilio_Pais(String Destinatario_Domicilio_Pais)
	{
		m_Destinatario_Domicilio_Pais = Destinatario_Domicilio_Pais;
	}

	public void setDestinatario_Domicilio_CodigoPostal(String Destinatario_Domicilio_CodigoPostal)
	{
		m_Destinatario_Domicilio_CodigoPostal = Destinatario_Domicilio_CodigoPostal;
	}


	public int getID_VC()
	{
		return m_ID_VC;
	}

	public String getTipoOperacion()
	{
		return m_TipoOperacion;
	}

	public String getClaveDePedimento()
	{
		return m_ClaveDePedimento;
	}

	public byte getCertificadoOrigen()
	{
		return m_CertificadoOrigen;
	}

	public String getNumCertificadoOrigen()
	{
		return m_NumCertificadoOrigen;
	}

	public String getNumeroExportadorConfiable()
	{
		return m_NumeroExportadorConfiable;
	}

	public String getIncoterm()
	{
		return m_Incoterm;
	}

	public byte getSubdivision()
	{
		return m_Subdivision;
	}

	public String getObservaciones()
	{
		return m_Observaciones;
	}

	public float getTipoCambioUsd()
	{
		return m_TipoCambioUsd;
	}

	public float getTotalUsd()
	{
		return m_TotalUsd;
	}

	public String getEmisor_Curp()
	{
		return m_Emisor_Curp;
	}

	public String getReceptor_Curp()
	{
		return m_Receptor_Curp;
	}

	public String getReceptor_NumRegIdTrib()
	{
		return m_Receptor_NumRegIdTrib;
	}

	public String getDestinatario_NumRegIdTrib()
	{
		return m_Destinatario_NumRegIdTrib;
	}

	public String getDestinatario_RFC()
	{
		return m_Destinatario_RFC;
	}

	public String getDestinatario_Curp()
	{
		return m_Destinatario_Curp;
	}

	public String getDestinatario_Nombre()
	{
		return m_Destinatario_Nombre;
	}

	public String getDestinatario_Domicilio_Calle()
	{
		return m_Destinatario_Domicilio_Calle;
	}

	public String getDestinatario_Domicilio_NumeroExterior()
	{
		return m_Destinatario_Domicilio_NumeroExterior;
	}

	public String getDestinatario_Domicilio_NumeroInterior()
	{
		return m_Destinatario_Domicilio_NumeroInterior;
	}

	public String getDestinatario_Domicilio_Colonia()
	{
		return m_Destinatario_Domicilio_Colonia;
	}

	public String getDestinatario_Domicilio_Localidad()
	{
		return m_Destinatario_Domicilio_Localidad;
	}

	public String getDestinatario_Domicilio_Referencia()
	{
		return m_Destinatario_Domicilio_Referencia;
	}

	public String getDestinatario_Domicilio_Municipio()
	{
		return m_Destinatario_Domicilio_Municipio;
	}

	public String getDestinatario_Domicilio_Estado()
	{
		return m_Destinatario_Domicilio_Estado;
	}

	public String getDestinatario_Domicilio_Pais()
	{
		return m_Destinatario_Domicilio_Pais;
	}

	public String getDestinatario_Domicilio_CodigoPostal()
	{
		return m_Destinatario_Domicilio_CodigoPostal;
	}


}

