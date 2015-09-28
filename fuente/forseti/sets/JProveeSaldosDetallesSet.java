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

public class JProveeSaldosDetallesSet extends JManejadorSet
{
	public JProveeSaldosDetallesSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_provee_saldos_detalles_conapl";
		m_PageSize = 50;
		this.request = request;
	}

	public view_provee_saldos_detalles getRow(int row)
	{
		return (view_provee_saldos_detalles)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_provee_saldos_detalles getAbsRow(int row)
	{
		return (view_provee_saldos_detalles)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_provee_saldos_detalles pNode = new view_provee_saldos_detalles();

			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setID_Entidad(m_RS.getByte("ID_Entidad"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setID_Clave(m_RS.getInt("ID_Clave"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_CXP(m_RS.getInt("ID_CXP"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setID_Moneda(m_RS.getByte("ID_Moneda"));
			pNode.setSimbolo(m_RS.getString("Simbolo"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setSaldoIni(m_RS.getFloat("SaldoIni"));
			pNode.setSaldoFin(m_RS.getFloat("SaldoFin"));
			pNode.setID_Aplicacion(m_RS.getInt("ID_Aplicacion"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
