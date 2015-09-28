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

import java.sql.*;

public class TBL_BDS
{
	private int m_ID_BD;
	private String m_Nombre;
	private String m_Usuario;
	private String m_Password;
	private Date m_FechaAlta;
	private String m_Compania;
	private String m_Direccion;
	private String m_Poblacion;
	private String m_CP;
	private String m_Mail;
	private String m_Web;
	private String m_SU;
	private String m_RFC;
	private int m_CFD;
	/*private String m_CFD_Serie;
	private int m_CFD_Folio;
	private int m_CFD_FolioINI;
	private int m_CFD_FolioFIN;
	private long m_CFD_NoAprobacion;
	private int m_CFD_AnoAprobacion;
	private String m_CFD_NoCertificado;
	private String m_CFD_ArchivoCertificado;
	private Date m_CFD_CaducidadCertificado;
	private Time m_CFD_HoraCaducidadCertificado;
	private String m_CFD_ArchivoLLave;
	private String m_CFD_ClaveLLave; */
	private String m_CFD_Calle;
	private String m_CFD_NoExt;
	private String m_CFD_NoInt;
	private String m_CFD_Colonia;
	private String m_CFD_Localidad;
	private String m_CFD_Municipio;
	private String m_CFD_Estado;
	private String m_CFD_Pais;
	private String m_CFD_CP;
	private String m_CFD_RegimenFiscal;

	public void setID_BD(int ID_BD)
	{
		m_ID_BD = ID_BD;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setUsuario(String Usuario)
	{
		m_Usuario = Usuario;
	}

	public void setPassword(String Password)
	{
		m_Password = Password;
	}

	public void setFechaAlta(Date FechaAlta)
	{
		m_FechaAlta = FechaAlta;
	}

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setDireccion(String Direccion)
	{
		m_Direccion = Direccion;
	}

	public void setPoblacion(String Poblacion)
	{
		m_Poblacion = Poblacion;
	}

	public void setCP(String CP)
	{
		m_CP = CP;
	}

	public void setMail(String Mail)
	{
		m_Mail = Mail;
	}

	public void setWeb(String Web)
	{
		m_Web = Web;
	}

	public void setSU(String SU)
	{
		m_SU = SU;
	}

	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}

	public void setCFD(int CFD)
	{
		m_CFD = CFD;
	}

/*	public void setCFD_Serie(String CFD_Serie)
	{
		m_CFD_Serie = CFD_Serie;
	}

	public void setCFD_Folio(int CFD_Folio)
	{
		m_CFD_Folio = CFD_Folio;
	}

	public void setCFD_FolioINI(int CFD_FolioINI)
	{
		m_CFD_FolioINI = CFD_FolioINI;
	}

	public void setCFD_FolioFIN(int CFD_FolioFIN)
	{
		m_CFD_FolioFIN = CFD_FolioFIN;
	}

	public void setCFD_NoAprobacion(long CFD_NoAprobacion)
	{
		m_CFD_NoAprobacion = CFD_NoAprobacion;
	}

	public void setCFD_AnoAprobacion(int CFD_AnoAprobacion)
	{
		m_CFD_AnoAprobacion = CFD_AnoAprobacion;
	}

	public void setCFD_NoCertificado(String CFD_NoCertificado)
	{
		m_CFD_NoCertificado = CFD_NoCertificado;
	}

	public void setCFD_ArchivoCertificado(String CFD_ArchivoCertificado)
	{
		m_CFD_ArchivoCertificado = CFD_ArchivoCertificado;
	}

	public void setCFD_CaducidadCertificado(Date CFD_CaducidadCertificado)
	{
		m_CFD_CaducidadCertificado = CFD_CaducidadCertificado;
	}
	
	public void setCFD_HoraCaducidadCertificado(Time CFD_HoraCaducidadCertificado)
	{
		m_CFD_HoraCaducidadCertificado = CFD_HoraCaducidadCertificado;
	}

	public void setCFD_ArchivoLLave(String CFD_ArchivoLLave)
	{
		m_CFD_ArchivoLLave = CFD_ArchivoLLave;
	}

	public void setCFD_ClaveLLave(String CFD_ClaveLLave)
	{
		m_CFD_ClaveLLave = CFD_ClaveLLave;
	} */

