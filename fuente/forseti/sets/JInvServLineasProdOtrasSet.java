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

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import forseti.JManejadorSet;

public class JInvServLineasProdOtrasSet extends JManejadorSet
{
	public JInvServLineasProdOtrasSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_lineas_prod_otras ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_lineas_prod_otras getRow(int row)
	{
		return (view_invserv_lineas_prod_otras)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_lineas_prod_otras getAbsRow(int row)
	{
		return (view_invserv_lineas_prod_otras)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_lineas_prod_otras pNode = new view_invserv_lineas_prod_otras();

			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setID_Linea(m_RS.getString("ID_Linea"));
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
