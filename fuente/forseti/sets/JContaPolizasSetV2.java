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

public class JContaPolizasSetV2 extends JManejadorSet
{
	public JContaPolizasSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_cont_polizas_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cont_polizas_modulo getRow(int row)
	{
		return (view_cont_polizas_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cont_polizas_modulo getAbsRow(int row)
	{
		return (view_cont_polizas_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_cont_polizas_modulo pNode = new view_cont_polizas_modulo();

			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setDebe(m_RS.getFloat("Debe"));
			pNode.setEstatus(m_RS.getString("Estatus"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setHaber(m_RS.getFloat("Haber"));
			pNode.setID(m_RS.getLong("ID"));
			pNode.setNum(m_RS.getInt("Num"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setID_Clasificacion(m_RS.getString("ID_Clasificacion"));
			pNode.setCE(m_RS.getString("CE"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
