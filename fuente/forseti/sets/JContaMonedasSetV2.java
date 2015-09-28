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

public class JContaMonedasSetV2 extends JManejadorSet
{
	public JContaMonedasSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_cont_monedas_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cont_monedas_modulo getRow(int row)
	{
		return (view_cont_monedas_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cont_monedas_modulo getAbsRow(int row)
	{
		return (view_cont_monedas_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_cont_monedas_modulo pNode = new view_cont_monedas_modulo();

			pNode.setClave(m_RS.getInt("Clave"));
			pNode.setMoneda(m_RS.getString("Moneda"));
			pNode.setSimbolo(m_RS.getString("Simbolo"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setDesplazamiento(m_RS.getString("Desplazamiento"));
			pNode.setDA(m_RS.getBoolean("DA"));
			pNode.setID_SatMoneda(m_RS.getString("ID_SatMoneda"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
