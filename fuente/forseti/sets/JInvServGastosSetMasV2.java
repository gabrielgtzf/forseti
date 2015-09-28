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

public class JInvServGastosSetMasV2 extends JManejadorSet
{
	public JInvServGastosSetMasV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_gastos_mas ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_gastos_mas getRow(int row)
	{
		return (view_invserv_gastos_mas)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_gastos_mas getAbsRow(int row)
	{
		return (view_invserv_gastos_mas)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_gastos_mas pNode = new view_invserv_gastos_mas();

			pNode.setClave(m_RS.getString("Clave"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setUltimoCosto(m_RS.getFloat("UltimoCosto"));
			pNode.setCostoPromedio(m_RS.getFloat("CostoPromedio"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setIVA_Deducible(m_RS.getFloat("IVA_Deducible"));
			pNode.setCantidadAcum(m_RS.getFloat("CantidadAcum"));
			pNode.setMontoAcum(m_RS.getFloat("MontoAcum"));
			pNode.setImpIEPS(m_RS.getFloat("ImpIEPS"));
			pNode.setCategoriaDescripcion(m_RS.getString("CategoriaDescripcion"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
