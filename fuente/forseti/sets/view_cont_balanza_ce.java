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

public class view_cont_balanza_ce
{
	private byte m_Mes;
	private int m_Ano;
	private String m_Cuenta;
	private String m_AC;
	private String m_Nombre;
	private float m_Inicial;
	private float m_Cargos;
	private float m_Abonos;
	private float m_Final;

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setAC(String AC)
	{
		m_AC = AC;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setInicial(float Inicial)
	{
		m_Inicial = Inicial;
	}

	public void setCargos(float Cargos)
	{
		m_Cargos = Cargos;
	}

	public void setAbonos(float Abonos)
	{
		m_Abonos = Abonos;
	}

	public void setFinal(float Final)
	{
		m_Final = Final;
	}


	public byte getMes()
	{
		return m_Mes;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public String getAC()
	{
		return m_AC;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public float getInicial()
	{
		return m_Inicial;
	}

	public float getCargos()
	{
		return m_Cargos;
	}

	public float getAbonos()
	{
		return m_Abonos;
	}

	public float getFinal()
	{
		return m_Final;
	}


}

