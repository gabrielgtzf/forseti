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

public class VIEW_FONACOT
{
	private String m_ID_Credito;
	private String m_ID_Empleado;
	private String m_Nombre;
	private Date m_Fecha;
	private byte m_Meses;
	private byte m_Plazo;
	private float m_Importe;
	private float m_Retencion;
	private float m_Descuentos;
	
	public void setID_Credito(String ID_Credito)
	{
		m_ID_Credito = ID_Credito;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setMeses(byte Meses)
	{
		m_Meses = Meses;
	}

	public void setPlazo(byte Plazo)
	{
		m_Plazo = Plazo;
	}

	public void setImporte(float Importe)
	{
		m_Importe = Importe;
	}

	public void setRetencion(float Retencion)
	{
		m_Retencion = Retencion;
	}


	public String getID_Credito()
	{
		return m_ID_Credito;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}    

	public String getNombre()
	{
		return m_Nombre;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public byte getMeses()
	{
		return m_Meses;
	}

	public byte getPlazo()
	{
		return m_Plazo;
	}

	public float getImporte()
	{
		return m_Importe;
	}

	public float getRetencion()
	{
		return m_Retencion;
	}

	public float getDescuentos() 
	{
		return m_Descuentos;
	}

	public void setDescuentos(float Descuentos) 
	{
		m_Descuentos = Descuentos;
	}


}

