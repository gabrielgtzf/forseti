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

public class JPublicCXPAnticiposSet extends JManejadorSet
{
	public JPublicCXPAnticiposSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_provee_cxp_anticipos ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_provee_cxp_anticipos getRow(int row)
	{
		return (view_provee_cxp_anticipos)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_provee_cxp_anticipos getAbsRow(int row)
	{
		return (view_provee_cxp_anticipos)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_provee_cxp_anticipos pNode = new view_provee_cxp_anticipos();
 
			pNode.setID_CXP(m_RS.getInt("ID_CXP"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setPagada(m_RS.getByte("Pagada"));
			pNode.setID_TipoProvee(m_RS.getString("ID_TipoProvee"));
			pNode.setID_ClaveProvee(m_RS.getInt("ID_ClaveProvee"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setSaldo(m_RS.getFloat("Saldo"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
