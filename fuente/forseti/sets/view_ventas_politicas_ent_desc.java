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

public class view_ventas_politicas_ent_desc
{
	private int m_ID_Entidad;
	private String m_Clave;
	private String m_Descripcion;
	private float m_P1;
	private float m_P2;
	private float m_P3;
	private float m_P4;
	private float m_P5;
	private byte m_Aplicacion;

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setP1(float P1)
	{
		m_P1 = P1;
	}

	public void setP2(float P2)
	{
		m_P2 = P2;
	}

	public void setP3(float P3)
	{
		m_P3 = P3;
	}

	public void setP4(float P4)
	{
		m_P4 = P4;
	}

	public void setP5(float P5)
	{
		m_P5 = P5;
	}

	public void setAplicacion(byte Aplicacion)
	{
		m_Aplicacion = Aplicacion;
	}


	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getP1()
	{
		return m_P1;
	}

	public float getP2()
	{
		return m_P2;
	}

	public float getP3()
	{
		return m_P3;
	}

	public float getP4()
	{
		return m_P4;
	}

	public float getP5()
	{
		return m_P5;
	}

	public byte getAplicacion()
	{
		return m_Aplicacion;
	}


}

