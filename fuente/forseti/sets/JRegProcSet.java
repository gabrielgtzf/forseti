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

public class JRegProcSet extends JManejadorSet
{
	public JRegProcSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_regproc_modulo";
		m_PageSize = 50;
		this.request = request;
	}

	public view_regproc_modulo getRow(int row)
	{
		return (view_regproc_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_regproc_modulo getAbsRow(int row)
	{
		return (view_regproc_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_regproc_modulo pNode = new view_regproc_modulo();

			pNode.setID_RegProc(m_RS.getInt("ID_RegProc"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setFSIBD(m_RS.getString("FSIBD"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setHora(m_RS.getTime("Fecha"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setID_Proceso(m_RS.getString("ID_Proceso"));
			pNode.setProceso(m_RS.getString("Proceso"));
			pNode.setID_Modulo(m_RS.getString("ID_Modulo"));
			pNode.setResultado(m_RS.getString("Resultado"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
