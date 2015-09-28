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

public class JCRMContactListSet extends JManejadorSet
{
	public JCRMContactListSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_crmv_contact_list";
		m_PageSize = 50;
		this.request = request;
	}

	public view_crmv_contact_list getRow(int row)
	{
		return (view_crmv_contact_list)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_crmv_contact_list getAbsRow(int row)
	{
		return (view_crmv_contact_list)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_crmv_contact_list pNode = new view_crmv_contact_list();

			pNode.setGU_contact(m_RS.getString("GU_contact"));
			pNode.setGU_workarea(m_RS.getString("GU_workarea"));
			pNode.setID_status(m_RS.getString("ID_status"));
			pNode.setFull_name(m_RS.getString("Full_name"));
			pNode.setTR_es(m_RS.getString("TR_es"));
			pNode.setTR_en(m_RS.getString("TR_en"));
			pNode.setTR_fr(m_RS.getString("TR_fr"));
			pNode.setTR_de(m_RS.getString("TR_de"));
			pNode.setTR_it(m_RS.getString("TR_it"));
			pNode.setTR_pt(m_RS.getString("TR_pt"));
			pNode.setTR_ja(m_RS.getString("TR_ja"));
			pNode.setTR_cn(m_RS.getString("TR_cn"));
			pNode.setTR_tw(m_RS.getString("TR_tw"));
			pNode.setTR_ca(m_RS.getString("TR_ca"));
			pNode.setTR_eu(m_RS.getString("TR_eu"));
			pNode.setGU_company(m_RS.getString("GU_company"));
			pNode.setNM_legal(m_RS.getString("NM_legal"));
			pNode.setNU_notes(m_RS.getInt("NU_notes"));
			pNode.setNU_attachs(m_RS.getInt("NU_attachs"));
			pNode.setDT_modified(m_RS.getDate("DT_modified"));
			pNode.setBO_private(m_RS.getInt("BO_private"));
			pNode.setGU_writer(m_RS.getString("GU_writer"));
			pNode.setBO_restricted(m_RS.getInt("BO_restricted"));
			pNode.setGU_geozone(m_RS.getString("GU_geozone"));
			pNode.setGU_sales_man(m_RS.getString("GU_sales_man"));
			pNode.setID_batch(m_RS.getString("ID_batch"));
			pNode.setID_ref(m_RS.getString("ID_ref"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
