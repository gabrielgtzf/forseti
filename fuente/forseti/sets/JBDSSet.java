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

public class JBDSSet extends JManejadorSet
{
	public JBDSSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_BD";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_BDS getRow(int row)
	{
		return (TBL_BDS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_BDS getAbsRow(int row)
	{
		return (TBL_BDS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_BDS pNode = new TBL_BDS();

			pNode.setID_BD(m_RS.getInt("ID_BD"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setUsuario(m_RS.getString("Usuario"));
			pNode.setPassword(m_RS.getString("Password"));
			pNode.setFechaAlta(m_RS.getDate("FechaAlta"));
			pNode.setCompania(m_RS.getString("Compania"));
			pNode.setDireccion(m_RS.getString("Direccion"));
			pNode.setPoblacion(m_RS.getString("Poblacion"));
			pNode.setCP(m_RS.getString("CP"));
			pNode.setMail(m_RS.getString("Mail"));
			pNode.setWeb(m_RS.getString("Web"));
			pNode.setSU(m_RS.getString("SU"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setCFD(m_RS.getInt("CFD"));
			pNode.setCFD_Calle(m_RS.getString("CFD_Calle"));
			pNode.setCFD_NoExt(m_RS.getString("CFD_NoExt"));
			pNode.setCFD_NoInt(m_RS.getString("CFD_NoInt"));
			pNode.setCFD_Colonia(m_RS.getString("CFD_Colonia"));
			pNode.setCFD_Localidad(m_RS.getString("CFD_Localidad"));
			pNode.setCFD_Municipio(m_RS.getString("CFD_Municipio"));
			pNode.setCFD_Estado(m_RS.getString("CFD_Estado"));
			pNode.setCFD_Pais(m_RS.getString("CFD_Pais"));
			pNode.setCFD_CP(m_RS.getString("CFD_CP"));
			pNode.setCFD_RegimenFiscal(m_RS.getString("CFD_RegimenFiscal"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
