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



public class VIEW_CALCULO_NOMINA_DET
{
	private int m_ID_Nomina;
	private String m_ID_Empleado;
	private int m_ID_Movimiento;
	private String m_Descripcion;
	private float m_Gravado;
	private float m_Exento;
	private float m_Deduccion;
	private float m_Total;
	private boolean m_EsDeduccion;
	private String m_Tipo;
	private String m_ID_SAT;

	public void setID_Nomina(int ID_Nomina)
	{
		m_ID_Nomina = ID_Nomina;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setGravado(float Gravado)
	{
		m_Gravado = Gravado;
	}

	public void setExento(float Exento)
	{
		m_Exento = Exento;
	}

	public void setDeduccion(float Deduccion)
	{
		m_Deduccion = Deduccion;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setEsDeduccion(boolean EsDeduccion)
	{
		m_EsDeduccion = EsDeduccion;
	}


	public int getID_Nomina()
	{
		return m_ID_Nomina;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getGravado()
	{
		return m_Gravado;
	}

	public float getExento()
	{
		return m_Exento;
	}

	public float getDeduccion()
	{
		return m_Deduccion;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public boolean getEsDeduccion()
	{
		return m_EsDeduccion;
	}

	public void setTipo(String Tipo) 
	{
		m_Tipo = Tipo;
	}

	public String getTipo() 
	{
		return m_Tipo;
	}

	public String getID_SAT() 
	{
		return m_ID_SAT;
	}
	
	public void setID_SAT(String ID_SAT) 
	{
		m_ID_SAT = ID_SAT;
	}
}

