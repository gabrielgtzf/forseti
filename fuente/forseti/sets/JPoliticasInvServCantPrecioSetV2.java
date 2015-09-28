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

public class JPoliticasInvServCantPrecioSetV2 extends JManejadorSet
{
	public JPoliticasInvServCantPrecioSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_POLITICAS_INVSERV_CANT_VS_PRECIO ";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_POLITICAS_INVSERV_CANT_VS_PRECIO getRow(int row)
	{
		return (TBL_POLITICAS_INVSERV_CANT_VS_PRECIO)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_POLITICAS_INVSERV_CANT_VS_PRECIO getAbsRow(int row)
	{
		return (TBL_POLITICAS_INVSERV_CANT_VS_PRECIO)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_POLITICAS_INVSERV_CANT_VS_PRECIO pNode = new TBL_POLITICAS_INVSERV_CANT_VS_PRECIO();

			pNode.setCantDesde(m_RS.getFloat("CantDesde"));
			pNode.setCantDesde2(m_RS.getFloat("CantDesde2"));
			pNode.setCantDesde3(m_RS.getFloat("CantDesde3"));
			pNode.setCantDesde4(m_RS.getFloat("CantDesde4"));
			pNode.setCantDesde5(m_RS.getFloat("CantDesde5"));
			pNode.setCantHasta(m_RS.getFloat("CantHasta"));
			pNode.setCantHasta2(m_RS.getFloat("CantHasta2"));
			pNode.setCantHasta3(m_RS.getFloat("CantHasta3"));
			pNode.setCantHasta4(m_RS.getFloat("CantHasta4"));
			pNode.setCantHasta5(m_RS.getFloat("CantHasta5"));
			pNode.setID_Prod(m_RS.getString("ID_Prod"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
