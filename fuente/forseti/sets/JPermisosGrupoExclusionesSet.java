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

public class JPermisosGrupoExclusionesSet extends JManejadorSet
{
	public JPermisosGrupoExclusionesSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_permisos_grupo_exclusiones";
		m_PageSize = 50;
		this.request = request;
	}

	public view_permisos_grupo_exclusiones getRow(int row)
	{
		return (view_permisos_grupo_exclusiones)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_permisos_grupo_exclusiones getAbsRow(int row)
	{
		return (view_permisos_grupo_exclusiones)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_permisos_grupo_exclusiones pNode = new view_permisos_grupo_exclusiones();

			pNode.setID_Compania(m_RS.getByte("ID_Compania"));
			pNode.setID_Sucursal(m_RS.getByte("ID_Sucursal"));
			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setID_FechaMovimiento(m_RS.getDate("ID_FechaMovimiento"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setNombre(m_RS.getString("Nombre"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
