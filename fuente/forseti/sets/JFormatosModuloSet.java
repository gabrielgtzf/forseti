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

public class JFormatosModuloSet extends JManejadorSet
{
	public JFormatosModuloSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_FORMATOS_MODULO";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_FORMATOS_MODULO getRow(int row)
	{
		return (VIEW_FORMATOS_MODULO)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_FORMATOS_MODULO getAbsRow(int row)
	{
		return (VIEW_FORMATOS_MODULO)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_FORMATOS_MODULO pNode = new VIEW_FORMATOS_MODULO();

			pNode.setID_Formato(m_RS.getString("ID_Formato"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setTipo_Desc(m_RS.getString("Tipo_Desc"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
