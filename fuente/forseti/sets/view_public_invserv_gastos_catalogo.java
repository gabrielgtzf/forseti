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

public class view_public_invserv_gastos_catalogo
{
	private String m_ID_Tipo;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Linea;
	private float m_UltimoCosto;
	private String m_ID_Unidad;
	private float m_IVA;
	private float m_CostoPromedio;

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

	public void setLinea(String Linea)
	{
		m_Linea = Linea;
	}

	public void setUltimoCosto(float UltimoCosto)
	{
		m_UltimoCosto = UltimoCosto;
	}

	public void setID_Unidad(String ID_Unidad)
	{
		m_ID_Unidad = ID_Unidad;
	}

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}

	public void setCostoPromedio(float CostoPromedio)
	{
		m_CostoPromedio = CostoPromedio;
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

	public String getLinea()
	{
		return m_Linea;
	}

	public float getUltimoCosto()
	{
		return m_UltimoCosto;
	}

	public String getID_Unidad()
	{
		return m_ID_Unidad;
	}

	public float getIVA()
	{
		return m_IVA;
	}

	public float getCostoPromedio()
	{
		return m_CostoPromedio;
	}


}

