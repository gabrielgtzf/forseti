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

public class tbl_crmk_oportunities
{
	private String m_GU_oportunity;
	private String m_GU_writer;
	private String m_GU_workarea;
	private int m_BO_private;
	private Date m_DT_created;
	private Date m_DT_modified;
	private Date m_DT_next_action;
	private Date m_DT_last_call;
	private int m_LV_interest;
	private int m_NU_oportunities;
	private String m_GU_campaign;
	private String m_GU_company;
	private String m_GU_contact;
	private String m_TX_company;
	private String m_TX_contact;
	private String m_TL_oportunity;
	private String m_TP_oportunity;
	private String m_TP_origin;
	private float m_IM_revenue;
	private float m_IM_cost;
	private String m_ID_status;
	private String m_ID_objetive;
	private String m_ID_message;
	private String m_TX_cause;
	private String m_TX_note;

	public void setGU_oportunity(String GU_oportunity)
	{
		m_GU_oportunity = GU_oportunity;
	}

	public void setGU_writer(String GU_writer)
	{
		m_GU_writer = GU_writer;
	}

	public void setGU_workarea(String GU_workarea)
	{
		m_GU_workarea = GU_workarea;
	}

	public void setBO_private(int BO_private)
	{
		m_BO_private = BO_private;
	}

	public void setDT_created(Date DT_created)
	{
		m_DT_created = DT_created;
	}

	public void setDT_modified(Date DT_modified)
	{
		m_DT_modified = DT_modified;
	}

	public void setDT_next_action(Date DT_next_action)
	{
		m_DT_next_action = DT_next_action;
	}

	public void setDT_last_call(Date DT_last_call)
	{
		m_DT_last_call = DT_last_call;
	}

	public void setLV_interest(int LV_interest)
	{
		m_LV_interest = LV_interest;
	}

	public void setNU_oportunities(int NU_oportunities)
	{
		m_NU_oportunities = NU_oportunities;
	}

	public void setGU_campaign(String GU_campaign)
	{
		m_GU_campaign = GU_campaign;
	}

	public void setGU_company(String GU_company)
	{
		m_GU_company = GU_company;
	}

	public void setGU_contact(String GU_contact)
	{
		m_GU_contact = GU_contact;
	}

	public void setTX_company(String TX_company)
	{
		m_TX_company = TX_company;
	}

	public void setTX_contact(String TX_contact)
	{
		m_TX_contact = TX_contact;
	}

	public void setTL_oportunity(String TL_oportunity)
	{
		m_TL_oportunity = TL_oportunity;
	}

	public void setTP_oportunity(String TP_oportunity)
	{
		m_TP_oportunity = TP_oportunity;
	}

	public void setTP_origin(String TP_origin)
	{
		m_TP_origin = TP_origin;
	}

	public void setIM_revenue(float IM_revenue)
	{
		m_IM_revenue = IM_revenue;
	}

	public void setIM_cost(float IM_cost)
	{
		m_IM_cost = IM_cost;
	}

	public void setID_status(String ID_status)
	{
		m_ID_status = ID_status;
	}

	public void setID_objetive(String ID_objetive)
	{
		m_ID_objetive = ID_objetive;
	}

	public void setID_message(String ID_message)
	{
		m_ID_message = ID_message;
	}

	public void setTX_cause(String TX_cause)
	{
		m_TX_cause = TX_cause;
	}

	public void setTX_note(String TX_note)
	{
		m_TX_note = TX_note;
	}


	public String getGU_oportunity()
	{
		return m_GU_oportunity;
	}

	public String getGU_writer()
	{
		return m_GU_writer;
	}

	public String getGU_workarea()
	{
		return m_GU_workarea;
	}

	public int getBO_private()
	{
		return m_BO_private;
	}

	public Date getDT_created()
	{
		return m_DT_created;
	}

	public Date getDT_modified()
	{
		return m_DT_modified;
	}

	public Date getDT_next_action()
	{
		return m_DT_next_action;
	}

	public Date getDT_last_call()
	{
		return m_DT_last_call;
	}

	public int getLV_interest()
	{
		return m_LV_interest;
	}

	public int getNU_oportunities()
	{
		return m_NU_oportunities;
	}

	public String getGU_campaign()
	{
		return m_GU_campaign;
	}

	public String getGU_company()
	{
		return m_GU_company;
	}

	public String getGU_contact()
	{
		return m_GU_contact;
	}

	public String getTX_company()
	{
		return m_TX_company;
	}

	public String getTX_contact()
	{
		return m_TX_contact;
	}

	public String getTL_oportunity()
	{
		return m_TL_oportunity;
	}

	public String getTP_oportunity()
	{
		return m_TP_oportunity;
	}

	public String getTP_origin()
	{
		return m_TP_origin;
	}

	public float getIM_revenue()
	{
		return m_IM_revenue;
	}

	public float getIM_cost()
	{
		return m_IM_cost;
	}

	public String getID_status()
	{
		return m_ID_status;
	}

	public String getID_objetive()
	{
		return m_ID_objetive;
	}

	public String getID_message()
	{
		return m_ID_message;
	}

	public String getTX_cause()
	{
		return m_TX_cause;
	}

	public String getTX_note()
	{
		return m_TX_note;
	}


}

