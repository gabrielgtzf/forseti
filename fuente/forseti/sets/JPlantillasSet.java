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

public class JPlantillasSet extends JManejadorSet
{
	public JPlantillasSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_PLANTILLAS";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_PLANTILLAS getRow(int row)
	{
		return (VIEW_PLANTILLAS)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_PLANTILLAS getAbsRow(int row)
	{
		return (VIEW_PLANTILLAS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_PLANTILLAS pNode = new VIEW_PLANTILLAS();

			pNode.setID_Plantilla(m_RS.getInt("ID_Plantilla"));
			pNode.setCompania(m_RS.getString("Compania"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setMovimiento(m_RS.getString("Movimiento"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setAplicacion(m_RS.getString("Aplicacion"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
