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

public class JNominasModuloSet extends JManejadorSet
{
	public JNominasModuloSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_nom_nominas_modulo";
		m_PageSize = 50;
		this.request = request;
	}

	public view_nominas_modulo getRow(int row)
	{
		return (view_nominas_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_nominas_modulo getAbsRow(int row)
	{
		return (view_nominas_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_nominas_modulo pNode = new view_nominas_modulo();

			pNode.setID_Nomina(m_RS.getInt("ID_Nomina"));
			pNode.setID_Compania(m_RS.getByte("ID_Compania"));
			pNode.setID_Sucursal(m_RS.getByte("ID_Sucursal"));
			pNode.setCompania(m_RS.getString("Compania"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setTipo_Nomina(m_RS.getString("Tipo_Nomina"));
			pNode.setNumero_Nomina(m_RS.getInt("Numero_Nomina"));
			pNode.setFecha_Desde(m_RS.getDate("Fecha_Desde"));
			pNode.setFecha_Hasta(m_RS.getDate("Fecha_Hasta"));
			pNode.setDias(m_RS.getInt("Dias"));
			pNode.setCerrado(m_RS.getBoolean("Cerrado"));
			pNode.setProteccion(m_RS.getString("Proteccion"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			pNode.setPol(m_RS.getString("Pol"));
			pNode.setFormaPago(m_RS.getString("FormaPago"));
			pNode.setID_Mov(m_RS.getInt("ID_Mov"));
			pNode.setPago(m_RS.getString("Pago"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
