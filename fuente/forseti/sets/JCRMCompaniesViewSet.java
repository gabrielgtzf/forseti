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

public class JCRMCompaniesViewSet extends JManejadorSet
{
	public JCRMCompaniesViewSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_crm_companies";
		m_PageSize = 50;
		this.request = request;
	}

	public view_crm_companies getRow(int row)
	{
		return (view_crm_companies)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_crm_companies getAbsRow(int row)
	{
		return (view_crm_companies)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_crm_companies pNode = new view_crm_companies();

			pNode.setGU_company(m_RS.getString("GU_company"));
			pNode.setDT_created(m_RS.getDate("DT_created"));
			pNode.setNM_legal(m_RS.getString("NM_legal"));
			pNode.setGU_workarea(m_RS.getString("GU_workarea"));
			pNode.setBO_restricted(m_RS.getInt("BO_restricted"));
			pNode.setNM_commercial(m_RS.getString("NM_commercial"));
			pNode.setDT_modified(m_RS.getDate("DT_modified"));
			pNode.setDT_founded(m_RS.getDate("DT_founded"));
			pNode.setID_batch(m_RS.getString("ID_batch"));
			pNode.setID_legal(m_RS.getString("ID_legal"));
			pNode.setID_sector(m_RS.getString("ID_sector"));
			pNode.setID_status(m_RS.getString("ID_status"));
			pNode.setID_ref(m_RS.getString("ID_ref"));
			pNode.setID_fare(m_RS.getString("ID_fare"));
			pNode.setID_bpartner(m_RS.getString("ID_bpartner"));
			pNode.setTP_company(m_RS.getString("TP_company"));
			pNode.setGU_geozone(m_RS.getString("GU_geozone"));
			pNode.setNU_employees(m_RS.getInt("NU_employees"));
			pNode.setIM_revenue(m_RS.getFloat("IM_revenue"));
			pNode.setID_vendedor(m_RS.getInt("ID_vendedor"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setGU_sales_man(m_RS.getString("GU_sales_man"));
			pNode.setTX_franchise(m_RS.getString("TX_franchise"));
			pNode.setDE_company(m_RS.getString("DE_company"));

			m_Rows.addElement(pNode); 

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
