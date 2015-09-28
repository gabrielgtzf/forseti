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

public class view_plantillas_modulo
{
	private int m_ID_Plantilla;
	private boolean m_bID_Empleado;
	private boolean m_bNomina;
	private boolean m_bTipo_Nomina;
	private boolean m_bCompania_Sucursal;
	private String m_Compania;
	private Date m_Fecha;
	private int m_ID_Movimiento;
	private String m_ID_Empleado;
	private String m_Movimiento;
	private String m_Descripcion;
	private String m_Aplicacion;
	private boolean m_Calcular;
	
	public void setID_Plantilla(int ID_Plantilla)
	{
		m_ID_Plantilla = ID_Plantilla;
	}

	public void setbID_Empleado(boolean bID_Empleado)
	{
		m_bID_Empleado = bID_Empleado;
	}

	public void setbNomina(boolean bNomina)
	{
		m_bNomina = bNomina;
	}

	public void setbTipo_Nomina(boolean bTipo_Nomina)
	{
		m_bTipo_Nomina = bTipo_Nomina;
	}

	public void setbCompania_Sucursal(boolean bCompania_Sucursal)
	{
		m_bCompania_Sucursal = bCompania_Sucursal;
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

	public boolean getbID_Empleado()
	{
		return m_bID_Empleado;
	}

	public boolean getbNomina()
	{
		return m_bNomina;
	}

	public boolean getbTipo_Nomina()
	{
		return m_bTipo_Nomina;
	}

	public boolean getbCompania_Sucursal()
	{
		return m_bCompania_Sucursal;
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

	public void setCalcular(boolean Calcular) 
	{
		m_Calcular = Calcular;
	}

	public boolean getCalcular()
	{
		return m_Calcular;
	}
}

