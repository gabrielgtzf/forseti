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

public class JVendedoresSet extends JManejadorSet
{
	public JVendedoresSet(HttpServletRequest request)
	{
		m_Select = " ID_Vendedor, Nombre, Comision, Status FROM TBL_VENDEDORES";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_VENDEDORES getRow(int row)
	{
		return (TBL_VENDEDORES)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_VENDEDORES getAbsRow(int row)
	{
		return (TBL_VENDEDORES)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_VENDEDORES pNode = new TBL_VENDEDORES();

			pNode.setID_Vendedor(m_RS.getInt("ID_Vendedor"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setComision(m_RS.getFloat("Comision"));
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
