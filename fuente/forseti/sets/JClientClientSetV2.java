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

public class JClientClientSetV2 extends JManejadorSet
{
	public JClientClientSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_client_client_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_client_client_modulo getRow(int row)
	{
		return (view_client_client_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_client_client_modulo getAbsRow(int row)
	{
		return (view_client_client_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_client_client_modulo pNode = new view_client_client_modulo();

			pNode.setCC(m_RS.getString("CC"));
			pNode.setClave(m_RS.getInt("Clave"));
			pNode.setNumero(m_RS.getInt("Numero"));
			pNode.setContacto(m_RS.getString("Contacto"));
			pNode.setDias(m_RS.getInt("Dias"));
			pNode.setEMail(m_RS.getString("EMail"));
			pNode.setID_CC_Comp(m_RS.getString("ID_CC_Comp"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setSaldo(m_RS.getString("Saldo"));
			pNode.setTel(m_RS.getString("Tel"));
			pNode.setID_EntidadVenta(m_RS.getShort("ID_EntidadVenta"));
			pNode.setEntidad(m_RS.getString("Entidad"));
			pNode.setSMTP(m_RS.getByte("SMTP"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
