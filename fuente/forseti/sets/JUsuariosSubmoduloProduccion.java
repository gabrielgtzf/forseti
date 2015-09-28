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

public class JUsuariosSubmoduloProduccion extends JManejadorSet
{
	public JUsuariosSubmoduloProduccion(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_USUARIOS_SUBMODULO_PRODUCCION";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_USUARIOS_SUBMODULO_PRODUCCION getRow(int row)
	{
		return (TBL_USUARIOS_SUBMODULO_PRODUCCION)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_USUARIOS_SUBMODULO_PRODUCCION getAbsRow(int row)
	{
		return (TBL_USUARIOS_SUBMODULO_PRODUCCION)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_USUARIOS_SUBMODULO_PRODUCCION pNode = new TBL_USUARIOS_SUBMODULO_PRODUCCION();

			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setID_EntidadProd(m_RS.getShort("ID_EntidadProd"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
