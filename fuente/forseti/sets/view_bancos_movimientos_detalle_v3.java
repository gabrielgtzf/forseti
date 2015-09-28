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


public class view_bancos_movimientos_detalle_v3
{
	private double m_Debe;
	private double m_Haber;
	private String m_CC;
	private String m_Concepto;
	private String m_Nombre;
	private double m_Parcial;
	private byte m_Part;
	private double m_TC;
	private byte m_Moneda;
	private long m_ID_Pol;

	public void setDebe(double Debe)
	{
		m_Debe = Debe;
	}

	public void setHaber(double Haber)
	{
		m_Haber = Haber;
	}

	public void setCC(String CC)
	{
		m_CC = CC;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setParcial(double Parcial)
	{
		m_Parcial = Parcial;
	}

	public void setPart(byte Part)
	{
		m_Part = Part;
	}

	public void setTC(double TC)
	{
		m_TC = TC;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setID_Pol(long ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}


	public double getDebe()
	{
		return m_Debe;
	}

	public double getHaber()
	{
		return m_Haber;
	}

	public String getCC()
	{
		return m_CC;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public double getParcial()
	{
		return m_Parcial;
	}

	public byte getPart()
	{
		return m_Part;
	}

	public double getTC()
	{
		return m_TC;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public long getID_Pol()
	{
		return m_ID_Pol;
	}


}

