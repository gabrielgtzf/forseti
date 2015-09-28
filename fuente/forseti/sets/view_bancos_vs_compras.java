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



public class view_bancos_vs_compras
{
	private short m_ID_EntidadCompra;
	private byte m_Tipo;
	private short m_Clave;
	private String m_Cuenta;
	private boolean m_Enlazado;

	public void setID_EntidadCompra(short ID_EntidadCompra)
	{
		m_ID_EntidadCompra = ID_EntidadCompra;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setClave(short Clave)
	{
		m_Clave = Clave;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setEnlazado(boolean Enlazado)
	{
		m_Enlazado = Enlazado;
	}


	public short getID_EntidadCompra()
	{
		return m_ID_EntidadCompra;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public short getClave()
	{
		return m_Clave;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public boolean getEnlazado()
	{
		return m_Enlazado;
	}


}

