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

public class TBL_MASEMP_4C
{
	private String m_ID_Empleado;
	private String m_Nombre;
	private String m_Apellido_Paterno;
	private String m_Apellido_Materno;
	private byte m_Status;
	private byte m_SMTP;
	private String m_EMail;

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setApellido_Paterno(String Apellido_Paterno)
	{
		m_Apellido_Paterno = Apellido_Paterno;
	}

	public void setApellido_Materno(String Apellido_Materno)
	{
		m_Apellido_Materno = Apellido_Materno;
	}


	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getApellido_Paterno()
	{
		return m_Apellido_Paterno;
	}

	public String getApellido_Materno()
	{
		return m_Apellido_Materno;
	}

	public void setStatus(byte Status) 
	{
		m_Status = Status;
	}

	public byte getStatus()
	{
		return m_Status;
	}

	public void setSMTP(byte SMTP) 
	{
		m_SMTP = SMTP;	
	}

	public void setEMail(String EMail) 
	{
		m_EMail = EMail;	
	}
	
	public byte getSMTP() 
	{
		return m_SMTP;	
	}

	public String getEMail() 
	{
		return m_EMail;	
	}
}

