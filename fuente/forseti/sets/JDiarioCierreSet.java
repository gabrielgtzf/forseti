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

public class JDiarioCierreSet extends JManejadorSet
{
	public JDiarioCierreSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_NOM_DIARIO_CIERRE";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_DIARIO_CIERRE getRow(int row)
	{
		return (VIEW_DIARIO_CIERRE)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_DIARIO_CIERRE getAbsRow(int row)
	{
		return (VIEW_DIARIO_CIERRE)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_DIARIO_CIERRE pNode = new VIEW_DIARIO_CIERRE();

			pNode.setID_FechaMovimiento(m_RS.getDate("ID_FechaMovimiento"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setDesde(m_RS.getDate("Desde"));
			pNode.setDesdeHora(m_RS.getTime("Desde"));
			pNode.setHasta(m_RS.getDate("Hasta"));
			pNode.setHastaHora(m_RS.getTime("Hasta"));
			pNode.setEntrada(m_RS.getDate("Entrada"));
			pNode.setEntradaHora(m_RS.getTime("Entrada"));
			pNode.setSalida(m_RS.getDate("Salida"));
			pNode.setSalidaHora(m_RS.getTime("Salida"));
			
			pNode.setHNA(m_RS.getFloat("HNA"));
			pNode.setHNP(m_RS.getFloat("HNP"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
