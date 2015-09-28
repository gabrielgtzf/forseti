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

public class JContBalanzaCESet extends JManejadorSet
{
	public JContBalanzaCESet(HttpServletRequest request)
	{
		m_Select = " * FROM view_cont_balanza_ce";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cont_balanza_ce getRow(int row)
	{
		return (view_cont_balanza_ce)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cont_balanza_ce getAbsRow(int row)
	{
		return (view_cont_balanza_ce)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cont_balanza_ce pNode = new view_cont_balanza_ce();

			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setAC(m_RS.getString("AC"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setInicial(m_RS.getFloat("Inicial"));
			pNode.setCargos(m_RS.getFloat("Cargos"));
			pNode.setAbonos(m_RS.getFloat("Abonos"));
			pNode.setFinal(m_RS.getFloat("Final"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
