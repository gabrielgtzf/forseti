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

public class JGastosPlantillasTempSet extends JManejadorSet
{
	public JGastosPlantillasTempSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_gastos_plantillas_temporizador";
		m_PageSize = 50;
		this.request = request;
	}

	public view_gastos_plantillas_temporizador getRow(int row)
	{
		return (view_gastos_plantillas_temporizador)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_gastos_plantillas_temporizador getAbsRow(int row)
	{
		return (view_gastos_plantillas_temporizador)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_gastos_plantillas_temporizador pNode = new view_gastos_plantillas_temporizador();

			pNode.setID_Plantilla(m_RS.getInt("ID_Plantilla"));
			pNode.setNumero(m_RS.getInt("Numero"));
			pNode.setID_Entidad(m_RS.getByte("ID_Entidad"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setTipoDesc(m_RS.getString("TipoDesc"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setTemporizador(m_RS.getByte("Temporizador"));
			pNode.setTempDesc(m_RS.getString("TempDesc"));
			pNode.setFechaIni(m_RS.getDate("FechaIni"));
			pNode.setFechaApl(m_RS.getDate("FechaApl"));
			pNode.setID_Nota(m_RS.getString("ID_Nota"));
			pNode.setTotal(m_RS.getFloat("Total"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
