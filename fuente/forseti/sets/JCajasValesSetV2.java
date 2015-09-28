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

public class JCajasValesSetV2 extends JManejadorSet
{
	public JCajasValesSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_vales ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_vales getRow(int row)
	{
		return (view_vales)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_vales getAbsRow(int row)
	{
		return (view_vales)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_vales pNode = new view_vales();

			pNode.setID_Vale(m_RS.getLong("ID_Vale"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setID_Gasto(m_RS.getString("ID_Gasto"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setProvisional(m_RS.getFloat("Provisional"));
			pNode.setFinal(m_RS.getFloat("Final"));
			pNode.setFactura(m_RS.getFloat("Factura"));
			pNode.setCompra(m_RS.getFloat("Compra"));
			pNode.setPago(m_RS.getFloat("Pago"));
			pNode.setTraspaso(m_RS.getFloat("Traspaso"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setID_Clave(m_RS.getInt("ID_Clave"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
