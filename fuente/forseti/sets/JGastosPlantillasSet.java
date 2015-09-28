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

public class JGastosPlantillasSet extends JManejadorSet
{
	public JGastosPlantillasSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_gastos_plantillas_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_gastos_plantillas_modulo getRow(int row)
	{
		return (view_gastos_plantillas_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_gastos_plantillas_modulo getAbsRow(int row)
	{
		return (view_gastos_plantillas_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_gastos_plantillas_modulo pNode = new view_gastos_plantillas_modulo();

			pNode.setAcreedor(m_RS.getString("Acreedor"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setID_Entidad(m_RS.getInt("ID_Entidad"));
			pNode.setID_Pol(m_RS.getLong("ID_Pol"));
			pNode.setID_Plantilla(m_RS.getLong("ID_Plantilla"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setNumero(m_RS.getLong("Numero"));
			pNode.setPol(m_RS.getString("Pol"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setReferencia(m_RS.getString("Referencia"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setID_Acreedor(m_RS.getLong("ID_Acreedor"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
