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

import forseti.*;
import javax.servlet.http.*;
import java.sql.*;

public class JS3RegistrosExitososSet extends JManejadorSet
{
	public JS3RegistrosExitososSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_s3_registros_exitosos";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_s3_registros_exitosos getRow(int row)
	{
		return (tbl_s3_registros_exitosos)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_s3_registros_exitosos getAbsRow(int row)
	{
		return (tbl_s3_registros_exitosos)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_s3_registros_exitosos pNode = new tbl_s3_registros_exitosos();

			pNode.setServidor(m_RS.getString("Servidor"));
			pNode.setBD(m_RS.getString("BD"));
			pNode.setID_Modulo(m_RS.getString("ID_Modulo"));
			pNode.setObj_ID1(m_RS.getString("Obj_ID1"));
			pNode.setObj_ID2(m_RS.getString("Obj_ID2"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setTamBites(m_RS.getLong("TamBites"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
