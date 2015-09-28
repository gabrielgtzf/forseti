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

public class JCreditoSalarioSet extends JManejadorSet
{
	public JCreditoSalarioSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_NOM_CREDITO_SALARIO";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_CREDITO_SALARIO getRow(int row)
	{
		return (TBL_CREDITO_SALARIO)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_CREDITO_SALARIO getAbsRow(int row)
	{
		return (TBL_CREDITO_SALARIO)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_CREDITO_SALARIO pNode = new TBL_CREDITO_SALARIO();

			pNode.setID_CS(m_RS.getByte("ID_CS"));
			pNode.setIngresos_Desde(m_RS.getFloat("Ingresos_Desde"));
			pNode.setIngresos_Hasta(m_RS.getFloat("Ingresos_Hasta"));
			pNode.setCSM(m_RS.getFloat("CSM"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
