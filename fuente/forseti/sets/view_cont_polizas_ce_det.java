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

public class view_cont_polizas_ce_det
{
	private int m_ID;
	private int m_Part;
	private String m_Cuenta;
	private String m_Concepto;
	private String m_Moneda;
	private float m_Parcial;
	private float m_TC;
	private float m_Debe;
	private float m_Haber;

	public void setID(int ID)
	{
		m_ID = ID;
	}

	public void setPart(int Part)
	{
		m_Part = Part;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setMoneda(String Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setParcial(float Parcial)
	{
		m_Parcial = Parcial;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setDebe(float Debe)
	{
		m_Debe = Debe;
	}

	public void setHaber(float Haber)
	{
		m_Haber = Haber;
	}


	public int getID()
	{
		return m_ID;
	}

	public int getPart()
	{
		return m_Part;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getMoneda()
	{
		return m_Moneda;
	}

	public float getParcial()
	{
		return m_Parcial;
	}

	public float getTC()
	{
		return m_TC;
	}

	public float getDebe()
	{
		return m_Debe;
	}

	public float getHaber()
	{
		return m_Haber;
	}


}

