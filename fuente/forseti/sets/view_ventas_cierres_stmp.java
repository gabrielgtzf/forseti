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


public class view_ventas_cierres_stmp
{
	private int m_ID_Cierre;
	private int m_Partida;
	private String m_Clave;
	private String m_Descripcion;
	private float m_Total;

	public void setID_Cierre(int ID_Cierre)
	{
		m_ID_Cierre = ID_Cierre;
	}

	public void setPartida(int Partida)
	{
		m_Partida = Partida;
	}

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}


	public int getID_Cierre()
	{
		return m_ID_Cierre;
	}

	public int getPartida()
	{
		return m_Partida;
	}

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getTotal()
	{
		return m_Total;
	}


}

