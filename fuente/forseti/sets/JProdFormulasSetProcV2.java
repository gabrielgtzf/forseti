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

public class JProdFormulasSetProcV2 extends JManejadorSet
{
	public JProdFormulasSetProcV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_prod_formulas_proc ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_prod_formulas_proc getRow(int row)
	{
		return (view_prod_formulas_proc)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_prod_formulas_proc getAbsRow(int row)
	{
		return (view_prod_formulas_proc)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_prod_formulas_proc pNode = new view_prod_formulas_proc();

			pNode.setID_Formula(m_RS.getInt("ID_Formula"));
			pNode.setID_Proceso(m_RS.getInt("ID_Proceso"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setTiempo(m_RS.getInt("Tiempo"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setID_SubProd(m_RS.getString("ID_SubProd"));
			pNode.setPorcentaje(m_RS.getFloat("Porcentaje"));
			pNode.setMasMenos(m_RS.getFloat("MasMenos"));
			
			  
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
