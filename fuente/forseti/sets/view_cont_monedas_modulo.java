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

public class view_cont_monedas_modulo
{
	private int m_Clave;
	private String m_Moneda;
	private String m_Simbolo;
	private float m_TC;
	private Date m_Fecha;
	private String m_Desplazamiento;
	private boolean m_DA;
	private String m_ID_SatMoneda;

	public void setClave(int Clave)
	{
		m_Clave = Clave;
	}

	public void setMoneda(String Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setSimbolo(String Simbolo)
	{
		m_Simbolo = Simbolo;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setDesplazamiento(String Desplazamiento)
	{
		m_Desplazamiento = Desplazamiento;
	}

	public void setDA(boolean DA)
	{
		m_DA = DA;
	}


	public int getClave()
	{
		return m_Clave;
	}

	public String getMoneda()
	{
		return m_Moneda;
	}

	public String getSimbolo()
	{
		return m_Simbolo;
	}

	public float getTC()
	{
		return m_TC;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getDesplazamiento()
	{
		return m_Desplazamiento;
	}

	public boolean getDA()
	{
		return m_DA;
	}

	public void setID_SatMoneda(String ID_SatMoneda) 
	{
		m_ID_SatMoneda = ID_SatMoneda;	
	}

	public String getID_SatMoneda() 
	{
		return m_ID_SatMoneda;	
	}
}

