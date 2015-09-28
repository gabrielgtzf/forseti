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

import java.sql.*;

public class tbl_crmk_campaigns
{
	private String m_GU_campaign;
	private String m_GU_workarea;
	private String m_NM_campaign;
	private Date m_DT_created;
	private int m_BO_active;

	public void setGU_campaign(String GU_campaign)
	{
		m_GU_campaign = GU_campaign;
	}

	public void setGU_workarea(String GU_workarea)
	{
		m_GU_workarea = GU_workarea;
	}

	public void setNM_campaign(String NM_campaign)
	{
		m_NM_campaign = NM_campaign;
	}

	public void setDT_created(Date DT_created)
	{
		m_DT_created = DT_created;
	}

	public void setBO_active(int BO_active)
	{
		m_BO_active = BO_active;
	}


	public String getGU_campaign()
	{
		return m_GU_campaign;
	}

	public String getGU_workarea()
	{
		return m_GU_workarea;
	}

	public String getNM_campaign()
	{
		return m_NM_campaign;
	}

	public Date getDT_created()
	{
		return m_DT_created;
	}

	public int getBO_active()
	{
		return m_BO_active;
	}


}

