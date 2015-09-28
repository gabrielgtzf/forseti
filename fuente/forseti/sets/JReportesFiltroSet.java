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

public class JReportesFiltroSet extends JManejadorSet
{
	public JReportesFiltroSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_REPORTS_FILTER";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_REPORTS_FILTER getRow(int row)
	{
		return (TBL_REPORTS_FILTER)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_REPORTS_FILTER getAbsRow(int row)
	{
		return (TBL_REPORTS_FILTER)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_REPORTS_FILTER pNode = new TBL_REPORTS_FILTER();

			pNode.setID_Report(m_RS.getInt("ID_Report"));
			pNode.setID_Column(m_RS.getShort("ID_Column"));
			pNode.setInstructions(m_RS.getString("Instructions"));
			pNode.setIsRange(m_RS.getBoolean("IsRange"));
			pNode.setPriDataName(m_RS.getString("PriDataName"));
			pNode.setPriDefault(m_RS.getString("PriDefault"));
			pNode.setSecDataName(m_RS.getString("SecDataName"));
			pNode.setSecDefault(m_RS.getString("SecDefault"));
			pNode.setBindDataType(m_RS.getString("BindDataType"));
			pNode.setFromCatalog(m_RS.getBoolean("FromCatalog"));
			pNode.setSelect_Clause(m_RS.getString("Select_Clause"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
