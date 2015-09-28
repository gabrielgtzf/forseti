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

public class JContPolizasDetalleCEComprobantesSet extends JManejadorSet
{
	public JContPolizasDetalleCEComprobantesSet(HttpServletRequest request)
	{
		m_Select = " * FROM tbl_cont_polizas_detalle_ce_comprobantes";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_cont_polizas_detalle_ce_comprobantes getRow(int row)
	{
		return (tbl_cont_polizas_detalle_ce_comprobantes)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_cont_polizas_detalle_ce_comprobantes getAbsRow(int row)
	{
		return (tbl_cont_polizas_detalle_ce_comprobantes)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_cont_polizas_detalle_ce_comprobantes pNode = new tbl_cont_polizas_detalle_ce_comprobantes();

			pNode.setID(m_RS.getInt("ID"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			pNode.setID_Part(m_RS.getInt("ID_Part"));
			pNode.setUUID_CFDI(m_RS.getString("UUID_CFDI"));
			pNode.setMonto(m_RS.getDouble("Monto"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setMoneda(m_RS.getString("Moneda"));
			pNode.setTipCamb(m_RS.getDouble("TipCamb"));
			pNode.setCFD_CBB_Serie(m_RS.getString("CFD_CBB_Serie"));
			pNode.setCFD_CBB_NumFol(m_RS.getInt("CFD_CBB_NumFol"));
			pNode.setNumFactExt(m_RS.getString("NumFactExt"));
			pNode.setTAXID(m_RS.getString("TAXID"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
