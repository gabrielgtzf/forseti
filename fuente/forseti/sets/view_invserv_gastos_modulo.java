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


public class view_invserv_gastos_modulo
{
	private String m_ID_Tipo;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Categoria;
	private String m_Unidad;
	private float m_Existencia;

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setCategoria(String Categoria)
	{
		m_Categoria = Categoria;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setExistencia(float Existencia)
	{
		m_Existencia = Existencia;
	}


	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getCategoria()
	{
		return m_Categoria;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public float getExistencia()
	{
		return m_Existencia;
	}


}

