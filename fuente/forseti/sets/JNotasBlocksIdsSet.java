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

public class JNotasBlocksIdsSet extends JManejadorSet
{
	public JNotasBlocksIdsSet(HttpServletRequest request) 
	{
		m_Select = " * FROM view_notas_blocks_ids";
		m_PageSize = 50;
		this.request = request;
	}
	
	public JNotasBlocksIdsSet(HttpServletRequest request, String usuario, String entidad) 
	{
		m_Select = " * FROM view_notas_blocks_ids";
		String sql = "select * from view_notas_blocks_ids('" + usuario + "','" + entidad + 
		"') as ( " +
		"id_usuario varchar, id_block smallint, etiqueta varchar, gu_workarea character(32) )";
		setSQL(sql);
		m_PageSize = 50;
		this.request = request;
	}

	public view_notas_blocks_ids getRow(int row)
	{
		return (view_notas_blocks_ids)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_notas_blocks_ids getAbsRow(int row)
	{
		return (view_notas_blocks_ids)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_notas_blocks_ids pNode = new view_notas_blocks_ids();

			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setID_Block(m_RS.getShort("ID_Block"));
			pNode.setEtiqueta(m_RS.getString("Etiqueta"));
			pNode.setGU_Workarea(m_RS.getString("GU_Workarea"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
