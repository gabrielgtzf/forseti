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

public class JUsuariosPermisosCatalogoSet extends JManejadorSet
{
	public JUsuariosPermisosCatalogoSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_USUARIOS_PERMISOS_CATALOGO";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_USUARIOS_PERMISOS_CATALOGO getRow(int row)
	{
		return (VIEW_USUARIOS_PERMISOS_CATALOGO)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_USUARIOS_PERMISOS_CATALOGO getAbsRow(int row)
	{
		return (VIEW_USUARIOS_PERMISOS_CATALOGO)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_USUARIOS_PERMISOS_CATALOGO pNode = new VIEW_USUARIOS_PERMISOS_CATALOGO();

			pNode.setID_Permiso(m_RS.getString("ID_Permiso"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setModulo(m_RS.getString("Modulo"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
