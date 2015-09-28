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

public class JAdmVariablesSet extends JManejadorSet
{
	public JAdmVariablesSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_VARIABLES_MODULO";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_VARIABLES getRow(int row)
	{
		return (TBL_VARIABLES)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_VARIABLES getAbsRow(int row)
	{
		return (TBL_VARIABLES)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  	protected void BindRow()
	{
		try
		{
			TBL_VARIABLES pNode = new TBL_VARIABLES();

			pNode.setID_Variable(m_RS.getString("ID_Variable"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setVEntero(m_RS.getInt("VEntero"));
			pNode.setVDecimal(m_RS.getFloat("VDecimal"));
			pNode.setVFecha(m_RS.getDate("VFecha"));
			pNode.setVHora(m_RS.getTime("VFecha"));
			pNode.setVAlfanumerico(m_RS.getString("VAlfanumerico"));
			pNode.setDeSistema(m_RS.getByte("DeSistema"));
			pNode.setModulo(m_RS.getString("Modulo"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
