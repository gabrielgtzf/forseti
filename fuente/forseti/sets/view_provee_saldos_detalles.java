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

public class view_provee_saldos_detalles
{
	private byte m_Mes;
	private int m_Ano;
	private byte m_ID_Entidad;
	private String m_ID_Tipo;
	private int m_ID_Clave;
	private String m_Status;
	private int m_ID_CXP;
	private String m_Descripcion;
	private byte m_ID_Moneda;
	private String m_Simbolo;
	private float m_TC;
	private float m_SaldoIni;
	private float m_SaldoFin;
	private int m_ID_Aplicacion;

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setID_Entidad(byte ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Clave(int ID_Clave)
	{
		m_ID_Clave = ID_Clave;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setID_CXP(int ID_CXP)
	{
		m_ID_CXP = ID_CXP;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_Moneda(byte ID_Moneda)
	{
		m_ID_Moneda = ID_Moneda;
	}

	public void setSimbolo(String Simbolo)
	{
		m_Simbolo = Simbolo;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setSaldoIni(float SaldoIni)
	{
		m_SaldoIni = SaldoIni;
	}

	public void setSaldoFin(float SaldoFin)
	{
		m_SaldoFin = SaldoFin;
	}


	public byte getMes()
	{
		return m_Mes;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public byte getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public int getID_Clave()
	{
		return m_ID_Clave;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public int getID_CXP()
	{
		return m_ID_CXP;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public byte getID_Moneda()
	{
		return m_ID_Moneda;
	}

	public String getSimbolo()
	{
		return m_Simbolo;
	}

	public float getTC()
	{
		return m_TC;
	}

	public float getSaldoIni()
	{
		return m_SaldoIni;
	}

	public float getSaldoFin()
	{
		return m_SaldoFin;
	}

	public int getID_Aplicacion() 
	{
		return m_ID_Aplicacion;
	}

	public void setID_Aplicacion(int ID_Aplicacion) 
	{
		m_ID_Aplicacion = ID_Aplicacion;
	}


}

