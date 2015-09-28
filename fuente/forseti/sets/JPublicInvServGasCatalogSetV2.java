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

public class JPublicInvServGasCatalogSetV2 extends JManejadorSet
{
	public JPublicInvServGasCatalogSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_public_invserv_gastos_catalogo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_public_invserv_gastos_catalogo getRow(int row)
	{
		return (view_public_invserv_gastos_catalogo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_public_invserv_gastos_catalogo getAbsRow(int row)
	{
		return (view_public_invserv_gastos_catalogo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_public_invserv_gastos_catalogo pNode = new view_public_invserv_gastos_catalogo();

			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setLinea(m_RS.getString("Linea"));
			pNode.setUltimoCosto(m_RS.getFloat("UltimoCosto"));
			pNode.setID_Unidad(m_RS.getString("ID_Unidad"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setCostoPromedio(m_RS.getFloat("CostoPromedio"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
