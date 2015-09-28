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

public class JAguinaldoSet extends JManejadorSet
{
	public JAguinaldoSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_NOM_AGUINALDO";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_AGUINALDO getRow(int row)
	{
		return (TBL_AGUINALDO)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_AGUINALDO getAbsRow(int row)
	{
		return (TBL_AGUINALDO)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_AGUINALDO pNode = new TBL_AGUINALDO();

			pNode.setID_Aguinaldo(m_RS.getInt("ID_Aguinaldo"));
			pNode.setDesde(m_RS.getFloat("Desde"));
			pNode.setHasta(m_RS.getFloat("Hasta"));
			pNode.setDias(m_RS.getByte("Dias"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
