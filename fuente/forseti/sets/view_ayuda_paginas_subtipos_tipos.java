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


public class view_ayuda_paginas_subtipos_tipos
{
	private String m_ID_Pagina;
	private String m_ID_SubTipo;
	private String m_ID_Tipo;
	private String m_Descripcion;
	private int m_Status;

	public void setID_Pagina(String ID_Pagina)
	{
		m_ID_Pagina = ID_Pagina;
	}

	public void setID_SubTipo(String ID_SubTipo)
	{
		m_ID_SubTipo = ID_SubTipo;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setStatus(int Status)
	{
		m_Status = Status;
	}


	public String getID_Pagina()
	{
		return m_ID_Pagina;
	}

	public String getID_SubTipo()
	{
		return m_ID_SubTipo;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public int getStatus()
	{
		return m_Status;
	}


}

