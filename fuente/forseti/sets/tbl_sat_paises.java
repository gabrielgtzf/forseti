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

public class tbl_sat_paises
{
	private String m_Alfa2;
	private String m_Alfa3;
	private int m_Numerico;
	private String m_Nombre;

	public void setAlfa2(String Alfa2)
	{
		m_Alfa2 = Alfa2;
	}

	public void setAlfa3(String Alfa3)
	{
		m_Alfa3 = Alfa3;
	}

	public void setNumerico(int Numerico)
	{
		m_Numerico = Numerico;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}


	public String getAlfa2()
	{
		return m_Alfa2;
	}

	public String getAlfa3()
	{
		return m_Alfa3;
	}

	public int getNumerico()
	{
		return m_Numerico;
	}

	public String getNombre()
	{
		return m_Nombre;
	}


}

