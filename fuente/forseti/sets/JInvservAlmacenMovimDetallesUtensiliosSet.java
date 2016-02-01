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

public class JInvservAlmacenMovimDetallesUtensiliosSet extends JManejadorSet
{
	public JInvservAlmacenMovimDetallesUtensiliosSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_almacen_movim_detalles_utensilios_modulo";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_almacen_movim_detalles_utensilios_modulo getRow(int row)
	{
		return (view_invserv_almacen_movim_detalles_utensilios_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_almacen_movim_detalles_utensilios_modulo getAbsRow(int row)
	{
		return (view_invserv_almacen_movim_detalles_utensilios_modulo)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_invserv_almacen_movim_detalles_utensilios_modulo pNode = new view_invserv_almacen_movim_detalles_utensilios_modulo();

			pNode.setID_Costo(m_RS.getLong("ID_Costo"));
			pNode.setID_Movimiento(m_RS.getLong("ID_Movimiento"));
			pNode.setNum(m_RS.getInt("Num"));
			pNode.setID_Concepto(m_RS.getShort("ID_Concepto"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setID_Bodega(m_RS.getShort("ID_Bodega"));
			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setEntrada(m_RS.getFloat("Entrada"));
			pNode.setSalida(m_RS.getFloat("Salida"));
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
