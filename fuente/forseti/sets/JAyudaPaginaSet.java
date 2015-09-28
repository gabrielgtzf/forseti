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

public class JAyudaPaginaSet extends JManejadorSet
{
	public JAyudaPaginaSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_AYUDA_PAGINAS";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_AYUDA_PAGINAS getRow(int row)
	{
		return (TBL_AYUDA_PAGINAS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_AYUDA_PAGINAS getAbsRow(int row)
	{
		return (TBL_AYUDA_PAGINAS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_AYUDA_PAGINAS pNode = new TBL_AYUDA_PAGINAS();

			pNode.setID_Pagina(m_RS.getString("ID_Pagina"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setBusqueda(m_RS.getString("Busqueda"));
			pNode.setCuerpo(m_RS.getString("Cuerpo"));
			pNode.setStatus(m_RS.getInt("Status"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setID_Alternativo(m_RS.getString("ID_Alternativo"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
