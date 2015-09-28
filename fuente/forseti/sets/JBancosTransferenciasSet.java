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

public class JBancosTransferenciasSet extends JManejadorSet
{
	public JBancosTransferenciasSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_bancos_transferencias";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_bancos_transferencias getRow(int row)
	{
		return (tbl_bancos_transferencias)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_bancos_transferencias getAbsRow(int row)
	{
		return (tbl_bancos_transferencias)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			tbl_bancos_transferencias pNode = new tbl_bancos_transferencias();

			pNode.setIDMovOrigen(m_RS.getInt("IDMovOrigen"));
			pNode.setIDMovDestino(m_RS.getInt("IDMovDestino"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
