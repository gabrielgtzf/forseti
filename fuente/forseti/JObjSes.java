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
package forseti;

public class JObjSes 
{
	private String m_ID_Modulo;
	private String m_Modulo;
	private JSesionPropSQL m_SesionPropSQL;
	
	JObjSes()
	{
		
	}
	
	public void setID_Modulo(String ID_Modulo)
	{
		m_ID_Modulo = ID_Modulo;
	}
	
	public String getID_Modulo()
	{
		return m_ID_Modulo;
	}

	public void setModulo(String Modulo)
	{
		m_Modulo = Modulo;
	}
	
	public String getModulo()
	{
		return m_Modulo;
	}

	public void setSesionPropSQL(JSesionPropSQL SesionPropSQL)
	{
		m_SesionPropSQL = SesionPropSQL;
	}
	
	public JSesionPropSQL getSesionPropSQL()
	{
		return m_SesionPropSQL;
	}
}
