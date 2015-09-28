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


public class view_client_client_modulo
{
	private String m_CC;
	private int m_Clave;
	private int m_Numero;
	private String m_Contacto;
	private int m_Dias;
	private String m_EMail;
	private String m_ID_CC_Comp;
	private String m_ID_Tipo;
	private String m_Nombre;
	private String m_Saldo;
	private String m_Tel;
	private short m_ID_EntidadVenta;
	private String m_Entidad;
	private String m_Status;
	private byte m_SMTP;
	
	public void setCC(String CC)
	{
		m_CC = CC;
	}

	public void setClave(int Clave)
	{
		m_Clave = Clave;
	}

	public void setNumero(int Numero)
	{
		m_Numero = Numero;
	}

	public void setContacto(String Contacto)
	{
		m_Contacto = Contacto;
	}

	public void setDias(int Dias)
	{
		m_Dias = Dias;
	}

	public void setEMail(String EMail)
	{
		m_EMail = EMail;
	}

	public void setID_CC_Comp(String ID_CC_Comp)
	{
		m_ID_CC_Comp = ID_CC_Comp;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setTel(String Tel)
	{
		m_Tel = Tel;
	}


	public String getCC()
	{
		return m_CC;
	}

	public int getClave()
	{
		return m_Clave;
	}

	public int getNumero()
	{
		return m_Numero;
	}

	public String getContacto()
	{
		return m_Contacto;
	}

	public int getDias()
	{
		return m_Dias;
	}

	public String getEMail()
	{
		return m_EMail;
	}

	public String getID_CC_Comp()
	{
		return m_ID_CC_Comp;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getTel()
	{
		return m_Tel;
	}

	public String getEntidad() 
	{
		return m_Entidad;
	}

	public void setEntidad(String Entidad) 
	{
		m_Entidad = Entidad;
	}

	public short getID_EntidadVenta() 
	{
		return m_ID_EntidadVenta;
	}

	public void setID_EntidadVenta(short ID_EntidadVenta) 
	{
		m_ID_EntidadVenta = ID_EntidadVenta;
	}

	public void setStatus(String Status) 
	{
		m_Status = Status;
	}

	public String getStatus() 
	{
		return m_Status;
	}

	public void setSaldo(String Saldo) 
	{
		m_Saldo = Saldo;
	}

	public String getSaldo() 
	{
		return m_Saldo;
	}

	public void setSMTP(byte SMTP) 
	{
		m_SMTP = SMTP;	
	}

	public byte getSMTP() 
	{
		return m_SMTP;	
	}


}

