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

public class JReportesBind1Set extends JManejadorSet
{
	public JReportesBind1Set(HttpServletRequest request, String usuario, String tipo, String entidad)
	{
		String sql = "select * from view_reportes('" + usuario + "','" + tipo + "','" + entidad + "') as (id_usuario varchar, id_report smallint, description varchar, tipo varchar, subtipo varchar, clave varchar, graficar bit(1))";
		setSQL(sql);
		m_PageSize = 50;
		this.request = request;
	}

	public JReportesBind1Set(HttpServletRequest request)
	{
		m_Select = " ID_Report, Description, Tipo, SubTipo, Clave, Graficar FROM view_reportes ";
		m_PageSize = 50;
		this.request = request;
	}
	
	public TBL_REPORTS getRow(int row)
	{
		return (TBL_REPORTS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_REPORTS getAbsRow(int row)
	{
		return (TBL_REPORTS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_REPORTS pNode = new TBL_REPORTS();

            pNode.setID_Report(m_RS.getInt("ID_Report"));
            pNode.setDescription(m_RS.getString("Description"));
            pNode.setTipo(m_RS.getString("Tipo"));
            pNode.setSubTipo(m_RS.getString("SubTipo"));
            pNode.setClave(m_RS.getString("Clave"));
            pNode.setGraficar(m_RS.getBoolean("Graficar"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
