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


public class TBL_CATEGORIAS
{
	private byte m_ID_Categoria;
	private String m_Descripcion;
	private float m_Sueldo;
	private float m_SueldoAM;
	private float m_IntegradoAM;
	private float m_Vales;
	private float m_ValesAM;

	public void setID_Categoria(byte ID_Categoria)
	{
		m_ID_Categoria = ID_Categoria;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setSueldo(float Sueldo)
	{
		m_Sueldo = Sueldo;
	}

	public void setSueldoAM(float SueldoAM)
	{
		m_SueldoAM = SueldoAM;
	}

	public void setIntegradoAM(float IntegradoAM)
	{
		m_IntegradoAM = IntegradoAM;
	}

	public void setVales(float Vales)
	{
		m_Vales = Vales;
	}

	public void setValesAM(float ValesAM)
	{
		m_ValesAM = ValesAM;
	}


	public byte getID_Categoria()
	{
		return m_ID_Categoria;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getSueldo()
	{
		return m_Sueldo;
	}

	public float getSueldoAM()
	{
		return m_SueldoAM;
	}

	public float getIntegradoAM()
	{
		return m_IntegradoAM;
	}

	public float getVales()
	{
		return m_Vales;
	}

	public float getValesAM()
	{
		return m_ValesAM;
	}


}

