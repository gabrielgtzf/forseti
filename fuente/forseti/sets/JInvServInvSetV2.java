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

public class JInvServInvSetV2 extends JManejadorSet
{
	public JInvServInvSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_inventarios_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_inventarios_modulo getRow(int row)
	{
		return (view_invserv_inventarios_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_inventarios_modulo getAbsRow(int row)
	{
		return (view_invserv_inventarios_modulo)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_inventarios_modulo pNode = new view_invserv_inventarios_modulo();

			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setExistencia(m_RS.getFloat("Existencia"));
			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setLinea(m_RS.getString("Linea"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setDescripcion_Linea(m_RS.getString("Descripcion_Linea"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
