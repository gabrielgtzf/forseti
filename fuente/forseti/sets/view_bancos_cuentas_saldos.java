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

public class view_bancos_cuentas_saldos
{
	private byte m_Mes;
	private int m_Ano;
	private byte m_Tipo;
	private byte m_Clave;
	private String m_Cuenta;
	private String m_CC;
	private String m_Nombre;
	private String m_Status;
	private boolean m_Fijo;
	private byte m_ID_Moneda;
	private String m_Simbolo;
	private float m_TC;
	private String m_Descripcion;
	private float m_SaldoIni;
	private float m_SaldoFin;
	private float m_CC_SaldoIni;
	private float m_CC_SaldoFin;

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setClave(byte Clave)
	{
		m_Clave = Clave;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setCC(String CC)
	{
		m_CC = CC;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setFijo(boolean Fijo)
	{
		m_Fijo = Fijo;
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

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
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

	public byte getTipo()
	{
		return m_Tipo;
	}

	public byte getClave()
	{
		return m_Clave;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public String getCC()
	{
		return m_CC;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public boolean getFijo()
	{
		return m_Fijo;
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

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getSaldoIni()
	{
		return m_SaldoIni;
	}

	public float getSaldoFin()
	{
		return m_SaldoFin;
	}

	public void setCC_SaldoIni(float CC_SaldoIni) 
	{
		m_CC_SaldoIni = CC_SaldoIni;	
	}

	public void setCC_SaldoFin(float CC_SaldoFin) 
	{
		m_CC_SaldoFin = CC_SaldoFin;
	}

	public float getCC_SaldoIni() 
	{
		return m_CC_SaldoIni;	
	}

	public float getCC_SaldoFin() 
	{
		return m_CC_SaldoFin;
	}

}

