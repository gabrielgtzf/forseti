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

public class JAlmacenesMovimSetV2 extends JManejadorSet
{
	public JAlmacenesMovimSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_almacen_movim_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_almacen_movim_modulo getRow(int row)
	{
		return (view_invserv_almacen_movim_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_almacen_movim_modulo getAbsRow(int row)
	{
		return (view_invserv_almacen_movim_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_almacen_movim_modulo pNode = new view_invserv_almacen_movim_modulo();

			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setID_Bodega(m_RS.getInt("ID_Bodega"));
			pNode.setID_Concepto(m_RS.getShort("ID_Concepto"));
			pNode.setID_Movimiento(m_RS.getLong("ID_Movimiento"));
			pNode.setID_Pol(m_RS.getLong("ID_Pol"));
			pNode.setNum(m_RS.getLong("Num"));
			pNode.setPol(m_RS.getString("Pol"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setReferencia(m_RS.getString("Referencia"));
			pNode.setBodega(m_RS.getString("Bodega"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
