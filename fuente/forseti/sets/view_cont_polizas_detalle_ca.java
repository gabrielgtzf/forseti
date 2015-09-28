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


public class view_cont_polizas_detalle_ca
{
	private String m_Descripcion;
	private double m_Debe;
	private double m_Haber;
	private byte m_Mes;
	private int m_Ano;
	private String m_Cuenta;
	private double m_Saldo;
	
	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setDebe(double Debe)
	{
		m_Debe = Debe;
	}

	public void setHaber(double Haber)
	{
		m_Haber = Haber;
	}

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

	public void setSaldo(double Saldo)
	{
		m_Saldo = Saldo;
	}

	public int getAno() 
	{
		return m_Ano;
	}

	public String getCuenta() 
	{
		return m_Cuenta;
	}

	public double getDebe() 
	{
		return m_Debe;
	}

	public String getDescripcion() 
	{
		return m_Descripcion;
	}

	public double getHaber() 
	{
		return m_Haber;
	}

	public byte getMes() 
	{
		return m_Mes;
	}

	public double getSaldo() 
	{
		return m_Saldo;
	}

	

}

