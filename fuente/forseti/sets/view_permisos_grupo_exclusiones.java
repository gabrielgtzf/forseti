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

public class view_permisos_grupo_exclusiones
{
	private byte m_ID_Compania;
	private byte m_ID_Sucursal;
	private int m_ID_Movimiento;
	private Date m_ID_FechaMovimiento;
	private String m_ID_Empleado;
	private String m_Nombre;

	public void setID_Compania(byte ID_Compania)
	{
		m_ID_Compania = ID_Compania;
	}

	public void setID_Sucursal(byte ID_Sucursal)
	{
		m_ID_Sucursal = ID_Sucursal;
	}

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setID_FechaMovimiento(Date ID_FechaMovimiento)
	{
		m_ID_FechaMovimiento = ID_FechaMovimiento;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}


	public byte getID_Compania()
	{
		return m_ID_Compania;
	}

	public byte getID_Sucursal()
	{
		return m_ID_Sucursal;
	}

	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public Date getID_FechaMovimiento()
	{
		return m_ID_FechaMovimiento;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getNombre()
	{
		return m_Nombre;
	}


}

