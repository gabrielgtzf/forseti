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

public class JMsjSet extends JManejadorSet
{
	public JMsjSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_msj";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_msj getRow(int row)
	{
		return (tbl_msj)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_msj getAbsRow(int row)
	{
		return (tbl_msj)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			tbl_msj pNode = new tbl_msj();

			pNode.setAlc(m_RS.getString("Alc"));
			pNode.setMod(m_RS.getString("Mod"));
			pNode.setSub(m_RS.getString("Sub"));
			pNode.setElm(m_RS.getString("Elm"));
			pNode.setMsj1(m_RS.getString("Msj1"));
			pNode.setMsj2(m_RS.getString("Msj2"));
			pNode.setMsj3(m_RS.getString("Msj3"));
			pNode.setMsj4(m_RS.getString("Msj4"));
			pNode.setMsj5(m_RS.getString("Msj5"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}



}
