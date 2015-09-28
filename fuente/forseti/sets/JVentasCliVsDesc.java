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

public class JVentasCliVsDesc extends JManejadorSet
{
	public JVentasCliVsDesc(HttpServletRequest request)
	{
		m_Select = " * FROM view_ventas_clientes_vs_descuentos ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_ventas_clientes_vs_descuentos getRow(int row)
	{
		return (view_ventas_clientes_vs_descuentos)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_ventas_clientes_vs_descuentos getAbsRow(int row)
	{
		return (view_ventas_clientes_vs_descuentos)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_ventas_clientes_vs_descuentos pNode = new view_ventas_clientes_vs_descuentos();

			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setID_Client(m_RS.getInt("ID_Client"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));
			pNode.setDescuento2(m_RS.getFloat("Descuento2"));
			pNode.setDescuento3(m_RS.getFloat("Descuento3"));
			pNode.setDescuento4(m_RS.getFloat("Descuento4"));
			pNode.setDescuento5(m_RS.getFloat("Descuento5"));
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
