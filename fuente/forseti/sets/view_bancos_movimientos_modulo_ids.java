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


public class view_bancos_movimientos_modulo_ids
{
	private String m_ID_Usuario;
	private byte m_Tipo;
	private byte m_ID;
	private String m_Cuenta;

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setID(byte ID)
	{
		m_ID = ID;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}


	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public byte getID()
	{
		return m_ID;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}


}

