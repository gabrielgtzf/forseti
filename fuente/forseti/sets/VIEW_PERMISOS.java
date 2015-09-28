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

public class VIEW_PERMISOS
{
	private byte m_ID_Compania;
	private byte m_ID_Sucursal;
	private String m_ID_Empleado;
	private int m_ID_Movimiento;
	private Date m_ID_FechaMovimiento;
	private String m_Nombre;
	private String m_Descripcion;
	private Date m_Desde;
	private Date m_Hasta;
	private boolean m_DiasCompletos;
	private int m_Num_de_Dias;
	private float m_Num_de_Horas;
	private float m_Tiempo_por_pagar;
	private String m_Obs;
	private Time m_HoraDesde;
	private Time m_HoraHasta;
	
	public String getObs() 
	{
		return m_Obs;
	}

	public void setObs(String Obs) 
	{
		m_Obs = Obs;
	}

	public int getNum_de_Dias() 
	{
		return m_Num_de_Dias;
	}

	public void setNum_de_Dias(int Num_de_Dias) 
	{
		m_Num_de_Dias = Num_de_Dias;
	}

	public float getNum_de_Horas() 
	{
		return m_Num_de_Horas;
	}

	public void setNum_de_Horas(float Num_de_Horas) 
	{
		m_Num_de_Horas = Num_de_Horas;
	}

	public float getTiempo_por_pagar() 
	{
		return m_Tiempo_por_pagar;
	}

	public void setTiempo_por_pagar(float Tiempo_por_pagar) 
	{
		m_Tiempo_por_pagar = Tiempo_por_pagar;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setID_FechaMovimiento(Date ID_FechaMovimiento)
	{
		m_ID_FechaMovimiento = ID_FechaMovimiento;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setDesde(Date Desde)
	{
		m_Desde = Desde;
	}

	public void setHasta(Date Hasta)
	{
		m_Hasta = Hasta;
	}


	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public Date getID_FechaMovimiento()
	{
		return m_ID_FechaMovimiento;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public Date getDesde()
	{
		return m_Desde;
	}

	public Date getHasta()
	{
		return m_Hasta;
	}

	public byte getID_Compania() 
	{
		return m_ID_Compania;
	}

	public void setID_Compania(byte ID_Compania) 
	{
		m_ID_Compania = ID_Compania;
	}

	public byte getID_Sucursal() 
	{
		return m_ID_Sucursal;
	}

	public void setID_Sucursal(byte ID_Sucursal) 
	{
		m_ID_Sucursal = ID_Sucursal;
	}

	public boolean getDiasCompletos() 
	{
		return m_DiasCompletos;
	}

	public void setDiasCompletos(boolean DiasCompletos) 
	{
		m_DiasCompletos = DiasCompletos;
	}

	public void setHoraDesde(Time HoraDesde) 
	{
		m_HoraDesde = HoraDesde;
	}

	public void setHoraHasta(Time HoraHasta) 
	{
		m_HoraHasta = HoraHasta;
	}
	
	public Time getHoraDesde() 
	{
		return m_HoraDesde;
	}
	
	public Time getHoraHasta() 
	{
		return m_HoraHasta;
	}
}

