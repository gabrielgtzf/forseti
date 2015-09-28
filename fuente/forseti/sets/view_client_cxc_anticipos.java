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

import java.sql.Date;

public class view_client_cxc_anticipos
{
	private int m_ID_CXC;
	private Date m_Fecha;
	private String m_Concepto;
	private byte m_Pagada;
	private String m_ID_TipoClient;
	private int m_ID_ClaveClient;
	private byte m_Moneda;
	private float m_TC;
	private float m_Total;
	private float m_Saldo;

	public void setID_CXC(int ID_CXC)
	{
		m_ID_CXC = ID_CXC;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setPagada(byte Pagada)
	{
		m_Pagada = Pagada;
	}

	public void setID_TipoClient(String ID_TipoClient)
	{
		m_ID_TipoClient = ID_TipoClient;
	}

	public void setID_ClaveClient(int ID_ClaveClient)
	{
		m_ID_ClaveClient = ID_ClaveClient;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setSaldo(float Saldo)
	{
		m_Saldo = Saldo;
	}


	public int getID_CXC()
	{
		return m_ID_CXC;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public byte getPagada()
	{
		return m_Pagada;
	}

	public String getID_TipoClient()
	{
		return m_ID_TipoClient;
	}

	public int getID_ClaveClient()
	{
		return m_ID_ClaveClient;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public float getTC()
	{
		return m_TC;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public float getSaldo()
	{
		return m_Saldo;
	}

	public Date getFecha() 
	{
		return m_Fecha;
	}

	public void setFecha(Date Fecha) 
	{
		m_Fecha = Fecha;
	}

}

