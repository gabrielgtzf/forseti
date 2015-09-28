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

public class view_crm_addresses
{
	private String m_GU_address;
	private int m_IX_address;
	private String m_GU_workarea;
	private Date m_DT_created;
	private int m_BO_active;
	private Date m_DT_modified;
	private String m_GU_user;
	private String m_TP_location;
	private String m_NM_company;
	private String m_TP_street;
	private String m_NM_street;
	private String m_NU_street;
	private String m_TX_addr1;
	private String m_TX_addr2;
	private String m_ID_country;
	private String m_NM_country;
	private String m_ID_state;
	private String m_NM_state;
	private String m_MN_city;
	private String m_Zipcode;
	private String m_Work_phone;
	private String m_Direct_phone;
	private String m_Home_phone;
	private String m_Mov_phone;
	private String m_Fax_phone;
	private String m_Other_phone;
	private String m_PO_box;
	private String m_TX_email;
	private String m_TX_email_alt;
	private String m_Url_addr;
	private float m_Coord_x;
	private float m_Coord_y;
	private String m_Contact_person;
	private String m_TX_salutation;
	private String m_TX_dept;
	private String m_ID_ref;
	private String m_TX_remarks;

	public void setGU_address(String GU_address)
	{
		m_GU_address = GU_address;
	}

	public void setIX_address(int IX_address)
	{
		m_IX_address = IX_address;
	}

	public void setGU_workarea(String GU_workarea)
	{
		m_GU_workarea = GU_workarea;
	}

	public void setDT_created(Date DT_created)
	{
		m_DT_created = DT_created;
	}

	public void setBO_active(int BO_active)
	{
		m_BO_active = BO_active;
	}

	public void setDT_modified(Date DT_modified)
	{
		m_DT_modified = DT_modified;
	}

	public void setGU_user(String GU_user)
	{
		m_GU_user = GU_user;
	}

	public void setTP_location(String TP_location)
	{
		m_TP_location = TP_location;
	}

	public void setNM_company(String NM_company)
	{
		m_NM_company = NM_company;
	}

	public void setTP_street(String TP_street)
	{
		m_TP_street = TP_street;
	}

	public void setNM_street(String NM_street)
	{
		m_NM_street = NM_street;
	}

	public void setNU_street(String NU_street)
	{
		m_NU_street = NU_street;
	}

	public void setTX_addr1(String TX_addr1)
	{
		m_TX_addr1 = TX_addr1;
	}

	public void setTX_addr2(String TX_addr2)
	{
		m_TX_addr2 = TX_addr2;
	}

	public void setID_country(String ID_country)
	{
		m_ID_country = ID_country;
	}

	public void setNM_country(String NM_country)
	{
		m_NM_country = NM_country;
	}

	public void setID_state(String ID_state)
	{
		m_ID_state = ID_state;
	}

	public void setNM_state(String NM_state)
	{
		m_NM_state = NM_state;
	}

	public void setMN_city(String MN_city)
	{
		m_MN_city = MN_city;
	}

	public void setZipcode(String Zipcode)
	{
		m_Zipcode = Zipcode;
	}

	public void setWork_phone(String Work_phone)
	{
		m_Work_phone = Work_phone;
	}

	public void setDirect_phone(String Direct_phone)
	{
		m_Direct_phone = Direct_phone;
	}

	public void setHome_phone(String Home_phone)
	{
		m_Home_phone = Home_phone;
	}

	public void setMov_phone(String Mov_phone)
	{
		m_Mov_phone = Mov_phone;
	}

	public void setFax_phone(String Fax_phone)
	{
		m_Fax_phone = Fax_phone;
	}

	public void setOther_phone(String Other_phone)
	{
		m_Other_phone = Other_phone;
	}

	public void setPO_box(String PO_box)
	{
		m_PO_box = PO_box;
	}

	public void setTX_email(String TX_email)
	{
		m_TX_email = TX_email;
	}

	public void setTX_email_alt(String TX_email_alt)
	{
		m_TX_email_alt = TX_email_alt;
	}

	public void setUrl_addr(String Url_addr)
	{
		m_Url_addr = Url_addr;
	}

	public void setCoord_x(float Coord_x)
	{
		m_Coord_x = Coord_x;
	}

	public void setCoord_y(float Coord_y)
	{
		m_Coord_y = Coord_y;
	}

	public void setContact_person(String Contact_person)
	{
		m_Contact_person = Contact_person;
	}

	public void setTX_salutation(String TX_salutation)
	{
		m_TX_salutation = TX_salutation;
	}

	public void setTX_dept(String TX_dept)
	{
		m_TX_dept = TX_dept;
	}

	public void setID_ref(String ID_ref)
	{
		m_ID_ref = ID_ref;
	}

	public void setTX_remarks(String TX_remarks)
	{
		m_TX_remarks = TX_remarks;
	}


	public String getGU_address()
	{
		return m_GU_address;
	}

	public int getIX_address()
	{
		return m_IX_address;
	}

	public String getGU_workarea()
	{
		return m_GU_workarea;
	}

	public Date getDT_created()
	{
		return m_DT_created;
	}

	public int getBO_active()
	{
		return m_BO_active;
	}

	public Date getDT_modified()
	{
		return m_DT_modified;
	}

	public String getGU_user()
	{
		return m_GU_user;
	}

	public String getTP_location()
	{
		return m_TP_location;
	}

	public String getNM_company()
	{
		return m_NM_company;
	}

	public String getTP_street()
	{
		return m_TP_street;
	}

	public String getNM_street()
	{
		return m_NM_street;
	}

	public String getNU_street()
	{
		return m_NU_street;
	}

	public String getTX_addr1()
	{
		return m_TX_addr1;
	}

	public String getTX_addr2()
	{
		return m_TX_addr2;
	}

	public String getID_country()
	{
		return m_ID_country;
	}

	public String getNM_country()
	{
		return m_NM_country;
	}

	public String getID_state()
	{
		return m_ID_state;
	}

	public String getNM_state()
	{
		return m_NM_state;
	}

	public String getMN_city()
	{
		return m_MN_city;
	}

	public String getZipcode()
	{
		return m_Zipcode;
	}

	public String getWork_phone()
	{
		return m_Work_phone;
	}

	public String getDirect_phone()
	{
		return m_Direct_phone;
	}

	public String getHome_phone()
	{
		return m_Home_phone;
	}

	public String getMov_phone()
	{
		return m_Mov_phone;
	}

	public String getFax_phone()
	{
		return m_Fax_phone;
	}

	public String getOther_phone()
	{
		return m_Other_phone;
	}

	public String getPO_box()
	{
		return m_PO_box;
	}

	public String getTX_email()
	{
		return m_TX_email;
	}

	public String getTX_email_alt()
	{
		return m_TX_email_alt;
	}

	public String getUrl_addr()
	{
		return m_Url_addr;
	}

	public float getCoord_x()
	{
		return m_Coord_x;
	}

	public float getCoord_y()
	{
		return m_Coord_y;
	}

	public String getContact_person()
	{
		return m_Contact_person;
	}

	public String getTX_salutation()
	{
		return m_TX_salutation;
	}

	public String getTX_dept()
	{
		return m_TX_dept;
	}

	public String getID_ref()
	{
		return m_ID_ref;
	}

	public String getTX_remarks()
	{
		return m_TX_remarks;
	}


}

