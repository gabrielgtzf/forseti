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

public class JInvservAlmacenMovimDetallesUtensiliosSetExist extends JManejadorSet
{
	public JInvservAlmacenMovimDetallesUtensiliosSetExist(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_almacen_movim_detalles_utensilios_modulo_exist";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_almacen_movim_detalles_utensilios_modulo_exist getRow(int row)
	{
		return (view_invserv_almacen_movim_detalles_utensilios_modulo_exist)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_almacen_movim_detalles_utensilios_modulo_exist getAbsRow(int row)
	{
		return (view_invserv_almacen_movim_detalles_utensilios_modulo_exist)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_invserv_almacen_movim_detalles_utensilios_modulo_exist pNode = new view_invserv_almacen_movim_detalles_utensilios_modulo_exist();

			pNode.setID_Bodega(m_RS.getShort("ID_Bodega"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_Linea(m_RS.getString("ID_Linea"));
			pNode.setLinea(m_RS.getString("Linea"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
