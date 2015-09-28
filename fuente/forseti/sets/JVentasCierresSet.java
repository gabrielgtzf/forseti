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

public class JVentasCierresSet extends JManejadorSet
{
	public JVentasCierresSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_ventas_cierres_modulo";
		m_PageSize = 50;
		this.request = request;
	}

	public view_ventas_cierres_modulo getRow(int row)
	{
		return (view_ventas_cierres_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_ventas_cierres_modulo getAbsRow(int row)
	{
		return (view_ventas_cierres_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_ventas_cierres_modulo pNode = new view_ventas_cierres_modulo();

			pNode.setID_Cierre(m_RS.getInt("ID_Cierre"));
			pNode.setID_Entidad(m_RS.getShort("ID_Entidad"));
			pNode.setNumero(m_RS.getInt("Numero"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setDesde(m_RS.getInt("Desde"));
			pNode.setHasta(m_RS.getInt("Hasta"));
			pNode.setObs(m_RS.getString("Obs"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
