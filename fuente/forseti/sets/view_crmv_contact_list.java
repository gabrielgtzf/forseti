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

public class view_crmv_contact_list
{
	private String m_GU_contact;
	private String m_GU_workarea; 
	private String m_ID_status;
	private String m_Full_name;
	private String m_TR_es;
	private String m_TR_en;
	private String m_TR_fr;
	private String m_TR_de;
	private String m_TR_it;
	private String m_TR_pt;
	private String m_TR_ja;
	private String m_TR_cn;
	private String m_TR_tw;
	private String m_TR_ca;
	private String m_TR_eu;
	private String m_GU_company;
	private String m_NM_legal;
	private int m_NU_notes;
	private int m_NU_attachs;
	private Date m_DT_modified;
	private int m_BO_private;
	private String m_GU_writer;
	private int m_BO_restricted;
	private String m_GU_geozone;
	private String m_GU_sales_man;
	private String m_ID_batch;
	private String m_ID_ref;

	public void setGU_contact(String GU_contact)
	{
		m_GU_contact = GU_contact;
	}

	public void setGU_workarea(String GU_workarea)
	{
		m_GU_workarea = GU_workarea;
	}

	public void setID_status(String ID_status)
	{
		m_ID_status = ID_status;
	}

	public void setFull_name(String Full_name)
	{
		m_Full_name = Full_name;
	}

	public void setTR_es(String TR_es)
	{
		m_TR_es = TR_es;
	}

	public void setTR_en(String TR_en)
	{
		m_TR_en = TR_en;
	}

	public void setTR_fr(String TR_fr)
	{
		m_TR_fr = TR_fr;
	}

	public void setTR_de(String TR_de)
	{
		m_TR_de = TR_de;
	}

	public void setTR_it(String TR_it)
	{
		m_TR_it = TR_it;
	}

	public void setTR_pt(String TR_pt)
	{
		m_TR_pt = TR_pt;
	}

	public void setTR_ja(String TR_ja)
	{
		m_TR_ja = TR_ja;
	}

	public void setTR_cn(String TR_cn)
	{
		m_TR_cn = TR_cn;
	}

	public void setTR_tw(String TR_tw)
	{
		m_TR_tw = TR_tw;
	}

	public void setTR_ca(String TR_ca)
	{
		m_TR_ca = TR_ca;
	}

	public void setTR_eu(String TR_eu)
	{
		m_TR_eu = TR_eu;
	}

	public void setGU_company(String GU_company)
	{
		m_GU_company = GU_company;
	}

	public void setNM_legal(String NM_legal)
	{
		m_NM_legal = NM_legal;
	}

	public void setNU_notes(int NU_notes)
	{
		m_NU_notes = NU_notes;
	}

	public void setNU_attachs(int NU_attachs)
	{
		m_NU_attachs = NU_attachs;
	}

	public void setDT_modified(Date DT_modified)
	{
		m_DT_modified = DT_modified;
	}

	public void setBO_private(int BO_private)
	{
		m_BO_private = BO_private;
	}

	public void setGU_writer(String GU_writer)
	{
		m_GU_writer = GU_writer;
	}

	public void setBO_restricted(int BO_restricted)
	{
		m_BO_restricted = BO_restricted;
	}

	public void setGU_geozone(String GU_geozone)
	{
		m_GU_geozone = GU_geozone;
	}

	public void setGU_sales_man(String GU_sales_man)
	{
		m_GU_sales_man = GU_sales_man;
	}

	public void setID_batch(String ID_batch)
	{
		m_ID_batch = ID_batch;
	}

	public void setID_ref(String ID_ref)
	{
		m_ID_ref = ID_ref;
	}


	public String getGU_contact()
	{
		return m_GU_contact;
	}

	public String getGU_workarea()
	{
		return m_GU_workarea;
	}

	public String getID_status()
	{
		return m_ID_status;
	}

	public String getFull_name()
	{
		return m_Full_name;
	}

	public String getTR_es()
	{
		return m_TR_es;
	}

	public String getTR_en()
	{
		return m_TR_en;
	}

	public String getTR_fr()
	{
		return m_TR_fr;
	}

	public String getTR_de()
	{
		return m_TR_de;
	}

	public String getTR_it()
	{
		return m_TR_it;
	}

	public String getTR_pt()
	{
		return m_TR_pt;
	}

	public String getTR_ja()
	{
		return m_TR_ja;
	}

	public String getTR_cn()
	{
		return m_TR_cn;
	}

	public String getTR_tw()
	{
		return m_TR_tw;
	}

	public String getTR_ca()
	{
		return m_TR_ca;
	}

	public String getTR_eu()
	{
		return m_TR_eu;
	}

	public String getGU_company()
	{
		return m_GU_company;
	}

	public String getNM_legal()
	{
		return m_NM_legal;
	}

	public int getNU_notes()
	{
		return m_NU_notes;
	}

	public int getNU_attachs()
	{
		return m_NU_attachs;
	}

	public Date getDT_modified()
	{
		return m_DT_modified;
	}

	public int getBO_private()
	{
		return m_BO_private;
	}

	public String getGU_writer()
	{
		return m_GU_writer;
	}

	public int getBO_restricted()
	{
		return m_BO_restricted;
	}

	public String getGU_geozone()
	{
		return m_GU_geozone;
	}

	public String getGU_sales_man()
	{
		return m_GU_sales_man;
	}

	public String getID_batch()
	{
		return m_ID_batch;
	}

	public String getID_ref()
	{
		return m_ID_ref;
	}


}

