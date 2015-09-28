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



public class view_movimientos
{
	private int m_ID_Movimiento;
	private String m_Descripcion;
	private int m_ID_Sistema;
	private String m_Nombre;
	private boolean m_DC;
	private int m_AplicaAlTipo;
	private boolean m_PorEmpleado;

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_Sistema(int ID_Sistema)
	{
		m_ID_Sistema = ID_Sistema;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setDC(boolean DC)
	{
		m_DC = DC;
	}

	public void setAplicaAlTipo(int AplicaAlTipo)
	{
		m_AplicaAlTipo = AplicaAlTipo;
	}

	public void setPorEmpleado(boolean PorEmpleado)
	{
		m_PorEmpleado = PorEmpleado;
	}


	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public int getID_Sistema()
	{
		return m_ID_Sistema;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public boolean getDC()
	{
		return m_DC;
	}

	public int getAplicaAlTipo()
	{
		return m_AplicaAlTipo;
	}

	public boolean getPorEmpleado()
	{
		return m_PorEmpleado;
	}


}

