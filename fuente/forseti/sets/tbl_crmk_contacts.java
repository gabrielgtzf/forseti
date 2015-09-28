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

public class tbl_crmk_contacts
{
	private String m_GU_contact;
	private String m_GU_workarea;
	private Date m_DT_created;
	private int m_BO_restricted;
	private int m_BO_private;
	private int m_NU_notes;
	private int m_NU_attachs;
	private int m_BO_change_pwd;
	private String m_TX_nickname;
	private String m_TX_pwd;
	private String m_TX_challenge;
	private String m_TX_reply;
	private Date m_DT_pwd_expires;
	private Date m_DT_modified;
	private String m_GU_writer;
	private String m_GU_company;
	private String m_ID_batch;
	private String m_ID_status;
	private String m_ID_ref;
	private String m_ID_fare;
	private String m_ID_bpartner;
	private String m_TX_name;
	private String m_TX_surname;
	private String m_DE_title;
	private String m_ID_gender;
	private Date m_DT_birth;
	private int m_NY_age;
	private String m_ID_nationality;
	private String m_SN_passport;
	private String m_TP_passport;
	private String m_SN_drivelic;
	private Date m_DT_drivelic;
	private String m_TX_dept;
	private String m_TX_division;
	private String m_GU_geozone;
	private String m_GU_sales_man;
	private String m_TX_comments;
	private String m_Url_linkedin;
	private String m_Url_facebook;
	private String m_Url_twitter;

	public void setGU_contact(String GU_contact)
	{
		m_GU_contact = GU_contact;
	}

	public void setGU_workarea(String GU_workarea)
	{
		m_GU_workarea = GU_workarea;
	}

	public void setDT_created(Date DT_created)
	{
		m_DT_created = DT_created;
	}

	public void setBO_restricted(int BO_restricted)
	{
		m_BO_restricted = BO_restricted;
	}

	public void setBO_private(int BO_private)
	{
		m_BO_private = BO_private;
	}

	public void setNU_notes(int NU_notes)
	{
		m_NU_notes = NU_notes;
	}

	public void setNU_attachs(int NU_attachs)
	{
		m_NU_attachs = NU_attachs;
	}

	public void setBO_change_pwd(int BO_change_pwd)
	{
		m_BO_change_pwd = BO_change_pwd;
	}

	public void setTX_nickname(String TX_nickname)
	{
		m_TX_nickname = TX_nickname;
	}

	public void setTX_pwd(String TX_pwd)
	{
		m_TX_pwd = TX_pwd;
	}

	public void setTX_challenge(String TX_challenge)
	{
		m_TX_challenge = TX_challenge;
	}

	public void setTX_reply(String TX_reply)
	{
		m_TX_reply = TX_reply;
	}

	public void setDT_pwd_expires(Date DT_pwd_expires)
	{
		m_DT_pwd_expires = DT_pwd_expires;
	}

	public void setDT_modified(Date DT_modified)
	{
		m_DT_modified = DT_modified;
	}

	public void setGU_writer(String GU_writer)
	{
		m_GU_writer = GU_writer;
	}

	public void setGU_company(String GU_company)
	{
		m_GU_company = GU_company;
	}

