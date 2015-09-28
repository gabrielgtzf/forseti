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

public class JMovimientosNomSet extends JManejadorSet
{
	public JMovimientosNomSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_NOM_MOVIMIENTOS_NOMINA_MODULO";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_MOVIMIENTOS_NOMINA getRow(int row)
	{
		return (TBL_MOVIMIENTOS_NOMINA)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_MOVIMIENTOS_NOMINA getAbsRow(int row)
	{
		return (TBL_MOVIMIENTOS_NOMINA)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_MOVIMIENTOS_NOMINA pNode = new TBL_MOVIMIENTOS_NOMINA();

			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setTipo_Movimiento(m_RS.getString("Tipo_Movimiento"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setDeduccion(m_RS.getBoolean("Deduccion"));
			pNode.setIMSS(m_RS.getBoolean("IMSS"));
			pNode.setISPT(m_RS.getBoolean("ISPT"));
			pNode.setDOSPOR(m_RS.getBoolean("DOSPOR"));
			pNode.setSAR(m_RS.getBoolean("SAR"));
			pNode.setINFONAVIT(m_RS.getBoolean("INFONAVIT"));
			pNode.setPTU(m_RS.getBoolean("PTU"));
			pNode.setID_SAT(m_RS.getString("ID_SAT"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
