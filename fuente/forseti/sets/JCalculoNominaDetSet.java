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

public class JCalculoNominaDetSet extends JManejadorSet
{
	public JCalculoNominaDetSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_NOM_CALCULO_NOMINA_DET";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_CALCULO_NOMINA_DET getRow(int row)
	{
		return (VIEW_CALCULO_NOMINA_DET)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_CALCULO_NOMINA_DET getAbsRow(int row)
	{
		return (VIEW_CALCULO_NOMINA_DET)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_CALCULO_NOMINA_DET pNode = new VIEW_CALCULO_NOMINA_DET();

			pNode.setID_Nomina(m_RS.getInt("ID_Nomina"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setGravado(m_RS.getFloat("Gravado"));
			pNode.setExento(m_RS.getFloat("Exento"));
			pNode.setDeduccion(m_RS.getFloat("Deduccion"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setEsDeduccion(m_RS.getBoolean("EsDeduccion"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setID_SAT(m_RS.getString("ID_SAT"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
