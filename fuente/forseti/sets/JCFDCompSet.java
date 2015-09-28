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

public class JCFDCompSet extends JManejadorSet
{
	public JCFDCompSet(HttpServletRequest request, String tipo)
	{
		if(tipo.equals("COMPRAS"))
			m_Select = " * FROM TBL_CFDCOMP";
		else if(tipo.equals("VENTAS"))
			m_Select = " * FROM TBL_CFDVEN";
		else // Nomina
			m_Select = " * FROM TBL_CFDNOM";
		
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_CFDCOMP getRow(int row)
	{
		return (TBL_CFDCOMP)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_CFDCOMP getAbsRow(int row)
	{
		return (TBL_CFDCOMP)m_Rows.elementAt(row);
	}

	 
	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			TBL_CFDCOMP pNode = new TBL_CFDCOMP();

			pNode.setID_CFD(m_RS.getInt("ID_CFD"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setHora(m_RS.getTime("Fecha"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setImpuesto(m_RS.getFloat("Impuesto"));
			pNode.setEfecto(m_RS.getString("Efecto"));
			pNode.setPedimento(m_RS.getString("Pedimento"));
			pNode.setFechaPedimento(m_RS.getString("FechaPedimento"));
			pNode.setAduana(m_RS.getString("Aduana"));
			pNode.setFSI_Tipo(m_RS.getString("FSI_Tipo"));
			pNode.setFSI_ID(m_RS.getInt("FSI_ID"));
			pNode.setNoCertificado(m_RS.getString("NoCertificado"));
			pNode.setSello(m_RS.getString("Sello"));
			pNode.setUUID(m_RS.getString("UUID"));
			pNode.setFechaTimbre(m_RS.getDate("FechaTimbre"));
			pNode.setHoraTimbre(m_RS.getTime("FechaTimbre"));
			pNode.setNoCertificadoSat(m_RS.getString("NoCertificadoSat"));
			pNode.setSelloSat(m_RS.getString("SelloSat"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
