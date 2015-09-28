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

public class VIEW_CALCULO_NOMINA
{
	private int m_ID_Nomina;
	private String m_Compania;
	private int m_Ano;
	private byte m_Tipo;
	private int m_Numero_Nomina;
	private Date m_Fecha_Desde;
	private Date m_Fecha_Hasta;
	private String m_ID_Empleado;
	private String m_Nombre;
	private int m_ID_Movimiento;
	private String m_Descripcion;
	private float m_Gravado;
	private float m_Exento;
	private float m_Deduccion;

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

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
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

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setGravado(float Gravado)
	{
		m_Gravado = Gravado;
	}

	public void setExento(float Exento)
	{
		m_Exento = Exento;
	}

	public void setDeduccion(float Deduccion)
	{
		m_Deduccion = Deduccion;
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

	public byte getTipo()
	{
		return m_Tipo;
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

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getGravado()
	{
		return m_Gravado;
	}

	public float getExento()
	{
		return m_Exento;
	}

	public float getDeduccion()
	{
		return m_Deduccion;
	}


}

