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



public class view_reportes_tipos
{
	private String m_Tipo;
	private String m_SubTipo;

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setSubTipo(String SubTipo)
	{
		m_SubTipo = SubTipo;
	}


	public String getTipo()
	{
		return m_Tipo;
	}

	public String getSubTipo()
	{
		return m_SubTipo;
	}


}

