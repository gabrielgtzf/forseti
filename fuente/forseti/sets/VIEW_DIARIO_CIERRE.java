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

public class VIEW_DIARIO_CIERRE
{
	private Date m_ID_FechaMovimiento;
	private String m_ID_Empleado;
	private String m_Nombre;
	private int m_ID_Movimiento;
	private String m_Descripcion;
	private Date m_Desde;
	private Date m_Hasta;
	private Date m_Entrada;
	private Date m_Salida;
	private float m_HNA;
	private float m_HNP;
	private Time m_DesdeHora;
	private Time m_HastaHora;
	private Time m_EntradaHora;
	private Time m_SalidaHora;
	private Time m_Entrada2Hora;
	private Time m_Salida2Hora;
	private Date m_Entrada2;
	private Date m_Salida2;

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

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
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

	public void setEntrada(Date Entrada)
	{
		m_Entrada = Entrada;
	}

	public void setSalida(Date Salida)
	{
		m_Salida = Salida;
	}

	public void setEntrada2(Date Entrada2)
	{
		m_Entrada2 = Entrada2;
	}

	public void setSalida2(Date Salida2)
	{
		m_Salida2 = Salida2;
	}
	
	public void setHNA(float HNA)
	{
		m_HNA = HNA;
	}

	public void setHNP(float HNP)
	{
		m_HNP = HNP;
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

	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
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

	public Date getEntrada()
	{
		return m_Entrada;
	}

	public Date getSalida()
	{
		return m_Salida;
	}

	public Date getEntrada2()
	{
		return m_Entrada2;
	}

	public Date getSalida2()
	{
		return m_Salida2;
	}
	
	public float getHNA()
	{
		return m_HNA;
	}

	public float getHNP()
	{
		return m_HNP;
	}

	public Time getDesdeHora()
	{
		return m_DesdeHora;
	}

	public Time getHastaHora()
	{
		return m_HastaHora;
	}

	public Time getEntradaHora()
	{
		return m_EntradaHora;
	}

	public Time getSalidaHora()
	{
		return m_SalidaHora;
	}
	
	public Time getEntrada2Hora()
	{
		return m_Entrada2Hora;
	}

	public Time getSalida2Hora()
	{
		return m_Salida2Hora;
	}
	
	public void setDesdeHora(Time DesdeHora)
	{
		m_DesdeHora = DesdeHora;
	}

	public void setHastaHora(Time HastaHora)
	{
		m_HastaHora = HastaHora;
	}

	public void setEntradaHora(Time EntradaHora)
	{
		m_EntradaHora = EntradaHora;
	}

	public void setSalidaHora(Time SalidaHora)
	{
		m_SalidaHora = SalidaHora;
	}

	public void setEntrada2Hora(Time Entrada2Hora)
	{
		m_Entrada2Hora = Entrada2Hora;
	}

	public void setSalida2Hora(Time Salida2Hora)
	{
		m_Salida2Hora = Salida2Hora;
	}
	
}

