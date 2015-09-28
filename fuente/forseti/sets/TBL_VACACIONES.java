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



public class TBL_VACACIONES
{
	private int m_ID_Vacaciones;
	private float m_Desde;
	private float m_Hasta;
	private byte m_Dias;
	private float m_PV;

	public void setID_Vacaciones(int ID_Vacaciones)
	{
		m_ID_Vacaciones = ID_Vacaciones;
	}

	public void setDesde(float Desde)
	{
		m_Desde = Desde;
	}

	public void setHasta(float Hasta)
	{
		m_Hasta = Hasta;
	}

	public void setDias(byte Dias)
	{
		m_Dias = Dias;
	}

	public void setPV(float PV)
	{
		m_PV = PV;
	}


	public int getID_Vacaciones()
	{
		return m_ID_Vacaciones;
	}

	public float getDesde()
	{
		return m_Desde;
	}

	public float getHasta()
	{
		return m_Hasta;
	}

	public byte getDias()
	{
		return m_Dias;
	}

	public float getPV()
	{
		return m_PV;
	}


}

