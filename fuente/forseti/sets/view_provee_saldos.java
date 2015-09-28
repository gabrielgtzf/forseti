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

public class view_provee_saldos
{
	private byte m_Mes;
	private int m_Ano;
	private String m_ID_Tipo;
	private int m_ID_Clave;
	private int m_ID_Numero;
	private String m_Proveedor;
	private String m_ID_CC;
	private String m_Nombre;
	private String m_Status;
	private float m_CC_SaldoIni;
	private float m_CC_SaldoFin;
	private byte m_ID_Entidad;
	private String m_SaldoFin;

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Clave(int ID_Clave)
	{
		m_ID_Clave = ID_Clave;
	}

	public void setID_Numero(int ID_Numero)
	{
		m_ID_Numero = ID_Numero;
	}

	public void setProveedor(String Proveedor)
	{
		m_Proveedor = Proveedor;
	}

	public void setID_CC(String ID_CC)
	{
		m_ID_CC = ID_CC;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setCC_SaldoIni(float CC_SaldoIni)
	{
		m_CC_SaldoIni = CC_SaldoIni;
	}

	public void setCC_SaldoFin(float CC_SaldoFin)
	{
		m_CC_SaldoFin = CC_SaldoFin;
	}


	public byte getMes()
	{
		return m_Mes;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public int getID_Clave()
	{
		return m_ID_Clave;
	}

	public int getID_Numero()
	{
		return m_ID_Numero;
	}

	public String getProveedor()
	{
		return m_Proveedor;
	}

	public String getID_CC()
	{
		return m_ID_CC;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public float getCC_SaldoIni()
	{
		return m_CC_SaldoIni;
	}

	public float getCC_SaldoFin()
	{
		return m_CC_SaldoFin;
	}

	public void setID_Entidad(byte ID_Entidad) 
	{
		m_ID_Entidad = ID_Entidad;	
	}
	
	public byte getID_Entidad() 
	{
		return m_ID_Entidad;	
	}

	public void setSaldoFin(String SaldoFin) 
	{
		m_SaldoFin = SaldoFin;
	}

	public String getSaldoFin() 
	{
		return m_SaldoFin;
	}

}