	public void setID_batch(String ID_batch)
	{
		m_ID_batch = ID_batch;
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

	public void setTX_name(String TX_name)
	{
		m_TX_name = TX_name;
	}

	public void setTX_surname(String TX_surname)
	{
		m_TX_surname = TX_surname;
	}

	public void setDE_title(String DE_title)
	{
		m_DE_title = DE_title;
	}

	public void setID_gender(String ID_gender)
	{
		m_ID_gender = ID_gender;
	}

	public void setDT_birth(Date DT_birth)
	{
		m_DT_birth = DT_birth;
	}

	public void setNY_age(int NY_age)
	{
		m_NY_age = NY_age;
	}

	public void setID_nationality(String ID_nationality)
	{
		m_ID_nationality = ID_nationality;
	}

	public void setSN_passport(String SN_passport)
	{
		m_SN_passport = SN_passport;
	}

	public void setTP_passport(String TP_passport)
	{
		m_TP_passport = TP_passport;
	}

	public void setSN_drivelic(String SN_drivelic)
	{
		m_SN_drivelic = SN_drivelic;
	}

	public void setDT_drivelic(Date DT_drivelic)
	{
		m_DT_drivelic = DT_drivelic;
	}

	public void setTX_dept(String TX_dept)
	{
		m_TX_dept = TX_dept;
	}

	public void setTX_division(String TX_division)
	{
		m_TX_division = TX_division;
	}

	public void setGU_geozone(String GU_geozone)
	{
		m_GU_geozone = GU_geozone;
	}

	public void setGU_sales_man(String GU_sales_man)
	{
		m_GU_sales_man = GU_sales_man;
	}

	public void setTX_comments(String TX_comments)
	{
		m_TX_comments = TX_comments;
	}

	public void setUrl_linkedin(String Url_linkedin)
	{
		m_Url_linkedin = Url_linkedin;
	}

	public void setUrl_facebook(String Url_facebook)
	{
		m_Url_facebook = Url_facebook;
	}

	public void setUrl_twitter(String Url_twitter)
	{
		m_Url_twitter = Url_twitter;
	}


	public String getGU_contact()
	{
		return m_GU_contact;
	}

	public String getGU_workarea()
	{
		return m_GU_workarea;
	}

	public Date getDT_created()
	{
		return m_DT_created;
	}

	public int getBO_restricted()
	{
		return m_BO_restricted;
	}

	public int getBO_private()
	{
		return m_BO_private;
	}

	public int getNU_notes()
	{
		return m_NU_notes;
	}

	public int getNU_attachs()
	{
		return m_NU_attachs;
	}

	public int getBO_change_pwd()
	{
		return m_BO_change_pwd;
	}

	public String getTX_nickname()
	{
		return m_TX_nickname;
	}

	public String getTX_pwd()
	{
		return m_TX_pwd;
	}

	public String getTX_challenge()
	{
		return m_TX_challenge;
	}

	public String getTX_reply()
	{
		return m_TX_reply;
	}

	public Date getDT_pwd_expires()
	{
		return m_DT_pwd_expires;
	}

	public Date getDT_modified()
	{
		return m_DT_modified;
	}

	public String getGU_writer()
	{
		return m_GU_writer;
	}

	public String getGU_company()
	{
		return m_GU_company;
	}

	public String getID_batch()
	{
		return m_ID_batch;
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

	public String getTX_name()
	{
		return m_TX_name;
	}

	public String getTX_surname()
	{
		return m_TX_surname;
	}

	public String getDE_title()
	{
		return m_DE_title;
	}

	public String getID_gender()
	{
		return m_ID_gender;
	}

	public Date getDT_birth()
	{
		return m_DT_birth;
	}

	public int getNY_age()
	{
		return m_NY_age;
	}

	public String getID_nationality()
	{
		return m_ID_nationality;
	}

	public String getSN_passport()
	{
		return m_SN_passport;
	}

	public String getTP_passport()
	{
		return m_TP_passport;
	}

	public String getSN_drivelic()
	{
		return m_SN_drivelic;
	}

	public Date getDT_drivelic()
	{
		return m_DT_drivelic;
	}

	public String getTX_dept()
	{
		return m_TX_dept;
	}

	public String getTX_division()
	{
		return m_TX_division;
	}

	public String getGU_geozone()
	{
		return m_GU_geozone;
	}

	public String getGU_sales_man()
	{
		return m_GU_sales_man;
	}

	public String getTX_comments()
	{
		return m_TX_comments;
	}

	public String getUrl_linkedin()
	{
		return m_Url_linkedin;
	}

	public String getUrl_facebook()
	{
		return m_Url_facebook;
	}

	public String getUrl_twitter()
	{
		return m_Url_twitter;
	}


}

