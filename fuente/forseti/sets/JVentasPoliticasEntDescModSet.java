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

public class JVentasPoliticasEntDescModSet extends JManejadorSet
{
	public JVentasPoliticasEntDescModSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_ventas_politicas_ent_desc_mod";
		m_PageSize = 50;
		this.request = request;
	}

	public view_ventas_politicas_ent_desc_mod getRow(int row)
	{
		return (view_ventas_politicas_ent_desc_mod)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_ventas_politicas_ent_desc_mod getAbsRow(int row)
	{
		return (view_ventas_politicas_ent_desc_mod)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_ventas_politicas_ent_desc_mod pNode = new view_ventas_politicas_ent_desc_mod();

			pNode.setID_Entidad(m_RS.getInt("ID_Entidad"));
			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setP1(m_RS.getString("P1"));
			pNode.setP2(m_RS.getString("P2"));
			pNode.setP3(m_RS.getString("P3"));
			pNode.setP4(m_RS.getString("P4"));
			pNode.setP5(m_RS.getString("P5"));
			pNode.setAplicacion(m_RS.getByte("Aplicacion"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
