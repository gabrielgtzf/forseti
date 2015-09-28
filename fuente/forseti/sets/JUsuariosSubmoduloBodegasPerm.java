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

public class JUsuariosSubmoduloBodegasPerm extends JManejadorSet
{
	public JUsuariosSubmoduloBodegasPerm(HttpServletRequest request, String usuario, String entidad)
	{
		m_Select = " * FROM view_usuarios_submodulo_bodegas";
		String sql = "select * from view_usuarios_submodulo_bodegas('" + usuario + "','" + entidad +
				"') as ( id_usuario varchar, id_bodega smallint, Nombre varchar, Permitido int )";
		setSQL(sql);
		m_PageSize = 50;
		this.request = request;
	}

	public view_usuarios_submodulo_bodegas getRow(int row)
	{
		return (view_usuarios_submodulo_bodegas)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_usuarios_submodulo_bodegas getAbsRow(int row)
	{
		return (view_usuarios_submodulo_bodegas)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_usuarios_submodulo_bodegas pNode = new view_usuarios_submodulo_bodegas();

			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setID_Bodega(m_RS.getShort("ID_Bodega"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setPermitido(m_RS.getBoolean("Permitido"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
