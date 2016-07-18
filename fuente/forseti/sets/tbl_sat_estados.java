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

public class tbl_sat_estados
{
	private String m_CodPais2;
	private String m_CodPais3;
	private int m_CodPaisNum;
	private String m_CodEstado;
	private String m_Nombre;

	public void setCodPais2(String CodPais2)
	{
		m_CodPais2 = CodPais2;
	}

	public void setCodPais3(String CodPais3)
	{
		m_CodPais3 = CodPais3;
	}

	public void setCodPaisNum(int CodPaisNum)
	{
		m_CodPaisNum = CodPaisNum;
	}

	public void setCodEstado(String CodEstado)
	{
		m_CodEstado = CodEstado;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}


	public String getCodPais2()
	{
		return m_CodPais2;
	}

	public String getCodPais3()
	{
		return m_CodPais3;
	}

	public int getCodPaisNum()
	{
		return m_CodPaisNum;
	}

	public String getCodEstado()
	{
		return m_CodEstado;
	}

	public String getNombre()
	{
		return m_Nombre;
	}


}

