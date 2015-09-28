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

public class JClientCXCPagosSetV2 extends JManejadorSet
{
	public JClientCXCPagosSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_client_cxc_pagos";
		m_PageSize = 50;
		this.request = request;
	}

	public view_client_cxc_pagos getRow(int row)
	{
		return (view_client_cxc_pagos)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_client_cxc_pagos getAbsRow(int row)
	{
		return (view_client_cxc_pagos)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_client_cxc_pagos pNode = new view_client_cxc_pagos();

			pNode.setID_CXC(m_RS.getInt("ID_CXC"));
			pNode.setNum(m_RS.getInt("Num"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			pNode.setPol(m_RS.getString("Pol"));
			pNode.setFormaPago(m_RS.getString("FormaPago"));
			pNode.setID_Mov(m_RS.getInt("ID_Mov"));
			pNode.setPago(m_RS.getString("Pago"));
			pNode.setID_Concepto(m_RS.getByte("ID_Concepto"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setObs(m_RS.getString("Obs"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
