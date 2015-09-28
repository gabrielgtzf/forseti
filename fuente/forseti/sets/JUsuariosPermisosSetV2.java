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

public class JUsuariosPermisosSetV2 extends JManejadorSet
{
	public JUsuariosPermisosSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_usuarios_permisos ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_usuarios_permisos getRow(int row)
	{
		return (view_usuarios_permisos)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_usuarios_permisos getAbsRow(int row)
	{
		return (view_usuarios_permisos)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_usuarios_permisos pNode = new view_usuarios_permisos();

			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setID_Permiso(m_RS.getString("ID_Permiso"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
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
