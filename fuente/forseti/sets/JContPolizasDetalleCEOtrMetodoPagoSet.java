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

public class JContPolizasDetalleCEOtrMetodoPagoSet extends JManejadorSet
{
	public JContPolizasDetalleCEOtrMetodoPagoSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_cont_polizas_detalle_ce_otrmetodopago";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_cont_polizas_detalle_ce_otrmetodopago getRow(int row)
	{
		return (tbl_cont_polizas_detalle_ce_otrmetodopago)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_cont_polizas_detalle_ce_otrmetodopago getAbsRow(int row)
	{
		return (tbl_cont_polizas_detalle_ce_otrmetodopago)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_cont_polizas_detalle_ce_otrmetodopago pNode = new tbl_cont_polizas_detalle_ce_otrmetodopago();

			pNode.setID(m_RS.getInt("ID"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			pNode.setID_Part(m_RS.getInt("ID_Part"));
			pNode.setMetPagoPol(m_RS.getString("MetPagoPol"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setBenef(m_RS.getString("Benef"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setMonto(m_RS.getDouble("Monto"));
			pNode.setMoneda(m_RS.getString("Moneda"));
			pNode.setTipCamb(m_RS.getDouble("TipCamb"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
