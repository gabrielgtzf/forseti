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

public class JAlmacenesBodegasSet extends JManejadorSet
{
	public JAlmacenesBodegasSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_bodegas_modulo";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_bodegas_modulo getRow(int row)
	{
		return (view_invserv_bodegas_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_bodegas_modulo getAbsRow(int row)
	{
		return (view_invserv_bodegas_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_bodegas_modulo pNode = new view_invserv_bodegas_modulo();

			pNode.setBodega(m_RS.getString("Bodega"));
			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setExistencia(m_RS.getFloat("Existencia"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setID_Bodega(m_RS.getByte("ID_Bodega"));
			pNode.setStockMax(m_RS.getFloat("StockMax"));
			pNode.setStockMin(m_RS.getFloat("StockMin"));
			pNode.setStatus(m_RS.getString("Status"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
