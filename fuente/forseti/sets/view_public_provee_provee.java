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



public class view_public_provee_provee
{
	private String m_ID_Tipo;
	private long m_ID_Clave;
	private String m_Nombre;
	private float m_Descuento;

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Clave(long ID_Clave)
	{
		m_ID_Clave = ID_Clave;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setDescuento(float Descuento)
	{
		m_Descuento = Descuento;
	}


	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public long getID_Clave()
	{
		return m_ID_Clave;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public float getDescuento()
	{
		return m_Descuento;
	}


}

