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

public class tbl_sat_codagrup
{
	private String m_Codigo;
	private String m_Descripcion;
	private byte m_Nivel;

	public void setCodigo(String Codigo)
	{
		m_Codigo = Codigo;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setNivel(byte Nivel)
	{
		m_Nivel = Nivel;
	}


	public String getCodigo()
	{
		return m_Codigo;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public byte getNivel()
	{
		return m_Nivel;
	}


}

