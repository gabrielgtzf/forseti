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

public class view_gastos_invserv_datos_x_acreedor
{
	private long m_ID_Acree;
	private String m_ID_Gasto;
	private byte m_Moneda;
	private float m_Precio;

	public void setID_Acree(long ID_Acree)
	{
		m_ID_Acree = ID_Acree;
	}

	public void setID_Gasto(String ID_Gasto)
	{
		m_ID_Gasto = ID_Gasto;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}


	public long getID_Acree()
	{
		return m_ID_Acree;
	}

	public String getID_Gasto()
	{
		return m_ID_Gasto;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public float getPrecio()
	{
		return m_Precio;
	}


}

