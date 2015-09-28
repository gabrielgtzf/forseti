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

public class JCRMLUCountriesSet extends JManejadorSet
{
	public JCRMLUCountriesSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_crmk_lu_countries";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_crmk_lu_countries getRow(int row)
	{
		return (tbl_crmk_lu_countries)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_crmk_lu_countries getAbsRow(int row)
	{
		return (tbl_crmk_lu_countries)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_crmk_lu_countries pNode = new tbl_crmk_lu_countries();

			pNode.setID_country(m_RS.getString("ID_country"));
			pNode.setTR_country_en(m_RS.getString("TR_country_en"));
			pNode.setTR_country_es(m_RS.getString("TR_country_es"));
			pNode.setTR_country_fr(m_RS.getString("TR_country_fr"));
			pNode.setTR_country_de(m_RS.getString("TR_country_de"));
			pNode.setTR_country_it(m_RS.getString("TR_country_it"));
			pNode.setTR_country_pt(m_RS.getString("TR_country_pt"));
			pNode.setTR_country_ca(m_RS.getString("TR_country_ca"));
			pNode.setTR_country_eu(m_RS.getString("TR_country_eu"));
			pNode.setTR_country_ja(m_RS.getString("TR_country_ja"));
			pNode.setTR_country_cn(m_RS.getString("TR_country_cn"));
			pNode.setTR_country_tw(m_RS.getString("TR_country_tw"));
			pNode.setTR_country_fi(m_RS.getString("TR_country_fi"));
			pNode.setTR_country_ru(m_RS.getString("TR_country_ru"));
			pNode.setTR_country_pl(m_RS.getString("TR_country_pl"));
			pNode.setTR_country_nl(m_RS.getString("TR_country_nl"));
			pNode.setTR_country_th(m_RS.getString("TR_country_th"));
			pNode.setTR_country_cs(m_RS.getString("TR_country_cs"));
			pNode.setTR_country_uk(m_RS.getString("TR_country_uk"));
			pNode.setTR_country_no(m_RS.getString("TR_country_no"));
			pNode.setTR_country_u1(m_RS.getString("TR_country_u1"));
			pNode.setTR_country_u2(m_RS.getString("TR_country_u2"));
			pNode.setTR_country_u3(m_RS.getString("TR_country_u3"));
			pNode.setTR_country_u4(m_RS.getString("TR_country_u4"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
