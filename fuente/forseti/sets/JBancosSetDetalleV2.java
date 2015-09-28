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

public class JBancosSetDetalleV2 extends JManejadorSet
{
	public JBancosSetDetalleV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_bancos_movimientos_detalle_v3 ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_bancos_movimientos_detalle_v3 getRow(int row)
	{
		return (view_bancos_movimientos_detalle_v3)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_bancos_movimientos_detalle_v3 getAbsRow(int row)
	{
		return (view_bancos_movimientos_detalle_v3)m_Rows.elementAt(row);
	}

	  
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_bancos_movimientos_detalle_v3 pNode = new view_bancos_movimientos_detalle_v3();

			pNode.setDebe(m_RS.getDouble("Debe"));
			pNode.setHaber(m_RS.getDouble("Haber"));
			pNode.setCC(m_RS.getString("CC"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setParcial(m_RS.getDouble("Parcial"));
			pNode.setPart(m_RS.getByte("Part"));
			pNode.setTC(m_RS.getDouble("TC"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setID_Pol(m_RS.getLong("ID_Pol"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
