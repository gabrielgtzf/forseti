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

public class tbl_invserv_unidades
{
	private String m_ID_Unidad;
	private String m_ID_InvServ;
	private String m_Descripcion;
	private int m_ID_SatUnidad;

	public void setID_Unidad(String ID_Unidad)
	{
		m_ID_Unidad = ID_Unidad;
	}

	public void setID_InvServ(String ID_InvServ)
	{
		m_ID_InvServ = ID_InvServ;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}


	public String getID_Unidad()
	{
		return m_ID_Unidad;
	}

	public String getID_InvServ()
	{
		return m_ID_InvServ;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public void setID_SatUnidad(int ID_SatUnidad) 
	{
		m_ID_SatUnidad = ID_SatUnidad;
	}

	public int getID_SatUnidad() 
	{
		return m_ID_SatUnidad;
	}

}

