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

public class view_invserv_almacen_movim_detalles_utensilios_modulo_exist
{
	private short m_ID_Bodega;
	private float m_Cantidad;
	private String m_ID_Prod;
	private String m_Descripcion;
	private String m_Unidad;
	private String m_Status;
	private String m_ID_Linea;
	private String m_Linea;

	public void setID_Bodega(short ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setID_Linea(String ID_Linea)
	{
		m_ID_Linea = ID_Linea;
	}

	public void setLinea(String Linea)
	{
		m_Linea = Linea;
	}


	public short getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public String getID_Linea()
	{
		return m_ID_Linea;
	}

	public String getLinea()
	{
		return m_Linea;
	}


}

