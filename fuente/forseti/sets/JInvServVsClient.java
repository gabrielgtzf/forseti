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

public class JInvServVsClient extends JManejadorSet
{
	public JInvServVsClient(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_inventarios_vs_client";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_inventarios_vs_client getRow(int row)
	{
		return (view_invserv_inventarios_vs_client)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_inventarios_vs_client getAbsRow(int row)
	{
		return (view_invserv_inventarios_vs_client)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_inventarios_vs_client pNode = new view_invserv_inventarios_vs_client();

			pNode.setID_Client(m_RS.getInt("ID_Client"));
			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setPrecio(m_RS.getFloat("Precio"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setID_Numero(m_RS.getInt("ID_Numero"));
			pNode.setNombreMoneda(m_RS.getString("NombreMoneda"));
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
