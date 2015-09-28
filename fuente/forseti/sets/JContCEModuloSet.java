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

public class JContCEModuloSet extends JManejadorSet
{
	public JContCEModuloSet(HttpServletRequest request, String tipo)
	{
		m_Select = " * FROM VIEW_CONT_CE_MODULO_" + tipo;
		m_PageSize = 50;
		this.request = request;
	}

	public view_cont_ce_modulo getRow(int row)
	{
		return (view_cont_ce_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cont_ce_modulo getAbsRow(int row)
	{
		return (view_cont_ce_modulo)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cont_ce_modulo pNode = new view_cont_ce_modulo();

			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setCerrado(m_RS.getBoolean("Cerrado"));
			pNode.setGenerado(m_RS.getString("Generado"));
			pNode.setErrores(m_RS.getInt("Errores"));
			pNode.setAlertas(m_RS.getInt("Alertas"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
