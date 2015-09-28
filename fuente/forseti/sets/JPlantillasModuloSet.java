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

public class JPlantillasModuloSet extends JManejadorSet
{
	public JPlantillasModuloSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_nom_plantillas_modulo";
		m_PageSize = 50;
		this.request = request;
	}

	public view_plantillas_modulo getRow(int row)
	{
		return (view_plantillas_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_plantillas_modulo getAbsRow(int row)
	{
		return (view_plantillas_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_plantillas_modulo pNode = new view_plantillas_modulo();

			pNode.setID_Plantilla(m_RS.getInt("ID_Plantilla"));
			pNode.setbID_Empleado(m_RS.getBoolean("bID_Empleado"));
			pNode.setbNomina(m_RS.getBoolean("bNomina"));
			pNode.setbTipo_Nomina(m_RS.getBoolean("bTipo_Nomina"));
			pNode.setbCompania_Sucursal(m_RS.getBoolean("bCompania_Sucursal"));
			pNode.setCompania(m_RS.getString("Compania"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setMovimiento(m_RS.getString("Movimiento"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setAplicacion(m_RS.getString("Aplicacion"));
			pNode.setCalcular(m_RS.getBoolean("Calcular"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
