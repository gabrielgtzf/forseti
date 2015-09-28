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

public class view_provee_cxp_anticipos
{
	private int m_ID_CXP;
	private Date m_Fecha;
	private String m_Concepto;
	private byte m_Pagada;
	private String m_ID_TipoProvee;
	private int m_ID_ClaveProvee;
	private byte m_Moneda;
	private float m_TC;
	private float m_Total;
	private float m_Saldo;

	public void setID_CXP(int ID_CXP)
	{
		m_ID_CXP = ID_CXP;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setPagada(byte Pagada)
	{
		m_Pagada = Pagada;
	}

	public void setID_TipoProvee(String ID_TipoProvee)
	{
		m_ID_TipoProvee = ID_TipoProvee;
	}

	public void setID_ClaveProvee(int ID_ClaveProvee)
	{
		m_ID_ClaveProvee = ID_ClaveProvee;
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


	public int getID_CXP()
	{
		return m_ID_CXP;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public byte getPagada()
	{
		return m_Pagada;
	}

	public String getID_TipoProvee()
	{
		return m_ID_TipoProvee;
	}

	public int getID_ClaveProvee()
	{
		return m_ID_ClaveProvee;
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

