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

public class view_invserv_inventarios_vs_client
{
	private int m_ID_Client;
	private String m_ID_Prod;
	private String m_Nombre;
	private Date m_Fecha;
	private float m_Precio;
	private byte m_Moneda;
	private String m_Descripcion;
	private int m_ID_Numero;
	private String m_NombreMoneda;
	private String m_Unidad;

	public void setID_Client(int ID_Client)
	{
		m_ID_Client = ID_Client;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_Numero(int ID_Numero)
	{
		m_ID_Numero = ID_Numero;
	}

	public void setNombreMoneda(String NombreMoneda)
	{
		m_NombreMoneda = NombreMoneda;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}


	public int getID_Client()
	{
		return m_ID_Client;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public float getPrecio()
	{
		return m_Precio;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public int getID_Numero()
	{
		return m_ID_Numero;
	}

	public String getNombreMoneda()
	{
		return m_NombreMoneda;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}


}

