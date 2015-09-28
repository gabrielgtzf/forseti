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

import forseti.*;
import javax.servlet.http.*;
import java.sql.*;

public class JCRMXCompanyProdsSet extends JManejadorSet
{
	public JCRMXCompanyProdsSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_crm_x_company_prods";
		m_PageSize = 50;
		this.request = request;
	}

	public view_crm_x_company_prods getRow(int row)
	{
		return (view_crm_x_company_prods)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_crm_x_company_prods getAbsRow(int row)
	{
		return (view_crm_x_company_prods)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_crm_x_company_prods pNode = new view_crm_x_company_prods();

			pNode.setGU_company(m_RS.getString("GU_company"));
			pNode.setID_legal(m_RS.getString("ID_legal"));
			pNode.setNM_legal(m_RS.getString("NM_legal"));
			pNode.setID_linea(m_RS.getString("ID_linea"));
			pNode.setID_invserv(m_RS.getString("ID_invserv"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
