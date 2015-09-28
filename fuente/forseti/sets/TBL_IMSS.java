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



public class TBL_IMSS
{
	private int m_ID_Imss;
	private String m_Concepto;
	private float m_Cuota_Patron;
	private float m_Cuota_Trabajador;
	private float m_Total;

	public void setID_Imss(int ID_Imss)
	{
		m_ID_Imss = ID_Imss;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setCuota_Patron(float Cuota_Patron)
	{
		m_Cuota_Patron = Cuota_Patron;
	}

	public void setCuota_Trabajador(float Cuota_Trabajador)
	{
		m_Cuota_Trabajador = Cuota_Trabajador;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public int getID_Imss()
	{
		return m_ID_Imss;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public float getCuota_Patron()
	{
		return m_Cuota_Patron;
	}

	public float getCuota_Trabajador()
	{
		return m_Cuota_Trabajador;
	}

	public float getTotal()
	{
		return m_Total;
	}


}

