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

public class JCRMCampaignsSet extends JManejadorSet
{
	public JCRMCampaignsSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_crmk_campaigns";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_crmk_campaigns getRow(int row)
	{
		return (tbl_crmk_campaigns)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_crmk_campaigns getAbsRow(int row)
	{
		return (tbl_crmk_campaigns)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_crmk_campaigns pNode = new tbl_crmk_campaigns();

			pNode.setGU_campaign(m_RS.getString("GU_campaign"));
			pNode.setGU_workarea(m_RS.getString("GU_workarea"));
			pNode.setNM_campaign(m_RS.getString("NM_campaign"));
			pNode.setDT_created(m_RS.getDate("DT_created"));
			pNode.setBO_active(m_RS.getInt("BO_active"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
