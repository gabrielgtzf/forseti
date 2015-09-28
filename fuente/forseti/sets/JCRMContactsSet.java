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

public class JCRMContactsSet extends JManejadorSet
{
	public JCRMContactsSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_crmk_contacts";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_crmk_contacts getRow(int row)
	{
		return (tbl_crmk_contacts)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_crmk_contacts getAbsRow(int row)
	{
		return (tbl_crmk_contacts)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_crmk_contacts pNode = new tbl_crmk_contacts();

			pNode.setGU_contact(m_RS.getString("GU_contact"));
			pNode.setGU_workarea(m_RS.getString("GU_workarea"));
			pNode.setDT_created(m_RS.getDate("DT_created"));
			pNode.setBO_restricted(m_RS.getInt("BO_restricted"));
			pNode.setBO_private(m_RS.getInt("BO_private"));
			pNode.setNU_notes(m_RS.getInt("NU_notes"));
			pNode.setNU_attachs(m_RS.getInt("NU_attachs"));
			pNode.setBO_change_pwd(m_RS.getInt("BO_change_pwd"));
			pNode.setTX_nickname(m_RS.getString("TX_nickname"));
			pNode.setTX_pwd(m_RS.getString("TX_pwd"));
			pNode.setTX_challenge(m_RS.getString("TX_challenge"));
			pNode.setTX_reply(m_RS.getString("TX_reply"));
			pNode.setDT_pwd_expires(m_RS.getDate("DT_pwd_expires"));
			pNode.setDT_modified(m_RS.getDate("DT_modified"));
			pNode.setGU_writer(m_RS.getString("GU_writer"));
			pNode.setGU_company(m_RS.getString("GU_company"));
			pNode.setID_batch(m_RS.getString("ID_batch"));
			pNode.setID_status(m_RS.getString("ID_status"));
			pNode.setID_ref(m_RS.getString("ID_ref"));
			pNode.setID_fare(m_RS.getString("ID_fare"));
			pNode.setID_bpartner(m_RS.getString("ID_bpartner"));
			pNode.setTX_name(m_RS.getString("TX_name"));
			pNode.setTX_surname(m_RS.getString("TX_surname"));
			pNode.setDE_title(m_RS.getString("DE_title"));
			pNode.setID_gender(m_RS.getString("ID_gender"));
			pNode.setDT_birth(m_RS.getDate("DT_birth"));
			pNode.setNY_age(m_RS.getInt("NY_age"));
			pNode.setID_nationality(m_RS.getString("ID_nationality"));
			pNode.setSN_passport(m_RS.getString("SN_passport"));
			pNode.setTP_passport(m_RS.getString("TP_passport"));
			pNode.setSN_drivelic(m_RS.getString("SN_drivelic"));
			pNode.setDT_drivelic(m_RS.getDate("DT_drivelic"));
			pNode.setTX_dept(m_RS.getString("TX_dept"));
			pNode.setTX_division(m_RS.getString("TX_division"));
			pNode.setGU_geozone(m_RS.getString("GU_geozone"));
			pNode.setGU_sales_man(m_RS.getString("GU_sales_man"));
			pNode.setTX_comments(m_RS.getString("TX_comments"));
			pNode.setUrl_linkedin(m_RS.getString("Url_linkedin"));
			pNode.setUrl_facebook(m_RS.getString("Url_facebook"));
			pNode.setUrl_twitter(m_RS.getString("Url_twitter")); 

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
