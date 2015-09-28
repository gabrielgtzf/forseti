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

public class JCRMLookUpSet extends JManejadorSet
{
	public JCRMLookUpSet(HttpServletRequest request, String lookup)
	{
		m_Select = " * FROM tbl_crmk_" + lookup + "_lookup";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_crmk_lookup getRow(int row)
	{
		return (tbl_crmk_lookup)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_crmk_lookup getAbsRow(int row)
	{
		return (tbl_crmk_lookup)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_crmk_lookup pNode = new tbl_crmk_lookup();

			pNode.setID_Section(m_RS.getString("ID_Section"));
			pNode.setPG_lookup(m_RS.getInt("PG_lookup"));
			pNode.setVL_lookup(m_RS.getString("VL_lookup"));
			pNode.setTR_es(m_RS.getString("TR_es"));
			pNode.setTR_en(m_RS.getString("TR_en"));
			pNode.setTR_de(m_RS.getString("TR_de"));
			pNode.setTR_it(m_RS.getString("TR_it"));
			pNode.setTR_fr(m_RS.getString("TR_fr"));
			pNode.setTR_pt(m_RS.getString("TR_pt"));
			pNode.setTR_ca(m_RS.getString("TR_ca"));
			pNode.setTR_eu(m_RS.getString("TR_eu"));
			pNode.setTR_ja(m_RS.getString("TR_ja"));
			pNode.setTR_cn(m_RS.getString("TR_cn"));
			pNode.setTR_tw(m_RS.getString("TR_tw"));
			pNode.setTR_fi(m_RS.getString("TR_fi"));
			pNode.setTR_ru(m_RS.getString("TR_ru"));
			pNode.setTR_nl(m_RS.getString("TR_nl"));
			pNode.setTR_th(m_RS.getString("TR_th"));
			pNode.setTR_cs(m_RS.getString("TR_cs"));
			pNode.setTR_uk(m_RS.getString("TR_uk"));
			pNode.setTR_no(m_RS.getString("TR_no"));
			pNode.setTR_ko(m_RS.getString("TR_ko"));
			pNode.setTR_sk(m_RS.getString("TR_sk"));
			pNode.setTR_pl(m_RS.getString("TR_pl"));
			pNode.setTR_vn(m_RS.getString("TR_vn"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
