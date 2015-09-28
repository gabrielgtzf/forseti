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

public class JContPolizasCETodoSet extends JManejadorSet
{
	public JContPolizasCETodoSet(HttpServletRequest request, int mes, int ano)
	{
		m_Select = " * FROM view_cont_polizas_ce_todo ";
		String sql = "select * FROM view_cont_polizas_ce_todo('" + mes + "','" + ano + "') as (";
		sql += "cl integer, tl character(3), id integer, part integer, tipo integer, num text, fecha timestamp, " +
				" concepto text, status text, DetCuenta text, DetConcepto text, DetMoneda text, DetParcial numeric(19,4), " +
				" DetTC numeric(19,4), DetDebe numeric(19,4), DetHaber numeric(19,4), ChqNum text, ChqBanco text, " +
				" ChqCtaOri text, ChqFecha timestamp, ChqMonto numeric(19,4), ChqBenef text, ChqRFC text, TrnCtaOri text, " +
				" TrnBancoOri text, TrnMonto numeric(19,4), TrnCtaDest text, TrnBancoDest text, TrnFecha timestamp, " +
				" TrnBenef text, TrnRFC text, XmlUUID_CFDI text, XmlMonto numeric(19,4), XmlRFC text ); ";
		setSQL(sql);
        m_PageSize = 50;
		this.request = request;
	}

	public view_cont_polizas_ce_todo getRow(int row)
	{
		return (view_cont_polizas_ce_todo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cont_polizas_ce_todo getAbsRow(int row)
	{
		return (view_cont_polizas_ce_todo)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cont_polizas_ce_todo pNode = new view_cont_polizas_ce_todo();

			pNode.setCL(m_RS.getInt("CL"));
			pNode.setTL(m_RS.getString("TL"));
			pNode.setID(m_RS.getInt("ID"));
			pNode.setPart(m_RS.getInt("Part"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setNum(m_RS.getString("Num"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setDetCuenta(m_RS.getString("DetCuenta"));
			pNode.setDetConcepto(m_RS.getString("DetConcepto"));
			pNode.setDetMoneda(m_RS.getString("DetMoneda"));
			pNode.setDetParcial(m_RS.getFloat("DetParcial"));
			pNode.setDetTC(m_RS.getFloat("DetTC"));
			pNode.setDetDebe(m_RS.getFloat("DetDebe"));
			pNode.setDetHaber(m_RS.getFloat("DetHaber"));
			pNode.setChqNum(m_RS.getString("ChqNum"));
			pNode.setChqBanco(m_RS.getString("ChqBanco"));
			pNode.setChqCtaOri(m_RS.getString("ChqCtaOri"));
			pNode.setChqFecha(m_RS.getDate("ChqFecha"));
			pNode.setChqMonto(m_RS.getFloat("ChqMonto"));
			pNode.setChqBenef(m_RS.getString("ChqBenef"));
			pNode.setChqRFC(m_RS.getString("ChqRFC"));
			pNode.setTrnCtaOri(m_RS.getString("TrnCtaOri"));
			pNode.setTrnBancoOri(m_RS.getString("TrnBancoOri"));
			pNode.setTrnMonto(m_RS.getFloat("TrnMonto"));
			pNode.setTrnCtaDest(m_RS.getString("TrnCtaDest"));
			pNode.setTrnBancoDest(m_RS.getString("TrnBancoDest"));
			pNode.setTrnFecha(m_RS.getDate("TrnFecha"));
			pNode.setTrnBenef(m_RS.getString("TrnBenef"));
			pNode.setTrnRFC(m_RS.getString("TrnRFC"));
			pNode.setXmlUUID_CFDI(m_RS.getString("XmlUUID_CFDI"));
			pNode.setXmlMonto(m_RS.getFloat("XmlMonto"));
			pNode.setXmlRFC(m_RS.getString("XmlRFC"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
