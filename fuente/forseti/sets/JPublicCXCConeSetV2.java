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

public class JPublicCXCConeSetV2 extends JManejadorSet
{
	public JPublicCXCConeSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_public_client_cxc_conceptos_cat ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_public_client_cxc_conceptos_cat getRow(int row)
	{
		return (view_public_client_cxc_conceptos_cat)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_public_client_cxc_conceptos_cat getAbsRow(int row)
	{
		return (view_public_client_cxc_conceptos_cat)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_public_client_cxc_conceptos_cat pNode = new view_public_client_cxc_conceptos_cat();

			pNode.setID_Concepto(m_RS.getShort("ID_Concepto"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setDeSistema(m_RS.getBoolean("DeSistema"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setCC(m_RS.getString("CC"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
