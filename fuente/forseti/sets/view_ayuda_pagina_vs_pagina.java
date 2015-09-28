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

public class view_ayuda_pagina_vs_pagina
{
	private String m_ID_Pagina;
	private String m_ID_Enlace;
	private String m_Descripcion;
	private boolean m_Enlazado;

	public void setID_Pagina(String ID_Pagina)
	{
		m_ID_Pagina = ID_Pagina;
	}

	public void setID_Enlace(String ID_Enlace)
	{
		m_ID_Enlace = ID_Enlace;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setEnlazado(boolean Enlazado)
	{
		m_Enlazado = Enlazado;
	}


	public String getID_Pagina()
	{
		return m_ID_Pagina;
	}

	public String getID_Enlace()
	{
		return m_ID_Enlace;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public boolean getEnlazado()
	{
		return m_Enlazado;
	}


}

