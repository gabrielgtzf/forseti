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


public class view_invserv_gastos_mas
{
	private String m_Clave;
	private String m_Obs;
	private float m_UltimoCosto;
	private float m_CostoPromedio;
	private float m_IVA;
	private float m_IVA_Deducible;
	private float m_CantidadAcum;
	private float m_MontoAcum;
	private float m_ImpIEPS;
	private String m_CategoriaDescripcion;

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}

	public void setUltimoCosto(float UltimoCosto)
	{
		m_UltimoCosto = UltimoCosto;
	}

	public void setCostoPromedio(float CostoPromedio)
	{
		m_CostoPromedio = CostoPromedio;
	}

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}

	public void setIVA_Deducible(float IVA_Deducible)
	{
		m_IVA_Deducible = IVA_Deducible;
	}

	public void setCantidadAcum(float CantidadAcum)
	{
		m_CantidadAcum = CantidadAcum;
	}

	public void setMontoAcum(float MontoAcum)
	{
		m_MontoAcum = MontoAcum;
	}

	public void setImpIEPS(float ImpIEPS)
	{
		m_ImpIEPS = ImpIEPS;
	}

	public void setCategoriaDescripcion(String CategoriaDescripcion)
	{
		m_CategoriaDescripcion = CategoriaDescripcion;
	}


	public String getClave()
	{
		return m_Clave;
	}

	public String getObs()
	{
		return m_Obs;
	}

	public float getUltimoCosto()
	{
		return m_UltimoCosto;
	}

	public float getCostoPromedio()
	{
		return m_CostoPromedio;
	}

	public float getIVA()
	{
		return m_IVA;
	}

	public float getIVA_Deducible()
	{
		return m_IVA_Deducible;
	}

	public float getCantidadAcum()
	{
		return m_CantidadAcum;
	}

	public float getMontoAcum()
	{
		return m_MontoAcum;
	}

	public float getImpIEPS()
	{
		return m_ImpIEPS;
	}

	public String getCategoriaDescripcion()
	{
		return m_CategoriaDescripcion;
	}


}

