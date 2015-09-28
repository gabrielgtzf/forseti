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

public class JCRMOportunitiesViewSet extends JManejadorSet
{
	public JCRMOportunitiesViewSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_crm_oportunities";
		m_PageSize = 50;
		this.request = request;
	}

	public view_crm_oportunities getRow(int row)
	{
		return (view_crm_oportunities)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_crm_oportunities getAbsRow(int row)
	{
		return (view_crm_oportunities)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_crm_oportunities pNode = new view_crm_oportunities();

			pNode.setGU_oportunity(m_RS.getString("GU_oportunity"));
			pNode.setGU_writer(m_RS.getString("GU_writer"));
			pNode.setGU_workarea(m_RS.getString("GU_workarea"));
			pNode.setBO_private(m_RS.getInt("BO_private"));
			pNode.setDT_created(m_RS.getDate("DT_created"));
			pNode.setDT_modified(m_RS.getDate("DT_modified"));
			pNode.setDT_next_action(m_RS.getDate("DT_next_action"));
			pNode.setDT_last_call(m_RS.getDate("DT_last_call"));
			pNode.setLV_interest(m_RS.getInt("LV_interest"));
			pNode.setNU_oportunities(m_RS.getInt("NU_oportunities"));
			pNode.setGU_campaign(m_RS.getString("GU_campaign"));
			pNode.setGU_company(m_RS.getString("GU_company"));
			pNode.setGU_contact(m_RS.getString("GU_contact"));
			pNode.setTX_company(m_RS.getString("TX_company"));
			pNode.setTX_contact(m_RS.getString("TX_contact"));
			pNode.setTL_oportunity(m_RS.getString("TL_oportunity"));
			pNode.setTP_oportunity(m_RS.getString("TP_oportunity"));
			pNode.setTP_origin(m_RS.getString("TP_origin"));
			pNode.setIM_revenue(m_RS.getFloat("IM_revenue"));
			pNode.setIM_cost(m_RS.getFloat("IM_cost"));
			pNode.setID_status(m_RS.getString("ID_status"));
			pNode.setID_objetive(m_RS.getString("ID_objetive"));
			pNode.setID_message(m_RS.getString("ID_message"));
			pNode.setTX_cause(m_RS.getString("TX_cause"));
			pNode.setTX_note(m_RS.getString("TX_note"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
