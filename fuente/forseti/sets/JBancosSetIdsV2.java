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

public class JBancosSetIdsV2 extends JManejadorSet
{
	public JBancosSetIdsV2(HttpServletRequest request, String usuario, String tipo, String entidad)
	{
		m_Select = " * FROM view_bancos_movimientos_modulo_id ";
		String sql = "select * from view_bancos_movimientos_modulo_id('" + usuario + "','" + tipo + "','" + entidad + "') as ( id_usuario varchar, tipo smallint, id smallint, cuenta varchar, descripcion varchar, ref int, fmt_dep varchar, fmt_ret varchar, cc char(19), estatus char, saldo numeric, saldototal numeric, saldoaplicado numeric, dsbc numeric, rpc numeric, id_moneda int, tc numeric, fijo bit)";
        setSQL(sql);
		m_PageSize = 50;
		this.request = request;
	}

	public view_bancos_movimientos_modulo_id getRow(int row)
	{
		return (view_bancos_movimientos_modulo_id)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_bancos_movimientos_modulo_id getAbsRow(int row)
	{
		return (view_bancos_movimientos_modulo_id)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_bancos_movimientos_modulo_id pNode = new view_bancos_movimientos_modulo_id();

			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setID(m_RS.getByte("ID"));
			pNode.setCC(m_RS.getString("CC"));
			pNode.setEstatus(m_RS.getString("Estatus"));
			pNode.setRef(m_RS.getLong("Ref"));
			pNode.setFmt_Dep(m_RS.getString("Fmt_Dep"));
			pNode.setFmt_Ret(m_RS.getString("Fmt_Ret"));
			pNode.setSaldo(m_RS.getDouble("Saldo"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setSaldoAplicado(m_RS.getDouble("SaldoAplicado"));
			pNode.setSaldoTotal(m_RS.getDouble("SaldoTotal"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setDSBC(m_RS.getDouble("DSBC"));
			pNode.setRPC(m_RS.getDouble("RPC"));
			pNode.setID_Moneda(m_RS.getByte("ID_Moneda"));
			pNode.setTC(m_RS.getDouble("TC"));
			pNode.setFijo(m_RS.getBoolean("Fijo"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
