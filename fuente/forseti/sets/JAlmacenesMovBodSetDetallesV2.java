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

public class JAlmacenesMovBodSetDetallesV2 extends JManejadorSet
{
	public JAlmacenesMovBodSetDetallesV2(HttpServletRequest request, String tipomov)
	{
		if(tipomov.equals("TRASPASOS"))
			m_Select = " * FROM view_invserv_almacen_bod_mov_detalle ";
		else if(tipomov.equals("REQUERIMIENTOS"))
			m_Select = " * FROM view_invserv_almacen_bod_req_detalle ";
		
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_almacen_bod_mov_detalle getRow(int row)
	{
		return (view_invserv_almacen_bod_mov_detalle)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_almacen_bod_mov_detalle getAbsRow(int row)
	{
		return (view_invserv_almacen_bod_mov_detalle)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_almacen_bod_mov_detalle pNode = new view_invserv_almacen_bod_mov_detalle();

			pNode.setID_Movimiento(m_RS.getLong("ID_Movimiento"));
			pNode.setPartida(m_RS.getInt("Partida"));
			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setUnidad(m_RS.getString("Unidad"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
