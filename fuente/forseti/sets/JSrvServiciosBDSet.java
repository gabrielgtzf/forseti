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

public class JSrvServiciosBDSet extends JManejadorSet
{
	public JSrvServiciosBDSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_srv_servicios_bd";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_srv_servicios_bd getRow(int row)
	{
		return (tbl_srv_servicios_bd)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_srv_servicios_bd getAbsRow(int row)
	{
		return (tbl_srv_servicios_bd)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_srv_servicios_bd pNode = new tbl_srv_servicios_bd();

			pNode.setID_Servidor(m_RS.getString("ID_Servidor"));
			pNode.setBasedatos(m_RS.getString("Basedatos"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setCostoMail(m_RS.getDouble("CostoMail"));
			pNode.setCostoS3MB(m_RS.getDouble("CostoS3MB"));
			pNode.setCostoSello(m_RS.getDouble("CostoSello"));
			pNode.setSaldo(m_RS.getDouble("Saldo"));
			pNode.setCobrarSello(m_RS.getBoolean("CobrarSello"));
			pNode.setCobrarMail(m_RS.getBoolean("CobrarMail"));
			pNode.setCobrarS3MB(m_RS.getBoolean("CobrarS3MB"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
