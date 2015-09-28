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


public class view_cont_catalogo_modulo
{
	private boolean m_AC;
	private String m_Cuenta;
	private String m_Nombre;
	private String m_Saldo;
	private String m_Tipo;
	private String m_Estatus;
	private String m_CE_CodAgrup;
	private String m_CE_Natur;

	public void setAC(boolean AC)
	{
		m_AC = AC;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setSaldo(String Saldo)
	{
		m_Saldo = Saldo;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}


	public boolean getAC()
	{
		return m_AC;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getSaldo()
	{
		return m_Saldo;
	}

	public String getTipo()
	{
		return m_Tipo;
	}

	public void setEstatus(String Estatus) 
	{
		m_Estatus = Estatus;
	}

	public String getEstatus()
	{
		return m_Estatus;
	}

	public void setCE_CodAgrup(String CE_CodAgrup) 
	{
		m_CE_CodAgrup = CE_CodAgrup;
	}

	public void setCE_Natur(String CE_Natur) 
	{
		m_CE_Natur = CE_Natur;	
	}
	
	public String getCE_CodAgrup() 
	{
		return m_CE_CodAgrup;
	}

	public String getCE_Natur() 
	{
		return m_CE_Natur;	
	}
}

