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

public class JInvsServProveeCodigosSet extends JManejadorSet
{
	public JInvsServProveeCodigosSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_invserv_provee_codigos";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_invserv_provee_codigos getRow(int row)
	{
		return (tbl_invserv_provee_codigos)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_invserv_provee_codigos getAbsRow(int row)
	{
		return (tbl_invserv_provee_codigos)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_invserv_provee_codigos pNode = new tbl_invserv_provee_codigos();

			pNode.setID_RFC(m_RS.getString("ID_RFC"));
			pNode.setID_Descripcion(m_RS.getString("ID_Descripcion"));
			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setID_Moneda(m_RS.getByte("ID_Moneda"));
			pNode.setPrecio(m_RS.getFloat("Precio"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
