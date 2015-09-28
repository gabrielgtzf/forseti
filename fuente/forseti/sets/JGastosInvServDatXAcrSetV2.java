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

public class JGastosInvServDatXAcrSetV2 extends JManejadorSet
{
	public JGastosInvServDatXAcrSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_gastos_invserv_datos_x_acreedor ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_gastos_invserv_datos_x_acreedor getRow(int row)
	{
		return (view_gastos_invserv_datos_x_acreedor)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_gastos_invserv_datos_x_acreedor getAbsRow(int row)
	{
		return (view_gastos_invserv_datos_x_acreedor)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_gastos_invserv_datos_x_acreedor pNode = new view_gastos_invserv_datos_x_acreedor();

			pNode.setID_Acree(m_RS.getLong("ID_Acree"));
			pNode.setID_Gasto(m_RS.getString("ID_Gasto"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setPrecio(m_RS.getFloat("Precio"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
