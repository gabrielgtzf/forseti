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

public class view_ventas_clientes_vs_descuentos
{
	private String m_ID_Prod;
	private int m_ID_Client;
	private String m_Nombre;
	private Date m_Fecha;
	private float m_Descuento;
	private float m_Descuento2;
	private float m_Descuento3;
	private float m_Descuento4;
	private float m_Descuento5;
	
	private String m_Unidad;
	
	public String getUnidad() 
	{
		return m_Unidad;
	}

	public void setUnidad(String Unidad) 
	{
		m_Unidad = Unidad;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setID_Client(int ID_Client)
	{
		m_ID_Client = ID_Client;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setDescuento(float Descuento)
	{
		m_Descuento = Descuento;
	}

	public void setDescuento2(float Descuento)
	{
		m_Descuento2 = Descuento;
	}
	
	public void setDescuento3(float Descuento)
	{
		m_Descuento3 = Descuento;
	}
	
	public void setDescuento4(float Descuento)
	{
		m_Descuento4 = Descuento;
	}
	
	public void setDescuento5(float Descuento)
	{
		m_Descuento5 = Descuento;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public int getID_Client()
	{
		return m_ID_Client;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public float getDescuento()
	{
		return m_Descuento;
	}

	public float getDescuento2() 
	{
		return m_Descuento2;
	}

	public float getDescuento3() 
	{
		return m_Descuento3;
	}
	
	public float getDescuento4() 
	{
		return m_Descuento4;
	}
	
	public float getDescuento5() 
	{
		return m_Descuento5;
	}

}

