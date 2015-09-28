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

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import forseti.JManejadorSet;

public class JInvServExistenciasSetV2 extends JManejadorSet
{
	public JInvServExistenciasSetV2(HttpServletRequest request, String tipo)
	{
		if(tipo.equals("P"))
			m_Select = " * FROM view_invserv_inventarios_existencias ";
		else
			m_Select = " * FROM view_invserv_gastos_existencias ";
		
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_inventarios_existencias getRow(int row)
	{
		return (view_invserv_inventarios_existencias)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_inventarios_existencias getAbsRow(int row)
	{
		return (view_invserv_inventarios_existencias)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_inventarios_existencias pNode = new view_invserv_inventarios_existencias();

			pNode.setBodega(m_RS.getString("Bodega"));
			pNode.setClave(m_RS.getString("Clave"));
			pNode.setExistencia(m_RS.getFloat("Existencia"));
			pNode.setID_Bodega(m_RS.getInt("ID_Bodega"));
			pNode.setStockMax(m_RS.getFloat("StockMax"));
			pNode.setStockMin(m_RS.getFloat("StockMin"));
			pNode.setTipo(m_RS.getString("Tipo"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
