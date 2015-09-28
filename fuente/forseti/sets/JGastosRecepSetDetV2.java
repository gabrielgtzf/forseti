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

public class JGastosRecepSetDetV2 extends JManejadorSet
{
	public JGastosRecepSetDetV2(HttpServletRequest request, String tipomov)
	{
		if(tipomov.equals("GASTOS"))
			m_Select = " * FROM view_gastos_recepciones_det ";
		else
			m_Select = " * FROM view_gastos_plantillas_det ";
		
		m_PageSize = 50;
		this.request = request;
	}

	public view_gastos_recepciones_det getRow(int row)
	{
		return (view_gastos_recepciones_det)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_gastos_recepciones_det getAbsRow(int row)
	{
		return (view_gastos_recepciones_det)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_gastos_recepciones_det pNode = new view_gastos_recepciones_det();

			pNode.setID_Recepcion(m_RS.getLong("ID_Recepcion"));
			pNode.setPartida(m_RS.getByte("Partida"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));
			pNode.setID_Gasto(m_RS.getString("ID_Gasto"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setPrecio(m_RS.getFloat("Precio"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setImporte(m_RS.getFloat("Importe"));
			pNode.setID_Unidad(m_RS.getString("ID_Unidad"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
