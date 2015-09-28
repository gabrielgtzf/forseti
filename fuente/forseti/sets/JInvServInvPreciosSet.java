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

public class JInvServInvPreciosSet extends JManejadorSet
{
	public JInvServInvPreciosSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_inventarios_precios";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_inventarios_precios getRow(int row)
	{
		return (view_invserv_inventarios_precios)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_inventarios_precios getAbsRow(int row)
	{
		return (view_invserv_inventarios_precios)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_inventarios_precios pNode = new view_invserv_inventarios_precios();

			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setLinea(m_RS.getString("Linea"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setSeProduce(m_RS.getBoolean("SeProduce"));
			pNode.setNoSeVende(m_RS.getBoolean("NoSeVende"));
			pNode.setP1(m_RS.getFloat("P1"));
			pNode.setP2(m_RS.getFloat("P2"));
			pNode.setP3(m_RS.getFloat("P3"));
			pNode.setP4(m_RS.getFloat("P4"));
			pNode.setP5(m_RS.getFloat("P5"));
			pNode.setPW(m_RS.getFloat("PW"));
			pNode.setPOW(m_RS.getFloat("POW"));
			pNode.setPMin(m_RS.getFloat("PMin"));
			pNode.setPMax(m_RS.getFloat("PMax"));
			pNode.setPComp(m_RS.getFloat("PComp"));
			pNode.setID_Moneda(m_RS.getByte("ID_Moneda"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
