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

public class JCajasVsGenGastoSet extends JManejadorSet
{
	public JCajasVsGenGastoSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_cajas_vs_gengasto";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cajas_vs_gengasto getRow(int row)
	{
		return (view_cajas_vs_gengasto)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cajas_vs_gengasto getAbsRow(int row)
	{
		return (view_cajas_vs_gengasto)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cajas_vs_gengasto pNode = new view_cajas_vs_gengasto();

			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setClave(m_RS.getInt("Clave"));
			pNode.setID_EntidadCompra(m_RS.getByte("ID_EntidadCompra"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setEnlazado(m_RS.getBoolean("Enlazado"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
