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

public class JEtiquetasSet extends JManejadorSet
{
	public JEtiquetasSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_ETIQUETAS";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_ETIQUETAS getRow(int row)
	{
		return (TBL_ETIQUETAS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_ETIQUETAS getAbsRow(int row)
	{
		return (TBL_ETIQUETAS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_ETIQUETAS pNode = new TBL_ETIQUETAS();

			pNode.setID_TEtiqueta(m_RS.getString("ID_TEtiqueta"));
			pNode.setID_Etiqueta(m_RS.getString("ID_Etiqueta"));
			pNode.setInstrucciones(m_RS.getString("Instrucciones"));
			pNode.setCodigo(m_RS.getString("Codigo"));
			pNode.setCodigo2(m_RS.getString("Codigo2"));
			pNode.setCodigo3(m_RS.getString("Codigo3"));
			pNode.setCodigo4(m_RS.getString("Codigo4"));
			pNode.setPosicion(m_RS.getInt("Posicion"));
			pNode.setPart(m_RS.getByte("Part"));
			pNode.setOpc(m_RS.getBoolean("Opc"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
