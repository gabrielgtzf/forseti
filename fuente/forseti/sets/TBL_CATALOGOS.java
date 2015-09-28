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


public class TBL_CATALOGOS
{
	private int m_ID_Catalogo;
	private String m_Nombre;
	private String m_Select_Clause;
	private String m_PriDefault;
	private String m_SecDefault;
	private String m_Seguridad;
	private boolean m_AplRep;

	public void setID_Catalogo(int ID_Catalogo)
	{
		m_ID_Catalogo = ID_Catalogo;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setSelect_Clause(String Select_Clause)
	{
		m_Select_Clause = Select_Clause;
	}

	public void setPriDefault(String PriDefault)
	{
		m_PriDefault = PriDefault;
	}

	public void setSecDefault(String SecDefault)
	{
		m_SecDefault = SecDefault;
	}

	public void setSeguridad(String Seguridad)
	{
		m_Seguridad = Seguridad;
	}

	public void setAplRep(boolean AplRep)
	{
		m_AplRep = AplRep;
	}


	public int getID_Catalogo()
	{
		return m_ID_Catalogo;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getSelect_Clause()
	{
		return m_Select_Clause;
	}

	public String getPriDefault()
	{
		return m_PriDefault;
	}

	public String getSecDefault()
	{
		return m_SecDefault;
	}

	public String getSeguridad()
	{
		return m_Seguridad;
	}

	public boolean getAplRep()
	{
		return m_AplRep;
	}


}

