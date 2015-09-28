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

public class JFonacotDetSet extends JManejadorSet
{
	public JFonacotDetSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_FONACOT_DET";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_FONACOT_DET getRow(int row)
	{
		return (TBL_FONACOT_DET)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_FONACOT_DET getAbsRow(int row)
	{
		return (TBL_FONACOT_DET)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_FONACOT_DET pNode = new TBL_FONACOT_DET();

			pNode.setID_Credito(m_RS.getString("ID_Credito"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
