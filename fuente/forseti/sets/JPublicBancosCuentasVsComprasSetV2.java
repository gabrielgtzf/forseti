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

public class JPublicBancosCuentasVsComprasSetV2 extends JManejadorSet
{
	public JPublicBancosCuentasVsComprasSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_public_bancos_cuentas_vs_compras ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_public_bancos_cuentas_vs_compras getRow(int row)
	{
		return (view_public_bancos_cuentas_vs_compras)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_public_bancos_cuentas_vs_compras getAbsRow(int row)
	{
		return (view_public_bancos_cuentas_vs_compras)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_public_bancos_cuentas_vs_compras pNode = new view_public_bancos_cuentas_vs_compras();

			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setClave(m_RS.getByte("Clave"));
			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setSigCheque(m_RS.getInt("SigCheque"));
			pNode.setID_EntidadCompra(m_RS.getInt("ID_EntidadCompra"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
