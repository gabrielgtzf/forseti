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

public class JCRMUsers extends JManejadorSet
{
	public JCRMUsers(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_crmk_users";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_crmk_users getRow(int row)
	{
		return (tbl_crmk_users)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_crmk_users getAbsRow(int row)
	{
		return (tbl_crmk_users)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_crmk_users pNode = new tbl_crmk_users();

			pNode.setGU_user(m_RS.getString("GU_user"));
			pNode.setDT_created(m_RS.getDate("DT_created"));
			pNode.setTX_nickname(m_RS.getString("TX_nickname"));
			pNode.setTX_pwd(m_RS.getString("TX_pwd"));
			pNode.setTX_pwd_sign(m_RS.getString("TX_pwd_sign"));
			pNode.setBO_change_pwd(m_RS.getInt("BO_change_pwd"));
			pNode.setBO_searchable(m_RS.getInt("BO_searchable"));
			pNode.setBO_active(m_RS.getInt("BO_active"));
			pNode.setNU_login_attempts(m_RS.getInt("NU_login_attempts"));
			pNode.setLen_quota(m_RS.getFloat("Len_quota"));
			pNode.setMax_quota(m_RS.getFloat("Max_quota"));
			pNode.setTP_account(m_RS.getString("TP_account"));
			pNode.setID_account(m_RS.getString("ID_account"));
			pNode.setDT_last_update(m_RS.getDate("DT_last_update"));
			pNode.setDT_last_visit(m_RS.getDate("DT_last_visit"));
			pNode.setDT_cancel(m_RS.getDate("DT_cancel"));
			pNode.setTX_main_email(m_RS.getString("TX_main_email"));
			pNode.setTX_alt_email(m_RS.getString("TX_alt_email"));
			pNode.setNM_user(m_RS.getString("NM_user"));
			pNode.setTX_surname1(m_RS.getString("TX_surname1"));
			pNode.setTX_surname2(m_RS.getString("TX_surname2"));
			pNode.setTX_challenge(m_RS.getString("TX_challenge"));
			pNode.setTX_reply(m_RS.getString("TX_reply"));
			pNode.setDT_pwd_expires(m_RS.getDate("DT_pwd_expires"));
			pNode.setGU_category(m_RS.getString("GU_category"));
			pNode.setGU_workarea(m_RS.getString("GU_workarea"));
			pNode.setNM_company(m_RS.getString("NM_company"));
			pNode.setDE_title(m_RS.getString("DE_title"));
			pNode.setID_gender(m_RS.getString("ID_gender"));
			pNode.setDT_birth(m_RS.getDate("DT_birth"));
			pNode.setNY_age(m_RS.getInt("NY_age"));
			pNode.setMarital_status(m_RS.getString("Marital_status"));
			pNode.setTX_education(m_RS.getString("TX_education"));
			pNode.setIcq_id(m_RS.getString("Icq_id"));
			pNode.setSN_passport(m_RS.getString("SN_passport"));
			pNode.setTP_passport(m_RS.getString("TP_passport"));
			pNode.setMov_phone(m_RS.getString("Mov_phone"));
			pNode.setTX_comments(m_RS.getString("TX_comments"));
			pNode.setFsi_user_id(m_RS.getString("Fsi_user_id"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
