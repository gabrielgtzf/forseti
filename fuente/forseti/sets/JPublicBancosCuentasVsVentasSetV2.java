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

public class JPublicBancosCuentasVsVentasSetV2 extends JManejadorSet
{
	public JPublicBancosCuentasVsVentasSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_public_bancos_cuentas_vs_ventas ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_public_bancos_cuentas_vs_ventas getRow(int row)
	{
		return (view_public_bancos_cuentas_vs_ventas)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_public_bancos_cuentas_vs_ventas getAbsRow(int row)
	{
		return (view_public_bancos_cuentas_vs_ventas)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_public_bancos_cuentas_vs_ventas pNode = new view_public_bancos_cuentas_vs_ventas();

			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setClave(m_RS.getByte("Clave"));
			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setSigCheque(m_RS.getInt("SigCheque"));
			pNode.setID_EntidadVenta(m_RS.getInt("ID_EntidadVenta"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
