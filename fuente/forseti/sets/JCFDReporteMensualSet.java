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

import forseti.JManejadorSet;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

public class JCFDReporteMensualSet extends JManejadorSet
{
    public JCFDReporteMensualSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_CFD_REPORTE_MENSUAL ";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_CFD_REPORTE_MENSUAL getRow(int row)
	{
		return (TBL_CFD_REPORTE_MENSUAL)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_CFD_REPORTE_MENSUAL getAbsRow(int row)
	{
		return (TBL_CFD_REPORTE_MENSUAL)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_CFD_REPORTE_MENSUAL pNode = new TBL_CFD_REPORTE_MENSUAL();

			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setArchivo(m_RS.getString("Archivo"));
			pNode.setCerrado(m_RS.getBoolean("Cerrado"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
