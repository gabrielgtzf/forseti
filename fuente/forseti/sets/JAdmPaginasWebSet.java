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

public class JAdmPaginasWebSet extends JManejadorSet
{
	public JAdmPaginasWebSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_paginas_web";
		m_PageSize = 50;
		this.request = request;
	}

	public view_paginas_web getRow(int row)
	{
		return (view_paginas_web)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_paginas_web getAbsRow(int row)
	{
		return (view_paginas_web)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_paginas_web pNode = new view_paginas_web();

			pNode.setID_Pagina(m_RS.getString("ID_Pagina"));
			pNode.setID_Plantilla(m_RS.getString("ID_Plantilla"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setTitulo(m_RS.getString("Titulo"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setID_Complemento(m_RS.getString("ID_Complemento"));
			pNode.setID_Complemento2(m_RS.getString("ID_Complemento2"));
			pNode.setID_Complemento3(m_RS.getString("ID_Complemento3"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
