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


public class view_cont_polizas_detalle
{
	private String m_Concepto;
	private double m_Debe;
	private double m_Haber;
	private long m_ID;
	private byte m_Moneda;
	private String m_Nombre;
	private String m_Numero;
	private double m_Parcial;
	private int m_Part;
	private double m_TC;

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setDebe(double Debe)
	{
		m_Debe = Debe;
	}

	public void setHaber(double Haber)
	{
		m_Haber = Haber;
	}

	public void setID(long ID)
	{
		m_ID = ID;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setNumero(String Numero)
	{
		m_Numero = Numero;
	}

	public void setParcial(double Parcial)
	{
		m_Parcial = Parcial;
	}

	public void setPart(int Part)
	{
		m_Part = Part;
	}

	public void setTC(double TC)
	{
		m_TC = TC;
	}


	public String getConcepto()
	{
		return m_Concepto;
	}

	public double getDebe()
	{
		return m_Debe;
	}

	public double getHaber()
	{
		return m_Haber;
	}

	public long getID()
	{
		return m_ID;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getNumero()
	{
		return m_Numero;
	}

	public double getParcial()
	{
		return m_Parcial;
	}

	public int getPart()
	{
		return m_Part;
	}

	public double getTC()
	{
		return m_TC;
	}


}

