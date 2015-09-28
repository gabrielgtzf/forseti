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


public class view_public_bancos_cuentas
{
	private byte m_Clave;
	private String m_Cuenta;
	private long m_SigCheque;
	private byte m_Tipo;
	private String m_CC;
	private byte m_ID_Moneda;
	private boolean m_Fijo;

	public byte getID_Moneda() 
	{
		return m_ID_Moneda;
	}

	public void setID_Moneda(byte moneda) 
	{
		m_ID_Moneda = moneda;
	}

	public void setClave(byte Clave)
	{
		m_Clave = Clave;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setSigCheque(long SigCheque)
	{
		m_SigCheque = SigCheque;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setCC(String CC)
	{
		m_CC = CC;
	}


	public byte getClave()
	{
		return m_Clave;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public long getSigCheque()
	{
		return m_SigCheque;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public String getCC()
	{
		return m_CC;
	}

	public boolean getFijo() 
	{
		return m_Fijo;
	}

	public void setFijo(boolean Fijo) 
	{
		m_Fijo = Fijo;
	}
}

