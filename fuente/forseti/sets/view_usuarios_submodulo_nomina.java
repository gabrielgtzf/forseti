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

public class view_usuarios_submodulo_nomina 
{
	private String m_ID_Usuario;
	private byte m_ID_Compania;
	private byte m_ID_Sucursal;
	private String m_Descripcion;
	private boolean m_Permitido;
	
	public String getDescripcion() 
	{
		return m_Descripcion;
	}
	public void setDescripcion(String Descripcion) 
	{
		m_Descripcion = Descripcion;
	}
	public byte getID_Compania() 
	{
		return m_ID_Compania;
	}
	public void setID_Compania(byte ID_Compania) 
	{
		m_ID_Compania = ID_Compania;
	}
	public byte getID_Sucursal() 
	{
		return m_ID_Sucursal;
	}
	public void setID_Sucursal(byte ID_Sucursal) 
	{
		m_ID_Sucursal = ID_Sucursal;
	}
	public String getID_Usuario() 
	{
		return m_ID_Usuario;
	}
	public void setID_Usuario(String ID_Usuario) 
	{
		m_ID_Usuario = ID_Usuario;
	}
	public boolean getPermitido() 
	{
		return m_Permitido;
	}
	public void setPermitido(boolean Permitido) 
	{
		m_Permitido = Permitido;
	}

}
