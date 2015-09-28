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

public class JBancosIdsSet extends JManejadorSet
{
	public JBancosIdsSet(HttpServletRequest request, String usuario, String tipo, String entidad)
	{
		m_Select = " * FROM view_bancos_movimientos_modulo_ids";
		String sql = "select * from view_bancos_movimientos_modulo_ids('" + usuario + "','" + tipo + "','" + entidad + "') as ( id_usuario varchar, tipo smallint, id smallint, cuenta varchar)";
        setSQL(sql);
        m_PageSize = 50;
		this.request = request;
	}

	public view_bancos_movimientos_modulo_ids getRow(int row)
	{
		return (view_bancos_movimientos_modulo_ids)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_bancos_movimientos_modulo_ids getAbsRow(int row)
	{
		return (view_bancos_movimientos_modulo_ids)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_bancos_movimientos_modulo_ids pNode = new view_bancos_movimientos_modulo_ids();

			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setID(m_RS.getByte("ID"));
			pNode.setCuenta(m_RS.getString("Cuenta"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
