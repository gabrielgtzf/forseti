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

public class view_cont_catalogo_detalle
{
	private byte m_Mes;
	private int m_Ano;
	private String m_Cuenta;
	private String m_Nombre;
	private boolean m_Acum;
	private float m_SaldoInicial;
	private float m_Debe;
	private float m_Haber;
	private float m_SaldoFinal;

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setAcum(boolean Acum)
	{
		m_Acum = Acum;
	}

	public void setSaldoInicial(float SaldoInicial)
	{
		m_SaldoInicial = SaldoInicial;
	}

	public void setDebe(float Debe)
	{
		m_Debe = Debe;
	}

	public void setHaber(float Haber)
	{
		m_Haber = Haber;
	}

	public void setSaldoFinal(float SaldoFinal)
	{
		m_SaldoFinal = SaldoFinal;
	}


	public byte getMes()
	{
		return m_Mes;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public boolean getAcum()
	{
		return m_Acum;
	}

	public float getSaldoInicial()
	{
		return m_SaldoInicial;
	}

	public float getDebe()
	{
		return m_Debe;
	}

	public float getHaber()
	{
		return m_Haber;
	}

	public float getSaldoFinal()
	{
		return m_SaldoFinal;
	}


}

