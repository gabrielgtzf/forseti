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

public class tbl_invserv_provee_codigos
{
	private String m_ID_RFC;
	private String m_ID_Descripcion;
	private String m_ID_Prod;
	private byte m_ID_Moneda;
	private float m_Precio;

	public void setID_RFC(String ID_RFC)
	{
		m_ID_RFC = ID_RFC;
	}

	public void setID_Descripcion(String ID_Descripcion)
	{
		m_ID_Descripcion = ID_Descripcion;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setID_Moneda(byte ID_Moneda)
	{
		m_ID_Moneda = ID_Moneda;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}


	public String getID_RFC()
	{
		return m_ID_RFC;
	}

	public String getID_Descripcion()
	{
		return m_ID_Descripcion;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public byte getID_Moneda()
	{
		return m_ID_Moneda;
	}

	public float getPrecio()
	{
		return m_Precio;
	}


}

