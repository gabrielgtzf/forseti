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

import java.sql.Date;
import java.sql.Time;

public class TBL_CFD_CERTIFICADOS 
{
	private String m_CFD_NoCertificado;
	private String m_CFD_ArchivoCertificado;
	private Date m_CFD_CaducidadCertificado;
	private Time m_CFD_HoraCaducidadCertificado;
	private String m_CFD_ArchivoLLave;
	private String m_CFD_ClaveLLave;
	
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

}
