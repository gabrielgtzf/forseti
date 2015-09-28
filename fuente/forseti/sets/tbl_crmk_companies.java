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

public class tbl_crmk_companies
{
	private String m_GU_company;
	private Date m_DT_created;
	private String m_NM_legal;
	private String m_GU_workarea;
	private int m_BO_restricted;
	private String m_NM_commercial;
	private Date m_DT_modified;
	private Date m_DT_founded;
	private String m_ID_batch;
	private String m_ID_legal;
	private String m_ID_sector;
	private String m_ID_status;
	private String m_ID_ref;
	private String m_ID_fare;
	private String m_ID_bpartner;
	private String m_TP_company;
	private String m_GU_geozone;
	private int m_NU_employees;
	private float m_IM_revenue;
	private String m_GU_sales_man;
	private String m_TX_franchise;
	private String m_DE_company;

	public void setGU_company(String GU_company)
	{
		m_GU_company = GU_company;
	}

	public void setDT_created(Date DT_created)
	{
		m_DT_created = DT_created;
	}

	public void setNM_legal(String NM_legal)
	{
		m_NM_legal = NM_legal;
	}

	public void setGU_workarea(String GU_workarea)
	{
		m_GU_workarea = GU_workarea;
	}

	public void setBO_restricted(int BO_restricted)
	{
		m_BO_restricted = BO_restricted;
	}

	public void setNM_commercial(String NM_commercial)
	{
		m_NM_commercial = NM_commercial;
	}

	public void setDT_modified(Date DT_modified)
	{
		m_DT_modified = DT_modified;
	}

	public void setDT_founded(Date DT_founded)
	{
		m_DT_founded = DT_founded;
	}

	public void setID_batch(String ID_batch)
	{
		m_ID_batch = ID_batch;
	}

	public void setID_legal(String ID_legal)
	{
		m_ID_legal = ID_legal;
	}

	public void setID_sector(String ID_sector)
	{
		m_ID_sector = ID_sector;
	}

	public void setID_status(String ID_status)
	{
		m_ID_status = ID_status;
	}

	public void setID_ref(String ID_ref)
	{
		m_ID_ref = ID_ref;
	}

	public void setID_fare(String ID_fare)
	{
		m_ID_fare = ID_fare;
	}

	public void setID_bpartner(String ID_bpartner)
	{
		m_ID_bpartner = ID_bpartner;
	}

	public void setTP_company(String TP_company)
	{
		m_TP_company = TP_company;
	}

	public void setGU_geozone(String GU_geozone)
	{
		m_GU_geozone = GU_geozone;
	}

	public void setNU_employees(int NU_employees)
	{
		m_NU_employees = NU_employees;
	}

	public void setIM_revenue(float IM_revenue)
	{
		m_IM_revenue = IM_revenue;
	}

	public void setGU_sales_man(String GU_sales_man)
	{
		m_GU_sales_man = GU_sales_man;
	}

	public void setTX_franchise(String TX_franchise)
	{
		m_TX_franchise = TX_franchise;
	}

	public void setDE_company(String DE_company)
	{
		m_DE_company = DE_company;
	}


	public String getGU_company()
	{
		return m_GU_company;
	}

	public Date getDT_created()
	{
		return m_DT_created;
	}

	public String getNM_legal()
	{
		return m_NM_legal;
	}

	public String getGU_workarea()
	{
		return m_GU_workarea;
	}

	public int getBO_restricted()
	{
		return m_BO_restricted;
	}

	public String getNM_commercial()
	{
		return m_NM_commercial;
	}

	public Date getDT_modified()
	{
		return m_DT_modified;
	}

	public Date getDT_founded()
	{
		return m_DT_founded;
	}

	public String getID_batch()
	{
		return m_ID_batch;
	}

	public String getID_legal()
	{
		return m_ID_legal;
	}

	public String getID_sector()
	{
		return m_ID_sector;
	}

	public String getID_status()
	{
		return m_ID_status;
	}

	public String getID_ref()
	{
		return m_ID_ref;
	}

	public String getID_fare()
	{
		return m_ID_fare;
	}

	public String getID_bpartner()
	{
		return m_ID_bpartner;
	}

	public String getTP_company()
	{
		return m_TP_company;
	}

	public String getGU_geozone()
	{
		return m_GU_geozone;
	}

	public int getNU_employees()
	{
		return m_NU_employees;
	}

	public float getIM_revenue()
	{
		return m_IM_revenue;
	}

	public String getGU_sales_man()
	{
		return m_GU_sales_man;
	}

	public String getTX_franchise()
	{
		return m_TX_franchise;
	}

	public String getDE_company()
	{
		return m_DE_company;
	}


}

