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

public class view_provee_cxp_pagos
{
	private int m_ID_CXP;
	private int m_Num;
	private Date m_Fecha;
	private byte m_Moneda;
	private float m_Total;
	private String m_Status;
	private int m_ID_Pol;
	private String m_Pol;
	private String m_FormaPago;
	private int m_ID_Mov;
	private String m_Pago;
	private byte m_ID_Concepto;
	private String m_Descripcion;
	private String m_Obs;

	public void setID_CXP(int ID_CXP)
	{
		m_ID_CXP = ID_CXP;
	}

	public void setNum(int Num)
	{
		m_Num = Num;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setID_Pol(int ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setPol(String Pol)
	{
		m_Pol = Pol;
	}

	public void setFormaPago(String FormaPago)
	{
		m_FormaPago = FormaPago;
	}

	public void setID_Mov(int ID_Mov)
	{
		m_ID_Mov = ID_Mov;
	}

	public void setPago(String Pago)
	{
		m_Pago = Pago;
	}

	public void setID_Concepto(byte ID_Concepto)
	{
		m_ID_Concepto = ID_Concepto;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}


	public int getID_CXP()
	{
		return m_ID_CXP;
	}

	public int getNum()
	{
		return m_Num;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public int getID_Pol()
	{
		return m_ID_Pol;
	}

	public String getPol()
	{
		return m_Pol;
	}

	public String getFormaPago()
	{
		return m_FormaPago;
	}

	public int getID_Mov()
	{
		return m_ID_Mov;
	}

	public String getPago()
	{
		return m_Pago;
	}

	public byte getID_Concepto()
	{
		return m_ID_Concepto;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getObs()
	{
		return m_Obs;
	}


}