	public void setCFD_Calle(String CFD_Calle)
	{
		m_CFD_Calle = CFD_Calle;
	}

	public void setCFD_NoExt(String CFD_NoExt)
	{
		m_CFD_NoExt = CFD_NoExt;
	}

	public void setCFD_NoInt(String CFD_NoInt)
	{
		m_CFD_NoInt = CFD_NoInt;
	}

	public void setCFD_Colonia(String CFD_Colonia)
	{
		m_CFD_Colonia = CFD_Colonia;
	}

	public void setCFD_Localidad(String CFD_Localidad)
	{
		m_CFD_Localidad = CFD_Localidad;
	}

	public void setCFD_Municipio(String CFD_Municipio)
	{
		m_CFD_Municipio = CFD_Municipio;
	}

	public void setCFD_Estado(String CFD_Estado)
	{
		m_CFD_Estado = CFD_Estado;
	}

	public void setCFD_Pais(String CFD_Pais)
	{
		m_CFD_Pais = CFD_Pais;
	}

	public void setCFD_CP(String CFD_CP)
	{
		m_CFD_CP = CFD_CP;
	}


	public int getID_BD()
	{
		return m_ID_BD;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getUsuario()
	{
		return m_Usuario;
	}

	public String getPassword()
	{
		return m_Password;
	}

	public Date getFechaAlta()
	{
		return m_FechaAlta;
	}

	public String getCompania()
	{
		return m_Compania;
	}

	public String getDireccion()
	{
		return m_Direccion;
	}

	public String getPoblacion()
	{
		return m_Poblacion;
	}

	public String getCP()
	{
		return m_CP;
	}

	public String getMail()
	{
		return m_Mail;
	}

	public String getWeb()
	{
		return m_Web;
	}

	public String getSU()
	{
		return m_SU;
	}

	public String getRFC()
	{
		return m_RFC;
	}

	public int getCFD()
	{
		return m_CFD;
	}

/*	public String getCFD_Serie()
	{
		return m_CFD_Serie;
	}

	public int getCFD_Folio()
	{
		return m_CFD_Folio;
	}

	public int getCFD_FolioINI()
	{
		return m_CFD_FolioINI;
	}

	public int getCFD_FolioFIN()
	{
		return m_CFD_FolioFIN;
	}

	public long getCFD_NoAprobacion()
	{
		return m_CFD_NoAprobacion;
	}

	public int getCFD_AnoAprobacion()
	{
		return m_CFD_AnoAprobacion;
	}

	public String getCFD_NoCertificado()
	{
		return m_CFD_NoCertificado;
	}

	public String getCFD_ArchivoCertificado()
	{
		return m_CFD_ArchivoCertificado;
	}

	public Date getCFD_CaducidadCertificado()
	{
		return m_CFD_CaducidadCertificado;
	}

	public Time getCFD_HoraCaducidadCertificado()
	{
		return m_CFD_HoraCaducidadCertificado;
	}

	public String getCFD_ArchivoLLave()
	{
		return m_CFD_ArchivoLLave;
	}

	public String getCFD_ClaveLLave()
	{
		return m_CFD_ClaveLLave;
	}
*/
	public String getCFD_Calle()
	{
		return m_CFD_Calle;
	}

	public String getCFD_NoExt()
	{
		return m_CFD_NoExt;
	}

	public String getCFD_NoInt()
	{
		return m_CFD_NoInt;
	}

	public String getCFD_Colonia()
	{
		return m_CFD_Colonia;
	}

	public String getCFD_Localidad()
	{
		return m_CFD_Localidad;
	}

	public String getCFD_Municipio()
	{
		return m_CFD_Municipio;
	}

	public String getCFD_Estado()
	{
		return m_CFD_Estado;
	}

	public String getCFD_Pais()
	{
		return m_CFD_Pais;
	}

	public String getCFD_CP()
	{
		return m_CFD_CP;
	}

	public void setCFD_RegimenFiscal(String CFD_RegimenFiscal) 
	{
		m_CFD_RegimenFiscal = CFD_RegimenFiscal;	
	}

	public String getCFD_RegimenFiscal() 
	{
		return m_CFD_RegimenFiscal;	
	}
}

