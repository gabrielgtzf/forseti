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

public class TBL_CFD
{
	private int m_ID_CFD;
	private String m_RFC;
	private String m_Serie;
	private int m_Folio;
	private int m_NoAprobacion;
	private int m_AnoAprobacion;
	private Date m_Fecha;
	private Time m_Hora;
	private float m_Total;
	private float m_Impuesto;
	private byte m_Estatus;
	private String m_Efecto;
	private String m_Pedimento;
	private String m_FechaPedimento;
	private String m_Aduana;
	private String m_FSI_Tipo;
	private int m_FSI_ID;
	private String m_NoCertificado;
	private String m_CadenaOriginal;
	private String m_Sello;

	public void setID_CFD(int ID_CFD)
	{
		m_ID_CFD = ID_CFD;
	}

	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}

	public void setSerie(String Serie)
	{
		m_Serie = Serie;
	}

	public void setFolio(int Folio)
	{
		m_Folio = Folio;
	}

	public void setNoAprobacion(int NoAprobacion)
	{
		m_NoAprobacion = NoAprobacion;
	}

	public void setAnoAprobacion(int AnoAprobacion)
	{
		m_AnoAprobacion = AnoAprobacion;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}
	
	public void setHora(Time Hora)
	{
		m_Hora = Hora;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setImpuesto(float Impuesto)
	{
		m_Impuesto = Impuesto;
	}

	public void setEstatus(byte Estatus)
	{
		m_Estatus = Estatus;
	}

	public void setEfecto(String Efecto)
	{
		m_Efecto = Efecto;
	}

	public void setPedimento(String Pedimento)
	{
		m_Pedimento = Pedimento;
	}

	public void setFechaPedimento(String FechaPedimento)
	{
		m_FechaPedimento = FechaPedimento;
	}

	public void setAduana(String Aduana)
	{
		m_Aduana = Aduana;
	}

	public void setFSI_Tipo(String FSI_Tipo)
	{
		m_FSI_Tipo = FSI_Tipo;
	}

	public void setFSI_ID(int FSI_ID)
	{
		m_FSI_ID = FSI_ID;
	}

	public void setNoCertificado(String NoCertificado)
	{
		m_NoCertificado = NoCertificado;
	}

	public void setCadenaOriginal(String CadenaOriginal)
	{
		m_CadenaOriginal = CadenaOriginal;
	}

	public void setSello(String Sello)
	{
		m_Sello = Sello;
	}


	public int getID_CFD()
	{
		return m_ID_CFD;
	}

	public String getRFC()
	{
		return m_RFC;
	}

	public String getSerie()
	{
		return m_Serie;
	}

	public int getFolio()
	{
		return m_Folio;
	}

	public int getNoAprobacion()
	{
		return m_NoAprobacion;
	}

	public int getAnoAprobacion()
	{
		return m_AnoAprobacion;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public Time getHora()
	{
		return m_Hora;
	}
	
	public float getTotal()
	{
		return m_Total;
	}

	public float getImpuesto()
	{
		return m_Impuesto;
	}

	public byte getEstatus()
	{
		return m_Estatus;
	}

	public String getEfecto()
	{
		return m_Efecto;
	}

	public String getPedimento()
	{
		return m_Pedimento;
	}

	public String getFechaPedimento()
	{
		return m_FechaPedimento;
	}

	public String getAduana()
	{
		return m_Aduana;
	}

	public String getFSI_Tipo()
	{
		return m_FSI_Tipo;
	}

	public int getFSI_ID()
	{
		return m_FSI_ID;
	}

	public String getNoCertificado()
	{
		return m_NoCertificado;
	}

	public String getCadenaOriginal()
	{
		return m_CadenaOriginal;
	}

	public String getSello()
	{
		return m_Sello;
	}


}

