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

public class view_invserv_costos_detalles
{
	private byte m_Mes;
	private int m_Ano;
	private String m_ID_Tipo;
	private String m_ID_Prod;
	private String m_Descripcion;
	private String m_ID_CC;
	private String m_Nombre;
	private String m_Status;
	private float m_ExistenciaIni;
	private float m_CostoPromIni;
	private float m_UltimoCostoIni;
	private float m_SaldoIni;
	private float m_ExistenciaFin;
	private float m_CostoPromFin;
	private float m_UltimoCostoFin;
	private float m_SaldoFin;
	private float m_CC_SaldoIni;
	private float m_CC_SaldoFin;

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_CC(String ID_CC)
	{
		m_ID_CC = ID_CC;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setExistenciaIni(float ExistenciaIni)
	{
		m_ExistenciaIni = ExistenciaIni;
	}

	public void setCostoPromIni(float CostoPromIni)
	{
		m_CostoPromIni = CostoPromIni;
	}

	public void setUltimoCostoIni(float UltimoCostoIni)
	{
		m_UltimoCostoIni = UltimoCostoIni;
	}

	public void setSaldoIni(float SaldoIni)
	{
		m_SaldoIni = SaldoIni;
	}

	public void setExistenciaFin(float ExistenciaFin)
	{
		m_ExistenciaFin = ExistenciaFin;
	}

	public void setCostoPromFin(float CostoPromFin)
	{
		m_CostoPromFin = CostoPromFin;
	}

	public void setUltimoCostoFin(float UltimoCostoFin)
	{
		m_UltimoCostoFin = UltimoCostoFin;
	}

	public void setSaldoFin(float SaldoFin)
	{
		m_SaldoFin = SaldoFin;
	}

	public void setCC_SaldoIni(float CC_SaldoIni)
	{
		m_CC_SaldoIni = CC_SaldoIni;
	}

	public void setCC_SaldoFin(float CC_SaldoFin)
	{
		m_CC_SaldoFin = CC_SaldoFin;
	}


	public byte getMes()
	{
		return m_Mes;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getID_CC()
	{
		return m_ID_CC;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public float getExistenciaIni()
	{
		return m_ExistenciaIni;
	}

	public float getCostoPromIni()
	{
		return m_CostoPromIni;
	}

	public float getUltimoCostoIni()
	{
		return m_UltimoCostoIni;
	}

	public float getSaldoIni()
	{
		return m_SaldoIni;
	}

	public float getExistenciaFin()
	{
		return m_ExistenciaFin;
	}

	public float getCostoPromFin()
	{
		return m_CostoPromFin;
	}

	public float getUltimoCostoFin()
	{
		return m_UltimoCostoFin;
	}

	public float getSaldoFin()
	{
		return m_SaldoFin;
	}

	public float getCC_SaldoIni()
	{
		return m_CC_SaldoIni;
	}

	public float getCC_SaldoFin()
	{
		return m_CC_SaldoFin;
	}


}

