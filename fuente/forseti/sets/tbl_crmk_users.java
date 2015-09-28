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

public class tbl_crmk_users
{
	private String m_GU_user;
	private Date m_DT_created;
	private String m_TX_nickname;
	private String m_TX_pwd;
	private String m_TX_pwd_sign;
	private int m_BO_change_pwd;
	private int m_BO_searchable;
	private int m_BO_active;
	private int m_NU_login_attempts;
	private float m_Len_quota;
	private float m_Max_quota;
	private String m_TP_account;
	private String m_ID_account;
	private Date m_DT_last_update;
	private Date m_DT_last_visit;
	private Date m_DT_cancel;
	private String m_TX_main_email;
	private String m_TX_alt_email;
	private String m_NM_user;
	private String m_TX_surname1;
	private String m_TX_surname2;
	private String m_TX_challenge;
	private String m_TX_reply;
	private Date m_DT_pwd_expires;
	private String m_GU_category;
	private String m_GU_workarea;
	private String m_NM_company;
	private String m_DE_title;
	private String m_ID_gender;
	private Date m_DT_birth;
	private int m_NY_age;
	private String m_Marital_status;
	private String m_TX_education;
	private String m_Icq_id;
	private String m_SN_passport;
	private String m_TP_passport;
	private String m_Mov_phone;
	private String m_TX_comments;
	private String m_Fsi_user_id;

	public void setGU_user(String GU_user)
	{
		m_GU_user = GU_user;
	}

	public void setDT_created(Date DT_created)
	{
		m_DT_created = DT_created;
	}

	public void setTX_nickname(String TX_nickname)
	{
		m_TX_nickname = TX_nickname;
	}

	public void setTX_pwd(String TX_pwd)
	{
		m_TX_pwd = TX_pwd;
	}

	public void setTX_pwd_sign(String TX_pwd_sign)
	{
		m_TX_pwd_sign = TX_pwd_sign;
	}

	public void setBO_change_pwd(int BO_change_pwd)
	{
		m_BO_change_pwd = BO_change_pwd;
	}

	public void setBO_searchable(int BO_searchable)
	{
		m_BO_searchable = BO_searchable;
	}

	public void setBO_active(int BO_active)
	{
		m_BO_active = BO_active;
	}

	public void setNU_login_attempts(int NU_login_attempts)
	{
		m_NU_login_attempts = NU_login_attempts;
	}

	public void setLen_quota(float Len_quota)
	{
		m_Len_quota = Len_quota;
	}

	public void setMax_quota(float Max_quota)
	{
		m_Max_quota = Max_quota;
	}

	public void setTP_account(String TP_account)
	{
		m_TP_account = TP_account;
	}

	public void setID_account(String ID_account)
	{
		m_ID_account = ID_account;
	}

	public void setDT_last_update(Date DT_last_update)
	{
		m_DT_last_update = DT_last_update;
	}

	public void setDT_last_visit(Date DT_last_visit)
	{
		m_DT_last_visit = DT_last_visit;
	}

	public void setDT_cancel(Date DT_cancel)
	{
		m_DT_cancel = DT_cancel;
	}

	public void setTX_main_email(String TX_main_email)
	{
		m_TX_main_email = TX_main_email;
	}

	public void setTX_alt_email(String TX_alt_email)
	{
		m_TX_alt_email = TX_alt_email;
	}

	public void setNM_user(String NM_user)
	{
		m_NM_user = NM_user;
	}

	public void setTX_surname1(String TX_surname1)
	{
		m_TX_surname1 = TX_surname1;
	}

	public void setTX_surname2(String TX_surname2)
	{
		m_TX_surname2 = TX_surname2;
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

	public void setGU_category(String GU_category)
	{
		m_GU_category = GU_category;
	}

	public void setGU_workarea(String GU_workarea)
	{
		m_GU_workarea = GU_workarea;
	}

	public void setNM_company(String NM_company)
	{
		m_NM_company = NM_company;
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

	public void setMarital_status(String Marital_status)
	{
		m_Marital_status = Marital_status;
	}

	public void setTX_education(String TX_education)
	{
		m_TX_education = TX_education;
	}

	public void setIcq_id(String Icq_id)
	{
		m_Icq_id = Icq_id;
	}

	public void setSN_passport(String SN_passport)
	{
		m_SN_passport = SN_passport;
	}

	public void setTP_passport(String TP_passport)
	{
		m_TP_passport = TP_passport;
	}

	public void setMov_phone(String Mov_phone)
	{
		m_Mov_phone = Mov_phone;
	}

	public void setTX_comments(String TX_comments)
	{
		m_TX_comments = TX_comments;
	}

	public void setFsi_user_id(String Fsi_user_id)
	{
		m_Fsi_user_id = Fsi_user_id;
	}


	public String getGU_user()
	{
		return m_GU_user;
	}

	public Date getDT_created()
	{
		return m_DT_created;
	}

	public String getTX_nickname()
	{
		return m_TX_nickname;
	}

	public String getTX_pwd()
	{
		return m_TX_pwd;
	}

	public String getTX_pwd_sign()
	{
		return m_TX_pwd_sign;
	}

	public int getBO_change_pwd()
	{
		return m_BO_change_pwd;
	}

	public int getBO_searchable()
	{
		return m_BO_searchable;
	}

	public int getBO_active()
	{
		return m_BO_active;
	}

	public int getNU_login_attempts()
	{
		return m_NU_login_attempts;
	}

	public float getLen_quota()
	{
		return m_Len_quota;
	}

	public float getMax_quota()
	{
		return m_Max_quota;
	}

	public String getTP_account()
	{
		return m_TP_account;
	}

	public String getID_account()
	{
		return m_ID_account;
	}

	public Date getDT_last_update()
	{
		return m_DT_last_update;
	}

	public Date getDT_last_visit()
	{
		return m_DT_last_visit;
	}

	public Date getDT_cancel()
	{
		return m_DT_cancel;
	}

	public String getTX_main_email()
	{
		return m_TX_main_email;
	}

	public String getTX_alt_email()
	{
		return m_TX_alt_email;
	}

	public String getNM_user()
	{
		return m_NM_user;
	}

	public String getTX_surname1()
	{
		return m_TX_surname1;
	}

	public String getTX_surname2()
	{
		return m_TX_surname2;
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

	public String getGU_category()
	{
		return m_GU_category;
	}

	public String getGU_workarea()
	{
		return m_GU_workarea;
	}

	public String getNM_company()
	{
		return m_NM_company;
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

	public String getMarital_status()
	{
		return m_Marital_status;
	}

	public String getTX_education()
	{
		return m_TX_education;
	}

	public String getIcq_id()
	{
		return m_Icq_id;
	}

	public String getSN_passport()
	{
		return m_SN_passport;
	}

	public String getTP_passport()
	{
		return m_TP_passport;
	}

	public String getMov_phone()
	{
		return m_Mov_phone;
	}

	public String getTX_comments()
	{
		return m_TX_comments;
	}

	public String getFsi_user_id()
	{
		return m_Fsi_user_id;
	}


}

