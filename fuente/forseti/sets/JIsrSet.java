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

public class JIsrSet extends JManejadorSet
{
	public JIsrSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_NOM_ISR";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_ISR getRow(int row)
	{
		return (VIEW_ISR)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_ISR getAbsRow(int row)
	{
		return (VIEW_ISR)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_ISR pNode = new VIEW_ISR();

			pNode.setID_Isr(m_RS.getByte("ID_Isr"));
			pNode.setLimite_Inferior(m_RS.getFloat("Limite_Inferior"));
			pNode.setLimite_Superior(m_RS.getFloat("Limite_Superior"));
			pNode.setCuota_Fija(m_RS.getFloat("Cuota_Fija"));
			pNode.setPorcentaje_Exd(m_RS.getFloat("Porcentaje_Exd"));
			pNode.setSubsidio(m_RS.getFloat("Subsidio"));
			pNode.setSubsidio_SIM(m_RS.getFloat("Subsidio_SIM"));
			pNode.setLimite_Inferior_Anual(m_RS.getFloat("Limite_Inferior_Anual"));
			pNode.setLimite_Superior_Anual(m_RS.getFloat("Limite_Superior_Anual"));
			pNode.setCuota_Fija_Anual(m_RS.getFloat("Cuota_Fija_Anual"));
			pNode.setPorcentaje_Exd_Anual(m_RS.getFloat("Porcentaje_Exd_Anual"));
			pNode.setSubsidio_Anual(m_RS.getFloat("Subsidio_Anual"));
			pNode.setSubsidio_SIM_Anual(m_RS.getFloat("Subsidio_SIM_Anual"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
