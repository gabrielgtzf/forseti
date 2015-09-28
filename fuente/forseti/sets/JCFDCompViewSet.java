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

public class JCFDCompViewSet extends JManejadorSet
{
	public JCFDCompViewSet(HttpServletRequest request, String tipo)
	{
		if(tipo.equals("COMPRAS"))
			m_Select = " * FROM VIEW_CFDCOMP";
		else if(tipo.equals("VENTAS"))
			m_Select = " * FROM VIEW_CFDVEN";
		else // Nomina
			m_Select = " * FROM VIEW_CFDNOM";
		
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_CFDCOMP getRow(int row)
	{
		return (VIEW_CFDCOMP)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_CFDCOMP getAbsRow(int row)
	{
		return (VIEW_CFDCOMP)m_Rows.elementAt(row);
	}

	 
	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			VIEW_CFDCOMP pNode = new VIEW_CFDCOMP();

			pNode.setID_CFD(m_RS.getInt("ID_CFD"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setHora(m_RS.getTime("Fecha"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setEfecto(m_RS.getString("Efecto"));
			pNode.setEfectoStr(m_RS.getString("EfectoStr"));
			pNode.setFSI_Tipo(m_RS.getString("FSI_Tipo"));
			pNode.setFSI_ID(m_RS.getInt("FSI_ID"));
			pNode.setEnlace(m_RS.getString("Enlace"));
			pNode.setUUID(m_RS.getString("UUID"));
			pNode.setFechaTimbre(m_RS.getDate("FechaTimbre"));
			pNode.setHoraTimbre(m_RS.getTime("FechaTimbre"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
