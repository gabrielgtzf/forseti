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


public class view_ventas_politicas_ent_desc_mod
{
	private int m_ID_Entidad;
	private String m_Clave;
	private String m_Descripcion;
	private String m_P1;
	private String m_P2;
	private String m_P3;
	private String m_P4;
	private String m_P5;
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

	public void setP1(String P1)
	{
		m_P1 = P1;
	}

	public void setP2(String P2)
	{
		m_P2 = P2;
	}

	public void setP3(String P3)
	{
		m_P3 = P3;
	}

	public void setP4(String P4)
	{
		m_P4 = P4;
	}

	public void setP5(String P5)
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

	public String getP1()
	{
		return m_P1;
	}

	public String getP2()
	{
		return m_P2;
	}

	public String getP3()
	{
		return m_P3;
	}

	public String getP4()
	{
		return m_P4;
	}

	public String getP5()
	{
		return m_P5;
	}

	public byte getAplicacion()
	{
		return m_Aplicacion;
	}


}

