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

public class VIEW_NOMINAS
{
	private int m_ID_Nomina;
	private String m_Compania;
	private int m_Ano;
	private String m_Tipo_Nomina;
	private int m_Numero_Nomina;
	private Date m_Fecha_Desde;
	private Date m_Fecha_Hasta;
	private byte m_Dias;
	private String m_Proteccion;

	public void setID_Nomina(int ID_Nomina)
	{
		m_ID_Nomina = ID_Nomina;
	}

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setTipo_Nomina(String Tipo_Nomina)
	{
		m_Tipo_Nomina = Tipo_Nomina;
	}

	public void setNumero_Nomina(int Numero_Nomina)
	{
		m_Numero_Nomina = Numero_Nomina;
	}

	public void setFecha_Desde(Date Fecha_Desde)
	{
		m_Fecha_Desde = Fecha_Desde;
	}

	public void setFecha_Hasta(Date Fecha_Hasta)
	{
		m_Fecha_Hasta = Fecha_Hasta;
	}

	public void setDias(byte Dias)
	{
		m_Dias = Dias;
	}

	public void setProteccion(String Proteccion)
	{
		m_Proteccion = Proteccion;
	}


	public int getID_Nomina()
	{
		return m_ID_Nomina;
	}

	public String getCompania()
	{
		return m_Compania;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public String getTipo_Nomina()
	{
		return m_Tipo_Nomina;
	}

	public int getNumero_Nomina()
	{
		return m_Numero_Nomina;
	}

	public Date getFecha_Desde()
	{
		return m_Fecha_Desde;
	}

	public Date getFecha_Hasta()
	{
		return m_Fecha_Hasta;
	}

	public byte getDias()
	{
		return m_Dias;
	}

	public String getProteccion()
	{
		return m_Proteccion;
	}


}

