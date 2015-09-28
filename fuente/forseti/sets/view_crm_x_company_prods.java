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

public class view_crm_x_company_prods
{
	private String m_GU_company;
	private String m_ID_legal;
	private String m_NM_legal;
	private String m_ID_linea;
	private String m_ID_invserv;
	private String m_Descripcion;

	public void setGU_company(String GU_company)
	{
		m_GU_company = GU_company;
	}

	public void setID_legal(String ID_legal)
	{
		m_ID_legal = ID_legal;
	}

	public void setNM_legal(String NM_legal)
	{
		m_NM_legal = NM_legal;
	}

	public void setID_linea(String ID_linea)
	{
		m_ID_linea = ID_linea;
	}

	public void setID_invserv(String ID_invserv)
	{
		m_ID_invserv = ID_invserv;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}


	public String getGU_company()
	{
		return m_GU_company;
	}

	public String getID_legal()
	{
		return m_ID_legal;
	}

	public String getNM_legal()
	{
		return m_NM_legal;
	}

	public String getID_linea()
	{
		return m_ID_linea;
	}

	public String getID_invserv()
	{
		return m_ID_invserv;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}


}

