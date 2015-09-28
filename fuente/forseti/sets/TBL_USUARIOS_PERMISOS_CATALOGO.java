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

public class TBL_USUARIOS_PERMISOS_CATALOGO
{
	private String m_ID_Permiso;
	private String m_Descripcion;
	private String m_Modulo;
	
	public void setID_Permiso(String ID_Permiso)
	{
		m_ID_Permiso = ID_Permiso;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setModulo(String Modulo)
	{
		m_Modulo = Modulo;
	}

	public String getID_Permiso()
	{
		return m_ID_Permiso;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getModulo() 
	{
		return m_Modulo;
	}


}

