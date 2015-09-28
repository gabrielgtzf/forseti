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

public class view_cont_polizas_ce_cab
{
	private int m_ID;
	private byte m_Tipo;
	private String m_Num;
	private Date m_Fecha;
	private String m_Concepto;
	private String m_Status;

	public void setID(int ID)
	{
		m_ID = ID;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setNum(String Num)
	{
		m_Num = Num;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}


	public int getID()
	{
		return m_ID;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public String getNum()
	{
		return m_Num;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getStatus()
	{
		return m_Status;
	}


}

