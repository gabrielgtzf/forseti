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

public class VIEW_PLANTILLAS
{
	private int m_ID_Plantilla;
	private String m_Compania;
	private Date m_Fecha;
	private int m_ID_Movimiento;
	private String m_ID_Empleado;
	private String m_Movimiento;
	private String m_Descripcion;
	private String m_Aplicacion;

	public void setID_Plantilla(int ID_Plantilla)
	{
		m_ID_Plantilla = ID_Plantilla;
	}

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setMovimiento(String Movimiento)
	{
		m_Movimiento = Movimiento;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setAplicacion(String Aplicacion)
	{
		m_Aplicacion = Aplicacion;
	}


	public int getID_Plantilla()
	{
		return m_ID_Plantilla;
	}

	public String getCompania()
	{
		return m_Compania;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getMovimiento()
	{
		return m_Movimiento;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getAplicacion()
	{
		return m_Aplicacion;
	}


}

