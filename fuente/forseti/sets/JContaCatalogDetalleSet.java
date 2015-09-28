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

public class JContaCatalogDetalleSet extends JManejadorSet
{
	public JContaCatalogDetalleSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_cont_catalogo_detalle";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cont_catalogo_detalle getRow(int row)
	{
		return (view_cont_catalogo_detalle)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cont_catalogo_detalle getAbsRow(int row)
	{
		return (view_cont_catalogo_detalle)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cont_catalogo_detalle pNode = new view_cont_catalogo_detalle();

			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setAcum(m_RS.getBoolean("Acum"));
			pNode.setSaldoInicial(m_RS.getFloat("SaldoInicial"));
			pNode.setDebe(m_RS.getFloat("Debe"));
			pNode.setHaber(m_RS.getFloat("Haber"));
			pNode.setSaldoFinal(m_RS.getFloat("SaldoFinal"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
