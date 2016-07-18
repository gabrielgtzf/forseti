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

public class tbl_comercio_exterior_det
{
	private int m_ID_VC;
	private int m_Partida;
	private String m_NoIdentificacion;
	private String m_FraccionArancelaria;
	private float m_CantidadAduana;
	private int m_UnidadAduana;
	private float m_ValorUnitarioAduana;
	private float m_ValorDolares;

	public void setID_VC(int ID_VC)
	{
		m_ID_VC = ID_VC;
	}

	public void setPartida(int Partida)
	{
		m_Partida = Partida;
	}

	public void setNoIdentificacion(String NoIdentificacion)
	{
		m_NoIdentificacion = NoIdentificacion;
	}

	public void setFraccionArancelaria(String FraccionArancelaria)
	{
		m_FraccionArancelaria = FraccionArancelaria;
	}

	public void setCantidadAduana(float CantidadAduana)
	{
		m_CantidadAduana = CantidadAduana;
	}

	public void setUnidadAduana(int UnidadAduana)
	{
		m_UnidadAduana = UnidadAduana;
	}

	public void setValorUnitarioAduana(float ValorUnitarioAduana)
	{
		m_ValorUnitarioAduana = ValorUnitarioAduana;
	}

	public void setValorDolares(float ValorDolares)
	{
		m_ValorDolares = ValorDolares;
	}


	public int getID_VC()
	{
		return m_ID_VC;
	}

	public int getPartida()
	{
		return m_Partida;
	}

	public String getNoIdentificacion()
	{
		return m_NoIdentificacion;
	}

	public String getFraccionArancelaria()
	{
		return m_FraccionArancelaria;
	}

	public float getCantidadAduana()
	{
		return m_CantidadAduana;
	}

	public int getUnidadAduana()
	{
		return m_UnidadAduana;
	}

	public float getValorUnitarioAduana()
	{
		return m_ValorUnitarioAduana;
	}

	public float getValorDolares()
	{
		return m_ValorDolares;
	}


}

