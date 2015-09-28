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


public class view_plantillas_web
{
	private String m_ID_Plantilla;
	private String m_Descripcion;

	public void setID_Plantilla(String ID_Plantilla)
	{
		m_ID_Plantilla = ID_Plantilla;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}


	public String getID_Plantilla()
	{
		return m_ID_Plantilla;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}
}

