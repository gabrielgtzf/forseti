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

public class view_cont_ce_modulo
{
	private byte m_Mes;
	private int m_Ano;
	private boolean m_Cerrado;
	private String m_Generado;
	private int m_Errores;
	private int m_Alertas;
	
	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setCerrado(boolean Cerrado)
	{
		m_Cerrado = Cerrado;
	}

	public void setGenerado(String Generado)
	{
		m_Generado = Generado;
	}

	public void setErrores(int Errores)
	{
		m_Errores = Errores;
	}

	public void setAlertas(int Alertas)
	{
		m_Alertas = Alertas;
	}


	public byte getMes()
	{
		return m_Mes;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public boolean getCerrado()
	{
		return m_Cerrado;
	}

	public String getGenerado()
	{
		return m_Generado;
	}

	public int getErrores()
	{
		return m_Errores;
	}

	public int getAlertas()
	{
		return m_Alertas;
	}
	
}

