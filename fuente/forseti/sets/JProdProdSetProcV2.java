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

public class JProdProdSetProcV2 extends JManejadorSet
{
	public JProdProdSetProcV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_prod_reportes_proc";
		m_PageSize = 50;
		this.request = request;
	}

	public view_prod_reportes_proc getRow(int row)
	{
		return (view_prod_reportes_proc)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_prod_reportes_proc getAbsRow(int row)
	{
		return (view_prod_reportes_proc)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_prod_reportes_proc pNode = new view_prod_reportes_proc();

			pNode.setID_Reporte(m_RS.getLong("ID_Reporte"));
			pNode.setPartida(m_RS.getByte("Partida"));
			pNode.setID_Proceso(m_RS.getLong("ID_Proceso"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setFechaSP(m_RS.getDate("FechaSP"));
			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setUnidad(m_RS.getString("Unidad"));
			pNode.setPorcentaje(m_RS.getFloat("Porcentaje"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setMasMenos(m_RS.getFloat("MasMenos"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			pNode.setID_PolSP(m_RS.getInt("ID_PolSP"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
