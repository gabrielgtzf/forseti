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

public class view_cont_catalogo_ce
{
	private boolean m_AC;
	private String m_Cuenta;
	private String m_Nombre;
	private byte m_Nivel;
	private String m_Estatus;
	private String m_CE_CodAgrup;
	private String m_CE_Natur;
	private String m_SubCuentaDe;

	public void setAC(boolean AC)
	{
		m_AC = AC;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}
	
	public void setSubCuentaDe(String SubCuentaDe)
	{
		m_SubCuentaDe = SubCuentaDe;
	}
	
	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setNivel(byte Nivel)
	{
		m_Nivel = Nivel;
	}

	public boolean getAC()
	{
		return m_AC;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public String getSubCuentaDe()
	{
		return m_SubCuentaDe;
	}
	
	public String getNombre()
	{
		return m_Nombre;
	}

	public byte getNivel()
	{
		return m_Nivel;
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

