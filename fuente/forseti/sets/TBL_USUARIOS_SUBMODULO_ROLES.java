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


public class TBL_USUARIOS_SUBMODULO_ROLES
{
	private String m_ID_Usuario;
	private String m_ID_Rol;

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setID_Rol(String ID_Rol)
	{
		m_ID_Rol = ID_Rol;
	}


	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public String getID_Rol()
	{
		return m_ID_Rol;
	}

}

