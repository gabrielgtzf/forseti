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

public class tbl_invserv_almacen_movim_cab
{
	private int m_ID_Movimiento;
	private Date m_Fecha;
	private byte m_ID_Bodega;
	private int m_Numero;
	private String m_Status;
	private int m_ID_Concepto;
	private String m_Concepto;
	private String m_Referencia;
	private int m_ID_Pol;
	private String m_Ref;
	private String m_CR_Pri;
	private int m_CR_Sec;

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Bodega(byte ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setNumero(int Numero)
	{
		m_Numero = Numero;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setID_Concepto(int ID_Concepto)
	{
		m_ID_Concepto = ID_Concepto;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setReferencia(String Referencia)
	{
		m_Referencia = Referencia;
	}

	public void setID_Pol(int ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setCR_Pri(String CR_Pri)
	{
		m_CR_Pri = CR_Pri;
	}

	public void setCR_Sec(int CR_Sec)
	{
		m_CR_Sec = CR_Sec;
	}


	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public byte getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public int getNumero()
	{
		return m_Numero;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public int getID_Concepto()
	{
		return m_ID_Concepto;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getReferencia()
	{
		return m_Referencia;
	}

	public int getID_Pol()
	{
		return m_ID_Pol;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public String getCR_Pri()
	{
		return m_CR_Pri;
	}

	public int getCR_Sec()
	{
		return m_CR_Sec;
	}


}

