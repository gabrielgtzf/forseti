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


public class view_public_cont_monedas
{
	private int m_Clave;
	private String m_Moneda;
	private String m_Simbolo;
	private String m_TC;

	public void setClave(int Clave)
	{
		m_Clave = Clave;
	}

	public void setMoneda(String Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setSimbolo(String Simbolo)
	{
		m_Simbolo = Simbolo;
	}

	public void setTC(String TC)
	{
		m_TC = TC;
	}


	public int getClave()
	{
		return m_Clave;
	}

	public String getMoneda()
	{
		return m_Moneda;
	}

	public String getSimbolo()
	{
		return m_Simbolo;
	}

	public String getTC()
	{
		return m_TC;
	}


}

