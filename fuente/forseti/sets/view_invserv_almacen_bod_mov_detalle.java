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

public class view_invserv_almacen_bod_mov_detalle
{
	private long m_ID_Movimiento;
	private int m_Partida;
	private String m_ID_Prod;
	private String m_Descripcion;
	private float m_Cantidad;
	private String m_Unidad;

	public void setID_Movimiento(long ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setPartida(int Partida)
	{
		m_Partida = Partida;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}


	public long getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public int getPartida()
	{
		return m_Partida;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}


}

