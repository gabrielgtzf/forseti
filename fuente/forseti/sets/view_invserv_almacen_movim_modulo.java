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

public class view_invserv_almacen_movim_modulo
{
	private String m_Concepto;
	private String m_Descripcion;
	private Date m_Fecha;
	private int m_ID_Bodega;
	private short m_ID_Concepto;
	private long m_ID_Movimiento;
	private long m_ID_Pol;
	private long m_Num;
	private String m_Pol;
	private String m_Status;
	private String m_Ref;
	private String m_Referencia;
	private String m_Bodega;

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Bodega(int ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setID_Concepto(short ID_Concepto)
	{
		m_ID_Concepto = ID_Concepto;
	}

	public void setID_Movimiento(long ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setID_Pol(long ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setNum(long Num)
	{
		m_Num = Num;
	}

	public void setPol(String Pol)
	{
		m_Pol = Pol;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setReferencia(String Referencia)
	{
		m_Referencia = Referencia;
	}

	public void setBodega(String Bodega)
	{
		m_Bodega = Bodega;
	}


	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public int getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public short getID_Concepto()
	{
		return m_ID_Concepto;
	}

	public long getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public long getID_Pol()
	{
		return m_ID_Pol;
	}

	public long getNum()
	{
		return m_Num;
	}

	public String getPol()
	{
		return m_Pol;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public String getReferencia()
	{
		return m_Referencia;
	}

	public String getBodega()
	{
		return m_Bodega;
	}


}

