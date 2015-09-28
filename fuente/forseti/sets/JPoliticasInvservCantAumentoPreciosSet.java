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

public class JPoliticasInvservCantAumentoPreciosSet extends JManejadorSet
{
	public JPoliticasInvservCantAumentoPreciosSet(HttpServletRequest request)
	{
		m_Select = " * FROM sp_politicas_invserv_cant_aumento_precios";
		m_PageSize = 50;
		this.request = request;
	}

	public sp_politicas_invserv_cant_aumento_precios getRow(int row)
	{
		return (sp_politicas_invserv_cant_aumento_precios)m_Rows.elementAt((getFloorRow() + row));
	}

	public sp_politicas_invserv_cant_aumento_precios getAbsRow(int row)
	{
		return (sp_politicas_invserv_cant_aumento_precios)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			sp_politicas_invserv_cant_aumento_precios pNode = new sp_politicas_invserv_cant_aumento_precios();

			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setP1A(m_RS.getFloat("P1A"));
			pNode.setP1M(m_RS.getFloat("P1M"));
			pNode.setP2A(m_RS.getFloat("P2A"));
			pNode.setP2M(m_RS.getFloat("P2M"));
			pNode.setP3A(m_RS.getFloat("P3A"));
			pNode.setP3M(m_RS.getFloat("P3M"));
			pNode.setP4A(m_RS.getFloat("P4A"));
			pNode.setP4M(m_RS.getFloat("P4M"));
			pNode.setP5A(m_RS.getFloat("P5A"));
			pNode.setP5M(m_RS.getFloat("P5M"));
			pNode.setPMinA(m_RS.getFloat("PMinA"));
			pNode.setPMinM(m_RS.getFloat("PMinM"));
			pNode.setPMaxA(m_RS.getFloat("PMaxA"));
			pNode.setPMaxM(m_RS.getFloat("PMaxM"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
