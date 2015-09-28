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

public class JProdEntidadesSetIdsV2 extends JManejadorSet
{
	public JProdEntidadesSetIdsV2(HttpServletRequest request, String usuario, String entidad)
	{
		m_Select = " * FROM view_prod_entidades_ids ";
		String sql = "select * from view_prod_entidades_ids('" + usuario + "','" + entidad + 
				"') as ( " +
				"id_usuario varchar, ID_Entidad smallint, ID_Tipo smallint, Serie varchar, Descripcion varchar, Doc int, Formato varchar, " +
				"ID_BodegaMP smallint, ID_BodegaPT smallint, BodegaMP varchar, BodegaPT varchar, AuditarAlm bit, ManejoStocks smallint )";
		setSQL(sql);
		m_PageSize = 50;
		this.request = request;
	}

	public view_prod_entidades_ids getRow(int row)
	{
		return (view_prod_entidades_ids)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_prod_entidades_ids getAbsRow(int row)
	{
		return (view_prod_entidades_ids)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_prod_entidades_ids pNode = new view_prod_entidades_ids();

			pNode.setID_Entidad(m_RS.getInt("ID_Entidad"));
			pNode.setID_Tipo(m_RS.getByte("ID_Tipo"));
			pNode.setSerie(m_RS.getString("Serie"));
			pNode.setDoc(m_RS.getLong("Doc"));
			pNode.setFormato(m_RS.getString("Formato"));
			pNode.setBodegaMP(m_RS.getString("BodegaMP"));
			pNode.setBodegaPT(m_RS.getString("BodegaPT"));
			pNode.setID_BodegaMP(m_RS.getInt("ID_BodegaMP"));
			pNode.setID_BodegaPT(m_RS.getInt("ID_BodegaPT"));
			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setAuditarAlm(m_RS.getBoolean("AuditarAlm"));
			pNode.setManejoStocks(m_RS.getByte("ManejoStocks"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
