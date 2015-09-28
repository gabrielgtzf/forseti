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

public class JProveeCXPSetV2 extends JManejadorSet
{
	public JProveeCXPSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_provee_cxp_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_provee_cxp_modulo getRow(int row)
	{
		return (view_provee_cxp_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_provee_cxp_modulo getAbsRow(int row)
	{
		return (view_provee_cxp_modulo)m_Rows.elementAt(row);
	}
	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_provee_cxp_modulo pNode = new view_provee_cxp_modulo();

			pNode.setClave(m_RS.getLong("Clave"));
			pNode.setID_TipoCP(m_RS.getString("ID_TipoCP"));
			pNode.setID_TipoProvee(m_RS.getString("ID_TipoProvee"));
			pNode.setID_ClaveProvee(m_RS.getInt("ID_ClaveProvee"));
			pNode.setID_Entidad(m_RS.getInt("ID_Entidad"));
			pNode.setPagada(m_RS.getByte("Pagada"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setMonedaSim(m_RS.getString("MonedaSim"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setSaldo(m_RS.getFloat("Saldo"));
			pNode.setVencimiento(m_RS.getDate("Vencimiento"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_Pol(m_RS.getLong("ID_Pol"));
			pNode.setPol(m_RS.getString("Pol"));
			pNode.setID_Concepto(m_RS.getByte("ID_Concepto"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setID_Aplicacion(m_RS.getLong("ID_Aplicacion"));
			pNode.setID_PagoBanCaj(m_RS.getLong("ID_PagoBanCaj"));
			pNode.setPagoBanCaj(m_RS.getString("PagoBanCaj"));
			pNode.setTotalPesos(m_RS.getFloat("TotalPesos"));
			pNode.setSaldoPesos(m_RS.getFloat("SaldoPesos"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
