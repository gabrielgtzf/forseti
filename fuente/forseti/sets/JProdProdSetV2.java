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

public class JProdProdSetV2 extends JManejadorSet
{
	public JProdProdSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_prod_reportes_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_prod_reportes_modulo getRow(int row)
	{
		return (view_prod_reportes_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_prod_reportes_modulo getAbsRow(int row)
	{
		return (view_prod_reportes_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_prod_reportes_modulo pNode = new view_prod_reportes_modulo();

			pNode.setID_Reporte(m_RS.getLong("ID_Reporte"));
			pNode.setCDA(m_RS.getBoolean("CDA"));
			pNode.setID_Entidad(m_RS.getInt("ID_Entidad"));
			pNode.setNumero(m_RS.getLong("Numero"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setNumProc(m_RS.getLong("NumProc"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setBodega_MP(m_RS.getString("Bodega_MP"));
			pNode.setBodega_PT(m_RS.getString("Bodega_PT"));
			pNode.setID_BodegaMP(m_RS.getInt("ID_BodegaMP"));
			pNode.setID_BodegaPT(m_RS.getInt("ID_BodegaPT"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setActual(m_RS.getByte("Actual"));
			pNode.setDirecta(m_RS.getBoolean("Directa"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
