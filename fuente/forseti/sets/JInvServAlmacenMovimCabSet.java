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

public class JInvServAlmacenMovimCabSet extends JManejadorSet
{
	public JInvServAlmacenMovimCabSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_invserv_almacen_movim_cab";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_invserv_almacen_movim_cab getRow(int row)
	{
		return (tbl_invserv_almacen_movim_cab)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_invserv_almacen_movim_cab getAbsRow(int row)
	{
		return (tbl_invserv_almacen_movim_cab)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_invserv_almacen_movim_cab pNode = new tbl_invserv_almacen_movim_cab();

			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setID_Bodega(m_RS.getByte("ID_Bodega"));
			pNode.setNumero(m_RS.getInt("Numero"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_Concepto(m_RS.getInt("ID_Concepto"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setReferencia(m_RS.getString("Referencia"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setCR_Pri(m_RS.getString("CR_Pri"));
			pNode.setCR_Sec(m_RS.getInt("CR_Sec"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
