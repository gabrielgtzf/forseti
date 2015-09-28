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

public class view_diario_cierre_modulo
{
	private String m_Compania;
	private byte m_ID_Compania;
	private byte m_ID_Sucursal;
	private Date m_ID_FechaMovimiento;
	private boolean m_Cerrado;

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setID_Compania(byte ID_Compania)
	{
		m_ID_Compania = ID_Compania;
	}

	public void setID_Sucursal(byte ID_Sucursal)
	{
		m_ID_Sucursal = ID_Sucursal;
	}

	public void setID_FechaMovimiento(Date ID_FechaMovimiento)
	{
		m_ID_FechaMovimiento = ID_FechaMovimiento;
	}

	public void setCerrado(boolean Cerrado)
	{
		m_Cerrado = Cerrado;
	}


	public String getCompania()
	{
		return m_Compania;
	}

	public byte getID_Compania()
	{
		return m_ID_Compania;
	}

	public byte getID_Sucursal()
	{
		return m_ID_Sucursal;
	}

	public Date getID_FechaMovimiento()
	{
		return m_ID_FechaMovimiento;
	}

	public boolean getCerrado()
	{
		return m_Cerrado;
	}


}

