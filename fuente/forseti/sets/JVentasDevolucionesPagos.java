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

public class JVentasDevolucionesPagos extends JManejadorSet
{
	public JVentasDevolucionesPagos(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_ventas_devoluciones_pagos";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_ventas_devoluciones_pagos getRow(int row)
	{
		return (tbl_ventas_devoluciones_pagos)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_ventas_devoluciones_pagos getAbsRow(int row)
	{
		return (tbl_ventas_devoluciones_pagos)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_ventas_devoluciones_pagos pNode = new tbl_ventas_devoluciones_pagos();

			pNode.setID_Devolucion(m_RS.getInt("ID_Devolucion"));
			pNode.setID_Mov(m_RS.getInt("ID_Mov"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
