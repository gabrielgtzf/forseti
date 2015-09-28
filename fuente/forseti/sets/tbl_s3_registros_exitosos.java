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

import java.sql.*;

public class tbl_s3_registros_exitosos
{
	private String m_Servidor;
	private String m_BD;
	private String m_ID_Modulo;
	private String m_Obj_ID1;
	private String m_Obj_ID2;
	private String m_Nombre;
	private Date m_Fecha;
	private long m_TamBites;

	public void setServidor(String Servidor)
	{
		m_Servidor = Servidor;
	}

	public void setBD(String BD)
	{
		m_BD = BD;
	}

	public void setID_Modulo(String ID_Modulo)
	{
		m_ID_Modulo = ID_Modulo;
	}

	public void setObj_ID1(String Obj_ID1)
	{
		m_Obj_ID1 = Obj_ID1;
	}

	public void setObj_ID2(String Obj_ID2)
	{
		m_Obj_ID2 = Obj_ID2;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setTamBites(long TamBites)
	{
		m_TamBites = TamBites;
	}


	public String getServidor()
	{
		return m_Servidor;
	}

	public String getBD()
	{
		return m_BD;
	}

	public String getID_Modulo()
	{
		return m_ID_Modulo;
	}

	public String getObj_ID1()
	{
		return m_Obj_ID1;
	}

	public String getObj_ID2()
	{
		return m_Obj_ID2;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public long getTamBites()
	{
		return m_TamBites;
	}


}

