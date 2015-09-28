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

public class JCalculoNominaEspSet extends JManejadorSet
{
	public  JCalculoNominaEspSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_NOM_CALCULO_NOMINA_ESP";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_CALCULO_NOMINA_ESP getRow(int row)
	{
		return (VIEW_CALCULO_NOMINA_ESP)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_CALCULO_NOMINA_ESP getAbsRow(int row)
	{
		return (VIEW_CALCULO_NOMINA_ESP)m_Rows.elementAt(row);
	}
	 
	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			VIEW_CALCULO_NOMINA_ESP pNode = new VIEW_CALCULO_NOMINA_ESP();

			pNode.setID_Nomina(m_RS.getInt("ID_Nomina"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setDias(m_RS.getFloat("Dias"));
			pNode.setFaltas(m_RS.getFloat("Faltas"));
			pNode.setRecibo(m_RS.getInt("Recibo"));
			pNode.setHE(m_RS.getFloat("HE"));
			pNode.setHD(m_RS.getFloat("HD"));
			pNode.setHT(m_RS.getFloat("HT"));
			pNode.setDiasHorasExtras(m_RS.getByte("DiasHorasExtras"));
			pNode.setIXA(m_RS.getFloat("IXA"));
			pNode.setIXE(m_RS.getFloat("IXE"));
			pNode.setIXM(m_RS.getFloat("IXM"));
			pNode.setID_CFD(m_RS.getInt("ID_CFD"));
			pNode.setTFD(m_RS.getByte("TFD"));
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
