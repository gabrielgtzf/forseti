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


public class view_cont_polizas_tipos
{
	private String m_ID_Tipo;
	private long m_Numero;
	private String m_Formato;
	private String m_Descripcion;

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setNumero(long Numero)
	{
		m_Numero = Numero;
	}

	public void setFormato(String Formato)
	{
		m_Formato = Formato;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}


	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public long getNumero()
	{
		return m_Numero;
	}

	public String getFormato()
	{
		return m_Formato;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}


}

