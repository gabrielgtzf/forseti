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

public class view_prod_reportes_modulo
{
	private long m_ID_Reporte;
	private boolean m_CDA;
	private int m_ID_Entidad;
	private long m_Numero;
	private Date m_Fecha;
	private String m_Status;
	private long m_NumProc;
	private String m_Concepto;
	private String m_Bodega_MP;
	private String m_Bodega_PT;
	private int m_ID_BodegaMP;
	private int m_ID_BodegaPT;
	private String m_Obs;
	private byte m_Actual;
	private boolean m_Directa;
	
	public void setID_Reporte(long ID_Reporte)
	{
		m_ID_Reporte = ID_Reporte;
	}

	public void setCDA(boolean CDA)
	{
		m_CDA = CDA;
	}

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setNumero(long Numero)
	{
		m_Numero = Numero;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setNumProc(long NumProc)
	{
		m_NumProc = NumProc;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setBodega_MP(String Bodega_MP)
	{
		m_Bodega_MP = Bodega_MP;
	}

	public void setBodega_PT(String Bodega_PT)
	{
		m_Bodega_PT = Bodega_PT;
	}

	public void setID_BodegaMP(int ID_BodegaMP)
	{
		m_ID_BodegaMP = ID_BodegaMP;
	}

	public void setID_BodegaPT(int ID_BodegaPT)
	{
		m_ID_BodegaPT = ID_BodegaPT;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}

	public void setActual(byte Actual)
	{
		m_Actual = Actual;
	}

	public void setDirecta(boolean Directa)
	{
		m_Directa = Directa;
	}


	public long getID_Reporte()
	{
		return m_ID_Reporte;
	}

	public boolean getCDA()
	{
		return m_CDA;
	}

	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public long getNumero()
	{
		return m_Numero;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public long getNumProc()
	{
		return m_NumProc;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getBodega_MP()
	{
		return m_Bodega_MP;
	}

	public String getBodega_PT()
	{
		return m_Bodega_PT;
	}

	public int getID_BodegaMP()
	{
		return m_ID_BodegaMP;
	}

	public int getID_BodegaPT()
	{
		return m_ID_BodegaPT;
	}

	public String getObs()
	{
		return m_Obs;
	}

	public byte getActual()
	{
		return m_Actual;
	}

	public boolean getDirecta()
	{
		return m_Directa;
	}

	

}

