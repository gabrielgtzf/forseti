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

public class JCRMAddressesViewSet extends JManejadorSet
{
	public JCRMAddressesViewSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_crm_addresses";
		m_PageSize = 50;
		this.request = request;
	}

	public view_crm_addresses getRow(int row)
	{
		return (view_crm_addresses)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_crm_addresses getAbsRow(int row)
	{
		return (view_crm_addresses)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_crm_addresses pNode = new view_crm_addresses();

			pNode.setGU_address(m_RS.getString("GU_address"));
			pNode.setIX_address(m_RS.getInt("IX_address"));
			pNode.setGU_workarea(m_RS.getString("GU_workarea"));
			pNode.setDT_created(m_RS.getDate("DT_created"));
			pNode.setBO_active(m_RS.getInt("BO_active"));
			pNode.setDT_modified(m_RS.getDate("DT_modified"));
			pNode.setGU_user(m_RS.getString("GU_user"));
			pNode.setTP_location(m_RS.getString("TP_location"));
			pNode.setNM_company(m_RS.getString("NM_company"));
			pNode.setTP_street(m_RS.getString("TP_street"));
			pNode.setNM_street(m_RS.getString("NM_street"));
			pNode.setNU_street(m_RS.getString("NU_street"));
			pNode.setTX_addr1(m_RS.getString("TX_addr1"));
			pNode.setTX_addr2(m_RS.getString("TX_addr2"));
			pNode.setID_country(m_RS.getString("ID_country"));
			pNode.setNM_country(m_RS.getString("NM_country"));
			pNode.setID_state(m_RS.getString("ID_state"));
			pNode.setNM_state(m_RS.getString("NM_state"));
			pNode.setMN_city(m_RS.getString("MN_city"));
			pNode.setZipcode(m_RS.getString("Zipcode"));
			pNode.setWork_phone(m_RS.getString("Work_phone"));
			pNode.setDirect_phone(m_RS.getString("Direct_phone"));
			pNode.setHome_phone(m_RS.getString("Home_phone"));
			pNode.setMov_phone(m_RS.getString("Mov_phone"));
			pNode.setFax_phone(m_RS.getString("Fax_phone"));
			pNode.setOther_phone(m_RS.getString("Other_phone"));
			pNode.setPO_box(m_RS.getString("PO_box"));
			pNode.setTX_email(m_RS.getString("TX_email"));
			pNode.setTX_email_alt(m_RS.getString("TX_email_alt"));
			pNode.setUrl_addr(m_RS.getString("Url_addr"));
			pNode.setCoord_x(m_RS.getFloat("Coord_x"));
			pNode.setCoord_y(m_RS.getFloat("Coord_y"));
			pNode.setContact_person(m_RS.getString("Contact_person"));
			pNode.setTX_salutation(m_RS.getString("TX_salutation"));
			pNode.setTX_dept(m_RS.getString("TX_dept"));
			pNode.setID_ref(m_RS.getString("ID_ref"));
			pNode.setTX_remarks(m_RS.getString("TX_remarks"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
