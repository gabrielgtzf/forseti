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

public class JBancosCuentasSaldosSet extends JManejadorSet
{
	public JBancosCuentasSaldosSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_bancos_cuentas_saldos";
		m_PageSize = 50;
		this.request = request;
	}

	public view_bancos_cuentas_saldos getRow(int row)
	{
		return (view_bancos_cuentas_saldos)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_bancos_cuentas_saldos getAbsRow(int row)
	{
		return (view_bancos_cuentas_saldos)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_bancos_cuentas_saldos pNode = new view_bancos_cuentas_saldos();

			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setClave(m_RS.getByte("Clave"));
			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setCC(m_RS.getString("CC"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setFijo(m_RS.getBoolean("Fijo"));
			pNode.setID_Moneda(m_RS.getByte("ID_Moneda"));
			pNode.setSimbolo(m_RS.getString("Simbolo"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setSaldoIni(m_RS.getFloat("SaldoIni"));
			pNode.setSaldoFin(m_RS.getFloat("SaldoFin"));
			pNode.setCC_SaldoIni(m_RS.getFloat("CC_SaldoIni"));
			pNode.setCC_SaldoFin(m_RS.getFloat("CC_SaldoFin"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
