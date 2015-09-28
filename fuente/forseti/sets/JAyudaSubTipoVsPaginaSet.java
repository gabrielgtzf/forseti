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

public class JAyudaSubTipoVsPaginaSet extends JManejadorSet
{
	public  JAyudaSubTipoVsPaginaSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_ayuda_subtipo_vs_pagina";
		m_PageSize = 50;
		this.request = request;
	}

	public view_ayuda_subtipo_vs_pagina getRow(int row)
	{
		return (view_ayuda_subtipo_vs_pagina)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_ayuda_subtipo_vs_pagina getAbsRow(int row)
	{
		return (view_ayuda_subtipo_vs_pagina)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_ayuda_subtipo_vs_pagina pNode = new view_ayuda_subtipo_vs_pagina();

			pNode.setID_Pagina(m_RS.getString("ID_Pagina"));
			pNode.setID_SubTipo(m_RS.getString("ID_SubTipo"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setEnlazado(m_RS.getBoolean("Enlazado"));
			pNode.setStatus(m_RS.getInt("Status"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
