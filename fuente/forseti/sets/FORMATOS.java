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



public class FORMATOS
{
	private String m_ID_Formato;
	private String m_Descripcion;
	private String m_Tipo;

	public void setID_Formato(String ID_Formato)
	{
		m_ID_Formato = ID_Formato;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}


	public String getID_Formato()
	{
		return m_ID_Formato;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getTipo()
	{
		return m_Tipo;
	}


}

