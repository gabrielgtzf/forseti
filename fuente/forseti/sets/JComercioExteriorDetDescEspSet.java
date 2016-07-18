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

public class JComercioExteriorDetDescEspSet extends JManejadorSet
{
	public JComercioExteriorDetDescEspSet(HttpServletRequest request, String tipo)
	{
		if(tipo.equals("COMPRA"))
			m_Select = " * FROM tbl_compras_facturas_comext_det_descesp";
		else if(tipo.equals("VENTA"))
			m_Select = " * FROM tbl_ventas_facturas_comext_det_descesp";
		else // será GASTO
			m_Select = " * FROM tbl_compras_gastos_comext_det_descesp";
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_comercio_exterior_det_descesp getRow(int row)
	{
		return (tbl_comercio_exterior_det_descesp)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_comercio_exterior_det_descesp getAbsRow(int row)
	{
		return (tbl_comercio_exterior_det_descesp)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_comercio_exterior_det_descesp pNode = new tbl_comercio_exterior_det_descesp();

			pNode.setID_VC(m_RS.getInt("ID_VC"));
			pNode.setPartida(m_RS.getInt("Partida"));
			pNode.setDescripcion(m_RS.getInt("Descripcion"));
			pNode.setMarca(m_RS.getString("Marca"));
			pNode.setModelo(m_RS.getString("Modelo"));
			pNode.setSubModelo(m_RS.getString("SubModelo"));
			pNode.setNumeroSerie(m_RS.getString("NumeroSerie"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
