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

public class view_invserv_almacen_bod_req_modulo 
{
	private long m_ID_Movimiento;
	private long m_Requerimiento;
	private String m_Status;
	private Date m_Fecha;
	private int m_ID_Bodega;
	private int m_ID_BodegaDEST;
	private String m_Concepto;
	private String m_Referencia;
	private Date m_Entrega;
	private String m_Bodega;
	private String m_BodegaDEST;
	private long m_ID_Traspaso;
	private long m_TraspasoNum;
	
	public long getID_Traspaso() 
	{
		return m_ID_Traspaso;
	}

	public void setID_Traspaso(long ID_Traspaso) 
	{
		m_ID_Traspaso = ID_Traspaso;
	}

	public void setID_Movimiento(long ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setRequerimiento(long Requerimiento)
	{
		m_Requerimiento = Requerimiento;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Bodega(int ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setID_BodegaDEST(int ID_BodegaDEST)
	{
		m_ID_BodegaDEST = ID_BodegaDEST;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setReferencia(String Referencia)
	{
		m_Referencia = Referencia;
	}

	public void setEntrega(Date Entrega)
	{
		m_Entrega = Entrega;
	}

	public void setBodega(String Bodega)
	{
		m_Bodega = Bodega;
	}

	public void setBodegaDEST(String BodegaDEST)
	{
		m_BodegaDEST = BodegaDEST;
	}


	public long getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public long getRequerimiento()
	{
		return m_Requerimiento;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public int getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public int getID_BodegaDEST()
	{
		return m_ID_BodegaDEST;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getReferencia()
	{
		return m_Referencia;
	}

	public Date getEntrega()
	{
		return m_Entrega;
	}

	public String getBodega()
	{
		return m_Bodega;
	}

	public String getBodegaDEST()
	{
		return m_BodegaDEST;
	}

	public void setTraspasoNum(long TraspasoNum) 
	{
		m_TraspasoNum = TraspasoNum;		
	}
	
	public long getTraspasoNum()
	{
		return m_TraspasoNum;
	}

}
