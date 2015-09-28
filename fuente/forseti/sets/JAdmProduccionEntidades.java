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

public class JAdmProduccionEntidades extends JManejadorSet
{
	public JAdmProduccionEntidades(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_PRODUCCION_ENTIDADES";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_PRODUCCION_ENTIDADES getRow(int row)
	{
		return (TBL_PRODUCCION_ENTIDADES)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_PRODUCCION_ENTIDADES getAbsRow(int row)
	{
		return (TBL_PRODUCCION_ENTIDADES)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_PRODUCCION_ENTIDADES pNode = new TBL_PRODUCCION_ENTIDADES();

			pNode.setID_EntidadProd(m_RS.getInt("ID_EntidadProd"));
			pNode.setID_TipoEntidad(m_RS.getShort("ID_TipoEntidad"));
			pNode.setSerie(m_RS.getString("Serie"));
			pNode.setDoc(m_RS.getInt("Doc"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setFormato(m_RS.getString("Formato"));
			pNode.setID_BodegaMP(m_RS.getShort("ID_BodegaMP"));
			pNode.setID_BodegaPT(m_RS.getShort("ID_BodegaPT"));
			pNode.setFija(m_RS.getBoolean("Fija"));
			pNode.setID_Clasificacion(m_RS.getString("ID_Clasificacion"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setNombreBodegaMP(m_RS.getString("NombreBodegaMP"));
			pNode.setNombreBodegaPT(m_RS.getString("NombreBodegaPT"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
