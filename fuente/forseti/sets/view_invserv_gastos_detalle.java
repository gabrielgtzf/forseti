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


public class view_invserv_gastos_detalle
{
	private String m_ID_Prod;
	private String m_Cuenta;
	private byte m_Porcentaje;
	private String m_Nombre;

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setPorcentaje(byte Porcentaje)
	{
		m_Porcentaje = Porcentaje;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}


	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public byte getPorcentaje()
	{
		return m_Porcentaje;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

}

