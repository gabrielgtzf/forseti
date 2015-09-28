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

public class JCalculoNominaSet extends JManejadorSet
{
	public JCalculoNominaSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_NOM_CALCULO_NOMINA";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_CALCULO_NOMINA getRow(int row)
	{
		return (VIEW_CALCULO_NOMINA)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_CALCULO_NOMINA getAbsRow(int row)
	{
		return (VIEW_CALCULO_NOMINA)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_CALCULO_NOMINA pNode = new VIEW_CALCULO_NOMINA();

			pNode.setID_Nomina(m_RS.getInt("ID_Nomina"));
			pNode.setCompania(m_RS.getString("Compania"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setNumero_Nomina(m_RS.getInt("Numero_Nomina"));
			pNode.setFecha_Desde(m_RS.getDate("Fecha_Desde"));
			pNode.setFecha_Hasta(m_RS.getDate("Fecha_Hasta"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setGravado(m_RS.getFloat("Gravado"));
			pNode.setExento(m_RS.getFloat("Exento"));
			pNode.setDeduccion(m_RS.getFloat("Deduccion"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
