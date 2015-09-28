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

public class JClientSaldosSet extends JManejadorSet
{
	public JClientSaldosSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_client_saldos";
		m_PageSize = 50;
		this.request = request;
	}

	public view_client_saldos getRow(int row)
	{
		return (view_client_saldos)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_client_saldos getAbsRow(int row)
	{
		return (view_client_saldos)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_client_saldos pNode = new view_client_saldos();

			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setID_Entidad(m_RS.getByte("ID_Entidad"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setID_Clave(m_RS.getInt("ID_Clave"));
			pNode.setID_Numero(m_RS.getInt("ID_Numero"));
			pNode.setCliente(m_RS.getString("Cliente"));
			pNode.setID_CC(m_RS.getString("ID_CC"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setCC_SaldoIni(m_RS.getFloat("CC_SaldoIni"));
			pNode.setCC_SaldoFin(m_RS.getFloat("CC_SaldoFin"));
			pNode.setSaldoFin(m_RS.getString("SaldoFin"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
