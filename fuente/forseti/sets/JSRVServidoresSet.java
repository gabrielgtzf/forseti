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

public class JSRVServidoresSet extends JManejadorSet
{
	public JSRVServidoresSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_srv_servidores";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_srv_servidores getRow(int row)
	{
		return (tbl_srv_servidores)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_srv_servidores getAbsRow(int row)
	{
		return (tbl_srv_servidores)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_srv_servidores pNode = new tbl_srv_servidores();

			pNode.setID_Servidor(m_RS.getString("ID_Servidor"));
			pNode.setUsuario(m_RS.getString("Usuario"));
			pNode.setPass(m_RS.getString("Pass"));
			pNode.setFechaAlta(m_RS.getDate("FechaAlta"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setCompania(m_RS.getString("Compania"));
			pNode.setDireccion(m_RS.getString("Direccion"));
			pNode.setPoblacion(m_RS.getString("Poblacion"));
			pNode.setCP(m_RS.getString("CP"));
			pNode.setMail(m_RS.getString("Mail"));
			pNode.setWeb(m_RS.getString("Web"));
			pNode.setTel(m_RS.getString("Tel"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
