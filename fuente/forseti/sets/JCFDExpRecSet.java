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

public class JCFDExpRecSet extends JManejadorSet
{
	String m_Tipo;
	
	public JCFDExpRecSet(HttpServletRequest request, String tipo)
	{
		m_Tipo = tipo;
		if(tipo.equals("EXP"))
			m_Select = " * FROM TBL_CFD_EXPEDICIONES";
		else
			m_Select = " * FROM TBL_CFD_RECEPTORES";

		m_PageSize = 50;
		this.request = request;
	}

	public TBL_CFD_EXPREC getRow(int row)
	{
		return (TBL_CFD_EXPREC)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_CFD_EXPREC getAbsRow(int row)
	{
		return (TBL_CFD_EXPREC)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_CFD_EXPREC pNode = new TBL_CFD_EXPREC();

			if(m_Tipo.equals("EXP"))
				pNode.setCFD_ID_ExpRec(m_RS.getByte("CFD_ID_Expedicion")); 
			else
				pNode.setCFD_ID_ExpRec(m_RS.getByte("CFD_ID_Receptor")); 
			pNode.setCFD_Nombre(m_RS.getString("CFD_Nombre"));
			pNode.setCFD_Calle(m_RS.getString("CFD_Calle"));
			pNode.setCFD_NoExt(m_RS.getString("CFD_NoExt"));
			pNode.setCFD_NoInt(m_RS.getString("CFD_NoInt"));
			pNode.setCFD_Colonia(m_RS.getString("CFD_Colonia"));
			pNode.setCFD_Localidad(m_RS.getString("CFD_Localidad"));
			pNode.setCFD_Municipio(m_RS.getString("CFD_Municipio"));
			pNode.setCFD_Estado(m_RS.getString("CFD_Estado"));
			pNode.setCFD_Pais(m_RS.getString("CFD_Pais"));
			pNode.setCFD_CP(m_RS.getString("CFD_CP"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
