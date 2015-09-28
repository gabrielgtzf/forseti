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

public class JContaPolizasDetalleCESet extends JManejadorSet
{
	public  JContaPolizasDetalleCESet(HttpServletRequest request)
	{
		m_Select = " * FROM view_cont_polizas_detalle_ce ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cont_polizas_detalle_ce getRow(int row)
	{
		return (view_cont_polizas_detalle_ce)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cont_polizas_detalle_ce getAbsRow(int row)
	{
		return (view_cont_polizas_detalle_ce)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_cont_polizas_detalle_ce pNode = new view_cont_polizas_detalle_ce();

			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setDebe(m_RS.getDouble("Debe"));
			pNode.setHaber(m_RS.getDouble("Haber"));
			pNode.setID(m_RS.getLong("ID"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setNumero(m_RS.getString("Numero"));
			pNode.setParcial(m_RS.getDouble("Parcial"));
			pNode.setPart(m_RS.getInt("Part"));
			pNode.setTC(m_RS.getDouble("TC"));
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
