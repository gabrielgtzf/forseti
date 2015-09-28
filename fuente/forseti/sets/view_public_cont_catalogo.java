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


public class view_public_cont_catalogo
{
	private boolean m_Acum;
	private String m_Nombre;
	private String m_Numero;
	private String m_Saldo;

	public void setAcum(boolean Acum)
	{
		m_Acum = Acum;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setNumero(String Numero)
	{
		m_Numero = Numero;
	}

	public void setSaldo(String Saldo)
	{
		m_Saldo = Saldo;
	}


	public boolean getAcum()
	{
		return m_Acum;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getNumero()
	{
		return m_Numero;
	}

	public String getSaldo()
	{
		return m_Saldo;
	}


}

