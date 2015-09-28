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

public class JProdProdSetDetV2 extends JManejadorSet
{
	public JProdProdSetDetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_prod_reportes_det ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_prod_reportes_det getRow(int row)
	{
		return (view_prod_reportes_det)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_prod_reportes_det getAbsRow(int row)
	{
		return (view_prod_reportes_det)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_prod_reportes_det pNode = new view_prod_reportes_det();

			pNode.setID_Reporte(m_RS.getLong("ID_Reporte"));
			pNode.setPartida(m_RS.getByte("Partida"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setMasMenos(m_RS.getFloat("MasMenos"));
			pNode.setLote(m_RS.getString("Lote"));
			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setFormula(m_RS.getString("Formula"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setID_Formula(m_RS.getLong("ID_Formula"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setNumProc(m_RS.getByte("NumProc"));
			pNode.setActualProc(m_RS.getByte("ActualProc"));
			pNode.setTerminada(m_RS.getBoolean("Terminada"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
