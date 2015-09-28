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

public class tbl_srv_servidores
{
	private String m_ID_Servidor;
	private String m_Usuario;
	private String m_Pass;
	private Date m_FechaAlta;
	private String m_RFC;
	private String m_Compania;
	private String m_Direccion;
	private String m_Poblacion;
	private String m_CP;
	private String m_Mail;
	private String m_Web;
	private String m_Tel;

	public void setID_Servidor(String ID_Servidor)
	{
		m_ID_Servidor = ID_Servidor;
	}

	public void setUsuario(String Usuario)
	{
		m_Usuario = Usuario;
	}

	public void setPass(String Pass)
	{
		m_Pass = Pass;
	}

	public void setFechaAlta(Date FechaAlta)
	{
		m_FechaAlta = FechaAlta;
	}

	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setDireccion(String Direccion)
	{
		m_Direccion = Direccion;
	}

	public void setPoblacion(String Poblacion)
	{
		m_Poblacion = Poblacion;
	}

	public void setCP(String CP)
	{
		m_CP = CP;
	}

	public void setMail(String Mail)
	{
		m_Mail = Mail;
	}

	public void setWeb(String Web)
	{
		m_Web = Web;
	}

	public void setTel(String Tel)
	{
		m_Tel = Tel;
	}


	public String getID_Servidor()
	{
		return m_ID_Servidor;
	}

	public String getUsuario()
	{
		return m_Usuario;
	}

	public String getPass()
	{
		return m_Pass;
	}

	public Date getFechaAlta()
	{
		return m_FechaAlta;
	}

	public String getRFC()
	{
		return m_RFC;
	}

	public String getCompania()
	{
		return m_Compania;
	}

	public String getDireccion()
	{
		return m_Direccion;
	}

	public String getPoblacion()
	{
		return m_Poblacion;
	}

	public String getCP()
	{
		return m_CP;
	}

	public String getMail()
	{
		return m_Mail;
	}

	public String getWeb()
	{
		return m_Web;
	}

	public String getTel()
	{
		return m_Tel;
	}


}

