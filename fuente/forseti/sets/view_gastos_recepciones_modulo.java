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

public class view_gastos_recepciones_modulo
{
	private String m_Acreedor;
	private Date m_Fecha;
	private int m_ID_Entidad;
	private long m_ID_Pol;
	private long m_ID_Recepcion;
	private byte m_Moneda;
	private long m_Numero;
	private String m_Pol;
	private String m_Ref;
	private String m_Referencia;
	private String m_Status;
	private float m_TC;
	private float m_Total;
	private long m_ID_Acreedor;

	public void setAcreedor(String Acreedor)
	{
		m_Acreedor = Acreedor;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setID_Pol(long ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setID_Recepcion(long ID_Recepcion)
	{
		m_ID_Recepcion = ID_Recepcion;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setNumero(long Numero)
	{
		m_Numero = Numero;
	}

	public void setPol(String Pol)
	{
		m_Pol = Pol;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setReferencia(String Referencia)
	{
		m_Referencia = Referencia;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setID_Acreedor(long ID_Acreedor)
	{
		m_ID_Acreedor = ID_Acreedor;
	}


	public String getAcreedor()
	{
		return m_Acreedor;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public long getID_Pol()
	{
		return m_ID_Pol;
	}

	public long getID_Recepcion()
	{
		return m_ID_Recepcion;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public long getNumero()
	{
		return m_Numero;
	}

	public String getPol()
	{
		return m_Pol;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public String getReferencia()
	{
		return m_Referencia;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public float getTC()
	{
		return m_TC;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public long getID_Acreedor()
	{
		return m_ID_Acreedor;
	}


}

