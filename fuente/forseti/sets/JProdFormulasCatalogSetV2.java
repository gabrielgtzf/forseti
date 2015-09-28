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

public class JProdFormulasCatalogSetV2 extends JManejadorSet
{
	public JProdFormulasCatalogSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_prod_reportes_add ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_prod_reportes_add getRow(int row)
	{
		return (view_prod_reportes_add)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_prod_reportes_add getAbsRow(int row)
	{
		return (view_prod_reportes_add)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_prod_reportes_add pNode = new view_prod_reportes_add();

			pNode.setID_Formula(m_RS.getLong("ID_Formula"));
			pNode.setID_Prod(m_RS.getString("ID_Prod"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setNumProc(m_RS.getLong("NumProc"));
			pNode.setFormula(m_RS.getString("Formula"));
			pNode.setUnidadUnica(m_RS.getBoolean("UnidadUnica"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setPrincipal(m_RS.getBoolean("Principal"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
