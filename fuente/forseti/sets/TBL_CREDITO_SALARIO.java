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



public class TBL_CREDITO_SALARIO
{
	private byte m_ID_CS;
	private float m_Ingresos_Desde;
	private float m_Ingresos_Hasta;
	private float m_CSM;

	public void setID_CS(byte ID_CS)
	{
		m_ID_CS = ID_CS;
	}

	public void setIngresos_Desde(float Ingresos_Desde)
	{
		m_Ingresos_Desde = Ingresos_Desde;
	}

	public void setIngresos_Hasta(float Ingresos_Hasta)
	{
		m_Ingresos_Hasta = Ingresos_Hasta;
	}

	public void setCSM(float CSM)
	{
		m_CSM = CSM;
	}


	public byte getID_CS()
	{
		return m_ID_CS;
	}

	public float getIngresos_Desde()
	{
		return m_Ingresos_Desde;
	}

	public float getIngresos_Hasta()
	{
		return m_Ingresos_Hasta;
	}

	public float getCSM()
	{
		return m_CSM;
	}


}

