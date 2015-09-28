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
 
public class JNominaEntidadesSetIds extends JManejadorSet
{
	public JNominaEntidadesSetIds(HttpServletRequest request)
	{
		m_Select = " * FROM view_nomina_entidades_ids";
		m_PageSize = 50;
		this.request = request;
	}

	public JNominaEntidadesSetIds(HttpServletRequest request, String usuario, String entidad) 
	{
		m_Select = " * FROM view_nomina_entidades_ids ";
		String sql = "select * from view_nomina_entidades_ids('" + usuario + "','" + entidad + 
				"') as ( " +
				"id_usuario varchar, ID_Sucursal smallint, Tipo smallint, Periodo char(3), Descripcion varchar, Nombre varchar, Fmt_Nomina varchar, Fmt_Recibo varchar, CFD bit(2))";
		setSQL(sql);
		m_PageSize = 50;
		this.request = request;
	}

	public view_nomina_entidades_ids getRow(int row)
	{
		return (view_nomina_entidades_ids)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_nomina_entidades_ids getAbsRow(int row)
	{
		return (view_nomina_entidades_ids)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_nomina_entidades_ids pNode = new view_nomina_entidades_ids();

			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setID_Sucursal(m_RS.getByte("ID_Sucursal"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setPeriodo(m_RS.getString("Periodo"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setFmt_Nomina(m_RS.getString("Fmt_Nomina"));
			pNode.setFmt_Recibo(m_RS.getString("Fmt_Recibo"));
			pNode.setCFD(m_RS.getBoolean("CFD"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
