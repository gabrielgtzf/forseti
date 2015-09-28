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

public class view_asistencias_chequeos
{
	private String m_Compania;
	private Date m_ID_Fecha;
	private String m_ID_Empleado;
	private String m_Nombre;
	private byte m_ID_Sucursal;
	private Time m_ID_Hora;

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setID_Fecha(Date ID_Fecha)
	{
		m_ID_Fecha = ID_Fecha;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}


	public String getCompania()
	{
		return m_Compania;
	}

	public Date getID_Fecha()
	{
		return m_ID_Fecha;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public void setID_Sucursal(byte ID_Sucursal) 
	{
		m_ID_Sucursal = ID_Sucursal;
	}

	public byte getID_Sucursal() 
	{
		return m_ID_Sucursal;
	}

	public void setID_Hora(Time ID_Hora) 
	{
		m_ID_Hora = ID_Hora;
	}
	
	public Time getID_Hora() 
	{
		return m_ID_Hora;
	}
}

