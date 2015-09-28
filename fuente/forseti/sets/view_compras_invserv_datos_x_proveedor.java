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

public class view_compras_invserv_datos_x_proveedor
{
	private String m_ID_Prod;
	private long m_ID_Provee;
	private float m_Precio;
	private byte m_Moneda;

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setID_Provee(long ID_Provee)
	{
		m_ID_Provee = ID_Provee;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}


	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public long getID_Provee()
	{
		return m_ID_Provee;
	}

	public float getPrecio()
	{
		return m_Precio;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}


}

