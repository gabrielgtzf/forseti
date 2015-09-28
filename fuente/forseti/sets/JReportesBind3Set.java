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

public class JReportesBind3Set extends JManejadorSet
{
	public JReportesBind3Set(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_REPORTS_SENTENCES_COLUMNS";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_REPORTS_SENTENCES_COLUMNS getRow(int row)
	{
		return (TBL_REPORTS_SENTENCES_COLUMNS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_REPORTS_SENTENCES_COLUMNS getAbsRow(int row)
	{
		return (TBL_REPORTS_SENTENCES_COLUMNS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_REPORTS_SENTENCES_COLUMNS pNode = new TBL_REPORTS_SENTENCES_COLUMNS();

			pNode.setID_Report(m_RS.getInt("ID_Report"));
			pNode.setID_Sentence(m_RS.getByte("ID_Sentence"));
			pNode.setID_IsCompute(m_RS.getByte("ID_IsCompute"));
			pNode.setID_Column(m_RS.getByte("ID_Column"));
			pNode.setColName(m_RS.getString("ColName"));
			pNode.setBindDataType(m_RS.getString("BindDataType"));
			pNode.setWillShow(m_RS.getBoolean("WillShow"));
			pNode.setFormat(m_RS.getString("Format"));
			pNode.setAncho(m_RS.getFloat("Ancho"));
			pNode.setAlinHor(m_RS.getString("AlinHor"));
			pNode.setFGColor(m_RS.getString("FGColor"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
