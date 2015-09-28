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

public class JMasempSet extends JManejadorSet
{
	public JMasempSet(HttpServletRequest request)
	{
		m_Select = " ID_Empleado, Nombre, Apellido_Paterno, Apellido_Materno, Status, SMTP, EMail FROM TBL_NOM_MASEMP ";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_MASEMP_4C getRow(int row)
	{
		return (TBL_MASEMP_4C)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_MASEMP_4C getAbsRow(int row)
	{
		return (TBL_MASEMP_4C)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_MASEMP_4C pNode = new TBL_MASEMP_4C();

			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setApellido_Paterno(m_RS.getString("Apellido_Paterno"));
			pNode.setApellido_Materno(m_RS.getString("Apellido_Materno"));
			pNode.setStatus(m_RS.getByte("Status"));
			pNode.setSMTP(m_RS.getByte("SMTP"));
			pNode.setEMail(m_RS.getString("EMail"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
