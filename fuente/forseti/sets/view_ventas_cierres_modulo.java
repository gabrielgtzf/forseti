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

public class view_ventas_cierres_modulo
{
	private int m_ID_Cierre;
	private short m_ID_Entidad;
	private int m_Numero;
	private Date m_Fecha;
	private String m_Status;
	private int m_Desde;
	private int m_Hasta;
	private String m_Obs;

	public void setID_Cierre(int ID_Cierre)
	{
		m_ID_Cierre = ID_Cierre;
	}

	public void setID_Entidad(short ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setNumero(int Numero)
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

	public void setDesde(int Desde)
	{
		m_Desde = Desde;
	}

	public void setHasta(int Hasta)
	{
		m_Hasta = Hasta;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}


	public int getID_Cierre()
	{
		return m_ID_Cierre;
	}

	public short getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public int getNumero()
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

	public int getDesde()
	{
		return m_Desde;
	}

	public int getHasta()
	{
		return m_Hasta;
	}

	public String getObs()
	{
		return m_Obs;
	}


}

